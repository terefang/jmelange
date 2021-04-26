/*
 * Copyright (c) 2019. terefang@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.terefang.jmelange.pdf.core.util;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;

public class CountingOutputStream extends OutputStream
{
	long count = 0;
	DigestOutputStream os;

	public CountingOutputStream()
	{

	}

	@SneakyThrows
	public CountingOutputStream(OutputStream os)
	{
		this();
		this.os = new DigestOutputStream(os, MessageDigest.getInstance("MD5"));
	}

	@SneakyThrows
	public CountingOutputStream(OutputStream os, long l)
	{
		this();
		this.os = new DigestOutputStream(os, MessageDigest.getInstance("MD5"));
		this.count = l;
		this.os.getMessageDigest().update(Long.toHexString(l).getBytes());
	}

	public MessageDigest getMessageDigest() {
		return os.getMessageDigest();
	}

	@Override
	public void write(int b) throws IOException
	{
		this.os.write(b);
		this.count++;
	}
	
	public long getCount()
	{
		return count;
	}
	
	public void setCount(long count)
	{
		this.count = count;
	}
	
	@Override
	public void write(byte[] b) throws IOException
	{
		for(byte x : b)
		{
			this.write((int)x);
		}
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		for(int j = off; j < (off+len); j++)
		{
			this.write((int) b[j]);
		}
	}
	
	@Override
	public void flush() throws IOException
	{
		os.flush();
	}
	
	@Override
	public void close() throws IOException
	{
		os.close();
	}
}
