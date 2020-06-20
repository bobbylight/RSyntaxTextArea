/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rtextarea.SmartHighlightPainter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@code XmlOccurrenceMarker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class XmlOccurrenceMarkerTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testGetTokenToMark_beginningOfWord() {

		String origContent = "<books><book></book></books>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, origContent);
		textArea.setCaretPosition(origContent.indexOf("book>"));

		XmlOccurrenceMarker marker = new XmlOccurrenceMarker();
		Token actual = marker.getTokenToMark(textArea);
		Assert.assertEquals("book", actual.getLexeme());
		Assert.assertEquals(TokenTypes.MARKUP_TAG_NAME, actual.getType());
	}


	@Test
	public void testGetTokenToMark_endOfWord() {

		String origContent = "<books><book></book></books>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, origContent);
		textArea.setCaretPosition(origContent.indexOf(">")); // Should get preceding "books"

		XmlOccurrenceMarker marker = new XmlOccurrenceMarker();
		Token actual = marker.getTokenToMark(textArea);
		Assert.assertEquals("books", actual.getLexeme());
		Assert.assertEquals(TokenTypes.MARKUP_TAG_NAME, actual.getType());
	}


	@Test
	public void testIsValidType_validType() {

		String origContent = "<books><book></book></books>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, origContent);
		char[] line = "foo".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.MARKUP_TAG_NAME, 0);

		XmlOccurrenceMarker marker = new XmlOccurrenceMarker();
		Assert.assertTrue(marker.isValidType(textArea, token));
	}


	@Test
	public void testIsValidType_invalidType() {

		String origContent = "<books><book></book></books>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, origContent);
		char[] line = "<".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.SEPARATOR, 0);

		XmlOccurrenceMarker marker = new XmlOccurrenceMarker();
		Assert.assertFalse(marker.isValidType(textArea, token));
	}


	@Test
	public void testMarkOccurrences_forward() {

		String origContent = "<books><book></book></books>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, origContent);

		// Caret is on first "books" opening tag
		char[] line = origContent.toCharArray();
		TokenImpl token = new TokenImpl(line, 1, 5, 1, TokenTypes.MARKUP_TAG_NAME, 0);

		XmlOccurrenceMarker marker = new XmlOccurrenceMarker();
		RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)textArea.getHighlighter();
		SmartHighlightPainter p = new SmartHighlightPainter();
		marker.markOccurrences((RSyntaxDocument)textArea.getDocument(), token, h, p);
	}


	@Test
	public void testMarkOccurrences_backward() {

		String origContent = "<books><book></book></books>";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_XML, origContent);

		// Caret is closing "books" tag
		char[] line = origContent.toCharArray();
		TokenImpl token = new TokenImpl(line, 22, 26, 22, TokenTypes.MARKUP_TAG_NAME, 0);

		XmlOccurrenceMarker marker = new XmlOccurrenceMarker();
		RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)textArea.getHighlighter();
		SmartHighlightPainter p = new SmartHighlightPainter();
		marker.markOccurrences((RSyntaxDocument)textArea.getDocument(), token, h, p);
	}
}
