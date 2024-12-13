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


import lombok.SneakyThrows;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

public class ClasspathResourceLoader implements ResourceLoader
{
	String file;
	ClassLoader classLoader;
	String[] options;

	@Override
	public String[] getOptions() {
		return options;
	}

	public static ClasspathResourceLoader of(String _file, String[] _options)
	{
		return of(_file, null, _options);
	}
	
	public static ClasspathResourceLoader of(String _file, ClassLoader _cl, String[] _options)
	{
		if(_cl == null)
		{
			_cl = ClassLoader.getSystemClassLoader();
		}
		
		ClasspathResourceLoader _rl = new ClasspathResourceLoader();
		_rl.file = _file;
		_rl.classLoader = _cl;
		_rl.options = _options;
		return _rl;
	}
	
	@Override
	public String getName()
	{
		return "cp:"+this.file;
	}
	
	@Override
	@SneakyThrows
	public InputStream getInputStream()
	{
		String _fn = this.file;
		if(_fn.startsWith("cp:/")) _fn = _fn.substring(4);
		if(_fn.startsWith("cp:")) _fn = _fn.substring(3);
		URL _res = this.classLoader.getResource(_fn);
		if(_res==null) return null;
		return _res.openStream();
	}

	@Override
	public ResourceLoader getSub(String _suffix) {
		return of(this.file+_suffix, this.classLoader, this.options);
	}
}
