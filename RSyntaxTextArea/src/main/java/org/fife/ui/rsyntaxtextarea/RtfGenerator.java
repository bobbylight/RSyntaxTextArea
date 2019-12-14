/*
 * 07/28/2008
 *
 * RtfGenerator.java - Generates RTF via a simple Java API.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import org.fife.ui.rtextarea.RTextArea;


/**
 * Generates RTF text via a simple Java API.<p>
 *
 * The following RTF features are supported:
 * <ul>
 *    <li>Fonts
 *    <li>Font sizes
 *    <li>Foreground and background colors
 *    <li>Bold, italic, and underline
 * </ul>
 *
 * The RTF generated isn't really "optimized," but it will do, especially for
 * small amounts of text, such as what's common when copy-and-pasting.  It
 * tries to be sufficient for the use case of copying syntax highlighted
 * code:
 * <ul>
 *    <li>It assumes that tokens changing foreground color often is fairly
 *        common.
 *    <li>It assumes that background highlighting is fairly uncommon.
 * </ul>
 *
 * @author Robert Futrell
 * @version 1.1
 */
public class RtfGenerator {

	private Color mainBG;
	private List<Font> fontList;
	private List<Color> colorList;
	private StringBuilder document;
	private boolean lastWasControlWord;
	private int lastFontIndex;
	private int lastFGIndex;
	private boolean lastBold;
	private boolean lastItalic;
	private int lastFontSize;

	/**
	 * Java2D assumes a 72 dpi screen resolution, but on Windows the screen
	 * resolution is either 96 dpi or 120 dpi, depending on your font display
	 * settings.  This is an attempt to make the RTF generated match the
	 * size of what's displayed in the RSyntaxTextArea.
	 */
	private int screenRes;

	/**
	 * The default font size for RTF.  This is point size, in half
	 * points.
	 */
	private static final int DEFAULT_FONT_SIZE = 12;//24;


	/**
	 * Constructor.
	 */
	public RtfGenerator(Color mainBG) {
		this.mainBG = mainBG;
		fontList = new ArrayList<>(1); // Usually only 1.
		colorList = new ArrayList<>(1); // Usually only 1.
		document = new StringBuilder();
		reset();
	}


	/**
	 * Adds a newline to the RTF document.
	 *
	 * @see #appendToDoc(String, Font, Color, Color)
	 */
	public void appendNewline() {
		document.append("\\line");
		document.append('\n'); // Just for ease of reading RTF.
		lastWasControlWord = false;
	}


	/**
	 * Appends styled text to the RTF document being generated.
	 *
	 * @param text The text to append.
	 * @param f The font of the text.  If this is <code>null</code>, the
	 *        default font is used.
	 * @param fg The foreground of the text.  If this is <code>null</code>,
	 *        the default foreground color is used.
	 * @param bg The background color of the text.  If this is
	 *        <code>null</code>, the default background color is used.
	 * @see #appendNewline()
	 */
	public void appendToDoc(String text, Font f, Color fg, Color bg) {
		appendToDoc(text, f, fg, bg, false);
	}


	/**
	 * Appends styled text to the RTF document being generated.
	 *
	 * @param text The text to append.
	 * @param f The font of the text.  If this is <code>null</code>, the
	 *        default font is used.
	 * @param bg The background color of the text.  If this is
	 *        <code>null</code>, the default background color is used.
	 * @param underline Whether the text should be underlined.
	 * @see #appendNewline()
	 */
	public void appendToDocNoFG(String text, Font f, Color bg,
							boolean underline) {
		appendToDoc(text, f, null, bg, underline, false);
	}


	/**
	 * Appends styled text to the RTF document being generated.
	 *
	 * @param text The text to append.
	 * @param f The font of the text.  If this is <code>null</code>, the
	 *        default font is used.
	 * @param fg The foreground of the text.  If this is <code>null</code>,
	 *        the default foreground color is used.
	 * @param bg The background color of the text.  If this is
	 *        <code>null</code>, the default background color is used.
	 * @param underline Whether the text should be underlined.
	 * @see #appendNewline()
	 */
	public void appendToDoc(String text, Font f, Color fg, Color bg,
							boolean underline) {
		appendToDoc(text, f, fg, bg, underline, true);
	}


