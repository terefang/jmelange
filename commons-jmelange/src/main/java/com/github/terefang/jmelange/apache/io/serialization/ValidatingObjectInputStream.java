/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.terefang.jmelange.apache.io.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.github.terefang.jmelange.apache.io.build.AbstractStreamBuilder;
import com.github.terefang.jmelange.apache.io.input.BOMInputStream;
import com.github.terefang.jmelange.apache.io.serialization.ClassNameMatcher;
import com.github.terefang.jmelange.apache.io.serialization.FullClassNameMatcher;
import com.github.terefang.jmelange.apache.io.serialization.RegexpClassNameMatcher;
import com.github.terefang.jmelange.apache.io.serialization.WildcardClassNameMatcher;

/**
 * An {@link ObjectInputStream} that's restricted to deserialize a limited set of classes.
 *
 * <p>
 * Various accept/reject methods allow for specifying which classes can be deserialized.
 * </p>
 * // *
 * <p>
 * // * <b>Reading safely</b> // *
 * </p>
 * <h2>Reading safely</h2>
 * <p>
 * Here is the only way to safely read a HashMap of String keys and Integer values:
 * </p>
 *
 * <pre>{@code
 * // Data
 * final HashMap<String, Integer> map1 = new HashMap<>();
 * map1.put("1", 1);
 * // Write
 * final byte[] byteArray;
 * try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
 *         final ObjectOutputStream oos = new ObjectOutputStream(baos)) {
 *     oos.writeObject(map1);
 *     oos.flush();
 *     byteArray = baos.toByteArray();
 * }
 * // Read
 * try (ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
 *         ValidatingObjectInputStream vois = ValidatingObjectInputStream.builder().setInputStream(bais).get()) {
 *     // String.class is automatically accepted
 *     vois.accept(HashMap.class, Number.class, Integer.class);
 *     final HashMap<String, Integer> map2 = (HashMap<String, Integer>) vois.readObject();
 *     assertEquals(map1, map2);
 * }
 * }</pre>
 * <p>
 * Design inspired by a <a href="http://www.ibm.com/developerworks/library/se-lookahead/">IBM DeveloperWorks Article</a>.
 * </p>
 *
 * @since 2.5
 */
public class ValidatingObjectInputStream extends ObjectInputStream {

    // @formatter:off
    /**
     * Builds a new {@link BOMInputStream}.
     *
     * <h2>Using NIO</h2>
     * <pre>{@code
     * ValidatingObjectInputStream s = ValidatingObjectInputStream.builder()
     *   .setPath(Paths.get("MyFile.xml"))
     *   .get();}
     * </pre>
     * <h2>Using IO</h2>
     * <pre>{@code
     * ValidatingObjectInputStream s = ValidatingObjectInputStream.builder()
     *   .setFile(new File("MyFile.xml"))
     *   .get();}
     * </pre>
     *
     * @see #get()
     * @since 2.18.0
     */
    // @formatter:on
    public static class Builder extends AbstractStreamBuilder<ValidatingObjectInputStream, Builder> {

        @Override
        public ValidatingObjectInputStream get() throws IOException {
            return new ValidatingObjectInputStream(getInputStream());
        }

    }

    /**
     * Constructs a new {@link Builder}.
     *
     * @return a new {@link Builder}.
     * @since 2.18.0
     */
    public static Builder builder() {
        return new Builder();
    }

    private final List<com.github.terefang.jmelange.apache.io.serialization.ClassNameMatcher> acceptMatchers = new ArrayList<>();
    private final List<com.github.terefang.jmelange.apache.io.serialization.ClassNameMatcher> rejectMatchers = new ArrayList<>();

    /**
     * Constructs an object to deserialize the specified input stream. At least one accept method needs to be called to specify which classes can be
     * deserialized, as by default no classes are accepted.
     *
     * @param input an input stream
     * @throws IOException if an I/O error occurs while reading stream header
     * @deprecated Use {@link Builder}.
     */
    @Deprecated
    public ValidatingObjectInputStream(final InputStream input) throws IOException {
        super(input);
    }

    /**
     * Accept the specified classes for deserialization, unless they are otherwise rejected.
     *
     * @param classes Classes to accept
     * @return this object
     */
    public ValidatingObjectInputStream accept(final Class<?>... classes) {
        Stream.of(classes).map(c -> new com.github.terefang.jmelange.apache.io.serialization.FullClassNameMatcher(c.getName())).forEach(acceptMatchers::add);
        return this;
    }

