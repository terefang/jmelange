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

import com.github.terefang.jmelange.apache.rng.UniformRandomProvider;

/**
 * Base class for a sampler.
 *
 * @since 1.0
 *
 * @deprecated Since version 1.1. Class intended for internal use only.
 */
@Deprecated
public class SamplerBase {
    /** RNG. */
    private final UniformRandomProvider rng;

    /**
     * Create an instance.
     *
     * @param rng Generator of uniformly distributed random numbers.
     */
    protected SamplerBase(UniformRandomProvider rng) {
        this.rng = rng;
    }

    /**
     * Return the next {@code double} value.
     *
     * @return a random value from a uniform distribution in the
     * interval {@code [0, 1)}.
     */
    protected double nextDouble() {
        return rng.nextDouble();
    }

    /**
     * Return the next {@code int} value.
     *
     * @return a random {@code int} value.
     */
    protected int nextInt() {
        return rng.nextInt();
    }

    /**
     * Return the next {@code int} value.
     *
     * @param max Upper bound (excluded).
     * @return a random {@code int} value in the interval {@code [0, max)}.
     */
    protected int nextInt(int max) {
        return rng.nextInt(max);
    }

    /**
     * Return the next {@code long} value.
     *
     * @return a random {@code long} value.
     */
    protected long nextLong() {
        return rng.nextLong();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "rng=" + rng.toString();
    }
}
