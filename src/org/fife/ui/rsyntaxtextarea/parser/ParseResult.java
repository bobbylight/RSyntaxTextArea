/*
 * 07/27/2009
 *
 * ParseResult.java - The result of a Parser parsing some section of an
 * RSyntaxTextArea.
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
package org.fife.ui.rsyntaxtextarea.parser;

import java.util.List;


/**
 * The result from a {@link Parser}.  This contains the section of lines
 * parsed and any notices for that section.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface ParseResult {


	/**
	 * Returns an error that occurred while parsing the document, if any.
	 *
	 * @return The error, or <code>null</code> if the document was
	 *         successfully parsed.
	 */
	public Exception getError();


	/**
	 * Returns the first line parsed.  All parser implementations should
	 * currently set this to <code>0</code> and parse the entire document.
	 *
	 * @return The first line parsed.
	 * @see #getLastLineParsed()
	 */
	public int getFirstLineParsed();


	/**
	 * Returns the first line parsed.  All parser implementations should
	 * currently set this to the document's line count and parse the entire
	 * document.
	 *
	 * @return The last line parsed.
	 * @see #getFirstLineParsed()
	 */
	public int getLastLineParsed();


	/**
	 * Returns the notices for the parsed section.
	 *
	 * @return The notices.
	 */
	public List getNotices();


	/**
	 * Returns the parser that generated these notices.
	 *
	 * @return The parser.
	 */
	public Parser getParser();


	/**
	 * Returns the amount of time this parser took to parse the specified
	 * range of text.  This is an optional operation; parsers are permitted
	 * to return <code>0</code> for this value.
	 *
	 * @return The parse time, in milliseconds, or <code>0</code> if the
	 *         parse time was not recorded.
	 */
	public long getParseTime();


}