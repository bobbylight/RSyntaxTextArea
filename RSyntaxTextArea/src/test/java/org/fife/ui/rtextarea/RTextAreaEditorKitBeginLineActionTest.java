/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Graphics;
import javax.swing.text.Utilities;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.BeginLineAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitBeginLineActionTest extends AbstractRTextAreaTest {


	/**
	 * Returns a narrow text area with line wrap enabled, so that its single
	 * line of text wraps across several visual rows.
	 */
	private static RSyntaxTextArea createWrappedTextArea() {
		RSyntaxTextArea textArea = new RSyntaxTextArea("   Hello world, this is a long line") {
			@Override
			public Graphics getGraphics() {
				return createTestGraphics();
			}
		};
		textArea.setLineWrap(true);
		textArea.setBounds(0, 0, 60, 400);
		return textArea;
	}


	@Test
	void testActionPerformedImpl_noSelect_lineWrap_firstRow() throws Exception {

		RSyntaxTextArea textArea = createWrappedTextArea();
		textArea.setCaretPosition(5);

		new RTextAreaEditorKit.BeginLineAction("beginLine", false).actionPerformedImpl(null, textArea);

		// With line wrap enabled, we always go to the row start (no toggling
		// to the first non-whitespace char)
		Assertions.assertEquals(0, textArea.getSelectionStart());
		Assertions.assertEquals(0, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_noSelect_lineWrap_laterRow() throws Exception {

		RSyntaxTextArea textArea = createWrappedTextArea();
		int end = textArea.getDocument().getLength();
		textArea.setCaretPosition(end);
		int lastRowStart = Utilities.getRowStart(textArea, end);
		Assertions.assertTrue(lastRowStart > 0, "Text didn't wrap");

		new RTextAreaEditorKit.BeginLineAction("beginLine", false).actionPerformedImpl(null, textArea);

		Assertions.assertEquals(lastRowStart, textArea.getSelectionStart());
		Assertions.assertEquals(lastRowStart, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_select_lineWrap_laterRow() throws Exception {

		RSyntaxTextArea textArea = createWrappedTextArea();
		int end = textArea.getDocument().getLength();
		textArea.setCaretPosition(end);
		int lastRowStart = Utilities.getRowStart(textArea, end);
		Assertions.assertTrue(lastRowStart > 0, "Text didn't wrap");

		new RTextAreaEditorKit.BeginLineAction("beginLine", true).actionPerformedImpl(null, textArea);

		Assertions.assertEquals(lastRowStart, textArea.getSelectionStart());
		Assertions.assertEquals(end, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_noSelect_noLineWrap_whitespaceOnlyLine() {

		RSyntaxTextArea textArea = new RSyntaxTextArea(" \t \nabc");
		textArea.setCaretPosition(1);

		new RTextAreaEditorKit.BeginLineAction("beginLine", false).actionPerformedImpl(null, textArea);

		// No non-whitespace on the line, so we go to the end of the whitespace
		Assertions.assertEquals(3, textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_noSelect_noLineWrap_tabs() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("\t\tfoo");
		textArea.setCaretPosition(5);

		new RTextAreaEditorKit.BeginLineAction("beginLine", false).actionPerformedImpl(null, textArea);

		Assertions.assertEquals(2, textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_noSelect_noLineWrap() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("   Hello world");
		textArea.setCaretPosition(14);

		RTextAreaEditorKit.BeginLineAction action = new RTextAreaEditorKit.BeginLineAction("beginLine", false);

		// First, go to first non-whitespace offset
		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(3, textArea.getSelectionStart());
		Assertions.assertEquals(3, textArea.getSelectionEnd());

		// Then to offset 0
		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(0, textArea.getSelectionStart());
		Assertions.assertEquals(0, textArea.getSelectionEnd());

		// Then back to first non-whitespace offset
		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(3, textArea.getSelectionStart());
		Assertions.assertEquals(3, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_select_noLineWrap() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("   Hello world");
		textArea.setCaretPosition(14);

		RTextAreaEditorKit.BeginLineAction action = new RTextAreaEditorKit.BeginLineAction("beginLine", true);

		// First, go to first non-whitespace offset
		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(3, textArea.getSelectionStart());
		Assertions.assertEquals(14, textArea.getSelectionEnd());

		// Then to offset 0
		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(0, textArea.getSelectionStart());
		Assertions.assertEquals(14, textArea.getSelectionEnd());

		// Then back to first non-whitespace offset
		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(3, textArea.getSelectionStart());
		Assertions.assertEquals(14, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_select_noLineWrap_emptyLine() {

		RSyntaxTextArea textArea = new RSyntaxTextArea();

		RTextAreaEditorKit.BeginLineAction action = new RTextAreaEditorKit.BeginLineAction("beginLine", true);

		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(0, textArea.getSelectionStart());
		Assertions.assertEquals(0, textArea.getSelectionEnd());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals("begin", new RTextAreaEditorKit.BeginLineAction("begin", false).getMacroID());
	}
}
