package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.commons.CommonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

@Slf4j
public class CfgDataUtil
{
    
    public static File getJarEtcDir()
    {
        return new File(OsUtil.getJarDirectory(),"etc");
    }
    
    public static File getJarConfDir()
    {
        return new File(OsUtil.getJarDirectory(),"conf");
    }
    
    public static File getJarLibDir()
    {
        return new File(OsUtil.getJarDirectory(),"lib");
    }
    
    public static File getJarShareDir()
    {
        return new File(OsUtil.getJarDirectory(),"share");
    }
    
    public static List<File> getConfigDirs(final String _app)
    {
        List<File> _list = new Vector<>();
        _list.add(getJarEtcDir());
        _list.add(getJarConfDir());
        _list.add(new File(OsUtil.getUserConfigDirectory(_app)));
        _list.add(new File(OsUtil.getUnixyUserDataDirectory(_app)));
        return _list;
    }
    
    public static List<File> getDataDirs(final String _app)
    {
        List<File> _list = new Vector<>();
        _list.add(getJarLibDir());
        _list.add(getJarShareDir());
        _list.add(new File(OsUtil.getUserDataDirectory(_app)));
        _list.add(new File(OsUtil.getUnixyUserDataDirectory(_app)));
        _list.add(new File(OsUtil.getSystemDataDirectory(_app)));
        return _list;
    }
    
    
    @SneakyThrows
    public static List<File> scanForDataFiles(final String _app, boolean doprint, String _prefix, String... _exts)
    {
        return scanForFiles(getDataDirs(_app), doprint, _prefix, _exts);
    }
    
    @SneakyThrows
    public static List<File> scanForConfigFiles(final String _app, boolean doprint, String _prefix, String... _exts)
    {
        return scanForFiles(getConfigDirs(_app), doprint, _prefix, _exts);
    }
    
    @SneakyThrows
    public static List<File> scanForFiles(List<File> _dirs, boolean doprint, String _prefix, String... _exts)
    {
        String _pattern = _exts.length==0 ? "*" : CommonUtil.join(_exts,',');
        List<File> _ret = new Vector<>();
        for(File _d : _dirs)
        {
            if(doprint) log.info("scanning "+_d.getAbsolutePath());
            try
            {
                List<File> _todos = FileUtils.getFiles(_d, _pattern, null, true);
                if(_prefix!=null)
                {
                    for(File _todo : _todos)
                    {
                        if(_todo.getName().toLowerCase().startsWith(_prefix.toLowerCase()))
                        {
                            _ret.add(_todo);
                        }
                    }
                }
                else
                {
                    _ret.addAll(_todos);
                }
            }
            catch (Exception _xe)
            {
                log.error(_xe.getMessage());
            }
        }
        return _ret;
    }
    
    public static File getConfigDir(final String _app)
    {
        File _f = new File(OsUtil.getUserConfigDirectory(_app));
        _f.mkdirs();
        return _f;
    }
    
    public static File getDataDir(final String _app)
    {
        File _f = new File(OsUtil.getUserDataDirectory(_app));
        _f.mkdirs();
        return _f;
    }
    
    public static final String LASTDIR = "_lastdir";
    public static final String RECENT = "_recent";
    
    public static File getConfigFile(final String _app, String _file)
    {
        return new File(getConfigDir(_app), _file);
    }

    public static File getDataFile(final String _app, String _file)
    {
        return new File(getDataDir(_app), _file);
    }
    
    public static File getLastDirFromConfig(final String _app, String _default)
    {
        try
        {
            return new File(getConfigAsString(_app, LASTDIR, _default==null ? null : _default));
        }
        catch(Exception _xe)
        {
            return _default!=null ? new File(_default) : null;
        }
    }
    
    @SneakyThrows
    public static void setLastDirToConfig(final String _app, File _ld)
    {
        setLastDirToConfig(_app, _ld.getAbsolutePath());
    }
    
    @SneakyThrows
    public static void setLastDirToConfig(final String _app, String _ld)
    {
        setConfigAsString(_app, LASTDIR, _ld);
    }
    
