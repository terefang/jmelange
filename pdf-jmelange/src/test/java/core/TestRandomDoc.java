package core;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.pdf.core.PDF;
import com.github.terefang.jmelange.pdf.core.PdfDocument;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFont;
import com.github.terefang.jmelange.pdf.core.fonts.PdfFontRegistry;
import com.github.terefang.jmelange.pdf.core.values.PdfPage;
import com.github.terefang.jmelange.random.ArcRand;
import com.github.terefang.jmelange.words.Conflux;
import lombok.SneakyThrows;

import java.io.File;
import java.util.List;
import java.util.Vector;

public class TestRandomDoc {

    public static String _WORDS = "On the other hand, we denounce with " +
             "righteous indignation and dislike men who are so beguiled and demoralized by the charms of pleasure " +
             "of the moment, so blinded by desire, that they cannot foresee the pain and trouble that are bound to " +
             "ensue; and equal blame belongs to those who fail in their duty through weakness of will, which is the " +
            // "same as saying through shrinking from toil and pain. These cases are perfectly simple and easy to " +
            // "distinguish. In a free hour, when our power of choice is untrammelled and when nothing prevents our " +
            // "being able to do what we like best, every pleasure is to be welcomed and every pain avoided. But in " +
            // "certain circumstances and owing to the claims of duty or the obligations of business it will frequently " +
            // "occur that pleasures have to be repudiated and annoyances accepted. The wise man therefore always holds " +
            // "in these matters to this principle of selection: he rejects pleasures to secure other greater pleasures, " +
            // "or else he endures pains to avoid worse pains. " +
            "Damit Ihr indess erkennt, woher dieser ganze Irrthum gekommen " +
            "ist, und weshalb man die Lust anklagt und den Schmerz lobet, so will ich Euch Alles eröffnen und auseinander " +
            "setzen, was jener Begründer der Wahrheit und gleichsam Baumeister des glücklichen Lebens selbst darüber " +
            "gesagt hat. Niemand, sagt er, verschmähe, oder hasse, oder fliehe die Lust als solche, sondern weil grosse " +
            "Schmerzen ihr folgen, wenn man nicht mit Vernunft ihr nachzugehen verstehe. Ebenso werde der Schmerz als " +
            "solcher von Niemand geliebt, gesucht und verlangt, sondern weil mitunter solche Zeiten eintreten, dass man " +
            "mittelst Arbeiten und Schmerzen eine grosse Lust sich zu verschaften suchen müsse. Um hier gleich bei dem " +
            "Einfachsten stehen zu bleiben, so würde Niemand von uns anstrengende körperliche Uebungen vornehmen, wenn " +
            "er nicht einen Vortheil davon erwartete. Wer dürfte aber wohl Den tadeln, der nach einer Lust verlangt, " +
            "welcher keine Unannehmlichkeit folgt, oder der einem Schmerze ausweicht, aus dem keine Lust hervorgeht? " +
            "Dagegen tadelt und hasst man mit Recht Den, welcher sich durch die Lockungen einer gegenwärtigen Lust " +
            "erweichen und verführen lässt, ohne in seiner blinden Begierde zu sehen, welche Schmerzen und " +
            "Unannehmlichkeiten seiner deshalb warten. Gleiche Schuld treffe Die, welche aus geistiger Schwäche, " +
            "d.h. um der Arbeit und dem Schmerze zu entgehen, ihre Pflichten verabsäumen. Man kann hier leicht und " +
            "schnell den richtigen Unterschied treffen; zu einer ruhigen Zeit, wo die Wahl der Entscheidung völlig frei " +
            "ist und nichts hindert, das zu thun, was den Meisten gefällt, hat man jede Lust zu erfassen und jeden " +
            "Schmerz abzuhalten; aber zu Zeiten trifft es sich in Folge von schuldigen Pflichten oder von sachlicher " +
            "Noth, dass man die Lust zurückweisen und Beschwerden nicht von sich weisen darf. Deshalb trifft der Weise " +
            "dann eine Auswahl, damit er durch Zurückweisung einer Lust dafür eine grössere erlange oder durch " +
            "Uebernahme gewisser Schmerzen sich grössere erspare.";
    @SneakyThrows
    public static void main(String[] args)
    {
        ArcRand _rng = ArcRand.from(CommonUtil.randomGUID());
        Conflux _cf = Conflux.loadFromString(_WORDS);
        List<String> _list = _cf.generateWords(_rng, new Vector<>(), 5, 500, true);
        PdfDocument _doc = new PdfDocument();
        _doc.setAuthor(CommonUtil.join(_list.subList(0,3), ' '));
        _doc.setCreator(CommonUtil.join(_list.subList(3,6), ' '));
        _doc.setKeywords(CommonUtil.join(_list.subList(6,9), ' '));
        _doc.setProducer(CommonUtil.join(_list.subList(9,12), ' '));
        _doc.setSubject(CommonUtil.join(_list.subList(12,15), ' '));
        _doc.setTitle(CommonUtil.join(_list.subList(15,18), ' '));

        PdfFont hf = _doc.registerCourierFont(PDF.ENCODING_PDFDOC);

        for(int _i=0; _i<10; _i++)
        {
            PdfPage _page = _doc.newPage();
            _doc.newOutline(CommonUtil.randomGUID(), _page);
            _page.setMediabox(0,0,595,842);
            PdfContent _content = _page.newContent(true);
            _content.setFont(hf, 12);

            for(int _cp = 800; _cp>30;)
            {
                while(_list.size()<8) _list = _cf.generateWords(_rng, _list, 5, 800, false);
                _content.drawString(CommonUtil.join(_list.subList(0,8), ' '), 30, _cp);
                _cp-=14;
                _list = _list.subList(8,_list.size());
            }
            System.err.println(_i);
        }
        _doc.writeTo("./out/pdf/x6z5-"+CommonUtil.randomGUID()+".pdf");
        System.err.println(CommonUtil.join(_list.subList(0,8), ' '));
    }
}
