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
package org.azkfw.sql.syntax.condition;

import java.util.ArrayList;
import java.util.List;

import org.azkfw.analysis.lexical.scanner.Token;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.syntax.expression.Expr;
import org.azkfw.sql.syntax.select.Subquery;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1>単純比較条件</h1>
 * <p>
 * 単純比較条件は、式または副問合せの結果の比較方法を指定します。
 * </p>
 * <p>
 * <b>simple_comparison_condition::=</b>
 * <p><img src="./doc-files/simple_comparison_condition.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/conditions002.htm#i1033286">LINK</a>
 * @author Kawakicchi
 */
public class SimpleComparisonCondition extends AbstractSyntax{

	public SimpleComparisonCondition() {
	}

	public SimpleComparisonCondition(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		
		List<SQLToken> sqlTokens = null;
		
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

		return false;
	}
	
	/**
	 * = {@link Expr :expr} (<b>!=</b>|<b>^=</b>|<b>&lt;&gt;</b>|<b>&gt;=</b>|<b>&lt;=</b>|<b>=</b>|<b>&lt;</b>|<b>&gt;</b>) {@link Expr :expr}
	 * @param tokens
	 * @param offset
	 * @param length
	 * @return
	 * @throws SyntaxException
	 */
	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<Integer> indexs = splitToken(tokens, offset, length, "!=", "^=", "<>", ">=", "<=", "=", "<", ">");
		for (int i = 0 ; i < indexs.size() ; i++) {
			int index = indexs.get(i);
			Expr expr1 = new Expr(getNestIndex());
			if (!expr1.analyze(tokens, offset, index - offset))
				continue;
			Expr expr2 = new Expr(getNestIndex());
			if (!expr2.analyze(tokens, index + 1, offset + length - (index + 1)))
				continue;
			
			List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
			sqlTokens.add(expr1.getSQLToken());
			sqlTokens.add(new SQLToken(tokens.get(index).getToken()));
			sqlTokens.add(expr2.getSQLToken());
			return sqlTokens;
		}
		return null;
	}
	
	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<Integer> indexs = splitToken(tokens, offset, length, "!=", "^=", "<>", "=");
		for (int i = 0; i < indexs.size(); i++) {
			int index = indexs.get(i);

			List<SQLToken> sqlTokens1 = pattern0201(tokens, offset, index-offset);
			if (null == sqlTokens1)
				continue;
			List<SQLToken> sqlTokens2 = pattern0202(tokens, index + 1, offset + length - (index + 1));
			if (null == sqlTokens2)
				continue;

			List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
			sqlTokens.addAll(sqlTokens1);
			sqlTokens.add(new SQLToken(tokens.get(index).getToken()));
			sqlTokens.addAll(sqlTokens2);
			return sqlTokens;
		}
		return null;
	}
	
	/**
	 * = ( {@link Expr :expr} , {@link Expr :expr} , ... ) 
	 * @param tokens
	 * @param offset
	 * @param length
	 * @return
	 * @throws SyntaxException
	 */
	private List<SQLToken> pattern0201(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (3 <= length) {
			if (startsWith(tokens, offset, length, "(") && endsWith(tokens, offset, length, ")")) {				
				int i0 = offset + 1;
				int i9 = offset + length - 1;

				List<Integer> indexs = splitToken(tokens, i0, i9 - i0, ",");
				if (0 < indexs.size()) {
					// ( expr , expr , ... )
					List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
					int pattern = getPatternSize(indexs);
					for (int i = pattern - 1 ; i >= 0 ; i--) {
						sqlTokens.clear();
						sqlTokens.add(new SQLToken("("));
						
						List<Integer> indexs2 = getPattern(indexs, pattern);
						int i1 = i0;
						for (int j = 0 ; j < indexs2.size() ; j++) {
							int i2 = indexs2.get(j);
							Expr expr = new Expr(getNestIndex());
							if (!expr.analyze(tokens, i1, i2-i1)) continue;
							sqlTokens.add( expr.getSQLToken() );
							
							i1 = i2 + 1;
							sqlTokens.add( new SQLToken(",") );
						}
						Expr expr = new Expr(getNestIndex());
						if (!expr.analyze(tokens, i1, i9-i1)) continue;
						sqlTokens.add(expr.getSQLToken());
						
						sqlTokens.add(new SQLToken(")"));
						return sqlTokens;
					}
				} else {
					// ( expr )
					Expr expr = new Expr(getNestIndex());
					if (expr.analyze(tokens, i0, i9-i0)) {
						List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
						sqlTokens.add(new SQLToken("("));
						sqlTokens.add(expr.getSQLToken());
						sqlTokens.add(new SQLToken(")"));
						return sqlTokens;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * = ( {@link Subquery :subquery} )
	 * @param tokens
	 * @param offset
	 * @param length
	 * @return
	 * @throws SyntaxException
	 */
	private List<SQLToken> pattern0202(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (3 <= length) {
			if (startsWith(tokens, offset, length, "(") && endsWith(tokens, offset, length, ")")) {
				Subquery subquery = new Subquery(this, 0, getNestIndex());
				if (subquery.analyze(tokens, offset, length)) {

					List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
					sqlTokens.add(new SQLToken("("));
					sqlTokens.add(subquery.getSQLToken());
					sqlTokens.add(new SQLToken(")"));
					return sqlTokens;
				}
			}
		}
		return null;
	}
}
