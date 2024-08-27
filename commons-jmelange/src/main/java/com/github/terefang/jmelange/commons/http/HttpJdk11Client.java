package com.github.terefang.jmelange.commons.http;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.util.ListMapUtil;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
@Data
public class HttpJdk11Client
        extends AbstractHttpClient implements HttpClientInterface
{
    private MemoryCookieJar _jar;

    public HttpJdk11Client() {
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification","true");
    }



    protected HttpRequest.Builder prepareConnection(HttpRequest.Builder _con, String method, Map<String, String> header, String _contentType, String _acceptType)
            throws IOException
    {
        if(this.getRequestHeader().size()>0)
        {
            for(Entry<String, String> entry : getRequestHeader().entrySet())
            {
                _con = _con.header(entry.getKey(), entry.getValue());
            }
        }

        if(header!=null && header.size()>0)
        {
            for(Entry<String, String> entry : header.entrySet())
            {
                _con = _con.header(entry.getKey(), entry.getValue());
            }
        }

        if(_acceptType!=null)
        {
            _con = _con.header(HTTP_HEADER_ACCEPT, _acceptType);
        }
        else
        if(this.getAcceptType()!=null)
        {
            _con = _con.header(HTTP_HEADER_ACCEPT, this.getAcceptType());
        }
        else
        {
            _con = _con.header(HTTP_HEADER_ACCEPT, "*/*");
        }


        if (getAcceptEncoding()!=null)
        {
            _con = _con.header(HTTP_HEADER_ACCEPT_ENCODING, getAcceptEncoding());
        }

        if (getAcceptCharset()!=null) {
            _con = _con.header("Accept-Charset", getAcceptCharset());
        }

        if(this.getLoginUsername()!=null)
        {
            _con = _con.header("Authorization", "Basic "+ CommonUtil.toBase64(this.getLoginUsername()+":"+ this.getLoginPassword()));
        }

        if(this.getCookieJar()!=null && this.getCookieJar().size()>0)
        {
            for(String _c : this.getCookieJar())
            {
                this._jar.add(_c);
            }
        }

        return _con;
    }

    private Map<String, String> readResponseHeader(HttpResponse _rsp, HttpClientResponse _resp)
    {
        Map<String, String> _h = new HashMap<>();
        if(_rsp.headers().firstValue(HTTP_HEADER_CONTENT_ENCODING).isPresent())
        {
            _resp.setContentEncoding(_rsp.headers().firstValue(HTTP_HEADER_CONTENT_ENCODING).get());
        }
        
        if(_rsp.headers().firstValue(HTTP_HEADER_CONTENT_TYPE).isPresent())
        {
            _resp.setContentType(_rsp.headers().firstValue(HTTP_HEADER_CONTENT_TYPE).get());
        }

        List<String> _c = new Vector<>();
        for(Entry<String, List<String>> _header : _rsp.headers().map().entrySet())
        {
            _h.put(_header.getKey(), _header.getValue().get(0));
            if("set-cookie".equalsIgnoreCase(_header.getKey()))
            {
                for(String _cl : _header.getValue())
                {
                    _c.add(_cl);
                    this._jar.add(_cl);
                }
            }
        }

        if(_c.size()>0 && (_resp!=null))
        {
            _resp.setCookieJar(_c);
        }
        if(_resp!=null) _resp.setHeader(_h);
        return _h;
    }

    @Override
    public HttpClientResponse<byte[]> executeRequest(String url, String method, String contentType, String acceptType, Map<String, String> header, byte[] _data) throws Exception
    {
        return executeRequest(url, method, acceptType, header, contentType, _data);
    }

    static X509TrustManager _anyTM = new X509TrustManager()
    {
        public X509Certificate[] getAcceptedIssuers()
        {
            return new X509Certificate[0];
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
    };

    public HttpClientResponse<byte[]> executeRequest(String url, String method, String acceptType, Map<String, String> header, String reqContentType, byte[] reqBody) throws Exception
    {
        if(this._jar == null)
        {
            this._jar = MemoryCookieJar.create();
        }

        if(this.getSslCtx() == null)
        {
            try
            {
                this.setSslCtx(SSLContext.getInstance(this.getSslVersion()));

                this.getSslCtx().init(new KeyManager[0], new TrustManager[] { _anyTM }, null);
            }
            catch (Exception e)
            {
                log.warn(e.getMessage(), e);
            }
        }
        
        HttpClient.Builder _builder = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(this.isFollowRedirects() ? HttpClient.Redirect.ALWAYS : HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofMillis(this.getTimeout()))
            .cookieHandler(this._jar)
            .sslContext(this.getSslCtx());
        
        if(this.getProxyUrl()!=null)
        {
            URI _uri = URI.create(this.getProxyUrl());
            _builder = _builder.proxy(ProxySelector.of(new InetSocketAddress(_uri.getHost(), _uri.getPort())));
        }

        if(this.getLoginUsername()!=null)
        {
            _builder = _builder.authenticator(new Authenticator()
            {
                @Override
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(HttpJdk11Client.this.getLoginUsername(), HttpJdk11Client.this.getLoginPassword()
                            .toCharArray());
                }
            });
        }
        
        HttpClient _client = _builder.build();
        
        HttpRequest.Builder _rb = HttpRequest.newBuilder();
        _rb = _rb.timeout(Duration.ofMillis(this.getTimeout()*4));
        _rb = _rb.uri(URI.create(url));
        
        _rb = prepareConnection(_rb, method, header, reqContentType, acceptType);

        if("GET".equalsIgnoreCase(method))
        {
            _rb = _rb.GET();
        }
        else
        if("POST".equalsIgnoreCase(method))
        {
            _rb = _rb.POST(HttpRequest.BodyPublishers.ofByteArray(reqBody));
            _rb = _rb.header(HTTP_HEADER_CONTENT_TYPE,reqContentType);
        }
        else
        if("PUT".equalsIgnoreCase(method))
        {
            _rb = _rb.PUT(HttpRequest.BodyPublishers.ofByteArray(reqBody));
            _rb = _rb.header(HTTP_HEADER_CONTENT_TYPE,reqContentType);
        }
        else
        if("DELETE".equalsIgnoreCase(method))
        {
            _rb = _rb.DELETE();
        }
        else
        {
            _rb = _rb.method("HEAD", HttpRequest.BodyPublishers.noBody());
        }

        HttpClientResponse _resp = HttpClientResponse.create();
        HttpResponse<byte[]> _rsp = null;
        try
        {
            HttpRequest _req = _rb.build();
            _rsp = _client.send(_req,HttpResponse.BodyHandlers.ofByteArray());

            readResponseHeader(_rsp, _resp);
            _resp.setObject(_rsp.body());
            _resp.setStatus(_rsp.statusCode());
            //_resp.setMessage(_rsp.headers()....);
        }
        catch(Exception _xe)
        {
            if(_rsp==null)
            {
                _resp.setStatus(599);
                _resp.setMessage(_xe.getMessage());
                _resp.setException(_xe);
            }
            else
            {
                _resp.setStatus(_rsp.statusCode());
                //_resp.setMessage(_rsp.headers()....);
            }
            _resp.setObject(null);
        }
        return _resp;
    }

    @Override
    public HttpClientResponse<byte[]> postForm(String url, String type, Map data) throws Exception
    {
        StringBuilder _sb = new StringBuilder();
        
        boolean _first = true;

        for(Object _k : data.keySet())
        {
            if(!_first)
            {
                _sb.append("&");
            }
            _sb.append(_k.toString());
            _sb.append("=");
            _sb.append(URLEncoder.encode(data.get(_k).toString(), StandardCharsets.UTF_8));
            _first = false;
        }
        
        return this.executeRequest(url, "POST", type, (Map)null, "application/x-www-form-urlencoded", _sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public HttpClientResponse<byte[]> postMultipartForm(String url, String type, Map _data) throws Exception
    {
        Map<String, String> data = _data;
        
        MimeMultipart _mp = new MimeMultipart("mixed");

        for (Entry<String, String> _e : data.entrySet())
        {
            MimeBodyPart _mbp = new MimeBodyPart();
            _mbp.setDisposition("name=\""+_e.getKey()+"\"");
            _mbp.setText(_e.getValue());
            _mp.addBodyPart(_mbp);
        }
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        _mp.writeTo(_baos);
        return this.executeRequest(url, "POST", type, (Map)null, "multipart/mixed", _baos.toByteArray());
    }

    public static final boolean fetchToFile(String _url, String _file, boolean _follow, int _to)
    {
        return fetchToFile(_url, Paths.get(_file).toFile(), _follow, _to);
    }

    public static final boolean fetchToFile(String _url, String _file)
    {
        return fetchToFile(_url, Paths.get(_file).toFile());
    }

    @SneakyThrows
    public static final boolean fetchToFile(String _url, File _file)
    {
        return fetchToFile(_url, _file, true, 600000);
    }

    @SneakyThrows
    public static final boolean fetchToFile(String _url, File _file, boolean _follow, int _to)
    {
        HttpJdk11Client _hc = new HttpJdk11Client();
        _hc.setFollowRedirects(_follow);
        _hc.setTimeout(_to);
        HttpClientResponse _resp = _hc.getRequest(_url, "*/*");
        if(_resp.getStatus()<300)
        {
            Files.write(Paths.get(_file.getAbsolutePath()), _resp.getAsBytes());
            return true;
        }
        return false;
    }

    @SneakyThrows
    public static final String fetchToString(String _url)
    {
        return fetchToString(_url, true, 60000);
    }

    @SneakyThrows
    public static final String fetchToString(String _url, boolean _follow, int _to)
    {
        HttpJdk11Client _hc = new HttpJdk11Client();
        _hc.setFollowRedirects(_follow);
        _hc.setTimeout(_to);
        HttpClientResponse _resp = _hc.getRequest(_url, "*/*");
        if(_resp.getStatus()<300)
        {
            return _resp.getAsString();
        }
        else if(_resp.getStatus()==599)
        {
            _resp.getException().printStackTrace(System.err);
        }
        return null;
    }

    @SneakyThrows
    public static void main(String[] args)
    {
        //System.err.println(HttpJdk11Client.fetchToString("https://www.google.com/"));
        HttpJdk11Client _c = new HttpJdk11Client();
        HttpClientResponse<byte[]> _r = _c.postMultipartForm("https://www.google.com/", null, ListMapUtil.toMap("q", "test"));
        System.err.println(_r.getAsString());
    }

    public static class MemoryCookieJar extends CookieHandler
    {
        public static MemoryCookieJar create() { return new MemoryCookieJar(); }

        Map<String, HttpCookie> _cookies = new HashMap<>();

        public void add(String _k, String _v)
        {
            this._cookies.put(_k, new HttpCookie(_k, _v));
        }
        
        @SneakyThrows
        public void add(String _c)
        {
            for(HttpCookie _ck : HttpCookie.parse(_c))
            {
                this._cookies.put(_ck.getName(), _ck);
            }
        }
        
        @Override
        public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders)
                throws IOException
        {
            Map<String, List<String>> _ret = new HashMap<>();
            List<String> _list = new Vector<>();
            _ret.put("Cookie", _list);
            for(HttpCookie _c : this._cookies.values())
            {
                _list.add(_c.toString());
            }
            return Collections.unmodifiableMap(_ret);
        }
        
        @Override
        public void put(URI uri, Map<String, List<String>> responseHeaders)
                throws IOException
        {
            for(String _k : responseHeaders.keySet())
            {
                if(_k.toLowerCase().startsWith("set-cookie"))
                {
                    for(String _line : responseHeaders.get(_k))
                    {
                        for(HttpCookie _ck : HttpCookie.parse(_line))
                        {
                            this._cookies.put(_ck.getName(), _ck);
                        }
                    }
                }
            }
        }
    }
    
}
