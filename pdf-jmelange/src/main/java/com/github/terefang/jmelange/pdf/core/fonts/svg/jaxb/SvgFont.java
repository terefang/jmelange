package com.github.terefang.jmelange.pdf.core.fonts.svg.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class SvgFont
{
    @XmlAttribute(name = "id")
    public String id;

    @XmlAttribute(name = "horiz-adv-x")
    public Double horizAdvX;

    @XmlElement(name = "font-face")
    public SvgFontFace face;

    @XmlElement(name = "missing-glyph")
    public SvgFontMissingGlyph missingGlyph;

    @XmlElement(name = "glyph")
    public List<SvgFontGlyph> glyphs;

    @XmlElement(name = "hkern")
    public List<SvgFontKern> kerns;
}
