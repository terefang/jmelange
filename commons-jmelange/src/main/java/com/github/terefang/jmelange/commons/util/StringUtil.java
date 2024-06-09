package com.github.terefang.jmelange.commons.util;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.apache.commons.text.WordUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil extends org.codehaus.plexus.util.StringUtils
{
    // probe utils

    public static String normalize(String input) {
        return input == null ? null : Normalizer.normalize(input, Normalizer.Form.NFKD);
    }

    public static String asciifyAccents(String input) {
        return normalize(input).replaceAll("\\p{M}", "");
    }


    public static String regReplace(String _text, String _expr, String _repl)
    {
        return StringUtil.replace(_text, _expr, _repl);
    }

    public static String regReplace(String _text, String _expr, String _repl, int _count)
    {
        return StringUtil.replace(_text, _expr, _repl, _count);
    }

    public static String extract(String s, String rx)
    {
        Pattern p = Pattern.compile(rx);
        Matcher m = p.matcher(s);
        if(m.find())
        {
            return m.group(1);
        }
        return "";
    }

    public static String[] extractN(String s, String rx)
    {
        Pattern p = Pattern.compile(rx);
        Matcher m = p.matcher(s);
        if(m.find())
        {
            String[] sa = new String[m.groupCount()];
            for(int i=0; i< sa.length; i++)
            {
                sa[i]=m.group(i+1);
            }
            return sa;
        }
        return new String[]{};
    }

    public static String formatMsg(String _fmt, Object... _params)
    {
        return MessageFormat.format(_fmt, _params);
    }

    public static String format(String _fmt, Object... _params)
    {
        return String.format(_fmt, _params);
    }

    public static String mformat(String _pattern, Object... _objs)
    {
        return MessageFormat.format(_pattern, _objs);
    }

    public static String sformat(String _pattern, Object... _objs)
    {
        return     String.format(_pattern, _objs);
    }

    public static boolean rmatch(String s, String rx)
    {
        Pattern p = Pattern.compile(rx);
        Matcher m = p.matcher(s);
        if(m.find())
        {
            return true;
        }
        return false;
    }

    public static boolean irmatch(String s, String rx)
    {
        Pattern p = Pattern.compile(rx, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        if(m.find())
        {
            return true;
        }
        return false;
    }

    public static boolean imatch(String s, String fx)
    {
        return fnmatch(s.toLowerCase(),fx.toLowerCase());
    }

    public static boolean fnmatch(String a, String fx)
    {
        return wcmatch(fx, a);
    }

    public static boolean wcmatch(String expr, String value)
    {
        if(value==null)
            return false;

        ArrayList<String> p = new ArrayList();

        switch(wildcard_substring(expr.toLowerCase(),p))
        {
            case -1: // presense
                return (value.length() > 0);

            case 1: // wcmatch
                return wildcard_check((String[])p.toArray(new String[p.size()]), value.toLowerCase());

            case 0: // simple
            default:
                return expr.equalsIgnoreCase(value);
        }
    }

    private static boolean wildcard_check(String[] pieces, String s)
    {
        // Walk the pieces to match the string
        // There are implicit stars between each piece,
        // and the first and last pieces might be "" to anchor the match.
        // assert (pieces.length > 1)
        // minimal case is <string>*<string>

        boolean result = true;
        int len = pieces.length;

        int index = 0;
        for (int i = 0; i < len; i++)
        {
            String piece = (String) pieces[i];

            if (i == len - 1)
            {
                // this is the last piece
                if (!wildcard_endsWith(s,piece))
                {
                    result = false;
                }
                break;
            }
            // initial non-star; assert index == 0
            else if (i == 0)
            {
                if (!wildcard_startsWith(s,piece))
                {
                    result = false;
                    break;
                }
            }
            // assert i > 0 && i < len-1
            else
            {
                // Sure wish stringbuffer supported e.g. indexOf
                index = wildcard_indexOf(s,piece, index);
                if (index < 0)
                {
                    result = false;
                    break;
                }
            }
            // start beyond the matching piece
            index += piece.length();
        }

        return result;
    }

    private static int wildcard_indexOf(String _hay, String _piece, int _index)
    {
        if(_piece.length()==0) return _index;
        if(_piece.contains("?"))
        {
            int _hay_len = _hay.length();
            int _piece_len = _piece.length();

            String[] _pieces = StringUtil.split(_piece, '?');
            for(int _i = _index; _i<_hay_len-_piece_len; _i++)
            {
                int _ofs = _hay.indexOf(_pieces[0], _index);

                if(_ofs == -1) return -1;

                int _rofs = _ofs+_pieces[0].length()+1;
                boolean _found = true;
                for(int _j = 1; _j<_pieces.length; _j++)
                {
                    int _tofs = _hay.indexOf(_pieces[_j], _rofs);
                    if(_tofs == _rofs)
                    {
                        _rofs = _rofs+_pieces[_j].length()+1;
                        continue;
                    }
                    _found = false;
                }

                if(_found) return _ofs;
            }
            return -1;
        }
        else
        {
            return _hay.indexOf(_piece, _index);
        }
    }

    private static boolean wildcard_endsWith(String _hay, String _piece)
    {
        if(_piece.length()==0) return true;
        if(_piece.contains("?"))
        {
            int _hay_len = _hay.length();
            int _piece_len = _piece.length();
            for(int _i = 0; _i<_piece_len; _i++)
            {
                char _a = _hay.charAt(_hay_len-_piece_len+_i);
                char _b = _piece.charAt(_i);
                if(_b!='?' && (_a!=_b)) return false;
            }
            return true;
        }
        else
        {
            return _hay.endsWith(_piece);
        }
    }

    private static boolean wildcard_startsWith(String _hay, String _piece)
    {
        if(_piece.length()==0) return true;
        if(_piece.contains("?"))
        {
            int _hay_len = _hay.length();
            int _piece_len = _piece.length();
            if(_hay_len<_piece_len) return false;
            for(int _i = 0; _i<_piece.length(); _i++)
            {
                char _a = _hay.charAt(_i);
                char _b = _piece.charAt(_i);
                if(_b!='?' && (_a!=_b)) return false;
            }
            return true;
        }
        else
        {
            return _hay.startsWith(_piece);
        }
    }

    private static int wildcard_substring(String wcstring, ArrayList pieces)
    {
        pieces.clear();
        StringBuffer ss = new StringBuffer();
        boolean wasStar = false; // indicates last piece was a star
        boolean leftstar = false; // track if the initial piece is a star
        boolean rightstar = false; // track if the final piece is a star

        char[] wcs = wcstring.toCharArray();
        int wco=0;
        // We assume (sub)strings can contain leading and trailing blanks
        for (;wco<wcs.length;)
        {
            int c = wcs[wco];
            switch (c)
            {
                case '\\' :
                    wasStar = false;
                    wco++;
                    if(wco<wcs.length)
                    {
                        c = wcs[wco]; wco++;
                        switch(c)
                        {
                            case 'n':
                                ss.append((char) 10);
                                break;
                            case 'r':
                                ss.append((char) 13);
                                break;
                            case 't':
                                ss.append((char) 9);
                                break;
                            default:
                                ss.append((char) c);
                                break;
                        }
                    }
                    break;
                case '%' :
                case '*' :
                    if (wasStar)
                    {
                        // encountered two successive stars;
                        // I assume this is illegal but permissive
                        wco++;
                        break;
                    }
                    wco++;
                    if (ss.length() > 0)
                    {
                        pieces.add(ss.toString()); // accumulate the pieces
                        // between '*' occurrences
                    }
                    ss.setLength(0);
                    // if this is a leading star, then track it
                    if (pieces.size() == 0)
                    {
                        leftstar = true;
                    }
                    ss.setLength(0);
                    wasStar = true;
                    break;
                default :
                    wasStar = false;
                    c = wcs[wco]; wco++;
                    ss.append((char) c);
            }
        }

        if (ss.length() > 0)
        {
            pieces.add(ss.toString()); // accumulate the pieces
            ss.setLength(0);
        }

        if (pieces.size() == 0)
        {
            return -1; // presense
        }
        if (leftstar || rightstar || pieces.size() > 1)
        {
            // insert leading and/or trailing "" to anchor ends
            if (rightstar)
            {
                pieces.add("");
            }
            if (leftstar)
            {
                pieces.add(0, "");
            }
            return 1; // wcmatch
        }
        // assert !leftstar && !rightstar && pieces.size == 1
        return 0; // simple
    }

    //*************************************************************************

    public static int countPrefix(String _text, char _c)
    {
        int _l = _text.length();
        int _i = 0;
        while(_text.charAt(_i)==_c && _i<_l) _i++;
        return _i;
    }

    public static int countSuffix(String _text, char _c)
    {
        int _l = _text.length()-1;
        int _i = _l;
        while(_text.charAt(_i)==_c && _i>0) _i--;
        return _l-_i;
    }

    public static String checkStringDefaultIfNullOrBlank(Object _str, String _def, String _blank)
    {
        if(_str==null)
        {
            return _def;
        }

        return checkStringDefaultIfNullOrBlank(_str.toString(), _def, _blank);
    }

    public static String checkStringDefaultIfNullOrBlank(String _str, String _def, String _blank)
    {
        if(_str==null)
        {
            return _def;
        }

        if(_str.trim().length()==0) return _blank;

        return _str;
    }

    public static String checkStringDefaultIfNull(Object _str, String _def)
    {
        return checkStringDefaultIfNullOrBlank(_str, _def, "(blank)");
    }

    public static String checkStringDefaultIfNull(String _str, String _def)
    {
        return checkStringDefaultIfNullOrBlank(_str, _def, "(blank)");
    }

    public static String checkStringDefaultIfBlank(Object _str, String _blank)
    {
        return checkStringDefaultIfNullOrBlank(_str,"(nil)", _blank);
    }

    public static String checkStringDefaultIfBlank(String _str, String _blank)
    {
        return checkStringDefaultIfNullOrBlank(_str,"(nil)", _blank);
    }

    public static String checkString(Object _str)
    {
        return checkStringDefaultIfNullOrBlank(_str, "(nil)", "(blank)");
    }

    public static String checkString(String _str)
    {
        return checkStringDefaultIfNullOrBlank(_str, "(nil)", "(blank)");
    }


    //*************************************************************************

    // plexus/lang3 StringUtils -- START

    public static String toString(Object _o)
    {
        return Objects.toString(_o);
    }

    public static String trimToEmpty(String str) {
        return StringUtils.trimToEmpty(str);
    }

    public static String trimToNull(String str) {
        return StringUtils.trimToNull(str);
    }

    public static String truncate(String str, int maxWidth) {
        return StringUtils.truncate(str, maxWidth);
    }

    public static String truncate(String str, int offset, int maxWidth) {
        return StringUtils.truncate(str, offset, maxWidth);
    }

    public static String uncapitalize(String str) {
        return StringUtils.uncapitalize(str);
    }

    public static String uncapitalize(String str, char... delimiters) {
        return WordUtils.uncapitalize(str, delimiters);
    }

    public static String unwrap(String str, char wrapChar) {
        return StringUtils.unwrap(str, wrapChar);
    }

    public static String unwrap(String str, String wrapToken) {
        return StringUtils.unwrap(str, wrapToken);
    }

    public static String substringAfter(String str, int separator) {
        return StringUtils.substringAfter(str, separator);
    }

    public static String substringAfter(String str, String separator) {
        return StringUtils.substringAfter(str, separator);
    }

    public static String substringAfterLast(String str, int separator) {
        return StringUtils.substringAfterLast(str, separator);
    }

    public static String substringAfterLast(String str, String separator) {
        return StringUtils.substringAfterLast(str, separator);
    }

    public static String substringBefore(String str, String separator) {
        return StringUtils.substringBefore(str, separator);
    }

    public static String substringBeforeLast(String str, String separator) {
        return StringUtils.substringBeforeLast(str, separator);
    }

    public static String substringBetween(String str, String tag) {
        return StringUtils.substringBetween(str, tag);
    }

    public static String substringBetween(String str, String open, String close) {
        return StringUtils.substringBetween(str, open, close);
    }

    public static String[] substringsBetween(String str, String open, String close) {
        return StringUtils.substringsBetween(str, open, close);
    }

    public static String normalizeSpace(String str) {
        return StringUtils.normalizeSpace(str);
    }

    public static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        return StringUtils.ordinalIndexOf(str, searchStr, ordinal);
    }

    public static String overlay(String str, String overlay, int start, int end) {
        return StringUtils.overlay(str, overlay, start, end);
    }

    public static String prependIfMissing(String str, CharSequence prefix, CharSequence... prefixes) {
        return StringUtils.prependIfMissing(str, prefix, prefixes);
    }

    public static String prependIfMissingIgnoreCase(String str, CharSequence prefix, CharSequence... prefixes) {
        return StringUtils.prependIfMissingIgnoreCase(str, prefix, prefixes);
    }

    public static String remove(String str, char remove) {
        return StringUtils.remove(str, remove);
    }

    public static String remove(String str, String remove) {
        return StringUtils.remove(str, remove);
    }

    @Deprecated
    public static String removeAll(String text, String regex) {
        return StringUtils.removeAll(text, regex);
    }

    public static String removeEnd(String str, String remove) {
        return StringUtils.removeEnd(str, remove);
    }

    public static String removeEndIgnoreCase(String str, String remove) {
        return StringUtils.removeEndIgnoreCase(str, remove);
    }

    @Deprecated
    public static String removeFirst(String text, String regex) {
        return StringUtils.removeFirst(text, regex);
    }

    public static String removeIgnoreCase(String str, String remove) {
        return StringUtils.removeIgnoreCase(str, remove);
    }

    @Deprecated
    public static String removePattern(String source, String regex) {
        return StringUtils.removePattern(source, regex);
    }

    public static String removeStart(String str, String remove) {
        return StringUtils.removeStart(str, remove);
    }

    public static String removeStartIgnoreCase(String str, String remove) {
        return StringUtils.removeStartIgnoreCase(str, remove);
    }

    public static String repeat(char ch, int repeat) {
        return StringUtils.repeat(ch, repeat);
    }

    public static String repeat(String str, int repeat) {
        return StringUtils.repeat(str, repeat);
    }

    public static String repeat(String str, String separator, int repeat) {
        return StringUtils.repeat(str, separator, repeat);
    }

    public static String[] splitByCharacterType(String str) {
        return StringUtils.splitByCharacterType(str);
    }

    public static String[] splitByCharacterTypeCamelCase(String str) {
        return StringUtils.splitByCharacterTypeCamelCase(str);
    }

    public static String[] splitByWholeSeparator(String str, String separator) {
        return StringUtils.splitByWholeSeparator(str, separator);
    }

    public static String[] splitByWholeSeparator(String str, String separator, int max) {
        return StringUtils.splitByWholeSeparator(str, separator, max);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator) {
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(str, separator);
    }

    public static String[] splitByWholeSeparatorPreserveAllTokens(String str, String separator, int max) {
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(str, separator, max);
    }

    public static String[] splitPreserveAllTokens(String str) {
        return StringUtils.splitPreserveAllTokens(str);
    }

    public static String[] splitPreserveAllTokens(String str, char separatorChar) {
        return StringUtils.splitPreserveAllTokens(str, separatorChar);
    }

    public static String[] splitPreserveAllTokens(String str, String separatorChars) {
        return StringUtils.splitPreserveAllTokens(str, separatorChars);
    }

    public static String[] splitPreserveAllTokens(String str, String separatorChars, int max) {
        return StringUtils.splitPreserveAllTokens(str, separatorChars, max);
    }

    public static boolean startsWith(CharSequence str, CharSequence prefix) {
        return StringUtils.startsWith(str, prefix);
    }

    public static boolean startsWithAny(CharSequence sequence, CharSequence... searchStrings) {
        return StringUtils.startsWithAny(sequence, searchStrings);
    }

    public static boolean startsWithIgnoreCase(CharSequence str, CharSequence prefix) {
        return StringUtils.startsWithIgnoreCase(str, prefix);
    }


    public static String concat(Object... array) {
        return concatenate(array);
    }

    public static String concatenate(Object... array)
    {
        StringBuilder _sb = new StringBuilder();
        for(Object _o : array)
        {
            _sb.append(toString(_o));
        }
        return _sb.toString();
    }

    public static String concat(String... array)
    {
        return concatenate((Object[]) array);
    }

    public static String concatenate(String... array)
    {
        return concatenate((Object[]) array);
    }

    public static String concat(String a1, String a2, String a3, String a4, String a5, String a6)
    {
        return concatenate((Object[]) toArray(a1,a2,a3,a4,a5,a6));
    }

    public static String concatenate(String a1, String a2, String a3, String a4, String a5, String a6)
    {
        return concatenate((Object[]) toArray(a1,a2,a3,a4,a5,a6));
    }

    public static String concat(String a1, String a2, String a3, String a4, String a5)
    {
        return concatenate((Object[]) toArray(a1,a2,a3,a4,a5));
    }

    public static String concatenate(String a1, String a2, String a3, String a4, String a5)
    {
        return concatenate((Object[]) toArray(a1,a2,a3,a4,a5));
    }

    public static String concat(String a1, String a2, String a3, String a4)
    {
        return concatenate((Object[]) toArray(a1,a2,a3,a4));
    }

    public static String concatenate(String a1, String a2, String a3, String a4)
    {
        return concatenate((Object[]) toArray(a1,a2,a3,a4));
    }

    public static String concat(String a1, String a2, String a3)
    {
        return concatenate((Object[]) toArray(a1,a2,a3));
    }

    public static String concatenate(String a1, String a2, String a3)
    {
        return concatenate((Object[]) toArray(a1,a2,a3));
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        return StringUtils.join(array, separator, startIndex, endIndex);
    }

    public static String join(short[] array, char separator) {
        return StringUtils.join(array, separator);
    }

    public static String join(short[] array, char separator, int startIndex, int endIndex) {
        return StringUtils.join(array, separator, startIndex, endIndex);
    }

    @SafeVarargs
    public static <T> String join(T... elements) {
        return StringUtils.join(elements);
    }

    public static String joinWith(String separator, Object... objects) {
        return StringUtils.joinWith(separator, objects);
    }

    public static int lastIndexOf(CharSequence seq, CharSequence searchSeq) {
        return StringUtils.lastIndexOf(seq, searchSeq);
    }

    public static int lastIndexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
        return StringUtils.lastIndexOf(seq, searchSeq, startPos);
    }

    public static int lastIndexOf(CharSequence seq, int searchChar) {
        return StringUtils.lastIndexOf(seq, searchChar);
    }

    public static int lastIndexOf(CharSequence seq, int searchChar, int startPos) {
        return StringUtils.lastIndexOf(seq, searchChar, startPos);
    }

    public static int lastIndexOfAny(CharSequence str, CharSequence... searchStrs) {
        return StringUtils.lastIndexOfAny(str, searchStrs);
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return StringUtils.lastIndexOfIgnoreCase(str, searchStr);
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        return StringUtils.lastIndexOfIgnoreCase(str, searchStr, startPos);
    }

    public static int lastOrdinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal) {
        return StringUtils.lastOrdinalIndexOf(str, searchStr, ordinal);
    }

    public static String replaceOnceIgnoreCase(String text, String searchString, String replacement) {
        return StringUtils.replaceOnceIgnoreCase(text, searchString, replacement);
    }

    @Deprecated
    public static String replacePattern(String source, String regex, String replacement) {
        return StringUtils.replacePattern(source, regex, replacement);
    }

    @Deprecated
    public static String replaceAll(String text, String regex, String replacement) {
        return StringUtils.replaceAll(text, regex, replacement);
    }

    public static String replaceChars(String str, char searchChar, char replaceChar) {
        return StringUtils.replaceChars(str, searchChar, replaceChar);
    }

    public static String replaceChars(String str, String searchChars, String replaceChars) {
        return StringUtils.replaceChars(str, searchChars, replaceChars);
    }

    public static String replaceEach(String text, String[] searchList, String[] replacementList) {
        return StringUtils.replaceEach(text, searchList, replacementList);
    }

    public static String replaceEachRepeatedly(String text, String[] searchList, String[] replacementList) {
        return StringUtils.replaceEachRepeatedly(text, searchList, replacementList);
    }

    @Deprecated
    public static String replaceFirst(String text, String regex, String replacement) {
        return StringUtils.replaceFirst(text, regex, replacement);
    }

    public static String replaceIgnoreCase(String text, String searchString, String replacement) {
        return StringUtils.replaceIgnoreCase(text, searchString, replacement);
    }

    public static String replaceIgnoreCase(String text, String searchString, String replacement, int max) {
        return StringUtils.replaceIgnoreCase(text, searchString, replacement, max);
    }

    public static String center(String str, int size, char padChar) {
        return StringUtils.center(str, size, padChar);
    }

    public static String chop(String str) {
        return StringUtils.chop(str);
    }

    public static int compare(String str1, String str2) {
        return StringUtils.compare(str1, str2);
    }

    public static int compare(String str1, String str2, boolean nullIsLess) {
        return StringUtils.compare(str1, str2, nullIsLess);
    }

    public static int compareIgnoreCase(String str1, String str2) {
        return StringUtils.compareIgnoreCase(str1, str2);
    }

    public static int compareIgnoreCase(String str1, String str2, boolean nullIsLess) {
        return StringUtils.compareIgnoreCase(str1, str2, nullIsLess);
    }

    public static boolean contains(CharSequence seq, CharSequence searchSeq) {
        return StringUtils.contains(seq, searchSeq);
    }

    public static boolean contains(CharSequence seq, int searchChar) {
        return StringUtils.contains(seq, searchChar);
    }

    public static boolean containsAny(CharSequence cs, char... searchChars) {
        return StringUtils.containsAny(cs, searchChars);
    }

    public static boolean containsAny(CharSequence cs, CharSequence searchChars) {
        return StringUtils.containsAny(cs, searchChars);
    }

    public static boolean containsAny(CharSequence cs, CharSequence... searchCharSequences) {
        return StringUtils.containsAny(cs, searchCharSequences);
    }

    public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
        return StringUtils.containsIgnoreCase(str, searchStr);
    }

    public static boolean containsNone(CharSequence cs, char... searchChars) {
        return StringUtils.containsNone(cs, searchChars);
    }

    public static boolean containsNone(CharSequence cs, String invalidChars) {
        return StringUtils.containsNone(cs, invalidChars);
    }

    public static boolean containsOnly(CharSequence cs, char... valid) {
        return StringUtils.containsOnly(cs, valid);
    }

    public static boolean containsOnly(CharSequence cs, String validChars) {
        return StringUtils.containsOnly(cs, validChars);
    }

    public static boolean containsWhitespace(CharSequence seq) {
        return StringUtils.containsWhitespace(seq);
    }

    public static int countMatches(CharSequence str, char ch) {
        return StringUtils.countMatches(str, ch);
    }

    public static int countMatches(CharSequence str, CharSequence sub) {
        return StringUtils.countMatches(str, sub);
    }

    public static <T extends CharSequence> T defaultIfBlank(T str, T defaultStr) {
        return StringUtils.defaultIfBlank(str, defaultStr);
    }

    public static <T extends CharSequence> T defaultIfEmpty(T str, T defaultStr) {
        return StringUtils.defaultIfEmpty(str, defaultStr);
    }

    public static String defaultString(String str) {
        return StringUtils.defaultString(str);
    }

    public static String defaultString(String str, String defaultStr) {
        return StringUtils.defaultString(str, defaultStr);
    }

    public static String deleteWhitespace(String str) {
        return StringUtils.deleteWhitespace(str);
    }

    public static String rightPad(String str, int size, char padChar) {
        return StringUtils.rightPad(str, size, padChar);
    }

    public static String rotate(String str, int shift) {
        return StringUtils.rotate(str, shift);
    }

    public static String[] split(String str) {
        return StringUtils.split(str);
    }

    public static String[] split(String str, char separatorChar) {
        return StringUtils.split(str, separatorChar);
    }

    public static String leftPad(String str, int size, char padChar) {
        return StringUtils.leftPad(str, size, padChar);
    }

    public static int length(CharSequence cs) {
        return StringUtils.length(cs);
    }

    public static String stripAccents(String input) {
        return StringUtils.stripAccents(input);
    }

    public static String stripToEmpty(String str) {
        return StringUtils.stripToEmpty(str);
    }

    public static String stripToNull(String str) {
        return StringUtils.stripToNull(str);
    }

    public static String valueOf(char[] value) {
        return StringUtils.valueOf(value);
    }

    public static String wrap(String str, char wrapWith) {
        return StringUtils.wrap(str, wrapWith);
    }

    public static String wrap(String str, String wrapWith) {
        return StringUtils.wrap(str, wrapWith);
    }

    public static String wrapIfMissing(String str, char wrapWith) {
        return StringUtils.wrapIfMissing(str, wrapWith);
    }

    public static String wrapIfMissing(String str, String wrapWith) {
        return StringUtils.wrapIfMissing(str, wrapWith);
    }

    public static String initials(String str) {
        return WordUtils.initials(str);
    }

    public static String initials(String str, char... delimiters) {
        return WordUtils.initials(str, delimiters);
    }

    public static boolean containsAllWords(CharSequence word, CharSequence... words) {
        return WordUtils.containsAllWords(word, words);
    }

    @Deprecated
    public static boolean isDelimiter(char ch, char[] delimiters) {
        return WordUtils.isDelimiter(ch, delimiters);
    }

    @Deprecated
    public static boolean isDelimiter(int codePoint, char[] delimiters) {
        return WordUtils.isDelimiter(codePoint, delimiters);
    }

    public static String abbreviate(String str, int lower, int upper, String appendToEnd) {
        return WordUtils.abbreviate(str, lower, upper, appendToEnd);
    }

    public static int[] toCodePoints(CharSequence str) {
        return StringUtils.toCodePoints(str);
    }

    public static String toEncodedString(byte[] bytes, Charset charset) {
        return StringUtils.toEncodedString(bytes, charset);
    }

    public static String toRootLowerCase(String source) {
        return StringUtils.toRootLowerCase(source);
    }

    public static String toRootUpperCase(String source) {
        return StringUtils.toRootUpperCase(source);
    }

    @Deprecated
    public static String toString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
        return StringUtils.toString(bytes, charsetName);
    }

    public static String reverseDelimited(String str, char separatorChar) {
        return StringUtils.reverseDelimited(str, separatorChar);
    }

    public static String abbreviate(String str, String abbrevMarker, int maxWidth) {
        return StringUtils.abbreviate(str, abbrevMarker, maxWidth);
    }

    public static String abbreviate(String str, String abbrevMarker, int offset, int maxWidth) {
        return StringUtils.abbreviate(str, abbrevMarker, offset, maxWidth);
    }

    public static String abbreviateMiddle(String str, String middle, int length) {
        return StringUtils.abbreviateMiddle(str, middle, length);
    }

    public static String appendIfMissing(String str, CharSequence suffix, CharSequence... suffixes) {
        return StringUtils.appendIfMissing(str, suffix, suffixes);
    }

    public static String appendIfMissingIgnoreCase(String str, CharSequence suffix, CharSequence... suffixes) {
        return StringUtils.appendIfMissingIgnoreCase(str, suffix, suffixes);
    }

    public static String capitalize(String str) {
        return StringUtils.capitalize(str);
    }

    public static String capitalize(String str, char... delimiters) {
        return WordUtils.capitalize(str, delimiters);
    }

    public static String capitalizeFully(String str) {
        return WordUtils.capitalizeFully(str);
    }

    public static String capitalizeFully(String str, char... delimiters) {
        return WordUtils.capitalizeFully(str, delimiters);
    }

    public static boolean endsWith(CharSequence str, CharSequence suffix) {
        return StringUtils.endsWith(str, suffix);
    }

    public static boolean endsWithAny(CharSequence sequence, CharSequence... searchStrings) {
        return StringUtils.endsWithAny(sequence, searchStrings);
    }

    public static boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return StringUtils.endsWithIgnoreCase(str, suffix);
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        return StringUtils.equals(cs1, cs2);
    }

    public static boolean equalsAny(CharSequence string, CharSequence... searchStrings) {
        return StringUtils.equalsAny(string, searchStrings);
    }

    public static boolean equalsAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) {
        return StringUtils.equalsAnyIgnoreCase(string, searchStrings);
    }

    public static boolean equalsIgnoreCase(CharSequence cs1, CharSequence cs2) {
        return StringUtils.equalsIgnoreCase(cs1, cs2);
    }

    public static boolean stringEquals(String str1, String str2) {
        return equals(str1, str2);
    }

    public static boolean stringEqualsIgnoreCase(String str1, String str2) {
        return equalsIgnoreCase(str1, str2);
    }

    @SafeVarargs
    public static <T extends CharSequence> T firstNonBlank(T... values) {
        return StringUtils.firstNonBlank(values);
    }

    @SafeVarargs
    public static <T extends CharSequence> T firstNonEmpty(T... values) {
        return StringUtils.firstNonEmpty(values);
    }

    public static byte[] getBytes(String string, Charset charset) {
        return StringUtils.getBytes(string, charset);
    }

    public static byte[] getBytes(String string, String charset) throws UnsupportedEncodingException {
        return StringUtils.getBytes(string, charset);
    }

    public static String getCommonPrefix(String... strs) {
        return StringUtils.getCommonPrefix(strs);
    }

    public static String getDigits(String str) {
        return StringUtils.getDigits(str);
    }

    @Deprecated
    public static int getFuzzyDistance(CharSequence term, CharSequence query, Locale locale) {
        return StringUtils.getFuzzyDistance(term, query, locale);
    }

    public static <T extends CharSequence> T getIfBlank(T str, Supplier<T> defaultSupplier) {
        return StringUtils.getIfBlank(str, defaultSupplier);
    }

    public static <T extends CharSequence> T getIfEmpty(T str, Supplier<T> defaultSupplier) {
        return StringUtils.getIfEmpty(str, defaultSupplier);
    }

    @Deprecated
    public static double getJaroWinklerDistance(CharSequence first, CharSequence second) {
        return StringUtils.getJaroWinklerDistance(first, second);
    }

    @Deprecated
    public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
        return StringUtils.getLevenshteinDistance(s, t);
    }

    @Deprecated
    public static int getLevenshteinDistance(CharSequence s, CharSequence t, int threshold) {
        return StringUtils.getLevenshteinDistance(s, t, threshold);
    }

    public static int indexOf(CharSequence seq, CharSequence searchSeq) {
        return StringUtils.indexOf(seq, searchSeq);
    }

    public static int indexOf(CharSequence seq, CharSequence searchSeq, int startPos) {
        return StringUtils.indexOf(seq, searchSeq, startPos);
    }

    public static int indexOf(CharSequence seq, int searchChar) {
        return StringUtils.indexOf(seq, searchChar);
    }

    public static int indexOf(CharSequence seq, int searchChar, int startPos) {
        return StringUtils.indexOf(seq, searchChar, startPos);
    }

    public static int indexOfAny(CharSequence cs, char... searchChars) {
        return StringUtils.indexOfAny(cs, searchChars);
    }

    public static int indexOfAny(CharSequence str, CharSequence... searchStrs) {
        return StringUtils.indexOfAny(str, searchStrs);
    }

    public static int indexOfAny(CharSequence cs, String searchChars) {
        return StringUtils.indexOfAny(cs, searchChars);
    }

    public static int indexOfAnyBut(CharSequence cs, char... searchChars) {
        return StringUtils.indexOfAnyBut(cs, searchChars);
    }

    public static int indexOfAnyBut(CharSequence seq, CharSequence searchChars) {
        return StringUtils.indexOfAnyBut(seq, searchChars);
    }

    public static int indexOfDifference(CharSequence... css) {
        return StringUtils.indexOfDifference(css);
    }

    public static int indexOfDifference(CharSequence cs1, CharSequence cs2) {
        return StringUtils.indexOfDifference(cs1, cs2);
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr) {
        return StringUtils.indexOfIgnoreCase(str, searchStr);
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        return StringUtils.indexOfIgnoreCase(str, searchStr, startPos);
    }

    public static boolean isAllBlank(CharSequence... css) {
        return StringUtils.isAllBlank(css);
    }

    public static boolean isAllEmpty(CharSequence... css) {
        return StringUtils.isAllEmpty(css);
    }

    public static <T> T[] toArray(T... _args)
    {
        return _args;
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3, Object _a4, Object _a5, Object _a6)
    {
        return new Object[]{_a1, _a2, _a3, _a4, _a5, _a6};
    }

    public static String[] toArray(String _a1, String _a2, String _a3, String _a4, String _a5, String _a6)
    {
        return new String[]{_a1, _a2, _a3, _a4, _a5, _a6};
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3, Object _a4, Object _a5)
    {
        return new Object[]{_a1, _a2, _a3, _a4, _a5};
    }

    public static String[] toArray(String _a1, String _a2, String _a3, String _a4, String _a5)
    {
        return new String[]{_a1, _a2, _a3, _a4, _a5};
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3, Object _a4)
    {
        return new Object[]{_a1, _a2, _a3, _a4};
    }

    public static String[] toArray(String _a1, String _a2, String _a3, String _a4)
    {
        return new String[]{_a1, _a2, _a3, _a4};
    }

    public static Object[] toArray(Object _a1, Object _a2, Object _a3)
    {
        return new Object[]{_a1, _a2, _a3};
    }

    public static String[] toArray(String _a1, String _a2, String _a3)
    {
        return new String[]{_a1, _a2, _a3};
    }

    public static Object[] toArray(Object _a1, Object _a2)
    {
        return new Object[]{_a1, _a2};
    }

    public static String[] toArray(String _a1, String _a2)
    {
        return new String[]{_a1, _a2};
    }

    public static Object[] toArray(Object _a1)
    {
        return new Object[]{_a1};
    }

    public static String[] toArray(String _a1)
    {
        return new String[]{_a1};
    }


    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isMixedCase(CharSequence cs) {
        return StringUtils.isMixedCase(cs);
    }

    public static boolean isNoneBlank(CharSequence... css) {
        return StringUtils.isNoneBlank(css);
    }

    public static boolean isNoneEmpty(CharSequence... css) {
        return StringUtils.isNoneEmpty(css);
    }

    public static boolean isNotBlank(CharSequence cs) {
        return StringUtils.isNotBlank(cs);
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isNumeric(CharSequence cs) {
        return StringUtils.isNumeric(cs);
    }

    public static boolean isNumericSpace(CharSequence cs) {
        return StringUtils.isNumericSpace(cs);
    }

    public static boolean isWhitespace(CharSequence cs) {
        return StringUtils.isWhitespace(cs);
    }

    public static String join(byte[] array, char separator) {
        return StringUtils.join(array, separator);
    }

    public static String join(byte[] array, char separator, int startIndex, int endIndex) {
        return StringUtils.join(array, separator, startIndex, endIndex);
    }

    public static String join(char[] array, char separator) {
        return StringUtils.join(array, separator);
    }

    public static String join(char[] array, char separator, int startIndex, int endIndex) {
        return StringUtils.join(array, separator, startIndex, endIndex);
    }

    public static String join(double[] array, char separator) {
        return StringUtils.join(array, separator);
    }

    public static String join(double[] array, char separator, int startIndex, int endIndex) {
        return StringUtils.join(array, separator, startIndex, endIndex);
    }

    public static String join(float[] array, char separator) {
        return StringUtils.join(array, separator);
    }

    public static String join(float[] array, char separator, int startIndex, int endIndex) {
        return StringUtils.join(array, separator, startIndex, endIndex);
    }

    public static String join(int[] array, char separator) {
        return StringUtils.join(array, separator);
    }

    public static String join(int[] array, char separator, int startIndex, int endIndex) {
        return StringUtils.join(array, separator, startIndex, endIndex);
    }

    public static String join(Iterable<?> iterable, char separator) {
        return StringUtils.join(iterable, separator);
    }

    public static String join(Iterable<?> iterable, String separator) {
        return StringUtils.join(iterable, separator);
    }

    public static String join(Iterator<?> iterator, char separator) {
        return StringUtils.join(iterator, separator);
    }

    public static String join(Iterator<?> iterator, String separator) {
        return StringUtils.join(iterator, separator);
    }

    public static String join(List<?> list, char separator, int startIndex, int endIndex) {
        return StringUtils.join(list, separator, startIndex, endIndex);
    }

    public static String join(List<?> list, String separator, int startIndex, int endIndex) {
        return StringUtils.join(list, separator, startIndex, endIndex);
    }

    public static String join(long[] array, char separator) {
        return StringUtils.join(array, separator);
    }

    public static String join(long[] array, char separator, int startIndex, int endIndex) {
        return StringUtils.join(array, separator, startIndex, endIndex);
    }

    public static String join(Object[] array, char separator) {
        return StringUtils.join(array, separator);
    }

    public static String join(Object[] array, char separator, int startIndex, int endIndex) {
        return StringUtils.join(array, separator, startIndex, endIndex);
    }

    public static boolean isAsciiPrintable(CharSequence cs) {
        return StringUtils.isAsciiPrintable(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        return StringUtils.isBlank(cs);
    }

    public static boolean isAllLowerCase(CharSequence cs) {
        return StringUtils.isAllLowerCase(cs);
    }

    public static boolean isAllUpperCase(CharSequence cs) {
        return StringUtils.isAllUpperCase(cs);
    }

    public static boolean isAlpha(CharSequence cs) {
        return StringUtils.isAlpha(cs);
    }

    public static boolean isAlphanumeric(CharSequence cs) {
        return StringUtils.isAlphanumeric(cs);
    }

    public static boolean isAlphanumericSpace(CharSequence cs) {
        return StringUtils.isAlphanumericSpace(cs);
    }

    public static boolean isAlphaSpace(CharSequence cs) {
        return StringUtils.isAlphaSpace(cs);
    }

    public static boolean isAnyBlank(CharSequence... css) {
        return StringUtils.isAnyBlank(css);
    }

    public static boolean isAnyEmpty(CharSequence... css) {
        return StringUtils.isAnyEmpty(css);
    }

    public static String cleanString(String str) {
        return org.codehaus.plexus.util.StringUtils.clean(str);
    }

    // plexus/lang3 StringUtils -- END

    //*************************************************************************

    // lang3 CharSequenceUtils -- START

    public static CharSequence subSequence(CharSequence cs, int start) {
        return CharSequenceUtils.subSequence(cs, start);
    }

    public static char[] toCharArray(CharSequence source) {
        return CharSequenceUtils.toCharArray(source);
    }

    // lang3 CharSequenceUtils -- END

    //*************************************************************************

    // text.WordUtils -- START

    public static String wrap(String str, int wrapLength) {
        return WordUtils.wrap(str, wrapLength);
    }

    public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
        return WordUtils.wrap(str, wrapLength, newLineStr, wrapLongWords);
    }

    public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords, String wrapOn) {
        return WordUtils.wrap(str, wrapLength, newLineStr, wrapLongWords, wrapOn);
    }

    // text.WordUtils -- END

    //*************************************************************************

    // text.CaseUtils -- START

    public static String toCamelCase(String str, boolean capitalizeFirstLetter, char... delimiters)
    {
        return CaseUtils.toCamelCase(str, capitalizeFirstLetter, delimiters);
    }

    // text.CaseUtils -- END

    //*************************************************************************

    // lang3 StringEscapeUtils -- START

    public static String escapeJava(String input) {
        return StringEscapeUtils.escapeJava(input);
    }

    public static String escapeEcmaScript(String input) {
        return StringEscapeUtils.escapeEcmaScript(input);
    }

    public static String escapeJson(String input) {
        return StringEscapeUtils.escapeJson(input);
    }

    public static String unescapeJava(String input) {
        return StringEscapeUtils.unescapeJava(input);
    }

    public static String unescapeEcmaScript(String input) {
        return StringEscapeUtils.unescapeEcmaScript(input);
    }

    public static String unescapeJson(String input) {
        return StringEscapeUtils.unescapeJson(input);
    }

    public static String escapeHtml4(String input) {
        return StringEscapeUtils.escapeHtml4(input);
    }

    public static String escapeHtml3(String input) {
        return StringEscapeUtils.escapeHtml3(input);
    }

    public static String unescapeHtml4(String input) {
        return StringEscapeUtils.unescapeHtml4(input);
    }

    public static String unescapeHtml3(String input) {
        return StringEscapeUtils.unescapeHtml3(input);
    }

    @Deprecated
    public static String escapeXml(String input) {
        return StringEscapeUtils.escapeXml(input);
    }

    public static String escapeXml10(String input) {
        return StringEscapeUtils.escapeXml10(input);
    }

    public static String escapeXml11(String input) {
        return StringEscapeUtils.escapeXml11(input);
    }

    public static String unescapeXml(String input) {
        return StringEscapeUtils.unescapeXml(input);
    }

    public static String escapeCsv(String input) {
        return StringEscapeUtils.escapeCsv(input);
    }

    public static String unescapeCsv(String input) {
        return StringEscapeUtils.unescapeCsv(input);
    }

    // lang3 StringEscapeUtils -- END


}
