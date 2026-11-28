/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rtextarea.RTextAreaEditorKit.DumbCompleteWordAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for the {@link DumbCompleteWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitDumbCompleteWordActionTest {


	private static RTextArea createTextArea(String text) {
		RTextArea textArea = new RTextArea(text);
		textArea.setCaretPosition(text.length());
		return textArea;
	}


	@Test
	void testActionPerformedImpl_completesFromPreviousWords() {

		RTextArea textArea = createTextArea("foobar food fo");
		DumbCompleteWordAction action = new DumbCompleteWordAction();

		// Closest previous match first
		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals("foobar food food", textArea.getText());
		Assertions.assertEquals(16, textArea.getCaretPosition());

		// Invoking again cycles to the next-closest match
		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals("foobar food foobar", textArea.getText());

		// No more matches - nothing changes
		action.actionPerformedImpl(null, textArea);
		Assertions.assertEquals("foobar food foobar", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_noMatch() {
		RTextArea textArea = createTextArea("abc xy");
		new DumbCompleteWordAction().actionPerformedImpl(null, textArea);
		Assertions.assertEquals("abc xy", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_prefixIsWhitespace() {
		RTextArea textArea = createTextArea("foo ");
		new DumbCompleteWordAction().actionPerformedImpl(null, textArea);
		Assertions.assertEquals("foo ", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_prefixIsPunctuation() {
		RTextArea textArea = createTextArea("foo f.");
		new DumbCompleteWordAction().actionPerformedImpl(null, textArea);
		Assertions.assertEquals("foo f.", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_caretAtStartOfDocument() {
		RTextArea textArea = createTextArea("foo f");
		textArea.setCaretPosition(0);
		new DumbCompleteWordAction().actionPerformedImpl(null, textArea);
		Assertions.assertEquals("foo f", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_notEditable() {
		RTextArea textArea = createTextArea("foo f");
		textArea.setEditable(false);
		new DumbCompleteWordAction().actionPerformedImpl(null, textArea);
		Assertions.assertEquals("foo f", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_notEnabled() {
		RTextArea textArea = createTextArea("foo f");
		textArea.setEnabled(false);
		new DumbCompleteWordAction().actionPerformedImpl(null, textArea);
		Assertions.assertEquals("foo f", textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaDumbCompleteWordAction,
			new DumbCompleteWordAction().getMacroID());
	}


	@Test
	void testGetWordStart() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RDocument doc = new RDocument();
		RTextArea textArea = new RTextArea(doc);

		textArea.setText("for if  while");
		Assertions.assertEquals(0, action.getWordStart(textArea, 0));
		Assertions.assertEquals(0, action.getWordStart(textArea, 1));
		Assertions.assertEquals(0, action.getWordStart(textArea, 2));
		Assertions.assertEquals(3, action.getWordStart(textArea, 3));
		Assertions.assertEquals(4, action.getWordStart(textArea, 4));
		Assertions.assertEquals(4, action.getWordStart(textArea, 5));
		Assertions.assertEquals(6, action.getWordStart(textArea, 6));
		// Multiple adjacent spaces treated as a word
		Assertions.assertEquals(6, action.getWordStart(textArea, 7));
		Assertions.assertEquals(8, action.getWordStart(textArea, 8));
		Assertions.assertEquals(8, action.getWordStart(textArea, 9));
		Assertions.assertEquals(8, action.getWordStart(textArea, 10));
		Assertions.assertEquals(8, action.getWordStart(textArea, 11));
		Assertions.assertEquals(8, action.getWordStart(textArea, 12));
		Assertions.assertEquals(8, action.getWordStart(textArea, 13));

		textArea.setText("  for  ");
		Assertions.assertEquals(0, action.getWordStart(textArea, 0));
		Assertions.assertEquals(0, action.getWordStart(textArea, 1));
		Assertions.assertEquals(2, action.getWordStart(textArea, 2));
		Assertions.assertEquals(2, action.getWordStart(textArea, 3));
		Assertions.assertEquals(2, action.getWordStart(textArea, 4));
		Assertions.assertEquals(5, action.getWordStart(textArea, 5));
		Assertions.assertEquals(5, action.getWordStart(textArea, 6));
		Assertions.assertEquals(5, action.getWordStart(textArea, 7));

		doc.replace(0, doc.getLength(), "", null);
		Assertions.assertEquals(0, action.getWordStart(textArea, 0));

	}


	@Test
	void testGetWordEnd() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RDocument doc = new RDocument();
		RTextArea textArea = new RTextArea(doc);

		textArea.setText("for if  while");
		Assertions.assertEquals(3, action.getWordEnd(textArea, 0));
		Assertions.assertEquals(3, action.getWordEnd(textArea, 1));
		Assertions.assertEquals(3, action.getWordEnd(textArea, 2));
		Assertions.assertEquals(4, action.getWordEnd(textArea, 3));
		Assertions.assertEquals(6, action.getWordEnd(textArea, 4));
		Assertions.assertEquals(6, action.getWordEnd(textArea, 5));
		Assertions.assertEquals(8, action.getWordEnd(textArea, 6));
		Assertions.assertEquals(8, action.getWordEnd(textArea, 7));
		Assertions.assertEquals(13, action.getWordEnd(textArea, 8));
		Assertions.assertEquals(13, action.getWordEnd(textArea, 9));
		Assertions.assertEquals(13, action.getWordEnd(textArea, 10));
		Assertions.assertEquals(13, action.getWordEnd(textArea, 11));
		Assertions.assertEquals(13, action.getWordEnd(textArea, 12));
		Assertions.assertEquals(13, action.getWordEnd(textArea, 13));

		textArea.setText("  for  ");
		Assertions.assertEquals(2, action.getWordEnd(textArea, 0));
		Assertions.assertEquals(2, action.getWordEnd(textArea, 1));
		Assertions.assertEquals(5, action.getWordEnd(textArea, 2));
		Assertions.assertEquals(5, action.getWordEnd(textArea, 3));
		Assertions.assertEquals(5, action.getWordEnd(textArea, 4));
		Assertions.assertEquals(7, action.getWordEnd(textArea, 5));
		Assertions.assertEquals(7, action.getWordEnd(textArea, 6));
		Assertions.assertEquals(7, action.getWordEnd(textArea, 7));

		doc.replace(0, doc.getLength(), "", null);
		Assertions.assertEquals(0, action.getWordEnd(textArea, 0));

	}


	@Test
	void testGetPreviousWord() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RDocument doc = new RDocument();
		RTextArea textArea = new RTextArea(doc);

		textArea.setText("for if  while");
		// Offset 0 throws an exception
		//Assertions.assertEquals(0, action.getPreviousWord(textArea, 0));
		Assertions.assertEquals(0, action.getPreviousWord(textArea, 1));
		Assertions.assertEquals(0, action.getPreviousWord(textArea, 2));
		Assertions.assertEquals(0, action.getPreviousWord(textArea, 3));
		Assertions.assertEquals(0, action.getPreviousWord(textArea, 4));
		Assertions.assertEquals(4, action.getPreviousWord(textArea, 5));
		Assertions.assertEquals(4, action.getPreviousWord(textArea, 6));
		// Spaces - find word before spaces
		Assertions.assertEquals(4, action.getPreviousWord(textArea, 7));
		Assertions.assertEquals(4, action.getPreviousWord(textArea, 8));
		Assertions.assertEquals(8, action.getPreviousWord(textArea, 9));
		Assertions.assertEquals(8, action.getPreviousWord(textArea, 10));
		Assertions.assertEquals(8, action.getPreviousWord(textArea, 11));
		Assertions.assertEquals(8, action.getPreviousWord(textArea, 12));
		Assertions.assertEquals(8, action.getPreviousWord(textArea, 13));

		textArea.setText("  for  ");
		// Offset 0 throws an exception
		//Assertions.assertEquals(0, action.getPreviousWord(textArea, 0));
		//Assertions.assertEquals(0, action.getPreviousWord(textArea, 1));
		//Assertions.assertEquals(2, action.getPreviousWord(textArea, 2));
		Assertions.assertEquals(2, action.getPreviousWord(textArea, 3));
		Assertions.assertEquals(2, action.getPreviousWord(textArea, 4));
		Assertions.assertEquals(2, action.getPreviousWord(textArea, 5));
		Assertions.assertEquals(2, action.getPreviousWord(textArea, 6));
		Assertions.assertEquals(2, action.getPreviousWord(textArea, 7));

	}


}
