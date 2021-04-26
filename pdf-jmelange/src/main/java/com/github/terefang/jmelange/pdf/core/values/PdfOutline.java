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

import com.github.terefang.jmelange.pdf.core.PdfDocument;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Vector;

public class PdfOutline extends PdfDictObject
{
	public PdfOutline(PdfDocument doc)
	{
		super(doc);
	}
	
	List<PdfOutline> outlineList=new Vector<>();
	
	public static PdfOutline of(PdfDocument doc, PdfOutline pdfOutlines, String text, PdfPage page)
	{
		PdfOutline _outline = new PdfOutline(doc);
		_outline.setParent(pdfOutlines);
		_outline.setTitle(text);
		_outline.setDest(page);
		return _outline;
	}
	
	public void setDest(PdfPage page)
	{
		this.set("Dest", PdfArray.from(page, PdfName.of("Fit")));
	}
	
	public void setTitle(String _t)
	{
		this.set("Title", PdfDocString.of(_t.replaceAll("\\s+", " ")));
	}
	
	public void setParent(PdfDictObject _o)
	{
		this.set("Parent", _o);
	}
	
	public void setNext(PdfOutline _outline)
	{
		this.set("Next", _outline);
	}
	
	public void setPrev(PdfOutline _outline)
	{
		this.set("Prev", _outline);
	}

	public PdfOutlineAnchor add(String _text)
	{
		PdfOutlineAnchor _outline = PdfOutlineAnchor.of(getDoc(), this, _text);
		if(outlineList.size()>0)
		{
			PdfOutline _last = outlineList.get(outlineList.size()-1);
			_last.setNext(_outline);
			_outline.setPrev(_last);
		}
		outlineList.add(_outline);
		return _outline;
	}

	public PdfOutline add(String _text, PdfPage _page)
	{
		if(_page==null)
		{
			return this.add(_text);
		}

		PdfOutline  _outline = PdfOutline.of(getDoc(), this, _text, _page);
		if(outlineList.size()>0)
		{
			PdfOutline _last = outlineList.get(outlineList.size()-1);
			_last.setNext(_outline);
			_outline.setPrev(_last);
		}
		outlineList.add(_outline);
		return _outline;
	}
	
	@Override
	public void writeHeaderTo(OutputStream os) throws IOException
	{
		if(outlineList.size()>0)
		{
			this.set("First", outlineList.get(0));
			this.set("Last", outlineList.get(outlineList.size() - 1));
			//this.set("Count", PdfNum.of(outlineList.size()));
		}
		super.writeHeaderTo(os);
	}
}
