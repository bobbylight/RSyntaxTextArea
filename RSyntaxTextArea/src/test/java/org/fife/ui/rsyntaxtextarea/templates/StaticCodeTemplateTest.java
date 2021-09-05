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

/**
 * Unit tests for the {@code StaticCodeTemplate} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class StaticCodeTemplateTest {


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
	void testInvoke_happyPath() throws Exception {

		StaticCodeTemplate template = new StaticCodeTemplate("id", "before", "after");

		RSyntaxTextArea textArea = new RSyntaxTextArea("id");
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
