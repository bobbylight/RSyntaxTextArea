/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for this action.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitBeginWordActionTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testActionPerformedImpl_inWhitespace_middleOfWhitespace() {

		String text = "word      word";
		RSyntaxTextArea textArea = new RSyntaxTextArea(text);
		textArea.setCaretPosition(7);

		RSyntaxTextAreaEditorKit.BeginWordAction action =
			new RSyntaxTextAreaEditorKit.BeginWordAction("name", false);

		// Moves to the beginning of whitespace between words
		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(4, textArea.getSelectionStart());
		Assertions.assertEquals(4, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_inWhitespace_endOfWhitespace() {

		String text = "word      word";
		RSyntaxTextArea textArea = new RSyntaxTextArea(text);
		textArea.setCaretPosition(text.lastIndexOf(' '));

		RSyntaxTextAreaEditorKit.BeginWordAction action =
			new RSyntaxTextAreaEditorKit.BeginWordAction("name", false);

		// Moves to the beginning of whitespace between words
		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(4, textArea.getSelectionStart());
		Assertions.assertEquals(4, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_inWord_beginningOfWord() {

		String text = "word word word";
		RSyntaxTextArea textArea = new RSyntaxTextArea(text);
		textArea.setCaretPosition(5);

		RSyntaxTextAreaEditorKit.BeginWordAction action =
			new RSyntaxTextAreaEditorKit.BeginWordAction("name", false);

		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(5, textArea.getSelectionStart());
		Assertions.assertEquals(5, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_inWord_endOfWord() {

		String text = "word word word";
		RSyntaxTextArea textArea = new RSyntaxTextArea(text);
		textArea.setCaretPosition(8);

		RSyntaxTextAreaEditorKit.BeginWordAction action =
			new RSyntaxTextAreaEditorKit.BeginWordAction("name", false);

		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(5, textArea.getSelectionStart());
		Assertions.assertEquals(5, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_inWord_middleOfWord() {

		String text = "word word word";
		RSyntaxTextArea textArea = new RSyntaxTextArea(text);
		textArea.setCaretPosition(7);

		RSyntaxTextAreaEditorKit.BeginWordAction action =
			new RSyntaxTextAreaEditorKit.BeginWordAction("name", false);

		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(5, textArea.getSelectionStart());
		Assertions.assertEquals(5, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_offset0() {

		String text = "word word word";
		RSyntaxTextArea textArea = new RSyntaxTextArea(text);
		textArea.setCaretPosition(0);

		RSyntaxTextAreaEditorKit.BeginWordAction action =
			new RSyntaxTextAreaEditorKit.BeginWordAction("name", false);

		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(0, textArea.getSelectionStart());
		Assertions.assertEquals(0, textArea.getSelectionEnd());
	}


	@Test
	void testActionPerformedImpl_startOfLineOtherThanTheFirst() {

		String text = "line 1\nline 2";
		RSyntaxTextArea textArea = new RSyntaxTextArea(text);
		int secondLineStartOffs = text.indexOf('\n') + 1;
		textArea.setCaretPosition(secondLineStartOffs);

		RSyntaxTextAreaEditorKit.BeginWordAction action =
			new RSyntaxTextAreaEditorKit.BeginWordAction("name", false);

		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals(secondLineStartOffs, textArea.getSelectionStart());
		Assertions.assertEquals(secondLineStartOffs, textArea.getSelectionEnd());
	}
}
