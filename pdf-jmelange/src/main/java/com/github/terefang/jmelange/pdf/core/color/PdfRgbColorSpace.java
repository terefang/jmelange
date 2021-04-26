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
import com.github.terefang.jmelange.pdf.core.values.PdfName;
import com.github.terefang.jmelange.pdf.core.values.PdfResource;

public class PdfRgbColorSpace extends PdfColorSpace
{
	public PdfRgbColorSpace(PdfDocument doc)
	{
		super(doc);
	}
	
	static PdfRgbColorSpace deviceRgb = null;
	
	public static synchronized PdfRgbColorSpace getDeviceRgb(PdfDocument doc)
	{
		if(deviceRgb==null)
		{
			deviceRgb = new PdfRgbColorSpace(doc) {
				@Override
				public PdfResource getResource()
				{
					PdfResource _pr =  new PdfResource("", "ColorSpace") {
						@Override
						public String getResName() {
							return "DeviceRGB";
						}
					};
					_pr.set(this);
					return _pr;
				}
			};
			deviceRgb.add(PdfName.of("DeviceRGB"));
		}
		return deviceRgb;
	}
}
