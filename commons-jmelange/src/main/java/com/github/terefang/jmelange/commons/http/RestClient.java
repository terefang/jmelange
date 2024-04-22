package com.github.terefang.jmelange.commons.http;

import com.github.terefang.jmelange.commons.CommonUtil;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class RestClient<R,S> extends HttpClient
{
    public static interface EncoderDecoder<A,B>
    {
        public default B decode (byte[] buf) { return decode(buf, "UTF-8"); }
        public B decode (byte[] buf, String _cs);
        @SneakyThrows
        public default B decodeStream (InputStream _is, String _cs)
        {
            return decode(CommonUtil.toByteArray(_is), _cs);
        }

        public default byte[] encode (A obj) { return encode(obj, "UTF-8"); }
        public byte[] encode (A obj, String _cs);
        @SneakyThrows
        public default void encodeStream (A obj, String _cs, OutputStream _os)
        {
            _os.write(encode(obj,_cs));
        }

        public String getContentType();
        public String getAcceptType();
    }

    public static <K,L> RestClient<K,L> of(EncoderDecoder<K,L> ed)
    {
        RestClient c = new RestClient();
        c.setEncoderDecoder(ed);
        return c;
    }

    private String serviceUrl = "https://localhost:443/axl/";
    private String serviceUsername = null;
    private String servicePassword = null;

    EncoderDecoder encoderDecoder;

    public EncoderDecoder getEncoderDecoder() {
        return encoderDecoder;
    }

    public void setEncoderDecoder(EncoderDecoder encoderDecoder) {
        this.encoderDecoder = encoderDecoder;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getServiceUsername() {
        return serviceUsername;
    }

    public void setServiceUsername(String serviceUsername) {
        this.serviceUsername = serviceUsername;
    }

    public String getServicePassword() {
        return servicePassword;
    }

    public void setServicePassword(String servicePassword) {
        this.servicePassword = servicePassword;
    }

    public synchronized HttpClientResponse<S> executeRestRequest(String urlPart, String httpMethod, String contentType, R arg) throws Throwable
    {
        return this.executeRestRequest(urlPart, httpMethod, null, contentType, null, arg);
    }

    public synchronized HttpClientResponse<S> executeRestRequest(String urlPart, String httpMethod, R arg) throws Throwable
    {
        return this.executeRestRequest(urlPart, httpMethod, null, null, null , arg);
    }

    public synchronized HttpClientResponse<S> executeRestRequest(String urlPart, R arg) throws Throwable
    {
        return this.executeRestRequest(urlPart, "POST", null, null, null , arg);
    }

    public synchronized HttpClientResponse<S> executeRestRequest(String urlPart, String httpMethod, Map<String, String> httpHeader, String contentType, String acceptType, R arg) throws Throwable
    {
        return executeRestRequest(urlPart, httpMethod, httpHeader, contentType, acceptType, arg, this.encoderDecoder);
    }

    public synchronized <R,S> HttpClientResponse<S> executeRestRequest(String urlPart, String httpMethod, Map<String, String> httpHeader, String contentType, String acceptType, R arg, EncoderDecoder<R,S> encoderDecoder) throws Throwable
    {
        if(this.getServicePassword()!=null)
        {
            this.setLoginCredential(this.getServiceUsername(), this.getServicePassword());
        }

        if(contentType == null) contentType = encoderDecoder.getContentType();
        if(acceptType == null) acceptType = encoderDecoder.getAcceptType();

        HttpClientResponse restRet = null;
        if(arg!=null)
        {
            restRet = this.executeRequest(serviceUrl+urlPart, httpMethod, contentType, acceptType, httpHeader, encoderDecoder.encode(arg));
        }
        else
        {
            restRet = this.executeRequest(serviceUrl+urlPart, httpMethod, contentType, acceptType, httpHeader, new byte[0]);
        }

        int status = restRet.getStatus();

        if(restRet.get()==null)
        {
            throw new Exception(""+restRet.getStatus());
        }
        restRet.setObject(encoderDecoder.decode((byte[]) restRet.get(), restRet.getContentCharset()));
        return restRet;
    }

}