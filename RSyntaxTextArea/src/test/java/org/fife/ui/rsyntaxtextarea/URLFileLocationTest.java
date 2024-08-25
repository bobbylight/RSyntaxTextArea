/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link URLFileLocation} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class URLFileLocationTest {


	@Test
	void testGetActualLastModified() {
		String url = "https://google.com";
		FileLocation loc = FileLocation.create(url);
		Assertions.assertInstanceOf(URLFileLocation.class, loc);
		Assertions.assertEquals(TextEditorPane.LAST_MODIFIED_UNKNOWN, loc.getActualLastModified());
	}


	@Test
	void testGetFileFullPath() {
		String url = "https://google.com";
		FileLocation loc = FileLocation.create(url);
		Assertions.assertInstanceOf(URLFileLocation.class, loc);
		Assertions.assertEquals(url, loc.getFileFullPath());
	}


	@Test
	void testGetFileName() {
		String url = "https://google.com";
		FileLocation loc = FileLocation.create(url);
		Assertions.assertInstanceOf(URLFileLocation.class, loc);
		Assertions.assertEquals("", loc.getFileName());
	}


	@Test
	void testIsLocal_https() {
		String url = "https://google.com";
		FileLocation loc = FileLocation.create(url);
		Assertions.assertInstanceOf(URLFileLocation.class, loc);
		Assertions.assertFalse(loc.isLocal());
	}


	@Test
	void testIsLocalAndExists() {
		String url = "https://google.com";
		FileLocation loc = FileLocation.create(url);
		Assertions.assertInstanceOf(URLFileLocation.class, loc);
		Assertions.assertFalse(loc.isLocalAndExists());
	}


}
