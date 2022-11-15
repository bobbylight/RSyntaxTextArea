package org.fife.ui.rtextarea.readonly;

import org.fife.ui.rtextarea.RContent;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;

public interface ReadOnlyContentInterface extends AbstractDocument.Content, RContent {
	char charAt(int offset) throws BadLocationException;

	int getStartOffset(int row);

	int getEndOffset(int index);

	int getElementCount();

	int getElementIndex(int offset);
}
