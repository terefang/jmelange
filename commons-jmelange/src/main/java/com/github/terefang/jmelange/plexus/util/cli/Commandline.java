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

/***************************************************************************************************
 * CruiseControl, a Continuous Integration Toolkit Copyright (c) 2001-2003, ThoughtWorks, Inc. 651 W
 * Washington Ave. Suite 500 Chicago, IL 60661 USA All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: + Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. + Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. +
 * Neither the name of ThoughtWorks, Inc., CruiseControl, nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **************************************************************************************************/

/*
 * ====================================================================
 * Copyright 2003-2004 The Apache Software Foundation.
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
 * ====================================================================
 */

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.github.terefang.jmelange.plexus.util.Os;
import com.github.terefang.jmelange.plexus.util.StringUtils;
import com.github.terefang.jmelange.plexus.util.cli.Arg;
import com.github.terefang.jmelange.plexus.util.cli.CommandLineException;
import com.github.terefang.jmelange.plexus.util.cli.CommandLineUtils;
import com.github.terefang.jmelange.plexus.util.cli.shell.BourneShell;
import com.github.terefang.jmelange.plexus.util.cli.shell.CmdShell;
import com.github.terefang.jmelange.plexus.util.cli.shell.CommandShell;
import com.github.terefang.jmelange.plexus.util.cli.shell.Shell;

/**
 * <p>Commandline objects help handling command lines specifying processes to execute.</p>
 *
 * <p>The class can be used to define a command line as nested elements or as a helper to define a command line by an
 * application.</p>
 *
 * <code>
 * &lt;someelement&gt;<br>
 * &nbsp;&nbsp;&lt;acommandline executable="/executable/to/run"&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;argument value="argument 1" /&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;argument line="argument_1 argument_2 argument_3" /&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;argument value="argument 4" /&gt;<br>
 * &nbsp;&nbsp;&lt;/acommandline&gt;<br>
 * &lt;/someelement&gt;<br>
 * </code>
 *
 * <p>The element <code>someelement</code> must provide a method <code>createAcommandline</code> which returns an instance
 * of this class.</p>
 *
 * @author thomas.haas@softwired-inc.com
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 */
public class Commandline implements Cloneable {
    /**
     * @deprecated Use {@link Os} class instead.
     */
    @Deprecated
    protected static final String OS_NAME = "os.name";

    /**
     * @deprecated Use {@link Os} class instead.
     */
    @Deprecated
    protected static final String WINDOWS = "Windows";

    protected Vector<com.github.terefang.jmelange.plexus.util.cli.Arg> arguments = new Vector<>();

    // protected Vector envVars = new Vector();
    // synchronized added to preserve synchronize of Vector class
    protected Map<String, String> envVars = Collections.synchronizedMap(new LinkedHashMap<String, String>());

    private long pid = -1;

    private Shell shell;

    /**
     * @deprecated Use {@link Commandline#setExecutable(String)} instead.
     */
    @Deprecated
    protected String executable;

    /**
     * @deprecated Use {@link Commandline#setWorkingDirectory(File)} or {@link Commandline#setWorkingDirectory(String)}
     *             instead.
     */
    @Deprecated
    private File workingDir;

    /**
     * Create a new command line object. Shell is autodetected from operating system Shell usage is only desirable when
     * generating code for remote execution.
     *
     * @param toProcess sh to process
     * @param shell Shell to use
     */
    public Commandline(String toProcess, Shell shell) {
        this.shell = shell;

        String[] tmp = new String[0];
        try {
            tmp = CommandLineUtils.translateCommandline(toProcess);
        } catch (Exception e) {
            System.err.println("Error translating Commandline.");
        }
        if ((tmp != null) && (tmp.length > 0)) {
            setExecutable(tmp[0]);
            for (int i = 1; i < tmp.length; i++) {
                createArgument().setValue(tmp[i]);
            }
        }
    }

    /**
     * Create a new command line object. Shell is autodetected from operating system Shell usage is only desirable when
     * generating code for remote execution.
     * @param shell the Shell
     */
    public Commandline(Shell shell) {
        this.shell = shell;
    }

    /**
     * Create a new command line object, given a command following POSIX sh quoting rules
     *
     * @param toProcess the process
     */
    public Commandline(String toProcess) {
        setDefaultShell();
        String[] tmp = new String[0];
        try {
            tmp = CommandLineUtils.translateCommandline(toProcess);
        } catch (Exception e) {
            System.err.println("Error translating Commandline.");
        }
        if ((tmp != null) && (tmp.length > 0)) {
            setExecutable(tmp[0]);
            for (int i = 1; i < tmp.length; i++) {
                createArgument().setValue(tmp[i]);
            }
        }
    }

