/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;


/**
 * The text entry mode for an {@link RTextArea}.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see RTextArea#getTextMode()
 * @see RTextArea#setTextMode(TextMode)
 */
public enum TextMode {

	/** Characters typed are inserted at the caret position. */
	INSERT,

	/** Characters typed overwrite the character at the caret position. */
	OVERWRITE,
}
