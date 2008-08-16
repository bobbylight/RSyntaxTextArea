/*
 * 11/14/2003
 *
 * RTextScrollPane.java - A JScrollPane that will only accept RTextAreas
 * so that it can display line numbers.
 * Copyright (C) 2003 Robert Futrell
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
package org.fife.ui.rtextarea;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * An extension of <code>org.fife.ui.RScrollPane</code> that will only take
 * <code>RTextArea</code>s for its view.  This class has the ability to show
 * line numbers for its text component view.<p>
 *
 * NOTE:  This class has two different implementations of line numbers,
 * <code>LineNumberList</code> and <code>LineNumberBorder</code>.
 * Currently, <code>LineNumberBorder</code> is used as it is much faster;
 * however, <code>LineNumberList</code> is more "object-oriented" and
 * would allow for more user-interaction with the line numbers...
 *
 * @author Robert Futrell
 * @version 0.5
 */
public class RTextScrollPane extends JScrollPane {

	public RTextArea textArea;

	// NOTE:  Change which line numbering scheme you use by changing the
	// boolean below.
	private LineNumberList lineNumberList;		// More OO-ish.
	private LineNumberBorder lineNumberBorder;	// Faster.
	private static final boolean usingLineNumberList = false;
	private boolean lineNumbersEnabled;


	/**
	 * Creates a scroll pane with preferred size (width, height).  A default
	 * value will be used for line number color (gray), and the current
	 * line's line number will be highlighted.
	 *
	 * @param width The preferred width of <code>area</code>.
	 * @param height The preferred height of <code>area</code>.
	 * @param area The text area this scroll pane will contain.
	 * @param lineNumbersEnabled Whether line numbers are initially enabled.
	 */
	public RTextScrollPane(int width, int height, RTextArea area,
						boolean lineNumbersEnabled) {
		this(width, height, area, lineNumbersEnabled, Color.GRAY);
	}


	/**
	 * Creates a scroll pane with preferred size (width, height).
	 *
	 * @param width The preferred width of <code>area</code>.
	 * @param height The preferred height of <code>area</code>.
	 * @param area The text area this scroll pane will contain.
	 * @param lineNumbersEnabled Whether line numbers are initially enabled.
	 * @param lineNumberColor The color to use for line numbers.
	 */
	public RTextScrollPane(int width, int height, RTextArea area,
					boolean lineNumbersEnabled, Color lineNumberColor) {

		super(area);
		setPreferredSize(new Dimension(width, height));

		// Create the text area and set it inside this scrollbar area.
		textArea = area;

		// Create the line number list for this document.
		if (usingLineNumberList) {
			lineNumberList = new LineNumberList(textArea, lineNumberColor);
			lineNumberList.setFont(new Font("Monospaced", Font.PLAIN, 12));
		}
		else {
			enableEvents(AWTEvent.MOUSE_EVENT_MASK);
			lineNumberBorder = new LineNumberBorder(this, textArea,
											lineNumberColor);
			lineNumberBorder.setFont(new Font("Monospaced", Font.PLAIN, 12));
		}
		setLineNumbersEnabled(lineNumbersEnabled);

		// Set miscellaneous properties.
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	}


	/**
	 * Returns <code>true</code> if the line numbers are enabled and visible.
	 *
	 * @return Whether or not line numbers are visible.
	 * @see #setLineNumbersEnabled(boolean)
	 */
	public boolean areLineNumbersEnabled() {
		return lineNumbersEnabled;
	}


	/**
	 * This method is overridden so that if the user clicks in the line
	 * number border, the caret is moved.<p>
	 *
	 * This method will ONLY work if LineNumberBorder is used
	 * (not LineNumberList).
	 *
	 * @param e The mouse event.
	 */
	protected void processMouseEvent(MouseEvent e) {
		if (e.getID()==MouseEvent.MOUSE_CLICKED) {
			int y = getViewport().getViewPosition().y + e.getY();
			int pos = textArea.viewToModel(new Point(0, y));
			textArea.setCaretPosition(pos);
		}
		super.processMouseEvent(e);
	}


	/**
	 * Toggles whether or not line numbers are visible.
	 *
	 * @param enabled Whether or not line numbers should be visible.
	 * @see #areLineNumbersEnabled()
	 */
	public void setLineNumbersEnabled(boolean enabled) {
		if (enabled!=this.lineNumbersEnabled) {
			this.lineNumbersEnabled = enabled;
			if (usingLineNumberList) {
				if (enabled) {
					lineNumberList.updateCellWidths();
					JViewport viewport = new JViewport() {
							public void setViewPosition(Point p) {
								Component c = getView();
								if (c!=null)
									c.repaint();
								//getView().setLocation(-p.x, -p.y);
								//fireStateChanged();
							}
						};
					viewport.setView(lineNumberList);
					setRowHeader(viewport);
					//setRowHeaderView(lineNumberList);
				}
				else {
					setRowHeaderView(null);
				}
			}
			else { // lineNumberBorder
				setViewportBorder(enabled ? lineNumberBorder : null);
				revalidate();
			}
		}
	}


}