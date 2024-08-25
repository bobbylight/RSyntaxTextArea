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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.NextWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitNextWordActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testActionPerformedImpl_atEndOfDocument() {

		String text = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(text);
		textArea.setCaretPosition(text.length());

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.NextWordAction("foo", true).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(text.length(), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_atEndOfLine_codeFolding_noLaterVisibleLine_noSelection() {

		String text = "line 1 {\n  foo;\n}";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf('\n'));
		textArea.setCodeFoldingEnabled(true);
		textArea.getFoldManager().getFold(0).setCollapsed(true);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.NextWordAction("foo", false).actionPerformedImpl(e, textArea);

		// Because of the fold, we're already on the last visible offset
		Assertions.assertEquals(text.indexOf('\n'), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_atEndOfLine_codeFolding_noLaterVisibleLine_selection() {

		String text = "line 1 {\n  foo;\n}";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf('\n'));
		textArea.setCodeFoldingEnabled(true);
		textArea.getFoldManager().getFold(0).setCollapsed(true);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.NextWordAction("foo", true).actionPerformedImpl(e, textArea);

		// Because of the fold, we're already on the last visible offset
		Assertions.assertEquals(text.indexOf('\n'), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_atEndOfLine_codeFolding_laterVisibleLine_noSelection() {

		String text = "line 1 {\n  foo;\n}\nlast line";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf('\n'));
		textArea.setCodeFoldingEnabled(true);
		textArea.getFoldManager().getFold(0).setCollapsed(true);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.NextWordAction("foo", false).actionPerformedImpl(e, textArea);

		// Just go to the start of the next line
		Assertions.assertEquals(text.indexOf("last line"), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_atEndOfLine_codeFolding_laterVisibleLine_selection() {

		String text = "line 1 {\n  foo;\n}\nlast line";
		RSyntaxTextArea textArea = createTextArea(text);
		textArea.setCaretPosition(text.indexOf('\n'));
		textArea.setCodeFoldingEnabled(true);
		textArea.getFoldManager().getFold(0).setCollapsed(true);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.NextWordAction("foo", true).actionPerformedImpl(e, textArea);

		// Just go to the start of the next line
		Assertions.assertEquals(text.indexOf("last line"), textArea.getCaretPosition());
		Assertions.assertEquals("\n  foo;\n}\n", textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_atEndOfLine_noCodeFolding_noSelection() {

		String text = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(text);
		textArea.setCaretPosition(text.indexOf('\n'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.NextWordAction("foo", false).actionPerformedImpl(e, textArea);

		// Just go to the start of the next line
		Assertions.assertEquals(text.indexOf('\n') + 1, textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_atEndOfLine_noCodeFolding_selection() {

		String text = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(text);
		textArea.setCaretPosition(text.indexOf('\n'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.NextWordAction("foo", true).actionPerformedImpl(e, textArea);

		// Just go to the start of the next line
		Assertions.assertEquals(text.indexOf('\n') + 1, textArea.getCaretPosition());
		Assertions.assertEquals("\n", textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_noSelection_lettersAndNumbers() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.NextWordAction("foo", false).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(5, textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_noSelection_symbolsWithTrailingWhitespace() {

		String text = "///// line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(text);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.NextWordAction("foo", false).actionPerformedImpl(e, textArea);

		// Skip the symbols and trailing whitespace to get to the next word
		Assertions.assertEquals(text.indexOf("line 1"), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_noSelection_symbolsAdjacentToNextWord() {

		String text = "/////line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(text);
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.NextWordAction("foo", false).actionPerformedImpl(e, textArea);

		// Skip the symbols to get to the next word
		Assertions.assertEquals(text.indexOf("line 1"), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_noSelection_nextLine() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('1'));

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.NextWordAction("foo", false).actionPerformedImpl(e, textArea);

		// If the next word starts on the next line, just select to the end of the current line
		Assertions.assertEquals(textArea.getText().indexOf('\n'), textArea.getCaretPosition());
		Assertions.assertNull(textArea.getSelectedText());
	}


	@Test
	void testActionPerformedImpl_selection() {

		RSyntaxTextArea textArea = new RSyntaxTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.NextWordAction("foo", true).actionPerformedImpl(e, textArea);

		Assertions.assertEquals(5, textArea.getCaretPosition());
		Assertions.assertEquals(0, textArea.getSelectionStart());
		Assertions.assertEquals(5, textArea.getSelectionEnd());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals("foo",
			new RSyntaxTextAreaEditorKit.NextWordAction("foo", true).getMacroID());
	}
}
