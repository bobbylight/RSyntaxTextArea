/*
 * 08/11/2009
 *
 * DocumentRange.java - A range of text in a document.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;


/**
 * A range of text in a document.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface DocumentRange {


	/**
	 * Gets the end offset of the range.
	 *
	 * @return The end offset.
	 * @see #getStartOffset()
	 */
	public int getEndOffset();


	/**
	 * Gets the starting offset of the range.
	 *
	 * @return The starting offset.
	 * @see #getEndOffset()
	 */
	public int getStartOffset();


}