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
import com.github.terefang.jmelange.pdf.core.PdfValue;

import java.io.IOException;
import java.io.OutputStream;

public class PdfPages extends PdfDictObject
{
	PdfArray<PdfValue> kids;
	public static PdfPages create(PdfDocument doc, PdfDictObject _parent)
	{
		return new PdfPages(doc, _parent);
	}
	
	public PdfPages(PdfDocument doc, PdfDictObject _parent)
	{
		super(doc);
		this.kids = new PdfArray();
		this.set("Type", PdfName.of("Pages"));
		this.set("Count", PdfNum.of(0));
		if(_parent!=null)
			this.set("Parent", _parent);
	}

	public void add(PdfPage page)
	{
		this.kids.add(page);
	}

	public void insert(int _idx, PdfPage page)
	{
		this.kids.add(_idx, page);
	}

	public void prepend(PdfPage page)
	{
		this.kids.prepend(page);
	}

	public void add(PdfPages page)
	{
		this.kids.add(page);
	}

	public void insert(int _idx, PdfPages pages)
	{
		this.kids.add(_idx, pages);
	}

	public void prepend(PdfPages page)
	{
		this.kids.prepend(page);
	}

	@Override
	public void writeTo(OutputStream os) throws IOException
	{
		this.set("Kids", this.kids);
		this.set("Count", PdfNum.of(pageCount()));
		super.writeTo(os);
	}

	public int pageCount()
	{
		int _count = 0;
		for(PdfValue _item : this.kids.asList())
		{
			if(_item instanceof PdfPage)
			{
				_count++;
			}
			else
			if(_item instanceof PdfPages)
			{
				_count+= ((PdfPages)_item).pageCount();
			}
		}
		return _count;
	}

	public PdfPage newPage()
	{
		PdfPage p = PdfPage.create(this.getDoc(), this);
		this.add(p);
		return p;
	}

	public PdfPages newPages()
	{
		PdfPages p = PdfPages.create(this.getDoc(), this);
		this.add(p);
		return p;
	}

	public PdfPage newPageInsert(int _index)
	{
		PdfPage p = PdfPage.create(this.getDoc(), this);
		this.insert(_index, p);
		return p;
	}

	public PdfPages newPagesInsert(int _index)
	{
		PdfPages p = PdfPages.create(this.getDoc(), this);
		this.insert(_index, p);
		return p;
	}

	public PdfPage newPagePrepend()
	{
		PdfPage p = PdfPage.create(this.getDoc(), this);
		this.prepend(p);
		return p;
	}

	public PdfPages newPagesPrepend()
	{
		PdfPages p = PdfPages.create(this.getDoc(), this);
		this.prepend(p);
		return p;
	}
}
