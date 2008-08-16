/*
 * 02/21/2005
 *
 * CodeTemplate.java - A "template" (macro) for commonly-typed code.
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
package org.fife.ui.rsyntaxtextarea;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
 * the caret is placed at the position denoted by <code>&lt;caret&gt;</code>.
 *
 * @author Robert Futrell
 * @version 0.1
 * @see org.fife.ui.rsyntaxtextarea.CodeTemplateManager
 */
class CodeTemplate implements Comparable {

	/**
	 * The "ID" for this code template (actually, the identifier used
	 * to represent the code snippet).
	 */
	private char[] id;

	/**
	 * The code inserted before the caret position.
	 */
	private String beforeCaret;

	/**
	 * The code inserted after the caret position.
	 */
	private String afterCaret;

	/**
	 * Cached value representing whether or not the text in this
	 * <code>CodeTemplate</code> contains newlines.  This value
	 * is cached because it helps speed things up when an instance
	 * of <code>RSyntaxTextArea</code> is actually using the
	 * template to insert text.
	 */
	private boolean containsNewline;

	/**
	 * Cached matcher for a regular expression to find newlines
	 * in our text to insert.  This value is cached so that we don't
	 * have to compile the regex every time this template is used.
	 */
	private Matcher matcher;


	/**
	 * Constructor.
	 *
	 * @param template2 A template of which this template will be a deep copy.
	 */
	CodeTemplate(CodeTemplate template2) {
		int idLength = template2.id.length;
		this.id = new char[idLength];
		System.arraycopy(template2.id,0, this.id,0, idLength);
		setBeforeCaretText(template2.getBeforeCaretText());
		setAfterCaretText(template2.getAfterCaretText());
	}


	/**
	 * Constructor.
	 *
	 * @param id The ID of this code template.
	 * @param beforeCaret The text to place before the caret.
	 * @param afterCaret The text to place after the caret.
	 */
	public CodeTemplate(String id, String beforeCaret, String afterCaret) {
		this.id = id.toCharArray();
		setBeforeCaretText(beforeCaret);
		setAfterCaretText(afterCaret);
	}


	/**
	 * Compares the <code>CodeTemplate</code> to another.
	 *
	 * @param o Another <code>CodeTemplate</code> object.
	 * @return A negative integer, zero, or a positive integer as this
	 *         object is less than, equal-to, or greater than the passed-in
	 *         object.
	 * @throws ClassCastException If <code>o</code> is
	 *         not an instance of <code>CodeTemplate</code>.
	 */
	public int compareTo(Object o) {
		int len1 = id.length;
		CodeTemplate t2 = (CodeTemplate)o;
		final char[] id2 = t2.getID();
		int len2 = id2.length;
		int n = Math.min(len1, len2);
		int i = 0;
		while (n-- != 0) {
			char c1 = id[i];
			char c2 = id2[i++];
			if (c1 != c2)
				return c1 - c2;
		}
		return len1 - len2;
	}


	/**
	 * Contains whether either of the before- or after-caret text pieces
	 * contain a newline character.
	 *
	 * @return Whether the text in this template contains a newline.
	 */
	public boolean containsNewline() {
		return containsNewline;
	}


	/**
	 * Overridden to return "<code>true</code>" iff {@link #compareTo(Object)}
	 * returns <code>0</code>.
	 *
	 * @return Whether this code template is equal to another.
	 */
	public boolean equals(Object obj) {
		return compareTo(obj)==0;
	}


	/**
	 * Returns the text that will be placed after the caret.
	 *
	 * @return The text.
	 * @see #setAfterCaretText
	 */
	public String getAfterCaretText() {
		return afterCaret;
	}


	/**
	 * Returns the text that will be placed before the caret.
	 *
	 * @return The text.
	 * @see #setBeforeCaretText
	 */
	public String getBeforeCaretText() {
		return beforeCaret;
	}


	/**
	 * Returns the actual content to insert.
	 *
	 * @param indent The whitespace with which to indent each new line
	 *        that is added by this template.
	 * @return The content to insert.
	 */
	public String getContentToInsert(String indent) {
		// We check to avoid the regex overhead of replaceAll if there are no
		// newlines.
		if (containsNewline())
			return matcher.replaceAll("\n" + indent);
		else
			return beforeCaret + afterCaret;
	}


	/**
	 * Returns the ID of this code template.  It is returned as an array of
	 * chars as opposed to a <code>String</code> so that it will perform
	 * better in loops.  Note that for this reason, we are giving you the
	 * actual stored ID of this template; this value should NOT be
	 * modified!
	 *
	 * @return The template's ID.
	 */
	public char[] getID() {
		return id;
	}


	/**
	 * Returns whether the specified character is a valid character for a
	 * <code>CodeTemplate</code> id.
	 *
	 * @param ch The character to check.
	 * @return Whether the character is a valid template character.
	 */
	public static final boolean isValidChar(char ch) {
		return RSyntaxUtilities.isLetterOrDigit(ch) || ch=='_';
	}


	/**
	 * Sets the text to place after the caret.
	 *
	 * @param text The text.
	 * @see #getAfterCaretText
	 */
	public void setAfterCaretText(String afterCaret) {
		this.afterCaret = afterCaret;
		updateContainsNewline();
		updateCachedRegex();
	}


	/**
	 * Sets the text to place before the caret.
	 *
	 * @param text The text.
	 * @see #getBeforeCaretText
	 */
	public void setBeforeCaretText(String beforeCaret) {
		this.beforeCaret = beforeCaret;
		updateContainsNewline();
		updateCachedRegex();
	}


	/**
	 * Returns a string representation of this template for debugging
	 * purposes.
	 *
	 * @return A string representation of this template.
	 */
	public String toString() {
		return "[CodeTemplate: id=" + new String(id) +
			", text=" + beforeCaret + "|" + afterCaret + "]";
	}


	/**
	 * Updates the cached regular expression pattern used to match
	 * newlines in our text to insert.
	 */
	private final void updateCachedRegex() {
		matcher = Pattern.compile("\n").matcher(
			getBeforeCaretText() + getAfterCaretText());
	}


	/**
	 * Updates our cached value of whether or not this template contains
	 * a newline.
	 */
	private final void updateContainsNewline() {
		containsNewline = false;
		if (beforeCaret!=null && beforeCaret.indexOf('\n')>-1)
			containsNewline = true;
		if (!containsNewline && afterCaret!=null &&
				afterCaret.indexOf('\n')>-1)
			containsNewline = true;
	}


}