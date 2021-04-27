package com.github.terefang.jmelange.pdf.core.fonts.svg.jaxb;

import lombok.Data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SvgFontGlyph
{
    @XmlAttribute(name = "unicode") public String unicode;
    @XmlAttribute(name = "glyph-name") public String glyphName;
    @XmlAttribute(name = "horiz-adv-x") public Double horizAdvX;
    @XmlAttribute(name = "d") public String path;
}
