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

import org.azkfw.analysis.lexical.scanner.Token;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.Syntax;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.syntax.clause.OrderByClause;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>subquery::=</b>
 * <p>
 * <img src="./doc-files/subquery.gif"/>
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
public class Subquery extends AbstractSyntax {

	private Syntax parent;
	private int pattern;
	
	public Subquery(final Syntax parent, final int pattern, final int index) {
		super(index);
		this.parent = parent;
		this.pattern = pattern;
	}

	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();

		int start = offset;
		int end = offset + length;

		boolean match = false;

		List<Integer> indexs = splitToken(tokens, offset, length, "ORDER");
		for (int i = indexs.size() - 1; i >= 0; i--) {
			int index = indexs.get(i);

			List<SQLToken> sqlTokens1 = pattern01(tokens, start, index - start);
			if (null == sqlTokens1) {
				continue;
			}
			List<SQLToken> sqlTokens2 = pattern02(tokens, index, end - index);
			if (null == sqlTokens2) {
				continue;
			}

			match = true;
			sqlTokens.addAll(sqlTokens1);
			sqlTokens.addAll(sqlTokens2);
			break;
		}
		if (!match) {
			List<SQLToken> sqlTokens1 = pattern01(tokens, start, end - start);
			if (null != sqlTokens1) {
				match = true;
				sqlTokens.addAll(sqlTokens1);
			}
		}

		if (match) {
			setSQLToken(new SQLToken(sqlTokens));
			return true;
		}
		return false;
	}

	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> sqlTokens = null;

		sqlTokens = pattern0101(tokens, offset, length);
		if (null != sqlTokens) {
			return sqlTokens;
		}

		sqlTokens = pattern0102(tokens, offset, length);
		if (null != sqlTokens) {
			return sqlTokens;
		}
		
		sqlTokens = pattern0103(tokens, offset, length);
		if (null != sqlTokens) {
			return sqlTokens;
		}

		return null;
	}

	private List<SQLToken> pattern0101(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();

		QueryBlock queryBlock = new QueryBlock(getNestIndex());
		if (!queryBlock.analyze(tokens, offset, length)) {
			return null;
		}

		result.add(queryBlock.getSQLToken());
		return result;
	}

	private List<SQLToken> pattern0102(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (!(parent instanceof Subquery) || 2 != pattern ) {
			List<SQLToken> result = new ArrayList<SQLToken>();
	
			List<Integer> indexs = splitToken(tokens, offset, length, "UNION", "INTERSECT", "MINUS");
			int pattern = getPatternSize(indexs);
			for (int i = 0; i < pattern; i++) {
				List<Integer> indexs2 = getPattern(indexs, pattern);
	
				result.clear();
				boolean match = true;
				
				int start = offset;
				int end = offset + length;
				int index1 = start;
				
				for (int j = 0; j < indexs2.size(); j++) {
					int index2 = indexs2.get(j);
	
					Subquery subquery = new Subquery(this, 2, getNestIndex());
					if (!subquery.analyze(tokens, index1, index2 - index1)) {
						match = false;
						break;
					}
					result.add(subquery.getSQLToken());
	
					if (startsWith(tokens, index2, end - index2, "UNION", "ALL")) {
						result.add(new SQLToken("UNION"));
						result.add(new SQLToken("ALL"));
						index1 = index2 + 2;
					} else {
						result.add(new SQLToken(tokens.get(index2).getToken()));
						index1 = index2 + 1;
					}
				}
				if (!match) {
					continue;
				}
	
				Subquery subquery = new Subquery(this, 2, getNestIndex());
				if (!subquery.analyze(tokens, index1, end - index1)) {
					continue;
				}
	
				return result;
			}

		}
		return null;
	}

	private List<SQLToken> pattern0103(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (!(parent instanceof Subquery) || 3 != pattern ) {

			if (!startsWith(tokens, offset, length, "("))
				return null;
			if (!endsWith(tokens, offset, length, ")"))
				return null;
	
			Subquery subquery = new Subquery(this, 3, getNestIndex());
			if (!subquery.analyze(tokens, offset + 1, length - 2))
				return null;
	
			List<SQLToken> result = new ArrayList<SQLToken>();
			result.add(new SQLToken("("));
			result.add(subquery.getSQLToken());
			result.add(new SQLToken(")"));
			return result;
		}
		return null;
	}

	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {
			OrderByClause clause = new OrderByClause(getNestIndex());
			if (!clause.analyze(tokens, offset, length)) {
				return null;
			}
			result.add(clause.getSQLToken());
		}
		return result;
	}
}
