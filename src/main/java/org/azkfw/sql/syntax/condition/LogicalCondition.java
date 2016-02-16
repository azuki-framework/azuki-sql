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

import org.azkfw.analysis.lexical.scanner.Token;
import org.azkfw.sql.syntax.AbstractSyntax;
import org.azkfw.sql.syntax.SyntaxException;

/**
 * <h1>論理条件</h1>
 * <p>
 * 論理条件は、2つのコンポーネント条件の結果を組み合せ、それらに基づいて単一の結果を生成したり、単一の条件の結果を反転させます。
 * </p>
 * <p>
 * <b>logical_condition::=</b>
 * <p><img src="./doc-files/logical_condition.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/conditions004.htm#i1052219">LINK</a>
 * @author Kawakicchi
 */
public class LogicalCondition extends AbstractSyntax{

	public LogicalCondition() {
	}

	public LogicalCondition(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		
		trace("未定義");

		return false;
	}	
}
