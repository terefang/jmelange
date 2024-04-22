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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Optional;

public class ByteArrayResourceLoader implements ResourceLoader
{
	String name;
	byte[] bytes;
	String[] options;

	@Override
	public String[] getOptions() {
		return options;
	}

	public static ByteArrayResourceLoader of(String _name, byte[] _bytes, String[] _options)
	{
		ByteArrayResourceLoader _rl = new ByteArrayResourceLoader();
		_rl.name = _name;
		_rl.bytes = _bytes;
		_rl.options = _options;
		return _rl;
	}

	@Override
	public String getName()
	{
		return this.name;
	}
	
	@Override
	public InputStream getInputStream()
	{
		return new ByteArrayInputStream(this.bytes);
	}

	@Override
	public ResourceLoader getSub(String _suffix) {
		return null;
	}
}
