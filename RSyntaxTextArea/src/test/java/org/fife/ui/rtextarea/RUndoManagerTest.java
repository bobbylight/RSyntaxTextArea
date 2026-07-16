/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.text.BadLocationException;


/**
 * Unit tests for the {@link RUndoManager} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RUndoManagerTest {


	/**
	 * Simulates the user typing {@code str} one character at a time at the
	 * end of {@code textArea}, since {@code append()} does not necessarily
	 * move the caret the way real key presses would.
	 */
	private static void type(RTextArea textArea, String str) throws BadLocationException {
		for (int i = 0; i < str.length(); i++) {
			int end = textArea.getDocument().getLength();
			textArea.getDocument().insertString(end, String.valueOf(str.charAt(i)), null);
			textArea.setCaretPosition(end + 1);
		}
	}


	@Test
	void testUndoableEditHappened_newLineStartsNewCompoundEdit() throws BadLocationException {

		RTextArea textArea = new RTextArea();

		type(textArea, "foo");
		type(textArea, "\n");
		type(textArea, "bar");

		Assertions.assertEquals("foo\nbar", textArea.getText());

		// "bar" is undone as its own edit.
		textArea.undoLastAction();
		Assertions.assertEquals("foo\n", textArea.getText());

		// The newline is undone separately from the text before or after it.
		textArea.undoLastAction();
		Assertions.assertEquals("foo", textArea.getText());

		// "foo" is undone as its own edit.
		textArea.undoLastAction();
		Assertions.assertEquals("", textArea.getText());

		Assertions.assertFalse(textArea.canUndo());
	}


	/**
	 * Simulates pasting {@code str} at the end of {@code textArea} as a
	 * single edit, the way {@code JTextComponent.paste()} would, rather
	 * than one character at a time like {@link #type(RTextArea, String)}.
	 */
	private static void paste(RTextArea textArea, String str) throws BadLocationException {
		int end = textArea.getDocument().getLength();
		textArea.getDocument().insertString(end, str, null);
		textArea.setCaretPosition(end + str.length());
	}


	@Test
	void testUndoableEditHappened_pastedTextWithNewlineIsUndoneAsOneEdit() throws BadLocationException {

		RTextArea textArea = new RTextArea();

		type(textArea, "foo");
		paste(textArea, "one\ntwo");

		Assertions.assertEquals("fooone\ntwo", textArea.getText());

		// The entire pasted blob, newline included, is undone as one edit.
		textArea.undoLastAction();
		Assertions.assertEquals("foo", textArea.getText());

		// "foo" is undone as its own edit.
		textArea.undoLastAction();
		Assertions.assertEquals("", textArea.getText());

		Assertions.assertFalse(textArea.canUndo());
	}


	@Test
	void testUndoableEditHappened_atomicNewLineStartsNewCompoundEdit() throws BadLocationException {

		RTextArea textArea = new RTextArea();

		type(textArea, "foo");

		// Simulates a newline insertion wrapped in an atomic edit, the way
		// RSyntaxTextAreaEditorKit.InsertBreakAction wraps a newline plus
		// auto-indent whitespace in beginAtomicEdit()/endAtomicEdit().
		textArea.beginAtomicEdit();
		try {
			type(textArea, "\n");
		} finally {
			textArea.endAtomicEdit();
		}

		type(textArea, "bar");

		Assertions.assertEquals("foo\nbar", textArea.getText());

		// "bar" is undone as its own edit.
		textArea.undoLastAction();
		Assertions.assertEquals("foo\n", textArea.getText());

		// The atomically-inserted newline is undone separately, just like a
		// plain (non-atomic) newline insertion would be.
		textArea.undoLastAction();
		Assertions.assertEquals("foo", textArea.getText());

		// "foo" is undone as its own edit.
		textArea.undoLastAction();
		Assertions.assertEquals("", textArea.getText());

		Assertions.assertFalse(textArea.canUndo());
	}


	@Test
	void testUndoableEditHappened_multipleNewLinesAreSeparateEdits() throws BadLocationException {

		RTextArea textArea = new RTextArea();

		type(textArea, "\n");
		type(textArea, "\n");

		Assertions.assertEquals("\n\n", textArea.getText());

		textArea.undoLastAction();
		Assertions.assertEquals("\n", textArea.getText());

		textArea.undoLastAction();
		Assertions.assertEquals("", textArea.getText());

		Assertions.assertFalse(textArea.canUndo());
	}


	@Test
	void testIsEnabled_defaultsToTrue() {
		RTextArea textArea = new RTextArea();
		Assertions.assertTrue(textArea.getUndoManager().isEnabled());
	}


	@Test
	void testSetEnabled_false_noLongerRecordsEdits() throws BadLocationException {

		RTextArea textArea = new RTextArea();
		RUndoManager undoManager = textArea.getUndoManager();

		undoManager.setEnabled(false);
		Assertions.assertFalse(undoManager.isEnabled());

		type(textArea, "foo");

		Assertions.assertEquals("foo", textArea.getText());
		Assertions.assertFalse(textArea.canUndo());
	}


	@Test
	void testSetEnabled_false_discardsExistingEdits() throws BadLocationException {

		RTextArea textArea = new RTextArea();
		RUndoManager undoManager = textArea.getUndoManager();

		type(textArea, "foo");
		Assertions.assertTrue(textArea.canUndo());

		undoManager.setEnabled(false);
		Assertions.assertFalse(textArea.canUndo());
	}


	@Test
	void testSetEnabled_false_endsInProgressAtomicEdit() throws BadLocationException {

		RTextArea textArea = new RTextArea();
		RUndoManager undoManager = textArea.getUndoManager();

		textArea.beginAtomicEdit();
		try {
			type(textArea, "foo");
			undoManager.setEnabled(false);
		} finally {
			// Should be a no-op since the manager is disabled, and must not
			// throw despite the internal atomic edit depth being reset.
			textArea.endAtomicEdit();
		}

		Assertions.assertFalse(textArea.canUndo());
	}


	@Test
	void testSetEnabled_true_afterBeingDisabled_resumesRecordingEdits() throws BadLocationException {

		RTextArea textArea = new RTextArea();
		RUndoManager undoManager = textArea.getUndoManager();

		undoManager.setEnabled(false);
		type(textArea, "foo");

		undoManager.setEnabled(true);
		Assertions.assertTrue(undoManager.isEnabled());
		type(textArea, "bar");

		Assertions.assertEquals("foobar", textArea.getText());
		Assertions.assertTrue(textArea.canUndo());

		textArea.undoLastAction();
		Assertions.assertEquals("foo", textArea.getText());
	}


	@Test
	void testSetEnabled_sameValue_isNoOp() throws BadLocationException {

		RTextArea textArea = new RTextArea();
		RUndoManager undoManager = textArea.getUndoManager();

		type(textArea, "foo");
		Assertions.assertTrue(textArea.canUndo());

		undoManager.setEnabled(true);

		Assertions.assertTrue(textArea.canUndo());
		textArea.undoLastAction();
		Assertions.assertEquals("", textArea.getText());
	}
}
