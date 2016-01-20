/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkfw.sql.analyzer.pattern;

import java.util.regex.Pattern;

/**
 * 
 * @author Kawakicchi
 */
public class NumberTokenPattern extends AbstractTokenPattern {

	public NumberTokenPattern() {
		super("NUMBER");
	}

	public NumberTokenPattern(final String name) {
		super(name);
	}

	private static final Pattern PTN = Pattern.compile("^[a-zA-Z]$");
	
	@Override
	public Integer start(final String string, final int index) {
		if (0 < index) {
			String s = string.charAt(index-1) + "";
			if (PTN.matcher(s).matches()) {
				return null;
			}
		}
		char c = string.charAt(index);
		if ('0' == c || '1' == c || '2' == c || '3' == c || '4' == c || '5' == c || '6' == c || '7' == c || '8' == c || '9' == c) {
			return 1;
		}
		return null;
	}

	@Override
	public Integer read(final String string, final int index) {
		char c = string.charAt(index);
		if ('0' == c || '1' == c || '2' == c || '3' == c || '4' == c || '5' == c || '6' == c || '7' == c || '8' == c || '9' == c) {
			return 1;
		} else if ('.' == c) {
			return 1;
		}
		return null;
	}
}