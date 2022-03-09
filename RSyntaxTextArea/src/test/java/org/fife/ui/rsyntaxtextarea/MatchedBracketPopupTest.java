/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import java.awt.*;


/**
 * Unit tests for the {@code MatchedBracketPopup} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class MatchedBracketPopupTest extends AbstractRSyntaxTextAreaTest {

	private JFrame frame;


	@BeforeEach
	void setUp() {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		frame = new JFrame();
		frame.setSize(100, 100);
		frame.setVisible(true);
	}


	@AfterEach
	void tearDown() {
		frame.dispose();
	}


	@Test
	void testGetPreferredSize() {
		RSyntaxTextArea textArea = createTextArea();
		MatchedBracketPopup popup = new MatchedBracketPopup(frame, textArea, 4);
		Assertions.assertNotNull(popup.getPreferredSize());
	}


	@Test
	void testDisposeOnWindowDeactivation() {

		RSyntaxTextArea textArea = createTextArea();
		MatchedBracketPopup popup = new MatchedBracketPopup(frame, textArea, 4);
		popup.setVisible(true);

		// Note this actually triggers componentLostFocus() and
		// not componentHidden() like you might think
		frame.setVisible(false);
		Assertions.assertFalse(popup.isVisible());
	}


	@Test
	@Disabled("Event is fired on next EDT tick, not sure how to test")
	void testDisposeOnWindowIconified() {

		RSyntaxTextArea textArea = createTextArea();
		MatchedBracketPopup popup = new MatchedBracketPopup(frame, textArea, 4);
		popup.setVisible(true);

		frame.setExtendedState(JFrame.ICONIFIED);
		Assertions.assertFalse(popup.isVisible());
	}


	@Test
	@Disabled("Event is fired on next EDT tick, not sure how to test")
	void testDisposeOnWindowMoved() {

		RSyntaxTextArea textArea = createTextArea();
		MatchedBracketPopup popup = new MatchedBracketPopup(frame, textArea, 4);
		popup.setVisible(true);

		Point loc = frame.getLocation();
		frame.setLocation(loc.x + 1, loc.y);
		Assertions.assertFalse(popup.isVisible());
	}
}
