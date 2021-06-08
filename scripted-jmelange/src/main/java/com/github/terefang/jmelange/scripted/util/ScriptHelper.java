package com.github.terefang.jmelange.scripted.util;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.gfx.impl.PdfImage;
import com.github.terefang.jmelange.gfx.impl.PixelImage;
import com.github.terefang.jmelange.gfx.impl.SvgImage;

public class ScriptHelper extends CommonUtil
{

    public static ScriptHelper create()
    {
        ScriptHelper _ret = new ScriptHelper();
        return _ret;
    }

    public static PixelImage png(int _w, int _h)
    {
        return PixelImage.create(_w,_h);
    }

    public static SvgImage svg(int _w, int _h)
    {
        return SvgImage.create(_w,_h);
    }

    public static PdfImage pdf(int _w, int _h)
    {
        return PdfImage.create(_w,_h);
    }
}
