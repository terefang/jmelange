package com.github.terefang.jmelange.pdf.core.fonts.svg.jaxb;

import javax.xml.bind.annotation.XmlAttribute;

public class SvgFontMissingGlyph
{
    @XmlAttribute(name = "horiz-adv-x") public Double horizAdvX;
    @XmlAttribute(name = "d") public String path;
}
