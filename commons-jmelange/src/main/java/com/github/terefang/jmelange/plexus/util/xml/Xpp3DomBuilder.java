package com.github.terefang.jmelange.plexus.util.xml;

/*
 * Copyright The Codehaus Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom;
import com.github.terefang.jmelange.plexus.util.xml.pull.MXParser;
import com.github.terefang.jmelange.plexus.util.xml.pull.XmlPullParser;
import com.github.terefang.jmelange.plexus.util.xml.pull.XmlPullParserException;

/**
 *
 */
public class Xpp3DomBuilder {
    private static final boolean DEFAULT_TRIM = true;

    public static com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom build(Reader reader) throws XmlPullParserException, IOException {
        return build(reader, null);
    }

    /**
     * @param reader the reader
     * @param locationBuilder the builder
     * @since 3.2.0
     * @return DOM
     * @throws XmlPullParserException xml exception
     * @throws IOException io
     */
    public static com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom build(Reader reader, InputLocationBuilder locationBuilder)
            throws XmlPullParserException, IOException {
        return build(reader, DEFAULT_TRIM, locationBuilder);
    }

    public static com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom build(InputStream is, String encoding) throws XmlPullParserException, IOException {
        return build(is, encoding, DEFAULT_TRIM);
    }

    public static com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom build(InputStream is, String encoding, boolean trim)
            throws XmlPullParserException, IOException {
        try {
            final XmlPullParser parser = new MXParser();
            parser.setInput(is, encoding);

            final com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom xpp3Dom = build(parser, trim);
            is.close();
            is = null;

            return xpp3Dom;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                    // ignore
                }
            }
        }
    }

    public static com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom build(Reader reader, boolean trim) throws XmlPullParserException, IOException {
        return build(reader, trim, null);
    }

    /**
     * @param reader the reader
     * @param trim to trim
     * @param locationBuilder the builder
     * @since 3.2.0
     * @return DOM
     * @throws XmlPullParserException xml exception
     * @throws IOException io
     */
    public static com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom build(Reader reader, boolean trim, InputLocationBuilder locationBuilder)
            throws XmlPullParserException, IOException {
        try {
            final XmlPullParser parser = new MXParser();
            parser.setInput(reader);

            final com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom xpp3Dom = build(parser, trim, locationBuilder);
            reader.close();
            reader = null;

            return xpp3Dom;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    // ignore
                }
            }
        }
    }

    public static com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom build(XmlPullParser parser) throws XmlPullParserException, IOException {
        return build(parser, DEFAULT_TRIM);
    }

    public static com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom build(XmlPullParser parser, boolean trim) throws XmlPullParserException, IOException {
        return build(parser, trim, null);
    }

    /**
     * @since 3.2.0
     * @param locationBuilder builder
     * @param parser the parser
     * @param trim do trim
     * @return DOM
     * @throws XmlPullParserException xml exception
     * @throws IOException io
     */
    public static com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom build(XmlPullParser parser, boolean trim, InputLocationBuilder locationBuilder)
            throws XmlPullParserException, IOException {
        List<com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom> elements = new ArrayList<>();

        List<StringBuilder> values = new ArrayList<>();

        int eventType = parser.getEventType();

        boolean spacePreserve = false;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                spacePreserve = false;

                String rawName = parser.getName();

                com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom childConfiguration = new com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom(rawName);

                if (locationBuilder != null) {
                    childConfiguration.setInputLocation(locationBuilder.toInputLocation(parser));
                }

                int depth = elements.size();

                if (depth > 0) {
                    com.github.terefang.jmelange.plexus.util.xml.Xpp3Dom parent = elements.get(depth - 1);

                    parent.addChild(childConfiguration);
                }

                elements.add(childConfiguration);

                if (parser.isEmptyElementTag()) {
                    values.add(null);
                } else {
                    values.add(new StringBuilder());
                }

                int attributesSize = parser.getAttributeCount();

                for (int i = 0; i < attributesSize; i++) {
                    String name = parser.getAttributeName(i);

                    String value = parser.getAttributeValue(i);

                    childConfiguration.setAttribute(name, value);

                    spacePreserve = spacePreserve || ("xml:space".equals(name) && "preserve".equals(value));
                }
            } else if (eventType == XmlPullParser.TEXT) {
                int depth = values.size() - 1;

                @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
                StringBuilder valueBuffer = values.get(depth);

                String text = parser.getText();

                if (trim && !spacePreserve) {
                    text = text.trim();
                }

                valueBuffer.append(text);
            } else if (eventType == XmlPullParser.END_TAG) {
                int depth = elements.size() - 1;

                Xpp3Dom finishedConfiguration = elements.remove(depth);

                /* this Object could be null if it is a singleton tag */
                Object accumulatedValue = values.remove(depth);

                if (finishedConfiguration.getChildCount() == 0) {
                    if (accumulatedValue == null) {
                        finishedConfiguration.setValue(null);
                    } else {
                        finishedConfiguration.setValue(accumulatedValue.toString());
                    }
                }

                if (depth == 0) {
                    return finishedConfiguration;
                }
            }

            eventType = parser.next();
        }

        throw new IllegalStateException("End of document found before returning to 0 depth");
    }

    /**
     * Input location builder interface, to be implemented to choose how to store data.
     *
     * @since 3.2.0
     */
    public static interface InputLocationBuilder {
        Object toInputLocation(XmlPullParser parser);
    }
}
