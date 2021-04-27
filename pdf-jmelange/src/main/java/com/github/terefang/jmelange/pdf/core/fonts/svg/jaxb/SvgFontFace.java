package com.github.terefang.jmelange.pdf.core.fonts.svg.jaxb;

import lombok.Data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SvgFontFace
{
    @XmlAttribute(name="font-family") public String fontFamily;
    @XmlAttribute(name="units-per-em") public Double unitsPerEm;
    @XmlAttribute(name="panose-1") public String panose;
    @XmlAttribute(name="ascent") public Double ascent;
    @XmlAttribute(name="descent") public Double descent;
    @XmlAttribute(name="alphabetic") public Long alphabetic;
}
