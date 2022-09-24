/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;


/**
 * The strategy to use when rendering expanded folds in the gutter.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public enum ExpandedFoldRenderStrategy {

	/**
	 * Always render expanded folds.
	 */
	ALWAYS,

	/**
	 * Only render expanded folds when the mouse is hovered over the gutter.
	 */
	ON_HOVER
}
