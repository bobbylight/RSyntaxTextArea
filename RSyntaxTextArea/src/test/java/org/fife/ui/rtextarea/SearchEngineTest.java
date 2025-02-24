/*
 * 05/12/2010
 *
 * SearchEngineTest.java - Test cases for SearchEngine.java
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.fife.ui.rsyntaxtextarea.DocumentRange;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


/**
 * Some very basic unit tests for the {@link SearchEngine} used by
 * <code>RTextArea</code>/<code>RSyntaxTextArea</code>.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class SearchEngineTest {

	private RSyntaxTextArea textArea = new RSyntaxTextArea();

	private static String text;
	private SearchResult result;


	/**
	 * Asserts that two strings are equal, ignoring case.
	 *
	 * @param expected The expected value.
	 * @param actual The actual value.
	 */
	private void assertEqualsIgnoreCase(String expected, String actual) {
		expected = expected != null ? expected.toLowerCase() : null;
		actual = actual != null ? actual.toLowerCase() : null;
		assertEquals(expected, actual);
	}


	/**
	 * Checks whether the result of the last find or replace operation matches
	 * the given criteria.
	 *
	 * @param expected The expected search result.
	 */
	private void assertResult(SearchResult expected) {
		assertEquals(expected, this.result);
	}


	/**
	 * Asserts that the text area's selection is at the expected location and
	 * has the expected value.
	 *
	 * @param expected The expected value.
	 * @param offs The offset.
	 * @param matchCase Whether to match case.
	 */
	private void assertSelected(String expected, int offs, boolean matchCase) {
		String actual = textArea.getSelectedText();
		if (matchCase) {
			assertEquals(expected, actual);
		} else {
			assertEqualsIgnoreCase(expected, actual);
		}
		int selOffs = textArea.getSelectionStart();
		assertEquals(offs, selOffs, "unexpected selection offset: ");
	}


	private boolean findImpl(SearchContext context) {
		result = SearchEngine.find(textArea, context);
		return result.wasFound();
	}


	private boolean replaceImpl(SearchContext context) {
		result = SearchEngine.replace(textArea, context);
		return result.wasFound();
	}


	private int replaceAllImpl(SearchContext context) {
		result = SearchEngine.replaceAll(textArea, context);
		return result.getCount();
	}


	@BeforeAll
	static void setUpOnce() throws Exception {

		StringBuilder sb = new StringBuilder();
		InputStream in = SearchEngineTest.class.
				getResourceAsStream("SearchEngineTest.txt");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String s;
			while ((s = br.readLine()) != null) {
				sb.append(s).append("\n");
			}
		}

		// Strip off last newline
		text = sb.substring(0, sb.length()-1);

	}


	@Test
	void testSearchEngineFindEmptyDocument() {

		testSearchEngineFindEmptyDocumentImpl(false, false, false);
		testSearchEngineFindEmptyDocumentImpl(false, true, false);
		testSearchEngineFindEmptyDocumentImpl(false, true, true);
		testSearchEngineFindEmptyDocumentImpl(false, false, true);

		testSearchEngineFindEmptyDocumentImpl(true, false, false);
		testSearchEngineFindEmptyDocumentImpl(true, true, false);
		testSearchEngineFindEmptyDocumentImpl(true, true, true);
		testSearchEngineFindEmptyDocumentImpl(true, false, true);
	}


	private void testSearchEngineFindEmptyDocumentImpl(boolean forward, boolean regex,
									   boolean wholeWord) {
		textArea.setText(null);

		SearchContext context = new SearchContext();
		context.setSearchFor("test");
		context.setSearchForward(forward);
		context.setRegularExpression(regex);
		context.setWholeWord(wholeWord);
		context.setSearchWrap(true);

		assertFalse(findImpl(context));
	}


	/**
	 * Tests <code>SearchEngine.find()</code> when searching backward.
	 */
	@Test
	void testSearchEngineFindBackward() {
		testSearchEngineFindBackwardImpl(true);
		testSearchEngineFindBackwardImpl(false);
	}


	/**
	 * Tests <code>SearchEngine.find()</code> when searching backward.
	 *
	 * @param markAll Whether "mark all" should be enabled during the
	 *        test.
	 */
	private void testSearchEngineFindBackwardImpl(boolean markAll) {

		textArea.setText(text);

		int end = text.length();
		SearchContext context = new SearchContext();
		context.setSearchForward(false);
		context.setMarkAll(markAll);

		// Search for "chuck", ignoring case.
		context.setSearchFor("chuck");
		int markedCount = markAll ? 4 : 0;
		context.setMatchCase(false);
		textArea.setCaretPosition(end);
		boolean found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 60, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 48, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(48, 53), 1, markedCount));
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 32, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(32, 37), 1, markedCount));
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 26, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(26, 31), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "Chuck", matching case.
		context.setSearchFor("Chuck");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("Chuck", 26, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(26, 31), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "chuck", ignoring case, whole word
		context.setSearchFor("Chuck");
		markedCount = markAll ? 2 : 0;
		context.setMatchCase(false);
		context.setWholeWord(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 60, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 32, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(32, 37), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "wood", matching case, whole word
		context.setSearchFor("wood");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		context.setWholeWord(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("wood", 9, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(9, 13), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for ".ould", regex, ignoring case
		context.setSearchFor(".ould");
		markedCount = markAll ? 2 : 0;
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("could", 54, true);
		assertResult(new SearchResult(new DocumentRange(54, 59), 1, markedCount));
		found = findImpl(context);
		assertTrue(found);
		assertSelected("wOuld", 14, true);
		assertResult(new SearchResult(new DocumentRange(14, 19), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for ".ould", regex, matching case
		context.setSearchFor(".ould");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("could", 54, true);
		assertResult(new SearchResult(new DocumentRange(54, 59), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "[cd]huck", regex, ignoring case, whole word
		context.setSearchFor("[cd]hUCk");
		markedCount = markAll ? 2 : 0;
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 60, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 32, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(32, 37), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "[cd]huck", regex, matching case, whole word
		context.setSearchFor("[cd]huck");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 60, true);
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

	}


	/**
	 * Tests <code>SearchEngine.find()</code> when searching forward.
	 */
	@Test
	void testSearchEngineFindForward() {
		testSearchEngineFindForwardImpl(true);
		testSearchEngineFindForwardImpl(false);
	}


	/**
	 * Tests <code>SearchEngine.find()</code> when searching forward.
	 *
	 * @param markAll Whether "mark all" should be enabled during the test.
	 */
	private void testSearchEngineFindForwardImpl(boolean markAll) {

		textArea.setText(text);

		SearchContext context = new SearchContext();
		context.setMarkAll(markAll);

		// Search for "chuck", ignoring case.
		context.setSearchFor("chuck");
		int markedCount = markAll ? 4 : 0;
		boolean found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 26, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(26, 31), 1, markedCount));
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 32, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(32, 37), 1, markedCount));
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 48, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(48, 53), 1, markedCount));
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 60, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "Chuck", matching case.
		context.setSearchFor("Chuck");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("Chuck", 26, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(26, 31), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "chuck", ignoring case, whole word
		context.setSearchFor("chuck");
		markedCount = markAll ? 2 : 0;
		context.setMatchCase(false);
		context.setWholeWord(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 32, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(32, 37), 1, markedCount));
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 60, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "wood", matching case, whole word
		context.setSearchFor("wood");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		context.setWholeWord(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("wood", 9, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(9, 13), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for ".ould", regex, ignoring case
		context.setSearchFor(".ould");
		markedCount = markAll ? 2 : 0;
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("wOuld", 14, true);
		assertResult(new SearchResult(new DocumentRange(14, 19), 1, markedCount));
		found = findImpl(context);
		assertTrue(found);
		assertSelected("could", 54, true);
		assertResult(new SearchResult(new DocumentRange(54, 59), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for ".ould", regex, matching case
		context.setSearchFor(".ould");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("could", 54, true);
		assertResult(new SearchResult(new DocumentRange(54, 59), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "[cd]huck", regex, ignoring case, whole word
		context.setSearchFor("[cd]hUCk");
		markedCount = markAll ? 2 : 0;
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 32, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(32, 37), 1, markedCount));
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 60, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "[cd]huck", regex, matching case, whole word
		context.setSearchFor("[cd]huck");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertTrue(found);
		assertSelected("chuck", 60, true);
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertFalse(found);
		assertResult(new SearchResult(null, 0, markedCount));

	}


	/**
	 * Tests <code>SearchEngine.find()</code> when searching with wrap.
	 */
	@Test
	void testSearchEngineFindWrap() {

		textArea.setText(text);
		SearchContext context = new SearchContext();

		// Search for "Chuck", non-matching case.
		context.setSearchForward(false);
		context.setSearchFor("Chuck");
		context.setSearchWrap(false);
		context.setMatchCase(true);
		textArea.setCaretPosition(27);
		boolean found = findImpl(context);
		assertFalse(found);
		assertFalse(result.isWrapped());

		// Search for "Chuck", matching case.
		context.setSearchFor("Chuck");
		context.setSearchWrap(true);
		context.setMatchCase(true);
		textArea.setCaretPosition(27);
		found = findImpl(context);
		assertTrue(found);
		assertTrue(result.isWrapped());

		// Search for "Chuck", non-matching case.
		context.setSearchForward(true);
		context.setSearchFor("wood");
		context.setSearchWrap(false);
		context.setMatchCase(true);
		textArea.setCaretPosition(49);
		found = findImpl(context);
		assertFalse(found);
		assertFalse(result.isWrapped());

		// Search for "Chuck", matching case.
		context.setSearchFor("Chuck");
		context.setSearchWrap(true);
		context.setMatchCase(true);
		textArea.setCaretPosition(49);
		found = findImpl(context);
		assertTrue(found);
		assertTrue(result.isWrapped());
	}


	/**
	 * Verifies the fix for bug
	 * <a href="https://github.com/bobbylight/RSyntaxTextArea/issues/38">38</a>.
	 */
	@Test
	void testSearchEngineRegexFindEmptyString() {

		textArea.setText("how the who for what is it howhow");

		String searchFor = "[how]{3}|";
		SearchContext context = new SearchContext(searchFor);
		context.setRegularExpression(true);

		assertTrue(findImpl(context));
		assertResult(new SearchResult(new DocumentRange(0, 3), 1, 4));
		assertTrue(findImpl(context));
		assertResult(new SearchResult(new DocumentRange(8, 11), 1, 4));
		assertTrue(findImpl(context));
		assertResult(new SearchResult(new DocumentRange(27, 30), 1, 4));
		assertTrue(findImpl(context));
		assertResult(new SearchResult(new DocumentRange(30, 33), 1, 4));
		assertFalse(findImpl(context));

	}


	/**
	 * Tests <code>SearchEngine.markAll()</code>.
	 */
	@Test
	void testSearchEngineMarkAll() {

		textArea.setText(text);

		int end = text.length();
		SearchContext context = new SearchContext();
		context.setMarkAll(true);

		// Search for "chuck", ignoring case.
		context.setSearchFor("chuck");
		context.setMatchCase(false);
		textArea.setCaretPosition(end);
		SearchResult res = SearchEngine.markAll(textArea, context);
		assertEquals(end, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 4), res);
		textArea.setCaretPosition(3);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(3, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 4), res);

		// Search for "Chuck", matching case.
		context.setSearchFor("Chuck");
		context.setMatchCase(true);
		textArea.setCaretPosition(end);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(end, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 1), res);
		textArea.setCaretPosition(1);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(1, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 1), res);

		// Search for "chuck", ignoring case, whole word
		context.setSearchFor("Chuck");
		context.setMatchCase(false);
		context.setWholeWord(true);
		textArea.setCaretPosition(end);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(end, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 2), res);
		textArea.setCaretPosition(5);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(5, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 2), res);

		// Search for "wood", matching case, whole word
		context.setSearchFor("wood");
		context.setMatchCase(true);
		context.setWholeWord(true);
		textArea.setCaretPosition(end);
		textArea.setCaretPosition(end);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(end, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 1), res);
		textArea.setCaretPosition(2);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(2, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 1), res);

		// Search for ".ould", regex, ignoring case
		context.setSearchFor(".ould");
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(end, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 2), res);
		textArea.setCaretPosition(end-1);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(end-1, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 2), res);

		// Search for ".ould", regex, matching case
		context.setSearchFor(".ould");
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(end, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 1), res);
		textArea.setCaretPosition(end-1);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(end-1, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 1), res);

		// Search for "[cd]huck", regex, ignoring case, whole word
		context.setSearchFor("[cd]hUCk");
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(end, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 2), res);
		textArea.setCaretPosition(end-1);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(end-1, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 2), res);

		// Search for "[cd]huck", regex, matching case, whole word
		context.setSearchFor("[cd]huck");
		context.setMatchCase(true);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(end, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 1), res);
		textArea.setCaretPosition(end-1);
		res = SearchEngine.markAll(textArea, context);
		assertEquals(end-1, textArea.getCaretPosition());
		assertEquals(new SearchResult(null, 0, 1), res);

	}


	/**
	 * Tests <code>SearchEngine.replace()</code>.
	 *
	 * @param forward Whether to test searching forward or backward.
	 */
	private void testSearchEngineReplaceImpl(boolean forward) {

		int offs = forward ? 0 : text.length();
		SearchContext context = new SearchContext();
		context.setSearchForward(forward);
		context.setReplaceWith("FOOBAR");

		// Search for "chuck", ignoring case.
		context.setSearchFor("chuck");
		textArea.setText(text);
		String expected = textArea.getText().replaceAll("(?i:" + context.getSearchFor() +  ")",
			context.getReplaceWith());
		textArea.setCaretPosition(offs);
		boolean found = replaceImpl(context);
		assertTrue(found);
		//assertResult(new SearchResult(new DocumentRange(26, 32), 1, 0));
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertFalse(found);
		assertEquals(expected, textArea.getText());

		// Search for "chuck", matching case.
		context.setSearchFor("chuck");
		textArea.setText(text);
		expected = textArea.getText().replaceAll(context.getSearchFor(), context.getReplaceWith());
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(false);
		textArea.setCaretPosition(offs);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertFalse(found);
		assertEquals(expected, textArea.getText());

		// Search for "chuck", ignoring case, whole word.
		context.setSearchFor("chuck");
		textArea.setText(text);
		expected = "How much wood wOuld a woodChuck FOOBAR, if a woodchuck could FOOBAR wOod?";
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(false);
		textArea.setCaretPosition(offs);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertFalse(found);
		assertEquals(expected, textArea.getText());

		// Search for "chuck", matching case, whole word.
		context.setSearchFor("chuck");
		textArea.setText(text);
		expected = "How much wood wOuld a woodChuck chUck, if a woodchuck could FOOBAR wOod?";
		context.setMatchCase(true);
		context.setWholeWord(true);
		context.setRegularExpression(false);
		textArea.setCaretPosition(offs);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertFalse(found);
		assertEquals(expected, textArea.getText());

		// Search for ".huck", regex, ignoring case
		context.setSearchFor(".huck");
		textArea.setText(text);
		expected = textArea.getText().replaceAll("(?i:" + context.getSearchFor() +  ")", context.getReplaceWith());
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(offs);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertFalse(found);
		assertEquals(expected, textArea.getText());

		// Search for ".huck", regex, matching case
		context.setSearchFor(".huck");
		textArea.setText(text);
		expected = textArea.getText().replaceAll(context.getSearchFor(), context.getReplaceWith());
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(offs);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertFalse(found);
		assertEquals(expected, textArea.getText());

		// Search for "[cd]huck", regex, ignoring case, whole word
		context.setSearchFor("[cd]huck");
		textArea.setText(text);
		expected = "How much wood wOuld a woodChuck FOOBAR, if a woodchuck could FOOBAR wOod?";
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(offs);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertFalse(found);
		assertEquals(expected, textArea.getText());

		// Search for "[cd]hUck", regex, matching case, whole word
		context.setSearchFor("[cd]hUck");
		textArea.setText(text);
		expected = "How much wood wOuld a woodChuck FOOBAR, if a woodchuck could chuck wOod?";
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(offs);
		found = replaceImpl(context);
		assertTrue(found);
		found = replaceImpl(context);
		assertFalse(found);
		assertEquals(expected, textArea.getText());

	}


	/**
	 * Tests <code>SearchEngine.replace()</code> when searching backward.
	 */
	@Test
	void testSearchEngineReplaceBackward() {
		testSearchEngineReplaceImpl(false);
	}

	/**
	 * Verifies the fix for bug
	 * <a href="https://github.com/bobbylight/RSyntaxTextArea/issues/427>427</a>.
	 */
	@Test
	void testSearchEngineReplaceBackwardOnEmptyDocument() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		SearchContext context = new SearchContext("nothing");
		context.setSearchWrap(true);
		context.setSearchForward(false);
		assertDoesNotThrow(() -> {
			SearchEngine.find(textArea, context);
		});
	}

	/**
	 * Tests <code>SearchEngine.replace()</code> when searching forward.
	 */
	@Test
	void testSearchEngineReplaceForward() {
		testSearchEngineReplaceImpl(true);
	}


	/**
	 * Tests <code>SearchEngine.replace()</code> when searching with wrap.
	 */
	@Test
	void testSearchEngineReplaceWrap() {

		textArea.setText("oneoneone");
		SearchContext context = new SearchContext();

		// Replace the second instance of "one"
		context.setSearchForward(true);
		context.setSearchFor("one");
		context.setReplaceWith("two");
		context.setSearchWrap(true);
		textArea.setCaretPosition(1);
		boolean found = replaceImpl(context);
		assertTrue(found);
		assertFalse(result.isWrapped());
		assertEquals("onetwoone", textArea.getText());
		// The next match is selected
		assertEquals(6, textArea.getSelectionStart());
		assertEquals(9, textArea.getSelectionEnd());

		// Replace the third/last instance of "one"
		found = replaceImpl(context);
		assertTrue(found);
		assertTrue(result.isWrapped());
		assertEquals("onetwotwo", textArea.getText());
		// The first match is selected (wrap)
		assertEquals(0, textArea.getSelectionStart());
		assertEquals(3, textArea.getSelectionEnd());

		// Replace the first/only remaining instance of "one"
		found = replaceImpl(context);
		assertTrue(found);
		assertFalse(result.isWrapped());
		assertEquals("twotwotwo", textArea.getText());
		assertEquals(3, textArea.getSelectionStart());
		assertEquals(3, textArea.getSelectionEnd());

		// No change if no more matches
		found = replaceImpl(context);
		assertFalse(found);
		assertEquals("twotwotwo", textArea.getText());
		assertEquals(3, textArea.getSelectionStart());
		assertEquals(3, textArea.getSelectionEnd());
	}


	@Test
	void testSearchEngineReplaceAll_zeroLengthMatches() {

		textArea.setText("one two three");

		SearchContext context = new SearchContext();
		context.setSearchFor(".*");
		context.setReplaceWith("");
		context.setRegularExpression(true);
		int count = replaceAllImpl(context);
		// The goal here is simply to not go into an infinite loop
		assertEquals(1, count);
	}


	@Test
	void testSearchEngineReplaceAll_zeroLengthMatches_emptyText() {

		textArea.setText("");

		SearchContext context = new SearchContext();
		context.setSearchFor(".*");
		context.setReplaceWith("");
		context.setRegularExpression(true);
		int count = replaceAllImpl(context);
		// The goal here is simply to not go into an infinite loop
		assertEquals(1, count);
	}


	@Test
	void testSearchEngineReplaceAll_zeroLengthMatches_multiMatch() {

		textArea.setText("a\nba\n\na");

		SearchContext context = new SearchContext();
		context.setSearchFor("(?=a)");
		context.setReplaceWith("CCC");
		context.setRegularExpression(true);
		int count = replaceAllImpl(context);
		assertEquals(3, count);
		assertEquals("CCCa\nbCCCa\n\nCCCa", textArea.getText());
	}


	/**
	 * Tests <code>SearchEngine.replaceAll()</code>.
	 */
	@Test
	void testSearchEngineReplaceAll() {

		SearchContext context = new SearchContext();
		context.setReplaceWith("FOOBAR");

		// Replace "chuck", ignoring case.
		context.setSearchFor("chuck");
		textArea.setText(text);
		String expected = textArea.getText().replaceAll("(?i:" + context.getSearchFor() +  ")",
			context.getReplaceWith());
		int count = replaceAllImpl(context);
		assertEquals(4, count);
		assertEquals(expected, textArea.getText());

		// Replace "wood", matching case.
		context.setSearchFor("wood");
		textArea.setText(text);
		expected = textArea.getText().replaceAll(context.getSearchFor(), context.getReplaceWith());
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(false);
		count = replaceAllImpl(context);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// Replace "wood", ignoring case, whole word.
		context.setSearchFor("wood");
		textArea.setText(text);
		expected = "How much FOOBAR wOuld a woodChuck chUck, if a woodchuck could chuck FOOBAR?";
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(false);
		count = replaceAllImpl(context);
		assertEquals(2, count);
		assertEquals(expected, textArea.getText());

		// Replace "chUck", matching case, whole word.
		context.setSearchFor("chUck");
		textArea.setText(text);
		expected = "How much wood wOuld a woodChuck FOOBAR, if a woodchuck could chuck wOod?";
		context.setMatchCase(true);
		context.setWholeWord(true);
		context.setRegularExpression(false);
		count = replaceAllImpl(context);
		assertEquals(1, count);
		assertEquals(expected, textArea.getText());

		// Replace "wo(?:o|ul)d", regex, ignoring case.
		context.setSearchFor("wo(?:o|ul)d");
		textArea.setText(text);
		expected = textArea.getText().replaceAll("(?i:" + context.getSearchFor() +  ")", context.getReplaceWith());
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		count = replaceAllImpl(context);
		assertEquals(5, count);
		assertEquals(expected, textArea.getText());

		// Replace "wo(?:o|ul)d", regex, matching case.
		context.setSearchFor("wo(?:o|ul)d");
		textArea.setText(text);
		expected = textArea.getText().replaceAll(context.getSearchFor(), context.getReplaceWith());
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		count = replaceAllImpl(context);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// Replace "wo(?:o|ul)d", regex, ignoring case, whole word
		context.setSearchFor("wo(?:o|ul)d");
		textArea.setText(text);
		expected = "How much FOOBAR FOOBAR a woodChuck chUck, if a woodchuck could chuck FOOBAR?";
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		count = replaceAllImpl(context);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// Replace "wO(?:o|ul)d", regex, matching case, whole word
		context.setSearchFor("wO(?:o|ul)d");
		textArea.setText(text);
		expected = "How much wood FOOBAR a woodChuck chUck, if a woodchuck could chuck FOOBAR?";
		context.setMatchCase(true);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		count = replaceAllImpl(context);
		assertEquals(2, count);
		assertEquals(expected, textArea.getText());

	}


	/**
	 * Verifies that the "wrap" flag is enabled when doing a "replace all"
	 * operation.  This needs to be done to avoid infinite loops, and is
	 * OK since the entire document is scanned anyway.
	 */
	@Test
	void testSearchEngineReplaceAll_ignoreWrapFlag_noRegex() {

		// If the wrap flag wasn't ignored here, we'd have an
		// infinite loop since the searched-for text is a
		// substring of the replacement text.
		SearchContext context = new SearchContext();
		context.setSearchWrap(true);
		context.setSearchFor("o");
		context.setReplaceWith("oo");

		textArea.setText(text);
		textArea.setCaretPosition(20); // Somewhere in the middle
		int count = replaceAllImpl(context);
		assertEquals(11, count);

	}


	/**
	 * Verifies that the "wrap" flag is enabled when doing a "replace all"
	 * operation.  This needs to be done to avoid infinite loops, and is
	 * OK since the entire document is scanned anyway.
	 */
	@Test
	void testSearchEngineReplaceAll_ignoreWrapFlag_regex() {

		// If the wrap flag wasn't ignored here, we'd have an
		// infinite loop since the searched-for text is a
		// substring of the replacement text.
		SearchContext context = new SearchContext();
		context.setSearchWrap(true);
		context.setRegularExpression(true);
		context.setSearchFor("[ou]");
		context.setReplaceWith("oo");

		textArea.setText(text);
		textArea.setCaretPosition(20); // Somewhere in the middle
		int count = replaceAllImpl(context);
		assertEquals(18, count);

	}


	@Test
	void testSearchEngineReplaceAll_documentFilterBlocksReplacement() {

		SearchContext context = new SearchContext();
		context.setSearchWrap(true);
		context.setSearchFor("o");
		context.setReplaceWith("X");

		// "o" chars to replace are everywhere - before and after
		// the caret, and inside and outside the range protected
		// by a DocumentFilter.
		textArea.setText(text);
		textArea.setCaretPosition(20); // Somewhere in the middle
		((AbstractDocument)textArea.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
				throws BadLocationException {
				if (offset >= 30) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});

		// Note that our count is wrong here - it's the count of matches, not
		// replacements.  But there's no way for SearchEngine to know that
		// a DocumentFilter forbade some of the replacements.
		int count = replaceAllImpl(context);
		assertEquals(11, count);

		// Note only chars >= offset 30 are updated
		String expected = "How much wood wOuld a woodChuck chUck, if a wXXdchuck cXuld chuck wXXd?";
		assertEquals(expected, textArea.getText());
	}

	/**
	 * Tests <code>SearchEngine.replaceAll()</code> when the replacement string
	 * has captured groups.
	 */
	@Test
	void testSearchEngineRegexReplaceAllWithCapturedGroups() {

		SearchContext context = new SearchContext();
		context.setRegularExpression(true);

		// A single captured group.
		context.setSearchFor("r(o+)t");
		textArea.setText("root roOt root");
		String expected = "oo oO oo";
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setReplaceWith("$1");
		int count = replaceAllImpl(context);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// A single captured group - caret location shouldn't matter
		context.setSearchFor("r(o+)t");
		textArea.setText("root roOt root");
		textArea.setCaretPosition(textArea.getText().length()-4);
		expected = "oo oO oo";
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setReplaceWith("$1");
		count = replaceAllImpl(context);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// Multiple captured groups.
		context.setSearchFor("(\\d)(\\d+)[kM]");
		textArea.setText("152k 5271143M 3985k");
		expected = "1.52 5.271143 3.985";
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setReplaceWith("$1.$2");
		count = replaceAllImpl(context);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// No matches
		context.setSearchFor("(\\d)(\\d+)ABC");
		expected = "152k 5271143M 3985k";
		textArea.setText(expected);
		textArea.setCaretPosition(8);
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setReplaceWith("$1.$2");
		count = replaceAllImpl(context);
		assertEquals(0, count);
		assertEquals(expected, textArea.getText());
		assertEquals(8, textArea.getCaretPosition()); // Caret doesn't move

	}


}
