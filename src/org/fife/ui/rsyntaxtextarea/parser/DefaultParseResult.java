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
	private List notices;


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
	public List getNotices() {
		return notices;
	}


	/**
	 * {@inheritDoc}
	 */
	public Parser getParser() {
		return parser;
	}


}