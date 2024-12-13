package com.github.terefang.jmelange.commons.util;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class OsUtil
{
    private static String OS = System.getProperty("os.name").toLowerCase();

    static public boolean isAndroid = System.getProperty("java.runtime.name").contains("Android");
    static public boolean isMac = !isAndroid && OS.contains("mac");
    static public boolean isWindows = !isAndroid && OS.contains("windows");
    static public boolean isLinux = !isAndroid && OS.contains("linux");
    static public boolean isIos = !isAndroid && (!(isWindows || isLinux || isMac)) || OS.startsWith("ios");

    static public boolean isARM = System.getProperty("os.arch").startsWith("arm") || System.getProperty("os.arch").startsWith("aarch64");
    static public boolean is64Bit = System.getProperty("os.arch").contains("64") || System.getProperty("os.arch").startsWith("armv8");

    public static boolean isGwt = false;

    static {
        try {
            Class.forName("com.google.gwt.core.client.GWT");
            isGwt = true;
        }
        catch(Exception ignored) { /* IGNORED */ }

        boolean isMOEiOS = "iOS".equals(System.getProperty("moe.platform.name"));
        if (isMOEiOS || (!isAndroid && !isWindows && !isLinux && !isMac)) {
            isIos = true;
            isAndroid = false;
            isWindows = false;
            isLinux = false;
            isMac = false;
            is64Bit = false;
        }
    }

    public static boolean isQuartz = false;
    static
    {
        try
        {
            isQuartz = isMac && System.getProperty("apple.awt.graphics.UseQuartz") != null && System.getProperty("apple.awt.graphics.UseQuartz").equals("true");
        }
        catch(Exception ignored) { /* IGNORED */ }
    }


    static String _appName = null;
    
    public static String getApplicationName()
    {
        if(_appName==null)
        {
            _appName = GuidUtil.randomUUID();
        }
        return _appName;
    }
    
    public static void setApplicationName(String _n)
    {
        _appName = _n;
    }
    
    public static String getUserConfigDirectory()
    {
        return getUserConfigDirectory(null);
    }

    public static String getUserConfigDirectory(String applicationName)
    {
        String CONFIG_HOME = null;

        if((CONFIG_HOME = System.getenv("XDG_CONFIG_HOME"))==null)
        {
            if(isLinux || isAndroid)
            {
                CONFIG_HOME = System.getProperty("user.home")+"/.config";
            }
            else if(isMac)
            {
                CONFIG_HOME = System.getProperty("user.home")+"/Library/Preferences";
            }
            else if(isIos)
            {
                CONFIG_HOME = System.getProperty("user.home")+"/Documents";
            }
            else if(isWindows)
            {
                if((CONFIG_HOME = System.getenv("APPDATA"))==null)
                {
                    CONFIG_HOME = System.getProperty("user.home")+"/Local Settings";
                }
            }
        }

        if(applicationName==null || CONFIG_HOME==null) return CONFIG_HOME;

        return CONFIG_HOME+"/"+applicationName;
    }

    public static String getUserDataDirectory()
    {
        return getUserDataDirectory(null);
    }

    public static String getUserDataDirectory(String applicationName)
    {
        String DATA_HOME = null;

        if((DATA_HOME = System.getenv("XDG_DATA_HOME"))==null)
        {
            if(isLinux || isAndroid)
            {
                DATA_HOME = System.getProperty("user.home")+"/.local/share";
            }
            else if(isMac)
            {
                DATA_HOME = System.getProperty("user.home")+"/Library/Application Support";
            }
            else if(isIos)
            {
                DATA_HOME = System.getProperty("user.home")+"/Documents";
            }
            else if(isWindows)
            {
                if((DATA_HOME = System.getenv("LOCALAPPDATA"))==null)
                {
                    if((DATA_HOME = System.getenv("APPDATA"))==null)
                    {
                        DATA_HOME = System.getProperty("user.home")+"/Local Settings/Application Data";
                    }
                }
            }
        }

        if(applicationName==null || DATA_HOME==null) return DATA_HOME;

        return DATA_HOME+"/"+applicationName;
    }
    
    public static String getUserCacheDirectory()
    {
        if(System.getenv("XDG_CACHE_HOME")!=null)
        {
            return new File(System.getenv("XDG_CACHE_HOME")).getAbsolutePath();
        }
        return new File(OsUtil.getUserHomeDirectory(),".cache").getAbsolutePath();
    }
    
    public static String getUserFontDirectory()
    {
        String DATA_HOME = null;
        
        if(isLinux || isAndroid)
        {
            return System.getProperty("user.home")+"/.local/share/fonts";
        }
        else if(isMac)
        {
            return System.getProperty("user.home")+"/Library/Fonts";
        }
        else if(isWindows)
        {
            if((DATA_HOME = System.getenv("LOCALAPPDATA"))!=null)
            {
                return DATA_HOME+"/Microsoft/Windows/Fonts";
            }
            
            DATA_HOME = System.getProperty("user.home")+"/AppData/Local";
            return DATA_HOME+"/Microsoft/Windows/Fonts";
        }
        
        return null;
    }
    
    public static String getSystemFontDirectory()
    {
        String DATA_HOME = null;
        
        if(isLinux || isAndroid)
        {
            return "/usr/share/fonts";
        }
        else if(isMac)
        {
            return "/System/Library/Fonts";
        }
        else if(isWindows)
        {
            if((DATA_HOME = System.getenv("WINDIR"))!=null)
            {
                return DATA_HOME+"/Fonts";
            }
            
            if((DATA_HOME = System.getenv("SYSTEMROOT"))!=null)
            {
                return DATA_HOME+"/Fonts";
            }
            
            return "C:/Windows/Fonts";
        }
        
        return null;
    }
    
    public static String getLocalFontDirectory()
    {
        String DATA_HOME = null;
        
        if(isLinux || isAndroid)
        {
            return "/usr/local/share/fonts";
        }
        else if(isMac)
        {
            return "/Library/Fonts";
        }
        else if(isWindows)
        {
            if((DATA_HOME = System.getenv("LOCALAPPDATA"))!=null)
            {
                return DATA_HOME+"/Microsoft/Windows/Fonts";
            }
            
            DATA_HOME = System.getProperty("user.home")+"/AppData/Local";
            return DATA_HOME+"/Microsoft/Windows/Fonts";
        }
        
        return null;
    }
    
    public static String getSystemDataDirectory()
    {
        return getSystemDataDirectory(null);
    }

    public static String getSystemDataDirectory(String applicationName)
    {
        String DATA_HOME = null;

        if(isLinux)
        {
            DATA_HOME = "/usr/share";
        }
        else if(isAndroid)
        {
            DATA_HOME = "/usr/local/share";
        }
        else if(isMac || isIos)
        {
            DATA_HOME = "/Library/Application Support";
        }
        else if(isWindows)
        {
            if((DATA_HOME = System.getenv("COMMONPROGRAMFILES"))==null)
            {
                if((DATA_HOME = System.getenv("COMMONPROGRAMFILES(x86)"))==null)
                {
                    if((DATA_HOME = System.getenv("CommonProgramW6432"))==null)
                    {
                        DATA_HOME = "C:/Program Files/Common Files";
                    }
                }
            }
        }

        if(applicationName==null || DATA_HOME==null) return DATA_HOME;

        return DATA_HOME+"/"+applicationName;
    }

    public static String getSystemConfigDirectory()
    {
        return getSystemConfigDirectory(null);
    }

    public static String getSystemConfigDirectory(String applicationName)
    {
        String CFG_HOME = null;

        if(isLinux)
        {
            CFG_HOME = "/usr/etc";
        }
        else if(isAndroid)
        {
            CFG_HOME = "/usr/local/etc";
        }
        else if(isMac || isIos)
        {
            CFG_HOME = "/Library/Application Support";
        }
        else if(isWindows)
        {
            if((CFG_HOME = System.getenv("COMMONPROGRAMFILES"))==null)
            {
                if((CFG_HOME = System.getenv("COMMONPROGRAMFILES(x86)"))==null)
                {
                    if((CFG_HOME = System.getenv("CommonProgramW6432"))==null)
                    {
                        CFG_HOME = "C:/Program Files/Common Files";
                    }
                }
            }
        }

        if(applicationName==null || CFG_HOME==null) return CFG_HOME;

        return CFG_HOME+"/"+applicationName;
    }

    public static String getUnixyUserConfigDirectory(String applicationName)
    {
        if(applicationName==null) throw new IllegalArgumentException(applicationName);

        String USER_HOME = System.getProperty("user.home");

        return USER_HOME+"/."+applicationName;
    }

    public static String getUnixyUserDataDirectory(String applicationName)
    {
        if(applicationName==null) throw new IllegalArgumentException(applicationName);

        String USER_HOME = System.getProperty("user.home");

        return USER_HOME+"/."+applicationName;
    }


    public static String getUserHomeDirectory(String applicationName)
    {
        if(applicationName==null) throw new IllegalArgumentException(applicationName);

        String USER_HOME = System.getProperty("user.home");

        return USER_HOME+"/"+applicationName;
    }

    public static String getUserHomeDirectory()
    {
        String USER_HOME = System.getProperty("user.home");

        return USER_HOME;
    }

    public static String getJarDirectory()
    {
        try
        {
            return new File(OsUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParent();
        }
        catch (URISyntaxException exception)
        {
            exception.printStackTrace();
        }

        return getCurrentDirectory();
    }

    public static String getCurrentDirectory()
    {
        return new File("").getAbsolutePath();
    }

    public static List<String> getConfigDirectories()
    {
        return getConfigDirectories(null, true);
    }
    public static List<String> getConfigDirectories(boolean _base)
    {
        return getConfigDirectories(null, _base);
    }
    public static List<String> getConfigDirectories(String _appname, boolean _base)
    {
        List<String> _list = new Vector<>();
        _list.add(new File(getJarDirectory(),"conf").getAbsolutePath());
        if(_appname!=null)
        {
            _list.add(new File(getUserConfigDirectory(_appname)).getAbsolutePath());
            _list.add(new File(getUnixyUserConfigDirectory(_appname)).getAbsolutePath());
            _list.add(new File(getSystemConfigDirectory(_appname)).getAbsolutePath());
        }

        if(_base || _appname==null) {
            _list.add(new File(getUserConfigDirectory()).getAbsolutePath());
            _list.add(new File(getSystemConfigDirectory()).getAbsolutePath());
        }
        return _list;
    }

    public static List<String> getDataDirectories()
    {
        return getDataDirectories(null, true);
    }
    public static List<String> getDataDirectories(boolean _base)
    {
        return getDataDirectories(null, _base);
    }
    public static List<String> getDataDirectories(String _appname, boolean _base)
    {
        List<String> _list = new Vector<>();
        _list.add(new File(getJarDirectory(),"data").getAbsolutePath());
        if(_appname!=null)
        {
            _list.add(new File(getUserDataDirectory(_appname)).getAbsolutePath());
            _list.add(new File(getUnixyUserDataDirectory(_appname)).getAbsolutePath());
            _list.add(new File(getSystemDataDirectory(_appname)).getAbsolutePath());
        }

        if(_base || _appname==null)
        {
            _list.add(new File(getUserDataDirectory()).getAbsolutePath());
            _list.add(new File(getSystemDataDirectory()).getAbsolutePath());
        }
        return _list;
    }

    public static File makeDataFile(String _appname, String _cfname)
    {
        File _file;
        if(_appname!=null)
        {
            _file = new File(getUserDataDirectory(_appname), _cfname);
        }
        else
        {
            _file = new File(getUserDataDirectory(), _cfname);
        }
        _file.getParentFile().mkdirs();
        return _file;
    }

    public static File makeConfigFile(String _appname, String _cfname)
    {
        File _file;
        if(_appname!=null)
        {
            _file = new File(getUserConfigDirectory(_appname), _cfname);
        }
        else
        {
            _file = new File(getUserConfigDirectory(), _cfname);
        }
        _file.getParentFile().mkdirs();
        return _file;
    }

    public static List<File> findDataFiles(String _cfname)
    {
        return findDataFiles((String) null, _cfname);
    }

    public static List<File> findDataFiles(String _appname, String _cfname)
    {
        return findDataFiles(Collections.singletonList(_appname), _cfname);
    }

    public static List<File> findDataFiles(List<String> _appnames, String _cfname)
    {
        List<File> _list = new Vector<>();
        List<String> _dirs = new Vector<>();
        for(String _appname : _appnames)
        {
            _dirs.addAll(getDataDirectories(_appname, false));
        }

        for(String _path : _dirs)
        {
            File _file = new File(_path, _cfname);
            if(_file.exists())
            {
                _list.add(_file);
            }
        }
        return _list;
    }

    public static List<File> findConfigFiles(String _cfname)
    {
        return findConfigFiles((String) null, _cfname);
    }

    public static List<File> findConfigFiles(String _appname, String _cfname)
    {
        return findConfigFiles(Collections.singletonList(_appname), _cfname);
    }

    public static List<File> findConfigFiles(List<String> _appnames, String _cfname)
    {
        List<File> _list = new Vector<>();
        List<String> _dirs = new Vector<>();

        for(String _appname : _appnames) {
            _dirs.addAll(getConfigDirectories(_appname, false));
        }

        for (String _path : _dirs) {
            File _file = new File(_path, _cfname);
            if (_file.exists()) {
                _list.add(_file);
            }
        }
        return _list;
    }
}
