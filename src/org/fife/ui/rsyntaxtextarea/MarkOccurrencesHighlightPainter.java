/*
 * 10/01/2009
 *
 * MarkOccurrencesHighlightPainter.java - Renders "marked occurrences."
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

//import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;


/**
 * Highlight painter that renders "mark occurrences."
 *
 * @author Robert Futrell
 * @version 1.0
 */
/*
 * NOTE: This implementation is a "hack" so typing at the "end" of the highlight
 * does not extend it to include the newly-typed chars, which is the standard
 * behavior of Swing Highlights.
 */
class MarkOccurrencesHighlightPainter extends ChangeableColorHighlightPainter {

	private Color borderColor;
//	private BasicStroke stroke;


	/**
	 * Constructor.
	 *
	 * @param color The color to draw the bounding boxes with.  This cannot
	 *        be <code>null</code>.
	 */
	public MarkOccurrencesHighlightPainter() {
		super(Color.BLUE);
//		float[] dash = { 6, 4 };
//		stroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
//							BasicStroke.JOIN_MITER, 1, dash, 0);
	}


	/**
	 * {@inheritDoc}
	 */
	public Shape paintLayer(Graphics g, int p0, int p1, Shape viewBounds,
								JTextComponent c, View view) {

		g.setColor(getColor());
		p1++; // Workaround for Java Highlight issues.

		// This special case isn't needed for most standard Swing Views (which
		// always return a width of 1 for modelToView() calls), but it is
		// needed for RSTA views, which actually return the width of chars for
		// modelToView calls.  But this should be faster anyway, as we
		// short-circuit and do only one modelToView() for one offset.
		if (p0==p1) {
			try {
				Shape s = view.modelToView(p0, viewBounds,
											Position.Bias.Forward);
				Rectangle r = s.getBounds();
				g.drawLine(r.x, r.y, r.x, r.y+r.height);
				return r;
			} catch (BadLocationException ble) {
				ble.printStackTrace(); // Never happens
				return null;
			}
		}

		if (p0 == view.getStartOffset() && p1 == view.getEndOffset()) {
			// Contained in view, can just use bounds.
			Rectangle alloc;
			if (viewBounds instanceof Rectangle) {
				alloc = (Rectangle) viewBounds;
			} else {
				alloc = viewBounds.getBounds();
			}
			g.fillRect(alloc.x, alloc.y, alloc.width, alloc.height);
			return alloc;
		}

		// Should only render part of View.
		Graphics2D g2d = (Graphics2D)g;
		try {
			// --- determine locations ---
			Shape shape = view.modelToView(p0, Position.Bias.Forward, p1,
					Position.Bias.Backward, viewBounds);
			Rectangle r = (shape instanceof Rectangle) ? (Rectangle) shape
												: shape.getBounds();
			g2d.fillRect(r.x, r.y, r.width, r.height);
			g2d.setColor(borderColor);
//			Stroke oldStroke = g2d.getStroke();
//			g2d.setStroke(stroke);
			g2d.drawRect(r.x,r.y, r.width-1,r.height-1);
//			g2d.setStroke(oldStroke);
			return r;
		} catch (BadLocationException e) { // Never happens
			e.printStackTrace();
			return null;
		}

	}


	/**
	 * {@inheritDoc}
	 */
	public void setColor(Color c) {
		super.setColor(c);
		borderColor = c.darker();
	}


}