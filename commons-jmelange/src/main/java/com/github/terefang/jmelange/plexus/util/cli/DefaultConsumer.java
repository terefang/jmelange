package com.github.terefang.jmelange.plexus.util.cli;

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

import com.github.terefang.jmelange.plexus.util.cli.StreamConsumer;

import java.io.IOException;

/**
 * @author <a href="mailto:evenisse@apache.org">Emmanuel Venisse</a>
 *
 */
public class DefaultConsumer implements StreamConsumer
{

    @Override
    public void consumeLine(String line) throws IOException {
        System.out.println(line);

        if (System.out.checkError()) {
            throw new IOException(String.format("Failure printing line '%s' to stdout.", line));
        }
    }
}