    @SneakyThrows
    public static void setRecentlyToConfig(final String _app, final String _tag, List<String> _ld)
    {
        setConfigAsStringList(_app, _tag, _ld);
    }
    
    @SneakyThrows
    public static void addRecentlyToConfig(final String _app, final String _tag, String _ld)
    {
        addRecentlyToConfig(_app,_tag,_ld, -1);
    }
    
    @SneakyThrows
    public static void addRecentlyToConfig(final String _app, final String _tag, String _ld, int _sz)
    {
        List<String> _list = new Vector<>();
        List<String> _tmp = new Vector<>();
        _list.add(_ld);
        _tmp.addAll(getRecentlyFromConfig(_app, _tag));
        _tmp.remove(_ld);
        while(_sz>0 && _tmp.size()>_sz)
        {
            _tmp.remove(_sz);
        }
        _list.addAll(_tmp);
        setRecentlyToConfig(_app,_tag,_list);
    }
    
    @SneakyThrows
    public static void trimRecentlyToConfig(final String _app, final String _tag, int _size)
    {
        List<String> _tmp = new Vector<>();
        _tmp.addAll(getRecentlyFromConfig(_app, _tag));
        while(_tmp.size()>_size)
        {
            _tmp.remove(_size);
        }
        setRecentlyToConfig(_app,_tag,_tmp);
    }
    
    @SneakyThrows
    public static List<String> getRecentlyFromConfig(final String _app, final String _tag)
    {
        return getRecentlyFromConfig(_app, _tag,null);
    }
    
    @SneakyThrows
    public static List<String> getRecentlyFromConfig(final String _app, final String _tag, String _default)
    {
        return getConfigAsStringList(_app, _tag, _default);
    }
    
    
    @SneakyThrows
    public static void setRecentToConfig(final String _app, List<String> _ld)
    {
        setRecentlyToConfig(_app, RECENT, _ld);
    }
    
    @SneakyThrows
    public static void addRecentToConfig(final String _app, String _ld)
    {
        addRecentlyToConfig(_app, RECENT,_ld);
    }
    
    @SneakyThrows
    public static void addRecentToConfig(final String _app, String _ld, int _sz)
    {
        addRecentlyToConfig(_app, RECENT,_ld, _sz);
    }
    
    @SneakyThrows
    public static void trimRecentToConfig(final String _app, int _sz)
    {
        trimRecentlyToConfig(_app, RECENT,_sz);
    }
    
    @SneakyThrows
    public static List<String> getRecentFromConfig(final String _app)
    {
        return getRecentlyFromConfig(_app, RECENT);
    }
    
    @SneakyThrows
    public static List<String> getRecentFromConfig(final String _app, String _default)
    {
        return getRecentlyFromConfig(_app, RECENT, _default);
    }
    
    @SneakyThrows
    public static List<File> getRecentDirsFromConfig(final String _app)
    {
        return getRecentDirsFromConfig(_app, (File)null);
    }
    
    @SneakyThrows
    public static List<File> getRecentDirsFromConfig(final String _app, File _default)
    {
        List<File> _ret = new Vector<>();
        for(String _i : getRecentlyFromConfig(_app, RECENT, _default==null ? null : _default.getAbsolutePath()))
        {
            File _x = new File(_i);
            if(_x.isDirectory() && !_ret.contains(_x))
            {
                _ret.add(_x);
            }
            else if(!_ret.contains(_x.getParentFile()))
            {
                _ret.add(_x.getParentFile());
            }
        }
        return _ret;
    }
    
    @SneakyThrows
    public static String getConfigAsString(final String _app, String _key, String _default)
    {
        File _cfg = getConfigFile(_app, _key);
        return getConfigAsString(_cfg, _default);
    }
    
    @SneakyThrows
    public static String getConfigAsString(final File _cfg, String _default)
    {
        if((!_cfg.exists()) ||  (!_cfg.isFile()))
        {
            if(_default==null) return _default;
            Files.writeString(_cfg.toPath(), _default);
        }
        return Files.readString(_cfg.toPath()).trim();
    }
    
