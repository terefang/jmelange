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
package com.github.terefang.jmelange.apache.rng.core;

import java.util.Arrays;

import com.github.terefang.jmelange.apache.rng.RandomProviderState;

/**
 * Wraps the internal state of a generator instance.
 * Its purpose is to store all the data needed to recover the same
 * state in order to restart a sequence where it left off.
 * External code should not try to modify the data contained in instances
 * of this class.
 *
 * @since 1.0
 */
public class RandomProviderDefaultState implements RandomProviderState {
    /** Internal state. */
    private final byte[] state;

    /**
     * Initializes an instance.
     * The contents of the {@code state} argument is unspecified, and is
     * guaranteed to be valid only if it was generated by implementations
     * provided by this library.
     *
     * @param state Mapping of all the data which an implementation of
     * {@link com.github.terefang.jmelange.apache.rng.UniformRandomProvider} needs in order
     * to reset its internal state.
     */
    public RandomProviderDefaultState(byte[] state) {
        this.state = Arrays.copyOf(state, state.length);
    }

    /**
     * Get the state.
     *
     * @return the internal state.
     */
    public byte[] getState() {
        return Arrays.copyOf(state, state.length);
    }
}
