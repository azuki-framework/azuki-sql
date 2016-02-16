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
 * <p><img src="./doc-files/select_list.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/statements_10002.htm#i2065646">LINK</a>
 * @author Kawakicchi
 */
public class SelectList extends AbstractSyntax {
	

	public SelectList() {
	}
	
	public SelectList(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));

		List<Integer> indexs = splitToken(tokens, offset, length, ",");
		if (0 == indexs.size()) {
			if (1 == length) {
				if (!startsWith(tokens, offset, length, "*")) return false;

				List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
				sqlTokens.add( new SQLToken("*"));
				setSQLToken( new SQLToken(sqlTokens));
				return true;
			} else if (3 == length) {
				if (!startsWith(tokens, offset+1, length-1, ".","*")) return false;
				
				List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
				sqlTokens.add( new SQLToken( tokens.get(offset).getToken() ));
				sqlTokens.add( new SQLToken("."));
				sqlTokens.add( new SQLToken("*"));
				setSQLToken( new SQLToken(sqlTokens));
				return true;
			} 
		} else {
			List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
			int pattern = getPatternSize(indexs);
			for (int i = pattern - 1 ; i >= 0 ; i--) {
				List<Integer> indexs2 = getPattern(indexs, i);

				sqlTokens.clear();
				boolean match = true;
				
				int i0 = offset;
				int i2 = offset + length;
				for (int j = 0 ; j < indexs2.size() ; j++) {
					int i1 = indexs2.get(j);
					
					List<SQLToken> sqlTokens1 = pattern01(tokens, i0, i1-i0);
					if (null == sqlTokens1) {
						match = false;
						break;
					}
					sqlTokens.addAll(sqlTokens1);
					
					i0 = i1 + 1;
					
					sqlTokens.add( new SQLToken(","));					
				}
				if (match) {
					List<SQLToken> sqlTokens1 = pattern01(tokens, i0, i2-i0);
					if (null == sqlTokens1) {
						continue;
					}
					sqlTokens.addAll(sqlTokens1);
				}
				
				if (match) {
					setSQLToken( new SQLToken(sqlTokens));
					return true;
				}
			}
		}
		
		return false;
	}
	
	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (3 == length && startsWith(tokens, offset+1, length-1, ".", "*")) {
			List<SQLToken> result = new ArrayList<SQLToken>();
			result.add( new SQLToken( tokens.get(offset).getToken()) );
			result.add( new SQLToken("."));
			result.add( new SQLToken("*"));
			return result;
		} else if (5 == length && startsWith(tokens, offset+1, length-1, ".") && startsWith(tokens, offset+3, length-3, ".", "*")) {
			List<SQLToken> result = new ArrayList<SQLToken>();
			result.add( new SQLToken( tokens.get(offset+0).getToken()) );
			result.add( new SQLToken("."));
			result.add( new SQLToken( tokens.get(offset+2).getToken()) );
			result.add( new SQLToken("."));
			result.add( new SQLToken("*"));	
			return result;
		} else {
			int i0 = offset;
			int i2 = offset + length;

			if (1 == length) {
				Expr expr = new Expr(getNestIndex());
				if (expr.analyze(tokens, offset, length)) {
					List<SQLToken> result = new ArrayList<SQLToken>();
					result.add(expr.getSQLToken());
					return result;
				} 
			} else if (2 == length) {
				Expr expr = null;
				expr = new Expr(getNestIndex());
				if (expr.analyze(tokens, offset, length)) {
					List<SQLToken> result = new ArrayList<SQLToken>();
					result.add(expr.getSQLToken());
					return result;
				} 
				expr = new Expr(getNestIndex());
				if (expr.analyze(tokens, offset, length-1)) {
					List<SQLToken> result = new ArrayList<SQLToken>();
					result.add(expr.getSQLToken());
					result.add( new SQLToken(tokens.get(offset+1).getToken()));
					return result;
				} 
			} else {
				Token token = tokens.get(offset+length-2);
				if (isEqualsToken(token, "AS")) {
					Expr expr = new Expr(getNestIndex());
					if (expr.analyze(tokens, offset, length-2)) {
						List<SQLToken> result = new ArrayList<SQLToken>();
						result.add(expr.getSQLToken());
						result.add( new SQLToken("AS"));
						result.add( new SQLToken(tokens.get(offset+length-1).getToken()));
						return result;
					}
				} else {
					Expr expr = null;
					expr = new Expr(getNestIndex());
					if (expr.analyze(tokens, offset, length)) {
						List<SQLToken> result = new ArrayList<SQLToken>();
						result.add(expr.getSQLToken());
						return result;
					} 
					expr = new Expr(getNestIndex());
					if (expr.analyze(tokens, offset, length-1)) {
						List<SQLToken> result = new ArrayList<SQLToken>();
						result.add(expr.getSQLToken());
						result.add( new SQLToken(tokens.get(offset+1).getToken()));
						return result;
					} 
				}
			}
			
		}
		
		return null;
	}
}
