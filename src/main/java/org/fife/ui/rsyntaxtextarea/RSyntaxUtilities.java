/*
 * 08/06/2004
 *
 * RSyntaxUtilities.java - Utility methods used by RSyntaxTextArea and its
 * views.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.Color;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.View;

import org.fife.ui.rsyntaxtextarea.TokenUtils.TokenSubList;
import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;


/**
 * Utility methods used by <code>RSyntaxTextArea</code> and its associated
 * classes.
 *
 * @author Robert Futrell
 * @version 0.2
 */
public final class RSyntaxUtilities implements SwingConstants {

	/**
	 * Integer constant representing a Windows-variant OS.
	 */
	public static final int OS_WINDOWS			= 1;

	/**
	 * Integer constant representing Mac OS X.
	 */
	public static final int OS_MAC_OSX			= 2;

	/**
	 * Integer constant representing Linux.
	 */
	public static final int OS_LINUX			= 4;

	/**
	 * Integer constant representing an "unknown" OS.  99.99% of the
	 * time, this means some UNIX variant (AIX, SunOS, etc.).
	 */
	public static final int OS_OTHER			= 8;

	/**
	 * Used for the color of hyperlinks when a LookAndFeel uses light text
	 * against a dark background.
	 */
	private static final Color LIGHT_HYPERLINK_FG = new Color(0xd8ffff);

	private static final int OS = getOSImpl();

	//private static final int DIGIT_MASK			= 1;
	private static final int LETTER_MASK			= 2;
	//private static final int WHITESPACE_MASK		= 4;
	//private static final int UPPER_CASE_MASK		= 8;
	private static final int HEX_CHARACTER_MASK		= 16;
	private static final int LETTER_OR_DIGIT_MASK	= 32;
	private static final int BRACKET_MASK			= 64;
	private static final int JAVA_OPERATOR_MASK		= 128;

	/**
	 * A lookup table used to quickly decide if a 16-bit Java char is a
	 * US-ASCII letter (A-Z or a-z), a digit, a whitespace char (either space
	 * (0x0020) or tab (0x0009)), etc.  This method should be faster
	 * than <code>Character.isLetter</code>, <code>Character.isDigit</code>,
	 * and <code>Character.isWhitespace</code> because we know we are dealing
	 * with ASCII chars and so don't have to worry about code planes, etc.
	 */
	private static final int[] DATA_TABLE = {
		 0,   0,   0,   0,   0,   0,   0,   0,   0,   4,   0,   0,   0,   0,   0,   0, // 0-15
		 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, // 16-31
		 4, 128,   0,   0,   0, 128, 128,   0,  64,  64, 128, 128,   0, 128,   0, 128, // 32-47
		49,  49,  49,  49,  49,  49,  49,  49,  49,  49, 128,   0, 128, 128, 128, 128, // 48-63
		 0,  58,  58,  58,  58,  58,  58,  42,  42,  42,  42,  42,  42,  42,  42,  42, // 64-79
		42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  42,  64,   0,  64, 128,   0, // 80-95
		 0,  50,  50,  50,  50,  50,  50,  34,  34,  34,  34,  34,  34,  34,  34,  34, // 96-111
		34,  34,  34,  34,  34,  34,  34,  34,  34,  34,  34,  64, 128,  64, 128,   0, // 112-127
		 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, // 128-143
		 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, // 144-
		 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, // 160-
		 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, // 176-
		 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, // 192-
		 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, // 208-
		 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0, // 224-
		 0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0,   0  // 240-255.
	};

	/**
	 * Used in bracket matching methods.
	 */
	private static Segment charSegment = new Segment();

	/**
	 * Used in token list manipulation methods.
	 */
	private static final TokenImpl TEMP_TOKEN = new TokenImpl();

	/**
	 * Used internally.
	 */
	private static final char[] JS_KEYWORD_RETURN = { 'r', 'e', 't', 'u', 'r', 'n' };
	private static final char[] JS_AND = { '&', '&' };
	private static final char[] JS_OR  = { '|', '|' };

	/**
	 * Used internally.
	 */
	private static final String BRACKETS = "{([})]";


	/**
	 * An unused constructor to prevent instantiation, and keep static analysis
	 * tools happy.
	 */
	private RSyntaxUtilities() { // NOSONAR
		// Private constructor to prevent instantiation.
	}


	/**
	 * Returns a string with characters that are special to HTML (such as
	 * <code>&lt;</code>, <code>&gt;</code> and <code>&amp;</code>) replaced
	 * by their HTML escape sequences.
	 *
	 * @param s The input string.
	 * @param newlineReplacement What to replace newline characters with.
	 *        If this is <code>null</code>, they are simply removed.
	 * @param inPreBlock Whether this HTML will be in within <code>pre</code>
	 *        tags.  If this is <code>true</code>, spaces will be kept as-is;
	 *        otherwise, they will be converted to "<code>&nbsp;</code>".
	 * @return The escaped version of <code>s</code>.
	 */
	public static String escapeForHtml(String s,
						String newlineReplacement, boolean inPreBlock) {

		if (s==null) {
			return null;
		}
		if (newlineReplacement==null) {
			newlineReplacement = "";
		}
		final String tabString = "   ";
		boolean lastWasSpace = false;

		StringBuilder sb = new StringBuilder();

		for (int i=0; i<s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
				case ' ':
					if (inPreBlock || !lastWasSpace) {
						sb.append(' ');
					}
					else {
						sb.append("&nbsp;");
					}
					lastWasSpace = true;
					break;
				case '\n':
					sb.append(newlineReplacement);
					lastWasSpace = false;
					break;
				case '&':
					sb.append("&amp;");
					lastWasSpace = false;
					break;
				case '\t':
					sb.append(tabString);
					lastWasSpace = false;
					break;
				case '<':
					sb.append("&lt;");
					lastWasSpace = false;
					break;
				case '>':
					sb.append("&gt;");
					lastWasSpace = false;
					break;
				case '\'':
					sb.append("&#39;");
					lastWasSpace = false;
					break;
				case '"':
					sb.append("&#34;");
					lastWasSpace = false;
					break;
				case '/': // OWASP-recommended even though unnecessary
					sb.append("&#47;");
					lastWasSpace = false;
					break;
				default:
					sb.append(ch);
					lastWasSpace = false;
					break;
			}
		}

