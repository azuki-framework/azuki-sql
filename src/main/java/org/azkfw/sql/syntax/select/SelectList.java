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
package org.azkfw.sql.syntax.select;

import java.util.ArrayList;
import java.util.List;

import org.azkfw.analysis.lexical.scanner.Token;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.syntax.expression.Expr;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>select_list::=</b>
 * <p>
 * <img src="./doc-files/select_list.gif"/>
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
 *      href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/statements_10002.htm#i2065646">LINK</a>
 * @author Kawakicchi
 */
public class SelectList extends AbstractSyntax {

	public SelectList(final int index) {
		super(index);
	}

	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));

		int start = offset;
		int end = offset + length;

		if (1 == length) {
			if (endsWith(tokens, start, end - start, "*")) {
				List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
				sqlTokens.add(new SQLToken("*"));
				setSQLToken(new SQLToken(sqlTokens));
				return true;
			}
		} else if (3 == length) {
			if (endsWith(tokens, start, end - start, ".", "*")) {
				List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
				sqlTokens.add(new SQLToken(tokens.get(start).getToken())); // t_alias
				sqlTokens.add(new SQLToken("."));
				sqlTokens.add(new SQLToken("*"));
				setSQLToken(new SQLToken(sqlTokens));
				return true;
			}
		}

		List<SQLToken> result = new ArrayList<SQLToken>();

		List<Integer> indexs = splitTokenEx(tokens, start, end - start, ",");
		int pattern = getPatternSize(indexs);
		for (int i = pattern - 1; i >= 0; i--) { // 全件から

			boolean match = true;
			result.clear();
			int index1 = start;

			List<Integer> indexs2 = getPattern(indexs, i);
			for (int j = 0; j < indexs2.size(); j++) {
				int index2 = indexs2.get(j);
				List<SQLToken> sqlTokens1 = pattern01(tokens, index1, index2 - index1);
				if (null == sqlTokens1) {
					match = false;
					break;
				}
				result.addAll(sqlTokens1);

				index1 = index2 + 1;
				result.add(new SQLToken(","));
			}
			if (!match) {
				continue;
			}
			
			List<SQLToken> sqlTokens1 = pattern01(tokens, index1, end - index1);
			if (null == sqlTokens1) {
				continue;
			}
			result.addAll(sqlTokens1);

			setSQLToken(new SQLToken(result));
			return true;
		}
		return false;
	}

	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		int start = offset;
		// int end = offset + length;

		if (3 == length && endsWith(tokens, offset, length, ".", "*")) {
			List<SQLToken> result = new ArrayList<SQLToken>();
			result.add(new SQLToken(tokens.get(offset).getToken())); // query_name
																		// or
																		// table
																		// or
																		// view
																		// or
																		// materialized_view
			result.add(new SQLToken("."));
			result.add(new SQLToken("*"));
			return result;
		} else if (5 == length && isEqualsToken(tokens.get(offset + 1), ".") && endsWith(tokens, offset, length, ".", "*")) {
			List<SQLToken> result = new ArrayList<SQLToken>();
			result.add(new SQLToken(tokens.get(offset + 0).getToken())); // schema
			result.add(new SQLToken("."));
			result.add(new SQLToken(tokens.get(offset + 2).getToken())); // table
																			// or
																			// view
																			// or
																			// materialized_view
			result.add(new SQLToken("."));
			result.add(new SQLToken("*"));
			return result;
		} else {

			if (1 == length) {
				Expr expr = new Expr(getNestIndex());
				if (expr.analyze(tokens, start, length)) {
					List<SQLToken> result = new ArrayList<SQLToken>();
					result.add(expr.getSQLToken());
					return result;
				}
			} else if (2 == length) {
				Expr expr = null;
				expr = new Expr(getNestIndex());
				if (expr.analyze(tokens, start, length)) {
					List<SQLToken> result = new ArrayList<SQLToken>();
					result.add(expr.getSQLToken());
					return result;
				}
				expr = new Expr(getNestIndex());
				if (expr.analyze(tokens, start, length - 1)) {
					List<SQLToken> result = new ArrayList<SQLToken>();
					result.add(expr.getSQLToken());
					result.add(new SQLToken(tokens.get(offset + 1).getToken())); // c_alias
					return result;
				}
			} else {
				Token token = tokens.get(offset + length - 2);
				if (isEqualsToken(token, "AS")) {
					Expr expr = new Expr(getNestIndex());
					if (expr.analyze(tokens, start, length - 2)) {
						List<SQLToken> result = new ArrayList<SQLToken>();
						result.add(expr.getSQLToken());
						result.add(new SQLToken("AS"));
						result.add(new SQLToken(tokens.get(offset + length - 1).getToken())); // c_alias
						return result;
					}
				} else {
					Expr expr = null;
					expr = new Expr(getNestIndex());
					if (expr.analyze(tokens, start, length)) {
						List<SQLToken> result = new ArrayList<SQLToken>();
						result.add(expr.getSQLToken());
						return result;
					}
					expr = new Expr(getNestIndex());
					if (expr.analyze(tokens, start, length - 1)) {
						List<SQLToken> result = new ArrayList<SQLToken>();
						result.add(expr.getSQLToken());
						result.add(new SQLToken(tokens.get(offset + 1).getToken())); // c_alias
						return result;
					}
				}
			}
		}

		return null;
	}
}
