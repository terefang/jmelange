package com.github.terefang.jmelange.randfractal.utils;

import java.awt.*;

/**
 * Class containing various mathematical functions
 */
public class MathHelper {
    /**
     * A "close to zero" double epsilon value for use
     */
    public static final double DBL_EPSILON = Double.longBitsToDouble(0x3cb0000000000000L);

    public static final double PI = Math.PI;

    public static final double SQUARED_PI = PI * PI;

    public static final double HALF_PI = 0.5 * PI;

    public static final double QUARTER_PI = 0.5 * HALF_PI;

    public static final double TWO_PI = 2.0 * PI;

    public static final double THREE_PI_HALVES = TWO_PI - HALF_PI;

    public static final double DEGTORAD = PI / 180.0;

    public static final double RADTODEG = 180.0 / PI;

    public static final double SQRTOFTWO = Math.sqrt(2.0);

    public static final double HALF_SQRTOFTWO = 0.5 * SQRTOFTWO;

    /**
     * Calculates the squared length of all axis offsets given
     *
     * @param values of the axis to get the squared length of
     * @return the squared length
     */
    public static double lengthSquared(double... values) {
        double rval = 0;
        for (double value : values) {
            rval += value * value;
        }
        return rval;
    }

    /**
     * Calculates the length of all axis offsets given
     *
     * @param values of the axis to get the length of
     * @return the length
     */
    public static double length(double... values) {
        return Math.sqrt(lengthSquared(values));
    }

    /**
     * Gets the difference between two angles
     * This value is always positive (0 - 180)
     *
     * @param angle1
     * @param angle2
     * @return the positive angle difference
     */
    public static double getAngleDifference(double angle1, double angle2) {
        return Math.abs(wrapAngle(angle1 - angle2));
    }

    /**
     * Gets the difference between two radians
     * This value is always positive (0 - PI)
     *
     * @param radian1
     * @param radian2
     * @return the positive radian difference
     */
    public static double getRadianDifference(double radian1, double radian2) {
        return Math.abs(wrapRadian(radian1 - radian2));
    }

    /**
     * Wraps the angle between -180 and 180 degrees
     *
     * @param angle to wrap
     * @return -180 > angle <= 180
     */
    public static double wrapAngle(double angle) {
        angle %= 360f;
        if (angle <= -180) {
            return angle + 360;
        } else if (angle > 180) {
            return angle - 360;
        } else {
            return angle;
        }
    }

    /**
     * Wraps a byte between 0 and 256
     *
     * @param value to wrap
     * @return 0 >= byte < 256
     */
    public static byte wrapByte(int value) {
        value %= 256;
        if (value < 0) {
            value += 256;
        }
        return (byte) value;
    }

    /**
     * Wraps the radian between -PI and PI
     *
     * @param radian to wrap
     * @return -PI > radian <= PI
     */
    public static double wrapRadian(double radian) {
        radian %= TWO_PI;
        if (radian <= -PI) {
            return radian + TWO_PI;
        } else if (radian > PI) {
            return radian - TWO_PI;
        } else {
            return radian;
        }
    }

    /**
     * Rounds a number to the amount of decimals specified
     *
     * @param input to round
     * @param decimals to round to
     * @return the rounded number
     */
    public static double round(double input, int decimals) {
        double p = Math.pow(10, decimals);
        return Math.round(input * p) / p;
    }

    /**
     * Performs linear interpolation between two values
     *
     * @param n0 first value
     * @param n1 second value
     * @param a the alpha value. Should be between 0 and 1.
     *
     * @return the interpolated value
     */
    public static double lerp(double n0, double n1, double a) {
        return (1.0 - a) * n0 + (a * n1);
    }

    /**
     * Calculates the linear interpolation between a and b with the given
     * percent
     *
     * @param a
     * @param b
     * @param percent
     * @return
     */
    public static int lerp(int a, int b, double percent) {
        return (int) ((1 - percent) * a + percent * b);
    }


    /**
     * Calculates the linear interpolation between a and b with the given
     * percent
     *
     * @param a
     * @param b
     * @param percent
     * @return
     */
    public static Color lerp(Color a, Color b, double percent) {
        int red = lerp(a.getRed(), b.getRed(), percent);
        int blue = lerp(a.getBlue(), b.getBlue(), percent);
        int green = lerp(a.getGreen(), b.getGreen(), percent);
        int alpha = lerp(a.getAlpha(), b.getAlpha(), percent);
        return new Color(red, green, blue, alpha);
    }

