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
import org.azkfw.sql.syntax.select.TableReference;
import org.azkfw.sql.token.SQLToken;

/**
 * <h1></h1>
 * <p>
 * </p>
 * <p>
 * <b>join_clause::=</b>
 * <p>
 * <img src="./doc-files/join_clause.gif"/>
 * </p>
 * </p>
 * <p>
 * 有効な例を次に示します。
 * 
 * <pre>
 * </pre>
 * </p>
 * <p>
 * <ul>
 * <li>{@link TableReference}</li>
 * <li>{@link InnerCrossJoinClause}</li>
 * <li>{@link OuterJoinClause}</li>
 * </ul>
 * </p>
 * 
 * @see <a
 *      href="https://docs.oracle.com/cd/E16338_01/server.112/b56299/functions004.htm#i97469">LINK</a>
 * @author Kawakicchi
 */
public class JoinClause extends AbstractSyntax {

	public JoinClause(final int index) {
		super(index);
	}

	@Override
	protected final boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		trace(toString(tokens, offset, length));
		
		int start = offset;
		int end = offset + length;

		List<SQLToken> result = new ArrayList<SQLToken>();

		List<Integer> indexs = splitTokenEx(tokens, offset, length, "NATURAL", "INNER", "OUTER", "CROSS", "JOIN", "PARTITION", "FULL", "LEFT",
				"RIGHT");
		// innser_cross_join
		trimPatternIndex1(tokens, indexs, "NATURAL", "INNER", "JOIN");
		trimPatternIndex1(tokens, indexs, "INNER", "JOIN");
		trimPatternIndex1(tokens, indexs, "CROSS", "JOIN");
		trimPatternIndex1(tokens, indexs, "NATURAL", "JOIN");
		// outer_join_clause
		trimPatternIndex1(tokens, indexs, "NATURAL", "FULL", "OUTER", "JOIN");
		trimPatternIndex1(tokens, indexs, "NATURAL", "LEFT", "OUTER", "JOIN");
		trimPatternIndex1(tokens, indexs, "NATURAL", "RIGHT", "OUTER", "JOIN");
		trimPatternIndex1(tokens, indexs, "NATURAL", "FULL", "JOIN");
		trimPatternIndex1(tokens, indexs, "NATURAL", "LEFT", "JOIN");
		trimPatternIndex1(tokens, indexs, "NATURAL", "RIGHT", "JOIN");
		trimPatternIndex1(tokens, indexs, "FULL", "OUTER", "JOIN");
		trimPatternIndex1(tokens, indexs, "LEFT", "OUTER", "JOIN");
		trimPatternIndex1(tokens, indexs, "RIGHT", "OUTER", "JOIN");
		trimPatternIndex1(tokens, indexs, "FULL", "JOIN");
		trimPatternIndex1(tokens, indexs, "LEFT", "JOIN");
		trimPatternIndex1(tokens, indexs, "RIGHT", "JOIN");

		trimPatternIndex2(tokens, indexs, "PARTITION", "NATURAL");
		trimPatternIndex2(tokens, indexs, "PARTITION", "FULL");
		trimPatternIndex2(tokens, indexs, "PARTITION", "LEFT");
		trimPatternIndex2(tokens, indexs, "PARTITION", "RIGHT");

		if (0 < indexs.size()) { // 必ず1つ以上ある
			int pattern = getPatternSize(indexs);
			for (int i = pattern - 1; i >= 0; i--) {
				List<Integer> indexs2 = getPattern(indexs, i);
				if (0 == indexs2.size()) { // 必ず1つ以上ある条件のみ
					continue;
				}

				boolean match = true;
				result.clear();
				int index1 = start;

				for (int j = 0; j < indexs2.size(); j++) {
					int index2 = indexs2.get(j);

					if (0 == j) {
						TableReference tableReference = new TableReference(getNestIndex());
						if (! tableReference.analyze(tokens, index1, index2-index1)) {
							match = false;
							break;
						}
						result.add( tableReference.getSQLToken() );
					} else {
						if (startsWith(tokens, index1, index2-index1, "NATURAL", "INNER", "JOIN") || 
							startsWith(tokens, index1, index2-index1, "NATURAL", "JOIN") || 
							startsWith(tokens, index1, index2-index1, "INNER", "JOIN") || 
							startsWith(tokens, index1, index2-index1, "CROSS", "JOIN") || 
							startsWith(tokens, index1, index2-index1, "JOIN") ) {
							InnerCrossJoinClause clause = new InnerCrossJoinClause(getNestIndex());
							if (! clause.analyze(tokens, index1, index2-index1)) {
								match = false;
								break;
							}
							result.add( clause.getSQLToken() );
						} else {
							OuterJoinClause clause = new OuterJoinClause(getNestIndex());
							if (! clause.analyze(tokens, index1, index2-index1)) {
								match = false;
								break;
							}
							result.add( clause.getSQLToken() );
						}
					}
					index1 = index2;
				}
				if (!match) {
					continue;
				}
				
				if (start == index1) {
					TableReference tableReference = new TableReference(getNestIndex());
					if (! tableReference.analyze(tokens, index1, end-index1)) {
						continue;
					}
					result.add( tableReference.getSQLToken() );
				} else {
					if (startsWith(tokens, index1, end-index1, "NATURAL", "INNER", "JOIN") || 
						startsWith(tokens, index1, end-index1, "NATURAL", "JOIN") || 
						startsWith(tokens, index1, end-index1, "INNER", "JOIN") || 
						startsWith(tokens, index1, end-index1, "CROSS", "JOIN") || 
						startsWith(tokens, index1, end-index1, "JOIN") ) {
						InnerCrossJoinClause clause = new InnerCrossJoinClause(getNestIndex());
						if (! clause.analyze(tokens, index1, end-index1)) {
							continue;
						}
						result.add( clause.getSQLToken() );
					} else {
						OuterJoinClause clause = new OuterJoinClause(getNestIndex());
						if (! clause.analyze(tokens, index1, end-index1)) {
							continue;
						}
						result.add( clause.getSQLToken() );
					}
				}
				
				setSQLToken( new SQLToken(result) );
				return true;
			}
		}

		return false;
	}
}
