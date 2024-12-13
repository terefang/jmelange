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
package com.github.terefang.jmelange.pdf.core;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.pdf.core.color.PdfCmykColorSpace;
import com.github.terefang.jmelange.pdf.core.color.PdfColorSpace;
import com.github.terefang.jmelange.pdf.core.color.PdfGrayColorSpace;
import com.github.terefang.jmelange.pdf.core.color.PdfRgbColorSpace;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.encoding.PdfEncoding;
import com.github.terefang.jmelange.pdf.core.fonts.*;
import com.github.terefang.jmelange.pdf.core.image.PdfAwtImage;
import com.github.terefang.jmelange.pdf.core.image.PdfImage;
import com.github.terefang.jmelange.commons.loader.*;
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.pdf.core.util.CountingOutputStream;
import com.github.terefang.jmelange.commons.util.FontUtil;
import com.github.terefang.jmelange.pdf.core.values.*;
import com.github.terefang.jmelange.pdf.core.values.PdfFormXObject;
import com.github.terefang.jmelange.pdf.core.values.PdfOutline;
import com.github.terefang.jmelange.pdf.core.values.PdfOutlines;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class PdfDocument
{
	private final PdfPages pagetree;
	private final PdfPages pagesFront;
	private final PdfPages pages;
	private final PdfPages pagesBack;

	List<AbstractPdfObject> objects = new Vector();
	AtomicInteger seq = new AtomicInteger(0);
	public Map<String,PdfOptionalContentGroup> layers = new HashMap<>();
	String version = "1.5";
	private PdfOutlines outlines;
	int pdfxConformance = PDF.PDFA_SRGB;

	int iccProfileType = 0;
	ResourceLoader iccProfileData = null;

	public int getIccProfileType() {
		return iccProfileType;
	}

	public void setIccProfileType(int iccProfileType) {
		this.iccProfileType = iccProfileType;
	}

	public ResourceLoader getIccProfileData() {
		return iccProfileData;
	}

	public void setIccProfileData(ResourceLoader iccProfileData) {
		this.iccProfileData = iccProfileData;
	}

	public void setIccProfile(int _type, ResourceLoader _data)
	{
		this.iccProfileType = _type;
		this.iccProfileData = _data;
	}

	public int getPdfxConformance() {
		return pdfxConformance;
	}

	public void setPdfxConformance(int pdfxConformance) {
		this.pdfxConformance = pdfxConformance;
	}

	float jpegCompression = 0.1f;
	private PdfDictObject dests;
	private PdfDictObject ocg;
	private PdfArray<PdfValue> ocgs;

	public float getJpegCompression() {
		return jpegCompression;
	}

	public void setJpegCompression(float jpegCompression) {
		this.jpegCompression = jpegCompression;
	}

	public PdfObjRef add(AbstractPdfObject object)
	{
		object.getRef().setValue(seq.incrementAndGet());
		this.objects.add(object);
		return object.getRef();
	}
	
	public static PdfDocument create()
	{
		return new PdfDocument();
	}
	
	public PdfDocument()
	{
		this.root = PdfCatalog.create(this);
		this.info = PdfInfo.create(this);
		this.pagetree = PdfPages.create(this, null);
		this.root.set("Pages", this.pagetree);

		// front matter
		this.pagesFront = this.pagetree.newPages();
		// body matter
		this.pages = this.pagetree.newPages();
		// back matter
		this.pagesBack = this.pagetree.newPages();
	}

	public int countPageTotal()
	{
		return this.countPage()
			+this.countPageFront()
			+this.countPageBack();
	}

	public int countPage()
	{
		return this.pages.pageCount();
	}

	public int countPageFront()
	{
		return this.pagesFront.pageCount();
	}

	public int countPageBack()
	{
		return this.pagesFront.pageCount();
	}

	public PdfPage newPage()
	{
		return this.pages.newPage();
	}

	public PdfPage newPageFront()
	{
		return this.pagesFront.newPage();
	}

	public PdfPage newPageBack()
	{
		return this.pagesFront.newPage();
	}

	public PdfPage newPageInsert(int _idx)
	{
		return this.pages.newPage();
	}

	public PdfPage newPageFrontInsert(int _idx)
	{
		return this.pagesFront.newPage();
	}

	public PdfPage newPageBackInsert(int _idx)
	{
		return this.pagesFront.newPage();
	}

	public PdfPage newPagePrepended()
	{
		return this.pages.newPagePrepend();
	}

	public PdfPage newPageFrontPrepended()
	{
		return this.pagesFront.newPagePrepend();
	}

	public PdfPage newPageBackPrepended()
	{
		return this.pagesFront.newPagePrepend();
	}

	public PdfOutline newOutline(String text, PdfPage _ref)
	{
		if(this.outlines==null)
		{
			this.outlines = new PdfOutlines(this);
			this.root.set("Outlines", this.outlines);
		}
		return this.outlines.add(text, _ref);
	}

	public void newNamedDestination(String text, PdfPage _ref)
	{
		if(this.dests==null)
		{
			this.dests = new PdfDictObject(this);
			this.root.set("Dests", this.dests);
		}
		this.dests.set(PDF.normalizeName(text), PdfArray.from(_ref, PdfName.of("Fit")));
	}

	public void newOCGroup(PdfOptionalContentGroup _cg)
	{
		if(this.ocg==null)
		{
			this.ocg = new PdfDictObject(this);
			this.ocgs = new PdfArray<>();
			this.ocg.set("OCGs", this.ocgs);

			PdfDictObject _state = new PdfDictObject(this);
			//_state.set("OFF", PdfArray.create());
			_state.set("Order", this.ocgs);
			_state.set("RBGroups", PdfArray.create());
			this.ocg.set("D", _state);
			this.root.set("OCProperties", this.ocg);
		}
		this.ocgs.add(_cg);
	}

	/*
	/OCProperties <<
		/D <<
			/OFF[]
			/Order[]
			/RBGroups[]
			>>
		/OCGs[683 0 R 684 0 R 685 0 R]>> */

	PdfCatalog root;
	PdfName pageMode;
	PdfInfo info;

	public PdfInfo getInfo() {
		return info;
	}

	public void setInfo(PdfInfo info) {
		this.info = info;
	}

	public void setPageMode(String mode)
	{
		this.pageMode = PdfName.of(mode);
	}
	
	public void streamBegin(final String _path) throws IOException
	{
		streamBegin(new File(_path));
	}
	
	public void streamBegin(final File _file) throws IOException
	{
		BufferedOutputStream _fh = new BufferedOutputStream(new FileOutputStream(_file), 8192);
		streamBegin(_fh);
	}
	
	CountingOutputStream streamCountingOutputStream = null;
	
	public void streamBegin(final OutputStream os) throws IOException
	{
		log.info("Starting to write stream ...");
		this.streamCountingOutputStream = new CountingOutputStream(os);

		if(this.pageMode!=null)
		{
			this.root.set("PageMode", this.pageMode);
		}
		
		/* write header */
		this.streamCountingOutputStream.write(("%PDF-"+this.version+"\n%").getBytes());
		this.streamCountingOutputStream.write(0xC5);
		this.streamCountingOutputStream.write(0xD6);
		this.streamCountingOutputStream.write(0xE7);
		this.streamCountingOutputStream.write(0xF8);
		this.streamCountingOutputStream.write("\n%% ---- START DOCUMENT ----\n".getBytes());
		this.streamCountingOutputStream.flush();
	}

	boolean useXrefStream;

	public boolean isUseXrefStream() {
		return useXrefStream;
	}

	public void setUseXrefStream(boolean useXrefStream) {
		this.useXrefStream = useXrefStream;
	}

	public void streamEnd(boolean _close) throws IOException
	{
		// collect unstreamed objects
		log.info("Flushing unwritten objects ...");
		int _count = 0;
		for(int j = 0; j<this.objects.size(); j++)
		{
			AbstractPdfObject obj = this.objects.get(j);
			if(obj.getStreamOffset() == -1L)
			{
				obj.streamOut();
				_count++;
			}
		}
		int _size = this.objects.size();
		log.info("Flushed "+_count+" of "+_size+" ...");

		log.info("Writing xref ...");

		// writeXRef
		//this.streamCountingOutputStream.write(("\n\n%% ---- START XREF ----\n\n").getBytes());
		this.streamCountingOutputStream.flush();
		long _xref = this.streamOffset();
		String _md = PdfHex.encodeString(this.streamDigest());

		if(this.useXrefStream
				|| this.version.equalsIgnoreCase(PDF.VERSION_15)
				|| this.version.equalsIgnoreCase(PDF.VERSION_16)
				|| this.version.equalsIgnoreCase(PDF.VERSION_17)
				|| this.version.equalsIgnoreCase(PDF.VERSION_20))
		{
			this.streamCountingOutputStream.write((Integer.toString(_size+1)+" 0 obj <<\n").getBytes());
			this.streamCountingOutputStream.write(("/Type /XRef\n").getBytes());
			this.streamCountingOutputStream.write(("/ID [ <"+_md+"> <"+_md+">]\n").getBytes());
			this.streamCountingOutputStream.write(("/Size "+Integer.toString(this.objects.size()+1)+"\n").getBytes());
			this.streamCountingOutputStream.write(("/Root "+this.root.asString()+"\n").getBytes());
			this.streamCountingOutputStream.write(("/Info "+this.info.asString()+"\n").getBytes());
			if(_xref < Integer.MAX_VALUE)
			{
				this.streamCountingOutputStream.write(("/W [ 1 4 1 ]\n").getBytes());
				this.streamCountingOutputStream.write(("/Length "+Integer.toString((this.objects.size()+1)*6)+"\n").getBytes());
			}
			else
			{
				this.streamCountingOutputStream.write(("/W [ 1 8 1 ]\n").getBytes());
				this.streamCountingOutputStream.write(("/Length "+Integer.toString((this.objects.size()+1)*10)+"\n").getBytes());
			}
			this.streamCountingOutputStream.write((">> stream\n").getBytes());

			if(_xref < Integer.MAX_VALUE)
			{
				this.streamCountingOutputStream.write(ByteBuffer.allocate(6).order(ByteOrder.BIG_ENDIAN)
						.put((byte)0)
						.putInt(0)
						.put((byte)-1)
						.array());
				for(int j = 0; j<this.objects.size(); j++)
				{
					AbstractPdfObject obj = this.objects.get(j);
					this.streamCountingOutputStream.write(ByteBuffer.allocate(6).order(ByteOrder.BIG_ENDIAN)
							.put((byte)1)
							.putInt((int) obj.getStreamOffset())
							.put((byte)0)
							.array());
				}
			}
			else
			{
				this.streamCountingOutputStream.write(ByteBuffer.allocate(10).order(ByteOrder.BIG_ENDIAN)
						.put((byte)0)
						.putLong(0)
						.put((byte)-1)
						.array());
				for(int j = 0; j<this.objects.size(); j++)
				{
					AbstractPdfObject obj = this.objects.get(j);
					this.streamCountingOutputStream.write(ByteBuffer.allocate(10).order(ByteOrder.BIG_ENDIAN)
							.put((byte)1)
							.putLong(obj.getStreamOffset())
							.put((byte)0)
							.array());
				}
			}

			this.streamCountingOutputStream.write(("endstream\n").getBytes());
			this.streamCountingOutputStream.write(("endobj\n").getBytes());
		}
		else
		{
			this.streamCountingOutputStream.write(("xref\n0 "+Integer.toString(this.objects.size()+1)+"\n").getBytes());
			this.streamCountingOutputStream.write((xrefToString(0, 65535)).getBytes());

			for(int j = 0; j<this.objects.size(); j++)
			{
				AbstractPdfObject obj = this.objects.get(j);
				this.streamCountingOutputStream.write((xrefToString(obj.getStreamOffset(), 0)).getBytes());
			}
			this.streamCountingOutputStream.flush();

			// writeTrailer
			//this.streamCountingOutputStream.write(("%% ---- START TRAILER ----\n").getBytes());
			this.streamCountingOutputStream.write("trailer\n".getBytes());
			this.streamCountingOutputStream.write(("<<\n").getBytes());
			this.streamCountingOutputStream.write(("/Size "+Integer.toString(this.objects.size()+1)+"\n").getBytes());
			this.streamCountingOutputStream.write(("/Root "+this.root.asString()+"\n").getBytes());
			this.streamCountingOutputStream.write(("/Info "+this.info.asString()+"\n").getBytes());
			this.streamCountingOutputStream.write(("/ID [ <"+_md+"> <"+_md+">]\n").getBytes());
			this.streamCountingOutputStream.write((">>\n").getBytes());
		}
		this.streamCountingOutputStream.write(("startxref\n"+Long.toString(_xref)+"\n%%EOF\n").getBytes());
		this.streamCountingOutputStream.flush();
		
		if(_close) this.streamCountingOutputStream.close();

		log.info("Finished.");
	}

	public long streamOffset()
	{
		return this.streamCountingOutputStream.getCount();
	}

	public byte[] streamDigest()
	{
		return this.streamCountingOutputStream.getMessageDigest().digest();
	}

	public void streamOut(AbstractPdfObject _obj) throws IOException
	{
		if(_obj.getStreamOffset() == -1L)
		{
			long _off = this.streamOffset();
			_obj.writeTo(this.streamCountingOutputStream);
			_obj.setStreamOffset(_off);
			this.streamCountingOutputStream.flush();
		}
	}

	public void writeTo(final String _path) throws IOException
	{
		writeTo(new File(_path));
	}
	
	public void writeTo(final File _file) throws IOException
	{
		BufferedOutputStream _fh = new BufferedOutputStream(new FileOutputStream(_file));
		writeTo(_fh, false);
		_fh.close();
	}
	
	public void writeTo(final OutputStream _os) throws IOException
	{
		writeTo(_os, true);
	}
	
	public void writeTo(final OutputStream _os, boolean _close) throws IOException
	{
		streamBegin(_os);

		/* streaend will take care of this.
		for(int j = 0; j<this.objects.size(); j++)
		{
			AbstractPdfObject obj = this.objects.get(j);
			obj.streamOut();
		}
		*/

		streamEnd(_close);
	}
	
	public String xrefToString(long offset, int generation)
	{
		String of = Long.toString(offset);
		String ge = Integer.toString(generation);
		String rs = "0000000000".substring(0, 10-of.length()) +
					of +
					" " +
					"00000".substring(0,5-ge.length())+ge;
		if(generation==65535)
			return rs+" f \n";
		return rs+" n \n";
	}
	
	public void setTitle(String title)
	{
		info.setTitle(title);
	}
	
	public void setCreator(String creator)
	{
		info.setCreator(creator);
	}
	
	public void setAuthor(String author)
	{
		info.setAuthor(author);
	}

	public void setSubject(String subject)
	{
		info.setSubject(subject);
	}

	public void setKeywords(String subject)
	{
		info.setKeywords(subject);
	}

	public void setProducer(String producer)
	{
		info.setProducer(producer);
	}
	
	public PdfFont registerSymbolFont()
	{
		if(true) return registerTtfFont("adobe-symbol",PDF.loadFrom(PDF.RES_SYMBOL_OTF));
		return registerSvgFont("adobe-symbol", PDF.loadFrom(PDF.RES_SYMBOL), null);
	}
	
	public PdfFont registerZapfDingbatsFont()
	{
		if(true) return registerTtfFont("adobe-zapf-dingbats",PDF.loadFrom(PDF.RES_DINGBATS_OTF));
		return registerSvgFont("adobe-zapf-dingbats", PDF.loadFrom(PDF.RES_DINGBATS), null);
	}
	
	public PdfFont registerSymbolFontAfm()
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_SYMBOL_AFM, this.getClass()
				.getClassLoader(), null),"adobe-symbol");
	}
	
	public PdfFont registerZapfDingbatsFontAfm()
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_ZAPF_DINGBATS_AFM, this.getClass()
				.getClassLoader(), null),"adobe-zapf-dingbats");
	}
	
	public PdfFont registerTimesRomanFontAfm(String _cs)
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_TIMES_ROMAN_AFM, this.getClass()
				.getClassLoader(), null),_cs);
	}
	
	public PdfFont registerTimesBoldFontAfm(String _cs)
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_TIMES_BOLD_AFM, this.getClass()
				.getClassLoader(), null),_cs);
	}
	
	public PdfFont registerTimesBoldItalicFontAfm(String _cs)
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_TIMES_BOLD_ITALIC_AFM, this.getClass()
				.getClassLoader(), null),_cs);
	}
	
	public PdfFont registerTimesItalicFontAfm(String _cs)
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_TIMES_ITALIC_AFM, this.getClass()
				.getClassLoader(), null),_cs);
	}
	
	public PdfFont registerTimesBoldFont(String _cs)
	{
		return registerTimesBoldFont(_cs, this.isAllT3() || this.isEmbedCoreFonts());
	}
	
	public PdfFont registerTimesItalicFont(String _cs)
	{
		return registerTimesItalicFont(_cs, this.isAllT3() || this.isEmbedCoreFonts());
	}
	
	public PdfFont registerTimesBoldItalicFont(String _cs)
	{
		return registerTimesBoldItalicFont(_cs, this.isAllT3() || this.isEmbedCoreFonts());
	}
	
	public PdfFont registerTimesRomanFont(String _cs)
	{
		return registerTimesRomanFont(_cs, this.isAllT3() || this.isEmbedCoreFonts());
	}
	
	public PdfFont registerTimesBoldFont(String _cs, boolean _embed)
	{
		if(_embed) return registerTtfFont(_cs,PDF.loadFrom(PDF.RES_TIMES_BOLD_OTF));
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_TIMES_BOLD), null);
	}
	
	public PdfFont registerTimesItalicFont(String _cs, boolean _embed)
	{
		if(_embed) return registerTtfFont(_cs,PDF.loadFrom(PDF.RES_TIMES_ITALIC_OTF));
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_TIMES_ITALIC), null);
	}
	
	public PdfFont registerTimesBoldItalicFont(String _cs, boolean _embed)
	{
		if(_embed) return registerTtfFont(_cs,PDF.loadFrom(PDF.RES_TIMES_BOLD_ITALIC_OTF));
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_TIMES_BOLD_ITALIC), null);
	}
	
	public PdfFont registerTimesRomanFont(String _cs, boolean _embed)
	{
		if(_embed) return registerTtfFont(_cs,PDF.loadFrom(PDF.RES_TIMES_REGULAR_OTF));
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_TIMES_REGULAR), null);
	}
	
	public PdfFont registerHelveticaObliqueFont(String _cs)
	{
		return registerHelveticaObliqueFont(_cs, this.isAllT3() || this.isEmbedCoreFonts());
	}
	
	public PdfFont registerHelveticaObliqueFont(String _cs, boolean _embed)
	{
		if(_embed) return registerTtfFont(_cs,PDF.loadFrom(PDF.RES_HELVETICA_ITALIC_OTF));
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_HELVETICA_ITALIC), null);
	}

	public PdfFont registerCondensedFont(String _cs)
	{
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_HELVETICA_REGULAR), CommonUtil.toArray("condensed"));
	}

	public PdfFont registerCondensedBoldFont(String _cs)
	{
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_HELVETICA_BOLD), CommonUtil.toArray("condensed"));
	}

	public PdfFont registerHelveticaFont(String _cs)
	{
		return registerHelveticaFont(_cs, this.isAllT3() || this.isEmbedCoreFonts());
	}
	
	public PdfFont registerHelveticaFontAfm(String _cs)
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_HELVETICA_AFM, this.getClass()
				.getClassLoader(), null),_cs);
	}
	
	public PdfFont registerHelveticaBoldFontAfm(String _cs)
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_HELVETICA_BOLD_AFM, this.getClass()
				.getClassLoader(), null),_cs);
	}
	
	public PdfFont registerHelveticaObliqueFontAfm(String _cs)
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_HELVETICA_OBLIQUE_AFM, this.getClass()
				.getClassLoader(), null),_cs);
	}
	
	public PdfFont registerHelveticaBoldObliqueFontAfm(String _cs)
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_HELVETICA_BOLD_OBLIQUE_AFM, this.getClass()
				.getClassLoader(), null),_cs);
	}
	
	public PdfFont registerHelveticaFont(String _cs, boolean _embed)
	{
		if(_embed) return registerTtfFont(_cs,PDF.loadFrom(PDF.RES_HELVETICA_REGULAR_OTF));
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_HELVETICA_REGULAR), null);
	}
	
	public PdfFont registerHelveticaBoldFont(String _cs)
	{
		return registerHelveticaBoldFont(_cs, this.isAllT3() || this.isEmbedCoreFonts());
	}
	
	public PdfFont registerHelveticaBoldFont(String _cs, boolean _embed)
	{
		if(_embed) return registerTtfFont(_cs,PDF.loadFrom(PDF.RES_HELVETICA_BOLD_OTF));
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_HELVETICA_BOLD), null);
	}
	
	public PdfFont registerHelveticaBoldObliqueFont(String _cs)
	{
		return registerHelveticaBoldObliqueFont(_cs, this.isAllT3() || this.isEmbedCoreFonts());
	}
	
	public PdfFont registerHelveticaBoldObliqueFont(String _cs, boolean _embed)
	{
		if(_embed) return registerTtfFont(_cs,PDF.loadFrom(PDF.RES_HELVETICA_BOLD_ITALIC_OTF));
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_HELVETICA_BOLD_ITALIC), null);
	}

	public PdfFont registerCourierObliqueFont(String _cs)
	{
		return registerCourierObliqueFont(_cs, this.isAllT3() || this.isEmbedCoreFonts());
	}
	
	public PdfFont registerCourierObliqueFont(String _cs, boolean _embed)
	{
		if(_embed) return registerTtfFont(_cs,PDF.loadFrom(PDF.RES_COURIER_ITALIC_OTF));
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_COURIER_ITALIC), null);
	}
	
	public PdfFont registerCourierFont(String _cs)
	{
		return registerCourierFont(_cs, this.isAllT3() || this.isEmbedCoreFonts());
	}
	
	public PdfFont registerCourierFont(String _cs, boolean _embed)
	{
		if(_embed) return registerTtfFont(_cs,PDF.loadFrom(PDF.RES_COURIER_REGULAR_OTF));
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_COURIER_REGULAR), null);
	}

	public PdfFont registerCourierBoldFont(String _cs)
	{
		return registerCourierBoldFont(_cs, this.isAllT3() || this.isEmbedCoreFonts());
	}
	
	public PdfFont registerCourierBoldFont(String _cs, boolean _embed)
	{
		if(_embed) return registerTtfFont(_cs,PDF.loadFrom(PDF.RES_COURIER_BOLD_OTF));
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_COURIER_BOLD), null);
	}
	
	public PdfFont registerCourierFontAfm(String _cs)
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_COURIER_AFM, this.getClass()
				.getClassLoader(), null),_cs);
	}
	
	public PdfFont registerCourierBoldFontAfm(String _cs)
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_COURIER_BOLD_AFM, this.getClass()
				.getClassLoader(), null),_cs);
	}
	
	public PdfFont registerCourierObliqueFontAfm(String _cs)
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_COURIER_OBLIQUE_AFM, this.getClass()
				.getClassLoader(), null),_cs);
	}
	
	public PdfFont registerCourierBoldObliqueFontAfm(String _cs)
	{
		return registerAfmFont(ClasspathResourceLoader.of(PDF.RES_COURIER_BOLD_OBLIQUE_AFM, this.getClass()
				.getClassLoader(), null),_cs);
	}
	
	public PdfFont registerCourierBoldObliqueFont(String _cs)
	{
		return registerCourierBoldObliqueFont(_cs, this.isAllT3() || this.isEmbedCoreFonts());
	}
	
	public PdfFont registerCourierBoldObliqueFont(String _cs, boolean _embed)
	{
		if(_embed) return registerTtfFont(_cs,PDF.loadFrom(PDF.RES_COURIER_BOLD_ITALIC_OTF));
		return registerSvgFont(_cs, PDF.loadFrom(PDF.RES_COURIER_BOLD_ITALIC), null);
	}
	
	static String[] FONT_TO_AFM = {
			PDF.FONT_COURIER,PDF.RES_COURIER_AFM,
			PDF.FONT_COURIER_BOLD,PDF.RES_COURIER_BOLD_AFM,
			PDF.FONT_COURIER_OBLIQUE,PDF.RES_COURIER_OBLIQUE_AFM,
			PDF.FONT_COURIER_BOLD_OBLIQUE,PDF.RES_COURIER_BOLD_OBLIQUE_AFM,
			PDF.FONT_HELVETICA,PDF.RES_HELVETICA_AFM,
			PDF.FONT_HELVETICA_BOLD,PDF.RES_HELVETICA_BOLD_AFM,
			PDF.FONT_HELVETICA_OBLIQUE,PDF.RES_HELVETICA_OBLIQUE_AFM,
			PDF.FONT_HELVETICA_BOLD_OBLIQUE,PDF.RES_HELVETICA_BOLD_OBLIQUE_AFM,
			PDF.FONT_TIMES,PDF.RES_TIMES_ROMAN_AFM,
			PDF.FONT_TIMES_BOLD,PDF.RES_TIMES_BOLD_AFM,
			PDF.FONT_TIMES_ITALIC,PDF.RES_TIMES_ITALIC_AFM,
			PDF.FONT_TIMES_BOLD_ITALIC,PDF.RES_TIMES_BOLD_ITALIC_AFM,
			PDF.FONT_ZAPFDINGBATS,PDF.RES_ZAPF_DINGBATS_AFM,
			PDF.FONT_SYMBOL,PDF.RES_SYMBOL_AFM,
	};
	
	static String[] FONT_TO_SVG = {
			PDF.FONT_COURIER,PDF.RES_COURIER_REGULAR,
			PDF.FONT_COURIER_BOLD,PDF.RES_COURIER_BOLD,
			PDF.FONT_COURIER_OBLIQUE,PDF.RES_COURIER_ITALIC,
			PDF.FONT_COURIER_BOLD_OBLIQUE,PDF.RES_COURIER_BOLD_ITALIC,
			PDF.FONT_HELVETICA,PDF.RES_HELVETICA_REGULAR,
			PDF.FONT_HELVETICA_BOLD,PDF.RES_HELVETICA_BOLD,
			PDF.FONT_HELVETICA_OBLIQUE,PDF.RES_HELVETICA_ITALIC,
			PDF.FONT_HELVETICA_BOLD_OBLIQUE,PDF.RES_HELVETICA_BOLD_ITALIC,
			PDF.FONT_TIMES,PDF.RES_TIMES_REGULAR,
			PDF.FONT_TIMES_BOLD,PDF.RES_TIMES_BOLD,
			PDF.FONT_TIMES_ITALIC,PDF.RES_TIMES_ITALIC,
			PDF.FONT_TIMES_BOLD_ITALIC,PDF.RES_TIMES_BOLD_ITALIC,
			PDF.FONT_ZAPFDINGBATS,PDF.RES_DINGBATS,
			PDF.FONT_SYMBOL,PDF.RES_SYMBOL,
	};
	
	public PdfFont registerAfmFontT3(String _font, String _cs)
	{
		for(int _i =0 ;_i< FONT_TO_SVG.length; _i+=2)
		{
			if(FONT_TO_SVG[_i].equalsIgnoreCase(_font))
			{
				return registerSvgFont(_cs,ClasspathResourceLoader.of(FONT_TO_SVG[_i+1], this.getClass()
						.getClassLoader(), null),null);
			}
		}
		
		return registerAfmFont(FileResourceLoader.of(_font), _cs);
	}

	public PdfFont registerAfmFont(String _font, String _cs)
	{
		for(int _i =0 ;_i<FONT_TO_AFM.length; _i+=2)
		{
			if(FONT_TO_AFM[_i].equalsIgnoreCase(_font))
			{
				return registerAfmFont(ClasspathResourceLoader.of(FONT_TO_AFM[_i+1], this.getClass()
						.getClassLoader(), null),_cs);
			}
		}
		
		return registerAfmFont(FileResourceLoader.of(_font), _cs);
	}
	
	public PdfFont registerAfmFont(ResourceLoader _font, String _cs)
	{
		PdfFont bf = registerAfmFont(AFM.of(_font), _cs);
		return bf;
	}
	
	public PdfFont registerAfmFont(AFM font, String _cs)
	{
		PdfFont bf = PdfAfmFont.of(this, font, _cs);
		return bf;
	}
	
	public PdfImage registerImage(String _img, String _compression, boolean _t, boolean _a, float _av, int _rot) throws IOException
	{
		return registerImage(FileResourceLoader.of(_img), _compression, _t, _a, _av, _rot);
	}
	
	public PdfImage registerImage(File _img, String _compression, boolean _t, boolean _a, float _av, int _rot) throws IOException
	{
		return registerImage(FileResourceLoader.of(_img, null), _compression, _t, _a, _av, _rot);
	}
	
	public PdfImage registerImage(ResourceLoader _img, String _compression, boolean _t, boolean _a, float _av, int _rot) throws IOException
	{
		if("indexed".equalsIgnoreCase(_compression)
				|| "index".equalsIgnoreCase(_compression))
		{
			return registerIndexedImage(ImageIO.read(_img.getInputStream()), _t, _a, _av, _rot);
		}
		return registerImage(ImageIO.read(_img.getInputStream()), _compression, _t, _a, _av, _rot);
	}
	
	public PdfImage registerImage(BufferedImage img, String _compression, boolean _t, boolean _a, float _av, int _rot)
	{
		PdfImage pi = PdfAwtImage.of(this, img, _compression, _t, _a, _av, _rot);
		return pi;
	}
	
	public PdfImage registerImage(String _img, String _compression, boolean _t, boolean _a, float _av, int _rot, double _levels) throws IOException
	{
		return registerImage(FileResourceLoader.of(_img), _compression, _t, _a, _av, _rot, _levels);
	}
	
	public PdfImage registerImage(File _img, String _compression, boolean _t, boolean _a, float _av, int _rot, double _levels) throws IOException
	{
		return registerImage(FileResourceLoader.of(_img, null), _compression, _t, _a, _av, _rot, _levels);
	}
	
	public PdfImage registerImage(ResourceLoader _img, String _compression, boolean _t, boolean _a, float _av, int _rot, double _levels) throws IOException
	{
		if("indexed".equalsIgnoreCase(_compression)
				|| "index".equalsIgnoreCase(_compression))
		{
			return registerIndexedImage(ImageIO.read(_img.getInputStream()), _t, _a, _av, _rot);
		}
		return registerImage(ImageIO.read(_img.getInputStream()), _compression, _t, _a, _av, _rot, _levels);
	}
	
	public PdfImage registerImage(BufferedImage img, String _compression, boolean _t, boolean _a, float _av, int _rot, double _levels)
	{
		PdfImage pi = PdfAwtImage.of(this, img, _compression, _t, _a, _av, _rot, _levels);
		return pi;
	}
	
	public PdfImage registerIndexedImage(BufferedImage img, boolean _t, boolean _a, float _av, int _rot)
	{
		PdfImage pi = PdfAwtImage.of(this, img, "indexed", _t, _a, _av, _rot);
		return pi;
	}

	@SneakyThrows
	public PdfFont registerAwtFont(String _awt_name, String _cs, String[] _options)
	{
		Font _awt = new Font(_awt_name, Font.PLAIN, 1);
		return this.registerAwtFont(_awt, _cs, _options);
	}

	@SneakyThrows
	public PdfFont registerAwtFont(Font _awt, String _cs, String[] _options)
	{
		if(_options!=null && _options.length>0)
		{
			return PdfJavaFont.of(this, _cs, _awt, _options);
		}
		else
		if(FontUtil.isTT(_awt))
		{
			String platName = FontUtil.getAwtFileName(_awt);
			if(this.isAllT3() && !"icons".equalsIgnoreCase(_cs) && !"unicode".equalsIgnoreCase(_cs))
			{
				return PdfJavaFont.of(this, _cs, Font.createFont(Font.TRUETYPE_FONT, FileResourceLoader.of(platName).getInputStream()), _options);
			}
			else
			{
				return registerTtfFont(_cs, _awt, FileResourceLoader.of(platName));
			}
		}
		else
		if(FontUtil.isT1(_awt) && FontUtil.getAwtFileName(_awt).endsWith(".pfb"))
		{
			String platName = FontUtil.getAwtFileName(_awt);
			if(this.isAllT3() && !"icons".equalsIgnoreCase(_cs) && !"unicode".equalsIgnoreCase(_cs))
			{
				return PdfJavaFont.of(this, _cs, Font.createFont(Font.TYPE1_FONT, FileResourceLoader.of(platName).getInputStream()), _options);
			}
			else
			{
				String afmName = platName.replace(".pfb", ".afm");
				File afmFile = new File(afmName);
				if(afmFile.exists())
				{
					return registerT1Font(_cs, FileResourceLoader.of(platName), FileResourceLoader.of(afmName));
					//return registerAfmFont(new AFM(new File(platName.replace(".pfb", ".afm"))), _cs);
				}
				return registerT1Font(_cs, _awt, FileResourceLoader.of(platName));
			}
		}
		else
		{
			return PdfJavaFont.of(this, _cs, _awt, _options);
		}
	}

	@SneakyThrows
	public PdfFont registerSvgFont(String _cs, File _Svg, String[] _options)
	{
		return registerSvgFont(_cs, FileResourceLoader.of(_Svg, _options), _options);
	}

	@SneakyThrows
	public PdfFont registerSvgFont(String _cs, String _Svg, String[] _options)
	{
		return registerSvgFont(_cs, FileResourceLoader.of(_Svg), _options);
	}

	@SneakyThrows
	public PdfFont registerSvgFont(String _cs, ResourceLoader _rl, String[] _options)
	{
		return PdfSvgFont.of(this, _cs, _rl, _options);
	}

	@SneakyThrows
	public PdfFont registerSytheticFont(String _cs, PdfFont _bfont, String[] _options)
	{
		return PdfSynthFont.of(this, _cs, _bfont, _options);
	}

	@SneakyThrows
	public PdfFont registerTtfFont(String _cs, File _ttf)
	{
		return registerTtfFont(_cs, FileResourceLoader.of(_ttf, null));
	}

	@SneakyThrows
	public PdfFont registerTtfFont(String _cs, String _ttf)
	{
		return registerTtfFont(_cs, FileResourceLoader.of(_ttf));
	}

	@SneakyThrows
	public PdfFont registerTtfFont(String _cs, ResourceLoader _rl)
	{
		if(this.isAllT3())
		{
			return PdfJavaFont.of(this, _cs, Font.createFont(Font.TRUETYPE_FONT, _rl.getInputStream()), null);
		}
		return PdfTtfFont.of(this, null, _cs, _rl);
	}

	@SneakyThrows
	public PdfFont registerTtfFont(String _cs, Font _awt, ResourceLoader _rl)
	{
		if(this.isAllT3())
		{
			return PdfJavaFont.of(this, _cs, _awt, null);
		}
		return PdfTtfFont.of(this, _awt, _cs, _rl);
	}

	@SneakyThrows
	public PdfFont registerOtxFont(String _cs, File _ttf)
	{
		return registerOtxFont(_cs, FileResourceLoader.of(_ttf, null));
	}

	@SneakyThrows
	public PdfFont registerOtxFont(File _ttf)
	{
		return registerOtxFont(PDF.ENCODING_PDFDOC, FileResourceLoader.of(_ttf, null));
	}

	@SneakyThrows
	public PdfFont registerOtxFont(String _cs, String _ttf)
	{
		return registerOtxFont(_cs, FileResourceLoader.of(_ttf));
	}

	@SneakyThrows
	public PdfFont registerOtxFont(String _ttf)
	{
		return registerOtxFont(PDF.ENCODING_PDFDOC, FileResourceLoader.of(_ttf));
	}

	@SneakyThrows
	public PdfFont registerOtxFont(ResourceLoader _rl)
	{
		return registerOtxFont(PDF.ENCODING_PDFDOC, _rl);
	}
	@SneakyThrows
	public PdfFont registerOtxFont(String _cs, ResourceLoader _rl)
	{
		if(this.isAllT3())
		{
			return PdfJavaFont.of(this, _cs, Font.createFont(Font.TRUETYPE_FONT, _rl.getInputStream()), null);
		}
		return PdfOtxFont.of(this, _rl,_cs);
	}

	public PdfFont registerT1Font(String _cs, Font _awt, ResourceLoader _rl)
	{
		if(this.isAllT3())
		{
			return PdfJavaFont.of(this, _cs, _awt, null);
		}
		return PdfType1Font.of(this, _awt, _cs, _rl);
	}

	@SneakyThrows
	public PdfFont registerT1Font(String _cs, ResourceLoader _pfb, ResourceLoader _afm)
	{
		if(this.isAllT3())
		{
			return PdfJavaFont.of(this, _cs, Font.createFont(Font.TYPE1_FONT, _pfb.getInputStream()), null);
		}
		return PdfType1Font.of(this, _cs, _pfb, _afm);
	}

	@SneakyThrows
	public PdfFont registerT1Font(String _cs, File _pfb, File _afm)
	{
		return registerT1Font(_cs, FileResourceLoader.of(_pfb, null), FileResourceLoader.of(_afm, null));
	}

	@SneakyThrows
	public PdfFont registerT1Font(String _cs, String _pfb, String _afm)
	{
		return registerT1Font(_cs, FileResourceLoader.of(_pfb), FileResourceLoader.of(_afm));
	}

	public PdfColorSpace getDeviceGray()
	{
		return PdfGrayColorSpace.getDeviceGray(this);
	}

	public PdfColorSpace getDeviceRGB()
	{
		return PdfRgbColorSpace.getDeviceRgb(this);
	}
	
	public PdfColorSpace getDeviceCMYK()
	{
		return PdfCmykColorSpace.getDeviceCmyk(this);
	}
	
	public PdfColorSpace getCalGray(float _wx, float _wy, float _wz, float _bx, float _by, float _bz, float _ga)
	{
		return PdfGrayColorSpace.getCalGray(this, _wx,_wy,_wz,_bx,_by,_bz,_ga);
	}
	
	public PdfFormXObject newForm()
	{
		return PdfFormXObject.create(this);
	}

	public PdfFontRegistry registerBase14Fonts(String _enc, boolean _embed)
	{
		PdfFontRegistry _reg = PdfFontRegistry.of(this);
		return registerBase14Fonts(_reg, _embed || this.isEmbedCoreFonts(), _enc);
	}
	
	public PdfFontRegistry registerBase14Fonts(String _enc)
	{
		PdfFontRegistry _reg = PdfFontRegistry.of(this);
		return registerBase14Fonts(_reg, this.isAllT3() || this.isEmbedCoreFonts(), _enc);
	}

	public PdfFontRegistry registerBase14FontsAfm(String _enc)
	{
		PdfFontRegistry _reg = PdfFontRegistry.of(this);
		return registerBase14FontsAfm(_reg, _enc);
	}
	
	public PdfFontRegistry registerBase14Fonts(PdfFontRegistry _reg, String _enc)
	{
		return registerBase14Fonts(_reg, this.isAllT3() || this.isEmbedCoreFonts(), _enc);
	}
	
	public PdfFontRegistry registerBase14Fonts(PdfFontRegistry _reg, boolean _embed, String _enc)
	{
		_embed = _embed  || this.isEmbedCoreFonts();
		
		_reg.registerFont(this.registerHelveticaFont(_enc,_embed), PDF.FONT_HELVETICA);
		_reg.registerFont(this.registerHelveticaBoldFont(_enc,_embed), PDF.FONT_HELVETICA_BOLD);
		_reg.registerFont(this.registerHelveticaObliqueFont(_enc,_embed), PDF.FONT_HELVETICA_OBLIQUE);
		_reg.registerFont(this.registerHelveticaBoldObliqueFont(_enc,_embed), PDF.FONT_HELVETICA_BOLD_OBLIQUE);
		
		_reg.registerFont(this.registerTimesRomanFont(_enc,_embed), PDF.FONT_TIMES);
		_reg.registerFont(this.registerTimesBoldFont(_enc,_embed), PDF.FONT_TIMES_BOLD);
		_reg.registerFont(this.registerTimesItalicFont(_enc,_embed), PDF.FONT_TIMES_ITALIC);
		_reg.registerFont(this.registerTimesBoldItalicFont(_enc,_embed), PDF.FONT_TIMES_BOLD_ITALIC);
		
		_reg.registerFont(this.registerCourierFont(_enc,_embed), PDF.FONT_COURIER);
		_reg.registerFont(this.registerCourierBoldFont(_enc,_embed), PDF.FONT_COURIER_BOLD);
		_reg.registerFont(this.registerCourierObliqueFont(_enc,_embed), PDF.FONT_COURIER_OBLIQUE);
		_reg.registerFont(this.registerCourierBoldObliqueFont(_enc,_embed), PDF.FONT_COURIER_BOLD_OBLIQUE);
		
		_reg.registerFont(this.registerSymbolFont(), PDF.FONT_SYMBOL);
		_reg.registerFont(this.registerZapfDingbatsFont(), PDF.FONT_ZAPFDINGBATS);
		
		return _reg;
	}
	
	public PdfFontRegistry registerBase14FontsAfm(PdfFontRegistry _reg, String _enc)
	{
		
		_reg.registerFont(this.registerHelveticaFontAfm(_enc), PDF.FONT_HELVETICA);
		_reg.registerFont(this.registerHelveticaBoldFontAfm(_enc), PDF.FONT_HELVETICA_BOLD);
		_reg.registerFont(this.registerHelveticaObliqueFontAfm(_enc), PDF.FONT_HELVETICA_OBLIQUE);
		_reg.registerFont(this.registerHelveticaBoldObliqueFontAfm(_enc), PDF.FONT_HELVETICA_BOLD_OBLIQUE);
		
		_reg.registerFont(this.registerTimesRomanFontAfm(_enc), PDF.FONT_TIMES);
		_reg.registerFont(this.registerTimesBoldFontAfm(_enc), PDF.FONT_TIMES_BOLD);
		_reg.registerFont(this.registerTimesItalicFontAfm(_enc), PDF.FONT_TIMES_ITALIC);
		_reg.registerFont(this.registerTimesBoldItalicFontAfm(_enc), PDF.FONT_TIMES_BOLD_ITALIC);
		
		_reg.registerFont(this.registerCourierFontAfm(_enc), PDF.FONT_COURIER);
		_reg.registerFont(this.registerCourierBoldFontAfm(_enc), PDF.FONT_COURIER_BOLD);
		_reg.registerFont(this.registerCourierObliqueFontAfm(_enc), PDF.FONT_COURIER_OBLIQUE);
		_reg.registerFont(this.registerCourierBoldObliqueFontAfm(_enc), PDF.FONT_COURIER_BOLD_OBLIQUE);
		
		_reg.registerFont(this.registerSymbolFontAfm(), PDF.FONT_SYMBOL);
		_reg.registerFont(this.registerZapfDingbatsFontAfm(), PDF.FONT_ZAPFDINGBATS);
		
		return _reg;
	}
	
	Map<String, PdfEncoding> encodeMap = new HashMap<>();
	public PdfEncoding getEncoding(String _cs, int _first, String[] _glyphs)
	{
		return PdfEncoding.of(this, _cs, _first, _glyphs);
		/*
		if(!encodeMap.containsKey(_cs))
		{
			encodeMap.put(_cs, PdfEncoding.of(this, _cs, _first, _glyphs));
		}
		return encodeMap.get(_cs);
		*/
	}

	PdfFontRegistry _rreg = null;
	public void report(int _w, int _h, int _margin, int _fs, int _ls, int _hscale, String _cs, String[] _lines)
	{
		if(_rreg==null)
		{
			_rreg = PdfFontRegistry.of(this);
		}

		PdfFont _font = null;
		if(_cs==null)
		{
			_font = _rreg.lookupFont(PDF.FONT_COURIER);
		}
		else
		{
			_font = _rreg.lookupFont(PDF.FONT_COURIER, _cs);
		}

		if(_font==null)
		{
			if(_cs==null)
			{
				_font = _rreg.registerFont(this.registerCourierFont(PDF.ENCODING_PDFDOC), PDF.FONT_COURIER);
			}
			else
			{
				_font = _rreg.registerFont(this.registerCourierFont(_cs), PDF.FONT_COURIER, _cs);
			}
		}
		report(_w, _h, _margin, _fs, _ls, _hscale, _font, _lines);
	}

	public void report(int _w, int _h, int _margin, int _fs, int _ls, int _hscale, PdfFont _font, String[] _lines)
	{

		PdfPage _page = null;
		PdfContent _cnt = null;
		int _y = 0;
		for(int _i = 0; _i<_lines.length; _i++)
		{
			if (_y<_margin || "\f".equalsIgnoreCase(_lines[_i]))
			{
				_page = this.newPage();
				_page.setMediabox(0,0, _w, _h);
				_cnt = _page.newContent();
				_y = _h-_fs-_margin;
			}

			if(!"\f".equalsIgnoreCase(_lines[_i]))
			{
				_cnt.save();
				_cnt.startText();
				_cnt.setFont(_font.getResource(), _fs);
				if(_hscale>0)
				{
					_cnt.hscale(_hscale);
				}
				_cnt.font(_font, _fs);
				_cnt.moveText(_margin,_y);
				_cnt.text(_lines[_i]);
				_cnt.endText();
				_cnt.restore();
				_y-=_ls;
			}
		}
	}

	public void report(int _hscale, String _cs, String[] _lines)
	{
		report(595, 842, 36, 10, 12, _hscale, _cs, _lines);
	}

	public void report(int _hscale, PdfFont _font, String[] _lines)
	{
		report(595, 842, 36, 10, 12, _hscale, _font, _lines);
	}

	public void report(String _cs, String[] _lines)
	{
		report(595, 842, 36, 10, 12, -1, _cs, _lines);
	}

	public void report(PdfFont _font, String[] _lines)
	{
		report(595, 842, 36, 10, 12, -1, _font, _lines);
	}

	public void report(String[] _lines)
	{
		report(595, 842, 36, 10, 12, -1, PDF.ENCODING_PDFDOC, _lines);
	}

    public PdfOutline newOutlineAnchor(String _ref)
	{
		if(this.outlines==null)
		{
			this.outlines = new PdfOutlines(this);
			this.root.set("Outlines", this.outlines);
		}
		return this.outlines.add(_ref);
    }

	private boolean embedCoreFonts;

	public boolean isEmbedCoreFonts() {
		return embedCoreFonts;
	}

	public void setEmbedCoreFonts(boolean embedCoreFonts) {
		this.embedCoreFonts = embedCoreFonts;
	}

	private boolean obfuscate;

	public boolean isObfuscate() {
		return obfuscate;
	}

	public void setObfuscate(boolean obfuscate) {
		this.obfuscate = obfuscate;
	}

	private boolean allT3;

	public boolean isAllT3() {
		return allT3;
	}

	public void setAllT3(boolean allT3) {
		this.allT3 = allT3;
	}
}
