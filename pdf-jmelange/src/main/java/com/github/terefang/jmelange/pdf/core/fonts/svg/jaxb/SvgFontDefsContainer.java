package com.github.terefang.jmelange.pdf.core.fonts.svg.jaxb;

import javax.xml.bind.annotation.XmlElement;

public class SvgFontDefsContainer
{
    @XmlElement(name = "font")
    public SvgFont font;
}
