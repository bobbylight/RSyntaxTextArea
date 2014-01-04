/*
 * 05/12/2010
 *
 * SearchEngineTest.java - Test cases for SearchEngine.java
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.text.BadLocationException;

import junit.framework.TestCase;

import org.fife.ui.rsyntaxtextarea.DocumentRange;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.SearchEngine;

/**
 * Some very basic unit tests for the {@link SearchEngine} used by
 * <code>RTextArea</code>/<code>RSyntaxTextArea</code>.
 * 
 * @author Robert Futrell
 * @version 1.0
 */
public class SearchEngineTest extends TestCase {

	private RSyntaxTextArea textArea = new RSyntaxTextArea();

	private static String text;
	private SearchResult result;


	/**
	 * Asserts that two strings are equal, ignoring case.
	 *
	 * @param expected
	 * @param actual
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
	 * @param expected
	 * @param offs
	 * @param matchCase
	 */
	private void assertSelected(String expected, int offs, boolean matchCase) {
		String actual = textArea.getSelectedText();
		if (matchCase) {
			assertEquals(expected, actual);
		} else {
			assertEqualsIgnoreCase(expected, actual);
		}
		int selOffs = textArea.getSelectionStart();
		assertEquals("unexpected selection offset: ", offs, selOffs);
	}


	private final boolean findImpl(SearchContext context) {
		result = SearchEngine.find(textArea, context);
		return result.wasFound();
	}


	private final boolean replaceImpl(SearchContext context) {
		result = SearchEngine.replace(textArea, context);
		return result.wasFound();
	}


