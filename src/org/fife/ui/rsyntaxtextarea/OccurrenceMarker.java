/*
 * 03/09/2013
 *
 * OccurrenceMarker - Marks occurrences of the current token.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
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
	 * Called when occurrences of a token should be marked.
	 *
	 * @param doc The document.
	 * @param t The document whose relevant occurrences should be marked.
	 * @param h The highlighter to add the highlights to.
	 * @param p The painter for the highlights.
	 */
	public void markOccurrences(RSyntaxDocument doc, Token t,
			RSyntaxTextAreaHighlighter h, SmartHighlightPainter p);


}