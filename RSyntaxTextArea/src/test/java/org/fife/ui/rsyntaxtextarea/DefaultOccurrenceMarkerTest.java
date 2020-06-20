/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rtextarea.SmartHighlightPainter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@code DefaultOccurrenceMarker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DefaultOccurrenceMarkerTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testGetTokenToMark_beginningOfWord() {

		String origContent = "public int foo() {}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(origContent.indexOf("foo"));

		DefaultOccurrenceMarker marker = new DefaultOccurrenceMarker();
		Token actual = marker.getTokenToMark(textArea);
		Assert.assertEquals("foo", actual.getLexeme());
		Assert.assertEquals(TokenTypes.IDENTIFIER, actual.getType());
	}


	@Test
	public void testGetTokenToMark_endOfWord() {

		String origContent = "public int foo() {}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(origContent.indexOf('(')); // Should get preceding "foo"

		DefaultOccurrenceMarker marker = new DefaultOccurrenceMarker();
		Token actual = marker.getTokenToMark(textArea);
		Assert.assertEquals("foo", actual.getLexeme());
		Assert.assertEquals(TokenTypes.IDENTIFIER, actual.getType());
	}


	@Test
	public void testIsValidType_validType() {

		String origContent = "public int foo() {}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);

		char[] line = "foo".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.IDENTIFIER, 0);

		DefaultOccurrenceMarker marker = new DefaultOccurrenceMarker();
		Assert.assertTrue(marker.isValidType(textArea, token));
	}


	@Test
	public void testIsValidType_invalidType() {

		String origContent = "public int foo() {}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);

		char[] line = "int".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.DATA_TYPE, 0);

		DefaultOccurrenceMarker marker = new DefaultOccurrenceMarker();
		Assert.assertFalse(marker.isValidType(textArea, token));
	}


	@Test
	public void testMarkOccurrences() {

		String origContent = "public int foo() {}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);

		char[] line = "int".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.DATA_TYPE, 0);

		DefaultOccurrenceMarker marker = new DefaultOccurrenceMarker();
		RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)textArea.getHighlighter();
		SmartHighlightPainter p = new SmartHighlightPainter();
		marker.markOccurrences((RSyntaxDocument)textArea.getDocument(), token, h, p);
	}
}
