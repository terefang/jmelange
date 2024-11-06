package com.github.terefang.jmelange.swing.html;

import com.github.terefang.jmelange.swing.html.txtmark.Configuration;
import com.github.terefang.jmelange.swing.html.txtmark.Processor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.net.URL;

public class TxtMarkViewPane extends HtmlViewPane
{
    public TxtMarkViewPane(String _title)
    {
        super(_title);
    }
    
    @Override
    public void loadDocument(String _content)
    {
        super.loadDocument("<html><body>"+ Processor.process(_content, Configuration.EXTENDED)+"</body></html>");
    }
    
    @Override
    @SneakyThrows
    public void loadDocument(URL _url)
    {
        super.loadDocument("<html><body>"+Processor.process(IOUtils.toString(_url),Configuration.EXTENDED)+"</body></html>",_url);
    }
    
    @Override
    @SneakyThrows
    public void loadDocument(File _file)
    {
        this.loadDocument(_file.toURL());
    }
    
    @Override
    public void loadDocument(String _content, URL _url)
    {
        super.loadDocument("<html><body>"+Processor.process(_content,Configuration.EXTENDED)+"</body></html>", _url);
    }
    
    @Override
    public void loadDocument(String _content, String _classpath)
    {
        super.loadDocument("<html><body>"+Processor.process(_content,Configuration.EXTENDED)+"</body></html>", _classpath);
    }
}
