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
package com.github.terefang.jmelange.apache.rng.sampling.distribution;

import java.util.stream.DoubleStream;

/**
 * Sampler that generates values of type {@code double}.
 *
 * @since 1.0
 */
public interface ContinuousSampler {
    /**
     * Creates a {@code double} sample.
     *
     * @return a sample.
     */
    double sample();

    /**
     * Returns an effectively unlimited stream of {@code double} sample values.
     *
     * <p>The default implementation produces a sequential stream that repeatedly
     * calls {@link #sample sample}().
     *
     * @return a stream of {@code double} values.
     * @since 1.5
     */
    default DoubleStream samples() {
        return DoubleStream.generate(this::sample).sequential();
    }

    /**
     * Returns a stream producing the given {@code streamSize} number of {@code double}
     * sample values.
     *
     * <p>The default implementation produces a sequential stream that repeatedly
     * calls {@link #sample sample}(); the stream is limited to the given {@code streamSize}.
     *
     * @param streamSize Number of values to generate.
     * @return a stream of {@code double} values.
     * @since 1.5
     */
    default DoubleStream samples(long streamSize) {
        return samples().limit(streamSize);
    }
}
