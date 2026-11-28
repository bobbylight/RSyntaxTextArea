/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for the {@link RTextAreaBase} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaBaseTest {


	private static RTextArea createTextArea(String text, int tabSize) {
		RTextArea textArea = new RTextArea(text);
		textArea.setTabSize(tabSize);
		return textArea;
	}


	@Test
	void testConvertSpacesToTabs() {
		RTextArea textArea = createTextArea("    foo        bar  baz", 4);
		textArea.setCaretPosition(0);

		textArea.convertSpacesToTabs();

		Assertions.assertEquals("\tfoo\t\tbar  baz", textArea.getText());
		Assertions.assertEquals(0, textArea.getCaretPosition());
	}


	@Test
	void testConvertSpacesToTabs_honorsTabSize() {
		RTextArea textArea = createTextArea("  a    b", 2);

		textArea.convertSpacesToTabs();

		Assertions.assertEquals("\ta\t\tb", textArea.getText());
	}


	@Test
	void testConvertSpacesToTabs_caretStillInRange_isPreserved() {
		RTextArea textArea = createTextArea("abcdef    ", 4);
		textArea.setCaretPosition(3);

		textArea.convertSpacesToTabs();

		Assertions.assertEquals("abcdef\t", textArea.getText());
		Assertions.assertEquals(3, textArea.getCaretPosition());
	}


	@Test
	void testConvertSpacesToTabs_caretPastNewEnd_isClampedToEnd() {
		RTextArea textArea = createTextArea("ab        ", 4);
		textArea.setCaretPosition(textArea.getDocument().getLength());

		textArea.convertSpacesToTabs();

		Assertions.assertEquals("ab\t\t", textArea.getText());
		Assertions.assertEquals(4, textArea.getCaretPosition());
	}


	@Test
	void testConvertSpacesToTabs_emptyDocument() {
		RTextArea textArea = createTextArea("", 4);

		textArea.convertSpacesToTabs();

		Assertions.assertEquals("", textArea.getText());
		Assertions.assertEquals(0, textArea.getCaretPosition());
	}


	@Test
	void testConvertSpacesToTabs_noSpaces() {
		RTextArea textArea = createTextArea("abc", 4);
		textArea.setCaretPosition(3);

		textArea.convertSpacesToTabs();

		Assertions.assertEquals("abc", textArea.getText());
		Assertions.assertEquals(3, textArea.getCaretPosition());
	}


	@Test
	void testConvertTabsToSpaces() {
		RTextArea textArea = createTextArea("\tfoo\t\tbar", 4);
		textArea.setCaretPosition(1);

		textArea.convertTabsToSpaces();

		Assertions.assertEquals("    foo        bar", textArea.getText());
		Assertions.assertEquals(1, textArea.getCaretPosition());
	}


	@Test
	void testConvertTabsToSpaces_honorsTabSize() {
		RTextArea textArea = createTextArea("\ta", 2);

		textArea.convertTabsToSpaces();

		Assertions.assertEquals("  a", textArea.getText());
	}
}
