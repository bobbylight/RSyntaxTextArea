/*
 * 01/30/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.io.File;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link FileLocation} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class FileLocationTest {


	@Test
	public void testCreate_StringArg_FileName() throws Exception {
		String url = "test.txt";
		FileLocation loc = FileLocation.create(url);
		Assert.assertTrue(loc instanceof FileFileLocation);
		Assert.assertTrue(loc.isLocal());
		Assert.assertFalse(loc.isRemote());
	}


	@Test
	public void testCreate_StringArg_FileUrl() throws Exception {
		String url = File.separatorChar == '/' ?
				"file:///test.txt" : "file:///C:/test.txt";
		FileLocation loc = FileLocation.create(url);
		Assert.assertTrue(loc instanceof FileFileLocation);
		Assert.assertTrue(loc.isLocal());
		Assert.assertFalse(loc.isRemote());
	}


	@Test
	public void testCreate_StringArg_FtpUrl() throws Exception {
		String url = "ftp://ftp.microsoft.com/deskapps/readme.txt";
		FileLocation loc = FileLocation.create(url);
		Assert.assertTrue(loc instanceof URLFileLocation);
		Assert.assertFalse(loc.isLocal());
		Assert.assertTrue(loc.isRemote());
	}


	@Test
	public void testCreate_StringArg_HttpUrl() throws Exception {
		String url = "http://google.com";
		FileLocation loc = FileLocation.create(url);
		Assert.assertTrue(loc instanceof URLFileLocation);
		Assert.assertFalse(loc.isLocal());
		Assert.assertTrue(loc.isRemote());
	}


	@Test
	public void testCreate_StringArg_HttpsUrl() throws Exception {
		String url = "https://google.com";
		FileLocation loc = FileLocation.create(url);
		Assert.assertTrue(loc instanceof URLFileLocation);
		Assert.assertFalse(loc.isLocal());
		Assert.assertTrue(loc.isRemote());
	}


	@Test
	public void testCreate_FileArg() throws Exception {
		File file = new File(File.separatorChar == '/' ?
				"test.txt" : "C:/test.txt");
		FileLocation loc = FileLocation.create(file);
		Assert.assertTrue(loc instanceof FileFileLocation);
		Assert.assertTrue(loc.isLocal());
		Assert.assertFalse(loc.isRemote());
	}


	@Test
	public void testCreate_UrlArg_HttpsUrl() throws Exception {
		URL url = new URL("https://google.com");
		FileLocation loc = FileLocation.create(url);
		Assert.assertTrue(loc instanceof URLFileLocation);
		Assert.assertFalse(loc.isLocal());
		Assert.assertTrue(loc.isRemote());
	}


	@Test
	public void testCreate_UrlArg_FileUrl() throws Exception {
		URL url = new URL("file:///test.txt");
		FileLocation loc = FileLocation.create(url);
		Assert.assertTrue(loc instanceof FileFileLocation);
		Assert.assertTrue(loc.isLocal());
		Assert.assertFalse(loc.isRemote());
	}


}