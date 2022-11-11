package org.fife.ui.rtextarea.readonly;

import org.fife.ui.rtextarea.RContent;

import javax.swing.text.AbstractDocument;

public interface ReadOnlyContentInterface extends AbstractDocument.Content, RContent {
	char charAt(int offset);

	int getStartOffset(int row);

	int getEndOffset(int index);

	int getElementCount();

	int getElementIndex(int offset);
}
