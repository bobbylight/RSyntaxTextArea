/*
 * 02/17/2012
 *
 * SearchContext.java - Container for options of a search/replace operation.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;


/**
 * Contains information about a find/replace operation.  Applications can
 * keep an instance of this class around and use it to maintain the user's
 * selection for options such as "match case," "regular expression," etc.,
 * between search operations.  They can then pass the instance as a parameter
 * to the public {@link SearchEngine} methods to do the actual searching.
 *
 * @author Robert Futrell
 * @version 2.0
 * @see SearchEngine
 */
public class SearchContext implements Cloneable, Serializable {

	/** Fired when the "search for" property is modified. */
	public static final String PROPERTY_SEARCH_FOR		= "Search.searchFor";

	/** Fired when the "replace with" property is modified. */
	public static final String PROPERTY_REPLACE_WITH	= "Search.replaceWith";

	/** Fired when the "match case" property is toggled. */
	public static final String PROPERTY_MATCH_CASE		= "Search.MatchCase";

	/** Fired when the "whole word" property is toggled. */
	public static final String PROPERTY_MATCH_WHOLE_WORD	= "Search.MatchWholeWord";

	/** Fired when search direction is toggled. */
	public static final String PROPERTY_SEARCH_FORWARD = "Search.Forward";

	/** Fired when search wrap is toggled. */
	public static final String PROPERTY_SEARCH_WRAP = "Search.Wrap";

	/** Fired when "search in selection" is toggled (not currently supported). */
	public static final String PROPERTY_SELECTION_ONLY = "Search.SelectionOnly";

	/** Fired when "use regular expressions" is toggled. */
	public static final String PROPERTY_USE_REGEX		= "Search.UseRegex";

	/** Fired when the user toggles the "Mark All" property. */
	public static final String PROPERTY_MARK_ALL = "Search.MarkAll";

	private String searchFor;
	private String replaceWith;
	private boolean forward;
	private boolean wrap;
	private boolean matchCase;
	private boolean wholeWord;
	private boolean regex;
	private boolean selectionOnly;
	private boolean markAll;

	private transient PropertyChangeSupport support;

	private static final long serialVersionUID = 1L;


	/**
	 * Creates a new search context.  Specifies a forward search,
	 * case-insensitive, not whole-word, not a regular expression.
	 */
	public SearchContext() {
		this(null);
	}


	/**
	 * Creates a new search context.  Specifies a forward search,
	 * case-insensitive, not whole-word, not a regular expression.
	 *
	 * @param searchFor The text to search for.
	 */
	public SearchContext(String searchFor) {
		this(searchFor, false);
	}


	/**
	 * Creates a new search context.  Specifies a forward search, not
	 * whole-word, not a regular expression.
	 *
	 * @param searchFor The text to search for.
	 * @param matchCase Whether to do a case-sensitive search.
	 */
	public SearchContext(String searchFor, boolean matchCase) {
		support = new PropertyChangeSupport(this);
		this.searchFor = searchFor;
		this.matchCase = matchCase;
		markAll = true;
		forward = true;
		wrap = false;
	}


	/**
	 * Adds a property change listener to this context.
	 *
	 * @param l The new listener.
	 * @see #removePropertyChangeListener(PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		support.addPropertyChangeListener(l);
	}


	@Override
	public SearchContext clone() {
		try {
			SearchContext context = null;
			context = (SearchContext)super.clone();
			// Don't copy over listeners
			context.support = new PropertyChangeSupport(context);
			return context;
		} catch (CloneNotSupportedException cnse) { // Never happens
			throw new RuntimeException("Should never happen", cnse);
		}
	}


	protected void firePropertyChange(String property, boolean oldValue,
			boolean newValue) {
		support.firePropertyChange(property, oldValue, newValue);
	}


	protected void firePropertyChange(String property, String oldValue,
			String newValue) {
		support.firePropertyChange(property, oldValue, newValue);
	}


	/**
	 * Returns whether "mark all" should be selected or enabled.
	 *
	 * @return Whether "mark all" should be enabled.
	 * @see #setMarkAll(boolean)
	 */
	public boolean getMarkAll() {
		return markAll;
	}


