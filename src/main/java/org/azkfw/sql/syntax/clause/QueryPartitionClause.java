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
import org.azkfw.sql.syntax.expression.Expr;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>query_partition_clause::=</b>
 * <p><img src="./doc-files/query_partition_clause.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * <p>
 * <ul>
 * <li>{@link Expr}</li>
 * </ul>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/functions004.htm#i1000371">LINK</a>
 * @author Kawakicchi
 */
public class QueryPartitionClause extends AbstractSQLSyntax{
	
	public static final String KW_PARTITION = "PARTITION";
	public static final String KW_BY = "BY";

	public QueryPartitionClause(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		
		int start = offset;
		int end = offset + length;				
		
		if (startsWith(tokens, start, end - start, KW_PARTITION, KW_BY)) {
			start += 2;			
			if (startsWith(tokens, start, end - start, "(") && endsWith(tokens, start, end - start, ")")) {
				start += 1;
				end -= 1;
				List<SQLToken> sqlTokens1 = patternExprList(tokens, start, end - start);
				if (null != sqlTokens1) {
					List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
					sqlTokens.add( new SQLToken(KW_PARTITION) );
					sqlTokens.add( new SQLToken(KW_BY) );
					sqlTokens.add( new SQLToken("(") );
					sqlTokens.addAll( sqlTokens1 );
					sqlTokens.add( new SQLToken(")") );
					setSQLToken( new SQLToken(sqlTokens) );
					return true;
				}
			} else {
				List<SQLToken> sqlTokens1 = patternExprList(tokens, start, end - start);
				if (null != sqlTokens1) {
					List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
					sqlTokens.add( new SQLToken(KW_PARTITION) );
					sqlTokens.add( new SQLToken(KW_BY) );
					sqlTokens.addAll( sqlTokens1 );
					setSQLToken( new SQLToken(sqlTokens) );
					return true;
				}
			}			
		}
		
		return false;
	}
}
