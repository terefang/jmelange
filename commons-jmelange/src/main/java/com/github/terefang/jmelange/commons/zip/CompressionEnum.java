package com.github.terefang.jmelange.commons.zip;

import com.github.luben.zstd.ZstdOutputStream;
import lombok.SneakyThrows;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;

import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public enum CompressionEnum {
    gzip,
    bzip2,
    xz,
    zstd;

    @SneakyThrows
    public static OutputStream createStream(CompressionEnum _compression, OutputStream _baos)
    {
        switch(_compression)
        {
            case zstd:
                return new ZstdOutputStream(_baos);
            case xz:
                return new XZCompressorOutputStream(_baos);
            case bzip2:
                return new BZip2CompressorOutputStream(_baos, 2);
            case gzip:
            default:
                return new GZIPOutputStream(_baos, 2);
        }
    }

    @SneakyThrows
    public static OutputStream createStream(String _file, OutputStream _baos)
    {
        for(CompressionEnum _e : CompressionEnum.values())
        {
            if(_file.endsWith(CompressionEnum.createSuffix(_e)))
            {
                return createStream(_e, _baos);
            }
        }
        return _baos;
    }

    @SneakyThrows
    public static String createSuffix(CompressionEnum _compression)
    {
        switch(_compression)
        {
            case zstd: return ".zst";
            case xz: return ".xz";
            case bzip2: return ".bz2";
            case gzip:
            default: return ".gz";
        }
    }
}
