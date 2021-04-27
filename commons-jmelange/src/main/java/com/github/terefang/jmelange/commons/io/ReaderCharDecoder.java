package com.github.terefang.jmelange.commons.io;

import com.github.terefang.jmelange.commons.CommonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class ReaderCharDecoder implements CharDecoder
{
    public static final ReaderCharDecoder from(InputStream _is, Charset _cs)
    {
        ReaderCharDecoder _rcd = new ReaderCharDecoder();
        _rcd.reader = new InputStreamReader(_is,_cs);
        return _rcd;
    }

    Reader reader;
    @Override
    public int readChar() throws IOException {
        return this.reader.read();
    }

    @Override
    public void dispose() throws IOException {
        CommonUtil.close(this.reader);
    }
}
