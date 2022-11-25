/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rtextarea.RecordableTextAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.InsertPairedCharacterAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitInsertPairedCharacterActionTest extends AbstractRSyntaxTextAreaTest {

	private static final String ORIG_CONTENT = "one word is selected";


	private static void assertNoPairedCharBehavior(RSyntaxTextArea textArea) {
		String expectedResultContent = "one (word is selected";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(ORIG_CONTENT.indexOf("word") + 1, textArea.getSelectionStart());
		Assertions.assertEquals(textArea.getSelectionStart(), textArea.getSelectionEnd());
	}

	private static void assertPairedCharBehavior(RSyntaxTextArea textArea) {
		String expectedResultContent = "one (word) is selected";
		Assertions.assertEquals(expectedResultContent, textArea.getText());
		Assertions.assertEquals(ORIG_CONTENT.indexOf("word") + 1, textArea.getSelectionStart());
		Assertions.assertEquals(textArea.getSelectionStart() + "word".length(), textArea.getSelectionEnd());
	}

	@Test
	void testActionPerformedImpl_notEditable() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE, ORIG_CONTENT);
		textArea.setCaretPosition(ORIG_CONTENT.indexOf("word"));
		textArea.setEditable(false);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertPairedCharacterAction("test", '(', ')');
		ActionEvent e = createActionEvent(textArea, "(");
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals(ORIG_CONTENT, textArea.getText()); // Unchanged
	}

	@Test
	void testActionPerformedImpl_notEnabled() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE, ORIG_CONTENT);
		textArea.setCaretPosition(ORIG_CONTENT.indexOf("word"));
		textArea.setEnabled(false);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertPairedCharacterAction("test", '(', ')');
		ActionEvent e = createActionEvent(textArea, "(");
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals(ORIG_CONTENT, textArea.getText()); // Unchanged
	}

	@Test
	void testActionPerformedImpl_disabled_noSelection() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE, ORIG_CONTENT);
		textArea.setInsertPairedCharacters(false);
		textArea.setCaretPosition(ORIG_CONTENT.indexOf("word"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertPairedCharacterAction("test", '(', ')');
		ActionEvent e = createActionEvent(textArea, "(");
		a.actionPerformedImpl(e, textArea);

		assertNoPairedCharBehavior(textArea);
	}

	@Test
	void testActionPerformedImpl_disabled_selection() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE, ORIG_CONTENT);
		textArea.setSelectionStart(ORIG_CONTENT.indexOf("word"));
		textArea.setSelectionEnd(textArea.getSelectionStart() + "word".length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertPairedCharacterAction("test", '(', ')');
		ActionEvent e = createActionEvent(textArea, "(");
		a.actionPerformedImpl(e, textArea);

		assertPairedCharBehavior(textArea);
	}

	@Test
	void testActionPerformedImpl_enabled_noSelection() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE, ORIG_CONTENT);
		textArea.setCaretPosition(ORIG_CONTENT.indexOf("word"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertPairedCharacterAction("test", '(', ')');
		ActionEvent e = createActionEvent(textArea, "(");
		a.actionPerformedImpl(e, textArea);

		assertNoPairedCharBehavior(textArea);
	}

	@Test
	void testActionPerformedImpl_enabled_selection() {

		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_NONE, ORIG_CONTENT);
		textArea.setSelectionStart(ORIG_CONTENT.indexOf("word"));
		textArea.setSelectionEnd(textArea.getSelectionStart() + "word".length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.InsertPairedCharacterAction("test", '(', ')');
		ActionEvent e = createActionEvent(textArea, "(");
		a.actionPerformedImpl(e, textArea);

		assertPairedCharBehavior(textArea);
	}
}
