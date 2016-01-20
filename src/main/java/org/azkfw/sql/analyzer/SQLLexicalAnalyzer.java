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
package org.azkfw.sql.analyzer;

import org.azkfw.sql.analyzer.pattern.AtSignTokenPattern;
import org.azkfw.sql.analyzer.pattern.CommaTokenPattern;
import org.azkfw.sql.analyzer.pattern.EqualTokenPattern;
import org.azkfw.sql.analyzer.pattern.MultiLineCommentTokenPattern;
import org.azkfw.sql.analyzer.pattern.NumberTokenPattern;
import org.azkfw.sql.analyzer.pattern.OracleHintTokenPattern;
import org.azkfw.sql.analyzer.pattern.ParenTokenPattern;
import org.azkfw.sql.analyzer.pattern.PeriodTokenPattern;
import org.azkfw.sql.analyzer.pattern.PlusSignTokenPattern;
import org.azkfw.sql.analyzer.pattern.ReturnTokenPattern;
import org.azkfw.sql.analyzer.pattern.SemicolonTokenPattern;
import org.azkfw.sql.analyzer.pattern.SingleLineCommentTokenPattern;
import org.azkfw.sql.analyzer.pattern.SpaceTokenPattern;
import org.azkfw.sql.analyzer.pattern.StringTokenPattern;
import org.azkfw.sql.analyzer.pattern.TabTokenPattern;

/**
 * 
 * @author Kawakicchi
 */
public class SQLLexicalAnalyzer extends PatternLexicalAnalyzer {

	public SQLLexicalAnalyzer() {
		addPattern(new OracleHintTokenPattern("HINT"));
		addPattern(new MultiLineCommentTokenPattern("COMMENT"));
		addPattern(new SingleLineCommentTokenPattern("COMMENT"));

		addPattern(new StringTokenPattern("STRING"));
		addPattern(new NumberTokenPattern("NUMBER"));

		addPattern(new ReturnTokenPattern("DUST"));
		addPattern(new SpaceTokenPattern("DUST"));
		addPattern(new TabTokenPattern("DUST"));

		addPattern(new ParenTokenPattern()); // ()
		addPattern(new SemicolonTokenPattern()); // ;
		addPattern(new AtSignTokenPattern()); // @
		addPattern(new PeriodTokenPattern()); // .
		addPattern(new CommaTokenPattern()); // ,
		addPattern(new PlusSignTokenPattern()); // +
		addPattern(new EqualTokenPattern()); // =
		// addPattern(new CommaTokenPattern());
		// addPattern(new ParenTokenPattern());
		// addPattern(new OperatorTokenPattern());
	}
}
