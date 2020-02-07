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
 * Unit tests for the {@link RTextAreaEditorKit.SetWritableAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitSetWritableActionTest {


	@Test
	public void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();
		textArea.setEditable(false);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.SetWritableAction().actionPerformedImpl(e, textArea);

		Assert.assertTrue(textArea.isEditable());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(DefaultEditorKit.writableAction,
			new RTextAreaEditorKit.SetWritableAction().getMacroID());
	}
}
