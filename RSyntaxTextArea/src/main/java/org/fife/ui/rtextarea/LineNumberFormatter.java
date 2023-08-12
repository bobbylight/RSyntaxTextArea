/*
 * 29/07/2023
 *
 * LineNumber - Format line numbers into a comprehensible String.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

/**
 * Formats line numbers into a comprehensible String to be
 * displayed to the user.
 *
 * @author Mino260806
 */
public interface LineNumberFormatter {
	/**
	 * Formats the given line number into a String.
	 *
	 * @param lineNumber the line number
	 * @return the formatted line number
	 */
	String format(int lineNumber);

	/**
	 * Returns the maximum number of characters of any string returned
	 * by {@link #format} provided a certain maximum line number.
	 *
	 * @param maxLineNumber the maximum line number
	 * @return the maximum length
	 */
	int getMaxLength(int maxLineNumber);
}
