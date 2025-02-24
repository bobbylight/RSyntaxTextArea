/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.TestUtil;
import org.fife.ui.rsyntaxtextarea.templates.CodeTemplate;
import org.fife.ui.rsyntaxtextarea.templates.StaticCodeTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
class CodeTemplateManagerTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testConstructor_zeroArg() {
		CodeTemplateManager manager = new CodeTemplateManager();
		Assertions.assertEquals(0, manager.getTemplates().length);
	}


	@Test
	void testAddTemplate_null() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			CodeTemplateManager manager = new CodeTemplateManager();
			manager.addTemplate(null);
		});
	}


	@Test
	void testAddTemplate_happyPath() {
		CodeTemplateManager manager = new CodeTemplateManager();
		manager.addTemplate(new StaticCodeTemplate());
	}


	@Test
	void testGetTemplate_noMatchingTemplate() {
		RSyntaxTextArea textArea = createTextArea();
		CodeTemplateManager manager = new CodeTemplateManager();
		manager.addTemplate(new StaticCodeTemplate("id", "foo", "bar"));
		Assertions.assertNull(manager.getTemplate(textArea));
	}


	@Test
	void testGetTemplates() {
		Assertions.assertEquals(0, new CodeTemplateManager().getTemplates().length);
	}


	@Test
	void testRemoveTemplate_stringArg_null() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			CodeTemplateManager manager = new CodeTemplateManager();
			manager.removeTemplate((String)null);
		});
	}


	@Test
	void testRemoveTemplate_stringArg_happyPath_found() {
		CodeTemplateManager manager = new CodeTemplateManager();
		manager.addTemplate(new StaticCodeTemplate("id", "foo", "bar"));
		Assertions.assertNotNull(manager.removeTemplate("id"));
	}


	@Test
	void testRemoveTemplate_stringArg_happyPath_notFound() {
		CodeTemplateManager manager = new CodeTemplateManager();
		manager.addTemplate(new StaticCodeTemplate("id", "foo", "bar"));
		Assertions.assertNull(manager.removeTemplate("notFound"));
	}


	@Test
	void testRemoveTemplate_codeTemplateArg_null() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			CodeTemplateManager manager = new CodeTemplateManager();
			manager.removeTemplate((CodeTemplate) null);
		});
	}


	@Test
	void testRemoveTemplate_codeTemplateArg_happyPath_found() {
		CodeTemplateManager manager = new CodeTemplateManager();
		CodeTemplate template = new StaticCodeTemplate("id", "foo", "bar");
		manager.addTemplate(template);
		Assertions.assertEquals(1, manager.getTemplates().length);
		Assertions.assertTrue(manager.removeTemplate(template));
		Assertions.assertEquals(0, manager.getTemplates().length);
	}


	@Test
	void testRemoveTemplate_codeTemplateArg_happyPath_notFound() {
		CodeTemplateManager manager = new CodeTemplateManager();
		CodeTemplate template = new StaticCodeTemplate("id", "foo", "bar");
		manager.addTemplate(template);
		CodeTemplate template2 = new StaticCodeTemplate("id2", "foo", "bar");
		Assertions.assertFalse(manager.removeTemplate(template2));
		Assertions.assertEquals(1, manager.getTemplates().length);
	}


	@Test
	void testReplaceTemplates() {

		CodeTemplateManager manager = new CodeTemplateManager();
		CodeTemplate template = new StaticCodeTemplate("id", "foo", "bar");
		manager.addTemplate(template);

		manager.replaceTemplates(new CodeTemplate[0]);
		Assertions.assertEquals(0, manager.getTemplates().length);
	}


	@Test
	void testSaveTemplates_nullDirectory() {
		CodeTemplateManager manager = new CodeTemplateManager();
		Assertions.assertFalse(manager.saveTemplates());
	}


	@Test
	void testSaveTemplates_directoryDoesNotExist() throws IOException {
		CodeTemplateManager manager = new CodeTemplateManager();
		// Because we ensure the directory exists when setting it, this
		// can only happen if the directory is removed out form under us.
		Path tempDir = Files.createTempDirectory("testDir");
		manager.setTemplateDirectory(tempDir.toFile());
		Files.delete(tempDir);
		Assertions.assertFalse(manager.saveTemplates());
	}


	@Test
	void testSaveTemplates_deletesOldFiles() throws IOException {

		CodeTemplateManager manager = new CodeTemplateManager();

		File tempDir = Files.createTempDirectory("testDir").toFile();
		manager.setTemplateDirectory(tempDir);

		// Directory starts out with a file in it
		TestUtil.createFile(".xml", "<test/>", tempDir);

		manager.saveTemplates();
		Assertions.assertEquals(0, tempDir.listFiles().length);
	}


	@Test
	void testSetTemplateDirectory_dirExists() throws IOException {

		CodeTemplateManager manager = new CodeTemplateManager();

		Path tempDir = Files.createTempDirectory("testDir");
		Assertions.assertEquals(0, manager.setTemplateDirectory(tempDir.toFile()));

		CodeTemplate template = new StaticCodeTemplate("id", "foo", "bar");
		manager.addTemplate(template);

		Assertions.assertTrue(manager.saveTemplates());

		// Set to same directory just to load the same template a second time
		Assertions.assertEquals(2, manager.setTemplateDirectory(tempDir.toFile()));
	}


	@Test
	void testSetTemplateDirectory_dirExists_xmlFileThatIsNotACodeTemplate() throws IOException {

		CodeTemplateManager manager = new CodeTemplateManager();
		File file = TestUtil.createFile(".xml", "<not-a-macro/>");

		// The non-template XML file is ignored.
		Assertions.assertEquals(0, manager.setTemplateDirectory(file.getParentFile()));
	}


	@Test
	void testSetTemplateDirectory_dirDoesNotExist() {

		CodeTemplateManager manager = new CodeTemplateManager();
		Assertions.assertEquals(-1, manager.setTemplateDirectory(new File("doesNotExist")));
	}
}
