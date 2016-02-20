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
package org.azkfw.sql.syntax.clause;

import java.util.ArrayList;
import java.util.List;

import org.azkfw.analysis.lexical.scanner.Token;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>for_update_clause::=</b>
 * <p>
 * <img src="./doc-files/for_update_clause.gif"/>
 * </p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * 
 * <pre>
 * </pre>
 * </p>
 * 
 * @see <a
 *      href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/functions004.htm#i97469">LINK</a>
 * @author Kawakicchi
 */
public class ForUpdateClause extends AbstractSyntax {

	public ForUpdateClause(final int index) {
		super(index);
	}

	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();

		if (startsWith(tokens, offset, length, "FOR", "UPDATE")) {
			sqlTokens.add(new SQLToken("FOR"));
			sqlTokens.add(new SQLToken("UPDATE"));

			if (2 == length) {
				setSQLToken(new SQLToken(sqlTokens));
				return true;

			} else {
				int start = offset + 2;
				int end = offset + length;

				boolean match = false;
				List<Integer> indexs = splitTokenEx(tokens, start, end - start, "NOWAIT", "WAIT", "SKIP");
				for (int i = 0; i < indexs.size(); i++) {
					int index = indexs.get(i);

					List<SQLToken> sqlTokens1 = pattern01(tokens, start, index - start);
					if (null == sqlTokens1) {
						continue;
					}

					List<SQLToken> sqlTokens2 = pattern02(tokens, index, end - index);
					if (null == sqlTokens2) {
						continue;
					}

					match = true;
					sqlTokens.addAll(sqlTokens1);
					sqlTokens.addAll(sqlTokens2);
					break;
				}

				if (!match) {
					List<SQLToken> sqlTokens2 = pattern02(tokens, start, end - start);
					if (null != sqlTokens2) {
						match = true;
						sqlTokens.addAll(sqlTokens2);
					}
				}
				if (!match) {
					List<SQLToken> sqlTokens1 = pattern01(tokens, start, end - start);
					if (null != sqlTokens1) {
						match = true;
						sqlTokens.addAll(sqlTokens1);
					}
				}

				if (match) {
					setSQLToken(new SQLToken(sqlTokens));
					return true;
				}
			}
		}
		return false;
	}

	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();

		if (startsWith(tokens, offset, length, "OF")) {
			result.add(new SQLToken("OF"));

			int start = offset + 1;
			int end = offset + length;
			int index1 = start;

			List<Integer> indexs = splitTokenEx(tokens, start, end - start, ",");
			for (int i = 0; i < indexs.size(); i++) {
				int index2 = indexs.get(i);

				List<SQLToken> sqlTokens = pattern0101(tokens, index1, index2 - index1);
				if (null == sqlTokens) {
					return null;
				}
				result.addAll(sqlTokens);
				
				result.add(new SQLToken(","));
				index1 = index2 + 1;
			}
			List<SQLToken> sqlTokens = pattern0101(tokens, index1, end - index1);
			if (null == sqlTokens) {
				return null;
			}
			result.addAll(sqlTokens);

			return result;
		}
		return null;
	}
	
	private List<SQLToken> pattern0101(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		
		if (1 == length) {
			result.add(new SQLToken(tokens.get(offset).getToken()));
			return result;
		} else if (3 == length) {
			if (!isEqualsToken(tokens.get(offset+1), ".")) {
				return null;
			}
			result.add(new SQLToken(tokens.get(offset).getToken()));
			result.add(new SQLToken("."));
			result.add(new SQLToken(tokens.get(offset+2).getToken()));
			return result;
		} else if (5 == length) {
			if (!isEqualsToken(tokens.get(offset+1), ".")) {
				return null;
			}
			if (!isEqualsToken(tokens.get(offset+3), ".")) {
				return null;
			}
			result.add(new SQLToken(tokens.get(offset).getToken()));
			result.add(new SQLToken("."));
			result.add(new SQLToken(tokens.get(offset+2).getToken()));
			result.add(new SQLToken("."));
			result.add(new SQLToken(tokens.get(offset+4).getToken()));
			return result;
		}
		
		return null;
	}

	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();

		if (0 == length)
			return result;

		if (1 == length) {
			if (startsWith(tokens, offset, length, "NOWAIT")) {
				result.add(new SQLToken("NOWAIT"));
				return result;
			}
		} else if (2 == length) {
			if (startsWith(tokens, offset, length, "WAIT")) {
				result.add(new SQLToken("WAIT"));
				result.add(new SQLToken(tokens.get(offset + 1).getToken()));
				return result;
			}
			if (startsWith(tokens, offset, length, "SKIP", "LOCKED")) {
				result.add(new SQLToken("SKIP"));
				result.add(new SQLToken("LOCKED"));
				return result;
			}
		}

		return null;
	}
}
