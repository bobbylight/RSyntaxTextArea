/*
 * 06/05/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenImpl;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.Segment;


/**
 * Utility classes for unit tests for <code>TokenMaker</code> implementations.
 *
 * @author Robert Futrell
 * @version 1.0
 */
abstract class AbstractCDerivedTokenMakerTest extends AbstractTokenMakerTest {


	/**
	 * Overridden to verify that indentation of the next line is done after curly braces
	 * and open parens.
	 */
	@Test
	@Override
	public void testCommon_getShouldIndentNextLineAfter() {
		TokenMaker tm = createTokenMaker();
		Token[] indentAfter = {
			new TokenImpl("{".toCharArray(), 0, 0, 0, TokenTypes.SEPARATOR, 0),
			new TokenImpl("(".toCharArray(), 0, 0, 0, TokenTypes.SEPARATOR, 0),
		};
		for (Token token : indentAfter) {
			Assertions.assertTrue(tm.getShouldIndentNextLineAfter(token));
		}
	}
}
