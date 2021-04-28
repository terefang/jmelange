package com.github.terefang.jmelange.scripted.util;

import com.github.terefang.jmelange.commons.CommonUtil;
import com.github.terefang.jmelange.commons.GuidUtil;
import com.github.terefang.jmelange.image.PdfImage;
import com.github.terefang.jmelange.image.PixelImage;
import com.github.terefang.jmelange.image.SvgImage;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.Channel;
import java.util.*;

public class ScriptHelper
{
    CommonUtil commonUtil;
    GuidUtil guidUtil;

    public static ScriptHelper create()
    {
        ScriptHelper _ret = new ScriptHelper();

        return _ret;
    }

    public static int checkInt(Object _str) {
        return CommonUtil.checkInt(_str);
    }

    public static int checkInt(String _str) {
        return CommonUtil.checkInt(_str);
    }

    public static Integer checkInteger(Object _str) {
        return CommonUtil.checkInteger(_str);
    }

    public static Integer checkInteger(String _str) {
        return CommonUtil.checkInteger(_str);
    }

    public static Long checkLong(Object _str) {
        return CommonUtil.checkLong(_str);
    }

    public static Long checkLong(String _str) {
        return CommonUtil.checkLong(_str);
    }

    public static Boolean checkBoolean(Object _str) {
        return CommonUtil.checkBoolean(_str);
    }

    public static Boolean checkBoolean(String _str) {
        return CommonUtil.checkBoolean(_str);
    }

    public static float toFloat(String str) {
        return CommonUtil.toFloat(str);
    }

    public static double toDouble(String str) {
        return CommonUtil.toDouble(str);
    }

    public static int toInt(String str) {
        return CommonUtil.toInt(str);
    }

    public static long toLong(String str) {
        return CommonUtil.toLong(str);
    }

    public static String sha512Hex(String _name) {
        return CommonUtil.sha512Hex(_name);
    }

    public static String sha256Hex(String _name) {
        return CommonUtil.sha256Hex(_name);
    }

    public static String sha1Hex(String _name) {
        return CommonUtil.sha1Hex(_name);
    }

    public static String sha384Hex(String _name) {
        return CommonUtil.sha384Hex(_name);
    }

    public static String toHex(Long _buf) {
        return CommonUtil.toHex(_buf);
    }

    public static String toHex(byte[] _buf) {
        return CommonUtil.toHex(_buf);
    }

    public static String toHex(String _buf) {
        return CommonUtil.toHex(_buf);
    }

    public static String toBase64(byte[] binaryData) {
        return CommonUtil.toBase64(binaryData);
    }

    public static String toBase64(String binaryData) {
        return CommonUtil.toBase64(binaryData);
    }

    public static String toBase26(byte[] binaryData) {
        return CommonUtil.toBase26(binaryData);
    }

    public static String toBase26(String binaryData) {
        return CommonUtil.toBase26(binaryData);
    }

    public static String toBase62(byte[] binaryData) {
        return CommonUtil.toBase62(binaryData);
    }

    public static String toBase62(String binaryData) {
        return CommonUtil.toBase62(binaryData);
    }

    public static String toBase36(byte[] binaryData) {
        return CommonUtil.toBase36(binaryData);
    }

    public static String toBase36(String binaryData) {
        return CommonUtil.toBase36(binaryData);
    }

    public static String toBase64(Long _n) {
        return CommonUtil.toBase64(_n);
    }

    public static String toBase26(Long _n) {
        return CommonUtil.toBase26(_n);
    }

    public static String toBase62(Long _n) {
        return CommonUtil.toBase62(_n);
    }

    public static String toBase36(Long _n) {
        return CommonUtil.toBase36(_n);
    }

    public static String toGuid(String data) {
        return CommonUtil.toGuid(data);
    }

    public static boolean isNumber(String str) {
        return CommonUtil.isNumber(str);
    }

    public static boolean isNumberCreatable(String str) {
        return CommonUtil.isNumberCreatable(str);
    }

    public static Float checkFloat(Object _str) {
        return CommonUtil.checkFloat(_str);
    }

