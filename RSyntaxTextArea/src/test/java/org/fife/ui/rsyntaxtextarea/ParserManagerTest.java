/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.parser.*;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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


	@Test
	void testConstructor_oneArg() {
		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);
		Assertions.assertEquals(1250, manager.getDelay());
	}


	@Test
	void testConstructor_twoArg() {
		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(2000, textArea);
		Assertions.assertEquals(2000, manager.getDelay());
	}


	@Test
	void testActionPerformed_noParsers() {

		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);
		Assertions.assertEquals(0, manager.getParserNotices().size());

		manager.actionPerformed(new ActionEvent(textArea, 0, null));
		Assertions.assertEquals(0, manager.getParserNotices().size());
	}


	@Test
	void testActionPerformed_parsersFireChangeEvents() {

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
		manager.addParser(parser);
		Assertions.assertEquals(0, manager.getParserNotices().size());

		manager.actionPerformed(new ActionEvent(textArea, 0, null));
		Assertions.assertEquals(1, manager.getParserNotices().size());
		Assertions.assertEquals("test", manager.getParserNotices().get(0).getMessage());
	}


	@Test
	void testAddRemoveParser() {

		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);

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


	@Test
	void testClearParsers() {

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
		manager.addParser(parser);
		Assertions.assertEquals(1, manager.getParserCount());

		manager.actionPerformed(new ActionEvent(textArea, 0, null));
		Assertions.assertEquals(1, manager.getParserNotices().size());
		Assertions.assertEquals("test", manager.getParserNotices().get(0).getMessage());

		manager.clearParsers();
		Assertions.assertEquals(0, manager.getParserNotices().size());
		Assertions.assertEquals(0, manager.getParserCount());
	}


	@Test
	void testForceReparsing_parsersFireChangeEvents() {

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


	@Test
	void testForceReparsing_disabledParserClearsItsNotices() {

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


	@Test
	void testGetSetDelay() {

		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);

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


	@Test
	void testGetToolTipText() {

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
		new RTextScrollPane(textArea); // text area needs a parent
		ParserManager manager = new ParserManager(textArea);
		manager.addParser(parser);

		manager.forceReparsing(0);

		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 2, 2, 1, false);
		ToolTipInfo tipInfo = manager.getToolTipText(e);
		Assertions.assertEquals("test", tipInfo.getToolTipText());
	}


	@Test
	void testInsertUpdate() {

		RSyntaxTextArea textArea = createTextArea();
		new ParserManager(textArea);

		textArea.insert("inserted text", 5);
	}


	@Test
	void testPropertyChange_document() {

		RSyntaxTextArea textArea = createTextArea();
		new ParserManager(textArea);
		RSyntaxDocument origDocument = (RSyntaxDocument)textArea.getDocument();

		RSyntaxDocument newDocument = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_C);
		textArea.setDocument(newDocument);

		// All we can really verify is that the original document has had the parser's
		// listener removed (along with all others)
		Assertions.assertEquals(0, origDocument.getDocumentListeners().length);
	}


	@Test
	void testRemoveUpdate() {

		RSyntaxTextArea textArea = createTextArea();
		new ParserManager(textArea);

		textArea.replaceRange("", 5, 9);
	}
}
