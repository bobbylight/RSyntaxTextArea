/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;


/**
 * Different rendering styles for the fold indicator inside a {@code Gutter}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public enum FoldIndicatorStyle {

	/**
	 * Fold icons are rendered as +/- symbols in boxes. When the user hovers
	 * over an expanded fold's icon, a line is drawn to indicate the range
	 * of lines in the editor that belong to that fold's region.
	 */
	CLASSIC,

	/**
	 * Fold icons are rendered as a chevron indicating the fold's
	 * collapsed or expanded state. There is no visual indication of
	 * a fold's line range.
	 */
	MODERN
}
