/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import org.fife.util.SwingUtils;


/**
 * Handles matching the bracket at (or near) the caret position with its
 * corresponding bracket, if any, and painting the match.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class BracketMatchingSupport {

	private final RSyntaxTextArea textArea;

	/**
	 * The rectangle surrounding the "matched bracket" if bracket matching
	 * is enabled.
	 */
	private Rectangle match;

	/**
	 * The rectangle surrounding the current offset if both bracket matching
	 * and "match both brackets" are enabled.
	 */
	private Rectangle dotRect;

	/**
	 * Used to store the location of the bracket at the caret position
	 * (either just before or just after it) and the location of its match.
	 */
	private Point bracketInfo;

	/**
	 * Colors used for the "matched bracket" if bracket matching is enabled.
	 */
	private Color matchedBracketBGColor;
	private Color matchedBracketBorderColor;

	/** The location of the last matched bracket. */
	private int lastBracketMatchPos;

	/** Whether bracket matching is enabled. */
	private boolean bracketMatchingEnabled;

	/** Whether bracket matching is animated. */
	private boolean animateBracketMatching;

	/** Whether <b>both</b> brackets are highlighted when bracket matching. */
	private boolean paintMatchedBracketPair;

	/** Whether a popup showing matched bracket lines when they're off-screen. */
	private boolean showMatchedBracketPopup;

	private BracketMatchingTimer bracketRepaintTimer;

	private MatchedBracketPopupTimer matchedBracketPopupTimer;


	/**
	 * Constructor.
	 *
	 * @param textArea The text area whose bracket matching this instance
	 *        will manage.
	 */
	BracketMatchingSupport(RSyntaxTextArea textArea) {
		this.textArea = textArea;
		lastBracketMatchPos = -1;
	}


	/**
	 * If the caret is on a bracket, this method finds the matching bracket,
	 * and if it exists, highlights it.
	 */
	void doBracketMatching() {

		// We always need to repaint the "matched bracket" highlight if it
		// exists.
		if (match!=null) {
			textArea.repaint(match);
			if (dotRect!=null) {
				textArea.repaint(dotRect);
			}
		}

		// If a matching bracket is found, get its bounds and paint it!
		int lastCaretBracketPos = bracketInfo==null ? -1 : bracketInfo.x;
		bracketInfo = RSyntaxUtilities.getMatchingBracketPosition(textArea,
				bracketInfo);
		if (bracketInfo.y>-1 &&
				(bracketInfo.y!=lastBracketMatchPos ||
				 bracketInfo.x!=lastCaretBracketPos)) {
			try {
				match = SwingUtils.getBounds(textArea, bracketInfo.y);
				if (match!=null) { // Happens if we're not yet visible
					if (paintMatchedBracketPair) {
						dotRect = SwingUtils.getBounds(textArea, bracketInfo.x);
					}
					else {
						dotRect = null;
					}
					if (animateBracketMatching) {
						bracketRepaintTimer.restart();
					}
					textArea.repaint(match);
					if (dotRect!=null) {
						textArea.repaint(dotRect);
					}

					if (showMatchedBracketPopup) {
						Container parent = textArea.getParent();
						if (parent instanceof JViewport) {
							Rectangle visibleRect = textArea.getVisibleRect();
							if (match.y + match.height < visibleRect.getY()) {
								if (matchedBracketPopupTimer == null) {
									matchedBracketPopupTimer =
											new MatchedBracketPopupTimer();
								}
								matchedBracketPopupTimer.restart(bracketInfo.y);
							}
						}
					}

				}
			} catch (BadLocationException ble) {
				ble.printStackTrace(); // Shouldn't happen.
			}
		}
		else if (bracketInfo.y==-1) {
			// Set match to null so the old value isn't still repainted.
			match = null;
			dotRect = null;
			bracketRepaintTimer.stop();
		}
		lastBracketMatchPos = bracketInfo.y;

	}


	/**
	 * Returns whether bracket matching should be animated.
	 *
	 * @return Whether bracket matching should be animated.
	 * @see #setAnimateBracketMatching(boolean)
	 */
	boolean getAnimateBracketMatching() {
		return animateBracketMatching;
	}


	/**
	 * Returns the caret's offset's rectangle, or <code>null</code> if there
	 * is currently no matched bracket, bracket matching is disabled, or "paint
	 * both matched brackets" is disabled.  This should never be called by the
	 * programmer directly.
	 *
	 * @return The rectangle surrounding the matched bracket.
	 * @see #getMatchRectangle()
	 */
	Rectangle getDotRectangle() {
		return dotRect;
	}


	/**
	 * Gets the color used as the background for a matched bracket.
	 *
	 * @return The color used.  If this is <code>null</code>, no special
	 *         background is painted behind a matched bracket.
	 * @see #setMatchedBracketBGColor(Color)
	 * @see #getMatchedBracketBorderColor()
	 */
	Color getMatchedBracketBGColor() {
		return matchedBracketBGColor;
	}


	/**
	 * Gets the color used as the border for a matched bracket.
	 *
	 * @return The color used.
	 * @see #setMatchedBracketBorderColor(Color)
	 * @see #getMatchedBracketBGColor()
	 */
	Color getMatchedBracketBorderColor() {
		return matchedBracketBorderColor;
	}


	/**
	 * Returns the matched bracket's rectangle, or <code>null</code> if there
	 * is currently no matched bracket.  This should never be called by the
	 * programmer directly.
	 *
	 * @return The rectangle surrounding the matched bracket.
	 * @see #getDotRectangle()
	 */
	Rectangle getMatchRectangle() {
		return match;
	}


	/**
	 * Returns whether the bracket at the caret position is painted as a
	 * "match" when a matched bracket is found.  Note that this property does
	 * nothing if {@link #isEnabled()} returns <code>false</code>.
	 *
	 * @return Whether both brackets in a bracket pair are highlighted when
	 *         bracket matching is enabled.
	 * @see #setPaintMatchedBracketPair(boolean)
	 * @see #isEnabled()
	 * @see #setEnabled(boolean)
	 */
	boolean getPaintMatchedBracketPair() {
		return paintMatchedBracketPair;
	}


	/**
	 * Returns whether a small popup window should display the text on the
	 * line containing a matched bracket whenever a matched bracket is off-
	 * screen.
	 *
	 * @return Whether to show the popup.
	 * @see #setShowMatchedBracketPopup(boolean)
	 */
	boolean getShowMatchedBracketPopup() {
		return showMatchedBracketPopup;
	}


	/**
	 * Forces the bracket match, if any, to be recomputed from the current
	 * caret position.  This should be called whenever something that affects
	 * the bracket's painted bounds changes, such as the font or color
	 * scheme.
	 */
	void forceReMatch() {
		lastBracketMatchPos = -1;
		doBracketMatching();
	}


	/**
	 * Hides any currently matched bracket's highlight, without recomputing
	 * the match.
	 */
	void hideMatch() {
		match = null;
		dotRect = null;
	}


	/**
	 * Returns whether bracket matching is enabled.
	 *
	 * @return <code>true</code> iff bracket matching is enabled.
	 * @see #setEnabled(boolean)
	 */
	boolean isEnabled() {
		return bracketMatchingEnabled;
	}


	/**
	 * Sets whether bracket matching should be animated.
	 *
	 * @param animate Whether to animate bracket matching.
	 * @see #getAnimateBracketMatching()
	 */
	void setAnimateBracketMatching(boolean animate) {
		if (animate!=animateBracketMatching) {
			animateBracketMatching = animate;
			if (animate && bracketRepaintTimer==null) {
				bracketRepaintTimer = new BracketMatchingTimer();
			}
			textArea.firePropertyChange(
					RSyntaxTextArea.ANIMATE_BRACKET_MATCHING_PROPERTY,
					!animate, animate);
		}
	}


	/**
	 * Sets whether bracket matching is enabled.
	 *
	 * @param enabled Whether bracket matching should be enabled.
	 * @see #isEnabled()
	 */
	void setEnabled(boolean enabled) {
		if (enabled!=bracketMatchingEnabled) {
			bracketMatchingEnabled = enabled;
			textArea.repaint();
			textArea.firePropertyChange(
					RSyntaxTextArea.BRACKET_MATCHING_PROPERTY, !enabled, enabled);
		}
	}


	/**
	 * Sets the color used as the background for a matched bracket.
	 *
	 * @param color The color to use.  If this is <code>null</code>, then no
	 *        special background is painted behind a matched bracket.
	 * @see #getMatchedBracketBGColor()
	 * @see #setMatchedBracketBorderColor(Color)
	 */
	void setMatchedBracketBGColor(Color color) {
		matchedBracketBGColor = color;
		if (match!=null) {
			textArea.repaint();
		}
	}


	/**
	 * Sets the color used as the border for a matched bracket.
	 *
	 * @param color The color to use.
	 * @see #getMatchedBracketBorderColor()
	 * @see #setMatchedBracketBGColor(Color)
	 */
	void setMatchedBracketBorderColor(Color color) {
		matchedBracketBorderColor = color;
		if (match!=null) {
			textArea.repaint();
		}
	}


	/**
	 * Sets whether the bracket at the caret position is painted as a "match"
	 * when a matched bracket is found.  Note that this property does nothing
	 * if {@link #isEnabled()} returns <code>false</code>.
	 *
	 * @param paintPair Whether both brackets in a bracket pair should be
	 *        highlighted when bracket matching is enabled.
	 * @see #getPaintMatchedBracketPair()
	 * @see #isEnabled()
	 * @see #setEnabled(boolean)
	 */
	void setPaintMatchedBracketPair(boolean paintPair) {
		if (paintPair!=paintMatchedBracketPair) {
			paintMatchedBracketPair = paintPair;
			doBracketMatching();
			textArea.repaint();
			textArea.firePropertyChange(
					RSyntaxTextArea.PAINT_MATCHED_BRACKET_PAIR_PROPERTY,
					!paintMatchedBracketPair, paintMatchedBracketPair);
		}
	}


	/**
	 * Sets whether a small popup window should display the text on the
	 * line containing a matched bracket whenever a matched bracket is off-
	 * screen.
	 *
	 * @param show Whether to show the popup.
	 * @see #getShowMatchedBracketPopup()
	 */
	void setShowMatchedBracketPopup(boolean show) {
		showMatchedBracketPopup = show;
	}


	/**
	 * A timer that animates the "bracket matching" animation.
	 */
	private class BracketMatchingTimer extends Timer implements ActionListener {

		private int pulseCount;

		BracketMatchingTimer() {
			super(20, null);
			addActionListener(this);
			setCoalesce(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (bracketMatchingEnabled) {
				if (match!=null) {
					updateAndInvalidate(match);
				}
				if (dotRect!=null && paintMatchedBracketPair) {
					updateAndInvalidate(dotRect);
				}
				if (++pulseCount==8) {
					pulseCount = 0;
					stop();
				}
			}
		}

		private void init(Rectangle r) {
			r.x += 3;
			r.y += 3;
			r.width -= 6;
			r.height -= 6; // So animation can "grow" match
		}

		@Override
		public void start() {
			init(match);
			if (dotRect!=null && paintMatchedBracketPair) {
				init(dotRect);
			}
			pulseCount = 0;
			super.start();
		}

		private void updateAndInvalidate(Rectangle r) {
			if (pulseCount<5) {
				r.x--;
				r.y--;
				r.width += 2;
				r.height += 2;
				textArea.repaint(r.x,r.y, r.width,r.height);
			}
			else if (pulseCount<7) {
				r.x++;
				r.y++;
				r.width -= 2;
				r.height -= 2;
				textArea.repaint(r.x-2,r.y-2, r.width+5,r.height+5);
			}
		}

	}


	/**
	 * Handles showing and hiding the popup with the text of a matched
	 * bracket's line, when that line is off-screen.
	 */
	private final class MatchedBracketPopupTimer extends Timer
			implements ActionListener, CaretListener {

		private MatchedBracketPopup popup;
		private int origDot;
		private int matchedBracketOffs;

		private MatchedBracketPopupTimer() {
			super(350, null);
			addActionListener(this);
			setRepeats(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if (popup != null) {
				popup.dispose();
			}

			if (textArea.hasFocus()) {
				Window window = SwingUtilities.getWindowAncestor(textArea);
				popup = new MatchedBracketPopup(window, textArea, matchedBracketOffs);
				popup.pack();
				popup.setVisible(true);
			}
		}

		@Override
		public void caretUpdate(CaretEvent e) {
			int dot = e.getDot();
			if (dot != origDot) {
				stop();
				textArea.removeCaretListener(this);
				if (popup != null) {
					popup.dispose();
				}
			}
		}

		/**
		 * Restarts this timer, and stores a new offset to paint.
		 *
		 * @param matchedBracketOffs The offset of the new matched bracket.
		 */
		public void restart(int matchedBracketOffs) {
			this.origDot = textArea.getCaretPosition();
			this.matchedBracketOffs = matchedBracketOffs;
			this.restart();
		}

		@Override
		public void start() {
			super.start();
			textArea.addCaretListener(this);
		}

	}


}
