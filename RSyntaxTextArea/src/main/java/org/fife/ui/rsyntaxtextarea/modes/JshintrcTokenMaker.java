/*
 * 03/15/2015
 *
 * JshintrcTokenMaker.java - Scanner for .jshintrc files.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;


/**
 * Scanner for .jshintrc files.  This is equivalent to JSON with C-style
 * end-of-line comments.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class JshintrcTokenMaker extends JsonTokenMaker {


	/**
	 * Constructor; overridden to enable highlighting of EOL comments.
	 */
	public JshintrcTokenMaker() {
		setHighlightEolComments(true);
	}


}
