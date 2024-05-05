package com.github.terefang.jmelange.pdf.core.fonts.svg.jaxb;

import javax.xml.bind.annotation.XmlAttribute;

public class SvgFontKern {
    @XmlAttribute(name = "u1") public String u1;
    @XmlAttribute(name = "u2") public String u2;
    @XmlAttribute(name = "g1") public String g1;
    @XmlAttribute(name = "g2") public String g2;
    @XmlAttribute(name = "k") public Integer kern;
}
