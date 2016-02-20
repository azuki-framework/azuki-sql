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
package org.azkfw.sql.syntax;

import java.util.ArrayList;
import java.util.List;

import org.azkfw.analysis.lexical.scanner.Token;
import org.azkfw.sql.syntax.expression.Expr;
import org.azkfw.sql.token.SQLToken;

/**
 * @author Kawakicchi
 *
 */
public abstract class AbstractSQLSyntax extends AbstractSyntax {

	public AbstractSQLSyntax(final int index) {
		super(index);
	}
	
	protected List<SQLToken> patternColumnList(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();

		int start = offset;
		int end = offset + length;
		int index1 = start;
		List<Integer> indexs = splitTokenEx(tokens, start, end - start, ",");
		for (int j = 0 ; j < indexs.size() ; j++) {
			int index2 = indexs.get(j);
			if (1 != index2 - index1) {
				return null;
			}
			
			sqlTokens.add( new SQLToken(tokens.get(index1).getToken()) ); // column
			sqlTokens.add( new SQLToken(",") );
			index1 = index2 + 1;
		}
		if (1 != end - index1) {
			return null;
		}
		sqlTokens.add( new SQLToken(tokens.get(index1).getToken()) ); // column
		
		return sqlTokens;
	}
	
	protected List<SQLToken> patternExprList(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();

		int start = offset;
		int end = offset + length;
		
		List<Integer> indexs1 = splitTokenEx(tokens, offset, length, ",");
		int pattern = getPatternSize(indexs1);
		for (int i = pattern - 1 ; i >= 0 ; i--) {
			List<Integer> indexs2 = getPattern(indexs1, i);
			
			int index1 = start;
			boolean match = true;
			sqlTokens.clear();
			
			for (int j = 0 ; j < indexs2.size() ; j++) {
				int index2 = indexs2.get(j);

				Expr expr = new Expr(getNestIndex());
				if (!expr.analyze(tokens, index1, index2 - index1)){
					match = false;
					break;
				}
				sqlTokens.add( expr.getSQLToken() );

				sqlTokens.add( new SQLToken(",") );
				index1 = index2 + 1;
			}
			if (!match) {
				continue;
			}

			Expr expr = new Expr(getNestIndex());
			if (!expr.analyze(tokens, index1, end - index1)){
				continue;
			}
			sqlTokens.add(expr.getSQLToken());
			
			return sqlTokens;
		}
		
		return null;
	}
}
