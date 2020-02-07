/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.NextBookmarkAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitNextBookmarkActionTest {

	private RTextArea textArea;
	private Gutter gutter;


	@Before
	public void setUp() {

		textArea = new RTextArea("line 1\nline 2\nline 3\nline 4");
		textArea.setCaretPosition(0);

		RTextScrollPane sp = new RTextScrollPane(textArea);

		gutter = sp.getGutter();
		gutter.setBookmarkingEnabled(true);
		gutter.setBookmarkIcon(new EmptyTestIcon());
	}


	@Test
	public void testActionPerformedImpl_nextBookmark_doNothingIfNoBookmarks() {

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextBookmarkAction("foo", true).actionPerformedImpl(e, textArea);
		Assert.assertEquals(0, textArea.getCaretPosition()); // Caret didn't move
	}


	@Test
	public void testActionPerformedImpl_nextBookmark_happyPath() throws BadLocationException {

		gutter.toggleBookmark(1);
		gutter.toggleBookmark(3);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextBookmarkAction("foo", true).actionPerformedImpl(e, textArea);
		Assert.assertEquals(1, textArea.getCaretLineNumber());

		new RTextAreaEditorKit.NextBookmarkAction("foo", true).actionPerformedImpl(e, textArea);
		Assert.assertEquals(3, textArea.getCaretLineNumber());

		// Loops back
		new RTextAreaEditorKit.NextBookmarkAction("foo", true).actionPerformedImpl(e, textArea);
		Assert.assertEquals(1, textArea.getCaretLineNumber());
	}


	@Test
	public void testActionPerformedImpl_previousBookmark_happyPath() throws BadLocationException {

		gutter.toggleBookmark(1);
		gutter.toggleBookmark(3);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextBookmarkAction("foo", false).actionPerformedImpl(e, textArea);
		Assert.assertEquals(3, textArea.getCaretLineNumber());

		new RTextAreaEditorKit.NextBookmarkAction("foo", false).actionPerformedImpl(e, textArea);
		Assert.assertEquals(1, textArea.getCaretLineNumber());

		// Loops back
		new RTextAreaEditorKit.NextBookmarkAction("foo", false).actionPerformedImpl(e, textArea);
		Assert.assertEquals(3, textArea.getCaretLineNumber());
	}
}
