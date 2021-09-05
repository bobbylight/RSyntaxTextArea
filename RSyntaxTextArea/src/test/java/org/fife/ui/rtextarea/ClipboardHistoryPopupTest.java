/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import java.util.Arrays;


/**
 * Unit tests for the {@link ClipboardHistoryPopup} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class ClipboardHistoryPopupTest extends AbstractRSyntaxTextAreaTest {

	private JFrame frame;
	private RSyntaxTextArea textArea;


	@BeforeEach
	void setUp() {

		frame = new JFrame();
		textArea = createTextArea();
		frame.add(textArea);
		frame.pack();
	}


	@Test
	@Disabled("These tests run locally, but not in CI environment for some reason")
	void testGetPreferredSize() {
		Assertions.assertNotNull(new ClipboardHistoryPopup(frame, textArea).getPreferredSize());
	}


	@Test
	@Disabled("These tests run locally, but not in CI environment for some reason")
	void testSetContents() {
		new ClipboardHistoryPopup(frame, textArea).setContents(Arrays.asList(
			"one", "two", "three"
		));
	}
}
