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

import java.io.PrintWriter;
import java.io.Writer;

/**
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 *
 */
public class WriterStreamConsumer implements StreamConsumer
{
    private PrintWriter writer;

    public WriterStreamConsumer(Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    @Override
    public void consumeLine(String line) {
        writer.println(line);

        writer.flush();
    }
}
