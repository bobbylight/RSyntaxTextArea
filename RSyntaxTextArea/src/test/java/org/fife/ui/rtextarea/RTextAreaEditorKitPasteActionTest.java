/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.PasteAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitPasteActionTest extends AbstractRTextAreaTest {


	@Test
	void testConstructor_multiArg() {
		RTextAreaEditorKit.PasteAction action = new RTextAreaEditorKit.PasteAction(
			"name", null, "Description", 0, null);
		Assertions.assertEquals("name", action.getName());
		Assertions.assertEquals("Description", action.getDescription());
	}


	@Test
	void testActionPerformedImpl() {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		RTextArea textArea = new RTextArea();

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("foo"), null);

		ActionEvent e = new ActionEvent(textArea, 0, "foo");
		new RTextAreaEditorKit.PasteAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("foo", textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(DefaultEditorKit.pasteAction,
			new RTextAreaEditorKit.PasteAction().getMacroID());
	}
}
