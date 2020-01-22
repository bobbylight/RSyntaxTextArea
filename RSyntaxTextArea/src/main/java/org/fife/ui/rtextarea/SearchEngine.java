/*
 * 02/19/2006
 *
 * SearchEngine.java - Handles find/replace operations in an RTextArea.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

import org.fife.ui.rsyntaxtextarea.DocumentRange;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;


/**
 * A singleton class that can perform advanced find/replace operations
 * in an {@link RTextArea}.  Simply create a {@link SearchContext} and call
 * one of the following methods:
 *
 * <ul>
 *    <li>{@link #find(JTextArea, SearchContext)}
 *    <li>{@link #replace(RTextArea, SearchContext)}
 *    <li>{@link #replaceAll(RTextArea, SearchContext)}
 *    <li>{@link #markAll(RTextArea, SearchContext)}
 * </ul>
 *
 * @author Robert Futrell
 * @version 1.0
 * @see SearchContext
 */
public final class SearchEngine {


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
	 * @param context What to search for and all search options.
	 * @return The result of the operation.
	 * @throws PatternSyntaxException If this is a regular expression search
	 *         but the search text is an invalid regular expression.
	 * @see #replace(RTextArea, SearchContext)
	 * @see #replaceAll(RTextArea, SearchContext)
	 */
	public static SearchResult find(JTextArea textArea, SearchContext context) {

		// Always clear previous "mark all" highlights
		if (textArea instanceof RTextArea || context.getMarkAll()) {
			((RTextArea)textArea).clearMarkAllHighlights();
		}
		boolean doMarkAll = textArea instanceof RTextArea && context.getMarkAll();

		String text = context.getSearchFor();
		if (text==null || text.length()==0) {
			if (doMarkAll) {
				// Force "mark all" event to be broadcast so listeners know to
				// clear their mark-all markers.  The RSTA already cleared its
				// highlights above, but cleraMarkAllHighlights() doesn't fire
				// an event itself for performance reasons.
				List<DocumentRange> emptyRangeList = Collections.emptyList();
				((RTextArea)textArea).markAll(emptyRangeList);
			}
			return new SearchResult();
		}

		// Be smart about what position we're "starting" at.  We don't want
		// to find a match in the currently selected text (if any), so we
		// start searching AFTER the selection if searching forward, and
		// BEFORE the selection if searching backward.
		Caret c = textArea.getCaret();
		boolean forward = context.getSearchForward();
		int start = forward ? Math.max(c.getDot(), c.getMark()) :
						Math.min(c.getDot(), c.getMark());

		String findIn = getFindInText(textArea, start, forward);
		if (!context.getSearchWrap() && (findIn == null || findIn.length() == 0)) {
			return new SearchResult();
		}

		int markAllCount = 0;
		if (doMarkAll) {
			markAllCount = markAllImpl((RTextArea)textArea, context).
					getMarkedCount();
		}

		SearchResult result = SearchEngine.findImpl(findIn == null ? "" : findIn, context);
		if (result.wasFound() && !result.getMatchRange().isZeroLength()) {
			// Without this, if JTextArea isn't in focus, selection
			// won't appear selected.
			textArea.getCaret().setSelectionVisible(true);
			if (forward && start>-1) {
				result.getMatchRange().translate(start);
			}
			RSyntaxUtilities.selectAndPossiblyCenter(textArea,
					result.getMatchRange(), true);
		} else if (context.getSearchWrap() && !result.wasFound()) {
			if (forward) {
				start = 0;
			} else {
				start = textArea.getDocument().getLength() - 1;
			}

			findIn = getFindInText(textArea, start, forward);

			if (findIn == null || findIn.length() == 0) {
				SearchResult emptyResult = new SearchResult();
				emptyResult.setWrapped(true);
				return emptyResult;
			}

			if (doMarkAll) {
				markAllCount = markAllImpl((RTextArea) textArea, context).
					getMarkedCount();
			}

			result = SearchEngine.findImpl(findIn, context);
			result.setWrapped(true);
			if (result.wasFound() && !result.getMatchRange().isZeroLength()) {
				// Without this, if JTextArea isn't in focus, selection
				// won't appear selected.
				textArea.getCaret().setSelectionVisible(true);
				if (forward) {
					result.getMatchRange().translate(start);
				}
				RSyntaxUtilities.selectAndPossiblyCenter(textArea,
					result.getMatchRange(), true
				);
			}
		}

		result.setMarkedCount(markAllCount);
		return result;

	}


