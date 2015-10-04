/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import org.fife.ui.rsyntaxtextarea.parser.ParserNotice.Level;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link Level} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class ParserNoticeLevelTest {


	@Test
	public void testGetNumericValue() {
		Assert.assertEquals(0, Level.ERROR.getNumericValue());
		Assert.assertEquals(1, Level.WARNING.getNumericValue());
		Assert.assertEquals(2, Level.INFO.getNumericValue());
	}


	@Test
	public void testIsEqualToOrWorseThan() {

		Assert.assertTrue(Level.ERROR.isEqualToOrWorseThan(Level.ERROR));
		Assert.assertTrue(Level.ERROR.isEqualToOrWorseThan(Level.WARNING));
		Assert.assertTrue(Level.ERROR.isEqualToOrWorseThan(Level.INFO));

		Assert.assertFalse(Level.WARNING.isEqualToOrWorseThan(Level.ERROR));
		Assert.assertTrue(Level.WARNING.isEqualToOrWorseThan(Level.WARNING));
		Assert.assertTrue(Level.WARNING.isEqualToOrWorseThan(Level.INFO));

		Assert.assertFalse(Level.INFO.isEqualToOrWorseThan(Level.ERROR));
		Assert.assertFalse(Level.INFO.isEqualToOrWorseThan(Level.WARNING));
		Assert.assertTrue(Level.INFO.isEqualToOrWorseThan(Level.INFO));

	}


}