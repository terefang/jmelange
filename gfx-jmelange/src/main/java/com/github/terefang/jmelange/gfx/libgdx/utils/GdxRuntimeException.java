/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.github.terefang.jmelange.gfx.libgdx.utils;

/** Typed runtime exception used throughout libGDX
 * 
 * @author mzechner */
public class GdxRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 6735854402467673117L;

	public GdxRuntimeException (String message) {
		super(message);
	}

	public GdxRuntimeException (Throwable t) {
		super(t);
	}

	public GdxRuntimeException (String message, Throwable t) {
		super(message, t);
	}

	public static GdxRuntimeException from(String message, Throwable t)
	{
		return new GdxRuntimeException(message, t);
	}

	public static GdxRuntimeException from(String message)
	{
		return new GdxRuntimeException(message);
	}

	public static GdxRuntimeException from(Throwable t)
	{
		return new GdxRuntimeException(t);
	}
}
