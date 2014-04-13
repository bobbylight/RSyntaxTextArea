/*
 * 03/09/2013
 *
 * DefaultOccurrenceMarker - Marks occurrences of the current token for most
 * languages.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

import org.fife.ui.rtextarea.SmartHighlightPainter;


/**
 * The default implementation of {@link OccurrenceMarker}.  It goes through
 * the document and marks all instances of the specified token.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DefaultOccurrenceMarker implements OccurrenceMarker {


	/**
	 * {@inheritDoc}
	 */
	public Token getTokenToMark(RSyntaxTextArea textArea) {

		// Get the token at the caret position.
		int line = textArea.getCaretLineNumber();
		Token tokenList = textArea.getTokenListForLine(line);
		Caret c = textArea.getCaret();
		int dot = c.getDot();

		Token t = RSyntaxUtilities.getTokenAtOffset(tokenList, dot);
		if (t==null /* EOL */ || !isValidType(textArea, t) ||
				RSyntaxUtilities.isNonWordChar(t)) {
			// Try to the "left" of the caret.
			dot--;
			try {
				if (dot>=textArea.getLineStartOffset(line)) {
					t = RSyntaxUtilities.getTokenAtOffset(tokenList, dot);
				}
			} catch (BadLocationException ble) {
				ble.printStackTrace(); // Never happens
			}
		}

		return t;

	}


	/**
	 * {@inheritDoc}
	 */
	public boolean isValidType(RSyntaxTextArea textArea, Token t) {
		return textArea.getMarkOccurrencesOfTokenType(t.getType());
	}


	/**
	 * {@inheritDoc}
	 */
	public void markOccurrences(RSyntaxDocument doc, Token t,
			RSyntaxTextAreaHighlighter h, SmartHighlightPainter p) {

		char[] lexeme = t.getLexeme().toCharArray();
		int type = t.getType();
		int lineCount = doc.getDefaultRootElement().getElementCount();

		for (int i=0; i<lineCount; i++) {
			Token temp = doc.getTokenListForLine(i);
			while (temp!=null && temp.isPaintable()) {
				if (temp.is(type, lexeme)) {
					try {
						int end = temp.getEndOffset();
						h.addMarkedOccurrenceHighlight(temp.getOffset(), end, p);
					} catch (BadLocationException ble) {
						ble.printStackTrace(); // Never happens
					}
				}
				temp = temp.getNextToken();
			}
		}

	}


}