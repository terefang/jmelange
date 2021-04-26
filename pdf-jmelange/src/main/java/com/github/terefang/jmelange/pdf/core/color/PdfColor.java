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

import java.awt.*;

public interface PdfColor
{
	public static final PdfRgbColor RED = PdfColor.from(255, 0, 0);
	public static final PdfRgbColor INDIANRED = PdfColor.from(205, 92, 92);
	public static final PdfRgbColor LIGHTCORAL = PdfColor.from(240, 128, 128);
	public static final PdfRgbColor SALMON = PdfColor.from(250, 128, 114);
	public static final PdfRgbColor DARKSALMON = PdfColor.from(233, 150, 122);
	public static final PdfRgbColor LIGHTSALMON = PdfColor.from(255, 160, 122);
	public static final PdfRgbColor CRIMSON = PdfColor.from(220, 20, 60);
	public static final PdfRgbColor FIREBRICK = PdfColor.from(178, 34, 34);
	public static final PdfRgbColor DARKRED = PdfColor.from(139, 0, 0);
	public static final PdfRgbColor PINK = PdfColor.from(255, 192, 203);
	public static final PdfRgbColor LIGHTPINK = PdfColor.from(255, 182, 193);
	public static final PdfRgbColor HOTPINK = PdfColor.from(255, 105, 180);
	public static final PdfRgbColor DEEPPINK = PdfColor.from(255, 20, 147);
	public static final PdfRgbColor MEDIUMVIOLETRED = PdfColor.from(199, 21, 133);
	public static final PdfRgbColor PALEVIOLETRED = PdfColor.from(219, 112, 147);
	public static final PdfRgbColor CORAL = PdfColor.from(255, 127, 80);
	public static final PdfRgbColor TOMATO = PdfColor.from(255, 99, 71);
	public static final PdfRgbColor ORANGERED = PdfColor.from(255, 69, 0);
	public static final PdfRgbColor DARKORANGE = PdfColor.from(255, 140, 0);
	public static final PdfRgbColor ORANGE = PdfColor.from(255, 165, 0);
	public static final PdfRgbColor GOLD = PdfColor.from(255, 215, 0);
	public static final PdfRgbColor YELLOW = PdfColor.from(255, 255, 0);
	public static final PdfRgbColor LIGHTYELLOW = PdfColor.from(255, 255, 224);
	public static final PdfRgbColor LEMONCHIFFON = PdfColor.from(255, 250, 205);
	public static final PdfRgbColor LIGHTGOLDENRODYELLOW = PdfColor.from(250, 250, 210);
	public static final PdfRgbColor PAPAYAWHIP = PdfColor.from(255, 239, 213);
	public static final PdfRgbColor MOCCASIN = PdfColor.from(255, 228, 181);
	public static final PdfRgbColor PEACHPUFF = PdfColor.from(255, 218, 185);
	public static final PdfRgbColor PALEGOLDENROD = PdfColor.from(238, 232, 170);
	public static final PdfRgbColor KHAKI = PdfColor.from(240, 230, 140);
	public static final PdfRgbColor DARKKHAKI = PdfColor.from(189, 183, 107);
	public static final PdfRgbColor LAVENDER = PdfColor.from(230, 230, 250);
	public static final PdfRgbColor THISTLE = PdfColor.from(216, 191, 216);
	public static final PdfRgbColor PLUM = PdfColor.from(221, 160, 221);
	public static final PdfRgbColor VIOLET = PdfColor.from(238, 130, 238);
	public static final PdfRgbColor ORCHID = PdfColor.from(218, 112, 214);
	public static final PdfRgbColor FUCHSIA = PdfColor.from(255, 0, 255);
	public static final PdfRgbColor MAGENTA = PdfColor.from(255, 0, 255);
	public static final PdfRgbColor MEDIUMORCHID = PdfColor.from(186, 85, 211);
	public static final PdfRgbColor MEDIUMPURPLE = PdfColor.from(147, 112, 219);
	public static final PdfRgbColor REBECCAPURPLE = PdfColor.from(102, 51, 153);
	public static final PdfRgbColor BLUEVIOLET = PdfColor.from(138, 43, 226);
	public static final PdfRgbColor DARKVIOLET = PdfColor.from(148, 0, 211);
	public static final PdfRgbColor DARKORCHID = PdfColor.from(153, 50, 204);
	public static final PdfRgbColor DARKMAGENTA = PdfColor.from(139, 0, 139);
	public static final PdfRgbColor PURPLE = PdfColor.from(128, 0, 128);
	public static final PdfRgbColor INDIGO = PdfColor.from(75, 0, 130);
	public static final PdfRgbColor SLATEBLUE = PdfColor.from(106, 90, 205);
	public static final PdfRgbColor DARKSLATEBLUE = PdfColor.from(72, 61, 139);
	public static final PdfRgbColor MEDIUMSLATEBLUE = PdfColor.from(123, 104, 238);
	public static final PdfRgbColor GREENYELLOW = PdfColor.from(173, 255, 47);
	public static final PdfRgbColor CHARTREUSE = PdfColor.from(127, 255, 0);
	public static final PdfRgbColor LAWNGREEN = PdfColor.from(124, 252, 0);
	public static final PdfRgbColor LIME = PdfColor.from(0, 255, 0);
	public static final PdfRgbColor LIMEGREEN = PdfColor.from(50, 205, 50);
	public static final PdfRgbColor PALEGREEN = PdfColor.from(152, 251, 152);
	public static final PdfRgbColor LIGHTGREEN = PdfColor.from(144, 238, 144);
	public static final PdfRgbColor MEDIUMSPRINGGREEN = PdfColor.from(0, 250, 154);
	public static final PdfRgbColor SPRINGGREEN = PdfColor.from(0, 255, 127);
	public static final PdfRgbColor MEDIUMSEAGREEN = PdfColor.from(60, 179, 113);
	public static final PdfRgbColor SEAGREEN = PdfColor.from(46, 139, 87);
	public static final PdfRgbColor FORESTGREEN = PdfColor.from(34, 139, 34);
	public static final PdfRgbColor GREEN = PdfColor.from(0, 128, 0);
	public static final PdfRgbColor DARKGREEN = PdfColor.from(0, 100, 0);
	public static final PdfRgbColor YELLOWGREEN = PdfColor.from(154, 205, 50);
	public static final PdfRgbColor OLIVEDRAB = PdfColor.from(107, 142, 35);
	public static final PdfRgbColor OLIVE = PdfColor.from(128, 128, 0);
	public static final PdfRgbColor DARKOLIVEGREEN = PdfColor.from(85, 107, 47);
	public static final PdfRgbColor MEDIUMAQUAMARINE = PdfColor.from(102, 205, 170);
	public static final PdfRgbColor DARKSEAGREEN = PdfColor.from(143, 188, 139);
	public static final PdfRgbColor LIGHTSEAGREEN = PdfColor.from(32, 178, 170);
	public static final PdfRgbColor DARKCYAN = PdfColor.from(0, 139, 139);
	public static final PdfRgbColor TEAL = PdfColor.from(0, 128, 128);
	public static final PdfRgbColor AQUA = PdfColor.from(0, 255, 255);
	public static final PdfRgbColor CYAN = PdfColor.from(0, 255, 255);
	public static final PdfRgbColor LIGHTCYAN = PdfColor.from(224, 255, 255);
	public static final PdfRgbColor PALETURQUOISE = PdfColor.from(175, 238, 238);
	public static final PdfRgbColor AQUAMARINE = PdfColor.from(127, 255, 212);
	public static final PdfRgbColor TURQUOISE = PdfColor.from(64, 224, 208);
	public static final PdfRgbColor MEDIUMTURQUOISE = PdfColor.from(72, 209, 204);
	public static final PdfRgbColor DARKTURQUOISE = PdfColor.from(0, 206, 209);
	public static final PdfRgbColor CADETBLUE = PdfColor.from(95, 158, 160);
	public static final PdfRgbColor STEELBLUE = PdfColor.from(70, 130, 180);
	public static final PdfRgbColor LIGHTSTEELBLUE = PdfColor.from(176, 196, 222);
	public static final PdfRgbColor POWDERBLUE = PdfColor.from(176, 224, 230);
	public static final PdfRgbColor LIGHTBLUE = PdfColor.from(173, 216, 230);
	public static final PdfRgbColor SKYBLUE = PdfColor.from(135, 206, 235);
	public static final PdfRgbColor LIGHTSKYBLUE = PdfColor.from(135, 206, 250);
	public static final PdfRgbColor DEEPSKYBLUE = PdfColor.from(0, 191, 255);
	public static final PdfRgbColor DODGERBLUE = PdfColor.from(30, 144, 255);
	public static final PdfRgbColor CORNFLOWERBLUE = PdfColor.from(100, 149, 237);
	public static final PdfRgbColor ROYALBLUE = PdfColor.from(65, 105, 225);
	public static final PdfRgbColor BLUE = PdfColor.from(0, 0, 255);
	public static final PdfRgbColor MEDIUMBLUE = PdfColor.from(0, 0, 205);
	public static final PdfRgbColor DARKBLUE = PdfColor.from(0, 0, 139);
	public static final PdfRgbColor NAVY = PdfColor.from(0, 0, 128);
	public static final PdfRgbColor MIDNIGHTBLUE = PdfColor.from(25, 25, 112);
	public static final PdfRgbColor CORNSILK = PdfColor.from(255, 248, 220);
	public static final PdfRgbColor BLANCHEDALMOND = PdfColor.from(255, 235, 205);
	public static final PdfRgbColor BISQUE = PdfColor.from(255, 228, 196);
	public static final PdfRgbColor NAVAJOWHITE = PdfColor.from(255, 222, 173);
	public static final PdfRgbColor WHEAT = PdfColor.from(245, 222, 179);
	public static final PdfRgbColor BURLYWOOD = PdfColor.from(222, 184, 135);
	public static final PdfRgbColor TAN = PdfColor.from(210, 180, 140);
	public static final PdfRgbColor ROSYBROWN = PdfColor.from(188, 143, 143);
	public static final PdfRgbColor SANDYBROWN = PdfColor.from(244, 164, 96);
	public static final PdfRgbColor GOLDENROD = PdfColor.from(218, 165, 32);
	public static final PdfRgbColor DARKGOLDENROD = PdfColor.from(184, 134, 11);
	public static final PdfRgbColor PERU = PdfColor.from(205, 133, 63);
	public static final PdfRgbColor CHOCOLATE = PdfColor.from(210, 105, 30);
	public static final PdfRgbColor SADDLEBROWN = PdfColor.from(139, 69, 19);
	public static final PdfRgbColor SIENNA = PdfColor.from(160, 82, 45);
	public static final PdfRgbColor BROWN = PdfColor.from(165, 42, 42);
	public static final PdfRgbColor MAROON = PdfColor.from(128, 0, 0);
	public static final PdfRgbColor WHITE = PdfColor.from(255, 255, 255);
	public static final PdfRgbColor SNOW = PdfColor.from(255, 250, 250);
	public static final PdfRgbColor HONEYDEW = PdfColor.from(240, 255, 240);
	public static final PdfRgbColor MINTCREAM = PdfColor.from(245, 255, 250);
	public static final PdfRgbColor AZURE = PdfColor.from(240, 255, 255);
	public static final PdfRgbColor ALICEBLUE = PdfColor.from(240, 248, 255);
	public static final PdfRgbColor GHOSTWHITE = PdfColor.from(248, 248, 255);
	public static final PdfRgbColor WHITESMOKE = PdfColor.from(245, 245, 245);
	public static final PdfRgbColor SEASHELL = PdfColor.from(255, 245, 238);
	public static final PdfRgbColor BEIGE = PdfColor.from(245, 245, 220);
	public static final PdfRgbColor OLDLACE = PdfColor.from(253, 245, 230);
	public static final PdfRgbColor FLORALWHITE = PdfColor.from(255, 250, 240);
	public static final PdfRgbColor IVORY = PdfColor.from(255, 255, 240);
	public static final PdfRgbColor ANTIQUEWHITE = PdfColor.from(250, 235, 215);
	public static final PdfRgbColor LINEN = PdfColor.from(250, 240, 230);
	public static final PdfRgbColor LAVENDERBLUSH = PdfColor.from(255, 240, 245);
	public static final PdfRgbColor MISTYROSE = PdfColor.from(255, 228, 225);
	public static final PdfRgbColor GAINSBORO = PdfColor.from(220, 220, 220);
	public static final PdfRgbColor LIGHTGRAY = PdfColor.from(211, 211, 211);
	public static final PdfRgbColor SILVER = PdfColor.from(192, 192, 192);
	public static final PdfRgbColor DARKGRAY = PdfColor.from(169, 169, 169);
	public static final PdfRgbColor GRAY = PdfColor.from(128, 128, 128);
	public static final PdfRgbColor DIMGRAY = PdfColor.from(105, 105, 105);
	public static final PdfRgbColor LIGHTSLATEGRAY = PdfColor.from(119, 136, 153);
	public static final PdfRgbColor SLATEGRAY = PdfColor.from(112, 128, 144);
	public static final PdfRgbColor DARKSLATEGRAY = PdfColor.from(47, 79, 79);
	public static final PdfRgbColor BLACK = PdfColor.from(0, 0, 0);
	
