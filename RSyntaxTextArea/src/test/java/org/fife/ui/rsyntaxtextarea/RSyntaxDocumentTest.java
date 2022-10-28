/*
 * 01/12/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.modes.CTokenMaker;
import org.fife.ui.rsyntaxtextarea.modes.HTMLTokenMaker;
import org.fife.ui.rsyntaxtextarea.modes.JavaScriptTokenMaker;
import org.fife.ui.rsyntaxtextarea.modes.XMLTokenMaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for {@link RSyntaxDocument}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RSyntaxDocumentTest {

	private RSyntaxDocument doc;


	/**
	 * Inserts code for "Hello world" in C into a document.
	 *
	 * @param doc The document.
	 * @throws Exception If something goes wrong (which should not happen).
	 */
	private static void insertHelloWorldC(RSyntaxDocument doc)
			throws Exception {
		String str = "#include <stdio.h>\n"
				+ "/*\n"
				+ " * Multi-line comment */\n"
				+ "int main(int argc, char **argv)\n"
				+ "{\n"
				+ "    printf(\"Hello world!\n\");\n"
				+ "\n"
				+ "}\n";
		doc.insertString(0, str, null);
	}


	@Test
	void test1ArgConstructor() {
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_JAVA;
		doc = new RSyntaxDocument(syntaxStyle);
		//Assertions.assertEquals(syntaxStyle, doc.getSyntaxStyle());
	}


	@Test
	void test2ArgConstructor() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_JAVA;

		// Standard case, taking default TokenMakerFactory
		doc = new RSyntaxDocument(null, syntaxStyle);
		//Assertions.assertEquals(syntaxStyle, doc.getSyntaxStyle());

		// Taking a custom TokenMakerFactory
		TokenMakerFactory customTmf = new AbstractTokenMakerFactory() {
			@Override
			protected void initTokenMakerMap() {
				// Do nothing
			}
		};
		doc = new RSyntaxDocument(customTmf, syntaxStyle);
		//Assertions.assertEquals(syntaxStyle, doc.getSyntaxStyle());

	}


	@Test
	void testFireDocumentEvent_InsertWithNoNewLines() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_JAVA;
		doc = new RSyntaxDocument(syntaxStyle);

		TestDocumentListener l = new TestDocumentListener();
		doc.addDocumentListener(l);

		// Two events sent - one "change" event containing line range AFTER
		// insert to repaint, second one is actual "insert" event.
		int offs = 0;
		String text = "package org.fife;";
		doc.insertString(offs, text, null);
		Assertions.assertEquals(2, l.events.size());
		DocumentEvent e = l.events.get(0);
		// offset and length == start and end lines AFTER insert with new EOL tokens
		assertDocumentEvent(e, DocumentEvent.EventType.CHANGE, 0, 0);
		e = l.events.get(1);
		assertDocumentEvent(e, DocumentEvent.EventType.INSERT, 0, text.length());

	}


	@Test
	void testFireDocumentEvent_InsertWithTwoNewLines() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		insertHelloWorldC(doc);

		TestDocumentListener l = new TestDocumentListener();
		doc.addDocumentListener(l);

		// Two events sent - one "change" event containing line range AFTER
		// insert repaint, second one is actual "insert" event.
		int oldLen = doc.getLength();
		String text = "// Inserted line 1\nprintf(\"This is working\n\");";
		doc.insertString(oldLen - 4, text, null);
		Assertions.assertEquals(2, l.events.size());
		DocumentEvent e = l.events.get(0);
		// offset and length == start and end lines AFTER insert with new EOL tokens
		// In this case a new line was "added" by this change.
		assertDocumentEvent(e, DocumentEvent.EventType.CHANGE, 8, 8);
		e = l.events.get(1);
		assertDocumentEvent(e, DocumentEvent.EventType.INSERT,
				oldLen - 4, text.length());

	}


	@Test
	void testFireDocumentEvent_InsertWithTwoNewLinesOneReplaced() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		insertHelloWorldC(doc);

		TestDocumentListener l = new TestDocumentListener();
		doc.addDocumentListener(l);

		int oldLen = doc.getLength();
		String text = "// Inserted line 1\nprintf(\"This is working\n\");";
		doc.replace(oldLen - 4, 1, text, null);

		// Four events sent - One change/remove pair and one change/insert
		// pair.  Remove is the one line replaced.
		Assertions.assertEquals(4, l.events.size());

		DocumentEvent e = l.events.get(0);
		// offset and length == start and end lines AFTER remove with new EOL
		// tokens. In this case a new line was "added" by this change.
		assertDocumentEvent(e, DocumentEvent.EventType.CHANGE, 6, 6);
		e = l.events.get(1);
		assertDocumentEvent(e, DocumentEvent.EventType.REMOVE,
				oldLen - 4, 1);

		e = l.events.get(2);
		// offset and length == start and end lines AFTER insert with new EOL
		// tokens.  In this case a new line was "added" by this change.
		assertDocumentEvent(e, DocumentEvent.EventType.CHANGE, 8, 8);
		e = l.events.get(3);
		assertDocumentEvent(e, DocumentEvent.EventType.INSERT,
				oldLen - 4, text.length());
	}


	@Test
	void testFireDocumentEvent_RemoveWithinOneLine() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		insertHelloWorldC(doc);

		TestDocumentListener l = new TestDocumentListener();
		doc.addDocumentListener(l);

		doc.replace(52, 3, null, null); // Replace "main" with "m"

		// Two events sent - A change/remove pair.
		Assertions.assertEquals(2, l.events.size());

		DocumentEvent e = l.events.get(0);
		// offset and length == start and end lines AFTER remove with new EOL
		// tokens. In this case a new line was "added" by this change.
		assertDocumentEvent(e, DocumentEvent.EventType.CHANGE, 3, 3);
		e = l.events.get(1);
		assertDocumentEvent(e, DocumentEvent.EventType.REMOVE, 52, 3);

	}


	@Test
	void testGetClosestStandardTokenTypeForInternalType() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
		doc = new RSyntaxDocument(syntaxStyle);
		JavaScriptTokenMaker tokenMaker = new JavaScriptTokenMaker();

		for (int i=0; i<TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			int expected = tokenMaker.getClosestStandardTokenTypeForInternalType(i);
			int actual = doc.getClosestStandardTokenTypeForInternalType(i);
			Assertions.assertEquals(expected, actual);
		}

	}


	@Test
	void testGetCompleteMarkupCloseTags() {

		// Non-markup language
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assertions.assertFalse(doc.getCompleteMarkupCloseTags());

		// Markup language defaulting to false
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_PHP;
		doc = new RSyntaxDocument(syntaxStyle);
		Assertions.assertFalse(doc.getCompleteMarkupCloseTags());

		// Markup language defaulting to true
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc = new RSyntaxDocument(syntaxStyle);
		Assertions.assertTrue(doc.getCompleteMarkupCloseTags());

	}


	@Test
	void testGetCurlyBracesDenoteCodeBlocks() {

		// Language that does use curly braces
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		TokenMaker tokenMaker = new CTokenMaker();
		Assertions.assertEquals(tokenMaker.getCurlyBracesDenoteCodeBlocks(0),
				doc.getCurlyBracesDenoteCodeBlocks(0));

		// Language that does not use curly braces
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc.setSyntaxStyle(syntaxStyle);
		tokenMaker = new XMLTokenMaker();
		Assertions.assertEquals(tokenMaker.getCurlyBracesDenoteCodeBlocks(0),
				doc.getCurlyBracesDenoteCodeBlocks(0));

		// Language in which some sub-languages do, some don't
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_HTML;
		doc.setSyntaxStyle(syntaxStyle);
		tokenMaker = new HTMLTokenMaker();
		Assertions.assertEquals(tokenMaker.getCurlyBracesDenoteCodeBlocks(0),
				doc.getCurlyBracesDenoteCodeBlocks(0));
		Assertions.assertEquals(tokenMaker.getCurlyBracesDenoteCodeBlocks(1),
				doc.getCurlyBracesDenoteCodeBlocks(1));
		Assertions.assertEquals(tokenMaker.getCurlyBracesDenoteCodeBlocks(2),
				doc.getCurlyBracesDenoteCodeBlocks(2));
		// Sanity
		Assertions.assertFalse(tokenMaker.getCurlyBracesDenoteCodeBlocks(0));
		Assertions.assertTrue(tokenMaker.getCurlyBracesDenoteCodeBlocks(1));
		Assertions.assertTrue(tokenMaker.getCurlyBracesDenoteCodeBlocks(2));

	}


	@Test
	void testGetLanguageIsMarkup() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assertions.assertFalse(doc.getLanguageIsMarkup());

		// Language that does not use curly braces
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc.setSyntaxStyle(syntaxStyle);
		Assertions.assertTrue(doc.getLanguageIsMarkup());

	}


	@Test
	void testGetLastTokenTypeOnLine() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		insertHelloWorldC(doc);

		Assertions.assertEquals(TokenTypes.NULL, doc.getLastTokenTypeOnLine(0));
		Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, doc.getLastTokenTypeOnLine(1));
		Assertions.assertEquals(TokenTypes.NULL, doc.getLastTokenTypeOnLine(2));

	}


	@Test
	void testGetLastTokenTypeOnLine_InvalidIndex() {

		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {

			String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
			doc = new RSyntaxDocument(syntaxStyle);
			insertHelloWorldC(doc);

			Assertions.assertEquals(TokenTypes.NULL, doc.getLastTokenTypeOnLine(1000));
		});
	}


	@Test
	public void testGetLineCommentStartAndEnd() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		String[] actual = doc.getLineCommentStartAndEnd(0);
		Assertions.assertEquals(2, actual.length);
		Assertions.assertEquals("//", actual[0]);
		Assertions.assertNull(actual[1]);

		// Language that does not use curly braces
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc.setSyntaxStyle(syntaxStyle);
		actual = doc.getLineCommentStartAndEnd(0);
		Assertions.assertEquals(2, actual.length);
		Assertions.assertEquals("<!--", actual[0]);
		Assertions.assertEquals("-->", actual[1]);

	}


	@Test
	void testCommon_getMarkOccurrencesOfTokenType() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assertions.assertTrue(doc.getMarkOccurrencesOfTokenType(TokenTypes.IDENTIFIER));
		Assertions.assertFalse(doc.getMarkOccurrencesOfTokenType(TokenTypes.COMMENT_EOL));

	}


	@Test
	void testGetOccurrenceMarker() {
		// Not really much we can test here
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assertions.assertNotNull(doc.getOccurrenceMarker());
	}


	@Test
	void testGetShouldIndentNextLine() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		insertHelloWorldC(doc);

		Assertions.assertFalse(doc.getShouldIndentNextLine(0));
		Assertions.assertFalse(doc.getShouldIndentNextLine(1));
		Assertions.assertFalse(doc.getShouldIndentNextLine(2));
		Assertions.assertFalse(doc.getShouldIndentNextLine(3));
		Assertions.assertTrue(doc.getShouldIndentNextLine(4));

	}


	@Test
	void testGetSyntaxStyle() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assertions.assertEquals(syntaxStyle, doc.getSyntaxStyle());

		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc.setSyntaxStyle(syntaxStyle);
		Assertions.assertEquals(syntaxStyle, doc.getSyntaxStyle());

		syntaxStyle = "text/custom";
		doc.setSyntaxStyle(syntaxStyle);
		Assertions.assertEquals(syntaxStyle, doc.getSyntaxStyle());

	}


	@Test
	void testGetTokenListForLine() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		insertHelloWorldC(doc);

		// #include <stdio.h>
		Token t = doc.getTokenListForLine(0);
		Assertions.assertTrue(t.is(TokenTypes.PREPROCESSOR, "#include"));
		t = t.getNextToken();
		Assertions.assertTrue(t.isSingleChar(TokenTypes.WHITESPACE, ' '));
		t = t.getNextToken();
		// Note CTokenMaker uses the "string" token type for included files
		Assertions.assertTrue(t.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "<stdio.h>"));
		t = t.getNextToken();
		Assertions.assertEquals(new TokenImpl(), t); // Null token

	}


	@Test
	void testInsertBreakSpecialHandling() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assertions.assertTrue(doc.insertBreakSpecialHandling(null));

		// Language that does not use curly braces
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc.setSyntaxStyle(syntaxStyle);
		Assertions.assertFalse(doc.insertBreakSpecialHandling(null));

	}


	@Test
	void testIsIdentifierChar() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assertions.assertTrue(doc.isIdentifierChar(0, 'a'));
		Assertions.assertFalse(doc.isIdentifierChar(0, '%'));

	}


	@Test
	void testIterator() {
		// Not much to test here
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assertions.assertNotNull(doc.iterator());
	}


	@Test
	void testSetSyntaxStyle() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assertions.assertEquals(syntaxStyle, doc.getSyntaxStyle());

		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc.setSyntaxStyle(syntaxStyle);
		Assertions.assertEquals(syntaxStyle, doc.getSyntaxStyle());

		syntaxStyle = "text/custom";
		doc.setSyntaxStyle(syntaxStyle);
		Assertions.assertEquals(syntaxStyle, doc.getSyntaxStyle());

	}


	@Test
	void testSetSyntaxStyle_CustomTokenMaker() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assertions.assertEquals(syntaxStyle, doc.getSyntaxStyle());

		TokenMaker tokenMaker = new HTMLTokenMaker();
		doc.setSyntaxStyle(tokenMaker);
		Assertions.assertNotEquals(syntaxStyle, doc.getSyntaxStyle());

	}


	@Test
	void testSetTokenMakerFactory() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);

		// By default, we do indeed get syntax highlighting for Java
		doc.setSyntaxStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertNotNull(doc.getLineCommentStartAndEnd(0));

		// No mappings -> default to PlainTextTokenMaker
		doc.setTokenMakerFactory(new EmptyTokenMakerFactory());
		doc.setSyntaxStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		// Ghetto test to show we are not picking up a JavaTokenMaker
		Assertions.assertNull(doc.getLineCommentStartAndEnd(0));

		// Verify restoring default instance
		doc.setTokenMakerFactory(null);
		doc.setSyntaxStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assertions.assertNotNull(doc.getLineCommentStartAndEnd(0));

	}


	/**
	 * Verifies that the type, offset, and length of a
	 * <code>DocumentEvent</code> have expected values.
	 *
	 * @param e The event to check.
	 * @param eventType The expected event type.
	 * @param offs The expected offset.
	 * @param len The expected length.
	 * @throws AssertionError If any value is not as expected.
	 */
	private static void assertDocumentEvent(DocumentEvent e,
											DocumentEvent.EventType eventType, int offs, int len) {
		Assertions.assertEquals(eventType, e.getType());
		Assertions.assertEquals(offs, e.getOffset());
		Assertions.assertEquals(len, e.getLength());
	}


	/**
	 * A token maker factory with no mappings to languages.
	 */
	private static class EmptyTokenMakerFactory
			extends AbstractTokenMakerFactory {

		@Override
		protected void initTokenMakerMap() {
			// Do nothing
		}

	}


	/**
	 * Aggregates document events for examination.
	 */
	private static class TestDocumentListener implements DocumentListener {

		private List<DocumentEvent> events;

		protected TestDocumentListener() {
			events = new ArrayList<>();
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			events.add(e);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			events.add(e);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			events.add(e);
		}

	}


}
