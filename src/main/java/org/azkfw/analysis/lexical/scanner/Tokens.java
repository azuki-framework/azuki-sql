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

/**
 * 
 * @author Kawakicchi
 */
public class Tokens {

	private List<Token> tokens;

	public Tokens() {
		tokens = new ArrayList<Token>();
	}

	public void add(final Token token) {
		tokens.add(token);
	}

	public List<Token> list() {
		return tokens;
	}
	
	public String toString() {
		String crlf = "\n";
		try {
			crlf = System.getProperty("line.separator");
		} catch(SecurityException e) {
		}
		
		StringBuffer string = new StringBuffer();
		for (Token token : tokens) {
			string.append(token.toString());
			string.append(crlf);
		}
		return string.toString();
	}
}
