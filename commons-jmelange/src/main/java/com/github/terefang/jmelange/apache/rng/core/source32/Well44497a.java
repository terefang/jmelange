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
package com.github.terefang.jmelange.apache.rng.core.source32;

/**
 * This class implements the WELL44497a pseudo-random number generator
 * from Fran&ccedil;ois Panneton, Pierre L'Ecuyer and Makoto Matsumoto.
 * <p>
 * This generator is described in a paper by Fran&ccedil;ois Panneton,
 * Pierre L'Ecuyer and Makoto Matsumoto
 * <a href="http://www.iro.umontreal.ca/~lecuyer/myftp/papers/wellrng.pdf">
 * Improved Long-Period Generators Based on Linear Recurrences Modulo 2</a>
 * ACM Transactions on Mathematical Software, 32, 1 (2006).
 * The errata for the paper are in
 * <a href="http://www.iro.umontreal.ca/~lecuyer/myftp/papers/wellrng-errata.txt">wellrng-errata.txt</a>.
 * </p>
 *
 * @see <a href="http://www.iro.umontreal.ca/~panneton/WELLRNG.html">WELL Random number generator</a>
 * @since 1.0
 */
public class Well44497a extends AbstractWell {
    /** Number of bits in the pool. */
    private static final int K = 44497;
    /** First parameter of the algorithm. */
    private static final int M1 = 23;
    /** Second parameter of the algorithm. */
    private static final int M2 = 481;
    /** Third parameter of the algorithm. */
    private static final int M3 = 229;
    /** The indirection index table. */
    private static final IndexTable TABLE = new IndexTable(K, M1, M2, M3);

    /**
     * Creates a new random number generator.
     *
     * @param seed Initial seed.
     */
    public Well44497a(int[] seed) {
        super(K, seed);
    }

    /** {@inheritDoc} */
    @Override
    public int next() {
        final int indexRm1 = TABLE.getIndexPred(index);
        final int indexRm2 = TABLE.getIndexPred2(index);

        final int v0 = v[index];
        final int vM1 = v[TABLE.getIndexM1(index)];
        final int vM2 = v[TABLE.getIndexM2(index)];
        final int vM3 = v[TABLE.getIndexM3(index)];

        // the values below include the errata of the original article
        final int z0 = (0xFFFF8000 & v[indexRm1]) ^ (0x00007FFF & v[indexRm2]);
        final int z1 = (v0 ^ (v0 << 24)) ^ (vM1 ^ (vM1 >>> 30));
        final int z2 = (vM2 ^ (vM2 << 10)) ^ (vM3 << 26);
        final int z3 = z1 ^ z2;
        final int z2Prime = ((z2 << 9) ^ (z2 >>> 23)) & 0xfbffffff;
        final int z2Second = ((z2 & 0x00020000) == 0) ? z2Prime : (z2Prime ^ 0xb729fcec);
        final int z4 = z0 ^ (z1 ^ (z1 >>> 20)) ^ z2Second ^ z3;

        v[index] = z3;
        v[indexRm1] = z4;
        v[indexRm2] &= 0xFFFF8000;
        index = indexRm1;

        return z4;
    }
}
