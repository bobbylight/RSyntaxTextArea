/*
 * 04/23/2014
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;


/**
 * Provides various ways to render a caret such as {@link ConfigurableCaret}..
 *
 * Currently supported renderings include:
 * 
 * <ol>
 *    <li>As a vertical line (like <code>DefaultCaret</code>)</li>
 *    <li>As a slightly thicker vertical line (like Eclipse)</li>
 *    <li>As an underline</li>
 *    <li>As a "block caret"</li>
 *    <li>As a rectangle around the current character</li>
 * </li>
 * 
 * @author Robert Futrell
 * @version 1.0
 */
public enum CaretStyle {

	VERTICAL_LINE_STYLE,

	UNDERLINE_STYLE,

	BLOCK_STYLE,

	BLOCK_BORDER_STYLE,

	THICK_VERTICAL_LINE_STYLE;

}