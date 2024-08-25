/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.EndAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitEndActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testActionPerformedImpl_folding_selectFalse() {

		String text = "{\n  printf(\"Hi\");\n  {\n    printf(\"Bye\");\n  }\n}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, text);
		textArea.setCaretPosition(0);
		textArea.getFoldManager().getFold(0).setCollapsed(true);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.EndAction("foo", false).actionPerformedImpl(e, textArea);

		// Caret is at the end of the first line (last visible offset => after the first "{"
		Assertions.assertEquals(1, textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_folding_selectTrue() {

		String text = "{\n  printf(\"Hi\");\n  {\n    printf(\"Bye\");\n  }\n}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_C, text);
		textArea.setCaretPosition(0);
		textArea.getFoldManager().getFold(0).setCollapsed(true);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.EndAction("foo", true).actionPerformedImpl(e, textArea);

		// Caret is at the end of the first line (last visible offset => after the first "{"
		Assertions.assertEquals(1, textArea.getCaretPosition());
		Assertions.assertEquals("{", textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_noFolding_selectFalse() {

		String text = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE, text);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.EndAction("foo", false).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(text.length(), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_noFolding_selectTrue() {

		String text = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE, text);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.EndAction("foo", true).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(text.length(), textArea.getCaretPosition());
		Assertions.assertNotNull(textArea.getSelectedText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals("foo",
			new RSyntaxTextAreaEditorKit.EndAction("foo", false).getMacroID());
	}
}
