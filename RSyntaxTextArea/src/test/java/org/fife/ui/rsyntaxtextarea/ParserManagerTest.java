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
	void testActionPerformed_parsersFireChangeEvents() {

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

		textArea.forceReparsing(parser);

		Assertions.assertTrue(parserNoticeChangeEventFired[0]);
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
		RTextScrollPane sp = new RTextScrollPane(textArea); // text area needs a parent
		ParserManager manager = new ParserManager(textArea);
		manager.addParser(parser);

		manager.forceReparsing(0);

		MouseEvent e = new MouseEvent(textArea, 0, 0, 0, 2, 2, 1, false);
		ToolTipInfo tipInfo = manager.getToolTipText(e);
		Assertions.assertEquals("test", tipInfo.getToolTipText());
	}


	@Test
	void testPropertyChange_document() {

		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);

		RSyntaxDocument origDocument = (RSyntaxDocument)textArea.getDocument();

		RSyntaxDocument newDocument = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_C);
		textArea.setDocument(newDocument);

		// All we can really verify is that the original document has had the parser's
		// listener removed (along with all others)
		Assertions.assertEquals(0, origDocument.getDocumentListeners().length);
	}
}
