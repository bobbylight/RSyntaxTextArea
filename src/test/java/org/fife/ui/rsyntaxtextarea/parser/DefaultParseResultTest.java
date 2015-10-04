/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link DefaultParseResultTest} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DefaultParseResultTest {


	@Test
	public void testConstructor() {
		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assert.assertEquals(parser, res.getParser());
	}


	@Test
	public void testAddNotice() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assert.assertEquals(0, res.getNotices().size());

		DefaultParserNotice notice = new DefaultParserNotice(parser, "message", 7);
		res.addNotice(notice);
		Assert.assertEquals(1, res.getNotices().size());

		notice = new DefaultParserNotice(parser, "message 2", 42);
		res.addNotice(notice);
		Assert.assertEquals(2, res.getNotices().size());
		
	}


	@Test
	public void testClearNotices() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assert.assertEquals(0, res.getNotices().size());

		DefaultParserNotice notice = new DefaultParserNotice(parser, "message", 7);
		res.addNotice(notice);
		Assert.assertEquals(1, res.getNotices().size());

		res.clearNotices();
		Assert.assertEquals(0, res.getNotices().size());
		
	}


	@Test
	public void testGetError() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assert.assertNull(res.getError());

		Exception e = new Exception("Test exception");
		res.setError(e);
		Assert.assertEquals(e, res.getError());

	}


	@Test
	public void testGetFirstLineParsed() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assert.assertEquals(0, res.getFirstLineParsed());

		res.setParsedLines(7, 42);
		Assert.assertEquals(7, res.getFirstLineParsed());

	}


	@Test
	public void testGetLastLineParsed() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assert.assertEquals(0, res.getFirstLineParsed());

		res.setParsedLines(7, 42);
		Assert.assertEquals(42, res.getLastLineParsed());

	}


	@Test
	public void testGetNotices() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assert.assertEquals(0, res.getNotices().size());

		DefaultParserNotice notice = new DefaultParserNotice(parser, "message", 7);
		res.addNotice(notice);
		List<ParserNotice> notices = res.getNotices();
		Assert.assertEquals(1, notices.size());
		Assert.assertEquals(notice, notices.get(0));

	}


	@Test
	public void testGetParser() {
		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assert.assertEquals(parser, res.getParser());
	}


	@Test
	public void testGetParseTime() {
		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assert.assertEquals(0, res.getParseTime());
		res.setParseTime(5000);
		Assert.assertEquals(5000, res.getParseTime());
	}


	@Test
	public void testSetError() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assert.assertNull(res.getError());

		Exception e = new Exception("Test exception");
		res.setError(e);
		Assert.assertEquals(e, res.getError());

	}


	@Test
	public void testSetParsedLines() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assert.assertEquals(0, res.getFirstLineParsed());
		Assert.assertEquals(0, res.getLastLineParsed());

		res.setParsedLines(7, 42);
		Assert.assertEquals(7, res.getFirstLineParsed());
		Assert.assertEquals(42, res.getLastLineParsed());

	}


	@Test
	public void testSetParseTime() {
		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assert.assertEquals(0, res.getParseTime());
		res.setParseTime(5000);
		Assert.assertEquals(5000, res.getParseTime());
	}


}