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
package com.github.terefang.jmelange.pdf.core.color;

import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.values.*;

public class PdfGrayColorSpace extends PdfColorSpace
{
	public PdfGrayColorSpace(PdfDocument doc)
	{
		super(doc);
	}
	
	static PdfGrayColorSpace deviceGray = null;
	
	public static synchronized PdfGrayColorSpace getDeviceGray(PdfDocument doc)
	{
		if(deviceGray==null)
		{
			deviceGray = new PdfGrayColorSpace(doc) {
				@Override
				public PdfResource getResource()
				{
					PdfResource _pr =  new PdfResource("", "ColorSpace") {
						@Override
						public String getResName() {
							return "DeviceGray";
						}
					};
					_pr.set(this);
					return _pr;
				}
			};
			deviceGray.add(PdfName.of("DeviceGray"));
		}
		return deviceGray;
	}
	
	public static synchronized PdfGrayColorSpace getCalGray(PdfDocument doc, float _wx, float _wy, float _wz, float _bx, float _by, float _bz, float _ga)
	{
		PdfGrayColorSpace calGray = new PdfGrayColorSpace(doc);
		calGray.add(PdfName.of("CalGray"));
		PdfDict _dict = PdfDict.create();
		calGray.add(_dict);
		_dict.set("WhitePoint", PdfArray.fromFloat(_wx, _wy, _wz));
		_dict.set("BlackPoint", PdfArray.fromFloat(_bx, _by, _bz));
		_dict.set("Gamma", PdfFloat.of(_ga));
		return calGray;
	}
}
