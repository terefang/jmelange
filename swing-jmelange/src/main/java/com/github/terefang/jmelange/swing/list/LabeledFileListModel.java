package com.github.terefang.jmelange.swing.list;

import javax.swing.DefaultListModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.List;

public class LabeledFileListModel
        extends DefaultListModel<LabeledFileListItem>
{
    public static LabeledFileListModel create()
    {
        return new LabeledFileListModel();
    }
    
    public static LabeledFileListModel from(List<String> _files, FileNameExtensionFilter... _filters)
    {
        LabeledFileListModel _rlm = new LabeledFileListModel();
        _rlm.add(_files, _filters);
        return _rlm;
    }
    
    public void add(List<String> _files, FileNameExtensionFilter... _filters)
    {
        for (String _i : _files)
        {
            File _f = new File(_i);
            for (FileNameExtensionFilter _filter : _filters)
            {
                if (_filter.accept(_f))
                {
                    addElement(LabeledFileListItem.from(_f));
                    break;
                }
            }
            if (_filters.length == 0)
            {
                addElement(LabeledFileListItem.from(_f));
            }
        }
    }
    
    public static LabeledFileListModel fromFiles(List<File> _files, FileNameExtensionFilter... _filters)
    {
        LabeledFileListModel _rlm = new LabeledFileListModel();
        _rlm.addFiles(_files, _filters);
        return _rlm;
    }
    
    public void addFiles(List<File> _files, FileNameExtensionFilter... _filters)
    {
        for (File _i : _files)
        {
            for (FileNameExtensionFilter _filter : _filters)
            {
                if (_filter.accept(_i))
                {
                    addElement(LabeledFileListItem.from(_i));
                    break;
                }
            }
            if (_filters.length == 0)
            {
                addElement(LabeledFileListItem.from(_i));
            }
        }
    }
    
    public void addFile(String _file)
    {
        addElement(LabeledFileListItem.from(_file));
    }
    
    public void addFile(File _file)
    {
        addElement(LabeledFileListItem.from(_file));
    }
    
    public void addFile(String _file, String _label)
    {
        addElement(LabeledFileListItem.from(_file, _label));
    }
    
    public void addFile(File _file, String _label)
    {
        addElement(LabeledFileListItem.from(_file, _label));
    }
}