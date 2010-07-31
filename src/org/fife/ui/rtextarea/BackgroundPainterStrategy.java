/*
 * 01/22/2005
 *
 * BackgroundPainterStrategy.java - Renders an RTextAreaBase's background
 * using some strategy.
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

import java.awt.Graphics;
import java.awt.Rectangle;


/**
 * Interface for classes that paint the background of an
 * <code>RTextAreaBase</code>.  The Strategy pattern is used for this
 * object because the background can be painted as a solid color, as
 * an image, and possibly other ways (gradients, animated images, etc.).
 * When a method to change the background of an <code>RTextAreaBase</code>
 * instance is called (such as <code>setBackground</code>,
 * <code>setBackgroundImage</code> or <code>setBackgoundObject</code>),
 * the correct strategy is then created and used to paint its background.
 *
 * @author Robert Futrell
 * @version 0.1
 * @see org.fife.ui.rtextarea.ImageBackgroundPainterStrategy
 * @see org.fife.ui.rtextarea.ColorBackgroundPainterStrategy
 */
public interface BackgroundPainterStrategy {


	/**
	 * Paints the background.
	 *
	 * @param g The graphics context.
	 * @param bounds The bounds of the object whose backgrouns we're
	 *        painting.
	 */
	public void paint(Graphics g, Rectangle bounds);


}