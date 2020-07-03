/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


import org.fife.ui.rsyntaxtextarea.templates.CodeTemplate;
import org.fife.ui.rsyntaxtextarea.templates.StaticCodeTemplate;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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


	@Test(expected = IllegalArgumentException.class)
	public void testAddTemplate_null() {
		CodeTemplateManager manager = new CodeTemplateManager();
		manager.addTemplate(null);
	}


	@Test
	public void testAddTemplate_happyPath() {
		CodeTemplateManager manager = new CodeTemplateManager();
		manager.addTemplate(new StaticCodeTemplate());
	}


	@Test
	public void testGetTemplate_noMatchingTemplate() {
		RSyntaxTextArea textArea = createTextArea();
		CodeTemplateManager manager = new CodeTemplateManager();
		manager.addTemplate(new StaticCodeTemplate("id", "foo", "bar"));
		Assert.assertNull(manager.getTemplate(textArea));
	}


	@Test
	public void testGetTemplates() {
		Assert.assertEquals(0, new CodeTemplateManager().getTemplates().length);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveTemplate_stringArg_null() {
		CodeTemplateManager manager = new CodeTemplateManager();
		manager.removeTemplate((String)null);
	}


	@Test
	public void testRemoveTemplate_stringArg_happyPath_found() {
		CodeTemplateManager manager = new CodeTemplateManager();
		manager.addTemplate(new StaticCodeTemplate("id", "foo", "bar"));
		Assert.assertNotNull(manager.removeTemplate("id"));
	}


	@Test
	public void testRemoveTemplate_stringArg_happyPath_notFound() {
		CodeTemplateManager manager = new CodeTemplateManager();
		manager.addTemplate(new StaticCodeTemplate("id", "foo", "bar"));
		Assert.assertNull(manager.removeTemplate("notFound"));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testRemoveTemplate_codeTemplateArg_null() {
		CodeTemplateManager manager = new CodeTemplateManager();
		manager.removeTemplate((CodeTemplate) null);
	}


	@Test
	public void testRemoveTemplate_codeTemplateArg_happyPath_found() {
		CodeTemplateManager manager = new CodeTemplateManager();
		CodeTemplate template = new StaticCodeTemplate("id", "foo", "bar");
		manager.addTemplate(template);
		Assert.assertTrue(manager.removeTemplate(template));
		Assert.assertEquals(0, manager.getTemplates().length);
	}


	@Test
	public void testRemoveTemplate_codeTemplateArg_happyPath_notFound() {
		CodeTemplateManager manager = new CodeTemplateManager();
		CodeTemplate template = new StaticCodeTemplate("id", "foo", "bar");
		manager.addTemplate(template);
		CodeTemplate template2 = new StaticCodeTemplate("id2", "foo", "bar");
		Assert.assertFalse(manager.removeTemplate(template2));
		Assert.assertEquals(1, manager.getTemplates().length);
	}


	@Test
	public void testReplaceTemplates() {

		CodeTemplateManager manager = new CodeTemplateManager();
		CodeTemplate template = new StaticCodeTemplate("id", "foo", "bar");
		manager.addTemplate(template);

		manager.replaceTemplates(new CodeTemplate[0]);
		Assert.assertEquals(0, manager.getTemplates().length);
	}


	@Test
	public void testSetTemplateDirectory_dirExists() throws IOException {

		CodeTemplateManager manager = new CodeTemplateManager();

		Path tempDir = Files.createTempDirectory("testDir");
		Assert.assertEquals(0, manager.setTemplateDirectory(tempDir.toFile()));

		CodeTemplate template = new StaticCodeTemplate("id", "foo", "bar");
		manager.addTemplate(template);

		Assert.assertTrue(manager.saveTemplates());

		// Set to same directory just to load the same template a second time
		Assert.assertEquals(2, manager.setTemplateDirectory(tempDir.toFile()));
	}


	@Test
	public void testSetTemplateDirectory_dirDoesNotExist() {

		CodeTemplateManager manager = new CodeTemplateManager();
		Assert.assertEquals(-1, manager.setTemplateDirectory(new File("doesNotExist")));
	}
}
