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
package org.azkfw.analysis.lexical.scanner;

import org.azkfw.analysis.lexical.scanner.pattern.AtSignTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.CommaTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.DustTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.EqualTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.MultiLineCommentTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.NumberTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.OracleHintTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.ParenTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.PeriodTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.PlusSignTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.ReturnTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.SemicolonTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.SingleLineCommentTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.SpaceTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.StringTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.TabTokenPattern;

/**
 * @author Kawakicchi
 *
 */
public class SQLScanner extends PatternScanner {

	public SQLScanner () {
		addPattern(new OracleHintTokenPattern());
		addPattern(new MultiLineCommentTokenPattern());
		addPattern(new SingleLineCommentTokenPattern());

		addPattern(new StringTokenPattern());
		addPattern(new NumberTokenPattern());

		addPattern(new DustTokenPattern());

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
