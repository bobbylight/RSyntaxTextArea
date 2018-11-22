package org.fife.ui.rtextarea;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.Icon;
import javax.swing.text.BadLocationException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Unit tests for the {@link Gutter} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class GutterTest {

	private static final String PLAIN_TEXT = "Line 1\n"
			+ "Line 2\n"
			+ "Line 3\n";


	@Test
	public void testAddLineTrackingIcon_2Arg_Valid() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new TestIcon();

		GutterIconInfo gii = gutter.addLineTrackingIcon(1, icon);
		int line1Start = textArea.getText().indexOf('\n') + 1;
		Assert.assertTrue(gii.getIcon() == icon);
		Assert.assertEquals(line1Start, gii.getMarkedOffset());
		Assert.assertNull(gii.getToolTip());

		textArea.insert("a", 0);
		line1Start = textArea.getText().indexOf('\n') + 1; // Should have advanced 1
		Assert.assertEquals(line1Start, gii.getMarkedOffset());

	}


	@Test(expected = BadLocationException.class)
	public void testAddLineTrackingIcon_2Arg_Invalid() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new TestIcon();

		gutter.addLineTrackingIcon(4, icon);

	}


	@Test
	public void testAddLineTrackingIcon_3Arg_Valid() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new TestIcon();
		String tip = "tip text";

		GutterIconInfo gii = gutter.addLineTrackingIcon(1, icon, tip);
		int line1Start = textArea.getText().indexOf('\n') + 1;
		Assert.assertTrue(gii.getIcon() == icon);
		Assert.assertEquals(line1Start, gii.getMarkedOffset());
		Assert.assertEquals(tip, gii.getToolTip());

		textArea.insert("a", 0);
		line1Start = textArea.getText().indexOf('\n') + 1; // Should have advanced 1
		Assert.assertEquals(line1Start, gii.getMarkedOffset());

	}


	@Test(expected = BadLocationException.class)
	public void testAddLineTrackingIcon_3Arg_Invalid() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new TestIcon();
		String tip = "tip text";

		gutter.addLineTrackingIcon(4, icon, tip);

	}


	@Test
	public void testAddOffsetTrackingIcon_2Arg_Valid() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new TestIcon();

		GutterIconInfo gii = gutter.addOffsetTrackingIcon(17, icon);
		Assert.assertTrue(gii.getIcon() == icon);
		Assert.assertEquals(17, gii.getMarkedOffset());
		Assert.assertNull(gii.getToolTip());

		textArea.insert("a", 3);
		Assert.assertEquals(18, gii.getMarkedOffset());

	}


	@Test(expected = BadLocationException.class)
	public void testAddOffsetTrackingIcon_2Arg_Invalid() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new TestIcon();

		gutter.addOffsetTrackingIcon(1024, icon);

	}


	@Test
	public void testAddOffsetTrackingIcon_3Arg_Valid() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new TestIcon();
		String tip = "tip text";

		GutterIconInfo gii = gutter.addOffsetTrackingIcon(17, icon, tip);
		Assert.assertTrue(gii.getIcon() == icon);
		Assert.assertEquals(17, gii.getMarkedOffset());
		Assert.assertEquals(tip, gii.getToolTip());

		textArea.insert("a", 3);
		Assert.assertEquals(18, gii.getMarkedOffset());

	}


	@Test(expected = BadLocationException.class)
	public void testAddOffsetTrackingIcon_3Arg_Invalid() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new TestIcon();
		String tip = "tip text";

		gutter.addOffsetTrackingIcon(1024, icon, tip);

	}


	@Test
	public void testGetActiveLineRangeColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.blue;
		gutter.setActiveLineRangeColor(color);
		Assert.assertEquals(color, gutter.getActiveLineRangeColor());

		color = Color.red;
		gutter.setActiveLineRangeColor(color);
		Assert.assertEquals(color, gutter.getActiveLineRangeColor());

	}


	@Test
	public void testGetBookmarkIcon() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertNull(gutter.getBookmarkIcon());

		Icon icon = new TestIcon();
		gutter.setBookmarkIcon(icon);
		Assert.assertTrue(icon == gutter.getBookmarkIcon());

	}


	@Test
	public void getBookmarks_EmptyArray() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertEquals(0, gutter.getBookmarks().length); // Non-null

	}


	@Test
	public void getBookmarks_SomeBookmarks() throws Exception {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		gutter.setBookmarkingEnabled(true);
		gutter.setBookmarkIcon(new TestIcon());
		Assert.assertTrue(gutter.toggleBookmark(1));
		Assert.assertTrue(gutter.toggleBookmark(2));
		Assert.assertEquals(2, gutter.getBookmarks().length); // Non-null
	}


	@Test
	public void getBookmarks_SomeBookmarks_BookmarkingDisabled() throws Exception {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertEquals(0, gutter.getBookmarks().length); // Non-null
	}


	@Test
	public void getBookmarks_SomeBookmarks_NoIcon() throws Exception {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		gutter.setBookmarkingEnabled(true);
		// Both enabled state and icon are reuqired
		Assert.assertEquals(0, gutter.getBookmarks().length); // Non-null
	}


	@Test
	public void testGetBorderColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setBorderColor(color);
		Assert.assertEquals(color, gutter.getBorderColor());

		color = Color.green;
		gutter.setBorderColor(color);
		Assert.assertEquals(color, gutter.getBorderColor());

	}


	@Test
	public void testGetFoldBackground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setFoldBackground(color);
		Assert.assertEquals(color, gutter.getFoldBackground());

		color = Color.green;
		gutter.setFoldBackground(color);
		Assert.assertEquals(color, gutter.getFoldBackground());

	}


	@Test
	public void testGetFoldIndicatorForeground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setFoldIndicatorForeground(color);
		Assert.assertEquals(color, gutter.getFoldIndicatorForeground());

		color = Color.green;
		gutter.setFoldIndicatorForeground(color);
		Assert.assertEquals(color, gutter.getFoldIndicatorForeground());

	}


	@Test
	public void testGetIconRowHeaderInheritsGutterBackground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertFalse(gutter.getIconRowHeaderInheritsGutterBackground());

		gutter.setIconRowHeaderInheritsGutterBackground(true);
		Assert.assertTrue(gutter.getIconRowHeaderInheritsGutterBackground());

	}


	@Test
	public void testGetLineNumberColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setLineNumberColor(color);
		Assert.assertEquals(color, gutter.getLineNumberColor());

		color = Color.green;
		gutter.setLineNumberColor(color);
		Assert.assertEquals(color, gutter.getLineNumberColor());

	}


	@Test
	public void testGetLineNumberFont() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Font font = new Font("Comic Sans", Font.PLAIN, 13);
		gutter.setLineNumberFont(font);
		Assert.assertEquals(font, gutter.getLineNumberFont());

		font = new Font("Arial", Font.ITALIC, 22);
		gutter.setLineNumberFont(font);
		Assert.assertEquals(font, gutter.getLineNumberFont());

	}


	@Test
	public void testGetLineNumberStartIndex() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertEquals(1, gutter.getLineNumberingStartIndex());

		gutter.setLineNumberingStartIndex(24);
		Assert.assertEquals(24, gutter.getLineNumberingStartIndex());

	}


	@Test
	public void testGetLineNumbersEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertTrue(gutter.getLineNumbersEnabled());

		gutter.setLineNumbersEnabled(false);
		Assert.assertFalse(gutter.getLineNumbersEnabled());

	}


	@Test
	public void testGetShowCollapsedRegionToolTips() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertTrue(gutter.getShowCollapsedRegionToolTips());

		gutter.setShowCollapsedRegionToolTips(true);
		Assert.assertTrue(gutter.getShowCollapsedRegionToolTips());

	}


	@Test
	public void testGetTrackingIcons_EmptyArray() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		textArea.setSize(200, 200);

		Point p = new Point(8, 8);
		Assert.assertEquals(0, gutter.getTrackingIcons(p).length);

	}


	@Test
	public void testIsFoldIndicatorEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertFalse(gutter.isFoldIndicatorEnabled());

		gutter.setFoldIndicatorEnabled(true);
		Assert.assertTrue(gutter.isFoldIndicatorEnabled());

	}


	@Test
	public void testIsBookmarkingEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertFalse(gutter.isBookmarkingEnabled());

		gutter.setBookmarkingEnabled(true);
		Assert.assertTrue(gutter.isBookmarkingEnabled());

	}


	@Test
	public void testIsIconRowHeaderEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertFalse(gutter.isIconRowHeaderEnabled());

		gutter.setIconRowHeaderEnabled(true);
		Assert.assertTrue(gutter.isIconRowHeaderEnabled());

	}


	@Test
	public void testRemoveAllTrackingIcons_Simple() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new TestIcon();
		textArea.setSize(200, 200);

		gutter.addLineTrackingIcon(0, icon);
		Point p = new Point(0, 4);
		Assert.assertEquals(1, gutter.getTrackingIcons(p).length);
		gutter.removeAllTrackingIcons();
		Assert.assertEquals(0, gutter.getTrackingIcons(p).length);

	}


	@Test
	public void testRemoveTrackingIcon_Simple() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon icon = new TestIcon();
		textArea.setSize(200, 200);

		GutterIconInfo info = gutter.addLineTrackingIcon(0, icon);
		Point p = new Point(0, 4);
		Assert.assertEquals(1, gutter.getTrackingIcons(p).length);
		gutter.removeTrackingIcon(info);
		Assert.assertEquals(0, gutter.getTrackingIcons(p).length);

	}


	@Test
	public void testSetActiveLineRangeColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.blue;
		gutter.setActiveLineRangeColor(color);
		Assert.assertEquals(color, gutter.getActiveLineRangeColor());

		color = Color.red;
		gutter.setActiveLineRangeColor(color);
		Assert.assertEquals(color, gutter.getActiveLineRangeColor());

	}


	@Test
	public void testSetBookmarkIcon() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertNull(gutter.getBookmarkIcon());

		Icon icon = new TestIcon();
		gutter.setBookmarkIcon(icon);
		Assert.assertTrue(icon == gutter.getBookmarkIcon());

	}


	@Test
	public void testSetBorderColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setBorderColor(color);
		Assert.assertEquals(color, gutter.getBorderColor());

		color = Color.green;
		gutter.setBorderColor(color);
		Assert.assertEquals(color, gutter.getBorderColor());

	}


	@Test
	@Ignore("Not yet implemented")
	public void testSetComponentOrientation() {
		
	}


	@Test
	@Ignore("Not yet implemented")
	public void testSetFoldIcons() {
		
	}


	@Test
	public void testSetFoldBackground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setFoldBackground(color);
		Assert.assertEquals(color, gutter.getFoldBackground());

		color = Color.green;
		gutter.setFoldBackground(color);
		Assert.assertEquals(color, gutter.getFoldBackground());

		// Sets to default - not a public value, but also not Color.green.
		gutter.setFoldBackground(null);
		Assert.assertNotNull(gutter.getFoldBackground());
		Assert.assertNotEquals(color, gutter.getFoldBackground());

	}


	@Test
	public void testSetFoldIndicatorForeground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setFoldIndicatorForeground(color);
		Assert.assertEquals(color, gutter.getFoldIndicatorForeground());

		color = Color.green;
		gutter.setFoldIndicatorForeground(color);
		Assert.assertEquals(color, gutter.getFoldIndicatorForeground());

		// Sets to default - not a public value, but also not Color.green.
		gutter.setFoldIndicatorForeground(null);
		Assert.assertNotNull(gutter.getFoldIndicatorForeground());
		Assert.assertNotEquals(color, gutter.getFoldIndicatorForeground());

	}


	@Test
	public void testSetIconRowHeaderInheritsGutterBackground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertFalse(gutter.getIconRowHeaderInheritsGutterBackground());

		gutter.setIconRowHeaderInheritsGutterBackground(true);
		Assert.assertTrue(gutter.getIconRowHeaderInheritsGutterBackground());

	}


	@Test
	public void testSetLineNumberColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setLineNumberColor(color);
		Assert.assertEquals(color, gutter.getLineNumberColor());

		color = Color.green;
		gutter.setLineNumberColor(color);
		Assert.assertEquals(color, gutter.getLineNumberColor());

	}


	@Test
	public void testSetLineNumberFont() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Font font = new Font("Comic Sans", Font.PLAIN, 13);
		gutter.setLineNumberFont(font);
		Assert.assertEquals(font, gutter.getLineNumberFont());

		font = new Font("Arial", Font.ITALIC, 22);
		gutter.setLineNumberFont(font);
		Assert.assertEquals(font, gutter.getLineNumberFont());

	}


	@Test(expected = IllegalArgumentException.class)
	public void testSetLineNumberFont_NullArg() {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		gutter.setLineNumberFont(null);
	}


	@Test
	public void testSetLineNumberStartIndex() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertEquals(1, gutter.getLineNumberingStartIndex());

		gutter.setLineNumberingStartIndex(24);
		Assert.assertEquals(24, gutter.getLineNumberingStartIndex());

	}


	@Test
	public void testSetLineNumbersEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertTrue(gutter.getLineNumbersEnabled());

		gutter.setLineNumbersEnabled(true);
		Assert.assertTrue(gutter.getLineNumbersEnabled());

	}


	@Test
	public void testSetShowCollapsedRegionToolTips() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertTrue(gutter.getShowCollapsedRegionToolTips());

		gutter.setShowCollapsedRegionToolTips(true);
		Assert.assertTrue(gutter.getShowCollapsedRegionToolTips());

	}


	@Test
	public void testSetTrackingIcons_EmptyArray() throws Exception {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		textArea.setSize(200, 200);

		Point p = new Point(8, 8);
		Assert.assertEquals(0, gutter.getTrackingIcons(p).length);

	}


	@Test
	public void testSetFoldIcons_Simple() {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Icon collapsedIcon = new TestIcon();
		Icon expandedIcon = new TestIcon();
		gutter.setFoldIcons(collapsedIcon, expandedIcon);
		// Not much we can verify here
	}


	@Test
	public void testSetFoldIndicatorEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertFalse(gutter.isFoldIndicatorEnabled());

		gutter.setFoldIndicatorEnabled(true);
		Assert.assertTrue(gutter.isFoldIndicatorEnabled());

		gutter.setFoldIndicatorEnabled(false);
		Assert.assertFalse(gutter.isFoldIndicatorEnabled());

	}


	@Test
	public void testSetBookmarkingEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertFalse(gutter.isBookmarkingEnabled());

		gutter.setBookmarkingEnabled(true);
		Assert.assertTrue(gutter.isBookmarkingEnabled());

	}


	@Test
	public void testSetIconRowHeaderEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assert.assertFalse(gutter.isIconRowHeaderEnabled());

		gutter.setIconRowHeaderEnabled(true);
		Assert.assertTrue(gutter.isIconRowHeaderEnabled());

		gutter.setIconRowHeaderEnabled(false);
		Assert.assertFalse(gutter.isIconRowHeaderEnabled());

	}


	@Test
	public void testToggleBookmark() throws Exception {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		gutter.setBookmarkingEnabled(true);
		gutter.setBookmarkIcon(new TestIcon());
		Assert.assertTrue(gutter.toggleBookmark(1));
		Assert.assertFalse(gutter.toggleBookmark(1));
		Assert.assertTrue(gutter.toggleBookmark(1));
	}


	/**
	 * A dummy icon implementation for test purposes.
	 */
	private static class TestIcon implements Icon {

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
		}

		@Override
		public int getIconWidth() {
			return 0;
		}

		@Override
		public int getIconHeight() {
			return 0;
		}

	}


}