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
package org.azkfw.sql.syntax;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.azkfw.analysis.lexical.scanner.SQLScanner;
import org.azkfw.analysis.lexical.scanner.Scanner;
import org.azkfw.analysis.lexical.scanner.Token;
import org.azkfw.analysis.lexical.scanner.Tokens;
import org.azkfw.analysis.lexical.scanner.pattern.CommentTokenPattern;
import org.azkfw.analysis.lexical.scanner.pattern.DustTokenPattern;
import org.azkfw.analysis.util.AnalysisUtility;

import junit.framework.TestCase;

/**
 * @author Kawakicchi
 *
 */
public class SyntaxTestCase extends TestCase {
	
	protected String getCharset() {
		return "UTF-8";
	}
	protected String getBaseDir() {
		return Paths.get(".", "src", "test", "data", "sql", "syntax").toString();
	}

	protected final List<Token> getTokens(final String...more) {
		File file = Paths.get(getBaseDir(), more).toFile();
		
		String source = AnalysisUtility.getString(file, getCharset());
		
		Scanner scanner = new SQLScanner();
		Tokens ts = scanner.scan(source);

		List<Token> tokens = new ArrayList<Token>();
		for (Token token : ts.list()) {
			if (DustTokenPattern.NAME.equals(token.getType())) {
				continue;
			}
			if (CommentTokenPattern.NAME.equals(token.getType())) {
				continue;
			}
			tokens.add(token);
		}
		return tokens;
	}
}
