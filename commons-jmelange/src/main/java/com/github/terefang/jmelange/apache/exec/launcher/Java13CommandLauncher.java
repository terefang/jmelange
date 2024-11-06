/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.github.terefang.jmelange.apache.exec.launcher;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.github.terefang.jmelange.apache.exec.CommandLine;
import com.github.terefang.jmelange.apache.exec.environment.EnvironmentUtils;

/**
 * A command launcher for JDK/JRE 1.3 (and higher). Uses the built-in Runtime.exec() command.
 */
public class Java13CommandLauncher extends CommandLauncherImpl {

    /**
     * Constructs a new instance.
     */
    public Java13CommandLauncher() {
    }

    /**
     * Launches the given command in a new process, in the given working directory.
     *
     * @param cmd        the command line to execute as an array of strings.
     * @param env        the environment to set as an array of strings.
     * @param workingDir the working directory where the command should run.
     * @throws IOException probably forwarded from {@link Runtime#exec(String[], String[], File)}.
     */
    @Override
    public Process exec(final CommandLine cmd, final Map<String, String> env, final File workingDir) throws IOException {
        return Runtime.getRuntime().exec(cmd.toStrings(), EnvironmentUtils.toStrings(env), workingDir);
    }
}
