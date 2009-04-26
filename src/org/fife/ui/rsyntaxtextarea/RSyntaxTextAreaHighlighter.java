/*
 * 04/23/2009
 *
 * RSyntaxTextAreaHighlighter.java - Highlighter for RTextAreas.
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
package org.fife.ui.rsyntaxtextarea;

import java.awt.Graphics;
import java.awt.Insets;
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

import org.fife.ui.rtextarea.RTextArea;


/**
 * The highlighter implementation used by {@link RSyntaxTextArea}s.  It knows to
 * always paint "marked occurrences" highlights below selection highlights,
 * and squiggle underline highlights above all other highlights.<p>
 *
 * Most of this code is copied from javax.swing.text.DefaultHighlighter;
 * unfortunately, we cannot re-use much of it since it is package private.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RSyntaxTextAreaHighlighter extends BasicHighlighter {

	/**
	 * The text component we are the highlighter for.
	 */
	private RTextArea textArea;

	/**
	 * Marked occurrences in the document (to be painted separately from
	 * other highlights).
	 */
	private List markedOccurrences;


	/**
	 * Constructor.
	 */
	public RSyntaxTextAreaHighlighter() {
		markedOccurrences = new ArrayList();
	}


	/**
	 * Adds a special "marked occurrence" highlight.
	 *
	 * @param start
	 * @param end
	 * @param p
	 * @return
	 * @throws BadLocationException
	 * @see {@link #removeMarkOccurrencesHighlight(Object)}
	 */
	Object addMarkedOccurrenceHighlight(int start, int end,
			MarkOccurrencesHighlightPainter p) throws BadLocationException {
		Document doc = textArea.getDocument();
		TextUI mapper = textArea.getUI();
		HighlightInfo i = getDrawsLayeredHighlights() ?
						new LayeredHighlightInfo() : new HighlightInfo();
		i.painter = p;
		i.p0 = doc.createPosition(start);
		// HACK: Use "end-1" to prevent chars the user types at the "end" of
		// the highlight to be absorbed into the highlight (default Highlight
		// behavior).
		i.p1 = doc.createPosition(end-1);
		markedOccurrences.add(i);
		mapper.damageRange(textArea, start, end);
		return i;
	}


	/**
	 * {@inheritDoc}
	 */
	public void deinstall(JTextComponent c) {
		this.textArea = null;
		markedOccurrences.clear();
	}


	/**
	 * {@inheritDoc}
	 */
	public void install(JTextComponent c) {
		super.install(c);
		this.textArea = (RTextArea)c;
	}


	/**
	 * Renders the highlights.
	 *
	 * @param g the graphics context
	 */
	public void paint(Graphics g) {

		int len = markedOccurrences.size();
		for (int i = 0; i < len; i++) {
			HighlightInfo info = (HighlightInfo) markedOccurrences.get(i);
			if (!(info instanceof LayeredHighlightInfo)) {
				// Avoid allocating unless we need it.
				Rectangle a = textArea.getBounds();
				Insets insets = textArea.getInsets();
				a.x = insets.left;
				a.y = insets.top;
				a.width -= insets.left + insets.right;
				a.height -= insets.top + insets.bottom;
				for (; i < len; i++) {
					info = (HighlightInfo)markedOccurrences.get(i);
					if (!(info instanceof LayeredHighlightInfo)) {
						Highlighter.HighlightPainter p = info.getPainter();
						p.paint(g, info.getStartOffset(), info.getEndOffset(),
								a, textArea);
					}
				}
		    }
		}

		super.paint(g);

	}


	/**
	 * When leaf Views (such as LabelView) are rendering they should
	 * call into this method. If a highlight is in the given region it will
	 * be drawn immediately.
	 *
	 * @param g Graphics used to draw
	 * @param p0 starting offset of view
	 * @param p1 ending offset of view
	 * @param viewBounds Bounds of View
	 * @param editor JTextComponent
	 * @param view View instance being rendered
	 */
	public void paintLayeredHighlights(Graphics g, int p0, int p1,
						Shape viewBounds, JTextComponent editor, View view) {

		for (int i=markedOccurrences.size()-1; i>=0; i--) {
			Object tag = markedOccurrences.get(i);
			if (tag instanceof LayeredHighlightInfo) {
				LayeredHighlightInfo lhi = (LayeredHighlightInfo)tag;
				int start = lhi.getStartOffset();
				int end = lhi.getEndOffset();
				if ((p0 < start && p1 > start) ||
						(p0 >= start && p0 < end)) {
					lhi.paintLayeredHighlights(g, p0, p1, viewBounds,
									editor, view);
				}
			}
		}

		super.paintLayeredHighlights(g, p0, p1, viewBounds, editor, view);

	}


	/**
	 * Removes a "marked occurrences" highlight from the view.
	 *
	 * @param tag The reference to the highlight
	 * @see #addMarkedOccurrenceHighlight(int, int, javax.swing.text.Highlighter.HighlightPainter)
	 */
	void removeMarkOccurrencesHighlight(Object tag) {
		if (tag instanceof LayeredHighlightInfo) {
			LayeredHighlightInfo lhi = (LayeredHighlightInfo)tag;
		    if (lhi.width > 0 && lhi.height > 0) {
		    	textArea.repaint(lhi.x, lhi.y, lhi.width, lhi.height);
		    }
		}
		else {
			HighlightInfo info = (HighlightInfo) tag;
			TextUI ui = textArea.getUI();
			ui.damageRange(textArea, info.getStartOffset(),info.getEndOffset());
			//safeDamageRange(info.p0, info.p1);
		}
		markedOccurrences.remove(tag);
    }


	private static class HighlightInfo implements Highlighter.Highlight {
	
		private Position p0;
		private Position p1;
		protected Highlighter.HighlightPainter painter;

		public int getStartOffset() {
			return p0.getOffset();
		}
		
		public int getEndOffset() {
			return p1.getOffset();
		}
		
		public Highlighter.HighlightPainter getPainter() {
			return painter;
		}
		
	}


	private static class LayeredHighlightInfo extends HighlightInfo {
	
		private int x;
		private int y;
		private int width;
		private int height;

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
		 * Restricts the region based on the receivers offsets and messages
		 * the painter to paint the region.
		 */
		void paintLayeredHighlights(Graphics g, int p0, int p1,
									Shape viewBounds, JTextComponent editor,
									View view) {
			int start = getStartOffset();
			int end = getEndOffset();
			// Restrict the region to what we represent
			p0 = Math.max(start, p0);
			p1 = Math.min(end, p1);
			// Paint the appropriate region using the painter and union
			// the effected region with our bounds.
			union(((LayeredHighlighter.LayerPainter)painter).paintLayer
								(g, p0, p1, viewBounds, editor, view));
		}
	
	}
	
	
}