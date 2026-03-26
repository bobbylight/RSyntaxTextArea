/*
 * 02/09/2025
 *
 * IconRowEvent.java - Event for IconRowHeader.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */

package org.fife.ui.rtextarea;

import javax.swing.text.BadLocationException;
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

	protected boolean consumed;

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

	/**
	 * Returns the event has been consumed.
	 *
	 * @return {@code true} if consumed, {@code false} otherwise
	 */
	public boolean isConsumed() {
		return consumed;
	}

	/**
	 * Marks this event as consumed.
	 *
	 * <p>Once consumed, the event will no longer be dispatched to additional
	 * listeners, and default handling—such as toggling a bookmark on a
	 * left‑click—will be suppressed.</p>
	 */
	public void consume() {
		this.consumed = true;
	}


	/**
	 * Returns all gutter icons present on the clicked line.
	 *
	 * @return an array of icons on the clicked line. It is always non-null
	 */
	public GutterIconInfo[] getIconsAtLine() {
		if (line < 0) { // should never happen
			return new GutterIconInfo[0];
		}

		try {
			IconRowHeader iconRowHeader = (IconRowHeader) getSource();
			return iconRowHeader.getTrackingIcons(getLine());
		} catch (BadLocationException e) {
			return new GutterIconInfo[0];
		}
	}
}
