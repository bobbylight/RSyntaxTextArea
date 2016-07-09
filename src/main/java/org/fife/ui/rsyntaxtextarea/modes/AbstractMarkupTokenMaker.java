/*
 * 10/03/2009
 *
 * AbstractMarkupTokenMaker.java - Base class for token makers for markup
 * languages.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.AbstractJFlexTokenMaker;


/**
 * Base class for token makers for markup languages.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractMarkupTokenMaker extends AbstractJFlexTokenMaker {


	/**
	 * Returns whether markup close tags should be completed.
	 *
	 * @return Whether closing markup tags are to be completed.
	 */
	public abstract boolean getCompleteCloseTags();


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[] { "<!--", "-->" };
	}


	/**
	 * Overridden to return <code>true</code>.
	 *
	 * @return <code>true</code> always.
	 */
	@Override
	public final boolean isMarkupLanguage() {
		return true;
	}


}