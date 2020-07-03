/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;


import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
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
	public void testGetFileTypeIcon_noIconFound() {

		IconGroup group = new IconGroup("foo", "/path");
		Assert.assertNull(group.getFileTypeIcon(SyntaxConstants.SYNTAX_STYLE_C));
	}


	@Test
	public void testGetFileTypeIcon_noSlashSoNoIconReturned() {

		IconGroup group = new IconGroup("foo", "/path");
		Assert.assertNull(group.getFileTypeIcon("invalidValue"));
	}


	@Test
	public void testGetHasSeparateLargeIcons_2ArgConstructor() {

		IconGroup group = new IconGroup("foo", "/path");
		Assert.assertFalse(group.hasSeparateLargeIcons());
	}


	@Test
	public void testGetIcon_notFound() {

		IconGroup group = new IconGroup("foo", "/path");
		Assert.assertNull(group.getIcon("foo"));
	}


	@Test
	public void testGetLargeIcon_notFound() {

		IconGroup group = new IconGroup("foo", "/path");
		Assert.assertNull(group.getLargeIcon("foo"));
	}

	@Test
	public void testGetName() {

		IconGroup group = new IconGroup("foo", "/path");
		Assert.assertEquals("foo", group.getName());
	}

	@Test
	public void testHashCode() {

		IconGroup group = new IconGroup("foo", "/path");
		Assert.assertTrue(group.hashCode() > 0);
	}
}
