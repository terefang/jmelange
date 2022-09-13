package com.github.terefang.jmelange.commons.http;

import java.util.List;
import java.util.Map;

public interface HttpClientInterface {
    String getAcceptCharset();

    void setAcceptCharset(String acceptCharset);

    String getSslVersion();

    void setSslVersion(String sslVersion);

    List<String> getCookieJar();

    void setCookieJar(List<String> cookieJar);

    String getAcceptEncoding();

    void setAcceptEncoding(String acceptEncoding);

    String getContentType();

    void setContentType(String contentType);

    String getAcceptType();

    void setAcceptType(String acceptType);

    Map<String, String> getRequestHeader();

    void setRequestHeader(Map<String, String> requestHeader);

    void addRequestHeader(String _name, String _value);

    void setLoginCredential(String name, String credential);

    HttpClientResponse executeRequest(String url, String method, String contentType, String acceptType, byte[] data) throws Exception;

    HttpClientResponse executeRequest(String url, String method, String contentType, String acceptType, String data) throws Exception;

    HttpClientResponse executeRequest(String url, String method, String contentType, byte[] data) throws Exception;

    HttpClientResponse executeRequest(String url, String method, String contentType, String data) throws Exception;

    HttpClientResponse executeRequest(String url, String method, byte[] data) throws Exception;

    HttpClientResponse executeRequest(String url, String method, String data) throws Exception;

    HttpClientResponse executeRequest(String url, byte[] data) throws Exception;

    HttpClientResponse executeRequest(String url, String data) throws Exception;

    HttpClientResponse executeRequest(String url, String method, String contentType, String acceptType, Map<String, String> header, String data) throws Exception;

    HttpClientResponse executeRequest(String url, String method, String contentType, String acceptType, Map<String, String> header, byte[] data) throws Exception;

    String getProxyUrl();

    void setProxyUrl(String proxyUrl);

    HttpClientResponse getRequest(String url, String acceptType, Map<String, String> header) throws Exception;

    HttpClientResponse getRequest(String url, String acceptType) throws Exception;

    HttpClientResponse getRequest(String url) throws Exception;

    HttpClientResponse postRequest(String url, String type, String data) throws Exception;

    HttpClientResponse postRequest(String url, String type, byte[] data) throws Exception;

    HttpClientResponse postForm(String url, String type, Map data) throws Exception;

    HttpClientResponse postMultipartForm(String url, String type, Map data) throws Exception;

    int getTimeout();

    boolean isFollowRedirects();

    void setTimeout(int timeout);

    void setFollowRedirects(boolean followRedirects);

}
