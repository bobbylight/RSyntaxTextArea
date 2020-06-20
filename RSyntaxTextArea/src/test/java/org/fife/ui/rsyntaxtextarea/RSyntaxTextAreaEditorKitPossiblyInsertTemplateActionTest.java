/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RSyntaxTextAreaEditorKit.PossiblyInsertTemplateAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RSyntaxTextAreaEditorKitPossiblyInsertTemplateActionTest extends AbstractRSyntaxTextAreaTest {

	private static boolean origTemplatesEnabled;


	@Before
	public void setUp() {
		origTemplatesEnabled = RSyntaxTextArea.getTemplatesEnabled();
	}


	@After
	public void tearDown() {
		RSyntaxTextArea.setTemplatesEnabled(origTemplatesEnabled);
	}


	@Test
	public void testActionPerformedImpl_notEnabled() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		RSyntaxTextArea.setTemplatesEnabled(true);
		textArea.setEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PossiblyInsertTemplateAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals(origContent, textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_templatesNotEnabled() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		RSyntaxTextArea.setTemplatesEnabled(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PossiblyInsertTemplateAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals(origContent + " ", textArea.getText());
	}


	@Test
	public void testActionPerformedImpl_templatesEnabled_noMatchingTemplate() {

		String origContent = "line 1\nline 2\nline 3";
		RSyntaxTextArea textArea = new RSyntaxTextArea(origContent);
		RSyntaxTextArea.setTemplatesEnabled(true);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RSyntaxTextAreaEditorKit.PossiblyInsertTemplateAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals(origContent + " ", textArea.getText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RSyntaxTextAreaEditorKit.rstaPossiblyInsertTemplateAction,
			new RSyntaxTextAreaEditorKit.PossiblyInsertTemplateAction().getMacroID());
	}
}
