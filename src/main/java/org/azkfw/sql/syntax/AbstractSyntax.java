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

import java.util.ArrayList;
import java.util.List;

import org.azkfw.analysis.lexical.scanner.Token;
import org.azkfw.sql.token.SQLToken;

/**
 * 
 * @author Kawakicchi
 */
public abstract class AbstractSyntax implements Syntax {

	private int index;
	private String indexSpace;
	
	private SQLToken sqlToken;
	
	public AbstractSyntax() {
		index = 0;
		indexSpace = "";
	}
	
	public AbstractSyntax(final int index) {
		this.index = index;
		indexSpace = getSpace(index*2);
	}
		
	public final SQLToken getSQLToken() {
		return sqlToken;
	}
	
	protected int getNestIndex() {
		return index+1;
	}
	
	protected final void setSQLToken(final SQLToken token) {
		sqlToken = token;
	}
	
	@Override
	public final boolean analyze(final List<Token> tokens) throws SyntaxException {
		boolean result = false;
		result = doAnalyze(tokens, 0, tokens.size());
		return result;
	}

	@Override
	public final boolean analyze(final List<Token> tokens, final int offset) throws SyntaxException {
		boolean result = false;
		result = doAnalyze(tokens, offset, tokens.size() - offset);
		return result;
	}

	@Override
	public final boolean analyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException {
		boolean result = false;
		result = doAnalyze(tokens, offset, length);
		return result;
	}

	/**
	 * 構文解析を行う。
	 * 
	 * @param tokens トークンリスト
	 * @param offset オフセット
	 * @param length サイズ
	 * @return 解析結果。
	 * @throws SyntaxException 構文が不正な場合
	 */
	protected abstract boolean doAnalyze(final List<Token> tokens, final int offset, final int length) throws SyntaxException;

	protected static boolean isEqualsTokens(final List<Token> tokens, final int offset, final String...keywords) {
		for (int i = 0 ; i < keywords.length ; i++) {
			if (tokens.size() <= offset+i) return false;
			Token token = tokens.get(offset+1);
			if (!isEqualsToken(token, keywords[i])) {
				return false;
			}
		}
		return true;
	}
	
