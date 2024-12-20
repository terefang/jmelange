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

package com.github.terefang.jmelange.apache.math3.ml.neuralnet.twod.util;

import com.github.terefang.jmelange.apache.math3.ml.neuralnet.twod.NeuronSquareMesh2D;

/**
 * Interface for algorithms that compute some metrics of the projection of
 * data on a 2D-map.
 * @since 3.6
 */
public interface MapDataVisualization {
    /**
     * Creates an image of the {@code data} metrics when represented by the
     * {@code map}.
     *
     * @param map Map.
     * @param data Data.
     * @return a 2D-array (in row major order) representing the metrics.
     */
    double[][] computeImage(NeuronSquareMesh2D map,
                            Iterable<double[]> data);
}