	/**
	 * Finds the next instance of the string/regular expression specified
	 * from the caret position.  If a match is found, it is selected in this
	 * text area.
	 *
	 * @param findIn The text to search in.
	 * @param context The search context.
	 * @return The result of the operation.  "Mark all" will always be zero,
	 *         since this method does not perform that operation.
	 * @throws PatternSyntaxException If this is a regular expression search
	 *         but the search text is an invalid regular expression.
	 */
	private static SearchResult findImpl(String findIn, SearchContext context) {

		String text = context.getSearchFor();
		boolean forward = context.getSearchForward();

		// Find the next location of the text we're searching for.
		DocumentRange range = null;
		if (!context.isRegularExpression()) {
			int pos = getNextMatchPos(text, findIn, forward,
								context.getMatchCase(), context.getWholeWord());
			findIn = null; // May help garbage collecting.
			if (pos!=-1) {
				range = new DocumentRange(pos, pos+text.length());
			}
		}

		else {
			// Regex matches can have varying widths.  The returned point's
			// x- and y-values represent the start and end indices of the
			// match in findIn.
			Point regExPos = null;
			int start = 0;
			do {
				regExPos = getNextMatchPosRegEx(text, findIn.substring(start),
					forward, context.getMatchCase(), context.getWholeWord());
				if (regExPos!=null) {
					if (regExPos.x!=regExPos.y) {
						regExPos.translate(start, start);
						range = new DocumentRange(regExPos.x, regExPos.y);
					}
					else {
						start += regExPos.x + 1;
					}
				}
			} while (start<findIn.length() && regExPos!=null && range==null);//while (emptyMatchFound);
		}

		if (range!=null) {
			return new SearchResult(range, 1, 0);
		}
		return new SearchResult();

	}


	/**
	 * Returns a <code>CharSequence</code> for a text area that doesn't make a
	 * copy of its contents for iteration.  This conserves memory but is likely
	 * just a tad slower.
	 *
	 * @param textArea The text area whose document is the basis for the
	 *        <code>CharSequence</code>.
	 * @param start The starting offset of the sequence (or ending offset if
	 *        <code>forward</code> is <code>false</code>).
	 * @param forward Whether we're searching forward or backward.
	 * @return The character sequence.
	 */
	private static CharSequence getFindInCharSequence(RTextArea textArea,
			int start, boolean forward) {
		RDocument doc = (RDocument)textArea.getDocument();
		int csStart = 0;
		int csEnd = 0;
		if (forward) {
			csStart = start;
			csEnd = doc.getLength();
		}
		else {
			csStart = 0;
			csEnd = start;
		}
		return new RDocumentCharSequence(doc, csStart, csEnd);
	}


