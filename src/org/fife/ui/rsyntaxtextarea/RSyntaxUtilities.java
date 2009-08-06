/*
 * 08/06/2004
 *
 * RSyntaxUtilities.java - Utility methods used by RSyntaxTextArea and its
 * views.
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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.View;


/**
 * Utility methods used by <code>RSyntaxTextArea</code> and its associated
 * classes.
 *
 * @author Robert Futrell
 * @version 0.2
 */
public class RSyntaxUtilities implements SwingConstants {

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
	private static final int[] dataTable = {
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
	 * Returns the leading whitespace of a string.
	 *
	 * @param text The String to check.
	 * @return The leading whitespace.
	 */
	public static String getLeadingWhitespace(String text) {
		int count = 0;
		int len = text.length();
		while (count<len && RSyntaxUtilities.isWhitespace(text.charAt(count))) {
			count++;
		}
		return text.substring(0, count);
	}


	private static final Element getLineElem(Document d, int offs) {
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
		if (p0<0)
			throw new BadLocationException("Invalid document position", p0);
		else if (p1>doc.getLength())
			throw new BadLocationException("Invalid document position", p1);

		// Ensure p0 and p1 are in the same line, and get the start/end
		// offsets for that line.
		Element map = doc.getDefaultRootElement();
		int lineNum = map.getElementIndex(p0);
		// We do ">1" because p1 might be the first position on the next line
		// or the last position on the previous one.
		// if (lineNum!=map.getElementIndex(p1))
		if (Math.abs(lineNum-map.getElementIndex(p1))>1)
			throw new IllegalArgumentException("p0 and p1 are not on the " +
						"same line (" + p0 + ", " + p1 + ").");

		// Get the token list.
		Token t = doc.getTokenListForLine(lineNum);

		// Modify the token list 't' to begin at p0 (but still have correct
		// token types, etc.), and get the x-location (in pixels) of the
		// beginning of this new token list.
		makeTokenListStartAt(t, p0, e, textArea, 0);

		rect = t.listOffsetToView(textArea, e, p1, x0, rect);
		return rect;

	}


	/**
	 * Returns the location of the bracket paired with the one at the current
	 * caret position.
	 *
	 * @param textArea The text area.
	 * @return The location of the matching bracket in the document, or
	 *         <code>-1</code> if there isn't a matching bracket (or the caret
	 *         isn't on a bracket).
	 */
	private static Segment charSegment = new Segment();
	public static int getMatchingBracketPosition(RSyntaxTextArea textArea) {

		try {

			// Actually position just BEFORE caret.
			int caretPosition = textArea.getCaretPosition() - 1;
			if (caretPosition>-1) {

				// Some variables that will be used later.
				Token token;
				Element map;
				int curLine;
				Element line;
				int start, end;
				RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
				char bracket  = doc.charAt(caretPosition);

				// First, see if the previous char was a bracket
				// ('{', '}', '(', ')', '[', ']').
				// If it was, then make sure this bracket isn't sitting in
				// the middle of a comment or string.  If it isn't, then
				// initialize some stuff so we can continue on.
				char bracketMatch;
				boolean goForward;
				switch (bracket) {

					case '{':
					case '(':
					case '[':

						// Ensure this bracket isn't in a comment.
						map = doc.getDefaultRootElement();
						curLine = map.getElementIndex(caretPosition);
						line = map.getElement(curLine);
						start = line.getStartOffset();
						end = line.getEndOffset();
						token = doc.getTokenListForLine(curLine);
						token = RSyntaxUtilities.getTokenAtOffset(token, caretPosition);
						// All brackets are always returned as "separators."
						if (token.type!=Token.SEPARATOR) {
							return -1;
						}
						bracketMatch = bracket=='{' ? '}' : (bracket=='(' ? ')' : ']');
						goForward = true;
						break;

					case '}':
					case ')':
					case ']':

						// Ensure this bracket isn't in a comment.
						map = doc.getDefaultRootElement();
						curLine = map.getElementIndex(caretPosition);
						line = map.getElement(curLine);
						start = line.getStartOffset();
						end = line.getEndOffset();
						token = doc.getTokenListForLine(curLine);
						token = RSyntaxUtilities.getTokenAtOffset(token, caretPosition);
						// All brackets are always returned as "separators."
						if (token.type!=Token.SEPARATOR) {
							return -1;
						}
						bracketMatch = bracket=='}' ? '{' : (bracket==')' ? '(' : '[');
						goForward = false;
						break;

					default:
						return -1;

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
								if (haveTokenList==false) {
									token = doc.getTokenListForLine(curLine);
									haveTokenList = true;
								}
								int offset = start + (i-segOffset);
								token = RSyntaxUtilities.getTokenAtOffset(token, offset);
								if (token.type==Token.SEPARATOR)
									numEmbedded++;
							}

							else if (ch==bracketMatch) {
								if (haveTokenList==false) {
									token = doc.getTokenListForLine(curLine);
									haveTokenList = true;
								}
								int offset = start + (i-segOffset);
								token = RSyntaxUtilities.getTokenAtOffset(token, offset);
								if (token.type==Token.SEPARATOR) {
									if (numEmbedded==0)
										return offset;
									numEmbedded--;
								}
							}

						} // End of for (int i=segOffset; i<segOffset+charSegment.count; i++).

						// Bail out if we've gone through all lines and
						// haven't found the match.
						if (++curLine==lastLine)
							return -1;

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
								if (haveTokenList==false) {
									token = doc.getTokenListForLine(curLine);
									haveTokenList = true;
								}
								int offset = start + (i-segOffset);
								t2 = RSyntaxUtilities.getTokenAtOffset(token, offset);
								if (t2.type==Token.SEPARATOR)
									numEmbedded++;
							}

							else if (ch==bracketMatch) {
								if (haveTokenList==false) {
									token = doc.getTokenListForLine(curLine);
									haveTokenList = true;
								}
								int offset = start + (i-segOffset);
								t2 = RSyntaxUtilities.getTokenAtOffset(token, offset);
								if (t2.type==Token.SEPARATOR) {
									if (numEmbedded==0)
										return offset;
									numEmbedded--;
								}
							}

						} // End of for (int i=segOffset; i<segOffset+charSegment.count; i++).

						// Bail out if we've gone through all lines and
						// haven't found the match.
						if (--curLine==-1)
							return -1;

						// Otherwise, get ready for going through the
						// next line.
						haveTokenList = false;
						line = map.getElement(curLine);
						start = line.getStartOffset();
						end = line.getEndOffset();

					} // End of while (true).

				} // End of else.

			} // End of if (caretPosition>-1).

		} catch (BadLocationException ble) {
			// Shouldn't ever happen.
			ble.printStackTrace();
		}

		// Something went wrong...
		return -1;

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
	 * @param pos the position to convert >= 0
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
				RSyntaxTextArea target = (RSyntaxTextArea)view.
													getContainer();
				Caret c = (target != null) ? target.getCaret() : null;
				// YECK! Ideally, the x location from the magic caret
				// position would be passed in.
				Point mcp;
				if (c != null)
					mcp = c.getMagicCaretPosition();
				else
					mcp = null;
				int x;
				if (mcp == null) {
					Rectangle loc = target.modelToView(pos);
					x = (loc == null) ? 0 : loc.x;
				}
				else {
					x = mcp.x;
				}
				if (direction == NORTH)
					pos = getPositionAbove(target,pos,x,(TabExpander)view);
				else
					pos = getPositionBelow(target,pos,x,(TabExpander)view);
				break;

			case WEST:
				if(pos == -1)
					pos = Math.max(0, view.getEndOffset() - 1);
				else
					pos = Math.max(0, pos - 1);
				break;

			case EAST:
				if(pos == -1)
					pos = view.getStartOffset();
				else
					pos = Math.min(pos + 1, view.getDocument().
													getLength());
				break;

			default:
				throw new IllegalArgumentException(
									"Bad direction: " + direction);
		}

		return pos;

	}


	/**
	 * Determines the position in the model that is closest to the given 
	 * view location in the row above.  The component given must have a
	 * size to compute the result.  If the component doesn't have a size
	 * a value of -1 will be returned.
	 *
	 * @param c the editor
	 * @param offs the offset in the document >= 0
	 * @param x the X coordinate >= 0
	 * @return the position >= 0 if the request can be computed, otherwise
	 *  a value of -1 will be returned.
	 * @exception BadLocationException if the offset is out of range
	 */
	public static final int getPositionAbove(RSyntaxTextArea c, int offs,
					float x, TabExpander e) throws BadLocationException {

		TokenOrientedView tov = (TokenOrientedView)e;
		Token token = tov.getTokenListForPhysicalLineAbove(offs);
		if (token==null)
			return -1;

		// A line containing only Token.NULL is an empty line.
		else if (token.type==Token.NULL) {
			int line = c.getLineOfOffset(offs);	// Sure to be >0 ??
			return c.getLineStartOffset(line-1);
		}

		else {
			return token.getListOffset(c, e, 0, x);
		}

	}


	/**
	 * Determines the position in the model that is closest to the given 
	 * view location in the row below.  The component given must have a
	 * size to compute the result.  If the component doesn't have a size
	 * a value of -1 will be returned.
	 *
	 * @param c the editor
	 * @param offs the offset in the document >= 0
	 * @param x the X coordinate >= 0
	 * @return the position >= 0 if the request can be computed, otherwise
	 *  a value of -1 will be returned.
	 * @exception BadLocationException if the offset is out of range
	 */
	public static final int getPositionBelow(RSyntaxTextArea c, int offs,
					float x, TabExpander e) throws BadLocationException {

		TokenOrientedView tov = (TokenOrientedView)e;
		Token token = tov.getTokenListForPhysicalLineBelow(offs);
		if (token==null)
			return -1;

		// A line containing only Token.NULL is an empty line.
		else if (token.type==Token.NULL) {
			int line = c.getLineOfOffset(offs);	// Sure to be > c.getLineCount()-1 ??
			return c.getLineStartOffset(line+1);
		}

		else {
			return token.getListOffset(c, e, 0, x);
		}

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
	 */
	public static final Token getTokenAtOffset(Token tokenList, int offset) {
		for (Token t=tokenList; t!=null; t=t.getNextToken()) {
			if (t.containsPosition(offset))
				return t;
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
	public static final float getTokenListWidth(Token tokenList,
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
	public static final float getTokenListWidth(final Token tokenList,
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
	public static final float getTokenListWidthUpTo(final Token tokenList,
								RSyntaxTextArea textArea, TabExpander e,
								float x0, int upTo) {
		float width = 0;
		for (Token t=tokenList; t!=null&&t.isPaintable(); t=t.getNextToken()) {
			if (t.containsPosition(upTo)) {
				return width + t.getWidthUpTo(upTo-t.offset, textArea, e,
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
	public static final boolean isBracket(char ch) {
		// We need the first condition as it might be that ch>255, and thus
		// not in our table.  '}' is the highest-valued char in the bracket
		// set.
		return ch<='}' && (dataTable[ch]&BRACKET_MASK)>0;
	}


	/**
	 * Returns whether or not a character is a digit (0-9).
	 *
	 * @param ch The character to check.
	 * @return Whether or not the character is a digit.
	 */
	public static final boolean isDigit(char ch) {
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
	public static final boolean isHexCharacter(char ch) {
		// We need the first condition as it could be that ch>255 (and thus
		// not a valid index into our table).  'f' is the highest-valued
		// char that is a valid hex character.
		return (ch<='f') && (dataTable[ch]&HEX_CHARACTER_MASK)>0;
	}


	/**
	 * Returns whether a character is a Java operator.  Note that C and C++
	 * operators are the same as Java operators.
	 *
	 * @param ch The character to check.
	 * @return Whether or not the character is a Java operator.
	 */
	public static final boolean isJavaOperator(char ch) {
		// We need the first condition as it could be that ch>255 (and thus
		// not a valid index into our table).  '~' is the highest-valued
		// char that is a valid Java operator.
		return (ch<='~') && (dataTable[ch]&JAVA_OPERATOR_MASK)>0;
	}


	/**
	 * Returns whether a character is a US-ASCII letter (A-Z or a-z).
	 *
	 * @param ch The character to check.
	 * @return Whether or not the character is a US-ASCII letter.
	 */
	public static final boolean isLetter(char ch) {
		// We need the first condition as it could be that ch>255 (and thus
		// not a valid index into our table).
		return (ch<='z') && (dataTable[ch]&LETTER_MASK)>0;
	}


	/**
	 * Returns whether or not a character is a US-ASCII letter or a digit.
	 *
	 * @param ch The character to check.
	 * @return Whether or not the character is a US-ASCII letter or a digit.
	 */
	public static final boolean isLetterOrDigit(char ch) {
		// We need the first condition as it could be that ch>255 (and thus
		// not a valid index into our table).
		return (ch<='z') && (dataTable[ch]&LETTER_OR_DIGIT_MASK)>0;
	}


	/**
	 * Returns whether or not a character is a whitespace character (either
	 * a space ' ' or tab '\t').  This checks for the Unicode character values
	 * 0x0020 and 0x0009.
	 *
	 * @param ch The character to check.
	 * @return Whether or not the character is a whitespace character.
	 */
	public static final boolean isWhitespace(char ch) {
		// We do it this way as we'd need to do two conditions anyway (first
		// to check that ch<255 so it can index into our table, then whether
		// that table position has the whitespace mask).
		return ch==' ' || ch=='\t';
	}


	/**
	 * Modifies the passed-in token list to start at the specified offset.
	 * For example, if the token list covered positions 20-60 in the document
	 * (inclusive) like so:
	 * <pre>
	 *   [token1] -> [token2] -> [token3] -> [token4]
	 *   20     30   31     40   41     50   51     60
	 * </pre>
	 * and you used this method to make the token list start at position 44,
	 * then the token list would be modified to be the following:
	 * <pre>
	 *   [part-of-old-token3] -> [token4]
	 *   44                 50   51     60
	 * </pre>
	 * Tokens that come before the specified position are forever lost, and
	 * the token containing that position is made to begin at that position if
	 * necessary.  All token types remain the same as they were originally.<p>
	 *
	 * This method can be useful if you are only interested in part of a token
	 * list (i.e., the line it represents), but you don't want to modify the
	 * token list yourself.
	 *
	 * @param tokenList The list to make start at the specified position.
	 *        This parameter is modified.
	 * @param pos The position at which the new token list is to start.  If
	 *        this position is not in the passed-in token list,
	 *        returned token list will either be <code>null</code> or the
	 *        unpaintable token(s) at the end of the passed-in token list.
	 * @param e How to expand tabs.
	 * @param textArea The text area from which the token list came.
	 * @param x0 The initial x-pixel position of the old token list.
	 * @return The width, in pixels, of the part of the token list "removed
	 *         from the front."  This way, you know the x-offset of the "new"
	 *         token list.
	 */
	public static float makeTokenListStartAt(Token tokenList, int pos,
									TabExpander e,
									final RSyntaxTextArea textArea,
									float x0) {

		Token t = tokenList;

		// Loop through the token list until you find the one that contains
		// pos.  Remember the cumulative width of all of these tokens.
		while (t!=null && t.isPaintable() && !t.containsPosition(pos)) {
			x0 += t.getWidth(textArea, e, x0);
			t = t.getNextToken();
		}

		// Make the token that contains pos start at pos.
		if (t!=null && t.isPaintable() && t.offset!=pos) {
			// Number of chars between p0 and token start.
			int difference = pos - t.offset;
			x0 += t.getWidthUpTo(t.textCount-difference+1, textArea, e, x0);
			t.makeStartAt(pos);
		}

		// Make the passed-in token list point to the proper place.
		// t can be null, for example, if line ends with unended MLC.
		if (t!=null && t.isPaintable())
			tokenList.copyFrom(t);
		else
			tokenList = null;
		t = null;

		// Return the x-offset (in pixels) of the newly-modified t.
		return x0;

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
	public static final char toLowerCase(char ch) {
		// We can logical OR with 32 because A-Z are 65-90 in the ASCII table
		// and none of them have the 6th bit (32) set, and a-z are 97-122 in
		// the ASCII table, which is 32 over from A-Z.
		// We do it this way as we'd need to do two conditions anyway (first
		// to check that ch<255 so it can index into our table, then whether
		// that table position has the upper-case mask).
		if (ch>='A' && ch<='Z')
			return (char)(ch | 0x20);
		return ch;
	}


}