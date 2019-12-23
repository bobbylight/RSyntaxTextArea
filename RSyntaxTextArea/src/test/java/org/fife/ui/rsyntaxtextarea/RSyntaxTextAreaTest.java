/*
 * 03/14/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit tests for the {@link RSyntaxTextArea} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testAnimateBracketMatching() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getAnimateBracketMatching());
		textArea.setAnimateBracketMatching(false);
		Assert.assertFalse(textArea.getAnimateBracketMatching());
	}


	@Test
	public void testAntiAliasingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getAntiAliasingEnabled());
		textArea.setAntiAliasingEnabled(false);
		Assert.assertFalse(textArea.getAntiAliasingEnabled());
	}


	@Test
	public void testAutoIndentEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.isAutoIndentEnabled());
		textArea.setAutoIndentEnabled(false);
		Assert.assertFalse(textArea.isAutoIndentEnabled());
	}


	@Test
	public void testBracketMatchingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.isBracketMatchingEnabled());
		textArea.setBracketMatchingEnabled(false);
		Assert.assertFalse(textArea.isBracketMatchingEnabled());
	}


	@Test
	public void testClearWhitespaceLinesEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.isClearWhitespaceLinesEnabled());
		textArea.setClearWhitespaceLinesEnabled(false);
		Assert.assertFalse(textArea.isClearWhitespaceLinesEnabled());
	}


	@Test
	public void testCloseCurlyBraces() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getCloseCurlyBraces());
		textArea.setCloseCurlyBraces(false);
		Assert.assertFalse(textArea.getCloseCurlyBraces());
	}


	@Test
	public void testCloseMarkupTags() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getCloseMarkupTags());
		textArea.setCloseMarkupTags(false);
		Assert.assertFalse(textArea.getCloseMarkupTags());
	}


	@Test
	public void testCodeFoldingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.isCodeFoldingEnabled());
		textArea.setCodeFoldingEnabled(true);
		Assert.assertTrue(textArea.isCodeFoldingEnabled());
	}


	@Test
	public void testCopyAsStyledText_zeroArg_html_happyPath() throws Exception {

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setText("public int getValue();");
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(textArea.getDocument().getLength());
		textArea.copyAsStyledText();

		String expectedPortion = "<span style=\"font-weight: bold;color: #0000ff;\">public</span>" +
			"<span style=\"color: #808080;\"> </span>" +
			"<span style=\"font-weight: bold;color: #008080;\">int</span>" +
			"<span style=\"color: #808080;\"> </span>" +
			"<span style=\"color: #000000;\">getValue</span>" +
			"<span style=\"color: #ff0000;\">(</span>" +
			"<span style=\"color: #ff0000;\">)</span>" +
			"<span style=\"color: #000000;\">;</span>";

		String clipboardContent = (String)textArea.getToolkit().getSystemClipboard().
			getData(DataFlavor.fragmentHtmlFlavor);
		Assert.assertTrue(clipboardContent.contains(expectedPortion));
	}


	@Test
	@Ignore("Fails in the travis-ci environment for some reason")
	public void testCopyAsStyledText_zeroArg_rtf_happyPath() throws Exception {

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setText("public int getValue();");
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(textArea.getDocument().getLength());
		textArea.copyAsStyledText();

		String expectedPortion =
			"{\\colortbl ;\\red0\\green0\\blue255;\\red0\\green128\\blue128;" +
				"\\red0\\green0\\blue0;\\red255\\green0\\blue0;\\red255\\green255\\blue255;}\n" +
			"\\cb5 \\f1\\fs20\\b\\cf1 public\\b0  \\b\\cf2 int\\b0  \\cf3 getValue\\cf4 ()\\cf3 ;\\line}";

		ByteArrayInputStream clipboardContent = (ByteArrayInputStream)textArea.getToolkit().getSystemClipboard().
			getData(new DataFlavor("text/rtf"));
		Scanner scanner = new Scanner(clipboardContent);
		scanner.useDelimiter("\\Z");
		String clipboardString = scanner.next();
		Assert.assertTrue("Unexpected clipboard contents: " + clipboardString,
			clipboardString.contains(expectedPortion));
	}


	@Test
	public void testCopyAsStyledText_zeroArg_string_happyPath() throws Exception {

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setText("public int getValue();");
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(textArea.getDocument().getLength());
		textArea.copyAsStyledText();

		String clipboardContent = (String)textArea.getToolkit().getSystemClipboard().
			getData(DataFlavor.stringFlavor);
		Assert.assertEquals(textArea.getText(), clipboardContent);
	}


	@Test
	public void testCopyAsStyledText_themeArg_string_happyPath() throws Exception {

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setText("public int getValue();");
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(textArea.getDocument().getLength());
		textArea.copyAsStyledText(new Theme(textArea));

		String clipboardContent = (String)textArea.getToolkit().getSystemClipboard().
			getData(DataFlavor.stringFlavor);
		Assert.assertEquals(textArea.getText(), clipboardContent);
	}


	@Test
	public void testCopyAsStyledText_nullThemeArg_string_happyPath() throws Exception {

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setText("public int getValue();");
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(textArea.getDocument().getLength());
		textArea.copyAsStyledText(null);

		String clipboardContent = (String)textArea.getToolkit().getSystemClipboard().
			getData(DataFlavor.stringFlavor);
		Assert.assertEquals(textArea.getText(), clipboardContent);
	}


	@Test
	public void testEOLMarkersVisible() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getEOLMarkersVisible());
		textArea.setEOLMarkersVisible(true);
		Assert.assertTrue(textArea.getEOLMarkersVisible());
	}


	@Test
	public void testGetSetRightHandSideCorrection() {
		RSyntaxTextArea textArea = createTextArea();
		Assert.assertEquals(0, textArea.getRightHandSideCorrection());
		textArea.setRightHandSideCorrection(5);
		Assert.assertEquals(5, textArea.getRightHandSideCorrection());
	}


	@Test
	public void testGetSetTemplatesEnabled() {
		Assert.assertFalse(RSyntaxTextArea.getTemplatesEnabled());
		try {
			RSyntaxTextArea.setTemplatesEnabled(true);
			Assert.assertTrue(RSyntaxTextArea.getTemplatesEnabled());
		} finally {
			RSyntaxTextArea.setTemplatesEnabled(false);
		}
	}


	@Test
	public void testFractionalFontMetricsEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getFractionalFontMetricsEnabled());
		textArea.setFractionalFontMetricsEnabled(true);
		Assert.assertTrue(textArea.getFractionalFontMetricsEnabled());
	}


	@Test
	public void testHighlightSecondaryLanguages() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getHighlightSecondaryLanguages());
		textArea.setHighlightSecondaryLanguages(false);
		Assert.assertFalse(textArea.getHighlightSecondaryLanguages());
	}


	@Test
	public void testHyperlinkForeground() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setHyperlinkForeground(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getHyperlinkForeground());
	}


	@Test
	public void testHyperlinksEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getHyperlinksEnabled());
		textArea.setHyperlinksEnabled(false);
		Assert.assertFalse(textArea.getHyperlinksEnabled());
	}


	@Test
	public void testMarkOccurrences() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getMarkOccurrences());
		textArea.setMarkOccurrences(true);
		Assert.assertTrue(textArea.getMarkOccurrences());
	}


	@Test
	public void testMarkOccurrencesColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMarkOccurrencesColor(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getMarkOccurrencesColor());
	}


	@Test
	public void testMarkOccurrencesDelay() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMarkOccurrencesDelay(5432);
		Assert.assertEquals(5432, textArea.getMarkOccurrencesDelay());
	}


	@Test
	public void testMatchedBracketBGColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMatchedBracketBGColor(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getMatchedBracketBGColor());
	}


	@Test
	public void testMatchedBracketBorderColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMatchedBracketBorderColor(Color.pink);
		Assert.assertEquals(Color.pink, textArea.getMatchedBracketBorderColor());
	}


	@Test
	public void testModelToToken() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("public int foo;");
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

		char[] chars = "int".toCharArray();
		TokenImpl expected = new TokenImpl(chars, 0, chars.length - 1, 7, TokenTypes.DATA_TYPE, 0);
		Assert.assertEquals(expected, textArea.modelToToken(8));
	}


	@Test
	public void testPaintComponent_noWrappedLines_happyPath() {

		String code = "/**\n" +
			" * This is a class\n" +
			" */\n" +
			"public class Foo {\n" +
			"  // A field\n" +
			"  private int value;\n" +
			"  public int getValue() { return value; }\n" +
			"}\n";

		// Create a text area to render and a random graphics context to render to.
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		Graphics g = createTestGraphics();

		textArea.paintComponent(g);
	}


	@Test
	public void testPaintComponent_wrappedLines_happyPath() {

		String code = "/**\n" +
			" * This is a class\n" +
			" */\n" +
			"public class Foo {\n" +
			"  // A field\n" +
			"  private int value;\n" +
			"  public int getValue() { return value; }\n" +
			"}\n";

		// Create a text area to render and a random graphics context to render to.
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, code);
		textArea.setLineWrap(true);
		Graphics g = createTestGraphics();

		textArea.paintComponent(g);
	}


	@Test
	public void testPaintMatchedBracketPair() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getPaintMatchedBracketPair());
		textArea.setPaintMatchedBracketPair(true);
		Assert.assertTrue(textArea.getPaintMatchedBracketPair());
	}

	@Test
	public void testPaintMarkOccurrencesBorder() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getPaintMarkOccurrencesBorder());
		textArea.setPaintMarkOccurrencesBorder(true);
		Assert.assertTrue(textArea.getPaintMarkOccurrencesBorder());
	}

	@Test
	public void testPaintTabLines() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getPaintTabLines());
		textArea.setPaintTabLines(true);
		Assert.assertTrue(textArea.getPaintTabLines());
	}


	@Test
	public void testSyntaxEditingStyle() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, textArea.getSyntaxEditingStyle());
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assert.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, textArea.getSyntaxEditingStyle());
	}


	@Test
	public void testTabLineColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setTabLineColor(Color.blue);
		Assert.assertEquals(Color.blue, textArea.getTabLineColor());
	}


	@Test
	public void testParserDelay() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setParserDelay(6789);
		Assert.assertEquals(6789, textArea.getParserDelay());
	}


	@Test
	public void testSetTemplateDirectory() throws IOException {

		RSyntaxTextArea.setTemplatesEnabled(true);

		try {
			Path tempDir = Files.createTempDirectory("testDir");
			Assert.assertTrue(RSyntaxTextArea.setTemplateDirectory(tempDir.toString()));
		} finally {
			RSyntaxTextArea.setTemplatesEnabled(false);
		}
	}


	@Test
	public void testUndoLastAction() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.discardAllEdits();
		Assert.assertFalse(textArea.canUndo());
		textArea.append("foo");
		Assert.assertTrue(textArea.canUndo());
		textArea.undoLastAction();;
		Assert.assertFalse(textArea.canUndo());
	}


	@Test
	public void testUseFocusableTips() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertTrue(textArea.getUseFocusableTips());
		textArea.setUseFocusableTips(false);
		Assert.assertFalse(textArea.getUseFocusableTips());
	}


	@Test
	public void testUseSelectedTextColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.getUseSelectedTextColor());
		textArea.setUseSelectedTextColor(true);
		Assert.assertTrue(textArea.getUseSelectedTextColor());
	}


	@Test
	public void testViewToToken() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, "public int foo;");

		char[] chars = "public".toCharArray();
		TokenImpl expected = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.RESERVED_WORD, 0);
		Assert.assertEquals(expected, textArea.viewToToken(new Point(1, 1)));
	}


	@Test
	public void testWhitespaceVisible() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertFalse(textArea.isWhitespaceVisible());
		textArea.setWhitespaceVisible(true);
		Assert.assertTrue(textArea.isWhitespaceVisible());
	}

}