    @SneakyThrows
    public static List<String> getConfigAsStringList(final String _app, String _key, String _default)
    {
        File _cfg = getConfigFile(_app, _key);
        return getConfigAsStringList(_cfg, _default);
    }
    
    @SneakyThrows
    public static List<String> getConfigAsStringList(final File _cfg, String _default)
    {
        if((!_cfg.exists()) ||  (!_cfg.isFile()))
        {
            if(_default==null) return Collections.emptyList();
            Files.writeString(_cfg.toPath(), _default);
        }
        return Files.readAllLines(_cfg.toPath(), StandardCharsets.UTF_8);
    }
    
    @SneakyThrows
    public static void setConfigAsString(final String _app, String _key, String _value)
    {
        File _cfg = getConfigFile(_app, _key);
        Files.writeString(_cfg.toPath(), _value);
    }
    
    @SneakyThrows
    public static void setConfigAsString(final File _cfg, String _key, String _value)
    {
        Files.writeString(_cfg.toPath(), _value);
    }
    
    @SneakyThrows
    public static void setConfigAsStringList(final String _app, String _key, List<String> _value)
    {
        File _cfg = getConfigFile(_app, _key);
        Files.write(_cfg.toPath(), _value, StandardCharsets.UTF_8);
    }
    
    @SneakyThrows
    public static void setConfigAsStringList(final File _cfg, List<String> _value)
    {
        Files.write(_cfg.toPath(), _value, StandardCharsets.UTF_8);
    }
    
    @SneakyThrows
    public static void setConfigAsFileList(final String _app, String _key, List<File> _value)
    {
        List<String> _lines = new Vector<>();
        for(File _f : _value)
        {
            _lines.add(_f.getAbsolutePath());
        }
        
        setConfigAsStringList(_app, _key,_lines);
    }
    
    @SneakyThrows
    public static void setConfigAsFileList(final File _cfg, List<File> _value)
    {
        List<String> _lines = new Vector<>();
        for(File _f : _value)
        {
            _lines.add(_f.getAbsolutePath());
        }
        
        setConfigAsStringList(_cfg,_lines);
    }
    
    @SneakyThrows
    public static List<File> getConfigAsFileList(final String _app, String _key, File _default)
    {
        List<File> _files = new Vector<>();
        for(String _s : getConfigAsStringList(_app, _key, _default==null ? null: _default.getAbsolutePath()))
        {
            _files.add(new File(_s).getAbsoluteFile());
        }
        return _files;
    }
    
    @SneakyThrows
    public static List<File> getConfigAsFileList(final File _cfg, String _key, File _default)
    {
        List<File> _files = new Vector<>();
        for(String _s : getConfigAsStringList(_cfg, _default==null ? null: _default.getAbsolutePath()))
        {
            _files.add(new File(_s).getAbsoluteFile());
        }
        return _files;
    }
    
    
    @SneakyThrows
    public static void setConfigAsInteger(final String _app, String _key, int _value)
    {
        File _cfg = getConfigFile(_app, _key);
        Files.writeString(_cfg.toPath(), Integer.toString(_value));
    }
    
    @SneakyThrows
    public static void setConfigAsInteger(final File _cfg, int _value)
    {
        Files.writeString(_cfg.toPath(), Integer.toString(_value));
    }
    
    @SneakyThrows
    public static boolean getConfigAsBoolean(final String _app, String _key, boolean _default)
    {
        return CommonUtil.checkBoolean(getConfigAsString(_app, _key, Boolean.toString(_default)));
    }
    
    @SneakyThrows
    public static boolean getConfigAsBoolean(final File _cfg, boolean _default)
    {
        return CommonUtil.checkBoolean(getConfigAsString(_cfg, Boolean.toString(_default)));
    }
    
    @SneakyThrows
    public static int getConfigAsInteger(final String _app, String _key, int _default)
    {
        return CommonUtil.checkInteger(getConfigAsString(_app, _key, Integer.toString(_default)));
    }

    @SneakyThrows
    public static int getConfigAsInteger(final File _cfg, int _default)
    {
        return CommonUtil.checkInteger(getConfigAsString(_cfg, Integer.toString(_default)));
    }
}
