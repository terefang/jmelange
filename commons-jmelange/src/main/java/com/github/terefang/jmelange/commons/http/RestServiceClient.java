package com.github.terefang.jmelange.commons.http;

import javax.ws.rs.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RestServiceClient<R,S,T> extends RestClient<R,S> implements InvocationHandler
{
    public synchronized T getService(Class<T> clazz)
    {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, this);
    }

    public synchronized S invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        try
        {
            String urlMethod = method.getAnnotation(GET.class)==null ? null : "GET";
            if(urlMethod==null) urlMethod = method.getAnnotation(POST.class)==null ? null : "POST";
            if(urlMethod==null) urlMethod = method.getAnnotation(PUT.class)==null ? null : "PUT";
            if(urlMethod==null) urlMethod = method.getAnnotation(DELETE.class)==null ? null : "DELETE";

            String urlPart = method.getAnnotation(Path.class)==null ? "" : method.getAnnotation(Path.class).value();

            String urlContent = method.getAnnotation(Consumes.class)==null ? null : method.getAnnotation(Consumes.class).value()[0];

            String urlAccept = method.getAnnotation(Produces.class)==null ? null : method.getAnnotation(Produces.class).value()[0];

            this.setAcceptType(urlAccept==null ? "application/json" : urlAccept);

            Map<String, String> urlHeader = new HashMap<>();

            int offs = 0;
            Parameter[] fields = method.getParameters();
            StringBuilder qsb = new StringBuilder();
            R requestObject = null;
            EncoderDecoder enDec = null;
            if(args!=null)
            {
                for(int i=1; i < args.length; i++)
                {
                    PathParam anno = fields[i].getAnnotation(PathParam.class);
                    QueryParam qanno = fields[i].getAnnotation(QueryParam.class);
                    HeaderParam hanno = fields[i].getAnnotation(HeaderParam.class);
                    BeanParam banno = fields[i].getAnnotation(BeanParam.class);

                    if(anno!=null)
                    {
                        if(args[i] instanceof Map)
                        {
                            for(Map.Entry<String, String> entry : ((Map<String,String>)args[i]).entrySet())
                            {
                                String findString = "{"+anno.value()+entry.getKey()+"}";
                                int rofs = urlPart.indexOf(findString);
                                if(rofs!=-1)
                                {
                                    urlPart = urlPart.substring(0, rofs) + URLEncoder.encode(entry.getValue()) + urlPart.substring(rofs+findString.length());
                                }
                            }
                        }
                        else
                        {
                            String findString = "{"+anno.value()+"}";
                            int rofs = urlPart.indexOf(findString);
                            if(rofs!=-1)
                            {
                                urlPart = urlPart.substring(0, rofs) + URLEncoder.encode(args[i].toString()) + urlPart.substring(rofs+findString.length());
                            }
                        }
                    }
                    else
                    if(qanno!=null)
                    {
                        if(args[i] instanceof Map)
                        {
                            for(Map.Entry<String, String> entry : ((Map<String,String>)args[i]).entrySet())
                            {
                                qsb.append("&" + qanno.value() +entry.getKey()+ "=");
                                qsb.append(URLEncoder.encode(entry.getValue()));
                            }
                        }
                        else
                        {
                            qsb.append("&" + qanno.value() + "=");
                            qsb.append(URLEncoder.encode(args[i].toString()));
                        }
                    }
                    else
                    if(hanno!=null)
                    {
                        if(args[i] instanceof Map)
                        {
                            for(Map.Entry<String, String> entry : ((Map<String,String>)args[i]).entrySet())
                            {
                                urlHeader.put(qanno.value()+entry.getKey(), entry.getValue());
                            }
                        }
                        else
                        {
                            urlHeader.put(qanno.value(), args[i].toString());
                        }
                    }
                    else
                    if(banno!=null)
                    {
                        requestObject = (R)args[i];
                    }
                    else
                    if(args[i] instanceof EncoderDecoder)
                    {
                        enDec = (EncoderDecoder) args[i];
                    }
                }

                if(qsb.toString().length()>0)
                {
                    urlPart += "?" + qsb.toString().substring(1);
                }
            }

            HttpClientResponse<S> ret = null;

            if(enDec == null)
            {
                this.executeRestRequest(urlPart, urlMethod, urlHeader, null, null, requestObject);
            }
            else
            {
                this.executeRestRequest(urlPart, urlMethod, urlHeader, enDec.getContentType(), enDec.getAcceptType(), requestObject, enDec);
            }

            return ret.get();
        }
        catch(Exception xe)
        {
            throw xe;
        }
    }

}