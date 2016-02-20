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
import org.azkfw.sql.syntax.expression.Expr;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>order_by_clause::=</b>
 * <p><img src="./doc-files/order_by_clause.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/functions004.htm#i97469">LINK</a>
 * @author Kawakicchi
 */
public class OrderByClause extends AbstractSyntax {
	
	public static final String KW_ORDER = "ORDER";
	
	public OrderByClause(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();

		int start = offset;
		int end = offset + length;
		
		if (startsWith(tokens, offset, length, KW_ORDER, "SIBLINGS", "BY")) {
			sqlTokens.add( new SQLToken(KW_ORDER) );
			sqlTokens.add( new SQLToken("SIBLINGS") );
			sqlTokens.add( new SQLToken("BY") );
			start = offset + 3;
			
		} else if (startsWith(tokens, offset, length, KW_ORDER, "BY")) {
			sqlTokens.add( new SQLToken(KW_ORDER) );
			sqlTokens.add( new SQLToken("BY") );
			start = offset + 2;

		} else {
			return false;
		}
		
		List<SQLToken> sqlTokens1 = pattern01(tokens, start, end - start);
		if(null == sqlTokens1) {
			return false;
		}
		sqlTokens.addAll(sqlTokens1);

		setSQLToken(new SQLToken(sqlTokens));
		return true;
	}
	
	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();

		int start = offset;
		int end = offset + length;	

		List<Integer> indexs = splitTokenEx(tokens, offset, length, ",");
		int pattern = getPatternSize(indexs);
		for (int i = pattern - 1 ; i >= 0 ; i--) {
			List<Integer> indexs2 = getPattern(indexs, i);

			result.clear();
			boolean match = true;
			int index1 = start;

			for (int j = 0 ; j < indexs2.size() ; j++) {
				int index2 = indexs2.get(j);

				List<SQLToken> sqlTokens1 = pattern0101(tokens, index1, index2 - index1);
				if (null == sqlTokens1) {
					match = false;
					break;
				}
				result.addAll(sqlTokens1);

				result.add( new SQLToken(","));
				index1 = index2 + 1;
			}
			if (!match) {
				continue;
			}

			List<SQLToken> sqlTokens1 = pattern0101(tokens, index1, end - index1);
			if (null == sqlTokens1) {
				continue;
			}
			result.addAll(sqlTokens1);

			return result;
		}
		return null;
	}

	private List<SQLToken> pattern0101(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		
		int start = offset;
		int end = offset + length;
		
		if (endsWith(tokens, start, end - start, "NULLS", "FIRST")) {
			result.add(new SQLToken("NULLS"));
			result.add(new SQLToken("FIRST"));
			end -= 2;
		} else if (endsWith(tokens, start, end - start, "NULLS", "LAST")) {
			result.add(new SQLToken("NULLS"));
			result.add(new SQLToken("LAST"));
			end -= 2;			
		}
		if (endsWith(tokens, start, end - start, "ASC")) {
			result.add(0, new SQLToken("ASC"));
			end -= 1;
		} else if (endsWith(tokens, start, end - start, "DESC")) {
			result.add(0, new SQLToken("DESC"));
			end -= 1;			
		}
		
		if (0 == end - start) {
			return null;
		} else if (1 == end - start) {
			// position or c_alias
			result.add(0, new SQLToken(tokens.get(offset).getToken()));
		} else {
			//
			Expr expr = new Expr(getNestIndex());
			if (!expr.analyze(tokens, start, end - start)) {
				return null;
			}
			result.add(0, expr.getSQLToken());
		}
		return result;
	}
}
