/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for the {@code RSyntaxTextAreaEditorKit} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RSyntaxTextAreaEditorKitTest {

	@Test
	void testCreateDefaultDocument() {
		RSyntaxTextAreaEditorKit kit = new RSyntaxTextAreaEditorKit();
		Assertions.assertInstanceOf(RSyntaxDocument.class, kit.createDefaultDocument());
	}

	@Test
	void testGetString_validString() {
		Assertions.assertNotNull(RSyntaxTextAreaEditorKit.getString("ContextMenu.Folding"));
	}
}
