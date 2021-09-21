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

import com.github.terefang.jmelange.commons.loader.ResourceLoader;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.PdfValue;
import com.github.terefang.jmelange.pdf.core.color.PdfColorSpace;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import org.omg.DynamicAny._DynFixedStub;

import java.io.IOException;
import java.io.OutputStream;

public class PdfPage extends PdfDictObject
{
	private PdfArray<PdfValue> annots;

	public static PdfPage create(PdfDocument doc, PdfDictObject parent)
	{
		return create(doc, parent, null);
	}
	
	public static PdfPage create(PdfDocument doc, PdfDictObject parent, PdfArray<PdfNum> _mediabox)
	{
		return new PdfPage(doc, parent, _mediabox);
	}
	
	public PdfPage(PdfDocument doc, PdfDictObject parent, PdfArray<PdfNum> _mediabox)
	{
		super(doc);
		this.set("Type", PdfName.of("Page"));
		this.set("Parent", parent);
		if(_mediabox==null)
		{
			this.setMediabox(PdfArray.from(0, 0, 595, 842));
		}
		else
		{
			this.setMediabox(_mediabox);
		}
		this.addResource("Font", this.fonts);
		this.addResource("XObject", this.xo);
		this.addResource("ExtGState", this.egs);
		this.addResource("ColorSpace", this.colorspace);
		this.addResource("Properties", this.properties);
		this.addResource("ProcSet", PdfArray.from("PDF", "Text", "ImageC", "ImageB", "ImageI"));
	}
	
	PdfArray<PdfNum> mediabox;
	PdfDict resources = PdfDict.create();
	PdfDict fonts = PdfDict.create();
	PdfDict colorspace = PdfDict.create();
	PdfDict properties = PdfDict.create();
	PdfDict xo = PdfDict.create();
	PdfArray<PdfContent> content = PdfArray.create();
	
	public int getMediaboxTop()
	{
		return this.getMediabox().get(3).getValue();
	}
	
	public int getMediaboxBottom()
	{
		return this.getMediabox().get(1).getValue();
	}
	
	public int getMediaboxLeft()
	{
		return this.getMediabox().get(0).getValue();
	}
	
	public int getMediaboxRight()
	{
		return this.getMediabox().get(2).getValue();
	}
	
	public PdfArray<PdfNum> getMediabox()
	{
		return mediabox;
	}
	
	public void setMediabox(PdfArray mediabox)
	{
		this.mediabox = mediabox;
		this.set("MediaBox", mediabox);
	}
	
	public void setMediabox(int _left, int _bottom, int _right, int _top)
	{
		this.setMediabox(PdfArray.from(_left, _bottom, _right, _top));
	}
	
	public void setMediabox(float _left, float _bottom, float _right, float _top)
	{
		this.setMediabox(PdfArray.fromFloat(_left, _bottom, _right, _top));
	}
	
	
	public PdfArray<PdfNum> getArtBox()
	{
		return (PdfArray<PdfNum>) this.get("ArtBox");
	}
	
	public void setArtBox(PdfArray mediabox)
	{
		this.set("ArtBox", mediabox);
	}
	
	public void setArtBox(int _left, int _bottom, int _right, int _top)
	{
		this.setArtBox(PdfArray.from(_left, _bottom, _right, _top));
	}
	
	public void setArtBox(float _left, float _bottom, float _right, float _top)
	{
		this.setArtBox(PdfArray.fromFloat(_left, _bottom, _right, _top));
	}
	
	
	
	public PdfArray<PdfNum> getTrimBox()
	{
		return (PdfArray<PdfNum>) this.get("TrimBox");
	}
	
	public void setTrimBox(PdfArray mediabox)
	{
		this.set("TrimBox", mediabox);
	}
	
	public void setTrimBox(int _left, int _bottom, int _right, int _top)
	{
		this.setTrimBox(PdfArray.from(_left, _bottom, _right, _top));
	}
	
	public void setTrimBox(float _left, float _bottom, float _right, float _top)
	{
		this.setTrimBox(PdfArray.fromFloat(_left, _bottom, _right, _top));
	}
	
	
	
	public PdfArray<PdfNum> getBleedBox()
	{
		return (PdfArray<PdfNum>) this.get("BleedBox");
	}
	
	public void setBleedBox(PdfArray mediabox)
	{
		this.set("BleedBox", mediabox);
	}
	
	public void setBleedBox(int _left, int _bottom, int _right, int _top)
	{
		this.setBleedBox(PdfArray.from(_left, _bottom, _right, _top));
	}
	
