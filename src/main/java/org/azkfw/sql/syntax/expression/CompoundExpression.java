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
import org.azkfw.sql.token.SQLToken;

/**
 * <h1>複合式</h1>
 * <p>
 * 複合式は、その他の式の組合せを指定します。
 * </p>
 * <p>
 * <b>compound_expression::=</b>
 * <p><img src="./doc-files/compound_expression.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
('CLARK' || 'SMITH') 
LENGTH('MOOSE') * 57 
SQRT(144) + 72 
my_fun(TO_CHAR(sysdate,'DD-MMM-YY'))
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/expressions003.htm#i1033618">LINK</a>
 * @author Kawakicchi
 */
public class CompoundExpression extends AbstractSyntax {

	public CompoundExpression(final int index) {
		super(index);
	}

	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));

		int start = offset;
		int end = offset + length;

		if (startsWith(tokens, start, end - start, "(") && endsWith(tokens, start, end - start, ")")) {
			Expr expr = new Expr(getNestIndex());
			if (expr.analyze(tokens, start + 1, end - start - 2)) {
				List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
				sqlTokens.add( new SQLToken("("));
				sqlTokens.add( expr.getSQLToken() );
				sqlTokens.add( new SQLToken(")"));
				setSQLToken( new SQLToken(sqlTokens) );
				return true;
			}
		} else if (startsWith(tokens, start, end - start, "+", "-", "PRIOR")) {
			Expr expr = new Expr(getNestIndex());
			if (expr.analyze(tokens, start + 1, end - start - 1)) {
				List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
				sqlTokens.add( new SQLToken(tokens.get(start).getToken()) );
				sqlTokens.add( expr.getSQLToken() );
				setSQLToken( new SQLToken(sqlTokens) );
				return true;
			}
		} else {
			List<Integer> indexs = splitTokenEx(tokens, start, end - start, "*","/","+","-","||");
			if ( 0 < indexs.size()) {
				for (int i = 0 ; i < indexs.size() ; i++) {
					int index = indexs.get(i);
					Expr expr1 = new Expr(getNestIndex());
					if (!expr1.analyze(tokens, start, index - start)) {
						continue;
					}
					Expr expr2 = new Expr(getNestIndex());
					if (!expr2.analyze(tokens, index + 1, end - index)) {
						continue;
					}
					List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
					sqlTokens.add(expr1.getSQLToken());
					sqlTokens.add( new SQLToken(tokens.get(index).getToken()));
					sqlTokens.add(expr2.getSQLToken());
					setSQLToken(new SQLToken(sqlTokens));
					return true;
				}
			}
		}
		return false;
	}
}
