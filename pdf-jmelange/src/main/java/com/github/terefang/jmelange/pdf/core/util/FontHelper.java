/*
 * Copyright (c) 2020. terefang@gmail.com
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

//import sun.font.*;

import lombok.SneakyThrows;

import java.awt.*;
import java.lang.reflect.Field;

public class FontHelper
{
	@SneakyThrows
	public static boolean isTT(Font _awt)
	{
		try
		{
			Class _fmf_c = Class.forName("sun.font.FontManagerFactory");
			Object _fm = _fmf_c.getMethod("getInstance").invoke(null);
			Object _f2d = _fm.getClass().getMethod("findFont2D", String.class, int.class, int.class).invoke(_fm,_awt.getFontName(), _awt.getStyle(), 2);
			if(_f2d.getClass().getSimpleName().equalsIgnoreCase("TrueTypeFont"))
			{
				return true;
			}
		}
		catch(Throwable _xe)
		{
			_xe.printStackTrace();
		}
		return false;
		/*
		FontManager fm = FontManagerFactory.getInstance();
		Font2D _f2d = fm.findFont2D(_awt.getFontName(), _awt.getStyle(), FontManager.LOGICAL_FALLBACK);
		if(_f2d instanceof TrueTypeFont)
		{
			return true;
		}
		return false;
		*/
	}

	@SneakyThrows
	public static String getAwtFileName(Font _awt)
	{
		Class _fmf_c = Class.forName("sun.font.FontManagerFactory");
		Object _fm = _fmf_c.getMethod("getInstance").invoke(null);
		Object _f2d = _fm.getClass().getMethod("findFont2D", String.class, int.class, int.class).invoke(_fm,_awt.getFontName(), _awt.getStyle(), 2);

		try
		{
			Field _platName = Class.forName("sun.font.PhysicalFont").getDeclaredField("platName");
			_platName.setAccessible(true);
			return _platName.get(_f2d).toString();
		}
		catch(RuntimeException _xe)
		{
			_xe.printStackTrace();
		}
		catch(Exception _xe)
		{
			_xe.printStackTrace();
		}
		return null;

		/*
		FontManager fm = FontManagerFactory.getInstance();
		Font2D _f2d = fm.findFont2D(_awt.getFontName(), _awt.getStyle(), FontManager.LOGICAL_FALLBACK);
		try
		{
			Field _platName = PhysicalFont.class.getDeclaredField("platName");
			_platName.setAccessible(true);
			return _platName.get(_f2d).toString();
		}
		catch(RuntimeException _xe)
		{
			_xe.printStackTrace();
		}
		catch(Exception _xe)
		{
			_xe.printStackTrace();
		}
		return null;
		*/
	}

	@SneakyThrows
	public static boolean isT1(Font _awt)
	{
		Class _fmf_c = Class.forName("sun.font.FontManagerFactory");
		Object _fm = _fmf_c.getMethod("getInstance").invoke(null);
		Object _f2d = _fm.getClass().getMethod("findFont2D", String.class, int.class, int.class).invoke(_fm,_awt.getFontName(), _awt.getStyle(), 2);
		if(_f2d.getClass().getSimpleName().equalsIgnoreCase("Type1Font"))
		{
			return true;
		}
		return false;
		/*
		FontManager fm = FontManagerFactory.getInstance();
		Font2D _f2d = fm.findFont2D(_awt.getFontName(), _awt.getStyle(), FontManager.LOGICAL_FALLBACK);
		if(_f2d instanceof Type1Font)
		{
			return true;
		}
		return false;
		*/
	}
	
}
