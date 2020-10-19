/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@code ActiveLineRangeEvent} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class ActiveLineRangeEventTest {


	@Test
	public void testHappyPath() {
		RSyntaxTextArea textArea = new RSyntaxTextArea();
		ActiveLineRangeEvent e = new ActiveLineRangeEvent(textArea, 1, 2);
		Assert.assertEquals(1, e.getMin());
		Assert.assertEquals(2, e.getMax());
	}
}
