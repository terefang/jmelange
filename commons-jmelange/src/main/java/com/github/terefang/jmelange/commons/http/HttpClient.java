package com.github.terefang.jmelange.commons.http;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.GuidUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.IOUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;

import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@Slf4j
@Data
public class HttpClient
{
    @Data
    public static class HttpClientSSLSocketFactory
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

    protected static final String HTTP_METHOD_POST = "POST";
    protected static final String HTTP_HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    protected static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    protected static final String HTTP_HEADER_ACCEPT = "Accept";
    protected static final String HTTP_HEADER_CONTENT_ENCODING = "Content-Encoding";
    protected static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    protected static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
    protected static final String ENCODING_GZIP = "gzip";
    protected static final String TLS_VERSION_SSLv3 = "SSLv3";
    protected static final String TLS_VERSION_10 = "TLSv1.0";
    protected static final String TLS_VERSION_11 = "TLSv1.1";
    protected static final String TLS_VERSION_12 = "TLSv1.2";
    protected static final String TLS_VERSION_13 = "TLSv1.3";

    public HttpClient() {}

    private SSLContext sslCtx = null;
    private String proxyUrl = null;
    private String acceptEncoding = null;
    private String acceptCharset = "utf-8";
    private String credential = null;
    private String contentType = null;
    private String acceptType = null;
    private Map<String, String> requestHeader = new HashMap();
    private String sslVersion = TLS_VERSION_12;
    private Set<String> sslProtocols = new HashSet();
    private Set<String> sslCiphers = new HashSet();
    private List<String> cookieJar = new Vector<>();
    private int timeout = 500;
    private boolean followRedirects;

    public String getAcceptCharset() {
        return acceptCharset;
    }

    public void setAcceptCharset(String acceptCharset) {
        this.acceptCharset = acceptCharset;
    }

    public String getSslVersion()
    {
        return sslVersion;
    }

    public void setSslVersion(String sslVersion) {
        this.sslVersion = sslVersion;
    }

    public List<String> getCookieJar() {
        return cookieJar;
    }

    public void setCookieJar(List<String> cookieJar) {
        this.cookieJar = cookieJar;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public String getCredential()
    {
        return credential;
    }

    public void setCredential(String credential)
    {
        this.credential = credential;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getAcceptType()
    {
        return acceptType;
    }

    public void setAcceptType(String acceptType)
    {
        this.acceptType = acceptType;
    }

    public Map<String, String> getRequestHeader()
    {
        return requestHeader;
    }

    public void setRequestHeader(Map<String, String> requestHeader)
    {
        this.requestHeader = requestHeader;
    }

    public void addRequestHeader(String _name, String _value)
    {
        if(this.requestHeader == null)
        {
            this.requestHeader = new HashMap<>();
        }
        this.requestHeader.put(_name, _value);
    }

    protected HttpURLConnection openConnection(String url)
            throws IOException
    {
        URLConnection _con = null;
        if(this.proxyUrl!=null)
        {
            try
            {
                URI _uri = URI.create(this.proxyUrl);
                if(_uri.getScheme().startsWith("http"))
                {
                    _con = new URL(url).openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(_uri.getHost(), _uri.getPort())));
                }
                else
                if(_uri.getScheme().startsWith("socks"))
                {
                    _con = new URL(url).openConnection(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(_uri.getHost(), _uri.getPort())));
                }
            }
            catch(Exception xe)
            {
                _con = new URL(url).openConnection();
            }
        }
        else
        {
            _con = new URL(url).openConnection();
        }

