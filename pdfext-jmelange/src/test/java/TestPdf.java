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

import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.color.PdfColorSpace;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.commons.loader.*;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.pdf.core.values.PdfDictObjectWithStream;
import com.github.terefang.jmelange.pdf.core.values.PdfFormXObject;
import com.github.terefang.jmelange.pdf.core.values.PdfOutline;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.pdf.ext.PdfExtDocument;
import com.github.terefang.jmelange.pdf.ext.text.Cell;
import com.github.terefang.jmelange.pdf.ext.text.TextCell;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

public class TestPdf
{
	public static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse ut volutpat arcu. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Sed sed efficitur lectus, nec ultrices sapien. Pellentesque vulputate rutrum odio eu ullamcorper. Curabitur ut aliquam lacus. Sed a posuere arcu. Aenean pretium condimentum placerat. Integer luctus velit eget arcu imperdiet rutrum quis non nulla. Fusce iaculis urna id urna pulvinar, sit amet tristique odio sagittis. Aenean odio nisl, pellentesque ut rhoncus maximus, tempor sed risus. Cras tellus ipsum, varius id tincidunt et, pellentesque quis mi. Cras tempor fringilla volutpat. Proin tristique arcu justo, et ultrices risus luctus ut. Ut gravida vel quam ut efficitur. Proin eget risus nec dui dapibus pulvinar. Aenean luctus ullamcorper sapien at elementum.";
	public static final String LOREM_IPSUM_EN = "On the other hand, we denounce with righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to distinguish. In a free hour, when our power of choice is untrammeled and when nothing prevents our being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in certain circumstances and owing to the claims of duty or the obligations of business it will frequently occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, or else he endures pains to avoid worse pains.";
	
	public static void main(String[] args) throws Exception
	{
		main_1(args);
	}

	public static void main_base14_forms_stream(String[] args) throws Exception
	{
		PdfExtDocument doc = new PdfExtDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC, false);

