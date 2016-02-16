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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.azkfw.analysis.lexical.Token;

/**
 * このクラスは、字句解析のスキャナー機能を実装する為の基底クラスです。
 * 
 * @author Kawakicchi
 */
public abstract class AbstractScanner implements Scanner {

	@Override
	public final List<Token> scan(final String string) {
		return doScan(string);
	}

	@Override
	public final List<Token> scan(final File file) throws IOException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public final List<Token> scan(final File file, final String charset) throws IOException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public final List<Token> scan(final File file, final Charset charset) throws IOException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	protected abstract List<Token> doScan(final String string);
}
