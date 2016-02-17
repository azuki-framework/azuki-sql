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
package org.azkfw.analysis.lexical.scanner.pattern;

/**
 * このインターフェースは、トークンパターンを定義するインターフェースです。
 * @author Kawakicchi
 */
public interface TokenPattern {

	/**
	 * 
	 * @return 名前
	 */
	public String getName();
	
	/**
	 * 
	 * @param string 文字列
	 * @param index 読み込み開始位置
	 * @return 読み込み数、マッチしなかった場合、<code>null</code>を返す。
	 */
	public Integer start(final String string, final int index);

	/**
	 * 
	 * @param string 文字列
	 * @param index 読み込み開始位置
	 * @return 読み込み数、読み込み終了の場合、<code>null</code>を返す。
	 */
	public Integer read(final String string, final int index);

}
