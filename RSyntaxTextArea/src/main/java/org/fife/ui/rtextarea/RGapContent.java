package org.fife.ui.rtextarea;

import javax.swing.text.BadLocationException;
import javax.swing.text.GapContent;

/**
 * Document content that provides fast access to individual characters.
 */
public class RGapContent extends GapContent implements RContent {

	@Override
	public char charAt(int offset) throws BadLocationException {
		if( offset < 0 || offset >= length() ){
			throw new BadLocationException("Invalid offset", offset);
		}
		int g0 = getGapStart();
		char[] array = (char[]) getArray();
		if( offset < g0 ){ // below gap
			return array[offset];
		}
		return array[getGapEnd() + offset - g0]; // above gap
	}

}
