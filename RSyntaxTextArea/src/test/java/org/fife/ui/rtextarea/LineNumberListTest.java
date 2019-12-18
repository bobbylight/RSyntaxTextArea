/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Unit tests for the {@link LineNumberList} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class LineNumberListTest {


	@Test
	public void testPaintComponent_noLineWrap_happyPath() {

		String code = "if (true) {\n" +
			"   println('Do work');\n" +
			"}";

		// Set properties that complicate line number rendering so we
		// exercise more code
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSHARP);
		textArea.setCodeFoldingEnabled(true);
		textArea.setBounds(0, 0, 80, 80);

		LineNumberList list = new LineNumberList(textArea);

		Graphics g = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB)
			.getGraphics();
		g.setClip(0, 0, 80, 80);
		list.paintComponent(g);
	}


	@Test
	public void testPaintComponent_lineWrap_happyPath() {

		String code = "if (true) {\n" +
			"   println('Do work');\n" +
			"}";

		// Set properties that complicate line number rendering so we
		// exercise more code
		RSyntaxTextArea textArea = new RSyntaxTextArea(code);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSHARP);
		textArea.setCodeFoldingEnabled(true);
		textArea.setLineWrap(true);
		textArea.setBounds(0, 0, 80, 80);

		LineNumberList list = new LineNumberList(textArea);

		Graphics g = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB)
			.getGraphics();
		textArea.setBounds(0, 0, 80, 80);
		list.paintComponent(g);
	}
}
