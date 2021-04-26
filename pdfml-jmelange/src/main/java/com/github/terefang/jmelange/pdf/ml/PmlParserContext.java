package com.github.terefang.jmelange.pdf.ml;

import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

@Data
@Builder
@With
public class PmlParserContext
{
    XmlPullParser parser;
    File basedir;
    File file;
    Properties properties;
    List<String> searchPath = new Vector<>();
}