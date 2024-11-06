/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.terefang.jmelange.apache.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.github.terefang.jmelange.apache.io.FileUtils;
import com.github.terefang.jmelange.apache.io.IOCase;
import com.github.terefang.jmelange.apache.io.filefilter.*;
import com.github.terefang.jmelange.apache.io.filefilter.AgeFileFilter;
import com.github.terefang.jmelange.apache.io.filefilter.AndFileFilter;
import com.github.terefang.jmelange.apache.io.filefilter.FalseFileFilter;
import com.github.terefang.jmelange.apache.io.filefilter.FileFileFilter;
import com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter;
import com.github.terefang.jmelange.apache.io.filefilter.NameFileFilter;
import com.github.terefang.jmelange.apache.io.filefilter.NotFileFilter;
import com.github.terefang.jmelange.apache.io.filefilter.OrFileFilter;
import com.github.terefang.jmelange.apache.io.filefilter.SizeFileFilter;
import com.github.terefang.jmelange.apache.io.filefilter.TrueFileFilter;

/**
 * Useful utilities for working with file filters. It provides access to most
 * file filter implementations in this package so you don't have to import
 * every class you use.
 *
 * @since 1.0
 */
public class FileFilterUtils {

    /* Constructed on demand and then cached */
    private static final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter CVS_FILTER = notFileFilter(
            and(directoryFileFilter(), nameFileFilter("CVS")));

    /* Constructed on demand and then cached */
    private static final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter SVN_FILTER = notFileFilter(
            and(directoryFileFilter(), nameFileFilter(".svn")));

    /**
     * Returns a filter that returns true if the file was last modified before
     * or at the specified cutoff date.
     *
     * @param cutoffDate  the time threshold
     * @return an appropriately configured age file filter
     * @see com.github.terefang.jmelange.apache.io.filefilter.AgeFileFilter
     * @since 1.2
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter ageFileFilter(final Date cutoffDate) {
        return new com.github.terefang.jmelange.apache.io.filefilter.AgeFileFilter(cutoffDate);
    }

    /**
     * Returns a filter that filters files based on a cutoff date.
     *
     * @param cutoffDate  the time threshold
     * @param acceptOlder  if true, older files get accepted, if false, newer
     * @return an appropriately configured age file filter
     * @see com.github.terefang.jmelange.apache.io.filefilter.AgeFileFilter
     * @since 1.2
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter ageFileFilter(final Date cutoffDate, final boolean acceptOlder) {
        return new com.github.terefang.jmelange.apache.io.filefilter.AgeFileFilter(cutoffDate, acceptOlder);
    }

    /**
     * Returns a filter that returns true if the file was last modified before
     * or at the same time as the specified reference file.
     *
     * @param cutoffReference  the file whose last modification
     *        time is used as the threshold age of the files
     * @return an appropriately configured age file filter
     * @see com.github.terefang.jmelange.apache.io.filefilter.AgeFileFilter
     * @since 1.2
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter ageFileFilter(final File cutoffReference) {
        return new com.github.terefang.jmelange.apache.io.filefilter.AgeFileFilter(cutoffReference);
    }

    /**
     * Returns a filter that filters files based on a cutoff reference file.
     *
     * @param cutoffReference  the file whose last modification
     *        time is used as the threshold age of the files
     * @param acceptOlder  if true, older files get accepted, if false, newer
     * @return an appropriately configured age file filter
     * @see com.github.terefang.jmelange.apache.io.filefilter.AgeFileFilter
     * @since 1.2
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter ageFileFilter(final File cutoffReference, final boolean acceptOlder) {
        return new com.github.terefang.jmelange.apache.io.filefilter.AgeFileFilter(cutoffReference, acceptOlder);
    }

    /**
     * Returns a filter that returns true if the file was last modified before
     * or at the specified cutoff time.
     *
     * @param cutoffMillis  the time threshold
     * @return an appropriately configured age file filter
     * @see com.github.terefang.jmelange.apache.io.filefilter.AgeFileFilter
     * @since 1.2
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter ageFileFilter(final long cutoffMillis) {
        return new com.github.terefang.jmelange.apache.io.filefilter.AgeFileFilter(cutoffMillis);
    }

    /**
     * Returns a filter that filters files based on a cutoff time.
     *
     * @param cutoffMillis  the time threshold
     * @param acceptOlder  if true, older files get accepted, if false, newer
     * @return an appropriately configured age file filter
     * @see com.github.terefang.jmelange.apache.io.filefilter.AgeFileFilter
     * @since 1.2
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter ageFileFilter(final long cutoffMillis, final boolean acceptOlder) {
        return new AgeFileFilter(cutoffMillis, acceptOlder);
    }

    /**
     * Returns a filter that ANDs the specified filters.
     *
     * @param filters the IOFileFilters that will be ANDed together.
     * @return a filter that ANDs the specified filters
     *
     * @throws IllegalArgumentException if the filters are null or contain a
     *         null value.
     * @see com.github.terefang.jmelange.apache.io.filefilter.AndFileFilter
     * @since 2.0
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter and(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter... filters) {
        return new com.github.terefang.jmelange.apache.io.filefilter.AndFileFilter(toList(filters));
    }

    /**
     * Returns a filter that ANDs the two specified filters.
     *
     * @param filter1  the first filter
     * @param filter2  the second filter
     * @return a filter that ANDs the two specified filters
     * @see #and(com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter...)
     * @see com.github.terefang.jmelange.apache.io.filefilter.AndFileFilter
     * @deprecated use {@link #and(com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter...)}
     */
    @Deprecated
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter andFileFilter(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter1, final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter2) {
        return new AndFileFilter(filter1, filter2);
    }

