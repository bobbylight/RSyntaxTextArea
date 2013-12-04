/*
 * 12/01/2014
 *
 * Copyright (C) 2013 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import org.fife.ui.rtextarea.SmartHighlightPainter;


/**
 * Marks occurrences of the current token for HTML.  Tags that require a
 * closing tag have their "opposite" tag closed.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class HtmlOccurrenceMarker implements OccurrenceMarker {

	private static final char[] CLOSE_TAG_START = { '<', '/' };
	private static final char[] TAG_SELF_CLOSE = { '/', '>' };

	private static final Set<String> TAGS_REQUIRING_CLOSING =
			getRequiredClosingTags();

	public static final Set<String> getRequiredClosingTags() {
		final String[] tags = {
			"html",
			"head",
			"title",
			"style",
			"script",
			"noscript",
			"body",
			"section",
			"nav",
			"article",
			"aside",
			"h1",
			"h2",
			"h3",
			"h4",
			"h5",
			"h6",
			"header",
			"footer",
			"address",
			"pre",
			"dialog",
			"blockquote",
			"ol",
			"ul",
			"dl",
			"a",
			"q",
			"cite",
			"em",
			"strong",
			"small",
			"mark",
			"dfn",
			"abbr",
			"time",
			"progress",
			"meter",
			"code",
			"var",
			"samp",
			"kbd",
			"sub",
			"sup",
			"span",
			"i",
			"b",
			"bdo",
			"ruby",
			"rt",
			"rp",
			"ins",
			"del",
			"figure",
			"iframe",
			"object",
			"video",
			"audio",
			"canvas",
			"map",
			"table",
			"caption",
			"form",
			"fieldset",
			"label",
			"button",
			"select",
			"datalist",
			"textarea",
			"output",
			"details",
			"bb",
			"menu",
			"legend",
			"div",
			// Obsolete elements
			"acronym",
			"applet",
			"big",
			"blink",
			"center",
			"dir",
			"font",
			"frame",
			"frameset",
			"isindex",
			"listing",
			"marquee",
			"nobr",
			"noembed",
			"noframes",
			"plaintext",
			"s",
			"spacer",
			"strike",
			"tt",
			"u",
			"xmp",
		};
		return new HashSet<String>(Arrays.asList(tags));
	}


	/**
	 * {@inheritDoc}
	 */
	public void markOccurrences(RSyntaxDocument doc, Token t,
			RSyntaxTextAreaHighlighter h, SmartHighlightPainter p) {

		String lexemeStr = t.getLexeme();
		char[] lexeme = lexemeStr.toCharArray();
		lexemeStr = lexemeStr.toLowerCase();
		int tokenOffs = t.getOffset();
		Element root = doc.getDefaultRootElement();
		int lineCount = root.getElementCount();
		int curLine = root.getElementIndex(t.getOffset());

		// For now, we only check for tags on the current line, for
		// simplicity.  Tags spanning multiple lines aren't common anyway.
		boolean found = false;
		boolean forward = true;
		t = doc.getTokenListForLine(curLine);
		while (t!=null && t.isPaintable()) {
			if (t.getType()==Token.MARKUP_TAG_DELIMITER) {
				if (t.isSingleChar('<') && t.getOffset()+1==tokenOffs) {
					// Don't try to match a tag that is optionally closed (or
					// closing is forbidden entirely).
					if (TAGS_REQUIRING_CLOSING.contains(lexemeStr)) {
						found = true;
					}
					break;
				}
				else if (t.is(CLOSE_TAG_START) && t.getOffset()+2==tokenOffs) {
					// Searching backward, we assume we can find the opening
					// tag.  Don't really care if it's valid or not.
					found = true;
					forward = false;
					break;
				}
			}
			t = t.getNextToken();
		}

		if (!found) {
			return;
		}

		if (forward) {

			t = t.getNextToken().getNextToken();

			do {

				while (t!=null && t.isPaintable()) {
					if (t.getType()==Token.MARKUP_TAG_DELIMITER) {
						if (t.is(CLOSE_TAG_START)) {
							Token match = t.getNextToken();
							if (match!=null && match.is(lexeme)) {
								try {
									int end = match.getOffset() + match.length();
									h.addMarkedOccurrenceHighlight(match.getOffset(), end, p);
									end = tokenOffs + match.length();
									h.addMarkedOccurrenceHighlight(tokenOffs, end, p);
								} catch (BadLocationException ble) {
									ble.printStackTrace(); // Never happens
								}
								return; // We're done!
							}
						}
					}
					t = t.getNextToken();
				}

				if (++curLine<lineCount) {
					t = doc.getTokenListForLine(curLine);
				}

			} while (curLine<lineCount);
				

		}

		else { // !forward

			Stack<Token> matches = new Stack<Token>();
			boolean inPossibleMatch = false;
			t = doc.getTokenListForLine(curLine);
			final int endBefore = tokenOffs - 2; // Stop before "</".

			do {

				while (t!=null && t.getOffset()<endBefore && t.isPaintable()) {
					if (t.getType()==Token.MARKUP_TAG_DELIMITER) {
						if (t.isSingleChar('<')) {
							Token next = t.getNextToken();
							if (next!=null) {
								if (next.is(lexeme)) {
									matches.push(next);
									inPossibleMatch = true;
								}
								else {
									inPossibleMatch = false;
								}
								t = next;
							}
						}
						else if (t.isSingleChar('>')) {
							inPossibleMatch = false;
						}
						else if (inPossibleMatch && t.is(TAG_SELF_CLOSE)) {
							matches.pop();
						}
						else if (t.is(CLOSE_TAG_START)) {
							Token next = t.getNextToken();
							if (next!=null) {
								// Invalid XML might not have a match
								if (next.is(lexeme) && !matches.isEmpty()) {
									matches.pop();
								}
								t = next;
							}
						}
					}
					t = t.getNextToken();
				}

				if (!matches.isEmpty()) {
					try {
						Token match = matches.pop();
						int end = match.getOffset() + match.length();
						h.addMarkedOccurrenceHighlight(match.getOffset(), end, p);
						end = tokenOffs + match.length();
						h.addMarkedOccurrenceHighlight(tokenOffs, end, p);
					} catch (BadLocationException ble) {
						ble.printStackTrace(); // Never happens
					}
					return;
				}

				if (--curLine>=0) {
					t = doc.getTokenListForLine(curLine);
				}

			} while (curLine>=0);
				

		}

	}


}