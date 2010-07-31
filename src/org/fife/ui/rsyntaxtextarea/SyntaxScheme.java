/*
 * 02/26/2004
 *
 * SyntaxScheme.java - The set of colors and tokens used by an RSyntaxTextArea
 * to color tokens.
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
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import javax.swing.text.StyleContext;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * The set of colors and styles used by an <code>RSyntaxTextArea</code> to
 * color tokens.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class SyntaxScheme implements Cloneable {

	public Style[] styles;

	private static final String VERSION			= "*ver1";


	/**
	 * Creates a color scheme that either has all color values set to
	 * a default value or set to <code>null</code>.
	 *
	 * @param useDefaults If <code>true</code>, all color values will
	 *        be set to default colors; if <code>false</code>, all colors
	 *        will be initially <code>null</code>.
	 */
	public SyntaxScheme(boolean useDefaults) {
		styles = new Style[Token.NUM_TOKEN_TYPES];
		if (useDefaults) {
			restoreDefaults(null);
		}
	}


	/**
	 * Creates a default color scheme.
	 *
	 * @param baseFont The base font to use.  Keywords will be a bold version
	 *        of this font, and comments will be an italicized version of this
	 *        font.
	 */
	public SyntaxScheme(Font baseFont) {
		styles = new Style[Token.NUM_TOKEN_TYPES];
		restoreDefaults(baseFont);
	}


	/**
	 * Changes the "base font" for this syntax scheme.  This is called by
	 * <code>RSyntaxTextArea</code> when its font changes via
	 * <code>setFont()</code>.  This looks for tokens that use a derivative of
	 * the text area's old font (but bolded and/or italicized) and make them
	 * use the new font with those stylings instead.  This is desirable because
	 * most programmers prefer a single font to be used in their text editor,
	 * but might want bold (say for keywords) or italics.
	 *
	 * @param old The old font of the text area.
	 * @param font The new font of the text area.
	 */
	void changeBaseFont(Font old, Font font) {
		for (int i=0; i<styles.length; i++) {
			Style style = styles[i];
			if (style!=null && style.font!=null) {
				if (style.font.getFamily().equals(old.getFamily()) &&
						style.font.getSize()==old.getSize()) {
					int s = style.font.getStyle(); // Keep bold or italic
					StyleContext sc = StyleContext.getDefaultStyleContext();
					style.font= sc.getFont(font.getFamily(), s, font.getSize());
				}
			}
		}
	}


	/**
	 * Returns a deep copy of this color scheme.
	 *
	 * @return The copy.
	 */
	public Object clone() {
		SyntaxScheme shcs = null;
		try {
			shcs = (SyntaxScheme)super.clone();
		} catch (CloneNotSupportedException cnse) { // Never happens
			cnse.printStackTrace();
			return null;
		}
		shcs.styles = new Style[Token.NUM_TOKEN_TYPES];
		for (int i=0; i<Token.NUM_TOKEN_TYPES; i++) {
			Style s = styles[i];
			if (s!=null) {
				shcs.styles[i] = (Style)s.clone();
			}
		}
		return shcs;
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

		// No need for null check; instanceof takes care of this for us,
		// i.e. "if (!(null instanceof Foo))" evaluates to "true".
		if (!(otherScheme instanceof SyntaxScheme)) {
			return false;
		}

		Style[] otherSchemes = ((SyntaxScheme)otherScheme).styles;

		int length = styles.length;
		for (int i=0; i<length; i++) {
			if (styles[i]==null) {
				if (otherSchemes[i]!=null) {
					return false;
				}
			}
			else if (!styles[i].equals(otherSchemes[i])) {
				return false;
			}
		}
		return true;

	}


	/**
	 * Returns a hex string representing an RGB color, of the form
	 * <code>"$rrggbb"</code>.
	 *
	 * @param c The color.
	 * @return The string representation of the color.
	 */
	private static final String getHexString(Color c) {
		return "$" + Integer.toHexString((c.getRGB() & 0xffffff)+0x1000000).
									substring(1);
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
		int count = styles.length;
		for (int i=0; i<count; i++) {
			if (styles[i]!=null) {
				hashCode ^= styles[i].hashCode();
				break;
			}
		}
		return hashCode;
	}


	/**
	 * Loads a syntax scheme from an input stream.
	 *
	 * @param baseFont The font to use as the "base" for the syntax scheme.
	 *        If this is <code>null</code>, a default monospaced font is used.
	 * @param in The stream to load from.  It is up to the caller to close this
	 *        stream when they are done.
	 * @return The syntax scheme.
	 * @throws IOException If an IO error occurs.
	 */
	public static SyntaxScheme load(Font baseFont, InputStream in)
									throws IOException {
		if (baseFont==null) {
			baseFont = RSyntaxTextArea.getDefaultFont();
		}
		return XmlParser.load(baseFont, in);
	}


	/**
	 * Loads a syntax highlighting color scheme from a string created from
	 * <code>toCommaSeparatedString</code>.  This method is useful for saving
	 * and restoring color schemes.
	 *
	 * @param string A string generated from {@link #toCommaSeparatedString()}.
	 * @return A color scheme.
	 */
	public static SyntaxScheme loadFromString(String string) {

		SyntaxScheme scheme = new SyntaxScheme(true);

		try {

			if (string!=null) {

				String[] tokens = string.split(",", -1);

				// Check the version string, use defaults if incompatible
				if (tokens.length==0 || !VERSION.equals(tokens[0])) {
					return scheme; // Still set to defaults
				}

				int tokenTypeCount = Token.NUM_TOKEN_TYPES;
				int tokenCount = tokenTypeCount*7 + 1; // Version string
				if (tokens.length!=tokenCount) {
					throw new Exception(
						"Not enough tokens in packed color scheme: expected " +
						tokenCount + ", found " + tokens.length);
				}

				// Loop through each token style.  Format:
				// "index,(fg|-),(bg|-),(t|f),((font,style,size)|(-,,))"
				for (int i=0; i<tokenTypeCount; i++) {

					int pos = i*7 + 1;
					int integer = Integer.parseInt(tokens[pos]); // == i
					if (integer!=i)
						throw new Exception("Expected " + i + ", found " +
											integer);

					Color fg = null; String temp = tokens[pos+1];
					if (!"-".equals(temp)) { // "-" => keep fg as null
						fg = stringToColor(temp);
					}
					Color bg = null; temp = tokens[pos+2];
					if (!"-".equals(temp)) { // "-" => keep bg as null
						bg = stringToColor(temp);
					}

					// Check for "true" or "false" since we don't want to
					// accidentally suck in an int representing the next
					// packed color, and any string != "true" means false.
					temp = tokens[pos+3];
					if (!"t".equals(temp) && !"f".equals(temp))
						throw new Exception("Expected 't' or 'f', found " + temp);
					boolean underline = "t".equals(temp);

					Font font = null;
					String family = tokens[pos+4];
					if (!"-".equals(family)) {
						font = new Font(family,
							Integer.parseInt(tokens[pos+5]),	// style
							Integer.parseInt(tokens[pos+6]));	// size
					}
					scheme.styles[i] = new Style(fg, bg, font, underline);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return scheme;

	}


	void refreshFontMetrics(Graphics2D g2d) {
		// It is assumed that any rendering hints are already applied to g2d.
		for (int i=0; i<styles.length; i++) {
			Style s = styles[i];
			if (s!=null) {
				s.fontMetrics = s.font==null ? null :
								g2d.getFontMetrics(s.font);
			}
		}
	}


	/**
	 * Restores all colors and fonts to their default values.
	 *
	 * @param baseFont The base font to use when creating this scheme.  If
	 *        this is <code>null</code>, then a default monospaced font is
	 *        used.
	 */
	public void restoreDefaults(Font baseFont) {

		// Colors used by tokens.
		Color comment			= new Color(0,128,0);
		Color docComment		= new Color(164,0,0);
		Color keyword			= Color.BLUE;
		Color function			= new Color(173,128,0);
		Color literalNumber		= new Color(100,0,200);
		Color literalString		= new Color(220,0,156);
		Color error			= new Color(148,148,0);

		// Special fonts.
		if (baseFont==null) {
			baseFont = RSyntaxTextArea.getDefaultFont();
		}
		// WORKAROUND for Sun JRE bug 6282887 (Asian font bug in 1.4/1.5)
		StyleContext sc = StyleContext.getDefaultStyleContext();
		Font boldFont = sc.getFont(baseFont.getFamily(), Font.BOLD,
				baseFont.getSize());
		Font italicFont = sc.getFont(baseFont.getFamily(), Font.ITALIC,
				baseFont.getSize());
		Font commentFont = italicFont;//baseFont.deriveFont(Font.ITALIC);
		Font keywordFont = boldFont;//baseFont.deriveFont(Font.BOLD);

		styles[Token.COMMENT_EOL]				= new Style(comment, null, commentFont);
		styles[Token.COMMENT_MULTILINE]			= new Style(comment, null, commentFont);
		styles[Token.COMMENT_DOCUMENTATION]		= new Style(docComment, null, commentFont);
		styles[Token.RESERVED_WORD]				= new Style(keyword, null, keywordFont);
		styles[Token.FUNCTION]					= new Style(function, null);
		styles[Token.LITERAL_BOOLEAN]			= new Style(literalNumber, null);
		styles[Token.LITERAL_NUMBER_DECIMAL_INT]	= new Style(literalNumber, null);
		styles[Token.LITERAL_NUMBER_FLOAT]		= new Style(literalNumber, null);
		styles[Token.LITERAL_NUMBER_HEXADECIMAL]	= new Style(literalNumber, null);
		styles[Token.LITERAL_STRING_DOUBLE_QUOTE]	= new Style(literalString, null);
		styles[Token.LITERAL_CHAR]				= new Style(literalString, null);
		styles[Token.LITERAL_BACKQUOTE]			= new Style(literalString, null);
		styles[Token.DATA_TYPE]				= new Style(new Color(0,128,128), null);
		styles[Token.VARIABLE]					= new Style(new Color(255,153,0), null);
		styles[Token.IDENTIFIER]				= new Style(null, null);
		styles[Token.WHITESPACE]				= new Style(Color.gray, null);
		styles[Token.SEPARATOR]				= new Style(Color.RED, null);
		styles[Token.OPERATOR]					= new Style(new Color(128,64,64), null);
		styles[Token.PREPROCESSOR]				= new Style(new Color(128,128,128), null);
		styles[Token.MARKUP_TAG_DELIMITER]		= new Style(Color.RED, null);
		styles[Token.MARKUP_TAG_NAME]			= new Style(Color.BLUE, null);
		styles[Token.MARKUP_TAG_ATTRIBUTE]		= new Style(new Color(63,127,127), null);
//		styles[Token.ERROR]					= null;
		styles[Token.ERROR_IDENTIFIER]			= new Style(error, null);
		styles[Token.ERROR_NUMBER_FORMAT]		= new Style(error, null);
		styles[Token.ERROR_STRING_DOUBLE]		= new Style(error, null);
		styles[Token.ERROR_CHAR]				= new Style(error, null);

	}


	/**
	 * Sets a style to use when rendering a token type.
	 *
	 * @param type The token type.
	 * @param style The style for the token type.
	 */
	public void setStyle(int type, Style style) {
		styles[type] = style;
	}


	/**
	 * Returns the color represented by a string.  If the first char in the
	 * string is '<code>$</code>', it is assumed to be in hex, otherwise it is
	 * assumed to be decimal.  So, for example, both of these:
	 * <pre>
	 * "$00ff00"
	 * "65280"
	 * </pre>
	 * will return <code>new Color(0, 255, 0)</code>.
	 *
	 * @param s The string to evaluate.
	 * @return The color.
	 */
	private static final Color stringToColor(String s) {
		// Check for decimal as well as hex, for backward
		// compatibility (fix from GwynEvans on forums)
		char ch = s.charAt(0);
		return new Color((ch=='$' || ch=='#') ?
				Integer.parseInt(s.substring(1),16) :
				Integer.parseInt(s));
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

		StringBuffer sb = new StringBuffer(VERSION);
		sb.append(',');

		for (int i=0; i<Token.NUM_TOKEN_TYPES; i++) {

			sb.append(i).append(',');

			Style ss = styles[i];
			if (ss==null) { // Only true for i==0 (NULL token)
				sb.append("-,-,f,-,,,");
				continue;
			}

			Color c = ss.foreground;
			sb.append(c!=null ? (getHexString(c) + ",") : "-,");
			c = ss.background;
			sb.append(c!=null ? (getHexString(c) + ",") : "-,");

			sb.append(ss.underline ? "t," : "f,");

			Font font = ss.font;
			if (font!=null) {
				sb.append(font.getFamily()).append(',').
					append(font.getStyle()).append(',').
						append(font.getSize()).append(',');
			}
			else {
				sb.append("-,,,");
			}

		}

		return sb.substring(0,sb.length()-1); // Take off final ','.

	}


	/**
	 * Loads a <code>SyntaxScheme</code> from an XML file.
	 */
	private static class XmlParser extends DefaultHandler {

		private Font baseFont;
		private SyntaxScheme scheme;

		public XmlParser(Font baseFont) {
			scheme = new SyntaxScheme(baseFont);
		}

		/**
		 * Creates the XML reader to use.  Note that in 1.4 JRE's, the reader
		 * class wasn't defined by default, but in 1.5+ it is.
		 *
		 * @return The XML reader to use.
		 */
		private static XMLReader createReader() throws IOException {
			XMLReader reader = null;
			try {
				reader = XMLReaderFactory.createXMLReader();
			} catch (SAXException e) {
				// Happens in JRE 1.4.x; 1.5+ define the reader class properly
				try {
					reader = XMLReaderFactory.createXMLReader(
							"org.apache.crimson.parser.XMLReaderImpl");
				} catch (SAXException se) {
					throw new IOException(se.toString());
				}
			}
			return reader;
		}

		public static SyntaxScheme load(Font baseFont,
										InputStream in) throws IOException {
			XMLReader reader = createReader();
			XmlParser parser = new XmlParser(baseFont);
			parser.baseFont = baseFont;
			reader.setContentHandler(parser);
			InputSource is = new InputSource(in);
			is.setEncoding("UTF-8");
			try {
				reader.parse(is);
			} catch (SAXException se) {
				throw new IOException(se.toString());
			}
			return parser.scheme;
		}

		public void startElement(String uri, String localName, String qName,
								Attributes attrs) {

			if ("style".equals(qName)) {

				String type = attrs.getValue("token");
				Field field = null;
				try {
					field = Token.class.getField(type);
				} catch (RuntimeException re) {
					throw re; // FindBugs
				} catch (Exception e) {
					System.err.println("Invalid token type: " + type);
					return;
				}

				if (field.getType()==int.class) {

					int index = 0;
					try {
						index = field.getInt(scheme);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						return;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						return;
					}

					String fgStr = attrs.getValue("fg");
					if (fgStr!=null) {
						Color fg = stringToColor(fgStr);
						scheme.styles[index].foreground = fg;
					}

					String bgStr = attrs.getValue("bg");
					if (bgStr!=null) {
						Color bg = stringToColor(bgStr);
						scheme.styles[index].background = bg;
					}

					boolean styleSpecified = false;
					boolean bold = false;
					boolean italic = false;
					String boldStr = attrs.getValue("bold");
					if (boldStr!=null) {
						bold = Boolean.valueOf(boldStr).booleanValue();
						styleSpecified = true;
					}
					String italicStr = attrs.getValue("italic");
					if (italicStr!=null) {
						italic = Boolean.valueOf(italicStr).booleanValue();
						styleSpecified = true;
					}
					if (styleSpecified) {
						int style = 0;
						if (bold) { style |= Font.BOLD; }
						if (italic) { style |= Font.ITALIC; }
						scheme.styles[index].font = baseFont.deriveFont(style);
					}

					String ulineStr = attrs.getValue("underline");
					if (ulineStr!=null) {
						boolean uline= Boolean.valueOf(ulineStr).booleanValue();
						scheme.styles[index].underline = uline;
					}

				}

			}

		}

	}


}