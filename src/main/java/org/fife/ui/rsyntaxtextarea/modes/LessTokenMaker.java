package org.fife.ui.rsyntaxtextarea.modes;


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


}