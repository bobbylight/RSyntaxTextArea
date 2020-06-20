/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.templates;


import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for the {@code StaticCodeTemplate} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class StaticCodeTemplateTest {


	@Test
	public void testGetSetAfterCaretText() {
		StaticCodeTemplate template = new StaticCodeTemplate();
		Assert.assertNull(template.getAfterCaretText());
		template.setAfterCaretText("foo");
		Assert.assertEquals("foo", template.getAfterCaretText());
	}


	@Test
	public void testGetSetBeforeCaretText() {
		StaticCodeTemplate template = new StaticCodeTemplate();
		Assert.assertNull(template.getBeforeCaretText());
		template.setBeforeCaretText("foo");
		Assert.assertEquals("foo", template.getBeforeCaretText());
	}


	@Test
	public void testInvoke_happyPath() throws Exception {

		StaticCodeTemplate template = new StaticCodeTemplate("id", "before", "after");

		RSyntaxTextArea textArea = new RSyntaxTextArea("id");
		template.invoke(textArea);

		Assert.assertEquals("beforeafter", textArea.getText());
		Assert.assertEquals("before".length(), textArea.getCaretPosition());
	}


	@Test
	public void testToString() {
		String expected = "[StaticCodeTemplate: id=null, text=null|null]";
		Assert.assertEquals(expected, new StaticCodeTemplate().toString());
	}
}
