/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.parser.*;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;


/**
 * Unit tests for the {@link ParserManager} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class ParserManagerTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testConstructor_oneArg() {
		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);
		Assert.assertEquals(1250, manager.getDelay());
	}


	@Test
	public void testConstructor_twoArg() {
		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(2000, textArea);
		Assert.assertEquals(2000, manager.getDelay());
	}


	@Test
	public void testActionPerformed_parsersFireChangeEvents() {

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

		Assert.assertTrue(parserNoticeChangeEventFired[0]);
	}


	@Test
	public void testAddRemoveParser() {

		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);

		Assert.assertEquals(0, manager.getParserCount());

		AbstractParser parser = new AbstractParser() {
			@Override
			public ParseResult parse(RSyntaxDocument doc, String style) {
				return new DefaultParseResult(this);
			}
		};
		manager.addParser(parser);

		Assert.assertEquals(1, manager.getParserCount());

		Assert.assertTrue(manager.removeParser(parser));
		Assert.assertEquals(0, manager.getParserCount());
	}


	@Test
	public void testGetToolTipText() {

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
		Assert.assertEquals("test", tipInfo.getToolTipText());
	}


	@Test
	public void testPropertyChange_document() {

		RSyntaxTextArea textArea = createTextArea();
		ParserManager manager = new ParserManager(textArea);

		RSyntaxDocument origDocument = (RSyntaxDocument)textArea.getDocument();

		RSyntaxDocument newDocument = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_C);
		textArea.setDocument(newDocument);

		// All we can really verify is that the original document has had the parser's
		// listener removed (along with all others)
		Assert.assertEquals(0, origDocument.getDocumentListeners().length);
	}
}
