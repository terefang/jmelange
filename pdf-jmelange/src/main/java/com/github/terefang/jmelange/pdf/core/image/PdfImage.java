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
package com.github.terefang.jmelange.pdf.core.image;

import com.github.terefang.jmelange.commons.util.ImgUtil;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.PdfResRef;
import com.github.terefang.jmelange.pdf.core.content.AbstractPdfContent;
import com.github.terefang.jmelange.pdf.core.values.PdfName;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.pdf.core.values.PdfResource;
import com.github.terefang.jmelange.pdf.core.values.PdfXObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public abstract class PdfImage extends PdfXObject implements PdfResRef
{
	public static class PdfImageResource extends PdfResource<PdfImage>
	{
		public PdfImageResource(PdfImage _xo, String _prefix)
		{
			super(_prefix, "XObject");
			set(_xo);
			_xo.setName(this.getResName());
		}
	}

	public static final PdfResource createResource(PdfImage _f)
	{
		return new PdfImageResource(_f, "I");
	}

	PdfResource _res;
	@Override
	public PdfResource getResource()
	{
		if(_res==null)
		{
			_res = createResource(this);
		}
		return _res;
	}

	public PdfImage(PdfDocument doc)
	{
		super(doc, true);
		this.set("Subtype", PdfName.of("Image"));
		//this.set("Name", PdfName.of(this.getResName()));
	}

	public static PdfImage ofx(PdfPage page, Image img, ImageObserver obs)
	{
		return of(page, img, obs, 0);
	}

	public static PdfImage of(PdfPage page, Image img, ImageObserver obs, int _rot)
	{
		return of(page, img, obs, "jpeg", false, 100f, _rot);
	}

	public static PdfImage of(PdfPage page, Image img, ImageObserver obs, String _compression, boolean _transparency, float _av, int _rot)
	{
		PdfAwtImage _image = PdfAwtImage.of(page.getDoc(),  ImgUtil.toBufferedImage(img, obs), _compression, _transparency, _transparency, _av, _rot);
		page.addXObject(_image.getResource().getResName(), _image);;
		return _image;
	}

	public static PdfImage of(AbstractPdfContent page, Image img, ImageObserver obs, String _compression, boolean _transparency, float _av, int _rot)
	{
		PdfAwtImage _image = PdfAwtImage.of(page.getDoc(), ImgUtil.toBufferedImage(img, obs), _compression, _transparency, _transparency, _av, _rot);
		page.addXObject(_image.getResource().getResName(), _image);
		return _image;
	}



	public abstract double getWidth();
	public abstract double getHeight();
}
