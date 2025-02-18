/*
 * 02/09/2025
 *
 * IconRowListener.java - Interface for an object that listens for changes in IconRowHeader.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */

package org.fife.ui.rtextarea;

import java.util.EventListener;


/**
 * Defines an interface for an object that listens to changes in a {@link IconRowHeader}.
 *
 * @author roger1337
 * @version 3.5.4
 * @see IconRowHeader
 */
public interface IconRowListener extends EventListener {

	/**
	 * Method that is called when a bookmark is added.
	 *
	 * @param e an IconRowEvent describing the changes to the IconRowHeader
	 * @see IconRowHeader#toggleBookmark(int)
	 */
	void bookmarkAdded(IconRowEvent e);

	/**
	 * Method that is called when a bookmark is removed.
	 *
	 * @param e an IconRowEvent describing the changes to the IconRowHeader
	 * @see IconRowHeader#toggleBookmark(int)
	 */
	void bookmarkRemoved(IconRowEvent e);

}
