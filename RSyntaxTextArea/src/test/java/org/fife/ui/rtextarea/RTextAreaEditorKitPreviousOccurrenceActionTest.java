/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.PreviousOccurrenceAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitPreviousOccurrenceActionTest {

	private RTextArea textArea;


	@Before
	public void setUp() {

		textArea = new RTextArea("one two one two\none two");
		textArea.setCaretPosition(textArea.getText().length());
	}


	@After
	public void tearDown() {
		RTextArea.setSelectedOccurrenceText(null);
	}


	@Test
	public void testActionPerformedImpl_noSelection_alsoNoPriorPreviousOccurrence() {

		// No selection and no previous "next occurrence" search

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.PreviousOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		int endOffs = textArea.getText().length();
		Assert.assertEquals(endOffs, textArea.getSelectionStart());
		Assert.assertEquals(endOffs, textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_noSelection_nextOnSameLine() {

		// No selection but "two" was previously searched for this way
		RTextArea.setSelectedOccurrenceText("two");

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.PreviousOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assert.assertEquals(20, textArea.getSelectionStart());
		Assert.assertEquals(23, textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_selection_nextOnSameLine() {

		// Select "two" on the first line
		textArea.setCaretPosition(15);
		textArea.moveCaretPosition(12);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// Prior instance of "two" on the first line is selected
		new RTextAreaEditorKit.PreviousOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assert.assertEquals(4, textArea.getSelectionStart());
		Assert.assertEquals(7, textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_selection_nextOnPreviousLine() {

		// Select "one" on line 2
		textArea.setCaretPosition(16);
		textArea.moveCaretPosition(19);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// Select second "one" on line 1
		new RTextAreaEditorKit.PreviousOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assert.assertEquals(8, textArea.getSelectionStart());
		Assert.assertEquals(11, textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_selection_noNextMatch() {

		// Select first "one"
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(3);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// Selection didn't change
		new RTextAreaEditorKit.PreviousOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assert.assertEquals(0, textArea.getSelectionStart());
		Assert.assertEquals(3, textArea.getSelectionEnd());
	}


	@Test
	public void testGetMacroId() {
		Assert.assertEquals("foo",
			new RTextAreaEditorKit.PreviousOccurrenceAction("foo").getMacroID());
	}
}
