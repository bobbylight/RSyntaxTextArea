/*
 * 02/11/2009
 *
 * LineNumberList.java - Renders line numbers in an RTextScrollPane.
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.View;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;


/**
 * The gutter is the component on the left-hand side of the text area that
 * displays optional information such as line numbers, icons (for bookmarks,
 * debugging breakpoints, error markers, etc.).
 *
 * @author Robert Futrell
 * @version 1.0
 */
class Gutter extends JComponent implements MouseInputListener {

	private RTextArea textArea;

	private int currentLine;	// The last line the caret was on.
	private int lastY = -1;		// Used to check if caret changes lines when line wrap is enabled.

	private int cellHeight;		// Height of a line number "cell" when word wrap is off.
	private int cellWidth;		// The width used for all line number cells.
	private int ascent;			// The ascent to use when painting line numbers.

	private int currentNumLines;

	private Color borderColor;

	private int mouseDragStartOffset;

	/**
	 * Listens for events from the current text area.
	 */
	private Listener l;

	/**
	 * Whether the line icons (bookmarks, errors markers, breakpoints, etc.)
	 * are shown.
	 */
	private boolean showLineIcons;

	/**
	 * Whether line numbers are visible.
	 */
	private boolean showLineNumbers;

	/**
	 * Used in {@link #paintComponent(Graphics)} to prevent reallocation on
	 * each paint.
	 */
	private Insets textAreaInsets;

	/**
	 * Used in {@link #paintComponent(Graphics)} to prevent reallocation on
	 * each paint.
	 */
	private Rectangle visibleRect;

	private List trackingIcons;

	private static final int RHS_BORDER_WIDTH	= 8;
	private static final int ICON_AREA_WIDTH	= 18;


	/**
	 * Constructs a new <code>LineNumberList</code> using default values for
	 * line number color (gray) and highlighting the current line.
	 *
	 * @param textArea The text component for which line numbers will be
	 *        displayed.
	 */
	public Gutter(RTextArea textArea) {
		this(textArea, Color.GRAY);
	}


