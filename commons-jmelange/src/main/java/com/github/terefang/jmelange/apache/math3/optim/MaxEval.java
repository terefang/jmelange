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
package com.github.terefang.jmelange.apache.math3.optim;

import com.github.terefang.jmelange.apache.math3.exception.NotStrictlyPositiveException;
import com.github.terefang.jmelange.apache.math3.optim.OptimizationData;

/**
 * Maximum number of evaluations of the function to be optimized.
 *
 * @since 3.1
 */
public class MaxEval implements OptimizationData
{
    /** Allowed number of evalutations. */
    private final int maxEval;

    /**
     * @param max Allowed number of evalutations.
     * @throws NotStrictlyPositiveException if {@code max <= 0}.
     */
    public MaxEval(int max) {
        if (max <= 0) {
            throw new NotStrictlyPositiveException(max);
        }

        maxEval = max;
    }

    /**
     * Gets the maximum number of evaluations.
     *
     * @return the allowed number of evaluations.
     */
    public int getMaxEval() {
        return maxEval;
    }

    /**
     * Factory method that creates instance of this class that represents
     * a virtually unlimited number of evaluations.
     *
     * @return a new instance suitable for allowing {@link Integer#MAX_VALUE}
     * evaluations.
     */
    public static MaxEval unlimited() {
        return new MaxEval(Integer.MAX_VALUE);
    }
}
