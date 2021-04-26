package com.github.terefang.jmelange.pdf.ml;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.pdf.core.loader.ClasspathResourceLoader;
import com.github.terefang.jmelange.pdf.core.util.AFM;
import com.github.terefang.jmelange.pdf.ml.io.PmlFileResourceWriter;
import lombok.SneakyThrows;
import picocli.CommandLine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

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
            System.out.println("Command: pmltopdf\n\tpmltopdf --cli ...");
            CommandLine cmd = new CommandLine(_ptp);
            cmd.usage(System.out);
            System.out.println("\tpmltopdf --list-fonts");
            System.out.println("\tpmltopdf --list-pdf-fonts");
            System.out.println("\tpmltopdf --list-awt-fonts");
            System.out.println("\tpmltopdf --list-images-formats");
            System.out.println("\tpmltopdf --dump-defaults");
            System.exit(0);
        }
        if(args.length>0 && "--cli".equalsIgnoreCase(args[0]))
        {
            String[] pargs = new String[args.length-1];
            System.arraycopy(args, 1, pargs, 0, pargs.length);
            CommandLine cmd = new CommandLine(_ptp);
            cmd.parseArgs(pargs);
            _ptp.runCli();
        }
        else
        if(args.length>0 && "--list-fonts".equalsIgnoreCase(args[0]))
        {
            _ptp.listBase14Fonts();
            _ptp.listAwtFonts();
        }
        else
        if(args.length>0 && "--list-pdf-fonts".equalsIgnoreCase(args[0]))
        {
            _ptp.listBase14Fonts();
        }
        else
        if(args.length>0 && "--list-awt-fonts".equalsIgnoreCase(args[0]))
        {
            _ptp.listAwtFonts();
        }
        else
        if(args.length>0 && "--list-images-formats".equalsIgnoreCase(args[0]))
        {
            _ptp.listImageFormats();
        }
        else
        if(args.length>0 && "--dump-defaults".equalsIgnoreCase(args[0]))
        {
            _ptp.dumpDefaults();
        }
        else
        if(args.length>0 && "--dump-image-loaders".equalsIgnoreCase(args[0]))
        {
            _ptp.dumpImageLoaders();
        }
        else
        {
            _ptp.runGui();
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
        CommonUtil.copy(ClasspathResourceLoader.of("attribute-defaults.properties").getInputStream(), System.out);
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
