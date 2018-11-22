/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import javax.swing.event.HyperlinkEvent;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;


/**
 * A mock implementation of {@link ExtendedHyperlinkListener} for unit
 * testing.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see ExtendedHyperlinkListener
 */
class MockExtendedHyperlinkListener implements ExtendedHyperlinkListener {

	@Override
	public void linkClicked(RSyntaxTextArea textArea, HyperlinkEvent e) {
		// Do nothing
	}


}