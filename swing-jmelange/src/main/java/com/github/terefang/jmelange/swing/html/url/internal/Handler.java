package com.github.terefang.jmelange.swing.html.url.internal;

import com.github.terefang.jmelange.commons.util.StringUtil;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

// this class /has/ to be called Handler
public class Handler extends URLStreamHandler
{
    static String _URL_PKG = "java.protocol.handler.pkgs";
    static String _URL_NS = "com.github.terefang.jmelange.swing.html.url";
    
    public static synchronized void register()
    {
        String _p = System.getProperty(_URL_PKG);
        if(StringUtil.isBlank(_p))
        {
            System.setProperty(_URL_PKG,_URL_NS);
        }
        else
        {
            System.setProperty(_URL_PKG,_URL_NS+"|"+_p);
        }
    }
    
    public URLConnection openConnection(URL u){
        return new InternalUrlConnection(u);
    }
}