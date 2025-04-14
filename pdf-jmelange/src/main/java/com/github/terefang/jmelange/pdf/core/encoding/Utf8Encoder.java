package com.github.terefang.jmelange.pdf.core.encoding;

import com.github.terefang.jmelange.fonts.AFM;
import com.github.terefang.jmelange.pdf.core.PDF;

import java.nio.charset.StandardCharsets;

public class Utf8Encoder extends GlyphEncoder
{
    private Character[] _uni;
    
    public Utf8Encoder()
    {
    }
    
    public Utf8Encoder(String _cs)
    {
        if(_cs!=null) this.addMapping(_cs);
    }
    
    public void addMapping(String _cs)
    {
        _uni = AFM.getUnicodeBase(_cs);
    }
    @Override
    public int encodeChar(Integer _c)
    {
        if(_uni!=null && _c<0xff)
        {
            return (int)_uni[_c];
        }
        
        return _c;
    }
    
    @Override
    public String encodeSingle(Integer _c, double wordSpace, double charSpace)
    {
        StringBuilder _ret = new StringBuilder();
        int _i = this.encodeChar(_c);
        
        _ret.append("<");
        for(int _j : Character.toString(_i).getBytes(StandardCharsets.UTF_8))
        {
            _ret.append(String.format("%02x", (_j & 0xff)));
        }
        _ret.append(">");
        
        if(isNeedWordSpaceAdjust())
        {
            if(Character.isSpaceChar(_c) && (wordSpace>0d))
            {
                _ret.append(" " + PDF.transformDP(-wordSpace) + " ");
            }
        }
        return _ret.toString();
    }
    
    @Override
    public int getGlyphNum()
    {
        return 0;
    }
}