	public void setBleedBox(float _left, float _bottom, float _right, float _top)
	{
		this.setBleedBox(PdfArray.fromFloat(_left, _bottom, _right, _top));
	}
	
	
	
	public PdfArray<PdfNum> getCropBox()
	{
		return (PdfArray<PdfNum>) this.get("CropBox");
	}
	
	public void setCropBox(PdfArray mediabox)
	{
		this.set("CropBox", mediabox);
	}
	
	public void setCropBox(int _left, int _bottom, int _right, int _top)
	{
		this.setCropBox(PdfArray.from(_left, _bottom, _right, _top));
	}
	
	public void setCropBox(float _left, float _bottom, float _right, float _top)
	{
		this.setCropBox(PdfArray.fromFloat(_left, _bottom, _right, _top));
	}
	
	public void streamOut(boolean _res) throws IOException
	{
		if(_res)
		{
			for(PdfValue _r : this.resources.asMap().values())
			{
				if(_r instanceof PdfDict)
				{
					for(PdfValue _c : ((PdfDict) _r).asMap().values())
					{
						if(_c instanceof AbstractPdfObject)
						{
							((AbstractPdfObject) _c).streamOut();
						}
					}
				}
			}
			
			for(PdfContent _c : this.content.asList())
			{
				_c.streamOut();
			}
		}
		super.streamOut(_res);
	}
	
	@Override
	public void writeTo(OutputStream os) throws IOException
	{
		this.set("Resources", this.resources);
		this.set("Contents", this.content);
		super.writeTo(os);
	}
	
	public void addResource(String name, PdfValue obj)
	{
		this.resources.set(name, obj);
	}
	
	public void addFont(String name, PdfDictObject obj)
	{
		this.fonts.set(name, obj);
	}

	public void addXObject(String name, PdfXObject obj)
	{
		this.xo.set(name, obj);
	}
	
	public void addContent(PdfContent c)
	{
		this.content.add(c);
	}

	public PdfContent newContent()
	{
		PdfContent c = PdfContent.create(this.getDoc(), this);
		c.setFlateFilter();
		this.addContent(c);
		return c;
	}

	public PdfContent newContent(boolean _flate)
	{
		PdfContent c = PdfContent.create(this.getDoc(), this);
		c.setFlateFilter(_flate);
		this.addContent(c);
		return c;
	}

	public PdfContent prependContent()
	{
		PdfContent c = PdfContent.create(this.getDoc(), this);
		this.content.prepend(c);
		return c;
	}

	public PdfContent prependContent(boolean _flate)
	{
		PdfContent c = this.prependContent();
		c.setFlateFilter(_flate);
		return c;
	}

	public void addColorSpace(String name, PdfColorSpace obj)
	{
		this.colorspace.set(name, obj);
	}

	public void setRotate(int i)
	{
		this.set("Rotate", PdfNum.of(i));
	}

	public void newNamedLink(String link, int _px, int _py, int _w, int _h)
	{
		if(this.annots == null)
		{
			this.annots = new PdfArray<>();
			this.set("Annots", this.annots);
		}
		PdfDictObject _annot = new PdfDictObject(this.getDoc());
		_annot.setType("Annot");
		_annot.setSubtype("Link");
		_annot.set("Rect", PdfArray.from(_px, _py, (_px+_w), (_py+_h)));
		_annot.set("Border", PdfArray.from(0,0,0));
		_annot.set("Dest", PdfName.of(PDF.normalizeName(link)));
		this.annots.add(_annot);
    }