	/**
	 * Returns the text in which to search, as a string.  This is used
	 * internally to grab the smallest buffer possible in which to search.
	 */
	private static String getFindInText(JTextArea textArea, int start,
									boolean forward) {

		// Be smart about the text we grab to search in.  We grab more than
		// a single line because our searches can return multi-line results.
		// We copy only the chars that will be searched through.
		String findIn = null;
		try {
			if (forward) {
				findIn = textArea.getText(start,
							textArea.getDocument().getLength()-start);
			}
			else { // backward
				findIn = textArea.getText(0, start);
			}
		} catch (BadLocationException ble) {
			// Never happens; findIn will be null anyway.
			ble.printStackTrace();
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List getMatches(Matcher m, String replaceStr) {
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
	 * case or not.<p>
	 *
	 * Most clients will have no need to call this method directly.
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
	 */
	public static int getNextMatchPos(String searchFor, String searchIn,
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
	private static int getNextMatchPosImpl(String searchFor,
								String searchIn, boolean goForward,
								boolean matchCase, boolean wholeWord) {

		if (wholeWord) {
			int len = searchFor.length();
			int temp = goForward ? 0 : searchIn.length();
			int tempChange = goForward ? 1 : -1;
			while (true) {
				if (goForward) {
					temp = searchIn.indexOf(searchFor, temp);
				}
				else {
					temp = searchIn.lastIndexOf(searchFor, temp);
				}
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
	private static Point getNextMatchPosRegEx(String regEx,
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
	private static Object getNextMatchPosRegExImpl(String regEx,
							CharSequence searchIn, boolean goForward,
							boolean matchCase, boolean wholeWord,
							String replaceStr) {

		if (wholeWord) {
			regEx = "\\b" + regEx + "\\b";
		}

		// Make a pattern that takes into account whether or not to match case.
		int flags = Pattern.MULTILINE; // '^' and '$' are done per line.
		flags = RSyntaxUtilities.getPatternFlags(matchCase, flags);
		Pattern pattern = null;
		try {
			pattern = Pattern.compile(regEx, flags);
		} catch (PatternSyntaxException pse) {
			return null; // e.g. a "mark all" request with incomplete regex
		}

		// Make a Matcher to find the regEx instances.
		Matcher m = pattern.matcher(searchIn);

		// Search forwards
		if (goForward) {
			if (m.find()) {
				if (replaceStr==null) { // Find, not replace.
					return new Point(m.start(), m.end());
				}
				// Otherwise, replace
				return new RegExReplaceInfo(m.group(0),
						m.start(), m.end(),
						getReplacementText(m, replaceStr));
			}
		}

		// Search backwards
		else {
			List<?> matches = getMatches(m, replaceStr);
			if (!matches.isEmpty()) {
				return matches.get(matches.size()-1);
			}
		}

		return null; // No match found

	}


	/**
	 * Returns information on how to implement a regular expression "replace"
	 * action in the specified text with the specified replacement string.
	 *
	 * @param searchIn The string to search in.
	 * @param context The search options.
	 * @return A <code>RegExReplaceInfo</code> object describing how to
	 *         implement the replace.
	 * @throws PatternSyntaxException If the search text is an invalid regular
	 *         expression.
	 * @throws IndexOutOfBoundsException If the replacement text references an
	 *         invalid group (less than zero or greater than the number of
	 *         groups matched).
	 * @see #getNextMatchPos
	 */
	private static RegExReplaceInfo getRegExReplaceInfo(CharSequence searchIn,
										SearchContext context) {
		// Can't pass null to getNextMatchPosRegExImpl or it'll think
		// you're doing a "find" operation instead of "replace, and return a
		// Point.
		String replacement = context.getReplaceWith();
		if (replacement==null) {
			replacement = "";
		}
		String regex = context.getSearchFor();
		boolean goForward = context.getSearchForward();
		boolean matchCase = context.getMatchCase();
		boolean wholeWord = context.getWholeWord();
		return (RegExReplaceInfo)getNextMatchPosRegExImpl(regex, searchIn,
						goForward, matchCase, wholeWord, replacement);
	}


	/**
	 * Called internally by <code>getMatches()</code>.  This method assumes
	 * that the specified matcher has just found a match, and that you want
	 * to get the string with which to replace that match.<p>
	 *
	 * Escapes simply insert the escaped character, except for <code>\n</code>
	 * and <code>\t</code>, which insert a newline and tab respectively.
	 * Substrings of the form <code>$\d+</code> are considered to be matched
	 * groups.  To include a literal dollar sign in your template, escape it
	 * (i.e. <code>\$</code>).<p>
	 *
	 * Most clients will have no need to call this method directly.
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
		StringBuilder result = new StringBuilder();

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
				if (m.group(refNum) != null) {
					result.append(m.group(refNum));
				}

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
	 * <code>substr(searchIn, startPos, startPos+searchStringLength)</code>
	 * are <em>not</em> letters or digits.
	 */
	private static boolean isWholeWord(CharSequence searchIn,
											int offset, int len) {

		boolean wsBefore, wsAfter;

		try {
			wsBefore = !Character.isLetterOrDigit(searchIn.charAt(offset - 1));
		} catch (IndexOutOfBoundsException e) {
			wsBefore = true;
		}

		try {
			wsAfter  = !Character.isLetterOrDigit(searchIn.charAt(offset + len));
		} catch (IndexOutOfBoundsException e) {
			wsAfter = true;
		}

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
	private static int makeMarkAndDotEqual(JTextArea textArea,
										boolean forward) {
		Caret c = textArea.getCaret();
		int val = forward ? Math.min(c.getDot(), c.getMark()) :
						Math.max(c.getDot(), c.getMark());
		c.setDot(val);
		return val;
	}


	/**
	 * Marks all instances of the specified text in this text area.  This
	 * method is typically only called directly in response to search events
	 * of type <code>SearchEvent.Type.MARK_ALL</code>.  "Mark all" behavior
	 * is automatically performed when {@link #find(JTextArea, SearchContext)}
	 * or {@link #replace(RTextArea, SearchContext)} is called.
	 *
	 * @param textArea The text area in which to mark occurrences.
	 * @param context The search context specifying the text to search for.
	 *        It is assumed that <code>context.getMarkAll()</code> has already
	 *        been checked and returns <code>true</code>.
	 * @return The results of the operation.
	 */
	public static SearchResult markAll(RTextArea textArea,
			SearchContext context) {
		textArea.clearMarkAllHighlights();
//		if (context.getMarkAll()) {
			return markAllImpl(textArea, context);
//		}
//		return new SearchResult();
	}


	/**
	 * Marks all instances of the specified text in this text area.  This
	 * method is typically only called directly in response to search events
	 * of type <code>SearchEvent.Type.MARK_ALL</code>.  "Mark all" behavior
	 * is automatically performed when {@link #find(JTextArea, SearchContext)}
	 * or #replace(RTextArea, SearchContext) is called.
	 *
	 * @param textArea The text area in which to mark occurrences.
	 * @param context The search context specifying the text to search for.
	 *        It is assumed that <code>context.getMarkAll()</code> has already
	 *        been checked and returns <code>true</code>.
	 * @return The results of the operation.
	 */
	private static SearchResult markAllImpl(RTextArea textArea,
			SearchContext context) {

		String toMark = context.getSearchFor();
		int markAllCount = 0;

		// context.getMarkAll()==false => clear "mark all" highlights
		if (context.getMarkAll() && toMark!=null && toMark.length()>0
				/*&& !toMark.equals(markedWord)*/) {

			List<DocumentRange> highlights = new ArrayList<>();
			context = context.clone();
			context.setSearchForward(true);
			context.setMarkAll(false);

			String findIn = textArea.getText();
			int start = 0;

			// Optimization for pedantic worst-case of thousands of matches
			// while doing a case-insensitive search.  Go ahead and normalize
			// case just once, so that toLowerCase() isn't done for toMark and
			// findIn for each match found.
			if (!context.getMatchCase()) {
				context.setMatchCase(true);
				context.setSearchFor(toMark.toLowerCase());
				findIn = findIn.toLowerCase();
			}

			SearchResult res = SearchEngine.findImpl(findIn, context);
			while (res.wasFound()) {
				DocumentRange match = res.getMatchRange().translate(start);
				if (match.isZeroLength()) {
					// Searched for a regex like "foo|".  The "empty string"
					// part of the regex matches space between chars.  We want
					// to skip these in the case of mark-all.
					start = match.getEndOffset() + 1;
					if (start>findIn.length()) {
						break;
					}
				}
				else {
					highlights.add(match);
					start = match.getEndOffset();
				}
				res = SearchEngine.findImpl(findIn.substring(start), context);
			}
			textArea.markAll(highlights);
			markAllCount = highlights.size();
		}

		else {
			// Force a repaint of "mark all" highlights so ErrorStrips can
			// get updated
			List<DocumentRange> empty = Collections.emptyList();
			textArea.markAll(empty);
		}

		return new SearchResult(null, 0, markAllCount);

	}


	/**
	 * Finds the next instance of the regular expression specified from
	 * the caret position.  If a match is found, it is replaced with
	 * the specified replacement string.
	 *
	 * @param textArea The text area in which to search.
	 * @param context What to search for and all search options.
	 * @return The result of the operation.
	 * @throws PatternSyntaxException If this is a regular expression search
	 *         but the search text is an invalid regular expression.
	 * @throws IndexOutOfBoundsException If this is a regular expression search
	 *         but the replacement text references an invalid group (less than
	 *         zero or greater than the number of groups matched).
	 * @see #replace(RTextArea, SearchContext)
	 * @see #find(JTextArea, SearchContext)
	 */
	private static SearchResult regexReplace(RTextArea textArea,
			SearchContext context) {

		// Be smart about what position we're "starting" at.  For example,
		// if they are searching backwards and there is a selection such that
		// the dot is past the mark, and the selection is the text for which
		// you're searching, this search will find and return the current
		// selection.  So, in that case we start at the beginning of the
		// selection.
		Caret c = textArea.getCaret();
		boolean forward = context.getSearchForward();
		int start = makeMarkAndDotEqual(textArea, forward);

		CharSequence findIn = getFindInCharSequence(textArea, start, forward);
		if (findIn==null) {
			return new SearchResult();
		}

		int markAllCount = 0;
		if (context.getMarkAll()) {
			markAllCount = markAllImpl(textArea, context).getMarkedCount();
		}

		// Find the next location of the text we're searching for.
		RegExReplaceInfo info = getRegExReplaceInfo(findIn, context);

		// If a match was found, do the replace and return!
		DocumentRange range = null;
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
			textArea.setSelectionStart(matchStart);
			textArea.setSelectionEnd(matchEnd);
			String replacement = info.getReplacement();
			textArea.replaceSelection(replacement);

			// If there is another match, find and select it.
			int dot = matchStart + replacement.length();
			findIn = getFindInCharSequence(textArea, dot, forward);
			info = getRegExReplaceInfo(findIn, context);
			if (info!=null) {
				matchStart = info.getStartIndex();
				matchEnd = info.getEndIndex();
				if (forward) {
					matchStart += dot;
					matchEnd += dot;
				}
				range = new DocumentRange(matchStart, matchEnd);
			}
			else {
				range = new DocumentRange(dot, dot);
			}
			RSyntaxUtilities.selectAndPossiblyCenter(textArea, range, true);

		}

		int count = range!=null ? 1 : 0;
		return new SearchResult(range, count, markAllCount);

	}


	/**
	 * Finds the next instance of the text/regular expression specified from
	 * the caret position.  If a match is found, it is replaced with the
	 * specified replacement string.
	 *
	 * @param textArea The text area in which to search.
	 * @param context What to search for and all search options.
	 * @return The result of the operation.
	 * @throws PatternSyntaxException If this is a regular expression search
	 *         but the search text is an invalid regular expression.
	 * @throws IndexOutOfBoundsException If this is a regular expression search
	 *         but the replacement text references an invalid group (less than
	 *         zero or greater than the number of groups matched).
	 * @see #replaceAll(RTextArea, SearchContext)
	 * @see #find(JTextArea, SearchContext)
	 */
	public static SearchResult replace(RTextArea textArea,
			SearchContext context) {

		// Always clear previous "mark all" highlights
		if (context.getMarkAll()) {
			textArea.clearMarkAllHighlights();
		}

		String toFind = context.getSearchFor();
		if (toFind==null || toFind.length()==0) {
			return new SearchResult();
		}

		textArea.beginAtomicEdit();
		try {

			// Regular expression replacements have their own method.
			if (context.isRegularExpression()) {
				return regexReplace(textArea, context);
			}

			// Plain text search.  If we find it, replace it!
			// First make the dot and mark equal (get rid of any selection), as
			// a common use-case is the user will use "Find" to select the text
			// to replace, then click "Replace" to replace the current
			// selection. Since our find() method searches from an endpoint of
			// the selection, we must remove the selection to work properly.
			makeMarkAndDotEqual(textArea, context.getSearchForward());
			SearchResult res = find(textArea, context);

			if (res.wasFound() && !res.getMatchRange().isZeroLength()) {

				// Do the replacement
				String replacement = context.getReplaceWith();
				textArea.replaceSelection(replacement);

				// Move the caret to the right position.
				int dot = res.getMatchRange().getStartOffset();
				if (context.getSearchForward()) {
					int length = replacement==null ? 0 : replacement.length();
					dot += length;
				}
				textArea.setCaretPosition(dot);

				SearchResult next = find(textArea, context);
				DocumentRange range;
				if (next.wasFound()) {
					range = next.getMatchRange();
				}
				else {
					range = new DocumentRange(dot, dot);
				}
				res.setMatchRange(range);
				RSyntaxUtilities.selectAndPossiblyCenter(textArea, range, true);

			}

			return res;

		} finally {
			textArea.endAtomicEdit();
		}

	}


	/**
	 * Replaces all instances of the text/regular expression specified in
	 * the specified document with the specified replacement.
	 *
	 * @param textArea The text area in which to search.
	 * @param context What to search for and all search options.
	 * @return The result of the operation.
	 * @throws PatternSyntaxException If this is a regular expression search
	 *         but the replacement text is an invalid regular expression.
	 * @throws IndexOutOfBoundsException If this is a regular expression search
	 *         but the replacement text references an invalid group (less than
	 *         zero or greater than the number of groups matched).
	 * @see #replace(RTextArea, SearchContext)
	 * @see #find(JTextArea, SearchContext)
	 */
	public static SearchResult replaceAll(RTextArea textArea,
			SearchContext context) {

		// Always clear previous "mark all" highlights
		if (context.getMarkAll()) {
			textArea.clearMarkAllHighlights();
		}

		context.setSearchForward(true); // Replace all always searches forward
		String toFind = context.getSearchFor();
		if (toFind==null || toFind.length()==0) {
			return new SearchResult();
		}

		// We call into SearchEngine.replace() multiple times, but we don't
		// want to perform a "mark all" ever.
		if (context.getMarkAll()) {
			context = context.clone();
			context.setMarkAll(false);
		}

		SearchResult lastFound = null;
		int count = 0;
		textArea.beginAtomicEdit();
		try {

			int oldOffs = textArea.getCaretPosition();
			textArea.setCaretPosition(0);
			SearchResult res = SearchEngine.replace(textArea, context);

			while (res.wasFound()) {

				lastFound = res;
				count++;

				// Protect against regexes that can match 0-length strings,
				// such as ".*" and "(?=a).  See:
				// https://github.com/bobbylight/RSyntaxTextArea/issues/107
				if (res.getMatchRange().isZeroLength()) {
					if (res.getMatchRange().getStartOffset() ==
							textArea.getDocument().getLength()) {
						break;
					}
					else { // End of a line
						textArea.setCaretPosition(textArea.getCaretPosition() + 1);
					}
				}

				res = SearchEngine.replace(textArea, context);

			}

			if (lastFound==null) { // If nothing was found, don't move the caret
				textArea.setCaretPosition(oldOffs);
				lastFound = new SearchResult();
			}

		} finally {
			textArea.endAtomicEdit();
		}

		lastFound.setCount(count);
		return lastFound;

	}


}
