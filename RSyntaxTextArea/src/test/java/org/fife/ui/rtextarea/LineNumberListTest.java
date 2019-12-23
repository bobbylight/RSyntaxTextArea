/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit tests for the {@link LineNumberList} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class LineNumberListTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testPaintComponent_noLineWrap_happyPath() {

		String code = "if (true) {\n" +
			"   println('Do work');\n" +
			"}";

		// Set properties that complicate line number rendering so we
		// exercise more code
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_CSHARP, code);

		LineNumberList list = new LineNumberList(textArea);

		list.paintComponent(createTestGraphics());
	}


	@Test
	public void testPaintComponent_lineWrap_happyPath() {

		String code = "if (true) {\n" +
			"   println('Do work');\n" +
			"}";

		// Set properties that complicate line number rendering so we
		// exercise more code
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_CSHARP, code);
		textArea.setLineWrap(true);

		LineNumberList list = new LineNumberList(textArea);

		list.paintComponent(createTestGraphics());
	}
}
