/*
 * 10/28/2004
 *
 * TokenFactory.java - Interface for a class that generates tokens of some type.
 * Copyright (C) 2004 Robert Futrell
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

import javax.swing.text.Segment;


/**
 * Interface for a class that generates tokens somehow.
 *
 * @author Robert Futrell
 * @version 0.1
 */
interface TokenFactory {


	/**
	 * Returns a null token.
	 *
	 * @return A null token.
	 */
	public Token createToken();


	/**
	 * Returns a token.
	 *
	 * @param line The segment from which to get the token's text.
	 * @param beg The starting offset of the token's text in the segment.
	 * @param end The ending offset of the token's text in the segment.
	 * @param startOffset The offset in the document of the token.
	 * @param type The type of token.
	 * @return The token.
	 */
	public Token createToken(final Segment line, final int beg,
					final int end, final int startOffset, final int type);


	/**
	 * Returns a token.
	 *
	 * @param line The char array from which to get the token's text.
	 * @param beg The starting offset of the token's text in the char array.
	 * @param end The ending offset of the token's text in the char array.
	 * @param startOffset The offset in the document of the token.
	 * @param type The type of token.
	 * @return The token.
	 */
	public Token createToken(final char[] line, final int beg,
					final int end, final int startOffset, final int type);


	/**
	 * Resets the state of this token maker, if necessary.
	 * FIXME:  Improve documentation.
	 */
	public void resetAllTokens();


}