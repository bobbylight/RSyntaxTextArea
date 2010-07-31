/*
 * 01/22/2005
 *
 * ColorBackgroundPainterStrategy.java - Renders an RTextAreaBase's background
 * as a single color.
 * Copyright (C) 2005 Robert Futrell
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
import java.awt.Graphics;
import java.awt.Rectangle;


/**
 * A strategy for painting the background of an <code>RTextAreaBase</code>
 * as a solid color.  The default background for <code>RTextAreaBase</code>s
 * is this strategy using the color white.
 *
 * @author Robert Futrell
 * @version 0.1
 * @see org.fife.ui.rtextarea.ImageBackgroundPainterStrategy
 */
public class ColorBackgroundPainterStrategy
				implements BackgroundPainterStrategy {

	private Color color;


	/**
	 * Constructor.
	 *
	 * @param color The color to use when painting the background.
	 */
	public ColorBackgroundPainterStrategy(Color color) {
		setColor(color);
	}


	/**
	 * Returns whether or not the specified object is equivalent to
	 * this one.
	 *
	 * @param o2 The object to which to compare.
	 * @return Whether <code>o2</code> is another
	 *         <code>ColorBackgroundPainterStrategy</code> representing
	 *         the same color as this one.
	 */
	public boolean equals(Object o2) {
		return o2!=null &&
			(o2 instanceof ColorBackgroundPainterStrategy) &&
			this.color.equals(
				((ColorBackgroundPainterStrategy)o2).getColor());
	}


	/**
	 * Returns the color used to paint the background.
	 *
	 * @return The color.
	 * @see #setColor
	 */
	public Color getColor() {
		return color;
	}


	/**
	 * Returns the hash code to use when placing an object of this type into
	 * hash maps.  This method is implemented since we overrode
	 * {@link #equals(Object)}, to keep FindBugs happy.
	 *
	 * @return The hash code.
	 */
	public int hashCode() {
		return color.hashCode();
	}


	/**
	 * Paints the background.
	 *
	 * @param g The graphics context.
	 * @param bounds The bounds of the object whose backgrouns we're
	 *        painting.
	 */
	public void paint(Graphics g, Rectangle bounds) {
		Color temp = g.getColor();
		g.setColor(color);
		g.fillRect(bounds.x,bounds.y, bounds.width,bounds.height);
		g.setColor(temp);
	}


	/**
	 * Sets the color used to paint the background.
	 *
	 * @param color The color to use.
	 * @see #getColor
	 */
	public void setColor(Color color) {
		this.color = color;
	}


}