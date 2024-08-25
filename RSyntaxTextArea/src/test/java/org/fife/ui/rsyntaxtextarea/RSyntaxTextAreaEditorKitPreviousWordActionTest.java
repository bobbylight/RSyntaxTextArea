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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.PreviousWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitPreviousWordActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testActionPerformedImpl_atOffset0() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(0, textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_middleOfLine_noSelection_letters() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		textArea.setCaretPosition(origContent.indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(origContent.indexOf("line 2"), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_middleOfLine_noSelection_symbols() {

		String origContent = "line 1\n@@@@ 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		textArea.setCaretPosition(origContent.indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(origContent.indexOf("@@@@ 2"), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_middleOfLine_noSelection_nextLine() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('\n'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		// If the prev word starts on the prev line, just select the last word on the prev line
		Assertions.assertEquals(textArea.getText().indexOf('1'), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_middleOfLine_selection() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		textArea.setCaretPosition(origContent.indexOf('2'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", true).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(origContent.indexOf("line 2"), textArea.getCaretPosition());
		Assertions.assertEquals("line ", textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_startOfLine_codeFolding_noSelection() {

		String origContent = "{\n  exit(0);\n}";
		RSyntaxTextArea textArea = createTextArea(origContent);
		textArea.setCaretPosition(origContent.indexOf('\n') + 1);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		// End of the previous line
		Assertions.assertEquals(origContent.indexOf('\n'), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_startOfLine_codeFolding_noSelection_hiddenLinesAbove() {

		// The caret is at the start of the last line. Lines immediately above it are collapsed.
		String origContent = "{\n  {\n    foo;\n  }\n}";
		RSyntaxTextArea textArea = createTextArea(origContent);
		textArea.getFoldManager().getFold(0).getChild(0).setCollapsed(true);
		textArea.setCaretPosition(origContent.lastIndexOf('\n') + 1);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		// End of the second line (where the collapsed section starts).
		Assertions.assertEquals(origContent.indexOf("  {") + 3, textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_startOfLine_noCodeFolding_noSelection() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		textArea.setCaretPosition(origContent.indexOf("line 2"));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", false).actionPerformedImpl(e, textArea);

		// End of the previous line
		Assertions.assertEquals(origContent.indexOf("1") + 1, textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals("foo",
			new RSyntaxTextAreaEditorKit.PreviousWordAction("foo", true).getMacroID());
	}
}
