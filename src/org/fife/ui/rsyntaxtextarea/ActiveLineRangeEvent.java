/*
 * 02/06/2011
 *
 * ActiveLineRangeEvent.java - Notifies listeners of an "active line range"
 * change in an RSyntaxTextArea.
 * Copyright (C) 2011 Robert Futrell
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

import java.util.EventObject;


/**
 * The event fired by {@link RSyntaxTextArea}s when the active line range
 * changes.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class ActiveLineRangeEvent extends EventObject {

	private int min;
	private int max;


	/**
	 * Constructor.
	 *
	 * @param source The text area.
	 * @param min The first line in the active line range, or
	 *        <code>-1</code> if the line range is being cleared.
	 * @param max The last line in the active line range, or
	 *        <code>-1</code> if the line range is being cleared.
	 */
	public ActiveLineRangeEvent(RSyntaxTextArea source, int min, int max) {
		super(source);
		this.min = min;
		this.max = max;
	}


	/**
	 * Returns the last line in the active line range.
	 *
	 * @return The last line, or <code>-1</code> if the range is being
	 *         cleared.
	 * @see #getMin()
	 */
	public int getMax() {
		return max;
	}


	/**
	 * Returns the first line in the active line range.
	 *
	 * @return The first line, or <code>-1</code> if the range is being
	 *         cleared.
	 * @see #getMax()
	 */
	public int getMin() {
		return min;
	}


}