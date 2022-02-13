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
package com.github.terefang.jmelange.commons.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileResourceLoader implements ResourceLoader
{
	File file;
	String[] options;

	@Override
	public String[] getOptions() {
		return options;
	}

	public static FileResourceLoader of(File _file, String[] _options)
	{
		FileResourceLoader _rl = new FileResourceLoader();
		_rl.file = _file;
		_rl.options = _options;
		return _rl;
	}

	public static FileResourceLoader of(String _file)
	{
		return of(new File(_file), null);
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
