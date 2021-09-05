/*
 * 08/22/2015
 *
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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.DeletePrevWordAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitDeletePrevWordActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testActionPerformedImpl_deletePrevWord() {

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
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.DeletePrevWordAction a = new RSyntaxTextAreaEditorKit.DeletePrevWordAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rtaDeletePrevWordAction, a.getMacroID());
	}
}
