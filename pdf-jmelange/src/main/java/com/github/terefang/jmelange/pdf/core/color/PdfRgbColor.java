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
import com.github.terefang.jmelange.pdf.core.content.AbstractPdfContent;

public class PdfRgbColor extends AbstractPdfColor implements PdfColor
{
	int red;
	int green;
	int blue;
	
	public static PdfRgbColor from(PdfCmykColor _cmyk)
	{
		int _R = (int) (255f * ((1f - _cmyk.getCyan()) * (1f - _cmyk.getBlack())));
		int _G = (int) (255f * ((1f - _cmyk.getMagenta()) * (1f - _cmyk.getBlack())));
		int _B = (int) (255f * ((1f - _cmyk.getYellow()) * (1f - _cmyk.getBlack())));
		return PdfRgbColor.from(_R, _G, _B);
	}
	
	public static PdfRgbColor from(int _r, int _g, int _b)
	{
		PdfRgbColor _c = new PdfRgbColor();
		_c.red = _r;
		_c.green = _g;
		_c.blue = _b;
		return _c;
	}
	
	public int getRed()
	{
		return red;
	}
	
	public void setRed(int red)
	{
		this.red = red;
	}
	
	public int getGreen()
	{
		return green;
	}
	
	public void setGreen(int green)
	{
		this.green = green;
	}
	
	public int getBlue()
	{
		return blue;
	}
	
	public void setBlue(int blue)
	{
		this.blue = blue;
	}
	
	@Override
	public void setFillColor(AbstractPdfContent _content)
	{
		_content.fillColor(this.getRed(), this.getGreen(), this.getBlue());
	}
	
	@Override
	public void setStrokeColor(AbstractPdfContent _content)
	{
		_content.strokeColor(this.getRed(), this.getGreen(), this.getBlue());
	}
	
	public PdfCmykColor toCmyk()
	{
		return PdfCmykColor.from(this);
	}
	
	@Override
	public float[] getComponents()
	{
		return new float[]{ red/255f, green/255f, blue/255f };
	}
	
	@Override
	public PdfColorSpace getColorSpace(PdfDocument doc)
	{
		return PdfRgbColorSpace.getDeviceRgb(doc);
	}
}
