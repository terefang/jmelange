package com.github.terefang.jmelange.scripted.impl.luaj;

import com.google.common.collect.Lists;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import java.util.List;

public class HashLib extends AbstractLibrary
{
	@Override
	public List<Class> getLibraryFunctions() {
		return Lists.newArrayList(_test.class
		);
	}

	@Override
	public String getLibraryName() {
		return "hash";
	}

	public HashLib()
	{
		super();
	}

	static final class _test extends VarArgFunction {
		@Override
		public Varargs invoke(Varargs _args)
		{
			return LuaString.valueOf("true");
		}
	}

	/*
		This module implements various hash functions. Available hashing algorithms should at least include md5, sha1, sha256, sha512, whirl (whirlpool). Hash types can be concatted, so:
		"whirl, sha1"
		means "first hash with whirlpool, then has the result of that with sha1"
		hash.types()       produces a string that lists the types of hash available.
		hash.hashstr(str, type, encoding)   hashes string 'str' using hash function 'type' (e.g. 'sha1'). encoding is optional, (e.g. 'base64', 'hex', 'octal' etc). Default encoding is 'hex'.
		hash.hashfile(path, type, encoding)   hashes file at 'path' using hash function 'type' (e.g. 'sha1'). encoding is optional, (e.g. 'base64', 'hex', 'octal' etc). Default encoding is 'hex'.
		Also available is the hash object for more general hashing:
		h=hash.HASH("whirl,sha1","base64", _hmackey);
		h:update("some text to hash");
		h:update("more text to hash");
		print("result: " .. h:finish());
	*/
}
