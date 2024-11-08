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
package com.github.terefang.jmelange.apache.math3.geometry;

import com.github.terefang.jmelange.apache.math3.geometry.Space;
import com.github.terefang.jmelange.apache.math3.geometry.Vector;

import java.io.Serializable;

/** This interface represents a generic geometrical point.
 * @param <S> Type of the space.
 * @see Space
 * @see Vector
 * @since 3.3
 */
public interface Point<S extends Space> extends Serializable {

    /** Get the space to which the point belongs.
     * @return containing space
     */
    Space getSpace();

    /**
     * Returns true if any coordinate of this point is NaN; false otherwise
     * @return  true if any coordinate of this point is NaN; false otherwise
     */
    boolean isNaN();

    /** Compute the distance between the instance and another point.
     * @param p second point
     * @return the distance between the instance and p
     */
    double distance(Point<S> p);

}
