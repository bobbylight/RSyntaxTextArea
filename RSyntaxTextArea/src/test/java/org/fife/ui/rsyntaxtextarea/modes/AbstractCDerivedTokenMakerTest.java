/*
 * 06/05/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Utility classes for unit tests for <code>TokenMaker</code> implementations.
 *
 * @author Robert Futrell
 * @version 1.0
 */
abstract class AbstractCDerivedTokenMakerTest extends AbstractJFlexTokenMakerTest {


	/**
	 * Overridden to assert {@code true} for the default language index. If
	 * subclasses have additional sub-languages, they should override this method.
	 */
	@Test
	@Override
	protected void testCommon_getCurlyBracesDenoteCodeBlocks() {
		TokenMaker tm = createTokenMaker();
		Assertions.assertTrue(tm.getCurlyBracesDenoteCodeBlocks(0));
	}


	/**
	 * Overridden to verify that indentation of the next line is done after curly braces
	 * and open parens for the default language index.
	 */
	@Test
	@Override
	protected void testCommon_getShouldIndentNextLineAfter() {
		testCommonHelper_getShouldIndentNextLineAfterCurliesAndParensForLanguageIndex(0);
	}
}
