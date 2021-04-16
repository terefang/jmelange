package com.github.terefang.jmelange.scripted.util;

import java.io.IOException;
import java.io.Reader;

public class DeTagifier {

    public static final int START = 0;
    public static final int IN_CODEBLOCK = 1;
    public static final int IN_OUTPUTBLOCK = 2;
    public static final int IN_EXPRBLOCK = 3;
    public static final int INSIDE_START_TAG = 4;
    public static final int INSIDE_INITIAL_START_TAG = 5;
    public static final int INSIDE_END_TAG = 6;
    public static final int INSIDE_CODE_EXPR_BLOCK = 7;
    public static final int INSIDE_EXPR_END_TAG = 8;
    public static final int INVALID_STATE = -1;

    private String outputStart;
    private String outputEnd;
    private String exprStart;
    private String exprEnd;

    public DeTagifier(String outputStart, String outputEnd, String exprStart, String exprEnd) {
        this.outputStart = outputStart;
        this.outputEnd = outputEnd;
        this.exprStart = exprStart;
        this.exprEnd = exprEnd;
    }

    public String parse(Reader reader) throws IOException {
        CharHolder charHolder = new DeTagifier.CharHolder();
        int i = 0;
        int state = START;
        while (-1 != (i = reader.read())) {
            //ignore control-M
            if (i == (int)'\r') {
                continue;
            }

            state = processChar(state, (char)i, charHolder);
            if (state == INVALID_STATE) {
                return null;
            }
        }
        if (state == IN_OUTPUTBLOCK) {
            charHolder.put(outputEnd);
        }

        return charHolder.getString();
    }

    public int processChar(int state, char c, CharHolder charHolder)
            throws IOException {

        switch (state) {
            case START:
                if (c == '<') {
                    return INSIDE_INITIAL_START_TAG;
                } else {
                    charHolder.put(outputStart);
                    charHolder.put(c);
                    return IN_OUTPUTBLOCK;
                }
            case IN_CODEBLOCK:
                if (c == '%') {
                    return INSIDE_END_TAG;
                } else {
                    charHolder.put(c);
                    return IN_CODEBLOCK;
                }
            case IN_OUTPUTBLOCK:
                if (c == '<') {
                    return INSIDE_START_TAG;
                } else if (c == '\n') {
                    charHolder.put('\\');
                    charHolder.put('n');
                    charHolder.put(outputEnd);
                    charHolder.put(outputStart);
                    return IN_OUTPUTBLOCK;
                } else if (c == '"') {
                    charHolder.put('\\');
                    charHolder.put(c);
                    return IN_OUTPUTBLOCK;
                } else {
                    charHolder.put(c);
                    return IN_OUTPUTBLOCK;
                }
            case IN_EXPRBLOCK:
                if (c == '%') {
                    return INSIDE_EXPR_END_TAG;
                }
                else {
                    charHolder.put(c);
                    return IN_EXPRBLOCK;
                }
            case INSIDE_INITIAL_START_TAG:
            case INSIDE_START_TAG:
                if (c == '%') {
                    if (state == INSIDE_START_TAG) {
                        charHolder.put(outputEnd);
                    }
                    return INSIDE_CODE_EXPR_BLOCK;
                } else {
                    if (state == INSIDE_INITIAL_START_TAG) {
                        charHolder.put(outputStart);
                    }
                    charHolder.put('<');
                    charHolder.put(c);
                    return IN_OUTPUTBLOCK;
                }
            case INSIDE_END_TAG:
                if (c == '>') {
                    charHolder.put(outputStart);
                    return IN_OUTPUTBLOCK;
                } else {
                    charHolder.put('%');
                    charHolder.put(c);
                    return IN_CODEBLOCK;
                }
            case INSIDE_CODE_EXPR_BLOCK:
                if (c == '=') {
                    charHolder.put(exprStart);
                    return IN_EXPRBLOCK;
                } else {
                    charHolder.put(c);
                    return IN_CODEBLOCK;
                }
            case INSIDE_EXPR_END_TAG:
                if (c == '>') {
                    charHolder.put(exprEnd);
                    charHolder.put(outputStart);
                    return IN_OUTPUTBLOCK;
                } else {
                    charHolder.put('%');
                    charHolder.put(c);
                    return IN_EXPRBLOCK;
                }
        }
        return INVALID_STATE;

    }


    public class CharHolder {
        private char[] chars = new char[1000];
        int current = 0;
        int size = 1000;

        public void put(char c) {
            if (current == size - 1) {
                char[] newChars = new char[2 * size];
                for (int i = 0; i < size; i++) {
                    newChars[i] = chars[i];
                }
                size *= 2;
                chars = newChars;
            }

            chars[current++] = c;
        }

        public void put(String str) {
            int l = str.length();
            for (int i = 0; i < l ; i++) {
                put(str.charAt(i));
            }
        }

        public String getString() {
            return new String(chars , 0, current);
        }
    }
}
