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

import java.util.ArrayList;
import java.util.List;

import org.azkfw.sql.token.Token;

/**
 * このクラスは、字句解析を行う為の基底クラスです。
 * 
 * @author Kawakicchi
 */
public abstract class AbstractLexicalAnalyzer implements LexicalAnalyzer {

	/** トークンリスト */
	private List<Token> tokens;

	/**
	 * コンストラクタ
	 */
	public AbstractLexicalAnalyzer() {
		tokens = new ArrayList<Token>();
	}

	@Override
	public final void analyze(final String string) {
		tokens.clear();
		doAnalyze(string);
	}

	@Override
	public final List<Token> getTokenList() {
		return tokens;
	}

	/**
	 * トークンを追加する。
	 * 
	 * @param token トークン
	 */
	protected final void addToken(final Token token) {
		tokens.add(token);
	}

	/**
	 * 解析を行う。
	 * 
	 * @param string 文字列
	 */
	protected abstract void doAnalyze(final String string);
}
