/*
 * 07/23/2009
 *
 * ChangeableColorHighlightPainter.java - A highlighter whose color you can
 * change.
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

import java.awt.Color;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;


/**
 * A highlighter whose color you can change.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ChangeableColorHighlightPainter extends DefaultHighlightPainter {

	/**
	 * DefaultHighlightPainter doesn't allow changing color, so we must cache
	 * ours here.
	 */
	private Color color;


	/**
	 * Constructor.
	 *
	 * @param color The initial color to use.  This cannot be <code>null</code>.
	 */
	public ChangeableColorHighlightPainter(Color color) {
		super(color);
		setColor(color);
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