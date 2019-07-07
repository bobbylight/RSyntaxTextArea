/*
 * 01/06/2009
 *
 * MarkOccurrencesSupport.java - Handles marking all occurrences of the
 * currently selected identifier in a text area.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Caret;

import org.fife.ui.rtextarea.SmartHighlightPainter;


/**
 * Marks all occurrences of the token at the current caret position, if it is
 * an identifier.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see OccurrenceMarker
 */
class MarkOccurrencesSupport implements CaretListener, ActionListener {

	private RSyntaxTextArea textArea;
	private Timer timer;
	private SmartHighlightPainter p;

	/**
	 * The default color used to mark occurrences.
	 */
	static final Color DEFAULT_COLOR = new Color(224, 224, 224);

	/**
	 * The default delay.
	 */
	static final int DEFAULT_DELAY_MS = 1000;


	/**
	 * Constructor.  Creates a listener with a 1 second delay.
	 */
	MarkOccurrencesSupport() {
		this(DEFAULT_DELAY_MS);
	}


	/**
	 * Constructor.
	 *
	 * @param delay The delay between when the caret last moves and when the
	 *        text should be scanned for matching occurrences.  This should
	 *        be in milliseconds.
	 */
	MarkOccurrencesSupport(int delay) {
		this(delay, DEFAULT_COLOR);
	}


	/**
	 * Constructor.
	 *
	 * @param delay The delay between when the caret last moves and when the
	 *        text should be scanned for matching occurrences.  This should
	 *        be in milliseconds.
	 * @param color The color to use to mark the occurrences.  This cannot be
	 *        <code>null</code>.
	 */
	MarkOccurrencesSupport(int delay, Color color) {
		timer = new Timer(delay, this);
		timer.setRepeats(false);
		p = new SmartHighlightPainter();
		setColor(color);
	}


	/**
	 * Called after the caret has been moved and a fixed time delay has
	 * elapsed.  This locates and highlights all occurrences of the identifier
	 * at the caret position, if any.<p>
	 *
	 * Callers should not call this method directly, but should rather prefer
	 * {@link #doMarkOccurrences()} to mark occurrences.
	 *
	 * @param e The event.
	 * @see #doMarkOccurrences()
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// Don't do anything if they are selecting text.
		Caret c = textArea.getCaret();
		if (c.getDot()!=c.getMark()) {
			return;
		}

		RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
		OccurrenceMarker occurrenceMarker = doc.getOccurrenceMarker();
		boolean occurrencesChanged = false;

		if (occurrenceMarker!=null) {

			doc.readLock();
			try {

				Token t = occurrenceMarker.getTokenToMark(textArea);

				if (t!=null && occurrenceMarker.isValidType(textArea, t) &&
						!RSyntaxUtilities.isNonWordChar(t)) {
					clear();
					RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)
							textArea.getHighlighter();
					occurrenceMarker.markOccurrences(doc, t, h, p);
					//textArea.repaint();
					// TODO: Do a textArea.repaint() instead of repainting each
					// marker as it's added if count is huge
					occurrencesChanged = true;
				} else {
					clear();
				}

			} finally {
				doc.readUnlock();
				//time = System.currentTimeMillis() - time;
				//System.out.println("MarkOccurrencesSupport took: " + time + " ms");
			}

		}

		if (occurrencesChanged) {
			textArea.fireMarkedOccurrencesChanged();
		}

	}


	/**
	 * Called when the caret moves in the text area.
	 *
	 * @param e The event.
	 */
	@Override
	public void caretUpdate(CaretEvent e) {
		timer.restart();
	}


	/**
	 * Removes all highlights added to the text area by this listener.
	 */
	void clear() {
		if (textArea!=null) {
			RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)
													textArea.getHighlighter();
			h.clearMarkOccurrencesHighlights();
		}
	}


	/**
	 * Immediately marks all occurrences of the token at the current caret
	 * position.
	 */
	public void doMarkOccurrences() {
		timer.stop();
		actionPerformed(null);
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
	 * Returns whether a border is painted around marked occurrences.
	 *
	 * @return Whether a border is painted.
	 * @see #setPaintBorder(boolean)
	 * @see #getColor()
	 */
	public boolean getPaintBorder() {
		return p.getPaintBorder();
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
		if (textArea.getMarkOccurrencesColor()!=null) {
			setColor(textArea.getMarkOccurrencesColor());
		}
	}


	/**
	 * Sets the color to use when marking occurrences.
	 *
	 * @param color The color to use.
	 * @see #getColor()
	 * @see #setPaintBorder(boolean)
	 */
	public void setColor(Color color) {
		p.setPaint(color);
		if (textArea!=null) {
			clear();
			caretUpdate(null); // Force a highlight repaint.
		}
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
		timer.setInitialDelay(delay);
	}


	/**
	 * Toggles whether a border is painted around marked highlights.
	 *
	 * @param paint Whether to paint a border.
	 * @see #getPaintBorder()
	 * @see #setColor(Color)
	 */
	public void setPaintBorder(boolean paint) {
		if (paint!=p.getPaintBorder()) {
			p.setPaintBorder(paint);
			if (textArea!=null) {
				textArea.repaint();
			}
		}
	}


	/**
	 * Uninstalls this listener from the current text area.  Does nothing if
	 * it not currently installed on any text area.
	 *
	 * @see #install(RSyntaxTextArea)
	 */
	public void uninstall() {
		if (textArea!=null) {
			clear();
			textArea.removeCaretListener(this);
		}
	}


}
