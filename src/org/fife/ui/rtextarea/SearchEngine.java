/*
 * 02/19/2006
 *
 * SearchEngine.java - Handles find/replace operations in an RTextArea.
 * Copyright (C) 2006 Robert Futrell
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
package org.fife.ui.rtextarea;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;


/**
 * A singleton class that can perform advanced find/replace operations
 * in an {@link RTextArea}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class SearchEngine {


	/**
	 * Private constructor to prevent instantiation.
	 */
	private SearchEngine() {
	}


	/**
	 * Finds the next instance of the string/regular expression specified
	 * from the caret position.  If a match is found, it is selected in this
	 * text area.
	 *
	 * @param textArea The text area in which to search.
	 * @param text The string literal or regular expression to search for.
	 * @param forward Whether to search forward from the caret position or
	 *        backward from it.
	 * @param matchCase Whether the search should be case-sensitive.
	 * @param wholeWord Whether there should be spaces or tabs on either side
	 *        of the match.
	 * @param regex Whether <code>text</code> is a Java regular expression to
	 *        search for.
	 * @return Whether a match was found (and thus selected).
	 * @throws PatternSyntaxException If <code>regex</code> is
	 *         <code>true</code> but <code>text</code> is not a valid regular
	 *         expression.
	 * @see #replace
	 * @see #regexReplace
	 */
	public static boolean find(JTextArea textArea, String text,
							boolean forward, boolean matchCase,
							boolean wholeWord, boolean regex)
									throws PatternSyntaxException {

		// Be smart about what position we're "starting" at.  We don't want
		// to find a match in the currently selected text (if any), so we
		// start searching AFTER the selection if searching forward, and
		// BEFORE the selection if searching backward.
		Caret c = textArea.getCaret();
		int start = forward ? Math.max(c.getDot(), c.getMark()) :
						Math.min(c.getDot(), c.getMark());

		String findIn = getFindInText(textArea, start, forward);
		if (findIn==null || findIn.length()==0) return false;

		// Find the next location of the text we're searching for.
		if (regex==false) {
			int pos = getNextMatchPos(text, findIn, forward,
											matchCase, wholeWord);
			findIn = null; // May help garbage collecting.
			if (pos!=-1) {
				// Without this, if JTextArea isn't in focus, selection
				// won't appear selected.
				c.setSelectionVisible(true);
				pos = forward ? start+pos : pos;
				selectAndPossiblyCenter(textArea, pos, pos+text.length());
				return true;
			}
		}
		else {
			// Regex matches can have varying widths.  The returned point's
			// x- and y-values represent the start and end indices of the
			// match in findIn.
			Point regExPos = getNextMatchPosRegEx(text, findIn,
									forward, matchCase, wholeWord);
			findIn = null; // May help garbage collecting.
			if (regExPos!=null) {
				// Without this, if JTextArea isn't in focus, selection
				// won't appear selected.
				c.setSelectionVisible(true);
				if (forward) {
					regExPos.translate(start, start);
				}
				selectAndPossiblyCenter(textArea, regExPos.x, regExPos.y);
				return true;
			}
		}

		// No match.
		return false;

	}


	/**
	 * Returns the text in which to search, as a string.  This is used
	 * internally to grab the smallest buffer possible in which to search.
	 */
	protected static String getFindInText(JTextArea textArea, int start,
									boolean forward) {

		// Be smart about the text we grab to search in.  We grab more than
		// a single line because our searches can return multiline results.
		// We copy only the chars that will be searched through.
		String findIn = null;
		if (forward) {
			try {
				findIn = textArea.getText(start,
							textArea.getDocument().getLength()-start);
			} catch (BadLocationException ble) {
				// Never happens; findIn will be null anyway.
				ble.printStackTrace();
			}
		}
		else { // backward
			try {
				findIn = textArea.getText(0, start);
			} catch (BadLocationException ble) {
				// Never happens; findIn will be null anyway.
				ble.printStackTrace();
			}
		}

		return findIn;

	}


	/**
	 * This method is called internally by
	 * <code>getNextMatchPosRegExImpl</code> and is used to get the locations
	 * of all regular-expression matches, and possibly their replacement
	 * strings.<p>
	 *
	 * Returns either:
	 * <ul>
	 *   <li>A list of points representing the starting and ending positions
	 *       of all matches returned by the specified matcher, or
	 *   <li>A list of <code>RegExReplaceInfo</code>s describing the matches
	 *       found by the matcher and the replacement strings for each.
	 * </ul>
	 *
	 * If <code>replacement</code> is <code>null</code>, this method call is
	 * assumed to be part of a "find" operation and points are returned.  If
	 * if is non-<code>null</code>, it is assumed to be part of a "replace"
	 * operation and the <code>RegExReplaceInfo</code>s are returned.<p>
	 *
	 * @param m The matcher.
	 * @param replaceStr The string to replace matches with.  This is a
	 *        "template" string and can contain captured group references in
	 *        the form "<code>${digit}</code>".
	 * @return A list of result objects.
	 * @throws IndexOutOfBoundsException If <code>replaceStr</code> references
	 *         an invalid group (less than zero or greater than the number of
	 *         groups matched).
	 */
	protected static List getMatches(Matcher m, String replaceStr) {
		ArrayList matches = new ArrayList();
		while (m.find()) {
			Point loc = new Point(m.start(), m.end());
			if (replaceStr==null) { // Find, not replace.
				matches.add(loc);
			}
			else { // Replace.
				matches.add(new RegExReplaceInfo(m.group(0), loc.x, loc.y,
								getReplacementText(m, replaceStr)));
			}
		}
		return matches;
	}


	/**
	 * Searches <code>searchIn</code> for an occurrence of
	 * <code>searchFor</code> either forwards or backwards, matching
	 * case or not.
	 *
	 * @param searchFor The string to look for.
	 * @param searchIn The string to search in.
	 * @param forward Whether to search forward or backward in
	 *        <code>searchIn</code>.
	 * @param matchCase If <code>true</code>, do a case-sensitive search for
	 *        <code>searchFor</code>.
	 * @param wholeWord If <code>true</code>, <code>searchFor</code>
	 *        occurrences embedded in longer words in <code>searchIn</code>
	 *        don't count as matches.
	 * @return The starting position of a match, or <code>-1</code> if no
	 *         match was found.
	 * @see #getNextMatchPosImpl
	 * @see #getNextMatchPosRegEx
	 */
	public static final int getNextMatchPos(String searchFor, String searchIn,
								boolean forward, boolean matchCase,
								boolean wholeWord) {

		// Make our variables lower case if we're ignoring case.
		if (!matchCase) {
			return getNextMatchPosImpl(searchFor.toLowerCase(),
								searchIn.toLowerCase(), forward,
								matchCase, wholeWord);
		}

		return getNextMatchPosImpl(searchFor, searchIn, forward,
								matchCase, wholeWord);

	}


	/**
	 * Actually does the work of matching; assumes searchFor and searchIn
	 * are already upper/lower-cased appropriately.<br>
	 * The reason this method is here is to attempt to speed up
	 * <code>FindInFilesDialog</code>; since it repeatedly calls
	 * this method instead of <code>getNextMatchPos</code>, it gets better
	 * performance as it no longer has to allocate a lower-cased string for
	 * every call.
	 *
	 * @param searchFor The string to search for.
	 * @param searchIn The string to search in.
	 * @param goForward Whether the search is forward or backward.
	 * @param matchCase Whether the search is case-sensitive.
	 * @param wholeWord Whether only whole words should be matched.
	 * @return The location of the next match, or <code>-1</code> if no
	 *         match was found.
	 */
	protected static final int getNextMatchPosImpl(String searchFor,
								String searchIn, boolean goForward,
								boolean matchCase, boolean wholeWord) {

		if (wholeWord) {
			int len = searchFor.length();
			int temp = goForward ? 0 : searchIn.length();
			int tempChange = goForward ? 1 : -1;
			while (true) {
				if (goForward)
					temp = searchIn.indexOf(searchFor, temp);
				else
					temp = searchIn.lastIndexOf(searchFor, temp);
				if (temp!=-1) {
					if (isWholeWord(searchIn, temp, len)) {
						return temp;
					}
					else {
						temp += tempChange;
						continue;
					}
				}
				return temp; // Always -1.
			}
		}
		else {
			return goForward ? searchIn.indexOf(searchFor) :
							searchIn.lastIndexOf(searchFor);
		}

	}


	/**
	 * Searches <code>searchIn</code> for an occurrence of <code>regEx</code>
	 * either forwards or backwards, matching case or not.
	 *
	 * @param regEx The regular expression to look for.
	 * @param searchIn The string to search in.
	 * @param goForward Whether to search forward.  If <code>false</code>,
	 *        search backward.
	 * @param matchCase Whether or not to do a case-sensitive search for
	 *        <code>regEx</code>.
	 * @param wholeWord If <code>true</code>, <code>regEx</code>
	 *        occurrences embedded in longer words in <code>searchIn</code>
	 *        don't count as matches.
	 * @return A <code>Point</code> representing the starting and ending
	 *         position of the match, or <code>null</code> if no match was
	 *         found.
	 * @throws PatternSyntaxException If <code>regEx</code> is an invalid
	 *         regular expression.
	 * @see #getNextMatchPos
	 */
	public static Point getNextMatchPosRegEx(String regEx,
							CharSequence searchIn, boolean goForward,
							boolean matchCase, boolean wholeWord) {
		return (Point)getNextMatchPosRegExImpl(regEx, searchIn, goForward,
									matchCase, wholeWord, null);
	}


	/**
	 * Searches <code>searchIn</code> for an occurrence of <code>regEx</code>
	 * either forwards or backwards, matching case or not.
	 *
	 * @param regEx The regular expression to look for.
	 * @param searchIn The string to search in.
	 * @param goForward Whether to search forward.  If <code>false</code>,
	 *        search backward.
	 * @param matchCase Whether or not to do a case-sensitive search for
	 *        <code>regEx</code>.
	 * @param wholeWord If <code>true</code>, <code>regEx</code>
	 *        occurrences embedded in longer words in <code>searchIn</code>
	 *        don't count as matches.
	 * @param replaceStr The string that will replace the match found (if
	 *        a match is found).  The object returned will contain the
	 *        replacement string with matched groups substituted.  If this
	 *        value is <code>null</code>, it is assumed this call is part of a
	 *        "find" instead of a "replace" operation.
	 * @return If <code>replaceStr</code> is <code>null</code>, a
	 *         <code>Point</code> representing the starting and ending points
	 *         of the match.  If it is non-<code>null</code>, an object with
	 *         information about the match and the morphed string to replace
	 *         it with.  If no match is found, <code>null</code> is returned.
	 * @throws PatternSyntaxException If <code>regEx</code> is an invalid
	 *         regular expression.
	 * @throws IndexOutOfBoundsException If <code>replaceStr</code> references
	 *         an invalid group (less than zero or greater than the number of
	 *         groups matched).
	 * @see #getNextMatchPos
	 */
	protected static Object getNextMatchPosRegExImpl(String regEx,
							CharSequence searchIn, boolean goForward,
							boolean matchCase, boolean wholeWord,
							String replaceStr) {

		// Make a pattern that takes into account whether or not to match case.
		int flags = Pattern.MULTILINE; // '^' and '$' are done per line.
		flags |= matchCase ? 0 : (Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE);
		Pattern pattern = Pattern.compile(regEx, flags);

		// Make a Matcher to find the regEx instances.
		Matcher m = pattern.matcher(searchIn);

		/*
		 * Our algorithm is broken into four cases:
		 *   1. Forward search, not whole-word: Just take first match found.
		 *   2. Forward search, whole-word: Loop until the first whole-word
		 *      match is found.
		 *   3. Backward search, not whole-word.  Find all matches first
		 *      (must do this since we can't search for regexes backwards),
		 *      and return last match found.
		 *   4. Backward search, whole-word.  Find all matches first, then
		 *      loop through them backwards until the first (i.e., the last!)
		 *      whole-word match is found.
		 */

		// If this is a forward-direction search...
		if (goForward) {

			// 1. Forward search, not whole word => easy.  Just return
			// the first match found.
			if (!wholeWord) {
				if (m.find()) {
					if (replaceStr==null) { // Find, not replace.
						return new Point(m.start(), m.end());
					}
					else { // Replace.
						return new RegExReplaceInfo(m.group(0),
								m.start(), m.end(),
								getReplacementText(m, replaceStr));
					}
				}
			}

			// 2. Forward search, whole word => just okay.  Find and look at
			// matches one at a time until you find one that's "whole word."
			else {
				while (m.find()) {
					Point loc = new Point(m.start(), m.end());
					if (isWholeWord(searchIn, loc.x,loc.y-loc.x)) {
						if (replaceStr==null) { // Find, not replace.
							return loc;
						}
						else { // Replace.
							return new RegExReplaceInfo(m.group(0),
								loc.x, loc.y,
								getReplacementText(m, replaceStr));
						}
					}
				}
			}

		} // End of if (goForward).

		// If this is a backward-direction search...
		else {

			// Get some variables ready.
			List matches = getMatches(m, replaceStr);
			if (matches.isEmpty())
				return null;
			int pos = matches.size() - 1;

			// 3. If they're not looking for a "whole word" just return
			// the first (i.e., last) match.
			if (wholeWord==false) {
				if (replaceStr==null) { // Find, not replace.
					return /*(Point)*/matches.get(pos);
				}
				else { // Replace.
					return /*(RegExReplaceInfo)*/matches.get(pos);
				}
			}

			// 4. Otherwise, go through the matches last-to-first.
			while (pos>=0) {
				Object matchObj = matches.get(pos);
				if (replaceStr==null) { // Find, not replace.
					Point loc = (Point)matchObj;
					if (isWholeWord(searchIn, loc.x,loc.y-loc.x)) {
						return matchObj;
					}
				}
				else { // Replace.
					RegExReplaceInfo info = (RegExReplaceInfo)matchObj;
					int x = info.getStartIndex();
					int y = info.getEndIndex();
					if (isWholeWord(searchIn, x,y-x)) {
						return matchObj;
					}
				}
				pos--;
			}

		}

		// If we didn't find a match after all that, return null.
		return null;

	}


	/**
	 * Returns information on how to implement a regular expression "replace"
	 * action in the specified text with the specified replacement string.
	 *
	 * @param regEx The regular expression to look for.
	 * @param searchIn The string to search in.
	 * @param goForward Whether to search forward.  If <code>false</code>,
	 *        search backward.
	 * @param matchCase Whether or not to do a case-sensitive search for
	 *        <code>regEx</code>.
	 * @param wholeWord If <code>true</code>, <code>regEx</code> occurrences
	 *        embedded in longer words in <code>searchIn</code> don't count as
	 *        matches.
	 * @param replacement A template for the replacement string (e.g., this
	 *        can contain <code>\t</code> and <code>\n</code> to mean tabs
	 *        and newlines, respectively, as well as group references
	 *        <code>$n</code>).
	 * @return A <code>RegExReplaceInfo</code> object describing how to
	 *         implement the replace.
	 * @throws PatternSyntaxException If <code>regEx</code> is an invalid
	 *         regular expression.
	 * @throws IndexOutOfBoundsException If <code>replacement</code> references
	 *         an invalid group (less than zero or greater than the number of
	 *         groups matched).
	 * @see #getNextMatchPos
	 */
	protected static RegExReplaceInfo getRegExReplaceInfo(String regEx,
									String searchIn, boolean goForward,
									boolean matchCase, boolean wholeWord,
									String replacement) {
		// Can't pass null to getNextMatchPosRegExImpl or it'll think
		// you're doing a "find" operation instead of "replace, and return a
		// Point.
		if (replacement==null) {
			replacement = "";
		}
		return (RegExReplaceInfo)getNextMatchPosRegExImpl(regEx, searchIn,
						goForward, matchCase, wholeWord, replacement);
	}


	/**
	 * Called internally by <code>getMatches()</code>.  This method assumes
	 * that the specified matcher has just found a match, and that you want
	 * to get the string with which to replace that match.
	 *
	 * @param m The matcher.
	 * @param template The template for the replacement string.  For example,
	 *        "<code>foo</code>" would yield the replacement string
	 *        "<code>foo</code>", while "<code>$1 is the greatest</code>"
	 *        would yield different values depending on the value of the first
	 *        captured group in the match.
	 * @return The string to replace the match with.
	 * @throws IndexOutOfBoundsException If <code>template</code> references
	 *         an invalid group (less than zero or greater than the number of
	 *         groups matched).
	 */
	public static String getReplacementText(Matcher m, CharSequence template) {

		// NOTE: This code was mostly ripped off from J2SE's Matcher
		// class.

		// Process substitution string to replace group references with groups
		int cursor = 0;
		StringBuffer result = new StringBuffer();

		while (cursor < template.length()) {
	
			char nextChar = template.charAt(cursor);
	
			if (nextChar == '\\') { // Escape character.
				nextChar = template.charAt(++cursor);
				switch (nextChar) { // Special cases.
					case 'n':
						nextChar = '\n';
						break;
					case 't':
						nextChar = '\t';
						break;
				}
				result.append(nextChar);
				cursor++;
			}
			else if (nextChar == '$') { // Group reference.

				cursor++; // Skip the '$'.

				// The first number is always a group
				int refNum = template.charAt(cursor) - '0';
				if ((refNum < 0)||(refNum > 9)) {
					// This should really be an IllegalArgumentException,
					// but we cheat to keep all "group" errors throwing
					// the same exception type.
					throw new IndexOutOfBoundsException(
								"No group " + template.charAt(cursor));
				}
				cursor++;

				// Capture the largest legal group string
				boolean done = false;
				while (!done) {
					if (cursor >= template.length()) {
						break;
					}
					int nextDigit = template.charAt(cursor) - '0';
					if ((nextDigit < 0)||(nextDigit > 9)) { // not a number
						break;
					}
					int newRefNum = (refNum * 10) + nextDigit;
					if (m.groupCount() < newRefNum) {
						done = true;
					}
					else {
						refNum = newRefNum;
						cursor++;
					}
				}

				// Append group
				if (m.group(refNum) != null)
					result.append(m.group(refNum));

			}

			else {
				result.append(nextChar);
				cursor++;
			}

		}

		return result.toString();

	}


	/**
	 * Returns whether the characters on either side of
	 * <code>substr(searchIn,startPos,startPos+searchStringLength)</code>
	 * are whitespace.  While this isn't the best definition of "whole word",
	 * it's the one we're going to use for now.
	 */
	private static final boolean isWholeWord(CharSequence searchIn,
											int offset, int len) {

		boolean wsBefore, wsAfter;

		try {
			wsBefore = !Character.isLetterOrDigit(searchIn.charAt(offset - 1));
		} catch (IndexOutOfBoundsException e) { wsBefore = true; }
		try {
			wsAfter  = !Character.isLetterOrDigit(searchIn.charAt(offset + len));
		} catch (IndexOutOfBoundsException e) { wsAfter = true; }

		return wsBefore && wsAfter;

	}


	/**
	 * Makes the caret's dot and mark the same location so that, for the
	 * next search in the specified direction, a match will be found even
	 * if it was within the original dot and mark's selection.
	 *
	 * @param textArea The text area.
	 * @param forward Whether the search will be forward through the
	 *        document (<code>false</code> means backward).
	 * @return The new dot and mark position.
	 */
	protected static int makeMarkAndDotEqual(JTextArea textArea,
										boolean forward) {
		Caret c = textArea.getCaret();
		int val = forward ? Math.min(c.getDot(), c.getMark()) :
						Math.max(c.getDot(), c.getMark());
		c.setDot(val);
		return val;
	}


	/**
	 * Finds the next instance of the regular expression specified from
	 * the caret position.  If a match is found, it is replaced with
	 * the specified replacement string.
	 *
	 * @param textArea The text area in which to search.
	 * @param toFind The regular expression to search for.
	 * @param replaceWith The string to replace the found regex with.
	 * @param forward Whether to search forward from the caret position
	 *        or backward from it.
	 * @param matchCase Whether the search should be case-sensitive.
	 * @param wholeWord Whether there should be spaces or tabs on either
	 *        side of the match.
	 * @return Whether a match was found (and thus replaced).
	 * @throws PatternSyntaxException If <code>toFind</code> is not a
	 *         valid regular expression.
	 * @throws IndexOutOfBoundsException If <code>replaceWith</code> references
	 *         an invalid group (less than zero or greater than the number of
	 *         groups matched).
	 * @see #replace
	 * @see #find
	 */
	protected static boolean regexReplace(JTextArea textArea, String toFind,
				String replaceWith, boolean forward, boolean matchCase,
				boolean wholeWord) throws PatternSyntaxException {

		// Be smart about what position we're "starting" at.  For example,
		// if they are searching backwards and there is a selection such that
		// the dot is past the mark, and the selection is the text for which
		// you're searching, this search will find and return the current
		// selection.  So, in that case we start at the beginning of the
		// selection.
		Caret c = textArea.getCaret();
		int start = makeMarkAndDotEqual(textArea, forward);

		String findIn = getFindInText(textArea, start, forward);
		if (findIn==null) return false;

		// Find the next location of the text we're searching for.
		RegExReplaceInfo info = getRegExReplaceInfo(toFind, findIn,
											forward, matchCase,
											wholeWord, replaceWith);

		findIn = null; // May help garbage collecting.

		// If a match was found, do the replace and return!
		if (info!=null) {

			// Without this, if JTextArea isn't in focus, selection won't
			// appear selected.
			c.setSelectionVisible(true);

			int matchStart = info.getStartIndex();
			int matchEnd = info.getEndIndex();
			if (forward) {
				matchStart += start;
				matchEnd += start;
			}
			selectAndPossiblyCenter(textArea, matchStart, matchEnd);
			textArea.replaceSelection(info.getReplacement());

			return true;

		}

		// No match.
		return false;

	}


	/**
	 * Finds the next instance of the text/regular expression specified from
	 * the caret position.  If a match is found, it is replaced with the
	 * specified replacement string.
	 *
	 * @param textArea The text area in which to search.
	 * @param toFind The text/regular expression  to search for.
	 * @param replaceWith The string to replace the found text with.
	 * @param forward Whether to search forward from the caret position or
	 *        backward from it.
	 * @param matchCase Whether the search should be case-sensitive.
	 * @param wholeWord Whether there should be spaces or tabs on either
	 *        side of the match.
	 * @param regex Whether or not this is a regular expression search.
	 * @return Whether a match was found (and thus replaced).
	 * @throws PatternSyntaxException If <code>regex</code> is
	 *         <code>true</code> but <code>toFind</code> is not a valid
	 *         regular expression.
	 * @throws IndexOutOfBoundsException If <code>regex</code> is
	 *         <code>true</code> and <code>replaceWith</code> references
	 *         an invalid group (less than zero or greater than the number
	 *         of groups matched).
	 * @see #regexReplace
	 * @see #find
	 */
	public static boolean replace(RTextArea textArea, String toFind,
				String replaceWith, boolean forward, boolean matchCase,
				boolean wholeWord, boolean regex)
									throws PatternSyntaxException {

		textArea.beginAtomicEdit();
		try {

			// Regular expression replacements have their own method.
			if (regex) {
				return regexReplace(textArea, toFind, replaceWith, forward,
								matchCase, wholeWord);
			}

			// Plain text search.  If we find it, replace it!
			// First make the dot and mark equal (get rid of any selection), as
			// a common use-case is the user will use "Find" to select the text
			// to replace, then click "Replace" to replace the current
			// selection. Since our find() method searches from an endpoint of
			// the selection, we must remove the selection to work properly.
			makeMarkAndDotEqual(textArea, forward);
			if (find(textArea, toFind, forward, matchCase, wholeWord, false)) {
				textArea.replaceSelection(replaceWith);
				return true;
			}

		} finally {
			textArea.endAtomicEdit();
		}

		return false;

	}


	/**
	 * Replaces all instances of the text/regular expression specified in
	 * the specified document with the specified replacement.
	 *
	 * @param textArea The text area in which to search.
	 * @param toFind The text/regular expression  to search for.
	 * @param replaceWith The string to replace the found text with.
	 * @param matchCase Whether the search should be case-sensitive.
	 * @param wholeWord Whether there should be spaces or tabs on either
	 *        side of the match.
	 * @param regex Whether or not this is a regular expression search.
	 * @return The number of replacements done.
	 * @throws PatternSyntaxException If <code>regex</code> is
	 *         <code>true</code> and <code>toFind</code> is an invalid
	 *         regular expression.
	 * @throws IndexOutOfBoundsException If <code>replaceWith</code> references
	 *         an invalid group (less than zero or greater than the number of
	 *         groups matched).
	 * @see #replace
	 * @see #regexReplace
	 * @see #find
	 */
	public static int replaceAll(RTextArea textArea, String toFind,
							String replaceWith, boolean matchCase,
							boolean wholeWord, boolean regex)
									throws PatternSyntaxException {

		int count = 0;

		textArea.beginAtomicEdit();
		try {

			if (regex) {

				if (replaceWith==null) {
					replaceWith = ""; // Needed by getReplacementText() below.
				}

				int oldOffs = textArea.getCaretPosition();
				textArea.setCaretPosition(0);
				int flags = Pattern.MULTILINE; // '^' and '$' are done per line.
				flags |= matchCase ? 0 :
							Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE;
				Pattern p = Pattern.compile(toFind, flags);
				while (SearchEngine.find(textArea, toFind, true, matchCase,
									wholeWord, true)) {
					Matcher m = p.matcher(textArea.getSelectedText());
					String replacement = getReplacementText(m, replaceWith);
					textArea.replaceSelection(replacement);
					count++;
				}
				if (count==0) { // If nothing was found, don't move the caret.
					textArea.setCaretPosition(oldOffs);
				}

			}

			else { // Non-regular expression search.
				textArea.setCaretPosition(0);
				while (SearchEngine.find(textArea, toFind, true, matchCase,
									wholeWord, false)) {
					textArea.replaceSelection(replaceWith);
					count++;
				}
			}

		} finally {
			textArea.endAtomicEdit();
		}

		return count;

	}


	/**
	 * Selects a range of text in a text component.  If the new selection is
	 * outside of the previous viewable rectangle, then the view is centered
	 * around the new selection.
	 *
	 * @param textArea The text component whose selection is to be centered.
	 * @param start The start of the range to select.
	 * @param end The end of the range to select.
	 */
	private static void selectAndPossiblyCenter(JTextArea textArea, int start,
												int end) {

		textArea.setSelectionStart(start);
		textArea.setSelectionEnd(end);

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
			textArea.setSelectionStart(start);
			textArea.setSelectionEnd(end);
			return;
		}

		Rectangle visible = textArea.getVisibleRect();

		// If the new selection is already in the view, don't scroll,
		// as that is visually jarring.
		if (visible.contains(r)) {
			textArea.setSelectionStart(start);
			textArea.setSelectionEnd(end);
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


}