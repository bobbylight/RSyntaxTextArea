/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rtextarea.SmartHighlightPainter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@code HtmlOccurrenceMarker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class HtmlOccurrenceMarkerTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testGetTokenToMark_beginningOfWord() {

		String origContent = "<html><body></body></html>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_HTML, origContent);
		textArea.setCaretPosition(origContent.indexOf("body>"));

		HtmlOccurrenceMarker marker = new HtmlOccurrenceMarker();
		Token actual = marker.getTokenToMark(textArea);
		Assert.assertEquals("body", actual.getLexeme());
		Assert.assertEquals(TokenTypes.MARKUP_TAG_NAME, actual.getType());
	}


	@Test
	public void testGetTokenToMark_endOfWord() {

		String origContent = "<html><body></body></html>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_HTML, origContent);
		textArea.setCaretPosition(origContent.indexOf(">")); // Should get preceding "html"

		HtmlOccurrenceMarker marker = new HtmlOccurrenceMarker();
		Token actual = marker.getTokenToMark(textArea);
		Assert.assertEquals("html", actual.getLexeme());
		Assert.assertEquals(TokenTypes.MARKUP_TAG_NAME, actual.getType());
	}


	@Test
	public void testIsValidType_validType() {

		String origContent = "<html><body></body></html>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_HTML, origContent);
		char[] line = "html".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.MARKUP_TAG_NAME, 0);

		HtmlOccurrenceMarker marker = new HtmlOccurrenceMarker();
		Assert.assertTrue(marker.isValidType(textArea, token));
	}


	@Test
	public void testIsValidType_invalidType() {

		String origContent = "<html><body></body></html>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_HTML, origContent);
		char[] line = "<".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.SEPARATOR, 0);

		HtmlOccurrenceMarker marker = new HtmlOccurrenceMarker();
		Assert.assertFalse(marker.isValidType(textArea, token));
	}


	@Test
	public void testMarkOccurrences_forward() {

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
	public void testMarkOccurrences_backward() {

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
