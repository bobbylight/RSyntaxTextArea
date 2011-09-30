package org.fife.ui.rsyntaxtextarea.folding;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;


/**
 * A basic fold parser that can be used for languages such as C, that use
 * curly braces to denote code blocks.  This parser searches for curly brace
 * pairs and creates code folds out of them.  It can also optionally find
 * C-style multi-line comments ("<code>/* ... *&#47;</code>") and make them
 * foldable as well.<p>
 * 
 * This parser knows nothing about language semantics; it uses
 * <code>RSyntaxTextArea</code>'s syntax highlighting tokens to identify
 * curly braces.  By default, it looks for single-char tokens of type
 * {@link Token#SEPARATOR}, with lexemes '<code>{</code>' or '<code>}</code>'.
 * If your {@link TokenMaker} uses a different token type for curly braces, you
 * should override the {@link #isLeftCurly(Token)} and
 * {@link #isRightCurly(Token)} methods with your own definitions.  In theory,
 * you could extend this fold parser to parse languages that use completely
 * different tokens than curly braces to denote foldable regions by overriding
 * those two methods.<p>
 *
 * Note also that this class may impose somewhat of a performance penalty on
 * large source files, since it re-parses the entire document each time folds
 * are reevaluated.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class CurlyFoldParser implements FoldParser {

	/**
	 * Whether to scan for C-style multi-line comments and make them foldable.
	 */
	private boolean foldableMultiLineComments;

	/**
	 * Ending of a multi-line comment in C, C++, Java, etc.
	 */
	private static final char[] C_MLC_END = { '*', '/' };


	/**
	 * Constructor.
	 *
	 * @param cStyleMultiLineComments Whether to scan for C-style multi-line
	 *        comments and make them foldable.
	 */
	public CurlyFoldParser(boolean cStyleMultiLineComments) {
		this.foldableMultiLineComments = cStyleMultiLineComments;
	}


	/**
	 * {@inheritDoc}
	 */
	public List getFolds(RSyntaxTextArea textArea) {

		List folds = new ArrayList();

		RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
		if (doc.getCurlyBracesDenoteCodeBlocks()) {

			Fold currentFold = null;

			try {

				int lineCount = textArea.getLineCount();
				boolean inMLC = false;
				int mlcStart = 0;

				for (int line=0; line<lineCount; line++) {

					Token t = textArea.getTokenListForLine(line);
					while (t!=null && t.isPaintable()) {

						if (foldableMultiLineComments && t.isComment()) {
							if (inMLC) {
								// If we found the end of an MLC that started
								// on a previous line...
								if (t.endsWith(C_MLC_END)) {
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
									//System.out.println("Ending MLC at: " + mlcEnd + ", parent==" + currentFold);
									inMLC = false;
									mlcStart = 0;
								}
								// Otherwise, this MLC is continuing on to yet
								// another line.
							}
							else {
								// If we're an MLC that ends on a later line...
								if (t.type!=Token.COMMENT_EOL && !t.endsWith(C_MLC_END)) {
									//System.out.println("Starting MLC at: " + t.offset);
									inMLC = true;
									mlcStart = t.offset;
								}
							}
						}

						else if (isLeftCurly(t)) {

							if (currentFold==null) {
								currentFold = new Fold(FoldType.CODE, textArea, t.offset);
								folds.add(currentFold);
							}
							else {
								currentFold = currentFold.createChild(FoldType.CODE, t.offset);
							}

						}

						else if (isRightCurly(t)) {

							if (currentFold!=null) {
								currentFold.setEndOffset(t.offset);
								Fold parentFold = currentFold.getParent();
								//System.out.println("... Adding regular fold at " + t.offset + ", parent==" + parentFold);
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

		}

		return folds;

	}


	/**
	 * Returns whether the token is a left curly brace.  This method exists
	 * so subclasses can provide their own curly brace definition.
	 *
	 * @param t The token.
	 * @return Whether it is a left curly brace.
	 * @see #isRightCurly(Token)
	 */
	public boolean isLeftCurly(Token t) {
		return t.isLeftCurly();
	}


	/**
	 * Returns whether the token is a right curly brace.  This method exists
	 * so subclasses can provide their own curly brace definition.
	 *
	 * @param t The token.
	 * @return Whether it is a right curly brace.
	 * @see #isLeftCurly(Token)
	 */
	public boolean isRightCurly(Token t) {
		return t.isRightCurly();
	}


}