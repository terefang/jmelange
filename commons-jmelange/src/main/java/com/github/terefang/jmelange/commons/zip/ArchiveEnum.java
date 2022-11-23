package com.github.terefang.jmelange.commons.zip;

import lombok.SneakyThrows;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.OutputStream;

public enum ArchiveEnum {
    ar,
    tar,
    bsdar,
    pax,
    zip;

    @SneakyThrows
    public static ArchiveOutputStream createStream(ArchiveEnum _fmt, OutputStream _baos)
    {
        switch(_fmt)
        {
            case ar:
                return new ArArchiveOutputStream(_baos);
            case bsdar:
                ArArchiveOutputStream _ar = new ArArchiveOutputStream(_baos);
                _ar.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
                return _ar;
            case tar:
                return new TarArchiveOutputStream(_baos);
            case pax:
                TarArchiveOutputStream _tar = new TarArchiveOutputStream(_baos);
                _tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
                return _tar;
            case zip:
            default:
                ZipArchiveOutputStream _zip = new ZipArchiveOutputStream(_baos);
                return _zip;
        }
    }

    @SneakyThrows
    public static ArchiveEntry createEntry(ArchiveEnum _fmt,
                                           String _name,
                                           boolean _isdir,
                                           boolean _islink,
                                           final long _length,
                                           final int _userId,
                                           final int _groupId,
                                           final int _mode,
                                           final long _lastModified)
    {
        switch(_fmt)
        {
            case ar:
            case bsdar:
                return new ArArchiveEntry(_name, _length, _userId, _groupId, _mode, _lastModified);
            case tar:
            case pax:
                TarArchiveEntry _ent = new TarArchiveEntry(_name, _islink ? TarConstants.LF_LINK : TarConstants.LF_NORMAL);
                _ent.setSize(_isdir ? 0 : _length);
                _ent.setUserId(_userId);
                _ent.setGroupId(_userId);
                _ent.setMode(_mode);
                _ent.setModTime(_lastModified);
                return _ent;
            case zip:
            default:
                ZipArchiveEntry _znt = new ZipArchiveEntry(_name);
                _znt.setSize(_isdir ? 0 : _length);
                _znt.setUnixMode(_mode);
                _znt.setTime(_lastModified);
                return _znt;

        }
    }
}
