package com.github.terefang.jmelange.commons;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class GuidUtil {
    public static String randomUUID() { return UUID.randomUUID().toString().toUpperCase(); }

    public static String toUUID(String _name) { return UUID.nameUUIDFromBytes(CommonUtil.checkString(_name).getBytes()).toString().toUpperCase(); }

    public static String randomGUID() {
        UUID _uuid = UUID.randomUUID();
        long _l = System.currentTimeMillis();
        long _n = System.nanoTime();
        return toGUID(_uuid.getMostSignificantBits(), _uuid.getLeastSignificantBits(), _l, _n);
    }

    public static String toGUID(long _n1, long _n2)
    {
        return String.format("%s-%s",
                Long.toString(_n1 & 0x7fffffffffffffffL, 21),
                Long.toString(_n2 & 0x7fffffffffffffffL, 36)
        ).toUpperCase();
    }

    public static String toGUID(long _n1, long _n2, long _n3)
    {
        return String.format("%s-%s-%s",
                Long.toString(_n1 & 0x7ffffffffffffffL, 21),
                Long.toString(_n2 & 0x7ffffffffffffffL, 35),
                Long.toString(_n3 & 0x7ffffffffffffffL, 36)
        ).toUpperCase();
    }

    public static String toGUID(long _n1, long _n2, long _n3, long _n4)
    {
        return String.format("%s-%s-%s-%s",
                Long.toString(_n1 & 0x7fffffffffffffffL, 22),
                Long.toString(_n2 & 0x7fffffffffffffffL, 23),
                Long.toString(_n3 & 0x7fffffffffffffffL, 35),
                Long.toString(_n4 & 0x7fffffffffffffffL, 36)
        ).toUpperCase();
    }

    public static String toGUID(long _n1, long _n2, long _n3, long _n4, long _n5)
    {
        return String.format("%s-%s-%s-%s-%s",
                Long.toString(_n1 & 0x7ffffffffffffffL, 36),
                Long.toString(_n2 & 0x7ffffffffffffffL, 36),
                Long.toString(_n3 & 0x7ffffffffffffffL, 36),
                Long.toString(_n4 & 0x7ffffffffffffffL, 36),
                Long.toString(_n5 & 0x7ffffffffffffffL, 36)
        ).toUpperCase();
    }

    public static String toHashGUID(String _name)
    {
        BigInteger _bi = sha512(CommonUtil.checkString(_name));

        return toGUID(_bi.longValue(),
                _bi.shiftRight(64).longValue(),
                _bi.shiftRight(128).longValue(),
                _bi.shiftRight(192).longValue(),
                _bi.shiftRight(256).longValue());
    }

    public static String toGUID(String _name)
    {
        UUID _uuid = UUID.nameUUIDFromBytes(CommonUtil.checkString(_name).getBytes());
        return toGUID(_name, _uuid.toString());
    }

    public static String toGUID(String _name1, String _name2)
    {
        UUID _uuid1 = UUID.nameUUIDFromBytes(CommonUtil.checkString(_name1).getBytes());
        UUID _uuid2 = UUID.nameUUIDFromBytes(CommonUtil.checkString(_name2).getBytes());
        return toGUID(_uuid1.getMostSignificantBits(), _uuid1.getLeastSignificantBits(), _uuid2.getMostSignificantBits(), _uuid2.getLeastSignificantBits());
    }

    public static String toGUID(String _name1, String _name2, String _name3)
    {
        UUID _uuid1 = UUID.nameUUIDFromBytes(CommonUtil.checkString(_name1).getBytes());
        UUID _uuid2 = UUID.nameUUIDFromBytes(CommonUtil.checkString(_name2).getBytes());
        UUID _uuid3 = UUID.nameUUIDFromBytes(CommonUtil.checkString(_name3).getBytes());
        return toGUID(_uuid1.getMostSignificantBits(), _uuid1.getLeastSignificantBits(), _uuid2.getMostSignificantBits(), _uuid2.getLeastSignificantBits()^_uuid3.getMostSignificantBits(), _uuid3.getLeastSignificantBits());
    }

    public static String toGUID(String _name1, long _num)
    {
        UUID _uuid1 = UUID.nameUUIDFromBytes(CommonUtil.checkString(_name1).getBytes());
        return toGUID(_uuid1.getMostSignificantBits(), _uuid1.getLeastSignificantBits(), _num);
    }

    public static String toGUID(String _name1, String _name2, long _num)
    {
        UUID _uuid1 = UUID.nameUUIDFromBytes(CommonUtil.checkString(_name1).getBytes());
        UUID _uuid2 = UUID.nameUUIDFromBytes(CommonUtil.checkString(_name2).getBytes());
        return toGUID(_uuid1.getMostSignificantBits(), _uuid1.getLeastSignificantBits(), _uuid2.getMostSignificantBits(), _uuid2.getLeastSignificantBits(), _num);
    }

    public static String toGUID(String _name1, String _name2, String _name3, long _num)
    {
        UUID _uuid1 = UUID.nameUUIDFromBytes(CommonUtil.checkString(_name1).getBytes());
        UUID _uuid2 = UUID.nameUUIDFromBytes(CommonUtil.checkString(_name2).getBytes());
        UUID _uuid3 = UUID.nameUUIDFromBytes(CommonUtil.checkString(_name3).getBytes());
        return toGUID(_uuid1.getMostSignificantBits(), _uuid1.getLeastSignificantBits()^_uuid2.getMostSignificantBits(), _uuid2.getLeastSignificantBits()^_uuid3.getMostSignificantBits(), _uuid3.getLeastSignificantBits(), _num);
    }

    public static BigInteger sha512(String _name)
    {
        try {
            MessageDigest _md = MessageDigest.getInstance("SHA-512");
            return new BigInteger(_md.digest(CommonUtil.checkString(_name).getBytes(StandardCharsets.UTF_8)));
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
