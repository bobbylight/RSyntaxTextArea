/*
 * 03/14/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rsyntaxtextarea.parser.Parser;
import org.junit.*;
import org.junit.runner.RunWith;

import javax.swing.*;
import javax.swing.event.HyperlinkListener;


/**
 * Unit tests for the {@link RSyntaxTextArea} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaTest extends AbstractRSyntaxTextAreaTest {

	private boolean origTemplatesEnabled;


	@Before
	public void setUp() {
		origTemplatesEnabled = RSyntaxTextArea.getTemplatesEnabled();
	}


	@After
	public void tearDown() {
		RSyntaxTextArea.setTemplatesEnabled(origTemplatesEnabled);
	}


	@Test
	public void testConstructor_rowsAndColumns() {
		RSyntaxTextArea textArea = new RSyntaxTextArea(25, 80);
		Assert.assertEquals(25, textArea.getRows());
		Assert.assertEquals(80, textArea.getColumns());
	}


	@Test
	public void testConstructor_textAndRowsAndColumns() {
		RSyntaxTextArea textArea = new RSyntaxTextArea("foo", 25, 80);
		Assert.assertEquals("foo", textArea.getText());
		Assert.assertEquals(25, textArea.getRows());
		Assert.assertEquals(80, textArea.getColumns());
	}


	@Test
	public void testConstructor_documentAndTextAndRowsAndColumns() {
		RSyntaxDocument doc = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_C);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc, "foo", 25, 80);
		Assert.assertEquals("foo", textArea.getText());
		Assert.assertEquals(25, textArea.getRows());
		Assert.assertEquals(80, textArea.getColumns());
	}


	@Test
	public void testAddRemoveActiveLineRangeListener() {

		ActiveLineRangeListener listener = e -> {
			// Do nothing
		};

		RSyntaxTextArea textArea = createTextArea();
		textArea.addActiveLineRangeListener(listener);
		textArea.removeActiveLineRangeListener(listener);
	}


	@Test
	public void testAddRemoveHyperlinkListener() {

		HyperlinkListener listener = e -> {
			// Do nothing
		};

		RSyntaxTextArea textArea = createTextArea();
		textArea.addHyperlinkListener(listener);
		textArea.removeHyperlinkListener(listener);
	}


	@Test
	public void testAddRemoveParser() {

		Parser parser = new AbstractParser() {
			@Override
			public ParseResult parse(RSyntaxDocument doc, String style) {
				return null;
			}
		};

		RSyntaxTextArea textArea = createTextArea();
		textArea.addParser(parser);
		textArea.removeParser(parser);
	}


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
	public void testClearParsers() {
		RSyntaxTextArea textArea = createTextArea();
		Assert.assertTrue(textArea.getParserCount() > 0);
		textArea.clearParsers();
		Assert.assertEquals(0, textArea.getParserCount());
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
	public void testConfigurePopupMenu() {

		RSyntaxTextArea textArea = createTextArea();
		JPopupMenu menu = textArea.getPopupMenu();
		textArea.configurePopupMenu(menu);
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
	public void testGetBackgroundForToken() {
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, "public");
		Token token = textArea.getTokenListForLine(0);
		Assert.assertNull(textArea.getBackgroundForToken(token));
	}


	@Test
	public void testGetLastVisibleOffset_foldingEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		Assert.assertEquals(textArea.getDocument().getLength(), textArea.getLastVisibleOffset());
	}


	@Test
	public void testGetLastVisibleOffset_foldingNotEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setCodeFoldingEnabled(false);
		Assert.assertEquals(textArea.getDocument().getLength(), textArea.getLastVisibleOffset());
	}


	@Test
	public void testGetSetLinkGenerator() {
		RSyntaxTextArea textArea = createTextArea();
		Assert.assertNull(textArea.getLinkGenerator());
		textArea.setLinkGenerator((textArea1, offs) -> null);
		Assert.assertNotNull(textArea.getLinkGenerator());
	}


	@Test
	public void testGetPopupMenu() {
		RSyntaxTextArea textArea = createTextArea();
		Assert.assertNotNull(textArea.getPopupMenu());
	}


	@Test
	public void testGetShouldAutoIndentNextLine_autoIndentEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		Assert.assertTrue(textArea.getShouldIndentNextLine(0));
	}


	@Test
	public void testGetShouldAutoIndentNextLine_autoIndentDisabled() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setAutoIndentEnabled(false);
		Assert.assertFalse(textArea.getShouldIndentNextLine(0));
	}


	@Test
	public void testGetToolTipText() {
		RSyntaxTextArea textArea = createTextArea();
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 0, 0, 1, false);
		Assert.assertNull(textArea.getToolTipText(e));
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
		textArea.setMarkOccurrences(false); // Needed for code path
		Assert.assertFalse(textArea.getMarkOccurrences());
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


	@Test(expected = IllegalArgumentException.class)
	public void testMarkOccurrencesDelay_invalidValue() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMarkOccurrencesDelay(0);
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
	public void testModelToToken_negativeValue() {
		RSyntaxTextArea textArea = createTextArea();
		Assert.assertNull(textArea.modelToToken(-1));
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
		RSyntaxTextArea textArea = createTextArea();
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
	public void testRedoLastAction() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.discardAllEdits();

		Assert.assertFalse(textArea.canUndo());
		textArea.append("foo");
		Assert.assertTrue(textArea.canUndo());
		Assert.assertFalse(textArea.canRedo());

		textArea.undoLastAction();;
		Assert.assertTrue(textArea.canRedo());

		textArea.redoLastAction();
		Assert.assertFalse(textArea.canRedo());
		Assert.assertTrue(textArea.getText().endsWith("foo"));
	}


	@Test
	public void testRemoveNotify() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.removeNotify();
	}


	@Test
	public void testSaveTemplates_templatesNotEnabled() {
		RSyntaxTextArea.setTemplatesEnabled(false);
		RSyntaxTextArea.saveTemplates();
	}


	@Test
	public void testSaveTemplates_templatesEnabled() {
		RSyntaxTextArea.setTemplatesEnabled(true);
		RSyntaxTextArea.saveTemplates();
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetLinkScanningMask_invalidValue() {
		createTextArea().setLinkScanningMask(0);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetRightHandSideCorrection_invalidValue() {
		createTextArea().setRightHandSideCorrection(-1);
	}


	@Test
	public void testSyntaxEditingStyle() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assert.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, textArea.getSyntaxEditingStyle());
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assert.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, textArea.getSyntaxEditingStyle());
	}


	@Test
	public void testSyntaxEditingStyle_defaultForNullValue() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setSyntaxEditingStyle(null);
		Assert.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, textArea.getSyntaxEditingStyle());
	}


	@Test
	public void testTabLineColor() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setTabLineColor(Color.blue);
		Assert.assertEquals(Color.blue, textArea.getTabLineColor());
	}


	@Test
	public void testParserDelay() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setParserDelay(6789);
		Assert.assertEquals(6789, textArea.getParserDelay());
	}


	@Test
	public void testSetTemplateDirectory_dirExists() throws IOException {

		RSyntaxTextArea.setTemplatesEnabled(true);

		try {
			Path tempDir = Files.createTempDirectory("testDir");
			Assert.assertTrue(RSyntaxTextArea.setTemplateDirectory(tempDir.toString()));
		} finally {
			RSyntaxTextArea.setTemplatesEnabled(false);
		}
	}


	@Test
	public void testSetTemplateDirectory_dirDoesNotExist() throws IOException {

		RSyntaxTextArea.setTemplatesEnabled(true);

		try {
			Path tempDir = Files.createTempDirectory("testDir");
			String nonExistentDir = tempDir.toString() + "/subdir";
			Assert.assertTrue(RSyntaxTextArea.setTemplateDirectory(nonExistentDir));
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
