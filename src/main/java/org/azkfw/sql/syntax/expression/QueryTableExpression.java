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
import org.azkfw.sql.syntax.clause.SubqueryRestrictionClause;
import org.azkfw.sql.syntax.select.Subquery;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>query_table_expression::=</b>
 * <p><img src="./doc-files/query_table_expression.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/expressions002.htm#i1033613">LINK</a>
 * @author Kawakicchi
 */
public class QueryTableExpression extends AbstractSyntax {

	public QueryTableExpression(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));

		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
		
		sqlTokens = pattern01(tokens, offset, length);
		if (null != sqlTokens) {
			setSQLToken( new SQLToken(sqlTokens) );
			return true;
		}
		sqlTokens = pattern02(tokens, offset, length);
		if (null != sqlTokens) {
			setSQLToken( new SQLToken(sqlTokens) );
			return true;
		}
		sqlTokens = pattern03(tokens, offset, length);
		if (null != sqlTokens) {
			setSQLToken( new SQLToken(sqlTokens) );
			return true;
		}
		sqlTokens = pattern04(tokens, offset, length);
		if (null != sqlTokens) {
			setSQLToken( new SQLToken(sqlTokens) );
			return true;
		}
		
		return false;
	}
	
	/**
	 * = query_name
	 * @param tokens
	 * @param offset
	 * @param length
	 * @return
	 * @throws SyntaxException
	 */
	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (1 == length) {
			List<SQLToken> result = new ArrayList<SQLToken>();
			result.add( new SQLToken(tokens.get(offset).getToken()) );
			return result;
		}
		return null;
	}
	
	/**
	 * 
	 * @param tokens
	 * @param offset
	 * @param length
	 * @return
	 * @throws SyntaxException
	 */
	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		// TODO:
		trace("未定義");
		return null;
	}
	
	/**
	 * 
	 * @param tokens
	 * @param offset
	 * @param length
	 * @return
	 * @throws SyntaxException
	 */
	private List<SQLToken> pattern03(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (3 >= length) return null;
		if (!startsWith(tokens, offset, length, "(")) return null;
		if (!  endsWith(tokens, offset, length, ")")) return null;

		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();

		int i0 = offset + 1;
		int i2 = offset + length - 1;
		for (int i1 = i2 ; i1 > i0 ; i1--) {
			sqlTokens.clear();
			sqlTokens.add( new SQLToken("(") );

			Subquery subquery = new Subquery(this, 0, getNestIndex());
			if (!subquery.analyze(tokens, i0, i1-i0)) continue;
			sqlTokens.add(subquery.getSQLToken());

			if (i1 != i2) {
				SubqueryRestrictionClause clause = new SubqueryRestrictionClause(getNestIndex());
				if(!clause.analyze(tokens, i1, i2-i1)) {
					continue;
				}
				sqlTokens.add(clause.getSQLToken());
			}
			
			sqlTokens.add( new SQLToken(")") );
			return sqlTokens;			
		}
		
		return null;
	}
	
	private List<SQLToken> pattern04(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (0 == length) return null;
		
		TableCollectionExpression expr = new TableCollectionExpression(getNestIndex());
		if (!expr.analyze(tokens, offset, length)) return null;
		
		List<SQLToken> result = new ArrayList<SQLToken>();
		result.add( expr.getSQLToken() );
		return result;
	}

}