    public static Color blend(Color a, Color b) {
        int red = lerp(a.getRed(), b.getRed(), (a.getAlpha()/255.0));
        int blue = lerp(a.getBlue(), b.getBlue(), (a.getAlpha()/255.0));
        int green = lerp(a.getGreen(), b.getGreen(), (a.getAlpha()/255.0));
        int alpha = lerp(a.getAlpha(), b.getAlpha(), (a.getAlpha()/255.0));
        return new Color(red, green, blue, alpha);
    }

    public static double clamp(double value, double low, double high) {
        if (value < low) {
            return low;
        }
        if (value > high) {
            return high;
        }
        return value;
    }

    public static int clamp(int value, int low, int high) {
        if (value < low) {
            return low;
        }
        if (value > high) {
            return high;
        }
        return value;
    }

    //Fast Math Implementation
    public final static double cos(final double x) {
        return sin(x + (x > HALF_PI ? -THREE_PI_HALVES : HALF_PI));
    }

    public final static double sin(double x) {
        x = sin_a * x * Math.abs(x) + sin_b * x;
        return sin_p * (x * Math.abs(x) - x) + x;
    }

    public final static double tan(final double x) {
        return sin(x) / cos(x);
    }

    public final static double asin(final double x) {
        return x * (Math.abs(x) * (Math.abs(x) * asin_a + asin_b) + asin_c) + Math.signum(x) * (asin_d - Math.sqrt(1 - x * x));
    }

    public final static double acos(final double x) {
        return HALF_PI - asin(x);
    }

    public final static double atan(final double x) {
        return Math.abs(x) < 1 ? x / (1 + atan_a * x * x) : Math.signum(x) * HALF_PI - x / (x * x + atan_a);
    }

    public final static double inverseSqrt(double x) {
        final double xhalves = 0.5d * x;
        x = Double.longBitsToDouble(0x5FE6EB50C7B537AAl - (Double.doubleToRawLongBits(x) >> 1));
        return x * (1.5d - xhalves * x * x);
    }

    public final static double sqrt(final double x) {
        return x * inverseSqrt(x);
    }

    private static final double sin_a = -4 / SQUARED_PI;

    private static final double sin_b = 4 / PI;

    private static final double sin_p = 9d / 40;

    private final static double asin_a = -0.0481295276831013447d;

    private final static double asin_b = -0.343835993947915197d;

    private final static double asin_c = 0.962761848425913169d;

    private final static double asin_d = 1.00138940860107040d;

    private final static double atan_a = 0.280872d;

    // Integer Maths

    public static int floor(float x) {
        int y = (int) x;
        if (x < y) {
            return y - 1;
        }
        return y;
    }

    public static int floor(double x) {
        int y = (int) x;
        if (x < y) {
            return y - 1;
        }
        return y;
    }

    /**
     * Gets the maximum byte value from two values
     *
     * @param value1
     * @param value2
     * @return the maximum value
     */
    public static byte max(byte value1, byte value2) {
        return value1 > value2 ? value1 : value2;
    }

    /**
     * Rounds an integer up to the next power of 2.
     *
     * @param x
     * @return the lowest power of 2 greater or equal to x
     */
    public static int roundUpPow2(int x) {
        if (x <= 0) {
            return 1;
        } else if (x > 0x40000000) {
            throw new IllegalArgumentException("Rounding " + x + " to the next highest power of two would exceed the int range");
        } else {
            x--;
            x |= x >> 1;
            x |= x >> 2;
            x |= x >> 4;
            x |= x >> 8;
            x |= x >> 16;
            x++;
            return x;
        }
    }

    /**
     * Casts a value to a double. May return null.
     *
     * @param o
     * @return
     */
    public static Float castFloat(Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof Number) {
            return ((Number) o).floatValue();
        }