    /**
     * Create a new command line object.
     */
    public Commandline() {
        setDefaultShell();
    }

    public long getPid() {
        if (pid == -1) {
            pid = Long.parseLong(String.valueOf(System.currentTimeMillis()));
        }

        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    /**
     * Class to keep track of the position of an Argument.
     */
    // <p>This class is there to support the srcfile and targetfile
    // elements of &lt;execon&gt; and &lt;transform&gt; - don't know
    // whether there might be additional use cases.</p> --SB
    public class Marker {

        private int position;

        private int realPos = -1;

        Marker(int position) {
            this.position = position;
        }

        /**
         * @return the number of arguments that preceded this marker.
         *
         * <p>The name of the executable - if set - is counted as the very first argument.</p>
         */
        public int getPosition() {
            if (realPos == -1) {
                realPos = (getLiteralExecutable() == null ? 0 : 1);
                for (int i = 0; i < position; i++) {
                    com.github.terefang.jmelange.plexus.util.cli.Arg arg = arguments.elementAt(i);
                    realPos += arg.getParts().length;
                }
            }
            return realPos;
        }
    }

    /**
     * <p>
     * Sets the shell or command-line interpreter for the detected operating system, and the shell arguments.
     * </p>
     */
    private void setDefaultShell() {
        // If this is windows set the shell to command.com or cmd.exe with correct arguments.
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            if (Os.isFamily(Os.FAMILY_WIN9X)) {
                setShell(new CommandShell());
            } else {
                setShell(new CmdShell());
            }
        } else {
            setShell(new BourneShell());
        }
    }

    /**
     * <p>Creates an argument object.</p>
     *
     * <p>Each commandline object has at most one instance of the argument class. This method calls
     * <code>this.createArgument(false)</code>.</p>
     *
     * @return the argument object.
     * @see #createArgument(boolean)
     * @deprecated Use {@link Commandline#createArg()} instead
     */
    @Deprecated
    public Argument createArgument() {
        return this.createArgument(false);
    }

    /**
     * <p>Creates an argument object and adds it to our list of args.</p>
     *
     * <p>Each commandline object has at most one instance of the argument class.</p>
     *
     * @param insertAtStart if true, the argument is inserted at the beginning of the list of args, otherwise it is
     *            appended.
     * @deprecated Use {@link Commandline#createArg(boolean)} instead
     * @return Argument the argument Object
     */
    @Deprecated
    public Argument createArgument(boolean insertAtStart) {
        Argument argument = new Argument();
        if (insertAtStart) {
            arguments.insertElementAt(argument, 0);
        } else {
            arguments.addElement(argument);
        }
        return argument;
    }

    /**
     * <p>Creates an argument object.</p>
     *
     * <p>Each commandline object has at most one instance of the argument class. This method calls
     * <code>this.createArgument(false)</code>.</p>
     *
     * @return the argument object.
     * @see #createArgument(boolean)
     */
    public com.github.terefang.jmelange.plexus.util.cli.Arg createArg() {
        return this.createArg(false);
    }

    /**
     * @return Creates an argument object and adds it to our list of args.
     *
     * <p>Each commandline object has at most one instance of the argument class.</p>
     *
     * @param insertAtStart if true, the argument is inserted at the beginning of the list of args, otherwise it is
     *            appended.
     */
    public com.github.terefang.jmelange.plexus.util.cli.Arg createArg(boolean insertAtStart) {
        com.github.terefang.jmelange.plexus.util.cli.Arg argument = new Argument();
        if (insertAtStart) {
            arguments.insertElementAt(argument, 0);
        } else {
            arguments.addElement(argument);
        }
        return argument;
    }

    /**
     * @param argument the argument
     * @see #addArg(com.github.terefang.jmelange.plexus.util.cli.Arg,boolean)
     */
    public void addArg(com.github.terefang.jmelange.plexus.util.cli.Arg argument) {
        this.addArg(argument, false);
    }

    /**
     * Adds an argument object to our list of args.
     * @param argument the argument
     * @param insertAtStart if true, the argument is inserted at the beginning of the list of args, otherwise it is
     *            appended.
     */
    public void addArg(com.github.terefang.jmelange.plexus.util.cli.Arg argument, boolean insertAtStart) {
        if (insertAtStart) {
            arguments.insertElementAt(argument, 0);
        } else {
            arguments.addElement(argument);
        }
    }

    /**
     * Sets the executable to run.
     * @param executable the executable
     */
    public void setExecutable(String executable) {
        shell.setExecutable(executable);
        this.executable = executable;
    }

