/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rtextarea.SmartHighlightPainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@code HtmlOccurrenceMarker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class HtmlOccurrenceMarkerTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testGetTokenToMark_beginningOfWord() {

		String origContent = "<html><body></body></html>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_HTML, origContent);
		textArea.setCaretPosition(origContent.indexOf("body>"));

		HtmlOccurrenceMarker marker = new HtmlOccurrenceMarker();
		Token actual = marker.getTokenToMark(textArea);
		Assertions.assertEquals("body", actual.getLexeme());
		Assertions.assertEquals(TokenTypes.MARKUP_TAG_NAME, actual.getType());
	}


	@Test
	void testGetTokenToMark_endOfWord() {

		String origContent = "<html><body></body></html>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_HTML, origContent);
		textArea.setCaretPosition(origContent.indexOf(">")); // Should get preceding "html"

		HtmlOccurrenceMarker marker = new HtmlOccurrenceMarker();
		Token actual = marker.getTokenToMark(textArea);
		Assertions.assertEquals("html", actual.getLexeme());
		Assertions.assertEquals(TokenTypes.MARKUP_TAG_NAME, actual.getType());
	}


	@Test
	void testIsValidType_validType() {

		String origContent = "<html><body></body></html>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_HTML, origContent);
		char[] line = "html".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.MARKUP_TAG_NAME, 0);

		HtmlOccurrenceMarker marker = new HtmlOccurrenceMarker();
		Assertions.assertTrue(marker.isValidType(textArea, token));
	}


	@Test
	void testIsValidType_invalidType() {

		String origContent = "<html><body></body></html>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_HTML, origContent);
		char[] line = "<".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.SEPARATOR, 0);

		HtmlOccurrenceMarker marker = new HtmlOccurrenceMarker();
		Assertions.assertFalse(marker.isValidType(textArea, token));
	}


	@Test
	void testMarkOccurrences_forward() {

		String origContent = "<html><body></body></html>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_HTML, origContent);

		// Caret is on first "html" opening tag
		char[] line = origContent.toCharArray();
		TokenImpl token = new TokenImpl(line, 1, 4, 1, TokenTypes.MARKUP_TAG_NAME, 0);

		HtmlOccurrenceMarker marker = new HtmlOccurrenceMarker();
		RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)textArea.getHighlighter();
		SmartHighlightPainter p = new SmartHighlightPainter();
		marker.markOccurrences((RSyntaxDocument)textArea.getDocument(), token, h, p);
	}


	@Test
	void testMarkOccurrences_backward() {

		String origContent = "<html><body></body></html>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_HTML, origContent);

		// Caret is on closing "html" tag
		char[] line = origContent.toCharArray();
		TokenImpl token = new TokenImpl(line, 21, 24, 21, TokenTypes.MARKUP_TAG_NAME, 0);

		HtmlOccurrenceMarker marker = new HtmlOccurrenceMarker();
		RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)textArea.getHighlighter();
		SmartHighlightPainter p = new SmartHighlightPainter();
		marker.markOccurrences((RSyntaxDocument)textArea.getDocument(), token, h, p);
	}
}
