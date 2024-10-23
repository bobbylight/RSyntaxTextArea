/*
 * 08/22/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.event.ActionEvent;
import javax.swing.text.DefaultEditorKit;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.EndWordAction;
import org.fife.ui.rtextarea.RTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link EndWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RSyntaxTextAreaEditorKitEndWordActionTest extends AbstractRSyntaxTextAreaTest {


	/**
	 * Returns a fake action event for test purposes.
	 *
	 * @param textArea The source text area.
	 * @return The fake action event.
	 */
	private static ActionEvent createActionEvent(RTextArea textArea) {
		return createActionEvent(textArea, DefaultEditorKit.endWordAction);
	}


	@Test
	void testGetWordEnd_noSelection_happyPath() {

		EndWordAction action = new EndWordAction("endWordAction", false);
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		final String TEXT = "This is the best";
		textArea.setText(TEXT);
		for (int i = 0; i < "This".length(); i++) {
			textArea.setCaretPosition(i);
			action.actionPerformed(createActionEvent(textArea));
			Assertions.assertEquals("This".length(), textArea.getCaretPosition());
		}

		textArea.setCaretPosition("This".length());
		action.actionPerformed(createActionEvent(textArea));
		Assertions.assertEquals("This ".length(), textArea.getCaretPosition());

		textArea.setCaretPosition("This ".length());
		action.actionPerformed(createActionEvent(textArea));
		Assertions.assertEquals("This is".length(), textArea.getCaretPosition());

		textArea.setCaretPosition(TEXT.length());
		action.actionPerformed(createActionEvent(textArea));
		Assertions.assertEquals(TEXT.length(), textArea.getCaretPosition());

	}


	@Test
	void testGetWordEnd_noSelection_atNonWordChar() {
		EndWordAction action = new EndWordAction("endWordAction", false);
		String TEXT = "!@#$%^&*()";
		RSyntaxTextArea textArea = createTextArea(TEXT);
		textArea.setCaretPosition(TEXT.indexOf('#'));
		action.actionPerformed(createActionEvent(textArea));
		Assertions.assertEquals(TEXT.indexOf('#'), textArea.getCaretPosition());
	}


	@Test
	void testGetWordEnd_noSelection_endOfLine() {
		EndWordAction action = new EndWordAction("endWordAction", false);
		String TEXT = "111 111\n222 222";
		RSyntaxTextArea textArea = createTextArea(TEXT);
		textArea.setCaretPosition(TEXT.indexOf('\n'));
		action.actionPerformed(createActionEvent(textArea));
		Assertions.assertEquals("111 111".length(), textArea.getCaretPosition());
	}


	@Test
	void testGetWordEnd_noSelection_startOfLineOtherThanFirst() {
		EndWordAction action = new EndWordAction("endWordAction", false);
		String TEXT = "111 111\n222 222";
		RSyntaxTextArea textArea = createTextArea(TEXT);
		textArea.setCaretPosition(TEXT.indexOf('\n') + 1);
		action.actionPerformed(createActionEvent(textArea));
		Assertions.assertEquals("111 111\n222".length(), textArea.getCaretPosition());
	}


}