    /**
     * Returns an {@link com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter} that wraps the
     * {@link FileFilter} instance.
     *
     * @param filter  the filter to be wrapped
     * @return a new filter that implements IOFileFilter
     * @see DelegateFileFilter
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter asFileFilter(final FileFilter filter) {
        return new DelegateFileFilter(filter);
    }

    /**
     * Returns an {@link com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter} that wraps the
     * {@link FilenameFilter} instance.
     *
     * @param filter  the filter to be wrapped
     * @return a new filter that implements IOFileFilter
     * @see DelegateFileFilter
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter asFileFilter(final FilenameFilter filter) {
        return new DelegateFileFilter(filter);
    }

    /**
     * Returns a filter that checks if the file is a directory.
     *
     * @return file filter that accepts only directories and not files
     * @see DirectoryFileFilter#DIRECTORY
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter directoryFileFilter() {
        return DirectoryFileFilter.DIRECTORY;
    }

    /**
     * Returns a filter that always returns false.
     *
     * @return a false filter
     * @see com.github.terefang.jmelange.apache.io.filefilter.FalseFileFilter#FALSE
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter falseFileFilter() {
        return FalseFileFilter.FALSE;
    }

    /**
     * Returns a filter that checks if the file is a file (and not a directory).
     *
     * @return file filter that accepts only files and not directories
     * @see com.github.terefang.jmelange.apache.io.filefilter.FileFileFilter#INSTANCE
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter fileFileFilter() {
        return com.github.terefang.jmelange.apache.io.filefilter.FileFileFilter.INSTANCE;
    }

    /**
     * <p>
     * Applies an {@link com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter} to the provided {@link File}
     * objects. The resulting array is a subset of the original file list that
     * matches the provided filter.
     * </p>
     *
     * <pre>
     * Set&lt;File&gt; allFiles = ...
     * Set&lt;File&gt; javaFiles = FileFilterUtils.filterSet(allFiles,
     *     FileFilterUtils.suffixFileFilter(".java"));
     * </pre>
     * @param filter the filter to apply to the set of files.
     * @param files the array of files to apply the filter to.
     *
     * @return a subset of {@code files} that is accepted by the
     *         file filter.
     * @throws NullPointerException if the filter is {@code null}
     *         or {@code files} contains a {@code null} value.
     *
     * @since 2.0
     */
    public static File[] filter(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter, final File... files) {
        Objects.requireNonNull(filter, "filter");
        if (files == null) {
            return FileUtils.EMPTY_FILE_ARRAY;
        }
        return filterFiles(filter, Stream.of(files), Collectors.toList()).toArray(FileUtils.EMPTY_FILE_ARRAY);
    }

