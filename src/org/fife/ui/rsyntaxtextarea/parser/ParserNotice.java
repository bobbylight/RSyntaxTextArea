/*
 * 09/23/2005
 *
 * ParserNotice.java - A notice (i.e, and error or warning) from a parser.
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
package org.fife.ui.rsyntaxtextarea.parser;

import java.awt.Color;


/**
 * A notice (e.g., a warning or error) from a parser.
 *
 * @author Robert Futrell
 * @version 0.1
 */
public interface ParserNotice extends Comparable {

	/**
	 * Indicates an info notice.
	 */
	public static final int INFO		= 2;

	/**
	 * Indicates a warning notice.
	 */
	public static final int WARNING		= 1;

	/**
	 * Indicates an error notice.
	 */
	public static final int ERROR		= 0;


	/**
	 * Returns whether this parser notice contains the specified location
	 * in the document.
	 *
	 * @param pos The position in the document.
	 * @return Whether the position is contained.  This will always return
	 *         <code>false</code> if {@link #getOffset()} returns
	 *         <code>-1</code>.
	 */
	public boolean containsPosition(int pos);


	/**
	 * Returns the color to use when painting this notice.
	 *
	 * @return The color.
	 */
	public Color getColor();


	/**
	 * Returns the length of the code the message is concerned with.
	 *
 	 * @return The length of the code the message is concerned with, or
 	 *         <code>-1</code> if unknown.
 	 * @see #getOffset()
 	 * @see #getLine()
	 */
	public int getLength();


	/**
	 * Returns the level of this notice.
	 *
	 * @return One of {@link #INFO}, {@link #WARNING} OR {@link #ERROR}.
	 */
	public int getLevel();


	/**
	 * Returns the line number the notice is about.
	 *
	 * @return The line number.
	 */
	public int getLine();


	/**
	 * Returns the message from the parser.
	 *
	 * @return The message from the parser.
	 */
	public String getMessage();


	/**
	 * Returns the offset of the code the message is concerned with.
	 *
	 * @return The offset, or <code>-1</code> if unknown.
	 * @see #getLength()
	 * @see #getLine()
	 */
	public int getOffset();


	/**
	 * Returns the parser that created this message.
	 *
	 * @return The parser.
	 */
	public Parser getParser();


	/**
	 * Whether a squiggle underline should be drawn in the editor for this
	 * notice.
	 *
	 * @return Whether a squiggle underline should be drawn.
	 */
	public boolean getShowInEditor();


	/**
	 * Returns the tooltip text to display for this notice.
	 *
	 * @return The tool tip text.  If none has been explicitly set, this
	 *         method returns the same text as {@link #getMessage()}.
	 */
	public String getToolTipText();


}