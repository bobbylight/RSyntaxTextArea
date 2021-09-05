/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;


import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link IconGroup} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class IconGroupTest {


	@Test
	void testEquals_sameGroup() {

		IconGroup group = new IconGroup("foo", "/path");
		//noinspection SimplifiableJUnitAssertion,EqualsWithItself
		Assertions.assertTrue(group.equals(group));
	}


	@Test
	void testGetFileTypeIcon_noIconFound() {

		IconGroup group = new IconGroup("foo", "/path");
		Assertions.assertNull(group.getFileTypeIcon(SyntaxConstants.SYNTAX_STYLE_C));
	}


	@Test
	void testGetFileTypeIcon_noSlashSoNoIconReturned() {

		IconGroup group = new IconGroup("foo", "/path");
		Assertions.assertNull(group.getFileTypeIcon("invalidValue"));
	}


	@Test
	void testGetHasSeparateLargeIcons_2ArgConstructor() {

		IconGroup group = new IconGroup("foo", "/path");
		Assertions.assertFalse(group.hasSeparateLargeIcons());
	}


	@Test
	void testGetIcon_notFound() {

		IconGroup group = new IconGroup("foo", "/path");
		Assertions.assertNull(group.getIcon("foo"));
	}


	@Test
	void testGetLargeIcon_notFound() {

		IconGroup group = new IconGroup("foo", "/path");
		Assertions.assertNull(group.getLargeIcon("foo"));
	}

	@Test
	void testGetName() {

		IconGroup group = new IconGroup("foo", "/path");
		Assertions.assertEquals("foo", group.getName());
	}

	@Test
	void testHashCode() {

		IconGroup group = new IconGroup("foo", "/path");
		Assertions.assertTrue(group.hashCode() > 0);
	}
}
