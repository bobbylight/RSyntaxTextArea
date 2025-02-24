/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */

package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

/**
 * Unit tests for the {@link IconRowEvent} class.
 *
 * @author roger1337
 * @version 3.5.4
 */
public class IconRowEventTest {

	@Test
	void testIconRowEvent_verify() {
		int line = 1;
		int markedOffset = 2;
		Icon icon = new ImageIcon();
		String tooltip = "Hello";
		Object source = new Object();

		GutterIconInfo info = new GutterIconInfo() {
			@Override
			public Icon getIcon() {
				return icon;
			}

			@Override
			public int getMarkedOffset() {
				return markedOffset;
			}

			@Override
			public String getToolTip() {
				return tooltip;
			}
		};

		IconRowEvent event = new IconRowEvent(source, info, line);

		Assertions.assertEquals(line, event.getLine());
		Assertions.assertEquals(tooltip, event.getIconInfo().getToolTip());
		Assertions.assertEquals(markedOffset, event.getIconInfo().getMarkedOffset());
		Assertions.assertEquals(icon, event.getIconInfo().getIcon());
		Assertions.assertEquals(source, event.getSource());
	}
}
