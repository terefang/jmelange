package com.github.terefang.jmelange.commons.http;

import lombok.Data;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Data
public abstract class AbstractHttpClient implements HttpClientInterface
{

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

    private SSLContext sslCtx = null;
    private String proxyUrl = null;
    private String acceptEncoding = null;
    private String acceptCharset = "utf-8";
    private String contentType = null;
    private String acceptType = null;
    private Map<String, String> requestHeader = new HashMap();
    private String sslVersion = TLS_VERSION_12;
    private Set<String> sslProtocols = new HashSet();
    private Set<String> sslCiphers = new HashSet();
    private List<String> cookieJar = new Vector<>();
    private int timeout = 500;
    private boolean followRedirects;

    private String loginUsername = null;
    private String loginPassword = null;

    @Override
    public void setLoginCredential(String name, String credential) {
        this.loginUsername = name;
        this.loginPassword = credential;
    }

    @Override
    public void addRequestHeader(String _name, String _value)
    {
        if(this.getRequestHeader() == null)
        {
            this.setRequestHeader(new HashMap<>());
        }
        this.getRequestHeader().put(_name, _value);
    }

    @Override
    public HttpClientResponse executeRequest(String url, String method, String contentType, String acceptType, byte[] data) throws Exception
    {
        return this.executeRequest(url, method, contentType, acceptType, null, data);
    }

    @Override
    public HttpClientResponse executeRequest(String url, String method, String contentType, String acceptType, String data) throws Exception
    {
        return this.executeRequest(url, method, contentType, acceptType, null, data);
    }

    @Override
    public HttpClientResponse executeRequest(String url, String method, String contentType, byte[] data) throws Exception
    {
        return this.executeRequest(url, method, contentType, null, null, data);
    }

    @Override
    public HttpClientResponse executeRequest(String url, String method, String contentType, String data) throws Exception
    {
        return this.executeRequest(url, method, contentType, null, null, data);
    }

    @Override
    public HttpClientResponse executeRequest(String url, String method, byte[] data) throws Exception
    {
        return this.executeRequest(url, method, null, null, null, data);
    }

    @Override
    public HttpClientResponse executeRequest(String url, String method, String data) throws Exception
    {
        return this.executeRequest(url, method, null, null, null, data);
    }

    @Override
    public HttpClientResponse executeRequest(String url, byte[] data) throws Exception
    {
        return this.executeRequest(url, "POST", null, null, null, data);
    }

    @Override
    public HttpClientResponse executeRequest(String url, String data) throws Exception
    {
        return this.executeRequest(url, "POST", null, null, null, data);
    }

    @Override
    public HttpClientResponse executeRequest(String url, String method, String contentType, String acceptType, Map<String, String> header, String data) throws Exception
    {
        HttpClientResponse rsp = executeRequest(url, method, contentType, acceptType, header, data==null ? null : data.getBytes());
        if(rsp == null) return null;
        if(rsp.get() == null) return rsp;
        String c = new String((byte[])rsp.get());
        rsp.setObject(c);
        return rsp;
    }

    @Override
    public HttpClientResponse getRequest(String url, String acceptType, Map<String, String> header) throws Exception
    {
        return this.executeRequest(url, "GET", null, acceptType, header, (byte[])null);
    }

    @Override
    public HttpClientResponse getRequest(String url, String acceptType) throws Exception
    {
        return this.executeRequest(url, "GET", null, acceptType, null, (byte[])null);
    }

    @Override
    public HttpClientResponse getRequest(String url) throws Exception
    {
        return this.executeRequest(url, "GET", null, null, null, (byte[])null);
    }

    @Override
    public HttpClientResponse postRequest(String url, String type, String data) throws Exception
    {
        return this.executeRequest(url, "POST", type, type, null, data);
    }

    @Override
    public HttpClientResponse postRequest(String url, String type, byte[] data) throws Exception
    {
        return this.executeRequest(url, "POST", type, type, null, data);
    }

    @Override
    public HttpClientResponse putRequest(String url, String type, String data) throws Exception
    {
        return this.executeRequest(url, "PUT", type, type, null, data);
    }

    @Override
    public HttpClientResponse putRequest(String url, String type, byte[] data) throws Exception
    {
        return this.executeRequest(url, "PUT", type, type, null, data);
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


}