	public static PdfRgbColor from(Color _color)
	{
		return PdfRgbColor.from(_color.getRed(), _color.getGreen(), _color.getBlue());
	}
	
	public static PdfRgbColor from(int _r, int _g, int _b)
	{
		return PdfRgbColor.from(_r, _g, _b);
	}
	
	public static PdfCmykColor from(float _c, float _m, float _y, float _k)
	{
		return PdfCmykColor.from(_c, _m, _y, _k);
	}
	
	public float[] getComponents();
	public PdfColorSpace getColorSpace(PdfDocument doc);
	public void setFillColor(AbstractPdfContent _content);
	public void setStrokeColor(AbstractPdfContent _content);

	public static PdfRgbColor fromHSL(float h, float s, float l)
	{
		h = h % 360.0f;
		h /= 360f;
		s /= 100f;
		l /= 100f;

		float q = 0;

		if (l < 0.5)
			q = l * (1 + s);
		else
			q = (l + s) - (s * l);

		float p = 2 * l - q;

		float r = Math.max(0, HueToRGB(p, q, h + (1.0f / 3.0f)));
		float g = Math.max(0, HueToRGB(p, q, h));
		float b = Math.max(0, HueToRGB(p, q, h - (1.0f / 3.0f)));

		r = Math.min(r, 1.0f)*255;
		g = Math.min(g, 1.0f)*255;
		b = Math.min(b, 1.0f)*255;

		return from((int)r, (int)g, (int)b);
	}

