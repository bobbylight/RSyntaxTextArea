/*
 * 01/12/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
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
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for {@link RSyntaxDocument}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RSyntaxDocumentTest {

	private RSyntaxDocument doc;


	/**
	 * Inserts code for "Hello world" in C into a document.
	 *
	 * @param doc The document.
	 * @throws Exception If something goes wrong (which should not happen).
	 */
	private static final void insertHelloWorldC(RSyntaxDocument doc)
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
	public void test1ArgConstructor() {
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_JAVA;
		doc = new RSyntaxDocument(syntaxStyle);
		//Assert.assertEquals(syntaxStyle, doc.getSyntaxStyle());
	}
	
	
	@Test
	public void test2ArgConstructor() {
		
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_JAVA;
		
		// Standard case, taking default TokenMakerFactory
		doc = new RSyntaxDocument(null, syntaxStyle);
		//Assert.assertEquals(syntaxStyle, doc.getSyntaxStyle());
		
		// Taking a custom TokenMakerFactory
		TokenMakerFactory customTmf = new AbstractTokenMakerFactory() {
			@Override
			protected void initTokenMakerMap() {
				// Do nothing
			}
		};
		doc = new RSyntaxDocument(customTmf, syntaxStyle);
		//Assert.assertEquals(syntaxStyle, doc.getSyntaxStyle());
		
	}
	
	
	@Test
	public void testFireDocumentEvent_InsertWithNoNewLines() throws Exception {
		
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_JAVA;
		doc = new RSyntaxDocument(syntaxStyle);
		
		TestDocumentListener l = new TestDocumentListener();
		doc.addDocumentListener(l);
		
		// Two events sent - one "change" event containing line range AFTER
		// insert to repaint, second one is actual "insert" event.
		int offs = 0;
		String text = "package org.fife;";
		doc.insertString(offs, text, null);
		Assert.assertEquals(2, l.events.size());
		DocumentEvent e = l.events.get(0);
		// offset and length == start and end lines AFTER insert with new EOL tokens
		assertDocumentEvent(e, DocumentEvent.EventType.CHANGE, 0, 0);
		e = l.events.get(1);
		assertDocumentEvent(e, DocumentEvent.EventType.INSERT, 0, text.length());
		
	}
	
	
	@Test
	public void testFireDocumentEvent_InsertWithTwoNewLines() throws Exception {

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
		Assert.assertEquals(2, l.events.size());
		DocumentEvent e = l.events.get(0);
		// offset and length == start and end lines AFTER insert with new EOL tokens
		// In this case a new line was "added" by this change.
		assertDocumentEvent(e, DocumentEvent.EventType.CHANGE, 8, 8);
		e = l.events.get(1);
		assertDocumentEvent(e, DocumentEvent.EventType.INSERT,
				oldLen - 4, text.length());
		
	}
	
	
	@Test
	public void testFireDocumentEvent_InsertWithTwoNewLinesOneReplaced() throws Exception {

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
		Assert.assertEquals(4, l.events.size());

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
	public void testFireDocumentEvent_RemoveWithinOneLine() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		insertHelloWorldC(doc);

		TestDocumentListener l = new TestDocumentListener();
		doc.addDocumentListener(l);
		
		doc.replace(52, 3, null, null); // Replace "main" with "m"

		// Two events sent - A change/remove pair.
		Assert.assertEquals(2, l.events.size());

		DocumentEvent e = l.events.get(0);
		// offset and length == start and end lines AFTER remove with new EOL
		// tokens. In this case a new line was "added" by this change.
		assertDocumentEvent(e, DocumentEvent.EventType.CHANGE, 3, 3);
		e = l.events.get(1);
		assertDocumentEvent(e, DocumentEvent.EventType.REMOVE, 52, 3);

	}


	@Test
	public void testGetClosestStandardTokenTypeForInternalType() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
		doc = new RSyntaxDocument(syntaxStyle);
		JavaScriptTokenMaker tokenMaker = new JavaScriptTokenMaker();

		for (int i=0; i<TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			int expected = tokenMaker.getClosestStandardTokenTypeForInternalType(i);
			int actual = doc.getClosestStandardTokenTypeForInternalType(i);
			Assert.assertEquals(expected, actual);
		}

	}


	@Test
	public void testGetCompleteMarkupCloseTags() {

		// Non-markup language
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assert.assertFalse(doc.getCompleteMarkupCloseTags());

		// Markup language defaulting to false
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_PHP;
		doc = new RSyntaxDocument(syntaxStyle);
		Assert.assertFalse(doc.getCompleteMarkupCloseTags());

		// Markup language defaulting to true
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc = new RSyntaxDocument(syntaxStyle);
		Assert.assertTrue(doc.getCompleteMarkupCloseTags());

	}


	@Test
	public void testGetCurlyBracesDenoteCodeBlocks() {

		// Language that does use curly braces
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		TokenMaker tokenMaker = new CTokenMaker();
		Assert.assertEquals(tokenMaker.getCurlyBracesDenoteCodeBlocks(0),
				doc.getCurlyBracesDenoteCodeBlocks(0));

		// Language that does not use curly braces
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc.setSyntaxStyle(syntaxStyle);
		tokenMaker = new XMLTokenMaker();
		Assert.assertEquals(tokenMaker.getCurlyBracesDenoteCodeBlocks(0),
				doc.getCurlyBracesDenoteCodeBlocks(0));

		// Language in which some sub-languages do, some don't
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_HTML;
		doc.setSyntaxStyle(syntaxStyle);
		tokenMaker = new HTMLTokenMaker();
		Assert.assertEquals(tokenMaker.getCurlyBracesDenoteCodeBlocks(0),
				doc.getCurlyBracesDenoteCodeBlocks(0));
		Assert.assertEquals(tokenMaker.getCurlyBracesDenoteCodeBlocks(1),
				doc.getCurlyBracesDenoteCodeBlocks(1));
		Assert.assertEquals(tokenMaker.getCurlyBracesDenoteCodeBlocks(2),
				doc.getCurlyBracesDenoteCodeBlocks(2));
		// Sanity
		Assert.assertFalse(tokenMaker.getCurlyBracesDenoteCodeBlocks(0));
		Assert.assertTrue(tokenMaker.getCurlyBracesDenoteCodeBlocks(1));
		Assert.assertTrue(tokenMaker.getCurlyBracesDenoteCodeBlocks(2));

	}


	@Test
	public void testGetLanguageIsMarkup() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assert.assertFalse(doc.getLanguageIsMarkup());

		// Language that does not use curly braces
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc.setSyntaxStyle(syntaxStyle);
		Assert.assertTrue(doc.getLanguageIsMarkup());

	}


	@Test
	public void testGetLastTokenTypeOnLine() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		insertHelloWorldC(doc);

		Assert.assertEquals(TokenTypes.NULL, doc.getLastTokenTypeOnLine(0));
		Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, doc.getLastTokenTypeOnLine(1));
		Assert.assertEquals(TokenTypes.NULL, doc.getLastTokenTypeOnLine(2));

	}


	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetLastTokenTypeOnLine_InvalidIndex() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		insertHelloWorldC(doc);

		Assert.assertEquals(TokenTypes.NULL, doc.getLastTokenTypeOnLine(1000));

	}


	@Test
	public void testGetLineCommentStartAndEnd() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		String[] actual = doc.getLineCommentStartAndEnd(0);
		Assert.assertEquals(2, actual.length);
		Assert.assertEquals("//", actual[0]);
		Assert.assertNull(actual[1]);

		// Language that does not use curly braces
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc.setSyntaxStyle(syntaxStyle);
		actual = doc.getLineCommentStartAndEnd(0);
		Assert.assertEquals(2, actual.length);
		Assert.assertEquals("<!--", actual[0]);
		Assert.assertEquals("-->", actual[1]);

	}


	@Test
	public void testGetMarkOccurrencesOfTokenType() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assert.assertTrue(doc.getMarkOccurrencesOfTokenType(TokenTypes.IDENTIFIER));
		Assert.assertFalse(doc.getMarkOccurrencesOfTokenType(TokenTypes.COMMENT_EOL));

	}


	@Test
	public void testGetOccurrenceMarker() {
		// Not really much we can test here
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assert.assertNotNull(doc.getOccurrenceMarker());
	}


	@Test
	public void testGetShouldIndentNextLine() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		insertHelloWorldC(doc);

		Assert.assertFalse(doc.getShouldIndentNextLine(0));
		Assert.assertFalse(doc.getShouldIndentNextLine(1));
		Assert.assertFalse(doc.getShouldIndentNextLine(2));
		Assert.assertFalse(doc.getShouldIndentNextLine(3));
		Assert.assertTrue(doc.getShouldIndentNextLine(4));

	}


	@Test
	public void testGetSyntaxStyle() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assert.assertEquals(syntaxStyle, doc.getSyntaxStyle());

		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc.setSyntaxStyle(syntaxStyle);
		Assert.assertEquals(syntaxStyle, doc.getSyntaxStyle());

		syntaxStyle = "text/custom";
		doc.setSyntaxStyle(syntaxStyle);
		Assert.assertEquals(syntaxStyle, doc.getSyntaxStyle());

	}


	@Test
	public void testGetTokenListForLine() throws Exception {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		insertHelloWorldC(doc);

		// #include <stdio.h>
		Token t = doc.getTokenListForLine(0);
		Assert.assertTrue(t.is(TokenTypes.PREPROCESSOR, "#include"));
		t = t.getNextToken();
		Assert.assertTrue(t.isSingleChar(TokenTypes.WHITESPACE, ' '));
		t = t.getNextToken();
		Assert.assertTrue(t.isSingleChar(TokenTypes.OPERATOR, '<'));
		t = t.getNextToken();
		Assert.assertTrue(t.is(TokenTypes.IDENTIFIER, "stdio"));
		t = t.getNextToken();
		Assert.assertTrue(t.isSingleChar(TokenTypes.OPERATOR, '.'));
		t = t.getNextToken();
		Assert.assertTrue(t.isSingleChar(TokenTypes.IDENTIFIER, 'h'));
		t = t.getNextToken();
		Assert.assertTrue(t.isSingleChar(TokenTypes.OPERATOR, '>'));
		t = t.getNextToken();
		Assert.assertEquals(new TokenImpl(), t); // Null token

	}


	@Test
	public void testInsertBreakSpecialHandling() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assert.assertTrue(doc.insertBreakSpecialHandling(null));

		// Language that does not use curly braces
		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc.setSyntaxStyle(syntaxStyle);
		Assert.assertFalse(doc.insertBreakSpecialHandling(null));

	}


	@Test
	public void testIsIdentifierChar() {
		
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assert.assertTrue(doc.isIdentifierChar(0, 'a'));
		Assert.assertFalse(doc.isIdentifierChar(0, '%'));

	}


	@Test
	public void testIterator() {
		// Not much to test here
		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assert.assertNotNull(doc.iterator());
	}


	@Test
	public void testSetSyntaxStyle() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assert.assertEquals(syntaxStyle, doc.getSyntaxStyle());

		syntaxStyle = SyntaxConstants.SYNTAX_STYLE_XML;
		doc.setSyntaxStyle(syntaxStyle);
		Assert.assertEquals(syntaxStyle, doc.getSyntaxStyle());

		syntaxStyle = "text/custom";
		doc.setSyntaxStyle(syntaxStyle);
		Assert.assertEquals(syntaxStyle, doc.getSyntaxStyle());

	}


	@Test
	public void testSetSyntaxStyle_CustomTokenMaker() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);
		Assert.assertEquals(syntaxStyle, doc.getSyntaxStyle());

		TokenMaker tokenMaker = new HTMLTokenMaker();
		doc.setSyntaxStyle(tokenMaker);
		Assert.assertNotEquals(syntaxStyle, doc.getSyntaxStyle());

	}


	@Test
	public void testSetTokenMakerFactory() {

		String syntaxStyle = SyntaxConstants.SYNTAX_STYLE_C;
		doc = new RSyntaxDocument(syntaxStyle);

		// By default, we do indeed get syntax highlighting for Java
		doc.setSyntaxStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assert.assertNotNull(doc.getLineCommentStartAndEnd(0));

		// No mappings -> default to PlainTextTokenMaker
		doc.setTokenMakerFactory(new EmptyTokenMakerFactory());
		doc.setSyntaxStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		// Ghetto test to show we are not picking up a JavaTokenMaker
		Assert.assertNull(doc.getLineCommentStartAndEnd(0));

		// Verify restoring default instance
		doc.setTokenMakerFactory(null);
		doc.setSyntaxStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		Assert.assertNotNull(doc.getLineCommentStartAndEnd(0));

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
	private static final void assertDocumentEvent(DocumentEvent e,
			DocumentEvent.EventType eventType, int offs, int len) {
		Assert.assertEquals(eventType, e.getType());
		Assert.assertEquals(offs, e.getOffset());
		Assert.assertEquals(len, e.getLength());
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
		
		public TestDocumentListener() {
			events = new ArrayList<DocumentEvent>();
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