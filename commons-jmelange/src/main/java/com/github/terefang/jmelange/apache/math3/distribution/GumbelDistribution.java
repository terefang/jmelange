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
package com.github.terefang.jmelange.apache.math3.distribution;

import com.github.terefang.jmelange.apache.math3.distribution.AbstractRealDistribution;
import com.github.terefang.jmelange.apache.math3.exception.NotStrictlyPositiveException;
import com.github.terefang.jmelange.apache.math3.exception.OutOfRangeException;
import com.github.terefang.jmelange.apache.math3.exception.util.LocalizedFormats;
import com.github.terefang.jmelange.apache.math3.random.RandomGenerator;
import com.github.terefang.jmelange.apache.math3.random.Well19937c;
import com.github.terefang.jmelange.apache.math3.util.FastMath;
import com.github.terefang.jmelange.apache.math3.util.MathUtils;

/**
 * This class implements the Gumbel distribution.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Gumbel_distribution">Gumbel Distribution (Wikipedia)</a>
 * @see <a href="http://mathworld.wolfram.com/GumbelDistribution.html">Gumbel Distribution (Mathworld)</a>
 *
 * @since 3.4
 */
public class GumbelDistribution extends AbstractRealDistribution
{

    /** Serializable version identifier. */
    private static final long serialVersionUID = 20141003;

    /**
     * Approximation of Euler's constant
     * see http://mathworld.wolfram.com/Euler-MascheroniConstantApproximations.html
     */
    private static final double EULER = FastMath.PI / (2 * FastMath.E);

    /** The location parameter. */
    private final double mu;
    /** The scale parameter. */
    private final double beta;

    /**
     * Build a new instance.
     * <p>
     * <b>Note:</b> this constructor will implicitly create an instance of
     * {@link Well19937c} as random generator to be used for sampling only (see
     * {@link #sample()} and {@link #sample(int)}). In case no sampling is
     * needed for the created distribution, it is advised to pass {@code null}
     * as random generator via the appropriate constructors to avoid the
     * additional initialisation overhead.
     *
     * @param mu location parameter
     * @param beta scale parameter (must be positive)
     * @throws NotStrictlyPositiveException if {@code beta <= 0}
     */
    public GumbelDistribution(double mu, double beta) {
        this(new Well19937c(), mu, beta);
    }

    /**
     * Build a new instance.
     *
     * @param rng Random number generator
     * @param mu location parameter
     * @param beta scale parameter (must be positive)
     * @throws NotStrictlyPositiveException if {@code beta <= 0}
     */
    public GumbelDistribution(RandomGenerator rng, double mu, double beta) {
        super(rng);

        if (beta <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SCALE, beta);
        }

        this.beta = beta;
        this.mu = mu;
    }

    /**
     * Access the location parameter, {@code mu}.
     *
     * @return the location parameter.
     */
    public double getLocation() {
        return mu;
    }

    /**
     * Access the scale parameter, {@code beta}.
     *
     * @return the scale parameter.
     */
    public double getScale() {
        return beta;
    }

    /** {@inheritDoc} */
    public double density(double x) {
        final double z = (x - mu) / beta;
        final double t = FastMath.exp(-z);
        return FastMath.exp(-z - t) / beta;
    }

    /** {@inheritDoc} */
    public double cumulativeProbability(double x) {
        final double z = (x - mu) / beta;
        return FastMath.exp(-FastMath.exp(-z));
    }

    /** {@inheritDoc} */
    @Override
    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        if (p < 0.0 || p > 1.0) {
            throw new OutOfRangeException(p, 0.0, 1.0);
        } else if (p == 0) {
            return Double.NEGATIVE_INFINITY;
        } else if (p == 1) {
            return Double.POSITIVE_INFINITY;
        }
        return mu - FastMath.log(-FastMath.log(p)) * beta;
    }

    /** {@inheritDoc} */
    public double getNumericalMean() {
        return mu + EULER * beta;
    }

    /** {@inheritDoc} */
    public double getNumericalVariance() {
        return (MathUtils.PI_SQUARED) / 6.0 * (beta * beta);
    }

    /** {@inheritDoc} */
    public double getSupportLowerBound() {
        return Double.NEGATIVE_INFINITY;
    }

    /** {@inheritDoc} */
    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    /** {@inheritDoc} */
    public boolean isSupportLowerBoundInclusive() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean isSupportConnected() {
        return true;
    }

}
