package com.github.terefang.jmelange.commons.http;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.GuidUtil;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.IOUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@Slf4j
@Data
public class HttpClient extends AbstractHttpClient implements HttpClientInterface
{
    public static final int LOG_NONE = -1;
    public static final int LOG_ERROR = 0;
    public static final int LOG_WARN = 1;
    public static final int LOG_INFO = 2;
    public static final int LOG_DEBUG = 3;
    public static final int LOG_TRACE = 4;

    int logLevel = LOG_ERROR;

    public HttpClient() {}

    protected HttpURLConnection openConnection(String url)
            throws IOException
    {
        URLConnection _con = null;
        if(this.getProxyUrl()!=null)
        {
            try
            {
                URI _uri = URI.create(this.getProxyUrl());
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
            if(this.logLevel>=LOG_ERROR)
            {
                log.error("Service URL [" + url + "] is not an HTTP URL");
            }
            throw new IOException("Service URL [" + url + "] is not an HTTP URL");
        }
        return (HttpURLConnection) _con;
    }

    protected void prepareConnection(HttpURLConnection con, String method, String _contentType, String _acceptType, int contentLength)
            throws IOException
    {
        con.setInstanceFollowRedirects(this.isFollowRedirects());

        if(this.getTimeout() > 0)
        {
            con.setConnectTimeout(this.getTimeout());
            con.setReadTimeout(2*this.getTimeout());
            if(this.logLevel>=LOG_DEBUG)
            {
                log.debug("apply Timeout = "+this.getTimeout());
            }
        }

        con.setDoOutput((contentLength > 0) || "PUT".equalsIgnoreCase(method) || "POST".equalsIgnoreCase(method));
        con.setRequestMethod((method == null) ? HTTP_METHOD_POST : method);
        if(this.logLevel>=LOG_DEBUG)
        {
            log.debug("apply method = "+((method == null) ? HTTP_METHOD_POST : method));
        }
        con.setInstanceFollowRedirects(this.isFollowRedirects());
        if(this.logLevel>=LOG_DEBUG)
        {
            log.debug("apply follow redirects = "+this.isFollowRedirects());
        }

        if(_contentType!=null)
        {
            con.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, _contentType);
            if(this.logLevel>=LOG_DEBUG)
            {
                log.debug("apply "+HTTP_HEADER_CONTENT_TYPE+" = "+_contentType);
            }
        }
        else
        if(this.getContentType()!=null)
        {
            con.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, this.getContentType());
            if(this.logLevel>=LOG_DEBUG)
            {
                log.debug("apply "+HTTP_HEADER_CONTENT_TYPE+" = "+this.getContentType());
            }
        }

        if(_acceptType!=null)
        {
            con.setRequestProperty(HTTP_HEADER_ACCEPT, _acceptType);
            if(this.logLevel>=LOG_DEBUG)
            {
                log.debug("apply "+HTTP_HEADER_ACCEPT+" = "+_acceptType);
            }
        }
        else
        if(this.getAcceptType()!=null)
        {
            con.setRequestProperty(HTTP_HEADER_ACCEPT, this.getAcceptType());
            if(this.logLevel>=LOG_DEBUG)
            {
                log.debug("apply "+HTTP_HEADER_ACCEPT+" = "+this.getAcceptType());
            }
        }
        else
        {
            con.setRequestProperty(HTTP_HEADER_ACCEPT, "*/*");
            if(this.logLevel>=LOG_DEBUG)
            {
                log.debug("apply "+HTTP_HEADER_ACCEPT+" = */*");
            }
        }

        con.setRequestProperty(HTTP_HEADER_CONTENT_LENGTH, Integer.toString(contentLength));
        if(this.logLevel>=LOG_DEBUG)
        {
            log.debug("apply "+HTTP_HEADER_CONTENT_LENGTH+" = "+Integer.toString(contentLength));
        }

        if (getAcceptEncoding()!=null) {
            con.setRequestProperty(HTTP_HEADER_ACCEPT_ENCODING, getAcceptEncoding());
            if(this.logLevel>=LOG_DEBUG)
            {
                log.debug("apply "+HTTP_HEADER_ACCEPT_ENCODING+" = "+getAcceptEncoding());
            }
        }

        if (getAcceptCharset()!=null) {
            con.setRequestProperty("Accept-Charset", getAcceptCharset());
            if(this.logLevel>=LOG_DEBUG)
            {
                log.debug("apply Accept-Charset = "+getAcceptCharset());
            }
        }

        if(this.getLoginUsername()!=null)
        {
            con.setRequestProperty("Authorization", "Basic "+ CommonUtil.toBase64(this.getLoginUsername()+":"+ this.getLoginPassword()));
            if(this.logLevel>=LOG_DEBUG)
            {
                log.debug("apply Authorization = Basic "+ CommonUtil.toBase64(this.getLoginUsername()+":"+ this.getLoginPassword()));
            }
        }

