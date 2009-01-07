/*
 * 01/06/2009
 *
 * MarkOccurrencesSupport.java - Handles marking all occurrences of the
 * currently selected identifier in a text area.
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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Highlighter;

import org.fife.ui.rtextarea.ChangableHighlightPainter;


/**
 * Marks all occurrences of the token at the current caret position, if it is
 * an identifier.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class MarkOccurrencesSupport implements CaretListener, ActionListener {

	private RSyntaxTextArea textArea;
	private javax.swing.Timer timer;
	private ChangableHighlightPainter p;
	private List tags;

	/**
	 * The default delay.
	 */
	private static final int DEFAULT_DELAY_MS = 1000;


	/**
	 * Constructor.  Creates a listener with a 1 second delay.
	 */
	public MarkOccurrencesSupport() {
		this(DEFAULT_DELAY_MS);
	}


	/**
	 * Constructor.
	 *
	 * @param delay The delay between when the caret last moves and when the
	 *        text should be scanned for matching occurrences.  This should
	 *        be in milliseconds.
	 */
	public MarkOccurrencesSupport(int delay) {
		timer = new Timer(delay, this);
		timer.setRepeats(false);
		p = new ChangableHighlightPainter();
		setColor(new Color(224,224,224));
		tags = new ArrayList();
	}


	/**
	 * Called after the caret has been moved and a fixed time delay has
	 * elapsed.  This locates and highlights all occurrences of the identifier
	 * at the caret position, if any.
	 *
	 * @param e The event.
	 */
	public void actionPerformed(ActionEvent e) {

		// Don't do anything if they are selecting text.
		Caret c = textArea.getCaret();
		if (c.getDot()!=c.getMark()) {
			return;
		}

		//System.out.println("DEBUG: Marking occurrences...");
		RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
		doc.readLock();
		try {

			// Remove old highlights
			removeHighlights();

			// Get the token at the caret position.
			Highlighter h = textArea.getHighlighter();
			int line = textArea.getCaretLineNumber();
			Token tokenList = textArea.getTokenListForLine(line);
			int dot = c.getDot();
			Token t = RSyntaxUtilities.getTokenAtOffset(tokenList, dot);
			if (t==null /* End of line */ || t.type!=Token.IDENTIFIER) {
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

			// Add new highlights if an identifier is selected.
			if (t!=null && t.type==Token.IDENTIFIER) {
				System.out.println("Identifier!");
				String lexeme = t.getLexeme();
				for (int i=0; i<textArea.getLineCount(); i++) {
					Token temp = textArea.getTokenListForLine(i);
					while (temp!=null && temp.isPaintable()) {
						if (temp.type==Token.IDENTIFIER &&
								temp.getLexeme().equals(lexeme)) {
							try {
								Object tag = h.addHighlight(temp.offset,
												temp.offset+temp.textCount, p);
								tags.add(tag);
							} catch (BadLocationException ble) {
								ble.printStackTrace(); // Never happens
							}
						}
						temp = temp.getNextToken();
					}
				}
			}

		} finally {
			doc.readUnlock();
		}

	}


	/**
	 * Called when the caret moves in the text area.
	 *
	 * @param e The event.
	 */
	public void caretUpdate(CaretEvent e) {
		timer.restart();
	}


	/**
	 * Returns the color being used to mark occurrences.
	 *
	 * @return The color being used.
	 * @see #setColor(Color)
	 */
	public Color getColor() {
		return (Color)p.getPaint();
	}


	/**
	 * Returns the delay, in milliseconds.
	 *
	 * @return The delay.
	 * @see #setDelay(int)
	 */
	public int getDelay() {
		return timer.getDelay();
	}


	/**
	 * Installs this listener on a text area.  If it is already installed on
	 * another text area, it is uninstalled first.
	 *
	 * @param textArea The text area to install on.
	 */
	public void install(RSyntaxTextArea textArea) {
		if (this.textArea!=null) {
			uninstall();
		}
		this.textArea = textArea;
		textArea.addCaretListener(this);
	}


	/**
	 * Removes all highlights added to the text area by this listener.
	 */
	private void removeHighlights() {
		if (textArea!=null) {
			Highlighter h = textArea.getHighlighter();
			for (int i=0; i<tags.size(); i++) {
				h.removeHighlight(tags.get(i));
			}
		}
		tags.clear();
	}


	/**
	 * Sets the color to use when marking occurrences.
	 *
	 * @param c The color to use.
	 * @see #getColor()
	 */
	public void setColor(Color c) {
		p.setPaint(c);
	}


	/**
	 * Sets the delay between the last caret position change and when the
	 * text is scanned for matching identifiers.  A delay is needed to prevent
	 * repeated scanning while the user is typing.
	 *
	 * @param delay The new delay.
	 * @see #getDelay()
	 */
	public void setDelay(int delay) {
		timer.setDelay(delay);
	}


	/**
	 * Uninstalls this listener from the current text area.  Does nothing if
	 * it not currently installed on any text area.
	 *
	 * @see #install(RSyntaxTextArea)
	 */
	public void uninstall() {
		if (textArea!=null) {
			removeHighlights();
			textArea.removeCaretListener(this);
		}
	}


}