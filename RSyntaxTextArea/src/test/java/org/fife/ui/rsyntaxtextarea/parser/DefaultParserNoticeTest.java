/*
 * 03/16/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.parser;

import java.awt.Color;

import org.fife.ui.rsyntaxtextarea.parser.ParserNotice.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link DefaultParserNotice} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DefaultParserNoticeTest {

	private MockParser parser;
	private DefaultParserNotice notice;


	@BeforeEach
	void setUp() {
		parser = new MockParser();
	}


	@Test
	void testDefaultParserNotice_3ArgConstructor() {

		notice = new DefaultParserNotice(parser, "Foo", 5);
		Assertions.assertEquals("Foo", notice.getMessage());
		Assertions.assertEquals(5, notice.getLine());
		Assertions.assertEquals(-1, notice.getOffset());
		Assertions.assertEquals(-1, notice.getLength());
		Assertions.assertFalse(notice.getKnowsOffsetAndLength());

	}


	@Test
	void testDefaultParserNoticeParserNotice_5ArgConstructor() {

		notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
		Assertions.assertEquals("Foo", notice.getMessage());
		Assertions.assertEquals(5, notice.getLine());
		Assertions.assertEquals(4, notice.getOffset());
		Assertions.assertEquals(7, notice.getLength());
		Assertions.assertTrue(notice.getKnowsOffsetAndLength());

	}


	@Test
	void testCompareTo_null() {
		notice = new DefaultParserNotice(parser, "Foo", 5);
		Assertions.assertTrue(notice.compareTo(null) < 0);
	}


	@Test
	void testCompareTo_differentLevels() {
		notice = new DefaultParserNotice(parser, "Foo", 5);
		notice.setLevel(Level.ERROR);
		DefaultParserNotice notice2 = new DefaultParserNotice(parser, "Foo", 5);
		notice2.setLevel(Level.INFO);
		Assertions.assertTrue(notice.compareTo(notice2) < 0);
		Assertions.assertTrue(notice2.compareTo(notice) > 0);
	}


	@Test
	void testCompareTo_differentLines() {

		notice = new DefaultParserNotice(parser, "Foo", 5);
		notice.setLevel(Level.INFO);

		DefaultParserNotice notice2 = new DefaultParserNotice(parser, "Foo", 6);
		notice2.setLevel(Level.INFO);

		Assertions.assertTrue(notice.compareTo(notice2) < 0);
		Assertions.assertTrue(notice2.compareTo(notice) > 0);

	}


	@Test
	void testCompareTo_differentMessages() {

		notice = new DefaultParserNotice(parser, "Foo", 5);
		notice.setLevel(Level.INFO);

		DefaultParserNotice notice2 = new DefaultParserNotice(parser, "FooBar", 5);
		notice2.setLevel(Level.INFO);

		Assertions.assertTrue(notice.compareTo(notice2) < 0);
		Assertions.assertTrue(notice2.compareTo(notice) > 0);

	}


	@Test
	void testCompareTo_equal() {

		notice = new DefaultParserNotice(parser, "Foo", 5);
		notice.setLevel(Level.INFO);
		Assertions.assertEquals(0, notice.compareTo(notice));

		DefaultParserNotice notice2 = new DefaultParserNotice(parser, "Foo", 5);
		notice2.setLevel(Level.INFO);

		Assertions.assertEquals(0, notice.compareTo(notice2));
		Assertions.assertEquals(0, notice2.compareTo(notice));

	}


	@Test
	void testContainsPosition() {

		int offs = 4;
		int len = 7;
		notice = new DefaultParserNotice(parser, "Foo", 5, offs, len);

		Assertions.assertFalse(notice.containsPosition(offs-1));
		for (int i=offs; i<offs+len; i++) {
			Assertions.assertTrue(notice.containsPosition(i));
		}
		Assertions.assertFalse(notice.containsPosition(offs+len));

	}


	@Test
	void testEquals_null() {
		notice = new DefaultParserNotice(parser, "Foo", 5);
		Assertions.assertFalse(notice.equals(null));
	}


	@Test
	void testEquals_differentObjectType() {
		notice = new DefaultParserNotice(parser, "Foo", 5);
		Assertions.assertFalse(notice.equals("Hello world"));
	}


	@Test
	void testEquals_unequalParserNotice() {

		notice = new DefaultParserNotice(parser, "Foo", 5);
		notice.setLevel(Level.INFO);

		DefaultParserNotice notice2 = new DefaultParserNotice(parser, "Bar", 6);
		notice2.setLevel(Level.INFO);

		Assertions.assertFalse(notice.equals(notice2));
		Assertions.assertFalse(notice2.equals(notice));

	}


	@Test
	void testEquals_equalParserNotice() {

		notice = new DefaultParserNotice(parser, "Foo", 5);
		notice.setLevel(Level.INFO);

		DefaultParserNotice notice2 = new DefaultParserNotice(parser, "Foo", 5);
		notice2.setLevel(Level.INFO);

		Assertions.assertTrue(notice.equals(notice));
		Assertions.assertTrue(notice.equals(notice2));
		Assertions.assertTrue(notice2.equals(notice));

	}


	@Test
	void testGetColor() {
		notice = new DefaultParserNotice(parser, "Foo", 5);
		notice.setColor(Color.yellow);
		Assertions.assertEquals(Color.yellow, notice.getColor());
		notice.setColor(Color.orange);
		Assertions.assertEquals(Color.orange, notice.getColor());
	}


	@Test
	void testGetKnowsOffsetAndLength() {

		notice = new DefaultParserNotice(parser, "Foo", 5);
		Assertions.assertFalse(notice.getKnowsOffsetAndLength());

		notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
		Assertions.assertTrue(notice.getKnowsOffsetAndLength());

	}


	@Test
	void testGetLength() {

		notice = new DefaultParserNotice(parser, "Foo", 5);
		Assertions.assertEquals(-1, notice.getLength());

		notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
		Assertions.assertEquals(7, notice.getLength());

	}


	@Test
	void testGetLevel() {
		notice = new DefaultParserNotice(parser, "Foo", 5);
		Assertions.assertEquals(Level.ERROR, notice.getLevel());
		notice.setLevel(Level.INFO);
		Assertions.assertEquals(Level.INFO, notice.getLevel());
		notice.setLevel(Level.WARNING);
		Assertions.assertEquals(Level.WARNING, notice.getLevel());
	}


	@Test
	void testGetLine() {
		notice = new DefaultParserNotice(parser, "Foo", 5);
		Assertions.assertEquals(5, notice.getLine());
	}


	@Test
	void testGetMessage() {
		notice = new DefaultParserNotice(parser, "Foo", 5);
		Assertions.assertEquals("Foo", notice.getMessage());
	}


	@Test
	void testGetOffset() {
		notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
		Assertions.assertEquals(4, notice.getOffset());
	}


	@Test
	void testGetParser() {
		notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
		Assertions.assertTrue(parser == notice.getParser());
	}


	@Test
	void testGetShowInEditor() {
		notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
		Assertions.assertTrue(notice.getShowInEditor());
		notice.setShowInEditor(false);
		Assertions.assertFalse(notice.getShowInEditor());
	}


	@Test
	void testGetToolTipText() {

		notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
		Assertions.assertEquals("Foo", notice.getToolTipText()); // Defaults to message

		final String tip = "Hello world";
		notice.setToolTipText(tip);
		Assertions.assertEquals(tip, notice.getToolTipText());

	}


	@Test
	void testHashCode() {

		int line = 5;
		DefaultParserNotice notice = new DefaultParserNotice(parser, "Foo", line);
		Assertions.assertEquals(line<<16 | notice.getOffset(), notice.hashCode());

		int offs = 3;
		notice = new DefaultParserNotice(parser, "Foo", line, offs, 4);
		Assertions.assertEquals(line<<16 | notice.getOffset(), notice.hashCode());

	}


	@Test
	void testSetColor() {
		notice = new DefaultParserNotice(parser, "Foo", 5);
		notice.setColor(Color.yellow);
		Assertions.assertEquals(Color.yellow, notice.getColor());
		notice.setColor(Color.orange);
		Assertions.assertEquals(Color.orange, notice.getColor());
	}


	@Test
	void testSetLevel() {
		notice = new DefaultParserNotice(parser, "Foo", 5);
		Assertions.assertEquals(Level.ERROR, notice.getLevel());
		notice.setLevel(Level.INFO);
		Assertions.assertEquals(Level.INFO, notice.getLevel());
		notice.setLevel(Level.WARNING);
		Assertions.assertEquals(Level.WARNING, notice.getLevel());
	}


	@Test
	void testSetLevel_null() {
		notice = new DefaultParserNotice(parser, "Foo", 5);
		notice.setLevel(Level.INFO);
		Assertions.assertEquals(Level.INFO, notice.getLevel());
		notice.setLevel(null);
		Assertions.assertEquals(Level.ERROR, notice.getLevel());
	}


	@Test
	void testSetShowInEditor() {
		notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
		Assertions.assertTrue(notice.getShowInEditor());
		notice.setShowInEditor(false);
		Assertions.assertFalse(notice.getShowInEditor());
	}


	@Test
	void testSetToolTipText() {

		notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
		Assertions.assertEquals("Foo", notice.getToolTipText()); // Defaults to message

		final String tip = "Hello world";
		notice.setToolTipText(tip);
		Assertions.assertEquals(tip, notice.getToolTipText());

	}


	@Test
	void testToString() {
		notice = new DefaultParserNotice(parser, "Foo", 5, 4, 7);
		Assertions.assertEquals("Line 5: Foo", notice.toString());
	}

}
