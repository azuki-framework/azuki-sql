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
 * <h1>プレースホルダ式</h1>
 * <p>
 * プレースホルダ式は、SQL文における位置を指定します。この位置に対して、第三世代言語のバインド変数によって値が指定されます。プレースホルダ式には、オプションで標識変数を指定できます。この書式の式は、埋込みSQL文またはOracle Call Interface(OCI)プログラムで処理されるSQL文のみで指定できます。
 * </p>
 * <p>
 * <b>placeholder_expression::=</b>
 * <p><img src="./doc-files/placeholder_expression.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
:employee_name INDICATOR :employee_name_indicator_var
:department_location 
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/expressions012.htm#i1033659">LINK</a>
 * @author Kawakicchi
 */
public class PlaceholderExpression extends AbstractSyntax{

	public PlaceholderExpression() {
	}
	
	public PlaceholderExpression(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		
		trace("未実装");
		
		return false;
	}
}
