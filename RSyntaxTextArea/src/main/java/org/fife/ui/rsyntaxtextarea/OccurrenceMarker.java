/*
 * 03/09/2013
 *
 * OccurrenceMarker - Marks occurrences of the current token.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rtextarea.SmartHighlightPainter;


/**
 * An <code>OccurrenceMarker</code> is called when the caret stops moving after
 * a short period.  If the current {@link TokenMaker} returns an instance of
 * this class, it is told to mark all occurrences of the identifier at the
 * caret position.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface OccurrenceMarker {


	/**
	 * Returns the token to mark occurrences, of, provided it matches the
	 * criteria put forth by {@link #isValidType(RSyntaxTextArea, Token)}.
	 * For most languages, this method should return the token at the caret
	 * position.
	 *
	 * @param textArea The text area.
	 * @return The token to (possibly) mark occurrences of, or
	 *         <code>null</code> if none.
	 */
	Token getTokenToMark(RSyntaxTextArea textArea);


	/**
	 * Returns whether the specified token is a type that we can do a
	 * "mark occurrences" of.  Typically, this will delegate to
	 * {@link RSyntaxTextArea#getMarkOccurrencesOfTokenType(int)}.
	 *
	 * @param textArea The text area.
	 * @param t The token.
	 * @return Whether we should mark all occurrences of this token.
	 */
	boolean isValidType(RSyntaxTextArea textArea, Token t);


	/**
	 * Called when occurrences of a token should be marked.
	 *
	 * @param doc The document.
	 * @param t The document whose relevant occurrences should be marked.
	 * @param h The highlighter to add the highlights to.
	 * @param p The painter for the highlights.
	 */
	void markOccurrences(RSyntaxDocument doc, Token t,
			RSyntaxTextAreaHighlighter h, SmartHighlightPainter p);


}
