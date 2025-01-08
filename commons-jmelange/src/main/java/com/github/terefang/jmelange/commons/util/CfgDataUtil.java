package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.commons.CommonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.github.terefang.jmelange.plexus.util.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Slf4j
public class CfgDataUtil
{
    
    static String[] _MAC_FONT_DIRS = {
            "/Library/Fonts",
            "/System/Library/Fonts",
            OsUtil.getUserHomeDirectory()+"/Library/Fonts",
            "/Network/Library/Fonts" };
    static String[] _LNX_FONT_DIRS = {
            "/usr/share/fonts", "/usr/local/share/fonts",
            "/usr/X11R6/lib/X11/fonts", "/usr/X11/lib/X11/fonts", "/usr/lib/X11/fonts",
            OsUtil.getUserHomeDirectory()+"/.local/share/fonts",
            OsUtil.getUnixyUserDataDirectory("fonts") };
    static String[] _WIN_FONT_DIRS = {
            System.getenv("SYSTEMROOT")+"/Fonts", "C:/Windows/Fonts",
            System.getenv("LOCALAPPDATA")+"/Microsoft/Windows/Fonts",
            System.getenv("APPDATA")+"/Local/Microsoft/Windows/Fonts",
            OsUtil.getUserHomeDirectory()+"/AppData/Local/Microsoft/Windows/Fonts",
            System.getenv("APPDATA")+"/Roaming/Microsoft/Windows/Fonts",
            OsUtil.getUserHomeDirectory()+"/AppData/Roaming/Microsoft/Windows/Fonts"
    };
    
    public static List<File> getFontDirs()
    {
        List<File> _ret = new Vector<>();
        String[] _tochecK = null;
        if(OsUtil.isMac || OsUtil.isIos)
        {
            _tochecK = _MAC_FONT_DIRS;
        }
        else
        if(OsUtil.isAndroid || OsUtil.isLinux /* unix ?*/)
        {
            _tochecK = _LNX_FONT_DIRS;
        }
        else
        if(OsUtil.isWindows)
        {
            _tochecK = _WIN_FONT_DIRS;
        }

        if(_tochecK!=null)
        {
            for(String _path : _tochecK)
            {
                File _f = new File(_path);
                if(_f.exists() && _f.isDirectory())
                {
                    _ret.add(_f);
                }
            }
        }
        
        if(System.getenv("XDG_DATA_HOME")!=null)
        {
            _ret.add(new File(System.getenv("XDG_DATA_HOME"), "fonts"));
        }
        
        if(System.getenv("XDG_DATA_DIRS")!=null)
        {
            for(String _part : System.getenv("XDG_DATA_DIRS").split(":"))
            {
                _ret.add(new File(_part, "fonts"));
            }
        }
        
        return _ret;
    }
    
    
    public static File getCacheDir(String _app)
    {
        File _dir = null;
        if(_app==null)
        {
            _dir = new File(OsUtil.getUserCacheDirectory(),OsUtil.getApplicationName());
        }
        else
        {
            _dir = new File(OsUtil.getUserCacheDirectory(), _app);
        }
        
        _dir.mkdirs();
        return _dir;
    }
    
    public static File getCacheDir()
    {
        return getCacheDir(null);
    }
    
    public static File getCacheFile(String _app, String _key)
    {
        return new File(getCacheDir(_app),_key);
    }
    
    public static File getCacheFile(String _key)
    {
        return new File(getCacheDir(),_key);
    }
    
    @SneakyThrows
    public static String getCacheDataAsString(String _app, String _key)
    {
        return Files.readString(getCacheFile(_app,_key).toPath(), StandardCharsets.UTF_8);
    }
    
    @SneakyThrows
    public static String getCacheDataAsString(String _key)
    {
        return Files.readString(getCacheFile(_key).toPath(), StandardCharsets.UTF_8);
    }
    
    @SneakyThrows
    public static void setCacheDataAsString(String _app, String _key, String _data)
    {
        Files.writeString(getCacheFile(_app,_key).toPath(), _data, StandardCharsets.UTF_8);
    }
    