    /**
     * <p>
     * Applies an {@link com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter} to the provided {@link File}
     * objects. The resulting array is a subset of the original file list that
     * matches the provided filter.
     * </p>
     *
     * <p>
     * The {@link Set} returned by this method is not guaranteed to be thread safe.
     * </p>
     *
     * <pre>
     * Set&lt;File&gt; allFiles = ...
     * Set&lt;File&gt; javaFiles = FileFilterUtils.filterSet(allFiles,
     *     FileFilterUtils.suffixFileFilter(".java"));
     * </pre>
     * @param filter the filter to apply to the set of files.
     * @param files the array of files to apply the filter to.
     *
     * @return a subset of {@code files} that is accepted by the
     *         file filter.
     * @throws IllegalArgumentException if the filter is {@code null}
     *         or {@code files} contains a {@code null} value.
     *
     * @since 2.0
     */
    public static File[] filter(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter, final Iterable<File> files) {
        return filterList(filter, files).toArray(FileUtils.EMPTY_FILE_ARRAY);
    }

    /**
     * <p>
     * Applies an {@link com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter} to the provided {@link File} stream and collects the accepted files.
     * </p>
     *
     * @param filter the filter to apply to the stream of files.
     * @param stream the stream of files on which to apply the filter.
     * @param collector how to collect the end result.
     *
     * @param <R> the return type.
     * @param <A> the mutable accumulation type of the reduction operation (often hidden as an implementation detail)
     * @return a subset of files from the stream that is accepted by the filter.
     * @throws NullPointerException if the filter is {@code null}.
     */
    private static <R, A> R filterFiles(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter, final Stream<File> stream,
                                        final Collector<? super File, A, R> collector) {
        Objects.requireNonNull(filter, "filter");
        Objects.requireNonNull(collector, "collector");
        if (stream == null) {
            return Stream.<File>empty().collect(collector);
        }
        return stream.filter(filter::accept).collect(collector);
    }

    /**
     * <p>
     * Applies an {@link com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter} to the provided {@link File}
     * objects. The resulting list is a subset of the original files that
     * matches the provided filter.
     * </p>
     *
     * <p>
     * The {@link List} returned by this method is not guaranteed to be thread safe.
     * </p>
     *
     * <pre>
     * List&lt;File&gt; filesAndDirectories = ...
     * List&lt;File&gt; directories = FileFilterUtils.filterList(filesAndDirectories,
     *     FileFilterUtils.directoryFileFilter());
     * </pre>
     * @param filter the filter to apply to each files in the list.
     * @param files the collection of files to apply the filter to.
     *
     * @return a subset of {@code files} that is accepted by the
     *         file filter.
     * @throws IllegalArgumentException if the filter is {@code null}
     *         or {@code files} contains a {@code null} value.
     * @since 2.0
     */
    public static List<File> filterList(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter, final File... files) {
        return Arrays.asList(filter(filter, files));
    }

    /**
     * <p>
     * Applies an {@link com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter} to the provided {@link File}
     * objects. The resulting list is a subset of the original files that
     * matches the provided filter.
     * </p>
     *
     * <p>
     * The {@link List} returned by this method is not guaranteed to be thread safe.
     * </p>
     *
     * <pre>
     * List&lt;File&gt; filesAndDirectories = ...
     * List&lt;File&gt; directories = FileFilterUtils.filterList(filesAndDirectories,
     *     FileFilterUtils.directoryFileFilter());
     * </pre>
     * @param filter the filter to apply to each files in the list.
     * @param files the collection of files to apply the filter to.
     *
     * @return a subset of {@code files} that is accepted by the
     *         file filter.
     * @throws IllegalArgumentException if the filter is {@code null}
     * @since 2.0
     */
    public static List<File> filterList(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter, final Iterable<File> files) {
        if (files == null) {
            return Collections.emptyList();
        }
        return filterFiles(filter, StreamSupport.stream(files.spliterator(), false), Collectors.toList());
    }

