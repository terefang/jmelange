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
package com.github.terefang.jmelange.apache.math3.fitting;

import com.github.terefang.jmelange.apache.math3.analysis.polynomials.PolynomialFunction;
import com.github.terefang.jmelange.apache.math3.fitting.PolynomialCurveFitter;
import com.github.terefang.jmelange.apache.math3.fitting.WeightedObservedPoints;
import com.github.terefang.jmelange.apache.math3.optim.nonlinear.vector.MultivariateVectorOptimizer;

/**
 * Polynomial fitting is a very simple case of {@link CurveFitter curve fitting}.
 * The estimated coefficients are the polynomial coefficients (see the
 * {@link #fit(double[]) fit} method).
 *
 * @since 2.0
 * @deprecated As of 3.3. Please use {@link PolynomialCurveFitter} and
 * {@link WeightedObservedPoints} instead.
 */
@Deprecated
public class PolynomialFitter extends CurveFitter<PolynomialFunction.Parametric> {
    /**
     * Simple constructor.
     *
     * @param optimizer Optimizer to use for the fitting.
     */
    public PolynomialFitter(MultivariateVectorOptimizer optimizer) {
        super(optimizer);
    }

    /**
     * Get the coefficients of the polynomial fitting the weighted data points.
     * The degree of the fitting polynomial is {@code guess.length - 1}.
     *
     * @param guess First guess for the coefficients. They must be sorted in
     * increasing order of the polynomial's degree.
     * @param maxEval Maximum number of evaluations of the polynomial.
     * @return the coefficients of the polynomial that best fits the observed points.
     * @throws com.github.terefang.jmelange.apache.math3.exception.TooManyEvaluationsException if
     * the number of evaluations exceeds {@code maxEval}.
     * @throws com.github.terefang.jmelange.apache.math3.exception.ConvergenceException
     * if the algorithm failed to converge.
     */
    public double[] fit(int maxEval, double[] guess) {
        return fit(maxEval, new PolynomialFunction.Parametric(), guess);
    }

    /**
     * Get the coefficients of the polynomial fitting the weighted data points.
     * The degree of the fitting polynomial is {@code guess.length - 1}.
     *
     * @param guess First guess for the coefficients. They must be sorted in
     * increasing order of the polynomial's degree.
     * @return the coefficients of the polynomial that best fits the observed points.
     * @throws com.github.terefang.jmelange.apache.math3.exception.ConvergenceException
     * if the algorithm failed to converge.
     */
    public double[] fit(double[] guess) {
        return fit(new PolynomialFunction.Parametric(), guess);
    }
}
