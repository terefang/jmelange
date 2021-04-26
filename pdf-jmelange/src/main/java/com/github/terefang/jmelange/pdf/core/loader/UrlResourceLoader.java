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
package com.github.terefang.jmelange.pdf.core.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlResourceLoader implements PdfResourceLoader
{
	URL file;
	
	public static UrlResourceLoader of(URL _file)
	{
		UrlResourceLoader _rl = new UrlResourceLoader();
		_rl.file = _file;
		return _rl;
	}
	
	public static UrlResourceLoader of(String _file)
	{
		try {
			return of(new URL(_file));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String getName()
	{
		return file.toString();
	}
	
	@Override
	public InputStream getInputStream()
	{
		try
		{
			return this.file.openStream();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
