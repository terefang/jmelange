package com.github.terefang.jmelange.commons.http;

import com.github.terefang.jmelange.commons.CommonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
public class HttpOkClient extends AbstractHttpClient implements HttpClientInterface
{
    private MemoryCookieJar _jar;

    public HttpOkClient() {}

    protected Request.Builder prepareConnection(Request.Builder _con, String method, Map<String, String> header, String _contentType, String _acceptType)
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
            _con = _con.header("Authorization", "Basic "+ CommonUtil.toBase64(this.getLoginUsername()+" "+ this.getLoginPassword()));
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

    private Map<String, String> readResponseHeader(Response _rsp, HttpClientResponse _resp)
    {
        Map<String, String> _h = new HashMap<>();
        _resp.setContentEncoding(_rsp.header(HTTP_HEADER_CONTENT_ENCODING));
        _resp.setContentType(_rsp.header(HTTP_HEADER_CONTENT_TYPE));

        List<String> _c = new Vector<>();
        _rsp.headers().iterator().forEachRemaining((_p) -> {
            _h.put(_p.getFirst(), _p.getSecond());
            if("set-cookie".equalsIgnoreCase(_p.getFirst()))
            {
                _c.add(_p.getSecond());
                this._jar.add(_p.getSecond());
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
    public HttpClientResponse<byte[]> executeRequest(String url, String method, String contentType, String acceptType, Map<String, String> header, byte[] _data) throws Exception
    {
        return executeRequest(url, method, acceptType, header, _data == null ? null : RequestBody.create(_data, MediaType.parse(contentType)));
    }

    public HttpClientResponse<String> executeRequestString(String url, String method, String acceptType, Map<String, String> header, RequestBody _body) throws Exception
    {
        HttpClientResponse rsp = executeRequest(url, method, acceptType, header, _body);
        if(rsp == null) return null;
        String c = new String((byte[])rsp.get());
        rsp.setObject(c);
        return rsp;
    }

    public HttpClientResponse<byte[]> executeRequest(String url, String method, String acceptType, Map<String, String> header, RequestBody _body) throws Exception
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

        HttpClientSSLSocketFactory _sfact = new HttpClientSSLSocketFactory();
        _sfact.setSslCtx(this.getSslCtx());
        _sfact.setSslProtocols(this.getSslProtocols());
        _sfact.setSslCiphers(this.getSslCiphers());

        OkHttpClient _client = new OkHttpClient.Builder()
                .cookieJar(this._jar)
                .readTimeout(this.getTimeout()*2, TimeUnit.MILLISECONDS)
                .callTimeout(this.getTimeout()*4, TimeUnit.MILLISECONDS)
                .connectTimeout(this.getTimeout(), TimeUnit.MILLISECONDS)
                .followRedirects(this.isFollowRedirects())
                .followSslRedirects(this.isFollowRedirects())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(_sfact)
                .build();

        Request.Builder _rb = new Request.Builder();

        _rb = prepareConnection(_rb, method, header, (_body == null) ? null : _body.contentType().toString(), acceptType);

        if("GET".equalsIgnoreCase(method))
        {
            _rb = _rb.get();
        }
        else
        {
            if("POST".equalsIgnoreCase(method))
            {
                _rb = _rb.post(_body);
            }
            else
            if("PUT".equalsIgnoreCase(method))
            {
                _rb = _rb.put(_body);
            }
            else
            if("DELETE".equalsIgnoreCase(method))
            {
                _rb = _rb.delete(_body);
            }
        }

        HttpClientResponse _resp = HttpClientResponse.create();
        Response _rsp = null;
        try
        {
            Request _req = _rb.build();
            Call _call = _client.newCall(_req);
            _rsp = _call.execute();

            readResponseHeader(_rsp, _resp);
            _resp.setObject(_rsp.body().bytes());
            _resp.setStatus(_rsp.code());
            _resp.setMessage(_rsp.message());
        }
        catch(Exception xe)
        {
            _resp.setObject(null);
            _resp.setStatus(_rsp.code());
            _resp.setMessage(_rsp.message());
        }
        return _resp;
    }

    @Override
    public HttpClientResponse<byte[]> postForm(String url, String type, Map data) throws Exception
    {
        FormBody.Builder _body = new FormBody.Builder();

        for(Object _k : data.keySet())
        {
            _body = _body.add(_k.toString(), data.get(_k).toString());
        }

        return this.executeRequest(url, "POST", type, null, _body.build());
    }

    @Override
    public HttpClientResponse<byte[]> postMultipartForm(String url, String type, Map _data) throws Exception
    {
        Map<String, String> data = _data;
        MultipartBody.Builder _mpb = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Map.Entry<String, String> _e : data.entrySet())
        {
            _mpb.addFormDataPart(_e.getKey(), _e.getValue());
        }

        return this.executeRequest(url, "POST", type, null, _mpb.build());
    }

    public static class MemoryCookieJar implements CookieJar
    {
        public static MemoryCookieJar create() { return new MemoryCookieJar(); }

        Map<String, Cookie> _cookies = new HashMap<>();

        @Override
        public List<Cookie> loadForRequest(HttpUrl httpUrl) {
            return new Vector<>(this._cookies.values());
        }

        @Override
        public void saveFromResponse(HttpUrl httpUrl, List<Cookie> _list)
        {
            for(Cookie _c : _list)
            {
                this._cookies.put(_c.name(), _c);
            }
        }

        public void add(String _k, String _v)
        {
            this._cookies.put(_k,new Cookie.Builder().domain("*").name(_k).value(_v).build());
        }
        public void add(String _c)
        {
            Cookie _ck = parseToCookie(_c);
            this._cookies.put(_ck.name(), _ck);
        }
    }

    public static final Cookie parseToCookie(String _c)
    {
        int _eq = _c.indexOf('=');
        String _k = _c.substring(0, _eq);
        int _end = _c.indexOf(';', _eq);
        String _v = _c.substring(_eq+1);
        if(_end > 0)
        {
            _v = _c.substring(_eq+1, _end);
        }
        return new Cookie.Builder().domain("*").name(_k).value(_v).build();
    }
}
