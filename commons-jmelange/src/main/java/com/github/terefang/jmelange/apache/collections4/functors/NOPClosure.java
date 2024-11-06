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
package com.github.terefang.jmelange.apache.collections4.functors;

import java.io.Serializable;

import com.github.terefang.jmelange.apache.collections4.Closure;

/**
 * Closure implementation that does nothing.
 *
 * @param <T> the type of the input to the operation.
 * @since 3.0
 */
public final class NOPClosure<T> implements Closure<T>, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = 3518477308466486130L;

    /** Singleton predicate instance */
    @SuppressWarnings("rawtypes")
    public static final Closure INSTANCE = new NOPClosure<>();

    /**
     * Factory returning the singleton instance.
     *
     * @param <E> the type that the closure acts on
     * @return the singleton instance
     * @since 3.1
     */
    public static <E> Closure<E> nopClosure() {
        return INSTANCE;
    }

    /**
     * Constructs a new instance.
     */
    private NOPClosure() {
    }

    /**
     * Do nothing.
     *
     * @param input  the input object
     */
    @Override
    public void execute(final T input) {
        // do nothing
    }

    /**
     * Returns the singleton instance.
     *
     * @return the singleton instance.
     */
    private Object readResolve() {
        return INSTANCE;
    }

}
