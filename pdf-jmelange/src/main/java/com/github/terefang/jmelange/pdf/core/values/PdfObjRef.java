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

public class PdfObjRef extends PdfNum
{
	public PdfObjRef(Integer v)
	{
		super(v);
	}
	
	public static PdfObjRef of(Integer v)
	{
		return new PdfObjRef(v);
	}
	public PdfObjRef()
	{
		super();
	}
	
	@Override
	public String asString()
	{
		return super.asString()+" 0 R";
	}
}
