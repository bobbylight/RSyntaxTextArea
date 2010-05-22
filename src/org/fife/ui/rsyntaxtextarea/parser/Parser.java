/*
 * 09/23/2005
 *
 * Parser.java - An interface for a parser for RSyntaxTextArea.
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

import java.net.URL;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.focusabletip.FocusableTip;


/**
 * An interface for a parser for content in an {@link RSyntaxTextArea}.
 * A <code>Parser</code> returns a list of issues it finds in the text area's
 * content, which the text area can flag (e.g. squiggle underline).  It can
 * also return descriptions of the issues, to be used in tool tips.
 *
 * @author Robert Futrell
 * @version 0.5
 * @see AbstractParser
 */
public interface Parser {


	/**
	 * Returns the listener for hyperlink events from {@link FocusableTip}s,
	 * or <code>null</code> if none.
	 *
	 * @return The listener.
	 */
	public ExtendedHyperlinkListener getHyperlinkListener();


	/**
	 * Returns the base URL for any images displayed in returned
	 * {@link ParserNotice} HTML text.  Note that if a parser notice's text
	 * is not HTML, this URL is not used.
	 *
	 * @return The URL.  This may be <code>null</code>.
	 */
	public URL getImageBase();


	/**
	 * Returns whether this parser is enabled.  If this returns
	 * <code>false</code>, it will not be run.
	 *
	 * @return Whether this parser is enabled.
	 */
	public boolean isEnabled();


	/**
	 * Parses input from the specified document.
	 *
	 * @param doc The document to parse.  This document is in a read lock,
	 *        so it cannot be modified while parsing is occurring.
	 * @param style The language being rendered, such as
	 *        {@link SyntaxConstants#SYNTAX_STYLE_JAVA}.
	 * @return An object describing the section of the document parsed and the
	 *         results.  This is guaranteed to be non-<code>null</code>.
	 */
	public ParseResult parse(RSyntaxDocument doc, String style);


}