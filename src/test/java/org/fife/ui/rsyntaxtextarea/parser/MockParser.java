/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;


/**
 * A mock parser implementation for use in unit tests.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class MockParser extends AbstractParser {

	private DefaultParseResult result;


	public MockParser() {
		result = new DefaultParseResult(this);
	}


	@Override
	public ParseResult parse(RSyntaxDocument doc, String style) {
		return result;
	}


}