    /**
     * @return Executable to be run, as a literal string (no shell quoting/munging)
     */
    public String getLiteralExecutable() {
        return executable;
    }

    /**
     * Return an executable name, quoted for shell use. Shell usage is only desirable when generating code for remote
     * execution.
     *
     * @return Executable to be run, quoted for shell interpretation
     */
    public String getExecutable() {
        String exec = shell.getExecutable();

        if (exec == null) {
            exec = executable;
        }

        return exec;
    }

    public void addArguments(String[] line) {
        for (String aLine : line) {
            createArgument().setValue(aLine);
        }
    }

    /**
     * Add an environment variable
     * @param name name
     * @param value value
     */
    public void addEnvironment(String name, String value) {
        // envVars.add( name + "=" + value );
        envVars.put(name, value);
    }

    /**
     * Add system environment variables
     * @throws Exception if error
     */
    public void addSystemEnvironment() throws Exception {
        Properties systemEnvVars = CommandLineUtils.getSystemEnvVars();

        for (Object o : systemEnvVars.keySet()) {
            String key = (String) o;
            if (!envVars.containsKey(key)) {
                addEnvironment(key, systemEnvVars.getProperty(key));
            }
        }
    }

    /**
     * @return String[] Return the list of environment variables
     * @throws CommandLineException if error
     */
    public String[] getEnvironmentVariables() throws CommandLineException {
        try {
            addSystemEnvironment();
        } catch (Exception e) {
            throw new CommandLineException("Error setting up environmental variables", e);
        }
        String[] environmentVars = new String[envVars.size()];
        int i = 0;
        for (Object o : envVars.keySet()) {
            String name = (String) o;
            String value = envVars.get(name);
            environmentVars[i] = name + "=" + value;
            i++;
        }
        return environmentVars;
    }

    /**
     * @return Returns the executable and all defined arguments.
     *      For Windows Family, {@link Commandline#getShellCommandline()} is returned
     */
    public String[] getCommandline() {
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            return getShellCommandline();
        }

