package com.github.terefang.jmelange.commons.http;

import lombok.Data;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

@Data
public class HttpClientSSLSocketFactory
		extends SSLSocketFactory {
	SSLContext sslCtx = null;
	Set<String> sslProtocols = new HashSet();
	Set<String> sslCiphers = new HashSet();

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
			throws IOException {
		SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(address, port, localAddress, localPort);
		setSocketOptions(socket);
		return socket;
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
			throws IOException, UnknownHostException {
		SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(host, port, localHost, localPort);
		setSocketOptions(socket);
		return socket;
	}

	@Override
	public Socket createSocket(InetAddress host, int port)
			throws IOException {
		SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(host, port);
		setSocketOptions(socket);
		return socket;
	}

	@Override
	public Socket createSocket(String host, int port)
			throws IOException, UnknownHostException {
		SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(host, port);
		setSocketOptions(socket);
		return socket;
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return sslCiphers.toArray(new String[0]);
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return sslCiphers.toArray(new String[0]);
	}

	@Override
	public Socket createSocket(Socket s, String host, int port, boolean autoClose)
			throws IOException {
		SSLSocket socket = (SSLSocket) sslCtx.getSocketFactory().createSocket(s, host, port, autoClose);
		setSocketOptions(socket);
		return socket;
	}

	void setSocketOptions(SSLSocket socket) {
		if (!sslProtocols.isEmpty()) {
			socket.setEnabledProtocols(sslProtocols.toArray(new String[0]));
		}

		if (!sslCiphers.isEmpty()) {
			socket.setEnabledCipherSuites(sslCiphers.toArray(new String[0]));
		}
	}
}
