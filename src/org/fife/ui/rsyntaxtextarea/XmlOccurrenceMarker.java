/*
 * 03/09/2013
 *
 * XmlOccurrenceMarker - Marks occurrences of the current token for XML.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.util.Stack;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import org.fife.ui.rtextarea.SmartHighlightPainter;


/**
 * Marks occurrences of the current token for XML.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class XmlOccurrenceMarker implements OccurrenceMarker {

	private static final char[] CLOSE_TAG_START = { '<', '/' };
	private static final char[] TAG_SELF_CLOSE = { '/', '>' };


	/**
	 * {@inheritDoc}
	 */
	public void markOccurrences(RSyntaxDocument doc, Token t,
			RSyntaxTextAreaHighlighter h, SmartHighlightPainter p) {

		char[] lexeme = t.getLexeme().toCharArray();
		int tokenOffs = t.getOffset();
		Element root = doc.getDefaultRootElement();
		int lineCount = root.getElementCount();
		int curLine = root.getElementIndex(t.getOffset());

		// For now, we only check for tags on the current line, for
		// simplicity.  Tags spanning multiple lines aren't common anyway.
		boolean found = false;
		boolean forward = true;
		t = doc.getTokenListForLine(curLine);
		while (t!=null && t.isPaintable()) {
			if (t.getType()==Token.MARKUP_TAG_DELIMITER) {
				if (t.isSingleChar('<') && t.getOffset()+1==tokenOffs) {
					found = true;
					break;
				}
				else if (t.is(CLOSE_TAG_START) && t.getOffset()+2==tokenOffs) {
					found = true;
					forward = false;
					break;
				}
			}
			t = t.getNextToken();
		}

		if (!found) {
			return;
		}

		if (forward) {

			int depth = 0;
			t = t.getNextToken().getNextToken();

			do {

				while (t!=null && t.isPaintable()) {
					if (t.getType()==Token.MARKUP_TAG_DELIMITER) {
						if (t.isSingleChar('<')) {
							depth++;
						}
						else if (t.is(TAG_SELF_CLOSE)) {
							if (depth>0) {
								depth--;
							}
							else {
								return; // No match for this tag
							}
						}
						else if (t.is(CLOSE_TAG_START)) {
							if (depth>0) {
								depth--;
							}
							else {
								Token match = t.getNextToken();
								if (match!=null && match.is(lexeme)) {
									try {
										int end = match.getOffset() + match.length();
										h.addMarkedOccurrenceHighlight(match.getOffset(), end, p);
										end = tokenOffs + match.length();
										h.addMarkedOccurrenceHighlight(tokenOffs, end, p);
									} catch (BadLocationException ble) {
										ble.printStackTrace(); // Never happens
									}
								}
								return; // We're done!
							}
						}
					}
					t = t.getNextToken();
				}

				if (++curLine<lineCount) {
					t = doc.getTokenListForLine(curLine);
				}

			} while (curLine<lineCount);
				

		}

		else { // !forward

			Stack<Token> matches = new Stack<Token>();
			boolean inPossibleMatch = false;
			t = doc.getTokenListForLine(curLine);
			final int endBefore = tokenOffs - 2; // Stop before "</".

			do {

				while (t!=null && t.getOffset()<endBefore && t.isPaintable()) {
					if (t.getType()==Token.MARKUP_TAG_DELIMITER) {
						if (t.isSingleChar('<')) {
							Token next = t.getNextToken();
							if (next!=null) {
								if (next.is(lexeme)) {
									matches.push(next);
									inPossibleMatch = true;
								}
								else {
									inPossibleMatch = false;
								}
								t = next;
							}
						}
						else if (t.isSingleChar('>')) {
							inPossibleMatch = false;
						}
						else if (inPossibleMatch && t.is(TAG_SELF_CLOSE)) {
							matches.pop();
						}
						else if (t.is(CLOSE_TAG_START)) {
							Token next = t.getNextToken();
							if (next!=null) {
								// Invalid XML might not have a match
								if (next.is(lexeme) && !matches.isEmpty()) {
									matches.pop();
								}
								t = next;
							}
						}
					}
					t = t.getNextToken();
				}

				if (!matches.isEmpty()) {
					try {
						Token match = matches.pop();
						int end = match.getOffset() + match.length();
						h.addMarkedOccurrenceHighlight(match.getOffset(), end, p);
						end = tokenOffs + match.length();
						h.addMarkedOccurrenceHighlight(tokenOffs, end, p);
					} catch (BadLocationException ble) {
						ble.printStackTrace(); // Never happens
					}
					return;
				}

				if (--curLine>=0) {
					t = doc.getTokenListForLine(curLine);
				}

			} while (curLine>=0);
				

		}

	}


}