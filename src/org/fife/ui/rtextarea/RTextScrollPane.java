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
import javax.swing.*;
import javax.swing.text.BadLocationException;


/**
 * An extension of <code>javax.swing.JScrollPane</code> that will only take
 * <code>RTextArea</code>s for its view.  This class has the ability to show
 * line numbers for its text component view, as well as per-line icons (for
 * bookmarks, debugging breakpoints, error markers, etc.).
 *
 * @author Robert Futrell
 * @version 0.8
 */
public class RTextScrollPane extends JScrollPane {

	public RTextArea textArea;

	private Gutter gutter;


	/**
	 * Constructor.  If you use this constructor, you must call
	 * {@link #setViewportView(Component)} and pass in an {@link RTextArea}
	 * for this scroll pane to render line numbers properly.
	 */
	public RTextScrollPane() {
		this(300, 300, null, true);
	}


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

		// Create the text area and set it inside this scroll bar area.
		textArea = area;

		// Create the line number list for this document.
		Font defaultFont = new Font("Monospaced", Font.PLAIN, 12);
		gutter = new Gutter(textArea, lineNumberColor);
		gutter.setFont(defaultFont);
		setLineNumbersEnabled(lineNumbersEnabled);

		// Set miscellaneous properties.
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	}


	/**
	 * Adds an icon that tracks a given line in the document, and is displayed
	 * adjacent to the line numbers.  This is useful for marking lines for
	 * bookmarks, debugging breakpoints, etc.
	 *
	 * @param line The line to track.
	 * @param icon The icon to display.  This should be small (say 16x16).
	 * @return A tag for this icon.
	 * @throws BadLocationException If <code>line</code> is an invalid line
	 *         number for the text area.
	 * @see #removeTrackingIcon(Object)
	 * @see #addOffsetTrackingIcon(int, Icon)
	 */
	public Object addLineTrackingIcon(int line, Icon icon)
										throws BadLocationException {
		int offs = textArea.getLineStartOffset(line);
		return addOffsetTrackingIcon(offs, icon);
	}


	/**
	 * Adds an icon that tracks an offset in the document, and is displayed
	 * adjacent to the line numbers.  This is useful for marking things such
	 * as source code errors.
	 *
	 * @param offs The offset to track.
	 * @param icon The icon to display.  This should be small (say 16x16).
	 * @return A tag for this icon.
	 * @throws BadLocationException If <code>offs</code> is an invalid offset
	 *         into the text area.
	 * @see #removeTrackingIcon(Object)
	 * @see #addLineTrackingIcon(int, Icon)
	 */
	public Object addOffsetTrackingIcon(int offs, Icon icon)
										throws BadLocationException {
		return gutter.addOffsetTrackingIcon(offs, icon);
	}


	/**
	 * Ensures the gutter is visible if it's showing anything.
	 */
	private void checkGutterVisibility() {
		if (gutter.getShowLineNumbers() || gutter.getShowIcons()) {
			if (getRowHeader()==null || getRowHeader().getView()==null) {
				setRowHeaderView(gutter);
			}
		}
		else {
			setRowHeaderView(null);
		}
	}


	/**
	 * Returns the color of the "border" line.
	 *
	 * @return The color.
	 * @see #setBorderColor(Color)
	 */
	public Color getBorderColor() {
		return gutter.getBorderColor();
	}


	/**
	 * Returns the color to use to paint line numbers.
	 *
	 * @return The color used when painting line numbers.
	 * @see #setLineNumberColor
	 */
	public Color getLineNumberColor() {
		return gutter.getLineNumberColor();
	}


	/**
	 * Returns the font being used to render line numbers.
	 *
	 * @return The line number font.
	 * @see #setLineNumberFont(Font)
	 */
	public Font getLineNumberFont() {
		return gutter.getFont();
	}


	/**
	 * Returns <code>true</code> if the line numbers are enabled and visible.
	 *
	 * @return Whether or not line numbers are visible.
	 * @see #setLineNumbersEnabled(boolean)
	 */
	public boolean getLineNumbersEnabled() {
		return gutter.getShowLineNumbers();
	}


	/**
	 * Returns whether the icon row header is enabled.
	 *
	 * @return Whether the icon row header is enabled.
	 * @see #setIconRowHeaderEnabled(boolean)
	 */
	public boolean isIconRowHeaderEnabled() {
		return gutter.getShowIcons();
	}


	/**
	 * Removes the specified tracking icon.
	 *
	 * @param tag A tag for a tracking icon.
	 * @see #removeAllTrackingIcons()
	 * @see #addLineTrackingIcon(int, Icon)
	 * @see #addOffsetTrackingIcon(int, Icon)
	 */
	public void removeTrackingIcon(Object tag) {
		gutter.removeTrackingIcon(tag);
	}


	/**
	 * Removes all tracking icons.
	 *
	 * @see #removeTrackingIcon(Object)
	 * @see #addLineTrackingIcon(int, Icon)
	 * @see #addOffsetTrackingIcon(int, Icon)
	 */
	public void removeAllTrackingIcons() {
		gutter.removeAllTrackingIcons();
	}


	/**
	 * Sets the color for the "border" line.
	 *
	 * @param color The new color.
	 * @see #getBorderColor()
	 */
	public void setBorderColor(Color color) {
		gutter.setBorderColor(color);
	}


	/**
	 * Toggles whether the icon row header (used for breakpoints, bookmarks,
	 * etc.) is enabled.
	 *
	 * @param enabled Whether the icon row header is enabled.
	 * @see #isIconRowHeaderEnabled()
	 */
	public void setIconRowHeaderEnabled(boolean enabled) {
		gutter.setShowIcons(enabled);
		checkGutterVisibility();
	}


	/**
	 * Sets the color to use to paint line numbers.
	 *
	 * @param color The color to use when painting line numbers.
	 * @see #getLineNumberColor()
	 */
	public void setLineNumberColor(Color color) {
		gutter.setLineNumberColor(color);
	}


	/**
	 * Sets the font used for line numbers.
	 *
	 * @param font The font to use.  This cannot be <code>null</code>.
	 * @see #getLineNumberFont()
	 */
	public void setLineNumberFont(Font font) {
		if (font==null) {
			throw new IllegalArgumentException("font cannot be null");
		}
		gutter.setFont(font);
	}


	/**
	 * Toggles whether or not line numbers are visible.
	 *
	 * @param enabled Whether or not line numbers should be visible.
	 * @see #getLineNumbersEnabled()
	 */
	public void setLineNumbersEnabled(boolean enabled) {
		gutter.setShowLineNumbers(enabled);
		checkGutterVisibility();
	}


	/**
	 * Sets the view for this scroll pane.  This must be an {@link RTextArea}.
	 *
	 * @param view The new view.
	 */
	public void setViewportView(Component view) {
		if (!(view instanceof RTextArea)) {
			throw new IllegalArgumentException("view must be an RTextArea");
		}
		super.setViewportView(view);
		textArea = (RTextArea)view;
		if (gutter!=null) {
			gutter.setTextArea(textArea);
		}
	}


}