package com.github.terefang.jmelange.plexus.util.io;

/*
 * Copyright The Codehaus Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface of a wrapper for input streams. This facade is used by methods, which are being implemented for files,
 * URL's, or input streams.
 */
public interface InputStreamFacade {
    /**
     * The caller must assume, that this method may be invoked only once.
     * @return Retrieves the actual {@link InputStream}.
     * @throws IOException if io issue
     */
    InputStream getInputStream() throws IOException;
}