        if (!(_con instanceof HttpURLConnection)) {
            throw new IOException("Service URL [" + url + "] is not an HTTP URL");
        }
        return (HttpURLConnection) _con;
    }

    protected void prepareConnection(HttpURLConnection con, String method, String _contentType, String _acceptType, int contentLength)
            throws IOException
    {
        con.setInstanceFollowRedirects(this.followRedirects);

        if(this.timeout > 0)
        {
            con.setConnectTimeout(this.timeout);
            con.setReadTimeout(2*this.timeout);
        }

        con.setDoOutput((contentLength > 0) || "PUT".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method));
        con.setRequestMethod((method == null) ? HTTP_METHOD_POST : method);
        con.setInstanceFollowRedirects(this.followRedirects);

        if(_contentType!=null)
        {
            con.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, _contentType);
        }
        else
        if(this.contentType!=null)
        {
            con.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, this.contentType);
        }

        if(_acceptType!=null)
        {
            con.setRequestProperty(HTTP_HEADER_ACCEPT, _acceptType);
        }
        else
        if(this.acceptType!=null)
        {
            con.setRequestProperty(HTTP_HEADER_ACCEPT, this.acceptType);
        }
        else
        {
            con.setRequestProperty(HTTP_HEADER_ACCEPT, "*/*");
        }

        con.setRequestProperty(HTTP_HEADER_CONTENT_LENGTH, Integer.toString(contentLength));

        if (getAcceptEncoding()!=null) {
            con.setRequestProperty(HTTP_HEADER_ACCEPT_ENCODING, getAcceptEncoding());
        }

        if (getAcceptCharset()!=null) {
            con.setRequestProperty("Accept-Charset", getAcceptCharset());
        }

        if(this.credential!=null)
        {
            con.setRequestProperty("Authorization", "Basic "+ CommonUtil.toBase64(credential));
        }

        if(con instanceof HttpsURLConnection)
        {
            if(this.sslCtx == null)
            {
                try
                {
                    this.sslCtx = SSLContext.getInstance(this.sslVersion);

                    this.sslCtx.init(new KeyManager[0], new TrustManager[] { new X509TrustManager()
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
                }
                catch (Exception e)
                {
                    log.warn(e.getMessage(), e);
                }
            }

            HttpsURLConnection _scon = (HttpsURLConnection)con;
            _scon.setHostnameVerifier(new HostnameVerifier()
            {
                public boolean verify(String arg0, SSLSession arg1)
                {
                    return true;
                }
            });

            HttpClientSSLSocketFactory _sfact = new HttpClientSSLSocketFactory();
            _sfact.setSslCtx(this.sslCtx);
            _sfact.setSslProtocols(this.sslProtocols);
            _sfact.setSslCiphers(this.sslCiphers);
            _scon.setSSLSocketFactory(_sfact);
        }
        con.setInstanceFollowRedirects(this.followRedirects);
    }

    protected void writeRequestBody(HttpURLConnection con, ByteArrayOutputStream baos)
            throws IOException
    {
        if(baos!=null)
        {
            baos.writeTo(con.getOutputStream());
        }
        con.getOutputStream().flush();
        con.getOutputStream().close();
    }

    protected InputStream readResponseBody(HttpURLConnection con)
            throws IOException
    {

        if (isGzipResponse(con)) {
            // GZIP response found - need to unzip.
            return new GZIPInputStream(con.getInputStream());
        }
        else {
            // Plain response found.
            return con.getInputStream();
        }
    }

    protected boolean isGzipResponse(HttpURLConnection con)
    {
        String encodingHeader = con.getHeaderField(HTTP_HEADER_CONTENT_ENCODING);
        return (encodingHeader != null && encodingHeader.toLowerCase().indexOf(ENCODING_GZIP) != -1);
    }

    public void setLoginCredential(String name, String credential) {
        this.credential = name+":"+credential;
    }

    public HttpClientResponse<byte[]> executeRequest(String url, String method, String contentType, String acceptType, byte[] data) throws Exception
    {
        return this.executeRequest(url, method, contentType, acceptType, null, data);
    }

    public HttpClientResponse<String> executeRequest(String url, String method, String contentType, String acceptType, String data) throws Exception
    {
        return this.executeRequest(url, method, contentType, acceptType, null, data);
    }

    public HttpClientResponse<byte[]> executeRequest(String url, String method, String contentType, byte[] data) throws Exception
    {
        return this.executeRequest(url, method, contentType, null, null, data);
    }

    public HttpClientResponse<String> executeRequest(String url, String method, String contentType, String data) throws Exception
    {
        return this.executeRequest(url, method, contentType, null, null, data);
    }

    public HttpClientResponse<byte[]> executeRequest(String url, String method, byte[] data) throws Exception
    {
        return this.executeRequest(url, method, null, null, null, data);
    }

    public HttpClientResponse<String> executeRequest(String url, String method, String data) throws Exception
    {
        return this.executeRequest(url, method, null, null, null, data);
    }

    public HttpClientResponse<byte[]> executeRequest(String url, byte[] data) throws Exception
    {
        return this.executeRequest(url, "POST", null, null, null, data);
    }

    public HttpClientResponse<String> executeRequest(String url, String data) throws Exception
    {
        return this.executeRequest(url, "POST", null, null, null, data);
    }

    public HttpClientResponse<String> executeRequest(String url, String method, String contentType, String acceptType,  Map<String, String> header, String data) throws Exception
    {
        HttpClientResponse rsp = executeRequest(url, method, contentType, acceptType, header, data==null ? null : data.getBytes());
        String c = new String((byte[])rsp.get());
        rsp.setObject(c);
        return rsp;
    }

    public HttpClientResponse<byte[]> executeRequest(String url, String method, String contentType, String acceptType, Map<String, String> header, byte[] data) throws Exception
    {

        HttpURLConnection con = openConnection(url);
        prepareConnection(con, method, contentType, acceptType, data!=null ? data.length : 0);
        if(this.requestHeader.size()>0)
        {
            for(Entry<String, String> entry : requestHeader.entrySet())
            {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        if(this.cookieJar.size()>0)
        {
            for(String entry : this.cookieJar)
            {
                con.addRequestProperty("Cookie", entry);
            }
        }

        if(header!=null && header.size()>0)
        {
            for(Entry<String, String> entry : header.entrySet())
            {
                con.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        if(data!=null && data.length!=0)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(data);
            writeRequestBody(con, baos);
        }

        con.setInstanceFollowRedirects(this.followRedirects);
        con.connect();

        HttpClientResponse _resp = HttpClientResponse.create();
        con.setInstanceFollowRedirects(this.followRedirects);
        readResponseHeader(con, _resp);
        try
        {
            InputStream responseBody = readResponseBody(con);
            byte[] ret = IOUtil.toByteArray(responseBody);
            con.disconnect();
            _resp.setObject(ret);
            _resp.setStatus(con.getResponseCode());
            _resp.setMessage(con.getResponseMessage());
        }
        catch(Exception xe)
        {
            con.disconnect();
            _resp.setObject(null);
            _resp.setStatus(con.getResponseCode());
            _resp.setMessage(con.getResponseMessage());
        }
        return _resp;
    }

    private Map<String, String> readResponseHeader(HttpURLConnection con, HttpClientResponse _resp)
    {
        Map<String, String> _h = new HashMap<>();
        _resp.setContentEncoding(con.getContentEncoding());
        _resp.setContentType(con.getContentType());

        List<String> _c = new Vector<>();
        con.getHeaderFields().forEach((k,v) -> {
            _h.put(k, v.get(0));
            if("set-cookie".equalsIgnoreCase(k))
            {
                _c.addAll(v);
            }
        });

        if(_c.size()>0 && (_resp!=null))
        {
            _resp.setCookieJar(_c);
        }
        if(_resp!=null) _resp.setHeader(_h);
        return _h;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public Set<String> getSslProtocols() {
        return sslProtocols;
    }

    public void setSslProtocols(Set<String> sslProtocols) {
        this.sslProtocols = sslProtocols;
    }

    public Set<String> getSslCiphers() {
        return sslCiphers;
    }

    public void setSslCiphers(Set<String> sslCiphers) {
        this.sslCiphers = sslCiphers;
    }



    public HttpClientResponse<byte[]> getRequest(String url, String acceptType, Map<String, String> header) throws Exception
    {
        return this.executeRequest(url, "GET", null, acceptType, header, (byte[])null);
    }

    public HttpClientResponse<String> getRequestString(String url, String acceptType, Map<String, String> header) throws Exception
    {
        return this.executeRequest(url, "GET", null, acceptType, header, (String)null);
    }

    public HttpClientResponse<byte[]> getRequest(String url, String acceptType) throws Exception
    {
        return this.executeRequest(url, "GET", null, acceptType, null, (byte[])null);
    }

    public HttpClientResponse<String> getRequestString(String url, String acceptType) throws Exception
    {
        return this.executeRequest(url, "GET", null, acceptType, null, (String)null);
    }

    public HttpClientResponse<byte[]> getRequest(String url) throws Exception
    {
        return this.executeRequest(url, "GET", null, null, null, (byte[])null);
    }

    public HttpClientResponse<String> getRequestString(String url) throws Exception
    {
        return this.executeRequest(url, "GET", null, null, null, (String)null);
    }

    public HttpClientResponse<String> postRequest(String url, String type, String data) throws Exception
    {
        return this.executeRequest(url, "POST", type, type, null, data);
    }

    public HttpClientResponse<byte[]> postRequest(String url, String type, byte[] data) throws Exception
    {
        return this.executeRequest(url, "POST", type, type, null, data);
    }

    public HttpClientResponse<byte[]> postForm(String url, String type, Map data) throws Exception
    {
        StringBuilder _sb = new StringBuilder();

        for(Object _k : data.keySet())
        {
            _sb.append(_k.toString());
            _sb.append("=");
            _sb.append(URLEncoder.encode(data.get(_k).toString(), StandardCharsets.UTF_8.toString()));
            _sb.append("&");
        }

        return this.executeRequest(url, "POST", "application/x-www-form-urlencoded", type, null, _sb.toString().getBytes());
    }

    public HttpClientResponse<byte[]> postMultipartForm(String url, String type, Map data) throws Exception
    {
        String _boundary = "-----"+GuidUtil.randomUUID();

        StringBuilder _sb = new StringBuilder();

        for(Object _k : data.keySet())
        {
            _sb.append("--");
            _sb.append(_boundary);
            _sb.append("\r\n");
            _sb.append("Content-Type: text/plain; charset=utf-8");
            _sb.append("\r\n");
            _sb.append("Content-Disposition: form-data; name=\"");
            _sb.append(_k.toString());
            _sb.append("\"");
            _sb.append("\r\n");
            _sb.append("\r\n");
            _sb.append(data.get(_k).toString());
            _sb.append("\r\n");
        }
        _sb.append("--");
        _sb.append(_boundary);
        _sb.append("--");
        _sb.append("\r\n");

        return this.executeRequest(url, "POST", "multipart/form-data; boundary="+_boundary, type, null, _sb.toString().getBytes());
    }

    public HttpClientResponse<String> postMultipartFormString(String url, String type, Map data) throws Exception
    {
        String _boundary = "-----"+GuidUtil.randomUUID();

        StringBuilder _sb = new StringBuilder();

        for(Object _k : data.keySet())
        {
            _sb.append("--");
            _sb.append(_boundary);
            _sb.append("\r\n");
            _sb.append("Content-Type: text/plain; charset=utf-8");
            _sb.append("\r\n");
            _sb.append("Content-Disposition: form-data; name=\"");
            _sb.append(_k.toString());
            _sb.append("\"");
            _sb.append("\r\n");
            _sb.append("\r\n");
            _sb.append(data.get(_k).toString());
            _sb.append("\r\n");
        }
        _sb.append("--");
        _sb.append(_boundary);
        _sb.append("--");
        _sb.append("\r\n");

        return this.executeRequest(url, "POST", "multipart/form-data; boundary="+_boundary, type, null, _sb.toString());
    }

    public HttpClientResponse<String> postFormString(String url, String type, Map data) throws Exception
    {
        StringBuilder _sb = new StringBuilder();

        for(Object _k : data.keySet())
        {
            _sb.append(_k.toString());
            _sb.append("=");
            _sb.append(URLEncoder.encode(data.get(_k).toString(), StandardCharsets.UTF_8.toString()));
            _sb.append("&");
        }

        return this.executeRequest(url, "POST", "application/x-www-form-urlencoded", type, null, _sb.toString());
    }



    public static void main(String[] args)
            throws Exception
    {
        HttpClient hc = new HttpClient();
        hc.setProxyUrl("http://proxy.it-austria.com:8080/");
        hc.getSslProtocols().add("TLSv1.2");
        hc.getSslCiphers().add("TLS_RSA_WITH_AES_256_CBC_SHA256");

        HttpClientResponse<String> res = hc.executeRequest("https://10.24.33.237:8443/", "GET", null, null,"");
        System.out.println(res.get());
    }
}
