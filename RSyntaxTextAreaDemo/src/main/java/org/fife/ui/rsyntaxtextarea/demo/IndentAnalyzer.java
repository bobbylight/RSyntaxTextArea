/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.demo;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;


/**
 * Analyzes text content to determine the suggested indent level for the next line.
 * Supports Java and Python languages.
 *
 * @author RSyntaxTextArea Demo
 * @version 1.0
 */
public class IndentAnalyzer {

	private final int spacesPerIndent;


	public IndentAnalyzer() {
		this(4);
	}


	public IndentAnalyzer(int spacesPerIndent) {
		this.spacesPerIndent = spacesPerIndent > 0 ? spacesPerIndent : 4;
	}


	public IndentResult analyze(String text, int caretLine, String syntaxStyle) {
		try {
			if (text == null || text.isEmpty()) {
				return IndentResult.empty();
			}

			String[] lines = text.split("\n", -1);
			if (caretLine < 0 || caretLine >= lines.length) {
				return IndentResult.empty();
			}

			String currentLine = lines[caretLine];

			if (SyntaxConstants.SYNTAX_STYLE_JAVA.equals(syntaxStyle)) {
				return analyzeJava(lines, caretLine, currentLine);
			} else if (SyntaxConstants.SYNTAX_STYLE_PYTHON.equals(syntaxStyle)) {
				return analyzePython(lines, caretLine, currentLine);
			} else {
				return IndentResult.unsupported();
			}
		} catch (Exception e) {
			return IndentResult.empty();
		}
	}


	private IndentResult analyzeJava(String[] lines, int caretLine, String currentLine) {
		int braceBalance = 0;
		boolean inString = false;
		boolean inChar = false;
		boolean inBlockComment = false;
		boolean inLineComment = false;

		for (int i = 0; i <= caretLine; i++) {
			String line = lines[i];
			inLineComment = false;
			char[] chars = line.toCharArray();

			for (int j = 0; j < chars.length; j++) {
				char c = chars[j];
				char next = (j + 1 < chars.length) ? chars[j + 1] : 0;

				if (inBlockComment) {
					if (c == '*' && next == '/') {
						inBlockComment = false;
						j++;
					}
					continue;
				}

				if (inLineComment) {
					continue;
				}

				if (inString) {
					if (c == '"' && (j == 0 || chars[j - 1] != '\\')) {
						inString = false;
					}
					continue;
				}

				if (inChar) {
					if (c == '\'' && (j == 0 || chars[j - 1] != '\\')) {
						inChar = false;
					}
					continue;
				}

				if (c == '/' && next == '*') {
					inBlockComment = true;
					j++;
					continue;
				}

				if (c == '/' && next == '/') {
					inLineComment = true;
					continue;
				}

				if (c == '"') {
					inString = true;
					continue;
				}

				if (c == '\'') {
					inChar = true;
					continue;
				}

				if (c == '{') {
					braceBalance++;
				} else if (c == '}') {
					braceBalance--;
				}
			}
		}

		String trimmedLine = currentLine.trim();
		boolean lineEndsWithOpenBrace = trimmedLine.endsWith("{");
		boolean lineStartsWithCloseBrace = trimmedLine.startsWith("}");

		int suggestedIndent = braceBalance;

		if (lineEndsWithOpenBrace) {
			suggestedIndent = braceBalance;
		}

		if (lineStartsWithCloseBrace && braceBalance > 0) {
			suggestedIndent = braceBalance - 1;
		}

		if (suggestedIndent < 0) {
			suggestedIndent = 0;
		}

		return new IndentResult(true, suggestedIndent, currentLine);
	}


	private IndentResult analyzePython(String[] lines, int caretLine, String currentLine) {
		int currentIndent = calculatePythonIndentLevel(currentLine);

		String trimmedLine = currentLine.trim();
		boolean isEmptyLine = trimmedLine.isEmpty();
		boolean isComment = trimmedLine.startsWith("#");

		if (isEmptyLine || isComment) {
			if (caretLine > 0) {
				for (int i = caretLine - 1; i >= 0; i--) {
					String prevLine = lines[i];
					String prevTrimmed = prevLine.trim();
					if (!prevTrimmed.isEmpty() && !prevTrimmed.startsWith("#")) {
						currentIndent = calculatePythonIndentLevel(prevLine);
						boolean prevEndsWithColon = prevTrimmed.endsWith(":");
						if (prevEndsWithColon) {
							return new IndentResult(true, currentIndent + 1, currentLine);
						}
						break;
					}
				}
			}
			return new IndentResult(true, currentIndent, currentLine);
		}

		boolean lineEndsWithColon = trimmedLine.endsWith(":");

		if (lineEndsWithColon) {
			return new IndentResult(true, currentIndent + 1, currentLine);
		}

		return new IndentResult(true, currentIndent, currentLine);
	}


	private int calculatePythonIndentLevel(String line) {
		int indent = 0;
		for (char c : line.toCharArray()) {
			if (c == ' ') {
				indent++;
			} else if (c == '\t') {
				indent += spacesPerIndent;
			} else {
				break;
			}
		}
		return indent / spacesPerIndent;
	}


	public int getSpacesPerIndent() {
		return spacesPerIndent;
	}


	public static class IndentResult {
		private final boolean supported;
		private final int indentLevel;
		private final String currentLine;
		private final boolean empty;


		private IndentResult(boolean supported, int indentLevel, String currentLine) {
			this.supported = supported;
			this.indentLevel = indentLevel;
			this.currentLine = currentLine;
			this.empty = false;
		}


		private IndentResult(boolean empty) {
			this.supported = false;
			this.indentLevel = 0;
			this.currentLine = "";
			this.empty = empty;
		}


		public static IndentResult empty() {
			return new IndentResult(true);
		}


		public static IndentResult unsupported() {
			return new IndentResult(false, 0, "");
		}


		public boolean isSupported() {
			return supported;
		}


		public int getIndentLevel() {
			return indentLevel;
		}


		public String getCurrentLine() {
			return currentLine;
		}


		public boolean isEmpty() {
			return empty;
		}
	}

}
