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

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipResourceLoader implements PdfResourceLoader
{
	ZipFile file;
	ZipEntry entry;
	
	public static ZipResourceLoader of(ZipFile _file, ZipEntry _entry)
	{
		ZipResourceLoader _rl = new ZipResourceLoader();
		_rl.file = _file;
		_rl.entry = _entry;
		return _rl;
	}
	
	@Override
	public String getName()
	{
		return "zip:"+this.file.getName()+"!"+this.entry.getName();
	}
	
	@Override
	public InputStream getInputStream()
	{
		try
		{
			return this.file.getInputStream(this.entry);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
