package com.github.terefang.jmelange.commons.util;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {

    public static double root(int value, int root) {
        return Math.pow(value, 1.0d / root);
    }

    public static double root(double value, double root) {
        return Math.pow(value, 1.0d / root);
    }

    public static float root(float value, float root) {
        return (float) Math.pow(value, 1.0f / root);
    }

    public static double round(double value, int places) {
        if (((Double) value).isNaN() || ((Float) (float) value).isNaN()) {
            return value;
        }
        if (((Double) value).isInfinite() || ((Float) (float) value).isInfinite()) {
            return value;
        }

        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int diff(int from, int to) {
        return Math.max(from, to) - Math.min (from, to);
    }

    public static long diff(long from, long to) {
        return Math.max(from, to) - Math.min (from, to);
    }

    public static float diff(float from, float to) {
        return Math.max(from, to) - Math.min (from, to);
    }

    public static double diff(double from, double to) {
        return Math.max(from, to) - Math.min (from, to);
    }
    public static int clamp(int _v, int _min, int _max)
    {
        if(_v<_min) return _min;

        if(_v>_max) return _max;

        return _v;
    }

    public static float clamp(float _v, float _min, float _max)
    {
        if(_v<_min) return _min;

        if(_v>_max) return _max;

        return _v;
    }

    public static long clamp(long _v, long _min, long _max)
    {
        if(_v<_min) return _min;

        if(_v>_max) return _max;

        return _v;
    }

    public static double clamp(double _v, double _min, double _max)
    {
        if(_v<_min) return _min;

        if(_v>_max) return _max;

        return _v;
    }

    public static long min(long... array) {
        return NumberUtils.min(array);
    }

    public static int min(int... array) {
        return NumberUtils.min(array);
    }

    public static short min(short... array) {
        return NumberUtils.min(array);
    }

    public static byte min(byte... array) {
        return NumberUtils.min(array);
    }

    public static double min(double... array) {
        return NumberUtils.min(array);
    }

    public static float min(float... array) {
        return NumberUtils.min(array);
    }

    public static long max(long... array) {
        return NumberUtils.max(array);
    }

    public static int max(int... array) {
        return NumberUtils.max(array);
    }

    public static short max(short... array) {
        return NumberUtils.max(array);
    }

    public static byte max(byte... array) {
        return NumberUtils.max(array);
    }

    public static double max(double... array) {
        return NumberUtils.max(array);
    }

    public static float max(float... array) {
        return NumberUtils.max(array);
    }

    public static long min(long a, long b, long c) {
        return NumberUtils.min(a, b, c);
    }

    public static int min(int a, int b, int c) {
        return NumberUtils.min(a, b, c);
    }

    public static short min(short a, short b, short c) {
        return NumberUtils.min(a, b, c);
    }

    public static byte min(byte a, byte b, byte c) {
        return NumberUtils.min(a, b, c);
    }

    public static double min(double a, double b, double c) {
        return NumberUtils.min(a, b, c);
    }

    public static float min(float a, float b, float c) {
        return NumberUtils.min(a, b, c);
    }

    public static long max(long a, long b, long c) {
        return NumberUtils.max(a, b, c);
    }

    public static int max(int a, int b, int c) {
        return NumberUtils.max(a, b, c);
    }

    public static short max(short a, short b, short c) {
        return NumberUtils.max(a, b, c);
    }

    public static byte max(byte a, byte b, byte c) {
        return NumberUtils.max(a, b, c);
    }

    public static double max(double a, double b, double c) {
        return NumberUtils.max(a, b, c);
    }

    public static float max(float a, float b, float c) {
        return NumberUtils.max(a, b, c);
    }

    public static double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    public static int lerp(int a, int b, double t) {
        return (int) (a + (t * (b - a)));
    }
    public static double quadLerp(double a, double b, double t) { return a + (t * t * (3. - 2. * t)) * (b - a); }
    public static int quadLerp(int a, int b, double t) { return (int) (a + (t * t * (3. - 2. * t)) * (b - a)); }

    public static double quinticLerp(double a, double b, double t) {
        return a + (t * t * t * (t * (t * 6. - 15.) + 10.)) * (b - a);
    }

    public static int quinticLerp(int a, int b, double t) {
        return (int) (a + (t * t * t * (t * (t * 6. - 15.) + 10.)) * (b - a));
    }

    public static double radianLerp(double a, double b, double t)
    {
        t = (clamp(t,-1.,1.)-.5)* Math.PI;
        t = (Math.sin(t)/2.)+.5;
        return lerp(a,b,t);
    }

    public static int radianLerp(int a, int b, double t)
    {
        t = (clamp(t,-1.,1.)-.5)* Math.PI;
        t = (Math.sin(t)/2.)+.5;
        return lerp(a,b,t);
    }


    /**
     * When used to repeatedly mutate {@code a} every frame, and a frame had {@code delta} seconds between it and its
     * predecessor, this will make {@code a} get closer and closer to the value of {@code b} as it is repeatedly called.
     * The {@code halfLife} is the number of seconds it takes for {@code a} to halve its difference to {@code b}. Note
     * that a will never actually reach b in a specific timeframe, it will just get very close.
     * <br>
     * Uses <a href="https://mastodon.social/@acegikmo/111931613710775864"> Freya Holmér suggestion</a>.
     *
     */
    public static double approachHalflife(double a, double b, double delta, double halfLife)
    {
        return b+((a-b)*Math.pow(2., -delta/halfLife));
    }

    public static double approachDecay(double a, double b, double delta, double decay)
    {
        return b+((a-b)*Math.exp(-decay*delta));
    }

    // --- 2D -----------------------

    public static double lerp2D(double p00, double p01, double p10, double p11, double t0, double t1)
    {
        return lerp(lerp(p00,p01,t0),lerp(p10,p11,t0), t1);
    }

    public static int lerp2D(int p00, int p01, int p10, int p11, double t0, double t1)
    {
        return lerp(lerp(p00,p01,t0),lerp(p10,p11,t0), t1);
    }

    public static double radianLerp2D(double p00, double p01, double p10, double p11, double t0, double t1)
    {
        return radianLerp(radianLerp(p00,p01,t0),radianLerp(p10,p11,t0), t1);
    }

    public static int radianLerp2D(int p00, int p01, int p10, int p11, double t0, double t1)
    {
        return radianLerp(radianLerp(p00,p01,t0),radianLerp(p10,p11,t0), t1);
    }

}