	static float HueToRGB(float p, float q, float h) {
		if (h < 0)
			h += 1;

		if (h > 1)
			h -= 1;

		if (6 * h < 1) {
			return p + ((q - p) * 6 * h);
		}

		if (2 * h < 1) {
			return q;
		}

		if (3 * h < 2) {
			return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
		}

		return p;
	}

	public static PdfRgbColor fromHSV(float h, float s, float v) {
		h /= 60f;
		s /= 100f;
		v /= 100f;
		int hi = (int) (Math.floor(h) % 6);

		float f = (float) (h - Math.floor(h));
		int p = (int) (255 * v * (1 - s));
		int q = (int) (255 * v * (1 - (s * f)));
		int t = (int) (255 * v * (1 - (s * (1 - f))));
		v *= 255;

		switch (hi) {
			case 1:
				return from(q, (int)v, p);
			case 2:
				return from(p, (int)v, t);
			case 3:
				return from(p, q, (int)v);
			case 4:
				return from(t, p, (int)v);
			case 5:
				return from((int)v, p, q);
			case 0:
			default:
				return from((int)v, t, p);
		}
	}

	// http://dev.w3.org/csswg/css-color/#hwb-to-rgb
	public static PdfRgbColor fromHWB(float h, float wh, float bl)
	{
		h /= 360f;
		wh /= 100f;
		bl /= 100f;

		float ratio = wh + bl;
		int i;
		float v;
		float f;
		float n;

		float r;
		float g;
		float b;

		// wh + bl cant be > 1
		if (ratio > 1) {
			wh /= ratio;
			bl /= ratio;
		}

		i = (int) Math.floor(6 * h);
		v = 1 - bl;
		f = 6 * h - i;

		if ((i & 0x01) != 0)
		{
			f = 1 - f;
		}

		n = wh + f * (v - wh); // linear interpolation

		switch (i) {
			default:
			case 6:
			case 0: r = v; g = n; b = wh; break;
			case 1: r = n; g = v; b = wh; break;
			case 2: r = wh; g = v; b = n; break;
			case 3: r = wh; g = n; b = v; break;
			case 4: r = n; g = wh; b = v; break;
			case 5: r = v; g = wh; b = n; break;
		}

		return from((int)(r * 255), (int)(g * 255), (int)(b * 255));
	}

