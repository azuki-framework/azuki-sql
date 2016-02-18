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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.azkfw.analysis.lexical.scanner.SQLScanner;
import org.azkfw.analysis.lexical.scanner.Scanner;
import org.azkfw.analysis.lexical.scanner.Token;
import org.azkfw.analysis.lexical.scanner.Tokens;
import org.azkfw.analysis.lexical.scanner.pattern.CommentTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.DustTokenPattern;
import org.azkfw.analysis.util.AnalysisUtility;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.syntax.clause.ForUpdateClause;
import org.azkfw.sql.token.SQLToken;

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

	public static void main(final String[] args) {
		String source = AnalysisUtility.getString(new File(args[0]), args[1]);
		
		Scanner scanner = new SQLScanner();
		Tokens ts = scanner.scan(source);

		List<Token> tokens = new ArrayList<Token>();
		for (Token token : ts.list()) {
			if (DustTokenPattern.NAME.equals(token.getType())) {
				continue;
			}
			if (CommentTokenPattern.NAME.equals(token.getType())) {
				continue;
			}
			tokens.add(token);
		}

		try {
			Select a = new Select();
			// Update a = new Update();
			if (a.analyze(tokens)) {
				String str3 = a.getSQLToken().toString();
				System.out.println(str3);
			} else {
				System.out.println("ERROR");
			}
		} catch (SyntaxException ex) {
			ex.printStackTrace();
		}
	}

	public Select() {
	}

	public Select(final int index) {
		super(index);
	}

	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();

		int start = offset;
		int end = offset + length;

		if (endsWith(tokens, offset, length, ";")) {
			end = offset + length - 1;
		}

		boolean match = false;

		List<Integer> indexs = splitTokenEx(tokens, offset, length, "FOR");
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
			if (endsWith(tokens, offset, length, ";")) {
				sqlTokens.add(new SQLToken(";"));
			}
			
			setSQLToken(new SQLToken(sqlTokens));
			return true;
		}

		return false;
	}

	private List<SQLToken> pattern01(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();

		Subquery subquery = new Subquery(this, 0, getNestIndex());
		if (!subquery.analyze(tokens, offset, length)) {
			return null;
		}
		SQLToken sqlToken = subquery.getSQLToken();
		sqlTokens.add(sqlToken);

		return sqlTokens;
	}

	private List<SQLToken> pattern02(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		List<SQLToken> sqlTokens = new ArrayList<SQLToken>();

		if (2 <= length) { // FOR UPDATE
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
