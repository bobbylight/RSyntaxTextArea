/*
 * 08/22/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.DeletePrevWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaEditorKitDeletePrevWordActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	public void testActionPerformedImpl_deletePrevWord() {

		String origText = "/*\n" +
			"    * comment\n" +
			"    */\n" +
			"    public void foo() {\n" +
			"        /* comment\n" +
			"          two */\n" +
			"    }";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origText);

		textArea.setCaretPosition(origText.indexOf('('));

		RSyntaxTextAreaEditorKit.DeletePrevWordAction a = new RSyntaxTextAreaEditorKit.DeletePrevWordAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rtaDeletePrevWordAction);
		a.actionPerformedImpl(e, textArea);

		String expected = origText.replace("foo", "");
		String actual = textArea.getText();
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetMacroId() {
		RSyntaxTextAreaEditorKit.DeletePrevWordAction a = new RSyntaxTextAreaEditorKit.DeletePrevWordAction();
		Assert.assertEquals(RSyntaxTextAreaEditorKit.rtaDeletePrevWordAction, a.getMacroID());
	}
}
