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
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.PasteAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitPasteActionTest extends AbstractRTextAreaTest {


	@Test
	public void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();

		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("foo"), null);

		ActionEvent e = new ActionEvent(textArea, 0, "foo");
		new RTextAreaEditorKit.PasteAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("foo", textArea.getText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(DefaultEditorKit.pasteAction,
			new RTextAreaEditorKit.PasteAction().getMacroID());
	}
}
