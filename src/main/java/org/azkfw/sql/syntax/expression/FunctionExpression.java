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
package org.azkfw.sql.syntax.expression;

import java.util.ArrayList;
import java.util.List;

import org.azkfw.analysis.lexical.scanner.Token;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1>ファンクション式</h1>
 * <p>
 * 組込みSQLファンクションまたはユーザー定義ファンクションを式として使用できます。有効な組込みファンクション式の例を次に示します。
 * </p>
 * <p>
 * <b>function_expression::=</b>
 * <p><img src="./doc-files/function_expression.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/expressions008.htm#i1033636">LINK</a>
 * @author Kawakicchi
 */
public class FunctionExpression extends AbstractSyntax{

	public FunctionExpression(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		
		int start = indexOf(tokens, offset, length, "(");
		int end = lastIndexOf(tokens, offset, length, ")");

		if (-1 == start && -1 == end) {
			// ()なし
			List<SQLToken> sqlTokens = pattern01(tokens, offset, length);
			if (null != sqlTokens) {
				setSQLToken( new SQLToken(sqlTokens) );
				return true;
			}
			
		} else if (-1 != start && -1 != end) {
			if (start < end && end == offset + length -1) { // ) は一番最後
				
				List<SQLToken> sqlTokens1 = pattern01(tokens, offset, start - offset);
				if (null != sqlTokens1) {
					List<SQLToken> sqlTokens2 = pattern02(tokens, start + 1, end - (start + 1));
					if (null != sqlTokens2) {
						List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
						sqlTokens.addAll(sqlTokens1);
						sqlTokens.add( new SQLToken("(") );
						sqlTokens.addAll(sqlTokens2);
						sqlTokens.add( new SQLToken(")") );
						setSQLToken( new SQLToken(sqlTokens) );
						return true;
					}
				}
				
			}
		}
		return false;
	}
	
	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> sqlTokens = null;
		
		int start = offset;
		int end = offset + length;

		List<Integer> indexs = splitTokenEx(tokens, start, end - start, "@");
		if (0 == indexs.size()) {
			trace("未実装");
			List<SQLToken> result = new ArrayList<SQLToken>();
			result.add( new SQLToken( toString(tokens, start, end-start) ) );
			return result;
		} else if (1 == indexs.size()) {
			trace("未実装");
			List<SQLToken> result = new ArrayList<SQLToken>();
			result.add( new SQLToken( toString(tokens, start, end-start) ) );
			return result;
		}

		return null;
	}
	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> sqlTokens = null;

		if (0 == length) {
			return new ArrayList<SQLToken>();
		} else {
		
			int start = offset;
			int end = offset + length;
			if (isEqualsToken(tokens.get(offset),"DISTINCT", "ALL")) {
				start ++;
			}

			List<SQLToken> result = new ArrayList<SQLToken>();
			
			List<Integer> indexs = splitTokenEx(tokens, start, end - start, ",");
			int pattern = getPatternSize(indexs);
			for (int i = pattern - 1 ; i >= 0 ; i--) {
				boolean match = true;

				result.clear();
				
				int index1 = start;
				
				List<Integer> indexs2 = getPattern(indexs, i);
				for (int j = 0 ; j < indexs2.size() ; j++) {
					int index2 = indexs2.get(j);
					
					Expr expr = new Expr(getNestIndex());
					if (!expr.analyze(tokens, index1, index2 - index1)) {
						match = false;
						break;
					}
					result.add(expr.getSQLToken());
					
					index1 = index2 + 1;
					result.add( new SQLToken(",") );
				}
				if (match) {
					Expr expr = new Expr(getNestIndex());
					if (!expr.analyze(tokens, index1, end - index1)) {
						match = false;
					} else {
						result.add(expr.getSQLToken());
					}
				}
				
				if (match) {
					if (isEqualsToken(tokens.get(offset),"DISTINCT", "ALL")) {
						result.add(0, new SQLToken( tokens.get(offset).getToken() ));
					}

					return result;
				}
			}
		}
		return null;
	}
}
