/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;
import java.util.Collections;


/**
 * Unit tests for the {@link RTextAreaEditorKit.PlaybackLastMacroAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitPlaybackLastMacroActionTest extends AbstractRTextAreaTest {


	@Test
	void testConstructor_multiArg() {
		RTextAreaEditorKit.PlaybackLastMacroAction action = new RTextAreaEditorKit.PlaybackLastMacroAction(
			"name", null, "Description", 0, null);
		Assertions.assertEquals("name", action.getName());
		Assertions.assertEquals("Description", action.getDescription());
	}


	@Test
	void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea();

		Macro macro = new Macro("test", Collections.singletonList(
			new Macro.MacroRecord(DefaultEditorKit.defaultKeyTypedAction, "x")
		));
		RTextArea.loadMacro(macro);

		ActionEvent e = new ActionEvent(textArea, 0, "foo");
		new RTextAreaEditorKit.PlaybackLastMacroAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals("x", textArea.getText());
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaPlaybackLastMacroAction,
			new RTextAreaEditorKit.PlaybackLastMacroAction().getMacroID());
	}

	@Test
	void testIsRecordable() {
		Assertions.assertFalse(new RTextAreaEditorKit.PlaybackLastMacroAction().isRecordable());
	}
}
