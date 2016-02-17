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


/**
 * 
 * @author Kawakicchi
 */
public class StringTokenPattern extends AbstractTokenPattern {

	public static final String NAME = "String";

	private boolean endFlag;

	private int type;
	private static final int TYPE_DOUBLE = 1;
	private static final int TYPE_SINGLE = 2;

	public StringTokenPattern() {
		super(NAME);
		type = 0;
	}
	
	protected StringTokenPattern(final String name) {
		super(name);
		type = 0;
	}
	
	@Override
	public Integer start(final String string, final int index) {
		Integer result = null;
		if ('\'' == string.charAt(index)) {
			type = TYPE_SINGLE;
			endFlag = false;
			result = 1;
		} else if ('"' == string.charAt(index)) {
			type = TYPE_DOUBLE;
			endFlag = false;
			result = 1;
		}
		return result;
	}

	@Override
	public Integer read(final String string, final int index) {
		Integer result = null;
		if (!endFlag) {
			if (type == TYPE_SINGLE) {
				if ('\'' == string.charAt(index)) {
					if (string.length() > index + 1) {
						if ('\'' == string.charAt(index + 1)) {
							result = 2;
						} else {
							endFlag = true;
							result = 1;
						}
					} else {
						// これ以上データなし
						endFlag = true;
						result = 1;
					}
				} else {
					result = 1;
				}
			} else if (type == TYPE_DOUBLE) {
				if ('"' == string.charAt(index)) {
					if (string.length() > index + 1) {
						if ('"' == string.charAt(index + 1)) {
							result = 2;
						} else {
							endFlag = true;
							result = 1;
						}
					} else {
						// これ以上データなし
						endFlag = true;
						result = 1;
					}
				} else {
					result = 1;
				}
			}
		}
		return result;
	}
}