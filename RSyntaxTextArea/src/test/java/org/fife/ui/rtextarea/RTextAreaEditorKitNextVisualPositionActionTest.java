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

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * Unit tests for the {@link RTextAreaEditorKit.NextVisualPositionAction} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaEditorKitNextVisualPositionActionTest extends AbstractRTextAreaTest {

	private RTextArea textArea;


	@Before
	public void setUp() {

		textArea = new RTextArea("one two one two\none two");
		textArea.setSize(800, 800);

		// BasicTextUI requires at least one paint cycle for visual positions to be computed
		textArea.paint(createTestGraphics());
	}


	@Test
	public void testActionPerformedImpl_noSelection_noSelect_east() {

		textArea.setCaretPosition(6);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextVisualPositionAction("foo", false, SwingConstants.EAST).
			actionPerformedImpl(e, textArea);
		Assert.assertEquals(7, textArea.getCaret().getDot());
	}


	@Test
	public void testActionPerformedImpl_noSelection_noSelect_north() {

		textArea.setCaretPosition(16);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextVisualPositionAction("foo", false, SwingConstants.NORTH).
			actionPerformedImpl(e, textArea);
		Assert.assertEquals(0, textArea.getCaret().getDot());
	}


	@Test
	public void testActionPerformedImpl_noSelection_noSelect_south() {

		textArea.setCaretPosition(0);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextVisualPositionAction("foo", false, SwingConstants.SOUTH).
			actionPerformedImpl(e, textArea);
		Assert.assertEquals(16, textArea.getCaret().getDot());
	}


	@Test
	public void testActionPerformedImpl_noSelection_noSelect_west() {

		textArea.setCaretPosition(6);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextVisualPositionAction("foo", false, SwingConstants.WEST).
			actionPerformedImpl(e, textArea);
		Assert.assertEquals(5, textArea.getCaretPosition());
	}


	@Test
	public void testActionPerformedImpl_noSelection_select_east() {

		textArea.setCaretPosition(6);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextVisualPositionAction("foo", true, SwingConstants.EAST).
			actionPerformedImpl(e, textArea);
		Assert.assertEquals(6, textArea.getSelectionStart());
		Assert.assertEquals(7, textArea.getSelectionEnd());
		Assert.assertEquals(7, textArea.getCaretPosition());
	}


	@Test
	public void testActionPerformedImpl_noSelection_select_west() {

		textArea.setCaretPosition(6);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		new RTextAreaEditorKit.NextVisualPositionAction("foo", true, SwingConstants.WEST).
			actionPerformedImpl(e, textArea);
		Assert.assertEquals(5, textArea.getSelectionStart());
		Assert.assertEquals(6, textArea.getSelectionEnd());
		Assert.assertEquals(5, textArea.getCaretPosition());
	}


	@Test
	public void testActionPerformedImpl_selection_noSelect_east() {

		textArea.setSelectionStart(3);
		textArea.setSelectionEnd(6);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// Selection removed, caret at end of prior selection
		new RTextAreaEditorKit.NextVisualPositionAction("foo", false, SwingConstants.EAST).
			actionPerformedImpl(e, textArea);
		Assert.assertEquals(6, textArea.getSelectionStart());
		Assert.assertEquals(6, textArea.getSelectionEnd());
	}


	@Test
	public void testActionPerformedImpl_selection_noSelect_west() {

		textArea.setSelectionStart(3);
		textArea.setSelectionEnd(6);

		ActionEvent e = new ActionEvent(textArea, 0, "command");

		// Selection removed, caret at start of prior selection
		new RTextAreaEditorKit.NextVisualPositionAction("foo", false, SwingConstants.WEST).
			actionPerformedImpl(e, textArea);
		Assert.assertEquals(3, textArea.getSelectionStart());
		Assert.assertEquals(3, textArea.getSelectionEnd());
	}


	@Test
	public void testGetMacroId() {
		Assert.assertEquals("foo",
			new RTextAreaEditorKit.NextVisualPositionAction("foo", false,
				SwingConstants.EAST).getMacroID());
	}
}
