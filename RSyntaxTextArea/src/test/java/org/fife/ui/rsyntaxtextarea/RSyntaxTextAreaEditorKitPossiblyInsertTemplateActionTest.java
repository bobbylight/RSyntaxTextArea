/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.templates.CodeTemplate;
import org.fife.ui.rsyntaxtextarea.templates.StaticCodeTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.PossiblyInsertTemplateAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitPossiblyInsertTemplateActionTest extends AbstractRSyntaxTextAreaTest {

	private static boolean origTemplatesEnabled;


	@BeforeEach
	void setUp() {
		origTemplatesEnabled = RSyntaxTextArea.getTemplatesEnabled();
	}


	@AfterEach
	void tearDown() {
		RSyntaxTextArea.setTemplatesEnabled(origTemplatesEnabled);
	}


	@Test
	void testActionPerformedImpl_notEditable() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		RSyntaxTextArea.setTemplatesEnabled(true);
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PossiblyInsertTemplateAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(origContent, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_notEnabled() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		RSyntaxTextArea.setTemplatesEnabled(true);
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PossiblyInsertTemplateAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(origContent, textArea.getText());
	}


	@Test
	void testActionPerformedImpl_templatesNotEnabled() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		RSyntaxTextArea.setTemplatesEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PossiblyInsertTemplateAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(origContent + " ", textArea.getText());
	}


	@Test
	void testActionPerformedImpl_templatesEnabled_matchingTemplate() {

		String origContent = "toReplace";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		RSyntaxTextArea.setTemplatesEnabled(true);
		CodeTemplate template = new StaticCodeTemplate("toReplace", "foo", "bar");
		RSyntaxTextArea.getCodeTemplateManager().addTemplate(template);
		textArea.setCaretPosition(origContent.length());

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PossiblyInsertTemplateAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("foobar", textArea.getText());
		Assertions.assertEquals("foo".length(), textArea.getCaretPosition());
	}


	@Test
	void testActionPerformedImpl_templatesEnabled_noMatchingTemplate() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		RSyntaxTextArea.setTemplatesEnabled(true);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PossiblyInsertTemplateAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(origContent + " ", textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RSyntaxTextAreaEditorKit.rstaPossiblyInsertTemplateAction,
			new RSyntaxTextAreaEditorKit.PossiblyInsertTemplateAction().getMacroID());
	}
}
