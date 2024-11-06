package com.github.terefang.jmelange.swing.list;

import java.io.File;

public class LabeledFileListItem
{
    public String label;
    private File file;
    
    public File getFile()
    {
        return file;
    }
    
    public void setFile(File _file)
    {
        file = _file;
    }
    
    @Override
    public String toString()
    {
        if(label!=null) return label;
        
        return String.format((file.isDirectory() ? "[%s/%s]" :"%s/%s"),file.getParentFile().getName(),file.getName());
    }
    
    public static LabeledFileListItem from(File _f)
    {
        LabeledFileListItem _rli = new LabeledFileListItem();
        _rli.file = _f;
        return _rli;
    }
    
    public static LabeledFileListItem from(File _f, String _label)
    {
        LabeledFileListItem _rli = new LabeledFileListItem();
        _rli.file = _f;
        _rli.label = _label;
        return _rli;
    }
    
    public static LabeledFileListItem from(String _f)
    {
        LabeledFileListItem _rli = new LabeledFileListItem();
        _rli.file = new File(_f);
        return _rli;
    }
    
    public static LabeledFileListItem from(String _f, String _l)
    {
        LabeledFileListItem _rli = new LabeledFileListItem();
        _rli.file = new File(_f);
        _rli.label = _l;
        return _rli;
    }
}
