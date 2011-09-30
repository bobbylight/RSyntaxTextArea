package org.fife.ui.rtextarea;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.folding.Fold;
import org.fife.ui.rsyntaxtextarea.folding.FoldManager;


/**
 * Component in the gutter that displays +/- icons to expand and collapse
 * fold regions in the editor.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class FoldIndicator extends AbstractGutterComponent {

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
	 * The fold to show the outline line for.
	 */
	private Fold foldWithOutlineShowing;

	/**
	 * The color used to paint fold outlines.
	 */
	private static final Color DEFAULT_FOREGROUND = Color.GRAY;

	/**
	 * Listens for events in this component.
	 */
	private Listener listener;

	/**
	 * Width of this component.
	 */
	private static final int WIDTH = 12;

	private static final int ICON_HEIGHT = 9;


	public FoldIndicator(RTextArea textArea) {
		super(textArea);
		setForeground(DEFAULT_FOREGROUND);
		listener = new Listener(this);
		visibleRect = new Rectangle();
		ToolTipManager.sharedInstance().registerComponent(this);
	}


	public Fold findOpenFoldClosestTo(Point p) {

		Fold fold = null;

		RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
		if (rsta.isCodeFoldingEnabled()) { // Should always be true
			int offs = rsta.viewToModel(p); // TODO: Optimize me
			if (offs>-1) {
				try {
					int line = rsta.getLineOfOffset(offs);
					FoldManager fm = rsta.getFoldManager();
					do {
						fold = fm.getFoldForLine(line);
					} while (fold==null && line-->=0);
				} catch (BadLocationException ble) {
					ble.printStackTrace(); // Never happens
				}
			}
		}
	
		return fold;

	}


	public Dimension getPreferredSize() {
		int h = textArea!=null ? textArea.getHeight() : 100; // Arbitrary
		return new Dimension(WIDTH, h);
	}


	/**
	 * Positions tool tips to be aligned in the text component, so that the
	 * displayed content is shown (almost) exactly where it would be in the
	 * editor.
	 *
	 * @param e The mouse location.
	 */
	public Point getToolTipLocation(MouseEvent e) {
		//return super.getToolTipLocation(e);
		Point p = e.getPoint();
		p.y = (p.y/textArea.getLineHeight()) * textArea.getLineHeight();
		p.x = getWidth();
		return p;
	}


	/**
	 * Overridden to show the content of a collapsed fold on mouse-overs.
	 *
	 * @param e The mouse location.
	 */
	public String getToolTipText(MouseEvent e) {

		String text = null;

		RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
		if (rsta.isCodeFoldingEnabled()) {
			FoldManager fm = rsta.getFoldManager();
			int pos = rsta.viewToModel(new Point(0, e.getY()));
			if (pos>=0) { // Not -1
				int line = 0;
				try {
					line = rsta.getLineOfOffset(pos);
				} catch (BadLocationException ble) {
					ble.printStackTrace(); // Never happens
					return null;
				}
				Fold fold = fm.getFoldForLine(line);
				if (fold!=null && fold.isFolded()) {

					int endLine = fold.getEndLine();

					StringBuffer sb = new StringBuffer("<html>");
					while (line<=endLine && line<rsta.getLineCount()) { // Sanity
						Token t = rsta.getTokenListForLine(line);
						while (t!=null && t.isPaintable()) {
							t.appendHTMLRepresentation(sb, rsta, true);
							t = t.getNextToken();
						}
						sb.append("<br>");
						line++;
					}

					text = sb.toString();

				}
			}
		}

		return text;

	}


	void handleDocumentEvent(DocumentEvent e) {
		int newLineCount = textArea.getLineCount();
		if (newLineCount!=currentLineCount) {
			currentLineCount = newLineCount;
			repaint();
		}
	}


	void lineHeightsChanged() {
		// TODO Auto-generated method stub
	}


	protected void paintComponent(Graphics g) {

		if (textArea==null) {
			return;
		}

		visibleRect = g.getClipBounds(visibleRect);
		if (visibleRect==null) { // ???
			visibleRect = getVisibleRect();
		}
		//System.out.println("IconRowHeader repainting: " + visibleRect);
		if (visibleRect==null) {
			return;
		}

		Color bg = getBackground();
		if (getGutter()!=null) { // Should always be true
			bg = getGutter().getBackground();
		}
		g.setColor(bg);
		g.fillRect(0,visibleRect.y, getWidth(),visibleRect.height);

		RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
		if (!rsta.isCodeFoldingEnabled()) { 
			return; // We should be hidden in this case, but still...
		}

		if (textArea.getLineWrap()) {
			paintComponentWrapped(g);
			return;
		}

		// Get where to start painting (top of the row).
		// We need to be "scrolled up" up just enough for the missing part of
		// the first line.
		int cellHeight = textArea.getLineHeight();
		int topLine = visibleRect.y/cellHeight;
		int y = topLine*cellHeight + (cellHeight-ICON_HEIGHT)/2;
		textAreaInsets = textArea.getInsets(textAreaInsets);
		if (textAreaInsets!=null) {
			y += textAreaInsets.top;
		}

		// Get the first and last lines to paint.
		FoldManager fm = rsta.getFoldManager();
		topLine += fm.getHiddenLineCountAbove(topLine, true);

		int width = getWidth();
		int x = width - 10;
		boolean paintingOutlineLine = false;

		int line = topLine;
		while (y<visibleRect.y+visibleRect.height) {
			if (paintingOutlineLine) {
				g.setColor(getForeground());
				int w2 = width/2;
				if (line==foldWithOutlineShowing.getEndLine()) {
					int y2 = y+cellHeight/2;
					g.drawLine(w2,y, w2,y2);
					g.drawLine(w2,y2, width-2,y2);
					paintingOutlineLine = false;
				}
				else {
					g.drawLine(w2,y, w2,y+cellHeight);
				}
			}
			Fold fold = fm.getFoldForLine(line);
			if (fold!=null) {
				if (fold==foldWithOutlineShowing && !fold.isFolded()) {
					g.setColor(getForeground());
					int w2 = width/2;
					g.drawLine(w2,y+cellHeight/2, w2,y+cellHeight);
					paintingOutlineLine = true;
				}
				paintFoldIcon(g, fold, x, y);
				if (fold.isFolded()) {
					line += fold.getLineCount();
				}
			}
			line++;
			y += cellHeight;
		}

	}


	/**
	 * Paints folding icons when line wrapping is enabled.
	 *
	 * @param g The graphics context.
	 */
	private void paintComponentWrapped(Graphics g) {
		// TODO
	}


	private void paintFoldIcon(Graphics g, Fold fold, int x, int y) {
		g.setColor(Color.white);
		g.fillRect(x,y, 8,8);
		g.setColor(getForeground());
		g.drawRect(x,y, 8,8);
		g.drawLine(x+2,y+4, x+2+4,y+4);
		if (fold.isFolded()) {
			g.drawLine(x+4,y+2, x+4,y+6);
		}
	}


	private int rowAtPoint(Point p) {

		int line = 0;

		try {
			int offs = textArea.viewToModel(p);
			if (offs>-1) {
				line = textArea.getLineOfOffset(offs);
			}
		} catch (BadLocationException ble) {
			ble.printStackTrace(); // Never happens
		}

		return line;

	}


	/**
	 * Overridden so we can track when code folding is enabled/disabled.
	 */
	public void setTextArea(RTextArea textArea) {
		if (this.textArea!=null) {
			this.textArea.removePropertyChangeListener(
					RSyntaxTextArea.CODE_FOLDING_PROPERTY, listener);
		}
		super.setTextArea(textArea);
		if (this.textArea!=null) {
			this.textArea.addPropertyChangeListener(
					RSyntaxTextArea.CODE_FOLDING_PROPERTY, listener);
		}
	}


	private class Listener extends MouseInputAdapter
			implements PropertyChangeListener {

		public Listener(FoldIndicator fgc) {
			fgc.addMouseListener(this);
			fgc.addMouseMotionListener(this);
		}

		public void mouseClicked(MouseEvent e) {

			Point p = e.getPoint();
			int line = rowAtPoint(p);

			RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
			FoldManager fm = rsta.getFoldManager();

			Fold fold = fm.getFoldForLine(line);
			if (fold!=null) {
				fold.toggleFoldState();
				getGutter().repaint();
				textArea.repaint();
			}

		}

		public void mouseExited(MouseEvent e) {
			if (foldWithOutlineShowing!=null) {
				foldWithOutlineShowing = null;
				repaint();
			}
		}

		public void mouseMoved(MouseEvent e) {
			Fold newSelectedFold = findOpenFoldClosestTo(e.getPoint());
			if (newSelectedFold!=foldWithOutlineShowing) {
				foldWithOutlineShowing = newSelectedFold;
				repaint();
			}
		}

		public void propertyChange(PropertyChangeEvent e) {
			// Whether or not code folding is enabled in the editor has changed.
			repaint();
		}

	}


}