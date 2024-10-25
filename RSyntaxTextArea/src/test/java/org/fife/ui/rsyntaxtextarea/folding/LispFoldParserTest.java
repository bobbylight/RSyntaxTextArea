/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.folding;

import org.fife.ui.rsyntaxtextarea.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link LispFoldParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class LispFoldParserTest extends CurlyFoldParserTest {

	@Test
	void testIsLeftCurly() {
		LispFoldParser parser = new LispFoldParser();
		Token t = new TokenImpl("(".toCharArray(), 0, 0, 0, TokenTypes.SEPARATOR, 0);
		Assertions.assertTrue(parser.isLeftCurly(t));
	}

	@Test
	void testIsRightCurly() {
		LispFoldParser parser = new LispFoldParser();
		Token t = new TokenImpl(")".toCharArray(), 0, 0, 0, TokenTypes.SEPARATOR, 0);
		Assertions.assertTrue(parser.isRightCurly(t));
	}
}