    public static Float checkFloat(String _str) {
        return CommonUtil.checkFloat(_str);
    }

    public static Double checkDouble(Object _str) {
        return CommonUtil.checkDouble(_str);
    }

    public static Double checkDouble(String _str) {
        return CommonUtil.checkDouble(_str);
    }

    public static Integer createInteger(String _str) {
        return CommonUtil.createInteger(_str);
    }

    public static String checkStringDefaultIfNullOrBlank(Object _str, String _def, String _blank) {
        return CommonUtil.checkStringDefaultIfNullOrBlank(_str, _def, _blank);
    }

    public static String checkStringDefaultIfNullOrBlank(String _str, String _def, String _blank) {
        return CommonUtil.checkStringDefaultIfNullOrBlank(_str, _def, _blank);
    }

    public static String checkStringDefaultIfNull(Object _str, String _def) {
        return CommonUtil.checkStringDefaultIfNull(_str, _def);
    }

    public static String checkStringDefaultIfNull(String _str, String _def) {
        return CommonUtil.checkStringDefaultIfNull(_str, _def);
    }

    public static String checkStringDefaultIfBlank(Object _str, String _blank) {
        return CommonUtil.checkStringDefaultIfBlank(_str, _blank);
    }

    public static String checkStringDefaultIfBlank(String _str, String _blank) {
        return CommonUtil.checkStringDefaultIfBlank(_str, _blank);
    }

    public static String checkString(Object _str) {
        return CommonUtil.checkString(_str);
    }

    public static String checkString(String _str) {
        return CommonUtil.checkString(_str);
    }

    public static boolean checkBooleanDefaultIfNull(Object _bool, boolean _b) {
        return CommonUtil.checkBooleanDefaultIfNull(_bool, _b);
    }

    public static boolean checkBooleanDefaultIfNull(String _bool, boolean _b) {
        return CommonUtil.checkBooleanDefaultIfNull(_bool, _b);
    }

    public static int toInteger(String str) {
        return CommonUtil.toInteger(str);
    }

    public static String sha512HMacHex(String _key, String _buffer) {
        return CommonUtil.sha512HMacHex(_key, _buffer);
    }

    public static String sha256HMacHex(String _key, String _buffer) {
        return CommonUtil.sha256HMacHex(_key, _buffer);
    }

    public static String sha1HMacHex(String _key, String _buffer) {
        return CommonUtil.sha1HMacHex(_key, _buffer);
    }

    public static String md5HMacHex(String _key, String _buffer) {
        return CommonUtil.md5HMacHex(_key, _buffer);
    }

    public static String sha384HMacHex(String _key, String _buffer) {
        return CommonUtil.sha384HMacHex(_key, _buffer);
    }

    public static String md5Hex(String _name) {
        return CommonUtil.md5Hex(_name);
    }

    public static <T> T[] toArray(T... _args) {
        return CommonUtil.toArray(_args);
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3, Object _a4, Object _a5, Object _a6) {
        return CommonUtil.toArray(_a1, _a2, _a3, _a4, _a5, _a6);
    }

    public static String[] toArray(String _a1, String _a2, String _a3, String _a4, String _a5, String _a6) {
        return CommonUtil.toArray(_a1, _a2, _a3, _a4, _a5, _a6);
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3, Object _a4, Object _a5) {
        return CommonUtil.toArray(_a1, _a2, _a3, _a4, _a5);
    }

    public static String[] toArray(String _a1, String _a2, String _a3, String _a4, String _a5) {
        return CommonUtil.toArray(_a1, _a2, _a3, _a4, _a5);
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3, Object _a4) {
        return CommonUtil.toArray(_a1, _a2, _a3, _a4);
    }

    public static String[] toArray(String _a1, String _a2, String _a3, String _a4) {
        return CommonUtil.toArray(_a1, _a2, _a3, _a4);
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3) {
        return CommonUtil.toArray(_a1, _a2, _a3);
    }

    public static String[] toArray(String _a1, String _a2, String _a3) {
        return CommonUtil.toArray(_a1, _a2, _a3);
    }

