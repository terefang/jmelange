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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class PdfFloat extends AbstractPdfValue
{
	DecimalFormat format;
	
	Float value;
	
	public PdfFloat()
	{
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		this.format = new DecimalFormat("0.######", dfs);
	}
	
	public PdfFloat(Float v)
	{
		this();
		this.value = v;
	}
	
	public float getValue()
	{
		return value;
	}
	
	public void setValue(Float value)
	{
		this.value = value;
	}
	
	public static PdfFloat of(Float v)
	{
		return new PdfFloat(v);
	}
	@Override
	public String asString()
	{
		return this.format.format(this.value);
	}
}
