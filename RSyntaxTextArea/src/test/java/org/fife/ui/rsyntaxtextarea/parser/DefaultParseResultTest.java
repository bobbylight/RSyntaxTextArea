/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link DefaultParseResultTest} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DefaultParseResultTest {


	@Test
	void testConstructor() {
		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assertions.assertEquals(parser, res.getParser());
	}


	@Test
	void testAddNotice() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assertions.assertEquals(0, res.getNotices().size());

		DefaultParserNotice notice = new DefaultParserNotice(parser, "message", 7);
		res.addNotice(notice);
		Assertions.assertEquals(1, res.getNotices().size());

		notice = new DefaultParserNotice(parser, "message 2", 42);
		res.addNotice(notice);
		Assertions.assertEquals(2, res.getNotices().size());

	}


	@Test
	void testClearNotices() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assertions.assertEquals(0, res.getNotices().size());

		DefaultParserNotice notice = new DefaultParserNotice(parser, "message", 7);
		res.addNotice(notice);
		Assertions.assertEquals(1, res.getNotices().size());

		res.clearNotices();
		Assertions.assertEquals(0, res.getNotices().size());

	}


	@Test
	void testGetError() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assertions.assertNull(res.getError());

		Exception e = new Exception("Test exception");
		res.setError(e);
		Assertions.assertEquals(e, res.getError());

	}


	@Test
	void testGetFirstLineParsed() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assertions.assertEquals(0, res.getFirstLineParsed());

		res.setParsedLines(7, 42);
		Assertions.assertEquals(7, res.getFirstLineParsed());

	}


	@Test
	void testGetLastLineParsed() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assertions.assertEquals(0, res.getFirstLineParsed());

		res.setParsedLines(7, 42);
		Assertions.assertEquals(42, res.getLastLineParsed());

	}


	@Test
	void testGetNotices() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assertions.assertEquals(0, res.getNotices().size());

		DefaultParserNotice notice = new DefaultParserNotice(parser, "message", 7);
		res.addNotice(notice);
		List<ParserNotice> notices = res.getNotices();
		Assertions.assertEquals(1, notices.size());
		Assertions.assertEquals(notice, notices.get(0));

	}


	@Test
	void testGetParser() {
		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assertions.assertEquals(parser, res.getParser());
	}


	@Test
	void testGetParseTime() {
		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assertions.assertEquals(0, res.getParseTime());
		res.setParseTime(5000);
		Assertions.assertEquals(5000, res.getParseTime());
	}


	@Test
	void testSetError() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assertions.assertNull(res.getError());

		Exception e = new Exception("Test exception");
		res.setError(e);
		Assertions.assertEquals(e, res.getError());

	}


	@Test
	void testSetParsedLines() {

		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assertions.assertEquals(0, res.getFirstLineParsed());
		Assertions.assertEquals(0, res.getLastLineParsed());

		res.setParsedLines(7, 42);
		Assertions.assertEquals(7, res.getFirstLineParsed());
		Assertions.assertEquals(42, res.getLastLineParsed());

	}


	@Test
	void testSetParseTime() {
		MockParser parser = new MockParser();
		DefaultParseResult res = new DefaultParseResult(parser);
		Assertions.assertEquals(0, res.getParseTime());
		res.setParseTime(5000);
		Assertions.assertEquals(5000, res.getParseTime());
	}


}