    public static Object[] toArray(Object _a1, Object _a2) {
        return CommonUtil.toArray(_a1, _a2);
    }

    public static String[] toArray(String _a1, String _a2) {
        return CommonUtil.toArray(_a1, _a2);
    }

    public static Object[] toArray(Object _a1) {
        return CommonUtil.toArray(_a1);
    }

    public static String[] toArray(String _a1) {
        return CommonUtil.toArray(_a1);
    }

    public static String toString(Object _o) {
        return CommonUtil.toString(_o);
    }

    public static String join(List<?> list, String separator) {
        return CommonUtil.join(list, separator);
    }

    public static String join(Collection<?> col, String separator) {
        return CommonUtil.join(col, separator);
    }

    public static String join(Set<?> set, String separator) {
        return CommonUtil.join(set, separator);
    }

    public static void close(InputStream inputStream) {
        CommonUtil.close(inputStream);
    }

    public static void close(Channel channel) {
        CommonUtil.close(channel);
    }

    public static void close(OutputStream outputStream) {
        CommonUtil.close(outputStream);
    }

    public static void close(Reader reader) {
        CommonUtil.close(reader);
    }

    public static void close(Writer writer) {
        CommonUtil.close(writer);
    }

    public static int countPrefix(String _text, char _c) {
        return CommonUtil.countPrefix(_text, _c);
    }

    public static int countSuffix(String _text, char _c) {
        return CommonUtil.countSuffix(_text, _c);
    }

    public static Boolean negate(Boolean bool) {
        return CommonUtil.negate(bool);
    }

    public static boolean isTrue(Boolean bool) {
        return CommonUtil.isTrue(bool);
    }

    public static boolean isNotTrue(Boolean bool) {
        return CommonUtil.isNotTrue(bool);
    }

    public static boolean isFalse(Boolean bool) {
        return CommonUtil.isFalse(bool);
    }

    public static boolean isNotFalse(Boolean bool) {
        return CommonUtil.isNotFalse(bool);
    }

    public static boolean toBoolean(Boolean bool) {
        return CommonUtil.toBoolean(bool);
    }

    public static boolean toBooleanDefaultIfNull(Boolean bool, boolean valueIfNull) {
        return CommonUtil.toBooleanDefaultIfNull(bool, valueIfNull);
    }

    public static boolean toBooleanDefaultIfNull(String bool, boolean valueIfNull) {
        return CommonUtil.toBooleanDefaultIfNull(bool, valueIfNull);
    }

    public static boolean toBoolean(int value) {
        return CommonUtil.toBoolean(value);
    }

    public static Boolean toBooleanObject(int value) {
        return CommonUtil.toBooleanObject(value);
    }

    public static Boolean toBooleanObject(Integer value) {
        return CommonUtil.toBooleanObject(value);
    }

    public static boolean toBoolean(int value, int trueValue, int falseValue) {
        return CommonUtil.toBoolean(value, trueValue, falseValue);
    }

    public static boolean toBoolean(Integer value, Integer trueValue, Integer falseValue) {
        return CommonUtil.toBoolean(value, trueValue, falseValue);
    }

    public static Boolean toBooleanObject(int value, int trueValue, int falseValue, int nullValue) {
        return CommonUtil.toBooleanObject(value, trueValue, falseValue, nullValue);
    }

    public static Boolean toBooleanObject(Integer value, Integer trueValue, Integer falseValue, Integer nullValue) {
        return CommonUtil.toBooleanObject(value, trueValue, falseValue, nullValue);
    }

    public static int toInteger(boolean bool) {
        return CommonUtil.toInteger(bool);
    }

    public static int toInteger(boolean bool, int trueValue, int falseValue) {
        return CommonUtil.toInteger(bool, trueValue, falseValue);
    }

    public static int toInteger(Boolean bool, int trueValue, int falseValue, int nullValue) {
        return CommonUtil.toInteger(bool, trueValue, falseValue, nullValue);
    }

    public static Integer toIntegerObject(boolean bool, Integer trueValue, Integer falseValue) {
        return CommonUtil.toIntegerObject(bool, trueValue, falseValue);
    }

