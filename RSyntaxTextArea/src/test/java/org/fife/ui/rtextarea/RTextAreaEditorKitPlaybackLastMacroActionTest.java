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
import java.util.Collections;


/**
 * Unit tests for the {@link RTextAreaEditorKit.PlaybackLastMacroAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitPlaybackLastMacroActionTest extends AbstractRTextAreaTest {


	@Test
	public void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();

		Macro macro = new Macro("test", Collections.singletonList(
			new Macro.MacroRecord(DefaultEditorKit.defaultKeyTypedAction, "x")
		));
		RTextArea.loadMacro(macro);

		ActionEvent e = new ActionEvent(textArea, 0, "foo");
		new RTextAreaEditorKit.PlaybackLastMacroAction().actionPerformedImpl(e, textArea);

		Assert.assertEquals("x", textArea.getText());
	}


	@Test
	public void testGetMacroID() {
		Assert.assertEquals(RTextAreaEditorKit.rtaPlaybackLastMacroAction,
			new RTextAreaEditorKit.PlaybackLastMacroAction().getMacroID());
	}

	@Test
	public void testIsRecordable() {
		Assert.assertFalse(new RTextAreaEditorKit.PlaybackLastMacroAction().isRecordable());
	}
}
