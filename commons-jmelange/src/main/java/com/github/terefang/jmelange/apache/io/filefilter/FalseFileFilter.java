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

import com.github.terefang.jmelange.apache.io.filefilter.FileFilterUtils;
import com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter;
import com.github.terefang.jmelange.apache.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * A file filter that always returns false.
 * <h2>Deprecating Serialization</h2>
 * <p>
 * <em>Serialization is deprecated and will be removed in 3.0.</em>
 * </p>
 *
 * @since 1.0
 * @see FileFilterUtils#falseFileFilter()
 */
public class FalseFileFilter implements com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter, Serializable {

    private static final String TO_STRING = Boolean.FALSE.toString();

    /**
     * Singleton instance of false filter.
     *
     * @since 1.3
     */
    public static final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter FALSE = new FalseFileFilter();

    /**
     * Singleton instance of false filter. Please use the identical FalseFileFilter.FALSE constant. The new name is more
     * JDK 1.5 friendly as it doesn't clash with other values when using static imports.
     */
    public static final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter INSTANCE = FALSE;

    private static final long serialVersionUID = 6210271677940926200L;

    /**
     * Restrictive constructor.
     */
    protected FalseFileFilter() {
    }

    /**
     * Returns false.
     *
     * @param file the file to check (ignored)
     * @return false
     */
    @Override
    public boolean accept(final File file) {
        return false;
    }

    /**
     * Returns false.
     *
     * @param dir the directory to check (ignored)
     * @param name the file name (ignored)
     * @return false
     */
    @Override
    public boolean accept(final File dir, final String name) {
        return false;
    }

    /**
     * Returns false.
     *
     * @param file the file to check (ignored)
     *
     * @return false
     * @since 2.9.0
     */
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return FileVisitResult.TERMINATE;
    }

    @Override
    public com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter and(final com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter fileFilter) {
        // FALSE AND expression <=> FALSE
        return INSTANCE;
    }

    @Override
    public com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter negate() {
        return TrueFileFilter.INSTANCE;
    }

    @Override
    public com.github.terefang.jmelange.apache.io.filefilter.IOFileFilter or(final IOFileFilter fileFilter) {
        // FALSE OR expression <=> expression
        return fileFilter;
    }

    @Override
    public String toString() {
        return TO_STRING;
    }
}
