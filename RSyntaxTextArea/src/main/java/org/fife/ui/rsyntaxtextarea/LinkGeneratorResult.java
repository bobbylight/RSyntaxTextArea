/*
 * 02/16/2012
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
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
	 * Executes the action associated with this object.  This method is called
	 * when the user clicks on the hyperlinked range of text in the editor.<p>
	 *
	 * If the result is a URL to open, a standard hyperlink event can be
	 * returned.  Alternatively, {@code null} can be returned and the action
	 * performed in this method itself.
	 *
	 * @return The hyperlink event to broadcast from the text area, or
	 *         <code>null</code> if the action's behavior occurs in this method
	 *         directly.  If a hyperlink event is returned, it should have type
	 *         {@code HyperlinkEvent.EventType#ACTIVATED} to denote the fact
	 *         that the link was clicked.
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
