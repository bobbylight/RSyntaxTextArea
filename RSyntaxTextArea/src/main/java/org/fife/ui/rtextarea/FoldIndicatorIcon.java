/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import javax.swing.*;


/**
 * A base class for icons in a {@code FoldIndicator}.
 *
 * @author  Robert Futrell
 * @version 1.0
 */
public abstract class FoldIndicatorIcon implements Icon {

	private final boolean collapsed;
	private boolean armed;


	/**
	 * Constructor.
	 *
	 * @param collapsed Whether this icon is for a collapsed fold.
	 */
	protected FoldIndicatorIcon(boolean collapsed) {
		this.collapsed = collapsed;
	}


	/**
	 * Whether this fold is armed.
	 *
	 * @return Whether this fold is armed.
	 * @see #setArmed(boolean)
	 */
	protected boolean isArmed() {
		return armed;
	}


	/**
	 * Returns whether this fold is collapsed.
	 *
	 * @return Whether this fold is collapsed.
	 */
	protected boolean isCollapsed() {
		return collapsed;
	}


	/**
	 * Toggles whether this fold is armed.
	 *
	 * @param armed Whether this fold is armed.
	 * @see #isArmed()
	 */
	protected void setArmed(boolean armed) {
		this.armed = armed;
	}
}
