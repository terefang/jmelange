package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.script.ITemplateContext;
import com.github.terefang.jmelange.script.ITemplateContextFactory;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class TemplateUtil
{
    @SneakyThrows
    public static String renderTypeFromFile(String _type, File _source, Map<String,Object> _data)
    {
        try(FileReader _fh = new FileReader(_source))
        {
            ITemplateContext _scp = ITemplateContextFactory.findByName(_type).newInstance(Collections.singletonList(_source.getParentFile()), null, null);
            return render(_scp, IOUtil.toString(_fh), _data);
        }
    }
    
    @SneakyThrows
    public static String renderTypeFromString(String _type, String _source, Map<String,Object> _data)
    {
        ITemplateContext _scp = ITemplateContextFactory.findByName(_type).newInstance();
        return render(_scp, _source, _data);
    }
    
    @SneakyThrows
    public static String renderFreeMarkerFromFile(File _source, Map<String,Object> _data)
    {
        return renderTypeFromFile("freemarker", _source, _data);
    }
    
    @SneakyThrows
    public static String renderFreeMarkerFromString(String _source, Map<String,Object> _data)
    {
        return renderTypeFromString("freemarker", _source, _data);
    }
    
    @SneakyThrows
    public static String renderGSimpleFromFile(File _source, Map<String,Object> _data)
    {
        return renderTypeFromFile("gsimple", _source, _data);
    }
    
    @SneakyThrows
    public static String renderGSimpleFromString(String _source, Map<String,Object> _data)
    {
        return renderTypeFromString("gsimple", _source, _data);
    }
    
    @SneakyThrows
    public static String renderThymeleafFromFile(File _source, Map<String,Object> _data)
    {
        return renderTypeFromFile("thymeleaf", _source, _data);
    }
    
    @SneakyThrows
    public static String renderThymeleafFromString(String _source, Map<String,Object> _data)
    {
        return renderTypeFromString("thymeleaf", _source, _data);
    }
    
    @SneakyThrows
    public static String renderJinjavaFromFile(File _source, Map<String,Object> _data)
    {
        return renderTypeFromFile("jinjava", _source, _data);
    }
    
    @SneakyThrows
    public static String renderJinjavaFromString(String _source, Map<String,Object> _data)
    {
        return renderTypeFromString("jinjava", _source, _data);
    }
    
    @SneakyThrows
    public static String renderTrimouFromFile(File _source, Map<String,Object> _data)
    {
        return renderTypeFromFile("trimou", _source, _data);
    }
    
    @SneakyThrows
    public static String renderTrimouFromString(String _source, Map<String,Object> _data)
    {
        return renderTypeFromString("trimou", _source, _data);
    }
    
    @SneakyThrows
    public static String renderBasicFromFile(File _source, Map<String,Object> _data)
    {
        return renderTypeFromFile("basictemplate", _source, _data);
    }
    
    @SneakyThrows
    public static String renderBasicFromString(String _source, Map<String,Object> _data)
    {
        return renderTypeFromString("basictemplate", _source, _data);
    }
    
    public static String renderDictFromString(String _source, Map<String,Object> _data)
    {
        return DictUtil.dformat(_source, _data);
    }
    
    private static String render(ITemplateContext _scp, String _source, Map<String,Object> _data)
    {
        _scp.compile(new StringReader(_source), "template");
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        //_scp.setOutputType("TEXT");
        if(_scp.run(_data, _baos)!=null)
        {
            return new String(_baos.toByteArray(), StandardCharsets.UTF_8);
        }
        return null;
    }
    
    private static boolean renderToFile(ITemplateContext _scp, String _source, Map<String,Object> _data, String _file)
    {
        return renderToFile(_scp, _source, _data, new File(_file));
    }
    
    @SneakyThrows
    private static boolean renderToFile(ITemplateContext _scp, String _source, Map<String,Object> _data, File _file)
    {
        _file.getParentFile().mkdirs();
        
        _scp.compile(new StringReader(_source), "template");
        
        BufferedOutputStream _baos = new BufferedOutputStream(new FileOutputStream(_file), 1024000);
        //_scp.setOutputType("TEXT");
        if(_scp.run(_data, _baos)!=null)
        {
            return true;
        }
        return false;
    }
}