	public static PdfRgbColor fromHCG(float h, float c, float g)
	{
		h /= 360f;
		c /= 100f;
		g /= 100f;

		if (c == 0.0) {
			return from((int)(g * 255), (int)(g * 255), (int)(g * 255));
		}

		float[] pure = new float[3];
		float hi = (h % 1) * 6;
		float v = hi % 1;
		float w = 1 - v;
		float mg = 0;

		switch ((int) Math.floor(hi)) {
			case 0:
				pure[0] = 1; pure[1] = v; pure[2] = 0; break;
			case 1:
				pure[0] = w; pure[1] = 1; pure[2] = 0; break;
			case 2:
				pure[0] = 0; pure[1] = 1; pure[2] = v; break;
			case 3:
				pure[0] = 0; pure[1] = w; pure[2] = 1; break;
			case 4:
				pure[0] = v; pure[1] = 0; pure[2] = 1; break;
			default:
				pure[0] = 1; pure[1] = 0; pure[2] = w;
		}

		mg = (1f - c) * g;

		return from(
				(int)((c * pure[0] + mg) * 255),
				(int)((c * pure[1] + mg) * 255),
				(int)((c * pure[2] + mg) * 255)
		);
	}

	public static PdfRgbColor fromXYZ(float x, float y, float z)
	{
		x /= 100f;
		y /= 100f;
		z /= 100f;
		float r;
		float g;
		float b;

		r = (x * 3.2406f) + (y * -1.5372f) + (z * -0.4986f);
		g = (x * -0.9689f) + (y * 1.8758f) + (z * 0.0415f);
		b = (x * 0.0557f) + (y * -0.2040f) + (z * 1.0570f);

		// assume sRGB
		r = r > 0.0031308f
				? (float) ((1.055f * Math.pow(r, 1.0f / 2.4f)) - 0.055f)
				: r * 12.92f;

		g = g > 0.0031308f
				? (float) ((1.055f * Math.pow(g, 1.0f / 2.4f)) - 0.055f)
				: g * 12.92f;

		b = b > 0.0031308f
				? (float) ((1.055f * Math.pow(b, 1.0f / 2.4f)) - 0.055f)
				: b * 12.92f;

		r = Math.min(Math.max(0, r), 1);
		g = Math.min(Math.max(0, g), 1);
		b = Math.min(Math.max(0, b), 1);

		return from(
				(int)(r * 255),
				(int)(g * 255),
				(int)(b * 255)
		);
	}

	public static PdfRgbColor fromLAB(float l, float a, float b)
	{
		float x;
		float y;
		float z;

		y = (l + 16f) / 116f;
		x = a / 500f + y;
		z = y - b / 200f;

		float y2 = (float) Math.pow(y, 3);
		float x2 = (float) Math.pow(x, 3);
		float z2 = (float) Math.pow(z, 3);
		y = y2 > 0.008856f ? y2 : (y - 16f / 116f) / 7.787f;
		x = x2 > 0.008856f ? x2 : (x - 16f / 116f) / 7.787f;
		z = z2 > 0.008856f ? z2 : (z - 16f / 116f) / 7.787f;

		x *= 95.047f;
		y *= 100f;
		z *= 108.883f;

		return fromXYZ(x, y, z);
	}

	public static PdfRgbColor fromLCH(float l, float c, float h)
	{
		float hr = (float) (h / 360f * 2f * Math.PI);
		float a = (float) (c * Math.cos(hr));
		float b = (float) (c * Math.sin(hr));

		return fromLAB(l, a, b);
	}

}
