/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link RTextAreaEditorKit.BeginLineAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RTextAreaEditorKitBeginLineActionTest extends AbstractRTextAreaTest {


	@Test
	@Disabled("Need a displayable text area")
	void testActionPerformedImpl_noSelect_lineWrap() {
	}


	@Test
	@Disabled("Need a displayable text area")
	void testActionPerformedImpl_select_lineWrap() {
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
