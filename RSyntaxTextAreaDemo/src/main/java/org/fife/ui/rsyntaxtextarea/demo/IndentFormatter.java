/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.demo;


/**
 * Formats indent levels into prefix strings (spaces or tabs).
 *
 * @author RSyntaxTextArea Demo
 * @version 1.0
 */
public class IndentFormatter {

	private final boolean useTabs;
	private final int tabSize;
	private final int spacesPerIndent;


	public IndentFormatter() {
		this(false, 4, 4);
	}


	public IndentFormatter(boolean useTabs, int tabSize, int spacesPerIndent) {
		this.useTabs = useTabs;
		this.tabSize = tabSize > 0 ? tabSize : 4;
		this.spacesPerIndent = spacesPerIndent > 0 ? spacesPerIndent : 4;
	}


	public String format(int indentLevel) {
		if (indentLevel <= 0) {
			return "";
		}

		if (useTabs) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < indentLevel; i++) {
				sb.append('\t');
			}
			return sb.toString();
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indentLevel * spacesPerIndent; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}


	public boolean isUseTabs() {
		return useTabs;
	}


	public int getTabSize() {
		return tabSize;
	}


	public int getSpacesPerIndent() {
		return spacesPerIndent;
	}


}
