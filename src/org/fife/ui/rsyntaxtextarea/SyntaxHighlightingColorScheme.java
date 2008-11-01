/*
 * 02/26/2004
 *
 * SyntaxHighlightingColorScheme.java - The set of colors and tokens used by
 * an RSyntaxTextArea to color tokens.
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
import javax.swing.text.StyleContext;


/**
 * The set of colors and styles used by an <code>RSyntaxTextArea</code> to
 * color tokens.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class SyntaxHighlightingColorScheme {

	public SyntaxScheme[] syntaxSchemes;


	/**
	 * Creates a color scheme that either has all color values set to
	 * a default value or set to <code>null</code>.
	 *
	 * @param useDefaults If <code>true</code>, all color values will
	 *        be set to default colors; if <code>false</code>, all colors
	 *        will be initially <code>null</code>.
	 */
	public SyntaxHighlightingColorScheme(boolean useDefaults) {

		syntaxSchemes = new SyntaxScheme[Token.NUM_TOKEN_TYPES];

		if (useDefaults) {

			// Colors used by tokens.
			Color comment			= new Color(0,128,0);
			Color docComment		= new Color(164,0,0);
			Color keyword			= Color.BLUE;
			Color function			= new Color(173,128,0);
			Color literalNumber		= new Color(100,0,200);
			Color literalString		= new Color(220,0,156);
			Color error			= new Color(148,148,0);

			// Special fonts.
			Font temp = RSyntaxTextArea.getDefaultFont();
			// WORKAROUND for Sun JRE bug 6282887 (Asian font bug in 1.4/1.5)
			StyleContext sc = StyleContext.getDefaultStyleContext();
			Font boldFont = sc.getFont(temp.getFamily(), Font.BOLD,
								temp.getSize());
			Font italicFont = sc.getFont(temp.getFamily(), Font.ITALIC,
								temp.getSize());
			Font commentFont = italicFont;//temp.deriveFont(Font.ITALIC);
			Font keywordFont = boldFont;//temp.deriveFont(Font.BOLD);

			syntaxSchemes[Token.COMMENT]					= null;
			syntaxSchemes[Token.COMMENT_EOL]				= new SyntaxScheme(comment, null, commentFont);
			syntaxSchemes[Token.COMMENT_MULTILINE]			= new SyntaxScheme(comment, null, commentFont);
			syntaxSchemes[Token.COMMENT_DOCUMENTATION]		= new SyntaxScheme(docComment, null, commentFont);
			syntaxSchemes[Token.RESERVED_WORD]				= new SyntaxScheme(keyword, null, keywordFont);
			syntaxSchemes[Token.FUNCTION]					= new SyntaxScheme(function, null);
			syntaxSchemes[Token.LITERAL]					= null;
			syntaxSchemes[Token.LITERAL_BOOLEAN]			= new SyntaxScheme(literalNumber, null);
			syntaxSchemes[Token.LITERAL_NUMBER_DECIMAL_INT]	= new SyntaxScheme(literalNumber, null);
			syntaxSchemes[Token.LITERAL_NUMBER_FLOAT]		= new SyntaxScheme(literalNumber, null);
			syntaxSchemes[Token.LITERAL_NUMBER_HEXADECIMAL]	= new SyntaxScheme(literalNumber, null);
			syntaxSchemes[Token.LITERAL_STRING_DOUBLE_QUOTE]	= new SyntaxScheme(literalString, null);
			syntaxSchemes[Token.LITERAL_CHAR]				= new SyntaxScheme(literalString, null);
			syntaxSchemes[Token.LITERAL_BACKQUOTE]			= new SyntaxScheme(literalString, null);
			syntaxSchemes[Token.DATA_TYPE]				= new SyntaxScheme(new Color(0,128,128), null);
			syntaxSchemes[Token.VARIABLE]					= new SyntaxScheme(new Color(255,153,0), null);
			syntaxSchemes[Token.IDENTIFIER]				= new SyntaxScheme(null, null);
			syntaxSchemes[Token.WHITESPACE]				= new SyntaxScheme(null, null);
			syntaxSchemes[Token.SEPARATOR]				= new SyntaxScheme(Color.RED, null);
			syntaxSchemes[Token.OPERATOR]					= new SyntaxScheme(new Color(128,64,64), null);
			syntaxSchemes[Token.PREPROCESSOR]				= new SyntaxScheme(new Color(128,128,128), null);
			syntaxSchemes[Token.ERROR]					= null;
			syntaxSchemes[Token.ERROR_IDENTIFIER]			= new SyntaxScheme(error, null);
			syntaxSchemes[Token.ERROR_NUMBER_FORMAT]		= new SyntaxScheme(error, null);
			syntaxSchemes[Token.ERROR_STRING_DOUBLE]		= new SyntaxScheme(error, null);
			syntaxSchemes[Token.ERROR_CHAR]				= new SyntaxScheme(error, null);

		}

	}


	/**
	 * Creates a new syntax highlighting color scheme with the same colors
 	 * as the one passed in.  This will be a deep copy.
	 *
	 * @param colorScheme The color scheme to copy.
	 * @throws NullPointerException if <code>colorScheme</code>
	 *         is <code>null</code>.
	 */
	public SyntaxHighlightingColorScheme(
							SyntaxHighlightingColorScheme colorScheme) {
		syntaxSchemes = new SyntaxScheme[Token.NUM_TOKEN_TYPES];
		for (int i=0; i<Token.NUM_TOKEN_TYPES; i++) {
			SyntaxScheme s = colorScheme.syntaxSchemes[i];
			if (s!=null) {
				syntaxSchemes[i] = new SyntaxScheme(s.foreground,
											s.background,
											s.font,
											s.underline);
				syntaxSchemes[i].fontMetrics = s.fontMetrics;
			}
		}
	}


	/**
	 * Tests whether this color scheme is the same as another color scheme.
	 *
	 * @param otherScheme The color scheme to compare to.
	 * @return <code>true</code> if this color scheme and
	 *         <code>otherScheme</code> are the same scheme;
	 *         <code>false</code> otherwise.
	 */
	public boolean equals(Object otherScheme) {

		if (!(otherScheme instanceof SyntaxHighlightingColorScheme) ||
				otherScheme==null)
			return false;

		SyntaxScheme[] otherSchemes =
				((SyntaxHighlightingColorScheme)otherScheme).syntaxSchemes;

		int length = syntaxSchemes.length;
		for (int i=0; i<length; i++) {
			if (syntaxSchemes[i]==null) {
				if (otherSchemes[i]!=null)
					return false;
			}
			else if (!syntaxSchemes[i].equals(otherSchemes[i]))
				return false;
		}
		return true;

	}


	/**
	 * This is implemented to be consistent with {@link #equals(Object)}.
	 * This is a requirement to keep FindBugs happy.
	 *
	 * @return The hash code for this object.
	 */
	public int hashCode() {
		// Keep me fast.  Iterating over *all* syntax schemes contained is
		// probably much slower than a "bad" hash code here.
		int hashCode = 0;
		int count = syntaxSchemes.length;
		for (int i=0; i<count; i++) {
			if (syntaxSchemes[i]!=null) {
				hashCode = syntaxSchemes[i].hashCode();
				break;
			}
		}
		return hashCode;
	}


	/**
	 * Loads a syntax highlighting color scheme from a string created from
	 * <code>toCommaSeparatedString</code>.  This method is useful for saving
	 * and restoring color schemes.
	 *
	 * @param string A string generated from {@link #toCommaSeparatedString()}.
	 * @return A color scheme.
	 */
	public static SyntaxHighlightingColorScheme loadFromString(String string) {

		SyntaxHighlightingColorScheme scheme =
					new SyntaxHighlightingColorScheme(true);

		try {

			if (string!=null) {

				int tokenTypeCount = Token.NUM_TOKEN_TYPES;
				int tokenCount = tokenTypeCount*7;
				String[] tokens = string.split(",", -1);
				if (tokens.length!=tokenCount) {
					throw new Exception("Not enough tokens in packed color scheme: expected " +
							tokenCount + ", found " + tokens.length);
				}

				// Loop through each token style.  Format:
				// "index,(fg|-),(bg|-),(t|f),((font,style,size)|(-,,))"
				for (int i=0; i<tokenTypeCount; i++) {

					int pos = i*7;
					int integer = Integer.parseInt(tokens[pos]); // == i.
					if (integer!=i)
						throw new Exception("Expected " + i + ", found " + integer);

					Color fg = null; String temp = tokens[pos+1];
					fg = "-".equals(temp) ? null : new Color(Integer.parseInt(temp));
					Color bg = null; temp = tokens[pos+2];
					bg = "-".equals(temp) ? null : new Color(Integer.parseInt(temp));

					// Check for "true" or "false" since we don't want to
					// accidentally suck in an int representing the next
					// packed color, and any string != "true" means false.
					temp = tokens[pos+3];
					if (!"t".equals(temp) && !"f".equals(temp))
						throw new Exception("Expected 't' or 'f', found " + temp);
					boolean underline = "t".equals(temp) ? true : false;

					Font font = null;
					String family = tokens[pos+4];
					if (!"-".equals(family)) {
						font = new Font(family,
							Integer.parseInt(tokens[pos+5]),	// style
							Integer.parseInt(tokens[pos+6]));	// size
					}
					scheme.syntaxSchemes[i] = new SyntaxScheme(fg, bg, font, underline);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return scheme;

	}


	/**
	 * Returns this syntax highlighting scheme as a comma-separated list of
	 * values as follows:
	 * <ul>
	 *   <li>If a color is non-null, it is added as a 24-bit integer
	 *      of the form <code>((r<<16) | (g<<8) | (b))</code>; if it is
	 *       <code>null</code>, it is added as "<i>-,</i>".
	 *   <li>The font and style (bold/italic) is added as an integer like so:
	 *       "<i>family,</i> <i>style,</i> <i>size</i>".
	 *   <li>The entire syntax highlighting scheme is thus one long string of
	 *       color schemes of the format "<i>i,[fg],[bg],uline,[style]</i>,
	 *       where:
	 *       <ul>
	 *          <li><code>i</code> is the index of the syntax scheme.
	 *          <li><i>fg</i> and <i>bg</i> are the foreground and background
	 *              colors for the scheme, and may be null (represented by 
	 *              <code>-</code>).
	 *          <li><code>uline</code> is whether or not the font should be
	 *              underlined, and is either <code>t</code> or <code>f</code>.
	 *          <li><code>style</code> is the <code>family,style,size</code>
	 *              triplet described above.
	 *       </ul>
	 * </ul>
	 *
	 * @return A string representing the rgb values of the colors.
	 */
	public String toCommaSeparatedString() {

		String retVal = "";

		for (int i=0; i<Token.NUM_TOKEN_TYPES; i++) {

			retVal += i + ",";

			SyntaxScheme ss = syntaxSchemes[i];
			if (ss==null) { // Only true for i==0 (NULL token)
				retVal += "-,-,f,-,,,";
				continue;
			}

			Color c = ss.foreground;
			retVal += c!=null ? (c.getRGB()+",") : "-,";
			c = ss.background;
			retVal += c!=null ? (c.getRGB()+",") : "-,";

			retVal += ss.underline ? "t," : "f,";

			Font font = ss.font;
			if (font!=null) {
				retVal += font.getFamily() + "," + font.getStyle() + "," +
						font.getSize() + ",";
			}
			else {
				retVal += "-,,,";
			}

		}

		return retVal.substring(0,retVal.length()-1); // Take off final ','.

	}


}