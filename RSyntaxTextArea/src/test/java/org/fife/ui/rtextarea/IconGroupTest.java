/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;


import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link IconGroup} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class IconGroupTest {


	@Test
	public void testEquals_sameGroup() {

		IconGroup group = new IconGroup("foo", "/path");
		//noinspection SimplifiableJUnitAssertion,EqualsWithItself
		Assert.assertTrue(group.equals(group));
	}


	@Test
	public void testGetHasSeparateLargeIcons_2ArgConstructor() {

		IconGroup group = new IconGroup("foo", "/path");
		Assert.assertFalse(group.hasSeparateLargeIcons());
	}


	@Test
	public void testGetName() {

		IconGroup group = new IconGroup("foo", "/path");
		Assert.assertEquals("foo", group.getName());
	}
}
