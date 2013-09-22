/*
 * 09/21/2013
 *
 * SearchResult - The result of a find, replace, or "mark all" operation.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;


/**
 * The result of a find, replace, or "mark all" operation.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see SearchEngine
 */
public class SearchResult {

	/**
	 * The number of instances found or replaced.
	 */
	private int count;

	/**
	 * The number of instances marked.
	 */
	private int markedCount;


	/**
	 * Constructor.
	 *
	 * @param count The number of matches found, replaced, or marked.
	 * @param markedCount The number of matches marked.
	 */
	public SearchResult(int count, int markedCount) {
		this.count = count;
		this.markedCount = markedCount;
	}


	/**
	 * Returns the number of instances found, replaced, or marked.  Note that,
	 * for "mark all" events, this value will be equal to
	 * {@link #getMarkedCount()}.
	 *
	 * @return The number of instances found.
	 * @see #wasFound()
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