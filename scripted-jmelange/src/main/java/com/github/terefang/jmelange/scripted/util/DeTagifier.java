package com.github.terefang.jmelange.scripted.util;

import lombok.Data;

import java.io.*;

@Data
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

    public static final int IN_COMMENT_START_TAG = 9;
    public static final int IN_COMMENT_BLOCK = 10;
    public static final int IN_COMMENT_END_TAG1 = 11;
    public static final int IN_COMMENT_END_TAG2 = 12;
    public static final int IN_COMMENT_END_TAG3 = 13;

    public static final int INVALID_STATE = -1;

    public static final int ESCAPE_NONE = 0;
    public static final int ESCAPE_C_HEX = 1;
    public static final int ESCAPE_JAVA_UNICODE = 2;
    public static final int ESCAPE_XML_ENTITY = 3;
    public static final int ESCAPE_RUBY_UNICODE = 4;

    private String parseStart="/* START */\n";
    private String parseEnd="/* END */\n";
    private String outputStart="";
    private String outputEnd="";
    private String commentStart="";
    private String commentEnd="";
    private String exprStart="";
    private String exprEnd="";

    private int escapeStyle = ESCAPE_C_HEX;

    public DeTagifier() {   }

    public String parse(Reader reader) throws IOException {
        CharHolder charHolder = new DeTagifier.CharHolder();
        int i = 0;
        int state = START;
        charHolder.put(parseStart);
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

        charHolder.put(parseEnd);
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
                if (c == '<')
                {
                    return INSIDE_START_TAG;
                }
                else if ((c < ' ') || (c == '\\') || (c == '"') || (c == '\'') || (c == '`'))
                {
                    if(this.escapeStyle == ESCAPE_C_HEX)
                    {
                        charHolder.put(String.format("\\x%02X", (int)c));
                    }
                    else if(this.escapeStyle == ESCAPE_JAVA_UNICODE)
                    {
                        charHolder.put(String.format("\\u%04X", (int)c));
                    }
                    else if(this.escapeStyle == ESCAPE_XML_ENTITY)
                    {
                        charHolder.put(String.format("&#%d;", (int)c));
                    }
                    else if(this.escapeStyle == ESCAPE_RUBY_UNICODE)
                    {
                        charHolder.put(String.format("\\u{%04X}", (int)c));
                    }
                    if (c == '\n') {
                        charHolder.put(outputEnd);
                        charHolder.put(outputStart);
                    }
                    return IN_OUTPUTBLOCK;
                }
                else
                {
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
            case IN_COMMENT_END_TAG1:
                if (c == '-') {
                    return IN_COMMENT_END_TAG2;
                }
                else {
                    charHolder.put('-');
                    charHolder.put(c);
                    return IN_COMMENT_BLOCK;
                }
            case IN_COMMENT_END_TAG2:
                if (c == '%') {
                    return IN_COMMENT_END_TAG3;
                }
                else {
                    charHolder.put('-');
                    charHolder.put('-');
                    charHolder.put(c);
                    return IN_COMMENT_BLOCK;
                }
            case IN_COMMENT_END_TAG3:
                if (c == '>') {
                    charHolder.put(commentEnd);
                    charHolder.put(outputStart);
                    return IN_OUTPUTBLOCK;
                } else {
                    charHolder.put('-');
                    charHolder.put('-');
                    charHolder.put('%');
                    charHolder.put(c);
                    return IN_COMMENT_BLOCK;
                }
            case IN_COMMENT_BLOCK:
                if (c == '-') {
                    return IN_COMMENT_END_TAG1;
                }
                else {
                    charHolder.put(c);
                    return IN_COMMENT_BLOCK;
                }
            case IN_COMMENT_START_TAG:
                if (c == '-') {
                    charHolder.put(commentStart);
                    return IN_COMMENT_BLOCK;
                } else {
                    charHolder.put('-');
                    charHolder.put(c);
                    return IN_CODEBLOCK;
                }
            case INSIDE_CODE_EXPR_BLOCK:
                if (c == '-') {
                    return IN_COMMENT_START_TAG;
                } else if (c == '=') {
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
