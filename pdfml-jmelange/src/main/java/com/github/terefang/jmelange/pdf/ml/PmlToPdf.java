package com.github.terefang.jmelange.pdf.ml;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.commons.util.ListMapUtil;
import com.github.terefang.jmelange.commons.util.OsUtil;
import com.github.terefang.jmelange.commons.util.PdataUtil;
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.pdf.ml.io.PmlFileResourceWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

@Slf4j
public class PmlToPdf
{
    @CommandLine.Parameters(index = "0")
    File infile;

    @CommandLine.Parameters(index = "1")
    File outfile;

    @CommandLine.Option(names = { "-c", "--defaults" }, paramLabel = "DEFFILE", description = "the defaults/style file")
    File[] deffile;

    @CommandLine.Option(names = { "-m", "--mount" }, paramLabel = "ZIPFILE", description = "the zip/resources file")
    File[] resfile;

    @CommandLine.Option(names = { "-b", "--base" }, paramLabel = "BASEDIR", description = "the basedir")
    File basedir;

    @CommandLine.Option(names = { "-I", "--include-path" }, paramLabel = "INCDIR", description = "the search path for script require/includes")
    String[] incdir;

    @CommandLine.Option(names = { "--all-t3", "--all-type3" }, description = "embed all fonts as Type3")
    boolean allT3;

    @CommandLine.Option(names = { "-F", "--flat-outline" }, description = "flatten outline")
    boolean outlineIsFlat;

    @CommandLine.Option(names = { "--draw" }, description = "infile is draw-script")
    boolean isDraw;

    @CommandLine.Option(names = { "--lua" }, description = "infile is lua-script")
    boolean isLua;

    @CommandLine.Option(names = "-D", description = "set option (only for lua/draw)")
    Properties options = new Properties();

    public static void usage() throws Exception
    {
        System.out.println("Command: pmltopdf ...");
        System.out.println("\tpmltopdf --help");
        System.out.println("\tpmltopdf --list-paths");
        System.out.println("\tpmltopdf --list-fonts");
        System.out.println("\tpmltopdf --list-pdf-fonts");
        System.out.println("\tpmltopdf --list-awt-fonts");
        System.out.println("\tpmltopdf --list-images-formats");
        System.out.println("\tpmltopdf --dump-defaults");
    }

    public static void main(String[] args) throws Exception
    {
        PmlToPdf _ptp = new PmlToPdf();

        if(args.length==0)
        {
            usage();
        }
        else
        if("--help".equalsIgnoreCase(args[0]))
        {
            usage();
            CommandLine cmd = new CommandLine(_ptp);
            cmd.usage(System.out);
        }
        else
        if("-version".equalsIgnoreCase(args[0]) || "--version".equalsIgnoreCase(args[0]))
        {
            System.out.println(Version.FULL);
        }
        else
        if("--list-paths".equalsIgnoreCase(args[0]))
        {
            _ptp.listPaths();
        }
        else
        if("--list-fonts".equalsIgnoreCase(args[0]))
        {
            _ptp.listBase14Fonts();
            _ptp.listAwtFonts();
            _ptp.listIncludedFonts();
        }
        else
        if("--list-pdf-fonts".equalsIgnoreCase(args[0]))
        {
            _ptp.listBase14Fonts();
        }
        else
        if("--list-awt-fonts".equalsIgnoreCase(args[0]))
        {
            _ptp.listAwtFonts();
        }
        else
        if("--list-images-formats".equalsIgnoreCase(args[0]))
        {
            _ptp.listImageFormats();
        }
        else
        if("--dump-defaults".equalsIgnoreCase(args[0]))
        {
            _ptp.dumpDefaults();
        }
        else
        if("--dump-image-loaders".equalsIgnoreCase(args[0]))
        {
            _ptp.dumpImageLoaders();
        }
        else
        {
            CommandLine cmd = new CommandLine(_ptp);
            try {
                cmd.parseArgs(args);
                _ptp.runCli();
            } catch (Exception _xe)
            {
                usage();
                _xe.printStackTrace(System.out);
            }
        }
        System.exit(0);
    }

    @SneakyThrows
    private void dumpImageLoaders()
    {
        for(String _format : ImageIO.getReaderFormatNames())
        {
            System.out.println("registered ImageIO loader for "+_format);
        }
    }

