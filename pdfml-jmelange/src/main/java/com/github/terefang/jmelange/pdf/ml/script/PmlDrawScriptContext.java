package com.github.terefang.jmelange.pdf.ml.script;


import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.pdf.core.content.PdfContent;
import com.github.terefang.jmelange.pdf.ml.*;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.MarkerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Slf4j
@Data
public class PmlDrawScriptContext extends AbstractPmlScriptContext
{
    public static PmlDrawScriptContext create(PmlParser _p, PmlParserContext _xpc)
    {
        PmlDrawScriptContext _c = new PmlDrawScriptContext();
        _c.parser = _p;
        _c.parserContext = _xpc;
        _c.page = _p.getPage(false);
        _c.binding = new HashMap<>();
        extractBindings(_c, _xpc.getProperties());
        return _c;
    }


    public static PmlDrawScriptContext create(PmlParser _p, PmlParserContext _xpc, Map<String, Object> _b)
    {
        PmlDrawScriptContext _c = create(_p, _xpc);
        _c.binding.putAll(_b);
        return _c;
    }

    @SneakyThrows
    public void runDraw(String _source, boolean _templating, boolean _variables, String _layer, boolean _bg)
    {
        runDraw(new StringReader(_source), _templating, _variables, _layer, _bg);
    }

    @SneakyThrows
    public void runDraw(File _source, boolean _templating, boolean _variables, String _layer, boolean _bg)
    {
        runDraw(new FileReader(_source), _templating, _variables, _layer, _bg);
    }

    @SneakyThrows
    public void runDraw(Reader _source, boolean _templating, boolean _variables, String _layer, boolean _bg)
    {
        if(_templating)
        {
            _source = applyTemplating(this.getBinding(), _source);
            _variables = false;
        }

        PdfContent _cnt = this.getContent(_bg);
        boolean _doLayer = CommonUtil.checkBooleanDefaultIfNull(_layer, true);
        if (_doLayer) {
            _cnt.startLayer(_layer);
        }

        StreamTokenizer st = initializeStreamTokenizer(_source);

        this.setMarker(MarkerFactory.getMarker("draw-code"));
        int ttype = 0;
        while (ttype != StreamTokenizer.TT_EOF) {
            ttype = st.nextToken();
            // skip possible whitespace to next command
            while ((ttype != StreamTokenizer.TT_WORD) && (ttype != StreamTokenizer.TT_EOF)) ttype = st.nextToken();

            if (ttype == StreamTokenizer.TT_EOF) break;

            String command = st.sval.replaceAll("[_\\-]+", "");
            List<String> args = new Vector<>();

            ttype = st.nextToken();
            while ((ttype != StreamTokenizer.TT_EOF)
                    && (ttype != StreamTokenizer.TT_EOL)
                    && (ttype != ';')) {
                switch (ttype) {
                    case '"':
                    case '|':
                    case '~':
                        if(_variables)
                        {
                            args.add(PmlUtil.formatSimple(st.sval, this.getBinding()));
                            break;
                        }
                    case '\'':
                    case StreamTokenizer.TT_WORD:
                        args.add(st.sval);
                        break;
                    case StreamTokenizer.TT_NUMBER:
                        args.add(Double.toString(st.nval));
                        break;
                    default:
                        args.add(Character.toString((char) ttype));
                        break;
                }
                ttype = st.nextToken();
            }

            try {
                DrawCommand _cmd = DrawCommand.valueOf(command);
                if (_cmd != null) {
                    _cmd.execute(this, command, args);
                    log.debug("draw: " + command + " ( " + CommonUtil.join(args.iterator(), ", ") + " )");
                }
            } catch (IllegalArgumentException _iae) {
                log.warn("draw: " + command + " ( " + CommonUtil.join(args.iterator(), ", ") + " ) - UNIMPLEMENTED!");
            } catch (Exception _xe) {
                log.warn("draw: " + command + " ( " + CommonUtil.join(args.iterator(), ", ") + " ) - ERR: " + _xe.getMessage());
            }
        }

        if (_doLayer) {
            _cnt.endLayer();
        }
        this.setMarker(null);
    }
    

    enum DrawCommand
    {
        settitle() { @Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.setTitle(_args.get(0)); } },
        setcreator() { @Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.setCreator(_args.get(0)); } },
        setauthor() { @Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.setAuthor(_args.get(0)); } },
        setsubject() { @Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.setSubject(_args.get(0)); } },
        setkeywords() { @Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.setKeywords(_args.get(0)); } },
        setproducer() { @Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.setProducer(_args.get(0)); } },
        content() { @Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.addContent(_args); } },
        newcontent() { @Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.newContent(); } },
        newpage() { @Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.newPage(); } },
        mediabox() { @Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.pageMediabox(_args); } },
        gsave() { @Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.gSave(); }},
        grestore() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.gRestore(); }},
        movetext() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.moveText(_args); }},
        starttext() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.startText(); }},
        endtext() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.endText(); }},
        loadfont() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.loadFont(_args); }},
        font() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.font(_args); }},
        text() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.text(_args); }},
        rendertext() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.renderText(_args); }},
        startlayer() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.startLayer(_args); }},
        endlayer() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.endLayer(); }},
        pen() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.pen(_args); }},
        fillcolor() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.fillColor(_args); }},
        strokecolor() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.strokeColor(_args); }},
        fillalpha() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.fillAlpha(_args); }},
        strokealpha() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.strokeAlpha(_args); }},
        hscale() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.hScale(_args); }},

        moveto() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.moveTo(_args); }},
        moverel() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.moveRel(_args); }},
        movepolar() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.movePolar(_args); }},
        movetox() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.moveToX(_args); }},
        movetoy() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.moveToY(_args); }},

        lineto() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) {
                for(int _i=1; _i<_args.size(); _i+=2)
                {
                    _ctx.lineTo(_args.get(_i-1),_args.get(_i));
                }
            }
        },
        line() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.line(_args); }},
        hline() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.hLine(_args); }},
        vline() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.vLine(_args); }},

        arrowto() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.arrowTo(_args); }},
        arrowrel() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.arrowRel(_args); }},
        arrowpolar() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.arrowPolar(_args); }},
        arrow() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.arrow(_args); }},

        curveto() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.curveTo(_args); }},
        arc() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.arc(_args); }},
        pie() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.pie(_args); }},
        circle() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.circle(_args); }},
        clip() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.clip(_args); }},
        ellipse() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.ellipse(_args); }},
        spline() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.spline(_args); }},
        closepath() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.closePath(); }},
        endpath() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.endPath(); }},
        rect() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.rect(_args); }},
        rectxy() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.rectXY(_args); }},
        matrix() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.matrix(_args); }},
        rotate() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.rotate(_args); }},
        skew() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.skew(_args); }},
        translate() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.translate(_args); }},
        scale() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.scale(_args); }},
        stroke() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.stroke(); }},
        fill() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.fill(_args); }},
        fillstroke() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.fillStroke(_args); }},
        linedash() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.lineDash(_args); }},
        linedashx() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.lineDashX(_args); }},
        linewidth() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.lineWidth(_args); }},
        linecap() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.lineCap(_args); }},
        linejoin() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.lineJoin(_args); }},
        meterlimit() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.meterlimit(_args); }},
        flatness() {@Override public void execute(PmlDrawScriptContext _ctx, String _cmd, List<String> _args) { _ctx.flatness(_args); }}
        ;
        public abstract void execute(PmlDrawScriptContext _ctx, String command, List<String> args);
    }

}
