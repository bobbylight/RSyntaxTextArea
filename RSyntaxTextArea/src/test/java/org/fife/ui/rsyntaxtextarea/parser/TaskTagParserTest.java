/*
 * 10/03/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import java.util.List;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link TaskTagParser} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class TaskTagParserTest {


	@Test
	public void testConstructor() {
		TaskTagParser parser = new TaskTagParser();
		Assert.assertEquals("TODO|FIXME|HACK", parser.getTaskPattern());
	}


	@Test
	public void testGetTaskPattern() {

		TaskTagParser parser = new TaskTagParser();
		Assert.assertEquals("TODO|FIXME|HACK", parser.getTaskPattern());

		parser.setTaskPattern("Hello|World");
		Assert.assertEquals("Hello|World", parser.getTaskPattern());

		parser.setTaskPattern(null);
		Assert.assertNull(parser.getTaskPattern());

	}


	@Test
	public void testParse_happyPath() throws Exception {

		TaskTagParser parser = new TaskTagParser();

		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_C);
		doc.insertString(0, "/* TODO: Fix this */", null);

		ParseResult res = parser.parse(doc, doc.getSyntaxStyle());
		Assert.assertEquals(parser, res.getParser());
		Assert.assertEquals(0, res.getFirstLineParsed());
		Assert.assertEquals(0, res.getLastLineParsed());
		List<ParserNotice> notices = res.getNotices();
		Assert.assertEquals(1, notices.size());
		// Note that the parser does not understand EOL vs. MLC comments, so
		// it just returns everything from the start of the task to the end of
		// the line.
		Assert.assertEquals("TODO: Fix this */", notices.get(0).getToolTipText());

	}


	@Test
	public void testParse_nullTaskPattern() throws Exception {

		TaskTagParser parser = new TaskTagParser();
		parser.setTaskPattern(null);

		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_C);
		doc.insertString(0, "/* TODO: Fix this */ for", null);

		ParseResult res = parser.parse(doc, doc.getSyntaxStyle());
		Assert.assertEquals(parser, res.getParser());
		Assert.assertEquals(0, res.getFirstLineParsed());
		Assert.assertEquals(0, res.getLastLineParsed());
		List<ParserNotice> notices = res.getNotices();
		Assert.assertEquals(0, notices.size());

	}


	@Test
	public void testParse_noLanguage() throws Exception {

		TaskTagParser parser = new TaskTagParser();
		parser.setTaskPattern(null);

		RSyntaxDocument doc = new RSyntaxDocument(
				SyntaxConstants.SYNTAX_STYLE_NONE);
		doc.insertString(0, "/* TODO: Fix this */ for", null);

		ParseResult res = parser.parse(doc, doc.getSyntaxStyle());
		Assert.assertEquals(parser, res.getParser());
		Assert.assertEquals(0, res.getFirstLineParsed());
		Assert.assertEquals(0, res.getLastLineParsed());
		List<ParserNotice> notices = res.getNotices();
		Assert.assertEquals(0, notices.size());

		doc.setSyntaxStyle((String)null); // Not really valid, but whatever
		res = parser.parse(doc, doc.getSyntaxStyle());
		Assert.assertEquals(parser, res.getParser());
		Assert.assertEquals(0, res.getFirstLineParsed());
		Assert.assertEquals(0, res.getLastLineParsed());
		notices = res.getNotices();
		Assert.assertEquals(0, notices.size());

	}


	@Test
	public void testSetTaskPattern() {

		TaskTagParser parser = new TaskTagParser();
		Assert.assertEquals("TODO|FIXME|HACK", parser.getTaskPattern());

		parser.setTaskPattern("Hello|World");
		Assert.assertEquals("Hello|World", parser.getTaskPattern());

		parser.setTaskPattern(null);
		Assert.assertNull(parser.getTaskPattern());

		parser.setTaskPattern(""); // We convert empty string to null
		Assert.assertNull(parser.getTaskPattern());

	}


}