        if(con instanceof HttpsURLConnection)
        {
            if(this.getSslCtx() == null)
            {
                try
                {
                    this.setSslCtx(SSLContext.getInstance(this.getSslVersion()));
                    if(this.logLevel>=LOG_DEBUG)
                    {
                        log.debug("apply SSLContext = "+this.getSslVersion());
                    }

                    this.getSslCtx().init(new KeyManager[0], new TrustManager[] { new X509TrustManager()
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
            _sfact.setSslCtx(this.getSslCtx());
            _sfact.setSslProtocols(this.getSslProtocols());
            _sfact.setSslCiphers(this.getSslCiphers());
            _scon.setSSLSocketFactory(_sfact);
        }
        con.setInstanceFollowRedirects(this.isFollowRedirects());
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

    @Override
    public HttpClientResponse executeRequest(String url, String method, String contentType, String acceptType, Map<String, String> header, byte[] data) throws Exception
    {
        if(this.logLevel>=LOG_DEBUG)
        {
            log.debug("executeRequest --> "+method+" "+url);
        }

        HttpURLConnection con = openConnection(url);
        prepareConnection(con, method, contentType, acceptType, data!=null ? data.length : 0);
        if(this.getRequestHeader().size()>0)
        {
            for(Entry<String, String> entry : getRequestHeader().entrySet())
            {
                con.setRequestProperty(entry.getKey(), entry.getValue());
                if(this.logLevel>=LOG_DEBUG)
                {
                    log.debug("apply "+entry.getKey()+" = "+entry.getValue());
                }
            }
        }

        if(this.getCookieJar().size()>0)
        {
            for(String entry : this.getCookieJar())
            {
                con.addRequestProperty("Cookie", entry);
                if(this.logLevel>=LOG_DEBUG)
                {
                    log.debug("apply Cookie = "+entry);
                }
            }
        }

        if(header!=null && header.size()>0)
        {
            for(Entry<String, String> entry : header.entrySet())
            {
                con.setRequestProperty(entry.getKey(), entry.getValue());
                if(this.logLevel>=LOG_DEBUG)
                {
                    log.debug("apply "+entry.getKey()+" = "+entry.getValue());
                }
            }
        }

        if(data!=null && data.length!=0)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(data);
            writeRequestBody(con, baos);
        }

        HttpClientResponse _resp = HttpClientResponse.create();
        try
        {
            con.setInstanceFollowRedirects(this.isFollowRedirects());
            con.connect();

            con.setInstanceFollowRedirects(this.isFollowRedirects());
            readResponseHeader(con, _resp);

            if(this.logLevel>=LOG_DEBUG)
            {
                _resp.getHeader().forEach((k,v)->{
                    log.debug("got "+k+" = "+v);
                });
            }

            InputStream responseBody = readResponseBody(con);
            byte[] ret = IOUtil.toByteArray(responseBody);
            con.disconnect();
            _resp.setObject(ret);
            _resp.setStatus(con.getResponseCode());
            _resp.setMessage(con.getResponseMessage());
            if(this.logLevel>=LOG_TRACE)
            {
                log.trace(CommonUtil.toHexDumo(ret));
            }
        }
        catch(Exception xe)
        {
            con.disconnect();
            _resp.setObject(null);
            _resp.setStatus(con.getResponseCode());
            _resp.setMessage(con.getResponseMessage());
        }
        if(this.logLevel>=LOG_DEBUG)
        {
            log.debug("got status = "+_resp.getStatus());
            log.debug("got message = "+_resp.getMessage());
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

    @Override
    public HttpClientResponse postForm(String url, String type, Map data) throws Exception
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

    @Override
    public HttpClientResponse postMultipartForm(String url, String type, Map data) throws Exception
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

    @SneakyThrows
    public static final boolean fetchToFile(String _url, File _file)
    {
        HttpOkClient _hc = new HttpOkClient();
        _hc.setFollowRedirects(true);
        HttpClientResponse _resp = _hc.getRequest(_url, "*/*");
        if(_resp.getStatus()<200)
        {
            Files.write(Paths.get(_file.getAbsolutePath()), _resp.getAsBytes());
            return true;
        }
        return false;
    }



    public static void main(String[] args)
            throws Exception
    {
        HttpClient hc = new HttpClient();
        hc.setProxyUrl("http://proxy.it-austria.com:8080/");
        hc.getSslProtocols().add("TLSv1.2");
        hc.getSslCiphers().add("TLS_RSA_WITH_AES_256_CBC_SHA256");

        HttpClientResponse res = hc.executeRequest("https://10.24.33.237:8443/", "GET", null, null,"");
        System.out.println(res.getAsString());
    }
}
