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

package com.github.terefang.jmelange.apache.net.daytime;

import java.io.IOException;

import com.github.terefang.jmelange.apache.io.IOUtils;
import com.github.terefang.jmelange.apache.net.SocketClient;

/**
 * The DaytimeTCPClient class is a TCP implementation of a client for the Daytime protocol described in RFC 867. To use the class, merely establish a connection
 * with {@link SocketClient#connect connect } and call {@link #getTime getTime() } to retrieve the daytime string, then call
 * {@link SocketClient#disconnect disconnect } to close the connection properly.
 *
 * @see DaytimeUDPClient
 */
public final class DaytimeTCPClient extends SocketClient {

    /**
     * The default daytime port {@value} per RFC 867.
     */
    public static final int DEFAULT_PORT = 13;

    /**
     * The default DaytimeTCPClient constructor. It merely sets the default port to <code>DEFAULT_PORT</code>.
     */
    public DaytimeTCPClient() {
        setDefaultPort(DEFAULT_PORT);
    }

    /**
     * Gets the time string from the server and returns it. The server will have closed the connection at this point, so you should call
     * {@link SocketClient#disconnect disconnect } after calling this method. To retrieve another time, you must initiate another
     * connection with {@link SocketClient#connect connect } before calling {@code getTime()} again.
     *
     * @return The time string retrieved from the server.
     * @throws IOException If an error occurs while fetching the time string.
     */
    public String getTime() throws IOException {
        return IOUtils.toString(_input_, getCharset());
    }

}