    /**
     * <p>
     * Applies an {@link com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter} to the provided {@link File}
     * objects. The resulting set is a subset of the original file list that
     * matches the provided filter.
     * </p>
     *
     * <p>
     * The {@link Set} returned by this method is not guaranteed to be thread safe.
     * </p>
     *
     * <pre>
     * Set&lt;File&gt; allFiles = ...
     * Set&lt;File&gt; javaFiles = FileFilterUtils.filterSet(allFiles,
     *     FileFilterUtils.suffixFileFilter(".java"));
     * </pre>
     * @param filter the filter to apply to the set of files.
     * @param files the collection of files to apply the filter to.
     *
     * @return a subset of {@code files} that is accepted by the
     *         file filter.
     * @throws IllegalArgumentException if the filter is {@code null}
     *         or {@code files} contains a {@code null} value.
     *
     * @since 2.0
     */
    public static Set<File> filterSet(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter, final File... files) {
        return new HashSet<>(Arrays.asList(filter(filter, files)));
    }

    /**
     * <p>
     * Applies an {@link com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter} to the provided {@link File}
     * objects. The resulting set is a subset of the original file list that
     * matches the provided filter.
     * </p>
     *
     * <p>
     * The {@link Set} returned by this method is not guaranteed to be thread safe.
     * </p>
     *
     * <pre>
     * Set&lt;File&gt; allFiles = ...
     * Set&lt;File&gt; javaFiles = FileFilterUtils.filterSet(allFiles,
     *     FileFilterUtils.suffixFileFilter(".java"));
     * </pre>
     * @param filter the filter to apply to the set of files.
     * @param files the collection of files to apply the filter to.
     *
     * @return a subset of {@code files} that is accepted by the
     *         file filter.
     * @throws IllegalArgumentException if the filter is {@code null}
     *
     * @since 2.0
     */
    public static Set<File> filterSet(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter, final Iterable<File> files) {
        if (files == null) {
            return Collections.emptySet();
        }
        return filterFiles(filter, StreamSupport.stream(files.spliterator(), false), Collectors.toSet());
    }

