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
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>condition::=</b>
 * <p><img src="./doc-files/condition.gif"/></p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * <pre>
 * </pre>
 * </p>
 * @see <a href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/conditions001.htm#i1034172">LINK</a>
 * @author Kawakicchi
 */
public class Condition extends AbstractSyntax{

	public Condition() {
	}
	
	public Condition(final int index) {
		super(index);
	}
	
	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));

		if ( -1 != indexOfEx(tokens, offset, length, "AND", "OR") ) {
			CompoundCondition condition = new CompoundCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}

		{
			SimpleComparisonCondition condition = new SimpleComparisonCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}
		{
			FloatingPointCondition condition = new FloatingPointCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}
		{
			LogicalCondition condition = new LogicalCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}		
		{
			ModelCondition condition = new ModelCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}
		{
			MultisetCondition condition = new MultisetCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}
		{
			PatternMatchingCondition condition = new PatternMatchingCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}
		
		// TODO: range_condition
		
		{
			NullCondition condition = new NullCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}
		{
			XMLCondition condition = new XMLCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}
		{
			BetweenCondition condition = new BetweenCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}
		{
			CompoundCondition condition = new CompoundCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}
		{
			ExistsCondition condition = new ExistsCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}
		{
			InCondition condition = new InCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}
		{
			IsOfTypeCondition condition = new IsOfTypeCondition(getNestIndex());
			if (condition.analyze(tokens, offset, length)) {
				setSQLToken( condition.getSQLToken() );
				return true;
			}
		}
		
		return true;
	}
}
