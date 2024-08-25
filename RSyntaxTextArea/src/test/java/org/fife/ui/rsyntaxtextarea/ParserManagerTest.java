/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.parser.*;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;


/**
 * Unit tests for the {@link ParserManager} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class ParserManagerTest extends AbstractRSyntaxTextAreaTest {


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testConstructor_oneArg(String debugParsing) {
		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);
		manager.setDebugParsing(Boolean.parseBoolean(debugParsing));
		Assertions.assertEquals(1250, manager.getDelay());
	}


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testConstructor_twoArg(String debugParsing) {
		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(2000, textArea);
		manager.setDebugParsing(Boolean.parseBoolean(debugParsing));
		Assertions.assertEquals(2000, manager.getDelay());
	}


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testActionPerformed_noParsers(String debugParsing) {

		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);
		manager.setDebugParsing(Boolean.parseBoolean(debugParsing));
		Assertions.assertEquals(0, manager.getParserNotices().size());

		manager.actionPerformed(new ActionEvent(textArea, 0, null));
		Assertions.assertEquals(0, manager.getParserNotices().size());
	}


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testActionPerformed_parsersFireChangeEvents(String debugParsing) {

		AbstractParser parser = new AbstractParser() {
			@Override
			public ParseResult parse(RSyntaxDocument doc, String style) {
				DefaultParseResult result = new DefaultParseResult(this);
				result.addNotice(new DefaultParserNotice(this, "test", 1));
				return result;
			}
		};


		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);
		manager.setDebugParsing(Boolean.parseBoolean(debugParsing));
		manager.addParser(parser);
		Assertions.assertEquals(0, manager.getParserNotices().size());

		manager.actionPerformed(new ActionEvent(textArea, 0, null));
		Assertions.assertEquals(1, manager.getParserNotices().size());
		Assertions.assertEquals("test", manager.getParserNotices().get(0).getMessage());
	}


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testAddRemoveParser(String debugParsing) {

		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);
		manager.setDebugParsing(Boolean.parseBoolean(debugParsing));

		Assertions.assertEquals(0, manager.getParserCount());

		AbstractParser parser = new AbstractParser() {
			@Override
			public ParseResult parse(RSyntaxDocument doc, String style) {
				return new DefaultParseResult(this);
			}
		};
		manager.addParser(parser);

		Assertions.assertEquals(1, manager.getParserCount());

		Assertions.assertTrue(manager.removeParser(parser));
		Assertions.assertEquals(0, manager.getParserCount());
	}


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testClearParsers(String debugParsing) {

		AbstractParser parser = new AbstractParser() {
			@Override
			public ParseResult parse(RSyntaxDocument doc, String style) {
				DefaultParseResult result = new DefaultParseResult(this);
				result.addNotice(new DefaultParserNotice(this, "test", 1));
				return result;
			}
		};


		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);
		manager.setDebugParsing(Boolean.parseBoolean(debugParsing));
		manager.addParser(parser);
		Assertions.assertEquals(1, manager.getParserCount());

		manager.actionPerformed(new ActionEvent(textArea, 0, null));
		Assertions.assertEquals(1, manager.getParserNotices().size());
		Assertions.assertEquals("test", manager.getParserNotices().get(0).getMessage());

		manager.clearParsers();
		Assertions.assertEquals(0, manager.getParserNotices().size());
		Assertions.assertEquals(0, manager.getParserCount());
	}


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testForceReparsing_parsersFireChangeEvents(String debugParsing) {

		boolean[] parserNoticeChangeEventFired = { false };

		AbstractParser parser = new AbstractParser() {
			@Override
			public ParseResult parse(RSyntaxDocument doc, String style) {
				DefaultParseResult result = new DefaultParseResult(this);
				result.addNotice(new DefaultParserNotice(this, "test", 1));
				return result;
			}
		};

		RSyntaxTextArea textArea = createTextArea();
		// Remove the fold parser so our test parser is the only one
		textArea.setCodeFoldingEnabled(false);
		textArea.addParser(parser);
		PropertyChangeListener listener = evt -> parserNoticeChangeEventFired[0] = true;
		textArea.addPropertyChangeListener(RSyntaxTextArea.PARSER_NOTICES_PROPERTY, listener);

		Assertions.assertTrue(textArea.forceReparsing(parser));

		Assertions.assertTrue(parserNoticeChangeEventFired[0]);
	}


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testForceReparsing_disabledParserClearsItsNotices(String debugParsing) {

		AbstractParser parser = new AbstractParser() {
			@Override
			public ParseResult parse(RSyntaxDocument doc, String style) {
				DefaultParseResult result = new DefaultParseResult(this);
				result.addNotice(new DefaultParserNotice(this, "test", 1));
				return result;
			}
		};

		RSyntaxTextArea textArea = createTextArea();
		// Remove the fold parser so our test parser is the only one
		textArea.setCodeFoldingEnabled(false);
		textArea.addParser(parser);

		// Initial run - adds notices to the text area.
		Assertions.assertTrue(textArea.forceReparsing(parser));
		Assertions.assertFalse(textArea.getParserNotices().isEmpty());

		// After being disabled and rerun - clears notices from the text area.
		parser.setEnabled(false);
		Assertions.assertTrue(textArea.forceReparsing(parser));
		Assertions.assertTrue(textArea.getParserNotices().isEmpty());

	}


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testGetSetDelay(String debugParsing) {

		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);
		manager.setDebugParsing(Boolean.parseBoolean(debugParsing));

		manager.setDelay(12345);
		Assertions.assertEquals(12345, manager.getDelay());

		manager.restartParsing();
		try {
			manager.setDelay(54321);
			Assertions.assertEquals(54321, manager.getDelay());
		} finally {
			manager.stopParsing();
		}
	}


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testGetToolTipText(String debugParsing) {

		AbstractParser parser = new AbstractParser() {
			@Override
			public ParseResult parse(RSyntaxDocument doc, String style) {
				DefaultParseResult result = new DefaultParseResult(this);
				result.addNotice(new DefaultParserNotice(this, "test", 0));
				return result;
			}
		};

		RSyntaxTextArea textArea = createTextArea();
		textArea.addNotify();
		textArea.setAntiAliasingEnabled(false); // Needed to initialize font metrics cache
		RTextScrollPane sp = new RTextScrollPane(textArea); // text area needs a parent
		ParserManager manager = new ParserManager(textArea);
		manager.setDebugParsing(Boolean.parseBoolean(debugParsing));
		manager.addParser(parser);

		manager.forceReparsing(0);

		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 2, 2, 1, false);
		ToolTipInfo tipInfo = manager.getToolTipText(e);
		Assertions.assertEquals("test", tipInfo.getToolTipText());
	}


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testInsertUpdate(String debugParsing) {

		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);
		manager.setDebugParsing(Boolean.parseBoolean(debugParsing));

		textArea.insert("inserted text", 5);
	}


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testPropertyChange_document(String debugParsing) {

		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);
		manager.setDebugParsing(Boolean.parseBoolean(debugParsing));
		RSyntaxDocument origDocument = (RSyntaxDocument)textArea.getDocument();

		RSyntaxDocument newDocument = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_C);
		textArea.setDocument(newDocument);

		// All we can really verify is that the original document has had the parser's
		// listener removed (along with all others)
		Assertions.assertEquals(0, origDocument.getDocumentListeners().length);
	}


	@ParameterizedTest
	@ValueSource(strings = { "true", "false" })
	void testRemoveUpdate(String debugParsing) {

		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);
		manager.setDebugParsing(Boolean.parseBoolean(debugParsing));

		textArea.replaceRange("", 5, 9);
	}
}
