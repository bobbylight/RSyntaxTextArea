/*
 * 05/12/2010
 *
 * SearchEngineTest.java - Test cases for SearchEngine.java
 * Copyright (C) 2010 Robert Futrell
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.text.BadLocationException;

import junit.framework.TestCase;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.SearchEngine;

/**
 * Some very basic unit tests for the {@link SearchEngine} used by
 * <tt>RTextArea</tt>/<tt>RSyntaxTextArea</tt>.
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
	 * Tests <tt>SearchEngine.find()</tt> when searching backward.
	 */
	public void testSearchEngineFindBackward() throws BadLocationException {

		textArea.setText(text);

		int end = text.length();
		boolean forward = false;
		boolean matchCase = false;
		boolean wholeWord = false;
		boolean regex = false;

		// Search for "chuck", ignoring case.
		String toFind = "chuck";
		textArea.setCaretPosition(end);
		boolean found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 60, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 48, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 32, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 26, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for "Chuck", matching case.
		toFind = "Chuck";
		matchCase = true;
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("Chuck", 26, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for "chuck", ignoring case, whole word
		toFind = "chuck";
		matchCase = false;
		wholeWord = true;
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 60, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 32, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for "wood", matching case, whole word
		toFind = "wood";
		matchCase = true;
		wholeWord = true;
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("wood", 9, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for ".ould", regex, ignoring case
		toFind = ".ould";
		matchCase = false;
		wholeWord = false;
		regex = true;
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("could", 54, true);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("wOuld", 14, true);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for ".ould", regex, matching case
		toFind = ".ould";
		matchCase = true;
		wholeWord = false;
		regex = true;
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("could", 54, true);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for "[cd]huck", regex, ignoring case, whole word
		toFind = "[cd]hUCk";
		matchCase = false;
		wholeWord = true;
		regex = true;
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 60, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 32, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for "[cd]huck", regex, matching case, whole word
		toFind = "[cd]huck";
		matchCase = true;
		wholeWord = true;
		regex = true;
		textArea.setCaretPosition(end);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 60, true);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

	}


	/**
	 * Tests <tt>SearchEngine.find()</tt> when searching forward.
	 */
	public void testSearchEngineFindForward() throws BadLocationException {

		textArea.setText(text);

		boolean forward = true;
		boolean matchCase = false;
		boolean wholeWord = false;
		boolean regex = false;

		// Search for "chuck", ignoring case.
		String toFind = "chuck";
		boolean found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 26, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 32, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 48, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 60, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for "Chuck", matching case.
		toFind = "Chuck";
		matchCase = true;
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("Chuck", 26, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for "chuck", ignoring case, whole word
		toFind = "chuck";
		matchCase = false;
		wholeWord = true;
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 32, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 60, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for "wood", matching case, whole word
		toFind = "wood";
		matchCase = true;
		wholeWord = true;
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("wood", 9, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for ".ould", regex, ignoring case
		toFind = ".ould";
		matchCase = false;
		wholeWord = false;
		regex = true;
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("wOuld", 14, true);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("could", 54, true);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for ".ould", regex, matching case
		toFind = ".ould";
		matchCase = true;
		wholeWord = false;
		regex = true;
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("could", 54, true);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for "[cd]huck", regex, ignoring case, whole word
		toFind = "[cd]hUCk";
		matchCase = false;
		wholeWord = true;
		regex = true;
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 32, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 60, matchCase);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

		// Search for "[cd]huck", regex, matching case, whole word
		toFind = "[cd]huck";
		matchCase = true;
		wholeWord = true;
		regex = true;
		textArea.setCaretPosition(0);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(true, found);
		assertSelected("chuck", 60, true);
		found = SearchEngine.find(textArea, toFind, forward, matchCase,
				wholeWord, regex);
		assertEquals(false, found);

	}


	/**
	 * Tests <tt>SearchEngine.replace()</tt>.
	 *
	 * @param forward Whether to test searching forward or backward.
	 */
	private void testSearchEngineReplace(boolean forward)
										throws BadLocationException {

		String replaceWith = "FOOBAR";
		int offs = forward ? 0 : text.length();

		// Search for "chuck", ignoring case.
		String toFind = "chuck";
		textArea.setText(text);
		String expected = textArea.getText().replaceAll("(?i:" + toFind + ")", replaceWith);
		boolean matchCase = false;
		boolean wholeWord = false;
		boolean regex = false;
		textArea.setCaretPosition(offs);
		boolean found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(false, found);
		assertEquals(expected, textArea.getText());

		// Search for "chuck", matching case.
		toFind = "chuck";
		textArea.setText(text);
		expected = textArea.getText().replaceAll(toFind, replaceWith);
		matchCase = true;
		wholeWord = false;
		regex = false;
		textArea.setCaretPosition(offs);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(false, found);
		assertEquals(expected, textArea.getText());

		// Search for "chuck", ignoring case, whole word.
		toFind = "chuck";
		textArea.setText(text);
		expected = "How much wood wOuld a woodChuck FOOBAR, if a woodchuck could FOOBAR wOod?";
		matchCase = false;
		wholeWord = true;
		regex = false;
		textArea.setCaretPosition(offs);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(false, found);
		assertEquals(expected, textArea.getText());

		// Search for "chuck", matching case, whole word.
		toFind = "chuck";
		textArea.setText(text);
		expected = "How much wood wOuld a woodChuck chUck, if a woodchuck could FOOBAR wOod?";
		matchCase = true;
		wholeWord = true;
		regex = false;
		textArea.setCaretPosition(offs);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(false, found);
		assertEquals(expected, textArea.getText());

		// Search for ".huck", regex, ignoring case
		toFind = ".huck";
		textArea.setText(text);
		expected = textArea.getText().replaceAll("(?i:" + toFind + ")", replaceWith);
		matchCase = false;
		wholeWord = false;
		regex = true;
		textArea.setCaretPosition(offs);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(false, found);
		assertEquals(expected, textArea.getText());

		// Search for ".huck", regex, matching case
		toFind = ".huck";
		textArea.setText(text);
		expected = textArea.getText().replaceAll(toFind, replaceWith);
		matchCase = true;
		wholeWord = false;
		regex = true;
		textArea.setCaretPosition(offs);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(false, found);
		assertEquals(expected, textArea.getText());

		// Search for "[cd]huck", regex, ignoring case, whole word
		toFind = "[cd]huck";
		textArea.setText(text);
		expected = "How much wood wOuld a woodChuck FOOBAR, if a woodchuck could FOOBAR wOod?";
		matchCase = false;
		wholeWord = true;
		regex = true;
		textArea.setCaretPosition(offs);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(false, found);
		assertEquals(expected, textArea.getText());

		// Search for "[cd]hUck", regex, matching case, whole word
		toFind = "[cd]hUck";
		textArea.setText(text);
		expected = "How much wood wOuld a woodChuck FOOBAR, if a woodchuck could chuck wOod?";
		matchCase = true;
		wholeWord = false;
		regex = true;
		textArea.setCaretPosition(offs);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(true, found);
		found = SearchEngine.replace(textArea, toFind, replaceWith,
				forward, matchCase, wholeWord, regex);
		assertEquals(false, found);
		assertEquals(expected, textArea.getText());

	}


	/**
	 * Tests <tt>SearchEngine.replace()</tt> when searching backward.
	 */
	public void testSearchEngineReplaceBackward() throws BadLocationException {
		testSearchEngineReplace(false);
	}


	/**
	 * Tests <tt>SearchEngine.replace()</tt> when searching forward.
	 */
	public void testSearchEngineReplaceForward() throws BadLocationException {
		testSearchEngineReplace(true);
	}


	/**
	 * Tests <tt>SearchEngine.replaceAll()</tt>.
	 */
	public void testSearchEngineReplaceAll() throws BadLocationException {

		String replaceWith = "FOOBAR";

		// Replace "chuck", ignoring case.
		String toFind = "chuck";
		textArea.setText(text);
		String expected = textArea.getText().replaceAll("(?i:" + toFind + ")", replaceWith);
		boolean matchCase = false;
		boolean wholeWord = false;
		boolean regex = false;
		int count = SearchEngine.replaceAll(textArea, toFind, replaceWith,
								matchCase, wholeWord, regex);
		assertEquals(4, count);
		assertEquals(expected, textArea.getText());

		// Replace "wood", matching case.
		toFind = "wood";
		textArea.setText(text);
		expected = textArea.getText().replaceAll(toFind, replaceWith);
		matchCase = true;
		wholeWord = false;
		regex = false;
		count = SearchEngine.replaceAll(textArea, toFind, replaceWith,
								matchCase, wholeWord, regex);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// Replace "wood", ignoring case, whole word.
		toFind = "wood";
		textArea.setText(text);
		expected = "How much FOOBAR wOuld a woodChuck chUck, if a woodchuck could chuck FOOBAR?";
		matchCase = false;
		wholeWord = true;
		regex = false;
		count = SearchEngine.replaceAll(textArea, toFind, replaceWith,
								matchCase, wholeWord, regex);
		assertEquals(2, count);
		assertEquals(expected, textArea.getText());

		// Replace "chUck", matching case, whole word.
		toFind = "chUck";
		textArea.setText(text);
		expected = "How much wood wOuld a woodChuck FOOBAR, if a woodchuck could chuck wOod?";
		matchCase = true;
		wholeWord = true;
		regex = false;
		count = SearchEngine.replaceAll(textArea, toFind, replaceWith,
								matchCase, wholeWord, regex);
		assertEquals(1, count);
		assertEquals(expected, textArea.getText());

		// Replace "wo(?:o|ul)d", regex, ignoring case.
		toFind = "wo(?:o|ul)d";
		textArea.setText(text);
		expected = textArea.getText().replaceAll("(?i:" + toFind + ")", replaceWith);
		matchCase = false;
		wholeWord = false;
		regex = true;
		count = SearchEngine.replaceAll(textArea, toFind, replaceWith,
								matchCase, wholeWord, regex);
		assertEquals(5, count);
		assertEquals(expected, textArea.getText());

		// Replace "wo(?:o|ul)d", regex, matching case.
		toFind = "wo(?:o|ul)d";
		textArea.setText(text);
		expected = textArea.getText().replaceAll(toFind, replaceWith);
		matchCase = true;
		wholeWord = false;
		regex = true;
		count = SearchEngine.replaceAll(textArea, toFind, replaceWith,
								matchCase, wholeWord, regex);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// Replace "wo(?:o|ul)d", regex, ignoring case, whole word
		toFind = "wo(?:o|ul)d";
		textArea.setText(text);
		expected = "How much FOOBAR FOOBAR a woodChuck chUck, if a woodchuck could chuck FOOBAR?";
		matchCase = false;
		wholeWord = true;
		regex = true;
		count = SearchEngine.replaceAll(textArea, toFind, replaceWith,
								matchCase, wholeWord, regex);
		assertEquals(3, count);
		assertEquals(expected, textArea.getText());

		// Replace "wO(?:o|ul)d", regex, matching case, whole word
		toFind = "wO(?:o|ul)d";
		textArea.setText(text);
		expected = "How much wood FOOBAR a woodChuck chUck, if a woodchuck could chuck FOOBAR?";
		matchCase = true;
		wholeWord = true;
		regex = true;
		count = SearchEngine.replaceAll(textArea, toFind, replaceWith,
								matchCase, wholeWord, regex);
		assertEquals(2, count);
		assertEquals(expected, textArea.getText());

	}


}