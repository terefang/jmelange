package com.github.terefang.jmelange.css;

import com.google.common.collect.Lists;
import com.google.common.io.BaseEncoding;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.visit.AbstractModifyingCSSUrlVisitor;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriter;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorInputStream;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CssResourceUtil
{
    @Data
    static class UrlModifier extends AbstractModifyingCSSUrlVisitor
    {
        File baseDir;
        List<File> resDirs;

        public static UrlModifier create(File _bd, List<File> _rd)
        {
            UrlModifier _um = new UrlModifier();
            _um.setBaseDir(_bd);
            _um.setResDirs(_rd);
            return _um;
        }

        @Override
        @SneakyThrows
        protected String getModifiedURI(String _url)
        {
             _url = findResource(_url, this.getResDirs()).getAbsolutePath();
             return resourceToDataUrl(_url);
        }
    }

    @SneakyThrows
    public static String stringifyCss(String _css, List<File> _rd)
    {
        return ((StringWriter)stringifyCss(_css, _rd, new StringWriter(), true)).getBuffer().toString();
    }

    @SneakyThrows
    public static String stringifyCss(String _css, List<File> _rd, boolean _wrapInStyle)
    {
        return ((StringWriter)stringifyCss(_css, _rd, new StringWriter(), _wrapInStyle)).getBuffer().toString();
    }

    @SneakyThrows
    public static Writer stringifyCss(String _css, List<File> _rd, Writer _wr, boolean _wrapInStyle)
    {
        File _file = findResource(_css, _rd);

        String _code = getStringBySuffix(_file.getAbsolutePath());
        if(_css.endsWith(".scss"))
        {
            _code = compileScss(_file);
        }

        CascadingStyleSheet _doc = CSSReader.readFromString(_code, ECSSVersion.CSS30);
        CSSVisitor.visitCSSUrl(_doc, UrlModifier.create(_file.getParentFile(), _rd));
        CSSWriter _w = new CSSWriter(ECSSVersion.CSS30);
        _w.setWriteHeaderText(true);
        _w.setHeaderText(_css);

        if(_wrapInStyle) _wr.write("<style type=\"text/css\">\n/*<![CDATA[*/\n");
        _w.writeCSS(_doc, _wr);
        if(_wrapInStyle) _wr.write("\n/*]]>*/\n</style>\n");
        _wr.flush();
        return _wr;
    }

    @SneakyThrows
    public static String compileScss(File _file)
    {
        return ((StringWriter)compileScss(_file, new StringWriter())).getBuffer().toString();
    }

    @SneakyThrows
    public static Writer compileScss(File _file, Writer _wr)
    {
        io.bit3.jsass.Compiler _compiler = new io.bit3.jsass.Compiler();
        Options _options = new Options();
        _options.setIncludePaths(Collections.singletonList(_file.getParentFile()));

        Output _output = _compiler.compileString(getStringBySuffix(_file.getAbsolutePath()), _options);
        _wr.write(_output.getCss());
        _wr.flush();
        return _wr;
    }

    public static List<String> IMAGE_EXT = Arrays.asList(ImageIO.getReaderFileSuffixes());
    public static List<String> COMPRESS_EXT = Lists.newArrayList("gz", "zstd", "zst", "xz", "bz2");

    public static boolean isImageSuffix(String _res)
    {
        if(isSuffix(_res, "svg")) return true;

        for(String _suf : IMAGE_EXT)
        {
            if(isSuffix(_res, _suf)) return true;
        }

        return false;
    }

    public static boolean isSuffix(String _res, String... _suf)
    {
        for(String _s : _suf)
        {
            if(_s.equalsIgnoreCase(FileUtils.getExtension(_res))) return true;

            try
            {
                if(isCompressSuffix(_res)
                        && _s.equalsIgnoreCase(FileUtils.getExtension(_res.substring(0,_res.lastIndexOf('.')))))
                    return true;
            }
            catch(Exception _xe)
            {
                // IGNORE
            }
        }

        return false;
    }

    public static boolean isCompressSuffix(String _res)
    {
        if(COMPRESS_EXT.contains(FileUtils.getExtension(_res))) return true;

        return false;
    }

    @SneakyThrows
    public static String resourceToDataUrl(String _res)
    {
        StringBuilder _sb = new StringBuilder();
        if(isSuffix(_res,"png"))
        {
            _sb.append("data:image/png;base64,");
        }
        else
        if(isSuffix(_res,"jpg", "jpeg"))
        {
            _sb.append("data:image/jpeg;base64,");
        }
        else
        if(isSuffix(_res,"otf"))
        {
            _sb.append("data:font/opentype;base64,");
        }
        else
        if(isSuffix(_res,"ttf"))
        {
            _sb.append("data:font/truetype;base64,");
        }
        else
        if(isSuffix(_res,"svg"))
        {
            _sb.append("data:image/svg;base64,");
        }
        else
        if(isImageSuffix(_res))
        {
            BufferedImage _img = ImageIO.read(getStreamBySuffix(_res));
            ByteArrayOutputStream _baos = new ByteArrayOutputStream();
            switch(_img.getType())
            {
                case BufferedImage.TYPE_4BYTE_ABGR:
                case BufferedImage.TYPE_4BYTE_ABGR_PRE:
                case BufferedImage.TYPE_INT_ARGB:
                case BufferedImage.TYPE_INT_ARGB_PRE:
                case BufferedImage.TYPE_BYTE_BINARY:
                case BufferedImage.TYPE_BYTE_INDEXED: {
                    _sb.append("data:image/png;base64,");
                    ImageIO.write(_img, "png", _baos);
                }

                case BufferedImage.TYPE_3BYTE_BGR:
                case BufferedImage.TYPE_BYTE_GRAY:
                case BufferedImage.TYPE_INT_BGR:
                case BufferedImage.TYPE_INT_RGB:
                case BufferedImage.TYPE_USHORT_555_RGB:
                case BufferedImage.TYPE_USHORT_565_RGB:
                case BufferedImage.TYPE_USHORT_GRAY:
                default: {
                    _sb.append("data:image/jpeg;base64,");
                    ImageIO.write(_img, "jpeg", _baos);
                }
            }
            _baos.flush();
            _sb.append(BaseEncoding.base64().encode(_baos.toByteArray()));
            return _sb.toString();
        }
        else
        {
            _sb.append("data:application/octetstream;base64,");
        }

        _sb.append(getDataBase64BySuffix(_res));

        return _sb.toString();
    }

    @SneakyThrows
    public static InputStream getStreamBySuffix(String _res)
    {
        InputStream _stream = new FileInputStream(_res);
        String _ext = FileUtils.getExtension(_res);
        if("gz".equalsIgnoreCase(_ext))
        {
            _stream = new GzipCompressorInputStream(_stream);
        }
        else
        if("zstd".equalsIgnoreCase(_ext) || "zst".equalsIgnoreCase(_ext))
        {
            _stream = new ZstdCompressorInputStream(_stream);
        }
        else
        if("xz".equalsIgnoreCase(_ext))
        {
            _stream = new XZCompressorInputStream(_stream);
        }
        else
        if("bz2".equalsIgnoreCase(_ext))
        {
            _stream = new BZip2CompressorInputStream(_stream);
        }
        return _stream;
    }

    @SneakyThrows
    public static byte[] getDataBySuffix(String _res)
    {
        InputStream _stream = getStreamBySuffix(_res);
        return IOUtil.toByteArray(_stream);
    }

    @SneakyThrows
    public static String getStringBySuffix(String _res)
    {
        InputStream _stream = getStreamBySuffix(_res);
        return IOUtil.toString(_stream);
    }

    @SneakyThrows
    public static String getDataBase64BySuffix(String _res)
    {
        return BaseEncoding.base64().encode(getDataBySuffix(_res));
    }

    public static File findResource(String _res, List<File> _rd)
    {
        File _file = new File(_res);

        int offs = _res.indexOf('/');
        while(!_file.exists() && offs >= 0)
        {
            for(File _r : _rd)
            {
                File _test = new File(_r, _res.substring(offs+1));
                if(_test.exists())
                {
                    return _test;
                }
            }
            offs = _res.indexOf('/', offs+1);
        }

        if(!_file.exists())
        {
            for(File _r : _rd)
            {
                File _test = new File(_r, _res);
                if(_test.exists())
                {
                    return _test;
                }
            }
        }

        if(!_file.exists()) throw new IllegalArgumentException(MessageFormat.format("Resource {0} does not exist", _res));
        return _file;
    }
}