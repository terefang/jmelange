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
package com.github.terefang.jmelange.pdf.core.encoding;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AglFn
{
	public static HashMap<Integer, String> AGLFN = new HashMap<Integer, String>();
	public static HashMap<String, Integer> UNIFN = new HashMap<String, Integer>();
	public static HashMap<Integer, String> ZDBFN = new HashMap<Integer, String>();
	static {
		try {
			AglFn.parseAglFN(AglFn.AGLFN, "afm/aglfn.txt");
			AglFn.parseUniFN(AglFn.UNIFN, "afm/aglfn.txt", true);
			AglFn.parseUniFN(AglFn.UNIFN, "afm/zapfdingbats.txt", false);
			AglFn.parseAglFN(AglFn.ZDBFN, "afm/zapfdingbats.txt");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void parseAglFN(HashMap<Integer, String> _FN, String _file) throws Exception
	{
		InputStream _rin = ClasspathResourceLoader.of(_file, null).getInputStream();
		InputStreamReader _isr = new InputStreamReader(_rin);
		BufferedReader in = new BufferedReader(_isr);
		String _line;
		while((_line = in.readLine())!=null)
		{
			if(_line.trim().startsWith("#")) { continue; }
			String[] _parts = _line.split(";", 3);
			int _c = CommonUtil.createInteger("0x"+_parts[0]);
			_FN.put(_c, _parts[1].trim());
		}
		in.close();
	}

	public static void parseUniFN(HashMap<String, Integer> _FN, String _file, boolean _order) throws Exception
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(ClasspathResourceLoader.of(_file, null).getInputStream()));
		String _line;
		while((_line = in.readLine())!=null)
		{
			if(_line.trim().startsWith("#")) { continue; }
			String[] _parts = _line.split(";", 3);
			if(_order)
			{
				int _c = CommonUtil.createInteger("0x"+_parts[0]);
				_FN.put(_parts[1].trim(), _c);
			}
			else
			{
				int _c = CommonUtil.createInteger("0x"+_parts[1].trim());
				_FN.put(_parts[0].trim(), _c);
			}
		}
		in.close();
	}
	
	public static String glyphName(HashMap<Integer, String> _FN, int _c, boolean _useUniName)
	{
		if(_FN.containsKey(_c))
		{
			return _FN.get(_c);
		}
		else if(_useUniName)
		{
			return String.format("uni%04X", _c);
		}
		return ".notdef";
	}

	public static int glyphCode(HashMap<String, Integer> _FN, String _c, boolean _useUniName)
	{
		if(_FN.containsKey(_c))
		{
			return _FN.get(_c);
		}
		else if(_useUniName && _c!=null && _c.startsWith("uni") && _c.length()==7)
		{
			return CommonUtil.createInteger("0x"+_c.substring(3));
		}
		return 0;
	}
	
}
