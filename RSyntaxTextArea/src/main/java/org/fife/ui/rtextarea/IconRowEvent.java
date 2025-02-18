/*
 * 02/09/2025
 *
 * IconRowEvent.java - Event for IconRowHeader.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */

package org.fife.ui.rtextarea;

import java.util.EventObject;

/**
 * Defines an event pertaining to changes in a {@link IconRowHeader}.
 *
 * @author roger1337
 * @version 3.5.4
 * @see IconRowHeader
 */
public class IconRowEvent extends EventObject {


	/**
	 * Information about the icon associated with the event.
	 */
	protected GutterIconInfo iconInfo;

	/**
	 * The line at which the event took place.
	 */
	protected int line;

	public IconRowEvent(Object source, GutterIconInfo iconInfo, int line) {
		super(source);
		this.iconInfo = iconInfo;
		this.line = line;
	}

	/**
	 * Returns the icon associated with the event.
	 *
	 * @return The icon associated with the event.
	 */
	public GutterIconInfo getIconInfo() {
		return iconInfo;
	}

	/**
	 * Returns the line associated with the event.
	 *
	 * @return The line associated with the event.
	 */
	public int getLine() {
		return line;
	}
}
