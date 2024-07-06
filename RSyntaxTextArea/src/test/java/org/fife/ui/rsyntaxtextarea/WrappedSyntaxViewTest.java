/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.event.DocumentEvent;
import javax.swing.text.*;
import java.awt.*;


/**
 * Unit test for the {@link WrappedSyntaxView} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class WrappedSyntaxViewTest extends AbstractRSyntaxTextAreaTest {

	private static final String LONG_CODE = "{\n" +
		"  for if while if for if while if for if while do if while for very long line for if {\n" +
		"    if (willBeCollapsed) {\n" +
		"       willBeCollapsedAndHidden();\n" +
		"    }\n" +
		"    while (nestedConditional) {\n" +
		"\n" + // Purposely empty line
		"      doSomething();\n" +
		"    }\n" +
		"  }\n" +
		"}";


	private static RSyntaxTextArea createWrappingTextArea() {
		return createWrappingTextArea(true);
	}


	private static RSyntaxTextArea createWrappingTextArea(boolean wrapStyleWord) {

		RSyntaxTextArea textArea = createTextArea(LONG_CODE);

		// Collapse the "if (willBeCollapsed)" conditional block
		textArea.getFoldManager().getFold(0).getChild(0).getChild(0).
			setCollapsed(true);

		textArea.setBounds(0, 0, 80, 800); // Make taller
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(wrapStyleWord);
		textArea.addNotify();
		return textArea;
	}


	private static void testDocumentEvent_happyPath(DocumentEvent.EventType eventType) {

		RSyntaxTextArea textArea = createWrappingTextArea();
		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		DocumentEvent e = new DocumentEvent() {
			@Override
			public int getOffset() {
				return 0;
			}

			@Override
			public int getLength() {
				return 1;
			}

			@Override
			public Document getDocument() {
				return textArea.getDocument();
			}

			@Override
			public EventType getType() {
				return eventType;
			}

			@Override
			public ElementChange getChange(Element elem) {
				return null;
			}
		};

		Rectangle r = new Rectangle();
		if (eventType == DocumentEvent.EventType.INSERT) {
			view.insertUpdate(e, r, null);
		}
		else if (eventType == DocumentEvent.EventType.REMOVE) {
			view.removeUpdate(e, r, null);
		}
		else {
			view.changedUpdate(e, r, null);
		}
	}


	@Test
	void testGetChildAllocation_notAllocatedYet() {

		RSyntaxTextArea textArea = createWrappingTextArea();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Rectangle r = new Rectangle();
		Shape childAllocation = view.getChildAllocation(0, r);
		Assertions.assertNull(childAllocation);
	}


	@Test
	void testGetMaximumSpan_happyPath() {

		RSyntaxTextArea textArea = createWrappingTextArea();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Assertions.assertTrue(view.getMaximumSpan(View.X_AXIS) > 0);
	}


	@Test
	void testGetMinimumSpan_happyPath() {

		RSyntaxTextArea textArea = createWrappingTextArea();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Assertions.assertTrue(view.getMinimumSpan(View.X_AXIS) > 0);
	}


	@Test
	void testGetPreferredSpan_xAxis_happyPath() {

		RSyntaxTextArea textArea = createWrappingTextArea();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Assertions.assertTrue(view.getPreferredSpan(View.X_AXIS) > 0);
	}


	@Test
	void testGetPreferredSpan_yAxis_happyPath() {

		RSyntaxTextArea textArea = createWrappingTextArea();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Assertions.assertTrue(view.getPreferredSpan(View.Y_AXIS) > 0);
	}


	@Test
	void testModelToView_3Arg_happyPath() throws BadLocationException {

		RSyntaxTextArea textArea = createWrappingTextArea();
		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Shape s = view.modelToView(8, textArea.getBounds(), Position.Bias.Forward);
		Assertions.assertNotNull(s);
	}


	@Test
	void testModelToView_3Arg_error_invalidPosition() {

		RSyntaxTextArea textArea = createWrappingTextArea();
		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		int offs = Integer.MAX_VALUE;
		Assertions.assertThrows(BadLocationException.class, () ->
			view.modelToView(offs, textArea.getBounds(), Position.Bias.Backward));
	}


	@Test
	void testModelToView_5Arg_happyPath() throws BadLocationException {

		RSyntaxTextArea textArea = createWrappingTextArea();
		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Shape s = view.modelToView(8, Position.Bias.Forward,
			12, Position.Bias.Forward, textArea.getBounds());
		Assertions.assertNotNull(s);
	}


	@Test
	void testNextTabStop_zero() {

		RSyntaxTextArea textArea = createWrappingTextArea();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);
		Assertions.assertEquals(0d, view.nextTabStop(0, 0), 0.001);
	}


	@Test
	void testPaint_noSelection() {
		RSyntaxTextArea textArea = createWrappingTextArea();
		textArea.setEOLMarkersVisible(true);
		textArea.paint(createTestGraphics(1000, 1000));
	}


	@Test
	void testPaint_noSelection_wrapStyleWordFalse() {
		RSyntaxTextArea textArea = createWrappingTextArea(false);
		textArea.paint(createTestGraphics(1000, 1000));
	}


	@Test
	void testPaint_selection_exactlyOneToken() {

		RSyntaxTextArea textArea = createWrappingTextArea();

		// Create a multi-line selection that includes empty lines
		// and starts in the middle of a token
		int selStart = LONG_CODE.indexOf("nestedConditional");
		int selEnd = selStart + "nestedConditional".length();
		textArea.setCaretPosition(selStart);
		textArea.moveCaretPosition(selEnd);

		textArea.paint(createTestGraphics(1000, 1000));
	}


	@Test
	void testPaint_selection_startsInMiddleOfTokenAndSpansMultipleLines() {

		RSyntaxTextArea textArea = createWrappingTextArea();
		textArea.setEOLMarkersVisible(true);

		// Create a multi-line selection that includes empty lines
		// and starts in the middle of a token
		int selStart = LONG_CODE.indexOf("nestedConditional") + 2;
		int selEnd = LONG_CODE.indexOf("}", selStart);
		textArea.setCaretPosition(selStart);
		textArea.moveCaretPosition(selEnd);

		textArea.paint(createTestGraphics(1000, 1000));
	}


	@Test
	void testPaint_selection_wrapStyleWordFalse_startsInMiddleOfTokenAndSpansMultipleLines() {

		RSyntaxTextArea textArea = createWrappingTextArea(false);

		// Create a multi-line selection that includes empty lines
		// and starts in the middle of a token
		int selStart = LONG_CODE.indexOf("nestedConditional") + 2;
		int selEnd = LONG_CODE.indexOf("}", selStart);
		textArea.setCaretPosition(selStart);
		textArea.moveCaretPosition(selEnd);

		textArea.paint(createTestGraphics(1000, 1000));
	}


	@Test
	void testRemoveUpdate_happyPath() {
		testDocumentEvent_happyPath(DocumentEvent.EventType.REMOVE);
	}


	@Test
	void testYForLine_happyPath() throws BadLocationException {

		RSyntaxTextArea textArea = createWrappingTextArea();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Rectangle r = new Rectangle(0, 0, 100, 100);
		view.yForLine(r, 0);
	}
}
