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
import org.azkfw.analysis.lexical.scanner.pattern.OracleHintTokenPattern;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.syntax.clause.JoinClause;
import org.azkfw.sql.syntax.clause.SubqueryFactoringClause;
import org.azkfw.sql.syntax.clause.WhereClause;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>query_block::=</b>
 * <p><img src="./doc-files/query_block.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/statements_10002.htm#i2065646">LINK</a>
 * @author Kawakicchi
 */
public class QueryBlock extends AbstractSyntax {
	
	public QueryBlock() {
	}
	
	public QueryBlock(final int index) {
		super(index);
	}
	
	// select ~ group by
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
		
		int i0 = offset;
		int i3 = offset + length;		

		List<Integer> indexs1 = splitToken(tokens, offset, length, "SELECT");
		for (int i = 0 ; i < indexs1.size() ; i++) {
			int i1 = indexs1.get(i);
			
			sqlTokens.clear();

			List<SQLToken> sqlTokens1 = pattern01(tokens, i0, i1-i0);
			if (null == sqlTokens1) {
				continue;
			}
			sqlTokens.addAll(sqlTokens1);
			sqlTokens.add(new SQLToken("SELECT"));
			
			i1 ++;
			List<Integer> indexs2 = splitToken(tokens, i1, i3-i1, "FROM");
			for (int j = 0 ; j < indexs2.size() ; j++) {
				int i2 = indexs2.get(j);
				
				List<SQLToken> sqlTokens2 = pattern02(tokens, i1, i2-i1);
				if (null == sqlTokens2) {
					continue;
				}

				i2 ++;

				List<SQLToken> sqlTokens3 = pattern03(tokens, i2, i3-i2);
				if (null == sqlTokens3) {
					continue;
				}

				sqlTokens.addAll(sqlTokens2);
				sqlTokens.add( new SQLToken("FROM") );
				sqlTokens.addAll(sqlTokens3);
				setSQLToken( new SQLToken(sqlTokens) );
				return true;
			}
		}
		
