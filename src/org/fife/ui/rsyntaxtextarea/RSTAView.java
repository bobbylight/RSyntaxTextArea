/*
 * 02/10/2009
 *
 * RSTAView.java - An <code>RSyntaxTextArea</code> view.
 * Copyright (C) 2003 Robert Futrell
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

import java.awt.Rectangle;

import javax.swing.text.BadLocationException;


/**
 * Utility methods for RSyntaxTextArea's views.
 *
 * @author Robert Futrell
 * @version 1.0
 */
interface RSTAView {


	/**
	 * Returns the y-coordinate of the line containing a specified offset.<p>
	 *
	 * This method is quicker than using traditional
	 * <code>modelToView(int)</code> calls, as the entire bounding box isn't
	 * computed.
	 *
	 * @param alloc The area the text area can render into.
	 * @param offs The offset info the document.
	 * @return The y-coordinate of the top of the offset, or <code>-1</code> if
	 *         this text area doesn't yet have a positive size.
	 * @throws BadLocationException If <code>offs</code> isn't a valid offset
	 *         into the document.
	 */
	public int yForLineContaining(Rectangle alloc, int offs)
											throws BadLocationException;


}