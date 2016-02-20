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
 * <b>inner_cross_join_clause::=</b>
 * <p><img src="./doc-files/inner_cross_join_clause.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * <p>
 * <ul>
 * <li>{@link TableReference}</li>
 * <li>{@link Condition}</li>
 * </ul>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/statements_9014.htm#i2080498">LINK</a>
 * @author Kawakicchi
 */
public class InnerCrossJoinClause extends AbstractSQLSyntax{

	public static final String KW_INNER = "INNER";
	public static final String KW_JOIN = "JOIN";
	public static final String KW_ON = "ON";
	public static final String KW_CROSS = "CROSS";
	public static final String KW_USING = "USING";
	public static final String KW_NATURAL = "NATURAL";
	
	public InnerCrossJoinClause(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		
		List<SQLToken> sqlTokens = null;
		
		Token token = tokens.get(offset);
		if (isEqualsToken(token, KW_INNER, KW_JOIN)) {
			sqlTokens = pattern01(tokens, offset, length);
		} else if (isEqualsToken(token, KW_CROSS, KW_NATURAL)) {
			sqlTokens = pattern02(tokens, offset, length);
		}

		if (null != sqlTokens) {
			setSQLToken(new SQLToken(sqlTokens));
			return true;
		}
		return false;
	}
	
	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
		
		int start = offset;
		int end = offset + length;
		
		if (startsWith(tokens, start, end - start, KW_INNER, KW_JOIN)) {
			sqlTokens.add( new SQLToken(KW_INNER) );
			sqlTokens.add( new SQLToken(KW_JOIN) );
			start += 2;
		} else if (startsWith(tokens, start, end - start, KW_JOIN)) {
			sqlTokens.add( new SQLToken(KW_JOIN) );
			start += 1;
		} else {
			return null;
		}
		
		List<Integer> indexs1 = splitTokenEx(tokens, start, end - start, KW_ON, KW_USING);
		if (0 == indexs1.size()) {
			return null;
		}
		for (int i = indexs1.size() - 1 ; i >= 0 ; i--) {
			int index1 = indexs1.get(i);
			TableReference tableReference = new TableReference(getNestIndex());
			if (!tableReference.analyze(tokens, start, index1 - start)) {
				continue;
			}
			
			if (isEqualsToken(tokens.get(index1), KW_ON)) {
				// condition
				Condition condition = new Condition(getNestIndex());
				if (!condition.analyze(tokens, index1 + 1, end - (index1 + 1))) {
					continue;
				}
				sqlTokens.add(tableReference.getSQLToken());
				sqlTokens.add( new SQLToken(KW_ON));
				sqlTokens.add(condition.getSQLToken());
				return sqlTokens;
			} else if (startsWith(tokens, index1, end - start, KW_USING, "(") && endsWith(tokens, index1, end - index1, ")")) {
				List<SQLToken> sqlTokens1 = patternColumnList(tokens, index1 + 2, (end - 1) - (index1 + 2));
				if (null != sqlTokens1) {
					sqlTokens.add(new SQLToken(KW_USING) );
					sqlTokens.add(new SQLToken("(") );
					sqlTokens.addAll( sqlTokens1 );
					sqlTokens.add(new SQLToken(")") );					
					return sqlTokens;
				}				
			}
		}
		
		return null;
	}
		
	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
		
		int start = offset;
		int end = offset + length;
		
		if (3 <= length && startsWith(tokens, offset, length, KW_CROSS, KW_JOIN)) {
			sqlTokens.add( new SQLToken(KW_CROSS) );
			sqlTokens.add( new SQLToken(KW_JOIN) );
			start += 2;
		} else if (3 <= length && startsWith(tokens, offset, length, KW_NATURAL, KW_JOIN)) {
			sqlTokens.add( new SQLToken(KW_NATURAL) );
			sqlTokens.add( new SQLToken(KW_JOIN) );
			start += 2;
		} else if (4 <= length && startsWith(tokens, offset, length, KW_NATURAL, KW_INNER, KW_JOIN)) {
			sqlTokens.add( new SQLToken(KW_NATURAL) );
			sqlTokens.add( new SQLToken(KW_INNER) );
			sqlTokens.add( new SQLToken(KW_JOIN) );
			start += 3;
		} else {
			return null;
		}
		
		TableReference tableReference = new TableReference(getNestIndex());
		if (!tableReference.analyze(tokens, start, end - start)) {
			return null;
		}
		sqlTokens.add(tableReference.getSQLToken());		
		return sqlTokens;
	}
}
