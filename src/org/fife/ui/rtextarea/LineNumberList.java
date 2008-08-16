/*
 * 11/14/2003
 *
 * LineNumberList.java - Line numbers for a text area contained
 * in an RTextScrollPane.
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;


/**
 * Used by <code>RTextScrollPane</code> to display line numbers for a text
 * area.  This component is capable of displaying line numbers for any
 * <code>RTextArea</code>, with line wrap enabled or disabled.  You can also
 * choose the line number font, color, and choose whether or not to "highlight"
 * the current line number.<p>
 *
 * NOTE:  To speed things up a little, this component does not "scroll."  It
 * needs to be added to an <code>RTextScrollPane</code> inside a special
 * viewport, like so:<p>
 *
 * <pre>
 *	JViewport viewport = new JViewport() {
 *			public void setViewPosition(java.awt.Point p) {
 *				Component c = getView();
 *				if (c!=null)
 *					c.repaint();
 *			}
 *		};
 *	viewport.setView(lineNumberList);
 *	setRowHeader(viewport);
 * </pre>
 *
 * This needs to be done because this component figures out how to paint itself
 * from the current scroll-state of the text area, so there is no need to scroll
 * this guy himself.<p>
 *
 * This class is probably going to be replaced by
 * {@link org.fife.ui.LineNumberBorder}, as not only does that class not need
 * to be scrolled, but he is also not a <code>JComponent</code> (less memory)
 * and he doesn't require the additional overhead of a <code>JViewport</code>
 * (slower).
 *
 * @author Robert Futrell
 * @version 0.5
 */
