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
public class ParserNotice implements Comparable {

	private Parser parser;
	private int line;
	private int offset;
	private int length;
	private Color color;
	private String message;
	private String toolTipText;


	/**
	 * Constructor.
	 *
	 * @param parser The parser that created this notice.
	 * @param msg The text of the message.
	 * @param line The line number for the message.
	 */
	public ParserNotice(Parser parser, String msg, int line) {
		this(parser, msg, line, -1, -1);
	}


	/**
	 * Constructor.
	 *
	 * @param parser The parser that created this notice.
	 * @param message The message.
	 * @param line The line number corresponding to the message.
	 * @param offset The offset in the input stream of the code the
	 *        message is concerned with, or <code>-1</code> if unknown.
	 * @param length The length of the code the message is concerned with,
	 *        or <code>-1</code> if unknown.
	 */
	public ParserNotice(Parser parser, String message, int line, int offset,
						int length) {
		this.parser = parser;
		this.message = message;
		this.line = line;
		this.offset = offset;
		this.length = length;
	}


	/**
	 * Compares this parser notice to another.
	 *
	 * @param obj Another parser notice.
	 * @return How the two parser notices should be sorted relative to one
	 *         another.
	 */
	public int compareTo(Object obj) {
		int diff = -1;
		if (obj instanceof ParserNotice) {
			ParserNotice p2 = (ParserNotice)obj;
			diff = line - p2.line;
			if (diff==0) {
				diff = message.compareTo(p2.message);
			}
		}
		return diff;
	}


	/**
	 * Returns whether this parser notice contains the specified location
	 * in the document.
	 *
	 * @param pos The position in the document.
	 * @return Whether the position is contained.  This will always return
	 *         <code>false</code> if {@link #getOffset()} returns
	 *         <code>-1</code>.
	 */
	public boolean containsPosition(int pos) {
		return offset<=pos && pos<(offset+length);
	}


	/**
	 * Returns whether this parser notice is equal to another one.
	 *
	 * @param obj Another parser notice.
	 * @return Whether the two notices are equal.
	 */
	public boolean equals(Object obj) {
		return compareTo(obj)==0;
	}


	/**
	 * Returns the color to use when painting this notice.
	 *
	 * @return The color.
	 * @see #setColor(Color)
	 */
	public Color getColor() {
		return color;
	}


	/**
	 * Returns the length of the code the message is concerned with.
	 *
 	 * @return The length of the code the message is concerned with, or
 	 *         <code>-1</code> if unknown.
 	 * @see #getOffset()
 	 * @see #getLine()
	 */
	public int getLength() {
		return length;
	}


	/**
	 * Returns the line number the notice is about.
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
	 * @return The offset, or <code>-1</code> if unknown.
	 * @see #getLength()
	 * @see #getLine()
	 */
	public int getOffset() {
		return offset;
	}


	/**
	 * Returns the parser that created this message.
	 *
	 * @return The parser.
	 */
	public Parser getParser() {
		return parser;
	}


	/**
	 * Returns the tooltip text to display for this notice.
	 *
	 * @return The tool tip text.  If none has been explicitly set, this
	 *         method returns the same text as {@link #getMessage()}.
	 * @see #setToolTipText(String)
	 */
	public String getToolTipText() {
		return toolTipText!=null ? toolTipText : getMessage();
	}


	/**
	 * Returns the hash code for this notice.
	 *
	 * @return The hash code.
	 */
	public int hashCode() {
		return (line<<16) | offset;
	}


	/**
	 * Sets the color to use when painting this notice.
	 *
	 * @param color The color to use.
	 * @see #getColor()
	 */
	public void setColor(Color color) {
		this.color = color;
	}


	/**
	 * Sets the tooltip text to display for this notice.
	 *
	 * @param text The new tooltip text.  This can be HTML.  If this is
	 *        <code>null</code>, then tooltips will return the same text as
	 *        {@link #getMessage()}.
	 * @see #getToolTipText()
	 */
	public void setToolTipText(String text) {
		this.toolTipText = text;
	}


	/**
	 * Returns a string representation of this parser notice.
	 *
	 * @return This parser notice as a string.
	 */
	public String toString() {
		return "Line " + getLine() + ": " + getMessage();
	}


}