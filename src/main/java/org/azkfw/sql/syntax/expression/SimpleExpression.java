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
 * <h1>単純式</h1>
 * <p>
 * 単純式は、列、疑似列、定数、順序番号またはNULLを指定します。
 * </p>
 * <p>
 * <b>simple_expression::=</b>
 * <p><img src="./doc-files/simple_expression.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
employees.last_name 
'this is a text string'
10 
N'this is an NCHAR string'
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/expressions002.htm#i1033613">LINK</a>
 * @author Kawakicchi
 */
public class SimpleExpression extends AbstractSyntax {

	public SimpleExpression() {
	}
	
	public SimpleExpression(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));

		Token token = null;
		int i0 = offset;
		int i9 = offset + length;
		
		if (5 == length) {
			if (!isEqualsToken(tokens.get(i0 + 1), ".")) return false;
			if (!isEqualsToken(tokens.get(i0 + 3), ".")) return false;
			
			setSQLToken( new SQLToken( toString(tokens, i0, length) ));
			return true;
			
		} else if (3 == length) {
			if (!isEqualsToken(tokens.get(i0 + 1), ".")) return false;
			
			setSQLToken( new SQLToken( toString(tokens, i0, length) ));
			return true;
			
		} else if (1 == length) {
			setSQLToken( new SQLToken( toString(tokens, i0, length) ));
			return true;
			
		}

		return false;
	}
}
