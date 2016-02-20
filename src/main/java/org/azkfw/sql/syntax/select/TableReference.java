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
import org.azkfw.sql.syntax.clause.FlashbackQueryClause;
import org.azkfw.sql.syntax.clause.PivotClause;
import org.azkfw.sql.syntax.clause.UnpivotClause;
import org.azkfw.sql.syntax.expression.QueryTableExpression;
import org.azkfw.sql.token.SQLToken;

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
 * <p>
 * <ul>
 * <li>{@link QueryTableExpression}</li>
 * <li>{@link PivotClause}</li>
 * <li>{@link UnpivotClause}</li>
 * <li>{@link FlashbackQueryClause}</li>
 * </ul>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/statements_10002.htm#i2065646">LINK</a>
 * @author Kawakicchi
 */
public class TableReference extends AbstractSyntax {
	
	public static final String KW_ONLY = "ONLY";

	public TableReference(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));

		List<SQLToken> sqlTokens = pattern(tokens, offset, length);
		if (null != sqlTokens) {
			setSQLToken( new SQLToken(sqlTokens) );
			return true;
		}
		sqlTokens = pattern(tokens, offset, length - 1);
		if (null != sqlTokens) {
			setSQLToken( new SQLToken(sqlTokens) );
			setSQLToken( new SQLToken(tokens.get(offset + length - 1).getToken() ) );
			return true;
		}

		return false;
	}
	
	private List<SQLToken> pattern(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		int start = offset;
		int end = offset + length;

		List<Integer> indexs = splitTokenEx(tokens, start, end - start, FlashbackQueryClause.KW_VERSIONS, FlashbackQueryClause.KW_AS);
		for (int i = indexs.size() - 1 ; i >= 0 ; i--) {
			int index = indexs.get(i);
			List<SQLToken> sqlTokens = pattern01(tokens, start, index - start);
			if (null == sqlTokens) {
				continue;
			}
			FlashbackQueryClause flashback = new FlashbackQueryClause(getNestIndex());
			if (!flashback.analyze(tokens, index, end - index)) {
				continue;
			}

			List<SQLToken> result = new ArrayList<SQLToken>();
			result.addAll(sqlTokens);
			result.add(flashback.getSQLToken());
			return result;
		}
		List<SQLToken> result = pattern01(tokens, start, end - start);
		if (null != result) {
			return result;
		}
		
		return null;
	}
	
	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		int start = offset;
		int end = offset + length;
		
		if (startsWith(tokens, start, end - start, KW_ONLY, "(")) {
			return pattern0101(tokens, start, end - start);
		} else {
			return pattern0102(tokens, start, end - start);			
		}
	}

	private List<SQLToken> pattern0101(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		int start = offset;
		int end = offset + length;

		if (startsWith(tokens, start, end - start, KW_ONLY, "(") && endsWith(tokens, start, end - start, ")")) {
			QueryTableExpression queryTable = new QueryTableExpression(getNestIndex());
			if (queryTable.analyze(tokens, start + 2, end - (start + 2) - 1)) {
				List<SQLToken> result = new ArrayList<SQLToken>();
				result.add( new SQLToken(KW_ONLY) ) ;
				result.add( new SQLToken("(") ) ;
				result.add( queryTable.getSQLToken() ) ;
				result.add( new SQLToken(")") ) ;
			}
		}
		return null;
	}
	
	private List<SQLToken> pattern0102(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		int start = offset;
		int end = offset + length;

		List<Integer> indexs = splitTokenEx(tokens, start, end - start, PivotClause.KW_PIVOT, UnpivotClause.KW_UNPIVOT);
		if (0 == indexs.size()) {
			QueryTableExpression queryTable = new QueryTableExpression(getNestIndex());
			if (queryTable.analyze(tokens, start, end-start)) {
				List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
				sqlTokens.add(queryTable.getSQLToken());
				return sqlTokens;
			}
		} else {
			for (int i = 0 ; i < indexs.size() ; i++) {
				int index = indexs.get(i);
				
				if (startsWith(tokens, index, 1, PivotClause.KW_PIVOT)) {
					PivotClause pivot = new PivotClause(getNestIndex());
					if (!pivot.analyze(tokens, index, end - index)) {
						continue;
					}
					QueryTableExpression queryTable = new QueryTableExpression(getNestIndex());
					if (!queryTable.analyze(tokens, start, index-start)) {
						continue;
					}
					List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
					sqlTokens.add(queryTable.getSQLToken());
					sqlTokens.add(pivot.getSQLToken());
					return sqlTokens;
				} else if (startsWith(tokens, index, 1, UnpivotClause.KW_UNPIVOT)) {
					UnpivotClause unpivot = new UnpivotClause(getNestIndex());
					if (!unpivot.analyze(tokens, index, end - index)) {
						continue;
					}
					QueryTableExpression queryTable = new QueryTableExpression(getNestIndex());
					if (!queryTable.analyze(tokens, start, index-start)) {
						continue;
					}
					List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
					sqlTokens.add(queryTable.getSQLToken());
					sqlTokens.add(unpivot.getSQLToken());
					return sqlTokens;
				}
				
			}
		}
		return null;
	}
}
