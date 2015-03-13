/*
 * 03/06/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.event.ActionEvent;
import javax.swing.ActionMap;

import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.DumbCompleteWordAction;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextAreaEditorKit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit tests for the {@link DumbCompleteWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaEditorKitDumbCompleteWordActionTest {


	/**
	 * Returns a fake action event for test purposes.
	 *
	 * @param textArea The source text area.
	 * @return The fake action event.
	 */
	private static final ActionEvent createActionEvent(RTextArea textArea) {
		return new ActionEvent(textArea, 0,
				RTextAreaEditorKit.rtaDumbCompleteWordAction);
	}


	/**
	 * Returns the "dumb complete word action" to test associated with a text
	 * area.
	 *
	 * @param textArea The text area.
	 * @return The associated action.
	 */
	private static final DumbCompleteWordAction getDumbCompleteWordAction(
			RSyntaxTextArea textArea) {
		ActionMap am = textArea.getActionMap();
		return (DumbCompleteWordAction)am.get(
				RTextAreaEditorKit.rtaDumbCompleteWordAction);
	}


	@Test
	public void testActionPerformed_manyLinesInBetween() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		textArea.setText("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   a");
		action.actionPerformed(createActionEvent(textArea));
		Assert.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   amazing", textArea.getText());
		action.actionPerformed(createActionEvent(textArea));
		Assert.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   aardvark", textArea.getText());
		action.actionPerformed(createActionEvent(textArea));
		Assert.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   arthur", textArea.getText());
		action.actionPerformed(createActionEvent(textArea));
		Assert.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   aaron", textArea.getText());
		action.actionPerformed(createActionEvent(textArea));
		Assert.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   aaron", textArea.getText());

	}


	@Test
	public void testActionPerformed_dollarSignImportant() throws Exception {

		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);
		DumbCompleteWordAction action = getDumbCompleteWordAction(textArea);

		textArea.setText("$routeProvider routeSkip $ro");
		action.actionPerformed(createActionEvent(textArea));
		String actual = textArea.getText();
		Assert.assertEquals("$routeProvider routeSkip $routeProvider", actual);

	}


	@Test
	public void testActionPerformed_underscoresImportant() throws Exception {

		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);
		DumbCompleteWordAction action = getDumbCompleteWordAction(textArea);

		textArea.setText("__foo\n__bar   __bas\n_");

		action.actionPerformed(createActionEvent(textArea));
		String actual = textArea.getText();
		Assert.assertEquals("__foo\n__bar   __bas\n__bas", actual);

		action.actionPerformed(createActionEvent(textArea));
		actual = textArea.getText();
		Assert.assertEquals("__foo\n__bar   __bas\n__bar", actual);

		action.actionPerformed(createActionEvent(textArea));
		actual = textArea.getText();
		Assert.assertEquals("__foo\n__bar   __bas\n__foo", actual);

		// No change when run again
		action.actionPerformed(createActionEvent(textArea));
		actual = textArea.getText();
		Assert.assertEquals("__foo\n__bar   __bas\n__foo", actual);

	}


	@Test
	public void testGetWordStart() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		textArea.setText("for if  while");
		Assert.assertEquals(0, action.getWordStart(textArea, 0));
		Assert.assertEquals(0, action.getWordStart(textArea, 1));
		Assert.assertEquals(0, action.getWordStart(textArea, 2));
		Assert.assertEquals(3, action.getWordStart(textArea, 3));
		Assert.assertEquals(4, action.getWordStart(textArea, 4));
		Assert.assertEquals(4, action.getWordStart(textArea, 5));
		Assert.assertEquals(6, action.getWordStart(textArea, 6));
		Assert.assertEquals(7, action.getWordStart(textArea, 7));
		Assert.assertEquals(8, action.getWordStart(textArea, 8));
		Assert.assertEquals(8, action.getWordStart(textArea, 9));
		Assert.assertEquals(8, action.getWordStart(textArea, 10));
		Assert.assertEquals(8, action.getWordStart(textArea, 11));
		Assert.assertEquals(8, action.getWordStart(textArea, 12));
		Assert.assertEquals(8, action.getWordStart(textArea, 13));

		textArea.setText("  for  ");
		Assert.assertEquals(0, action.getWordStart(textArea, 0));
		Assert.assertEquals(1, action.getWordStart(textArea, 1));
		Assert.assertEquals(2, action.getWordStart(textArea, 2));
		Assert.assertEquals(2, action.getWordStart(textArea, 3));
		Assert.assertEquals(2, action.getWordStart(textArea, 4));
		Assert.assertEquals(5, action.getWordStart(textArea, 5));
		Assert.assertEquals(6, action.getWordStart(textArea, 6));
		Assert.assertEquals(7, action.getWordStart(textArea, 7));

		doc.replace(0, doc.getLength(), "", null);
		Assert.assertEquals(0, action.getWordStart(textArea, 0));

	}


	@Test
	public void testGetWordEnd() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		textArea.setText("for if  while");
		Assert.assertEquals(3, action.getWordEnd(textArea, 0));
		Assert.assertEquals(3, action.getWordEnd(textArea, 1));
		Assert.assertEquals(3, action.getWordEnd(textArea, 2));
		Assert.assertEquals(3, action.getWordEnd(textArea, 3));
		Assert.assertEquals(6, action.getWordEnd(textArea, 4));
		Assert.assertEquals(6, action.getWordEnd(textArea, 5));
		Assert.assertEquals(6, action.getWordEnd(textArea, 6));
		Assert.assertEquals(7, action.getWordEnd(textArea, 7));
		Assert.assertEquals(13, action.getWordEnd(textArea, 8));
		Assert.assertEquals(13, action.getWordEnd(textArea, 9));
		Assert.assertEquals(13, action.getWordEnd(textArea, 10));
		Assert.assertEquals(13, action.getWordEnd(textArea, 11));
		Assert.assertEquals(13, action.getWordEnd(textArea, 12));
		Assert.assertEquals(13, action.getWordEnd(textArea, 13));

		textArea.setText("  for  ");
		Assert.assertEquals(0, action.getWordEnd(textArea, 0));
		Assert.assertEquals(1, action.getWordEnd(textArea, 1));
		Assert.assertEquals(5, action.getWordEnd(textArea, 2));
		Assert.assertEquals(5, action.getWordEnd(textArea, 3));
		Assert.assertEquals(5, action.getWordEnd(textArea, 4));
		Assert.assertEquals(5, action.getWordEnd(textArea, 5));
		Assert.assertEquals(6, action.getWordEnd(textArea, 6));
		Assert.assertEquals(7, action.getWordEnd(textArea, 7));

		doc.replace(0, doc.getLength(), "", null);
		Assert.assertEquals(0, action.getWordEnd(textArea, 0));

	}


	@Test
	public void testGetPreviousWord() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		textArea.setText("for if  while");
		// Offset 0 throws an exception
		//Assert.assertEquals(0, action.getPreviousWord(textArea, 0));
		Assert.assertEquals(0, action.getPreviousWord(textArea, 1));
		Assert.assertEquals(0, action.getPreviousWord(textArea, 2));
		Assert.assertEquals(0, action.getPreviousWord(textArea, 3));
		Assert.assertEquals(0, action.getPreviousWord(textArea, 4));
		Assert.assertEquals(4, action.getPreviousWord(textArea, 5));
		Assert.assertEquals(4, action.getPreviousWord(textArea, 6));
		// Spaces - find word before spaces
		Assert.assertEquals(4, action.getPreviousWord(textArea, 7));
		Assert.assertEquals(4, action.getPreviousWord(textArea, 8));
		Assert.assertEquals(8, action.getPreviousWord(textArea, 9));
		Assert.assertEquals(8, action.getPreviousWord(textArea, 10));
		Assert.assertEquals(8, action.getPreviousWord(textArea, 11));
		Assert.assertEquals(8, action.getPreviousWord(textArea, 12));
		Assert.assertEquals(8, action.getPreviousWord(textArea, 13));

		textArea.setText("  for  ");
		// Offset 0 throws an exception
		//Assert.assertEquals(0, action.getPreviousWord(textArea, 0));
		//Assert.assertEquals(0, action.getPreviousWord(textArea, 1));
		//Assert.assertEquals(2, action.getPreviousWord(textArea, 2));
		Assert.assertEquals(2, action.getPreviousWord(textArea, 3));
		Assert.assertEquals(2, action.getPreviousWord(textArea, 4));
		Assert.assertEquals(2, action.getPreviousWord(textArea, 5));
		Assert.assertEquals(2, action.getPreviousWord(textArea, 6));
		Assert.assertEquals(2, action.getPreviousWord(textArea, 7));

	}


	@Test
	public void testGetPreviousWord_manyLinesInBetween() throws Exception {

		DumbCompleteWordAction action = new DumbCompleteWordAction();
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		textArea.setText("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   a");
		Assert.assertEquals(textArea.getDocument().getLength()-1, action.getPreviousWord(textArea, textArea.getDocument().getLength()));
		Assert.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\n".length(), action.getPreviousWord(textArea, textArea.getDocument().getLength()-2));
		Assert.assertEquals("aaron arthur ".length(), action.getPreviousWord(textArea, 22));
		Assert.assertEquals("aaron ".length(), action.getPreviousWord(textArea, 8));

	}


}