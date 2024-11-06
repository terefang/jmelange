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

package com.github.terefang.jmelange.apache.io.function;

import com.github.terefang.jmelange.apache.io.function.IOBaseStreamAdapter;
import com.github.terefang.jmelange.apache.io.function.IOStream;

import java.util.stream.Stream;

/**
 * Adapts an {@link Stream} as an {@link com.github.terefang.jmelange.apache.io.function.IOStream}.
 *
 * Keep package-private for now.
 *
 * @param <T> the type of the stream elements.
 */
final class IOStreamAdapter<T> extends IOBaseStreamAdapter<T, com.github.terefang.jmelange.apache.io.function.IOStream<T>, Stream<T>>
        implements com.github.terefang.jmelange.apache.io.function.IOStream<T>
{

    static <T> com.github.terefang.jmelange.apache.io.function.IOStream<T> adapt(final Stream<T> delegate) {
        return delegate != null ? new IOStreamAdapter<>(delegate) : com.github.terefang.jmelange.apache.io.function.IOStream.empty();
    }

    private IOStreamAdapter(final Stream<T> delegate) {
        super(delegate);
    }

    @Override
    public IOStream<T> wrap(final Stream<T> delegate) {
        return unwrap() == delegate ? this : adapt(delegate);
    }

}
