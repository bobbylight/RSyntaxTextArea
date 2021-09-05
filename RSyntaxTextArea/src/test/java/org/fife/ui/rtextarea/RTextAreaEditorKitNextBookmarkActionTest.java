/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.NextBookmarkAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaEditorKitNextBookmarkActionTest {

	private RTextArea textArea;
	private Gutter gutter;


	@BeforeEach
	void setUp() {

		textArea = new RTextArea("line 1\nline 2\nline 3\nline 4");
		textArea.setCaretPosition(0);

		RTextScrollPane sp = new RTextScrollPane(textArea);

		gutter = sp.getGutter();
		gutter.setBookmarkingEnabled(true);
		gutter.setBookmarkIcon(new EmptyTestIcon());
	}


	@Test
	void testActionPerformedImpl_nextBookmark_doNothingIfNoBookmarks() {

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextBookmarkAction("foo", true).actionPerformedImpl(e, textArea);
		Assertions.assertEquals(0, textArea.getCaretPosition()); // Caret didn't move
	}


	@Test
	void testActionPerformedImpl_nextBookmark_happyPath() throws BadLocationException {

		gutter.toggleBookmark(1);
		gutter.toggleBookmark(3);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextBookmarkAction("foo", true).actionPerformedImpl(e, textArea);
		Assertions.assertEquals(1, textArea.getCaretLineNumber());

		new RTextAreaEditorKit.NextBookmarkAction("foo", true).actionPerformedImpl(e, textArea);
		Assertions.assertEquals(3, textArea.getCaretLineNumber());

		// Loops back
		new RTextAreaEditorKit.NextBookmarkAction("foo", true).actionPerformedImpl(e, textArea);
		Assertions.assertEquals(1, textArea.getCaretLineNumber());
	}


	@Test
	void testActionPerformedImpl_previousBookmark_happyPath() throws BadLocationException {

		gutter.toggleBookmark(1);
		gutter.toggleBookmark(3);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextBookmarkAction("foo", false).actionPerformedImpl(e, textArea);
		Assertions.assertEquals(3, textArea.getCaretLineNumber());

		new RTextAreaEditorKit.NextBookmarkAction("foo", false).actionPerformedImpl(e, textArea);
		Assertions.assertEquals(1, textArea.getCaretLineNumber());

		// Loops back
		new RTextAreaEditorKit.NextBookmarkAction("foo", false).actionPerformedImpl(e, textArea);
		Assertions.assertEquals(3, textArea.getCaretLineNumber());
	}


	@Test
	void testGetMacroID() {
		RTextAreaEditorKit.NextBookmarkAction action = new RTextAreaEditorKit.NextBookmarkAction("name", true);
		Assertions.assertEquals("name", action.getMacroID());
	}
}
