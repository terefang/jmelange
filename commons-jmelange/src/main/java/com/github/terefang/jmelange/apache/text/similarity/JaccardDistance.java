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
package com.github.terefang.jmelange.apache.text.similarity;

import com.github.terefang.jmelange.apache.text.similarity.EditDistance;
import com.github.terefang.jmelange.apache.text.similarity.JaccardSimilarity;
import com.github.terefang.jmelange.apache.text.similarity.SimilarityInput;

/**
 * Measures the Jaccard distance of two sets of character sequence. Jaccard distance is the dissimilarity between two sets. It is the complementary of Jaccard
 * similarity.
 *
 * <p>
 * For further explanation about Jaccard Distance, refer https://en.wikipedia.org/wiki/Jaccard_index
 * </p>
 *
 * @since 1.0
 */
public class JaccardDistance implements EditDistance<Double>
{

    /**
     * Computes the Jaccard distance of two set character sequence passed as input. Calculates Jaccard similarity and returns the complement of it.
     *
     * @param left  first input sequence.
     * @param right second input sequence.
     * @return index
     * @throws IllegalArgumentException if either String input {@code null}.
     */
    @Override
    public Double apply(final CharSequence left, final CharSequence right) {
        return apply(SimilarityInput.input(left), SimilarityInput.input(right));
    }

    /**
     * Computes the Jaccard distance of two set character sequence passed as input. Calculates Jaccard similarity and returns the complement of it.
     *
     * @param <E>   The type of similarity score unit.
     * @param left  first input sequence.
     * @param right second input sequence.
     * @return index
     * @throws IllegalArgumentException if either String input {@code null}.
     */
    public <E> Double apply(final SimilarityInput<E> left, final SimilarityInput<E> right) {
        return 1.0 - JaccardSimilarity.INSTANCE.apply(left, right).doubleValue();
    }

}
