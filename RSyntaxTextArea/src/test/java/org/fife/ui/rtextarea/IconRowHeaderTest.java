/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.AbstractRSyntaxTextAreaTest;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.MouseEvent;


/**
 * Unit tests for the {@link IconRowHeader} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class IconRowHeaderTest extends AbstractRSyntaxTextAreaTest {


	@Test
	void testAddOffsetTrackingIcon_2arg_invalidOffset() {
		Assertions.assertThrows(BadLocationException.class, () -> {
			RSyntaxTextArea textArea = createTextArea();
			IconRowHeader header = new IconRowHeader(textArea);
			header.addOffsetTrackingIcon(1000, new EmptyTestIcon());
		});
	}


	@Test
	void testAddOffsetTrackingIcon_3arg_invalidOffset() {
		Assertions.assertThrows(BadLocationException.class, () -> {
			RSyntaxTextArea textArea = createTextArea();
			IconRowHeader header = new IconRowHeader(textArea);
			header.addOffsetTrackingIcon(1000, new EmptyTestIcon(), "tool tip text");
		});
	}


	@Test
	void testClearActiveLineRange() {
		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		header.setActiveLineRange(1, 2);
		header.clearActiveLineRange();
	}


	@Test
	void testGetToggleBookmarks() throws BadLocationException {
		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		header.setBookmarkingEnabled(true);
		header.setBookmarkIcon(new EmptyTestIcon());
		header.addOffsetTrackingIcon(1, new EmptyTestIcon());
		header.toggleBookmark(1);
		GutterIconInfo[] bookmarks = header.getBookmarks();
		Assertions.assertEquals(1, bookmarks.length);
	}


	@Test
	void testGetSetActiveLineRangeColor() {
		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		header.setActiveLineRangeColor(Color.PINK);
		Assertions.assertEquals(Color.PINK, header.getActiveLineRangeColor());
		header.setActiveLineRangeColor(null); // Coverage
		Assertions.assertEquals(Gutter.DEFAULT_ACTIVE_LINE_RANGE_COLOR, header.getActiveLineRangeColor());
	}


	@Test
	void testGetSetBookmarkIcon() {
		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		Assertions.assertNull(header.getBookmarkIcon());
		header.setBookmarkIcon(new EmptyTestIcon());
		Assertions.assertNotNull(header.getBookmarkIcon());
	}


	@Test
	void testGetSetBookmarkingEnabled() {

		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);

		Assertions.assertFalse(header.isBookmarkingEnabled());
		header.setBookmarkingEnabled(true);
		Assertions.assertTrue(header.isBookmarkingEnabled());
		header.setBookmarkingEnabled(false);
		Assertions.assertFalse(header.isBookmarkingEnabled()); // coverage
	}


	@Test
	void testGetToolTipText() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		header.addOffsetTrackingIcon(0, new EmptyTestIcon(), "tool tip text");
		textArea.paint(createTestGraphics()); // Needed to initialize FontMetrics cache

		MouseEvent e = new MouseEvent(header, 0, 0, 0, 3, 3, 1, false);
		Assertions.assertEquals("tool tip text", header.getToolTipText(e));
	}


	@Test
	void testPaintComponent_noLineWrap() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		header.addOffsetTrackingIcon(1, new EmptyTestIcon());
		header.addOffsetTrackingIcon(2, new EmptyTestIcon(), "tool tip text");
		header.setActiveLineRange(1, 2);

		header.paintComponent(createTestGraphics());
	}


	@Test
	void testPaintComponent_withLineWrap() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		textArea.setLineWrap(true);
		IconRowHeader header = new IconRowHeader(textArea);
		header.addOffsetTrackingIcon(1, new EmptyTestIcon());
		header.addOffsetTrackingIcon(2, new EmptyTestIcon(), "tool tip text");
		header.setActiveLineRange(1, 2);

		header.paintComponent(createTestGraphics());
	}


	@Test
	void testSetInheritsGutterBackground() {
		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		header.setInheritsGutterBackground(true);
		header.setInheritsGutterBackground(false);
	}


	@Test
	void testToggleBookmark_bookmarkingDisabled() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		header.toggleBookmark(1);

		Assertions.assertEquals(0, header.getBookmarks().length);
	}


	@Test
	void testToggleBookmark_firstIconAdded() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		header.setBookmarkingEnabled(true);
		header.setBookmarkIcon(new EmptyTestIcon());

		header.toggleBookmark(1);
		Assertions.assertEquals(1, header.getBookmarks().length);
	}


	@Test
	void testToggleBookmark_secondBookmarkIconAdded() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		header.setBookmarkingEnabled(true);
		header.setBookmarkIcon(new EmptyTestIcon());

		header.toggleBookmark(1);
		header.toggleBookmark(2);
		Assertions.assertEquals(2, header.getBookmarks().length);
	}


	@Test
	void testToggleBookmark_togglingExistingBookmarkOff() throws BadLocationException {

		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		header.setBookmarkingEnabled(true);
		header.setBookmarkIcon(new EmptyTestIcon());

		header.toggleBookmark(1);
		header.toggleBookmark(1);
		Assertions.assertEquals(0, header.getBookmarks().length);
	}

	@Test
	void testAddIconRowListener_countOfListeners() {

		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);
		IconRowListener iconRowListener = new IconRowListener() {
			@Override
			public void bookmarkAdded(IconRowEvent e) {
			}
			@Override
			public void bookmarkRemoved(IconRowEvent e) {
			}
		};

		header.addIconRowListener(iconRowListener);
		Object[] listeners = header.getListeners(IconRowListener.class);
		Assertions.assertEquals(1, listeners.length);

		header.removeIconRowListener(iconRowListener);
		listeners = header.getListeners(IconRowListener.class);
		Assertions.assertEquals(0, listeners.length);
	}

	@Test
	void testAddIconRowListener_bookmarkAdded() {
		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);

		class IconRowListenerBookmarkAddedTest implements IconRowListener {
			private boolean added;

			@Override
			public void bookmarkAdded(IconRowEvent e) {
				added = true;
			}
			@Override
			public void bookmarkRemoved(IconRowEvent e) {
			}
		}

		IconRowListenerBookmarkAddedTest test = new IconRowListenerBookmarkAddedTest();

		header.addIconRowListener(test);
		header.setBookmarkingEnabled(true);
		header.setBookmarkIcon(new ImageIcon());
		Assertions.assertDoesNotThrow(() -> header.toggleBookmark(1),
			"Unexpected exception occurred when toggling bookmark");

		Assertions.assertTrue(test.added);
	}

	@Test
	void testAddIconRowListener_bookmarkRemoved() {
		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);

		class IconRowListenerBookmarkRemovedTest implements IconRowListener {
			private boolean removed;

			@Override
			public void bookmarkAdded(IconRowEvent e) {

			}
			@Override
			public void bookmarkRemoved(IconRowEvent e) {
				removed=true;
			}
		}

		IconRowListenerBookmarkRemovedTest test = new IconRowListenerBookmarkRemovedTest();

		header.addIconRowListener(test);
		header.setBookmarkingEnabled(true);
		header.setBookmarkIcon(new ImageIcon());
		Assertions.assertDoesNotThrow(() -> header.toggleBookmark(1),
			"Unexpected exception occurred when toggling bookmark");
		Assertions.assertDoesNotThrow(() -> header.toggleBookmark(1),
			"Unexpected exception occurred when toggling bookmark");

		Assertions.assertTrue(test.removed);
	}


	@Test
	void testAddIconRowListener_multipleBookmarkListeners() {
		RSyntaxTextArea textArea = createTextArea();
		IconRowHeader header = new IconRowHeader(textArea);

		class IconRowListenerBookmarkMultipleTest implements IconRowListener {
			private static int addedCount;
			private static int removedCount;


			@Override
			public void bookmarkAdded(IconRowEvent e) {
				addedCount++;
			}
			@Override
			public void bookmarkRemoved(IconRowEvent e) {
				removedCount++;
			}
		}

		IconRowListenerBookmarkMultipleTest test1 = new IconRowListenerBookmarkMultipleTest();
		IconRowListenerBookmarkMultipleTest test2 = new IconRowListenerBookmarkMultipleTest();
		IconRowListenerBookmarkMultipleTest test3 = new IconRowListenerBookmarkMultipleTest();

		header.addIconRowListener(test1);
		header.addIconRowListener(test2);
		header.addIconRowListener(test3);
		Assertions.assertEquals(3, header.getListeners(IconRowListener.class).length);

		header.setBookmarkingEnabled(true);
		header.setBookmarkIcon(new ImageIcon());
		Assertions.assertDoesNotThrow(() -> header.toggleBookmark(1),
			"Unexpected exception occurred when toggling bookmark");
		Assertions.assertDoesNotThrow(() -> header.toggleBookmark(1),
			"Unexpected exception occurred when toggling bookmark");

		Assertions.assertEquals(3, IconRowListenerBookmarkMultipleTest.addedCount);
		Assertions.assertEquals(3, IconRowListenerBookmarkMultipleTest.removedCount);

	}
}
