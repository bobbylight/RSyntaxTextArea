/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.templates;


import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.text.BadLocationException;

/**
 * Unit tests for the {@code StaticCodeTemplate} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class StaticCodeTemplateTest {


	@Test
	void testClone() {
		StaticCodeTemplate template = new StaticCodeTemplate("id", "x", "y");
		Assertions.assertEquals(template, template.clone());
	}


	@Test
	void testCompareTo_nullIsNotAnError() {
		Assertions.assertEquals(-1, new StaticCodeTemplate().compareTo(null));
	}


	@Test
	void testEquals() {

		StaticCodeTemplate id1 = new StaticCodeTemplate("id", "a", "b");
		StaticCodeTemplate id2 = new StaticCodeTemplate("id", "x", "y");
		StaticCodeTemplate different = new StaticCodeTemplate("foo", "x", "y");

		Assertions.assertEquals(id1, id1);
		Assertions.assertEquals(id1, id2);
		Assertions.assertNotEquals(id1, null);
		Assertions.assertNotEquals(id1, different);
	}


	@Test
	void testGetSetAfterCaretText() {
		StaticCodeTemplate template = new StaticCodeTemplate();
		Assertions.assertNull(template.getAfterCaretText());
		template.setAfterCaretText("foo");
		Assertions.assertEquals("foo", template.getAfterCaretText());
	}


	@Test
	void testGetSetBeforeCaretText() {
		StaticCodeTemplate template = new StaticCodeTemplate();
		Assertions.assertNull(template.getBeforeCaretText());
		template.setBeforeCaretText("foo");
		Assertions.assertEquals("foo", template.getBeforeCaretText());
	}


	@Test
	void testGetSetId() {
		StaticCodeTemplate template = new StaticCodeTemplate();
		Assertions.assertNull(template.getID());
		template.setID("id");
		Assertions.assertEquals("id", template.getID());
	}


	@Test
	void testSetId_errorIfNull() {
		StaticCodeTemplate template = new StaticCodeTemplate();
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> template.setID(null));
	}


	@Test
	void testHashCode() {
		Assertions.assertNotEquals(0, new StaticCodeTemplate("id", "x", "y")
			.hashCode());
	}


	@Test
	void testInvoke_happyPath() throws BadLocationException {

		StaticCodeTemplate template = new StaticCodeTemplate("id", "before", "after");

		RSyntaxTextArea textArea = new RSyntaxTextArea("id");
		template.invoke(textArea);

		Assertions.assertEquals("beforeafter", textArea.getText());
		Assertions.assertEquals("before".length(), textArea.getCaretPosition());
	}


	@Test
	void testInvoke_happyPath_indentedWithSpaces() throws BadLocationException {

		StaticCodeTemplate template = new StaticCodeTemplate("id",
			"before 1\nbefore 2", "after 1\nafter 2");

		RSyntaxTextArea textArea = new RSyntaxTextArea("   id");
		template.invoke(textArea);

		Assertions.assertEquals("   before 1\n   before 2after 1\n   after 2",
			textArea.getText());
		Assertions.assertEquals("   before 1\n   before 2".length(),
			textArea.getCaretPosition());
	}


	@Test
	void testInvoke_happyPath_indentedWithTabs() throws BadLocationException {

		StaticCodeTemplate template = new StaticCodeTemplate("id",
			"before 1\nbefore 2", "after 1\nafter 2");

		RSyntaxTextArea textArea = new RSyntaxTextArea("\tid");
		template.invoke(textArea);

		Assertions.assertEquals("\tbefore 1\n\tbefore 2after 1\n\tafter 2",
			textArea.getText());
		Assertions.assertEquals("\tbefore 1\n\tbefore 2".length(),
			textArea.getCaretPosition());
	}


	@Test
	void testInvoke_happyPath_keyWithNewline() throws BadLocationException {

		StaticCodeTemplate template = new StaticCodeTemplate("id\nx\ny", "before", "after");

		RSyntaxTextArea textArea = new RSyntaxTextArea("id\nx\ny");
		template.invoke(textArea);

		Assertions.assertEquals("beforeafter", textArea.getText());
		Assertions.assertEquals("before".length(), textArea.getCaretPosition());
	}


	@Test
	void testToString() {
		String expected = "[StaticCodeTemplate: id=null, text=null|null]";
		Assertions.assertEquals(expected, new StaticCodeTemplate().toString());
	}
}
