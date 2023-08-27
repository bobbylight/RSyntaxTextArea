/*
 * 08/23/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenTypes;


/**
 * Scanner for Less files.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class LessTokenMaker extends CSSTokenMaker {


	/**
	 * Constructor; overridden to enable the niceties added by Less.
	 */
	public LessTokenMaker() {
		setHighlightingLess(true);
	}


	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[] { "//", null };
	}


	@Override
	public boolean getMarkOccurrencesOfTokenType(int type) {
		return type == TokenTypes.VARIABLE ||
				super.getMarkOccurrencesOfTokenType(type);
	}


}
