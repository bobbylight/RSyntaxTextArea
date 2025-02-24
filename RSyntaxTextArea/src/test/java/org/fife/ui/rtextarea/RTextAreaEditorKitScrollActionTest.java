/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.ScrollAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitScrollActionTest {


	@Test
	void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea("foo");
		new RTextScrollPane(textArea); // Needed for the test

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.ScrollAction("scroll", 1).actionPerformedImpl(e, textArea);

		// Not sure how to verify
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals("scroll",
			new RTextAreaEditorKit.ScrollAction("scroll", 1).getMacroID());
	}
}
