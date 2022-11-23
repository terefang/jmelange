package com.github.terefang.jmelange.pdf.ml;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.pdf.ml.io.PmlFileResourceWriter;
import lombok.SneakyThrows;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

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

    @CommandLine.Option(names = "-D", description = "set option (only for lua/draw)")
    Properties options = new Properties();

    public static void main(String[] args) throws Exception
    {
        PmlToPdf _ptp = new PmlToPdf();

        if(args.length==0 || "--help".equalsIgnoreCase(args[0]))
        {
            System.out.println("Command: pmltopdf\n\tpmltopdf ...");
            CommandLine cmd = new CommandLine(_ptp);
            cmd.usage(System.out);
            System.out.println("\tpmltopdf --list-fonts");
            System.out.println("\tpmltopdf --list-pdf-fonts");
            System.out.println("\tpmltopdf --list-awt-fonts");
            System.out.println("\tpmltopdf --list-images-formats");
            System.out.println("\tpmltopdf --dump-defaults");
            System.exit(0);
        }
        else
        if(args.length>0 && "--list-fonts".equalsIgnoreCase(args[0]))
        {
            _ptp.listBase14Fonts();
            _ptp.listAwtFonts();
            _ptp.listIncludedFonts();
            System.exit(0);
        }
        else
        if(args.length>0 && "--list-pdf-fonts".equalsIgnoreCase(args[0]))
        {
            _ptp.listBase14Fonts();
            System.exit(0);
        }
        else
        if(args.length>0 && "--list-awt-fonts".equalsIgnoreCase(args[0]))
        {
            _ptp.listAwtFonts();
            System.exit(0);
        }
        else
        if(args.length>0 && "--list-images-formats".equalsIgnoreCase(args[0]))
        {
            _ptp.listImageFormats();
            System.exit(0);
        }
        else
        if(args.length>0 && "--dump-defaults".equalsIgnoreCase(args[0]))
        {
            _ptp.dumpDefaults();
            System.exit(0);
        }
        else
        if(args.length>0 && "--dump-image-loaders".equalsIgnoreCase(args[0]))
        {
            _ptp.dumpImageLoaders();
            System.exit(0);
        }

        if(args.length>0)
        {
            CommandLine cmd = new CommandLine(_ptp);
            cmd.parseArgs(args);
            _ptp.runCli();
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
        ClasspathResourceLoader _rl = ClasspathResourceLoader.of("config/font-aliases.properties", null);
        Properties _p = new Properties();
        _p.load(_rl.getInputStream());
        List<String> _list = new Vector<String>(_p.stringPropertyNames());
        Collections.sort(_list);
        for(String _font : _list)
        {
            System.out.println(_font);
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

        if(this.outlineIsFlat)
        {
            _p.setFlatOutline(outlineIsFlat);
        }

        if(deffile!=null && deffile.length>0)
        {
            for(File _def : deffile)
            {
                _p.loadDefaults(_def);
            }
        }

        if(resfile!=null && resfile.length>0)
        {
            for(File _res : resfile)
            {
                _p.mountZip(_res);
            }
        }

        if(this.infile==null) throw new IllegalArgumentException("input file is missing.");
        if(this.outfile==null) throw new IllegalArgumentException("output file is missing.");

        if(this.incdir!=null && this.incdir.length>0)
        {
            this.options.setProperty("-search-path", CommonUtil.join(this.incdir, ";"));
        }

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

    public void runGui()
    {

    }
}