	/**
	 * Appends styled text to the RTF document being generated.
	 *
	 * @param text The text to append.
	 * @param f The font of the text.  If this is <code>null</code>, the
	 *        default font is used.
	 * @param fg The foreground of the text.  If this is <code>null</code>,
	 *        the default foreground color is used.
	 * @param bg The background color of the text.  If this is
	 *        <code>null</code>, the default background color is used.
	 * @param underline Whether the text should be underlined.
	 * @param setFG Whether the foreground specified by <code>fg</code> should
	 *        be honored (if it is non-<code>null</code>).
	 * @see #appendNewline()
	 */
	public void appendToDoc(String text, Font f, Color fg, Color bg,
							boolean underline, boolean setFG) {

		if (text!=null) {

			// Set font to use, if different from last addition.
			int fontIndex = f==null ? 0 : (getFontIndex(fontList, f)+1);
			if (fontIndex!=lastFontIndex) {
				document.append("\\f").append(fontIndex);
				lastFontIndex = fontIndex;
				lastWasControlWord = true;
			}

			// Set styles to use.
			if (f!=null) {
				int fontSize = fixFontSize(f.getSize2D()*2); // Half points!
				if (fontSize!=lastFontSize) {
					document.append("\\fs").append(fontSize);
					lastFontSize = fontSize;
					lastWasControlWord = true;
				}
				if (f.isBold()!=lastBold) {
					document.append(lastBold ? "\\b0" : "\\b");
					lastBold = !lastBold;
					lastWasControlWord = true;
				}
				if (f.isItalic()!=lastItalic) {
					document.append(lastItalic ? "\\i0" : "\\i");
					lastItalic = !lastItalic;
					lastWasControlWord = true;
				}
			}
			else { // No font specified - assume neither bold nor italic.
				if (lastFontSize!=DEFAULT_FONT_SIZE) {
					document.append("\\fs").append(DEFAULT_FONT_SIZE);
					lastFontSize = DEFAULT_FONT_SIZE;
					lastWasControlWord = true;
				}
				if (lastBold) {
					document.append("\\b0");
					lastBold = false;
					lastWasControlWord = true;
				}
				if (lastItalic) {
					document.append("\\i0");
					lastItalic = false;
					lastWasControlWord = true;
				}
			}
			if (underline) {
				document.append("\\ul");
				lastWasControlWord = true;
			}

			// Set the foreground color.
			if (setFG) {
				int fgIndex = 0;
				if (fg!=null) { // null => fg color index 0
					fgIndex = getColorIndex(colorList, fg)+1;
				}
				if (fgIndex!=lastFGIndex) {
					document.append("\\cf").append(fgIndex);
					lastFGIndex = fgIndex;
					lastWasControlWord = true;
				}
			}

			// Set the background color.
			if (bg!=null) {
				int pos = getColorIndex(colorList, bg);
				document.append("\\highlight").append(pos+1);
				lastWasControlWord = true;
			}

			if (lastWasControlWord) {
				document.append(' '); // Delimiter
				lastWasControlWord = false;
			}
			escapeAndAdd(document, text);

			// Reset everything that was set for this text fragment.
			if (bg!=null) {
				document.append("\\highlight0");
				lastWasControlWord = true;
			}
			if (underline) {
				document.append("\\ul0");
				lastWasControlWord = true;
			}

		}

	}


	/**
	 * Appends some text to a buffer, with special care taken for special
	 * characters as defined by the RTF spec.
	 *
	 * <ul>
	 *   <li>All tab characters are replaced with the string
	 *       "<code>\tab</code>"
	 *   <li>'\', '{' and '}' are changed to "\\", "\{" and "\}"
	 * </ul>
	 *
	 * @param text The text to append (with tab chars substituted).
	 * @param sb The buffer to append to.
	 */
	private void escapeAndAdd(StringBuilder sb, String text) {
		int count = text.length();
		for (int i=0; i<count; i++) {
			char ch = text.charAt(i);
			switch (ch) {
				case '\t':
					// Micro-optimization: for syntax highlighting with
					// tab indentation, there are often multiple tabs
					// back-to-back at the start of lines, so don't put
					// spaces between each "\tab".
					sb.append("\\tab");
					while ((++i<count) && text.charAt(i)=='\t') {
						sb.append("\\tab");
					}
					sb.append(' ');
					i--; // We read one too far.
					break;
				case '\\':
				case '{':
				case '}':
					sb.append('\\').append(ch);
					break;
				default:
					if (ch <= 127) {
						sb.append(ch);
					} else {
						// Trailing space for delimiter
						sb.append("\\u").append((int)ch).append(' ');
					}
					break;
			}
		}
	}


	/**
	 * Returns a font point size, adjusted for the current screen resolution.<p>
	 *
	 * Java2D assumes 72 dpi.  On systems with larger dpi (Windows, GTK, etc.),
	 * font rendering will appear too small if we simply return a Java "Font"
	 * object's getSize() value.  We need to adjust it for the screen
	 * resolution.
	 *
	 * @param pointSize A Java Font's point size, as returned from
	 *        <code>getSize2D()</code>.
	 * @return The font point size, adjusted for the current screen resolution.
	 *         This will allow other applications to render fonts the same
	 *         size as they appear in the Java application.
	 */
	private int fixFontSize(float pointSize) {
		if (screenRes!=72) { // Java2D assumes 72 dpi
			pointSize = Math.round(pointSize*72f/screenRes);
		}
		return (int)pointSize;
	}


