/*
 * 12/06/2008
 *
 * RUndoManager.java - Handles undo/redo behavior for RTextArea.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.util.ResourceBundle;

import javax.swing.Action;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.BadLocationException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;


/**
 * This class manages undos/redos for a particular editor pane.  It groups
 * all undos that occur one character position apart together, to avoid
 * Java's horrible "one character at a time" undo behavior.  It also
 * recognizes "replace" actions (i.e., text is selected, then the user
 * types), and treats it as a single action, instead of a remove/insert
 * action pair.
 * <p>
 * Pressing Enter to insert a newline ends the current compound edit and
 * starts a new one, so that undoing after typing on a new line won't also
 * remove the newline and any text typed before it.  This matches the
 * behavior of editors such as IntelliJ and VS Code.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RUndoManager extends UndoManager {

	private RCompoundEdit compoundEdit;
	private RTextArea textArea;
	private int lastOffset;
	private String cantUndoText;
	private String cantRedoText;
	private boolean enabled;

	private int internalAtomicEditDepth;

	private static final String MSG = "org.fife.ui.rtextarea.RTextArea";


	/**
	 * Constructor.
	 *
	 * @param textArea The parent text area.
	 */
	public RUndoManager(RTextArea textArea) {
		this.textArea = textArea;
		this.enabled = true;
		ResourceBundle msg = ResourceBundle.getBundle(MSG);
		cantUndoText = msg.getString("Action.CantUndo.Name");
		cantRedoText = msg.getString("Action.CantRedo.Name");
	}


	/**
	 * Begins an "atomic" edit.  This method is called when RTextArea
	 * KNOWS that some edits should be compound automatically, such as
	 * when the user is typing in overwrite mode (the deletion of the
	 * current char + insertion of the new one) or the playing back of a
	 * macro.
	 *
	 * @see #endInternalAtomicEdit()
	 */
	public void beginInternalAtomicEdit() {
		if (!enabled) {
			return;
		}
		if (++internalAtomicEditDepth==1) {
			if (compoundEdit!=null) {
				compoundEdit.end();
			}
			compoundEdit = new RCompoundEdit();
		}
	}


	/**
	 * Ends an "atomic" edit.
	 *
	 * @see #beginInternalAtomicEdit()
	 */
	public void endInternalAtomicEdit() {
		if (!enabled) {
			return;
		}
		if (internalAtomicEditDepth>0 && --internalAtomicEditDepth==0) {
			addEdit(compoundEdit);
			compoundEdit.end();
			compoundEdit = null;
			updateActions();	// Needed to show the new display name.
		}
	}


	/**
	 * Returns whether this undo manager is recording undoable edits.
	 *
	 * @return Whether this undo manager is enabled.
	 * @see #setEnabled(boolean)
	 */
	public boolean isEnabled() {
		return enabled;
	}


	/**
	 * Sets whether this undo manager records undoable edits.  Applications
	 * that perform their own large-scale, high-frequency document
	 * modifications and have no need for undo/redo on a given text area
	 * (e.g. a scrolling, read-only log console) should call
	 * {@code setEnabled(false)} rather than relying on
	 * {@link #setLimit(int)}.  Contrary to what one might expect,
	 * {@code setLimit(0)} does <em>not</em> disable undo tracking; per the
	 * contract of {@link UndoManager#setLimit(int)}, it instead makes the
	 * edit history unbounded, which can quickly exhaust memory.
	 * <p>
	 * Disabling this undo manager discards any edits already being tracked,
	 * since they can no longer be undone or redone in a meaningful way once
	 * tracking stops.  Re-enabling it does not restore those edits; it
	 * simply resumes recording edits from that point forward.
	 *
	 * @param enabled Whether this undo manager should record undoable
	 *        edits.
	 * @see #isEnabled()
	 */
	public void setEnabled(boolean enabled) {
		if (enabled != this.enabled) {
			this.enabled = enabled;
			internalAtomicEditDepth = 0;
			compoundEdit = null;
			discardAllEdits();
			updateActions();
		}
	}


	/**
	 * Returns the localized "Can't Redo" string.
	 *
	 * @return The localized "Can't Redo" string.
	 * @see #getCantUndoText()
	 */
	public String getCantRedoText() {
		return cantRedoText;
	}


	/**
	 * Returns the localized "Can't Undo" string.
	 *
	 * @return The localized "Can't Undo" string.
	 * @see #getCantRedoText()
	 */
	public String getCantUndoText() {
		return cantUndoText;
	}


	/**
	 * Returns whether the undoable edit represents the insertion of a
	 * single newline character.
	 *
	 * @param e The event to check.
	 * @return Whether the event represents a single newline insertion.
	 */
	private boolean isSingleNewLineInsertion(UndoableEditEvent e) {
		UndoableEdit edit = e.getEdit();
		if (edit instanceof DocumentEvent) {
			DocumentEvent de = (DocumentEvent)edit;
			if (de.getType() == DocumentEvent.EventType.INSERT && de.getLength() == 1) {
				try {
					return "\n".equals(textArea.getText(de.getOffset(), 1));
				} catch (BadLocationException ble) {
					// Shouldn't happen
				}
			}
		}
		return false;
	}


	@Override
	public void redo() {
		super.redo();
		updateActions();
	}


	private RCompoundEdit startCompoundEdit(UndoableEdit edit) {
		lastOffset = textArea.getCaretPosition();
		compoundEdit = new RCompoundEdit();
		compoundEdit.addEdit(edit);
		addEdit(compoundEdit);
		return compoundEdit;
	}


	@Override
	public void undo() {
		super.undo();
		updateActions();
	}


	@Override
	public void undoableEditHappened(UndoableEditEvent e) {

		if (!enabled) {
			return;
		}

		boolean newLineInsertion = internalAtomicEditDepth==0 &&
			isSingleNewLineInsertion(e);

		// This happens when the first undoable edit occurs, and
		// just after an undo.  So, we need to update our actions.
		if (compoundEdit==null) {
			compoundEdit = startCompoundEdit(e.getEdit());
			updateActions();
			// A newline should be undone by itself, separately from
			// whatever gets typed after it.
			if (newLineInsertion) {
				compoundEdit.end();
				compoundEdit = null;
			}
			return;
		}

		else if (internalAtomicEditDepth>0) {
			compoundEdit.addEdit(e.getEdit());
			return;
		}

		// A newline ends whatever compound edit was in progress, and starts
		// (and immediately ends) a new one containing only the newline, so
		// undoing it doesn't also undo text typed before or after it.
		if (newLineInsertion) {
			compoundEdit.end();
			compoundEdit = startCompoundEdit(e.getEdit());
			compoundEdit.end();
			compoundEdit = null;
			return;
		}

		// This happens when there's already an undo that has occurred.
		// Test to see if these undos are on back-to-back characters,
		// and if they are, group them as a single edit.  Since an
		// undo has already occurred, there is no need to update our
		// actions here.
		int diff = textArea.getCaretPosition() - lastOffset;
		// "<=1" allows contiguous "overwrite mode" key presses to be
		// grouped together.
		if (Math.abs(diff)<=1) {//==1) {
			compoundEdit.addEdit(e.getEdit());
			lastOffset += diff;
			//updateActions();
			return;
		}

		// This happens when this UndoableEdit didn't occur at the
		// character just after the previous undoable edit.  Since an
		// undo has already occurred, there is no need to update our
		// actions here either.
		compoundEdit.end();
		compoundEdit = startCompoundEdit(e.getEdit());
		//updateActions();

	}


	/**
	 * Ensures that undo/redo actions are enabled appropriately and have
	 * descriptive text at all times.
	 */
	public void updateActions() {

		String text;

		Action a = RTextArea.getAction(RTextArea.UNDO_ACTION);
		if (canUndo()) {
			a.setEnabled(true);
			text = getUndoPresentationName();
			a.putValue(Action.NAME, text);
			a.putValue(Action.SHORT_DESCRIPTION, text);
		}
		else {
			if (a.isEnabled()) {
				a.setEnabled(false);
				text = cantUndoText;
				a.putValue(Action.NAME, text);
				a.putValue(Action.SHORT_DESCRIPTION, text);
			}
		}

		a = RTextArea.getAction(RTextArea.REDO_ACTION);
		if (canRedo()) {
			a.setEnabled(true);
			text = getRedoPresentationName();
			a.putValue(Action.NAME, text);
			a.putValue(Action.SHORT_DESCRIPTION, text);
		}
		else {
			if (a.isEnabled()) {
				a.setEnabled(false);
				text = cantRedoText;
				a.putValue(Action.NAME, text);
				a.putValue(Action.SHORT_DESCRIPTION, text);
			}
		}

	}

	/**
	 * The edit used by {@link RUndoManager}.
	 */
	class RCompoundEdit extends CompoundEdit {

		@Override
		public String getUndoPresentationName() {
			return UIManager.getString("AbstractUndoableEdit.undoText");
		}

		@Override
		public String getRedoPresentationName() {
			return UIManager.getString("AbstractUndoableEdit.redoText");
		}

		@Override
		public boolean isInProgress() {
			return false;
		}

		@Override
		public void undo() {
			if (compoundEdit!=null) {
				compoundEdit.end();
			}
			super.undo();
			compoundEdit = null;
		}

	}


}