    public static Integer toIntegerObject(Boolean bool, Integer trueValue, Integer falseValue, Integer nullValue) {
        return CommonUtil.toIntegerObject(bool, trueValue, falseValue, nullValue);
    }

    public static Boolean toBooleanObject(String str) {
        return CommonUtil.toBooleanObject(str);
    }

    public static Boolean toBooleanObject(String str, String trueString, String falseString, String nullString) {
        return CommonUtil.toBooleanObject(str, trueString, falseString, nullString);
    }

    public static boolean toBoolean(String str) {
        return CommonUtil.toBoolean(str);
    }

    public static boolean toBoolean(String str, String trueString, String falseString) {
        return CommonUtil.toBoolean(str, trueString, falseString);
    }

    public static boolean toBoolean(String str, String trueString) {
        return CommonUtil.toBoolean(str, trueString);
    }

    public static String toStringTrueFalse(Boolean bool) {
        return CommonUtil.toStringTrueFalse(bool);
    }

    public static String toStringOnOff(Boolean bool) {
        return CommonUtil.toStringOnOff(bool);
    }

    public static String toStringYesNo(Boolean bool) {
        return CommonUtil.toStringYesNo(bool);
    }

    public static String toString(Boolean bool, String trueString, String falseString, String nullString) {
        return CommonUtil.toString(bool, trueString, falseString, nullString);
    }

    public static String toStringTrueFalse(boolean bool) {
        return CommonUtil.toStringTrueFalse(bool);
    }

    public static String toStringOnOff(boolean bool) {
        return CommonUtil.toStringOnOff(bool);
    }

    public static String toStringYesNo(boolean bool) {
        return CommonUtil.toStringYesNo(bool);
    }

    public static String toString(boolean bool, String trueString, String falseString) {
        return CommonUtil.toString(bool, trueString, falseString);
    }

    public static String toUUID(String data) {
        return GuidUtil.toUUID(data);
    }

    public static String randomUUID() {
        return GuidUtil.randomUUID();
    }

    public static String toTUID(String _name) {
        return GuidUtil.toTUID(_name);
    }

    public static String toTUID(String _name, String _key) {
        return GuidUtil.toTUID(_name, _key);
    }

    public static String randomGUID() {
        return GuidUtil.randomGUID();
    }

    public static String toHashGUID(String _name) {
        return GuidUtil.toHashGUID(_name);
    }

    public static String toHashGUID(String _name, String _key) {
        return GuidUtil.toHashGUID(_name, _key);
    }

    public static String toGUID(String _name) {
        return GuidUtil.toGUID(_name);
    }

    public static String toGUID(String _name1, String _name2) {
        return GuidUtil.toGUID(_name1, _name2);
    }

    public static String toGUID(String _name1, String _name2, String _name3) {
        return GuidUtil.toGUID(_name1, _name2, _name3);
    }

    public static String toGUID(String _name1, long _num) {
        return GuidUtil.toGUID(_name1, _num);
    }

    public static String toGUID(String _name1, String _name2, long _num) {
        return GuidUtil.toGUID(_name1, _name2, _num);
    }

    public static String toGUID(String _name1, String _name2, String _name3, long _num) {
        return GuidUtil.toGUID(_name1, _name2, _name3, _num);
    }

    public static String toXUID(String _name) {
        return GuidUtil.toXUID(_name);
    }

    public static String toXUID(String _name1, String _name2) {
        return GuidUtil.toXUID(_name1, _name2);
    }

    public static String toXUID(String _name1, String _name2, String _name3) {
        return GuidUtil.toXUID(_name1, _name2, _name3);
    }

    public static String toXUID(String _name1, long _num) {
        return GuidUtil.toXUID(_name1, _num);
    }

    public static String toXUID(String _name1, String _name2, long _num) {
        return GuidUtil.toXUID(_name1, _name2, _num);
    }

    public static String toXUID(String _name1, String _name2, String _name3, long _num) {
        return GuidUtil.toXUID(_name1, _name2, _name3, _num);
    }

    public static long ipToLong(String _addr) {
        return CommonUtil.ipToLong(_addr);
    }

