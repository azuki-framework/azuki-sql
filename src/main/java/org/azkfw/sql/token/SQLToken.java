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
package org.azkfw.sql.token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Kawakicchi
 *
 */
public class SQLToken {

	private String value;
	private List<SQLToken> tokens;
	
	public SQLToken() {
		value = null;
		tokens = new ArrayList<SQLToken>();
	}
	
	public SQLToken(final List<SQLToken> tokens) {
		value = null;
		this.tokens = tokens;
	}
		
	public SQLToken(final String value) {
		this.value = value;
		tokens = new ArrayList<SQLToken>();
	}
	
	public void addToken(final SQLToken token) {
		tokens.add(token);
	}
	
	public void addTokens(final Collection<SQLToken> tokens) {
		tokens.addAll(tokens);
	}
	
	public String getValue() {
		return value;
	}
	
	public List<SQLToken> getTokens() {
		return tokens;
	}
	
	public String toString() {
		return toString(0);
	}
	
	public String toString(int indent) {
		StringBuffer s = new StringBuffer();
		String space = getSpace(indent*4);
		if (null != value) {
			s.append(space);
			s.append(value);
			s.append("\n");
		} else {
			for (SQLToken token : tokens) {
				if (null != token) {
					s.append(token.toString(indent+1));
				} else {
					s.append("(null)\n");
				}
			}
		}
		return s.toString();
	}
	
	private String getSpace(final int length) {
		StringBuffer s = new StringBuffer();
		for (int i = 0 ; i < length ; i++) {
			s.append(" ");
		}
		return s.toString();
	}
}
