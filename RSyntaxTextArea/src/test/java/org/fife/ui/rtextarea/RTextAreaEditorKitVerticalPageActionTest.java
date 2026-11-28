/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for the {@link RTextAreaEditorKit.VerticalPageAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitVerticalPageActionTest {


	/**
	 * Returns a text area with many lines, in a scroll pane much shorter
	 * than the text area, so only a small part of the text is visible.
	 */
	private static RTextArea createScrollableTextArea() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 200; i++) {
			sb.append("line ").append(i).append('\n');
		}
		RTextArea textArea = new RTextArea(sb.toString());

		JScrollPane sp = new JScrollPane(textArea);
		sp.setSize(300, 150);
		sp.doLayout();
		sp.getViewport().doLayout();
		return textArea;
	}


	private static JViewport getViewport(RTextArea textArea) {
		return (JViewport)textArea.getParent();
	}


	private static void assertCaretVisible(RTextArea textArea) throws BadLocationException {
		Rectangle caretBounds = textArea.modelToView2D(textArea.getCaretPosition()).getBounds();
		Assertions.assertTrue(textArea.getVisibleRect().contains(caretBounds.getLocation()),
			"Caret not visible: " + caretBounds + ", visible rect: " + textArea.getVisibleRect());
	}


	@Test
	void testActionPerformedImpl_down_caretVisible_scrollsAndMovesCaret() throws Exception {

		RTextArea textArea = createScrollableTextArea();
		textArea.setCaretPosition(0);

		new RTextAreaEditorKit.VerticalPageAction("foo", 1, false).actionPerformedImpl(null, textArea);

		Assertions.assertTrue(getViewport(textArea).getViewPosition().y > 0);
		Assertions.assertTrue(textArea.getCaretLineNumber() > 0);
		assertCaretVisible(textArea);
	}


	@Test
	void testActionPerformedImpl_up_caretVisible_scrollsAndMovesCaret() throws Exception {

		RTextArea textArea = createScrollableTextArea();
		JViewport viewport = getViewport(textArea);
		viewport.setViewPosition(new Point(0, 1000));
		int topLine = textArea.getLineOfOffset(textArea.viewToModel2D(new Point(0, 1000)));
		textArea.setCaretPosition(textArea.getLineStartOffset(topLine + 2));

		new RTextAreaEditorKit.VerticalPageAction("foo", -1, false).actionPerformedImpl(null, textArea);

		Assertions.assertTrue(viewport.getViewPosition().y < 1000);
		Assertions.assertTrue(textArea.getCaretLineNumber() < topLine);
		assertCaretVisible(textArea);
	}


	@Test
	void testActionPerformedImpl_down_caretNotVisible_movesCaretToBottomOfNewView() throws Exception {

		RTextArea textArea = createScrollableTextArea();
		JViewport viewport = getViewport(textArea);
		textArea.setCaretPosition(0);
		viewport.setViewPosition(new Point(0, 1000));
		int topLine = textArea.getLineOfOffset(textArea.viewToModel2D(new Point(0, 1000)));

		new RTextAreaEditorKit.VerticalPageAction("foo", 1, false).actionPerformedImpl(null, textArea);

		Assertions.assertTrue(viewport.getViewPosition().y > 1000);
		Assertions.assertTrue(textArea.getCaretLineNumber() > topLine);
	}


	@Test
	void testActionPerformedImpl_up_caretNotVisible_movesCaretToTopOfNewView() throws Exception {

		RTextArea textArea = createScrollableTextArea();
		JViewport viewport = getViewport(textArea);
		textArea.setCaretPosition(textArea.getDocument().getLength());
		viewport.setViewPosition(new Point(0, 1000));
		int topLine = textArea.getLineOfOffset(textArea.viewToModel2D(new Point(0, 1000)));

		new RTextAreaEditorKit.VerticalPageAction("foo", -1, false).actionPerformedImpl(null, textArea);

		Assertions.assertTrue(viewport.getViewPosition().y < 1000);
		Assertions.assertTrue(textArea.getCaretLineNumber() < topLine);
		assertCaretVisible(textArea);
	}


	@Test
	void testActionPerformedImpl_down_atEndOfDocument_caretStaysPut() {

		RTextArea textArea = createScrollableTextArea();
		JViewport viewport = getViewport(textArea);
		int end = textArea.getDocument().getLength();
		textArea.setCaretPosition(end);
		int maxY = textArea.getHeight() - viewport.getExtentSize().height;
		viewport.setViewPosition(new Point(0, maxY));

		new RTextAreaEditorKit.VerticalPageAction("foo", 1, false).actionPerformedImpl(null, textArea);

		Assertions.assertEquals(end, textArea.getCaretPosition());
		Assertions.assertEquals(maxY, viewport.getViewPosition().y);
	}


	@Test
	void testActionPerformedImpl_preservesMagicCaretPosition() {

		RTextArea textArea = createScrollableTextArea();
		textArea.setCaretPosition(0);
		Point magic = new Point(20, 0);
		textArea.getCaret().setMagicCaretPosition(magic);

		new RTextAreaEditorKit.VerticalPageAction("foo", 1, false).actionPerformedImpl(null, textArea);

		Assertions.assertEquals(magic, textArea.getCaret().getMagicCaretPosition());
	}


	@Test
	void testActionPerformedImpl_up_noSelect() {

		String content = "line 1\nline 2\nline 3";

		RTextArea textArea = new RTextArea(content);
		textArea.setCaretPosition(content.indexOf('1'));
		textArea.setBounds(0, 0, 800, 800);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.VerticalPageAction("foo", -1, false).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(5, textArea.getCaretPosition()); // End of first line
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_up_select() {

		String content = "line 1\nline 2\nline 3";

		RTextArea textArea = new RTextArea(content);
		textArea.setBounds(0, 0, 800, 800);
		textArea.setCaretPosition(content.indexOf('1'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.VerticalPageAction("foo", -1, true).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(5, textArea.getCaretPosition()); // End of first line
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_down_noSelect() {

		String content = "line 1\nline 2\nline 3";

		RTextArea textArea = new RTextArea(content);
		textArea.setBounds(0, 0, 800, 800);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.VerticalPageAction("foo", 1, false).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(20, textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_down_select() {

		String content = "line 1\nline 2\nline 3";

		RTextArea textArea = new RTextArea(content);
		textArea.setBounds(0, 0, 800, 800);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.VerticalPageAction("foo", 1, true).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(20, textArea.getCaretPosition());
		Assertions.assertEquals("line 1\nline 2\nline 3", textArea.getSelectedText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals("foo",
			new RTextAreaEditorKit.VerticalPageAction("foo", 1, false).getMacroID());
	}
}
