/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@code OS} enum.
 */
class OSTest {

	@Test
	void testGet_notNull() {
		Assertions.assertNotNull(OS.get());
	}

	@Test
	void testGet_matchesSystemProperty() {
		String osName = System.getProperty("os.name");
		OS expected = OS.OTHER;
		if (osName != null) {
			osName = osName.toLowerCase();
			if (osName.contains("windows")) {
				expected = OS.WINDOWS;
			}
			else if (osName.contains("mac os x")) {
				expected = OS.MAC_OS_X;
			}
			else if (osName.contains("linux")) {
				expected = OS.LINUX;
			}
		}
		Assertions.assertEquals(expected, OS.get());
	}

	@Test
	void testIsCaseSensitive_windows() {
		Assertions.assertFalse(OS.WINDOWS.isCaseSensitive());
	}

	@Test
	void testIsCaseSensitive_macOsX() {
		Assertions.assertFalse(OS.MAC_OS_X.isCaseSensitive());
	}

	@Test
	void testIsCaseSensitive_linux() {
		Assertions.assertTrue(OS.LINUX.isCaseSensitive());
	}

	@Test
	void testIsCaseSensitive_other() {
		Assertions.assertTrue(OS.OTHER.isCaseSensitive());
	}
}
