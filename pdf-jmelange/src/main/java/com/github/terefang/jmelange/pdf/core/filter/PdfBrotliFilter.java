package com.github.terefang.jmelange.pdf.core.filter;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.encoder.BrotliOutputStream;

import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
public class PdfBrotliFilter extends PdfFilter
{
    static {
        Brotli4jLoader.ensureAvailability();
    }
    public static PdfBrotliFilter create() { return new PdfBrotliFilter(); }
    
    public PdfBrotliFilter()
    {
        super("BrotliDecode");
    }
    
    @SneakyThrows
    public byte[] wrap(byte[] content)
    {
        /*
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BrotliOutputStream    dos  = new BrotliOutputStream();
        try
        {
            dos.write(content);
            dos.flush();
            dos.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return baos.toByteArray();
         */
        return null;
    }
}