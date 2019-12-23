/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Unit tests for the {@link FoldingAwareIconRowHeader} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class FoldingAwareIconRowHeaderTest extends AbstractRSyntaxTextAreaTest {


	@Test
	public void testPaintComponent_noLineWrap() {

		RSyntaxTextArea textArea = createTextArea();
		FoldingAwareIconRowHeader header = new FoldingAwareIconRowHeader(textArea);
		header.setActiveLineRange(1, 2);

		header.paintComponent(createTestGraphics());
	}


	@Test
	public void testPaintComponent_withLineWrap() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		FoldingAwareIconRowHeader header = new FoldingAwareIconRowHeader(textArea);
		header.setActiveLineRange(1, 2);

		header.paintComponent(createTestGraphics());
	}
}