	/**
	 * Returns whether case should be honored while searching.
	 *
	 * @return Whether case should be honored.
	 * @see #setMatchCase(boolean)
	 */
	public boolean getMatchCase() {
		return matchCase;
	}


	/**
	 * Returns the text to replace with, if doing a replace operation.
	 *
	 * @return The text to replace with.
	 * @see #setReplaceWith(String)
	 * @see #getSearchFor()
	 */
	public String getReplaceWith() {
		return replaceWith;
	}


	/**
	 * Returns the text to search for.
	 *
	 * @return The text to search for.
	 * @see #setSearchFor(String)
	 * @see #getReplaceWith()
	 */
	public String getSearchFor() {
		return searchFor;
	}


	/**
	 * Returns whether the search should be forward through the text (vs.
	 * backwards).
	 *
	 * @return Whether we should search forwards.
	 * @see #setSearchForward(boolean)
	 */
	public boolean getSearchForward() {
		return forward;
	}


	/**
	 * Returns whether the search should wrap.
	 *
	 * @return Whether we should search wrap
	 * @see #setSearchWrap(boolean)
	 */
	public boolean getSearchWrap() {
		return wrap;
	}


	/**
	 * Returns whether the search should only be done in the selected text.
	 * This flag is currently not supported.
	 *
	 * @return Whether only the selected text should be searched.
	 * @see #setSearchSelectionOnly(boolean)
	 */
	public boolean getSearchSelectionOnly() {
		return selectionOnly;
	}


	/**
	 * Returns whether only "whole word" matches should be returned.  A match
	 * is considered to be "whole word" if the character on either side of the
	 * matched text is a non-word character, or if there is no character on
	 * one side of the word, such as when it's at the beginning or end of a
	 * line.
	 *
	 * @return Whether only "whole word" matches should be returned.
	 * @see #setWholeWord(boolean)
	 */
	public boolean getWholeWord() {
		return wholeWord;
	}


	/**
	 * Returns whether a regular expression search should be done.
	 *
	 * @return Whether a regular expression search should be done.
	 * @see #setRegularExpression(boolean)
	 */
	public boolean isRegularExpression() {
		return regex;
	}


	/**
	 * Removes a property change listener from this context.
	 *
	 * @param l The listener to remove.
	 * @see #addPropertyChangeListener(PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		support.removePropertyChangeListener(l);
	}


	/**
	 * Sets whether "mark all" should be selected or enabled.  This method
	 * fires property change events of type {@link #PROPERTY_MARK_ALL}.
	 *
	 * @param markAll Whether "mark all" should be enabled.
	 * @see #getMarkAll()
	 */
	public void setMarkAll(boolean markAll) {
		if (markAll!=this.markAll) {
			this.markAll = markAll;
			firePropertyChange(PROPERTY_MARK_ALL, !markAll, markAll);
		}
	}


	/**
	 * Sets whether case should be honored while searching. This method
	 * fires a property change event of type {@link #PROPERTY_MATCH_CASE}.
	 *
	 * @param matchCase Whether case should be honored.
	 * @see #getMatchCase()
	 */
	public void setMatchCase(boolean matchCase) {
		if (matchCase!=this.matchCase) {
			this.matchCase = matchCase;
			firePropertyChange(PROPERTY_MATCH_CASE, !matchCase, matchCase);
		}
	}


	/**
	 * Sets whether a regular expression search should be done.This method
	 * fires a property change event of type {@link #PROPERTY_USE_REGEX}.
	 *
	 * @param regex Whether a regular expression search should be done.
	 * @see #isRegularExpression()
	 */
	public void setRegularExpression(boolean regex) {
		if (regex!=this.regex) {
			this.regex = regex;
			firePropertyChange(PROPERTY_USE_REGEX, !regex, regex);
		}
	}


