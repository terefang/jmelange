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

package com.github.terefang.jmelange.apache.io.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;

import com.github.terefang.jmelange.apache.io.file.SimplePathVisitor;
import com.github.terefang.jmelange.apache.io.function.IOBiFunction;

/**
 * A noop path visitor.
 *
 * @since 2.9.0
 */
public class NoopPathVisitor extends SimplePathVisitor
{

    /**
     * The singleton instance.
     */
    public static final NoopPathVisitor INSTANCE = new NoopPathVisitor();

    /**
     * Constructs a new instance.
     */
    public NoopPathVisitor() {
    }

    /**
     * Constructs a new instance.
     *
     * @param visitFileFailed Called on {@link #visitFileFailed(Path, IOException)}.
     * @since 2.12.0
     */
    public NoopPathVisitor(final IOBiFunction<Path, IOException, FileVisitResult> visitFileFailed) {
        super(visitFileFailed);
    }
}