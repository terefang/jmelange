package core;

import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.commons.loader.ResourceLoader;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;

import lombok.SneakyThrows;

import java.io.File;

public class TestSvgFont {

    @SneakyThrows
    public static void main(String[] args)
    {
        //File _font = new File("res/fonts/texgyre/courier/qcrr.svg");
        File _font = new File("res/fonts/base14/helvetica/qhvr.svg");

        PdfDocument _doc = new PdfDocument();
        PdfFontRegistry _reg = PdfFontRegistry.of(_doc);

        PdfFont hf = _doc.registerHelveticaBoldFont(PDF.ENCODING_PDFDOC);

        String[] _b14fonts = {
                PDF.RES_COURIER_REGULAR,
                PDF.RES_COURIER_BOLD,
                PDF.RES_COURIER_ITALIC,
                PDF.RES_COURIER_BOLD_ITALIC,
                PDF.RES_HELVETICA_REGULAR,
                PDF.RES_HELVETICA_BOLD,
                PDF.RES_HELVETICA_ITALIC,
                PDF.RES_HELVETICA_BOLD_ITALIC,
                PDF.RES_TIMES_REGULAR,
                PDF.RES_TIMES_BOLD,
                PDF.RES_TIMES_ITALIC,
                PDF.RES_TIMES_BOLD_ITALIC
        };
        String [] _encodings = {"pdfdoc", "adobe-standard","cp1250", "cp1251", "cp1252", "cp1253", "cp1254", "cp1255", "cp1256", "cp1257", "cp1258",
                "hp-roman8", "ibm437", "ibm850", "ibm851", "ibm852", "ibm855", "ibm857",
                "iso-8859-1", "iso-8859-2", "iso-8859-3", "iso-8859-4", "iso-8859-5", "iso-8859-6", "iso-8859-7", "iso-8859-8", "iso-8859-9", "iso-8859-13", "iso-8859-15",
                "koi8-r", "koi8-ru", "koi8-u",
                "macintosh", "texnansi", "text"};

        for(String _fnt : _b14fonts)
        {
            for(String _encode : _encodings)
            {
                PdfFont _xf = _doc.registerSvgFont(_encode, ClasspathResourceLoader.of(_fnt.substring(3), null) , null);
                PdfPage _page = _doc.newPage();
                _doc.newOutline(_xf.getFontName()+" | "+_encode, _page);
                _page.setMediabox(0,0,595,842);
                PdfContent _content = _page.newContent(true);
                _content.startLayer(_xf.getFontName()+_encode);
                _content.setFont(hf, 20);
                _content.drawString(_xf.getFontName()+" | "+_encode, 30, 820);

                _content.setFont(_xf, 30);
                for(int _cp = 0; _cp<256; _cp++)
                {
                    _content.drawString(Character.toString((char) _cp), 30+(35 * (_cp % 16)), 30+(45 * (_cp / 16)));
                }
                _content.endLayer();
            }
        }

        _doc.writeTo("./out/pdf/test-svgfont-encoding.pdf");
    }
}
