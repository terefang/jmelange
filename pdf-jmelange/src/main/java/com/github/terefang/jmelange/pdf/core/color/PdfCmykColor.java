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

public class PdfCmykColor extends AbstractPdfColor implements PdfColor
{
	float cyan;
	float magenta;
	float yellow;
	float black;

	public static PdfCmykColor from(PdfRgbColor _rgb)
	{
		float _R = _rgb.getRed()/255f;
		float _G = _rgb.getGreen()/255f;
		float _B = _rgb.getBlue()/255f;

		float _K = 1f-Math.max(_R, Math.max(_G, _B));
		float _C = (1f-_R-_K) / (1f-_K);
		float _M = (1f-_G-_K) / (1f-_K);
		float _Y = (1f-_B-_K) / (1f-_K);

		return PdfCmykColor.from(_C, _M, _Y, _K);
	}

	public static PdfCmykColor from(double _R, double _G, double _B)
	{
		float _K = (float) (1f-Math.max(_R, Math.max(_G, _B)));
		float _C = (float) ((1f-_R-_K) / (1f-_K));
		float _M = (float) ((1f-_G-_K) / (1f-_K));
		float _Y = (float) ((1f-_B-_K) / (1f-_K));

		return PdfCmykColor.from(_C, _M, _Y, _K);
	}

	public static PdfCmykColor from(int _r, int _g, int _b)
	{
		float _R = _r/255f;
		float _G = _g/255f;
		float _B = _b/255f;

		float _K = 1f-Math.max(_R, Math.max(_G, _B));
		float _C = (1f-_R-_K) / (1f-_K);
		float _M = (1f-_G-_K) / (1f-_K);
		float _Y = (1f-_B-_K) / (1f-_K);

		return PdfCmykColor.from(_C, _M, _Y, _K);
	}

	public static PdfCmykColor from(float _c, float _m, float _y, float _k)
	{
		PdfCmykColor _n = new PdfCmykColor();
		_n.cyan = _c;
		_n.magenta = _m;
		_n.yellow = _y;
		_n.black = _k;
		return _n;
	}
	
	public float getCyan()
	{
		return cyan;
	}
	
	public void setCyan(float cyan)
	{
		this.cyan = cyan;
	}
	
	public float getMagenta()
	{
		return magenta;
	}
	
	public void setMagenta(float magenta)
	{
		this.magenta = magenta;
	}
	
	public float getYellow()
	{
		return yellow;
	}
	
	public void setYellow(float yellow)
	{
		this.yellow = yellow;
	}
	
	public float getBlack()
	{
		return black;
	}
	
	public void setBlack(float black)
	{
		this.black = black;
	}
	
	@Override
	public void setFillColor(AbstractPdfContent _content)
	{
		_content.fillColorCMYK(this.getCyan(), this.getMagenta(), this.getYellow(), this.getBlack());
	}
	
	@Override
	public void setStrokeColor(AbstractPdfContent _content)
	{
		_content.strokeColorCMYK(this.getCyan(), this.getMagenta(), this.getYellow(), this.getBlack());
	}
	
	public PdfRgbColor toRgb()
	{
		return PdfRgbColor.from(this);
	}
	
	@Override
	public float[] getComponents()
	{
		return new float[]{ cyan, magenta, yellow, black };
	}
	
	@Override
	public PdfColorSpace getColorSpace(PdfDocument doc)
	{
		return PdfCmykColorSpace.getDeviceCmyk(doc);
	}
}
