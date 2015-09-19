/*
 * 06/30/2012
 *
 * RDocument.java - Document class used by RTextAreas.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.GapContent;


/**
 * The document implementation used by instances of <code>RTextArea</code>.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RDocument extends DefaultStyledDocument {


	/**
	 * Constructor.
	 */
	public RDocument() {
		super();
	}


	/**
	 * Returns the character in the document at the specified offset.
	 *
	 * @param offset The offset of the character.
	 * @return The character.
	 * @throws BadLocationException If the offset is invalid.
	 */
	public char charAt(int offset) throws BadLocationException {
		//return ((RGapContent)getContent()).charAt(offset);
		return getText(offset, 1).charAt(0);
	}


	/**
	 * Document content that provides fast access to individual characters.
	 */
	private static class RGapContent extends GapContent {

		public char charAt(int offset) throws BadLocationException {
			if (offset<0 || offset>=length()) {
				throw new BadLocationException("Invalid offset", offset);
			}
			int g0 = getGapStart();
			char[] array = (char[]) getArray();
			if (offset<g0) { // below gap
				return array[offset];
			}
			return array[getGapEnd() + offset - g0]; // above gap
		}

	}


}