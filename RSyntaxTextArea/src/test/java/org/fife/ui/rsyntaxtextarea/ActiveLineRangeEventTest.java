/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@code ActiveLineRangeEvent} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ActiveLineRangeEventTest {


	@Test
	void testHappyPath() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		ActiveLineRangeEvent e = new ActiveLineRangeEvent(textArea, 1, 2);
		Assertions.assertEquals(1, e.getMin());
		Assertions.assertEquals(2, e.getMax());
	}
}
