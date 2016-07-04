/*
 * 02/16/2012
 *
 * Copyright (C) 2013 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;


/**
 * Generates hyperlinks in a document.  If one of these is installed on an
 * <code>RSyntaxTextArea</code> it is queried when the mouse is moved and
 * hyperlinks are enabled.  If the user is not hovering over a "real" hyperlink
 * (e.g. "http://www.google.com"), the link generator is asked if a text region
 * at the mouse position should be considered a hyperlink.  If so, a result
 * object is returned, describing exactly what region of text is the link, and
 * where it goes to.<p>
 *
 * This interface is typically used by applications providing advanced support
 * for programming languages, such as IDEs.  For example, an implementation of
 * this class could identify the token under the mouse position as a "variable,"
 * and the hyperlink returned would select the variable's declaration in the
 * document.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface LinkGenerator {


	/**
	 * If a region of text under the mouse position should be considered a
	 * hyperlink, a result object is returned.  This object describes what
	 * region of text is the link, and what action to perform if the link is
	 * clicked.
	 *
	 * @param textArea The text component.
	 * @param offs The offset in the document under the mouse position.
	 * @return The link information, or <code>null</code> if no link is at the
	 *         specified offset.
	 */
	LinkGeneratorResult isLinkAtOffset(RSyntaxTextArea textArea, int offs);


}