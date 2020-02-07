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
 * Unit tests for the {@link RTextAreaEditorKit.UnselectAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitUnselectActionTest {


	@Test
	public void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();
		textArea.append("foo");
		textArea.selectAll();

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.UnselectAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals(3, textArea.getCaretPosition());
		Assert.assertNull(textArea.getSelectedText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.rtaUnselectAction,
			new RTextAreaEditorKit.UnselectAction().getMacroID());
	}
}
