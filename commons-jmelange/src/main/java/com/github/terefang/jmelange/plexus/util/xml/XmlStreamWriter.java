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

import com.github.terefang.jmelange.plexus.util.xml.XmlReader;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Character stream that handles (or at least attempts to) all the necessary Voodo to figure out the charset encoding of
 * the XML document written to the stream.
 *
 * @author <a href="mailto:hboutemy@codehaus.org">Herve Boutemy</a>
 *
 * @since 1.4.4
 */
public class XmlStreamWriter extends Writer {
    private static final int BUFFER_SIZE = 4096;

    private StringWriter xmlPrologWriter = new StringWriter(BUFFER_SIZE);

    private OutputStream out;

    private Writer writer;

    private String encoding;

    public XmlStreamWriter(OutputStream out) {
        this.out = out;
    }

    public XmlStreamWriter(File file) throws IOException {
        this(Files.newOutputStream(file.toPath()));
    }

    public String getEncoding() {
        return encoding;
    }

    @Override
    public void close() throws IOException {
        if (writer == null) {
            encoding = "UTF-8";
            writer = new OutputStreamWriter(out, encoding);
            writer.write(xmlPrologWriter.toString());
        }
        writer.close();
    }

    @Override
    public void flush() throws IOException {
        if (writer != null) {
            writer.flush();
        }
    }

    private void detectEncoding(char[] cbuf, int off, int len) throws IOException {
        int size = len;
        StringBuffer xmlProlog = xmlPrologWriter.getBuffer();
        if (xmlProlog.length() + len > BUFFER_SIZE) {
            size = BUFFER_SIZE - xmlProlog.length();
        }
        xmlPrologWriter.write(cbuf, off, size);

        // try to determine encoding
        if (xmlProlog.length() >= 5) {
            if (xmlProlog.substring(0, 5).equals("<?xml")) {
                // try to extract encoding from XML prolog
                int xmlPrologEnd = xmlProlog.indexOf("?>");
                if (xmlPrologEnd > 0) {
                    // ok, full XML prolog written: let's extract encoding
                    Matcher m = ENCODING_PATTERN.matcher(xmlProlog.substring(0, xmlPrologEnd));
                    if (m.find()) {
                        encoding = m.group(1).toUpperCase(Locale.ENGLISH);
                        encoding = encoding.substring(1, encoding.length() - 1);
                    } else {
                        // no encoding found in XML prolog: using default encoding
                        encoding = "UTF-8";
                    }
                } else {
                    if (xmlProlog.length() >= BUFFER_SIZE) {
                        // no encoding found in first characters: using default encoding
                        encoding = "UTF-8";
                    }
                }
            } else {
                // no XML prolog: using default encoding
                encoding = "UTF-8";
            }
            if (encoding != null) {
                // encoding has been chosen: let's do it
                xmlPrologWriter = null;
                writer = new OutputStreamWriter(out, encoding);
                writer.write(xmlProlog.toString());
                if (len > size) {
                    writer.write(cbuf, off + size, len - size);
                }
            }
        }
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (xmlPrologWriter != null) {
            detectEncoding(cbuf, off, len);
        } else {
            writer.write(cbuf, off, len);
        }
    }

    static final Pattern ENCODING_PATTERN = XmlReader.ENCODING_PATTERN;
}
