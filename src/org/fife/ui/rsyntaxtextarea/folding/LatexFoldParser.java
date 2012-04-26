/*
 * 04/24/2012
 *
 * LatexFoldParser.java - Fold parser for LaTeX.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;


/**
 * A fold parser for LaTeX documents.  This is likely incomplete and/or not
 * quite right; feedback is appreciated.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class LatexFoldParser implements FoldParser {

	private static final char[] BEGIN = "\\begin{".toCharArray();
	private static final char[] END = "\\end{".toCharArray();


	/**
	 * {@inheritDoc}
	 */
	public List getFolds(RSyntaxTextArea textArea) {

		List folds = new ArrayList();
		Stack expectedStack = new Stack();

		Fold currentFold = null;
		int lineCount = textArea.getLineCount();

		try {

			for (int line=0; line<lineCount; line++) {

				Token t = textArea.getTokenListForLine(line);
				while (t!=null && t.isPaintable()) {

					if (t.type==Token.RESERVED_WORD) { // A \begin{} or \end{}
						if (t.startsWith(BEGIN)) {
							if (currentFold==null) {
								currentFold = new Fold(FoldType.CODE, textArea, t.offset);
								folds.add(currentFold);
							}
							else {
								currentFold = currentFold.createChild(FoldType.CODE, t.offset);
							}
							String expected = t.getLexeme();
							expected = expected.substring(7, expected.length()-1);
							expectedStack.push(expected);
						}
						else if (t.startsWith(END)) {
							if (currentFold!=null && !expectedStack.isEmpty()) {
								String value = t.getLexeme();
								value = value.substring(5, value.length()-1);
								if (expectedStack.peek().equals(value)) {
									expectedStack.pop();
									currentFold.setEndOffset(t.textOffset);
									Fold parentFold = currentFold.getParent();
									// Don't add fold markers for single-line blocks
									if (currentFold.isOnSingleLine()) {
										if (parentFold!=null) {
											currentFold.removeFromParent();
										}
										else {
											folds.remove(folds.size()-1);
										}
									}
									currentFold = parentFold;
								}
							}
						}
					}

					t = t.getNextToken();

				}

			}

		} catch (BadLocationException ble) {
			ble.printStackTrace(); // Never happens
		}

		return folds;

	}


}