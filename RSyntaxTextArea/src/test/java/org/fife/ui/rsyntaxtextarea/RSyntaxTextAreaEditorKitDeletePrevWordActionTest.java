/*
 * 08/22/2015
 *
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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.DeletePrevWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitDeletePrevWordActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testActionPerformedImpl_notEditable() {

		RSyntaxTextArea textArea = createTextArea("foo");
		textArea.setCaretPosition(textArea.getDocument().getLength());
		textArea.setEditable(false);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DeletePrevWordAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		// Text is unchanged
		Assertions.assertEquals("foo", textArea.getText());
		Assertions.assertEquals(textArea.getDocument().getLength(), textArea.getCaretPosition());
	}

	@Test
	void testActionPerformedImpl_notEnabled() {

		RSyntaxTextArea textArea = createTextArea("foo");
		textArea.setCaretPosition(textArea.getDocument().getLength());
		textArea.setEnabled(false);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DeletePrevWordAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		// Text is unchanged
		Assertions.assertEquals("foo", textArea.getText());
		Assertions.assertEquals(textArea.getDocument().getLength(), textArea.getCaretPosition());
	}

	@Test
	void testActionPerformedImpl_caretAtOffset0() {

		String origText = "foobar";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origText);
		textArea.setCaretPosition(0);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DeletePrevWordAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals(origText, textArea.getText());
	}

	@Test
	void testActionPerformedImpl_caretAtStartOfLineOtherThanFirst() {

		String origText = "111\n222";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origText);
		textArea.setCaretPosition(origText.indexOf('\n') + 1);

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DeletePrevWordAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		// Just delete the newline
		Assertions.assertEquals("111222", textArea.getText());
	}

	@Test
	void testActionPerformedImpl_caretAtEndOfWord() {

		String origText = "/*\n" +
			"    * comment\n" +
			"    */\n" +
			"    public    void foo() {\n" +
			"        /* comment\n" +
			"          two */\n" +
			"    }";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origText);

		textArea.setCaretPosition(origText.indexOf('('));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DeletePrevWordAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		String expected = origText.replace("foo", "");
		String actual = textArea.getText();
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void testActionPerformedImpl_caretAfterWordAndTrailingSpace() {

		String origText = "/*\n" +
			"    * comment\n" +
			"    */\n" +
			"    public void foo() {\n" +
			"        /* comment\n" +
			"          two */\n" +
			"    }";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origText);

		textArea.setCaretPosition(origText.indexOf("void"));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DeletePrevWordAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		// Deletes both the prior word "public" and its trailing space
		String expected = origText.replace("public ", "");
		String actual = textArea.getText();
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void testActionPerformedImpl_caretAtEndOfSeriesOfSymbols() {

		String origText = "////";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origText);
		textArea.setCaretPosition(origText.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DeletePrevWordAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals("", textArea.getText());
	}

	@Test
	void testActionPerformedImpl_caretAtEndOfSeriesOfSymbolsWithTrailingWhitespace() {

		String origText = "////    ";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origText);
		textArea.setCaretPosition(origText.length());

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DeletePrevWordAction();
		ActionEvent e = createActionEvent(textArea, a.getMacroID());
		a.actionPerformedImpl(e, textArea);

		Assertions.assertEquals("", textArea.getText());
	}

	@Test
	void testGetMacroId() {
		RecordableTextAction a = new RSyntaxTextAreaEditorKit.DeletePrevWordAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rtaDeletePrevWordAction, a.getMacroID());
	}
}
