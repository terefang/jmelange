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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PdfDict extends AbstractPdfValue
{
	public static PdfDict create() { return new PdfDict(); }
	
	public PdfDict()
	{}
	
	Map<PdfName, PdfValue> dict = new LinkedHashMap<>();
	
	public PdfDict set(String key, PdfValue value)
	{
		this.dict.put(PdfName.of(key), value);
		return this;
	}
	
	public Collection<PdfValue> values() { return this.dict.values(); }
	
	public PdfValue get(String key)
	{
		return this.dict.get(PdfName.of(key));
	}
	
	public String asString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<<");
		for(Map.Entry<PdfName, PdfValue> e : this.dict.entrySet())
		{
			if(e.getValue() instanceof PdfDict && ((PdfDict)e.getValue()).dict.size()==0)
			{
				// IGNORE
			}
			else
			{
				sb.append("\n  ");
				sb.append(e.getKey().asString());
				sb.append(" ");
				sb.append(e.getValue().asString());
				if(sb.toString().length()-sb.toString().lastIndexOf('\n') > 80) sb.append("\n");
			}
		}
		sb.append(">> ");
		return sb.toString();
	}
	
	public Map<PdfName, PdfValue> asMap()
	{
		return Collections.unmodifiableMap(this.dict);
	}

	public void setName(String _name)
	{
		this.set("Name", PdfName.of(_name));
	}

	public void setType(String _type)
	{
		this.set("Type", PdfName.of(_type));
	}

	public void setSubtype(String _type)
	{
		this.set("Subtype", PdfName.of(_type));
	}
}
