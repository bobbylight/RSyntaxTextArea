/*
 * 02/10/2009
 *
 * LineHighlightManager - Manages line highlights.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.Position;


/**
 * Manages line highlights in an <code>RTextArea</code>.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class LineHighlightManager {

	private RTextArea textArea;
	private List<LineHighlightInfo> lineHighlights;
	private LineHighlightInfoComparator comparator;

	/**
	 * Constructor.
	 *
	 * @param textArea The parent text area.
	 */
	LineHighlightManager(RTextArea textArea) {
		this.textArea = textArea;
		comparator = new LineHighlightInfoComparator();
	}


	/**
	 * Highlights the specified line.
	 *
	 * @param line The line to highlight.
	 * @param color The color to highlight with.
	 * @return A tag for the highlight.
	 * @throws BadLocationException If <code>line</code> is not a valid line
	 *         number.
	 * @see #removeLineHighlight(Object)
	 */
	public Object addLineHighlight(int line, Color color)
									throws BadLocationException {
		int offs = textArea.getLineStartOffset(line);
		LineHighlightInfo lhi = new LineHighlightInfo(
						textArea.getDocument().createPosition(offs), color);
		if (lineHighlights==null) {
			lineHighlights = new ArrayList<>(1);
		}
		int index = Collections.binarySearch(lineHighlights, lhi, comparator);
		if (index<0) { // Common case
			index = -(index+1);
		}
		lineHighlights.add(index, lhi);
		repaintLine(lhi);
		return lhi;
	}


	/**
	 * Returns the current line highlights' tags.
	 *
	 * @return The current line highlights' tags, or an empty list if there
	 *         are none.
	 */
	protected List<Object> getCurrentLineHighlightTags() {
		return lineHighlights == null ? Collections.emptyList() :
			new ArrayList<>(lineHighlights);
	}


	/**
	 * Returns the current number of line highlights.  Useful for testing.
	 *
	 * @return The current number of line highlights.
	 */
	protected int getLineHighlightCount() {
		return lineHighlights == null ? 0 : lineHighlights.size();
	}


	/**
	 * Paints any highlighted lines in the specified line range.
	 *
	 * @param g The graphics context.
	 */
	public void paintLineHighlights(Graphics g) {

		int count = lineHighlights==null ? 0 : lineHighlights.size();
		if (count>0) {

			int docLen = textArea.getDocument().getLength();
			Rectangle vr = textArea.getVisibleRect();
			int lineHeight = textArea.getLineHeight();

			try {

				for (int i=0; i<count; i++) {
					LineHighlightInfo lhi = lineHighlights.get(i);
					int offs = lhi.getOffset();
					if (offs>=0 && offs<=docLen) {
						int y = textArea.yForLineContaining(offs);
						if (y>vr.y-lineHeight) {
							if (y<vr.y+vr.height) {
								g.setColor(lhi.getColor());
								g.fillRect(0,y, textArea.getWidth(),lineHeight);
							}
							else {
								break; // Out of visible rect
							}
						}
					}
				}

			} catch (BadLocationException ble) { // Never happens
				ble.printStackTrace();
			}
		}

	}


	/**
	 * Removes all line highlights.
	 *
	 * @see #removeLineHighlight(Object)
	 */
	public void removeAllLineHighlights() {
		if (lineHighlights!=null) {
			lineHighlights.clear();
			textArea.repaint();
		}
	}


	/**
	 * Removes a line highlight.
	 *
	 * @param tag The tag of the line highlight to remove.
	 * @see #addLineHighlight(int, Color)
	 */
	public void removeLineHighlight(Object tag) {
		if (tag instanceof LineHighlightInfo) {
			lineHighlights.remove(tag);
			repaintLine((LineHighlightInfo)tag);
		}
	}


	/**
	 * Repaints the line pointed to by the specified highlight information.
	 *
	 * @param lhi The highlight information.
	 */
	private void repaintLine(LineHighlightInfo lhi) {
		int offs = lhi.getOffset();
		// May be > length if they deleted text including the highlight
		if (offs>=0 && offs<=textArea.getDocument().getLength()) {
			try {
				int y = textArea.yForLineContaining(offs);
				if (y>-1) {
					textArea.repaint(0, y,
								textArea.getWidth(), textArea.getLineHeight());
				}
			} catch (BadLocationException ble) {
				ble.printStackTrace(); // Never happens
			}
		}
	}


	/**
	 * Information about a line highlight.
	 */
	private static class LineHighlightInfo {

		private Position offs;
		private Color color;

		LineHighlightInfo(Position offs, Color c) {
			this.offs = offs;
			this.color = c;
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof LineHighlightInfo) {
				LineHighlightInfo lhi2 = (LineHighlightInfo)other;
				return this.getOffset() == lhi2.getOffset() &&
					this.getColor() == null ? lhi2.getColor() == null :
							this.getColor().equals(lhi2.getColor());
			}
			return false;
		}

		public Color getColor() {
			return color;
		}

		public int getOffset() {
			return offs.getOffset();
		}

		@Override
		public int hashCode() {
			return getOffset();
		}

	}


	/**
	 * Comparator used when adding new highlights.  This is done here instead
	 * of making <code>LineHighlightInfo</code> implement
	 * <code>Comparable</code> as correctly implementing the latter prevents
	 * two LHI's pointing to the same line from correctly being distinguished
	 * from one another.  See:
	 * https://github.com/bobbylight/RSyntaxTextArea/issues/161
	 */
	private static class LineHighlightInfoComparator
			implements Comparator<LineHighlightInfo> {

		@Override
		public int compare(LineHighlightInfo lhi1, LineHighlightInfo lhi2) {
			if (lhi1.getOffset() < lhi2.getOffset()) {
				return -1;
			}
			return lhi1.getOffset() == lhi2.getOffset() ? 0 : 1;
		}

	}


}
