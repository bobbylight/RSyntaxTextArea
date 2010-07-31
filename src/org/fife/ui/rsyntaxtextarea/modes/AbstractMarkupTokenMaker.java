/*
 * 10/03/2009
 *
 * AbstractMarkupTokenMaker.java - Base class for token makers for markup
 * languages.
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
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.AbstractJFlexTokenMaker;


/**
 * Base class for token makers for markup languages.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractMarkupTokenMaker extends AbstractJFlexTokenMaker {


	/**
	 * Returns whether markup close tags should be completed.
	 *
	 * @return Whether closing markup tags are to be completed.
	 */
	public abstract boolean getCompleteCloseTags();


	/**
	 * Returns the text to place at the beginning and end of a
	 * line to "comment" it in a this programming language.
	 *
	 * @return The start and end strings to add to a line to "comment"
	 *         it out.
	 */
	public String[] getLineCommentStartAndEnd() {
		return new String[] { "<!--", "-->" };
	}


	/**
	 * Overridden to return <code>true</code>.
	 *
	 * @return <code>true</code> always.
	 */
	public final boolean isMarkupLanguage() {
		return true;
	}


}