        try {
            return Float.valueOf(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Casts a value to a byte. May return null.
     *
     * @param o
     * @return
     */
    public static Byte castByte(Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof Number) {
            return ((Number)o).byteValue();
        }

        try {
            return Byte.valueOf(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Casts a value to a short. May return null.
     *
     * @param o
     * @return
     */
    public static Short castShort(Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof Number) {
            return ((Number)o).shortValue();
        }

        try {
            return Short.valueOf(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Casts a value to an integer. May return null.
     *
     * @param o
     * @return
     */
    public static Integer castInt(Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof Number) {
            return ((Number)o).intValue();
        }

        try {
            return Integer.valueOf(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Casts a value to a double. May return null.
     *
     * @param o
     * @return
     */
    public static Double castDouble(Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof Number) {
            return ((Number)o).doubleValue();
        }

        try {
            return Double.valueOf(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Casts a value to a long. May return null.
     *
     * @param o
     * @return
     */
    public static Long castLong(Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof Number) {
            return ((Number)o).longValue();
        }

        try {
            return Long.valueOf(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Casts a value to a boolean. May return null.
     *
     * @param o
     * @return
     */
    public static Boolean castBoolean(Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof Boolean) {
            return (Boolean) o;
        } else if (o instanceof String) {
            try {
                return Boolean.parseBoolean((String) o);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        return null;
    }

    /**
     * Calculates the mean of a set of values
     *
     * @param values to calculate the mean of
     * @return the mean
     */
    public static int mean(int... values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum/values.length;
    }

    /**
     * Calculates the mean of a set of values.
     *
     * @param values to calculate the mean of
     */
    public static double mean(double... values) {
        double sum = 0;
        for (double v : values) {
            sum += v;
        }
        return sum/values.length;
    }

    public static int rotl32 (int x, int k) { return (x<<k) | (x>>>(32-k)); }

    public static long rotl64 (long x, int k) { return (x<<k) | (x>>>(64-k)); }



    public static final double SQRT_2 = 1.4142135623730950488;
    public static final double SQRT_3 = 1.7320508075688772935;
    public static final double DEG_TO_RAD = PI / 180.0;
    public static final double RAD_TO_DEG = 1.0 / DEG_TO_RAD;

    /**
     * Performs cubic interpolation between two values bound between two other
     * values
     *
     * @param n0 the value before the first value
     * @param n1 the first value
     * @param n2 the second value
     * @param n3 the value after the second value
     * @param a the alpha value
     * @return the interpolated value
     */
    public static double CubicInterp(double n0, double n1, double n2, double n3, double a) {
        double p = (n3 - n2) - (n0 - n1);
        double q = (n0 - n1) - p;
        double r = n2 - n0;

        return p * a * a * a + q * a * a + r * a + n1;
    }


    /**
     * Maps a value onto a cubic S-Curve
     *
     * @param a the value to map onto a S-Curve
     * @return the mapped value
     */
    public static double SCurve3(double a) {
        return (a * a * (3.0 - 2.0 * a));
    }

    /**
     * maps a value onto a quitnic S-Curve
     *
     * @param a the value to map onto a quitic S-curve
     * @return the mapped value
     */
    public static double SCurve5(double a) {
        double a3 = a * a * a;
        double a4 = a3 * a;
        double a5 = a4 * a;
        return (6.0 * a5) - (15.0 * a4) + (10.0 * a3);
    }

    public static double[] LatLonToXYZ(double latitude, double longitude) {
        double r = MathHelper.cos(Math.toRadians(latitude));
        double x = r * MathHelper.cos(Math.toRadians(longitude));
        double y = MathHelper.sin(Math.toRadians(latitude));
        double z = r * MathHelper.sin(Math.toRadians(longitude));
        return new double[] {x, y, z};
    }

    public static int ClampValue(int value, int lowerBound, int upperBound) {
        if (value < lowerBound) {
            return lowerBound;
        } else if (value > upperBound) {
            return upperBound;
        } else {
            return value;
        }
    }

    public static double ClampValue(double value, double lowerBound, double upperBound)
    {
        if (value < lowerBound)
        {
            return lowerBound;
        }
        else if (value > upperBound)
        {
            return upperBound;
        }
        else
        {
            return value;
        }
    }

    public static int GetMax(int a, int b) {
        return (a > b) ? a : b;
    }

    public static double GetMax(double a, double b) {
        return (a > b) ? a : b;
    }

    public static double GetMin(double a, double b) {
        return (a < b) ? a : b;
    }

    public static int GetMin(int a, int b) {
        return (a < b) ? a : b;
    }

    /**
     * Modifies a doubleing-point value so that it can be stored in a
     * noise::int32 variable.
     *
     * @param n A doubleing-point number.
     * @return The modified doubleing-point number.
     *
     *         This function does not modify @a n.
     *
     *         In libnoise, the noise-generating algorithms are all
     *         integer-based; they use variables of type noise::int32. Before
     *         calling a noise function, pass the @a x, @a y, and @a z
     *         coordinates to this function to ensure that these coordinates can
     *         be cast to a noise::int32 value.
     *
     *         Although you could do a straight cast from double to
     *         noise::int32, the resulting value may differ between platforms.
     *         By using this function, you ensure that the resulting value is
     *         identical between platforms.
     */
    public static double MakeInt32Range(double n) {
        if (n >= 1073741824.0) {
            return (2.0 * n % 1073741824.0) - 1073741824.0;
        } else if (n <= -1073741824.0) {
            return (2.0 * n % 1073741824.0) + 1073741824.0;
        } else {
            return n;
        }
    }

    /**
     *
     *
     * A table of 256 random normalized vectors. Each row is an (x, y, z, 0)
     * coordinate. The 0 is used as padding so we can use bit shifts to index
     * any row in the table. These vectors have an even statistical
     * distribution, which improves the quality of the coherent noise generated
     * by these vectors. For more information, see "GPU Gems", Chapter 5 -
     * Implementing Improved Perlin FastNoise by Ken Perlin, specifically page 76.
     *
     */
    public static final double[] RandomVectors = {
            -0.763874, -0.596439, -0.246489,
            0.0, 0.396055, 0.904518,
            -0.158073, 0.0, -0.499004,
            -0.8665, -0.0131631, 0.0,
            0.468724, -0.824756, 0.316346,
            0.0, 0.829598, 0.43195,
            0.353816, 0.0, -0.454473,
            0.629497, -0.630228, 0.0,
            -0.162349, -0.869962, -0.465628,
            0.0, 0.932805, 0.253451,
            0.256198, 0.0, -0.345419,
            0.927299, -0.144227, 0.0,
            -0.715026, -0.293698, -0.634413,
            0.0, -0.245997, 0.717467,
            -0.651711, 0.0, -0.967409,
            -0.250435, -0.037451, 0.0,
            0.901729, 0.397108, -0.170852,
            0.0, 0.892657, -0.0720622,
            -0.444938, 0.0, 0.0260084,
            -0.0361701, 0.999007, 0.0,
            0.949107, -0.19486, 0.247439,
            0.0, 0.471803, -0.807064,
            -0.355036, 0.0, 0.879737,
            0.141845, 0.453809, 0.0,
            0.570747, 0.696415, 0.435033,
            0.0, -0.141751, -0.988233,
            -0.0574584, 0.0, -0.58219,
            -0.0303005, 0.812488, 0.0,
            -0.60922, 0.239482, -0.755975,
            0.0, 0.299394, -0.197066,
            -0.933557, 0.0, -0.851615,
            -0.220702, -0.47544, 0.0,
            0.848886, 0.341829, -0.403169,
            0.0, -0.156129, -0.687241,
            0.709453, 0.0, -0.665651,
            0.626724, 0.405124, 0.0,
            0.595914, -0.674582, 0.43569,
            0.0, 0.171025, -0.509292,
            0.843428, 0.0, 0.78605,
            0.536414, -0.307222, 0.0,
            0.18905, -0.791613, 0.581042,
            0.0, -0.294916, 0.844994,
            0.446105, 0.0, 0.342031,
            -0.58736, -0.7335, 0.0,
            0.57155, 0.7869, 0.232635,
            0.0, 0.885026, -0.408223,
            0.223791, 0.0, -0.789518,
            0.571645, 0.223347, 0.0,
            0.774571, 0.31566, 0.548087,
            0.0, -0.79695, -0.0433603,
            -0.602487, 0.0, -0.142425,
            -0.473249, -0.869339, 0.0,
            -0.0698838, 0.170442, 0.982886,
            0.0, 0.687815, -0.484748,
            0.540306, 0.0, 0.543703,
            -0.534446, -0.647112, 0.0,
            0.97186, 0.184391, -0.146588,
            0.0, 0.707084, 0.485713,
            -0.513921, 0.0, 0.942302,
            0.331945, 0.043348, 0.0,
            0.499084, 0.599922, 0.625307,
            0.0, -0.289203, 0.211107,
            0.9337, 0.0, 0.412433,
            -0.71667, -0.56239, 0.0,
            0.87721, -0.082816, 0.47291,
            0.0, -0.420685, -0.214278,
            0.881538, 0.0, 0.752558,
            -0.0391579, 0.657361, 0.0,
            0.0765725, -0.996789, 0.0234082,
            0.0, -0.544312, -0.309435,
            -0.779727, 0.0, -0.455358,
            -0.415572, 0.787368, 0.0,
            -0.874586, 0.483746, 0.0330131,
            0.0, 0.245172, -0.0838623,
            0.965846, 0.0, 0.382293,
            -0.432813, 0.81641, 0.0,
            -0.287735, -0.905514, 0.311853,
            0.0, -0.667704, 0.704955,
            -0.239186, 0.0, 0.717885,
            -0.464002, -0.518983, 0.0,
            0.976342, -0.214895, 0.0240053,
            0.0, -0.0733096, -0.921136,
            0.382276, 0.0, -0.986284,
            0.151224, -0.0661379, 0.0,
            -0.899319, -0.429671, 0.0812908,
            0.0, 0.652102, -0.724625,
            0.222893, 0.0, 0.203761,
            0.458023, -0.865272, 0.0,
            -0.030396, 0.698724, -0.714745,
            0.0, -0.460232, 0.839138,
            0.289887, 0.0, -0.0898602,
            0.837894, 0.538386, 0.0,
            -0.731595, 0.0793784, 0.677102,
            0.0, -0.447236, -0.788397,
            0.422386, 0.0, 0.186481,
            0.645855, -0.740335, 0.0,
            -0.259006, 0.935463, 0.240467,
            0.0, 0.445839, 0.819655, -0.359712, 0.0, 0.349962, 0.755022, -0.554499, 0.0, -0.997078, -0.0359577, 0.0673977, 0.0, -0.431163, -0.147516, -0.890133, 0.0, 0.299648, -0.63914, 0.708316, 0.0, 0.397043, 0.566526, -0.722084, 0.0, -0.502489, 0.438308, -0.745246, 0.0, 0.0687235, 0.354097, 0.93268, 0.0, -0.0476651, -0.462597, 0.885286, 0.0, -0.221934, 0.900739, -0.373383, 0.0, -0.956107, -0.225676, 0.186893, 0.0, -0.187627, 0.391487, -0.900852, 0.0, -0.224209, -0.315405, 0.92209, 0.0, -0.730807, -0.537068, 0.421283, 0.0, -0.0353135, -0.816748, 0.575913, 0.0, -0.941391, 0.176991, -0.287153, 0.0, -0.154174, 0.390458, 0.90762, 0.0, -0.283847, 0.533842, 0.796519, 0.0, -0.482737, -0.850448, 0.209052, 0.0, -0.649175, 0.477748, 0.591886, 0.0, 0.885373, -0.405387, -0.227543, 0.0, -0.147261, 0.181623, -0.972279, 0.0, 0.0959236, -0.115847, -0.988624, 0.0, -0.89724, -0.191348, 0.397928, 0.0, 0.903553, -0.428461, -0.00350461, 0.0, 0.849072, -0.295807, -0.437693, 0.0, 0.65551, 0.741754, -0.141804, 0.0, 0.61598, -0.178669, 0.767232, 0.0, 0.0112967, 0.932256, -0.361623, 0.0, -0.793031, 0.258012, 0.551845, 0.0, 0.421933, 0.454311, 0.784585, 0.0, -0.319993, 0.0401618, -0.946568, 0.0, -0.81571, 0.551307, -0.175151, 0.0, -0.377644, 0.00322313, 0.925945, 0.0, 0.129759, -0.666581, -0.734052, 0.0, 0.601901, -0.654237, -0.457919, 0.0, -0.927463, -0.0343576, -0.372334, 0.0, -0.438663, -0.868301, -0.231578, 0.0, -0.648845, -0.749138, -0.133387, 0.0, 0.507393, -0.588294, 0.629653, 0.0, 0.726958, 0.623665, 0.287358, 0.0, 0.411159, 0.367614, -0.834151, 0.0, 0.806333, 0.585117, -0.0864016, 0.0, 0.263935, -0.880876, 0.392932, 0.0, 0.421546, -0.201336, 0.884174, 0.0, -0.683198, -0.569557, -0.456996, 0.0, -0.117116, -0.0406654, -0.992285, 0.0, -0.643679, -0.109196, -0.757465, 0.0, -0.561559, -0.62989, 0.536554, 0.0, 0.0628422, 0.104677, -0.992519, 0.0, 0.480759, -0.2867, -0.828658, 0.0, -0.228559, -0.228965, -0.946222, 0.0, -0.10194, -0.65706, -0.746914, 0.0, 0.0689193, -0.678236, 0.731605, 0.0, 0.401019, -0.754026, 0.52022, 0.0, -0.742141, 0.547083, -0.387203, 0.0, -0.00210603, -0.796417, -0.604745, 0.0, 0.296725, -0.409909, -0.862513, 0.0, -0.260932, -0.798201, 0.542945, 0.0, -0.641628, 0.742379, 0.192838, 0.0, -0.186009, -0.101514, 0.97729, 0.0, 0.106711, -0.962067, 0.251079, 0.0, -0.743499, 0.30988, -0.592607, 0.0, -0.795853, -0.605066, -0.0226607, 0.0, -0.828661, -0.419471, -0.370628, 0.0, 0.0847218, -0.489815, -0.8677, 0.0, -0.381405, 0.788019, -0.483276, 0.0, 0.282042, -0.953394, 0.107205, 0.0, 0.530774, 0.847413, 0.0130696, 0.0, 0.0515397, 0.922524, 0.382484, 0.0, -0.631467, -0.709046, 0.313852, 0.0, 0.688248, 0.517273, 0.508668, 0.0, 0.646689, -0.333782, -0.685845, 0.0, -0.932528, -0.247532, -0.262906, 0.0, 0.630609, 0.68757, -0.359973, 0.0, 0.577805, -0.394189, 0.714673, 0.0, -0.887833, -0.437301, -0.14325, 0.0, 0.690982, 0.174003, 0.701617, 0.0, -0.866701, 0.0118182, 0.498689, 0.0, -0.482876, 0.727143, 0.487949, 0.0, -0.577567, 0.682593, -0.447752, 0.0, 0.373768, 0.0982991, 0.922299, 0.0, 0.170744, 0.964243, -0.202687, 0.0, 0.993654, -0.035791, -0.106632, 0.0, 0.587065, 0.4143, -0.695493, 0.0, -0.396509, 0.26509, -0.878924, 0.0, -0.0866853, 0.83553, -0.542563, 0.0, 0.923193, 0.133398, -0.360443, 0.0, 0.00379108, -0.258618, 0.965972, 0.0, 0.239144, 0.245154, -0.939526, 0.0, 0.758731, -0.555871, 0.33961, 0.0, 0.295355, 0.309513, 0.903862, 0.0, 0.0531222, -0.91003, -0.411124, 0.0, 0.270452, 0.0229439, -0.96246, 0.0, 0.563634, 0.0324352, 0.825387, 0.0, 0.156326, 0.147392, 0.976646, 0.0, -0.0410141, 0.981824, 0.185309, 0.0, -0.385562, -0.576343, -0.720535, 0.0, 0.388281, 0.904441, 0.176702, 0.0, 0.945561, -0.192859, -0.262146, 0.0, 0.844504, 0.520193, 0.127325, 0.0, 0.0330893, 0.999121, -0.0257505, 0.0, -0.592616, -0.482475, -0.644999, 0.0, 0.539471, 0.631024, -0.557476, 0.0, 0.655851, -0.027319, -0.754396, 0.0, 0.274465, 0.887659, 0.369772, 0.0, -0.123419, 0.975177, -0.183842, 0.0, -0.223429, 0.708045, 0.66989, 0.0, -0.908654, 0.196302, 0.368528, 0.0, -0.95759, -0.00863708, 0.288005, 0.0, 0.960535, 0.030592, 0.276472, 0.0, -0.413146, 0.907537, 0.0754161, 0.0, -0.847992, 0.350849, -0.397259, 0.0, 0.614736, 0.395841, 0.68221, 0.0, -0.503504, -0.666128, -0.550234, 0.0, -0.268833, -0.738524, -0.618314, 0.0, 0.792737, -0.60001, -0.107502, 0.0, -0.637582, 0.508144, -0.579032, 0.0, 0.750105, 0.282165, -0.598101, 0.0, -0.351199, -0.392294, -0.850155, 0.0, 0.250126, -0.960993, -0.118025, 0.0, -0.732341, 0.680909, -0.0063274, 0.0, -0.760674, -0.141009, 0.633634, 0.0, 0.222823, -0.304012, 0.926243, 0.0, 0.209178, 0.505671, 0.836984, 0.0, 0.757914, -0.56629, -0.323857, 0.0, -0.782926, -0.339196, 0.52151, 0.0, -0.462952, 0.585565, 0.665424, 0.0, 0.61879, 0.194119, -0.761194, 0.0, 0.741388, -0.276743, 0.611357, 0.0, 0.707571, 0.702621, 0.0752872, 0.0, 0.156562, 0.819977, 0.550569, 0.0, -0.793606, 0.440216, 0.42, 0.0, 0.234547, 0.885309, -0.401517, 0.0, 0.132598, 0.80115, -0.58359, 0.0, -0.377899, -0.639179, 0.669808, 0.0, -0.865993, -0.396465, 0.304748, 0.0, -0.624815, -0.44283, 0.643046, 0.0, -0.485705, 0.825614, -0.287146, 0.0, -0.971788, 0.175535, 0.157529, 0.0, -0.456027, 0.392629, 0.798675, 0.0, -0.0104443, 0.521623, -0.853112, 0.0, -0.660575, -0.74519, 0.091282, 0.0, -0.0157698, -0.307475, -0.951425, 0.0, -0.603467, -0.250192, 0.757121, 0.0, 0.506876, 0.25006, 0.824952, 0.0, 0.255404, 0.966794, 0.00884498, 0.0, 0.466764, -0.874228, -0.133625, 0.0, 0.475077, -0.0682351, -0.877295, 0.0, -0.224967, -0.938972, -0.260233, 0.0, -0.377929, -0.814757, -0.439705, 0.0, -0.305847, 0.542333, -0.782517, 0.0, 0.26658, -0.902905, -0.337191, 0.0, 0.0275773, 0.322158, -0.946284, 0.0, 0.0185422, 0.716349, 0.697496, 0.0, -0.20483, 0.978416, 0.0273371, 0.0, -0.898276, 0.373969, 0.230752, 0.0, -0.00909378, 0.546594, 0.837349, 0.0, 0.6602, -0.751089, 0.000959236, 0.0, 0.855301, -0.303056, 0.420259, 0.0, 0.797138, 0.0623013, -0.600574, 0.0, 0.48947, -0.866813, 0.0951509, 0.0, 0.251142, 0.674531, 0.694216, 0.0, -0.578422, -0.737373, -0.348867, 0.0, -0.254689, -0.514807, 0.818601, 0.0, 0.374972, 0.761612, 0.528529, 0.0, 0.640303, -0.734271, -0.225517, 0.0, -0.638076, 0.285527, 0.715075, 0.0, 0.772956, -0.15984, -0.613995, 0.0, 0.798217, -0.590628, 0.118356, 0.0, -0.986276, -0.0578337, -0.154644, 0.0, -0.312988, -0.94549, 0.0899272, 0.0, -0.497338, 0.178325, 0.849032, 0.0, -0.101136, -0.981014, 0.165477, 0.0, -0.521688, 0.0553434, -0.851339, 0.0, -0.786182, -0.583814, 0.202678, 0.0, -0.565191, 0.821858, -0.0714658, 0.0, 0.437895, 0.152598, -0.885981, 0.0, -0.92394, 0.353436, -0.14635, 0.0, 0.212189, -0.815162, -0.538969, 0.0, -0.859262, 0.143405, -0.491024, 0.0, 0.991353, 0.112814, 0.0670273, 0.0, 0.0337884, -0.979891, -0.196654, 0.0};

}