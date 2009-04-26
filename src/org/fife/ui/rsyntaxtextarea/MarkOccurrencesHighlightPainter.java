package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
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
class MarkOccurrencesHighlightPainter extends
							DefaultHighlighter.DefaultHighlightPainter {

	/**
	 * DefaultHighlightPainter doesn't allow changing color, so we must cache
	 * ours here.
	 */
	private Color color;

	
	/**
	 * Constructor.
	 *
	 * @param color The color to draw the bounding boxes with.  This cannot
	 *        be <code>null</code>.
	 */
	public MarkOccurrencesHighlightPainter() {
		super(Color.BLUE);
	}


	/**
	 * Returns the color to paint with.
	 *
	 * @return The color.
	 * @see #setColor(Color)
	 */
	public Color getColor() {
		return color;
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
		try {
			// --- determine locations ---
			Shape shape = view.modelToView(p0, Position.Bias.Forward, p1,
					Position.Bias.Backward, viewBounds);
			Rectangle r = (shape instanceof Rectangle) ? (Rectangle) shape
					: shape.getBounds();
			g.fillRect(r.x, r.y, r.width, r.height);
			return r;
		} catch (BadLocationException e) { // Never happens
			e.printStackTrace();
			return null;
		}

	}


	/**
	 * Sets the color to paint the bounding boxes with.
	 *
	 * @param color The new color.  This cannot be <code>null</code>.
	 * @see #getColor()
	 */
	public void setColor(Color color) {
		if (color==null) {
			throw new IllegalArgumentException("color cannot be null");
		}
		this.color = color;
	}


}