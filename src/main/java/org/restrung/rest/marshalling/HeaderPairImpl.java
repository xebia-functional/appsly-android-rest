/*
 * Copyright (C) 2012 47 Degrees, LLC
 * http://47deg.com
 * hello@47deg.com
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

package org.restrung.rest.marshalling;

import org.apache.http.Header;

/**
 * Wrapper for http commons headers
 */
public class HeaderPairImpl implements HeaderPair {

	/**
	 * The underlying header delegate
	 */
	private Header delegate;

	/**
	 * Constructor that constructs a HeaderPairImpl of a Header
	 * @param delegate
	 */
	public HeaderPairImpl(Header delegate) {
		this.delegate = delegate;
	}

	/**
	 * @see HeaderPair#getName()
	 */
	@Override
	public String getName() {
		return delegate.getName();
	}

	/**
	 * @see HeaderPair#getValue()
	 */
	@Override
	public String getValue() {
		return delegate.getValue();
	}
}