    /**
     * Accept class names where the supplied ClassNameMatcher matches for deserialization, unless they are otherwise rejected.
     *
     * @param m the matcher to use
     * @return this object
     */
    public ValidatingObjectInputStream accept(final com.github.terefang.jmelange.apache.io.serialization.ClassNameMatcher m) {
        acceptMatchers.add(m);
        return this;
    }

    /**
     * Accept class names that match the supplied pattern for deserialization, unless they are otherwise rejected.
     *
     * @param pattern standard Java regexp
     * @return this object
     */
    public ValidatingObjectInputStream accept(final Pattern pattern) {
        acceptMatchers.add(new com.github.terefang.jmelange.apache.io.serialization.RegexpClassNameMatcher(pattern));
        return this;
    }

    /**
     * Accept the wildcard specified classes for deserialization, unless they are otherwise rejected.
     *
     * @param patterns Wildcard file name patterns as defined by {@link com.github.terefang.jmelange.apache.io.FilenameUtils#wildcardMatch(String, String)
     *                 FilenameUtils.wildcardMatch}
     * @return this object
     */
    public ValidatingObjectInputStream accept(final String... patterns) {
        Stream.of(patterns).map(com.github.terefang.jmelange.apache.io.serialization.WildcardClassNameMatcher::new).forEach(acceptMatchers::add);
        return this;
    }

    /**
     * Checks that the class name conforms to requirements.
     *
     * @param name The class name
     * @throws InvalidClassException when a non-accepted class is encountered
     */
    private void checkClassName(final String name) throws InvalidClassException {
        // Reject has precedence over accept
        for (final com.github.terefang.jmelange.apache.io.serialization.ClassNameMatcher m : rejectMatchers) {
            if (m.matches(name)) {
                invalidClassNameFound(name);
            }
        }

        boolean ok = false;
        for (final com.github.terefang.jmelange.apache.io.serialization.ClassNameMatcher m : acceptMatchers) {
            if (m.matches(name)) {
                ok = true;
                break;
            }
        }
        if (!ok) {
            invalidClassNameFound(name);
        }
    }

    /**
     * Called to throw {@link InvalidClassException} if an invalid class name is found during deserialization. Can be overridden, for example to log those class
     * names.
     *
     * @param className name of the invalid class
     * @throws InvalidClassException if the specified class is not allowed
     */
    protected void invalidClassNameFound(final String className) throws InvalidClassException {
        throw new InvalidClassException("Class name not accepted: " + className);
    }

    /**
     * Reject the specified classes for deserialization, even if they are otherwise accepted.
     *
     * @param classes Classes to reject
     * @return this object
     */
    public ValidatingObjectInputStream reject(final Class<?>... classes) {
        Stream.of(classes).map(c -> new com.github.terefang.jmelange.apache.io.serialization.FullClassNameMatcher(c.getName())).forEach(rejectMatchers::add);
        return this;
    }

    /**
     * Reject class names where the supplied ClassNameMatcher matches for deserialization, even if they are otherwise accepted.
     *
     * @param m the matcher to use
     * @return this object
     */
    public ValidatingObjectInputStream reject(final ClassNameMatcher m) {
        rejectMatchers.add(m);
        return this;
    }

    /**
     * Reject class names that match the supplied pattern for deserialization, even if they are otherwise accepted.
     *
     * @param pattern standard Java regexp
     * @return this object
     */
    public ValidatingObjectInputStream reject(final Pattern pattern) {
        rejectMatchers.add(new com.github.terefang.jmelange.apache.io.serialization.RegexpClassNameMatcher(pattern));
        return this;
    }

    /**
     * Reject the wildcard specified classes for deserialization, even if they are otherwise accepted.
     *
     * @param patterns Wildcard file name patterns as defined by {@link com.github.terefang.jmelange.apache.io.FilenameUtils#wildcardMatch(String, String)
     *                 FilenameUtils.wildcardMatch}
     * @return this object
     */
    public ValidatingObjectInputStream reject(final String... patterns) {
        Stream.of(patterns).map(com.github.terefang.jmelange.apache.io.serialization.WildcardClassNameMatcher::new).forEach(rejectMatchers::add);
        return this;
    }

    @Override
    protected Class<?> resolveClass(final ObjectStreamClass osc) throws IOException, ClassNotFoundException {
        checkClassName(osc.getName());
        return super.resolveClass(osc);
    }
}