    /**
     * Returns a filter that accepts files that begin with the provided magic
     * number.
     *
     * @param magicNumber the magic number (byte sequence) to match at the
     *        beginning of each file.
     *
     * @return an IOFileFilter that accepts files beginning with the provided
     *         magic number.
     *
     * @throws IllegalArgumentException if {@code magicNumber} is
     *         {@code null} or is of length zero.
     * @see MagicNumberFileFilter
     * @since 2.0
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter magicNumberFileFilter(final byte[] magicNumber) {
        return new MagicNumberFileFilter(magicNumber);
    }

    /**
     * Returns a filter that accepts files that contains the provided magic
     * number at a specified offset within the file.
     *
     * @param magicNumber the magic number (byte sequence) to match at the
     *        provided offset in each file.
     * @param offset the offset within the files to look for the magic number.
     *
     * @return an IOFileFilter that accepts files containing the magic number
     *         at the specified offset.
     *
     * @throws IllegalArgumentException if {@code magicNumber} is
     *         {@code null}, or contains no bytes, or {@code offset}
     *         is a negative number.
     * @see MagicNumberFileFilter
     * @since 2.0
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter magicNumberFileFilter(final byte[] magicNumber, final long offset) {
        return new MagicNumberFileFilter(magicNumber, offset);
    }

    /**
     * Returns a filter that accepts files that begin with the provided magic
     * number.
     *
     * @param magicNumber the magic number (byte sequence) to match at the
     *        beginning of each file.
     *
     * @return an IOFileFilter that accepts files beginning with the provided
     *         magic number.
     *
     * @throws IllegalArgumentException if {@code magicNumber} is
     *         {@code null} or the empty String.
     * @see MagicNumberFileFilter
     * @since 2.0
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter magicNumberFileFilter(final String magicNumber) {
        return new MagicNumberFileFilter(magicNumber);
    }

    /**
     * Returns a filter that accepts files that contains the provided magic
     * number at a specified offset within the file.
     *
     * @param magicNumber the magic number (byte sequence) to match at the
     *        provided offset in each file.
     * @param offset the offset within the files to look for the magic number.
     *
     * @return an IOFileFilter that accepts files containing the magic number
     *         at the specified offset.
     *
     * @throws IllegalArgumentException if {@code magicNumber} is
     *         {@code null} or the empty String, or if offset is a
     *         negative number.
     * @see MagicNumberFileFilter
     * @since 2.0
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter magicNumberFileFilter(final String magicNumber, final long offset) {
        return new MagicNumberFileFilter(magicNumber, offset);
    }

    /**
     * Decorates a filter to make it ignore CVS directories.
     * Passing in {@code null} will return a filter that accepts everything
     * except CVS directories.
     *
     * @param filter  the filter to decorate, null means an unrestricted filter
     * @return the decorated filter, never null
     * @since 1.1 (method existed but had a bug in 1.0)
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter makeCVSAware(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter) {
        return filter == null ? CVS_FILTER : and(filter, CVS_FILTER);
    }

    /**
     * Decorates a filter so that it only applies to directories and not to files.
     *
     * @param filter  the filter to decorate, null means an unrestricted filter
     * @return the decorated filter, never null
     * @see DirectoryFileFilter#DIRECTORY
     * @since 1.3
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter makeDirectoryOnly(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter) {
        if (filter == null) {
            return DirectoryFileFilter.DIRECTORY;
        }
        return DirectoryFileFilter.DIRECTORY.and(filter);
    }

    /**
     * Decorates a filter so that it only applies to files and not to directories.
     *
     * @param filter  the filter to decorate, null means an unrestricted filter
     * @return the decorated filter, never null
     * @see com.github.terefang.jmelange.apache.io.filefilter.FileFileFilter#INSTANCE
     * @since 1.3
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter makeFileOnly(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter) {
        if (filter == null) {
            return com.github.terefang.jmelange.apache.io.filefilter.FileFileFilter.INSTANCE;
        }
        return FileFileFilter.INSTANCE.and(filter);
    }

    /**
     * Decorates a filter to make it ignore SVN directories.
     * Passing in {@code null} will return a filter that accepts everything
     * except SVN directories.
     *
     * @param filter  the filter to decorate, null means an unrestricted filter
     * @return the decorated filter, never null
     * @since 1.1
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter makeSVNAware(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter) {
        return filter == null ? SVN_FILTER : and(filter, SVN_FILTER);
    }

    /**
     * Returns a filter that returns true if the file name matches the specified text.
     *
     * @param name  the file name
     * @return a name checking filter
     * @see com.github.terefang.jmelange.apache.io.filefilter.NameFileFilter
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter nameFileFilter(final String name) {
        return new com.github.terefang.jmelange.apache.io.filefilter.NameFileFilter(name);
    }

    /**
     * Returns a filter that returns true if the file name matches the specified text.
     *
     * @param name  the file name
     * @param ioCase  how to handle case sensitivity, null means case-sensitive
     * @return a name checking filter
     * @see com.github.terefang.jmelange.apache.io.filefilter.NameFileFilter
     * @since 2.0
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter nameFileFilter(final String name, final IOCase ioCase) {
        return new NameFileFilter(name, ioCase);
    }

    /**
     * Returns a filter that NOTs the specified filter.
     *
     * @param filter  the filter to invert
     * @return a filter that NOTs the specified filter
     * @see NotFileFilter
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter notFileFilter(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter) {
        return filter.negate();
    }

    /**
     * Returns a filter that ORs the specified filters.
     *
     * @param filters the IOFileFilters that will be ORed together.
     * @return a filter that ORs the specified filters
     *
     * @throws IllegalArgumentException if the filters are null or contain a
     *         null value.
     * @see com.github.terefang.jmelange.apache.io.filefilter.OrFileFilter
     * @since 2.0
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter or(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter... filters) {
        return new com.github.terefang.jmelange.apache.io.filefilter.OrFileFilter(toList(filters));
    }

    /**
     * Returns a filter that ORs the two specified filters.
     *
     * @param filter1  the first filter
     * @param filter2  the second filter
     * @return a filter that ORs the two specified filters
     * @see #or(com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter...)
     * @see com.github.terefang.jmelange.apache.io.filefilter.OrFileFilter
     * @deprecated use {@link #or(com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter...)}
     */
    @Deprecated
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter orFileFilter(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter1, final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter filter2) {
        return new OrFileFilter(filter1, filter2);
    }

