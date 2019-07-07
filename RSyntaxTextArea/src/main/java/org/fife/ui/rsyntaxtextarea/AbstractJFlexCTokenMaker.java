/*
 * 01/25/2009
 *
 * AbstractJFlexCTokenMaker.java - Base class for token makers that use curly
 * braces to denote code blocks, such as C, C++, Java, Perl, etc.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Action;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;

import org.fife.ui.rtextarea.RTextArea;


/**
 * Base class for JFlex-based token makers using C-style syntax.  This class
 * knows how to:
 *
 * <ul>
 *    <li>Auto-indent after opening braces and parens
 *    <li>Automatically close multi-line and documentation comments
 * </ul>
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractJFlexCTokenMaker extends AbstractJFlexTokenMaker {

	private final Action INSERT_BREAK_ACTION;

	private static final Pattern MLC_PATTERN =
			Pattern.compile("([ \\t]*)(/?[\\*]+)([ \\t]*)");


	protected AbstractJFlexCTokenMaker() {
		INSERT_BREAK_ACTION = createInsertBreakAction();
	}


	/**
	 * Creates and returns the action to use when the user inserts a newline.
	 * The default implementation intelligently closes multi-line comments.
	 * Subclasses can override.
	 *
	 * @return The action.
	 * @see #getInsertBreakAction()
	 */
	protected Action createInsertBreakAction() {
		return new CStyleInsertBreakAction();
	}


	/**
	 * Returns <code>true</code> always as C-style languages use curly braces
	 * to denote code blocks.
	 *
	 * @return <code>true</code> always.
	 */
	@Override
	public boolean getCurlyBracesDenoteCodeBlocks(int languageIndex) {
		return true;
	}


	/**
	 * Returns an action to handle "insert break" key presses (i.e. Enter).
	 * An action is returned that handles newlines differently in multi-line
	 * comments.
	 *
	 * @return The action.
	 */
	@Override
	public Action getInsertBreakAction() {
		return INSERT_BREAK_ACTION;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getMarkOccurrencesOfTokenType(int type) {
		return type==Token.IDENTIFIER || type==Token.FUNCTION;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getShouldIndentNextLineAfter(Token t) {
		if (t!=null && t.length()==1) {
			char ch = t.charAt(0);
			return ch=='{' || ch=='(';
		}
		return false;
	}


	/**
	 * Returns whether a given token is an internal token type that represents
	 * an MLC or documentation comment continuing on to the next line.  This is
	 * done by languages such as JavaScript that are a little more verbose
	 * than necessary so that their code can be copy-and-pasted into other
	 * <code>TokenMaker</code>s that use them as nested languages (such as
	 * HTML, JSP, etc.).
	 *
	 * @param t The token to check.  This cannot be <code>null</code>.
	 * @return Whether the token is an internal token representing the end of
	 *         a line for an MLC/doc comment continuing on to the next line.
	 */
	private boolean isInternalEolTokenForMLCs(Token t) {
		int type = t.getType();
		if (type<0) {
			type = getClosestStandardTokenTypeForInternalType(type);
			return type==TokenTypes.COMMENT_MULTILINE ||
					type==TokenTypes.COMMENT_DOCUMENTATION;
		}
		return false;
	}


	/**
	 * Action that knows how to special-case inserting a newline in a
	 * multi-line comment for languages like C and Java.
	 */
	protected class CStyleInsertBreakAction extends
							RSyntaxTextAreaEditorKit.InsertBreakAction {

		@Override
		public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {

			if (!textArea.isEditable() || !textArea.isEnabled()) {
				UIManager.getLookAndFeel().provideErrorFeedback(textArea);
				return;
			}

			RSyntaxTextArea rsta = (RSyntaxTextArea)getTextComponent(e);
			RSyntaxDocument doc = (RSyntaxDocument)rsta.getDocument();

			int line = textArea.getCaretLineNumber();
			int type = doc.getLastTokenTypeOnLine(line);
			if (type<0) {
				type = doc.getClosestStandardTokenTypeForInternalType(type);
			}

			// Only in MLC's should we try this
			if (type==Token.COMMENT_DOCUMENTATION ||
					type==Token.COMMENT_MULTILINE) {
				insertBreakInMLC(e, rsta, line);
			}
			else {
				handleInsertBreak(rsta, true);
			}

		}


		/**
		 * Returns whether the MLC token containing <code>offs</code> appears
		 * to have a "nested" comment (i.e., contains "<code>/*</code>"
		 * somewhere inside of it).  This implies that it is likely a "new" MLC
		 * and needs to be closed.  While not foolproof, this is usually good
		 * enough of a sign.
		 *
		 * @param textArea
		 * @param line
		 * @param offs
		 * @return Whether a comment appears to be nested inside this one.
		 */
		private boolean appearsNested(RSyntaxTextArea textArea,
						int line, int offs) {

			final int firstLine = line; // Remember the line we start at.

			while (line<textArea.getLineCount()) {
				Token t = textArea.getTokenListForLine(line);
				int i = 0;
				// If examining the first line, start at offs.
				if (line++==firstLine) {
					t = RSyntaxUtilities.getTokenAtOffset(t, offs);
					if (t==null) { // offs was at end of the line
						continue;
					}
					i = t.documentToToken(offs);
				}
				else {
					i = t.getTextOffset();
				}
				int textOffset = t.getTextOffset();
				while (i<textOffset+t.length()-1) {
					if (t.charAt(i-textOffset)=='/' && t.charAt(i-textOffset+1)=='*') {
						return true;
					}
					i++;
				}
				// If tokens come after this one on this line, our MLC ended.
				if ((t=t.getNextToken())!=null && !isInternalEolTokenForMLCs(t)) {
					return false;
				}
			}

			return true; // No match - MLC goes to the end of the file

		}

		private void insertBreakInMLC(ActionEvent e, RSyntaxTextArea textArea,
										int line) {

			Matcher m = null;
			int start = -1;
			int end = -1;
			String text = null;
			try {
				start = textArea.getLineStartOffset(line);
				end = textArea.getLineEndOffset(line);
				text = textArea.getText(start, end-start);
				m = MLC_PATTERN.matcher(text);
			} catch (BadLocationException ble) { // Never happens
				UIManager.getLookAndFeel().provideErrorFeedback(textArea);
				ble.printStackTrace();
				return;
			}

			if (m.lookingAt()) {

				String leadingWS = m.group(1);
				String mlcMarker = m.group(2);

				// If the caret is "inside" any leading whitespace or MLC
				// marker, move it to the end of the line.
				int dot = textArea.getCaretPosition();
				if (dot>=start &&
						dot<start+leadingWS.length()+mlcMarker.length()) {
					// If we're in the whitespace before the very start of the
					// MLC though, just insert a normal newline
					if (mlcMarker.charAt(0)=='/') {
						handleInsertBreak(textArea, true);
						return;
					}
					textArea.setCaretPosition(end-1);
				}
				else {
					// Ensure caret is at the "end" of any whitespace
					// immediately after the '*', but before any possible
					// non-whitespace chars.
					boolean moved = false;
					while (dot<end-1 &&
							Character.isWhitespace(text.charAt(dot-start))) {
						moved = true;
						dot++;
					}
					if (moved) {
						textArea.setCaretPosition(dot);
					}
				}

				boolean firstMlcLine = mlcMarker.charAt(0)=='/';
				boolean nested = appearsNested(textArea, line,
												start+leadingWS.length()+2);
				String header = leadingWS +
						(firstMlcLine ? " * " : "*") +
						m.group(3);
				textArea.replaceSelection("\n" + header);
				if (nested) {
					dot = textArea.getCaretPosition(); // Has changed
					textArea.insert("\n" + leadingWS + " */", dot);
					textArea.setCaretPosition(dot);
				}

			}
			else {
				handleInsertBreak(textArea, true);
			}

		}

	}


}
