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
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.MouseInputListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;


/**
 * Renders line numbers in the gutter.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class LineNumberList extends AbstractGutterComponent
								implements MouseInputListener {

	private int currentLine;	// The last line the caret was on.
	private int lastY = -1;		// Used to check if caret changes lines when line wrap is enabled.

	private int cellHeight;		// Height of a line number "cell" when word wrap is off.
	private int cellWidth;		// The width used for all line number cells.
	private int ascent;			// The ascent to use when painting line numbers.

	private int mouseDragStartOffset;

	/**
	 * Listens for events from the current text area.
	 */
	private Listener l;

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

	/**
	 * The index at which line numbering should start.  The default value is
	 * <code>1</code>, but applications can change this if, for example, they
	 * are displaying a subset of lines in a file.
	 */
	private int lineNumberingStartIndex;

	private static final int RHS_BORDER_WIDTH	= 8;


	/**
	 * Constructs a new <code>LineNumberList</code> using default values for
	 * line number color (gray) and highlighting the current line.
	 *
	 * @param textArea The text component for which line numbers will be
	 *        displayed.
	 */
	public LineNumberList(RTextArea textArea) {
		this(textArea, Color.GRAY);
	}


	/**
	 * Constructs a new <code>LineNumberList</code>.
	 *
	 * @param textArea The text component for which line numbers will be
	 *        displayed.
	 * @param numberColor The color to use for the line numbers.
	 */
	public LineNumberList(RTextArea textArea, Color numberColor) {

		super(textArea);

		if (numberColor!=null) {
			setForeground(numberColor);
		}
		else {
			setForeground(Color.GRAY);
		}

		// Initialize currentLine; otherwise, the current line won't start
		// off as highlighted.
		currentLine = 0;
		setLineNumberingStartIndex(1);

		visibleRect = new Rectangle(); // Must be initialized

		addMouseListener(this);
		addMouseMotionListener(this);

	}


	/**
	 * Overridden to set width of this component correctly when we are first
	 * displayed (as keying off of the RTextArea gives us (0,0) when it isn't
	 * yet displayed.
	 */
	public void addNotify() {
		super.addNotify();
		if (textArea!=null) {
			l.install(textArea); // Won't double-install
		}
		updateCellWidths();
		updateCellHeights();
	}


	/**
	 * Returns the starting line's line number.  The default value is
	 * <code>1</code>.
	 *
	 * @return The index
	 * @see #setLineNumberingStartIndex(int)
	 */
	public int getLineNumberingStartIndex() {
		return lineNumberingStartIndex;
	}


	/**
	 * {@inheritDoc}
	 */
	public Dimension getPreferredSize() {
		int h = textArea!=null ? textArea.getHeight() : 100; // Arbitrary
		return new Dimension(cellWidth, h);
	}


	/**
	 * {@inheritDoc}
	 */
	void handleDocumentEvent(DocumentEvent e) {
		int newLineCount = textArea!=null ? textArea.getLineCount() : 0;
		if (newLineCount!=currentLineCount) {
			// Adjust the amount of space the line numbers take up,
			// if necessary.
			if (newLineCount/10 != currentLineCount/10) {
				updateCellWidths();
			}
			currentLineCount = newLineCount;
			repaint();
		}
	}


	/**
	 * {@inheritDoc}
	 */
	void lineHeightsChanged() {
		updateCellHeights();
	}


	public void mouseClicked(MouseEvent e) {
	}


	public void mouseDragged(MouseEvent e) {
		if (mouseDragStartOffset>-1) {
			int pos = textArea.viewToModel(new Point(0, e.getY()));
			if (pos>=0) { // Not -1
				textArea.setCaretPosition(mouseDragStartOffset);
				textArea.moveCaretPosition(pos);
			}
		}
	}


	public void mouseEntered(MouseEvent e) {
	}


	public void mouseExited(MouseEvent e) {
	}


	public void mouseMoved(MouseEvent e) {
	}


	public void mousePressed(MouseEvent e) {
		if (textArea==null) {
			return;
		}
		if (e.getButton()==MouseEvent.BUTTON1) {
			int pos = textArea.viewToModel(new Point(0, e.getY()));
			if (pos>=0) { // Not -1
				textArea.setCaretPosition(pos);
			}
			mouseDragStartOffset = pos;
		}
		else {
			mouseDragStartOffset = -1;
		}
	}


	public void mouseReleased(MouseEvent e) {
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
		//System.out.println("LineNumberList repainting: " + visibleRect);
		if (visibleRect==null) {
			return;
		}

		Color bg = getBackground();
		if (getGutter()!=null) { // Should always be true
			bg = getGutter().getBackground();
		}
		g.setColor(bg);
		g.fillRect(0,visibleRect.y, cellWidth,visibleRect.height);
		g.setFont(getFont());

		Document doc = textArea.getDocument();
		Element root = doc.getDefaultRootElement();

		if (textArea.getLineWrap()) {
			paintWrappedLineNumbers(g, visibleRect);
			return;
		}

		// Get the first and last lines to paint.
		int topLine = visibleRect.y/cellHeight;
		int bottomLine = Math.min(topLine+visibleRect.height/cellHeight+1,
							root.getElementCount());

		// Get where to start painting (top of the row), and where to paint
		// the line number (drawString expects y==baseline).
		// We need to be "scrolled up" up just enough for the missing part of
		// the first line.
		int actualTopY = topLine*cellHeight;
		textAreaInsets = textArea.getInsets(textAreaInsets);
		actualTopY += textAreaInsets.top;
		int y = actualTopY + ascent;

		// Highlight the current line's line number, if desired.
		if (textArea.getHighlightCurrentLine() && currentLine>=topLine &&
				currentLine<=bottomLine) {
			g.setColor(textArea.getCurrentLineHighlightColor());
			g.fillRect(0,actualTopY+(currentLine-topLine)*cellHeight,
						cellWidth,cellHeight);
		}

		// Paint line numbers
		g.setColor(getForeground());
		boolean ltr = getComponentOrientation().isLeftToRight();
		if (ltr) {
			FontMetrics metrics = g.getFontMetrics();
			int rhs = getWidth() - RHS_BORDER_WIDTH;
			for (int i=topLine+1; i<=bottomLine; i++) {
				int index = i + getLineNumberingStartIndex() - 1;
				String number = Integer.toString(index);
				int width = metrics.stringWidth(number);
				g.drawString(number, rhs-width,y);
				y += cellHeight;
			}
		}
		else { // rtl
			for (int i=topLine+1; i<=bottomLine; i++) {
				int index = i + getLineNumberingStartIndex() - 1;
				String number = Integer.toString(index);
				g.drawString(number, RHS_BORDER_WIDTH, y);
				y += cellHeight;
			}
		}

	}


	/**
	 * Paints line numbers for text areas with line wrap enabled.
	 *
	 * @param g The graphics context.
	 * @param visibleRect The visible rectangle of these line numbers.
	 */
	private void paintWrappedLineNumbers(Graphics g, Rectangle visibleRect) {

		// The variables we use are as follows:
		// - visibleRect is the "visible" area of the text area; e.g.
		// [0,100, 300,100+(lineCount*cellHeight)-1].
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
		Rectangle r = LineNumberList.getChildViewBounds(v, topLine,
												visibleEditorRect);
		int y = r.y;

		int rhs;
		boolean ltr = getComponentOrientation().isLeftToRight();
		if (ltr) {
			rhs = width - RHS_BORDER_WIDTH;
		}
		else { // rtl
			rhs = RHS_BORDER_WIDTH;
		}
		int visibleBottom = visibleRect.y + visibleRect.height;
		FontMetrics metrics = g.getFontMetrics();

		// Keep painting lines until our y-coordinate is past the visible
		// end of the text area.
		g.setColor(getForeground());

		while (y < visibleBottom) {

			r = LineNumberList.getChildViewBounds(v, topLine, visibleEditorRect);

			// Highlight the current line's line number, if desired.
			if (currentLineHighlighted && topLine==currentLine) {
				g.setColor(textArea.getCurrentLineHighlightColor());
				g.fillRect(0,y, width,(r.y+r.height)-y);
				g.setColor(getForeground());
			}

			// Paint the line number.
			int index = (topLine+1) + getLineNumberingStartIndex() - 1;
			String number = Integer.toString(index);
			if (ltr) {
				int strWidth = metrics.stringWidth(number);
				g.drawString(number, rhs-strWidth,y+ascent);
			}
			else {
				int x = RHS_BORDER_WIDTH;
				g.drawString(number, x, y+ascent);
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

	}


	/**
	 * Called when this component is removed from the view hierarchy.
	 */
	public void removeNotify() {
		super.removeNotify();
		if (textArea!=null) {
			l.uninstall(textArea);
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
	 * Overridden to ensure line number cell sizes are updated with the
	 * font size change.
	 *
	 * @param font The new font to use for line numbers.
	 */
	public void setFont(Font font) {
		super.setFont(font);
		updateCellWidths();
		updateCellHeights();
	}


	/**
	 * Sets the starting line's line number.  The default value is
	 * <code>1</code>.  Applications can call this method to change this value
	 * if they are displaying a subset of lines in a file, for example.
	 *
	 * @param index The new index.
	 * @see #getLineNumberingStartIndex()
	 */
	public void setLineNumberingStartIndex(int index) {
		lineNumberingStartIndex = index;
	}


	/**
	 * Sets the text area being displayed.
	 *
	 * @param textArea The text area.
	 */
	public void setTextArea(RTextArea textArea) {

		if (l==null) {
			l = new Listener();
		}

		if (this.textArea!=null) {
			l.uninstall(textArea);
		}

		super.setTextArea(textArea);

		if (textArea!=null) {
			l.install(textArea); // Won't double-install
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

		// Adjust the amount of space the line numbers take up, if necessary.
		if (textArea!=null) {
			Font font = getFont();
			if (font!=null) {
				FontMetrics fontMetrics = getFontMetrics(font);
				int count = 0;
				int lineCount = textArea.getLineCount();
				while (lineCount >= 10) {
					lineCount = lineCount/10;
					count++;
				}
				cellWidth += fontMetrics.charWidth('9')*(count+1) + 5;
			}
		}

		if (cellWidth!=oldCellWidth) { // Always true
			revalidate();
		}

	}


	/**
	 * Listens for events in the text area we're interested in.
	 */
	private class Listener implements CaretListener, PropertyChangeListener {

		private boolean installed;

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

		public void install(RTextArea textArea) {
			if (!installed) {
				//System.out.println("Installing");
				textArea.addCaretListener(this);
				textArea.addPropertyChangeListener(this);
				caretUpdate(null); // Force current line highlight repaint
				installed = true;
			}
		}

		public void propertyChange(PropertyChangeEvent e) {

			String name = e.getPropertyName();

			// If they change the current line highlight in any way...
			if (RTextArea.HIGHLIGHT_CURRENT_LINE_PROPERTY.equals(name) ||
				RTextArea.CURRENT_LINE_HIGHLIGHT_COLOR_PROPERTY.equals(name)) {
				repaintLine(currentLine);
			}

		}

		public void uninstall(RTextArea textArea) {
			if (installed) {
				//System.out.println("Uninstalling");
				textArea.removeCaretListener(this);
				textArea.removePropertyChangeListener(this);
				installed = false;
			}
		}

	}


}