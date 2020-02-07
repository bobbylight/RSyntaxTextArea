/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.SetReadOnlyAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitSetReadOnlyActionTest {


	@Test
	public void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();
		Assert.assertTrue(textArea.isEditable());

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.SetReadOnlyAction().actionPerformedImpl(e, textArea);

		Assert.assertFalse(textArea.isEditable());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(DefaultEditorKit.readOnlyAction,
			new RTextAreaEditorKit.SetReadOnlyAction().getMacroID());
	}
}
