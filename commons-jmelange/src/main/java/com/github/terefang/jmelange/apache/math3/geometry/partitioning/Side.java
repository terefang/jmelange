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
package com.github.terefang.jmelange.apache.math3.geometry.partitioning;

import com.github.terefang.jmelange.apache.math3.geometry.partitioning.Hyperplane;

/** Enumerate representing the location of an element with respect to an
 * {@link Hyperplane hyperplane} of a space.
 * @since 3.0
 */
public enum Side {

    /** Code for the plus side of the hyperplane. */
    PLUS,

    /** Code for the minus side of the hyperplane. */
    MINUS,

    /** Code for elements crossing the hyperplane from plus to minus side. */
    BOTH,

    /** Code for the hyperplane itself. */
    HYPER;

}
