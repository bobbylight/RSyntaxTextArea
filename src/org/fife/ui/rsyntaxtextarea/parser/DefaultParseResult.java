/*
 * 07/27/2009
 *
 * DefaultParseResult.java - A basic implementation of a ParseResult.
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

import java.util.ArrayList;
import java.util.List;


/**
 * A basic implementation of {@link ParseResult}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DefaultParseResult implements ParseResult {

	private Parser parser;
	private int firstLineParsed;
	private int lastLineParsed;
	private List notices;
	private long parseTime;
	private Exception error;


	public DefaultParseResult(Parser parser) {
		this.parser = parser;
		notices = new ArrayList();
	}


	/**
	 * Adds a parser notice.
	 *
	 * @param notice The new notice.
	 * @see #clearNotices()
	 */
	public void addNotice(ParserNotice notice) {
		notices.add(notice);
	}


	/**
	 * Clears any parser notices in this result.
	 *
	 * @see #addNotice(ParserNotice)
	 */
	public void clearNotices() {
		notices.clear();
	}


	/**
	 * {@inheritDoc}
	 */
	public Exception getError() {
		return error;
	}


	/**
	 * {@inheritDoc}
	 */
	public int getFirstLineParsed() {
		return firstLineParsed;
	}


	/**
	 * {@inheritDoc}
	 */
	public int getLastLineParsed() {
		return lastLineParsed;
	}


	/**
	 * {@inheritDoc}
	 */
	public List getNotices() {
		return notices;
	}


	/**
	 * {@inheritDoc}
	 */
	public long getParseTime() {
		return parseTime;
	}


	/**
	 * {@inheritDoc}
	 */
	public Parser getParser() {
		return parser;
	}


	/**
	 * Sets the error that occurred when last parsing the document, if
	 * any.
	 *
	 * @param e The error that occurred, or <code>null</code> if no error
	 *         occurred.
	 */
	public void setError(Exception e) {
		this.error = e;
	}


	/**
	 * Sets the amount of time it took for this parser to parse the document.
	 *
	 * @param time The amount of time, in milliseconds.
	 * @see #getParseTime()
	 */
	public void setParseTime(long time) {
		parseTime = time;
	}


	/**
	 * Sets the line range parsed.
	 *
	 * @param first The first line parsed, inclusive.
	 * @param last The last line parsed, inclusive.
	 * @see #getFirstLineParsed()
	 * @see #getLastLineParsed()
	 */
	public void setParsedLines(int first, int last) {
		firstLineParsed = first;
		lastLineParsed = last;
	}


}