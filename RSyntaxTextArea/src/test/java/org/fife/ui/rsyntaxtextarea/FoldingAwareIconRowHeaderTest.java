/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for the {@link FoldingAwareIconRowHeader} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class FoldingAwareIconRowHeaderTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testPaintComponent_noLineWrap() {

		RSyntaxTextArea textArea = createTextArea();
		FoldingAwareIconRowHeader header = new FoldingAwareIconRowHeader(textArea);
		header.setActiveLineRange(1, 2);

		header.paintComponent(createTestGraphics());
	}


	@Test
	void testPaintComponent_withLineWrap() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		FoldingAwareIconRowHeader header = new FoldingAwareIconRowHeader(textArea);
		header.setActiveLineRange(1, 2);

		header.paintComponent(createTestGraphics());
	}
}
