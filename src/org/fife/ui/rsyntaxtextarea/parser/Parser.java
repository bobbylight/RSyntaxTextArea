/*
 * 09/23/2005
 *
 * Parser.java - An interface for the parser in a compiler.
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

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;


/**
 * An interface for a parser.
 *
 * @author Robert Futrell
 * @version 0.1
 */
public interface Parser {


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