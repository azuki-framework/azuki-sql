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
package org.azkfw.sql;

import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.azkfw.component.text.TextEditor;
import org.azkfw.component.text.TextGradationsView;
import org.azkfw.component.text.TextLineNumberView;
import org.azkfw.sql.analyzer.LexicalAnalyzer;
import org.azkfw.sql.analyzer.SQLLexicalAnalyzer;
import org.azkfw.sql.syntax.SyntaxException;
import org.azkfw.sql.syntax.select.Select;
import org.azkfw.sql.token.Token;

/**
 * @author Kawakicchi
 *
 */
public class SQLFrame extends JFrame {
	
	public static void main(final String[] args) {
		SQLFrame frm = new SQLFrame();
		frm.setVisible(true);
	}

	/** serialVersionUID */
	private static final long serialVersionUID = -2707713780007263391L;

	private JSplitPane split1;
	private JSplitPane split2;
	
	private JScrollPane sclPlain1;
	private TextEditor txtPlain1;
	private JScrollPane sclPlain2;
	private TextEditor txtPlain2;
	private JScrollPane sclPlain3;
	private TextEditor txtPlain3;
	
	public SQLFrame() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(null);
		
		txtPlain1 = new TextEditor();
		sclPlain1 = new JScrollPane(txtPlain1);
		sclPlain1.setRowHeaderView(new TextLineNumberView(txtPlain1));
		sclPlain1.setColumnHeaderView(new TextGradationsView(txtPlain1));

		txtPlain2 = new TextEditor();
		sclPlain2 = new JScrollPane(txtPlain2);
		sclPlain2.setRowHeaderView(new TextLineNumberView(txtPlain2));
		sclPlain2.setColumnHeaderView(new TextGradationsView(txtPlain2));
		
		txtPlain3 = new TextEditor();
		sclPlain3 = new JScrollPane(txtPlain3);
		sclPlain3.setRowHeaderView(new TextLineNumberView(txtPlain3));
		sclPlain3.setColumnHeaderView(new TextGradationsView(txtPlain3));

		split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split1.setLocation(0, 0);
		
		split2.setLeftComponent(sclPlain2);
		split2.setRightComponent(sclPlain3);

		split1.setLeftComponent(sclPlain1);
		split1.setRightComponent(split2);
		add(split1);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				Insets insets = getInsets();
				int width = getWidth() - (insets.left + insets.right);
				int height = getHeight() - (insets.top + insets.bottom);
				split1.setSize(width, height);
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				doLoad();
			}
		});
		
		split1.setDividerLocation(350);
		split2.setDividerLocation(500);
		setSize(1400, 600);
		
	}
	private void doLoad() {
		LexicalAnalyzer analyzer = new SQLLexicalAnalyzer();
		analyzer.analyze(getString(Paths.get("src", "test", "resources", "select01.sql").toFile(), "UTF-8"));
		// analyzer.analyze(getString(Paths.get("src", "test", "resources", "update01.sql").toFile(), "UTF-8"));

		List<Token> tokens = new ArrayList<Token>();
		for (Token token : analyzer.getTokenList()) {
			if (!"DUST".equals(token.getType())) {
				tokens.add(token);
			}
		}
		
		String str1 = toString(tokens);
		System.out.println(str1);
		txtPlain1.setText(str1);

		try {
			Select a = new Select();
			// Update a = new Update();
			if (a.analyze(tokens)) {
				String str3 = a.getSQLToken().toString();
				System.out.println(str3);
				txtPlain3.setText(str3);
			} else {
				System.out.println("ERROR");
			}
		} catch (SyntaxException ex) {
			ex.printStackTrace();
		}
	}
	
	public static String toString(final List<Token> tokens) {
		String crlf = "\n";
		try {
			crlf = System.getProperty("line.separator");
		} catch (SecurityException e) {
		}

		StringBuffer s = new StringBuffer();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			// s.append(String.format("%04d %10s  %s%s", i, s(token.getType()), token.getToken(), crlf));
			s.append(String.format("%10s  %s%s", s(token.getType()), token.getToken(), crlf));
		}
		return s.toString();
	}

	public static String s(final String string) {
		String str = "";
		if (null != string) {
			str = string;
		}
		return str;
	}

	public static String getString(final File file, final String charset) {
		StringBuffer s = new StringBuffer();
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file), charset);
			char[] buffer = new char[1024];
			int size = -1;
			while (-1 != (size = reader.read(buffer, 0, 1024))) {
				s.append(buffer, 0, size);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return s.toString();
	}
}
