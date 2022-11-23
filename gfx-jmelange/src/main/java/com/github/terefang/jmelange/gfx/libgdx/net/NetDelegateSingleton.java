package com.github.terefang.jmelange.gfx.libgdx.net;

import com.github.terefang.jmelange.gfx.libgdx.Files;
import com.github.terefang.jmelange.gfx.libgdx.Net;
import com.github.terefang.jmelange.gfx.libgdx.files.FileHandle;

public class NetDelegateSingleton implements Net
{
    private static Net net;
    private static NetDelegateSingleton instance;

    private NetDelegateSingleton() {}
    public static synchronized NetDelegateSingleton getInstance()
    {
        if(instance==null)
        {
            instance = new NetDelegateSingleton();
        }
        return instance;
    }

    public static synchronized void register(Net _n)
    {
        net = _n;
    }

    @Override
    public void sendHttpRequest(HttpRequest httpRequest, HttpResponseListener httpResponseListener) {
        if(net==null) return;
        net.sendHttpRequest(httpRequest, httpResponseListener);
    }

    @Override
    public void cancelHttpRequest(HttpRequest httpRequest) {
        if(net==null) return;
        net.cancelHttpRequest(httpRequest);
    }

    @Override
    public ServerSocket newServerSocket(Protocol protocol, String hostname, int port, ServerSocketHints hints) {
        if(net==null) return null;
        return net.newServerSocket(protocol, hostname, port, hints);
    }

    @Override
    public ServerSocket newServerSocket(Protocol protocol, int port, ServerSocketHints hints) {
        if(net==null) return null;
        return net.newServerSocket(protocol, port, hints);
    }

    @Override
    public Socket newClientSocket(Protocol protocol, String host, int port, SocketHints hints) {
        if(net==null) return null;
        return net.newClientSocket(protocol, host, port, hints);
    }

    @Override
    public boolean openURI(String URI) {
        if(net==null) return false;
        return net.openURI(URI);
    }
}
