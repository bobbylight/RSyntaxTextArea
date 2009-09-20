/*
 * 08/06/2004
 *
 * Style.java - A set of traits for a particular token type to use while
 * painting.
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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JPanel;


/**
 * The color and style information for a token type.  Each token type in an
 * <code>RSyntaxTextArea</code> has a corresponding <code>Style</code>; this
 * <code>Style</code> tells us the following things:
 *
 * <ul>
 *   <li>What foreground color to use for tokens of this type.</li>
 *   <li>What background color to use.</li>
 *   <li>The font to use.</li>
 *   <li>Whether the token should be underlined.</li>
 * </ul>
 *
 * @author Robert Futrell
 * @version 0.6
 */
public class Style implements Cloneable {

	public static final Color DEFAULT_FOREGROUND	= Color.BLACK;
	public static final Color DEFAULT_BACKGROUND	= null;
	public static final Font DEFAULT_FONT			= null;

	public Color foreground;
	public Color background;
	public boolean underline;
	public Font font;

	FontMetrics fontMetrics;


	/**
	 * Creates a new syntax scheme defaulting to black foreground, no
	 * background, and no styling.
	 */
	public Style() {
		this(DEFAULT_FOREGROUND, DEFAULT_BACKGROUND);
	}


	/**
	 * Creates a new syntax scheme with the specified colors and no styling.
	 *
	 * @param fg The foreground color to use.
	 * @param bg The background color to use.
	 */
	public Style(Color fg, Color bg) {
		this(fg, bg, DEFAULT_FONT);
	}


	/**
	 * Creates a new syntax scheme.
	 *
	 * @param fg The foreground color to use.
	 * @param bg The background color to use.
	 * @param font The font for this syntax scheme.
	 */
	public Style(Color fg, Color bg, Font font) {
		this(fg, bg, font, false);
	}


	/**
	 * Creates a new syntax scheme.
	 *
	 * @param fg The foreground color to use.
	 * @param bg The background color to use.
	 * @param font The font for this syntax scheme.
	 * @param underline Whether or not to underline tokens with this style.
	 */
	public Style(Color fg, Color bg, Font font, boolean underline) {
		foreground = fg;
		background = bg;
		this.font = font;
		this.underline = underline;
		this.fontMetrics = font==null ? null :
			new JPanel().getFontMetrics(font); // Default, no rendering hints!
	}


	/**
	 * Returns whether or not two (possibly <code>null</code>) objects are
	 * equal.
	 */
	private boolean areEqual(Object o1, Object o2) {
		return (o1==null && o2==null) || (o1!=null && o1.equals(o2));
	}


	/**
	 * Returns a deep copy of this object.
	 *
	 * @return The copy.
	 */
	public Object clone() {
		Style clone = null;
		try {
			clone = (Style)super.clone();
		} catch (CloneNotSupportedException cnse) { // Never happens
			cnse.printStackTrace();
			return null;
		}
		clone.foreground = foreground;
		clone.background = background;
		clone.font = font;
		clone.underline = underline;
		clone.fontMetrics = fontMetrics;
		return clone;
	}


	/**
	 * Returns whether or not two syntax schemes are equal.
	 *
	 * @param o2 The object with which to compare this syntax scheme.
	 * @return Whether or not these two syntax schemes represent the same
	 *         scheme.
	 */
	public boolean equals(Object o2) {
		if (o2 instanceof Style) {
			Style ss2 = (Style)o2;
			if (this.underline==ss2.underline &&
				areEqual(foreground, ss2.foreground) &&
				areEqual(background, ss2.background) &&
				areEqual(font, ss2.font) &&
				areEqual(fontMetrics, ss2.fontMetrics))
					return true;
		}
		return false;
	}


	/**
	 * Computes the hash code to use when adding this syntax scheme to
	 * hash tables.<p>
	 *
	 * This method is implemented, since {@link #equals(Object)} is implemented,
	 * to keep FindBugs happy.
	 *
	 * @return The hash code.
	 */
	public int hashCode() {
		int hashCode = underline ? 1 : 0;
		if (foreground!=null) {
			hashCode ^= foreground.hashCode();
		}
		if (background!=null) {
			hashCode ^= background.hashCode();
		} 
		return hashCode;
	}


	/**
	 * Returns a string representation of this style.
	 *
	 * @return A string representation of this style.
	 */
	public String toString() {
		return "[Style: foreground: " + foreground +
			  ", background: " + background + ", underline: " +
			  underline + ", font: " + font + "]";
	}


}