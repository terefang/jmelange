package com.github.terefang.jmelange.commons.http;

import com.github.terefang.jmelange.commons.CommonUtil;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HttpClientResponse<K> implements Future<K>
{
    public static HttpClientResponse create()
    {
        return new HttpClientResponse();
    }

    public static HttpClientResponse create(Object o)
    {
        HttpClientResponse crc = new HttpClientResponse();
        crc.object = o;
        return crc;
    }

    public static HttpClientResponse create(Object o, int s)
    {
        HttpClientResponse crc = new HttpClientResponse();
        crc.object = o;
        crc.status = s;
        return crc;
    }

    public static HttpClientResponse create(Object o, int s, Map<String,String> h)
    {
        HttpClientResponse crc = new HttpClientResponse();
        crc.object = o;
        crc.status = s;
        crc.header = h;
        return crc;
    }

    public static HttpClientResponse create(Object o, int s, Map<String,String> h, String m)
    {
        HttpClientResponse crc = new HttpClientResponse();
        crc.object = o;
        crc.status = s;
        crc.header = h;
        crc.message = m;
        return crc;
    }

    private K object;

    private int status = 200;

    String contentType;
    String contentEncoding;

    Throwable exception;
    
    public Throwable getException()
    {
        return exception;
    }
    
    public void setException(Throwable _exception)
    {
        exception = _exception;
    }
    
    private Map<String,String> header = new HashMap<>();

    private String message;

    private List<String> cookieJar = new Vector<>();

    public List<String> getCookieJar() {
        return cookieJar;
    }

    public void setCookieJar(List<String> cookieJar) {
        this.cookieJar = cookieJar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning)
    {
        return false;
    }

    @Override
    public boolean isCancelled()
    {
        return false;
    }

    @Override
    public boolean isDone()
    {
        return true;
    }

    @Override
    public K get() throws InterruptedException, ExecutionException
    {
        return this.object;
    }

    @Override
    public K get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
    {
        return this.object;
    }

    public String getAsString() throws InterruptedException, ExecutionException
    {
        if(this.object == null) return null;

        if(this.object instanceof String)
        {
            return (String)this.object;
        }
        else
        if(this.object instanceof byte[])
        {
            return new String((byte[]) this.object, StandardCharsets.UTF_8);
        }
        else
        {
            return Objects.toString(this.object);
        }
    }

    public byte[] getAsBytes() throws InterruptedException, ExecutionException
    {
        if(this.object == null) return null;

        if(this.object instanceof String)
        {
            return ((String)this.object).getBytes(StandardCharsets.UTF_8);
        }
        else
        if(this.object instanceof byte[])
        {
            return (byte[]) this.object;
        }
        else
        {
            return Objects.toString(this.object).getBytes(StandardCharsets.UTF_8);
        }
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public K getObject() {
        return object;
    }

    public void setObject(K object) {
        this.object = object;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getContentCharset()
    {
        String _cs = "ASCII";
        int _off = 0;
        if((_off = CommonUtil.indexOf(getContentType(), "charset=")) > 0)
        {
            _cs = getContentType().substring(_off+8);
        }
        return _cs;
    }

    public String getRedirect()
    {
        if(this.getHeader().containsKey("location"))
        {
            return this.getHeader().get("location");
        }
        else
        if(this.getHeader().containsKey("Location"))
        {
            return this.getHeader().get("Location");
        }
        return null;
    }
}