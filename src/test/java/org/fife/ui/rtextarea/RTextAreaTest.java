package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit tests for the {@link RTextArea} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTextAreaTest {


	@Test
	public void testCanRedo() {
		RTextArea textArea = new RTextArea();
		Assert.assertFalse(textArea.canRedo());
		textArea.replaceSelection("Hi");
		Assert.assertFalse(textArea.canRedo());
		textArea.undoLastAction();
		Assert.assertTrue(textArea.canRedo());
		textArea.redoLastAction();
		Assert.assertFalse(textArea.canRedo());
	}


	@Test
	public void testCanUndo() {
		RTextArea textArea = new RTextArea();
		Assert.assertFalse(textArea.canUndo());
		textArea.replaceSelection("Hi");
		Assert.assertTrue(textArea.canUndo());
		textArea.undoLastAction();
		Assert.assertFalse(textArea.canUndo());
	}


	@Test
	public void testCreateDefaultModel() {
		RTextArea textArea = new RTextArea();
		Assert.assertTrue(textArea.createDefaultModel() instanceof RDocument);
	}


	@Test
	public void testDiscardAllEdits() {
		RTextArea textArea = new RTextArea();
		textArea.replaceSelection("Hi");
		Assert.assertTrue(textArea.canUndo());
		textArea.discardAllEdits();
		Assert.assertFalse(textArea.canUndo());
	}


	@Test
	public void testGetPopupMenu() {
		RTextArea textArea = new RTextArea();
		Assert.assertNotNull(textArea.getPopupMenu());
	}


	@Test
	public void testRecordingMacro_happyPath() {
		Assert.assertFalse(RTextArea.isRecordingMacro());
		RTextArea.beginRecordingMacro();
		Assert.assertTrue(RTextArea.isRecordingMacro());
		RTextArea.endRecordingMacro();
		Assert.assertFalse(RTextArea.isRecordingMacro());
	}


	@Test
	public void testRecordingMacro_endWhileNotRecording() {
		Assert.assertFalse(RTextArea.isRecordingMacro());
		Assert.assertFalse(RTextArea.isRecordingMacro());
		RTextArea.endRecordingMacro();
		Assert.assertFalse(RTextArea.isRecordingMacro());
	}


	@Test
	public void testMarkAllOnOccurrenceSearches() {
		RTextArea textArea = new RTextArea();
		Assert.assertTrue(textArea.getMarkAllOnOccurrenceSearches());
		textArea.setMarkAllOnOccurrenceSearches(false);
		Assert.assertFalse(textArea.getMarkAllOnOccurrenceSearches());
	}


}