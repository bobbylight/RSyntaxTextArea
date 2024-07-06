/*
 * 01/30/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link FileLocation} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class FileLocationTest {


	@Test
	void testCreate_StringArg_FileName() {
		String url = "test.txt";
		FileLocation loc = FileLocation.create(url);
		Assertions.assertInstanceOf(FileFileLocation.class, loc);
		Assertions.assertTrue(loc.isLocal());
		Assertions.assertFalse(loc.isRemote());
	}


	@Test
	void testCreate_StringArg_FileUrl() {
		String url = File.separatorChar == '/' ?
				"file:///test.txt" : "file:///C:/test.txt";
		FileLocation loc = FileLocation.create(url);
		Assertions.assertInstanceOf(FileFileLocation.class, loc);
		Assertions.assertTrue(loc.isLocal());
		Assertions.assertFalse(loc.isRemote());
	}


	@Test
	void testCreate_StringArg_FtpUrl() {
		String url = "ftp://ftp.microsoft.com/deskapps/readme.txt";
		FileLocation loc = FileLocation.create(url);
		Assertions.assertInstanceOf(URLFileLocation.class, loc);
		Assertions.assertFalse(loc.isLocal());
		Assertions.assertTrue(loc.isRemote());
	}


	@Test
	void testCreate_StringArg_HttpUrl() {
		String url = "http://google.com";
		FileLocation loc = FileLocation.create(url);
		Assertions.assertInstanceOf(URLFileLocation.class, loc);
		Assertions.assertFalse(loc.isLocal());
		Assertions.assertTrue(loc.isRemote());
	}


	@Test
	void testCreate_StringArg_HttpsUrl() {
		String url = "https://google.com";
		FileLocation loc = FileLocation.create(url);
		Assertions.assertInstanceOf(URLFileLocation.class, loc);
		Assertions.assertFalse(loc.isLocal());
		Assertions.assertTrue(loc.isRemote());
	}


	@Test
	void testCreate_FileArg() {
		File file = new File(File.separatorChar == '/' ?
				"test.txt" : "C:/test.txt");
		FileLocation loc = FileLocation.create(file);
		Assertions.assertInstanceOf(FileFileLocation.class, loc);
		Assertions.assertTrue(loc.isLocal());
		Assertions.assertFalse(loc.isRemote());
	}


	@Test
	void testCreate_UrlArg_HttpsUrl() throws Exception {
		URL url = new URL("https://google.com");
		FileLocation loc = FileLocation.create(url);
		Assertions.assertInstanceOf(URLFileLocation.class, loc);
		Assertions.assertFalse(loc.isLocal());
		Assertions.assertTrue(loc.isRemote());
	}


	@Test
	void testCreate_UrlArg_FileUrl() throws Exception {
		URL url = new URL("file:///test.txt");
		FileLocation loc = FileLocation.create(url);
		Assertions.assertInstanceOf(FileFileLocation.class, loc);
		Assertions.assertTrue(loc.isLocal());
		Assertions.assertFalse(loc.isRemote());
	}


}
