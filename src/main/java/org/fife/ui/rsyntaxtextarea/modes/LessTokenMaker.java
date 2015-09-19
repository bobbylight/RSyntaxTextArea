/*
 * 08/23/2015
 *
 * Copyright (C) 2015 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSTALanguageSupport.License.txt file for details.
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


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[] { "//", null };
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getMarkOccurrencesOfTokenType(int type) {
		return type == TokenTypes.VARIABLE ||
				super.getMarkOccurrencesOfTokenType(type);
	}


}