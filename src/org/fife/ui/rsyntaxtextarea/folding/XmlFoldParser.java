/*
 * 10/23/2011
 *
 * XmlFoldParser.java - Fold parser for XML.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;


/**
 * Fold parser for XML.  Any tags that span more than one line, as well as
 * comment regions spanning more than one line, are identified as foldable
 * regions.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class XmlFoldParser implements FoldParser {

	private static final char[] MARKUP_CLOSING_TAG_START = { '<', '/' };
	private static final char[] MARKUP_SHORT_TAG_END = { '/', '>' };
	private static final char[] MLC_END = { '-', '-', '>' };


	/**
	 * {@inheritDoc}
	 */
	public List getFolds(RSyntaxTextArea textArea) {

		List folds = new ArrayList();

		Fold currentFold = null;
		int lineCount = textArea.getLineCount();
		boolean inMLC = false;
		int mlcStart = 0;

		try {

			for (int line=0; line<lineCount; line++) {

				Token t = textArea.getTokenListForLine(line);
				while (t!=null && t.isPaintable()) {

					if (t.isComment()) {

						// Continuing an MLC from a previous line
						if (inMLC) {
							// Found the end of the MLC starting on a previous line...
							if (t.endsWith(MLC_END)) {
								int mlcEnd = t.offset + t.textCount - 1;
								if (currentFold==null) {
									currentFold = new Fold(FoldType.COMMENT, textArea, mlcStart);
									currentFold.setEndOffset(mlcEnd);
									folds.add(currentFold);
									currentFold = null;
								}
								else {
									currentFold = currentFold.createChild(FoldType.COMMENT, mlcStart);
									currentFold.setEndOffset(mlcEnd);
									currentFold = currentFold.getParent();
								}
								inMLC = false;
								mlcStart = 0;
							}
							// Otherwise, this MLC is continuing on to yet
							// another line.
						}

						else {
							// If we're an MLC that ends on a later line...
							if (t.type==Token.COMMENT_MULTILINE && !t.endsWith(MLC_END)) {
								inMLC = true;
								mlcStart = t.offset;
							}
						}

					}

					else if (t.type==Token.MARKUP_TAG_DELIMITER && t.isSingleChar('<')) {
						if (currentFold==null) {
							currentFold = new Fold(FoldType.CODE, textArea, t.offset);
							folds.add(currentFold);
						}
						else {
							currentFold = currentFold.createChild(FoldType.CODE, t.offset);
						}
					}

					else if (t.is(Token.MARKUP_TAG_DELIMITER, MARKUP_SHORT_TAG_END)) {
						if (currentFold!=null) {
							Fold parentFold = currentFold.getParent();
							currentFold.removeFromParent();
							currentFold = parentFold;
						}
					}

					else if (t.is(Token.MARKUP_TAG_DELIMITER, MARKUP_CLOSING_TAG_START)) {
						if (currentFold!=null) {
							currentFold.setEndOffset(t.offset);
							Fold parentFold = currentFold.getParent();
							// Don't add fold markers for single-line blocks
							if (currentFold.isOnSingleLine()) {
								currentFold.removeFromParent();
							}
							currentFold = parentFold;
						}
					}

					t = t.getNextToken();

				}

			}

		} catch (BadLocationException ble) { // Should never happen
			ble.printStackTrace();
		}

		return folds;
	
	}


}