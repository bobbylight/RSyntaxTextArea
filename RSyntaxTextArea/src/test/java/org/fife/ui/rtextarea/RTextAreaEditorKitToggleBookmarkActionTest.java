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
 * Unit tests for the {@link RTextAreaEditorKit.ToggleBookmarkAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitToggleBookmarkActionTest {


	@Test
	void testActionPerformedImpl() {

		RTextArea textArea = new RTextArea("line 1\nline 2\nline 3");
		textArea.setCaretPosition(textArea.getText().indexOf('2'));
		RTextScrollPane sp = new RTextScrollPane(textArea);
		Gutter gutter = sp.getGutter();

		gutter.setBookmarkingEnabled(true);
		gutter.setBookmarkIcon(new EmptyTestIcon());

		ActionEvent e = new ActionEvent(textArea, 0, "command");
		new RTextAreaEditorKit.ToggleBookmarkAction().actionPerformedImpl(e, textArea);

		Assertions.assertEquals(1, gutter.getBookmarks().length);
	}


	@Test
	void testGetMacroID() {
		Assertions.assertEquals(RTextAreaEditorKit.rtaToggleBookmarkAction,
			new RTextAreaEditorKit.ToggleBookmarkAction().getMacroID());
	}
}
