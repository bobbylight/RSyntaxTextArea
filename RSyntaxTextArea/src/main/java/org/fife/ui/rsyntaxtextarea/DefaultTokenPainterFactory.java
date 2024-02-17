/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


/**
 * Standard implementation of a token painter factory.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see DefaultTokenPainter
 * @see VisibleWhitespaceTokenPainter
 */
public class DefaultTokenPainterFactory implements TokenPainterFactory {


	@Override
	public TokenPainter getTokenPainter(RSyntaxTextArea textArea) {
		return textArea.isWhitespaceVisible() ? new VisibleWhitespaceTokenPainter() :
			new DefaultTokenPainter();
	}


}
