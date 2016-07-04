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

import javax.swing.event.HyperlinkEvent;


/**
 * A result object from a {@link LinkGenerator}.  Implementations of this class
 * specify what action to execute when the user clicks on the "link" specified
 * by the <code>LinkGenerator</code>.  Typically, this will do something like
 * select another region of text in the document (the declaration of the
 * variable at the mouse position), or open another file in the parent
 * application, etc.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see SelectRegionLinkGeneratorResult
 */
public interface LinkGeneratorResult {


	/**
	 * Executes the action associated with this object.  If the result is a
	 * URL to open, a standard hyperlink event can be returned.  Alternatively,
	 * <code>null</code> can be returned and the action performed in this
	 * method itself.
	 *
	 * @return The hyperlink event to broadcast from the text area, or
	 *         <code>null</code> if the action's behavior occurs in this method
	 *         directly.
	 */
	HyperlinkEvent execute();


	/**
	 * Returns the starting offset of the link specified by the parent
	 * <code>LinkGenerator</code>.
	 *
	 * @return The offset.
	 */
	int getSourceOffset();


}