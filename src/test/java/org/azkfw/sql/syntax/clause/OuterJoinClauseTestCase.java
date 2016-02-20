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

import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.syntax.SyntaxTestCase;
import org.junit.Test;

/**
 * @author Kawakicchi
 *
 */
public class OuterJoinClauseTestCase extends SyntaxTestCase {

	@Test
	public void test01() throws SyntaxException{
		OuterJoinClause clause = new OuterJoinClause(0);
		boolean result = clause.analyze(getTokens("clause", "OuterJoinClause01.sql"));
		assertEquals(true, result);
		System.out.println(clause.getSQLToken().toString());
	}

	@Test
	public void test02() throws SyntaxException{
		OuterJoinClause clause = new OuterJoinClause(0);
		boolean result = clause.analyze(getTokens("clause", "OuterJoinClause02.sql"));
		assertEquals(true, result);
		System.out.println(clause.getSQLToken().toString());
	}

	@Test
	public void test03() throws SyntaxException{
		OuterJoinClause clause = new OuterJoinClause(0);
		boolean result = clause.analyze(getTokens("clause", "OuterJoinClause03.sql"));
		assertEquals(true, result);
		System.out.println(clause.getSQLToken().toString());
	}

	@Test
	public void test04() throws SyntaxException{
		OuterJoinClause clause = new OuterJoinClause(0);
		boolean result = clause.analyze(getTokens("clause", "OuterJoinClause04.sql"));
		assertEquals(true, result);
		System.out.println(clause.getSQLToken().toString());
	}
	
	@Test
	public void test05() throws SyntaxException{
		OuterJoinClause clause = new OuterJoinClause(0);
		boolean result = clause.analyze(getTokens("clause", "OuterJoinClause05.sql"));
		assertEquals(true, result);
		System.out.println(clause.getSQLToken().toString());
	}

	@Test
	public void test06() throws SyntaxException{
		OuterJoinClause clause = new OuterJoinClause(0);
		boolean result = clause.analyze(getTokens("clause", "OuterJoinClause06.sql"));
		assertEquals(true, result);
		System.out.println(clause.getSQLToken().toString());
	}

	@Test
	public void test07() throws SyntaxException{
		OuterJoinClause clause = new OuterJoinClause(0);
		boolean result = clause.analyze(getTokens("clause", "OuterJoinClause07.sql"));
		assertEquals(true, result);
		System.out.println(clause.getSQLToken().toString());
	}

	@Test
	public void test08() throws SyntaxException{
		OuterJoinClause clause = new OuterJoinClause(0);
		boolean result = clause.analyze(getTokens("clause", "OuterJoinClause08.sql"));
		assertEquals(true, result);
		System.out.println(clause.getSQLToken().toString());
	}

	@Test
	public void test09() throws SyntaxException{
		OuterJoinClause clause = new OuterJoinClause(0);
		boolean result = clause.analyze(getTokens("clause", "OuterJoinClause09.sql"));
		assertEquals(true, result);
		System.out.println(clause.getSQLToken().toString());
	}


}