	/**
	 * Returns the index of the specified item in a list.  If the item
	 * is not in the list, it is added, and its new index is returned.
	 *
	 * @param list The list (possibly) containing the item.
	 * @param item The item to get the index of.
	 * @return The index of the item.
	 */
	private static int getColorIndex(List<Color> list, Color item) {
		int pos = list.indexOf(item);
		if (pos==-1) {
			list.add(item);
			pos = list.size()-1;
		}
		return pos;
	}


	private String getColorTableRtf() {

		// Example:
		// "{\\colortbl ;\\red255\\green0\\blue0;\\red0\\green0\\blue255; }"

		StringBuilder sb = new StringBuilder();

		sb.append("{\\colortbl ;");
		for (Color c : colorList) {
			sb.append("\\red").append(c.getRed());
			sb.append("\\green").append(c.getGreen());
			sb.append("\\blue").append(c.getBlue());
			sb.append(';');
		}
		sb.append("}");

		return sb.toString();

	}


	/**
	 * Returns the index of the specified font in a list of fonts.  This
	 * method only checks for a font by its family name; its attributes such
	 * as bold and italic are ignored.<p>
	 *
	 * If the font is not in the list, it is added, and its new index is
	 * returned.
	 *
	 * @param list The list (possibly) containing the font.
	 * @param font The font to get the index of.
	 * @return The index of the font.
	 */
	private static int getFontIndex(List<Font> list, Font font) {
		String fontName = font.getFamily();
		for (int i=0; i<list.size(); i++) {
			Font font2 = list.get(i);
			if (font2.getFamily().equals(fontName)) {
				return i;
			}
		}
		list.add(font);
		return list.size()-1;
	}


	private String getFontTableRtf() {

		// Example:
		// "{\\fonttbl{\\f0\\fmodern\\fcharset0 Courier;}}"

		StringBuilder sb = new StringBuilder();

		// Workaround for text areas using the Java logical font "Monospaced"
		// by default.  There's no way to know what it's mapped to, so we
		// just search for a monospaced font on the system.
		String monoFamilyName = getMonospacedFontFamily();

		sb.append("{\\fonttbl{\\f0\\fnil\\fcharset0 ").append(monoFamilyName).append(";}");
		for (int i=0; i<fontList.size(); i++) {
			Font f = fontList.get(i);
			String familyName = f.getFamily();
			if (familyName.equals("Monospaced")) {
				familyName = monoFamilyName;
			}
			sb.append("{\\f").append(i+1).append("\\fnil\\fcharset0 ");
			sb.append(familyName).append(";}");
		}
		sb.append('}');

		return sb.toString();

	}


	/**
	 * Returns a good "default" monospaced font to use when Java's logical
	 * font "Monospaced" is found.
	 *
	 * @return The monospaced font family to use.
	 */
	private static String getMonospacedFontFamily() {
		String family = RTextArea.getDefaultFont().getFamily();
		if ("Monospaced".equals(family)) {
			family = "Courier";
		}
		return family;
	}


	/**
	 * Returns the RTF document created by this generator.
	 *
	 * @return The RTF document, as a <code>String</code>.
	 */
	public String getRtf() {

		// Add background to the color table before adding it to our buffer
		int mainBGIndex = getColorIndex(colorList, mainBG);

		StringBuilder sb = new StringBuilder();
		sb.append("{");

		// Header
		sb.append("\\rtf1\\ansi\\ansicpg1252");
		sb.append("\\deff0"); // First font in font table is the default
		sb.append("\\deflang1033");
		sb.append("\\viewkind4");		// "Normal" view
		sb.append("\\uc\\pard\\f0");
		sb.append("\\fs20");			// Font size in half-points (default 24)
		sb.append(getFontTableRtf()).append('\n');
		sb.append(getColorTableRtf()).append('\n');

		// Content
		int bgIndex = mainBGIndex + 1;
		sb.append("\\cb").append(bgIndex).append(' ');
		lastWasControlWord = true;
		if (document.length() > 0) {
			document.append("\\line"); // Forced line break
		}
		sb.append(document);

		sb.append("}");

		//System.err.println("*** " + sb.length());
		return sb.toString();

	}


	/**
	 * Resets this generator.  All document information and content is
	 * cleared.
	 */
	public void reset() {
		fontList.clear();
		colorList.clear();
		document.setLength(0);
		lastWasControlWord = false;
		lastFontIndex = 0;
		lastFGIndex = 0;
		lastBold = false;
		lastItalic = false;
		lastFontSize = DEFAULT_FONT_SIZE;
		screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
	}


}
