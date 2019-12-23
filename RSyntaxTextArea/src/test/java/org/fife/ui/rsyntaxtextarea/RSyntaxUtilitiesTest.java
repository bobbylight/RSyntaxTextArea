/*
 * 12/10/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rtextarea.RTextScrollPane;
import org.junit.Assert;
import org.junit.Test;

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
public class RSyntaxUtilitiesTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testEscapeForHtml_nullInput() {
		Assert.assertNull(RSyntaxUtilities.escapeForHtml(null, "<br>", true));
	}


	@Test
	public void testEscapeForHtml_nullNewlineReplacement() {
		Assert.assertEquals("", RSyntaxUtilities.escapeForHtml("\n", null, true));
	}


	@Test
	public void testEscapeForHtml_happyPath() {
		Assert.assertEquals("hello", RSyntaxUtilities.escapeForHtml("hello", "<br>", true));
		Assert.assertEquals("2 &lt; 4", RSyntaxUtilities.escapeForHtml("2 < 4", "<br>", true));
	}


	@Test
	public void testEscapeForHtml_problemChars() {
		Assert.assertEquals(" <br>&amp;   &lt;&gt;&#39;&#34;&#47;",
				RSyntaxUtilities.escapeForHtml(" \n&\t<>'\"/", "<br>", true));
	}


	@Test
	public void testEscapeForHtml_multipleSpaces_inPreBlock() {
		Assert.assertEquals("   ",
				RSyntaxUtilities.escapeForHtml("   ", "<br>", true));
	}


	@Test
	public void testEscapeForHtml_multipleSpaces_notInPreBlock() {
		Assert.assertEquals(" &nbsp;&nbsp;",
				RSyntaxUtilities.escapeForHtml("   ", "<br>", false));
	}


	@Test
	public void testGetFoldedLineBottomColor_gutterFound() {
		RSyntaxTextArea textArea = createTextArea();
		RTextScrollPane sp = new RTextScrollPane(textArea);
		Assert.assertEquals(sp.getGutter().getFoldIndicatorForeground(),
			RSyntaxUtilities.getFoldedLineBottomColor(textArea));
	}


	@Test
	public void testGetFoldedLineBottomColor_noGutter() {
		RSyntaxTextArea textArea = createTextArea();
		Assert.assertEquals(Color.GRAY, RSyntaxUtilities.getFoldedLineBottomColor(textArea));
	}


	@Test
	public void testGetGutter_found() {
		RSyntaxTextArea textArea = createTextArea();
		RTextScrollPane sp = new RTextScrollPane(textArea);
		Assert.assertEquals(sp.getGutter(), RSyntaxUtilities.getGutter(textArea));
	}


	@Test
	public void testGetGutter_notFound() {
		RSyntaxTextArea textArea = createTextArea();
		Assert.assertNull(RSyntaxUtilities.getGutter(textArea));
	}


	@Test
	public void testGetHyperlinkForeground() {
		Assert.assertNotNull(RSyntaxUtilities.getHyperlinkForeground());
	}


	@Test
	public void testGetLeadingWhitespace_string_none() {
		Assert.assertEquals("", RSyntaxUtilities.getLeadingWhitespace("none"));
	}


	@Test
	public void testGetLeadingWhitespace_string_spaces() {
		Assert.assertEquals("  ", RSyntaxUtilities.getLeadingWhitespace("  two"));
	}


	@Test
	public void testGetLeadingWhitespace_string_tabs() {
		Assert.assertEquals("\t\t", RSyntaxUtilities.getLeadingWhitespace("\t\ttwo"));
	}


	@Test
	public void testGetLeadingWhitespace_string_spacesAndTabs() {
		Assert.assertEquals(" \t \t", RSyntaxUtilities.getLeadingWhitespace(" \t \tfour"));
	}


	@Test
	public void testGetLeadingWhitespace_document_spacesAndTabs() throws BadLocationException {
		RSyntaxTextArea textArea = createTextArea(" \t \tfour");
		Document doc = textArea.getDocument();
		Assert.assertEquals(" \t \t", RSyntaxUtilities.getLeadingWhitespace(doc, 0));
	}


	@Test
	public void testGetNextImportantToken_skipWhitespaceOnSameLine() {

		RSyntaxTextArea textArea = createTextArea("line one\nline two\n line 3");
		Token t = textArea.getTokenListForLine(0);

		// First token is important
		char[] chars = "line".toCharArray();
		Token expected = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.IDENTIFIER, 0);
		Token actual = RSyntaxUtilities.getNextImportantToken(t, textArea, 0);
		Assert.assertEquals(expected, actual);

		// Next token is space, so next "important" token is the following one
		t = t.getNextToken(); // space character
		chars = "one".toCharArray();
		expected = new TokenImpl(chars, 0, chars.length - 1, 5, TokenTypes.IDENTIFIER, 0);
		actual = RSyntaxUtilities.getNextImportantToken(t, textArea, 0);
		Assert.assertEquals(expected, actual);
	}


	@Test
	public void testGetNextImportantToken_goToNextLine() {

		RSyntaxTextArea textArea = createTextArea("line one   \nline two\n line 3");
		Token t = textArea.getTokenListForLine(0);

		// Get token "   "
		t = t.getNextToken().getNextToken().getNextToken();

		// Next token is "important", but on the next line
		char[] chars = "line".toCharArray();
		Token expected = new TokenImpl(chars, 0, chars.length - 1, 12, TokenTypes.IDENTIFIER, 0);
		Token actual = RSyntaxUtilities.getNextImportantToken(t, textArea, 0);
		Assert.assertEquals(expected, actual);
	}


	@Test
	public void testGetNextVisualPositionFrom_right_sameLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Rectangle bounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea).getView(0);

		int nextPos = RSyntaxUtilities.getNextVisualPositionFrom(0, Position.Bias.Forward,
			bounds, SwingConstants.EAST, new Position.Bias[1], view);
		Assert.assertEquals(1, nextPos);
	}


	@Test
	public void testGetNextVisualPositionFrom_right_nextLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Rectangle bounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea).getView(0);

		int firstLineEnd = textArea.getLineEndOffset(0) - 1;

		int nextPos = RSyntaxUtilities.getNextVisualPositionFrom(firstLineEnd, Position.Bias.Forward,
			bounds, SwingConstants.EAST, new Position.Bias[1], view);
		Assert.assertEquals(firstLineEnd + 1, nextPos);
	}


	@Test
	public void testGetNextVisualPositionFrom_left_sameLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Rectangle bounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea).getView(0);

		int nextPos = RSyntaxUtilities.getNextVisualPositionFrom(1, Position.Bias.Forward,
			bounds, SwingConstants.WEST, new Position.Bias[1], view);
		Assert.assertEquals(0, nextPos);
	}


	@Test
	public void testGetNextVisualPositionFrom_left_prevLine() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Rectangle bounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea).getView(0);

		int secondLineStart = textArea.getLineStartOffset(1);

		int nextPos = RSyntaxUtilities.getNextVisualPositionFrom(secondLineStart, Position.Bias.Forward,
			bounds, SwingConstants.WEST, new Position.Bias[1], view);
		Assert.assertEquals(secondLineStart - 1, nextPos);
	}


	@Test
	public void testGetNextVisualPositionFrom_north() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Rectangle bounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea).getView(0);

		int secondLineStart = textArea.getLineStartOffset(1);

		int nextPos = RSyntaxUtilities.getNextVisualPositionFrom(secondLineStart, Position.Bias.Forward,
			bounds, SwingConstants.NORTH, new Position.Bias[1], view);
		Assert.assertEquals(0, nextPos);
	}


	@Test
	public void testGetNextVisualPositionFrom_south() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		Rectangle bounds = textArea.getBounds();
		View view = textArea.getUI().getRootView(textArea).getView(0);

		int secondLineStart = textArea.getLineStartOffset(1);

		int nextPos = RSyntaxUtilities.getNextVisualPositionFrom(0, Position.Bias.Forward,
			bounds, SwingConstants.SOUTH, new Position.Bias[1], view);
		Assert.assertEquals(secondLineStart, nextPos);
	}


	@Test
	public void testGetPreviousImportantTokenFromOffs_skipWhitespaceOnSameLine() {

		RSyntaxTextArea textArea = createTextArea("line one");
		RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();

		// Previous token is space, so previous "important" token is "line"
		char[] chars = "line".toCharArray();
		Token expected = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.IDENTIFIER, 0);
		Token actual = RSyntaxUtilities.getPreviousImportantTokenFromOffs(doc, 5);
		Assert.assertEquals(expected, actual);
	}


	@Test
	public void testGetTokenAtOffset_textArea() {

		RSyntaxTextArea textArea = createTextArea("line one");

		char[] chars = "line".toCharArray();
		Token expected = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.IDENTIFIER, 0);
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(textArea, 0));
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(textArea, 1));
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(textArea, 2));
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(textArea, 3));

		chars = " ".toCharArray();
		expected = new TokenImpl(chars, 0, 0, 4, TokenTypes.WHITESPACE, 0);
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(textArea, 4));
	}


	@Test
	public void testGetTokenAtOffset_document() {

		RSyntaxTextArea textArea = createTextArea("line one");
		RSyntaxDocument document = (RSyntaxDocument)textArea.getDocument();

		char[] chars = "line".toCharArray();
		Token expected = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.IDENTIFIER, 0);
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(document, 0));
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(document, 1));
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(document, 2));
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(document, 3));

		chars = " ".toCharArray();
		expected = new TokenImpl(chars, 0, 0, 4, TokenTypes.WHITESPACE, 0);
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(document, 4));
	}


	@Test
	public void testGetTokenAtOffset_tokenList() {

		RSyntaxTextArea textArea = createTextArea("line one");
		Token t = textArea.getTokenListForLine(0);

		char[] chars = "line".toCharArray();
		Token expected = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.IDENTIFIER, 0);
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(t, 0));
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(t, 1));
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(t, 2));
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(t, 3));

		chars = " ".toCharArray();
		expected = new TokenImpl(chars, 0, 0, 4, TokenTypes.WHITESPACE, 0);
		Assert.assertEquals(expected, RSyntaxUtilities.getTokenAtOffset(t, 4));
	}


	@Test
	public void testGetWordEnd() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea("line one");
		Assert.assertEquals(4, RSyntaxUtilities.getWordEnd(textArea, 1));
	}


	@Test
	public void testGetWordStart() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea("line one");
		Assert.assertEquals(0, RSyntaxUtilities.getWordStart(textArea, 1));
	}


	@Test
	public void testIsBracket() {
		Assert.assertTrue(RSyntaxUtilities.isBracket('['));
		Assert.assertTrue(RSyntaxUtilities.isBracket(']'));
		Assert.assertTrue(RSyntaxUtilities.isBracket('{'));
		Assert.assertTrue(RSyntaxUtilities.isBracket('}'));
		Assert.assertTrue(RSyntaxUtilities.isBracket('('));
		Assert.assertTrue(RSyntaxUtilities.isBracket(')'));
		Assert.assertFalse(RSyntaxUtilities.isBracket('x'));
	}


	@Test
	public void testPossiblyRepaintGutter() {
		RSyntaxTextArea textArea = createTextArea();
		new RTextScrollPane(textArea);
		RSyntaxUtilities.possiblyRepaintGutter(textArea);
	}


	@Test
	public void testWildcardToPattern() {

		Assert.assertEquals(".*",
			RSyntaxUtilities.wildcardToPattern("*", false, false).pattern());

		Assert.assertEquals(".foo.",
			RSyntaxUtilities.wildcardToPattern("?foo?", false, false).pattern());

		Assert.assertEquals("foobar\\.\\$tmp",
			RSyntaxUtilities.wildcardToPattern("foobar.$tmp", false, false).pattern());
	}
}
