/*
 * 10/30/2011
 *
 * Theme.java - A color theme for RSyntaxTextArea.
 * Copyright (C) 2011 Robert Futrell
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
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import javax.swing.JViewport;

import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;


/**
 * A theme is a set of fonts and colors to use to style RSyntaxTextArea.
 * Themes are defined in XML files.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class Theme {

	private Font baseFont;
	private Color bgColor;
	private Color caretColor;
	private Color selectionBG;
	private Color currentLineHighlight;
	private boolean fadeCurrentLineHighlight;
	private Color marginLineColor;
	private Color markAllHighlightColor;
	private Color markOccurrencesColor;
	private Color matchedBracketFG;
	private Color matchedBracketBG;
	private boolean matchedBracketAnimate;

	private SyntaxScheme scheme;

	private Color gutterBorderColor;
	private Color lineNumberColor;
	private Color foldIndicatorFG;
	private Color foldBG;


	/**
	 * Applies this theme to a text area.
	 *
	 * @param textArea The text area to apply this theme to.
	 */
	public void apply(RSyntaxTextArea textArea) {

		textArea.setFont(baseFont);
		textArea.setBackground(bgColor);
		textArea.setCaretColor(caretColor);
		textArea.setSelectionColor(selectionBG);
		textArea.setCurrentLineHighlightColor(currentLineHighlight);
		textArea.setFadeCurrentLineHighlight(fadeCurrentLineHighlight);
		textArea.setMarginLineColor(marginLineColor);
		textArea.setMarkAllHighlightColor(markAllHighlightColor);
		textArea.setMarkOccurrencesColor(markOccurrencesColor);
		textArea.setMatchedBracketBGColor(matchedBracketBG);
		textArea.setMatchedBracketBorderColor(matchedBracketFG);
		textArea.setAnimateBracketMatching(matchedBracketAnimate);

		textArea.setSyntaxScheme(scheme);

		if (textArea.getParent() instanceof JViewport &&
				textArea.getParent().getParent() instanceof RTextScrollPane) {
			RTextScrollPane scrollPane = (RTextScrollPane)textArea.getParent().getParent();
			Gutter gutter = scrollPane.getGutter();
			gutter.setBackground(bgColor);
			gutter.setBorderColor(gutterBorderColor);
			gutter.setLineNumberColor(lineNumberColor);
			gutter.setFoldIndicatorForeground(foldIndicatorFG);
			gutter.setFoldBackground(foldBG);
		}

	}


	/**
	 * Loads a theme.
	 *
	 * @param in The input stream to read from.  This will be closed when this
	 *        method returns.
	 * @return The theme.
	 * @throws IOException If an IO error occurs.
	 */
	public static Theme load(InputStream in) throws IOException {

		Theme theme = new Theme();

		BufferedInputStream bin = new BufferedInputStream(in);
		try {
			XmlParser.load(theme, bin);
		} finally {
			bin.close();
		}

		return theme;

	}


	/**
	 * Returns the color represented by a string.  The input is expected to
	 * be a 6-digit hex string, optionally prefixed by a '$'.  For example,
	 * either of the following:
	 * <pre>
	 * "$00ff00"
	 * "00ff00"
	 * </pre>
	 * will return <code>new Color(0, 255, 0)</code>.
	 *
	 * @param s The string to evaluate.
	 * @return The color.
	 */
	private static final Color stringToColor(String s) {
		if (s!=null && (s.length()==6 || s.length()==7)) {
			if (s.charAt(0)=='$') {
				s = s.substring(1);
			}
			return new Color(Integer.parseInt(s, 16));
		}
		return null;
	}


	/**
	 * Loads a <code>SyntaxScheme</code> from an XML file.
	 */
	private static class XmlParser extends DefaultHandler {

		private Theme theme;

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

		public static void load(Theme theme, InputStream in) throws IOException {
			XMLReader reader = createReader();
			XmlParser parser = new XmlParser();
			parser.theme = theme;
			reader.setContentHandler(parser);
			InputSource is = new InputSource(in);
			is.setEncoding("UTF-8");
			try {
				reader.parse(is);
			} catch (SAXException se) {
				throw new IOException(se.toString());
			}
		}

		public void startElement(String uri, String localName, String qName,
								Attributes attrs) {

			if ("background".equals(qName)) {

				String color = attrs.getValue("color");
				if (color!=null) {
					theme.bgColor = stringToColor(color);
				}
				else {
					String img = attrs.getValue("image");
					if (img!=null) {
						throw new IllegalArgumentException("Not yet implemented");
					}
				}
			}

			// The base font to use in the editor.
			else if ("baseFont".equals(qName)) {
				String family = attrs.getValue("family");
				int size = Integer.parseInt(attrs.getValue("size"));
				if (family!=null) {
					theme.baseFont = new Font(family, Font.PLAIN, size);
				}
				else {
					theme.baseFont = RSyntaxTextArea.getDefaultFont();
					theme.baseFont = theme.baseFont.deriveFont(size*1f);
				}
			}

			else if ("caret".equals(qName)) {
				String color = attrs.getValue("color");
				theme.caretColor = stringToColor(color);
			}

			else if ("currentLineHighlight".equals(qName)) {
				String color = attrs.getValue("color");
				theme.currentLineHighlight = stringToColor(color);
				String fadeStr = attrs.getValue("fade");
				boolean fade = Boolean.valueOf(fadeStr).booleanValue();
				theme.fadeCurrentLineHighlight = fade;
			}

			else if ("foldIndicator".equals(qName)) {
				String color = attrs.getValue("fg");
				theme.foldIndicatorFG = stringToColor(color);
				color = attrs.getValue("iconBg");
				theme.foldBG = stringToColor(color);
			}

			else if ("gutterBorder".equals(qName)) {
				String color = attrs.getValue("color");
				theme.gutterBorderColor = stringToColor(color);
			}

			else if ("lineNumbers".equals(qName)) {
				String color = attrs.getValue("fg");
				theme.lineNumberColor = stringToColor(color);
			}

			else if ("marginLine".equals(qName)) {
				String color = attrs.getValue("fg");
				theme.marginLineColor = stringToColor(color);
			}

			else if ("markAllHighlight".equals(qName)) {
				String color = attrs.getValue("color");
				theme.markAllHighlightColor = stringToColor(color);
			}

			else if ("markOccurrencesHighlight".equals(qName)) {
				String color = attrs.getValue("color");
				theme.markOccurrencesColor = stringToColor(color);
			}

			else if ("matchedBracket".equals(qName)) {
				String fg = attrs.getValue("fg");
				theme.matchedBracketFG = stringToColor(fg);
				String bg = attrs.getValue("bg");
				theme.matchedBracketBG = stringToColor(bg);
				String animate = attrs.getValue("animate");
				theme.matchedBracketAnimate = Boolean.valueOf(animate).booleanValue();
			}

			else if ("selection".equals(qName)) {
				String color = attrs.getValue("bg");
				theme.selectionBG = stringToColor(color);
			}

			// Start of the syntax scheme definition
			else if ("tokenStyles".equals(qName)) {
				theme.scheme = new SyntaxScheme(theme.baseFont);
			}

			// A style in the syntax scheme
			else if ("style".equals(qName)) {

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
						index = field.getInt(theme.scheme);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						return;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						return;
					}

					String fgStr = attrs.getValue("fg");
					Color fg = stringToColor(fgStr);
					theme.scheme.getStyle(index).foreground = fg;

					String bgStr = attrs.getValue("bg");
					Color bg = stringToColor(bgStr);
					theme.scheme.getStyle(index).background = bg;

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
						theme.scheme.getStyle(index).font =
								theme.baseFont.deriveFont(style);
					}

					String ulineStr = attrs.getValue("underline");
					if (ulineStr!=null) {
						boolean uline= Boolean.valueOf(ulineStr).booleanValue();
						theme.scheme.getStyle(index).underline = uline;
					}

				}

			}

		}

	}


}