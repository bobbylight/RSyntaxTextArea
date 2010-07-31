/*
 * 10/28/2004
 *
 * VisibleWhitespaceTokenFactory.java - Visible whitespace token factory.
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


/**
 * Token factory that generates tokens that display special symbols for the
 * whitespace characters space and tab.<p>
 *
 * NOTE:  This class should only be used by {@link TokenMaker}; nobody else
 * needs it!
 *
 * @author Robert Futrell
 * @version 0.1
 */
class VisibleWhitespaceTokenFactory extends DefaultTokenFactory {


	/**
	 * Cosnstructor.
	 */
	public VisibleWhitespaceTokenFactory() {
		this(DEFAULT_START_SIZE, DEFAULT_INCREMENT);
	}


	/**
	 * Constructor.
	 *
	 * @param size The initial number of tokens in this factory.
	 * @param increment How many tokens to increment by when the stack gets
	 *        empty.
	 */
	public VisibleWhitespaceTokenFactory(int size, int increment) {
		super(size, increment);
	}

	/**
	 * Creates a token for use internally by this token factory.  This method
	 * should NOT be called externally; only by this class and possibly
	 * subclasses.
	 *
	 * @return A token to add to this token factory's internal stack.
	 */
	protected Token createInternalUseOnlyToken() {
		return new VisibleWhitespaceToken();
	}


}