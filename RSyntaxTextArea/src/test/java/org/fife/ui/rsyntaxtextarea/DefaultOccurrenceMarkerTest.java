/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rtextarea.SmartHighlightPainter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@code DefaultOccurrenceMarker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DefaultOccurrenceMarkerTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testGetTokenToMark_beginningOfWord() {

		String origContent = "public int foo() {}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(origContent.indexOf("foo"));

		DefaultOccurrenceMarker marker = new DefaultOccurrenceMarker();
		Token actual = marker.getTokenToMark(textArea);
		Assertions.assertEquals("foo", actual.getLexeme());
		Assertions.assertEquals(TokenTypes.IDENTIFIER, actual.getType());
	}


	@Test
	void testGetTokenToMark_endOfWord() {

		String origContent = "public int foo() {}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(origContent.indexOf('(')); // Should get preceding "foo"

		DefaultOccurrenceMarker marker = new DefaultOccurrenceMarker();
		Token actual = marker.getTokenToMark(textArea);
		Assertions.assertEquals("foo", actual.getLexeme());
		Assertions.assertEquals(TokenTypes.IDENTIFIER, actual.getType());
	}


	@Test
	void testGetTokenToMark_none_atEndOfLineAfterNonWordChar() {

		String origContent = "foo\n.";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(origContent.indexOf('.') + 1); // End of document

		DefaultOccurrenceMarker marker = new DefaultOccurrenceMarker();
		Assertions.assertNull(marker.getTokenToMark(textArea));
	}


	@Test
	void testGetTokenToMark_none_betweenTwoNonWordChars() {

		String origContent = "..";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(1); // Between the two periods

		DefaultOccurrenceMarker marker = new DefaultOccurrenceMarker();
		Assertions.assertNull(marker.getTokenToMark(textArea));
	}


	@Test
	void testGetTokenToMark_none_atStartOfLinePrecedingNonWordChar() {

		String origContent = "foo\n.";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);
		textArea.setCaretPosition(origContent.indexOf('.')); // Start of line before the period

		DefaultOccurrenceMarker marker = new DefaultOccurrenceMarker();
		Assertions.assertNull(marker.getTokenToMark(textArea));
	}


	@Test
	void testIsValidType_validType() {

		String origContent = "public int foo() {}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);

		char[] line = "foo".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.IDENTIFIER, 0);

		DefaultOccurrenceMarker marker = new DefaultOccurrenceMarker();
		Assertions.assertTrue(marker.isValidType(textArea, token));
	}


	@Test
	void testIsValidType_invalidType() {

		String origContent = "public int foo() {}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);

		char[] line = "int".toCharArray();
		TokenImpl token = new TokenImpl(line, 0, line.length - 1, 0, TokenTypes.DATA_TYPE, 0);

		DefaultOccurrenceMarker marker = new DefaultOccurrenceMarker();
		Assertions.assertFalse(marker.isValidType(textArea, token));
	}


	@Test
	void testMarkOccurrences() {

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
