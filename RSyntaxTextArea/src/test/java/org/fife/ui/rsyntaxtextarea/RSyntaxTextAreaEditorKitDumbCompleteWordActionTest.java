/*
 * 03/06/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.event.ActionEvent;
import java.text.BreakIterator;
import javax.swing.ActionMap;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.DumbCompleteWordAction;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextAreaEditorKit;
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
class RSyntaxTextAreaEditorKitDumbCompleteWordActionTest extends AbstractRSyntaxTextAreaTest {


	/**
	 * Returns a fake action event for test purposes.
	 *
	 * @param textArea The source text area.
	 * @return The fake action event.
	 */
	private static ActionEvent createActionEvent(RTextArea textArea) {
		return createActionEvent(textArea, RTextAreaEditorKit.rtaDumbCompleteWordAction);
	}


	/**
	 * Returns the "dumb complete word action" to test associated with a text
	 * area.
	 *
	 * @param textArea The text area.
	 * @return The associated action.
	 */
	private static DumbCompleteWordAction getDumbCompleteWordAction(
			RSyntaxTextArea textArea) {
		ActionMap am = textArea.getActionMap();
		return (DumbCompleteWordAction)am.get(
				RTextAreaEditorKit.rtaDumbCompleteWordAction);
	}


	@Test
	void testActionPerformed_offset0() {

		RSyntaxDocument doc = new RSyntaxDocument(
			SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);
		String origText = "aaa aaaa aaaaa";
		textArea.setText(origText);
		textArea.setCaretPosition(0);
		DumbCompleteWordAction action = getDumbCompleteWordAction(textArea);

		action.actionPerformed(createActionEvent(textArea));
		Assertions.assertEquals(origText, textArea.getText());
		Assertions.assertEquals(0, textArea.getCaretPosition());
	}


	@Test
	void testActionPerformed_manyLinesInBetween() {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		textArea.setText("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   a");
		action.actionPerformed(createActionEvent(textArea));
		Assertions.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   amazing", textArea.getText());
		action.actionPerformed(createActionEvent(textArea));
		Assertions.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   aardvark",
			textArea.getText());
		action.actionPerformed(createActionEvent(textArea));
		Assertions.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   arthur", textArea.getText());
		action.actionPerformed(createActionEvent(textArea));
		Assertions.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   aaron", textArea.getText());
		action.actionPerformed(createActionEvent(textArea));
		Assertions.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   aaron", textArea.getText());

	}


	@Test
	void testActionPerformed_dollarSignImportant() {

		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);
		DumbCompleteWordAction action = getDumbCompleteWordAction(textArea);

		textArea.setText("$routeProvider routeSkip $ro");
		action.actionPerformed(createActionEvent(textArea));
		String actual = textArea.getText();
		Assertions.assertEquals("$routeProvider routeSkip $routeProvider", actual);

	}


	@Test
	void testActionPerformed_underscoresImportant() {

		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);
		DumbCompleteWordAction action = getDumbCompleteWordAction(textArea);

		textArea.setText("__foo\n__bar   __bas\n_");

		action.actionPerformed(createActionEvent(textArea));
		String actual = textArea.getText();
		Assertions.assertEquals("__foo\n__bar   __bas\n__bas", actual);

		action.actionPerformed(createActionEvent(textArea));
		actual = textArea.getText();
		Assertions.assertEquals("__foo\n__bar   __bas\n__bar", actual);

		action.actionPerformed(createActionEvent(textArea));
		actual = textArea.getText();
		Assertions.assertEquals("__foo\n__bar   __bas\n__foo", actual);

		// No change when run again
		action.actionPerformed(createActionEvent(textArea));
		actual = textArea.getText();
		Assertions.assertEquals("__foo\n__bar   __bas\n__foo", actual);

	}


	@Test
	void testGetWordStart() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		textArea.setText("for if  while");
		Assertions.assertEquals(0, action.getWordStart(textArea, 0));
		Assertions.assertEquals(0, action.getWordStart(textArea, 1));
		Assertions.assertEquals(0, action.getWordStart(textArea, 2));
		Assertions.assertEquals(3, action.getWordStart(textArea, 3));
		Assertions.assertEquals(4, action.getWordStart(textArea, 4));
		Assertions.assertEquals(4, action.getWordStart(textArea, 5));
		Assertions.assertEquals(6, action.getWordStart(textArea, 6));
		Assertions.assertEquals(7, action.getWordStart(textArea, 7));
		Assertions.assertEquals(8, action.getWordStart(textArea, 8));
		Assertions.assertEquals(8, action.getWordStart(textArea, 9));
		Assertions.assertEquals(8, action.getWordStart(textArea, 10));
		Assertions.assertEquals(8, action.getWordStart(textArea, 11));
		Assertions.assertEquals(8, action.getWordStart(textArea, 12));
		Assertions.assertEquals(8, action.getWordStart(textArea, 13));

		textArea.setText("  for  ");
		Assertions.assertEquals(0, action.getWordStart(textArea, 0));
		Assertions.assertEquals(1, action.getWordStart(textArea, 1));
		Assertions.assertEquals(2, action.getWordStart(textArea, 2));
		Assertions.assertEquals(2, action.getWordStart(textArea, 3));
		Assertions.assertEquals(2, action.getWordStart(textArea, 4));
		Assertions.assertEquals(5, action.getWordStart(textArea, 5));
		Assertions.assertEquals(6, action.getWordStart(textArea, 6));
		Assertions.assertEquals(7, action.getWordStart(textArea, 7));

		doc.replace(0, doc.getLength(), "", null);
		Assertions.assertEquals(0, action.getWordStart(textArea, 0));

	}


	@Test
	void testGetWordEnd() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		textArea.setText("for if  while");
		Assertions.assertEquals(3, action.getWordEnd(textArea, 0));
		Assertions.assertEquals(3, action.getWordEnd(textArea, 1));
		Assertions.assertEquals(3, action.getWordEnd(textArea, 2));
		Assertions.assertEquals(3, action.getWordEnd(textArea, 3));
		Assertions.assertEquals(6, action.getWordEnd(textArea, 4));
		Assertions.assertEquals(6, action.getWordEnd(textArea, 5));
		Assertions.assertEquals(6, action.getWordEnd(textArea, 6));
		Assertions.assertEquals(7, action.getWordEnd(textArea, 7));
		Assertions.assertEquals(13, action.getWordEnd(textArea, 8));
		Assertions.assertEquals(13, action.getWordEnd(textArea, 9));
		Assertions.assertEquals(13, action.getWordEnd(textArea, 10));
		Assertions.assertEquals(13, action.getWordEnd(textArea, 11));
		Assertions.assertEquals(13, action.getWordEnd(textArea, 12));
		Assertions.assertEquals(13, action.getWordEnd(textArea, 13));

		textArea.setText("  for  ");
		Assertions.assertEquals(0, action.getWordEnd(textArea, 0));
		Assertions.assertEquals(1, action.getWordEnd(textArea, 1));
		Assertions.assertEquals(5, action.getWordEnd(textArea, 2));
		Assertions.assertEquals(5, action.getWordEnd(textArea, 3));
		Assertions.assertEquals(5, action.getWordEnd(textArea, 4));
		Assertions.assertEquals(5, action.getWordEnd(textArea, 5));
		Assertions.assertEquals(6, action.getWordEnd(textArea, 6));
		Assertions.assertEquals(7, action.getWordEnd(textArea, 7));

		doc.replace(0, doc.getLength(), "", null);
		Assertions.assertEquals(0, action.getWordEnd(textArea, 0));

	}


	@Test
	void testGetPreviousWord() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

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


	@Test
	void testGetPreviousWord_manyLinesInBetween() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		textArea.setText("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   a");
		Assertions.assertEquals(textArea.getDocument().getLength()-1,
			action.getPreviousWord(textArea, textArea.getDocument().getLength()));
		Assertions.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\n".length(),
			action.getPreviousWord(textArea, textArea.getDocument().getLength()-2));
		Assertions.assertEquals("aaron arthur ".length(), action.getPreviousWord(textArea, 22));
		Assertions.assertEquals("aaron ".length(), action.getPreviousWord(textArea, 8));

	}


	@Test
	void testGetWordStart_offset0() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RSyntaxDocument doc = new RSyntaxDocument(
			SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		textArea.setText("aa aaa aaaa");
		textArea.setCaretPosition(0);
		Assertions.assertEquals(BreakIterator.DONE, action.getPreviousWord(textArea, 0));
	}


	@Test
	void testIsAcceptablePrefix_emptyString() {
		DumbCompleteWordAction action = new DumbCompleteWordAction();
		Assertions.assertFalse(action.isAcceptablePrefix(""));
	}


	@Test
	void testIsAcceptablePrefix_happyPath() {
		DumbCompleteWordAction action = new DumbCompleteWordAction();
		Assertions.assertTrue(action.isAcceptablePrefix("aa"));
		Assertions.assertTrue(action.isAcceptablePrefix("aa123"));
		Assertions.assertTrue(action.isAcceptablePrefix("aa_"));
	}


	@Test
	void testIsAcceptablePrefix_trailingNonIdentifierChar() {
		DumbCompleteWordAction action = new DumbCompleteWordAction();
		Assertions.assertFalse(action.isAcceptablePrefix("aa/"));
		Assertions.assertFalse(action.isAcceptablePrefix("aa-"));
	}
}