	protected static int indexOf(final List<Token> tokens, final int offset, final int length, final String...keywords) {
		int index = -1;
		for (int i = offset ; i < offset + length ; i++) {
			Token token = tokens.get(i);
			if (isEqualsToken(token, keywords)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	protected static int indexOfEx(final List<Token> tokens, final int offset, final int length, final String...keywords) {
		int cntParen = 0;
		int index = -1;
		for (int i = offset ; i < offset + length ; i++) {
			Token token = tokens.get(i);
			
			if (isEqualsToken(token, "(")) {
				cntParen ++;
			} else if (isEqualsToken(token, ")")) {
				cntParen --;
			}

			if (0 == cntParen) {
				if (isEqualsToken(token, keywords)) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	protected static int lastIndexOf(final List<Token> tokens, final int offset, final int length, final String...keywords) {
		int index = -1;
		for (int i = offset + length - 1 ; i >= offset ; i--) {
			if (isEqualsToken(tokens.get(i), keywords)) {
				index = i;
				break;
			}
		}
		return index;
	}

	protected static boolean startsWith(final List<Token> tokens, final int offset, final int length, final String...keywords) {
		if (length < keywords.length) return false;
		for (int i = 0 ; i < keywords.length ; i++) {
			Token token = tokens.get(offset + i);
			if (!isEqualsToken(token, keywords[i])) return false;
		}		
		return true;
	}
	
	protected static boolean endsWith(final List<Token> tokens, final int offset, final int length, final String...keywords) {
		if (length < keywords.length) return false;
		for (int i = 0 ; i < keywords.length ; i++) {
			Token token = tokens.get(offset + length - (keywords.length-i));
			if (!isEqualsToken(token, keywords[i])) return false;
		}
		return true;
	}

	protected List<Integer> splitToken(final List<Token> tokens, final int offset, final int length, final String...keywords) {
		// trace(toString(tokens, offset, length));
		List<Integer> indexs = new ArrayList<Integer>();
		for (int i = offset ; i < offset + length ; i++) {
			Token token = tokens.get(i);
			if (isEqualsToken(token, keywords)) {
				indexs.add(i);
			}
		}
		return indexs;
	}

	protected List<Integer> splitTokenEx(final List<Token> tokens, final int offset, final int length, final String...keywords) {
		// trace(toString(tokens, offset, length));
		int cntParen = 0;
		List<Integer> indexs = new ArrayList<Integer>();
		for (int i = offset ; i < offset + length ; i++) {
			Token token = tokens.get(i);

			if (isEqualsToken(token, "(")) {
				cntParen ++;
			} else if (isEqualsToken(token, ")")) {
				cntParen --;
			}

			if (0 == cntParen) {
				if (isEqualsToken(token, keywords)) {
					indexs.add(i);
				}
			}

		}
		return indexs;
	}

	protected static boolean isEqualsToken(final Token token, final List<String> keywords) {
		boolean result = false;
		if (null != token && null != keywords) {
			String string = token.getToken();
			if (null != string) {
				string = string.toUpperCase();
				for (String keyword : keywords) {
					if (string.equals(keyword.toUpperCase())) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}
	protected static boolean isEqualsToken(final Token token, final String... keywords) {
		boolean result = false;
		if (null != token && null != keywords) {
			String string = token.getToken();
			if (null != string) {
				string = string.toUpperCase();
				for (String keyword : keywords) {
					if (string.equals(keyword.toUpperCase())) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}
	
	protected final int getPatternSize(final List<Integer> indexs) {
		return (int)Math.pow(2, indexs.size());		
	}
	
	protected final List<Integer> getPattern(final List<Integer> indexs, final int pattern) {
		List<Integer> result = new ArrayList<Integer>();
		//System.out.print("[");
		for (int index = 0 ; index < indexs.size() ; index++) {
			if (0 == (pattern & (int)Math.pow(2,index))) {
				//System.out.print("0");
			} else {
				//System.out.print("1");
				result.add(indexs.get(index));
			}
		}
		//System.out.print("] ");
		//System.out.println(toString(result));
		return result;
	}
	protected final String toString(final List<Token> tokens, final int start, final int length) {
		StringBuffer s = new StringBuffer();
		for (int i = start ; i < start + length ; i++) {
			if (0 < s.length()) {
				s.append(" ");
			}
			s.append(tokens.get(i).getToken());
		}
		return s.toString();
	}
	
	protected final String toString(final List<Integer> indexs) {
		StringBuilder s = new StringBuilder();
		for (int index : indexs) {
			if (0 != s.length()) {
				s.append(" ");
			}
			s.append(index);
		}
		return s.toString();
	}
	
	protected void print(final List<Token> tokens, final int offset, final int length, final int index) {
		StringBuffer left = new StringBuffer();
		for (int i = offset ; i < index ; i++) {
			if (0 < left.length()) {
				left.append(" ");
			}
			left.append(tokens.get(i).getToken());
		}
		StringBuffer right = new StringBuffer();
		for (int i = index ; i < length ; i++) {
			if (0 < right.length()) {
				right.append(" ");
			}
			right.append(tokens.get(i).getToken());			
		}
		trace(String.format("%s - %s", left.toString(), right.toString()));
	}
	
	private String getSpace(final int index) {
		StringBuffer s = new StringBuffer();
		for (int i = 0 ; i < index ; i++) {
			s.append(" ");
		}
		return s.toString();
	}
	
	private static final String LOG_LEVEL_DEBUG = "DEBUG";
	private static final String LOG_LEVEL_TRACE = "TRACE";
	private static final String LOG_LEVEL_ERROR = "ERROR";

	protected final void trace(final String message) {
		log(LOG_LEVEL_TRACE, message);
	}
	
	protected final void debug(final String message) {
		log(LOG_LEVEL_DEBUG, message);
	}
	protected final void error(final String message) {
		log(LOG_LEVEL_ERROR, message);
	}

	private final void log(final String level, final String log) {
		StackTraceElement element = new Throwable().getStackTrace()[2];
		//String s = String.format("[%5s] %s(%d) %s",level, element.getClassName(), element.getLineNumber(), log);
		String name = element.getClassName().substring(element.getClassName().lastIndexOf(".")+1);
		String s = String.format("[%26s] %s%s", name, indexSpace , log);
		System.out.println(s);
	}
}