    @SneakyThrows
    private void dumpDefaults()
    {
        System.out.println("!!! INTERNAL DEFAULTS ");
        CommonUtil.copy(ClasspathResourceLoader.of("attribute-defaults.properties", null).getInputStream(), System.out);
    }

    private void listBase14Fonts()
    {
        for(String _font : AFM.AFMs.keySet())
        {
            System.out.println("pdf:"+_font);
        }
    }

    private void listAwtFonts()
    {
        GraphicsEnvironment _env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for(Font _font : _env.getAllFonts())
        {
            System.out.println("awt:"+_font.getFontName());
        }
    }

    @SneakyThrows
    private void listIncludedFonts()
    {
        String _Props = "config/font-aliases.properties";
        ClasspathResourceLoader _rl = ClasspathResourceLoader.of(_Props, null);

        Properties _p = new Properties();
        try (InputStream _in = _rl.getInputStream();)
        {
            _p.load(_in);
        }

        List<String> _list = new Vector<String>(_p.stringPropertyNames());
        Collections.sort(_list);
        for(String _font : _list)
        {
            System.out.println(_font+" (internal)");
        }

        _p.clear();

        for(String _Path : PmlParserUtil.defaultSearchPaths())
        {
            File _file = new File(_Path,_Props);
            System.out.println("# (local) -> "+_Path);
            if(_file.exists())
            {
                try (InputStream _in = new FileInputStream(_file);)
                {
                    _p.load(_in);
                }
                System.out.println("# (local) -> "+_file.getAbsolutePath());
            }
        }

        _list = new Vector<String>(_p.stringPropertyNames());
        Collections.sort(_list);
        for(String _font : _list)
        {
            System.out.println(_font+" (local) -> "+_p.getProperty(_font));
        }
    }

    @SneakyThrows
    private void listPaths()
    {
        for(String _p : PmlParserUtil.defaultSearchPaths())
        {
            System.out.println(_p+" (search/mount)");
        }
        for(String _p : PmlParserUtil.defaultLuayPaths())
        {
            System.out.println(_p+" (luay)");
        }
    }

    private void listImageFormats()
    {
        //ImageIO
        for(String _suf : ImageIO.getReaderMIMETypes())
        {
            System.out.println("image-format:"+_suf);
        }
    }

    public void runCli() throws IOException
    {
        PmlParser _p = new PmlParser();

        if(this.infile==null) throw new IllegalArgumentException("input file is missing.");
        if(this.outfile==null) throw new IllegalArgumentException("output file is missing.");

        if(this.outlineIsFlat)
        {
            _p.setFlatOutline(outlineIsFlat);
        }

        if(!isLua && deffile!=null && deffile.length>0)
        {
            for(File _def : deffile)
            {
                _p.loadDefaults(_def);
            }
        }

        Map<String,Object> _data = new LinkedHashMap<>();
        if(isLua && deffile!=null && deffile.length>0)
        {
            for(File _def : deffile)
            {
                _data.putAll(PdataUtil.loadFrom(_def));;
                _p.loadDefaults(_def);
            }
        }

        if(resfile!=null && resfile.length>0)
        {
            for(File _res : resfile)
            {
                if(_res.isDirectory())
                {
                    _p.mountDir(_res.getAbsolutePath(), this.basedir, this.infile.getParentFile());
                }
                else
                {
                    _p.mountZip(_res);
                }
            }
        }

        if(this.incdir!=null && this.incdir.length>0)
        {
            this.options.setProperty("-search-path", CommonUtil.join(this.incdir, ";"));
        }

        for(String _Path : PmlParserUtil.defaultSearchPaths())
        {
            _p.mountDir(_Path, null, null);
        }

        _p.mountClasspath();

        if(isLua)
        {
            _p.processLUA(this.infile, PmlFileResourceWriter.of(this.outfile), this.allT3, this.options, _data);
        }
        else
        if(isDraw)
        {
            _p.processDRW(this.infile, PmlFileResourceWriter.of(this.outfile), this.allT3, this.options);
        }
        else
        {
            _p.processPML(this.infile, PmlFileResourceWriter.of(this.outfile), this.allT3);
        }

        System.exit(0);
    }
}