    public static String longToIp(long _addr) {
        return CommonUtil.longToIp(_addr);
    }

    public static String ipToHex(String _addr) {
        return CommonUtil.ipToHex(_addr);
    }

    public static String ipToHex(long _addr) {
        return CommonUtil.ipToHex(_addr);
    }

    public static String hexToIp(String _hex) {
        return CommonUtil.hexToIp(_hex);
    }

    @SneakyThrows
    public static Date dateToTime(String _format, String _text) {
        return CommonUtil.dateToTime(_format, _text);
    }

    @SneakyThrows
    public static Long dateToLong(String _format, String _text) {
        return CommonUtil.dateToLong(_format, _text);
    }

    public static Date getDate() {
        return CommonUtil.getDate();
    }

    public static Long getDateLong() {
        return CommonUtil.getDateLong();
    }

    public static String timeToDate(String _format, Date _time) {
        return CommonUtil.timeToDate(_format, _time);
    }

    public static String timeToDate(String _format, long _time) {
        return CommonUtil.timeToDate(_format, _time);
    }

    public static String toBase(int _base, long _n) {
        return CommonUtil.toBase(_base, _n);
    }

    public static String toBase(int _base, long _n, int _min) {
        return CommonUtil.toBase(_base, _n, _min);
    }

    public static String decimalToAscii(String _text) {
        return CommonUtil.decimalToAscii(_text);
    }

    public static String decimalToHex(String _text) {
        return CommonUtil.decimalToHex(_text);
    }

    public static String decimalToHex(String _text, String _sep) {
        return CommonUtil.decimalToHex(_text, _sep);
    }

    public static String decimalToHex(String _text, String _sep, String _delim) {
        return CommonUtil.decimalToHex(_text, _sep, _delim);
    }

    public static String extract(String s, String rx) {
        return CommonUtil.extract(s, rx);
    }

    public static String[] extractN(String s, String rx) {
        return CommonUtil.extractN(s, rx);
    }

    public static String formatMsg(String _fmt, Object... _params) {
        return CommonUtil.formatMsg(_fmt, _params);
    }

    public static String format(String _fmt, Object... _params) {
        return CommonUtil.format(_fmt, _params);
    }

    public static boolean fnmatch(String a, String fx) {
        return CommonUtil.fnmatch(a, fx);
    }

    public static boolean wcmatch(String expr, String value) {
        return CommonUtil.wcmatch(expr, value);
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3, String _k4, Object _v4, String _k5, Object _v5, String _k6, Object _v6) {
        return CommonUtil.toMap(_k1, _v1, _k2, _v2, _k3, _v3, _k4, _v4, _k5, _v5, _k6, _v6);
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3, String _k4, Object _v4, String _k5, Object _v5) {
        return CommonUtil.toMap(_k1, _v1, _k2, _v2, _k3, _v3, _k4, _v4, _k5, _v5);
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3, String _k4, Object _v4) {
        return CommonUtil.toMap(_k1, _v1, _k2, _v2, _k3, _v3, _k4, _v4);
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2, String _k3, Object _v3) {
        return CommonUtil.toMap(_k1, _v1, _k2, _v2, _k3, _v3);
    }

    public static Map<String, Object> toMap(String _k1, Object _v1, String _k2, Object _v2) {
        return CommonUtil.toMap(_k1, _v1, _k2, _v2);
    }

    public static Map<String, Object> toMap(String _k, Object _v) { return CommonUtil.toMap(_k, _v); }

    public static Map<String, Object> toMap(String... _args) {
        return CommonUtil.toMap(_args);
    }

    public static Map<String, Object> toMap(Object... _args) {
        return CommonUtil.toMap(_args);
    }

    public static List<String> toList(String _a1, String _a2, String _a3, String _a4, String _a5, String _a6) {
        return CommonUtil.toList(_a1, _a2, _a3, _a4, _a5, _a6);
    }

    public static List<String> toList(String _a1, String _a2, String _a3, String _a4, String _a5) {
        return CommonUtil.toList(_a1, _a2, _a3, _a4, _a5);
    }

