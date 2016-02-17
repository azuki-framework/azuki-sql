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
package org.azkfw.analysis.lexical.scanner.pattern;

import java.util.regex.Pattern;


/**
 * 
 * @author Kawakicchi
 */
public class OracleWordTokenPattern extends AbstractTokenPattern {

	public static final String NAME = "Oracle word";

	private static final Pattern PTN1 = Pattern.compile("^[_a-zA-Z]$");
	private static final Pattern PTN2 = Pattern.compile("^[_a-zA-Z0-9]$");

	private boolean endFlag;

	public OracleWordTokenPattern() {
		super(NAME);
	}

	protected OracleWordTokenPattern(final String name) {
		super(name);
	}
	
	@Override
	public Integer start(final String string, final int index) {
		Integer result = null;

		String s = Character.toString(string.charAt(index));
		if (PTN1.matcher(s).matches()) {
			endFlag = false;
			result = 1;
		}

		return result;
	}

	@Override
	public Integer read(final String string, final int index) {
		Integer result = null;
		if (!endFlag) {
			String s = Character.toString(string.charAt(index));
			if (PTN2.matcher(s).matches()) {
				result = 1;
			} else {
				endFlag = true;
			}
		}
		return result;
	}
}