	/**
	 * Sets the text to replace with, if doing a replace operation.  This
	 * method fires a property change event of type
	 * {@link #PROPERTY_REPLACE_WITH}.
	 *
	 * @param replaceWith The text to replace with.
	 * @see #getReplaceWith()
	 * @see #setSearchFor(String)
	 */
	public void setReplaceWith(String replaceWith) {
		if ((replaceWith==null && this.replaceWith!=null) ||
				(replaceWith!=null && !replaceWith.equals(this.replaceWith))) {
			String old = this.replaceWith;
			this.replaceWith = replaceWith;
			firePropertyChange(PROPERTY_REPLACE_WITH, old, replaceWith);
		}
	}


	/**
	 * Sets the text to search for.  This method fires a property change
	 * event of type {@link #PROPERTY_SEARCH_FOR}.
	 *
	 * @param searchFor The text to search for.
	 * @see #getSearchFor()
	 * @see #setReplaceWith(String)
	 */
	public void setSearchFor(String searchFor) {
		if ((searchFor==null && this.searchFor!=null) ||
				(searchFor!=null && !searchFor.equals(this.searchFor))) {
			String old = this.searchFor;
			this.searchFor = searchFor;
			firePropertyChange(PROPERTY_SEARCH_FOR, old, searchFor);
		}
	}


	/**
	 * Sets whether the search should be forward through the text (vs.
	 * backwards).  This method fires a property change event of type
	 * {@link #PROPERTY_SEARCH_FORWARD}.
	 *
	 * @param forward Whether we should search forwards.
	 * @see #getSearchForward()
	 */
	public void setSearchForward(boolean forward) {
		if (forward!=this.forward) {
			this.forward = forward;
			firePropertyChange(PROPERTY_SEARCH_FORWARD, !forward, forward);
		}
	}


	/**
	 * Sets whether the search should be wrap through the text.
	 * This method fires a property change event of type
	 * {@link #PROPERTY_SEARCH_WRAP}.
	 *
	 * @param wrap Whether we should search wrap
	 * @see #getSearchWrap()
	 */
	public void setSearchWrap(boolean wrap) {
		if (wrap!=this.wrap) {
			this.wrap = wrap;
			firePropertyChange(PROPERTY_SEARCH_WRAP, !wrap, wrap);
		}
	}


	/**
	 * Sets whether only the selected text should be searched.  This method
	 * fires a property change event of type {@link #PROPERTY_SELECTION_ONLY}.
	 * <p>
	 *
	 * This flag is currently not supported.  Calling this method will throw
	 * an {@link UnsupportedOperationException} until it is implemented.
	 *
	 * @param selectionOnly Whether only selected text should be searched.
	 * @see #getSearchSelectionOnly()
	 */
	public void setSearchSelectionOnly(boolean selectionOnly) {
		if (selectionOnly!=this.selectionOnly) {
			this.selectionOnly = selectionOnly;
			firePropertyChange(PROPERTY_SELECTION_ONLY,
					!selectionOnly, selectionOnly);
			if (selectionOnly) {
				throw new UnsupportedOperationException(
						"Searching in selection is not currently supported");
			}
		}
	}


	/**
	 * Sets whether only "whole word" matches should be returned.  A match
	 * is considered to be "whole word" if the character on either side of the
	 * matched text is a non-word character, or if there is no character on
	 * one side of the word, such as when it's at the beginning or end of a
	 * line.This method fires a property change event of type
	 * {@link #PROPERTY_MATCH_WHOLE_WORD}.
	 *
	 * @param wholeWord Whether only "whole word" matches should be returned.
	 * @see #getWholeWord()
	 */
	public void setWholeWord(boolean wholeWord) {
		if (wholeWord!=this.wholeWord) {
			this.wholeWord = wholeWord;
			firePropertyChange(PROPERTY_MATCH_WHOLE_WORD, !wholeWord,wholeWord);
		}
	}


	@Override
	public String toString() {
		return "[SearchContext: " +
			"searchFor=" + getSearchFor() +
			", replaceWith=" + getReplaceWith() +
			", matchCase=" + getMatchCase() +
			", wholeWord=" + getWholeWord() +
			", regex=" + isRegularExpression() +
			", markAll=" + getMarkAll() +
			"]";
	}


}