    public static List<String> toList(String _a1, String _a2, String _a3, String _a4) {
        return CommonUtil.toList(_a1, _a2, _a3, _a4);
    }

    public static List<String> toList(String _a1, String _a2, String _a3) {
        return CommonUtil.toList(_a1, _a2, _a3);
    }

    public static List<String> toList(String _a1, String _a2) {
        return CommonUtil.toList(_a1, _a2);
    }

    public static List<Object> toList(Object _a1, Object _a2, Object _a3, Object _a4, Object _a5, Object _a6) {
        return CommonUtil.toList(_a1, _a2, _a3, _a4, _a5, _a6);
    }

    public static List<Object> toList(Object _a1, Object _a2, Object _a3, Object _a4, Object _a5) {
        return CommonUtil.toList(_a1, _a2, _a3, _a4, _a5);
    }

    public static List<Object> toList(Object _a1, Object _a2, Object _a3, Object _a4) {
        return CommonUtil.toList(_a1, _a2, _a3, _a4);
    }

    public static List<Object> toList(Object _a1, Object _a2, Object _a3) {
        return CommonUtil.toList(_a1, _a2, _a3);
    }

    public static List<Object> toList(Object _a1, Object _a2) {
        return CommonUtil.toList(_a1, _a2);
    }

    public static List<String> toList(String... _args) {
        return CommonUtil.toList(_args);
    }

    public static List<Object> toList(Object... _args) {
        return CommonUtil.toList(_args);
    }

    public static List newList() {
        return CommonUtil.newList();
    }

    public static Map newMap() {
        return CommonUtil.newMap();
    }

    public static Map newLMap() {
        return CommonUtil.newLMap();
    }

    public static String stringClean(String str) {
        return CommonUtil.stringClean(str);
    }

    public static String trim(String str) {
        return CommonUtil.trim(str);
    }

    public static String deleteWhitespace(String str) {
        return CommonUtil.deleteWhitespace(str);
    }

    public static boolean isNotEmpty(String str) {
        return CommonUtil.isNotEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return CommonUtil.isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return CommonUtil.isBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return CommonUtil.isNotBlank(str);
    }

    public static boolean stringEquals(String str1, String str2) {
        return CommonUtil.stringEquals(str1, str2);
    }

    public static boolean stringEqualsIgnoreCase(String str1, String str2) {
        return CommonUtil.stringEqualsIgnoreCase(str1, str2);
    }

    public static int indexOfAny(String str, String[] searchStrs) {
        return CommonUtil.indexOfAny(str, searchStrs);
    }

    public static int lastIndexOfAny(String str, String[] searchStrs) {
        return CommonUtil.lastIndexOfAny(str, searchStrs);
    }

    public static String substring(String str, int start) {
        return CommonUtil.substring(str, start);
    }

    public static String substring(String str, int start, int end) {
        return CommonUtil.substring(str, start, end);
    }

    public static String left(String str, int len) {
        return CommonUtil.left(str, len);
    }

    public static String right(String str, int len) {
        return CommonUtil.right(str, len);
    }

    public static String mid(String str, int pos, int len) {
        return CommonUtil.mid(str, pos, len);
    }

    public static String[] split(String str) {
        return CommonUtil.split(str);
    }

    public static String[] split(String text, String separator) {
        return CommonUtil.split(text, separator);
    }

    public static String[] split(String str, String separator, int max) {
        return CommonUtil.split(str, separator, max);
    }

    public static String concat(Object[] array) {
        return CommonUtil.concat(array);
    }

    public static String concatenate(Object[] array) {
        return CommonUtil.concatenate(array);
    }

    public static String join(Object[] array, String separator) {
        return CommonUtil.join(array, separator);
    }

    public static String join(Iterator<?> iterator, String separator) {
        return CommonUtil.join(iterator, separator);
    }

    public static String replaceOnce(String text, char repl, char with) {
        return CommonUtil.replaceOnce(text, repl, with);
    }

    public static String replace(String text, char repl, char with) {
        return CommonUtil.replace(text, repl, with);
    }

