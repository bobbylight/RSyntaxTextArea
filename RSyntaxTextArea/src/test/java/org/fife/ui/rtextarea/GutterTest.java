/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import javax.swing.Icon;
import javax.swing.text.BadLocationException;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for the {@link Gutter} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class GutterTest {

	private static final String PLAIN_TEXT = "Line 1\n"
			+ "Line 2\n"
			+ "Line 3\n";


	@Test
	void testAddLineTrackingIcon_2Arg_Valid() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new EmptyTestIcon();

		GutterIconInfo gii = gutter.addLineTrackingIcon(1, icon);
		int line1Start = textArea.getText().indexOf('\n') + 1;
		Assertions.assertEquals(icon, gii.getIcon());
		Assertions.assertEquals(line1Start, gii.getMarkedOffset());
		Assertions.assertNull(gii.getToolTip());

		textArea.insert("a", 0);
		line1Start = textArea.getText().indexOf('\n') + 1; // Should have advanced 1
		Assertions.assertEquals(line1Start, gii.getMarkedOffset());

	}


	@Test
	void testAddLineTrackingIcon_2Arg_Invalid() {

		Assertions.assertThrows(BadLocationException.class, () -> {
			RTextArea textArea = new RTextArea(PLAIN_TEXT);
			Gutter gutter = new Gutter(textArea);
			Icon icon = new EmptyTestIcon();

			gutter.addLineTrackingIcon(4, icon);
		});
	}


	@Test
	void testAddLineTrackingIcon_3Arg_Valid() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new EmptyTestIcon();
		String tip = "tip text";

		GutterIconInfo gii = gutter.addLineTrackingIcon(1, icon, tip);
		int line1Start = textArea.getText().indexOf('\n') + 1;
		Assertions.assertEquals(icon, gii.getIcon());
		Assertions.assertEquals(line1Start, gii.getMarkedOffset());
		Assertions.assertEquals(tip, gii.getToolTip());

		textArea.insert("a", 0);
		line1Start = textArea.getText().indexOf('\n') + 1; // Should have advanced 1
		Assertions.assertEquals(line1Start, gii.getMarkedOffset());

	}


	@Test
	void testAddLineTrackingIcon_3Arg_Invalid() {

		Assertions.assertThrows(BadLocationException.class, () -> {
			RTextArea textArea = new RTextArea(PLAIN_TEXT);
			Gutter gutter = new Gutter(textArea);
			Icon icon = new EmptyTestIcon();
			String tip = "tip text";

			gutter.addLineTrackingIcon(4, icon, tip);
		});
	}


	@Test
	void testAddOffsetTrackingIcon_2Arg_Valid() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new EmptyTestIcon();

		GutterIconInfo gii = gutter.addOffsetTrackingIcon(17, icon);
		Assertions.assertEquals(icon, gii.getIcon());
		Assertions.assertEquals(17, gii.getMarkedOffset());
		Assertions.assertNull(gii.getToolTip());

		textArea.insert("a", 3);
		Assertions.assertEquals(18, gii.getMarkedOffset());

	}


	@Test
	void testAddOffsetTrackingIcon_2Arg_Invalid() {

		Assertions.assertThrows(BadLocationException.class, () -> {
			RTextArea textArea = new RTextArea(PLAIN_TEXT);
			Gutter gutter = new Gutter(textArea);
			Icon icon = new EmptyTestIcon();

			gutter.addOffsetTrackingIcon(1024, icon);
		});
	}


	@Test
	void testAddOffsetTrackingIcon_3Arg_Valid() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new EmptyTestIcon();
		String tip = "tip text";

		GutterIconInfo gii = gutter.addOffsetTrackingIcon(17, icon, tip);
		Assertions.assertEquals(icon, gii.getIcon());
		Assertions.assertEquals(17, gii.getMarkedOffset());
		Assertions.assertEquals(tip, gii.getToolTip());

		textArea.insert("a", 3);
		Assertions.assertEquals(18, gii.getMarkedOffset());

	}


	@Test
	void testAddOffsetTrackingIcon_3Arg_Invalid() {

		Assertions.assertThrows(BadLocationException.class, () -> {

			RTextArea textArea = new RTextArea(PLAIN_TEXT);
			Gutter gutter = new Gutter(textArea);
			Icon icon = new EmptyTestIcon();
			String tip = "tip text";

			gutter.addOffsetTrackingIcon(1024, icon, tip);
		});
	}


	@Test
	void testGetActiveLineRangeColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.blue;
		gutter.setActiveLineRangeColor(color);
		Assertions.assertEquals(color, gutter.getActiveLineRangeColor());

		color = Color.red;
		gutter.setActiveLineRangeColor(color);
		Assertions.assertEquals(color, gutter.getActiveLineRangeColor());

	}


	@Test
	void testGetBookmarkIcon() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertNull(gutter.getBookmarkIcon());

		Icon icon = new EmptyTestIcon();
		gutter.setBookmarkIcon(icon);
		Assertions.assertEquals(icon, gutter.getBookmarkIcon());

	}


	@Test
	void getBookmarks_EmptyArray() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertEquals(0, gutter.getBookmarks().length); // Non-null

	}


	@Test
	void getBookmarks_SomeBookmarks() throws Exception {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		gutter.setBookmarkingEnabled(true);
		gutter.setBookmarkIcon(new EmptyTestIcon());
		Assertions.assertTrue(gutter.toggleBookmark(1));
		Assertions.assertTrue(gutter.toggleBookmark(2));
		Assertions.assertEquals(2, gutter.getBookmarks().length); // Non-null
	}


	@Test
	void getBookmarks_SomeBookmarks_BookmarkingDisabled() {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertEquals(0, gutter.getBookmarks().length); // Non-null
	}


	@Test
	void getBookmarks_SomeBookmarks_NoIcon() {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		gutter.setBookmarkingEnabled(true);
		// Both enabled state and icon are required
		Assertions.assertEquals(0, gutter.getBookmarks().length); // Non-null
	}


	@Test
	void testGetBorderColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setBorderColor(color);
		Assertions.assertEquals(color, gutter.getBorderColor());

		color = Color.green;
		gutter.setBorderColor(color);
		Assertions.assertEquals(color, gutter.getBorderColor());

	}


	@Test
	void testGetFoldBackground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setFoldBackground(color);
		Assertions.assertEquals(color, gutter.getFoldBackground());

		color = Color.green;
		gutter.setFoldBackground(color);
		Assertions.assertEquals(color, gutter.getFoldBackground());

	}


	@Test
	void testGetFoldIndicatorForeground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setFoldIndicatorForeground(color);
		Assertions.assertEquals(color, gutter.getFoldIndicatorForeground());

		color = Color.green;
		gutter.setFoldIndicatorForeground(color);
		Assertions.assertEquals(color, gutter.getFoldIndicatorForeground());

		// Sets to default - not a public value, but also not Color.green.
		gutter.setFoldIndicatorForeground(null);
		Assertions.assertNotNull(gutter.getFoldIndicatorForeground());
		Assertions.assertNotEquals(color, gutter.getFoldIndicatorForeground());

	}


	@Test
	void testGetIconRowHeaderInheritsGutterBackground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertFalse(gutter.getIconRowHeaderInheritsGutterBackground());

		gutter.setIconRowHeaderInheritsGutterBackground(true);
		Assertions.assertTrue(gutter.getIconRowHeaderInheritsGutterBackground());

	}


	@Test
	void testGetLineNumberColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setLineNumberColor(color);
		Assertions.assertEquals(color, gutter.getLineNumberColor());

		color = Color.green;
		gutter.setLineNumberColor(color);
		Assertions.assertEquals(color, gutter.getLineNumberColor());

	}


	@Test
	void testGetLineNumberFont() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Font font = new Font("Comic Sans", Font.PLAIN, 13);
		gutter.setLineNumberFont(font);
		Assertions.assertEquals(font, gutter.getLineNumberFont());

		font = new Font("Arial", Font.ITALIC, 22);
		gutter.setLineNumberFont(font);
		Assertions.assertEquals(font, gutter.getLineNumberFont());

	}


	@Test
	void testGetLineNumberStartIndex() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertEquals(1, gutter.getLineNumberingStartIndex());

		gutter.setLineNumberingStartIndex(24);
		Assertions.assertEquals(24, gutter.getLineNumberingStartIndex());

	}


	@Test
	void testGetLineNumbersEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertTrue(gutter.getLineNumbersEnabled());

		gutter.setLineNumbersEnabled(false);
		Assertions.assertFalse(gutter.getLineNumbersEnabled());

	}


	@Test
	void testGetSetSpacingBetweenLineNumbersAndFoldIndicator() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertEquals(0, gutter.getSpacingBetweenLineNumbersAndFoldIndicator());

		gutter.setSpacingBetweenLineNumbersAndFoldIndicator(5);
		Assertions.assertEquals(5, gutter.getSpacingBetweenLineNumbersAndFoldIndicator());
	}


	@Test
	void testGetSetShowCollapsedRegionToolTips() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertTrue(gutter.getShowCollapsedRegionToolTips());

		gutter.setShowCollapsedRegionToolTips(true);
		Assertions.assertTrue(gutter.getShowCollapsedRegionToolTips());

	}


	@Test
	void testGetTrackingIcons_EmptyArray() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		textArea.setSize(200, 200);

		Point p = new Point(8, 8);
		Assertions.assertEquals(0, gutter.getTrackingIcons(p).length);

	}


	@Test
	void testIsFoldIndicatorEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertFalse(gutter.isFoldIndicatorEnabled());

		gutter.setFoldIndicatorEnabled(true);
		Assertions.assertTrue(gutter.isFoldIndicatorEnabled());

	}


	@Test
	void testIsBookmarkingEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertFalse(gutter.isBookmarkingEnabled());

		gutter.setBookmarkingEnabled(true);
		Assertions.assertTrue(gutter.isBookmarkingEnabled());

	}


	@Test
	void testIsIconRowHeaderEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertFalse(gutter.isIconRowHeaderEnabled());

		gutter.setIconRowHeaderEnabled(true);
		Assertions.assertTrue(gutter.isIconRowHeaderEnabled());

	}


	@Test
	void testRemoveAllTrackingIcons_Simple() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new EmptyTestIcon();
		textArea.setSize(200, 200);

		gutter.addLineTrackingIcon(0, icon);
		Point p = new Point(0, 4);
		Assertions.assertEquals(1, gutter.getTrackingIcons(p).length);
		gutter.removeAllTrackingIcons();
		Assertions.assertEquals(0, gutter.getTrackingIcons(p).length);

	}


	@Test
	void testRemoveTrackingIcon_Simple() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new EmptyTestIcon();
		textArea.setSize(200, 200);

		GutterIconInfo info = gutter.addLineTrackingIcon(0, icon);
		Point p = new Point(0, 4);
		Assertions.assertEquals(1, gutter.getTrackingIcons(p).length);
		gutter.removeTrackingIcon(info);
		Assertions.assertEquals(0, gutter.getTrackingIcons(p).length);

	}


	@Test
	void testSetActiveLineRangeColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.blue;
		gutter.setActiveLineRangeColor(color);
		Assertions.assertEquals(color, gutter.getActiveLineRangeColor());

		color = Color.red;
		gutter.setActiveLineRangeColor(color);
		Assertions.assertEquals(color, gutter.getActiveLineRangeColor());

	}


	@Test
	void testSetBookmarkIcon() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertNull(gutter.getBookmarkIcon());

		Icon icon = new EmptyTestIcon();
		gutter.setBookmarkIcon(icon);
		Assertions.assertEquals(icon, gutter.getBookmarkIcon());

	}


	@Test
	void testSetBorderColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setBorderColor(color);
		Assertions.assertEquals(color, gutter.getBorderColor());

		color = Color.green;
		gutter.setBorderColor(color);
		Assertions.assertEquals(color, gutter.getBorderColor());

	}


	@Test
	@Disabled("Not yet implemented")
	void testSetComponentOrientation() {

	}


	@Test
	@Disabled("Not yet implemented")
	void testSetFoldIcons() {

	}


	@Test
	void testSetFoldBackground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setFoldBackground(color);
		Assertions.assertEquals(color, gutter.getFoldBackground());

		color = Color.green;
		gutter.setFoldBackground(color);
		Assertions.assertEquals(color, gutter.getFoldBackground());

		// Sets to default - not a public value, but also not Color.green.
		gutter.setFoldBackground(null);
		Assertions.assertNotNull(gutter.getFoldBackground());
		Assertions.assertNotEquals(color, gutter.getFoldBackground());

	}


	@Test
	void testSetIconRowHeaderInheritsGutterBackground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertFalse(gutter.getIconRowHeaderInheritsGutterBackground());

		gutter.setIconRowHeaderInheritsGutterBackground(true);
		Assertions.assertTrue(gutter.getIconRowHeaderInheritsGutterBackground());

	}


	@Test
	void testSetLineNumberColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setLineNumberColor(color);
		Assertions.assertEquals(color, gutter.getLineNumberColor());

		color = Color.green;
		gutter.setLineNumberColor(color);
		Assertions.assertEquals(color, gutter.getLineNumberColor());

	}


	@Test
	void testSetLineNumberFont() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Font font = new Font("Comic Sans", Font.PLAIN, 13);
		gutter.setLineNumberFont(font);
		Assertions.assertEquals(font, gutter.getLineNumberFont());

		font = new Font("Arial", Font.ITALIC, 22);
		gutter.setLineNumberFont(font);
		Assertions.assertEquals(font, gutter.getLineNumberFont());

	}


	@Test
	void testSetLineNumberFont_NullArg() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			RTextArea textArea = new RTextArea(PLAIN_TEXT);
			Gutter gutter = new Gutter(textArea);
			gutter.setLineNumberFont(null);
		});
	}


	@Test
	void testSetLineNumberStartIndex() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertEquals(1, gutter.getLineNumberingStartIndex());

		gutter.setLineNumberingStartIndex(24);
		Assertions.assertEquals(24, gutter.getLineNumberingStartIndex());

	}


	@Test
	void testSetLineNumbersEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertTrue(gutter.getLineNumbersEnabled());

		gutter.setLineNumbersEnabled(true);
		Assertions.assertTrue(gutter.getLineNumbersEnabled());

	}


	@Test
	void testSetTrackingIcons_EmptyArray() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		textArea.setSize(200, 200);

		Point p = new Point(8, 8);
		Assertions.assertEquals(0, gutter.getTrackingIcons(p).length);

	}


	@Test
	void testSetFoldIcons_Simple() {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon collapsedIcon = new EmptyTestIcon();
		Icon expandedIcon = new EmptyTestIcon();
		gutter.setFoldIcons(collapsedIcon, expandedIcon);
		// Not much we can verify here
	}


	@Test
	void testSetFoldIndicatorEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertFalse(gutter.isFoldIndicatorEnabled());

		gutter.setFoldIndicatorEnabled(true);
		Assertions.assertTrue(gutter.isFoldIndicatorEnabled());

		gutter.setFoldIndicatorEnabled(false);
		Assertions.assertFalse(gutter.isFoldIndicatorEnabled());

	}


	@Test
	void testSetBookmarkingEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertFalse(gutter.isBookmarkingEnabled());

		gutter.setBookmarkingEnabled(true);
		Assertions.assertTrue(gutter.isBookmarkingEnabled());

	}


	@Test
	void testSetIconRowHeaderEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertFalse(gutter.isIconRowHeaderEnabled());

		gutter.setIconRowHeaderEnabled(true);
		Assertions.assertTrue(gutter.isIconRowHeaderEnabled());

		gutter.setIconRowHeaderEnabled(false);
		Assertions.assertFalse(gutter.isIconRowHeaderEnabled());

	}


	@Test
	void testToggleBookmark() throws Exception {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		gutter.setBookmarkingEnabled(true);
		gutter.setBookmarkIcon(new EmptyTestIcon());
		Assertions.assertTrue(gutter.toggleBookmark(1));
		Assertions.assertFalse(gutter.toggleBookmark(1));
		Assertions.assertTrue(gutter.toggleBookmark(1));
	}


}
