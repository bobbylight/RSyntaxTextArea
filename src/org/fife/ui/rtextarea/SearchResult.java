/*
 * 09/21/2013
 *
 * SearchResult - The result of a find, replace, or "mark all" operation.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.rsyntaxtextarea.DocumentRange;


/**
 * The result of a find, replace, or "mark all" operation.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see SearchEngine
 */
public class SearchResult {

	/**
	 * If a find or replace operation is successful, this will be the range
	 * of text representing the found text (for "find" operations) or the
	 * replacement text inserted (for "replace" operations; for "replace all"
	 * operations this will be the last replacement region).  If no match was
	 * found, or this was a "mark all" operation, this will be
	 * <code>null</code>.
	 */
	private DocumentRange matchRange;

	/**
	 * The number of matches found or replaced.  For regular "find" and
	 * "replace" operations, this will be zero or <code>1</code>.  For "replace
	 * all" operations, this will be the number of replacements.  For "mark
	 * all" operations, this should be zero.
	 */
	private int count;

	/**
	 * The number of instances marked.
	 */
	private int markedCount;


	/**
	 * Constructor; indicates no match is found.
	 */
	public SearchResult() {
		this(null, 0, 0);
	}


	/**
	 * Constructor.
	 *
	 * @param range The selected range of text after the find or replace
	 *        operation.  This can be <code>null</code> if the selection was
	 *        not changed.
	 * @param count The number of matches found or replaced.  For regular
	 *        "find" and "replace" operations, this will be zero or
	 *        <code>1</code>; for "replace all" operations, this will be the
	 *        number of replacements.
	 * @param markedCount The number of matches marked.  If "mark all" is
	 *        disabled, this should be zero.
	 */
	public SearchResult(DocumentRange range, int count, int markedCount) {
		this.matchRange = range;
		this.count = count;
		this.markedCount = markedCount;
	}


	/**
	 * Returns the number of matches found or replaced.  For regular "find" and
	 * "replace" operations, this will be zero or <code>1</code>.  For "replace
	 * all" operations, this will be the number of replacements.  For "mark
	 * all" operations, this will be zero.
	 *
	 * @return The count.
	 * @see #setCount(int)
	 */
	public int getCount() {
		return count;
	}


	/**
	 * Returns the number of instances marked.  If "mark all" was not enabled,
	 * this will be <code>0</code>.
	 *
	 * @return The number of instances marked.
	 */
	public int getMarkedCount() {
		return markedCount;
	}


	/**
	 * If a find or replace operation is successful, this will be the range
	 * of text representing the found text (for "find" operations) or the
	 * replacement text inserted (for "replace" operations; for "replace all"
	 * operations this will be the last replacement region).  If no match was
	 * found, or this was a "mark all" operation, this will be
	 * <code>null</code>, since they do not update the editor's selection.
	 *
	 * @return The matched range of text.
	 * @see #setMatchRange(DocumentRange)
	 */
	public DocumentRange getMatchRange() {
		return matchRange;
	}


	/**
	 * Sets the number of matches found or replaced.  For regular "find" and
	 * "replace" operations, this should be zero or <code>1</code>.  For
	 * "replace all" operations, this should be the number of replacements. 
	 * For "mark all" operations, this should be zero.
	 *
	 * @param count The count.
	 * @see #getCount()
	 */
	public void setCount(int count) {
		this.count = count;
	}


	/**
	 * Sets the selected range for this search operation.
	 * 
	 * @param range The new selected range.
	 * @see #getMatchRange()
	 */
	public void setMatchRange(DocumentRange range) {
		this.matchRange = range;
	}


	/**
	 * Returns a string representation of this object.  Useful for debugging.
	 *
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return "[SearchResult: " +
				"count=" + getCount() +
				", markedCount=" + getMarkedCount() +
				", matchRange=" + getMatchRange() +
				"]";
	}


	/**
	 * Returns whether anything was found in this search operation.  This is
	 * shorthand for <code>getCount()>0</code>.
	 *
	 * @return Whether anything was found in this search operation.
	 * @see #getCount()
	 */
	public boolean wasFound() {
		return getCount()>0;
	}


}