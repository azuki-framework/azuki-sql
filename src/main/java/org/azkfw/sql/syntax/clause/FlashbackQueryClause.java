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

import java.util.List;

import org.azkfw.analysis.lexical.scanner.Token;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>flashback_query_clause::=</b>
 * <p><img src="./doc-files/flashback_query_clause.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/statements_9014.htm#i2064185">LINK</a>
 * @author Kawakicchi
 */
public class FlashbackQueryClause extends AbstractSyntax {

	public static final String KW_VERSIONS = "VERSIONS";
	public static final String KW_BETWEEN = "BETWEEN";
	public static final String KW_AS = "AS";
	public static final String KW_OF = "OF";
	
	public FlashbackQueryClause(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));

		if (!startsWith(tokens, offset, length, KW_VERSIONS)) return false;
		if (!startsWith(tokens, offset, length, KW_AS)) return false;

		// TODO: 未定義
		trace("未実装");
		return false;
	}
}
