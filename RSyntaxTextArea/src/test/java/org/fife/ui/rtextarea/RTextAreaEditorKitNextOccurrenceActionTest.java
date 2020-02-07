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
 * Unit tests for the {@link RTextAreaEditorKit.NextOccurrenceAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitNextOccurrenceActionTest {

	private RTextArea textArea;


	@Before
	public void setUp() {

		textArea = new RTextArea("one two one two\none two");
		textArea.setCaretPosition(0);
	}


	@After
	public void tearDown() {
		RTextArea.setSelectedOccurrenceText(null);
	}


	@Test
	public void testActionPerformedImpl_noSelection_alsoNoPriorNextOccurrence() {

		// No selection and no previous "next occurrence" search

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assert.assertEquals(0, textArea.getSelectionStart());
		Assert.assertEquals(0, textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_noSelection_nextOnSameLine() {

		// No selection but "two" was previously searched for this way
		RTextArea.setSelectedOccurrenceText("two");

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assert.assertEquals(4, textArea.getSelectionStart());
		Assert.assertEquals(7, textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_selection_nextOnSameLine() {

		// Select "one"
		textArea.setSelectionStart(0);
		textArea.setSelectionEnd(3);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assert.assertEquals(8, textArea.getSelectionStart());
		Assert.assertEquals(11, textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_selection_nextOnNextLine() {

		// Select second "one"
		textArea.setSelectionStart(8);
		textArea.setSelectionEnd(11);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assert.assertEquals(16, textArea.getSelectionStart());
		Assert.assertEquals(19, textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_selection_noNextMatch() {

		// Select last "one"
		textArea.setSelectionStart(16);
		textArea.setSelectionEnd(19);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// Selection didn't change
		new RTextAreaEditorKit.NextOccurrenceAction("foo").actionPerformedImpl(e, textArea);
		Assert.assertEquals(16, textArea.getSelectionStart());
		Assert.assertEquals(19, textArea.getSelectionEnd());
	}


	@Test
	public void testGetMacroId() {
		Assert.assertEquals("foo",
			new RTextAreaEditorKit.NextOccurrenceAction("foo").getMacroID());
	}
}
