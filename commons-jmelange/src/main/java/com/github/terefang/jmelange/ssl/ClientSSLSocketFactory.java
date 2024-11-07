package com.github.terefang.jmelange.ssl;

import lombok.Data;
import lombok.SneakyThrows;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

@Data
public class ClientSSLSocketFactory
        extends SSLSocketFactory
{
    SSLContext sslCtx = null;
    Set<String> sslProtocols = new HashSet();
    Set<String> sslCiphers = new HashSet();
    
    @SneakyThrows
    public static ClientSSLSocketFactory create() {
        ClientSSLSocketFactory _csf = new ClientSSLSocketFactory();
        return _csf;
    }
    
    static String[] _PROTOCOLS = { "TLSv1.1", "TLSv1.3", "TLSv1.2" };
    
    @SneakyThrows
    public ClientSSLSocketFactory()
    {
        super();
        for(String _p : _PROTOCOLS)
        {
            Security.setProperty("jdk.tls.disabledAlgorithms",
                    Security.getProperty("jdk.tls.disabledAlgorithms")
                            .replace(_p+", ", ""));
            this.sslProtocols.add(_p);
        }
        this.sslCtx = SSLContext.getInstance(_PROTOCOLS[0]);
        this.sslCiphers.add("TLS_RSA_WITH_AES_256_CBC_SHA256");
        for(String _cs : ((SSLServerSocketFactory)SSLServerSocketFactory.getDefault()).getSupportedCipherSuites())
        {
            this.sslCiphers.add(_cs);
        }
        this.sslCtx.init(new KeyManager[0], new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            
            public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException
            {
                // TODO Auto-generated method stub
            }
            
            public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException {
                // TODO Auto-generated method stub
            }
        }}, null);
    }
    
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
            throws IOException {
        SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(host, port);
        setSocketOptions(socket);
        return socket;
    }
    
    @Override
    public Socket createSocket(String host, int port)
            throws IOException, UnknownHostException {
        SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(host, port);
        setSocketOptions(socket);
        return socket;
    }
    
    @Override
    public String[] getSupportedCipherSuites() {
        return sslCiphers.toArray(new String[0]);
    }
    
    @Override
    public String[] getDefaultCipherSuites() {
        return sslCiphers.toArray(new String[0]);
    }
    
    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose)
            throws IOException {
        SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(s, host, port, autoClose);
        setSocketOptions(socket);
        return socket;
    }
    
    void setSocketOptions(SSLSocket socket)
    {
        SSLParameters _p = new SSLParameters(sslCiphers.toArray(new String[0]), sslProtocols.toArray(new String[0]));
        socket.setSSLParameters(_p);
    }
}