	public PdfDictObject newFileAttachment(ResourceLoader _file, String _name, int _px, int _py, int _w, int _h)
	{
		if(this.annots == null)
		{
			this.annots = new PdfArray<>();
			this.set("Annots", this.annots);
		}
		PdfDictObject _annot = new PdfDictObject(this.getDoc());
		_annot.setType("Annot");
		_annot.setSubtype("FileAttachment");
		_annot.setName("PushPin"); // 'Graph', 'Paperclip', 'PushPin', 'Tag'
		_annot.set("Contents", PdfString.of(_name==null ? _file.getName() : _name));
		_annot.set("C", PdfArray.from(1,1,0));
		_annot.set("Rect", PdfArray.from(_px, _py, (_px+_w), (_py+_h)));
		_annot.set("Border", PdfArray.from(0,0,1));
		PdfDict _fs = PdfDict.create();
		_fs.setType("F");
		_fs.set("F", PdfString.of(_name==null ? _file.getName() : _name));
		PdfDict _ef = PdfDict.create();
		PdfDictObjectWithStream _fobj = PdfDictObjectWithStream.create(this.getDoc(), true);
		_fobj.setType("EmbeddedFile");
		_fobj.putStream(_file);
		_ef.set("F", _fobj);
		_fs.set("EF", _ef);
		_annot.set("FS", _fs);
		this.annots.add(_annot);
		return _annot;
	}
	/*
	sub appearance {
		my ( $self, $icon, %opts ) = @_;

		return unless $self->{Subtype}->val eq 'FileAttachment';

		my @r = @{ $opts{-rect}} if defined $opts{-rect};
		die "insufficient -rect parameters to annotation->appearance( ) "
		  unless(scalar @r == 4);

		# Handle custom icon type 'None'.
		if ( $icon eq 'None' ) {
			# It is not clear what viewers will do, so provide an
			# appearance dict with no graphics content.

		# 9 0 obj <<
		#    ...
		#    /AP << /D 11 0 R /N 11 0 R /R 11 0 R >>
		#    ...
		# >>
		# 11 0 obj <<
		#    /BBox [ 0 0 100 100 ]
		#    /FormType 1
		#    /Length 6
		#    /Matrix [ 1 0 0 1 0 0 ]
		#    /Resources <<
		#        /ProcSet [ /PDF ]
		#    >>
		# >> stream
		# 0 0 m
		# endstream endobj

		$self->{AP} = PDFDict();
		my $d = PDFDict();
		$self->{' apipdf'}->new_obj($d);
		$d->{FormType} = PDFNum(1);
		$d->{Matrix} = PDFArray( map { PDFNum($_) } 1, 0, 0, 1, 0, 0 );
		$d->{Resources} = PDFDict();
		$d->{Resources}->{ProcSet} = PDFArray( map { PDFName($_) } qw(PDF));
		$d->{BBox} = PDFArray( map { PDFNum($_) } 0, 0, $r[2]-$r[0], $r[3]-$r[1] );
		$d->{' stream'} = "0 0 m";
		$self->{AP}->{N} = $d;	# normal appearance
		# Should default to N, but be sure.
		$self->{AP}->{R} = $d;	# Rollover
		$self->{AP}->{D} = $d;	# Down
		}

		# Handle custom icon.
		elsif ( ref $icon ) {
			# Provide an appearance dict with the image.

		# 9 0 obj <<
		#    ...
		#    /AP << /D 11 0 R /N 11 0 R /R 11 0 R >>
		#    ...
		# >>
		# 11 0 obj <<
		#    /BBox [ 0 0 1 1 ]
		#    /FormType 1
		#    /Length 13
		#    /Matrix [ 1 0 0 1 0 0 ]
		#    /Resources <<
		#        /ProcSet [ /PDF /Text /ImageB /ImageC /ImageI ]
		#        /XObject << /PxCBA 7 0 R >>
		#    >>
		# >> stream
		# q /PxCBA Do Q
		# endstream endobj

		$self->{AP} = PDFDict();
		my $d = PDFDict();
		$self->{' apipdf'}->new_obj($d);
		$d->{FormType} = PDFNum(1);
		$d->{Matrix} = PDFArray( map { PDFNum($_) } 1, 0, 0, 1, 0, 0 );
		$d->{Resources} = PDFDict();
		$d->{Resources}->{ProcSet} = PDFArray( map { PDFName($_) } qw(PDF Text ImageB ImageC ImageI));
		$d->{Resources}->{XObject} = PDFDict();
		my $im = $icon->{Name}->val;
		$d->{Resources}->{XObject}->{$im} = $icon;
		# Note that the image is scaled to one unit in user space.
		$d->{BBox} = PDFArray( map { PDFNum($_) } 0, 0, 1, 1 );
		$d->{' stream'} = "q /$im Do Q";
		$self->{AP}->{N} = $d;	# normal appearance

		# Should default to N, but be sure.
		$self->{AP}->{R} = $d;	# Rollover
		$self->{AP}->{D} = $d;	# Down
		}

		return $self;
	}
	*/

	public void addFont(PdfFont f)
	{
		addFont(f.getResource().getResName(), f);
	}

	public void addProperty(String name, PdfDictObject obj)
	{
		this.properties.set(name, obj);
	}

	PdfDict egs = PdfDict.create();

	public PdfDict getEgs()
	{
		return egs;
	}

	public void addEGState(PdfEGState _state)
	{
		this.getEgs().set(_state.getResource().getResName(), _state);
	}
}