	private final int replaceAllImpl(SearchContext context) {
		result = SearchEngine.replaceAll(textArea, context);
		return result.getCount();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setUp() throws Exception {

		// setUp() is called once per test, each with a new instantiation of
		// SearchEngineTest, so check a static variable to ensure that
		// initialization is only done once.

		if (text == null || text.length() <= 0) {

			StringBuilder sb = new StringBuilder();
			InputStream in = getClass().getResourceAsStream("text.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String s = null;
			try {
				while ((s=br.readLine())!=null) {
					sb.append(s).append("\n");
				}
			} finally {
				br.close();
			}

			// Strip off last newline
			text = sb.toString().substring(0, sb.length()-1);

		}

		super.setUp();

	}


	/**
	 * Tests <code>SearchEngine.find()</code> when searching backward.
	 */
	public void testSearchEngineFindBackward() throws BadLocationException {
		testSearchEngineFindBackwardImpl(true);
		testSearchEngineFindBackwardImpl(false);
	}


	/**
	 * Tests <code>SearchEngine.find()</code> when searching backward.
	 *
	 * @param markAll Whether or not "mark all" should be enabled during the
	 *        test.
	 */
	private void testSearchEngineFindBackwardImpl(boolean markAll)
			throws BadLocationException {

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
		assertEquals(true, found);
		assertSelected("chuck", 60, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 48, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(48, 53), 1, markedCount));
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 32, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(32, 37), 1, markedCount));
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 26, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(26, 31), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "Chuck", matching case.
		context.setSearchFor("Chuck");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("Chuck", 26, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(26, 31), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "chuck", ignoring case, whole word
		context.setSearchFor("Chuck");
		markedCount = markAll ? 2 : 0;
		context.setMatchCase(false);
		context.setWholeWord(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 60, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 32, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(32, 37), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "wood", matching case, whole word
		context.setSearchFor("wood");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		context.setWholeWord(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("wood", 9, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(9, 13), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for ".ould", regex, ignoring case
		context.setSearchFor(".ould");
		markedCount = markAll ? 2 : 0;
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("could", 54, true);
		assertResult(new SearchResult(new DocumentRange(54, 59), 1, markedCount));
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("wOuld", 14, true);
		assertResult(new SearchResult(new DocumentRange(14, 19), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for ".ould", regex, matching case
		context.setSearchFor(".ould");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("could", 54, true);
		assertResult(new SearchResult(new DocumentRange(54, 59), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "[cd]huck", regex, ignoring case, whole word
		context.setSearchFor("[cd]hUCk");
		markedCount = markAll ? 2 : 0;
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 60, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 32, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(32, 37), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "[cd]huck", regex, matching case, whole word
		context.setSearchFor("[cd]huck");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 60, true);
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

	}


	/**
	 * Tests <code>SearchEngine.find()</code> when searching forward.
	 */
	public void testSearchEngineFindForward() throws BadLocationException {
		testSearchEngineFindForwardImpl(true);
		testSearchEngineFindForwardImpl(false);
	}


	/**
	 * Tests <code>SearchEngine.find()</code> when searching forward.
	 *
	 * @param markAll Whether "mark all" should be enabled during the test.
	 */
	public void testSearchEngineFindForwardImpl(boolean markAll)
			throws BadLocationException {

		textArea.setText(text);

		SearchContext context = new SearchContext();
		context.setMarkAll(markAll);

		// Search for "chuck", ignoring case.
		context.setSearchFor("chuck");
		int markedCount = markAll ? 4 : 0;
		boolean found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 26, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(26, 31), 1, markedCount));
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 32, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(32, 37), 1, markedCount));
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 48, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(48, 53), 1, markedCount));
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 60, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "Chuck", matching case.
		context.setSearchFor("Chuck");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("Chuck", 26, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(26, 31), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "chuck", ignoring case, whole word
		context.setSearchFor("chuck");
		markedCount = markAll ? 2 : 0;
		context.setMatchCase(false);
		context.setWholeWord(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 32, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(32, 37), 1, markedCount));
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 60, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "wood", matching case, whole word
		context.setSearchFor("wood");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		context.setWholeWord(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("wood", 9, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(9, 13), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for ".ould", regex, ignoring case
		context.setSearchFor(".ould");
		markedCount = markAll ? 2 : 0;
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("wOuld", 14, true);
		assertResult(new SearchResult(new DocumentRange(14, 19), 1, markedCount));
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("could", 54, true);
		assertResult(new SearchResult(new DocumentRange(54, 59), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for ".ould", regex, matching case
		context.setSearchFor(".ould");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("could", 54, true);
		assertResult(new SearchResult(new DocumentRange(54, 59), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "[cd]huck", regex, ignoring case, whole word
		context.setSearchFor("[cd]hUCk");
		markedCount = markAll ? 2 : 0;
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 32, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(32, 37), 1, markedCount));
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 60, context.getMatchCase());
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

		// Search for "[cd]huck", regex, matching case, whole word
		context.setSearchFor("[cd]huck");
		markedCount = markAll ? 1 : 0;
		context.setMatchCase(true);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(0);
		found = findImpl(context);
		assertEquals(true, found);
		assertSelected("chuck", 60, true);
		assertResult(new SearchResult(new DocumentRange(60, 65), 1, markedCount));
		found = findImpl(context);
		assertEquals(false, found);
		assertResult(new SearchResult(null, 0, markedCount));

	}


	/**
	 * Tests <code>SearchEngine.markAll()</code>.
	 */
	public void testSearchEngineMarkAll() {

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
	private void testSearchEngineReplace(boolean forward)
										throws BadLocationException {

		int offs = forward ? 0 : text.length();
		SearchContext context = new SearchContext();
		context.setSearchForward(forward);
		context.setReplaceWith("FOOBAR");

		// Search for "chuck", ignoring case.
		context.setSearchFor("chuck");
		textArea.setText(text);
		String expected = textArea.getText().replaceAll("(?i:" + context.getSearchFor() +  ")", context.getReplaceWith());
		textArea.setCaretPosition(offs);
		boolean found = replaceImpl(context);
		assertEquals(true, found);
		//assertResult(new SearchResult(new DocumentRange(26, 32), 1, 0));
		found = replaceImpl(context);
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(false, found);
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
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(false, found);
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
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(false, found);
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
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(false, found);
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
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(false, found);
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
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(false, found);
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
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(false, found);
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
		assertEquals(true, found);
		found = replaceImpl(context);
		assertEquals(false, found);
		assertEquals(expected, textArea.getText());

	}


	/**
	 * Tests <code>SearchEngine.replace()</code> when searching backward.
	 */
	public void testSearchEngineReplaceBackward() throws BadLocationException {
		testSearchEngineReplace(false);
	}


	/**
	 * Tests <code>SearchEngine.replace()</code> when searching forward.
	 */
	public void testSearchEngineReplaceForward() throws BadLocationException {
		testSearchEngineReplace(true);
	}


	/**
	 * Tests <code>SearchEngine.replaceAll()</code>.
	 */
	public void testSearchEngineReplaceAll() throws BadLocationException {

		SearchContext context = new SearchContext();
		context.setReplaceWith("FOOBAR");

		// Replace "chuck", ignoring case.
		context.setSearchFor("chuck");
		textArea.setText(text);
		String expected = textArea.getText().replaceAll("(?i:" + context.getSearchFor() +  ")", context.getReplaceWith());
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
	 * Tests <code>SearchEngine.replaceAll()</code> when the replacement string
	 * has captured groups.
	 */
	public void testSearchEngineRegexReplaceAllWithCapturedGroups() throws BadLocationException {

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