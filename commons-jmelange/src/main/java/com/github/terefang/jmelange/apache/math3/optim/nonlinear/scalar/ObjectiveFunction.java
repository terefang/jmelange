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
package com.github.terefang.jmelange.apache.math3.optim.nonlinear.scalar;

import com.github.terefang.jmelange.apache.math3.analysis.MultivariateFunction;
import com.github.terefang.jmelange.apache.math3.optim.OptimizationData;

/**
 * Scalar function to be optimized.
 *
 * @since 3.1
 */
public class ObjectiveFunction implements OptimizationData {
    /** Function to be optimized. */
    private final MultivariateFunction function;

    /**
     * @param f Function to be optimized.
     */
    public ObjectiveFunction(MultivariateFunction f) {
        function = f;
    }

    /**
     * Gets the function to be optimized.
     *
     * @return the objective function.
     */
    public MultivariateFunction getObjectiveFunction() {
        return function;
    }
}
