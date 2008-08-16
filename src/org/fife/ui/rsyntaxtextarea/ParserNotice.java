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
package org.fife.ui.rsyntaxtextarea;


/**
 * A notice (e.g., a warning or error) from a parser.
 *
 * @author Robert Futrell
 * @version 0.1
 */
public class ParserNotice {

	private int offset;
	private int length;
	private int line;
	private int column;
	private String message;


	/**
	 * Constructor.
	 *
	 * @param message The message.
	 * @param offset The offset in the input stream of the code the
	 *        message is concerned with.
	 * @param length The length of the code the message is concerned with.
	 */
	public ParserNotice(String message, int offset, int length) {
		this(message, offset,length, -1,-1);
	}


	/**
	 * Constructor.
	 *
	 * @param message The message.
	 * @param offset The offset in the input stream of the code the
	 *        message is concerned with.
	 * @param length The length of the code the message is concerned with.
	 * @param line The line number of the notice, <code>-1</code> if none.
	 * @param column The character-offset into the line of the notice,
	 *        <code>-1</code> if none.
	 */
	public ParserNotice(String message, int offset, int length,
					int line, int column) {
		this.message = message;
		this.offset = offset;
		this.length = length;
		this.line = line;
		this.column = column;
	}


	/**
	 * Returns whether this parser notice contains the specified location
	 * in the document.
	 *
	 * @param pos The position in the document.
	 * @return Whether the position is contained.
	 */
	public boolean containsPosition(int pos) {
		return offset<=pos && pos<(offset+length);
	}


	/**
	 * Returns the character offset into the line of the parser notice,
	 * if any.
	 *
	 * @return The column.
	 */
	public int getColumn() {
		return column;
	}


	/**
	 * Returns the length of the code the message is concerned with.
	 *
 	 * @return The length of the code the message is concerned with.
	 */
	public int getLength() {
		return length;
	}


	/**
	 * Returns the line number the notice is about, if any.
	 *
	 * @return The line number.
	 */
	public int getLine() {
		return line;
	}


	/**
	 * Returns the message from the parser.
	 *
	 * @return The message from the parser.
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * Returns the offset of the code the message is concerned with.
	 *
	 * @return The offset.
	 */
	public int getOffset() {
		return offset;
	}


	/**
	 * Returns a string representation of this parser notice.
	 *
	 * @return This parser notice as a string.
	 */
	public String toString() {
		return "Offset " + getOffset() + ": " +
				getMessage();
	}


}