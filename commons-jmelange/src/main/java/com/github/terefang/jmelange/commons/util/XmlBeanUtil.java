package com.github.terefang.jmelange.commons.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Slf4j
public class XmlBeanUtil
{
    /**
     @param clazz
     @param name
     @return the file to store the bean, according to its class and name.
     */
    private static File getFileForBean(Class clazz, @NonNull String name)
    {
        String className = clazz.getCanonicalName();
        
        String fileName = className + ".xml";
        
        // Create an storage file.
        File storageFolder = new File(OsUtil.getUserDataDirectory(name));
        storageFolder.mkdirs();
        
        // Create the output file.
        File storageFile = new File(storageFolder, fileName);
        
        return storageFile;
    }
    
    private static File getFileForBean(Class clazz, @NonNull String app, @NonNull String name)
    {
        String className = clazz.getCanonicalName();
        
        String fileName = className + ".xml";
        
        // Create an storage file.
        File storageFolder = new File(OsUtil.getUserDataDirectory(app), name);
        storageFolder.mkdirs();
        
        // Create the output file.
        File storageFile = new File(storageFolder, fileName);
        
        return storageFile;
    }
    
    /**
     Saves a bean, in the given xml file.
     */
    public static boolean saveBean(Object bean, File storageFile)
    {
        // Write XML
        try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(storageFile)))
        {
            encoder.writeObject(bean);
            encoder.flush();
        } catch (Exception ex)
        {
            log.error(XmlBeanUtil.class.getName(), ex);
            return false;
        }
        return true;
    }
    
    /**
     Saves a bean, in a xml file with the given name.
     */
    public static boolean saveBean(Object bean, String name)
    {
        return saveBean(bean, getFileForBean(bean.getClass(), name));
    }
    
    /**
     Loads a bean, of type T.
     
     @param <T> The type of object to return.
     @param clazz The class of the object to return.
     @param storageFile The file where the bean is stored.
     @return The deserialized object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T loadBean(Class<T> clazz, File storageFile)
    {
        T t = null;
        try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(storageFile)))
        {
            t = (T) decoder.readObject();
        } catch (FileNotFoundException ex)
        {
            log.error(XmlBeanUtil.class.getName(), ex);
        }
        return t;
    }
    
    /**
     Loads a bean, of type T.
     
     @param <T> The type of object to return.
     @param clazz The class of the object to return.
     @param name The object's name.
     @return The deserialized object, obtained from the XML file corresponding to the name and class.
     */
    public static <T> T loadBean(Class<T> clazz, String name)
    {
        return loadBean(clazz, getFileForBean(clazz, name));
    }
    
    public static String toXmlString(String string)
    {
        return toXmlString(false, string);
    }
    
    public static String toXmlString(boolean _nbsp, String string)
    {
        StringBuffer sb = new StringBuffer(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;
        
        for (int i = 0; i < len; i++)
        {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (_nbsp && lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                }
                else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            }
            else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == '"')
                    sb.append("&quot;");
                else if (c == '&')
                    sb.append("&amp;");
                else if (c == '<')
                    sb.append("&lt;");
                else if (c == '>')
                    sb.append("&gt;");
                else if (c == '\'')
                    sb.append("&apos;");
                else {
                    int ci = 0xffff & c;
                    if (ci < 160 )
                        // nothing special only 7 Bit
                        sb.append(c);
                    else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(new Integer(ci).toString());
                        sb.append(';');
                    }
                }
            }
        }
        return sb.toString();
    }
}
