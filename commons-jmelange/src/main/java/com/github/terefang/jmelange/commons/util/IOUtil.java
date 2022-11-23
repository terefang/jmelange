package com.github.terefang.jmelange.commons.util;

import com.github.terefang.jmelange.commons.zip.ByFileArchiver;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.function.IOConsumer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class IOUtil
{
    @SneakyThrows
    public static String read4CC(InputStream _in)
    {
        return new String(readBytes(4, _in));
    }

    @SneakyThrows
    public static String read2CC(InputStream _in)
    {
        return new String(readBytes(2, _in));
    }

    public static byte[] from4CC(String _s)
    {
        return _s.substring(0,4).getBytes();
    }

    public static byte[] from2CC(String _s)
    {
        return _s.substring(0,2).getBytes();
    }

    public static byte[] from4CC(int _s)
    {
        return toByteArray(_s);
    }

    public static byte[] from2CC(int _s)
    {
        return toByteArray((short)_s);
    }

    public static int to4CCInt(byte[] _cc)
    {
        return (int) bytesToLong(_cc);
    }

    public static int to2CCInt(byte[] _cc)
    {
        return (int) bytesToLong(_cc);
    }

    public static String to4CC(byte[] _cc)
    {
        return new String(_cc).substring(0,4);
    }

    public static String to2CC(byte[] _cc)
    {
        return new String(_cc).substring(0,4);
    }

    public static String to4CC(int _cc)
    {
        return new String(toByteArray(_cc));
    }

    public static String to2CC(int _cc)
    {
        return new String(toByteArray((short)_cc));
    }


    public static long bytesToLong(byte[] _buf)
    {
        long _l = 0L;
        for(int _i =0; _i<_buf.length; _i++)
        {
            _l = (_l<<8) | (_buf[_i] & 0xff);
        }
        return _l;
    }

    @SneakyThrows
    public static byte[] readBytes(final int _i, InputStream _in)
    {
        byte[] _buf = new byte[_i];
        _in.read(_buf);
        return _buf;
    }

    @SneakyThrows
    public static long readLong(final InputStream _in)
    {
        long _i = 0;
        for(int _k=0; _k<8; _k++)
        {
            _i = (_i << 8) | (_in.read() & 0xff);
        }
        return _i;
    }

    @SneakyThrows
    public static int readInt(final InputStream _in)
    {
        int _i = 0;
        for(int _k=0; _k<4; _k++)
        {
            _i = (_i << 8) | (_in.read() & 0xff);
        }
        return _i;
    }

    @SneakyThrows
    public static int readShort(final InputStream _in)
    {
        int _i = 0;
        for(int _k=0; _k<2; _k++)
        {
            _i = (_i << 8) | (_in.read() & 0xff);
        }
        return _i;
    }

    @SneakyThrows
    public static int readByte(final InputStream _in)
    {
        return (_in.read() & 0xff);
    }

    public static byte[] toByteArray(final Long _i)
    {
        return toByteArray(_i.longValue());
    }

    public static byte[] toByteArray(final long _i)
    {
        return new byte[] {
                (byte) ((_i >>> 56) & 0xff),
                (byte) ((_i >>> 48) & 0xff),
                (byte) ((_i >>> 40) & 0xff),
                (byte) ((_i >>> 32) & 0xff),
                (byte) ((_i >>> 24) & 0xff),
                (byte) ((_i >>> 16) & 0xff),
                (byte) ((_i >>> 8) & 0xff),
                (byte) (_i & 0xff),
        };
    }

    public static byte[] toByteArray(final Integer _i)
    {
        return toByteArray(_i.intValue());
    }

    public static byte[] toByteArray(final int _i)
    {
        return new byte[] {
                (byte) ((_i >>> 24) & 0xff),
                (byte) ((_i >>> 16) & 0xff),
                (byte) ((_i >>> 8) & 0xff),
                (byte) (_i & 0xff),
        };
    }

    public static byte[] toByteArray(final Short _i)
    {
        return toByteArray(_i.shortValue());
    }

    public static byte[] toByteArray(final short _i)
    {
        return new byte[] {
                (byte) ((_i >>> 8) & 0xff),
                (byte) (_i & 0xff),
        };
    }

    //**************************************************************************************************

    public static BufferedInputStream buffer(InputStream inputStream) {
        return IOUtils.buffer(inputStream);
    }

    public static BufferedInputStream buffer(InputStream inputStream, int size) {
        return IOUtils.buffer(inputStream, size);
    }

    public static BufferedOutputStream buffer(OutputStream outputStream) {
        return IOUtils.buffer(outputStream);
    }

    public static BufferedOutputStream buffer(OutputStream outputStream, int size) {
        return IOUtils.buffer(outputStream, size);
    }

    public static BufferedReader buffer(Reader reader) {
        return IOUtils.buffer(reader);
    }

    public static BufferedReader buffer(Reader reader, int size) {
        return IOUtils.buffer(reader, size);
    }

    public static BufferedWriter buffer(Writer writer) {
        return IOUtils.buffer(writer);
    }

    public static BufferedWriter buffer(Writer writer, int size) {
        return IOUtils.buffer(writer, size);
    }

    public static byte[] byteArray() {
        return IOUtils.byteArray();
    }

    public static byte[] byteArray(int size) {
        return IOUtils.byteArray(size);
    }

    public static void close(Closeable closeable) throws IOException {
        IOUtils.close(closeable);
    }

    public static void close(Closeable... closeables) throws IOException {
        IOUtils.close(closeables);
    }

    public static void close(Closeable closeable, IOConsumer<IOException> consumer) throws IOException {
        IOUtils.close(closeable, consumer);
    }

    public static void close(URLConnection conn) {
        IOUtils.close(conn);
    }

    public static void closeQuietly(Closeable closeable) {
        IOUtils.closeQuietly(closeable);
    }

    public static void closeQuietly(Closeable... closeables) {
        IOUtils.closeQuietly(closeables);
    }

    public static void closeQuietly(Closeable closeable, Consumer<IOException> consumer) {
        IOUtils.closeQuietly(closeable, consumer);
    }

    public static void closeQuietly(InputStream input) {
        IOUtils.closeQuietly(input);
    }

    public static void closeQuietly(OutputStream output) {
        IOUtils.closeQuietly(output);
    }

    public static void closeQuietly(Reader reader) {
        IOUtils.closeQuietly(reader);
    }

    public static void closeQuietly(Selector selector) {
        IOUtils.closeQuietly(selector);
    }

    public static void closeQuietly(ServerSocket serverSocket) {
        IOUtils.closeQuietly(serverSocket);
    }

    public static void closeQuietly(Socket socket) {
        IOUtils.closeQuietly(socket);
    }

    public static void closeQuietly(Writer writer) {
        IOUtils.closeQuietly(writer);
    }

    public static long consume(InputStream input) throws IOException {
        return IOUtils.consume(input);
    }

    public static void copy(InputStream input, OutputStream output) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output);
    }

    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output, bufferSize);
    }

    public static void copy(Reader input, Writer output) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output);
    }

    public static long copy(URL url, File file) throws IOException {
        return IOUtils.copy(url, file);
    }

    public static long copy(URL url, OutputStream outputStream) throws IOException {
        return IOUtils.copy(url, outputStream);
    }

    public static long copyLarge(InputStream inputStream, OutputStream outputStream) throws IOException {
        return IOUtils.copyLarge(inputStream, outputStream);
    }

    public static long copyLarge(InputStream inputStream, OutputStream outputStream, byte[] buffer) throws IOException {
        return IOUtils.copyLarge(inputStream, outputStream, buffer);
    }

    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length) throws IOException {
        return IOUtils.copyLarge(input, output, inputOffset, length);
    }

    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer) throws IOException {
        return IOUtils.copyLarge(input, output, inputOffset, length, buffer);
    }

    public static long copyLarge(Reader reader, Writer writer) throws IOException {
        return IOUtils.copyLarge(reader, writer);
    }

    public static long copyLarge(Reader reader, Writer writer, char[] buffer) throws IOException {
        return IOUtils.copyLarge(reader, writer, buffer);
    }

    public static long copyLarge(Reader reader, Writer writer, long inputOffset, long length) throws IOException {
        return IOUtils.copyLarge(reader, writer, inputOffset, length);
    }

    public static long copyLarge(Reader reader, Writer writer, long inputOffset, long length, char[] buffer) throws IOException {
        return IOUtils.copyLarge(reader, writer, inputOffset, length, buffer);
    }

    public static long copyToFile(InputStream _in, File _file) throws IOException
    {
        try (BufferedOutputStream _out = new BufferedOutputStream(new FileOutputStream(_file), 8192))
        {
            return copyLarge(_in, _out);
        }
    }

    public static long copyToFile(InputStream _in, String _file) throws IOException
    {
        try (BufferedOutputStream _out = new BufferedOutputStream(new FileOutputStream(_file), 8192))
        {
            return copyLarge(_in, _out);
        }
    }

    public static long copyToFile(Reader _in, File _file) throws IOException
    {
        try (OutputStreamWriter _out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(_file), 8192), StandardCharsets.UTF_8))
        {
            return copyLarge(_in, _out);
        }
    }

    public static long copyToFile(Reader _in, String _file) throws IOException
    {
        try (OutputStreamWriter _out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(_file), 8192), StandardCharsets.UTF_8))
        {
            return copyLarge(_in, _out);
        }
    }

    public static long copyToFile(Reader _in, File _file, Charset _cs) throws IOException
    {
        try (OutputStreamWriter _out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(_file), 8192), _cs))
        {
            return copyLarge(_in, _out);
        }
    }

    public static long copyToFile(Reader _in, String _file, Charset _cs) throws IOException
    {
        try (OutputStreamWriter _out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(_file), 8192), _cs))
        {
            return copyLarge(_in, _out);
        }
    }

    public static long copyToFile(Reader _in, File _file, String _cs) throws IOException
    {
        try (OutputStreamWriter _out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(_file), 8192), _cs))
        {
            return copyLarge(_in, _out);
        }
    }

    public static long copyToFile(Reader _in, String _file, String _cs) throws IOException
    {
        try (OutputStreamWriter _out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(_file), 8192), _cs))
        {
            return copyLarge(_in, _out);
        }
    }

    public static int length(byte[] array) {
        return IOUtils.length(array);
    }

    public static int length(char[] array) {
        return IOUtils.length(array);
    }

    public static int length(CharSequence csq) {
        return IOUtils.length(csq);
    }

    public static int length(Object[] array) {
        return IOUtils.length(array);
    }

    public static LineIterator lineIterator(InputStream input, Charset charset) {
        return IOUtils.lineIterator(input, charset);
    }

    public static LineIterator lineIterator(InputStream input, String charsetName) {
        return IOUtils.lineIterator(input, charsetName);
    }

    public static LineIterator lineIterator(Reader reader) {
        return IOUtils.lineIterator(reader);
    }

    public static int read(InputStream input, byte[] buffer) throws IOException {
        return IOUtils.read(input, buffer);
    }

    public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        return IOUtils.read(input, buffer, offset, length);
    }

    public static int read(ReadableByteChannel input, ByteBuffer buffer) throws IOException {
        return IOUtils.read(input, buffer);
    }

    public static int read(Reader reader, char[] buffer) throws IOException {
        return IOUtils.read(reader, buffer);
    }

    public static int read(Reader reader, char[] buffer, int offset, int length) throws IOException {
        return IOUtils.read(reader, buffer, offset, length);
    }

    public static void readFully(InputStream input, byte[] buffer) throws IOException {
        IOUtils.readFully(input, buffer);
    }

    public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
        IOUtils.readFully(input, buffer, offset, length);
    }

    public static byte[] readFully(InputStream input, int length) throws IOException {
        return IOUtils.readFully(input, length);
    }

    public static void readFully(ReadableByteChannel input, ByteBuffer buffer) throws IOException {
        IOUtils.readFully(input, buffer);
    }

    public static void readFully(Reader reader, char[] buffer) throws IOException {
        IOUtils.readFully(reader, buffer);
    }

    public static void readFully(Reader reader, char[] buffer, int offset, int length) throws IOException {
        IOUtils.readFully(reader, buffer, offset, length);
    }

    @Deprecated
    public static List<String> readLines(InputStream input) throws IOException {
        return IOUtils.readLines(input);
    }

    public static List<String> readLines(InputStream input, Charset charset) throws IOException {
        return IOUtils.readLines(input, charset);
    }

    public static List<String> readLines(InputStream input, String charsetName) throws IOException {
        return IOUtils.readLines(input, charsetName);
    }

    public static List<String> readLines(Reader reader) throws IOException {
        return IOUtils.readLines(reader);
    }

    public static byte[] resourceToByteArray(String name) throws IOException {
        return IOUtils.resourceToByteArray(name);
    }

    public static byte[] resourceToByteArray(String name, ClassLoader classLoader) throws IOException {
        return IOUtils.resourceToByteArray(name, classLoader);
    }

    public static String resourceToString(String name, Charset charset) throws IOException {
        return IOUtils.resourceToString(name, charset);
    }

    public static String resourceToString(String name, Charset charset, ClassLoader classLoader) throws IOException {
        return IOUtils.resourceToString(name, charset, classLoader);
    }

    public static URL resourceToURL(String name) throws IOException {
        return IOUtils.resourceToURL(name);
    }

    public static URL resourceToURL(String name, ClassLoader classLoader) throws IOException {
        return IOUtils.resourceToURL(name, classLoader);
    }

    public static long skip(InputStream input, long toSkip) throws IOException {
        return IOUtils.skip(input, toSkip);
    }

    public static long skip(ReadableByteChannel input, long toSkip) throws IOException {
        return IOUtils.skip(input, toSkip);
    }

    public static long skip(Reader reader, long toSkip) throws IOException {
        return IOUtils.skip(reader, toSkip);
    }

    public static void skipFully(InputStream input, long toSkip) throws IOException {
        IOUtils.skipFully(input, toSkip);
    }

    public static void skipFully(ReadableByteChannel input, long toSkip) throws IOException {
        IOUtils.skipFully(input, toSkip);
    }

    public static void skipFully(Reader reader, long toSkip) throws IOException {
        IOUtils.skipFully(reader, toSkip);
    }

    public static InputStream toBufferedInputStream(InputStream input) throws IOException {
        return IOUtils.toBufferedInputStream(input);
    }

    public static InputStream toBufferedInputStream(InputStream input, int size) throws IOException {
        return IOUtils.toBufferedInputStream(input, size);
    }

    public static BufferedReader toBufferedReader(Reader reader) {
        return IOUtils.toBufferedReader(reader);
    }

    public static BufferedReader toBufferedReader(Reader reader, int size) {
        return IOUtils.toBufferedReader(reader, size);
    }

    public static void copy(Reader input, Writer output, int bufferSize) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output, bufferSize);
    }

    @Deprecated
    public static void copy(InputStream input, Writer output) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output);
    }

    public static void copy(InputStream input, Writer writer, Charset inputCharset) throws IOException {
        IOUtils.copy(input, writer, inputCharset);
    }

    public static void copy(InputStream input, Writer output, int bufferSize) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output, bufferSize);
    }

    public static void copy(InputStream input, Writer output, String encoding) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output, encoding);
    }

    public static long copy(Reader reader, Appendable output) throws IOException {
        return IOUtils.copy(reader, output);
    }

    public static long copy(Reader reader, Appendable output, CharBuffer buffer) throws IOException {
        return IOUtils.copy(reader, output, buffer);
    }

    public static void copy(InputStream input, Writer output, String encoding, int bufferSize) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output, encoding, bufferSize);
    }

    @Deprecated
    public static String toString(InputStream input) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toString(input);
    }

    public static String toString(InputStream input, Charset charset) throws IOException {
        return IOUtils.toString(input, charset);
    }

    public static String toString(InputStream input, int bufferSize) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toString(input, bufferSize);
    }

    public static String toString(InputStream input, String encoding) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toString(input, encoding);
    }

    public static String toString(InputStream input, String encoding, int bufferSize) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toString(input, encoding, bufferSize);
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toByteArray(input);
    }

    public static byte[] toByteArray(InputStream input, int bufferSize) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toByteArray(input, bufferSize);
    }

    public static byte[] toByteArray(InputStream input, long size) throws IOException {
        return IOUtils.toByteArray(input, size);
    }

    @Deprecated
    public static void copy(Reader input, OutputStream output) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output);
    }

    public static void copy(Reader reader, OutputStream output, Charset outputCharset) throws IOException {
        IOUtils.copy(reader, output, outputCharset);
    }

    public static void copy(Reader reader, OutputStream output, String outputCharsetName) throws IOException {
        IOUtils.copy(reader, output, outputCharsetName);
    }

    public static void copy(Reader input, OutputStream output, int bufferSize) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output, bufferSize);
    }

    public static String toString(Reader input) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toString(input);
    }

    @Deprecated
    public static String toString(URI uri) throws IOException {
        return IOUtils.toString(uri);
    }

    public static String toString(URI uri, Charset encoding) throws IOException {
        return IOUtils.toString(uri, encoding);
    }

    public static String toString(URI uri, String charsetName) throws IOException {
        return IOUtils.toString(uri, charsetName);
    }

    @Deprecated
    public static String toString(URL url) throws IOException {
        return IOUtils.toString(url);
    }

    public static String toString(URL url, Charset encoding) throws IOException {
        return IOUtils.toString(url, encoding);
    }

    public static String toString(URL url, String charsetName) throws IOException {
        return IOUtils.toString(url, charsetName);
    }

    public static void write(byte[] data, OutputStream output) throws IOException {
        IOUtils.write(data, output);
    }

    @Deprecated
    public static void write(byte[] data, Writer writer) throws IOException {
        IOUtils.write(data, writer);
    }

    public static void write(byte[] data, Writer writer, Charset charset) throws IOException {
        IOUtils.write(data, writer, charset);
    }

    public static void write(byte[] data, Writer writer, String charsetName) throws IOException {
        IOUtils.write(data, writer, charsetName);
    }

    @Deprecated
    public static void write(char[] data, OutputStream output) throws IOException {
        IOUtils.write(data, output);
    }

    public static void write(char[] data, OutputStream output, Charset charset) throws IOException {
        IOUtils.write(data, output, charset);
    }

    public static void write(char[] data, OutputStream output, String charsetName) throws IOException {
        IOUtils.write(data, output, charsetName);
    }

    public static void write(char[] data, Writer writer) throws IOException {
        IOUtils.write(data, writer);
    }

    @Deprecated
    public static void write(CharSequence data, OutputStream output) throws IOException {
        IOUtils.write(data, output);
    }

    public static void write(CharSequence data, OutputStream output, Charset charset) throws IOException {
        IOUtils.write(data, output, charset);
    }

    public static void write(CharSequence data, OutputStream output, String charsetName) throws IOException {
        IOUtils.write(data, output, charsetName);
    }

    public static void write(CharSequence data, Writer writer) throws IOException {
        IOUtils.write(data, writer);
    }

    @Deprecated
    public static void write(String data, OutputStream output) throws IOException {
        IOUtils.write(data, output);
    }

    public static void write(String data, OutputStream output, Charset charset) throws IOException {
        IOUtils.write(data, output, charset);
    }

    public static void write(String data, OutputStream output, String charsetName) throws IOException {
        IOUtils.write(data, output, charsetName);
    }

    public static void write(String data, Writer writer) throws IOException {
        IOUtils.write(data, writer);
    }

    @Deprecated
    public static void write(StringBuffer data, OutputStream output) throws IOException {
        IOUtils.write(data, output);
    }

    @Deprecated
    public static void write(StringBuffer data, OutputStream output, String charsetName) throws IOException {
        IOUtils.write(data, output, charsetName);
    }

    @Deprecated
    public static void write(StringBuffer data, Writer writer) throws IOException {
        IOUtils.write(data, writer);
    }

    public static void writeChunked(byte[] data, OutputStream output) throws IOException {
        IOUtils.writeChunked(data, output);
    }

    public static void writeChunked(char[] data, Writer writer) throws IOException {
        IOUtils.writeChunked(data, writer);
    }

    @Deprecated
    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output) throws IOException {
        IOUtils.writeLines(lines, lineEnding, output);
    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, Charset charset) throws IOException {
        IOUtils.writeLines(lines, lineEnding, output, charset);
    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, String charsetName) throws IOException {
        IOUtils.writeLines(lines, lineEnding, output, charsetName);
    }

    public static void writeLines(Collection<?> lines, String lineEnding, Writer writer) throws IOException {
        IOUtils.writeLines(lines, lineEnding, writer);
    }

    public static Writer writer(Appendable appendable) {
        return IOUtils.writer(appendable);
    }

    public static String toString(Reader input, int bufferSize) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toString(input, bufferSize);
    }

    @Deprecated
    public static byte[] toByteArray(Reader input) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toByteArray(input);
    }

    public static byte[] toByteArray(Reader reader, Charset charset) throws IOException {
        return IOUtils.toByteArray(reader, charset);
    }

    public static byte[] toByteArray(Reader reader, String charsetName) throws IOException {
        return IOUtils.toByteArray(reader, charsetName);
    }

    public static byte[] toByteArray(Reader input, int bufferSize) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toByteArray(input, bufferSize);
    }

    public static void copy(String input, OutputStream output) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output);
    }

    public static void copy(String input, OutputStream output, int bufferSize) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output, bufferSize);
    }

    public static void copy(String input, Writer output) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output);
    }

    public static void bufferedCopy(InputStream input, OutputStream output) throws IOException {
        org.codehaus.plexus.util.IOUtil.bufferedCopy(input, output);
    }

    @Deprecated
    public static byte[] toByteArray(String input) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toByteArray(input);
    }

    public static byte[] toByteArray(URI uri) throws IOException {
        return IOUtils.toByteArray(uri);
    }

    public static byte[] toByteArray(URL url) throws IOException {
        return IOUtils.toByteArray(url);
    }

    public static byte[] toByteArray(URLConnection urlConn) throws IOException {
        return IOUtils.toByteArray(urlConn);
    }

    @Deprecated
    public static char[] toCharArray(InputStream inputStream) throws IOException {
        return IOUtils.toCharArray(inputStream);
    }

    public static char[] toCharArray(InputStream inputStream, Charset charset) throws IOException {
        return IOUtils.toCharArray(inputStream, charset);
    }

    public static char[] toCharArray(InputStream inputStream, String charsetName) throws IOException {
        return IOUtils.toCharArray(inputStream, charsetName);
    }

    public static char[] toCharArray(Reader reader) throws IOException {
        return IOUtils.toCharArray(reader);
    }

    @Deprecated
    public static InputStream toInputStream(CharSequence input) {
        return IOUtils.toInputStream(input);
    }

    public static InputStream toInputStream(CharSequence input, Charset charset) {
        return IOUtils.toInputStream(input, charset);
    }

    public static InputStream toInputStream(CharSequence input, String charsetName) {
        return IOUtils.toInputStream(input, charsetName);
    }

    @Deprecated
    public static InputStream toInputStream(String input) {
        return IOUtils.toInputStream(input);
    }

    public static InputStream toInputStream(String input, Charset charset) {
        return IOUtils.toInputStream(input, charset);
    }

    public static InputStream toInputStream(String input, String charsetName) {
        return IOUtils.toInputStream(input, charsetName);
    }

    public static byte[] toByteArray(String input, int bufferSize) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toByteArray(input, bufferSize);
    }

    public static void copy(byte[] input, Writer output) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output);
    }

    public static void copy(byte[] input, Writer output, int bufferSize) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output, bufferSize);
    }

    public static void copy(byte[] input, Writer output, String encoding) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output, encoding);
    }

    public static void copy(byte[] input, Writer output, String encoding, int bufferSize) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output, encoding, bufferSize);
    }

    @Deprecated
    public static String toString(byte[] input) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toString(input);
    }

    public static String toString(byte[] input, int bufferSize) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toString(input, bufferSize);
    }

    public static String toString(byte[] input, String encoding) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toString(input, encoding);
    }

    public static String toString(byte[] input, String encoding, int bufferSize) throws IOException {
        return org.codehaus.plexus.util.IOUtil.toString(input, encoding, bufferSize);
    }

    public static void copy(byte[] input, OutputStream output) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output);
    }

    public static void copy(byte[] input, OutputStream output, int bufferSize) throws IOException {
        org.codehaus.plexus.util.IOUtil.copy(input, output, bufferSize);
    }

    public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
        return org.codehaus.plexus.util.IOUtil.contentEquals(input1, input2);
    }

    public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
        return IOUtils.contentEquals(input1, input2);
    }

    public static boolean contentEqualsIgnoreEOL(Reader reader1, Reader reader2) throws IOException {
        return IOUtils.contentEqualsIgnoreEOL(reader1, reader2);
    }

    public static void close(InputStream inputStream) {
        org.codehaus.plexus.util.IOUtil.close(inputStream);
    }

    public static void close(Channel channel) {
        org.codehaus.plexus.util.IOUtil.close(channel);
    }

    public static void close(OutputStream outputStream) {
        org.codehaus.plexus.util.IOUtil.close(outputStream);
    }

    public static void close(Reader reader) {
        org.codehaus.plexus.util.IOUtil.close(reader);
    }

    public static void close(OutputStreamWriter writer)
    {
        org.codehaus.plexus.util.IOUtil.close(writer);
    }

    public static String toHexDumo(byte[] _input)
    {
        StringBuilder _sb = new StringBuilder();
        int _i = 0;
        for(_i = 0; _i<_input.length; _i+=16)
        {
            _sb.append(String.format("%04X: ", _i));
            for(int _j = 0; _j<16; _j++)
            {
                int _o = _i+_j;
                if(_o<_input.length)
                {
                    _sb.append(String.format("%02X ", _input[_o]));
                }
                else
                {
                    _sb.append("-- ");
                }
            }
            for(int _j = 0; _j<16; _j++)
            {
                int _o = _i+_j;
                if(_o<_input.length)
                {
                    if(((_input[_o] & 0xff)<0x20)
                    || ((_input[_o] & 0xff)>0x7e))
                    {
                        _sb.append(".");
                    }
                    else
                    {
                        _sb.append((char)(_input[_o] & 0xff));
                    }
                }
                else
                {
                    _sb.append(" ");
                }
            }
            _sb.append(" \n");
        }
        return _sb.toString();
    }

}
