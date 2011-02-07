/*
 * 01/25/2009
 *
 * AbstractJFlexCTokenMaker.java - Base class for token makers that use curly
 * braces to denote code blocks, such as C, C++, Java, Perl, etc.
 * Copyright (C) 2009 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
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
 * knows how to auto-indent after opening braces and parens.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractJFlexCTokenMaker extends AbstractJFlexTokenMaker {

	protected static final Action INSERT_BREAK_ACTION = new InsertBreakAction();


	/**
	 * Returns <code>true</code> always as C-style languages use curly braces
	 * to denote code blocks.
	 *
	 * @return <code>true</code> always.
	 */
	public boolean getCurlyBracesDenoteCodeBlocks() {
		return true;
	}


	/**
	 * Returns an action to handle "insert break" key presses (i.e. Enter).
	 * An action is returned that handles newlines differently in multi-line
	 * comments.
	 *
	 * @return The action.
	 */
	public Action getInsertBreakAction() {
		return INSERT_BREAK_ACTION;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean getShouldIndentNextLineAfter(Token t) {
		if (t!=null && t.textCount==1) {
			char ch = t.text[t.textOffset];
			return ch=='{' || ch=='(';
		}
		return false;
	}


	/**
	 * Action that knows how to special-case inserting a newline in a
	 * multi-line comment for languages like C and Java.
	 */
	private static class InsertBreakAction extends
							RSyntaxTextAreaEditorKit.InsertBreakAction {

		private static final Pattern p =
							Pattern.compile("([ \\t]*)(/?[\\*]+)([ \\t]*)");

		public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {

			if (!textArea.isEditable() || !textArea.isEnabled()) {
				UIManager.getLookAndFeel().provideErrorFeedback(textArea);
				return;
			}

			RSyntaxTextArea rsta = (RSyntaxTextArea)getTextComponent(e);
			RSyntaxDocument doc = (RSyntaxDocument)rsta.getDocument();

			int line = textArea.getCaretLineNumber();
			int type = doc.getLastTokenTypeOnLine(line);

			// Only in MLC's should we try this
			if (type==Token.COMMENT_DOCUMENTATION ||
					type==Token.COMMENT_MULTILINE) {
				insertBreakInMLC(e, rsta, line);
			}
			else {
				handleInsertBreak(rsta, true);
			}

		}

		private void insertBreakInMLC(ActionEvent e, RSyntaxTextArea textArea,
										int line) {

			Matcher m = null;
			int start = -1;
			int end = -1;
			try {
				start = textArea.getLineStartOffset(line);
				end = textArea.getLineEndOffset(line);
				String text = textArea.getText(start, end-start);
				m = p.matcher(text);
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
					textArea.setCaretPosition(end-1);
				}

				boolean newMLC = mlcMarker.charAt(0)=='/';
				String header = leadingWS +
						(newMLC ? " * " : "*") +
						m.group(3);
				textArea.replaceSelection("\n" + header);
				if (newMLC) {
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