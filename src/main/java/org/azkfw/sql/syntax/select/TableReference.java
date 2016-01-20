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

import org.azkfw.sql.analyzer.SQLToken;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.syntax.clause.FlashbackQueryClause;
import org.azkfw.sql.syntax.clause.PivotClause;
import org.azkfw.sql.syntax.clause.UnpivotClause;
import org.azkfw.sql.syntax.expression.QueryTableExpression;
import org.azkfw.sql.token.Token;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>table_reference::=</b>
 * <p><img src="./doc-files/table_reference.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/statements_10002.htm#i2065646">LINK</a>
 * @author Kawakicchi
 */
public class TableReference extends AbstractSyntax {
	
	
	public TableReference() {
	}
	
	public TableReference(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		

		int i0 = offset;
		int i3 = offset + length;
		for (int i1 = i0 ; i1 <= i3 ; i1++) {
			List<SQLToken> sqlTokens1 = pattern01(tokens, i0, i1-i0);
			if (null == sqlTokens1) continue;
			
			for (int i2 = i1 ; i2 <= i3 ; i2++) {
				List<SQLToken> sqlTokens2 = pattern02(tokens, i1, i2-i1);
				if (null == sqlTokens2) continue;
				
				List<SQLToken> sqlTokens3 = pattern03(tokens, i2, i3-i2);
				if (null == sqlTokens3) continue;
				
				List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
				sqlTokens.addAll(sqlTokens1);
				sqlTokens.addAll(sqlTokens2);
				sqlTokens.addAll(sqlTokens3);
				setSQLToken( new SQLToken(sqlTokens) );
				return true;
			}
		}
		return false;
	}

	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = null;
		result = pattern0101(tokens, offset, length);
		if (null != result) {
			return result;
		}
		result = pattern0102(tokens, offset, length);
		if (null != result) {
			return result;
		}
		return null;
	}
	private List<SQLToken> pattern0101(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (4 > length) return null;
		if (!startsWith(tokens, offset, length, "ONLY", "(")) return null;
		if (!endsWith(tokens, offset, length, ")")) return null;
		
		QueryTableExpression expr = new QueryTableExpression(getNestIndex());
		if (!expr.analyze(tokens, offset+2, length-3)) return null;
		
		List<SQLToken> result = new ArrayList<SQLToken>();
		result.add( new SQLToken("ONLY"));
		result.add( new SQLToken("("));
		result.add(expr.getSQLToken());
		result.add( new SQLToken(")"));
		return result;
	}
	private List<SQLToken> pattern0102(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (0 == length) return null;
		
		List<SQLToken> result = new ArrayList<SQLToken>();
		
		int i0 = offset;
		int i2 = offset + length;
		for (int i1 = i2 ; i1 > i0 ; i1--) {
			result.clear();
			
			QueryTableExpression expr = new QueryTableExpression(getNestIndex());
			if (!expr.analyze(tokens, i0, i1-i0)) continue;
			result.add(expr.getSQLToken());
			
			if (i1 != i2) {
				PivotClause clause1 = new PivotClause(getNestIndex());
				if(!clause1.analyze(tokens, i1, i2-i1)) {
					UnpivotClause clause2 = new UnpivotClause(getNestIndex());
					if (!clause2.analyze(tokens, i1, i2-i1)) {
						continue;
					} else {
						result.add(clause2.getSQLToken());
					}
				} else {
					result.add(clause1.getSQLToken());
				}
			}
			return result;
		}
		return null;
	}
	
	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {
			FlashbackQueryClause clause = new FlashbackQueryClause(getNestIndex());
			if (!clause.analyze(tokens, offset, length)) return null;
			result.add(clause.getSQLToken());
		}
		return result;
	}
	private List<SQLToken> pattern03(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {
			if (1 != length) return null;
			result.add( new SQLToken( tokens.get(offset).getToken() ) );
		}
		return result;
	}
}
