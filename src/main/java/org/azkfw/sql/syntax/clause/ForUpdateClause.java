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

import org.azkfw.sql.analyzer.SQLToken;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.token.Token;

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

	public ForUpdateClause() {
	}

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
				int i0 = offset + 2;
				int i2 = offset + length;

				boolean match = false;
				List<Integer> indexs = splitToken(tokens, i0, i2 - i0, "NOWAIT", "WAIT", "SKIP");
				for (int i = 0; i < indexs.size(); i++) {
					int i1 = indexs.get(i);

					List<SQLToken> sqlTokens1 = pattern01(tokens, i0, i1 - i0);
					if (null == sqlTokens1) {
						continue;
					}

					List<SQLToken> sqlTokens2 = pattern02(tokens, i1, i2 - i1);
					if (null == sqlTokens2) {
						continue;
					}

					match = true;
					sqlTokens.addAll(sqlTokens1);
					sqlTokens.addAll(sqlTokens2);
					break;
				}

				if (!match) {
					List<SQLToken> sqlTokens2 = pattern02(tokens, i0, i2 - i0);
					if (null != sqlTokens2) {
						match = true;
						sqlTokens.addAll(sqlTokens2);
					}
				}
				if (!match) {
					List<SQLToken> sqlTokens1 = pattern01(tokens, i0, i2 - i0);
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
		trace(toString(tokens, offset, length));
		List<SQLToken> result = new ArrayList<SQLToken>();

		if (startsWith(tokens, offset, length, "OF")) {
			result.add(new SQLToken("OF"));

			int i0 = offset + 1;
			int i2 = offset + length;
			int i1 = i0;

			List<Integer> indexs = splitToken(tokens, offset + 1, length - 1, ",");
			for (int i = 0; i < indexs.size(); i++) {
				int i9 = indexs.get(i);

				result.add(new SQLToken(","));
				result.add(new SQLToken(toString(tokens, i1, i9 - i1)));
				i1 = i9 + 1;
			}
			result.add(new SQLToken(toString(tokens, i1, i2 - i1)));

			return result;
		}
		return null;
	}

	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
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
