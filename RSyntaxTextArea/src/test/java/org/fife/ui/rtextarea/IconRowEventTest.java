/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */

package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

/**
 * Unit tests for the {@link IconRowHeader} class.
 *
 * @author roger1337
 * @version 3.5.4
 */
public class IconRowEventTest {

	@Test
	void testIconRowEvent_verify() {
		int LINE = 1;
		int MARKED_OFFSET = 2;
		Icon ICON = new ImageIcon();
		String TOOLTIP = "Hello";
		Object SOURCE = new Object();

		GutterIconInfo info = new GutterIconInfo() {
			@Override
			public Icon getIcon() {
				return ICON;
			}

			@Override
			public int getMarkedOffset() {
				return MARKED_OFFSET;
			}

			@Override
			public String getToolTip() {
				return TOOLTIP;
			}
		};

		IconRowEvent event = new IconRowEvent(SOURCE, info, LINE);

		Assertions.assertEquals(LINE, event.getLine());
		Assertions.assertEquals(TOOLTIP, event.getIconInfo().getToolTip());
		Assertions.assertEquals(MARKED_OFFSET, event.getIconInfo().getMarkedOffset());
		Assertions.assertEquals(ICON, event.getIconInfo().getIcon());
		Assertions.assertEquals(SOURCE, event.getSource());
	}
}