        return getRawCommandline();
    }

    /**
     * Returns the executable and all defined arguments.
     * @return the command line as array not escaped neither quoted
     */
    public String[] getRawCommandline() {
        final String[] args = getArguments();
        String executable = getLiteralExecutable();

        if (executable == null) {
            return args;
        }
        final String[] result = new String[args.length + 1];
        result[0] = executable;
        System.arraycopy(args, 0, result, 1, args.length);
        return result;
    }

    /**
     * Returns the shell, executable and all defined arguments. Shell usage is only desirable when generating code for
     * remote execution.
     * @return the command line as array
     */
    public String[] getShellCommandline() {
        // TODO: Provided only for backward compat. with <= 1.4
        verifyShellState();

        return getShell().getShellCommandLine(getArguments()).toArray(new String[0]);
    }

    /**
     * @return Returns all arguments defined by <code>addLine</code>, <code>addValue</code> or the argument object.
     */
    public String[] getArguments() {
        Vector<String> result = new Vector<>(arguments.size() * 2);
        for (int i = 0; i < arguments.size(); i++) {
            com.github.terefang.jmelange.plexus.util.cli.Arg arg = arguments.elementAt(i);
            String[]                         s   = arg.getParts();
            if (s != null) {
                for (String value : s) {
                    result.addElement(value);
                }
            }
        }

        String[] res = new String[result.size()];
        result.copyInto(res);
        return res;
    }

    @Override
    public String toString() {
        return StringUtils.join(getShellCommandline(), " ");
    }

    public int size() {
        return getCommandline().length;
    }

    @Override
    public Object clone() {
        Commandline c = new Commandline((Shell) shell.clone());
        c.executable = executable;
        c.workingDir = workingDir;
        c.addArguments(getArguments());
        return c;
    }

    /**
     * Clear out the whole command line.
     */
    public void clear() {
        executable = null;
        workingDir = null;
        shell.setExecutable(null);
        shell.clearArguments();
        arguments.removeAllElements();
    }

    /**
     * Clear out the arguments but leave the executable in place for another operation.
     */
    public void clearArgs() {
        arguments.removeAllElements();
    }

    /**
     *
     * <p>This marker can be used to locate a position on the commandline - to insert something for example - when all
     * parameters have been set.
     * </p>
     * @return Return a marker.
     */
    public Marker createMarker() {
        return new Marker(arguments.size());
    }

    /**
     * Sets execution directory.
     * @param path the working directory as String
     */
    public void setWorkingDirectory(String path) {
        shell.setWorkingDirectory(path);
        workingDir = new File(path);
    }

    /**
     * Sets execution directory.
     * @param workingDirectory the File used as working directory
     */
    public void setWorkingDirectory(File workingDirectory) {
        shell.setWorkingDirectory(workingDirectory);
        workingDir = workingDirectory;
    }

    public File getWorkingDirectory() {
        File workDir = shell.getWorkingDirectory();

        if (workDir == null) {
            workDir = workingDir;
        }

        return workDir;
    }

    /**
     * Executes the command.
     * @return the Process
     * @throws CommandLineException if error
     */
    public Process execute() throws CommandLineException {
        // TODO: Provided only for backward compat. with <= 1.4
        verifyShellState();

        Process process;

        // addEnvironment( "MAVEN_TEST_ENVAR", "MAVEN_TEST_ENVAR_VALUE" );

        String[] environment = getEnvironmentVariables();

        File workingDir = shell.getWorkingDirectory();

        try {
            if (workingDir == null) {
                process = Runtime.getRuntime().exec(getCommandline(), environment, workingDir);
            } else {
                if (!workingDir.exists()) {
                    throw new CommandLineException(
                            "Working directory \"" + workingDir.getPath() + "\" does not exist!");
                } else if (!workingDir.isDirectory()) {
                    throw new CommandLineException(
                            "Path \"" + workingDir.getPath() + "\" does not specify a directory.");
                }

                process = Runtime.getRuntime().exec(getCommandline(), environment, workingDir);
            }
        } catch (IOException ex) {
            throw new CommandLineException("Error while executing process.", ex);
        }

        return process;
    }

    /**
     * @deprecated Remove once backward compat with plexus-utils <= 1.4 is no longer a consideration
     */
    @Deprecated
    private void verifyShellState() {
        if (shell.getWorkingDirectory() == null) {
            shell.setWorkingDirectory(workingDir);
        }

        if (shell.getOriginalExecutable() == null) {
            shell.setExecutable(executable);
        }
    }

    public Properties getSystemEnvVars() throws Exception {
        return CommandLineUtils.getSystemEnvVars();
    }

    /**
     * Allows to set the shell to be used in this command line. Shell usage is only desirable when generating code for
     * remote execution.
     *
     * @param shell Shell to use
     * @since 1.2
     */
    public void setShell(Shell shell) {
        this.shell = shell;
    }

    /**
     * Get the shell to be used in this command line. Shell usage is only desirable when generating code for remote
     * execution.
     *
     * @since 1.2
     * @return the Shell
     */
    public Shell getShell() {
        return shell;
    }

    /**
     * @param toProcess the process
     * @return the command line arguments
     * @throws Exception if error happen
     * @deprecated Use {@link CommandLineUtils#translateCommandline(String)} instead.
     */
    @Deprecated
    public static String[] translateCommandline(String toProcess) throws Exception {
        return CommandLineUtils.translateCommandline(toProcess);
    }

    /**
     * @param argument the argument
     * @return the quote arg
     * @throws CommandLineException if error happen
     * @deprecated Use {@link CommandLineUtils#quote(String)} instead.
     */
    @Deprecated
    public static String quoteArgument(String argument) throws CommandLineException {
        return CommandLineUtils.quote(argument);
    }

    /**
     * @deprecated Use {@link CommandLineUtils#toString(String[])} instead.
     * @param line the lines
     * @return lines as single String
     */
    @Deprecated
    public static String toString(String[] line) {
        return CommandLineUtils.toString(line);
    }

    public static class Argument implements Arg
    {
        private String[] parts;

        /*
         * (non-Javadoc)
         * @see com.github.terefang.jmelange.plexus.util.cli.Argument#setValue(java.lang.String)
         */
        @Override
        public void setValue(String value) {
            if (value != null) {
                parts = new String[] {value};
            }
        }

        /*
         * (non-Javadoc)
         * @see com.github.terefang.jmelange.plexus.util.cli.Argument#setLine(java.lang.String)
         */
        @Override
        public void setLine(String line) {
            if (line == null) {
                return;
            }
            try {
                parts = CommandLineUtils.translateCommandline(line);
            } catch (Exception e) {
                System.err.println("Error translating Commandline.");
            }
        }

        /*
         * (non-Javadoc)
         * @see com.github.terefang.jmelange.plexus.util.cli.Argument#setFile(java.io.File)
         */
        @Override
        public void setFile(File value) {
            parts = new String[] {value.getAbsolutePath()};
        }

        /*
         * (non-Javadoc)
         * @see com.github.terefang.jmelange.plexus.util.cli.Argument#getParts()
         */
        @Override
        public String[] getParts() {
            return parts;
        }
    }
}
