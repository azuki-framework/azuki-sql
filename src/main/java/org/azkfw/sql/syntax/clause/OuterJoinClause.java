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
import org.azkfw.sql.syntax.AbstractSQLSyntax;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.syntax.condition.Condition;
import org.azkfw.sql.syntax.select.TableReference;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>outer_join_clause::=</b>
 * <p><img src="./doc-files/outer_join_clause.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * <p>
 * <ul>
 * <li>{@link QueryPartitionClause}</li>
 * <li>{@link OuterJoinType}</li>
 * <li>{@link TableReference}</li>
 * <li>{@link Condition}</li>
 * </ul>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/statements_9014.htm#i2080498">LINK</a>
 * @author Kawakicchi
 */
public class OuterJoinClause extends AbstractSQLSyntax{
	
	public static final String KW_NATURAL = "NATURAL";
	public static final String KW_JOIN = "JOIN";
	public static final String KW_ON = "ON";
	public static final String KW_USING = "USING";

	public OuterJoinClause(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		
		List<Integer> indexs1 = splitTokenEx(tokens, offset, length, KW_JOIN);
		for (int i = 0 ; i < indexs1.size() ; i++) {
			int index = indexs1.get(i);
			
			List<SQLToken> sqlTokens1 = pattern01(tokens, offset, index - offset);
			if (null == sqlTokens1) {
				continue;
			}
			List<SQLToken> sqlTokens2 = pattern02(tokens, index + 1, offset + length - (index + 1));
			if (null == sqlTokens2) {
				continue;
			}
			
			List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
			sqlTokens.addAll(sqlTokens1);
			sqlTokens.add( new SQLToken(KW_JOIN) );
			sqlTokens.addAll(sqlTokens2);
			setSQLToken( new SQLToken(sqlTokens) );
			return true;
		}
		
		return false;
	}
	
	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		int start = offset;
		int end = offset + length;
		
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
		
		List<Integer> indexs = splitTokenEx(tokens, start, end - start, OuterJoinType.KW_FULL, OuterJoinType.KW_LEFT, OuterJoinType.KW_RIGHT);
		for (int i = indexs.size() - 1; i >= 0 ; i--) {
			int index = indexs.get(i);
			
			sqlTokens.clear();
			
			OuterJoinType type = new OuterJoinType(getNestIndex());
			if (!type.analyze(tokens, index, end - index)) {
				continue;
			}
			sqlTokens.add( type.getSQLToken() );
			
			if (0 < index - start) {
				if (isEqualsToken(tokens.get(index-1), KW_NATURAL)) {
					sqlTokens.add(0, new SQLToken(KW_NATURAL));
					index --;
				}
			}
			if (0 < index - start) {
				QueryPartitionClause clause = new QueryPartitionClause(getNestIndex());
				if (!clause.analyze(tokens, start, index - start)) {
					continue;
				}
				sqlTokens.add(0, clause.getSQLToken());
			}
			
			return sqlTokens;
		}
		
		return null;
	}
	
	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		int start = offset;
		int end = offset + length;

		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
		
		List<Integer> indexs = splitTokenEx(tokens, start, end - start, KW_ON, KW_USING);
		for (int i = indexs.size() - 1 ; i >= 0 ; i--) {
			int index = indexs.get(i);
			
			sqlTokens.clear();
			
			if (isEqualsToken(tokens.get(index), KW_ON)) {
				sqlTokens.add( new SQLToken(KW_ON) );
				
				Condition condition = new Condition(getNestIndex());
				if (!condition.analyze(tokens, index+1, end - (index + 1))) {
					continue;
				}
				sqlTokens.add( new SQLToken(tokens.get(index+1).getToken()) );
				sqlTokens.add(condition.getSQLToken());
			} else if (4 <= end - index && startsWith(tokens, index, end - index, KW_USING, "(") && endsWith(tokens, index, end - index, ")")) {
				List<SQLToken> sqlTokens1 = patternColumnList(tokens, index+2, (end - 1) - (index+2));
				if (null == sqlTokens1) {
					continue;
				}
				sqlTokens.add( new SQLToken(KW_USING) );
				sqlTokens.add( new SQLToken("(") );
				sqlTokens.add( new SQLToken(sqlTokens1) );
				sqlTokens.add( new SQLToken(")") );				
			}
			
			List<SQLToken> sqlTokens2 = pattern0201(tokens, start, index - start);
			if (null == sqlTokens2) {
				continue;
			}
			sqlTokens.addAll(0, sqlTokens2);

			return sqlTokens;
		}
		sqlTokens.clear();

		List<SQLToken> sqlTokens2 = pattern0201(tokens, start, end - start);
		if (null != sqlTokens2) {
			sqlTokens.addAll(0, sqlTokens2);
			return sqlTokens;
		}

		return null;
	}
	
	private List<SQLToken> pattern0201(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		int start = offset;
		int end = offset + length;
		
		List<Integer> indexs = splitTokenEx(tokens, offset, length, QueryPartitionClause.KW_PARTITION);
		for (int i = indexs.size() - 1 ; i >= 0 ; i--) {
			int index = indexs.get(i);
			
			QueryPartitionClause clause = new QueryPartitionClause(getNestIndex());
			if (!clause.analyze(tokens, index, end - index)) {
				continue;
			}
			TableReference table = new TableReference(getNestIndex());
			if (!table.analyze(tokens, start, index - start)) {
				continue;
			}

			List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
			sqlTokens.add(table.getSQLToken());
			sqlTokens.add(clause.getSQLToken());
			return sqlTokens;
		}
		
		TableReference table = new TableReference(getNestIndex());
		if (table.analyze(tokens, start, end - start)) {
			List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
			sqlTokens.add(table.getSQLToken());
			return sqlTokens;
		}

		return null;
	}
}
