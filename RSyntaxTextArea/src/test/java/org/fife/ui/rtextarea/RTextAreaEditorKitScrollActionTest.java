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
 * Unit tests for the {@link RTextAreaEditorKit.ScrollAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitScrollActionTest {


	@Test
	public void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea("foo");
		RTextScrollPane sp = new RTextScrollPane(textArea);

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.ScrollAction("scroll", 1).actionPerformedImpl(e, textArea);

		// Not sure how to verify
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals("scroll",
			new RTextAreaEditorKit.ScrollAction("scroll", 1).getMacroID());
	}
}
