/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */

package org.fife.ui.rtextarea;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.text.BadLocationException;

/**
 * Unit tests for the {@link IconRowEvent} class.
 *
 * @author roger1337
 * @version 3.5.4
 */
public class IconRowEventTest {

	@Test
	void testMouseClicked() throws BadLocationException {
		int line = 0;
		ImageIcon icon = new ImageIcon();
		RTextArea textArea = new RTextArea();

		IconRowHeader h = new IconRowHeader(textArea);
		IconRowEvent evt = new IconRowEvent(h, null, line);

		Assertions.assertFalse(evt.isConsumed());
		Assertions.assertNotNull(evt.getIconsAtLine());
		Assertions.assertEquals(0, evt.getIconsAtLine().length);

		// icons at line
		h.addOffsetTrackingIcon(0, icon);
		h.addOffsetTrackingIcon(0, icon);
		Assertions.assertEquals(2, evt.getIconsAtLine().length);

		Assertions.assertNull(evt.getIconInfo()); // not a bookmark event

		evt.consume();
		Assertions.assertTrue(evt.isConsumed());
	}

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
