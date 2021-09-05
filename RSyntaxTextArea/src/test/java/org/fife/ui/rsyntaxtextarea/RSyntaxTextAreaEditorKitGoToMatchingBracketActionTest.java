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
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.GoToMatchingBracketAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitGoToMatchingBracketActionTest extends AbstractRSyntaxTextAreaTest {

	@Test
	void testActionPerformedImpl_goToMatchingBracket() {

		String origContent = "public void foo() {\n" +
			"  if (something) {\n" +
			"    System.out.println(\"Hello world\");\n" +
			"  }\n" +
			"}";
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_JAVA, origContent);

		textArea.setCaretPosition(origContent.indexOf('{'));

		RecordableTextAction a = new RSyntaxTextAreaEditorKit.GoToMatchingBracketAction();
		ActionEvent e = createActionEvent(textArea, RSyntaxTextAreaEditorKit.rstaGoToMatchingBracketAction);
		a.actionPerformedImpl(e, textArea);

		int expectedOffset = origContent.lastIndexOf('}') + 1;
		Assertions.assertEquals(expectedOffset, textArea.getCaretPosition());
	}

	@Test
	void testGetMacroId() {
		RSyntaxTextAreaEditorKit.GoToMatchingBracketAction a = new RSyntaxTextAreaEditorKit.GoToMatchingBracketAction();
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaGoToMatchingBracketAction, a.getMacroID());
	}
}
