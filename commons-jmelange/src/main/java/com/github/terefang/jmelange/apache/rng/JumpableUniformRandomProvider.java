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
package com.github.terefang.jmelange.apache.rng;

import java.util.stream.Stream;

/**
 * Applies to generators that can be advanced a large number of
 * steps of the output sequence in a single operation.
 *
 * @since 1.3
 */
public interface JumpableUniformRandomProvider extends UniformRandomProvider {
    /**
     * Creates a copy of the UniformRandomProvider and then advances the
     * state of the current instance. The copy is returned.
     *
     * <p>The current state will be advanced in a single operation by the equivalent of a
     * number of sequential calls to a method that updates the state of the provider. The
     * size of the jump is implementation dependent.</p>
     *
     * <p>Repeat invocations of this method will create a series of generators
     * that are uniformly spaced at intervals of the output sequence. Each generator provides
     * non-overlapping output for the length of the jump for use in parallel computations.</p>
     *
     * @return A copy of the current state.
     */
    UniformRandomProvider jump();

    /**
     * Returns an effectively unlimited stream of new random generators, each of which
     * implements the {@link UniformRandomProvider} interface.
     *
     * @return a stream of random generators.
     * @since 1.5
     */
    default Stream<UniformRandomProvider> jumps() {
        return Stream.generate(this::jump).sequential();
    }

    /**
     * Returns a stream producing the given {@code streamSize} number of new random
     * generators, each of which implements the {@link UniformRandomProvider}
     * interface.
     *
     * @param streamSize Number of objects to generate.
     * @return a stream of random generators; the stream is limited to the given
     * {@code streamSize}.
     * @throws IllegalArgumentException if {@code streamSize} is negative.
     * @since 1.5
     */
    default Stream<UniformRandomProvider> jumps(long streamSize) {
        UniformRandomProviderSupport.validateStreamSize(streamSize);
        return jumps().limit(streamSize);
    }
}
