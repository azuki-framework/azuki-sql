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
import org.azkfw.sql.syntax.condition.Condition;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>where_clause::=</b>
 * <p><img src="./doc-files/where_clause.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/statements_10008.htm#i2068118">LINK</a>
 * @author Kawakicchi
 */
public class WhereClause extends AbstractSyntax {

	public WhereClause(final int index) {
		super(index);
	}
	
	@Override
	protected boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));

		if (!startsWith(tokens, offset, length, "WHERE")) return false;

		Condition condition = new Condition(getNestIndex());
		if (condition.analyze(tokens, offset+1, length-1)) {
			
			List<SQLToken> sqlTokens = new ArrayList<SQLToken>();
			sqlTokens.add( new SQLToken("WHERE") );
			sqlTokens.add(condition.getSQLToken());
			setSQLToken( new SQLToken(sqlTokens) );
			return true;
		}
		
		return false;
	}

}
