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

package com.github.terefang.jmelange.apache.math3.analysis.solvers;

import com.github.terefang.jmelange.apache.math3.analysis.UnivariateFunction;
import com.github.terefang.jmelange.apache.math3.analysis.solvers.BaseAbstractUnivariateSolver;

/**
 * Base class for solvers.
 *
 * @since 3.0
 */
public abstract class AbstractUnivariateSolver
    extends BaseAbstractUnivariateSolver<UnivariateFunction>
    implements UnivariateSolver {
    /**
     * Construct a solver with given absolute accuracy.
     *
     * @param absoluteAccuracy Maximum absolute error.
     */
    protected AbstractUnivariateSolver(final double absoluteAccuracy) {
        super(absoluteAccuracy);
    }
    /**
     * Construct a solver with given accuracies.
     *
     * @param relativeAccuracy Maximum relative error.
     * @param absoluteAccuracy Maximum absolute error.
     */
    protected AbstractUnivariateSolver(final double relativeAccuracy,
                                       final double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }
    /**
     * Construct a solver with given accuracies.
     *
     * @param relativeAccuracy Maximum relative error.
     * @param absoluteAccuracy Maximum absolute error.
     * @param functionValueAccuracy Maximum function value error.
     */
    protected AbstractUnivariateSolver(final double relativeAccuracy,
                                       final double absoluteAccuracy,
                                       final double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
    }
}
