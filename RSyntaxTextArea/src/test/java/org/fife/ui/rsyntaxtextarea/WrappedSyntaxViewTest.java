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
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;
import java.awt.*;


/**
 * Unit test for the {@link WrappedSyntaxView} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class WrappedSyntaxViewTest extends AbstractRSyntaxTextAreaTest {


	private static void testDocumentEvent_happyPath(DocumentEvent.EventType eventType) {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.addNotify();

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

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.addNotify();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Rectangle r = new Rectangle();
		Shape childAllocation = view.getChildAllocation(0, r);
		Assertions.assertNull(childAllocation);
	}


	@Test
	void testGetMaximumSpan_happyPath() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.addNotify();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Assertions.assertTrue(view.getMaximumSpan(View.X_AXIS) > 0);
	}


	@Test
	void testGetMinimumSpan_happyPath() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.addNotify();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Assertions.assertTrue(view.getMinimumSpan(View.X_AXIS) > 0);
	}


	@Test
	void testGetPreferredSpan_xAxis_happyPath() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.addNotify();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Assertions.assertTrue(view.getPreferredSpan(View.X_AXIS) > 0);
	}


	@Test
	void testGetPreferredSpan_yAxis_happyPath() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.addNotify();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Assertions.assertTrue(view.getPreferredSpan(View.Y_AXIS) > 0);
	}


	@Test
	void testNextTabStop_zero() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.addNotify();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);
		Assertions.assertEquals(0d, view.nextTabStop(0, 0), 0.001);
	}


	@Test
	void testRemoveUpdate_happyPath() {
		testDocumentEvent_happyPath(DocumentEvent.EventType.REMOVE);
	}


	@Test
	void testYForLine_happyPath() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.addNotify();

		WrappedSyntaxView view = (WrappedSyntaxView)textArea.getUI().
			getRootView(textArea).getView(0);

		Rectangle r = new Rectangle(0, 0, 100, 100);
		view.yForLine(r, 0);
	}
}
