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
	
	public OrderByClause() {
	}
	
	public OrderByClause(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();

		int i0 = offset;
		int i9 = offset + length;
		
		if (startsWith(tokens, offset, length, "ORDER", "SIBLINGS", "BY")) {
			sqlTokens.add( new SQLToken("ORDER") );
			sqlTokens.add( new SQLToken("SIBLINGS") );
			sqlTokens.add( new SQLToken("BY") );
			i0 = offset + 3;
			
		} else if (startsWith(tokens, offset, length, "ORDER", "BY")) {
			sqlTokens.add( new SQLToken("ORDER") );
			sqlTokens.add( new SQLToken("BY") );
			i0 = offset + 2;

		} else {
			return false;
		}
		
		List<SQLToken> sqlTokens1 = pattern01(tokens, i0, i9 - i0);
		if(null == sqlTokens1) {
			return false;
		}
		sqlTokens.addAll(sqlTokens1);

		setSQLToken(new SQLToken(sqlTokens));
		return true;
	}
	
	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();

		int i0 = offset;
		int i2 = offset + length;		
		int i1 = i0;
		
		List<Integer> indexs = splitToken(tokens, offset, length, ",");
		for (int i = 0 ; i < indexs.size() ; i++) {
			int i9 = indexs.get(i);
			result.add( new SQLToken(toString(tokens, i1, i9-i1)) );
			
			i1 = i9 + 1;
			result.add( new SQLToken(","));
		}
		result.add( new SQLToken(toString(tokens, i1, i2-i1)) );
		
		return result;
	}

}