		return sb.toString();

	}


	/**
	 * Returns the rendering hints for text that will most accurately reflect
	 * those of the native windowing system.
	 *
	 * @return The rendering hints, or <code>null</code> if they cannot be
	 *         determined.
	 */
	public static Map<?,?> getDesktopAntiAliasHints() {
		return (Map<?,?>)Toolkit.getDefaultToolkit().
				getDesktopProperty("awt.font.desktophints");
	}


	/**
	 * Returns the color to use for the line underneath a folded region line.
	 *
	 * @param textArea The text area.
	 * @return The color to use.
	 */
	public static Color getFoldedLineBottomColor(RSyntaxTextArea textArea) {
		Color color = Color.gray;
		Gutter gutter = RSyntaxUtilities.getGutter(textArea);
		if (gutter!=null) {
			color = gutter.getFoldIndicatorForeground();
		}
		return color;
	}


	/**
	 * Returns the gutter component of the scroll pane containing a text
	 * area, if any.
	 *
	 * @param textArea The text area.
	 * @return The gutter, or <code>null</code> if the text area is not in
	 *         an {@link RTextScrollPane}.
	 * @see RTextScrollPane#getGutter()
	 */
	public static Gutter getGutter(RTextArea textArea) {
		Gutter gutter = null;
		Container parent = textArea.getParent();
		if (parent instanceof JViewport) {
			parent = parent.getParent();
			if (parent instanceof RTextScrollPane) {
				RTextScrollPane sp = (RTextScrollPane)parent;
				gutter = sp.getGutter(); // Should always be non-null
			}
		}
		return gutter;
	}


	/**
	 * Returns the color to use for hyperlink-style components.  This method
	 * will return <code>Color.blue</code> unless it appears that the current
	 * LookAndFeel uses light text on a dark background, in which case a
	 * brighter alternative is returned.
	 *
	 * @return The color to use for hyperlinks.
	 * @see #isLightForeground(Color)
	 */
	public static Color getHyperlinkForeground() {

		// This property is defined by all standard LaFs, even Nimbus (!),
		// but you never know what crazy LaFs there are...
		Color fg = UIManager.getColor("Label.foreground");
		if (fg==null) {
			fg = new JLabel().getForeground();
		}

		return isLightForeground(fg) ? LIGHT_HYPERLINK_FG : Color.blue;

	}


	/**
	 * Returns the leading whitespace of a string.
	 *
	 * @param text The String to check.
	 * @return The leading whitespace.
	 * @see #getLeadingWhitespace(Document, int)
	 */
	public static String getLeadingWhitespace(String text) {
		int count = 0;
		int len = text.length();
		while (count<len && RSyntaxUtilities.isWhitespace(text.charAt(count))) {
			count++;
		}
		return text.substring(0, count);
	}


	/**
	 * Returns the leading whitespace of a specific line in a document.
	 *
	 * @param doc The document.
	 * @param offs The offset whose line to get the leading whitespace for.
	 * @return The leading whitespace.
	 * @throws BadLocationException If <code>offs</code> is not a valid offset
	 *         in the document.
	 * @see #getLeadingWhitespace(String)
	 */
	public static String getLeadingWhitespace(Document doc, int offs)
									throws BadLocationException {
		Element root = doc.getDefaultRootElement();
		int line = root.getElementIndex(offs);
		Element elem = root.getElement(line);
		int startOffs = elem.getStartOffset();
		int endOffs = elem.getEndOffset() - 1;
		String text = doc.getText(startOffs, endOffs-startOffs);
		return getLeadingWhitespace(text);
	}


	private static Element getLineElem(Document d, int offs) {
		Element map = d.getDefaultRootElement();
		int index = map.getElementIndex(offs);
		Element elem = map.getElement(index);
		if ((offs>=elem.getStartOffset()) && (offs<elem.getEndOffset())) {
			return elem;
		}
		return null;
	}


	/**
	 * Returns the bounding box (in the current view) of a specified position
	 * in the model.  This method is designed for line-wrapped views to use,
	 * as it allows you to specify a "starting position" in the line, from
	 * which the x-value is assumed to be zero.  The idea is that you specify
	 * the first character in a physical line as <code>p0</code>, as this is
	 * the character where the x-pixel value is 0.
	 *
	 * @param textArea The text area containing the text.
	 * @param s A segment in which to load the line.  This is passed in so we
	 *        don't have to reallocate a new <code>Segment</code> for each
	 *        call.
	 * @param p0 The starting position in the physical line in the document.
	 * @param p1 The position for which to get the bounding box in the view.
	 * @param e How to expand tabs.
	 * @param rect The rectangle whose x- and width-values are changed to
	 *        represent the bounding box of <code>p1</code>.  This is reused
	 *        to keep from needlessly reallocating Rectangles.
	 * @param x0 The x-coordinate (pixel) marking the left-hand border of the
	 *        text.  This is useful if the text area has a border, for example.
	 * @return The bounding box in the view of the character <code>p1</code>.
	 * @throws BadLocationException If <code>p0</code> or <code>p1</code> is
	 *         not a valid location in the specified text area's document.
	 * @throws IllegalArgumentException If <code>p0</code> and <code>p1</code>
	 *         are not on the same line.
	 */
	public static Rectangle getLineWidthUpTo(RSyntaxTextArea textArea,
								Segment s, int p0, int p1,
								TabExpander e, Rectangle rect,
								int x0)
								throws BadLocationException {

		RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();

		// Ensure p0 and p1 are valid document positions.
		if (p0<0) {
			throw new BadLocationException("Invalid document position", p0);
		}
		else if (p1>doc.getLength()) {
			throw new BadLocationException("Invalid document position", p1);
		}

		// Ensure p0 and p1 are in the same line, and get the start/end
		// offsets for that line.
		Element map = doc.getDefaultRootElement();
		int lineNum = map.getElementIndex(p0);
		// We do ">1" because p1 might be the first position on the next line
		// or the last position on the previous one.
		// if (lineNum!=map.getElementIndex(p1))
		if (Math.abs(lineNum-map.getElementIndex(p1))>1) {
			throw new IllegalArgumentException("p0 and p1 are not on the " +
						"same line (" + p0 + ", " + p1 + ").");
		}

		// Get the token list.
		Token t = doc.getTokenListForLine(lineNum);

		// Modify the token list 't' to begin at p0 (but still have correct
		// token types, etc.), and get the x-location (in pixels) of the
		// beginning of this new token list.
		TokenSubList subList = TokenUtils.getSubTokenList(t, p0, e, textArea,
				0, TEMP_TOKEN);
		t = subList.tokenList;

		rect = t.listOffsetToView(textArea, e, p1, x0, rect);
		return rect;

	}


	/**
	 * Returns the location of the bracket paired with the one at the current
	 * caret position.
	 *
	 * @param textArea The text area.
	 * @param input A point to use as the return value.  If this is
	 *        <code>null</code>, a new object is created and returned.
	 * @return A point representing the matched bracket info.  The "x" field
	 *         is the offset of the bracket at the caret position (either just
	 *         before or just after the caret), and the "y" field is the offset
	 *         of the matched bracket.  Both "x" and "y" will be
	 *         <code>-1</code> if there isn't a matching bracket (or the caret
	 *         isn't on a bracket).
	 */
	public static Point getMatchingBracketPosition(RSyntaxTextArea textArea,
										Point input) {

		if (input==null) {
			input = new Point();
		}
		input.setLocation(-1, -1);

		try {

			// Actually position just BEFORE caret.
			int caretPosition = textArea.getCaretPosition() - 1;
			RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
			char bracket = 0;

			// If the caret was at offset 0, we can't check "to its left."
			if (caretPosition>=0) {
				bracket  = doc.charAt(caretPosition);
			}

			// Try to match a bracket "to the right" of the caret if one
			// was not found on the left.
			int index = BRACKETS.indexOf(bracket);
			if (index==-1 && caretPosition<doc.getLength()-1) {
				bracket = doc.charAt(++caretPosition);
			}

			// First, see if the char was a bracket (one of "{[()]}").
			if (index==-1) {
				index = BRACKETS.indexOf(bracket);
				if (index==-1) {
					return input;
				}
			}

			// If it was, then make sure this bracket isn't sitting in
			// the middle of a comment or string.  If it isn't, then
			// initialize some stuff so we can continue on.
			char bracketMatch;
			boolean goForward;
			Element map = doc.getDefaultRootElement();
			int curLine = map.getElementIndex(caretPosition);
			Element line = map.getElement(curLine);
			int start = line.getStartOffset();
			int end = line.getEndOffset();
			Token token = doc.getTokenListForLine(curLine);
			token = RSyntaxUtilities.getTokenAtOffset(token, caretPosition);
			// All brackets are always returned as "separators."
			if (token.getType()!=Token.SEPARATOR) {
				return input;
			}
			int languageIndex = token.getLanguageIndex();
			if (index<3) { // One of "{[("
				goForward = true;
				bracketMatch = BRACKETS.charAt(index + 3);
			}
			else { // One of ")]}"
				goForward = false;
				bracketMatch = BRACKETS.charAt(index - 3);
			}

			if (goForward) {

				int lastLine = map.getElementCount();

				// Start just after the found bracket since we're sure
				// we're not in a comment.
				start = caretPosition + 1;
				int numEmbedded = 0;
				boolean haveTokenList = false;

				while (true) {

					doc.getText(start,end-start, charSegment);
					int segOffset = charSegment.offset;

					for (int i=segOffset; i<segOffset+charSegment.count; i++) {

						char ch = charSegment.array[i];

						if (ch==bracket) {
							if (!haveTokenList) {
								token = doc.getTokenListForLine(curLine);
								haveTokenList = true;
							}
							int offset = start + (i-segOffset);
							token = RSyntaxUtilities.getTokenAtOffset(token, offset);
							if (token.getType()==Token.SEPARATOR &&
									token.getLanguageIndex()==languageIndex) {
								numEmbedded++;
							}
						}

						else if (ch==bracketMatch) {
							if (!haveTokenList) {
								token = doc.getTokenListForLine(curLine);
								haveTokenList = true;
							}
							int offset = start + (i-segOffset);
							token = RSyntaxUtilities.getTokenAtOffset(token, offset);
							if (token.getType()==Token.SEPARATOR &&
									token.getLanguageIndex()==languageIndex) {
								if (numEmbedded==0) {
									if (textArea.isCodeFoldingEnabled() &&
											textArea.getFoldManager().isLineHidden(curLine)) {
										return input; // Match hidden in a fold
									}
									input.setLocation(caretPosition, offset);
									return input;
								}
								numEmbedded--;
							}
						}

					} // End of for (int i=segOffset; i<segOffset+charSegment.count; i++).

					// Bail out if we've gone through all lines and
					// haven't found the match.
					if (++curLine==lastLine) {
						return input;
					}

					// Otherwise, go through the next line.
					haveTokenList = false;
					line = map.getElement(curLine);
					start = line.getStartOffset();
					end = line.getEndOffset();

				} // End of while (true).

			} // End of if (goForward).


			// Otherwise, we're going backward through the file
			// (since we found '}', ')' or ']').
			else {	// goForward==false

				// End just before the found bracket since we're sure
				// we're not in a comment.
				end = caretPosition;// - 1;
				int numEmbedded = 0;
				boolean haveTokenList = false;
				Token t2;

				while (true) {

					doc.getText(start,end-start, charSegment);
					int segOffset = charSegment.offset;
					int iStart = segOffset + charSegment.count - 1;

					for (int i=iStart; i>=segOffset; i--) {

						char ch = charSegment.array[i];

						if (ch==bracket) {
							if (!haveTokenList) {
								token = doc.getTokenListForLine(curLine);
								haveTokenList = true;
							}
							int offset = start + (i-segOffset);
							t2 = RSyntaxUtilities.getTokenAtOffset(token, offset);
							if (t2.getType()==Token.SEPARATOR &&
									token.getLanguageIndex()==languageIndex) {
								numEmbedded++;
							}
						}

						else if (ch==bracketMatch) {
							if (!haveTokenList) {
								token = doc.getTokenListForLine(curLine);
								haveTokenList = true;
							}
							int offset = start + (i-segOffset);
							t2 = RSyntaxUtilities.getTokenAtOffset(token, offset);
							if (t2.getType()==Token.SEPARATOR &&
									token.getLanguageIndex()==languageIndex) {
								if (numEmbedded==0) {
									input.setLocation(caretPosition, offset);
									return input;
								}
								numEmbedded--;
							}
						}

					}

					// Bail out if we've gone through all lines and
					// haven't found the match.
					if (--curLine==-1) {
						return input;
					}

					// Otherwise, get ready for going through the
					// next line.
					haveTokenList = false;
					line = map.getElement(curLine);
					start = line.getStartOffset();
					end = line.getEndOffset();

				} // End of while (true).

			} // End of else.

		} catch (BadLocationException ble) {
			// Shouldn't ever happen.
			ble.printStackTrace();
		}

		// Something went wrong...
		return input;

	}


	/**
	 * Returns the next non-whitespace, non-comment token in a text area.
	 *
	 * @param t The next token in this line's token list.
	 * @param textArea The text area.
	 * @param line The current line index (the line index of <code>t</code>).
	 * @return The next non-whitespace, non-comment token, or <code>null</code>
	 *         if there isn't one.
	 * @see #getPreviousImportantToken(RSyntaxDocument, int)
	 * @see #getPreviousImportantTokenFromOffs(RSyntaxDocument, int)
	 */
	public static Token getNextImportantToken(Token t,
			RSyntaxTextArea textArea, int line) {
		while (t!=null && t.isPaintable() && t.isCommentOrWhitespace()) {
			t = t.getNextToken();
		}
		if ((t==null || !t.isPaintable()) && line<textArea.getLineCount()-1) {
			t = textArea.getTokenListForLine(++line);
			return getNextImportantToken(t, textArea, line);
		}
		return t;
	}


	/**
	 * Provides a way to determine the next visually represented model
	 * location at which one might place a caret.
	 * Some views may not be visible,
	 * they might not be in the same order found in the model, or they just
	 * might not allow access to some of the locations in the model.<p>
	 *
	 * NOTE:  You should only call this method if the passed-in
	 * <code>javax.swing.text.View</code> is an instance of
	 * {@link TokenOrientedView} and <code>javax.swing.text.TabExpander</code>;
	 * otherwise, a <code>ClassCastException</code> could be thrown.
	 *
	 * @param pos the position to convert &gt;= 0
	 * @param a the allocated region in which to render
	 * @param direction the direction from the current position that can
	 *  be thought of as the arrow keys typically found on a keyboard.
	 *  This will be one of the following values:
	 * <ul>
	 * <li>SwingConstants.WEST
	 * <li>SwingConstants.EAST
	 * <li>SwingConstants.NORTH
	 * <li>SwingConstants.SOUTH
	 * </ul>
	 * @return the location within the model that best represents the next
	 *  location visual position
	 * @exception BadLocationException
	 * @exception IllegalArgumentException if <code>direction</code>
	 *		doesn't have one of the legal values above
	 */
	public static int getNextVisualPositionFrom(int pos, Position.Bias b,
									Shape a, int direction,
									Position.Bias[] biasRet, View view)
									throws BadLocationException {

		RSyntaxTextArea target = (RSyntaxTextArea)view.getContainer();
		biasRet[0] = Position.Bias.Forward;

		// Do we want the "next position" above, below, to the left or right?
		switch (direction) {

			case NORTH:
			case SOUTH:
				if (pos == -1) {
					pos = (direction == NORTH) ?
								Math.max(0, view.getEndOffset() - 1) :
								view.getStartOffset();
					break;
				}
				Caret c = (target != null) ? target.getCaret() : null;
				// YECK! Ideally, the x location from the magic caret
				// position would be passed in.
				Point mcp;
				if (c != null) {
					mcp = c.getMagicCaretPosition();
				}
				else {
					mcp = null;
				}
				int x;
				if (mcp == null) {
					Rectangle loc = target.modelToView(pos);
					x = (loc == null) ? 0 : loc.x;
				}
				else {
					x = mcp.x;
				}
				if (direction == NORTH) {
					pos = getPositionAbove(target,pos,x,(TabExpander)view);
				}
				else {
					pos = getPositionBelow(target,pos,x,(TabExpander)view);
				}
				break;

			case WEST:
				int endOffs = view.getEndOffset();
				if(pos == -1) {
					pos = Math.max(0, endOffs - 1);
				}
				else {
					pos = Math.max(0, pos - 1);
					if (target.isCodeFoldingEnabled()) {
						int last = pos==endOffs-1 ? target.getLineCount()-1 :
							target.getLineOfOffset(pos+1);
						int current = target.getLineOfOffset(pos);
						if (last!=current) { // If moving up a line...
							FoldManager fm = target.getFoldManager();
							if (fm.isLineHidden(current)) {
								while (--current>0 && fm.isLineHidden(current));
								pos = target.getLineEndOffset(current) - 1;
							}
						}
					}
				}
				break;

			case EAST:
				if(pos == -1) {
					pos = view.getStartOffset();
				}
				else {
					pos = Math.min(pos + 1, view.getDocument().getLength());
					if (target.isCodeFoldingEnabled()) {
						int last = pos==0 ? 0 : target.getLineOfOffset(pos-1);
						int current = target.getLineOfOffset(pos);
						if (last!=current) { // If moving down a line...
							FoldManager fm = target.getFoldManager();
							if (fm.isLineHidden(current)) {
								int lineCount = target.getLineCount();
								while (++current<lineCount && fm.isLineHidden(current));
								pos = current==lineCount ?
										target.getLineEndOffset(last)-1 : // Was the last visible line
										target.getLineStartOffset(current);
							}
						}
					}
				}
				break;

			default:
				throw new IllegalArgumentException(
									"Bad direction: " + direction);
		}

		return pos;

	}


	/**
	 * Returns an integer constant representing the OS.  This can be handy for
	 * special case situations such as Mac OS-X (special application
	 * registration) or Windows (allow mixed case, etc.).
	 *
	 * @return An integer constant representing the OS.
	 */
	public static int getOS() {
		return OS;
	}


	/**
	 * Returns an integer constant representing the OS.  This can be handy for
	 * special case situations such as Mac OS-X (special application
	 * registration) or Windows (allow mixed case, etc.).
	 *
	 * @return An integer constant representing the OS.
	 */
	private static int getOSImpl() {
		int os = OS_OTHER;
		String osName = System.getProperty("os.name");
		if (osName!=null) { // Should always be true.
			osName = osName.toLowerCase();
			if (osName.indexOf("windows") > -1) {
				os = OS_WINDOWS;
			}
			else if (osName.indexOf("mac os x") > -1) {
				os = OS_MAC_OSX;
			}
			else if (osName.indexOf("linux") > -1) {
				os = OS_LINUX;
			}
			else {
				os = OS_OTHER;
			}
		}
		return os;
	}


	/**
	 * Returns the flags necessary to create a {@link Pattern}.
	 *
	 * @param matchCase Whether the pattern should be case sensitive.
	 * @param others Any other flags.  This may be <code>0</code>.
	 * @return The flags.
	 */
	public static int getPatternFlags(boolean matchCase, int others) {
		if (!matchCase) {
			others |= Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE;
		}
		return others;
	}


	/**
	 * Determines the position in the model that is closest to the given
	 * view location in the row above.  The component given must have a
	 * size to compute the result.  If the component doesn't have a size
	 * a value of -1 will be returned.
	 *
	 * @param c the editor
	 * @param offs the offset in the document &gt;= 0
	 * @param x the X coordinate &gt;= 0
	 * @return the position &gt;= 0 if the request can be computed, otherwise
	 *  a value of -1 will be returned.
	 * @exception BadLocationException if the offset is out of range
	 */
	public static int getPositionAbove(RSyntaxTextArea c, int offs,
					float x, TabExpander e) throws BadLocationException {

		TokenOrientedView tov = (TokenOrientedView)e;
		Token token = tov.getTokenListForPhysicalLineAbove(offs);
		if (token==null) {
			return -1;
		}
		else if (token.getType()==Token.NULL) {
			int line = c.getLineOfOffset(offs);	// Sure to be >0 ??
			return c.getLineStartOffset(line-1);
		}

		else {
			return token.getListOffset(c, e, c.getMargin().left, x);
		}

	}


	/**
	 * Determines the position in the model that is closest to the given
	 * view location in the row below.  The component given must have a
	 * size to compute the result.  If the component doesn't have a size
	 * a value of -1 will be returned.
	 *
	 * @param c the editor
	 * @param offs the offset in the document &gt;= 0
	 * @param x the X coordinate &gt;= 0
	 * @return the position &gt;= 0 if the request can be computed, otherwise
	 *  a value of -1 will be returned.
	 * @exception BadLocationException if the offset is out of range
	 */
	public static int getPositionBelow(RSyntaxTextArea c, int offs,
					float x, TabExpander e) throws BadLocationException {

		TokenOrientedView tov = (TokenOrientedView)e;
		Token token = tov.getTokenListForPhysicalLineBelow(offs);
		if (token==null) {
			return -1;
		}
		else if (token.getType()==Token.NULL) {
			int line = c.getLineOfOffset(offs);	// Sure to be > c.getLineCount()-1 ??
//			return c.getLineStartOffset(line+1);
FoldManager fm = c.getFoldManager();
line = fm.getVisibleLineBelow(line);
return c.getLineStartOffset(line);
		}

		else {
			return token.getListOffset(c, e, c.getMargin().left, x);
		}

	}


	/**
	 * Returns the last non-whitespace, non-comment token, starting with the
	 * specified line.
	 *
	 * @param doc The document.
	 * @param line The line at which to start looking.
	 * @return The last non-whitespace, non-comment token, or <code>null</code>
	 *         if there isn't one.
	 * @see #getNextImportantToken(Token, RSyntaxTextArea, int)
	 * @see #getPreviousImportantTokenFromOffs(RSyntaxDocument, int)
	 */
	public static Token getPreviousImportantToken(RSyntaxDocument doc,
			int line) {
		if (line<0) {
			return null;
		}
		Token t = doc.getTokenListForLine(line);
		if (t!=null) {
			t = t.getLastNonCommentNonWhitespaceToken();
			if (t!=null) {
				return t;
			}
		}
		return getPreviousImportantToken(doc, line-1);
	}


	/**
	 * Returns the last non-whitespace, non-comment token, before the
	 * specified offset.
	 *
	 * @param doc The document.
	 * @param offs The ending offset for the search.
	 * @return The last non-whitespace, non-comment token, or <code>null</code>
	 *         if there isn't one.
	 * @see #getPreviousImportantToken(RSyntaxDocument, int)
	 * @see #getNextImportantToken(Token, RSyntaxTextArea, int)
	 */
	public static Token getPreviousImportantTokenFromOffs(
			RSyntaxDocument doc, int offs) {

		Element root = doc.getDefaultRootElement();
		int line = root.getElementIndex(offs);
		Token t = doc.getTokenListForLine(line);

		// Check line containing offs
		Token target = null;
		while (t!=null && t.isPaintable() && !t.containsPosition(offs)) {
			if (!t.isCommentOrWhitespace()) {
				target = t;
			}
			t = t.getNextToken();
		}

		// Check previous line(s)
		if (target==null) {
			target = RSyntaxUtilities.getPreviousImportantToken(doc, line-1);
		}

		return target;

	}


	/**
	 * Returns the token at the specified offset.
	 *
	 * @param textArea The text area.
	 * @param offset The offset of the token.
	 * @return The token, or <code>null</code> if the offset is not valid.
	 * @see #getTokenAtOffset(RSyntaxDocument, int)
	 * @see #getTokenAtOffset(Token, int)
	 */
	public static Token getTokenAtOffset(RSyntaxTextArea textArea,
			int offset) {
		RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
		return RSyntaxUtilities.getTokenAtOffset(doc, offset);
	}


	/**
	 * Returns the token at the specified offset.
	 *
	 * @param doc The document.
	 * @param offset The offset of the token.
	 * @return The token, or <code>null</code> if the offset is not valid.
	 * @see #getTokenAtOffset(RSyntaxTextArea, int)
	 * @see #getTokenAtOffset(Token, int)
	 */
	public static Token getTokenAtOffset(RSyntaxDocument doc,
			int offset) {
		Element root = doc.getDefaultRootElement();
		int lineIndex = root.getElementIndex(offset);
		Token t = doc.getTokenListForLine(lineIndex);
		return RSyntaxUtilities.getTokenAtOffset(t, offset);
	}


	/**
	 * Returns the token at the specified index, or <code>null</code> if
	 * the given offset isn't in this token list's range.<br>
	 * Note that this method does NOT check to see if <code>tokenList</code>
	 * is null; callers should check for themselves.
	 *
	 * @param tokenList The list of tokens in which to search.
	 * @param offset The offset at which to get the token.
	 * @return The token at <code>offset</code>, or <code>null</code> if
	 *         none of the tokens are at that offset.
	 * @see #getTokenAtOffset(RSyntaxTextArea, int)
	 * @see #getTokenAtOffset(RSyntaxDocument, int)
	 */
	public static Token getTokenAtOffset(Token tokenList, int offset) {
		for (Token t=tokenList; t!=null && t.isPaintable(); t=t.getNextToken()){
			if (t.containsPosition(offset)) {
				return t;
			}
		}
		return null;
	}


	/**
	 * Returns the end of the word at the given offset.
	 *
	 * @param textArea The text area.
	 * @param offs The offset into the text area's content.
	 * @return The end offset of the word.
	 * @throws BadLocationException If <code>offs</code> is invalid.
	 * @see #getWordStart(RSyntaxTextArea, int)
	 */
	public static int getWordEnd(RSyntaxTextArea textArea, int offs)
										throws BadLocationException {

		Document doc = textArea.getDocument();
		int endOffs = textArea.getLineEndOffsetOfCurrentLine();
		int lineEnd = Math.min(endOffs, doc.getLength());
		if (offs == lineEnd) { // End of the line.
			return offs;
		}

		String s = doc.getText(offs, lineEnd-offs-1);
		if (s!=null && s.length()>0) { // Should always be true
			int i = 0;
			int count = s.length();
			char ch = s.charAt(i);
			if (Character.isWhitespace(ch)) {
				while (i<count && Character.isWhitespace(s.charAt(i++)));
			}
			else if (Character.isLetterOrDigit(ch)) {
				while (i<count && Character.isLetterOrDigit(s.charAt(i++)));
			}
			else {
				i = 2;
			}
			offs += i - 1;
		}

		return offs;

	}

	/**
	 * Returns the start of the word at the given offset.
	 *
	 * @param textArea The text area.
	 * @param offs The offset into the text area's content.
	 * @return The start offset of the word.
	 * @throws BadLocationException If <code>offs</code> is invalid.
	 * @see #getWordEnd(RSyntaxTextArea, int)
	 */
	public static int getWordStart(RSyntaxTextArea textArea, int offs)
											throws BadLocationException {

		Document doc = textArea.getDocument();
		Element line = getLineElem(doc, offs);
		if (line == null) {
			throw new BadLocationException("No word at " + offs, offs);
		}

		int lineStart = line.getStartOffset();
		if (offs==lineStart) { // Start of the line.
			return offs;
		}

		int endOffs = Math.min(offs+1, doc.getLength());
		String s = doc.getText(lineStart, endOffs-lineStart);
		if(s != null && s.length() > 0) {
			int i = s.length() - 1;
			char ch = s.charAt(i);
			if (Character.isWhitespace(ch)) {
				while (i>0 && Character.isWhitespace(s.charAt(i-1))) {
					i--;
				}
				offs = lineStart + i;
			}
			else if (Character.isLetterOrDigit(ch)) {
				while (i>0 && Character.isLetterOrDigit(s.charAt(i-1))) {
					i--;
				}
				offs = lineStart + i;
			}

		}

		return offs;

	}


	/**
	 * Determines the width of the given token list taking tabs
	 * into consideration.  This is implemented in a 1.1 style coordinate
	 * system where ints are used and 72dpi is assumed.<p>
	 *
	 * This method also assumes that the passed-in token list begins at
	 * x-pixel <code>0</code> in the view (for tab purposes).
	 *
	 * @param tokenList The tokenList list representing the text.
	 * @param textArea The text area in which this token list resides.
	 * @param e The tab expander.  This value cannot be <code>null</code>.
	 * @return The width of the token list, in pixels.
	 */
	public static float getTokenListWidth(Token tokenList,
									RSyntaxTextArea textArea,
									TabExpander e) {
		return getTokenListWidth(tokenList, textArea, e, 0);
	}


	/**
	 * Determines the width of the given token list taking tabs
	 * into consideration.  This is implemented in a 1.1 style coordinate
	 * system where ints are used and 72dpi is assumed.<p>
	 *
	 * @param tokenList The token list list representing the text.
	 * @param textArea The text area in which this token list resides.
	 * @param e The tab expander.  This value cannot be <code>null</code>.
	 * @param x0 The x-pixel coordinate of the start of the token list.
	 * @return The width of the token list, in pixels.
	 * @see #getTokenListWidthUpTo
	 */
	public static float getTokenListWidth(final Token tokenList,
									RSyntaxTextArea textArea,
									TabExpander e, float x0) {
		float width = x0;
		for (Token t=tokenList; t!=null&&t.isPaintable(); t=t.getNextToken()) {
			width += t.getWidth(textArea, e, width);
		}
		return width - x0;
	}


	/**
	 * Determines the width of the given token list taking tabs into
	 * consideration and only up to the given index in the document
	 * (exclusive).
	 *
	 * @param tokenList The token list representing the text.
	 * @param textArea The text area in which this token list resides.
	 * @param e The tab expander.  This value cannot be <code>null</code>.
	 * @param x0 The x-pixel coordinate of the start of the token list.
	 * @param upTo The document position at which you want to stop,
	 *        exclusive.  If this position is before the starting position
	 *        of the token list, a width of <code>0</code> will be
	 *        returned; similarly, if this position comes after the entire
	 *        token list, the width of the entire token list is returned.
	 * @return The width of the token list, in pixels, up to, but not
	 *         including, the character at position <code>upTo</code>.
	 * @see #getTokenListWidth
	 */
	public static float getTokenListWidthUpTo(final Token tokenList,
								RSyntaxTextArea textArea, TabExpander e,
								float x0, int upTo) {
		float width = 0;
		for (Token t=tokenList; t!=null&&t.isPaintable(); t=t.getNextToken()) {
			if (t.containsPosition(upTo)) {
				return width + t.getWidthUpTo(upTo-t.getOffset(), textArea, e,
													x0+width);
			}
			width += t.getWidth(textArea, e, x0+width);
		}
		return width;
	}


	/**
	 * Returns whether or not this character is a "bracket" to be matched by
	 * such programming languages as C, C++, and Java.
	 *
	 * @param ch The character to check.
	 * @return Whether or not the character is a "bracket" - one of '(', ')',
	 *         '[', ']', '{', and '}'.
	 */
	public static boolean isBracket(char ch) {
		// We need the first condition as it might be that ch>255, and thus
		// not in our table.  '}' is the highest-valued char in the bracket
		// set.
		return ch<='}' && (DATA_TABLE[ch]&BRACKET_MASK)>0;
	}


	/**
	 * Returns whether or not a character is a digit (0-9).
	 *
	 * @param ch The character to check.
	 * @return Whether or not the character is a digit.
	 */
	public static boolean isDigit(char ch) {
		// We do it this way as we'd need to do two conditions anyway (first
		// to check that ch<255 so it can index into our table, then whether
		// that table position has the digit mask).
		return ch>='0' && ch<='9';
	}


	/**
	 * Returns whether or not this character is a hex character.  This method
	 * accepts both upper- and lower-case letters a-f.
	 *
	 * @param ch The character to check.
	 * @return Whether or not the character is a hex character 0-9, a-f, or
	 *         A-F.
	 */
	public static boolean isHexCharacter(char ch) {
		// We need the first condition as it could be that ch>255 (and thus
		// not a valid index into our table).  'f' is the highest-valued
		// char that is a valid hex character.
		return (ch<='f') && (DATA_TABLE[ch]&HEX_CHARACTER_MASK)>0;
	}


	/**
	 * Returns whether a character is a Java operator.  Note that C and C++
	 * operators are the same as Java operators.
	 *
	 * @param ch The character to check.
	 * @return Whether or not the character is a Java operator.
	 */
	public static boolean isJavaOperator(char ch) {
		// We need the first condition as it could be that ch>255 (and thus
		// not a valid index into our table).  '~' is the highest-valued
		// char that is a valid Java operator.
		return (ch<='~') && (DATA_TABLE[ch]&JAVA_OPERATOR_MASK)>0;
	}


	/**
	 * Returns whether a character is a US-ASCII letter (A-Z or a-z).
	 *
	 * @param ch The character to check.
	 * @return Whether or not the character is a US-ASCII letter.
	 */
	public static boolean isLetter(char ch) {
		// We need the first condition as it could be that ch>255 (and thus
		// not a valid index into our table).
		return (ch<='z') && (DATA_TABLE[ch]&LETTER_MASK)>0;
	}


	/**
	 * Returns whether or not a character is a US-ASCII letter or a digit.
	 *
	 * @param ch The character to check.
	 * @return Whether or not the character is a US-ASCII letter or a digit.
	 */
	public static boolean isLetterOrDigit(char ch) {
		// We need the first condition as it could be that ch>255 (and thus
		// not a valid index into our table).
		return (ch<='z') && (DATA_TABLE[ch]&LETTER_OR_DIGIT_MASK)>0;
	}


	/**
	 * Returns whether the specified color is "light" to use as a foreground.
	 * Colors that return <code>true</code> indicate that the current Look and
	 * Feel probably uses light text colors on a dark background.
	 *
	 * @param fg The foreground color.
	 * @return Whether it is a "light" foreground color.
	 * @see #getHyperlinkForeground()
	 */
	public static boolean isLightForeground(Color fg) {
		return fg.getRed()>0xa0 && fg.getGreen()>0xa0 && fg.getBlue()>0xa0;
	}


	/**
	 * Returns whether the specified token is a single non-word char (e.g. not
	 * in <code>[A-Za-z]</code>.  This is a HACK to work around the fact that
	 * many standard token makers return things like semicolons and periods as
	 * {@link Token#IDENTIFIER}s just to make the syntax highlighting coloring
	 * look a little better.
	 *
	 * @param t The token to check.  This cannot be <code>null</code>.
	 * @return Whether the token is a single non-word char.
	 */
	public static boolean isNonWordChar(Token t) {
		return t.length()==1 && !RSyntaxUtilities.isLetter(t.charAt(0));
	}


	/**
	 * Returns whether or not a character is a whitespace character (either
	 * a space ' ' or tab '\t').  This checks for the Unicode character values
	 * 0x0020 and 0x0009.
	 *
	 * @param ch The character to check.
	 * @return Whether or not the character is a whitespace character.
	 */
	public static boolean isWhitespace(char ch) {
		// We do it this way as we'd need to do two conditions anyway (first
		// to check that ch<255 so it can index into our table, then whether
		// that table position has the whitespace mask).
		return ch==' ' || ch=='\t';
	}


	/**
	 * Repaints the gutter in a text area's scroll pane, if necessary.
	 *
	 * @param textArea The text area.
	 */
	public static void possiblyRepaintGutter(RTextArea textArea) {
		Gutter gutter = RSyntaxUtilities.getGutter(textArea);
		if (gutter!=null) {
			gutter.repaint();
		}
	}


	/**
	 * Returns whether a regular expression token can follow the specified
	 * token in JavaScript.
	 *
	 * @param t The token to check, which may be <code>null</code>.
	 * @return Whether a regular expression token may follow this one in
	 *         JavaScript.
	 */
	public static boolean regexCanFollowInJavaScript(Token t) {
		char ch;
		// We basically try to mimic Eclipse's JS editor's behavior here.
		return t==null ||
				//t.isOperator() ||
				(t.length()==1 && (
					(ch=t.charAt(0))=='=' ||
					ch=='(' ||
					ch==',' ||
					ch=='?' ||
					ch==':' ||
					ch=='[' ||
					ch=='!' ||
					ch=='&'
				)) ||
				/* Operators "==", "===", "!=", "!==", "&&", "||" */
				(t.getType()==Token.OPERATOR &&
					(t.charAt(t.length()-1)=='=' ||
					t.is(JS_AND) || t.is(JS_OR))) ||
				t.is(Token.RESERVED_WORD_2, JS_KEYWORD_RETURN);
	}


	/**
	 * Selects a range of text in a text component.  If the new selection is
	 * outside of the previous viewable rectangle, then the view is centered
	 * around the new selection.
	 *
	 * @param textArea The text component whose selection is to be centered.
	 * @param range The range to select.
	 */
	public static void selectAndPossiblyCenter(JTextArea textArea,
			DocumentRange range, boolean select) {

		int start = range.getStartOffset();
		int end = range.getEndOffset();

		boolean foldsExpanded = false;
		if (textArea instanceof RSyntaxTextArea) {
			RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
			FoldManager fm = rsta.getFoldManager();
			if (fm.isCodeFoldingSupportedAndEnabled()) {
				foldsExpanded = fm.ensureOffsetNotInClosedFold(start);
				foldsExpanded |= fm.ensureOffsetNotInClosedFold(end);
			}
		}

		if (select) {
			textArea.setSelectionStart(start);
			textArea.setSelectionEnd(end);
		}

		Rectangle r = null;
		try {
			r = textArea.modelToView(start);
			if (r==null) { // Not yet visible; i.e. JUnit tests
				return;
			}
			if (end!=start) {
				r = r.union(textArea.modelToView(end));
			}
		} catch (BadLocationException ble) { // Never happens
			ble.printStackTrace();
			if (select) {
				textArea.setSelectionStart(start);
				textArea.setSelectionEnd(end);
			}
			return;
		}

		Rectangle visible = textArea.getVisibleRect();

		// If the new selection is already in the view, don't scroll,
		// as that is visually jarring.
		if (!foldsExpanded && visible.contains(r)) {
			if (select) {
				textArea.setSelectionStart(start);
				textArea.setSelectionEnd(end);
			}
			return;
		}

		visible.x = r.x - (visible.width - r.width) / 2;
		visible.y = r.y - (visible.height - r.height) / 2;

		Rectangle bounds = textArea.getBounds();
		Insets i = textArea.getInsets();
		bounds.x = i.left;
		bounds.y = i.top;
		bounds.width -= i.left + i.right;
		bounds.height -= i.top + i.bottom;

		if (visible.x < bounds.x) {
			visible.x = bounds.x;
		}

		if (visible.x + visible.width > bounds.x + bounds.width) {
			visible.x = bounds.x + bounds.width - visible.width;
		}

		if (visible.y < bounds.y) {
			visible.y = bounds.y;
		}

		if (visible.y + visible.height > bounds.y + bounds.height) {
			visible.y = bounds.y + bounds.height - visible.height;
		}

		textArea.scrollRectToVisible(visible);

	}


	/**
	 * If the character is an upper-case US-ASCII letter, it returns the
	 * lower-case version of that letter; otherwise, it just returns the
	 * character.
	 *
	 * @param ch The character to lower-case (if it is a US-ASCII upper-case
	 *        character).
	 * @return The lower-case version of the character.
	 */
	public static char toLowerCase(char ch) {
		// We can logical OR with 32 because A-Z are 65-90 in the ASCII table
		// and none of them have the 6th bit (32) set, and a-z are 97-122 in
		// the ASCII table, which is 32 over from A-Z.
		// We do it this way as we'd need to do two conditions anyway (first
		// to check that ch<255 so it can index into our table, then whether
		// that table position has the upper-case mask).
		if (ch>='A' && ch<='Z') {
			return (char)(ch | 0x20);
		}
		return ch;
	}


	/**
	 * Creates a regular expression pattern that matches a "wildcard" pattern.
	 *
	 * @param wildcard The wildcard pattern.
	 * @param matchCase Whether the pattern should be case sensitive.
	 * @param escapeStartChar Whether to escape a starting <code>'^'</code>
	 *        character.
	 * @return The pattern.
	 */
	public static Pattern wildcardToPattern(String wildcard, boolean matchCase,
			boolean escapeStartChar) {

		int flags = RSyntaxUtilities.getPatternFlags(matchCase, 0);

		StringBuilder sb = new StringBuilder();
		for (int i=0; i<wildcard.length(); i++) {
			char ch = wildcard.charAt(i);
			switch (ch) {
				case '*':
					sb.append(".*");
					break;
				case '?':
					sb.append('.');
					break;
				case '^':
					if (i>0 || escapeStartChar) {
						sb.append('\\');
					}
					sb.append('^');
					break;
				case '\\':
				case '.': case '|':
				case '+': case '-':
				case '$':
				case '[': case ']':
				case '{': case '}':
				case '(': case ')':
					sb.append('\\').append(ch);
					break;
				default:
					sb.append(ch);
					break;
			}
		}

		Pattern p = null;
		try {
			p = Pattern.compile(sb.toString(), flags);
		} catch (PatternSyntaxException pse) {
			pse.printStackTrace();
			p = Pattern.compile(".+");
		}

		return p;

	}


}