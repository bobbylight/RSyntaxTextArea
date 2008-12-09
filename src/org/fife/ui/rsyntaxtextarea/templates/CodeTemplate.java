/*
 * 11/29/2008
 *
 * CodeTemplate.java - A "template" (macro) for commonly-typed code.
 * Copyright (C) 2008 Robert Futrell
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
package org.fife.ui.rsyntaxtextarea.templates;

import java.io.Serializable;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;


/**
 * A "code template" is a kind of macro for commonly-typed code.  It
 * associates a short identifier with a longer code snippet, then when the
 * code template is enabled and the short identifier is typed, it is
 * replaced with the longer code snippet.<p>
 *
 * For example, you can associate the identifier <code>forb</code>
 * (short for "for-block") with the following code:<p>
 *
 * <pre>
 *   for (&lt;caret&gt;) {
 *
 *   }
 * </pre>
 *
 * Then, whenever you type <code>forb</code> followed by a trigger
 * (e.g., a space) into a text area with this <code>CodeTemplate</code>,
 * the code snippet is added in place of <code>forb</code>.  Further,
 * the caret is placed at the position denoted by <code>&lt;caret&gt;</code>.<p>
 *
 * Static text replacements are done with {@link StaticCodeTemplate}.  Dynamic
 * templates can also be created and used.
 *
 * @author Robert Futrell
 * @version 0.1
 * @see StaticCodeTemplate
 */
public interface CodeTemplate extends Cloneable, Comparable, Serializable {


	/**
	 * Creates a deep copy of this template.
	 *
	 * @return A deep copy of this template.
	 */
	public Object clone();


	/**
	 * Returns the ID of this code template.
	 *
	 * @return The template's ID.
	 */
	public String getID();


	/**
	 * Invokes this code template.  The changes are made to the given text
	 * area.
	 *
	 * @param textArea The text area to operate on.
	 * @throws BadLocationException If something bad happens.
	 */
	public void invoke(RSyntaxTextArea textArea) throws BadLocationException;


}