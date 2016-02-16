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
import org.azkfw.sql.token.SQLToken;

/**
 * <h1>複合条件</h1>
 * <p>
 * 複合条件は、異なる条件の組合せを指定します。
 * </p>
 * <p>
 * <b>compound_condition::=</b>
 * <p>
 * <img src="./doc-files/compound_condition.gif"/>
 * </p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * 
 * <pre>
 * </pre>
 * </p>
 * 
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/conditions010.htm#i1051061">LINK</a>
 * @author Kawakicchi
 */
public class CompoundCondition extends AbstractSyntax {

	public CompoundCondition() {
	}

	public CompoundCondition(final int index) {
		super(index);
	}

	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));

		List<SQLToken> sqlTokens = null;

		sqlTokens = pattern01(tokens, offset, length);
		if (null != sqlTokens) {
			setSQLToken(new SQLToken(sqlTokens));
			return true;
		}
		sqlTokens = pattern02(tokens, offset, length);
		if (null != sqlTokens) {
			setSQLToken(new SQLToken(sqlTokens));
			return true;
		}
		sqlTokens = pattern03(tokens, offset, length);
		if (null != sqlTokens) {
			setSQLToken(new SQLToken(sqlTokens));
			return true;
		}

		return false;
	}

	/**
	 * = ( {@link Condition :condition} )
	 * @param tokens
	 * @param offset
	 * @param length
	 * @return
	 * @throws SyntaxException
	 */
	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (3 <= length) {
			if (startsWith(tokens, offset, length, "(") && endsWith(tokens, offset, length, ")")) {
				Condition condition = new Condition(getNestIndex());
				if (condition.doAnalyze(tokens, offset + 1, length - 2)) {

					List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
					sqlTokens.add(new SQLToken("("));
					sqlTokens.add(condition.getSQLToken());
					sqlTokens.add(new SQLToken(")"));
					return sqlTokens;
				}
			}
		}
		return null;
	}

	/**
	 * = <b>NOT</b> {@link Condition :condition}
	 * @param tokens
	 * @param offset
	 * @param length
	 * @return
	 * @throws SyntaxException
	 */
	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (2 <= length) {
			if (startsWith(tokens, offset, length, "NOT")) {
				Condition condition = new Condition(getNestIndex());
				if (condition.analyze(tokens, offset + 1, length - 1)) {

					List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
					sqlTokens.add(new SQLToken("NOT"));
					sqlTokens.add(condition.getSQLToken());
					return sqlTokens;
				}
			}
		}
		return null;
	}

	/**
	 * = {@link Condition :condition} (<b>AND</b>|<b>OR</b>) {@link Condition :condition} 
	 * @param tokens
	 * @param offset
	 * @param length
	 * @return
	 * @throws SyntaxException
	 */
	private List<SQLToken> pattern03(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<Integer> indexs = splitToken(tokens, offset, length, "AND", "OR");
		for (int i = 0; i < indexs.size(); i++) {
			int index = indexs.get(i);

			Condition condition1 = new Condition(getNestIndex());
			if (!condition1.analyze(tokens, offset, index - offset))
				continue;
			Condition condition2 = new Condition(getNestIndex());
			if (!condition2.analyze(tokens, index + 1, offset + length - (index + 1)))
				continue;

			List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
			sqlTokens.add(condition1.getSQLToken());
			sqlTokens.add(new SQLToken(tokens.get(index).getToken()));
			sqlTokens.add(condition2.getSQLToken());
			return sqlTokens;
		}
		return null;
	}
}
