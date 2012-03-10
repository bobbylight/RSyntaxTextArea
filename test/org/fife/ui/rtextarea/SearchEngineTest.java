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


	/**
	 * {@inheritDoc}
	 */
	protected void setUp() throws Exception {

		// setUp() is called once per test, each with a new instantiation of
		// SearchEngineTest, so check a static variable to ensure that
		// initialization is only done once.

		if (text == null || text.length() <= 0) {

			StringBuffer sb = new StringBuffer();
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

		textArea.setText(text);

		int end = text.length();
		SearchContext context = new SearchContext();
		context.setSearchForward(false);

		// Search for "chuck", ignoring case.
		context.setSearchFor("chuck");
		context.setMatchCase(false);
		textArea.setCaretPosition(end);
		boolean found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 60, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 48, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 32, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 26, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for "Chuck", matching case.
		context.setSearchFor("Chuck");
		context.setMatchCase(true);
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("Chuck", 26, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for "chuck", ignoring case, whole word
		context.setSearchFor("Chuck");
		context.setMatchCase(false);
		context.setWholeWord(true);
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 60, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 32, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for "wood", matching case, whole word
		context.setSearchFor("wood");
		context.setMatchCase(true);
		context.setWholeWord(true);
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("wood", 9, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for ".ould", regex, ignoring case
		context.setSearchFor(".ould");
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("could", 54, true);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("wOuld", 14, true);
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for ".ould", regex, matching case
		context.setSearchFor(".ould");
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("could", 54, true);
		found = SearchEngine.find(textArea,context);
		assertEquals(false, found);

		// Search for "[cd]huck", regex, ignoring case, whole word
		context.setSearchFor("[cd]hUCk");
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 60, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 32, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for "[cd]huck", regex, matching case, whole word
		context.setSearchFor("[cd]huck");
		context.setMatchCase(true);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 60, true);
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

	}


	/**
	 * Tests <code>SearchEngine.find()</code> when searching forward.
	 */
	public void testSearchEngineFindForward() throws BadLocationException {

		textArea.setText(text);

		SearchContext context = new SearchContext();

		// Search for "chuck", ignoring case.
		context.setSearchFor("chuck");
		boolean found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 26, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 32, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 48, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 60, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for "Chuck", matching case.
		context.setSearchFor("Chuck");
		context.setMatchCase(true);
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("Chuck", 26, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for "chuck", ignoring case, whole word
		context.setSearchFor("chuck");
		context.setMatchCase(false);
		context.setWholeWord(true);
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 32, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 60, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for "wood", matching case, whole word
		context.setSearchFor("wood");
		context.setMatchCase(true);
		context.setWholeWord(true);
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("wood", 9, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for ".ould", regex, ignoring case
		context.setSearchFor(".ould");
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("wOuld", 14, true);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("could", 54, true);
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for ".ould", regex, matching case
		context.setSearchFor(".ould");
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("could", 54, true);
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for "[cd]huck", regex, ignoring case, whole word
		context.setSearchFor("[cd]hUCk");
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 32, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 60, context.getMatchCase());
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

		// Search for "[cd]huck", regex, matching case, whole word
		context.setSearchFor("[cd]huck");
		context.setMatchCase(true);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, context);
		assertEquals(true, found);
		assertSelected("chuck", 60, true);
		found = SearchEngine.find(textArea, context);
		assertEquals(false, found);

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
		boolean found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
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
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
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
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
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
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
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
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
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
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
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
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
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
		found = SearchEngine.replace(textArea, context);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, context);
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
		int count = SearchEngine.replaceAll(textArea, context);
		assertEquals(4, count);
		assertEquals(expected, textArea.getText());

		// Replace "wood", matching case.
		context.setSearchFor("wood");
		textArea.setText(text);
		expected = textArea.getText().replaceAll(context.getSearchFor(), context.getReplaceWith());
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(false);
		count = SearchEngine.replaceAll(textArea, context);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// Replace "wood", ignoring case, whole word.
		context.setSearchFor("wood");
		textArea.setText(text);
		expected = "How much FOOBAR wOuld a woodChuck chUck, if a woodchuck could chuck FOOBAR?";
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(false);
		count = SearchEngine.replaceAll(textArea, context);
		assertEquals(2, count);
		assertEquals(expected, textArea.getText());

		// Replace "chUck", matching case, whole word.
		context.setSearchFor("chUck");
		textArea.setText(text);
		expected = "How much wood wOuld a woodChuck FOOBAR, if a woodchuck could chuck wOod?";
		context.setMatchCase(true);
		context.setWholeWord(true);
		context.setRegularExpression(false);
		count = SearchEngine.replaceAll(textArea, context);
		assertEquals(1, count);
		assertEquals(expected, textArea.getText());

		// Replace "wo(?:o|ul)d", regex, ignoring case.
		context.setSearchFor("wo(?:o|ul)d");
		textArea.setText(text);
		expected = textArea.getText().replaceAll("(?i:" + context.getSearchFor() +  ")", context.getReplaceWith());
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		count = SearchEngine.replaceAll(textArea, context);
		assertEquals(5, count);
		assertEquals(expected, textArea.getText());

		// Replace "wo(?:o|ul)d", regex, matching case.
		context.setSearchFor("wo(?:o|ul)d");
		textArea.setText(text);
		expected = textArea.getText().replaceAll(context.getSearchFor(), context.getReplaceWith());
		context.setMatchCase(true);
		context.setWholeWord(false);
		context.setRegularExpression(true);
		count = SearchEngine.replaceAll(textArea, context);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// Replace "wo(?:o|ul)d", regex, ignoring case, whole word
		context.setSearchFor("wo(?:o|ul)d");
		textArea.setText(text);
		expected = "How much FOOBAR FOOBAR a woodChuck chUck, if a woodchuck could chuck FOOBAR?";
		context.setMatchCase(false);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		count = SearchEngine.replaceAll(textArea, context);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// Replace "wO(?:o|ul)d", regex, matching case, whole word
		context.setSearchFor("wO(?:o|ul)d");
		textArea.setText(text);
		expected = "How much wood FOOBAR a woodChuck chUck, if a woodchuck could chuck FOOBAR?";
		context.setMatchCase(true);
		context.setWholeWord(true);
		context.setRegularExpression(true);
		count = SearchEngine.replaceAll(textArea, context);
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
		int count = SearchEngine.replaceAll(textArea, context);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// Multiple captured groups.
		context.setSearchFor("(\\d)(\\d+)[kM]");
		textArea.setText("152k 5271143M 3985k");
		expected = "1.52 5.271143 3.985";
		context.setMatchCase(false);
		context.setWholeWord(false);
		context.setReplaceWith("$1.$2");
		count = SearchEngine.replaceAll(textArea, context);
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
		count = SearchEngine.replaceAll(textArea, context);
		assertEquals(0, count);
		assertEquals(expected, textArea.getText());
		assertEquals(8, textArea.getCaretPosition()); // Caret doesn't move

	}


}