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

import java.util.List;

import org.azkfw.analysis.lexical.scanner.Token;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;

/**
 * <h1>期間式</h1>
 * <p>
 * 期間式は、INTERVAL YEAR TO MONTHまたはINTERVAL DAY TO SECONDの値を戻します。
 * </p>
 * <p>
 * <b>interval_expression::=</b>
 * <p><img src="./doc-files/interval_expression.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
SELECT (SYSTIMESTAMP - order_date) DAY(9) TO SECOND FROM orders
   WHERE order_id = 2458;
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/expressions009.htm#i1033523">LINK</a>
 * @author Kawakicchi
 */
public class IntervalExpression extends AbstractSyntax{

	public IntervalExpression() {
	}
	
	public IntervalExpression(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		
		trace("未実装");
		
		return false;
	}
}
