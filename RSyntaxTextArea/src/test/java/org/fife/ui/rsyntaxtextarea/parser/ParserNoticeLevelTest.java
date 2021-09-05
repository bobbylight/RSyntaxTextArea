/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import org.fife.ui.rsyntaxtextarea.parser.ParserNotice.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link Level} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ParserNoticeLevelTest {


	@Test
	void testGetNumericValue() {
		Assertions.assertEquals(0, Level.ERROR.getNumericValue());
		Assertions.assertEquals(1, Level.WARNING.getNumericValue());
		Assertions.assertEquals(2, Level.INFO.getNumericValue());
	}


	@Test
	void testIsEqualToOrWorseThan() {

		Assertions.assertTrue(Level.ERROR.isEqualToOrWorseThan(Level.ERROR));
		Assertions.assertTrue(Level.ERROR.isEqualToOrWorseThan(Level.WARNING));
		Assertions.assertTrue(Level.ERROR.isEqualToOrWorseThan(Level.INFO));

		Assertions.assertFalse(Level.WARNING.isEqualToOrWorseThan(Level.ERROR));
		Assertions.assertTrue(Level.WARNING.isEqualToOrWorseThan(Level.WARNING));
		Assertions.assertTrue(Level.WARNING.isEqualToOrWorseThan(Level.INFO));

		Assertions.assertFalse(Level.INFO.isEqualToOrWorseThan(Level.ERROR));
		Assertions.assertFalse(Level.INFO.isEqualToOrWorseThan(Level.WARNING));
		Assertions.assertTrue(Level.INFO.isEqualToOrWorseThan(Level.INFO));

	}


}