    public static String replace(String text, char repl, char with, int max) {
        return CommonUtil.replace(text, repl, with, max);
    }

    public static String replaceOnce(String text, String repl, String with) {
        return CommonUtil.replaceOnce(text, repl, with);
    }

    public static String replace(String text, String repl, String with) {
        return CommonUtil.replace(text, repl, with);
    }

    public static String replace(String text, String repl, String with, int max) {
        return CommonUtil.replace(text, repl, with, max);
    }

    public static String overlayString(String text, String overlay, int start, int end) {
        return CommonUtil.overlayString(text, overlay, start, end);
    }

    public static String center(String str, int size) {
        return CommonUtil.center(str, size);
    }

    public static String center(String str, int size, String delim) {
        return CommonUtil.center(str, size, delim);
    }

    public static String chomp(String str) {
        return CommonUtil.chomp(str);
    }

    public static String chomp(String str, String sep) {
        return CommonUtil.chomp(str, sep);
    }

    public static String chompLast(String str) {
        return CommonUtil.chompLast(str);
    }

    public static String chompLast(String str, String sep) {
        return CommonUtil.chompLast(str, sep);
    }

    public static String getChomp(String str, String sep) {
        return CommonUtil.getChomp(str, sep);
    }

    public static String prechomp(String str, String sep) {
        return CommonUtil.prechomp(str, sep);
    }

    public static String getPrechomp(String str, String sep) {
        return CommonUtil.getPrechomp(str, sep);
    }

    public static String chop(String str) {
        return CommonUtil.chop(str);
    }

    public static String chopNewline(String str) {
        return CommonUtil.chopNewline(str);
    }

    public static String escape(String str) {
        return CommonUtil.escape(str);
    }

    public static String repeat(String str, int repeat) {
        return CommonUtil.repeat(str, repeat);
    }

    public static String rightPad(String str, int size) {
        return CommonUtil.rightPad(str, size);
    }

    public static String rightPad(String str, int size, String delim) {
        return CommonUtil.rightPad(str, size, delim);
    }

    public static String leftPad(String str, int size) {
        return CommonUtil.leftPad(str, size);
    }

    public static String leftPad(String str, int size, String delim) {
        return CommonUtil.leftPad(str, size, delim);
    }

    public static String strip(String str) {
        return CommonUtil.strip(str);
    }

    public static String strip(String str, String delim) {
        return CommonUtil.strip(str, delim);
    }

    public static String[] stripAll(String[] strs) {
        return CommonUtil.stripAll(strs);
    }

    public static String[] stripAll(String[] strs, String delimiter) {
        return CommonUtil.stripAll(strs, delimiter);
    }

    public static String stripEnd(String str, String strip) {
        return CommonUtil.stripEnd(str, strip);
    }

    public static String stripStart(String str, String strip) {
        return CommonUtil.stripStart(str, strip);
    }

    public static String upperCase(String str) {
        return CommonUtil.upperCase(str);
    }

    public static String lowerCase(String str) {
        return CommonUtil.lowerCase(str);
    }

    public static String uncapitalise(String str) {
        return CommonUtil.uncapitalise(str);
    }

    public static String capitalise(String str) {
        return CommonUtil.capitalise(str);
    }

    public static String swapCase(String str) {
        return CommonUtil.swapCase(str);
    }

    public static String capitaliseAllWords(String str) {
        return CommonUtil.capitaliseAllWords(str);
    }

    public static String uncapitaliseAllWords(String str) {
        return CommonUtil.uncapitaliseAllWords(str);
    }

    public static String getNestedString(String str, String tag) {
        return CommonUtil.getNestedString(str, tag);
    }

    public static String getNestedString(String str, String open, String close) {
        return CommonUtil.getNestedString(str, open, close);
    }

    public static int countMatches(String str, String sub) {
        return CommonUtil.countMatches(str, sub);
    }

    public static boolean isAlpha(String str) {
        return CommonUtil.isAlpha(str);
    }

    public static boolean isWhitespace(String str) {
        return CommonUtil.isWhitespace(str);
    }

    public static boolean isAlphaSpace(String str) {
        return CommonUtil.isAlphaSpace(str);
    }

