/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link CodeTemplateManager} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class CodeTemplateManagerTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testConstructor_zeroArg() {
		CodeTemplateManager manager = new CodeTemplateManager();
		Assert.assertEquals(0, manager.getTemplates().length);
	}


	@Test
	public void testGetTemplate_noMatchingTemplate() {

		RSyntaxTextArea textArea = createTextArea();
		CodeTemplateManager manager = new CodeTemplateManager();
		Assert.assertNull(manager.getTemplate(textArea));
	}
}
