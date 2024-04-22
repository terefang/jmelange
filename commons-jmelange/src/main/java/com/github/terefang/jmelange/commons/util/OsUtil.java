package com.github.terefang.jmelange.commons.util;

import java.io.File;
import java.net.URISyntaxException;

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
                if((DATA_HOME = System.getenv("APPDATA"))==null)
                {
                    DATA_HOME = System.getProperty("user.home")+"/Local Settings/Application Data";
                }
            }
        }

        if(applicationName==null || DATA_HOME==null) return DATA_HOME;

        return DATA_HOME+"/"+applicationName;
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
}
