/*
 * 10/08/2011
 *
 * FoldIndicator.java - Gutter component allowing the user to expand and
 * collapse folds.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.focusabletip.TipUtil;
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
	 * The color used for the foreground of armed folds.
	 */
	private Color armedForeground;


	/**
	 * The color to use for fold icon backgrounds, if the default icons
	 * are used.
	 */
	private Color foldIconBackground;

	/**
	 * The color to use for armed fold icon backgrounds, if the default icons
	 * are used.  This may be {@code null}.
	 */
	private Color foldIconArmedBackground;

	/**
	 * The icon used for collapsed folds.
	 */
	private FoldIndicatorIcon collapsedFoldIcon;

	/**
	 * The icon used for expanded folds.
	 */
	private FoldIndicatorIcon expandedFoldIcon;

	/**
	 * Used while painting; global flag to denote whether the mouse is over
	 * a fold indicator.
	 */
	private boolean mouseOverFoldIcon;

	/**
	 * Used while painting; global flag to denote whether the
	 * currently-being-painted fold should be rendered as armed.
	 */
	private boolean paintFoldArmed;

	/**
	 * Whether tool tips are displayed showing the contents of collapsed
	 * fold regions.
	 */
	private boolean showFoldRegionTips;

	/**
	 * Whether the range of lines covered by an expanded, armed fold icon
	 * should be visually shown.
	 */
	private boolean showArmedFoldRange;

	/**
	 * Optional additional left margin.
	 */
	private int additionalLeftMargin;

	/**
	 * The strategy to use when rendering expanded folds.
	 */
	private ExpandedFoldRenderStrategy expandedFoldRenderStrategy;

	/**
	 * The color used to paint fold outlines.
	 */
	public static final Color DEFAULT_FOREGROUND = Color.GRAY;

	/**
	 * The default color used to paint the "inside" of fold icons.
	 */
	public static final Color DEFAULT_FOLD_BACKGROUND = Color.WHITE;

	/**
	 * The alpha used for "collapsed" fold icons.
	 */
	private float collapsedFoldIconAlpha;

	/**
	 * Used to update the collapsed fold icons' alpha value on a timer.
	 */
	private AlphaRunnable alphaRunnable;

	/**
	 * The timer used to update collapsed fold icons' alpha.
	 */
	private Timer timer;

	/**
	 * Listens for events in this component.
	 */
	private Listener listener;

	private static final int COLLAPSED_FOLD_ALPHA_DELAY_MILLIS = 16;

	private static final float ALPHA_DELTA = 0.1f;


	public FoldIndicator(RTextArea textArea) {
		super(textArea);
	}


	/**
	 * Overridden to use the editor's background if it's detected that the
	 * user isn't using white as the editor bg, but the system's tool tip
	 * background is yellow-ish.
	 *
	 * @return The tool tip.
	 */
	@Override
	public JToolTip createToolTip() {
		JToolTip tip = super.createToolTip();
		tip.setBackground(TipUtil.getToolTipBackground(textArea));
		tip.setBorder(TipUtil.getToolTipBorder(textArea));
		return tip;
	}


	/**
	 * Returns the amount of additional size to give the left margin of this
	 * component.  This can be used to add blank space between this component
	 * and the line number indicator in the gutter.
	 *
	 * @return The additional left margin.
	 * @see #setAdditionalLeftMargin(int)
	 */
	public int getAdditionalLeftMargin() {
		return additionalLeftMargin;
	}


	private Fold findOpenFoldClosestTo(Point p) {

		Fold fold = null;
		mouseOverFoldIcon = false;

		RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
		if (rsta.isCodeFoldingEnabled()) { // Should always be true
			int offs = rsta.viewToModel(p); // TODO: Optimize me
			if (offs>-1) {
				try {
					int line = rsta.getLineOfOffset(offs);
					FoldManager fm = rsta.getFoldManager();
					fold = fm.getFoldForLine(line);
					if (fold != null) {
						// The mouse is directly over the fold indicator
						mouseOverFoldIcon = true;
					}
					else {
						fold = fm.getDeepestOpenFoldContaining(offs);
					}
				} catch (BadLocationException ble) {
					ble.printStackTrace(); // Never happens
				}
			}
		}

		return fold;

	}


	/**
	 * Returns the foreground color used for armed folds.
	 *
	 * @return The foreground color used for armed folds.
	 * @see #setArmedForeground(Color)
	 */
	public Color getArmedForeground() {
		return armedForeground;
	}


	/**
	 * Returns the strategy to use for rendering expanded folds.
	 *
	 * @return The strategy to use for rendering expanded folds.
	 * @see #setExpandedFoldRenderStrategy(ExpandedFoldRenderStrategy)
	 */
	public ExpandedFoldRenderStrategy getExpandedFoldRenderStrategy() {
		return expandedFoldRenderStrategy;
	}


	/**
	 * Returns the color to use for the "background" of armed fold icons.  This
	 * is ignored if custom icons are used.
	 *
	 * @return The background color.  If this is {@code null}, there is no
	 *         special color for armed fold icons.
	 * @see #setFoldIconArmedBackground(Color)
	 * @see #getFoldIconBackground()
	 */
	public Color getFoldIconArmedBackground() {
		return foldIconArmedBackground;
	}


	/**
	 * Returns the color to use for the "background" of fold icons.  This
	 * is ignored if custom icons are used.
	 *
	 * @return The background color.
	 * @see #setFoldIconBackground(Color)
	 * @see #getFoldIconArmedBackground()
	 */
	public Color getFoldIconBackground() {
		return foldIconBackground;
	}


	/**
	 * Returns whether to paint expanded folds.
	 *
	 * @return Whether to paint expanded folds.
	 */
	private boolean getPaintExpandedFolds() {
		return expandedFoldRenderStrategy == ExpandedFoldRenderStrategy.ALWAYS || collapsedFoldIconAlpha > 0;
	}


	@Override
	public Dimension getPreferredSize() {
		int iconWidth = Math.max(expandedFoldIcon.getIconWidth(), collapsedFoldIcon.getIconWidth());
		int h = textArea!=null ? textArea.getHeight() : 100; // Arbitrary
		return new Dimension(iconWidth + 4 + additionalLeftMargin, h);
	}


	/**
	 * Returns whether a line should be drawn to show the range of lines contained
	 * in an expanded fold when it is armed (hovered over).
	 *
	 * @return Whether to show an armed fold's range.
	 * @see #setShowArmedFoldRange(boolean)
	 */
	public boolean getShowArmedFoldRange() {
		return showArmedFoldRange;
	}


	/**
	 * Returns whether tool tips are displayed showing the contents of
	 * collapsed fold regions when the mouse hovers over a +/- icon.
	 *
	 * @return Whether these tool tips are displayed.
	 * @see #setShowCollapsedRegionToolTips(boolean)
	 */
	public boolean getShowCollapsedRegionToolTips() {
		return showFoldRegionTips;
	}


	/**
	 * Positions tool tips to be aligned in the text component, so that the
	 * displayed content is shown (almost) exactly where it would be in the
	 * editor.
	 *
	 * @param e The mouse location.
	 */
	@Override
	public Point getToolTipLocation(MouseEvent e) {

		// ToolTipManager requires both location and text to be null to hide
		// a currently-visible tool tip window.  If text is null but location
		// has some value, it will show a tool tip with empty content, the size
		// of its border (!).
		String text = getToolTipText(e);
		if (text==null) {
			return null;
		}

		// Try to overlap the tip's text directly over the code
		Point p = e.getPoint();
		p.y = (p.y/textArea.getLineHeight()) * textArea.getLineHeight();
		p.x = getWidth() + textArea.getMargin().left;
		Gutter gutter = getGutter();
		int gutterMargin = gutter.getInsets().right;
		p.x += gutterMargin;
		JToolTip tempTip = createToolTip();
		p.x -= tempTip.getInsets().left;
		p.y += 16;
		return p;
	}


	/**
	 * Overridden to show the content of a collapsed fold on mouse-overs.
	 *
	 * @param e The mouse location.
	 */
	@Override
	public String getToolTipText(MouseEvent e) {

		String text = null;

		RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
		if (rsta.isCodeFoldingEnabled()) {
			FoldManager fm = rsta.getFoldManager();
			int pos = rsta.viewToModel(new Point(0, e.getY()));
			if (pos>=0) { // Not -1
				int line;
				try {
					line = rsta.getLineOfOffset(pos);
				} catch (BadLocationException ble) {
					ble.printStackTrace(); // Never happens
					return null;
				}
				Fold fold = fm.getFoldForLine(line);
				if (fold!=null && fold.isCollapsed()) {

					int endLine = fold.getEndLine();
					if (fold.getLineCount()>25) { // Not too big
						endLine = fold.getStartLine() + 25;
					}

					StringBuilder sb = new StringBuilder("<html><nobr>");
					while (line<=endLine && line<rsta.getLineCount()) { // Sanity
						Token t = rsta.getTokenListForLine(line);
						while (t!=null && t.isPaintable()) {
							t.appendHTMLRepresentation(sb, rsta, true, true);
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


	void gutterArmedUpdate(boolean armed) {
		if (expandedFoldRenderStrategy == ExpandedFoldRenderStrategy.ON_HOVER) {
			alphaRunnable.delta = armed ? ALPHA_DELTA : -ALPHA_DELTA;
			timer.restart();
		}
		else {
			collapsedFoldIconAlpha = 1;
			timer.stop();
			repaint();
		}
	}


	@Override
	void handleDocumentEvent(DocumentEvent e) {
		int newLineCount = textArea.getLineCount();
		if (newLineCount!=currentLineCount) {
			currentLineCount = newLineCount;
			repaint();
		}
	}


	@Override
	protected void init() {
		super.init();
		setForeground(DEFAULT_FOREGROUND);
		setFoldIconBackground(DEFAULT_FOLD_BACKGROUND);
		setStyle(FoldIndicatorStyle.MODERN);
		listener = new Listener(this);
		visibleRect = new Rectangle();
		setShowCollapsedRegionToolTips(true);
		alphaRunnable = new AlphaRunnable();
		timer = new Timer(COLLAPSED_FOLD_ALPHA_DELAY_MILLIS, alphaRunnable);
		timer.setRepeats(true);
	}


	@Override
	void lineHeightsChanged() {
		// TODO Auto-generated method stub
	}


	@Override
	protected void paintComponent(Graphics g) {

		if (textArea==null) {
			return;
		}

		visibleRect = g.getClipBounds(visibleRect);
		if (visibleRect==null) { // ???
			visibleRect = getVisibleRect();
		}
		//System.out.println("FoldIndicator repainting: " + visibleRect);
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
		textAreaInsets = textArea.getInsets(textAreaInsets);
		if (visibleRect.y<textAreaInsets.top) {
			visibleRect.height -= (textAreaInsets.top - visibleRect.y);
			visibleRect.y = textAreaInsets.top;
		}
		int cellHeight = textArea.getLineHeight();
		int topLine = (visibleRect.y-textAreaInsets.top)/cellHeight;
		int y = topLine*cellHeight +
			(cellHeight-collapsedFoldIcon.getIconHeight())/2;
		y += textAreaInsets.top;

		// Get the first and last lines to paint.
		FoldManager fm = rsta.getFoldManager();
		topLine += fm.getHiddenLineCountAbove(topLine, true);

		int width = getWidth();
		int line = topLine;
		boolean paintingOutlineLine = getShowArmedFoldRange() &&
			foldWithOutlineShowing!=null &&
			foldWithOutlineShowing.containsLine(line);

		while (y<visibleRect.y+visibleRect.height) {

			if (paintingOutlineLine) {
				g.setColor(getForeground());
				int w2 = width / 2;
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
				if (fold==foldWithOutlineShowing) {
					if (!fold.isCollapsed()) {
						paintingOutlineLine = getShowArmedFoldRange();
						if (paintingOutlineLine) {
							g.setColor(getForeground());
							int w2 = width / 2;
							g.drawLine(w2, y + cellHeight / 2, w2, y + cellHeight);
						}
					}
					if (mouseOverFoldIcon) {
						paintFoldArmed = true;
					}
				}
				if (fold.isCollapsed()) {
					int x = (width - collapsedFoldIcon.getIconWidth()) / 2;
					paintIcon(collapsedFoldIcon, g, x, y, true);
					// Skip to next line to paint, taking extra care for lines with
					// block ends and begins together, e.g. "} else {"
					do {
						int hiddenLineCount = fold.getLineCount();
						if (hiddenLineCount==0) {
							// Fold parser identified a zero-line fold region.
							// This is really a bug, but we'll be graceful here
							// and avoid an infinite loop.
							break;
						}
						line += hiddenLineCount;
						fold = fm.getFoldForLine(line);
					} while (fold!=null && fold.isCollapsed());
				}
				else if (getPaintExpandedFolds()) {
					int x = (width - expandedFoldIcon.getIconWidth()) / 2;
					paintIcon(expandedFoldIcon, g, x, y, false);
				}
				paintFoldArmed = false;
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
		// - Paint that line's indicator, if appropriate.  Increment y to be
		//   just below the line we just painted (i.e., the beginning of the
		//   next logical line's view area).
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
		Document doc = textArea.getDocument();
		Element root = doc.getDefaultRootElement();
		int topPosition = textArea.viewToModel(
								new Point(visibleRect.x,visibleRect.y));
		int topLine = root.getElementIndex(topPosition);
		int cellHeight = textArea.getLineHeight();
		FoldManager fm = ((RSyntaxTextArea)textArea).getFoldManager();

		// Compute the y at which to begin painting text, taking into account
		// that 1 logical line => at least 1 physical line, so it may be that
		// y<0.  The computed y-value is the y-value of the top of the first
		// (possibly) partially-visible view.
		Rectangle visibleEditorRect = ui.getVisibleEditorRect();
		Rectangle r = LineNumberList.getChildViewBounds(v, topLine,
												visibleEditorRect);
		int y = r.y;
		y += (cellHeight-collapsedFoldIcon.getIconHeight())/2;

		int visibleBottom = visibleRect.y + visibleRect.height;
		int line = topLine;
		boolean paintingOutlineLine = getShowArmedFoldRange() &&
			foldWithOutlineShowing!=null &&
			foldWithOutlineShowing.containsLine(line);
		int lineCount = root.getElementCount();

		while (y<visibleBottom && line<lineCount) {

			int curLineH = LineNumberList.getChildViewBounds(v, line,
					visibleEditorRect).height;

			if (paintingOutlineLine) {
				g.setColor(getForeground());
				int w2 = width / 2;
				if (line==foldWithOutlineShowing.getEndLine()) {
					int y2 = y + curLineH - cellHeight/2;
					g.drawLine(w2,y, w2,y2);
					g.drawLine(w2,y2, width-2,y2);
					paintingOutlineLine = false;
				}
				else {
					g.drawLine(w2,y, w2,y+curLineH);
				}
			}

			Fold fold = fm.getFoldForLine(line);
			if (fold!=null) {
				if (fold==foldWithOutlineShowing) {
					if (!fold.isCollapsed()) {
						paintingOutlineLine = getShowArmedFoldRange();
						if (paintingOutlineLine) {
							g.setColor(getForeground());
							int w2 = width / 2;
							g.drawLine(w2, y + cellHeight / 2, w2, y + curLineH);
						}
					}
					if (mouseOverFoldIcon) {
						paintFoldArmed = true;
					}
				}
				if (fold.isCollapsed()) {
					int x = (width - collapsedFoldIcon.getIconWidth()) / 2;
					paintIcon(collapsedFoldIcon, g, x, y, true);
					y += LineNumberList.getChildViewBounds(v, line,
								visibleEditorRect).height;
					line += fold.getLineCount() + 1;
				}
				else {
					if (getPaintExpandedFolds()) {
						int x = (width - expandedFoldIcon.getIconWidth()) / 2;
						paintIcon(expandedFoldIcon, g, x, y, false);
					}
					y += curLineH;
					line++;
				}
				paintFoldArmed = false;
			}
			else {
				y += curLineH;
				line++;
			}
		}

	}


	private void paintIcon(FoldIndicatorIcon icon, Graphics g, int x, int y, boolean collapsed) {

		Graphics2D g2d = (Graphics2D)g;
		Composite orig = g2d.getComposite();

		if (!collapsed) {
			if (collapsedFoldIconAlpha == 0) {
				return;
			}
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, collapsedFoldIconAlpha);
			g2d.setComposite(ac);
		}
		try {
			icon.setArmed(paintFoldArmed);
			icon.paintIcon(this, g, x, y);
		} finally {
			if (!collapsed) {
				g2d.setComposite(orig);
			}
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
	 * Adds to  the amount of additional size to give the left margin of this
	 * component.  This can be used to add blank space between this component
	 * and the line number indicator in the gutter.
	 *
	 * @param leftMargin The additional left margin.  This should be
	 *        {@code >= 0}.
	 * @see #getAdditionalLeftMargin()
	 */
	public void setAdditionalLeftMargin(int leftMargin) {

		if (leftMargin < 0) {
			throw new IllegalArgumentException("leftMargin must be >= 0");
		}

		this.additionalLeftMargin = leftMargin;
		revalidate();
	}


	/**
	 * Sets the foreground color used for armed folds.
	 *
	 * @param fg The new armed fold foreground.
	 * @see #getArmedForeground()
	 */
	public void setArmedForeground(Color fg) {
		if (fg==null) {
			fg = FoldIndicator.DEFAULT_FOREGROUND;
		}
		armedForeground = fg;
	}


	private void setCollapsedFoldIconAlpha(float collapsedFoldIconAlpha) {
		collapsedFoldIconAlpha = Math.max(0, Math.min(collapsedFoldIconAlpha, 1));
		if (collapsedFoldIconAlpha != this.collapsedFoldIconAlpha) {
			this.collapsedFoldIconAlpha = collapsedFoldIconAlpha;
			repaint();
		}
	}


	/**
	 * Sets the strategy to use for rendering expanded folds.
	 *
	 * @param strategy The strategy to use. This cannot be {@code null}.
	 * @see #getExpandedFoldRenderStrategy()
	 */
	public void setExpandedFoldRenderStrategy(ExpandedFoldRenderStrategy strategy) {
		if (strategy == null) {
			throw new NullPointerException("strategy cannot be null");
		}
		expandedFoldRenderStrategy = strategy;
		collapsedFoldIconAlpha = strategy == ExpandedFoldRenderStrategy.ALWAYS ? 1 : 0;
	}


	/**
	 * Sets the color to use for the "background" of armed fold icons.  This
	 * will be ignored if custom icons are used.
	 *
	 * @param bg The new background color.  If {@code null} is passed in,
	 *        there will be no special color for armed fold icons.
	 * @see #getFoldIconArmedBackground()
	 * @see #setFoldIconBackground(Color)
	 */
	public void setFoldIconArmedBackground(Color bg) {
		foldIconArmedBackground = bg;
	}


	/**
	 * Sets the color to use for the "background" of fold icons.  This will
	 * be ignored if custom icons are used.
	 *
	 * @param bg The new background color.  This should not be {@code null}.
	 * @see #getFoldIconBackground()
	 * @see #setFoldIconArmedBackground(Color)
	 */
	public void setFoldIconBackground(Color bg) {
		foldIconBackground = bg;
		repaint();
	}


	/**
	 * Sets the icons to use to represent collapsed and expanded folds.
	 * This method can be used for further customization after setting this
	 * component's general appearance via {@link #setStyle(FoldIndicatorStyle)}.
	 *
	 * @param collapsedIcon The collapsed fold icon.  This cannot be
	 *        <code>null</code>.
	 * @param expandedIcon The expanded fold icon.  This cannot be
	 *        <code>null</code>.
	 * @see #setStyle(FoldIndicatorStyle)
	 */
	public void setFoldIcons(FoldIndicatorIcon collapsedIcon, FoldIndicatorIcon expandedIcon) {
		this.collapsedFoldIcon = collapsedIcon;
		this.expandedFoldIcon = expandedIcon;
		revalidate(); // Icons may be different sizes.
		repaint();
	}


	/**
	 * Toggles whether a line should be drawn to show the range of lines contained
	 * in an expanded fold when it is armed (hovered over).
	 *
	 * @param show Whether to show an armed fold's range.
	 * @see #getShowArmedFoldRange()
	 */
	public void setShowArmedFoldRange(boolean show) {
		if (show != showArmedFoldRange) {
			showArmedFoldRange = show;
			repaint();
		}
	}


	/**
	 * Toggles whether tool tips should be displayed showing the contents of
	 * collapsed fold regions when the mouse hovers over a +/- icon.
	 *
	 * @param show Whether to show these tool tips.
	 * @see #getShowCollapsedRegionToolTips()
	 */
	public void setShowCollapsedRegionToolTips(boolean show) {
		if (show!=showFoldRegionTips) {
			if (show) {
				ToolTipManager.sharedInstance().registerComponent(this);
			}
			else {
				ToolTipManager.sharedInstance().unregisterComponent(this);
			}
			showFoldRegionTips = show;
		}
	}


	/**
	 * Toggles the presentation of this component. This method sets the icons used
	 * for fold regions to default values, amongst other configuration. To further
	 * customize these icons, see {@link #setFoldIcons(FoldIndicatorIcon, FoldIndicatorIcon)}.
	 *
	 * @param style The new presentation style.
	 * @see #setFoldIcons(FoldIndicatorIcon, FoldIndicatorIcon)
	 */
	void setStyle(FoldIndicatorStyle style) {

		switch (style) {

			case CLASSIC:
				setFoldIcons(new PlusMinusFoldIcon(true), new PlusMinusFoldIcon(false));
				setShowArmedFoldRange(true);
				setExpandedFoldRenderStrategy(ExpandedFoldRenderStrategy.ALWAYS);
				break;

			case MODERN:
				setFoldIcons(new ChevronFoldIcon(true), new ChevronFoldIcon(false));
				setShowArmedFoldRange(false);
				setExpandedFoldRenderStrategy(ExpandedFoldRenderStrategy.ON_HOVER);
				break;
		}
	}


	/**
	 * Overridden so we can track when code folding is enabled/disabled.
	 */
	@Override
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


	/**
	 * Updates the alpha used for this component's "collapsed" fold icons, if
	 * necessary.
	 */
	private final class AlphaRunnable implements ActionListener {

		private float delta;

		@Override
		public void actionPerformed(ActionEvent e) {
			setCollapsedFoldIconAlpha(collapsedFoldIconAlpha + delta);
			if (collapsedFoldIconAlpha == 0 || collapsedFoldIconAlpha == 1) {
				timer.stop();
			}
		}
	}


	/**
	 * Listens for events in this component.
	 */
	private class Listener extends MouseInputAdapter
			implements PropertyChangeListener {

		Listener(FoldIndicator fgc) {
			fgc.addMouseListener(this);
			fgc.addMouseMotionListener(this);
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			Point p = e.getPoint();
			int line = rowAtPoint(p);

			RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
			FoldManager fm = rsta.getFoldManager();

			Fold fold = fm.getFoldForLine(line);
			if (fold!=null) {
				fold.toggleCollapsedState();
				getGutter().repaint();
				textArea.repaint();
			}

		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (foldWithOutlineShowing!=null) {
				foldWithOutlineShowing = null;
				mouseOverFoldIcon = false;
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			boolean oldMouseOverFoldIcon = mouseOverFoldIcon;
			Fold newSelectedFold = findOpenFoldClosestTo(e.getPoint());
			if (newSelectedFold!=foldWithOutlineShowing &&
					newSelectedFold!=null && !newSelectedFold.isOnSingleLine()) {
				foldWithOutlineShowing = newSelectedFold;
				repaint();
			}
			else if (mouseOverFoldIcon != oldMouseOverFoldIcon) {
				repaint();
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			// Whether folding is enabled in the editor has changed.
			repaint();
		}

	}


}
