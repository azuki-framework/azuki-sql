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
import org.azkfw.sql.syntax.clause.ForUpdateClause;
import org.azkfw.sql.token.Token;

/**
 * <h1>SELECT</h1>
 * <p>
 * SELECT文または副問合せを使用すると、1つ以上の表、オブジェクト表、ビュー、オブジェクト・ビューまたはマテリアライズド・
 * ビューからデータを取り出すことができます。
 * </p>
 * <p>
 * <b>select::=</b>
 * <p>
 * <img src="./doc-files/select.gif"/>
 * </p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * 
 * <pre>
 * </pre>
 * </p>
 * 
 * @see <a
 *      href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/statements_10002.htm#i2065646">LINK</a>
 * @author Kawakicchi
 */
public class Select extends AbstractSyntax {
	
	public Select() {
	}
	
	public Select(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();

		if (endsWith(tokens, offset, length, ";")) {

			int i0 = offset;
			int i2 = offset + length - 1;

			boolean match = false;
			
			List<Integer> indexs = splitToken(tokens, offset, length, "FOR");
			for (int i = indexs.size()-1 ; i >= 0 ; i--) {
				int i1 = indexs.get(i);
				
				List<SQLToken> sqlTokens1 = pattern01(tokens, i0, i1-i0);
				if (null == sqlTokens1) {
					continue;
				}
	
				List<SQLToken> sqlTokens2 = pattern02(tokens, i1, i2-i1);
				if (null == sqlTokens2) {
					continue;
				}
	
				match = true;
				sqlTokens.addAll(sqlTokens1);
				sqlTokens.addAll(sqlTokens2);
				break;
			}
			if (!match) {
				List<SQLToken> sqlTokens1 = pattern01(tokens, i0, i2-i0);
				if (null != sqlTokens1) {
					match = true;
					sqlTokens.addAll(sqlTokens1);
				}
			}
			
			sqlTokens.add( new SQLToken(";") );

			if (match) {
				setSQLToken(new SQLToken(sqlTokens));
				return true;
			}
		}
		return false;
	}

	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
		
		Subquery subquery = new Subquery(getNestIndex());
		if (!subquery.analyze(tokens, offset, length)) {
			return null;
		}
		SQLToken sqlToken = subquery.getSQLToken();
		sqlTokens.add(sqlToken);
		
		return sqlTokens;
	}

	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
		
		if (0 < length) {
			ForUpdateClause forUpdateClause = new ForUpdateClause(getNestIndex());
			if (!forUpdateClause.analyze(tokens, offset, length)) {
				return null;
			}
			SQLToken sqlToken = forUpdateClause.getSQLToken();
			sqlTokens.add(sqlToken);
		}
		
		return sqlTokens;
	}

}