    @SneakyThrows
    public static void setCacheDataAsString(String _key, String _data)
    {
        Files.writeString(getCacheFile(_key).toPath(), _data, StandardCharsets.UTF_8);
    }
    
    @SneakyThrows
    public static byte[] getCacheDataAsBytes(String _app, String _key)
    {
        return Files.readAllBytes(getCacheFile(_app,_key).toPath());
    }
    
    @SneakyThrows
    public static byte[] getCacheDataAsBytes(String _key)
    {
        return Files.readAllBytes(getCacheFile(_key).toPath());
    }
    
    @SneakyThrows
    public static void setCacheDataAsBytes(String _app, String _key, byte[] _data)
    {
        Files.write(getCacheFile(_app,_key).toPath(), _data);
    }
    
    @SneakyThrows
    public static void setCacheDataAsBytes(String _key, byte[] _data)
    {
        Files.write(getCacheFile(_key).toPath(), _data);
    }
    
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
    
    public static List<File> getConfigDirs()
    {
        return getConfigDirs(OsUtil.getApplicationName());
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
    
    public static List<File> getDataDirs()
    {
        return getDataDirs(OsUtil.getApplicationName());
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
    public static List<File> scanForDataFiles(boolean doprint, String _prefix, String... _exts)
    {
        return scanForFiles(getDataDirs(), doprint, _prefix, _exts);
    }
    
    @SneakyThrows
    public static List<File> scanForDataFiles(final String _app, boolean doprint, String _prefix, String... _exts)
    {
        return scanForFiles(getDataDirs(_app), doprint, _prefix, _exts);
    }
    
    @SneakyThrows
    public static List<File> scanForConfigFiles(boolean doprint, String _prefix, String... _exts)
    {
        return scanForFiles(getConfigDirs(), doprint, _prefix, _exts);
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
    
    public static File getConfigDir()
    {
        File _f = new File(OsUtil.getUserConfigDirectory(OsUtil.getApplicationName()));
        _f.mkdirs();
        return _f;
    }
    
    public static File getConfigDir(final String _app)
    {
        File _f = new File(OsUtil.getUserConfigDirectory(_app));
        _f.mkdirs();
        return _f;
    }
    
    public static File getDataDir()
    {
        File _f = new File(OsUtil.getUserDataDirectory(OsUtil.getApplicationName()));
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
    
    public static File getConfigFile(String _file)
    {
        return new File(getConfigDir(), _file);
    }
    public static File getConfigFile(final String _app, String _file)
    {
        return new File(getConfigDir(_app), _file);
    }
    
    public static File getDataFile(String _file)
    {
        return new File(getDataDir(), _file);
    }
    public static File getDataFile(final String _app, String _file)
    {
        return new File(getDataDir(_app), _file);
    }
    
    public static File getLastDir()
    {
        return getLastDirFromConfig(null);
    }
    
    public static File getLastDirFromConfig(String _default)
    {
        try
        {
            return new File(getConfigAsString(LASTDIR, _default==null ? null : _default));
        }
        catch(Exception _xe)
        {
            return _default!=null ? new File(_default) : null;
        }
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
    public static void setLastDirToConfig(File _ld)
    {
        setLastDirToConfig(OsUtil.getApplicationName(), _ld.getAbsolutePath());
    }
    
    @SneakyThrows
    public static void setLastDirToConfig(final String _app, String _ld)
    {
        setConfigAsString(_app, LASTDIR, _ld);
    }
    
    @SneakyThrows
    public static void setLastDirToConfig(String _ld)
    {
        setConfigAsString(OsUtil.getApplicationName(), LASTDIR, _ld);
    }
    
    @SneakyThrows
    public static void setRecentlyToConfig(final String _app, final String _tag, List<String> _ld)
    {
        setConfigAsStringList(_app, _tag, _ld);
    }
    
    @SneakyThrows
    public static void setRecentlyToConfig(final String _tag, List<String> _ld)
    {
        setConfigAsStringList(OsUtil.getApplicationName(), _tag, _ld);
    }
    
    @SneakyThrows
    public static void addRecentlyToConfig(final String _app, final String _tag, String _ld)
    {
        addRecentlyToConfig(_app,_tag,_ld, -1);
    }
    
    @SneakyThrows
    public static void addRecentlyToConfig(final String _tag, String _ld)
    {
        addRecentlyToConfig(OsUtil.getApplicationName(),_tag,_ld, -1);
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
    public static void trimRecentlyToConfig(final String _tag, int _size)
    {
        trimRecentlyToConfig(OsUtil.getApplicationName(),_tag, _size);
    }
    
    @SneakyThrows
    public static List<String> getRecentlyFromConfig(final String _app, final String _tag)
    {
        return getRecentlyFromConfig(_app, _tag,null);
    }
    
    @SneakyThrows
    public static List<String> getRecentlyFromConfig(final String _tag)
    {
        return getRecentlyFromConfig(OsUtil.getApplicationName(), _tag,null);
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
    public static void setRecentToConfig(List<String> _ld)
    {
        setRecentlyToConfig(OsUtil.getApplicationName(), RECENT, _ld);
    }
    
    @SneakyThrows
    public static void addRecentToConfig(final String _app, String _ld)
    {
        addRecentlyToConfig(_app, RECENT,_ld);
    }
    
    @SneakyThrows
    public static void addRecentToConfig(String _ld)
    {
        addRecentlyToConfig(OsUtil.getApplicationName(), RECENT,_ld);
    }
    
    @SneakyThrows
    public static void addRecentToConfig(final String _app, String _ld, int _sz)
    {
        addRecentlyToConfig(_app, RECENT,_ld, _sz);
    }
    @SneakyThrows

    public static void addRecentToConfig(String _ld, int _sz)
    {
        addRecentlyToConfig(OsUtil.getApplicationName(), RECENT,_ld, _sz);
    }
    
    @SneakyThrows
    public static void trimRecentToConfig(final String _app, int _sz)
    {
        trimRecentlyToConfig(_app, RECENT,_sz);
    }
    
    @SneakyThrows
    public static void trimRecentToConfig(int _sz)
    {
        trimRecentlyToConfig(OsUtil.getApplicationName(), RECENT,_sz);
    }
    
    @SneakyThrows
    public static List<String> getRecentFromConfig(final String _app)
    {
        return getRecentlyFromConfig(_app, RECENT);
    }
    
    @SneakyThrows
    public static List<String> getRecentFromConfig()
    {
        return getRecentlyFromConfig(OsUtil.getApplicationName(), RECENT);
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
    public static List<File> getRecentDirsFromConfig()
    {
        return getRecentDirsFromConfig(OsUtil.getApplicationName(), (File)null);
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
    public static String getConfigAsString(String _key, String _default)
    {
        File _cfg = getConfigFile(OsUtil.getApplicationName(), _key);
        return getConfigAsString(_cfg, _default);
    }

    @SneakyThrows
    public static String getConfigAsString(final String _app, String _key, String _default)
    {
        File _cfg = getConfigFile(_app, _key);
        return getConfigAsString(_cfg, _default);
    }
    
    @SneakyThrows
    public static Map<String,?> getConfigAsComplex(String _key, Map<String,?> _default)
    {
        File _cfg = getConfigFile(OsUtil.getApplicationName(), _key);
        return getConfigAsComplex(_cfg,_default);
    }
    
    @SneakyThrows
    public static Map<String,?> getConfigAsComplex(final String _app, String _key, Map<String,?> _default)
    {
        File _cfg = getConfigFile(_app, _key);
        return getConfigAsComplex(_cfg,_default);
    }
    
    @SneakyThrows
    public static Map<String,?> getConfigAsComplex(final File _cfg, Map<String,?> _default)
    {
        if((!_cfg.exists()) ||  (!_cfg.isFile()))
        {
            if(_default==null) return _default;
            LdataUtil.writeTo(_default,_cfg);
        }
        return LdataUtil.loadFrom(_cfg);
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
    public static List<String> getConfigAsStringList(String _key, String _default)
    {
        File _cfg = getConfigFile(OsUtil.getApplicationName(), _key);
        return getConfigAsStringList(_cfg, _default);
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
    public static void setConfigAsString(String _key, String _value)
    {
        File _cfg = getConfigFile(OsUtil.getApplicationName(), _key);
        Files.writeString(_cfg.toPath(), _value);
    }
    
    @SneakyThrows
    public static void setConfigAsString(final File _cfg, String _value)
    {
        Files.writeString(_cfg.toPath(), _value);
    }
    
    @SneakyThrows
    public static void setConfigAsComplex(final String _app, String _key, Map<String,?> _value)
    {
        File _cfg = getConfigFile(_app, _key);
        setConfigAsComplex(_cfg, _value);
    }
    
    @SneakyThrows
    public static void setConfigAsComplex(String _key, Map<String,?> _value)
    {
        File _cfg = getConfigFile(OsUtil.getApplicationName(), _key);
        setConfigAsComplex(_cfg, _value);
    }
    
    @SneakyThrows
    public static void setConfigAsComplex(final File _cfg, Map<String,?> _value)
    {
        LdataUtil.writeTo(_value,_cfg);
    }
    
    @SneakyThrows
    public static void setConfigAsStringList(final String _app, String _key, List<String> _value)
    {
        File _cfg = getConfigFile(_app, _key);
        Files.write(_cfg.toPath(), _value, StandardCharsets.UTF_8);
    }
    
    @SneakyThrows
    public static void setConfigAsStringList(String _key, List<String> _value)
    {
        File _cfg = getConfigFile(OsUtil.getApplicationName(), _key);
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
    public static void setConfigAsFileList(String _key, List<File> _value)
    {
        List<String> _lines = new Vector<>();
        for(File _f : _value)
        {
            _lines.add(_f.getAbsolutePath());
        }
        
        setConfigAsStringList(OsUtil.getApplicationName(), _key,_lines);
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
    public static List<File> getConfigAsFileList(String _key, File _default)
    {
        List<File> _files = new Vector<>();
        for(String _s : getConfigAsStringList(OsUtil.getApplicationName(), _key, _default==null ? null: _default.getAbsolutePath()))
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
    public static void setConfigAsInteger(String _key, int _value)
    {
        File _cfg = getConfigFile(_key);
        Files.writeString(_cfg.toPath(), Integer.toString(_value));
    }
    
    @SneakyThrows
    public static void setConfigAsInteger(final File _cfg, int _value)
    {
        Files.writeString(_cfg.toPath(), Integer.toString(_value));
    }
    
    @SneakyThrows
    public static void setConfigAsBoolean(final String _app, String _key, boolean _value)
    {
        File _cfg = getConfigFile(_app, _key);
        Files.writeString(_cfg.toPath(), Boolean.toString(_value));
    }
    
    @SneakyThrows
    public static void setConfigAsBoolean(String _key, boolean _value)
    {
        File _cfg = getConfigFile(_key);
        Files.writeString(_cfg.toPath(), Boolean.toString(_value));
    }
    
    @SneakyThrows
    public static void setConfigAsBoolean(final File _cfg, boolean _value)
    {
        Files.writeString(_cfg.toPath(), Boolean.toString(_value));
    }
    
    @SneakyThrows
    public static boolean getConfigAsBoolean(final String _app, String _key, boolean _default)
    {
        return CommonUtil.checkBoolean(getConfigAsString(_app, _key, Boolean.toString(_default)));
    }
    
    @SneakyThrows
    public static boolean getConfigAsBoolean(String _key, boolean _default)
    {
        return CommonUtil.checkBoolean(getConfigAsString(_key, Boolean.toString(_default)));
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
    public static int getConfigAsInteger(String _key, int _default)
    {
        return CommonUtil.checkInteger(getConfigAsString(_key, Integer.toString(_default)));
    }
    
    @SneakyThrows
    public static int getConfigAsInteger(final File _cfg, int _default)
    {
        return CommonUtil.checkInteger(getConfigAsString(_cfg, Integer.toString(_default)));
    }
}
