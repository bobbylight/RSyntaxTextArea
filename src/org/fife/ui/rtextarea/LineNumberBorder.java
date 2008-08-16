/*
 * 11/18/2004
 *
 * LineNumberBorder.java - Line numbers for a text area contained
 * in an RTextScrollPane.
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
package org.fife.ui.rtextarea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JScrollPane;
import javax.swing.border.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.View;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;


/**
 * Used by <code>RTextScrollPane</code> to display line numbers for a text
 * area.  This component is capable of displaying line numbers for any
 * <code>RTextArea</code>, with line wrap enabled or disabled.  You can also
 * choose the line number font, color, and choose whether or not to "highlight"
 * the current line number.
 *
 * @author Robert Futrell
 * @version 0.5
 */
class LineNumberBorder implements Border, CaretListener, DocumentListener,
							PropertyChangeListener, ChangeListener{

	private static final int MIN_CELL_WIDTH		= 24;
	private static final int RHS_BORDER_WIDTH	= 8;

	private static final Color DEFAULT_FOREGROUND	= new Color(128,128,128);

	private RTextArea textArea;
	private JScrollPane scrollPane;

	private Font font;
	private Color foreground;
	private Color background;

	private int currentLine;	// The last line the caret was on.
	private int lastY = -1;	// Used to check if caret chanes lines when line wrap's enabled.

	private Insets insets;
	private int cellWidth;
	private int cellHeight;	// The height of a line number "cell" when word wrap is off.
	private int ascent;		// The ascent to use when painting line numbers.

	private int currentNumLines;


	/**
	 * Constructs a new <code>LineNumberBorder</code> using default values for
	 * line number color (gray) and highlighting the current line.
	 *
	 * @param scrollPane The scroll pane using this border as the viewport
	 *        border.
	 * @param textArea The text component for which line numbers will be
	 *        displayed.
	 */
	public LineNumberBorder(JScrollPane scrollPane, RTextArea textArea) {
		this(scrollPane, textArea, DEFAULT_FOREGROUND);
	}


	/**
	 * Constructs a new <code>LineNumberBorder</code>.
	 *
	 * @param scrollPane The scroll pane using this border as the viewport
	 *        border.
	 * @param textArea The text component for which line numbers will be
	 *        displayed.
	 * @param numberColor The color to use for the line numbers.  If
	 *        <code>null</code>, a default is used.
	 */
	public LineNumberBorder(JScrollPane scrollPane, RTextArea textArea,
						Color numberColor) {

		// Remember what text component we're keeping line numbers for.
		this.textArea = textArea;
		this.scrollPane = scrollPane;

		setForeground(numberColor!=null ? numberColor : DEFAULT_FOREGROUND);
		Color bg = textArea.getBackground();
		setBackground(bg==null ? Color.WHITE : bg);

		textArea.addCaretListener(this);
		textArea.addPropertyChangeListener(this);
		scrollPane.getViewport().addChangeListener(this);
		textArea.getDocument().addDocumentListener(this);

		// Initialize currentLine; otherwise, the current line won't start
		// off as highlighted.
		currentLine = 1;

		setFont(null); // Default font.
		insets = new Insets(0,0,0,0);

		updateCellHeights();
		updateCellWidths();
		insertUpdate(null); // Will call updateCellWidths().

	}


	/**
	 * Called whenever the caret changes position; highlight the correct line
	 * number.
	 *
	 * @param e The caret event.
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
				scrollPane.repaint();
			}
		}
		else { // lineWrap is enabled; must check actual y-coord. of caret.
			try {
				Rectangle r = textArea.modelToView(caretPosition);
				if (r!=null && r.y!=lastY) {
					lastY = r.y;
					currentLine = textArea.getDocument().
									getDefaultRootElement().
									getElementIndex(caretPosition) + 1;
					scrollPane.repaint();
				}
			} catch (BadLocationException ble) {
				ble.printStackTrace();
			}
		}

	}


	public void changedUpdate(DocumentEvent e) {}


	/**
	 * Returns the background color of the line number list.
	 *
	 * @return The background color.
	 * @see #setBackground
	 */
	public Color getBackground() {
		return background;
	}


	/**
	 * Returns the insets of this border.
	 *
	 * @param c This parameter is ignored.
	 * @return The insets of this border.
	 */
	public Insets getBorderInsets(Component c) {
		// Check textArea's orientation, not c's, as c is the JScrollPane.
		// The application might allow the user to toggle the text area's
		// orientation separately from the UI's.
		if (textArea.getComponentOrientation().isLeftToRight()) {
			insets.left = cellWidth;
			insets.right = 0;
		}
		else {
			insets.left = 0;
			insets.right = cellWidth;
		}
		return insets;
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
	 * Returns the font used for the line numbers.
	 *
	 * @return The font.
	 * @see #setFont
	 */
	public Font getFont() {
		return font;
	}


	/**
	 * Returns the foreground color of the line number list.
	 *
	 * @return The foreground color.
	 * @see #setForeground
	 */
	public Color getForeground() {
		return foreground;
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
	 * Called whenever a character is input (key is typed) in the text
	 * document we're line-numbering.
	 *
	 * @param e The document event.
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


	/**
	 * Returns whether this border is opaque.
	 *
	 * @return Whether this border is opaque.
	 */
	public boolean isBorderOpaque() {
		return true;
	}


	/**
	 * Paints the line numbers.  If word wrap is enabled, the gruntwork is
	 * passed on to another method.
	 *
	 * @param c The text area.
	 * @param g The graphics context.
	 * @param x The x-coordinate of the border.
	 * @param y The y-coordinate of the border.
	 * @param width The width of the border.
	 * @param height The height of the border.
	 * @see #paintWrappedLineNumbers
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
						int height) {

		Element root = textArea.getDocument().getDefaultRootElement();
		Rectangle visibleRect = textArea.getVisibleRect();

		if (visibleRect==null)
			return;

		Insets insets = getBorderInsets(c);
		boolean ltr = textArea.getComponentOrientation().isLeftToRight();
		int paintX,paintWidth;
		if (ltr) {
			paintX = x;
			paintWidth = insets.left;
		}
		else {
			paintX = x + width - insets.right;
			paintWidth = insets.right;
		}

		// Fill in the background the same color as the text component.
		g.setColor(getBackground());
		g.fillRect(paintX,y, paintWidth,height);
		g.setFont(font);

		if (textArea.getLineWrap()==true) {
			paintWrappedLineNumbers(g, root, x, y, width, visibleRect);
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
		int actualTopY = y - (visibleRect.y%cellHeight);
		Insets textAreaInsets = textArea.getInsets();
		if (textAreaInsets!=null) {
			actualTopY += textAreaInsets.top;
		}
		int y2 = actualTopY + ascent;

		// Highlight the current line's line number, if desired.
		if (textArea.isCurrentLineHighlightEnabled() && currentLine>=topLine &&
				currentLine<bottomLine) {
			g.setColor(textArea.getCurrentLineHighlightColor());
			g.fillRect(paintX,actualTopY+(currentLine-topLine)*cellHeight,
						paintWidth,cellHeight);
		}

		// Paint the "border" line.
		g.setColor(Color.BLACK);
		if (ltr) {
			g.drawLine(paintX+paintWidth-4,0,
					paintX+paintWidth-4,visibleRect.height+1);
		}
		else {
			g.drawLine(paintX+4,0, paintX+4, visibleRect.height+1);
		}

		g.setColor(getForeground());
		FontMetrics metrics = g.getFontMetrics();
		if (ltr) {
			int rhs = paintX + insets.left - RHS_BORDER_WIDTH;
			for (int i=topLine; i<bottomLine; i++) {
				String number = Integer.toString(i);
				int w = (int)metrics.getStringBounds(number, g).getWidth();
				g.drawString(number, rhs-w,y2);
				y2 += cellHeight;
			}
		}
		else {
			paintX += RHS_BORDER_WIDTH;
			for (int i=topLine; i<bottomLine; i++) {
				String number = Integer.toString(i);
				g.drawString(number, paintX,y2);
				y2 += cellHeight;
			}
		}

	}


	/**
	 * Paints line numbers for text areas with line wrap enabled.
	 *
	 * @param g The graphics context.
	 * @param root The root element of the document being painted.
	 * @param x The x-coordinate of the border.
	 * @param y The y-coordinate of the border.
	 * @param width The width of the component.
	 * @param visibleRect The visible rectangle of the text area.
	 */
	public void paintWrappedLineNumbers(Graphics g, Element root, int x, int y,
								int width, Rectangle visibleRect) {

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
		Insets insets = getBorderInsets(textArea);
		boolean ltr = textArea.getComponentOrientation().isLeftToRight();
		int paintX,paintWidth;
		if (ltr) {
			paintX = x;
			paintWidth = insets.left;
		}
		else {
			paintX = x + width - insets.right;
			paintWidth = insets.right;
		}

		RTextAreaUI ui = (RTextAreaUI)textArea.getUI();
		Rectangle visibleEditorRect = ui.getVisibleEditorRect();
		View v = ui.getRootView(textArea).getView(0);
		boolean currentLineHighlighted = textArea.
									isCurrentLineHighlightEnabled();
		int lineCount = root.getElementCount();
		int topPosition = textArea.viewToModel(
								new Point(visibleRect.x,visibleRect.y));
		int topLine = root.getElementIndex(topPosition);

		// Compute the y at which to begin painting text, taking into account
		// that 1 logical line => at least 1 physical line, so it may be that
		// y<0.  The computed y-value is the y-value of the top of the first
		// (possibly) partially-visible view.
		Rectangle r = LineNumberBorder.getChildViewBounds(v, topLine,
												visibleEditorRect);
		int offset = r.y - visibleRect.y;
		int y2 = y + offset;	// The y-coordinate at which we're painting.

		FontMetrics metrics = g.getFontMetrics();
		int rhs = x + insets.left - RHS_BORDER_WIDTH;
		int visibleBottom = y2 + visibleRect.height;

		// Keep painting lines until our y-coordinate is passed the visible
		// end of the text area.
		g.setColor(getForeground());
		while (y2 < visibleBottom) {

			r = LineNumberBorder.getChildViewBounds(v, topLine,
											visibleEditorRect);
			int lineEndY = (r.y+r.height) - visibleRect.y;

			// Highlight the current line's line number, if desired.
			if (currentLineHighlighted && topLine==currentLine-1) {
				g.setColor(textArea.getCurrentLineHighlightColor());
				// FIXME:  Why is the "+2" needed???
				g.fillRect(paintX,y2, paintWidth,lineEndY-y2 + 2);
				g.setColor(getForeground());
			}

			// Paint the line number.
			String number = Integer.toString(topLine+1);
			if (ltr) {
				width = (int)metrics.getStringBounds(number, g).getWidth();
				g.drawString(number, rhs-width,y2+ascent);
			}
			else {
				g.drawString(number, paintX+RHS_BORDER_WIDTH, y2+ascent);
			}

			// The next possible y-coordinate is just after the last line
			// painted.
			y2 += r.height;

			// Update topLine (we're actually using it for our "current line"
			// variable now).
			topLine++;
			if (topLine>=lineCount)
				break;

		}

		// Paint the "border" line.  Remember that the line number list
		// doesn't scroll and always has visible rect ==
		// [0,0, insets.left-1,visibleRect.height-1].
		g.setColor(Color.BLACK);
		if (ltr) {
			x = x + insets.left - 4;
		}
		else {
			x = paintX + 4;
		}
		g.drawLine(x,y, x,y+visibleRect.height-1);

	}


	/**
	 * Called whenever the text area fires a property change event.
	 *
	 * @param e The event.
	 */
	public void propertyChange(PropertyChangeEvent e) {

		String name = e.getPropertyName();

		// If they changed the background color of the text area.
		if (name.equals("background")) {
			Color bg = textArea.getBackground();
			setBackground(bg==null ? Color.WHITE : bg);
			scrollPane.repaint();
		}

		// If they changed the background to an image.
		if (name.equals("background.image")) {
			setBackground(Color.WHITE);
			scrollPane.repaint();
		}

		// If they change the text area's font, we need to update cell heights
		// to match the font's height.
		else if (name.equals("font")) {
			updateCellHeights();
		}

		// If they change the current line highlight in any way...
		else if (name.equals(RTextArea.CURRENT_LINE_HIGHLIGHT_PROPERTY) ||
			name.equals(RTextArea.CURRENT_LINE_HIGHLIGHT_COLOR_PROPERTY)) {
			scrollPane.repaint();
		}

		// If they change the text area's syntax scheme (i.e., it is an
		// RSyntaxTextArea), update cell heights.
		else if (name.equals(RSyntaxTextArea.SYNTAX_SCHEME_PROPERTY)) {
			updateCellHeights();
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
			// Need to repaint in case they removed a line by pressing
			// delete.
			scrollPane.repaint();
		}
	}


	/**
	 * Sets the background color of the line number list.
	 *
	 * @param color The background color.
	 * @see #getBackground
	 */
	public void setBackground(Color background) {
		if (background!=null && !background.equals(this.background)) {
			this.background = background;
			//repaint();
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
		this.font = font;
		//repaint();
	}


	/**
	 * Sets the foreground color of the line number list.
	 *
	 * @param color The foreground color.
	 * @see #getForeground
	 */
	public void setForeground(Color foreground) {
		if (foreground!=null && !foreground.equals(this.foreground)) {
			this.foreground = foreground;
			//repaint();
		}
	}


	/**
	 * Sets the color to use to paint line numbers.
	 *
	 * @param color The color to use when painting line numbers.
	 * @see #getLineNumberColor
	 */
	public void setLineNumberColor(Color color) {
		setForeground(color);
		//repaint();
	}


	/**
	 * Messages from the viewport.
	 *
	 * @param change The change event.
	 */
	public void stateChanged(ChangeEvent e) {
		scrollPane.repaint();
	}


	/**
	 * Changes the height of the cells in the JList so that they are as tall as
	 * the height of a line of text in the text area.  This should be called
	 * whenever the user changes the font in an <code>RTextArea</code> or a
	 * syntax style in an <code>RSyntaxTextArea</code>.
	 */
	public void updateCellHeights() {
		cellHeight = textArea.getLineHeight();
		ascent = textArea.getMaxAscent();
	}


	/**
	 * Changes the width of the cells in the JList so you can see every digit
	 * of each.
	 */
	public void updateCellWidths() {

		// Adjust the amount of space the line numbers take up, if necessary.
		Font font = getFont();
		if (font!=null) {
			FontMetrics fontMetrics = textArea.getFontMetrics(font);
			int count = 0;
			int numLines = textArea.getDocument().getDefaultRootElement().getElementCount();
			while (numLines >= 10) {
				numLines = numLines/10;
				count++;
			}
			cellWidth = Math.max(fontMetrics.charWidth('9')*(count+2)+5,
								MIN_CELL_WIDTH);
			//revalidate();
		}

	}


}