		doc.streamBegin("./out/pdf/test-b14-form.pdf");
		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);
		
		List<PdfFont> _list = new Vector(_reg.allFonts());
		List<PdfFormXObject> _forms = new Vector<>();
		for(PdfFont _font : _list)
		{
			PdfFormXObject _f = doc.newForm();
			_f.setFormBBox(0, 0, 595, 842);
			_f.setFont(hf, 20);
			_f.drawString(_font.getFontName(), 30, 820);
			
			_f.setFont(_font, 30);
			for(int _cp = 0; _cp<256; _cp++)
			{
				_f.drawString(Character.toString((char) _cp), 30+(35 * (_cp % 16)), 30+(45 * (_cp / 16)));
			}
			
			_forms.add(_f);
			_f.streamOut();
		}
		
		for(PdfFormXObject _form : _forms)
		{
			PdfPage _page = doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(true);
			_content.form(_form.getResource(), 0, 0);
			
			_page.streamOut();
		}
		
		doc.streamEnd(true);
		System.exit(0);
	}
	
	public static void main_base14_forms(String[] args) throws Exception
	{
		PdfExtDocument doc = new PdfExtDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC, false);
		
		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);
		
		List<PdfFont> _list = new Vector(_reg.allFonts());
		List<PdfFormXObject> _forms = new Vector<>();
		for(PdfFont _font : _list)
		{
			PdfFormXObject _f = doc.newForm();
			_f.setFormBBox(0, 0, 595, 842);
			_f.setFont(hf, 20);
			_f.drawString(_font.getFontName(), 30, 820);
			
			_f.setFont(_font, 30);
			for(int _cp = 0; _cp<256; _cp++)
			{
				_f.drawString(Character.toString((char) _cp), 30+(35 * (_cp % 16)), 30+(45 * (_cp / 16)));
			}
			_forms.add(_f);
		}
		
		for(PdfFormXObject _form : _forms)
		{
			PdfPage _page = doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(true);
			_content.form(_form, 0, 0);
		}
		
		doc.writeTo("./out/test.pdf");
		System.exit(0);
	}
	//"adobe-standard adobe-symbol adobe-zapf-dingbats ansi-x3-4-1968ansi-x3-110-1983asmo-449bs-4730bs-viewdata cp037cp437 cp500cp737 cp775cp850 cp852cp855 cp857cp860 cp861cp862 cp863cp864 cp865cp866 cp866lrcp869 cp874cp875 cp1026cp1250 cp1251cp1252 cp1253cp1254 cp1255cp1256 cp1257cp1258 cp10000cp10006 cp10007cp10029 cp10079cp10081 csa-z243-4-1985-1csa-z243-4-1985-2csa-z243-4-1985-gr csn-369103dec-mcs din-66003dk-us ds-2089ebcdic-at-de ebcdic-at-de-a ebcdic-ca-fr ebcdic-dk-no ebcdic-dk-no-a ebcdic-es ebcdic-es-a ebcdic-es-s ebcdic-fi-se ebcdic-fi-se-a ebcdic-fr ebcdic-it ebcdic-pt ebcdic-uk ebcdic-us ecma-cyrillic eses2 gb-1988-80gost-19768-74greek7 greek7-old greek-ccitt hp-roman8 ibm037ibm038 ibm273ibm274 ibm275ibm277 ibm278ibm280 ibm281ibm284 ibm285ibm290 ibm297ibm420 ibm424ibm437 ibm500ibm850 ibm851ibm852 ibm855ibm857 ibm860ibm861 ibm862ibm863 ibm864ibm865 ibm868ibm869 ibm870ibm871 ibm880ibm891 ibm903ibm904 ibm905ibm918 ibm1026iec-p27-1inis inis-8inis-cyrillic invariantiso-646-basic iso-646-irv iso-2033-1983iso-5427iso-5428iso-6937-2-25iso-6937-2-add iso-8859-1iso-8859-2iso-8859-3iso-8859-4iso-8859-5iso-8859-6iso-8859-7iso-8859-8iso-8859-9iso-8859-13iso-8859-15iso-8859-supp iso-10367-box iso-ir-90it jis-c6220-1969-jp jis-c6220-1969-ro jis-c6229-1984-a jis-c6229-1984-b jis-c6229-1984-b-add jis-c6229-1984-hand jis-c6229-1984-hand-add jis-c6229-1984-kana jis-x0201 jus-i-b1-002jus-i-b1-003-mac jus-i-b1-003-serb koi8-r koi8-u ksc5636latin1 latin2latin3 latin4latin5 latin6latin7 latin8latin13 latin15latin-greek latin-greek-1latin-lap macintoshmicrosoft-dingbats msz-7795-3nats-dano nats-dano-add nats-sefi nats-sefi-add nc-nc00-10nf-z-62-010ns-4551-1ns-4551-2pt pt2sen-850200-b sen-850200-c t-61-7bit t-61-8bit t-101-g2 us-dk videotex-suppl"
	public static void main_base14(String[] args) throws Exception
	{
		PdfExtDocument doc = new PdfExtDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC, true);
		
		_reg.registerFont(doc.registerSymbolFont(), "symbol-noembed");
		_reg.registerFont(doc.registerZapfDingbatsFont(), "zapf-noembed");
		
		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);
		
		List<PdfFont> _list = new Vector(_reg.allFonts());
		for(PdfFont _font : _list)
		{
			PdfPage _page = doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(true);
			_content.setFont(hf, 20);
			_content.drawString(_font.getFontName(), 30, 820);
			
			_content.setFont(_font, 30);
			for(int _cp = 0; _cp<256; _cp++)
			{
				_content.drawString(Character.toString((char) _cp), 30+(35 * (_cp % 16)), 30+(45 * (_cp / 16)));
			}
		}
		
		doc.writeTo("./target/test.pdf");
		System.exit(0);
	}
	
	public static void main_colorspace_cmyk(String[] args) throws Exception
	{
		PdfExtDocument doc = new PdfExtDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC, false);

		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);
		
		for(int i =0; i<4; i++)
		{
			PdfPage _page = doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(true);
			_content.setFont(hf, 30);
			_content.fillColorSpace(doc.getDeviceCMYK());
			for(int _bmp = 255; _bmp>=9; _bmp-=16)
			{
				int[] _cmyk = new int[4];
				_cmyk[i] = _bmp;
				_content.fillSpaceColor(true, _cmyk[0]/255f,_cmyk[1]/255f,_cmyk[2]/255f,_cmyk[3]/255f);
				_content.drawString(LOREM_IPSUM_EN, 30, 30+(_bmp*2));
			}
		}
		doc.writeTo("./out/test.pdf");
		System.exit(0);
	}
	
	public static void main_colorspace_rgb(String[] args) throws Exception
	{
		PdfExtDocument doc = new PdfExtDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC, false);
		
		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);
		
		
		for(int i =0; i<3; i++)
		{
			PdfPage _page = doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(true);
			_content.setFont(hf, 30);
			_content.fillColorSpace(doc.getDeviceRGB());
			for(int _bmp = 255; _bmp>=9; _bmp-=16)
			{
				int[] _rgb = new int[3];
				_rgb[i] = _bmp;
				_content.fillSpaceColor(false, _rgb[0]/255f,_rgb[1]/255f,_rgb[2]/255f);
				_content.drawString(LOREM_IPSUM_EN, 30, 30+(_bmp*2));
			}
		}
		doc.writeTo("./out/test.pdf");
		System.exit(0);
	}
	
	public static void main_colorspace_gray(String[] args) throws Exception
	{
		PdfExtDocument doc = new PdfExtDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC, false);
		
		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);
		
		for(PdfColorSpace _cs : Arrays.asList( doc.getDeviceGray(), doc.getCalGray(1f,1f,1f,0f,0f,0f,4.5f)))
		{
			PdfPage _page = doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(false);
			_content.setFont(hf, 30);
			_content.fillColorSpace(_cs);
			for(int _bmp = 255; _bmp>=9; _bmp-=16)
			{
				_content.fillSpaceColor(false, _bmp/255f);
				_content.drawString(LOREM_IPSUM_EN, 30, 30+(_bmp*2));
			}
		}
		doc.writeTo("./out/test.pdf");
		System.exit(0);
	}
	
	public static void main_unifont(String[] args) throws Exception
	{
		PdfExtDocument doc = new PdfExtDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC, false);
		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);
		
		//PdfFont _uf = doc.registerTtfFont(FileResourceLoader.of("/home/fredo/IdeaProjects/pdf-gen/Cardo104s.ttf"));
		PdfFont _uf = doc.registerOtuFont("../res/fonts/unifont/unifont-12.1.04.ttf");
		
		for(int _bmp = 0; _bmp<256; _bmp++)
		{
			PdfPage _page = doc.newPage();
			_page.setMediabox(0,0,595,842);
			PdfContent _content = _page.newContent(true);
			_content.setFont(hf, 20);
			_content.drawString("BMP-"+_bmp, 30, 820);
			
			_content.setFont(_uf, 30);
			for(int _cp = 0; _cp<256; _cp++)
			{
				_content.drawString(Character.toString((char) (_cp | (_bmp<<8))), 30+(35 * (_cp % 16)), 30+(45 * (_cp / 16)));
			}
		}
		doc.writeTo("./out/test.pdf");
		System.exit(0);
	}
	
	public static void main_bleed(String[] args) throws Exception
	{
		PdfExtDocument doc = new PdfExtDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC, false);
		PdfFont hf = _reg.lookupFont(PDF.FONT_HELVETICA_BOLD);
		
		PdfPage _page = doc.newPage();
		_page.setTrimBox(0,0,595,842);
		_page.setBleedBox(-9,-9,595+9,842+9);
		_page.setMediabox(-9,-9,595+9,842+9);
		
		PdfContent _content = _page.newContent(true);
		_content.moveTo(0,0);
		_content.rectangle(0,0,595, 842);
		_content.stroke();
		doc.writeTo("./out/test.pdf");
		System.exit(0);
	}
	
	public static void main__20200105(String[] args) throws Exception
	{
		String _cs = PDF.ENCODING_PDFDOC;
		PdfExtDocument doc = new PdfExtDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC, false);

		PdfFontRegistry _freg = _reg;
		
		PdfFont hf = _freg.lookupFont(PDF.FONT_HELVETICA_BOLD);
		
		_freg.registerFont(doc.registerOtuFont(FileResourceLoader.of("/u/fredo/.fonts/FritzQuadrata-Regular.ttf")));
		_freg.registerFont(doc.registerOtuFont(FileResourceLoader.of("/u/fredo/.fonts/Friz-Quadrata-Std-Regular.otf")));
		_freg.registerFont(doc.registerPfbAfmFont(FileResourceLoader.of("/data/fredo/_fontz/_adobe/AGaramond-Bold.pfb"),
												  FileResourceLoader.of("/data/fredo/_fontz/_adobe/AGaramond-Bold.afm"),
												  _cs));
		
		_freg.registerFont(doc.registerOtuFont("../res/fonts/icons/foundation-icons.ttf"));
		
		PdfPage page;
		PdfContent content;
		for(PdfFont _core : _freg.allFonts())
		{
			page = doc.newPage();

			PdfOutline _outline = doc.newOutline(_core.getFontName(), page);
			content = page.newContent(true);
			page.addFont(hf);
			page.addFont(_core);
			content.setFont(hf, 20);
			content.drawString(_core.getFontName(), 10, 10);
			
			System.err.println(_core.getFontName());
			
			content.setFont(_core, 30);
			for(int i=32; i < 384; i++)
			{
				content.drawString(Character.toString((char) i), 30+(35 * (i % 16)), 35 * (i / 16));
			}
			
			page = doc.newPage();
			content = page.newContent(true);
			
			page.addFont(hf);
			page.addFont(_core);
			
			content.setFont(hf, 20);
			content.drawString(_core.getFontName(), 10, 10);
			
			content.save();
			content.startText();
			content.lead(20);
			//content.rotate(10);
			content.skew(30, 0);
			content.moveText(30,30);
			content.font(_core, 20);
			content.text("Hola, Hello World !");
			content.nl();
			content.text("Hola2, Hello World2 !");
			content.endText();;
			content.restore();
			
			content.save();
			content.startText();
			content.moveText(300,800);
			content.lead(20);
			content.font(_core, 20);
			content.text("Hola, Hello World !");
			content.nl();
			content.text_center("Hola, Hello World !");
			content.nl();
			content.text_right("Hola, Hello World !");
			for(int i = 250; i>=100; i-=50)
			{
				content.nl();
				content.text_justified("jHola, Hello World !", i, false);
				content.nl();
				content.text_fill_left("lHola, Hello World !", i, true);
				content.nl();
				content.text_fill_left("lHola, Hello World !", i, false);
				content.nl();
				content.text_fill_justified("fjHola, Hello World !", i, true);
				content.nl();
				content.text_fill_justified("fjHola, Hello World !", i, false);
			}
			content.endText();;
			content.restore();
			
			int _FONT_SIZE = 10;
			double _LEAD_SIZE = 11.5;
			
			page = doc.newPage();
			content = page.newContent(true);
			page.addFont(hf);
			page.addFont(_core);
			
			content.setFont(hf, 20);
			content.drawString(_core.getFontName(), 10, 10);
			
			content.save();
			content.startText();
			content.font(_core, _FONT_SIZE);
			content.moveText(50,800);
			content.paragraph(LOREM_IPSUM_EN, _LEAD_SIZE, 210, 230, false, false);
			content.endText();;
			content.restore();
			
			content.save();
			content.startText();
			content.font(_core, _FONT_SIZE);
			content.moveText(50,550);
			content.paragraph(LOREM_IPSUM_EN, _LEAD_SIZE, 210, 230, true, false);
			content.endText();;
			content.restore();
			
			content.save();
			content.startText();
			content.font(_core, _FONT_SIZE);
			content.moveText(50,300);
			content.paragraph(LOREM_IPSUM_EN, _LEAD_SIZE, 210, 230, true, false);
			content.endText();;
			content.restore();
			
			content.save();
			content.startText();
			content.font(_core, _FONT_SIZE);
			content.moveText(325,800);
			content.paragraph(LOREM_IPSUM_EN, _LEAD_SIZE, 210, 230, false, true);
			content.endText();;
			content.restore();
			
			content.save();
			content.startText();
			content.font(_core, _FONT_SIZE);
			content.moveText(325,550);
			content.paragraph(LOREM_IPSUM_EN, _LEAD_SIZE, 210, 230, true, true);
			content.endText();;
			content.restore();
			
			content.save();
			content.startText();
			content.font(_core, _FONT_SIZE);
			content.moveText(325,300);
			content.paragraph(LOREM_IPSUM_EN, _LEAD_SIZE, 210, 230, true, true);
			content.endText();;
			content.restore();
			
			page = doc.newPage();
			_outline.add("AW", page);
			content = page.newContent(true);
			page.addFont(hf);
			page.addFont(_core);
			
			content.setFont(hf, 20);
			content.drawString(_core.getFontName(), 10, 10);
			
			content.save();
			content.startText();
			content.font(_core, 500);
			content.moveText(50,200);
			content.text("AW");
			content.endText();;
			content.restore();
			
		}
		
		doc.writeTo("./out/test.pdf");
		System.exit(0);
	}
	
	public static void main_cells(String[] args) throws Exception
	{
		String _cs = PDF.ENCODING_PDFDOC;
		PdfExtDocument doc = new PdfExtDocument();
		PdfFontRegistry _reg = doc.registerBase14Fonts(PDF.ENCODING_PDFDOC, false);
		PdfFontRegistry _freg = _reg;
		float _hscale = 100f;
		PdfFont hf = _freg.lookupFont(PDF.FONT_HELVETICA_BOLD);
		
		PdfFont _core = _freg.registerFont(doc.registerOtuFont(FileResourceLoader.of("/u/fredo/.fonts/FritzQuadrata-Regular.ttf")));
	
		PdfPage page;
		PdfContent content;
		{
			int _FONT_SIZE = 10;
			double _LEAD_SIZE = 11.5;
			
			page = doc.newPage();
			content = page.newContent(true);
			page.addFont(hf);
			page.addFont(_core);
			
			content.setFont(hf, 20);
			content.drawString(_core.getFontName(), 10, 10);
			
			content.save();
			content.startText();
			content.moveText(50,800);
			content.lead(_LEAD_SIZE);
			Cell _tc = TextCell.builder().text(LOREM_IPSUM).fontFace(_core.getFontName()).fontSize(_FONT_SIZE).build();
			int _LINE_WIDTH = 500;
			
			Queue<Cell> _ccq = new ArrayDeque();
			_ccq.add(_tc);
			while(!_ccq.isEmpty())
			{
				_tc = _ccq.poll();
				if(_tc.width(_freg, true)>_LINE_WIDTH)
				{
					List<Cell> _cc = _tc.breakup(_freg, _LINE_WIDTH, _hscale);
					_ccq.addAll(_cc);
				}
				else
				{
					if(_ccq.isEmpty())
					{
						content.wordspace(0);
						content.addContent(_tc.render(_freg, content, true, 0, 0, 0, _hscale));
						content.nl();
					}
					else
					{
						List<Cell> _vv = _tc.breakup(_freg, -1, _hscale);
						
						double _diff = _LINE_WIDTH;
						for(int gg = 0; gg<_vv.size(); gg++)
						{
							_diff -= _vv.get(gg).width(_freg, (gg==0 ? true: false));
						}
						
						//double _wspc = _diff/_tc.numberOfSpaces(true);
						//content.wordspace(_wspc);
						
						//content.addContent(_tc.render(_freg, true, _wspc, 0));
						
						double _id = _diff/(_vv.size()-1);
						for(int gg = 0; gg<_vv.size(); gg++)
						{
							content.addContent(_vv.get(gg).render(_freg, content, (gg==0 ? true: false), (gg==0 ? 0: _id),  0, 0, _hscale));
						}
						
						content.nl();
					}
				}
			}
			content.endText();;
			content.restore();
			
		}
		
		doc.writeTo("./out/pdf/test-cells.pdf");
		System.exit(0);
	}
	
	
	public static void main_1(String[] args) throws Exception
	{
		PdfExtDocument doc = new PdfExtDocument();
		
		//PdfImage pi = doc.registerImage(ImageIO.read(new File("/data/fredo/wappen/IMG_0250_2.png")));
		
		PdfDictObjectWithStream ps = PdfDictObjectWithStream.create(doc, false);
		
		ps.getPrintStream().println("Hello World !!");
		
		PdfFont hf = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);
		String _cs = PDF.ENCODING_PDFDOC;
		
		List<PdfFont> _cores = new Vector<>();
		for(String _core : AFM.AFMs.keySet())
		{
			_cores.add(doc.registerAfmFont(AFM.AFMs.get(_core), _cs));
		}
		
		_cores.add(doc.registerOtuFont(FileResourceLoader.of("/u/fredo/.fonts/FritzQuadrata-Regular.ttf")));
		_cores.add(doc.registerOtuFont(FileResourceLoader.of("/u/fredo/.fonts/Friz-Quadrata-Std-Regular.otf")));
		
		
		for(PdfFont _core : _cores)
		{
			PdfPage page = doc.newPage();
			PdfContent content = page.newContent(true);
			System.err.println(_core.getResource().getResName());
			//PdfAfmFont bf = doc.registerFont(AFM.AFMs.get(_core), null);
			page.addFont(hf);
			page.addFont(_core);
			content.setFont(hf, 5);
			content.drawString(_core.getResource().getResName(), 30, 800);
			content.setFont(_core, 20);
			Character[] _chars = AFM.getUnicodeBase(_cs);
			for(int i=32; i < 256; i++)
			{
				if(_chars[i-32]!=null)
					content.drawString(_chars[i-32].toString(), 50 + (30 * (i % 16)), 50 + (35 * (i / 16)));
			}
		}
		
		FileOutputStream fh = new FileOutputStream("./out/test.pdf");
		BufferedOutputStream bfh = new BufferedOutputStream(fh, 8192);
		doc.writeTo(bfh);
		bfh.close();
	}
	
	
	public static List<String> scandirs(String... dirs) throws Exception
	{
		List<String> _ret = new Vector();
		for(String _dir : dirs)
		{
			DirectoryScanner _scanner = new DirectoryScanner();
			_scanner.setBasedir(_dir);
			_scanner.setCaseSensitive(true);
			_scanner.setIncludes(new String[]{"**/*.ttf", "**/*.otf"});
			_scanner.scan();
			
			for(String includedFile : _scanner.getIncludedFiles())
			{
				_ret.add(new File(_scanner.getBasedir(), includedFile).getAbsolutePath());
			}
		}
		return _ret;
	}

}
