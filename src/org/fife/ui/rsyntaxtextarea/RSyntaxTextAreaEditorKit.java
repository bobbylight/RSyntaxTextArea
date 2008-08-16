/*
 * 08/29/2004
 *
 * RSyntaxTextAreaEditorKit.java - The editor kit used by RSyntaxTextArea.
 * Copyright (C) 2004 Robert Futrell
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

import org.fife.ui.rtextarea.RecordableTextAction;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextAreaEditorKit;


/**
 * An extension of <code>RTextAreaEditorKit</code> that adds functionality for
 * programming-specific stuff.  There are currently subclasses to handle:
 *
 * <ul>
 *   <li>Copying the current selection as RTF.</li>
 *   <li>Block indentation (increasing the indent of one or multiple lines)</li>
 *   <li>Block un-indentation (decreasing the indent of one or multiple lines)
 *       </li>
 *   <li>Inserting a "code template" when a configurable key (e.g. a space) is
 *       pressed</li>
 *   <li>Decreasing the point size of all fonts in the text area</li>
 *   <li>Increasing the point size of all fonts in the text area</li>
 *   <li>Moving the caret to the "matching bracket" of the one at the current
 *       caret position</li>
 *   <li>Toggling whether the currently selected lines are commented out.</li>
 * </ul>
 *
 * @author Robert Futrell
 * @version 0.3
 */
public class RSyntaxTextAreaEditorKit extends RTextAreaEditorKit {

	private static final long serialVersionUID = 1L;

	public static final String rstaCopyAsRtfAction			= "RSTA.CopyAsRtfAction";
	public static final String rstaDecreaseIndentAction		= "RSTA.DecreaseIndentAction";
	public static final String rstaGoToMatchingBracketAction	= "RSTA.GoToMatchingBracketAction";
	public static final String rstaPossiblyInsertTemplateAction = "RSTA.TemplateAction";
	public static final String rstaToggleCommentAction 		= "RSTA.ToggleCommentAction";


	/**
	 * The actions that <code>RSyntaxTextAreaEditorKit</code> adds to those of
	 * <code>RTextAreaEditorKit</code>.
	 */
	private static final Action[] defaultActions = {
		new CopyAsRtfAction(),
		//new DecreaseFontSizeAction(),
		new DecreaseIndentAction(),
		//new GoToMatchingBracketAction(),
		new InsertBreakAction(),
		//new IncreaseFontSizeAction(),
		new InsertTabAction(),
		new PossiblyInsertTemplateAction(),
		//new ToggleCommentAction(),
	};


	/**
	 * Constructor.
	 */
	public RSyntaxTextAreaEditorKit() {
		super();
	}


	/**
	 * Returns the default document used by <code>RSyntaxTextArea</code>s.
	 *
	 * @return The document.
	 */
	public Document createDefaultDocument() {
		return new RSyntaxDocument(SyntaxConstants.NO_SYNTAX_STYLE);
	}


	/**
	 * Fetches the set of commands that can be used
	 * on a text component that is using a model and
	 * view produced by this kit.
	 *
	 * @return the command list
	 */ 
	public Action[] getActions() {
		return TextAction.augmentList(super.getActions(),
							RSyntaxTextAreaEditorKit.defaultActions);
	}


	/**
	 * Action for copying text as RTF.
	 */
	public static class CopyAsRtfAction extends RecordableTextAction {

 
		public CopyAsRtfAction() {
			super(rstaCopyAsRtfAction);
		}

		public CopyAsRtfAction(String name, Icon icon, String desc,
					Integer mnemonic, KeyStroke accelerator) {
			super(name, icon, desc, mnemonic, accelerator);
		}

		public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
			((RSyntaxTextArea)textArea).copyAsRtf();
			textArea.requestFocusInWindow();
		}

