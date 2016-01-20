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

import java.util.List;

import org.azkfw.sql.analyzer.SQLToken;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.syntax.expression.Expr;
import org.azkfw.sql.token.Token;

/**
 * <h1>浮動小数点条件</h1>
 * <p>
 * 浮動小数点条件では、式が無限か、または演算の未定義の結果(非数値(NaN))かを判断します。
 * </p>
 * <p>
 * <b>floating_point_condition::=</b>
 * <p><img src="./doc-files/floating_point_condition.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/conditions003.htm#i1052323">LINK</a>
 * @author Kawakicchi
 */
public class FloatingPointCondition extends AbstractSyntax{

	public FloatingPointCondition() {
	}

	public FloatingPointCondition(final int index) {
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
	 * = {@link Expr :expr} <b>IS</b> <b>NOT</b> (<b>MAN</b>|<b>INFINITE</b>)
	 * @param tokens
	 * @param offset
	 * @param length
	 * @return
	 * @throws SyntaxException
	 */
	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (4 <= length && startsWith(tokens, offset+length-3, 1, "IS")) {
			trace("未定義");
		}
		return null;
	}
	
	/**
	 * = {@link Expr :expr} <b>IS</b> (<b>MAN</b>|<b>INFINITE</b>)
	 * @param tokens
	 * @param offset
	 * @param length
	 * @return
	 * @throws SyntaxException
	 */
	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (3 <= length && startsWith(tokens, offset+length-2, 1, "")) {
			trace("未定義");
		}
		return null;
	}
}
