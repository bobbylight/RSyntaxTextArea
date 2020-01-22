/*
 * 09/21/2013
 *
 * SearchResult - The result of a find, replace, or "mark all" operation.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
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
public class SearchResult implements Comparable<SearchResult> {

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
	 * Whether this result is from a "wrapping" search - that is, no results were found from caret -> end of document,
	 * but a result _was_ found in a subsequent search from document start -> caret.
	 */
	private boolean wrapped;


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
	 * Compares this search result to another.
	 *
	 * @param other Another search result to compare to.
	 * @return How this result object should be sorted compared to
	 *         <code>other</code>.
	 */
	@Override
	public int compareTo(SearchResult other) {
		if (other==null) {
			return 1;
		}
		if (other==this) {
			return 0;
		}
		int diff = count - other.count;
		if (diff!=0) {
			return diff;
		}
		diff = markedCount - other.markedCount;
		if (diff!=0) {
			return diff;
		}
		if (matchRange==null) {
			return other.matchRange==null ? 0 : -1;
		}
		return matchRange.compareTo(other.matchRange);
	}


	/**
	 * Returns whether this search result represents the same logical result
	 * as another.
	 *
	 * @param other Another object (presumably another
	 *        <code>SearchResult</code>).
	 */
	@Override
	public boolean equals(Object other) {
		if (other==this) {
			return true;
		}
		if (other instanceof SearchResult) {
			return this.compareTo((SearchResult)other)==0;
		}
		return false;
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
	 * @see #setMarkedCount(int)
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
	 * Overridden simply as a best practice, since {@link #equals(Object)} is
	 * overridden.
	 *
	 * @return The hash code for this object.
	 */
	@Override
	public int hashCode() {
		int hash = count + markedCount;
		if (matchRange!=null) {
			hash += matchRange.hashCode();
		}
		return hash;
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
	 * Sets the number of marked occurrences found.
	 *
	 * @param markedCount The number of marked occurrences found.
	 * @see #getMarkedCount()
	 */
	public void setMarkedCount(int markedCount) {
		this.markedCount = markedCount;
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
	 * Marks this search result as a 'wrapped' result; that is, it came from the second pass through a
	 * document, after an initial search forward or backward from the caret didn't return anything.
	 *
	 * @param wrapped The new 'wrapped' state. Defaults to false.
	 * @see #isWrapped()
	 * @see SearchContext#setSearchWrap(boolean)
	 */
	public void setWrapped(boolean wrapped) {
		this.wrapped = wrapped;
	}


	/**
	 * Returns whether this is a 'wrapped' result, meaning it came from a second pass through the document, after an
	 * initial search forward or backward from the caret didn't return anything.
	 *
	 * @return true if the result was from a 'wrapping' search; false otherwise.
	 * @see SearchContext#setSearchWrap(boolean)
	 */
	public boolean isWrapped() {
		return wrapped;
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
	 * shorthand for <code>getCount()&gt;0</code>.
	 *
	 * @return Whether anything was found in this search operation.
	 * @see #getCount()
	 */
	public boolean wasFound() {
		return getCount()>0;
	}


}