	/**
	 * Constructs a new <code>LineNumberList</code>.
	 *
	 * @param textArea The text component for which line numbers will be
	 *        displayed.
	 * @param numberColor The color to use for the line numbers.
	 */
	public Gutter(RTextArea textArea, Color numberColor) {

		// Remember what text component we're keeping line numbers for.
		l = new Listener();
		setTextArea(textArea);

		if (numberColor!=null) {
			setForeground(numberColor);
		}
		else {
			setForeground(Color.GRAY);
		}
		borderColor = new Color(221, 221, 221);

		// Initialize currentLine; otherwise, the current line won't start
		// off as highlighted.
		currentLine = 0;

		visibleRect = new Rectangle(); // Must be initialized

		addMouseListener(this);
		addMouseMotionListener(this);

		setShowLineNumbers(true);
		setShowIcons(true);

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
	 */
	public Object addOffsetTrackingIcon(int offs, Icon icon)
										throws BadLocationException {
		Position pos = textArea.getDocument().createPosition(offs);
		TrackingIcon ti = new TrackingIcon(icon, pos);
		if (trackingIcons==null) {
			trackingIcons = new ArrayList(1); // Usually small
		}
		int index = Collections.binarySearch(trackingIcons, ti);
		if (index<0) {
			index = -(index+1);
		}
		trackingIcons.add(index, ti);
		return ti;
	}


	/**
	 * Overridden to set width of this component correctly when we are first
	 * displayed (as keying off of the RTextArea gives us (0,0) when it isn't
	 * yet displayed.
	 */
	public void addNotify() {
		super.addNotify();
		updateCellWidths();
		updateCellHeights();
	}


	/**
	 * Returns the color of the "border" line.
	 *
	 * @return The color.
	 * @see #setBorderColor(Color)
	 */
	public Color getBorderColor() {
		return borderColor;
	}


	/**
	 * Returns the bounds of a child view as a rectangle, since
	 * <code>View</code>s tend to use <code>Shape</code>.
	 *
	 * @param parent The parent view of the child whose bounds we're getting.
	 * @param line The index of the child view.
	 * @param editorRect Returned from the text area's
	 *        <code>getVisibleEditorRect</code> method.
	 * @return The child view's bounds.
	 */
	private static final Rectangle getChildViewBounds(View parent, int line,
										Rectangle editorRect) {
		Shape alloc = parent.getChildAllocation(line, editorRect);
		return alloc instanceof Rectangle ? (Rectangle)alloc :
										alloc.getBounds();
	}


	/**
	 * Returns the color to use to paint line numbers.
	 *
	 * @return The color used when painting line numbers.
	 * @see #setLineNumberColor
	 */
	public Color getLineNumberColor() {
		return getForeground();
	}


	/**
	 * {@inheritDoc}
	 */
	public Dimension getPreferredSize() {
		int h = textArea!=null ? textArea.getHeight() : 100; // Arbitrary
		return new Dimension(cellWidth, h);
	}


	/**
	 * Returns whether the icons are visible.
	 *
	 * @return Whether the icons are is visible.
	 * @see #setShowIcons(boolean)
	 */
	public boolean getShowIcons() {
		return showLineIcons;
	}


	/**
	 * Returns whether line numbers are visible.
	 *
	 * @return Whether line numbers are visible.
	 * @see #setShowLineNumbers(boolean)
	 */
	public boolean getShowLineNumbers() {
		return showLineNumbers;
	}


	public void mouseClicked(MouseEvent e) {
	}


	public void mouseDragged(MouseEvent e) {
		int pos = textArea.viewToModel(new Point(0, e.getY()));
		if (pos>=0) { // Not -1
			textArea.setCaretPosition(mouseDragStartOffset);
			textArea.moveCaretPosition(pos);
		}
	}


	public void mouseEntered(MouseEvent e) {
	}


	public void mouseExited(MouseEvent e) {
	}


	public void mouseMoved(MouseEvent e) {
	}


	public void mousePressed(MouseEvent e) {
		int pos = textArea.viewToModel(new Point(0, e.getY()));
		if (pos>=0) { // Not -1
			textArea.setCaretPosition(pos);
		}
		mouseDragStartOffset = pos;
	}


	public void mouseReleased(MouseEvent e) {
	}


	/**
	 * Paints the border line.
	 *
	 * @param g The graphics context.
	 * @param visibleRect The rectangle to render in.
	 * @param ltr Whether this component is left-to-right.
	 */
	private void paintBorderLine(Graphics g, Rectangle visibleRect,
							boolean ltr) {
		g.setColor(borderColor);
		int x = ltr ? (cellWidth-1) : 0;
		g.drawLine(x,visibleRect.y, x,visibleRect.y+visibleRect.height);
	}


	/**
	 * Paints this component.
	 *
	 * @param g The graphics context.
	 */
	protected void paintComponent(Graphics g) {

		if (textArea==null) {
			return;
		}

		visibleRect = g.getClipBounds(visibleRect);
		if (visibleRect==null) { // ???
			visibleRect = getVisibleRect();
		}
		//System.out.println("Gutter repainting: " + visibleRect);
		if (visibleRect==null) {
			return;
		}

		// Fill in the background the same color as the text component.
		g.setColor(getBackground());
		g.fillRect(0,visibleRect.y, cellWidth,visibleRect.height);

		Document doc = textArea.getDocument();
		Element root = doc.getDefaultRootElement();
		boolean ltr = getComponentOrientation().isLeftToRight();

		if (textArea.getLineWrap()) {
			paintWrappedLineNumbers(g, visibleRect, ltr);
			return;
		}

		// Get the first and last lines to paint.
		int topLine = visibleRect.y/cellHeight;
		int bottomLine = Math.min(topLine+visibleRect.height/cellHeight,
							root.getElementCount());

		// Get where to start painting (top of the row), and where to paint
		// the line number (drawString expects y==baseline).
		// We need to be "scrolled up" up just enough for the missing part of
		// the first line.
		int actualTopY = topLine*cellHeight;
		textAreaInsets = textArea.getInsets(textAreaInsets);
		if (textAreaInsets!=null) {
			actualTopY += textAreaInsets.top;
		}
		int y = actualTopY + ascent;

		// Highlight the current line's line number, if desired.
		if (textArea.getHighlightCurrentLine() && currentLine>=topLine &&
				currentLine<bottomLine) {
			g.setColor(textArea.getCurrentLineHighlightColor());
			g.fillRect(0,actualTopY+(currentLine-topLine)*cellHeight,
						cellWidth,cellHeight);
		}

		paintBorderLine(g, visibleRect, ltr);

		int rhs;
		if (ltr) {
			rhs = getWidth() - RHS_BORDER_WIDTH;
			if (showLineIcons) {
				rhs -= ICON_AREA_WIDTH;
			}
		}
		else { // rtl
			rhs = RHS_BORDER_WIDTH;
		}

		// Paint any icons, if they are enabled.  We assume there is
		// relatively small number of icons; we just iterate through them.
		if (showLineIcons && trackingIcons!=null) {
			int lastLine = bottomLine;
			for (int i=trackingIcons.size()-1; i>=0; i--) { // Last to first
				TrackingIcon ti = (TrackingIcon)trackingIcons.get(i);
				int offs = ti.getMarkedOffset();
				if (offs>=0 && offs<=doc.getLength()) {
					int line = root.getElementIndex(offs);
					if (line<=lastLine-1 && line>topLine-1) {
						Icon icon = ti.getIcon();
						if (icon!=null) {
							int y2 = y + (line-topLine)*cellHeight - ascent;
							y2 += (cellHeight-icon.getIconHeight())/2;
							ti.getIcon().paintIcon(this, g, rhs+2, y2);
							lastLine = line-1; // Paint only 1 icon per line
						}
					}
					else if (line<=topLine-1) {
						break;
					}
				}
			}
		}

		// Paint line numbers
		if (showLineNumbers) {
			g.setColor(getForeground());
			if (ltr) {
				FontMetrics metrics = g.getFontMetrics();
				for (int i=topLine+1; i<=bottomLine; i++) {
					String number = Integer.toString(i);
					int width = metrics.stringWidth(number);
					g.drawString(number, rhs-width,y);
					y += cellHeight;
				}
			}
			else { // rtl
				int x = RHS_BORDER_WIDTH;
				if (showLineIcons) {
					x += ICON_AREA_WIDTH;
				}
				for (int i=topLine+1; i<=bottomLine; i++) {
					String number = Integer.toString(i);
					g.drawString(number, x, y);
					y += cellHeight;
				}
			}
		}

	}


	/**
	 * Paints line numbers for text areas with line wrap enabled.
	 *
	 * @param g The graphics context.
	 * @param visibleRect The visible rectangle of these line numbers.
	 * @param ltr Whether the orientation is left-to-right.
	 */
	private void paintWrappedLineNumbers(Graphics g, Rectangle visibleRect,
										boolean ltr) {

		// The variables we use are as follows:
		// - visibleRect is the "visible" area of the text area; e.g.
		// [0,100, 300,100+(numLines*cellHeight)-1].
		// actualTop.y is the topmost-pixel in the first logical line we
		// paint.  Note that we may well not paint this part of the logical
		// line, as it may be broken into many physical lines, with the first
		// few physical lines scrolled past.  Note also that this is NOT the
		// visible rect of this line number list; this line number list has
		// visible rect == [0,0, insets.left-1,visibleRect.height-1].
		// - offset (<=0) is the y-coordinate at which we begin painting when
		// we begin painting with the first logical line.  This can be
		// negative, signifying that we've scrolled past the actual topmost
		// part of this line.

		// The algorithm is as follows:
		// - Get the starting y-coordinate at which to paint.  This may be
		//   above the first visible y-coordinate as we're in line-wrapping
		//   mode, but we always paint entire logical lines.
		// - Paint that line's line number and highlight, if appropriate.
		//   Increment y to be just below the are we just painted (i.e., the
		//   beginning of the next logical line's view area).
		// - Get the ending visual position for that line.  We can now loop
		//   back, paint this line, and continue until our y-coordinate is
		//   past the last visible y-value.

		// We avoid using modelToView/viewToModel where possible, as these
		// methods trigger a parsing of the line into syntax tokens, which is
		// costly.  It's cheaper to just grab the child views' bounds.

		// Some variables we'll be using.
		int width = getWidth();

		RTextAreaUI ui = (RTextAreaUI)textArea.getUI();
		View v = ui.getRootView(textArea).getView(0);
		boolean currentLineHighlighted = textArea.getHighlightCurrentLine();
		Document doc = textArea.getDocument();
		Element root = doc.getDefaultRootElement();
		int lineCount = root.getElementCount();
		int topPosition = textArea.viewToModel(
								new Point(visibleRect.x,visibleRect.y));
		int topLine = root.getElementIndex(topPosition);

		// Compute the y at which to begin painting text, taking into account
		// that 1 logical line => at least 1 physical line, so it may be that
		// y<0.  The computed y-value is the y-value of the top of the first
		// (possibly) partially-visible view.
		Rectangle visibleEditorRect = ui.getVisibleEditorRect();
		Rectangle r = Gutter.getChildViewBounds(v, topLine,
												visibleEditorRect);
		int y = r.y;

		int rhs;
		if (ltr) {
			rhs = width - RHS_BORDER_WIDTH;
			if (showLineIcons) {
				rhs -= ICON_AREA_WIDTH;
			}
		}
		else { // rtl
			rhs = RHS_BORDER_WIDTH;
		}
		int visibleBottom = y + visibleRect.height;
		FontMetrics metrics = g.getFontMetrics();

		// Get the first possibly visible icon index.
		int currentIcon = -1;
		if (showLineIcons && trackingIcons!=null) {
			for (int i=0; i<trackingIcons.size(); i++) {
				TrackingIcon icon = (TrackingIcon)trackingIcons.get(i);
				int offs = icon.getMarkedOffset();
				if (offs>=0 && offs<=doc.getLength()) {
					int line = root.getElementIndex(offs);
					if (line>=topLine) {
						currentIcon = i;
						break;
					}
				}
			}
		}

		// Keep painting lines until our y-coordinate is past the visible
		// end of the text area.
		g.setColor(getForeground());
		while (y < visibleBottom) {

			r = Gutter.getChildViewBounds(v, topLine, visibleEditorRect);
			int lineEndY = r.y+r.height;

			// Highlight the current line's line number, if desired.
			if (currentLineHighlighted && topLine==currentLine) {
				g.setColor(textArea.getCurrentLineHighlightColor());
				g.fillRect(0,y, width,lineEndY-y);
				g.setColor(getForeground());
			}

			// Paint the line number.
			String number = Integer.toString(topLine+1);
			if (ltr) {
				int strWidth = metrics.stringWidth(number);
				g.drawString(number, rhs-strWidth,y+ascent);
			}
			else {
				int x = RHS_BORDER_WIDTH;
				if (showLineIcons) {
					x += ICON_AREA_WIDTH;
				}
				g.drawString(number, x, y+ascent);
			}

			// Possibly paint an icon.
			if (currentIcon>-1) {
				// We want to paint the last icon added for this line.
				TrackingIcon toPaint = null;
				while (currentIcon<trackingIcons.size()) {
					TrackingIcon ti = (TrackingIcon)trackingIcons.get(currentIcon);
					int offs = ti.getMarkedOffset();
					if (offs>=0 && offs<=doc.getLength()) {
						int line = root.getElementIndex(offs);
						if (line==topLine) {
							toPaint = ti;
						}
						else if (line>topLine) {
							break;
						}
					}
					currentIcon++;
				}
				if (toPaint!=null) {
					Icon icon = toPaint.getIcon();
					if (icon!=null) {
						int y2 = y + (cellHeight-icon.getIconHeight())/2;
						icon.paintIcon(this, g, rhs+2, y2);
					}
				}
			}

			// The next possible y-coordinate is just after the last line
			// painted.
			y += r.height;

			// Update topLine (we're actually using it for our "current line"
			// variable now).
			topLine++;
			if (topLine>=lineCount)
				break;

		}

		paintBorderLine(g, visibleRect, ltr);

	}


	/**
	 * Removes the specified tracking icon.
	 *
	 * @param tag A tag for a tracking icon.
	 * @see #removeAllTrackingIcons()
	 * @see #addOffsetTrackingIcon(int, Icon)
	 */
	public void removeTrackingIcon(Object tag) {
		if (trackingIcons!=null && trackingIcons.remove(tag) && showLineIcons) {
			repaint();
		}
	}


	/**
	 * Removes all tracking icons.
	 *
	 * @see #removeTrackingIcon(Object)
	 * @see #addOffsetTrackingIcon(int, Icon)
	 */
	public void removeAllTrackingIcons() {
		if (trackingIcons!=null && trackingIcons.size()>0) {
			trackingIcons.clear();
			if (showLineIcons) {
				repaint();
			}
		}
	}


	/**
	 * Repaints a single line in this list.
	 *
	 * @param line The line to repaint.
	 */
	private void repaintLine(int line) {
		int y = textArea.getInsets().top;
		y += line*cellHeight;
		repaint(0,y, cellWidth,cellHeight);
	}


	/**
	 * Sets the font used to render the line numbers.
	 *
	 * @param font The <code>java.awt.Font</code> to use to to render the line
	 *        numbers.  If <code>null</code>, a 10-point monospaced font
	 *        will be used.
	 * @see #getFont()
	 */
	public void setFont(Font font) {
		if (font==null) {
			font = new Font("Monospaced", Font.PLAIN, 10);
		}
		super.setFont(font);
		repaint();
	}


	/**
	 * Sets the color for the "border" line.
	 *
	 * @param color The new color.
	 * @see #getBorderColor()
	 */
	public void setBorderColor(Color color) {
		borderColor = color;
		repaint();
	}


	/**
	 * Toggles whether icons (used for breakpoints, bookmarks, etc.) are
	 * enabled.
	 *
	 * @param show Whether the icons should be visible.
	 * @see #getShowIcons()
	 */
	public void setShowIcons(boolean show) {
		if (show!=showLineIcons) {
			showLineIcons = show;
			updateCellWidths(); // Will also cause a revalidate()
		}
	}


	/**
	 * Toggles whether line numbers are enabled.
	 *
	 * @param show Whether line numbers should be visible.
	 * @see #getShowLineNumbers()
	 */
	public void setShowLineNumbers(boolean show) {
		if (show!=showLineNumbers) {
			showLineNumbers = show;
			updateCellWidths(); // Will also cause a revalidate()
		}
	}


	/**
	 * Sets the color to use to paint line numbers.
	 *
	 * @param color The color to use when painting line numbers.
	 * @see #getLineNumberColor()
	 */
	public void setLineNumberColor(Color color) {
		setForeground(color);
		repaint();
	}


	/**
	 * Sets the text area being displayed.
	 *
	 * @param textArea The text area.
	 */
	public void setTextArea(RTextArea textArea) {

		if (this.textArea!=null) {
			l.uninstall(textArea);
		}

		this.textArea = textArea;
		Color bg = textArea==null ? Color.WHITE : textArea.getBackground();
		// textArea.getBackground() may also return null (image bg)
		setBackground(bg==null ? Color.WHITE : bg);

		if (textArea!=null) {
			l.install(textArea);
			updateCellHeights();
			updateCellWidths();
		}

	}


	/**
	 * Changes the height of the cells in the JList so that they are as tall as
	 * font. This function should be called whenever the user changes the Font
	 * of <code>textArea</code>.
	 */
	private void updateCellHeights() {
		if (textArea!=null) {
			cellHeight = textArea.getLineHeight();
			ascent = textArea.getMaxAscent();
		}
		else {
			cellHeight = 20; // Arbitrary number.
			ascent = 5; // Also arbitrary
		}
		repaint();
	}


	/**
	 * Changes the width of the cells in the JList so you can see every digit
	 * of each.
	 */
	private void updateCellWidths() {

		int oldCellWidth = cellWidth;
		cellWidth = RHS_BORDER_WIDTH;
		cellWidth += showLineIcons ? ICON_AREA_WIDTH : 0;

		// Adjust the amount of space the line numbers take up, if necessary.
		if (textArea!=null && showLineNumbers) {
			Font font = getFont();
			if (font!=null) {
				FontMetrics fontMetrics = getFontMetrics(font);
				int count = 0;
				int numLines = textArea.getLineCount();
				while (numLines >= 10) {
					numLines = numLines/10;
					count++;
				}
				cellWidth += fontMetrics.charWidth('9')*(count+2) + 5;
			}
		}

		if (cellWidth!=oldCellWidth) { // Always true
			revalidate();
		}

	}


	/**
	 * Listens for events in the text area we're interested in.
	 */
	private class Listener extends ComponentAdapter implements CaretListener,
						DocumentListener, PropertyChangeListener {

		public void caretUpdate(CaretEvent e) {

			int dot = textArea.getCaretPosition();

			// We separate the line wrap/no line wrap cases because word wrap
			// can make a single line from the model (document) be on multiple
			// lines on the screen (in the view); thus, we have to enhance the
			// logic for that case a bit - we check the actual y-coordinate of
			// the caret when line wrap is enabled.  For the no-line-wrap case,
			// getting the line number of the caret suffices.  This increases
			// efficiency in the no-line-wrap case.

			if (textArea.getLineWrap()==false) {
				int line = textArea.getDocument().getDefaultRootElement().
										getElementIndex(dot);
				if (currentLine!=line) {
					repaintLine(line);
					repaintLine(currentLine);
					currentLine = line;
				}
			}
			else { // lineWrap enabled; must check actual y position of caret
				try {
					int y = textArea.yForLineContaining(dot);
					if (y!=lastY) {
						lastY = y;
						currentLine = textArea.getDocument().
								getDefaultRootElement().getElementIndex(dot);
						repaint(); // *Could* be optimized...
					}
				} catch (BadLocationException ble) {
					ble.printStackTrace();
				}
			}

		}

		public void changedUpdate(DocumentEvent e) {}

		public void componentResized(java.awt.event.ComponentEvent e) {
			setPreferredSize(new Dimension(cellWidth,
								e.getComponent().getHeight()));
			revalidate();
		}

		public void insertUpdate(DocumentEvent e) {
			int newNumLines = textArea!=null ? textArea.getLineCount() : 0;
			if (newNumLines > currentNumLines) {
				// Adjust the amount of space the line numbers take up,
				// if necessary.
				if (newNumLines/10 > currentNumLines/10)
					updateCellWidths();
				currentNumLines = newNumLines;
				repaint();
			}
		}

		public void install(RTextArea textArea) {
			textArea.addCaretListener(this);
			textArea.addPropertyChangeListener(this);
			textArea.getDocument().addDocumentListener(this);
			textArea.addComponentListener(this);
		}

		public void propertyChange(PropertyChangeEvent e) {

			String name = e.getPropertyName();

			// If they changed the background color of the text area.
			if ("background".equals(name)) {
				Color bg = textArea.getBackground();
				setBackground(bg==null ? Color.WHITE : bg);
				repaint();
			}

			// If they changed the background to an image.
			else if ("background.image".equals(name)) {
				setBackground(Color.WHITE);
				repaint();
			}

			// If they change the text area's font, we need to update cell
			// heights to match the font's height.
			else if ("font".equals(name)) {
				updateCellHeights();
			}

			// If they change the current line highlight in any way...
			else if (RTextArea.HIGHLIGHT_CURRENT_LINE_PROPERTY.equals(name) ||
				RTextArea.CURRENT_LINE_HIGHLIGHT_COLOR_PROPERTY.equals(name)) {
				repaint();
			}

			// If they change the text area's syntax scheme (i.e., it is an
			// RSyntaxTextArea), update cell heights.
			else if (RSyntaxTextArea.SYNTAX_SCHEME_PROPERTY.equals(name)) {
				updateCellHeights();
			}

		}

		public void removeUpdate(DocumentEvent e) {
			int newNumLines = textArea.getLineCount();
			if (newNumLines < currentNumLines) { // Used to be <
				// Adjust amount of space the line numbers take up, if necessary.
				if (newNumLines/10 < currentNumLines/10)
					updateCellWidths();
				currentNumLines = newNumLines;
				repaint();
			}
		}

		public void uninstall(RTextArea textArea) {
			textArea.removeCaretListener(this);
			textArea.removePropertyChangeListener(this);
			textArea.getDocument().removeDocumentListener(this);
			textArea.removeComponentListener(this);
		}

	}


	static class TrackingIcon implements Comparable {

		private Icon icon;
		private Position pos;

		public TrackingIcon(Icon icon, Position pos) {
			this.icon = icon;
			this.pos = pos;
		}

		public int compareTo(Object o) {
			if (o instanceof TrackingIcon) {
				return pos.getOffset() - ((TrackingIcon)o).getMarkedOffset();
			}
			return -1;
		}

		public boolean equals(Object o) {
			return o==this;
		}

		public Icon getIcon() {
			return icon;
		}

		public int getMarkedOffset() {
			return pos.getOffset();
		}

		public int hashCode() {
			return icon.hashCode(); // FindBugs
		}

	}


}