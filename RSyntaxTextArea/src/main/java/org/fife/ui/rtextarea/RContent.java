package org.fife.ui.rtextarea;

import javax.swing.text.BadLocationException;

public interface RContent {
	char charAt(int offset) throws BadLocationException;
}