		return false;
	}

	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {
			SubqueryFactoringClause clause = new SubqueryFactoringClause();
			if(!clause.analyze(tokens, offset, length)) {
				return null;
			}
			result.add(clause.getSQLToken());
		}
		return result;
	}

	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {		
		List<SQLToken> result = new ArrayList<SQLToken>();
		
		Token token = null;
		int i0 = offset;
		int i9 = offset + length;		

		if (i0 >= offset + length) return null;
		
		token = tokens.get(i0);
		if (OracleHintTokenPattern.NAME.equals(token.getType())) {
			result.add( new SQLToken(token.getToken()) );
			i0 ++;
		}
		
		if (i0 >= offset + length) return null;
		
		token = tokens.get(i0);
		if (isEqualsToken(token, "DISTINCT", "UNIQUE", "ALL")) {
			result.add( new SQLToken(token.getToken()) );
			i0 ++;
		}
		
		List<SQLToken> sqlTokens1 = pattern0201(tokens, i0, i9-i0);
		if (null == sqlTokens1) {
			return null;
		}
		result.addAll(sqlTokens1);
		
		return result;
	}
	
	private List<SQLToken> pattern0201(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		SelectList selectList = new SelectList(getNestIndex());
		if (!selectList.analyze(tokens, offset, length)) {
			return null;
		}
		result.add(selectList.getSQLToken());
		return result;
	}
	
	private List<SQLToken> pattern03(final List<Token> tokens, final int offset, final int length) throws SyntaxException {		
		int i0 = offset;
		int i5 = offset + length;

		for (int i4 = i0 ; i4 <= i5 ; i4++ ) {
			List<SQLToken> sqlTokens5 = pattern0305(tokens, i4, i5-i4);
			if (null == sqlTokens5) continue;

			for (int i3 = i0 ; i3 <= i4 ; i3++ ) {
				List<SQLToken> sqlTokens4 = pattern0304(tokens, i3, i4-i3);
				if (null == sqlTokens4) continue;
				
				for (int i2 = i0 ; i2 <= i3 ; i2++ ) {
					List<SQLToken> sqlTokens3 = pattern0303(tokens, i2, i3-i2);
					if (null == sqlTokens3) continue;
					
					for (int i1 = i0 ; i1 <= i2 ; i1++ ) {
						List<SQLToken> sqlTokens2 = pattern0302(tokens, i1, i2-i1);
						if (null == sqlTokens2) continue;
						List<SQLToken> sqlTokens1 = pattern0301(tokens, i0, i1-i0);
						if (null == sqlTokens1) continue;

						List<SQLToken> result = new ArrayList<SQLToken>();
						result.addAll(sqlTokens1);
						result.addAll(sqlTokens2);
						result.addAll(sqlTokens3);
						result.addAll(sqlTokens4);
						result.addAll(sqlTokens5);
						return result;
					}
				}
			}
		}
		return null;
	}
	
	private List<SQLToken> pattern0301(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (0 == length) return null;
		
		List<SQLToken> result = new ArrayList<SQLToken>();
		
		List<Integer> indexs = splitToken(tokens, offset, length, ",");
		int pattern = getPatternSize(indexs);
		for (int i = 0 ; i < pattern ; i++) {
			boolean match = true;

			result.clear();
			
			int i0 = offset;
			int i9 = offset + length;
			
			List<Integer> indexs2 = getPattern(indexs, i);			
			for (int j = 0 ; j < indexs2.size() ; j++) {
				int i1 = indexs2.get(j);
				List<SQLToken> sqlTokens1 = pattern030101(tokens, i0, i1 - i0);
				if (null == sqlTokens1) {
					match = false;
					break;
				}
				result.addAll(sqlTokens1);
				
				i0 = i1+1;
				result.add( new SQLToken(",") );
			}
			if (match) {
				List<SQLToken> sqlTokens1 = pattern030101(tokens, i0, i9 - i0);
				if (null == sqlTokens1) {
					match = false;
				} else {
					result.addAll(sqlTokens1);
				}
			}
			
			if (match) {
				return result;
			}
		}
		return null;
	}
	
	private List<SQLToken> pattern0302(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {
			
			WhereClause clause = new WhereClause(getNestIndex());
			if (!clause.analyze(tokens, offset, length)) {
				return null;
			}
			result.add(clause.getSQLToken());
		}
		return result;
	}
	private List<SQLToken> pattern0303(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {
			if (!startsWith(tokens, offset, length, "CONNECT", "START")) return null;
			
			// TODO: hierarchical_query_clause
			result.add( new SQLToken( toString(tokens, offset, length) ) );
		}
		return result;
	}
	private List<SQLToken> pattern0304(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {
			if (!startsWith(tokens, offset, length, "GROUP")) return null;
			
			// TODO: group_by_clause
			result.add( new SQLToken( toString(tokens, offset, length) ) );
		}
		return result;
	}
	private List<SQLToken> pattern0305(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {
			if (!startsWith(tokens, offset, length, "MODEL")) return null;
			
			// TODO: model_clause
			result.add( new SQLToken( toString(tokens, offset, length) ) );
		}
		return result;
	}
	
	private List<SQLToken> pattern030101(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (0 == length) return null;
		
		List<SQLToken> result = new ArrayList<SQLToken>();
		
		TableReference tableReference = new TableReference(getNestIndex());
		if (tableReference.analyze(tokens, offset, length)) {
			result.add(tableReference.getSQLToken());
			return result;
		}
		
		JoinClause clause1 = new JoinClause(getNestIndex());
		if (clause1.analyze(tokens, offset, length)) {
			result.add(clause1.getSQLToken());
			return result;
		}
		
		if ( startsWith(tokens, offset, length, "(") && endsWith(tokens, offset, length, ")") ) {
			JoinClause clause2 = new JoinClause();
			if (clause2.analyze(tokens, offset+1, length-2)) {
				
				result.add( new SQLToken("(") );
				result.add(clause2.getSQLToken());
				result.add( new SQLToken(")") );
				return result;
			}			
		}
		
		return null;
	}
}