    public static boolean isAlphanumeric(String str) {
        return CommonUtil.isAlphanumeric(str);
    }

    public static boolean isAlphanumericSpace(String str) {
        return CommonUtil.isAlphanumericSpace(str);
    }

    public static boolean isNumeric(String str) {
        return CommonUtil.isNumeric(str);
    }

    public static boolean isNumericSpace(String str) {
        return CommonUtil.isNumericSpace(str);
    }

    public static String defaultString(Object obj) {
        return CommonUtil.defaultString(obj);
    }

    public static String defaultString(Object obj, String defaultString) {
        return CommonUtil.defaultString(obj, defaultString);
    }

    public static String reverse(String str) {
        return CommonUtil.reverse(str);
    }

    public static String reverseDelimitedString(String str, String delimiter) {
        return CommonUtil.reverseDelimitedString(str, delimiter);
    }

    public static String abbreviate(String s, int maxWidth) {
        return CommonUtil.abbreviate(s, maxWidth);
    }

    public static String abbreviate(String s, int offset, int maxWidth) {
        return CommonUtil.abbreviate(s, offset, maxWidth);
    }

    public static String difference(String s1, String s2) {
        return CommonUtil.difference(s1, s2);
    }

    public static int differenceAt(String s1, String s2) {
        return CommonUtil.differenceAt(s1, s2);
    }

    public static String interpolate(String text, Map<?, ?> namespace) {
        return CommonUtil.interpolate(text, namespace);
    }

    public static String removeAndHump(String data, String replaceThis) {
        return CommonUtil.removeAndHump(data, replaceThis);
    }

    public static String capitalizeFirstLetter(String data) {
        return CommonUtil.capitalizeFirstLetter(data);
    }

    public static String lowercaseFirstLetter(String data) {
        return CommonUtil.lowercaseFirstLetter(data);
    }

    public static String addAndDeHump(String view) {
        return CommonUtil.addAndDeHump(view);
    }

    public static String quoteAndEscape(String source, char quoteChar) {
        return CommonUtil.quoteAndEscape(source, quoteChar);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] quotingTriggers) {
        return CommonUtil.quoteAndEscape(source, quoteChar, quotingTriggers);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] escapedChars, char escapeChar, boolean force) {
        return CommonUtil.quoteAndEscape(source, quoteChar, escapedChars, escapeChar, force);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] escapedChars, char[] quotingTriggers, char escapeChar, boolean force) {
        return CommonUtil.quoteAndEscape(source, quoteChar, escapedChars, quotingTriggers, escapeChar, force);
    }

    public static String quoteAndEscape(String source, char quoteChar, char[] escapedChars, char[] quotingTriggers, String escapePattern, boolean force) {
        return CommonUtil.quoteAndEscape(source, quoteChar, escapedChars, quotingTriggers, escapePattern, force);
    }

    public static String escape(String source, char[] escapedChars, char escapeChar) {
        return CommonUtil.escape(source, escapedChars, escapeChar);
    }

    public static String escape(String source, char[] escapedChars, String escapePattern) {
        return CommonUtil.escape(source, escapedChars, escapePattern);
    }

    public static String removeDuplicateWhitespace(String s) {
        return CommonUtil.removeDuplicateWhitespace(s);
    }

    public static String unifyLineSeparators(String s) {
        return CommonUtil.unifyLineSeparators(s);
    }

    public static String unifyLineSeparators(String s, String ls) {
        return CommonUtil.unifyLineSeparators(s, ls);
    }

    public static boolean contains(String str, char searchChar) {
        return CommonUtil.contains(str, searchChar);
    }

    public static boolean contains(String str, String searchStr) {
        return CommonUtil.contains(str, searchStr);
    }

    public static PixelImage png(int _w, int _h)
    {
        return PixelImage.create(_w,_h);
    }

    public static SvgImage svg(int _w, int _h)
    {
        return SvgImage.create(_w,_h);
    }

    public static PdfImage pdf(int _w, int _h)
    {
        return PdfImage.create(_w,_h);
    }
}
