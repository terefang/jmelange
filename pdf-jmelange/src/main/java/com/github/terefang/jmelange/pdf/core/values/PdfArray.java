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
package com.github.terefang.jmelange.pdf.core.values;

import com.github.terefang.jmelange.pdf.core.PdfValue;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class PdfArray<T extends PdfValue> extends AbstractPdfValue
{
	public static PdfArray create() { return new PdfArray(); }
	
	public static PdfArray from(PdfValue... vs)
	{
		PdfArray arr = create();
		for(PdfValue v : vs)
		{
			arr.add(v);
		}
		return arr;
	}
	
	public static PdfArray from(String... names)
	{
		PdfArray arr = create();
		for(String n : names)
		{
			arr.add(PdfName.of(n));
		}
		return arr;
	}
	
	public static PdfArray from(int... values)
	{
		PdfArray arr = create();
		for(int v : values)
		{
			arr.add(PdfNum.of(v));
		}
		return arr;
	}
	
	public static PdfArray fromFloat(float... values)
	{
		PdfArray arr = create();
		for(float v : values)
		{
			arr.add(PdfFloat.of(v));
		}
		return arr;
	}
	
	public PdfArray()
	{}
	
	List<T> dict = new Vector();
	
	public List<T> asList()
	{
		return Collections.unmodifiableList(this.dict);
	}
	
	public PdfArray add(T value)
	{
		this.dict.add(value);
		return this;
	}
	
	public String asString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");

		for(PdfValue v : this.dict)
		{
			sb.append(v.asString());
			sb.append(" ");
			if(sb.toString().length()-sb.toString().lastIndexOf('\n') > 80) sb.append("\n");
		}
		sb.append("] ");
		return sb.toString();
	}
	
	public int size()
	{
		return dict.size();
	}
	
	public boolean isEmpty()
	{
		return dict.isEmpty();
	}

	public void add(int index, T element)
	{
		dict.add(index, element);
	}

	public void prepend(T element)
	{
		dict.add(0, element);
	}

	public T get(int index)
	{
		return dict.get(index);
	}
}
