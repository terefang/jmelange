package com.github.terefang.jmelange.pdf.core.fonts.svg.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "svg")
public class SvgFontContainer {
    @XmlElement(name = "defs")
    public SvgFontDefsContainer defs;
    @XmlElement(name = "metadata")
    public SvgFontMetaDataContainer metadata;
}
