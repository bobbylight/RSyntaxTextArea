/*
 * 06/30/2012
 *
 * RDocument.java - Document class used by RTextAreas.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


/**
 * The document implementation used by instances of <code>RTextArea</code>.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RDocument extends PlainDocument {

	/**
	 * Constructor.
	 */
	public RDocument() {
		this(new RGapContent());
	}

	public RDocument(Content content) {
		super(content);
	}

	/**
	 * Returns the character in the document at the specified offset.
	 *
	 * @param offset The offset of the character.
	 * @return The character.
	 * @throws BadLocationException If the offset is invalid.
	 */
	public char charAt(int offset) throws BadLocationException {
		return ((RContent) getContent()).charAt(offset);
	}
}