		public final String getMacroID() {
			return getName();
		}

	}


	/**
	 * Action for decreasing the font size of all fonts in the text area.
	 */
	public static class DecreaseFontSizeAction
					extends RTextAreaEditorKit.DecreaseFontSizeAction {

		private static final long serialVersionUID = 1L;

		public DecreaseFontSizeAction() {
			super();
		}

		public DecreaseFontSizeAction(String name, Icon icon, String desc,
							Integer mnemonic, KeyStroke accelerator) {
			super(name, icon, desc, mnemonic, accelerator);
		}

		public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {

			RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
			SyntaxHighlightingColorScheme scheme = rsta.
									getSyntaxHighlightingColorScheme();

			// All we need to do is update all of the fonts in syntax
			// schemes, then call setSyntaxHighlightingColorScheme with the
			// same scheme already being used.  This relies on the fact that
			// that method does not check whether the new scheme is different
			// from the old scheme before updating.

			boolean changed = false;
			int count = scheme.syntaxSchemes.length;
			for (int i=0; i<count; i++) {
				SyntaxScheme ss = scheme.syntaxSchemes[i];
				if (ss!=null) {
					Font font = ss.font;
					if (font!=null) {
						float oldSize = font.getSize2D();
						float newSize = oldSize - decreaseAmount;
						if (newSize>=MINIMUM_SIZE) {
							// Shrink by decreaseAmount.
							ss.font = font.deriveFont(newSize);
							changed = true;
						}
						else if (oldSize>MINIMUM_SIZE) {
							// Can't shrink by full decreaseAmount, but
							// can shrink a little bit.
							ss.font = font.deriveFont(MINIMUM_SIZE);
							changed = true;
						}
					}
				}
			}

			// Do the text area's font also.
			Font font = rsta.getFont();
			float oldSize = font.getSize2D();
			float newSize = oldSize - decreaseAmount;
			if (newSize>=MINIMUM_SIZE) {
				// Shrink by decreaseAmount.
				rsta.setFont(font.deriveFont(newSize));
				changed = true;
			}
			else if (oldSize>MINIMUM_SIZE) {
				// Can't shrink by full decreaseAmount, but
				// can shrink a little bit.
				rsta.setFont(font.deriveFont(MINIMUM_SIZE));
				changed = true;
			}

			// If we updated at least one font, update the screen.  If
			// all of the fonts were already the minimum size, beep.
			if (changed) {
				rsta.setSyntaxHighlightingColorScheme(scheme);
				// NOTE:  This is a hack to get an encompassing
				// RTextScrollPane to repaint its line numbers to account
				// for a change in line height due to a font change.  I'm
				// not sure why we need to do this here but not when we
				// change the syntax highlighting color scheme via the
				// Options dialog... setSyntaxHighlightingColorScheme()
				// calls revalidate() which won't repaint the scroll pane
				// if scrollbars don't change, which is why we need this.
				Component parent = rsta.getParent();
				if (parent instanceof javax.swing.JViewport) {
					parent = parent.getParent();
					if (parent instanceof JScrollPane) {
						parent.repaint();
					}
				}
			}
			else
				UIManager.getLookAndFeel().provideErrorFeedback(rsta);

		}

	}


	/**
	 * Action for when un-indenting lines (either the current line if there is
	 * selection, or all selected lines if there is one).
	 */
	public static class DecreaseIndentAction extends RecordableTextAction {

		private static final long serialVersionUID = 1L;
	
		private Segment s;

		public DecreaseIndentAction() {
			this(rstaDecreaseIndentAction);
		}

		public DecreaseIndentAction(String name) {
			super(name);
			s = new Segment();
		}

		public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {

			if (!textArea.isEditable() || !textArea.isEnabled()) {
				UIManager.getLookAndFeel().provideErrorFeedback(textArea);
				return;
			}

			Document document = textArea.getDocument();
			Element map = document.getDefaultRootElement();
			Caret c = textArea.getCaret();
			int dot = c.getDot();
			int mark = c.getMark();
			int line1 = map.getElementIndex(dot);
			int tabSize = textArea.getTabSize();

			// If there is a selection, indent all lines in the selection.
			// Otherwise, indent the line the caret is on.
			if (dot!=mark) {
				// Note that we cheaply reuse variables here, so don't
				// take their names to mean what they are.
				int line2 = map.getElementIndex(mark);
				dot = Math.min(line1, line2);
				mark = Math.max(line1, line2);
				Element elem;
				try {
					for (line1=dot; line1<mark; line1++) {
						elem = map.getElement(line1);
						handleDecreaseIndent(elem, document, tabSize);
					}
					// Don't do the last line if the caret is at its
					// beginning.  We must call getDot() again and not just
					// use 'dot' as the caret's position may have changed
					// due to the insertion of the tabs above.
					elem = map.getElement(mark);
					int start = elem.getStartOffset();
					if (Math.max(c.getDot(),c.getMark())!=start) {
						handleDecreaseIndent(elem, document, tabSize);
					}
				} catch (BadLocationException ble) {
					ble.printStackTrace();
					UIManager.getLookAndFeel().
								provideErrorFeedback(textArea);
				}
			}
			else {
				Element elem = map.getElement(line1);
				try {
					handleDecreaseIndent(elem, document, tabSize);
				} catch (BadLocationException ble) {
					ble.printStackTrace();
					UIManager.getLookAndFeel().
								provideErrorFeedback(textArea);
				}
			}

		}

		public final String getMacroID() {
			return rstaDecreaseIndentAction;
		}

		/**
		 * Actually does the "de-indentation."  This method finds where the
		 * given element's leading whitespace ends, then, if there is indeed
		 * leading whitespace, removes either the last char in it (if it is a
		 * tab), or removes up to the number of spaces equal to a tab in the
		 * specified document (i.e., if the tab size was 5 and there were 3
		 * spaces at the end of the leading whitespace, the three will be
		 * removed; if there were 8 spaces, only the first 5 would be
		 * removed).
		 *
		 * @param elem The element to "de-indent."
		 * @param doc The document containing the specified element.
		 * @param tabSize The size of a tab, in spaces.
		 */
		private final void handleDecreaseIndent(Element elem, Document doc,
									int tabSize)
									throws BadLocationException {
			int start = elem.getStartOffset();
			int end = elem.getEndOffset() - 1; // Why always true??
			doc.getText(start,end-start, s);
			int i = s.offset;
			end = i+s.count;
			if (end>i) {
				// If the first character is a tab, remove it.
				if (s.array[i]=='\t') {
					doc.remove(start, 1);
				}
				// Otherwise, see if the first character is a space.  If it
				// is, remove all contiguous whitespaces at the beginning of
				// this line, up to the tab size.
				else if (s.array[i]==' ') {
					i++;
					int toRemove = 1;
					while (i<end && s.array[i]==' ' && toRemove<tabSize) {
						i++;
						toRemove++;
					}
					doc.remove(start, toRemove);
				}
			}
		}

	}


	/**
	 * Action for moving the caret to the "matching bracket" of the bracket
	 * at the caret position (either before or after).
	 */
	public static class GoToMatchingBracketAction
									extends RecordableTextAction {

		private static final long serialVersionUID = 1L;

		public GoToMatchingBracketAction() {
			super(rstaGoToMatchingBracketAction);
		}

		public GoToMatchingBracketAction(String name, Icon icon, String desc,
							Integer mnemonic, KeyStroke accelerator) {
			super(name, icon, desc, mnemonic, accelerator);
		}

		public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
			RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
			int pos = RSyntaxUtilities.getMatchingBracketPosition(rsta);
			if (pos>-1) {
				// Go to the position AFTER the bracket so the previous
				// bracket (which we were just on) is highlighted.
				rsta.setCaretPosition(pos+1);
			}
			else {
				UIManager.getLookAndFeel().provideErrorFeedback(rsta);
			}
		}

		public final String getMacroID() {
			return rstaGoToMatchingBracketAction;
		}

	}


	/**
	 * Action for increasing the font size of all fonts in the text area.
	 */
	public static class IncreaseFontSizeAction
					extends RTextAreaEditorKit.IncreaseFontSizeAction {

		private static final long serialVersionUID = 1L;

		public IncreaseFontSizeAction() {
			super();
		}

		public IncreaseFontSizeAction(String name, Icon icon, String desc,
							Integer mnemonic, KeyStroke accelerator) {
			super(name, icon, desc, mnemonic, accelerator);
		}

		public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {

			RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
			SyntaxHighlightingColorScheme scheme = rsta.
									getSyntaxHighlightingColorScheme();

			// All we need to do is update all of the fonts in syntax
			// schemes, then call setSyntaxHighlightingColorScheme with the
			// same scheme already being used.  This relies on the fact that
			// that method does not check whether the new scheme is different
			// from the old scheme before updating.

			boolean changed = false;
			int count = scheme.syntaxSchemes.length;
			for (int i=0; i<count; i++) {
				SyntaxScheme ss = scheme.syntaxSchemes[i];
				if (ss!=null) {
					Font font = ss.font;
					if (font!=null) {
						float oldSize = font.getSize2D();
						float newSize = oldSize + increaseAmount;
						if (newSize<=MAXIMUM_SIZE) {
							// Grow by increaseAmount.
							ss.font = font.deriveFont(newSize);
							changed = true;
						}
						else if (oldSize<MAXIMUM_SIZE) {
							// Can't grow by full increaseAmount, but
							// can grow a little bit.
							ss.font = font.deriveFont(MAXIMUM_SIZE);
							changed = true;
						}
					}
				}
			}

			// Do the text area's font also.
			Font font = rsta.getFont();
			float oldSize = font.getSize2D();
			float newSize = oldSize + increaseAmount;
			if (newSize<=MAXIMUM_SIZE) {
				// Grow by increaseAmount.
				rsta.setFont(font.deriveFont(newSize));
				changed = true;
			}
			else if (oldSize<MAXIMUM_SIZE) {
				// Can't grow by full increaseAmount, but
				// can grow a little bit.
				rsta.setFont(font.deriveFont(MAXIMUM_SIZE));
				changed = true;
			}

			// If we updated at least one font, update the screen.  If
			// all of the fonts were already the minimum size, beep.
			if (changed) {
				rsta.setSyntaxHighlightingColorScheme(scheme);
				// NOTE:  This is a hack to get an encompassing
				// RTextScrollPane to repaint its line numbers to account
				// for a change in line height due to a font change.  I'm
				// not sure why we need to do this here but not when we
				// change the syntax highlighting color scheme via the
				// Options dialog... setSyntaxHighlightingColorScheme()
				// calls revalidate() which won't repaint the scroll pane
				// if scrollbars don't change, which is why we need this.
				Component parent = rsta.getParent();
				if (parent instanceof javax.swing.JViewport) {
					parent = parent.getParent();
					if (parent instanceof JScrollPane) {
						parent.repaint();
					}
				}
			}
			else
				UIManager.getLookAndFeel().provideErrorFeedback(rsta);

		}

	}


	/**
	 * Action for when the user presses the Enter key.  This is here so we can
	 * be smart and "auto-indent" for programming languages.
	 */
	public static class InsertBreakAction extends RecordableTextAction {

		private static final long serialVersionUID = 1L;

		public InsertBreakAction() {
			super(DefaultEditorKit.insertBreakAction);
		}

		public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {

			if (!textArea.isEditable() || !textArea.isEnabled()) {
				UIManager.getLookAndFeel().provideErrorFeedback(textArea);
				return;
			}

			RSyntaxTextArea sta = (RSyntaxTextArea)textArea;

			// If we're in insert-mode and auto-indenting...
			if (sta.getTextMode()==RTextArea.INSERT_MODE && 
				sta.isAutoIndentEnabled()) {

				try {

					int caretPosition = textArea.getCaretPosition();
					Document doc = textArea.getDocument();
					Element map = doc.getDefaultRootElement();
					int lineNum = map.getElementIndex(caretPosition);
					Element line = map.getElement(lineNum);
					int start = line.getStartOffset();
					int end = line.getEndOffset()-1; // Why always "-1"?
					String s = sta.getText(start,end-start);
					int len = s.length();

					// endWS is the end of the leading whitespace of the
					// current line.
					int endWS = 0;
					while (endWS<len &&
						RSyntaxUtilities.isWhitespace(s.charAt(endWS)))
						endWS++;

					// If there is only whitespace between the caret and
					// the EOL, pressing Enter auto-indents the new line to
					// the same place as the previous line.
					int nonWhitespacePos = atEndOfLine(caretPosition-start,
												s, len);
					if (nonWhitespacePos==-1) {
						// If the line was nothing but whitespace...
						if (endWS==len && sta.isClearWhitespaceLinesEnabled())
							sta.replaceRange(null, start,end);
						sta.replaceSelection("\n"+s.substring(0,endWS));
					}

					// If there is non-whitespace between the caret and the
					// EOL, pressing Enter takes that text to the next line
					// and auto-indents it to the same place as the last
					// line.
					else {
						sta.replaceRange("\n" + s.substring(0,endWS) +
								s.substring(nonWhitespacePos),
								caretPosition, end);
						sta.setCaretPosition(caretPosition + endWS + 1);
					}

				} catch (BadLocationException ble) {
					sta.replaceSelection("\n");
				}

			}

			// Otherwise, we're in overwrite-mode or not auto-indenting.
			else {
				textArea.replaceSelection("\n");
			}

		}

		/**
		 * @return The first location in the string past <code>pos</code> that
		 *         is NOT a whitespace char, or <code>-1</code> if only
		 *         whitespace chars follow <code>pos</code> (or it is the end
		 *         position in the string).
		 */
		private static final int atEndOfLine(int pos, String s, int sLen) {
			for (int i=pos; i<sLen; i++) {
				if (!RSyntaxUtilities.isWhitespace(s.charAt(i)))
					return i;
			}
			return -1;
		}

		public final String getMacroID() {
			return DefaultEditorKit.insertBreakAction;
		}

	}


	/**
	 * Action for inserting tabs.  This is extended to "block indent" a
	 * group of contiguous lines if they are selected.
	 */
	public static class InsertTabAction extends RecordableTextAction {

		private static final long serialVersionUID = 1L;

		public InsertTabAction() {
			super(insertTabAction);
		}

		public InsertTabAction(String name) {
			super(name);
		}

		public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {

			if (!textArea.isEditable() || !textArea.isEnabled()) {
				UIManager.getLookAndFeel().provideErrorFeedback(textArea);
				return;
			}

			Document document = textArea.getDocument();
			Element map = document.getDefaultRootElement();
			Caret c = textArea.getCaret();
			int dot = c.getDot();
			int mark = c.getMark();
			int dotLine = map.getElementIndex(dot);
			int markLine = map.getElementIndex(mark);

			// If there is a multiline selection, indent all lines in
			// the selection.
			if (dotLine!=markLine) {
				int first = Math.min(dotLine, markLine);
				int last = Math.max(dotLine, markLine);
				Element elem; int start;
				try {
					for (int i=first; i<last; i++) {
						elem = map.getElement(i);
						start = elem.getStartOffset();
						document.insertString(start, "\t", null);
					}
					// Don't do the last line if the caret is at its
					// beginning.  We must call getDot() again and not just
					// use 'dot' as the caret's position may have changed
					// due to the insertion of the tabs above.
					elem = map.getElement(last);
					start = elem.getStartOffset();
					if (Math.max(c.getDot(), c.getMark())!=start) {
						document.insertString(start, "\t", null);
					}
				} catch (BadLocationException ble) { // Never happens.
					ble.printStackTrace();
					UIManager.getLookAndFeel().
									provideErrorFeedback(textArea);
				}
			}
			else {
				textArea.replaceSelection("\t");
			}

		}

		public final String getMacroID() {
			return insertTabAction;
		}

	}


	/**
	 * Action for when the user tries to insert a template (that is,
	 * they've typed a template ID and pressed the trigger character
	 * (a space) in an attempt to do the substitution).
	 */
	public static class PossiblyInsertTemplateAction extends RecordableTextAction {

		private static final long serialVersionUID = 1L;

		public PossiblyInsertTemplateAction() {
			super(rstaPossiblyInsertTemplateAction);
		}

		public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {

			if (!textArea.isEditable() || !textArea.isEnabled())
				return;

			RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;

			if (RSyntaxTextArea.getTemplatesEnabled()) {

				Document doc = textArea.getDocument();
				if (doc != null) {

					try {

						Caret c = textArea.getCaret();
						int dot = c.getDot();
						int mark = c.getMark();
						int p0 = Math.min(dot, mark);
						int p1 = Math.max(dot, mark);
						CodeTemplateManager manager = RSyntaxTextArea.
											getCodeTemplateManager();
						CodeTemplate template = 
							manager==null ? null :
										manager.getTemplate(rsta);

						// A non-null template means modify the text to insert!
						if (template!=null) {
							Element map = doc.getDefaultRootElement();
							int lineNum = map.getElementIndex(dot);
							Element line = map.getElement(lineNum);
							int start = line.getStartOffset();
							int end = line.getEndOffset()-1; // Why always "-1"?
							String s = textArea.getText(start,end-start);
							int len = s.length();
							// endWS is the end of the leading whitespace
							// of the current line.
							int endWS = 0;
							while (endWS<len && RSyntaxUtilities.
										isWhitespace(s.charAt(endWS)))
								endWS++;
							s = s.substring(0, endWS);
							p0 -= template.getID().length;
							s = template.getContentToInsert(s);
							((RSyntaxDocument)doc).replace(p0,p1-p0,
										s, null);
							textArea.setCaretPosition(p0 +
									template.getBeforeCaretText().length());
						}

						// No template - insert default text.  This is
						// exactly what DefaultKeyTypedAction does.
						else {
							doDefaultInsert(rsta);
						}

					} catch (BadLocationException ble) {
						UIManager.getLookAndFeel().
								provideErrorFeedback(textArea);
					}


				} // End of if (doc!=null).

			} // End of if (textArea.getTemplatesEnabled()).

			// If templates aren't enabled, just insert the text as usual.
			else {
				doDefaultInsert(rsta);
			}

		}

		private final void doDefaultInsert(RTextArea textArea) {
			// FIXME:  We need a way to get the "trigger string" (i.e.,
			// the text that was just typed); however, the text area's
			// template manager might be null (if templates are disabled).
			// Also, the manager's trigger string doesn't yet match up with
			// that defined in RSyntaxTextAreaEditorKit.java (which is
			// hardcoded as a space)...
			//String str = manager.getInsertTriggerString();
			//int mod = manager.getInsertTrigger().getModifiers();
			//if (str!=null && str.length()>0 &&
			//	((mod&ActionEvent.ALT_MASK)==(mod&ActionEvent.CTRL_MASK))) {
			//	char ch = str.charAt(0);
			//	if (ch>=0x20 && ch!=0x7F)
			//		textArea.replaceSelection(str);
			//}
			textArea.replaceSelection(" ");
		}

		public final String getMacroID() {
			return rstaPossiblyInsertTemplateAction;
		}

	}


	/**
	 * Action that toggles whether the currently selected lines are
	 * commented.
	 */
	public static class ToggleCommentAction extends RecordableTextAction {

		public ToggleCommentAction() {
			super(rstaToggleCommentAction);
		}

		public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {

			if (!textArea.isEditable() || !textArea.isEnabled()) {
				UIManager.getLookAndFeel().provideErrorFeedback(textArea);
				return;
			}

			RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
			String[] startEnd = doc.getLineCommentStartAndEnd();

			if (startEnd==null) {
				UIManager.getLookAndFeel().provideErrorFeedback(textArea);
				return;
			}

			Element map = doc.getDefaultRootElement();
			Caret c = textArea.getCaret();
			int dot = c.getDot();
			int mark = c.getMark();
			int line1 = map.getElementIndex(dot);
			int line2 = map.getElementIndex(mark);
			int start = Math.min(line1, line2);
			int end   = Math.max(line1, line2);

			// Don't toggle comment on last line if there is no
			// text selected on it.
			if (start!=end) {
				Element elem = map.getElement(end);
				if (Math.max(dot, mark)==elem.getStartOffset()) {
					end--;
				}
			}

			try {
				boolean add = getDoAdd(doc,map, start,end, startEnd);
				for (line1=start; line1<=end; line1++) {
					Element elem = map.getElement(line1);
					handleToggleComment(elem, doc, startEnd, add);
				}
			} catch (BadLocationException ble) {
				ble.printStackTrace();
				UIManager.getLookAndFeel().provideErrorFeedback(textArea);
			}

		}

		private boolean getDoAdd(Document doc, Element map, int startLine,
							int endLine, String[] startEnd)
								throws BadLocationException {
			boolean doAdd = false;
			for (int i=startLine; i<=endLine; i++) {
				Element elem = map.getElement(i);
				int start = elem.getStartOffset();
				String t = doc.getText(start, elem.getEndOffset()-start-1);
				if (!t.startsWith(startEnd[0]) ||
						(startEnd[1]!=null && !t.endsWith(startEnd[1]))) {
					doAdd = true;
					break;
				}
			}
			return doAdd;
		}

		private void handleToggleComment(Element elem, Document doc,
			String[] startEnd, boolean add) throws BadLocationException {
			int start = elem.getStartOffset();
			int end = elem.getEndOffset() - 1;
			if (add) {
				doc.insertString(start, startEnd[0], null);
				if (startEnd[1]!=null) {
					doc.insertString(end+startEnd[0].length(), startEnd[1],
									null);
				}
			}
			else {
				doc.remove(start, startEnd[0].length());
				if (startEnd[1]!=null) {
					int temp = startEnd[1].length();
					doc.remove(end-startEnd[0].length()-temp, temp);
				}
			}
		}

		public final String getMacroID() {
			return rstaToggleCommentAction;
		}

	}


}