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

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.modes.JavaTokenMaker;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rsyntaxtextarea.parser.Parser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


/**
 * Unit tests for the {@link RSyntaxTextArea} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaTest extends AbstractRSyntaxTextAreaTest {

	private boolean origTemplatesEnabled;


	@BeforeEach
	void setUp() {
		origTemplatesEnabled = RSyntaxTextArea.getTemplatesEnabled();
	}


	@AfterEach
	void tearDown() {
		RSyntaxTextArea.setTemplatesEnabled(origTemplatesEnabled);
	}


	@Test
	void testFoo() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setText("test");
		Assertions.assertEquals("test", textArea.getText());
	}


	@Test
	void testConstructor_rowsAndColumns() {
		RSyntaxTextArea textArea = new RSyntaxTextArea(25, 80);
		Assertions.assertEquals(25, textArea.getRows());
		Assertions.assertEquals(80, textArea.getColumns());
	}


	@Test
	void testConstructor_textAndRowsAndColumns() {
		RSyntaxTextArea textArea = new RSyntaxTextArea("foo", 25, 80);
		Assertions.assertEquals("foo", textArea.getText());
		Assertions.assertEquals(25, textArea.getRows());
		Assertions.assertEquals(80, textArea.getColumns());
	}


	@Test
	void testConstructor_documentAndTextAndRowsAndColumns() {
		RSyntaxDocument doc = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_C);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc, "foo", 25, 80);
		Assertions.assertEquals("foo", textArea.getText());
		Assertions.assertEquals(25, textArea.getRows());
		Assertions.assertEquals(80, textArea.getColumns());
	}


	@Test
	void testAddRemoveActiveLineRangeListener() {

		ActiveLineRangeListener listener = e -> {
			// Do nothing
		};

		RSyntaxTextArea textArea = createTextArea();
		textArea.addActiveLineRangeListener(listener);
		textArea.removeActiveLineRangeListener(listener);
	}


	@Test
	void testAddRemoveHyperlinkListener() {

		HyperlinkListener listener = e -> {
			// Do nothing
		};

		RSyntaxTextArea textArea = createTextArea();
		textArea.addHyperlinkListener(listener);
		textArea.removeHyperlinkListener(listener);
	}


	@Test
	void testAddRemoveParser() {

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
	void testAnimateBracketMatching() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertTrue(textArea.getAnimateBracketMatching());
		textArea.setAnimateBracketMatching(false);
		Assertions.assertFalse(textArea.getAnimateBracketMatching());
	}


	@Test
	void testAntiAliasingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertTrue(textArea.getAntiAliasingEnabled());
		textArea.setAntiAliasingEnabled(false);
		Assertions.assertFalse(textArea.getAntiAliasingEnabled());
	}


	@Test
	void testAutoIndentEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertTrue(textArea.isAutoIndentEnabled());
		textArea.setAutoIndentEnabled(false);
		Assertions.assertFalse(textArea.isAutoIndentEnabled());
	}


	@Test
	void testBracketMatchingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertTrue(textArea.isBracketMatchingEnabled());
		textArea.setBracketMatchingEnabled(false);
		Assertions.assertFalse(textArea.isBracketMatchingEnabled());
	}


	@Test
	void testClearParsers() {
		RSyntaxTextArea textArea = createTextArea();
		Assertions.assertTrue(textArea.getParserCount() > 0);
		textArea.clearParsers();
		Assertions.assertEquals(0, textArea.getParserCount());
	}


	@Test
	void testClearWhitespaceLinesEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertTrue(textArea.isClearWhitespaceLinesEnabled());
		textArea.setClearWhitespaceLinesEnabled(false);
		Assertions.assertFalse(textArea.isClearWhitespaceLinesEnabled());
	}


	@Test
	void testCloseCurlyBraces() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertTrue(textArea.getCloseCurlyBraces());
		textArea.setCloseCurlyBraces(false);
		Assertions.assertFalse(textArea.getCloseCurlyBraces());
	}


	@Test
	void testCloseMarkupTags() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertTrue(textArea.getCloseMarkupTags());
		textArea.setCloseMarkupTags(false);
		Assertions.assertFalse(textArea.getCloseMarkupTags());
	}


	@Test
	void testCodeFoldingEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertFalse(textArea.isCodeFoldingEnabled());
		textArea.setCodeFoldingEnabled(true);
		Assertions.assertTrue(textArea.isCodeFoldingEnabled());
	}


	@Test
	void testConfigurePopupMenu() {

		RSyntaxTextArea textArea = createTextArea();
		JPopupMenu menu = textArea.getPopupMenu();
		textArea.configurePopupMenu(menu);
	}


	@Test
	void testCopyAsStyledText_zeroArg_html_happyPath() throws Exception {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

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
		Assertions.assertTrue(clipboardContent.contains(expectedPortion));
	}


	@Test
	@Disabled("Fails in the CI environment for some reason")
	void testCopyAsStyledText_zeroArg_rtf_happyPath() throws Exception {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

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
		Assertions.assertTrue(clipboardString.contains(expectedPortion),
			"Unexpected clipboard contents: " + clipboardString);
	}


	@Test
	void testCopyAsStyledText_zeroArg_string_happyPath() throws Exception {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setText("public int getValue();");
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(textArea.getDocument().getLength());
		textArea.copyAsStyledText();

		String clipboardContent = (String)textArea.getToolkit().getSystemClipboard().
			getData(DataFlavor.stringFlavor);
		Assertions.assertEquals(textArea.getText(), clipboardContent);
	}


	@Test
	void testCopyAsStyledText_themeArg_string_happyPath() throws Exception {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setText("public int getValue();");
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(textArea.getDocument().getLength());
		textArea.copyAsStyledText(new Theme(textArea));

		String clipboardContent = (String)textArea.getToolkit().getSystemClipboard().
			getData(DataFlavor.stringFlavor);
		Assertions.assertEquals(textArea.getText(), clipboardContent);
	}


	@Test
	void testCopyAsStyledText_nullThemeArg_string_happyPath() throws Exception {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setText("public int getValue();");
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(textArea.getDocument().getLength());
		textArea.copyAsStyledText(null);

		String clipboardContent = (String)textArea.getToolkit().getSystemClipboard().
			getData(DataFlavor.stringFlavor);
		Assertions.assertEquals(textArea.getText(), clipboardContent);
	}


	@Test
	void testEOLMarkersVisible() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertFalse(textArea.getEOLMarkersVisible());
		textArea.setEOLMarkersVisible(true);
		Assertions.assertTrue(textArea.getEOLMarkersVisible());
	}


	@Test
	void testGetSetRightHandSideCorrection() {
		RSyntaxTextArea textArea = createTextArea();
		Assertions.assertEquals(0, textArea.getRightHandSideCorrection());
		textArea.setRightHandSideCorrection(5);
		Assertions.assertEquals(5, textArea.getRightHandSideCorrection());
	}


	@Test
	void testGetSetTemplatesEnabled() {
		Assertions.assertFalse(RSyntaxTextArea.getTemplatesEnabled());
		try {
			RSyntaxTextArea.setTemplatesEnabled(true);
			Assertions.assertTrue(RSyntaxTextArea.getTemplatesEnabled());
		} finally {
			RSyntaxTextArea.setTemplatesEnabled(false);
		}
	}


	@Test
	void testFractionalFontMetricsEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertFalse(textArea.getFractionalFontMetricsEnabled());
		textArea.setFractionalFontMetricsEnabled(true);
		Assertions.assertTrue(textArea.getFractionalFontMetricsEnabled());
	}


	@Test
	void testGetBackgroundForToken() {
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, "public");
		Token token = textArea.getTokenListForLine(0);
		Assertions.assertNull(textArea.getBackgroundForToken(token));
	}


	@Test
	void testGetLastVisibleOffset_foldingEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		Assertions.assertEquals(textArea.getDocument().getLength(), textArea.getLastVisibleOffset());
	}


	@Test
	void testGetLastVisibleOffset_foldingNotEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setCodeFoldingEnabled(false);
		Assertions.assertEquals(textArea.getDocument().getLength(), textArea.getLastVisibleOffset());
	}


	@Test
	void testGetSetLinkGenerator() {
		RSyntaxTextArea textArea = createTextArea();
		Assertions.assertNull(textArea.getLinkGenerator());
		textArea.setLinkGenerator((textArea1, offs) -> null);
		Assertions.assertNotNull(textArea.getLinkGenerator());
	}


	@Test
	void testGetSetFont() {
		RSyntaxTextArea textArea = createTextArea();
		Font origFont = textArea.getFont();
		Font newFont = origFont.deriveFont(Font.ITALIC, 22f);
		textArea.setFont(newFont);
		Assertions.assertEquals(newFont, textArea.getFont());
	}


	@Test
	void testGetPopupMenu() {
		RSyntaxTextArea textArea = createTextArea();
		Assertions.assertNotNull(textArea.getPopupMenu());
	}


	@Test
	void testGetShouldAutoIndentNextLine_autoIndentEnabled() {
		RSyntaxTextArea textArea = createTextArea();
		Assertions.assertTrue(textArea.getShouldIndentNextLine(0));
	}


	@Test
	void testGetShouldAutoIndentNextLine_autoIndentDisabled() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setAutoIndentEnabled(false);
		Assertions.assertFalse(textArea.getShouldIndentNextLine(0));
	}


	@Test
	void testGetToolTipText() {
		RSyntaxTextArea textArea = createTextArea();
		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 0, 0, 1, false);
		Assertions.assertNull(textArea.getToolTipText(e));
	}


	@Test
	void testHighlightSecondaryLanguages() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertTrue(textArea.getHighlightSecondaryLanguages());
		textArea.setHighlightSecondaryLanguages(false);
		Assertions.assertFalse(textArea.getHighlightSecondaryLanguages());
	}


	@Test
	@Disabled("Not possible to do the setup for this test headlessly currently")
	void testHyperlinkEventsAreFired_linkGenerator() {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		// TODO: Figure out a way to do the setup necessary for
		// link generator events to fire
	}


	@Test
	void testHyperlinkEventsAreFired_noLinkGenerator() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA,
			"// https://www.google.com");
		textArea.setCaretPosition(3);

		HyperlinkEvent[] lastEvent =  new HyperlinkEvent[1];
		HyperlinkListener listener = e -> lastEvent[0] = e;
		textArea.addHyperlinkListener(listener);

		// Verify "entered" events are propagated
		textArea.fireHyperlinkUpdate(HyperlinkEvent.EventType.ENTERED);
		Assertions.assertNotNull(lastEvent[0]);
		Assertions.assertEquals(HyperlinkEvent.EventType.ENTERED, lastEvent[0].getEventType());

		// Verify "activated" events are propagated
		textArea.fireHyperlinkUpdate(HyperlinkEvent.EventType.ACTIVATED);
		Assertions.assertNotNull(lastEvent[0]);
		Assertions.assertEquals(HyperlinkEvent.EventType.ACTIVATED, lastEvent[0].getEventType());

		// Verify "exited" events are propagated
		textArea.fireHyperlinkUpdate(HyperlinkEvent.EventType.EXITED);
		Assertions.assertNotNull(lastEvent[0]);
		Assertions.assertEquals(HyperlinkEvent.EventType.EXITED, lastEvent[0].getEventType());
	}


	@Test
	void testHyperlinkForeground() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setHyperlinkForeground(Color.pink);
		Assertions.assertEquals(Color.pink, textArea.getHyperlinkForeground());
	}


	@Test
	void testHyperlinksEnabled() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertTrue(textArea.getHyperlinksEnabled());
		textArea.setHyperlinksEnabled(false);
		Assertions.assertFalse(textArea.getHyperlinksEnabled());
	}


	@Test
	void testMarkOccurrences() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertFalse(textArea.getMarkOccurrences());
		textArea.setMarkOccurrences(true);
		Assertions.assertTrue(textArea.getMarkOccurrences());
		textArea.setMarkOccurrences(false); // Needed for code path
		Assertions.assertFalse(textArea.getMarkOccurrences());
	}


	@Test
	void testMarkOccurrencesColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMarkOccurrencesColor(Color.pink);
		Assertions.assertEquals(Color.pink, textArea.getMarkOccurrencesColor());
	}


	@Test
	void testMarkOccurrencesDelay() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMarkOccurrencesDelay(5432);
		Assertions.assertEquals(5432, textArea.getMarkOccurrencesDelay());
	}


	@Test
	void testMarkOccurrencesDelay_zero() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMarkOccurrencesDelay(0);
		Assertions.assertEquals(0, textArea.getMarkOccurrencesDelay());
	}


	@Test
	void testMarkOccurrencesDelay_invalidValue() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			RSyntaxTextArea textArea = new RSyntaxTextArea();
			textArea.setMarkOccurrencesDelay(-1);
		});
	}


	@Test
	void testMatchedBracketBGColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMatchedBracketBGColor(Color.pink);
		Assertions.assertEquals(Color.pink, textArea.getMatchedBracketBGColor());
	}


	@Test
	void testMatchedBracketBorderColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setMatchedBracketBorderColor(Color.pink);
		Assertions.assertEquals(Color.pink, textArea.getMatchedBracketBorderColor());
	}


	@Test
	void testModelToToken() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("public int foo;");
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

		char[] chars = "int".toCharArray();
		TokenImpl expected = new TokenImpl(chars, 0, chars.length - 1, 7, TokenTypes.DATA_TYPE, 0);
		Assertions.assertEquals(expected, textArea.modelToToken(8));
	}


	@Test
	void testModelToToken_negativeValue() {
		RSyntaxTextArea textArea = createTextArea();
		Assertions.assertNull(textArea.modelToToken(-1));
	}


	@Test
	void testPaintComponent_noWrappedLines_happyPath() {

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
	void testPaintComponent_wrappedLines_happyPath() {

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
	void testPaintMatchedBracketPair() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertFalse(textArea.getPaintMatchedBracketPair());
		textArea.setPaintMatchedBracketPair(true);
		Assertions.assertTrue(textArea.getPaintMatchedBracketPair());
	}

	@Test
	void testPaintMarkOccurrencesBorder() {
		RSyntaxTextArea textArea = createTextArea();
		Assertions.assertFalse(textArea.getPaintMarkOccurrencesBorder());
		textArea.setPaintMarkOccurrencesBorder(true);
		Assertions.assertTrue(textArea.getPaintMarkOccurrencesBorder());
	}

	@Test
	void testPaintTabLines() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertFalse(textArea.getPaintTabLines());
		textArea.setPaintTabLines(true);
		Assertions.assertTrue(textArea.getPaintTabLines());
	}


	@Test
	void testRedoLastAction() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.discardAllEdits();

		Assertions.assertFalse(textArea.canUndo());
		textArea.append("foo");
		Assertions.assertTrue(textArea.canUndo());
		Assertions.assertFalse(textArea.canRedo());

		textArea.undoLastAction();
		Assertions.assertTrue(textArea.canRedo());

		textArea.redoLastAction();
		Assertions.assertFalse(textArea.canRedo());
		Assertions.assertTrue(textArea.getText().endsWith("foo"));
	}


	@Test
	void testRemoveNotify() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.removeNotify();
	}


	@Test
	void testSaveTemplates_templatesNotEnabled() {
		RSyntaxTextArea.setTemplatesEnabled(false);
		RSyntaxTextArea.saveTemplates();
	}


	@Test
	void testSaveTemplates_templatesEnabled() {
		RSyntaxTextArea.setTemplatesEnabled(true);
		RSyntaxTextArea.saveTemplates();
	}


	@Test
	void testSetLinkScanningMask_invalidValue() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> createTextArea().setLinkScanningMask(0));
	}


	@Test
	void testSetRightHandSideCorrection_invalidValue() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> createTextArea().setRightHandSideCorrection(-1));
	}


	@Test
	void testSyntaxEditingStyle() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, textArea.getSyntaxEditingStyle());
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, textArea.getSyntaxEditingStyle());
	}


	/**
	 * In {@code RSyntaxTextArea}, the code path {@code setDocument()} ->
	 * {@code setSyntaxEditingStyle()} should not call back into the
	 * Document to update its TokenMaker. Best-case, this call is
	 * unnecessary as the Document is what's triggering this update so
	 * it already has the right value; worst-case, for custom TokenMakers
	 * not registered with the TokenMakerFactory, syntax highlighting
	 * can be lost.
	 * See https://github.com/bobbylight/RSyntaxTextArea/issues/206.
	 */
	@Test
	void testSyntaxEditingStyle_dontUpdateDocumentIfCalledViaSetDocument() {

		// Unfortunately we can't use Mockito here because of its issues
		// running in Java 17 (as of Mockito 4.4.0).
		int[] stringOverloadCalled = { 0 };
		int[] tokenMakerOverloadCalled = { 0 };

		RSyntaxDocument doc = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_NONE) {
			@Override
			public void setSyntaxStyle(TokenMaker tm) {
				tokenMakerOverloadCalled[0]++;
				super.setSyntaxStyle(tm);
			}
			@Override
			public void setSyntaxStyle(String style) {
				stringOverloadCalled[0]++;
				super.setSyntaxStyle(style);
			}
		};
		doc.setSyntaxStyle(new JavaTokenMaker());
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		// Verify the Document has its syntax style set only once, by the explicit
		// call to the setSyntaxStyle(TokenMaker) overload.
		// Verifying the string overload isn't called (after the initial call
		// via its constructor) per GitHub issue 206.
		Assertions.assertEquals(1, stringOverloadCalled[0]);
		Assertions.assertEquals(1, tokenMakerOverloadCalled[0]);

	}


	@Test
	void testSyntaxEditingStyle_defaultForNullValue() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setSyntaxEditingStyle(null);
		Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, textArea.getSyntaxEditingStyle());
	}


	@Test
	void testTabLineColor() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setTabLineColor(Color.blue);
		Assertions.assertEquals(Color.blue, textArea.getTabLineColor());
	}


	@Test
	void testParserDelay() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.setParserDelay(6789);
		Assertions.assertEquals(6789, textArea.getParserDelay());
	}


	@Test
	void testSetTemplateDirectory_dirExists() throws IOException {

		RSyntaxTextArea.setTemplatesEnabled(true);

		try {
			Path tempDir = Files.createTempDirectory("testDir");
			Assertions.assertTrue(RSyntaxTextArea.setTemplateDirectory(tempDir.toString()));
		} finally {
			RSyntaxTextArea.setTemplatesEnabled(false);
		}
	}


	@Test
	void testSetTemplateDirectory_dirDoesNotExist() throws IOException {

		RSyntaxTextArea.setTemplatesEnabled(true);

		try {
			Path tempDir = Files.createTempDirectory("testDir");
			String nonExistentDir = tempDir.toString() + "/subdir";
			Assertions.assertTrue(RSyntaxTextArea.setTemplateDirectory(nonExistentDir));
		} finally {
			RSyntaxTextArea.setTemplatesEnabled(false);
		}
	}


	@Test
	void testUndoLastAction() {
		RSyntaxTextArea textArea = createTextArea();
		textArea.discardAllEdits();
		Assertions.assertFalse(textArea.canUndo());
		textArea.append("foo");
		Assertions.assertTrue(textArea.canUndo());
		textArea.undoLastAction();
		Assertions.assertFalse(textArea.canUndo());
	}


	@Test
	void testUseFocusableTips() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertTrue(textArea.getUseFocusableTips());
		textArea.setUseFocusableTips(false);
		Assertions.assertFalse(textArea.getUseFocusableTips());
	}


	@Test
	void testUseSelectedTextColor() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertFalse(textArea.getUseSelectedTextColor());
		textArea.setUseSelectedTextColor(true);
		Assertions.assertTrue(textArea.getUseSelectedTextColor());
	}


	@Test
	void testViewToToken() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, "public int foo;");

		char[] chars = "public".toCharArray();
		TokenImpl expected = new TokenImpl(chars, 0, chars.length - 1, 0, TokenTypes.RESERVED_WORD, 0);
		Assertions.assertEquals(expected, textArea.viewToToken(new Point(1, 1)));
	}


	@Test
	void testWhitespaceVisible() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		Assertions.assertFalse(textArea.isWhitespaceVisible());
		textArea.setWhitespaceVisible(true);
		Assertions.assertTrue(textArea.isWhitespaceVisible());
	}

}
