package com.github.terefang.jmelange.swing.html.url.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class InternalUrlConnection
        extends URLConnection
{
    /**
     * Constructs a URL connection to the specified URL. A connection to
     * the object referenced by the URL is not created.
     *
     * @param url the specified URL.
     */
    
    URL _real;
    URLConnection _realConnection;
    
    protected InternalUrlConnection(URL url)
    {
        super(url);
    }
    
    @Override
    public InputStream getInputStream()
            throws IOException
    {
        if(this._real==null) connect();
        return this._realConnection.getInputStream();
    }
    
    @Override
    public void connect()
            throws IOException
    {
        String _path = this.getURL().getPath();
        this._real = InternalUrlConnection.class.getResource(_path);
        this._realConnection = this._real.openConnection();
    }
}
