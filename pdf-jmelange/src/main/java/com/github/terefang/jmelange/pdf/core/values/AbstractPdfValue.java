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

import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractPdfValue implements PdfValue
{
	public abstract String asString();
	
	public void writeTo(OutputStream os) throws IOException
	{
		os.write(this.asString().getBytes());
	}
	
	@Override
	public int hashCode()
	{
		return this.asString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof AbstractPdfValue)
		{
			return this.asString().equals(((AbstractPdfValue)obj).asString());
		}
		return false;
	}
}
