/*
 * 02/19/2006
 *
 * RegExReplaceInfo.java - Information about a regex text match.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;


/**
 * Information on how to implement a regular expression "replace" operation.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RegExReplaceInfo {

	private final int startIndex;
	private final int endIndex;
	private final String replacement;


	/**
	 * Constructor.
	 *
	 * @param matchedText The text that matched the regular expression.
	 * @param start The start index of the matched text in the
	 *        <code>CharSequence</code> searched.
	 * @param end The end index of the matched text in the
	 *        <code>CharSequence</code> searched.
	 * @param replacement The text to replace the matched text with.  This
	 *        string has any matched groups and character escapes replaced.
	 */
	RegExReplaceInfo(int start, int end,
						String replacement) {
		this.startIndex = start;
		this.endIndex = end;
		this.replacement = replacement;
	}

	/**
	 * Returns the end index of the matched text.
	 *
	 * @return The end index of the matched text in the document searched.
	 * @see #getEndIndex()
	 */
	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * Returns the string to replaced the matched text with.
	 *
	 * @return The string to replace the matched text with.
	 */
	public String getReplacement() {
		return replacement;
	}

	/**
	 * Returns the start index of the matched text.
	 *
	 * @return The start index of the matched text in the document searched.
	 * @see #getEndIndex()
	 */
	public int getStartIndex() {
		return startIndex;
	}

}