class LineNumberList extends JComponent implements CaretListener,
							DocumentListener, PropertyChangeListener {

	private static final int MIN_CELL_WIDTH		= 24;
	private static final int RHS_BORDER_WIDTH	= 8;

	private RTextArea textArea;

	private int currentLine;	// The last line the caret was on.
	private int lastY = -1;		// Used to check if caret changes lines when line wrap is enabled.

	private int cellHeight;		// The height of a line number "cell" when word wrap is off.
	private int cellWidth;		// The width used for all line number cells.
	private int ascent;			// The ascent to use when painting line numbers.

	private int currentNumLines;


	/**
	 * Constructs a new <code>LineNumberList</code> using default values for
	 * line number color (gray) and highlighting the current line.
	 *
	 * @param textArea The text component for which line numbers will be
	 *        displayed.
	 */
	public LineNumberList(RTextArea textArea) {
		this(textArea, new Color(128,128,128));
	}


	/**
	 * Constructs a new <code>LineNumberList</code>.
	 *
	 * @param textArea The text component for which line numbers will be
	 *        displayed.
	 * @param numberColor The color to use for the line numbers.
	 */
	public LineNumberList(RTextArea textArea, Color numberColor) {

		// Remember what text component we're keeping line numbers for.
		this.textArea = textArea;

		if (numberColor!=null)
			setForeground(numberColor);
		else
			setForeground(new Color(128,128,128));
		Color bg = textArea.getBackground();
		setBackground(bg==null ? Color.WHITE : bg);

		textArea.addCaretListener(this);
		textArea.addPropertyChangeListener(this);
		textArea.getDocument().addDocumentListener(this);

		// Initialize currentLine; otherwise, the current line won't start
		// off as highlighted.
		currentLine = 1;

		// Have the line number space be just enough space for '1' through '9'.
		updateCellHeights();
		updateCellWidths();

	}


	/**
	 * Called whenever the caret changes position; highlight the correct line
	 * number.
	 */
	public void caretUpdate(CaretEvent e) {

		int caretPosition = textArea.getCaretPosition();

		// We separate the line wrap/no line wrap cases because word wrap can
		// make a single line from the model (document) be on multiple lines
		// on the screen (in the view); thus, we have to enhance the logic for
		// that case a bit - we check the actual y-coordinate of the caret
		// when line wrap is enabled.  For the no-line-wrap case, getting the
		// line number of the caret suffices.  This increases efficiency in
		// the no-line-wrap case.

		if (textArea.getLineWrap()==false) {
			int line = textArea.getDocument().getDefaultRootElement().
									getElementIndex(caretPosition) + 1;
			if (currentLine!=line) {
				currentLine = line;
				repaint();
			}
		}
		else { // lineWrap is enabled; must check actual y-coord. of caret.
			try {
				int y = textArea.modelToView(caretPosition).y;
				if (y!=lastY) {
					lastY = y;
					currentLine = textArea.getDocument().
									getDefaultRootElement().
									getElementIndex(caretPosition) + 1;
					repaint();
				}
			} catch (BadLocationException ble) {
				ble.printStackTrace();
			}
		}

	}


	public void changedUpdate(DocumentEvent e) {}


	/**
	 * Returns the color to use to paint line numbers.
	 *
	 * @return The color used when painting line numbers.
	 * @see #setLineNumberColor
	 */
	public Color getLineNumberColor() {
		return getForeground();
	}


	public Dimension getPreferredSize() {
		return new Dimension(cellWidth, textArea.getHeight());
	}


	/**
	 * Returns the length of a string if it is drawn with the specified
	 * graphics context.  This method assumes that there are NO tabs in
	 * the string.<br><br>
	 *
	 * NOTE:  This is basically ripped off from
	 * <code>javax.swing.text.Utilities</code>, but slightly optimized for our
	 * situation.
	 *
	 * @param text The text to be painted.
	 * @param metrics The metrics with which to do the calculating.
	 * @return The width of the string when painted.
	 */
	public static final int getTextWidth(String text, FontMetrics metrics) {
		int width = 0;
		int end = text.length();
		for (int i=0; i<end; i++)
			width += metrics.charWidth(text.charAt(i));
		return width;
	}


	/**
	 * Called whenever a character is input (key is typed) in the text document
	 * we're line-numbering.
	 */
	public void insertUpdate(DocumentEvent e) {

		int newNumLines = textArea.getDocument().getDefaultRootElement().
												getElementCount();
		if (newNumLines > currentNumLines) {
			// Adjust the amount of space the line numbers take up,
			// if necessary.
			if (newNumLines/10 > currentNumLines/10)
				updateCellWidths();
			currentNumLines = newNumLines;
		}

	}


	// Returns the Component used as the JList cell.
	public void paint(Graphics g) {

		Element root = textArea.getDocument().getDefaultRootElement();
		Rectangle visibleRect = textArea.getVisibleRect();

		if (visibleRect==null)
			return;

		// Fill in the background the same color as the text component.
		g.setColor(getBackground());
		g.fillRect(0,0, cellWidth,visibleRect.height);

		if (textArea.getLineWrap()==true) {
			paintWrappedLineNumbers(g, root, visibleRect);
			return;
		}

		// Get the first and last lines to paint.
		int topLine = visibleRect.y/cellHeight + 1;
		int bottomLine = Math.min(topLine+visibleRect.height/cellHeight,
							root.getElementCount()) + 1;

		// Get where to start painting (top of the row), and where to paint
		// the line number (drawString expects y==baseline).
		// We need to be "scrolled up" up just enough for the missing part of
		// the first line.
		int actualTopY = -(visibleRect.y%cellHeight);
		Insets textAreaInsets = textArea.getInsets();
		if (textAreaInsets!=null) {
			actualTopY += textAreaInsets.top;
		}
		int y = actualTopY + ascent;

		// Highlight the current line's line number, if desired.
		if (textArea.isCurrentLineHighlightEnabled() && currentLine>=topLine &&
				currentLine<bottomLine) {
			g.setColor(textArea.getCurrentLineHighlightColor());
			g.fillRect(0,actualTopY+(currentLine-topLine)*cellHeight, cellWidth,cellHeight);
		}

		// Paint the "border" line.
		g.setColor(Color.BLACK);
		g.drawLine(cellWidth-4,0, cellWidth-4,visibleRect.height);

		g.setColor(getForeground());
		FontMetrics metrics = g.getFontMetrics();
		int rhs = getBounds().width - RHS_BORDER_WIDTH;
		for (int i=topLine; i<bottomLine; i++) {
			String number = Integer.toString(i);
			int width = getTextWidth(number, metrics);
			g.drawString(number, rhs-width,y);
			y += cellHeight;
		}

	}


	/**
	 * Paints line numbers for text areas with line wrap enabled.
	 *
	 * @param g The graphics context.
	 * @param root The root element of the document being painted.
	 * @param visibleRect The visible rectangle of the text area.
	 */
	/*
	 * FIXME:  This method isn't as efficient as the version in
	 * LineNumberBorder, as it uses modelToView() a lot.  Fix it to be like
	 * LineNumberBorder's version if you ever use this again.
	 */
	public void paintWrappedLineNumbers(Graphics g, Element root,
									Rectangle visibleRect) {

		// The variables we use are as follows:
		// visibleRect is the "visible" area of the text area; e.g.
		// [0,100, 300,100+(numLines*cellHeight)-1].
		// actualTop.y is the topmost-pixel in the first logical line we paint.
		// Note that we may well not paint this part of the logical line, as it
		// may be broken into many physical lines, with the first few physical
		// lines scrolled past.  Note also that this is NOT the visible rect of
		// this line number list; this line number list has visible rect ==
		// [0,0, cellWidth-1,visibleRect.height-1].
		// offset (<=0) is the y-coordinate at which we begin painting when we
		// begin painting with the first logical line.  This can be negative
		// signifying that we've scrolled past the actual topmost part of this line.
		// The algorithm is as follows:
		// - Get the starting y-coordinate at which to paint.  This may be above the
		//   first visible y-coordinate as we're in line-wrapping mode, but we
		//   always paint entire logical lines.
		// - Paint that line's line number and highlight, if appropriate.  Increment
		//   y to be just below the are we just painted (i.e., the beginning of the
		//   next logical line's view area).
		// - Get the ending visual position for that line.  We can now loop back,
		//   paint this line, and continue until our y-coordinate is past the last
		//   visible y-value.

		int lineCount = root.getElementCount();
		int topPosition = textArea.viewToModel(new Point(visibleRect.x,visibleRect.y));
		int topLine = root.getElementIndex(topPosition);
		Element line = root.getElement(topLine);
		int topLineStartOffset = line.getStartOffset();
		int topLineEndOffset = line.getEndOffset();


		// Compute the y at which to begin painting text, taking into account scrolling.
		Rectangle actualTop = null;
		try {
			// Could be (0,-5), for example.
			actualTop = textArea.modelToView(topLineStartOffset);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// How much "higher" we are than the actual viewport (negative value).
		int offset = actualTop.y - visibleRect.y;
		int y = offset;			// The y-coordinate at which we're painting.

		g.setColor(getForeground());
		FontMetrics metrics = g.getFontMetrics();
		int rhs = getBounds().width - RHS_BORDER_WIDTH;
		int visibleBottom = visibleRect.height;

		// Keep painting lines until our y-coordinate is passed the visible end of
		// the text area.
		while (y < visibleBottom) {

			// Compute the next y-value at which to paint a line number.
			// This is also needed to know how much to highlight when
			// highlighting the current line number.
			Rectangle lineEndViewPos = null;
			try {
				lineEndViewPos = textArea.modelToView(topLineEndOffset - 1);
				lineEndViewPos.y -= visibleRect.y;	// Take into account we always start at y==0.
			} catch (BadLocationException ble) {
				ble.printStackTrace();
				break;
			}

			// Highlight the current line's line number, if desired.
			if (textArea.isCurrentLineHighlightEnabled() &&
					topLine==currentLine-1) {
				g.setColor(textArea.getCurrentLineHighlightColor());
				g.fillRect(0,y, cellWidth,lineEndViewPos.y+lineEndViewPos.height-y);
				g.setColor(getForeground());
			}

			// Paint the line number.
			String number = Integer.toString(topLine+1);
			int width = getTextWidth(number, metrics);
			g.drawString(number, rhs-width,y+ascent);

			// The next possible y-coordinate is just after the last line
			// painted.
			y = lineEndViewPos.y + lineEndViewPos.height;

			// Update topLine (we're actually using it for our "current line" variable now).
			topLine++;
			if (topLine>=lineCount)
				break;
			topLineEndOffset = root.getElement(topLine).getEndOffset();

		}

		// Paint the "border" line.  Remember that the line number list doesn't
		// scroll and always has visible rect ==
		// [0,0, cellWidth-1,visibleRect.height-1].
		g.setColor(Color.BLACK);
		int x = cellWidth - 4;
		g.drawLine(x,0, x,visibleRect.height-1);

	}


	public void propertyChange(PropertyChangeEvent e) {

		String name = e.getPropertyName();

		// If they changed the background color of the text area.
		if (name.equals("background")) {
			Color bg = textArea.getBackground();
			setBackground(bg==null ? Color.WHITE : bg);
			repaint();
		}

		// If they changed the background to an image.
		if (name.equals("background.image")) {
			setBackground(Color.WHITE);
			repaint();
		}

		// They toggled lineWrap in the text area.
		else if (name.equals("lineWrap")) {
			lastY = -1; // Just to force a repaint if lineWrap is being enabled.
			setSize(getWidth(), textArea.getHeight());
		}

		// If they change the text area's font, we need to update cell heights
		// to match the font's height.
		else if (name.equals("font")) {
			updateCellHeights();
		}

		// If they change the current line highlight in any way...
		else if (name.equals(RTextArea.CURRENT_LINE_HIGHLIGHT_PROPERTY) ||
			name.equals(RTextArea.CURRENT_LINE_HIGHLIGHT_COLOR_PROPERTY)) {
			repaint();
		}

	}


	/**
	 * Called whenever a character is removed (ie backspace, delete) in the
	 * text document we're line-numbering.
	 */
	public void removeUpdate(DocumentEvent e) {
		int newNumLines = textArea.getDocument().getDefaultRootElement().getElementCount();
		if (newNumLines < currentNumLines) { // Used to be <=
			// Adjust the amount of space the line numbers take up, if necessary.
			if (newNumLines/10 < currentNumLines/10)
				updateCellWidths();
			currentNumLines = newNumLines;
			repaint();
		}
	}


	/**
	 * Sets the font used to render the line numbers.
	 *
	 * @param font The <code>java.awt.Font</code> to use to to render the line
	 *        numbers.  If <code>null</code>, a 10-point monospaced font
	 *        will be used.
	 * @see #getFont
	 */
	public void setFont(Font font) {
		if (font==null)
			font = new Font("Monospaced", Font.PLAIN, 10);
		super.setFont(font);
	}


	/**
	 * Sets the color to use to paint line numbers.
	 *
	 * @param color The color to use when painting line numbers.
	 * @see #getLineNumberColor
	 */
	public void setLineNumberColor(Color color) {
		setForeground(color);
		repaint();
	}


	/**
	 * Changes the height of the cells in the JList so that they are as tall as
	 * font. This function should be called whenever the user changes the Font
	 * of <code>textArea</code>.
	 */
	public void updateCellHeights() {
		//FontMetrics fontMetrics = textArea.getFontMetrics(textArea.getFont());
		cellHeight = textArea.getLineHeight();//fontMetrics.getHeight();
		ascent = textArea.getMaxAscent();//fontMetrics.getAscent();
		repaint();
	}


	/**
	 * Changes the width of the cells in the JList so you can see every digit
	 * of each.
	 */
	public void updateCellWidths() {

		// Adjust the amount of space the line numbers take up, if necessary.
		Font font = getFont();
		if (font!=null) {
			FontMetrics fontMetrics = getFontMetrics(font);
			int count = 0;
			int numLines = textArea.getDocument().getDefaultRootElement().getElementCount();
			while (numLines >= 10) {
				numLines = numLines/10;
				count++;
			}
			cellWidth = Math.max(fontMetrics.charWidth('9') * (count+2) + 5, MIN_CELL_WIDTH);
			revalidate();
		}

	}


}