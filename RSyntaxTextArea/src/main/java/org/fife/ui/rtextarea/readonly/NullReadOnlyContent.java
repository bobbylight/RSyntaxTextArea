package org.fife.ui.rtextarea.readonly;

import org.fife.ui.rtextarea.readonly.ReadOnlyContentInterface;

import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.undo.UndoableEdit;

public class NullReadOnlyContent implements ReadOnlyContentInterface {
	@Override
	public char charAt(int offset) {
		return 0;
	}

	@Override
	public int getStartOffset(int row) {
		return 0;
	}

	@Override
	public int getEndOffset(int index) {
		return 0;
	}

	@Override
	public int getElementCount() {
		return 0;
	}

	@Override
	public int getElementIndex(int offset) {
		return 0;
	}

	@Override
	public Position createPosition(int offset) {
		return null;
	}

	@Override
	public int length() {
		return 0;
	}

	@Override
	public UndoableEdit insertString(int where, String str) throws BadLocationException {
		return null;
	}

	@Override
	public UndoableEdit remove(int where, int nitems) throws BadLocationException {
		return null;
	}

	@Override
	public String getString(int where, int len) throws BadLocationException {
		return "";
	}

	@Override
	public void getChars(int where, int len, Segment txt) throws BadLocationException {

	}
}
