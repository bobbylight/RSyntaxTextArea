package org.fife.ui.rtextarea;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;

/**
 * An interface for AbstractDocument.Content implementations that can be used in
 * {@link RDocument}'s. It defines fast direct read access to single characters in the content.
 */
public interface RContent extends AbstractDocument.Content {

    /**
     * Allows access to a single character in the content.
     * @param offset    the offset of the character to be accessed.
     * @return the character at the given offset.
     * @throws BadLocationException in case the given offset is outside the content.
     */
    char charAt(int offset) throws BadLocationException;
}
