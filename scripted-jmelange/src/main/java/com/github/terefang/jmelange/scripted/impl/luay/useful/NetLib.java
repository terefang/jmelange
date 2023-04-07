package com.github.terefang.jmelange.scripted.impl.luay.useful;

import luay.vm.LuaString;
import luay.vm.LuaTable;
import luay.vm.LuaValue;
import luay.vm.lib.OneArgFunction;
import luay.vm.lib.TwoArgFunction;

import java.net.URI;

public class NetLib extends TwoArgFunction
{
	public NetLib()
	{
	}

	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaTable string = new LuaTable();
		string.set("parseURL", new NetLib._parseURL());
		string.set("reformatURL", new NetLib._reformatURL());

		env.set("net", string);

		if (!env.get("package").isnil()) env.get("package").get("loaded").set("net", string);

		return string;
	}

	/* net.parseURL(URL)   - return a structure containing broken-down parts of a URL (see URL_INFO_STRUCT above)*/
	static final class _parseURL extends OneArgFunction
	{
		@Override
		public LuaValue call(LuaValue arg)
		{
			URI _uri = URI.create(arg.checkjstring());
			LuaTable _string = new LuaTable();
			_string.set("type", _uri.getScheme());  //protocol
			_string.set("host", _uri.getHost());  //hostname
			_string.set("port", _uri.getPort());
			_string.set("path", _uri.getPath());
			_string.set("args", _uri.getQuery()); //http style args (things following ? in a url)
			String _ui = _uri.getUserInfo();
			int _ofs = _ui.indexOf(':');
			if(_ofs>0)
			{
				_string.set("user", _ui.substring(0,_ofs));
				_string.set("pass", _ui.substring(_ofs+1));
			}
			else
			{
				_string.set("user", _ui);
			}
			return _string;
		}
	}

	static final class _reformatURL extends OneArgFunction
	{
		@Override
		public LuaValue call(LuaValue arg)
		{
			URI _uri = URI.create(arg.checkjstring());
			return LuaString.valueOf(_uri.normalize().toString());
		}
	}

	/* net.lookupIP(hostname)   - return IP address associated with hostname */
	/* net.lookupHost(IPAddress)   - return hostname associated with IP address */
	/* net.IsIP4Address(string)   - returns 'true' if string is a valid IP4 address */
	/* net.IsIP6Address(string)   - returns 'true' if string is a valid IP6 address */
	/* net.IsIPAddress(string)   - returns 'true' if string is a either an IP4 or IP6 address */
	/* net.interfaceIP("eth0")   - returns primary IP address of a network interface (in this case eth0) */
	/* net.externalIP()   - returns external IP address of the system by conversing with various external servers */

		/* net.setProxy(string)  - set a proxy chain to be used for all connections by this process. A proxy chain is a
		list of proxy urls separated by '|'. Proxy urls have the form <protocol>://<user>:<password>@<host>:<port>.
		<user> and <password> are optional when using open proxies, in which case the url is <protocol>://@<host>:<port>.
		The '//' syntax can also be ommited if desired.
		proxy protocols are:
		https
		socks4
		socks5
		ssh        - use ssh -w method to connect to remote host/port
		sshtunnel  - use ssh -L method to connect to remote host/port
		sshproxy   - use ssh -D method to connect to remote host/port
		e.g.   net.setProxy("sshtunnel://localhost|socks5:27.88.101.50:1080")  - setup ssh tunnel through localhost, and then through that use a socks5 proxy to finally access the destination
		*/


	/* SERVER provides a network server object. You use it like: net.SERVER("tcp::80") */

		/* setup TLS/SSL. 'Certificate' and 'Key' are paths to the Certificate and Key file respectively
  			you call this before 'accept', and when connections are accepted, TLS is activated on the new
  			connection */
	// bool setupTLS(const char *Certificate, const char *Key)
	// STREAM *accept()
	/* return port server is bound to. If you bind tls::0 then use this to figure out assigned port */
	//int port()
}





