/*
 * 12/10/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rtextarea.RTextScrollPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Position;
import javax.swing.text.View;
import java.awt.*;


/**
 * Unit tests for the {@link RSyntaxUtilities} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RSyntaxUtilitiesTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testGetFoldedLineBottomColor_gutterFound() {
		RSyntaxTextArea textArea = createTextArea();
		RTextScrollPane sp = new RTextScrollPane(textArea);
		Assertions.assertEquals(sp.getGutter().getFoldIndicatorForeground(),
			RSyntaxUtilities.getFoldedLineBottomColor(textArea));
	}


	@Test
	void testGetFoldedLineBottomColor_noGutter() {
		RSyntaxTextArea textArea = createTextArea();
		Assertions.assertEquals(Color.GRAY, RSyntaxUtilities.getFoldedLineBottomColor(textArea));
	}


	@Test
	void testGetGutter_found() {
		RSyntaxTextArea textArea = createTextArea();
		RTextScrollPane sp = new RTextScrollPane(textArea);
		Assertions.assertEquals(sp.getGutter(), RSyntaxUtilities.getGutter(textArea));
	}


	@Test
	void testGetGutter_notFound() {
		RSyntaxTextArea textArea = createTextArea();
		Assertions.assertNull(RSyntaxUtilities.getGutter(textArea));
	}


	@Test
	void testGetHyperlinkForeground() {
		Assertions.assertNotNull(RSyntaxUtilities.getHyperlinkForeground());
	}


	@Test
	void testGetLeadingWhitespace_string_none() {
		Assertions.assertEquals("", RSyntaxUtilities.getLeadingWhitespace("none"));
	}


	@Test
	void testGetLeadingWhitespace_string_spaces() {
		Assertions.assertEquals("  ", RSyntaxUtilities.getLeadingWhitespace("  two"));
	}


	@Test
	void testGetLeadingWhitespace_string_tabs() {
		Assertions.assertEquals("\t\t", RSyntaxUtilities.getLeadingWhitespace("\t\ttwo"));
	}


	@Test
	void testGetLeadingWhitespace_string_spacesAndTabs() {
		Assertions.assertEquals(" \t \t", RSyntaxUtilities.getLeadingWhitespace(" \t \tfour"));
	}


	@Test
	void testGetLeadingWhitespace_document_spacesAndTabs() throws BadLocationException {
		RSyntaxTextArea textArea = createTextArea(" \t \tfour");
		Document doc = textArea.getDocument();
		Assertions.assertEquals(" \t \t", RSyntaxUtilities.getLeadingWhitespace(doc, 0));
	}


	@Test
	void testGetNextImportantToken_skipWhitespaceOnSameLine() {

		RSyntaxTextArea textArea = createTextArea("line one\nline two\n line 3");
		Token t = textArea.getTokenListForLine(0);

		// First token is important
		char[] chars = "line".toCharArray();
		Token expected = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.IDENTIFIER, 0);
		Token actual = RSyntaxUtilities.getNextImportantToken(t, textArea, 0);
		Assertions.assertEquals(expected, actual);

		// Next token is space, so next "important" token is the following one
		t = t.getNextToken(); // space character
		chars = "one".toCharArray();
		expected = new TokenImpl(chars, 0, chars.length - 1, 5, TokenTypes.IDENTIFIER, 0);
		actual = RSyntaxUtilities.getNextImportantToken(t, textArea, 0);
		Assertions.assertEquals(expected, actual);
	}


	@Test
	void testGetNextImportantToken_goToNextLine() {

		RSyntaxTextArea textArea = createTextArea("line one   \nline two\n line 3");
		Token t = textArea.getTokenListForLine(0);

		// Get token "   "
		t = t.getNextToken().getNextToken().getNextToken();

		// Next token is "important", but on the next line
		char[] chars = "line".toCharArray();
		Token expected = new TokenImpl(chars, 0, chars.length - 1, 12, TokenTypes.IDENTIFIER, 0);
		Token actual = RSyntaxUtilities.getNextImportantToken(t, textArea, 0);
		Assertions.assertEquals(expected, actual);
	}


	@Test
	void testGetNextVisualPositionFrom_right_sameLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Rectangle bounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea).getView(0);

		int nextPos = RSyntaxUtilities.getNextVisualPositionFrom(0, Position.Bias.Forward,
			bounds, SwingConstants.EAST, new Position.Bias[1], view);
		Assertions.assertEquals(1, nextPos);
	}


	@Test
	void testGetNextVisualPositionFrom_right_nextLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Rectangle bounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea).getView(0);

		int firstLineEnd = textArea.getLineEndOffset(0) - 1;

		int nextPos = RSyntaxUtilities.getNextVisualPositionFrom(firstLineEnd, Position.Bias.Forward,
			bounds, SwingConstants.EAST, new Position.Bias[1], view);
		Assertions.assertEquals(firstLineEnd + 1, nextPos);
	}


	@Test
	void testGetNextVisualPositionFrom_left_sameLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Rectangle bounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea).getView(0);

		int nextPos = RSyntaxUtilities.getNextVisualPositionFrom(1, Position.Bias.Forward,
			bounds, SwingConstants.WEST, new Position.Bias[1], view);
		Assertions.assertEquals(0, nextPos);
	}


	@Test
	void testGetNextVisualPositionFrom_left_prevLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Rectangle bounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea).getView(0);

		int secondLineStart = textArea.getLineStartOffset(1);

		int nextPos = RSyntaxUtilities.getNextVisualPositionFrom(secondLineStart, Position.Bias.Forward,
			bounds, SwingConstants.WEST, new Position.Bias[1], view);
		Assertions.assertEquals(secondLineStart - 1, nextPos);
	}


	@Test
	void testGetNextVisualPositionFrom_north() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Rectangle bounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea).getView(0);

		int secondLineStart = textArea.getLineStartOffset(1);

		int nextPos = RSyntaxUtilities.getNextVisualPositionFrom(secondLineStart, Position.Bias.Forward,
			bounds, SwingConstants.NORTH, new Position.Bias[1], view);
		Assertions.assertEquals(0, nextPos);
	}


	@Test
	void testGetNextVisualPositionFrom_south() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Rectangle bounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea).getView(0);

		int secondLineStart = textArea.getLineStartOffset(1);

		int nextPos = RSyntaxUtilities.getNextVisualPositionFrom(0, Position.Bias.Forward,
			bounds, SwingConstants.SOUTH, new Position.Bias[1], view);
		Assertions.assertEquals(secondLineStart, nextPos);
	}


	@Test
	void testGetPreviousImportantTokenFromOffs_skipWhitespaceOnSameLine() {

		RSyntaxTextArea textArea = createTextArea("line one");
		RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();

		// Previous token is space, so previous "important" token is "line"
		char[] chars = "line".toCharArray();
		Token expected = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.IDENTIFIER, 0);
		Token actual = RSyntaxUtilities.getPreviousImportantTokenFromOffs(doc, 5);
		Assertions.assertEquals(expected, actual);
	}


	@Test
	void testGetTokenAtOffset_textArea() {

		RSyntaxTextArea textArea = createTextArea("line one");

		char[] chars = "line".toCharArray();
		Token expected = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.IDENTIFIER, 0);
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(textArea, 0));
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(textArea, 1));
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(textArea, 2));
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(textArea, 3));

		chars = " ".toCharArray();
		expected = new TokenImpl(chars, 0, 0, 4, TokenTypes.WHITESPACE, 0);
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(textArea, 4));
	}


	@Test
	void testGetTokenAtOffset_document() {

		RSyntaxTextArea textArea = createTextArea("line one");
		RSyntaxDocument document = (RSyntaxDocument)textArea.getDocument();

		char[] chars = "line".toCharArray();
		Token expected = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.IDENTIFIER, 0);
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(document, 0));
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(document, 1));
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(document, 2));
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(document, 3));

		chars = " ".toCharArray();
		expected = new TokenImpl(chars, 0, 0, 4, TokenTypes.WHITESPACE, 0);
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(document, 4));
	}


	@Test
	void testGetTokenAtOffset_tokenList() {

		RSyntaxTextArea textArea = createTextArea("line one");
		Token t = textArea.getTokenListForLine(0);

		char[] chars = "line".toCharArray();
		Token expected = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.IDENTIFIER, 0);
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(t, 0));
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(t, 1));
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(t, 2));
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(t, 3));

		chars = " ".toCharArray();
		expected = new TokenImpl(chars, 0, 0, 4, TokenTypes.WHITESPACE, 0);
		Assertions.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(t, 4));
	}


	@Test
	void testGetWordEnd() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea("line one");
		Assertions.assertEquals(4, RSyntaxUtilities.getWordEnd(textArea, 1));
	}


	@Test
	void testGetWordStart() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea("line one");
		Assertions.assertEquals(0, RSyntaxUtilities.getWordStart(textArea, 1));
	}


	@Test
	void testIsBracket() {
		Assertions.assertTrue(RSyntaxUtilities.isBracket('['));
		Assertions.assertTrue(RSyntaxUtilities.isBracket(']'));
		Assertions.assertTrue(RSyntaxUtilities.isBracket('{'));
		Assertions.assertTrue(RSyntaxUtilities.isBracket('}'));
		Assertions.assertTrue(RSyntaxUtilities.isBracket('('));
		Assertions.assertTrue(RSyntaxUtilities.isBracket(')'));
		Assertions.assertFalse(RSyntaxUtilities.isBracket('x'));
	}


	@Test
	void testIsOSCaseSensitive() {
		int os = RSyntaxUtilities.getOS();
		boolean expected = !(os == RSyntaxUtilities.OS_MAC_OSX ||
			os == RSyntaxUtilities.OS_WINDOWS);
		Assertions.assertEquals(expected, RSyntaxUtilities.isOsCaseSensitive());
	}


	@Test
	void testPossiblyRepaintGutter() {
		RSyntaxTextArea textArea = createTextArea();
		new RTextScrollPane(textArea);
		RSyntaxUtilities.possiblyRepaintGutter(textArea);
	}


	@Test
	void testWildcardToPattern() {

		Assertions.assertEquals(".*",
			RSyntaxUtilities.wildcardToPattern("*", false, false).pattern());

		Assertions.assertEquals(".foo.",
			RSyntaxUtilities.wildcardToPattern("?foo?", false, false).pattern());

		Assertions.assertEquals("foobar\\.\\$tmp",
			RSyntaxUtilities.wildcardToPattern("foobar.$tmp", false, false).pattern());
	}
}
