/*
 * 12/23/2012
 *
 * JsonFoldParser.java - Fold parser for JSON.
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
import org.fife.ui.rsyntaxtextarea.TokenTypes;


/**
 * The fold parser for JSON.  Objects (<code>"{ ... }</code>") and arrays
 * (<code>"[ ... ]"</code>) that span multiple lines are considered fold
 * regions.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class JsonFoldParser implements FoldParser {

	private static final Object OBJECT_BLOCK = new Object();
	private static final Object ARRAY_BLOCK = new Object();


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Fold> getFolds(RSyntaxTextArea textArea) {

		Stack<Object> blocks = new Stack<Object>();
		List<Fold> folds = new ArrayList<Fold>();

		Fold currentFold = null;
		int lineCount = textArea.getLineCount();

		try {

			for (int line=0; line<lineCount; line++) {

				Token t = textArea.getTokenListForLine(line);
				while (t!=null && t.isPaintable()) {

					if (t.isLeftCurly()) {
						if (currentFold==null) {
							currentFold = new Fold(FoldType.CODE, textArea, t.getOffset());
							folds.add(currentFold);
						}
						else {
							currentFold = currentFold.createChild(FoldType.CODE, t.getOffset());
						}
						blocks.push(OBJECT_BLOCK);
					}

					else if (t.isRightCurly() && popOffTop(blocks, OBJECT_BLOCK)) {
						if (currentFold!=null) {
							currentFold.setEndOffset(t.getOffset());
							Fold parentFold = currentFold.getParent();
							//System.out.println("... Adding regular fold at " + t.offset + ", parent==" + parentFold);
							// Don't add fold markers for single-line blocks
							if (currentFold.isOnSingleLine()) {
								if (!currentFold.removeFromParent()) {
									folds.remove(folds.size()-1);
								}
							}
							currentFold = parentFold;
						}
					}

					else if (isLeftBracket(t)) {
						if (currentFold==null) {
							currentFold = new Fold(FoldType.CODE, textArea, t.getOffset());
							folds.add(currentFold);
						}
						else {
							currentFold = currentFold.createChild(FoldType.CODE, t.getOffset());
						}
						blocks.push(ARRAY_BLOCK);
					}

					else if (isRightBracket(t) && popOffTop(blocks, ARRAY_BLOCK)) {
						if (currentFold!=null) {
							currentFold.setEndOffset(t.getOffset());
							Fold parentFold = currentFold.getParent();
							//System.out.println("... Adding regular fold at " + t.offset + ", parent==" + parentFold);
							// Don't add fold markers for single-line blocks
							if (currentFold.isOnSingleLine()) {
								if (!currentFold.removeFromParent()) {
									folds.remove(folds.size()-1);
								}
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


	/**
	 * Returns whether a token is the left bracket token.
	 *
	 * @param t The token.
	 * @return Whether the token is the left bracket token.
	 * @see #isRightBracket(Token)
	 */
	private static boolean isLeftBracket(Token t) {
		return t.getType()==TokenTypes.SEPARATOR && t.isSingleChar('[');
	}


	/**
	 * Returns whether a token is the right bracket token.
	 *
	 * @param t The token.
	 * @return Whether the token is the right bracket token.
	 * @see #isLeftBracket(Token)
	 */
	private static boolean isRightBracket(Token t) {
		return t.getType()==TokenTypes.SEPARATOR && t.isSingleChar(']');
	}


	/**
	 * If the specified value is on top of the stack, pop it off and return
	 * <code>true</code>.  Otherwise, return <code>false</code>.
	 *
	 * @param stack The stack.
	 * @param value The value to check for.
	 * @return Whether the value was found on top of the stack.
	 */
	private static boolean popOffTop(Stack<Object> stack, Object value) {
		if (stack.size()>0 && stack.peek()==value) {
			stack.pop();
			return true;
		}
		return false;
	}


}