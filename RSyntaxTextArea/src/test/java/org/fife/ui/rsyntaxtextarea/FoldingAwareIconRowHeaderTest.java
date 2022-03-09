/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rtextarea.EmptyTestIcon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.text.BadLocationException;


/**
 * Unit tests for the {@link FoldingAwareIconRowHeader} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class FoldingAwareIconRowHeaderTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testPaintComponent_noRender_nullTextArea() {
		FoldingAwareIconRowHeader header = new FoldingAwareIconRowHeader(null);
		header.paintComponent(createTestGraphics());
	}


	@Test
	void testPaintComponent_noRender_codeFoldingNotSupportedForLanguage() {
		RSyntaxTextArea textArea = createTextArea(SyntaxConstants.SYNTAX_STYLE_CSV, "foo");
		FoldingAwareIconRowHeader header = new FoldingAwareIconRowHeader(textArea);
		header.paintComponent(createTestGraphics());
	}


	@Test
	void testPaintComponent_noLineWrap_noTrackingIcon() {

		RSyntaxTextArea textArea = createTextArea();
		FoldingAwareIconRowHeader header = new FoldingAwareIconRowHeader(textArea);
		header.setActiveLineRange(1, 2);

		header.paintComponent(createTestGraphics());
	}


	@Test
	void testPaintComponent_noLineWrap_withTrackingIcon() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		FoldingAwareIconRowHeader header = new FoldingAwareIconRowHeader(textArea);
		header.addOffsetTrackingIcon(3, new EmptyTestIcon());
		header.setActiveLineRange(1, 2);

		header.paintComponent(createTestGraphics());
	}


	@Test
	void testPaintComponent_withLineWrap_noTrackingIcon() {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		FoldingAwareIconRowHeader header = new FoldingAwareIconRowHeader(textArea);
		header.setActiveLineRange(1, 2);

		header.paintComponent(createTestGraphics());
	}


	@Test
	void testPaintComponent_withLineWrap_withTrackingIcon() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		FoldingAwareIconRowHeader header = new FoldingAwareIconRowHeader(textArea);
		header.addOffsetTrackingIcon(3, new EmptyTestIcon());
		header.setActiveLineRange(1, 2);

		header.paintComponent(createTestGraphics());
	}
}
