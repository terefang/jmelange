package com.github.terefang.jmelange.commons.util;

public class BitsUtil
{
    //binary_and()	Returns a result of the bitwise and operation between two values.
    public static final long binaryAnd(long _a, long _b)
    {
        return (_a&_b);
    }

    public static final int binaryAnd(int _a, int _b)
    {
        return (_a&_b);
    }

    public static final long binaryAnd(long _a, long... _b)
    {
        for(long _x : _b) _a&=_x;
        return _a;
    }

    public static final int binaryAnd(int _a, int... _b)
    {
        for(int _x : _b) _a&=_x;
        return _a;
    }

    //binary_not()	Returns a bitwise negation of the input value.
    public static final long binaryNot(long _l)
    {
        return (~_l);
    }

    public static final int binaryNot(int _l)
    {
        return (~_l);
    }

    //binary_or()	Returns a result of the bitwise or operation of the two values.
    public static final long binaryOr(long _a, long _b)
    {
        return (_a|_b);
    }

    public static final int binaryOr(int _a, int _b)
    {
        return (_a|_b);
    }

    public static final long binaryOr(long _a, long... _b)
    {
        for(long _x : _b) _a|=_x;
        return _a;
    }

    public static final int binaryOr(int _a, int... _b)
    {
        for(int _x : _b) _a|=_x;
        return _a;
    }

    //binary_shift_left()	Returns binary shift left operation on a pair of numbers: a << n.
    public static final long binaryShiftLeft(long _a, long _n)
    {
        return (_a<<_n);
    }

    public static final int binaryShiftLeft(int _a, int _n)
    {
        return (_a<<_n);
    }

    //binary_shift_right()	Returns binary shift right operation on a pair of numbers: a >> n.
    public static final long binaryShiftRight(long _a, long _n)
    {
        return (_a>>>_n);
    }

    public static final int binaryShiftRight(int _a, int _n)
    {
        return (_a>>>_n);
    }

    //binary_xor()	Returns a result of the bitwise xor operation of the two values.
    public static final long binaryXor(long _a, long _b)
    {
        return (_a^_b);
    }

    public static final int binaryXor(int _a, int _b)
    {
        return (_a^_b);
    }

    //bitset_count_ones() Returns the number of set bits in the binary representation of a number.
    public static final int bitsetCountOnes(long _l)
    {
        return Long.bitCount(_l);
    }

    public static final int bitsetCountZeros(long _l)
    {
        return 64-Long.bitCount(_l);
    }

    public static final int bitsetCountOnes(int _l)
    {
        return Integer.bitCount(_l);
    }

    public static final int bitsetCountZeros(int _l)
    {
        return 32-Integer.bitCount(_l);
    }

    // is_bit_set()
    public static final boolean bitIsOne(long _l, int _bit)
    {
        return (_l & (1L<<_bit)) != 0L;
    }

    public static final boolean bitIsOne(int _l, int _bit)
    {
        return (_l & (1<<_bit)) != 0;
    }

    public static final boolean bitIsZero(long _l, int _bit)
    {
        return (_l & (1L<<_bit)) == 0L;
    }

    public static final boolean bitIsZero(int _l, int _bit)
    {
        return (_l & (1<<_bit)) == 0;
    }

}
