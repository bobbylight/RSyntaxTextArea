/*
 * 02/17/2009
 *
 * Gutter.java - Manages line numbers, icons, etc. on the left-hand side of
 * an RTextArea.
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
package org.fife.ui.rtextarea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;


/**
 * The gutter is the component on the left-hand side of the text area that
 * displays optional information such as line numbers and icons (for bookmarks,
 * debugging breakpoints, error markers, etc.).
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class Gutter extends JComponent {

	/**
	 * The text area.
	 */
	private RTextArea textArea;

	/**
	 * Renders line numbers.
	 */
	private LineNumberList lineNumberList;

	/**
	 * Renders bookmark icons, breakpoints, error icons, etc.
	 */
	private IconRowHeader iconArea;

	/**
	 * Listens for events in our text area.
	 */
	private TextAreaListener listener;


	/**
	 * Constructor.
	 *
	 * @param textArea The parent text area.
	 */
	public Gutter(RTextArea textArea) {
		this.textArea = textArea;
		listener = new TextAreaListener();
		textArea.addComponentListener(listener);
		lineNumberList = new LineNumberList(textArea);
		iconArea = new IconRowHeader(textArea);
		setLayout(new BorderLayout());
		add(lineNumberList);
		//add(iconArea, BorderLayout.LINE_START);
		setBorder(new GutterBorder(0, 0, 0, 1)); // Assume ltr
	}


	/**
	 * Adds an icon that tracks an offset in the document, and is displayed
	 * adjacent to the line numbers.  This is useful for marking things such
	 * as source code errors.
	 *
	 * @param line The line to track (zero-based).
	 * @param icon The icon to display.  This should be small (say 16x16).
	 * @return A tag for this icon.
	 * @throws BadLocationException If <code>offs</code> is an invalid offset
	 *         into the text area.
	 * @see #addOffsetTrackingIcon(int, Icon)
	 * @see #removeTrackingIcon(GutterIconInfo)
	 */
	public GutterIconInfo addLineTrackingIcon(int line, Icon icon)
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
	 * @see #addLineTrackingIcon(int, Icon)
	 * @see #removeTrackingIcon(GutterIconInfo)
	 */
	public GutterIconInfo addOffsetTrackingIcon(int offs, Icon icon)
												throws BadLocationException {
		return iconArea.addOffsetTrackingIcon(offs, icon);
	}


	/**
	 * Returns the icon to use for bookmarks.
	 *
	 * @return The icon to use for bookmarks.  If this is <code>null</code>,
	 *         bookmarking is effectively disabled.
	 * @see #setBookmarkIcon(Icon)
	 * @see #isBookmarkingEnabled()
	 */
	public Icon getBookmarkIcon() {
		return iconArea.getBookmarkIcon();
	}


	/**
	 * Returns the bookmarks known to this gutter.
	 *
	 * @return The bookmarks.  If there are no bookmarks, an empty array is
	 *         returned.
	 */
	public GutterIconInfo[] getBookmarks() {
		return iconArea.getBookmarks();
	}


	/**
	 * Returns the color of the "border" line.
	 *
	 * @return The color.
	 * @see #setBorderColor(Color)
	 */
	public Color getBorderColor() {
		return ((GutterBorder)getBorder()).getColor();
	}


	/**
	 * Returns the color to use to paint line numbers.
	 *
	 * @return The color used when painting line numbers.
	 * @see #setLineNumberColor(Color)
	 */
	public Color getLineNumberColor() {
		return lineNumberList.getForeground();
	}


	/**
	 * Returns the font used for line numbers.
	 *
	 * @return The font used for line numbers.
	 * @see #setLineNumberFont(Font)
	 */
	public Font getLineNumberFont() {
		return lineNumberList.getFont();
	}


	/**
	 * Returns <code>true</code> if the line numbers are enabled and visible.
	 *
	 * @return Whether or not line numbers are visible.
	 * @see #setLineNumbersEnabled(boolean)
	 */
	public boolean getLineNumbersEnabled() {
		for (int i=0; i<getComponentCount(); i++) {
			if (getComponent(i)==lineNumberList) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Returns the tracking icons at the specified view position.
	 *
	 * @param p The view position.
	 * @return The tracking icons at that position.  If there are no tracking
	 *         icons there, this will be an empty array.
	 * @throws BadLocationException If <code>p</code> is invalid.
	 */
	public Object[] getTrackingIcons(Point p) throws BadLocationException {
		int offs = textArea.viewToModel(new Point(0, p.y));
		int line = textArea.getLineOfOffset(offs);
		return iconArea.getTrackingIcons(line);
	}


	/**
	 * Returns whether bookmarking is enabled.
	 *
	 * @return Whether bookmarking is enabled.
	 * @see #setBookmarkingEnabled(boolean)
	 */
	public boolean isBookmarkingEnabled() {
		return iconArea.isBookmarkingEnabled();
	}


	/**
	 * Returns whether the icon row header is enabled.
	 *
	 * @return Whether the icon row header is enabled.
	 * @see #setIconRowHeaderEnabled(boolean)
	 */
	public boolean isIconRowHeaderEnabled() {
		for (int i=0; i<getComponentCount(); i++) {
			if (getComponent(i)==iconArea) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Removes the specified tracking icon.
	 *
	 * @param tag A tag for a tracking icon.
	 * @see #removeAllTrackingIcons()
	 * @see #addOffsetTrackingIcon(int, Icon)
	 */
	public void removeTrackingIcon(GutterIconInfo tag) {
		iconArea.removeTrackingIcon(tag);
	}


	/**
	 * Removes all tracking icons.
	 *
	 * @see #removeTrackingIcon(GutterIconInfo)
	 * @see #addOffsetTrackingIcon(int, Icon)
	 */
	public void removeAllTrackingIcons() {
		iconArea.removeAllTrackingIcons();
	}


	/**
	 * Sets the icon to use for bookmarks.
	 *
	 * @param icon The new bookmark icon.  If this is <code>null</code>,
	 *        bookmarking is effectively disabled.
	 * @see #getBookmarkIcon()
	 * @see #isBookmarkingEnabled()
	 */
	public void setBookmarkIcon(Icon icon) {
		iconArea.setBookmarkIcon(icon);
	}


	/**
	 * Sets whether bookmarking is enabled.  Note that a bookmarking icon
	 * must be set via {@link #setBookmarkIcon(Icon)} before bookmarks are
	 * truly enabled.
	 *
	 * @param enabled Whether bookmarking is enabled.
	 * @see #isBookmarkingEnabled()
	 * @see #setBookmarkIcon(Icon)
	 */
	public void setBookmarkingEnabled(boolean enabled) {
		iconArea.setBookmarkingEnabled(enabled);
		if (enabled && !isIconRowHeaderEnabled()) {
			setIconRowHeaderEnabled(true);
		}
	}


	/**
	 * Sets the color for the "border" line.
	 *
	 * @param color The new color.
	 * @see #getBorderColor()
	 */
	public void setBorderColor(Color color) {
		((GutterBorder)getBorder()).setColor(color);
		repaint();
	}


	/**
	 * {@inheritDoc}
	 */
	public void setComponentOrientation(ComponentOrientation o) {
		if (o.isLeftToRight()) {
			setBorder(new GutterBorder(0, 0, 0, 1));
		}
		else {
			setBorder(new GutterBorder(0, 1, 0, 0));
		}
		super.setComponentOrientation(o);
	}


	/**
	 * Toggles whether the icon row header (used for breakpoints, bookmarks,
	 * etc.) is enabled.
	 *
	 * @param enabled Whether the icon row header is enabled.
	 * @see #isIconRowHeaderEnabled()
	 */
	public void setIconRowHeaderEnabled(boolean enabled) {
		if (enabled) {
			add(iconArea, BorderLayout.LINE_START);
		}
		else {
			remove(iconArea);
		}
		revalidate();
	}


	/**
	 * Sets the color to use to paint line numbers.
	 *
	 * @param color The color to use when painting line numbers.
	 * @see #getLineNumberColor()
	 */
	public void setLineNumberColor(Color color) {
		lineNumberList.setForeground(color);
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
		lineNumberList.setFont(font);
	}


	/**
	 * Toggles whether or not line numbers are visible.
	 *
	 * @param enabled Whether or not line numbers should be visible.
	 * @see #getLineNumbersEnabled()
	 */
	public void setLineNumbersEnabled(boolean enabled) {
		if (enabled) {
			add(lineNumberList);
		}
		else {
			remove(lineNumberList);
		}
		revalidate();
	}


	/**
	 * Sets the text area being displayed.  This will clear any tracking
	 * icons currently displayed.
	 *
	 * @param textArea The text area.
	 */
	void setTextArea(RTextArea textArea) {
		if (this.textArea!=null) {
			this.textArea.removeComponentListener(listener);
		}
		textArea.addComponentListener(listener);
		lineNumberList.setTextArea(textArea);
		iconArea.setTextArea(textArea);
		this.textArea = textArea;
	}


	/**
	 * Programatically toggles whether there is a bookmark for the specified
	 * line.  If bookmarking is not enabled, this method does nothing.
	 *
	 * @param line The line.
	 * @return Whether a bookmark is now at the specified line.
	 * @throws BadLocationException If <code>line</code> is an invalid line
	 *         number in the text area.
	 */
	public boolean toggleBookmark(int line) throws BadLocationException {
		return iconArea.toggleBookmark(line);
	}


	/**
	 * The border used by the gutter.
	 */
	private static class GutterBorder extends EmptyBorder {

		private Color color;

		public GutterBorder(int top, int left, int bottom, int right) {
			super(top, left, bottom, right);
			color = new Color(221, 221, 221);
		}

		public Color getColor() {
			return color;
		}

		public void paintBorder(Component c, Graphics g, int x, int y,
								int width, int height) {
			g.setColor(color);
			if (left==1) {
				g.drawLine(0,0, 0,height);
			}
			else {
				g.drawLine(width-1,0, width-1,height);
			}
		}

		public void setColor(Color color) {
			this.color = color;
		}

	}


	/**
	 * Listens for the text area resizing.
	 */
	/*
	 * This is necessary to keep child components the same height as the text
	 * area.  The worse case is when the user toggles word-wrap and it changes
	 * the height of the text area. In that case, if we listen for the
	 * "lineWrap" property change, we get notified BEFORE the text area
	 * decides on its new size, thus we cannot resize properly.  We listen
	 * instead for ComponentEvents so we change size after the text area has
	 * resized.
	 */
	private class TextAreaListener extends ComponentAdapter {

		public void componentResized(java.awt.event.ComponentEvent e) {
			revalidate();
		}


	}


}