    /**
     * Returns a filter that returns true if the file name starts with the specified text.
     *
     * @param prefix  the file name prefix
     * @return a prefix checking filter
     * @see PrefixFileFilter
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter prefixFileFilter(final String prefix) {
        return new PrefixFileFilter(prefix);
    }

    /**
     * Returns a filter that returns true if the file name starts with the specified text.
     *
     * @param prefix  the file name prefix
     * @param ioCase  how to handle case sensitivity, null means case-sensitive
     * @return a prefix checking filter
     * @see PrefixFileFilter
     * @since 2.0
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter prefixFileFilter(final String prefix, final IOCase ioCase) {
        return new PrefixFileFilter(prefix, ioCase);
    }

    /**
     * Returns a filter that returns true if the file is bigger than a certain size.
     *
     * @param threshold  the file size threshold
     * @return an appropriately configured SizeFileFilter
     * @see com.github.terefang.jmelange.apache.io.filefilter.SizeFileFilter
     * @since 1.2
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter sizeFileFilter(final long threshold) {
        return new com.github.terefang.jmelange.apache.io.filefilter.SizeFileFilter(threshold);
    }

    /**
     * Returns a filter that filters based on file size.
     *
     * @param threshold  the file size threshold
     * @param acceptLarger  if true, larger files get accepted, if false, smaller
     * @return an appropriately configured SizeFileFilter
     * @see com.github.terefang.jmelange.apache.io.filefilter.SizeFileFilter
     * @since 1.2
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter sizeFileFilter(final long threshold, final boolean acceptLarger) {
        return new com.github.terefang.jmelange.apache.io.filefilter.SizeFileFilter(threshold, acceptLarger);
    }

    /**
     * Returns a filter that accepts files whose size is &gt;= minimum size
     * and &lt;= maximum size.
     *
     * @param minSizeInclusive the minimum file size (inclusive)
     * @param maxSizeInclusive the maximum file size (inclusive)
     * @return an appropriately configured IOFileFilter
     * @see com.github.terefang.jmelange.apache.io.filefilter.SizeFileFilter
     * @since 1.3
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter sizeRangeFileFilter(final long minSizeInclusive, final long maxSizeInclusive ) {
        final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter minimumFilter = new com.github.terefang.jmelange.apache.io.filefilter.SizeFileFilter(minSizeInclusive, true);
        final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter maximumFilter = new SizeFileFilter(maxSizeInclusive + 1L, false);
        return minimumFilter.and(maximumFilter);
    }

    /**
     * Returns a filter that returns true if the file name ends with the specified text.
     *
     * @param suffix  the file name suffix
     * @return a suffix checking filter
     * @see SuffixFileFilter
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter suffixFileFilter(final String suffix) {
        return new SuffixFileFilter(suffix);
    }

    /**
     * Returns a filter that returns true if the file name ends with the specified text.
     *
     * @param suffix  the file name suffix
     * @param ioCase  how to handle case sensitivity, null means case-sensitive
     * @return a suffix checking filter
     * @see SuffixFileFilter
     * @since 2.0
     */
    public static com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter suffixFileFilter(final String suffix, final IOCase ioCase) {
        return new SuffixFileFilter(suffix, ioCase);
    }

    /**
     * Create a List of file filters.
     *
     * @param filters The file filters
     * @return The list of file filters
     * @throws NullPointerException if the filters are null or contain a
     *         null value.
     * @since 2.0
     */
    public static List<com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter> toList(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter... filters) {
        return Stream.of(Objects.requireNonNull(filters, "filters")).map(Objects::requireNonNull).collect(Collectors.toList());
    }

    /**
     * Returns a filter that always returns true.
     *
     * @return a true filter
     * @see com.github.terefang.jmelange.apache.io.filefilter.TrueFileFilter#TRUE
     */
    public static IOFileFilter trueFileFilter() {
        return TrueFileFilter.TRUE;
    }

    /**
     * FileFilterUtils is not normally instantiated.
     */
    public FileFilterUtils() {
    }

}
