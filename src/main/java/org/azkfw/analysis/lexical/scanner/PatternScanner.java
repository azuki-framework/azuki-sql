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
package org.azkfw.analysis.lexical.scanner;

import java.util.ArrayList;
import java.util.List;

import org.azkfw.analysis.lexical.scanner.pattern.TokenPattern;

/**
 * @author Kawakicchi
 *
 */
public class PatternScanner extends AbstractScanner {

	/** */
	private List<TokenPattern> patterns;

	public PatternScanner() {
		patterns = new ArrayList<TokenPattern>();
	}

	public final void addPattern(final TokenPattern pattern) {
		patterns.add(pattern);
	}

	@Override
	protected Tokens doScan(final String string) {
		Tokens tokens = new Tokens();

		TokenPattern pattern = null;
		StringBuilder buffer = new StringBuilder();
		int startIndex = 0;

		for (int index = 0; index < string.length();) {
			if (null == pattern) {
				for (TokenPattern tp : patterns) {
					Integer size = tp.start(string, index);
					if (null != size) {

						if (0 < buffer.length()) {
							tokens.add(new Token(startIndex, buffer.toString()));
							buffer = new StringBuilder();
							startIndex = index;
						}

						if (0 < size) {
							buffer.append(string.substring(index, index + size));
						}
						index += size;
						pattern = tp;
						break;
					}
				}

				if (null == pattern) {
					buffer.append(string.charAt(index));
					index++;
				}
			} else {
				Integer size = null;
				while (null != (size = pattern.read(string, index))) {
					if (0 < size) {
						buffer.append(string.substring(index, index + size));
						index += size;
					}
					if (index >= string.length()) {
						break;
					}
				}
				if (0 < buffer.length()) {
					tokens.add(new Token(startIndex, buffer.toString(), pattern.getName()));
					buffer = new StringBuilder();
					startIndex = index;
				}
				pattern = null;
			}
		}
		if (0 < buffer.length()) {
			if (null != pattern) {
				tokens.add(new Token(startIndex, buffer.toString(), pattern.getName()));
			} else {
				tokens.add(new Token(startIndex, buffer.toString()));
			}
		}
		
		return tokens;
	}

}
