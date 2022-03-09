/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

/**
 * Unit tests for the {@code PopupWindowDecorator} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class PopupWindowDecoratorTest {


	@Test
	void testGetSet() {

		PopupWindowDecorator decorator = new PopupWindowDecorator() {
			@Override
			public void decorate(JWindow window) {
				// Do nothing (comment for Sonar)
			}
		};

		Assertions.assertNull(PopupWindowDecorator.get());
		PopupWindowDecorator.set(decorator);
		Assertions.assertEquals(decorator, PopupWindowDecorator.get());
	}
}
