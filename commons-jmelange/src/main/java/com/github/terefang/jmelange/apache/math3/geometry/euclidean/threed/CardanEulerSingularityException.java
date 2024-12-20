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

package com.github.terefang.jmelange.apache.math3.geometry.euclidean.threed;

import com.github.terefang.jmelange.apache.math3.exception.MathIllegalStateException;
import com.github.terefang.jmelange.apache.math3.exception.util.LocalizedFormats;

/** This class represents exceptions thrown while extractiong Cardan
 * or Euler angles from a rotation.

 * @since 1.2
 */
public class CardanEulerSingularityException
  extends MathIllegalStateException {

    /** Serializable version identifier */
    private static final long serialVersionUID = -1360952845582206770L;

    /**
     * Simple constructor.
     * build an exception with a default message.
     * @param isCardan if true, the rotation is related to Cardan angles,
     * if false it is related to EulerAngles
     */
    public CardanEulerSingularityException(boolean isCardan) {
        super(isCardan ? LocalizedFormats.CARDAN_ANGLES_SINGULARITY : LocalizedFormats.EULER_ANGLES_SINGULARITY);
    }

}
