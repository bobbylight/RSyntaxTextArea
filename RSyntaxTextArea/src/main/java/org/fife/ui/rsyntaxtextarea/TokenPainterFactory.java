/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


/**
 * Returns the {@link TokenPainter} to use for a text area.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface TokenPainterFactory {


	/**
	 * Returns the text area to use.
	 *
	 * @param textArea The text area.
	 * @return The token painter.
	 */
	TokenPainter getTokenPainter(RSyntaxTextArea textArea);


}
