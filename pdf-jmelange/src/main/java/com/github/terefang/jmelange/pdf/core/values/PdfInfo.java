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

import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfInfo extends PdfDictObject
{
	public static PdfInfo create(PdfDocument doc) { return new PdfInfo(doc); }
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public PdfInfo(PdfDocument doc)
	{
		super(doc);
		this.setTitle(PDF.class.getName());
		this.set("ModDate", PdfString.of("D:"+sdf.format(new Date())));
		this.set("CreationDate", PdfString.of("D:"+sdf.format(new Date())));
		this.setCreator(PDF.class.getName());
		this.setAuthor(PDF.class.getName());
		this.setProducer(PDF.class.getName());
		this.setSubject(PDF.class.getName());
	}

	String subject;
	String creator;
	String author;
	String producer;
	String title;
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
		this.set("Title", PdfString.of(this.title));
	}
	
	public String getCreator()
	{
		return creator;
	}
	
	public void setCreator(String creator)
	{
		this.creator = creator;
		this.set("Creator", PdfString.of(this.creator));
	}
	
	public String getAuthor()
	{
		return author;
	}
	
	public void setAuthor(String author)
	{
		this.author = author;
		this.set("Author", PdfString.of(this.author));
	}
	
	public String getProducer()
	{
		return producer;
	}
	
	public void setProducer(String producer)
	{
		this.producer = producer;
		this.set("Producer", PdfString.of(this.producer));
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
		this.set("Subject", PdfString.of(subject));
	}

	public String getSubject() {
		return subject;
	}

	public void setKeywords(String words)
	{
		this.set("Keywords", PdfString.of(words));
	}
}
