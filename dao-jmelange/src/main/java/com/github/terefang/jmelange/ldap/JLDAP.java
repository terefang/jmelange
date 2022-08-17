package com.github.terefang.jmelange.ldap;

import com.unboundid.ldap.sdk.*;
import lombok.Data;
import lombok.SneakyThrows;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

@Data
public class JLDAP
{
    public static JLDAP create(String _url, String _user, String _pwd)
    {
        JLDAP _l = create(_url);
        if(_l.bind(_user, _pwd))
        {
            return _l;
        }
        return null;
    }

    public static JLDAP create(String _url)
    {
        JLDAP _l = new JLDAP();
        _l.connect(_url);
        return _l;
    }

    @SneakyThrows
    public static SSLContext createSslContext(String sslVersion)
    {
        SSLContext sslCtx = SSLContext.getInstance(sslVersion);

        sslCtx.init(new KeyManager[0], new TrustManager[] { new X509TrustManager()
        {
            public X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException
            {
                // TODO Auto-generated method stub
            }

            public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException
            {
                // TODO Auto-generated method stub
            }
        }}, null);
        return sslCtx;
    }

    @Data
    public static class LdapClientSSLSocketFactory
            extends SSLSocketFactory
    {
        SSLContext sslCtx = null;
        Set<String> sslProtocols = new HashSet();
        Set<String> sslCiphers = new HashSet();

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
                throws IOException
        {
            SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(address, port, localAddress, localPort);
            setSocketOptions(socket);
            return socket;
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
                throws IOException, UnknownHostException
        {
            SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(host, port, localHost, localPort);
            setSocketOptions(socket);
            return socket;
        }

        @Override
        public Socket createSocket(InetAddress host, int port)
                throws IOException
        {
            SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(host, port);
            setSocketOptions(socket);
            return socket;
        }

        @Override
        public Socket createSocket(String host, int port)
                throws IOException,     UnknownHostException
        {
            SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(host, port);
            setSocketOptions(socket);
            return socket;
        }

        @Override
        public String[] getSupportedCipherSuites()
        {
            return sslCiphers.toArray(new String[0]);
        }

        @Override
        public String[] getDefaultCipherSuites()
        {
            return sslCiphers.toArray(new String[0]);
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose)
                throws IOException
        {
            SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(s, host, port, autoClose);
            setSocketOptions(socket);
            return socket;
        }

        void setSocketOptions(SSLSocket socket)
        {
            if(!sslProtocols.isEmpty())
            {
                socket.setEnabledProtocols(sslProtocols.toArray(new String[0]));
            }

            if(!sslCiphers.isEmpty())
            {
                socket.setEnabledCipherSuites(sslCiphers.toArray(new String[0]));
            }
        }
    }

    int timeout = 5000;
    LDAPConnection connection;

    @SneakyThrows
    public void connect(String _url, int _timeout)
    {
        URI _uri = URI.create(_url);
        if(_uri.getScheme().equalsIgnoreCase("ldaps"))
        {
            LdapClientSSLSocketFactory _f = new LdapClientSSLSocketFactory();
            _f.setSslCtx(createSslContext("TLSv1.2"));
            this.connection = new LDAPConnection(_f);
        }
        else
        {
            this.connection = new LDAPConnection();
        }
        this.connection.connect(_uri.getHost(), _uri.getPort(), _timeout);
    }

    public void connect(String _url)
    {
        connect(_url, this.timeout);
    }

    @SneakyThrows
    public boolean bind(String _user, String _pass)
    {
        BindResult _res = this.connection.bind(_user, _pass);
        return _res.getResultCode() == ResultCode.SUCCESS;
    }

    @SneakyThrows
    public List<Map<String, String[]>> search(String baseDN, String _filter, String... attributes)
    {
        return convert(connection.search(baseDN, SearchScope.SUB, _filter, attributes));
    }

    @SneakyThrows
    public List<Map<String, String[]>> search(String baseDN, String _filter)
    {
        return convert(connection.search(baseDN, SearchScope.SUB, _filter));
    }

    @SneakyThrows
    public Map<String, String[]> get(String dn, String... attributes)
    {
        return convert(this.connection.getEntry(dn, attributes));
    }

    @SneakyThrows
    public Map<String, String[]> get(String dn)
    {
        return convert(this.connection.getEntry(dn));
    }

    static List<Map<String, String[]>> convert(SearchResult _entries)
    {
        if(_entries==null) return null;
        List<Map<String, String[]>> _ret = new Vector<>();
        for(SearchResultEntry _e : _entries.getSearchEntries())
        {
            _ret.add(convert(_e));
        }
        return _ret;
    }

    static Map<String, String[]> convert(SearchResultEntry _entry)
    {
        if(_entry==null) return null;
        Map<String, String[]> _ret = new HashMap<>();
        _ret.put("DN", new String[] { _entry.getDN() });
        for(Attribute _a : _entry.getAttributes())
        {
            _ret.put(_a.getName(), _a.getValues());
        }
        return _ret;
    }
}
