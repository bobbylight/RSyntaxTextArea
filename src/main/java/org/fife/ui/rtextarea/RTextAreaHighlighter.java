/*
 * 10/13/2013
 *
 * RTextAreaHighlighter.java - Highlighter for RTextAreas.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.TextUI;
import javax.swing.plaf.basic.BasicTextUI.BasicHighlighter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.View;

import org.fife.ui.rsyntaxtextarea.DocumentRange;


/**
 * The highlighter implementation used by {@link RTextArea}s.  It knows to
 * always paint "mark all" highlights below selection highlights.<p>
 *
 * Most of this code is copied from javax.swing.text.DefaultHighlighter;
 * unfortunately, we cannot re-use much of it since it is package private.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RTextAreaHighlighter extends BasicHighlighter {

	/**
	 * The text component we are the highlighter for.
	 */
	protected RTextArea textArea;

	/**
	 * The "mark all" highlights (to be painted separately from other
	 * highlights).
	 */
	private List<HighlightInfo> markAllHighlights;

	/**
	 * Constructor.
	 */
	public RTextAreaHighlighter() {
		markAllHighlights = new ArrayList<HighlightInfo>();
	}


	/**
	 * Adds a special "marked occurrence" highlight.
	 *
	 * @param start
	 * @param end
	 * @param p
	 * @return A tag to reference the highlight later.
	 * @throws BadLocationException
	 * @see #clearMarkAllHighlights()
	 */
	Object addMarkAllHighlight(int start, int end, HighlightPainter p)
			throws BadLocationException {
		Document doc = textArea.getDocument();
		TextUI mapper = textArea.getUI();
		// Always layered highlights for marked occurrences.
		HighlightInfoImpl i = new LayeredHighlightInfoImpl();
		i.setPainter(p);
		i.p0 = doc.createPosition(start);
		// HACK: Use "end-1" to prevent chars the user types at the "end" of
		// the highlight to be absorbed into the highlight (default Highlight
		// behavior).
		i.p1 = doc.createPosition(end-1);
		markAllHighlights.add(i);
		mapper.damageRange(textArea, start, end);
		return i;
	}


	/**
	 * Removes all "mark all" highlights from the view.
	 *
	 * @see #addMarkAllHighlight(int, int, javax.swing.text.Highlighter.HighlightPainter)
	 */
	void clearMarkAllHighlights() {
		// Don't remove via an iterator; since our List is an ArrayList, this
		// implies tons of System.arrayCopy()s
		for (HighlightInfo info : markAllHighlights) {
			repaintListHighlight(info);
		}
		markAllHighlights.clear();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deinstall(JTextComponent c) {
		this.textArea = null;
		markAllHighlights.clear();
	}


	/**
	 * Returns the number of "mark all" highlights currently shown in the
	 * editor.
	 *
	 * @return The "mark all" highlight count.
	 */
	public int getMarkAllHighlightCount() {
		return markAllHighlights.size();
	}


	/**
	 * Returns a list of "mark all" highlights in the text area.  If there are
	 * no such highlights, this will be an empty list.
	 *
	 * @return The list of "mark all" highlight ranges.
	 */
	public List<DocumentRange> getMarkAllHighlightRanges() {
		List<DocumentRange> list = new ArrayList<DocumentRange>(
				markAllHighlights.size());
		for (HighlightInfo info : markAllHighlights) {
			int start = info.getStartOffset();
			int end = info.getEndOffset() + 1; // HACK
			DocumentRange range = new DocumentRange(start, end);
			list.add(range);
		}
		return list;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void install(JTextComponent c) {
		super.install(c);
		this.textArea = (RTextArea)c;
	}


	/**
	 * When leaf Views (such as LabelView) are rendering they should
	 * call into this method. If a highlight is in the given region it will
	 * be drawn immediately.
	 *
	 * @param g Graphics used to draw
	 * @param lineStart starting offset of view
	 * @param lineEnd ending offset of view
	 * @param viewBounds Bounds of View
	 * @param editor JTextComponent
	 * @param view View instance being rendered
	 */
	@Override
	public void paintLayeredHighlights(Graphics g, int lineStart, int lineEnd,
						Shape viewBounds, JTextComponent editor, View view) {
		paintListLayered(g, lineStart,lineEnd, viewBounds, editor, view,
				markAllHighlights);
		super.paintLayeredHighlights(g, lineStart, lineEnd, viewBounds, editor,
				view);
	}


	protected void paintListLayered(Graphics g, int lineStart, int lineEnd,
			Shape viewBounds, JTextComponent editor, View view,
			List<? extends HighlightInfo> highlights) {
		for (int i=highlights.size()-1; i>=0; i--) {
			HighlightInfo tag = highlights.get(i);
			if (tag instanceof LayeredHighlightInfo) {
				LayeredHighlightInfo lhi = (LayeredHighlightInfo)tag;
				int highlightStart = lhi.getStartOffset();
				int highlightEnd = lhi.getEndOffset() + 1; // "+1" workaround for Java highlight issues
				if ((lineStart < highlightStart && lineEnd > highlightStart) ||
						(lineStart >= highlightStart && lineStart < highlightEnd)) {
					lhi.paintLayeredHighlights(g, lineStart, lineEnd, viewBounds,
									editor, view);
				}
			}
		}
	}


	protected void repaintListHighlight(HighlightInfo info) {
		// Note: We're relying on implementation here, not interface.  Yuck...
		if (info instanceof LayeredHighlightInfoImpl) {
			LayeredHighlightInfoImpl lhi = (LayeredHighlightInfoImpl)info;
		    if (lhi.width > 0 && lhi.height > 0) {
		    	textArea.repaint(lhi.x, lhi.y, lhi.width, lhi.height);
		    }
		}
		else {
			TextUI ui = textArea.getUI();
			ui.damageRange(textArea, info.getStartOffset(),info.getEndOffset());
			//safeDamageRange(info.p0, info.p1);
		}
	}


	/**
	 * Information about a highlight being painted by this highlighter.
	 */
	public interface HighlightInfo extends Highlighter.Highlight {}


	/**
	 * Information about a layered highlight being painted by this highlighter.
	 */
	public interface LayeredHighlightInfo extends HighlightInfo {

		/**
		 * Restricts the region based on the receivers offsets and messages
		 * the painter to paint the region.
		 */
		void paintLayeredHighlights(Graphics g, int p0, int p1,
					Shape viewBounds, JTextComponent editor, View view);

	}


	/**
	 * A straightforward implementation of <code>HighlightInfo</code>.
	 */
	protected static class HighlightInfoImpl implements HighlightInfo {

		private Position p0;
		private Position p1;
		private Highlighter.HighlightPainter painter;

		/** To be extended by subclasses. */
		public Color getColor() {
			return null;
		}

		@Override
		public int getStartOffset() {
			return p0.getOffset();
		}

		@Override
		public int getEndOffset() {
			return p1.getOffset();
		}

		@Override
		public Highlighter.HighlightPainter getPainter() {
			return painter;
		}

		public void setStartOffset(Position startOffset) {
			this.p0 = startOffset;
		}

		public void setEndOffset(Position endOffset) {
			this.p1 = endOffset;
		}

		public void setPainter(Highlighter.HighlightPainter painter) {
			this.painter = painter;
		}

	}


	/**
	 * A straightforward implementation of <code>HighlightInfo</code> for
	 * painting layered highlights.
	 */
	@SuppressWarnings({ "checkstyle:visibilitymodifier" })
	protected static class LayeredHighlightInfoImpl extends HighlightInfoImpl
			implements LayeredHighlightInfo {

		/*
		 * NOTE: This implementation is a "hack" so typing at the "end" of the highlight
		 * does not extend it to include the newly-typed chars, which is the standard
		 * behavior of Swing Highlights.  It assumes that the "p1" Position set is
		 * actually 1 char too short, and will render the selection as if that "extra"
		 * char should be highlighted.
		 */

		public int x;
		public int y;
		public int width;
		public int height;

		void union(Shape bounds) {
			if (bounds == null) {
				return;
			}
			Rectangle alloc = (bounds instanceof Rectangle) ?
					(Rectangle)bounds : bounds.getBounds();
			if (width == 0 || height == 0) {
				x = alloc.x;
				y = alloc.y;
				width = alloc.width;
				height = alloc.height;
			}
			else {
				width = Math.max(x + width, alloc.x + alloc.width);
				height = Math.max(y + height, alloc.y + alloc.height);
				x = Math.min(x, alloc.x);
				width -= x;
				y = Math.min(y, alloc.y);
				height -= y;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void paintLayeredHighlights(Graphics g, int p0, int p1,
									Shape viewBounds, JTextComponent editor,
									View view) {
			int start = getStartOffset();
			int end = getEndOffset();
			end++; // Workaround for Java highlight issues
			// Restrict the region to what we represent
			p0 = Math.max(start, p0);
			p1 = Math.min(end, p1);
			if (getColor()!=null &&
					getPainter() instanceof ChangeableHighlightPainter) {
				((ChangeableHighlightPainter)getPainter()).setPaint(getColor());
			}
			// Paint the appropriate region using the painter and union
			// the effected region with our bounds.
			union(((LayeredHighlighter.LayerPainter)getPainter()).paintLayer(
								g, p0, p1, viewBounds, editor, view));
		}

	}


}