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

import com.github.terefang.jmelange.apache.math3.analysis.MultivariateVectorFunction;
import com.github.terefang.jmelange.apache.math3.optim.OptimizationData;

/**
 * Gradient of the scalar function to be optimized.
 *
 * @since 3.1
 */
public class ObjectiveFunctionGradient implements OptimizationData {
    /** Function to be optimized. */
    private final MultivariateVectorFunction gradient;

    /**
     * @param g Gradient of the function to be optimized.
     */
    public ObjectiveFunctionGradient(MultivariateVectorFunction g) {
        gradient = g;
    }

    /**
     * Gets the gradient of the function to be optimized.
     *
     * @return the objective function gradient.
     */
    public MultivariateVectorFunction getObjectiveFunctionGradient() {
        return gradient;
    }
}
