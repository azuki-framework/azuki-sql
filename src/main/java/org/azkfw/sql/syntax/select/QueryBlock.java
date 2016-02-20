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
import org.azkfw.analysis.lexical.scanner.pattern.OracleHintTokenPattern;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.syntax.clause.JoinClause;
import org.azkfw.sql.syntax.clause.SubqueryFactoringClause;
import org.azkfw.sql.syntax.clause.WhereClause;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>query_block::=</b>
 * <p>
 * <img src="./doc-files/query_block.gif"/>
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
public class QueryBlock extends AbstractSyntax {

	public QueryBlock(final int index) {
		super(index);
	}

	// select ~ group by
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));

		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();

		int start = offset;
		int end = offset + length;

		List<Integer> indexs1 = splitTokenEx(tokens, start, end - start, "SELECT");
		for (int i = 0; i < indexs1.size(); i++) {
			int index1 = indexs1.get(i);

			sqlTokens.clear();

			List<SQLToken> sqlTokens1 = pattern01(tokens, start, index1 - start);
			if (null == sqlTokens1) {
				continue;
			}
			sqlTokens.addAll(sqlTokens1);
			sqlTokens.add(new SQLToken("SELECT"));

			index1++;
			List<Integer> indexs2 = splitTokenEx(tokens, index1, end - index1, "FROM");
			for (int j = 0; j < indexs2.size(); j++) {
				int index2 = indexs2.get(j);

				List<SQLToken> sqlTokens2 = pattern02(tokens, index1, index2 - index1);
				if (null == sqlTokens2) {
					continue;
				}

				index2++;

				List<SQLToken> sqlTokens3 = pattern03(tokens, index2, end - index2);
				if (null == sqlTokens3) {
					continue;
				}

				sqlTokens.addAll(sqlTokens2);
				sqlTokens.add(new SQLToken("FROM"));
				sqlTokens.addAll(sqlTokens3);
				setSQLToken(new SQLToken(sqlTokens));
				return true;
			}
		}
		return false;
	}

	// no or subquery_factoring_clause
	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {
			SubqueryFactoringClause clause = new SubqueryFactoringClause(getNestIndex());
			if (!clause.analyze(tokens, offset, length)) {
				return null;
			}
			result.add(clause.getSQLToken());
		}
		return result;
	}

	// SELECT ～ FROM
	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();

		Token token = null;
		int start = offset;
		int end = offset + length;

		if (start >= offset + length)
			return null;

		token = tokens.get(start);
		if (OracleHintTokenPattern.NAME.equals(token.getType())) {
			result.add(new SQLToken(token.getToken()));
			start++;
		}

		if (start >= offset + length)
			return null;

		token = tokens.get(start);
		if (isEqualsToken(token, "DISTINCT", "UNIQUE", "ALL")) {
			result.add(new SQLToken(token.getToken()));
			start++;
		}

		List<SQLToken> sqlTokens1 = pattern0201(tokens, start, end - start);
		if (null == sqlTokens1) {
			return null;
		}
		result.addAll(sqlTokens1);

		return result;
	}

	private List<SQLToken> pattern0201(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		SelectList selectList = new SelectList(getNestIndex());
		if (!selectList.analyze(tokens, offset, length)) {
			return null;
		}
		result.add(selectList.getSQLToken());
		return result;
	}

	// FROM ～ end
	private List<SQLToken> pattern03(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		int start = offset;
		int end = offset + length;

		List<Integer> indexs = splitTokenEx(tokens, start, end - start, "MODEL");
		for (int i1 = indexs.size() - 1; i1 >= 0; i1--) {
			int index = indexs.get(i1);
			List<SQLToken> sqlTokens2 = pattern0305(tokens, index, end - index);
			if (null == sqlTokens2) {
				continue;
			}
			List<SQLToken> sqlTokens1 = pattern03_01(tokens, start, index - start);
			if (null == sqlTokens1) {
				continue;
			}

			List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
			sqlTokens.addAll(sqlTokens1);
			sqlTokens.addAll(sqlTokens2);
			return sqlTokens;
		}
		List<SQLToken> sqlTokens = pattern03_01(tokens, start, end - start);
		if (null != sqlTokens) {
			return sqlTokens;
		}
		return null;
	}

	// Modelより前
	private List<SQLToken> pattern03_01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		int start = offset;
		int end = offset + length;

		List<Integer> indexs = splitTokenEx(tokens, start, end - start, "GROUP");
		for (int i1 = indexs.size() - 1; i1 >= 0; i1--) {
			int index = indexs.get(i1);
			List<SQLToken> sqlTokens2 = pattern0304(tokens, index, end - index);
			if (null == sqlTokens2) {
				continue;
			}
			List<SQLToken> sqlTokens1 = pattern03_02(tokens, start, index - start);
			if (null == sqlTokens1) {
				continue;
			}

			List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
			sqlTokens.addAll(sqlTokens1);
			sqlTokens.addAll(sqlTokens2);
			return sqlTokens;
		}
		List<SQLToken> sqlTokens = pattern03_02(tokens, start, end - start);
		if (null != sqlTokens) {
			return sqlTokens;
		}

		return null;
	}

	// Group by より前
	private List<SQLToken> pattern03_02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		int start = offset;
		int end = offset + length;

		List<Integer> indexs = splitTokenEx(tokens, start, end - start, "CONNECT", "START");
		for (int i1 = indexs.size() - 1; i1 >= 0; i1--) {
			int index = indexs.get(i1);
			List<SQLToken> sqlTokens2 = pattern0303(tokens, index, end - index);
			if (null == sqlTokens2) {
				continue;
			}
			List<SQLToken> sqlTokens1 = pattern03_03(tokens, start, index - start);
			if (null == sqlTokens1) {
				continue;
			}

			List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
			sqlTokens.addAll(sqlTokens1);
			sqlTokens.addAll(sqlTokens2);
			return sqlTokens;
		}
		List<SQLToken> sqlTokens = pattern03_03(tokens, start, end - start);
		if (null != sqlTokens) {
			return sqlTokens;
		}

		return null;
	}

	// hierarchical より前
	private List<SQLToken> pattern03_03(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		int start = offset;
		int end = offset + length;

		List<Integer> indexs = splitTokenEx(tokens, start, end - start, "WHERE");
		for (int i1 = indexs.size() - 1; i1 >= 0; i1--) {
			int index = indexs.get(i1);
			List<SQLToken> sqlTokens2 = pattern0302(tokens, index, end - index);
			if (null == sqlTokens2) {
				continue;
			}
			List<SQLToken> sqlTokens1 = pattern0301(tokens, start, index - start);
			if (null == sqlTokens1) {
				continue;
			}

			List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
			sqlTokens.addAll(sqlTokens1);
			sqlTokens.addAll(sqlTokens2);
			return sqlTokens;
		}
		List<SQLToken> sqlTokens = pattern0301(tokens, start, end - start);
		if (null != sqlTokens) {
			return sqlTokens;
		}

		return null;
	}

	// From 
	private List<SQLToken> pattern0301(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (0 == length)
			return null;

		int start = offset;
		int end = offset + length;

		List<SQLToken> result = new ArrayList<SQLToken>();

		List<Integer> indexs = splitTokenEx(tokens, offset, length, ",");
		int pattern = getPatternSize(indexs);
		for (int i = pattern - 1; i >= 0; i--) {

			boolean match = true;
			result.clear();
			int index1 = start;

			List<Integer> indexs2 = getPattern(indexs, i);
			for (int j = 0; j < indexs2.size(); j++) {
				int index2 = indexs2.get(j);
				
				List<SQLToken> sqlTokens1 = pattern030101(tokens, index1, index2 - index1);
				if (null == sqlTokens1) {
					match = false;
					break;
				}
				result.addAll(sqlTokens1);

				index1 = index2 + 1;
				result.add(new SQLToken(","));
			}
			if (!match) {
				continue;
			}
			
			List<SQLToken> sqlTokens1 = pattern030101(tokens, index1, end - index1);
			if (null == sqlTokens1) {
				continue;
			}
			result.addAll(sqlTokens1);

			return result;
		}
		return null;
	}

	// Where
	private List<SQLToken> pattern0302(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {

			WhereClause clause = new WhereClause(getNestIndex());
			if (!clause.analyze(tokens, offset, length)) {
				return null;
			}
			result.add(clause.getSQLToken());
		}
		return result;
	}

	private List<SQLToken> pattern0303(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {
			if (!startsWith(tokens, offset, length, "CONNECT", "START"))
				return null;

			// TODO: hierarchical_query_clause
			result.add(new SQLToken(toString(tokens, offset, length)));
		}
		return result;
	}

	private List<SQLToken> pattern0304(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {
			if (!startsWith(tokens, offset, length, "GROUP"))
				return null;

			// TODO: group_by_clause
			result.add(new SQLToken(toString(tokens, offset, length)));
		}
		return result;
	}

	private List<SQLToken> pattern0305(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> result = new ArrayList<SQLToken>();
		if (0 != length) {
			if (!startsWith(tokens, offset, length, "MODEL"))
				return null;

			// TODO: model_clause
			result.add(new SQLToken(toString(tokens, offset, length)));
		}
		return result;
	}

	// 
	private List<SQLToken> pattern030101(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		if (0 == length)
			return null;

		List<SQLToken> result = new ArrayList<SQLToken>();

		if (startsWith(tokens, offset, length, "(") && endsWith(tokens, offset, length, ")")) {
			JoinClause clause2 = new JoinClause(getNestIndex());
			if (clause2.analyze(tokens, offset + 1, length - 2)) {

				result.add(new SQLToken("("));
				result.add(clause2.getSQLToken());
				result.add(new SQLToken(")"));
				return result;
			}
		}
		
		if (-1 != indexOfEx(tokens, offset, length, "NATURAL", "INNER", "OUTER", "CROSS", "JOIN", "PARTITION", "FULL", "LEFT",
				"RIGHT")) {
			JoinClause clause1 = new JoinClause(getNestIndex());
			if (clause1.analyze(tokens, offset, length)) {
				result.add(clause1.getSQLToken());
				return result;
			}
		}

		TableReference tableReference = new TableReference(getNestIndex());
		if (tableReference.analyze(tokens, offset, length)) {
			result.add(tableReference.getSQLToken());
			return result;
		}

		return null;
	}
}
