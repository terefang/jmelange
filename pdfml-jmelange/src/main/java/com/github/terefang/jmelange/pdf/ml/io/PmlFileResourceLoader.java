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
package com.github.terefang.jmelange.pdf.ml.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class PmlFileResourceLoader implements PmlResourceLoader
{
	File file;
	
	public static PmlFileResourceLoader of(File _file)
	{
		PmlFileResourceLoader _rl = new PmlFileResourceLoader();
		_rl.file = _file;
		return _rl;
	}
	
	public static PmlFileResourceLoader of(String _file)
	{
		return of(new File(_file));
	}
	
	@Override
	public String getName()
	{
		return this.file.getName();
	}
	
	@Override
	public InputStream getInputStream()
	{
		try
		{
			return new FileInputStream(this.file);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
