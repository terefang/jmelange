package com.github.terefang.jmelange.gfx.libgdx.files;

import com.github.terefang.jmelange.gfx.libgdx.Files;

public class FilesDelegateSingleton implements Files
{
    private static Files files;
    private static FilesDelegateSingleton instance;

    private FilesDelegateSingleton() {}
    public static synchronized FilesDelegateSingleton getInstance()
    {
        if(instance==null)
        {
            instance = new FilesDelegateSingleton();
        }
        return instance;
    }

    public static synchronized void register(Files _f)
    {
        files = _f;
    }

    public FileHandle getFileHandle(String path, Files.FileType type) {
        if(files==null) return null;
        return files.getFileHandle(path, type);
    }

    public FileHandle classpath(String path) {
        if(files==null) return null;
        return files.classpath(path);
    }

    public FileHandle internal(String path) {
        if(files==null) return null;
        return files.internal(path);
    }

    public FileHandle external(String path) {
        if(files==null) return null;
        return files.external(path);
    }

    public FileHandle absolute(String path) {
        if(files==null) return null;
        return files.absolute(path);
    }

    public FileHandle local(String path) {
        if(files==null) return null;
        return files.local(path);
    }

    public String getExternalStoragePath() {
        if(files==null) return null;
        return files.getExternalStoragePath();
    }

    public boolean isExternalStorageAvailable() {
        if(files==null) return false;
        return files.isExternalStorageAvailable();
    }

    public String getLocalStoragePath() {
        if(files==null) return null;
        return files.getLocalStoragePath();
    }

    public boolean isLocalStorageAvailable() {
        if(files==null) return false;
        return files.isLocalStorageAvailable();
    }
}
