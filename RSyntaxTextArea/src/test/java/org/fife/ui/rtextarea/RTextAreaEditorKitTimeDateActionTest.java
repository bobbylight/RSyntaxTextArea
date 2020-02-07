/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.TimeDateAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitTimeDateActionTest {


	@Test
	public void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.TimeDateAction().actionPerformedImpl(e, textArea);

		Assert.assertTrue(textArea.getText().length() > 0);
	}


	@Test
	public void testActionPerformedImpl_notEditable() {

		RTextArea textArea = new RTextArea();
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.TimeDateAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals(0, textArea.getText().length());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.rtaTimeDateAction,
			new RTextAreaEditorKit.TimeDateAction().getMacroID());
	}
}
