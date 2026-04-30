/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link IndentFormatter} class.
 *
 * @author RSyntaxTextArea Demo
 * @version 1.0
 */
class IndentFormatterTest {

	@Test
	void testDefaultConstructor() {
		IndentFormatter formatter = new IndentFormatter();
		Assertions.assertFalse(formatter.isUseTabs());
		Assertions.assertEquals(4, formatter.getTabSize());
		Assertions.assertEquals(4, formatter.getSpacesPerIndent());
	}


	@Test
	void testParameterizedConstructor() {
		IndentFormatter formatter = new IndentFormatter(true, 8, 2);
		Assertions.assertTrue(formatter.isUseTabs());
		Assertions.assertEquals(8, formatter.getTabSize());
		Assertions.assertEquals(2, formatter.getSpacesPerIndent());
	}


	@Test
	void testParameterizedConstructorWithNegativeValues() {
		IndentFormatter formatter = new IndentFormatter(false, -1, -5);
		Assertions.assertEquals(4, formatter.getTabSize());
		Assertions.assertEquals(4, formatter.getSpacesPerIndent());
	}


	@Test
	void testFormatZeroIndentLevel() {
		IndentFormatter formatter = new IndentFormatter();
		Assertions.assertEquals("", formatter.format(0));
	}


	@Test
	void testFormatNegativeIndentLevel() {
		IndentFormatter formatter = new IndentFormatter();
		Assertions.assertEquals("", formatter.format(-1));
	}


	@Test
	void testFormatWithSpaces() {
		IndentFormatter formatter = new IndentFormatter(false, 4, 4);
		Assertions.assertEquals("    ", formatter.format(1));
		Assertions.assertEquals("        ", formatter.format(2));
	}


	@Test
	void testFormatWithDifferentSpacesPerIndent() {
		IndentFormatter formatter = new IndentFormatter(false, 4, 2);
		Assertions.assertEquals("  ", formatter.format(1));
		Assertions.assertEquals("    ", formatter.format(2));
		Assertions.assertEquals("      ", formatter.format(3));
	}


	@Test
	void testFormatWithTabs() {
		IndentFormatter formatter = new IndentFormatter(true, 4, 4);
		Assertions.assertEquals("\t", formatter.format(1));
		Assertions.assertEquals("\t\t", formatter.format(2));
		Assertions.assertEquals("\t\t\t", formatter.format(3));
	}


	@Test
	void testFormatWithTabsAndCustomTabSize() {
		IndentFormatter formatter = new IndentFormatter(true, 8, 4);
		Assertions.assertEquals("\t", formatter.format(1));
		Assertions.assertEquals("\t\t", formatter.format(2));
	}

}
