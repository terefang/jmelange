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

import java.io.InputStream;

public class PmlClasspathResourceLoader implements PmlResourceLoader
{
	String file;
	ClassLoader classLoader;
	
	public static PmlClasspathResourceLoader of(String _file)
	{
		return of(_file, PmlClasspathResourceLoader.class.getClassLoader());
	}
	
	public static PmlClasspathResourceLoader of(String _file, ClassLoader _cl)
	{
		if(_cl == null)
		{
			_cl = ClassLoader.getSystemClassLoader();
		}
		
		PmlClasspathResourceLoader _rl = new PmlClasspathResourceLoader();
		_rl.file = _file;
		_rl.classLoader = _cl;
		return _rl;
	}
	
	@Override
	public String getName()
	{
		return "cp:"+this.file;
	}
	
	@Override
	public InputStream getInputStream()
	{
		return this.classLoader.getResourceAsStream(this.file);
	}
}
