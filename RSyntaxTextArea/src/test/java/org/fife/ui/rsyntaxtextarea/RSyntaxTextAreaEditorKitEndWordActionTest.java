/*
 * 08/22/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.event.ActionEvent;
import javax.swing.text.DefaultEditorKit;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit.EndWordAction;
import org.fife.ui.rtextarea.RTextArea;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link EndWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RSyntaxTextAreaEditorKitEndWordActionTest {


	/**
	 * Returns a fake action event for test purposes.
	 *
	 * @param textArea The source text area.
	 * @return The fake action event.
	 */
	private static final ActionEvent createActionEvent(RTextArea textArea) {
		return new ActionEvent(textArea, 0, DefaultEditorKit.endWordAction);
	}


	@Test
	public void testGetWordEnd_noSelection_happyPath() {

		EndWordAction action = new EndWordAction("endWordAction", false);
		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_JAVA);
		RSyntaxTextArea textArea = new RSyntaxTextArea(doc);

		final String TEXT = "This is the best";
		textArea.setText(TEXT);
		for (int i = 0; i < "This".length(); i++) {
			textArea.setCaretPosition(i);
			action.actionPerformed(createActionEvent(textArea));
			Assert.assertEquals("This".length(), textArea.getCaretPosition());
		}

		textArea.setCaretPosition("This".length());
		action.actionPerformed(createActionEvent(textArea));
		Assert.assertEquals("This ".length(), textArea.getCaretPosition());

		textArea.setCaretPosition("This ".length());
		action.actionPerformed(createActionEvent(textArea));
		Assert.assertEquals("This is".length(), textArea.getCaretPosition());

		textArea.setCaretPosition(TEXT.length());
		action.actionPerformed(createActionEvent(textArea));
		Assert.assertEquals(TEXT.length(), textArea.getCaretPosition());

	}


}