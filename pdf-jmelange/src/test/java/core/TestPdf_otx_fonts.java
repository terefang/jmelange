package core;/*
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
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;


import java.io.File;

public class TestPdf_otx_fonts
{
	public static final String[] _fontlist = {
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/AccanthisADFStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/AccanthisADFStdNo2-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/AccanthisADFStdNo3-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/ArrowsADF.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/AurelisADFNo2Std-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/BaskervaldADFStd.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/BerenisADFPro-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/BerenisADFProMath-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/BulletsADF.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/ElectrumADFExp-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/GilliusADF-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/GilliusADFNo2-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/IkariusADFNo2Std-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/IkariusADFStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/IrianisADFStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/IrianisADFStyleStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/KeypadADF.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/KeypadADFNo2.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/LibrisADFStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/MekanusADFStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/MekanusADFTitlingStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/MintSpirit-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/MintSpiritNo2-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/NeoGothisADFStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/OldaniaADFStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/OrnementsADF.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/RomandeADFNo2Std-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/RomandeADFStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/RomandeADFStyleStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/Solothurn-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/SwitzeraADF-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/TribunADFStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/UniversalisADFStd-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/Verana-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/adf/VeranaSans-Regular.otf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/croscore/Arimo-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/croscore/Caladea-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/croscore/Carlito-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/croscore/Cousine-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/croscore/Tinos-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/sil/Andika-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/sil/AndikaNewBasic-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/sil/CharisSIL-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/sil/DoulosSIL-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/sil/GalatiaSIL-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/sil/GentinumBasic-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/sil/GentinumBookBasic-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/sil/GentiumBookPlus-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/sil/GentiumPlus-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/urw/truetype/C059-Roman.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/urw/truetype/D050000L.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/urw/truetype/NimbusMonoPS-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/urw/truetype/NimbusRoman-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/urw/truetype/NimbusSans-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/urw/truetype/NimbusSansNarrow-Regular.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/urw/truetype/P052-Roman.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/urw/truetype/StandardSymbolsPS.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/urw/truetype/URWBookman-Demi.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/urw/truetype/URWGothic-Book.ttf",
			"/u/fredo/IdeaProjects/jmelange/res/fonts/urw/truetype/Z003-MediumItalic.ttf",
	};

	public static void main(String[] args) throws Exception
	{
		PdfDocument doc = new PdfDocument();

		doc.streamBegin("./out/test-otx-fonts.pdf");

		PdfFont _hf = doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);
		PdfFont _uni = doc.registerOtxFont(null, "res/fonts/unifont/unifont-12.1.04.ttf");

		for(String _fn : _fontlist)
		{
			System.err.println(_fn);
			try
			{
				PdfPage _page = null;
				PdfContent _content = null;
				PdfFont _cf = doc.registerOtxFont(PDF.loadFrom(_fn));
				for(int _bmp = 0; _bmp<256; _bmp++)
				{
					if(_cf.hasCoverage(_bmp))
					{
						_page = doc.newPage();
						_page.setMediabox(0,0,750,842);
						_content = _page.newContent(true);
						_content.setFont(_hf, 15);
						_content.drawString(_cf.getFontName()+" | bmp="+_bmp, 30, 820);
						doc.newOutline(_cf.getFontName()+" | bmp="+_bmp, _page);
						_content.setFont(_hf, 9);
						_content.drawString(new File(_fn).getName(), 30, 810);
						for(int _cp = 0; _cp<256; _cp++)
						{
							_content.fillColor("#000000");
							_content.setFont(_cf, 30);
							_content.drawString(Character.toString((char) (_cp +(_bmp<<8))), 30+(45 * (_cp % 16)), 800-(30+(45 * (_cp / 16))));
							_content.fillColor("#ff0000");
							_content.setFont(_uni, 10);
							_content.drawString(String.format("%04X %s", (_cp +(_bmp<<8)), Character.toString((char) (_cp +(_bmp<<8)))), 30+(45 * (_cp % 16)), 800-(45+(45 * (_cp / 16))));
						}
						_page.streamOut();
					}
				}
			}
			catch(Exception _xe)
			{
				_xe.printStackTrace();
			}
		}
		doc.streamEnd(true);
		System.exit(0);
	}
}
