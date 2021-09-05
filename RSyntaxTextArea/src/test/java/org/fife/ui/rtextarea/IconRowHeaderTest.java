/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for the {@link IconRowHeader} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
public class IconRowHeaderTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testPaintComponent_noLineWrap() {

		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		header.setActiveLineRange(1, 2);

		header.paintComponent(createTestGraphics());
	}


	@Test
	public void testPaintComponent_withLineWrap() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		IconRowHeader header = new IconRowHeader(textArea);
		header.setActiveLineRange(1, 2);

		header.paintComponent(createTestGraphics());
	}
}
