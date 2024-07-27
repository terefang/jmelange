package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.commons.version.Version;
import com.github.terefang.jmelange.commons.version.VersionType;

public class VersionUtil
{
    // 1.2.3.rc-5
    public static Version fromSimple(String _ver)
    {
        String [] _parts = _ver.split("[\\.\\-/]", 5);
        int _maj = 0;
        int _min = 0;
        int _bld = 0;
        VersionType _vt = VersionType.RELEASE;
        int _rel = 0;

        if(_parts.length>=1) _maj = NumberUtil.checkInt(_parts[0]);
        if(_parts.length>=2) _min = NumberUtil.checkInt(_parts[1]);
        if(_parts.length>=3) _bld = NumberUtil.checkInt(_parts[2]);
        if(_parts.length>=4) _vt = VersionType.valueOf(_parts[3].toUpperCase());
        if(_parts.length>=5) _rel = NumberUtil.checkInt(_parts[4]);

        return new Version(_maj,_min,_bld, _vt, _rel);
    }

    public static String toSimple(Version _ver)
    {
        return String.format("%d.%d.%d.%s-%d", _ver.major(), _ver.minor(), _ver.build(), _ver.type().getName(), _ver.release());
    }

    // 1.2.3.4-type / GRADLE
    public static Version fromGradle(String _ver)
    {
        String [] _parts = _ver.split("[\\.\\-]", 5);
        int _maj = 0;
        int _min = 0;
        int _bld = 0;
        int _rel = 0;
        VersionType _vt = VersionType.RELEASE;

        if(_parts.length>=1) _maj = NumberUtil.checkInt(_parts[0]);
        if(_parts.length>=2) _min = NumberUtil.checkInt(_parts[1]);
        if(_parts.length>=3) _bld = NumberUtil.checkInt(_parts[2]);
        if(_parts.length>=4) _rel = NumberUtil.checkInt(_parts[3]);

        try {
            if(_parts.length>=3) _vt = VersionType.valueOf(_parts[_parts.length-1].toUpperCase());
        }
        catch (Exception _xe) { /* IGNORE */ }

        return new Version(_maj,_min,_bld, _vt, _rel);
    }

    public static String toGradle(Version _ver)
    {
        if(_ver.build()==0 && _ver.release()==0)
        {
            return String.format("%d.%d-%s", _ver.major(), _ver.minor(), _ver.type().getName());
        }
        else
        if(_ver.build()==0)
        {
            return String.format("%d.%d.%d-%s", _ver.major(), _ver.minor(), _ver.release(), _ver.type().getName());
        }
        return String.format("%d.%d.%d.%d-%s", _ver.major(), _ver.minor(), _ver.build(), _ver.release(), _ver.type().getName());
    }


    public static String toHex(Version _ver)
    {
        return String.format("%04X%02X%04X%01X%03X", _ver.major(), _ver.minor(), _ver.build(), _ver.type().ordinal(), _ver.release());
    }

    public static Version fromHex(String _ver)
    {
        // "%04X %02X %04X %01X %03X"
        int _maj = 0;
        int _min = 0;
        int _bld = 0;
        VersionType _vt = VersionType.RELEASE;
        int _rel = 0;

        if(_ver.length()>=4) _maj = Integer.parseUnsignedInt(_ver.substring(0,4), 16);
        if(_ver.length()>=6) _min = Integer.parseUnsignedInt(_ver.substring(4,6), 16);
        if(_ver.length()>=10) _bld = Integer.parseUnsignedInt(_ver.substring(6,10), 16);
        if(_ver.length()>=11) _vt = VersionType.values()[Integer.parseUnsignedInt(_ver.substring(10,11), 16)];
        if(_ver.length()>=14) _rel = Integer.parseUnsignedInt(_ver.substring(11,14), 16);

        return new Version(_maj,_min,_bld, _vt, _rel);

    }

    static long _MAJOR_MASK = 0xffff0000000000L;
    static long _MINOR_MASK = 0x0000ff00000000L;
    static long _BUILD_MASK = 0x000000ffff0000L;
    static long _TYPE_MASK = 0x0000000000f000L;
    static long _RELEASE_MASK = 0x00000000000fffL;

    public static boolean isSameMajor(Version _v1, Version _v2)
    {
        long _1 = _v1.toLong();
        long _2 = _v2.toLong();
        return (_1 & _MAJOR_MASK) == (_2 & _MAJOR_MASK);
    }

    public static boolean isSameMinor(Version _v1, Version _v2)
    {
        long _1 = _v1.toLong();
        long _2 = _v2.toLong();
        return (_1 & (_MAJOR_MASK|_MINOR_MASK)) == (_2 & (_MAJOR_MASK|_MINOR_MASK));
    }

    public static boolean isSameBuild(Version _v1, Version _v2)
    {
        long _1 = _v1.toLong();
        long _2 = _v2.toLong();
        return (_1 & (_MAJOR_MASK|_MINOR_MASK|_BUILD_MASK)) == (_2 & (_MAJOR_MASK|_MINOR_MASK|_BUILD_MASK));
    }

    public static boolean isSame(Version _v1, Version _v2)
    {
        long _1 = _v1.toLong();
        long _2 = _v2.toLong();
        return (_1 == _2);
    }

    public static void main(String[] args) {
        Version _ver = VersionUtil.fromSimple("1.2.3/rc-5");
        System.err.println(VersionUtil.toHex(_ver));
        System.err.println(VersionUtil.toSimple(_ver));
        System.err.println(VersionUtil.toGradle(_ver));

        _ver = VersionUtil.fromSimple("1.2");
        System.err.println(VersionUtil.toHex(_ver));
        System.err.println(VersionUtil.toSimple(_ver));
        System.err.println(VersionUtil.toGradle(_ver));
    }
}
