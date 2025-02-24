/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.*;

import javax.swing.Icon;
import javax.swing.text.BadLocationException;

import org.fife.ui.SwingRunnerExtension;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Unit tests for the {@link Gutter} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class GutterTest extends AbstractRTextAreaTest {

	private static final String PLAIN_TEXT = "Line 1\nLine 2\nLine 3\n";


	@Test
	void testAddLineTrackingIcon_2Arg_Valid() throws Exception {

		// Extra coverage/logic for RSyntaxTextAreas
		RSyntaxTextArea textArea = new RSyntaxTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		gutter.setLineNumbersEnabled(true);
		gutter.setIconRowHeaderEnabled(true);
		gutter.setBookmarkingEnabled(true);
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
	void testGetSetActiveLineRangeColor() {

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
	void testGetSetArmedFoldBackground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		gutter.setArmedFoldBackground(Color.PINK);
		Assertions.assertEquals(Color.PINK, gutter.getArmedFoldBackground());
	}


	@Test
	void testGetSetBorderColor() {

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
	void testGetSetCurrentLineNumberColor() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Assertions.assertNull(gutter.getCurrentLineNumberColor());

		Color color = Color.red;
		gutter.setCurrentLineNumberColor(color);
		Assertions.assertEquals(color, gutter.getCurrentLineNumberColor());

	}


	@Test
	void testGetSetExpandedFoldRenderStrategy() {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertEquals(ExpandedFoldRenderStrategy.ON_HOVER, gutter.getExpandedFoldRenderStrategy());
		gutter.setExpandedFoldRenderStrategy(ExpandedFoldRenderStrategy.ALWAYS);
		Assertions.assertEquals(ExpandedFoldRenderStrategy.ALWAYS, gutter.getExpandedFoldRenderStrategy());
	}


	@Test
	void testGetSetFoldBackground() {

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
	void testGetSetFoldIndicatorArmedForeground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		Color color = Color.red;
		gutter.setFoldIndicatorArmedForeground(color);
		Assertions.assertEquals(color, gutter.getFoldIndicatorArmedForeground());

		color = Color.green;
		gutter.setFoldIndicatorArmedForeground(color);
		Assertions.assertEquals(color, gutter.getFoldIndicatorArmedForeground());

		// Sets to default - not a public value, but also not Color.green.
		gutter.setFoldIndicatorArmedForeground(null);
		Assertions.assertNotNull(gutter.getFoldIndicatorArmedForeground());
		Assertions.assertNotEquals(color, gutter.getFoldIndicatorArmedForeground());

	}


	@Test
	void testGetSetFoldIndicatorForeground() {

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
	void testGetSetIconRowHeaderInheritsGutterBackground() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertFalse(gutter.getIconRowHeaderInheritsGutterBackground());

		gutter.setIconRowHeaderInheritsGutterBackground(true);
		Assertions.assertTrue(gutter.getIconRowHeaderInheritsGutterBackground());

	}


	@Test
	void testGetSetLineNumberColor() {

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
	void testGetSetLineNumberFont() {

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
	void testGetSetLineNumberFormatter() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		LineNumberFormatter testFormatter = new LineNumberFormatter() {
			@Override
			public String format(int lineNumber) {
				return "test";
			}

			@Override
			public int getMaxLength(int maxLineNumber) {
				return 100;
			}
		};
		gutter.setLineNumberFormatter(testFormatter);

		Assertions.assertEquals(testFormatter, gutter.getLineNumberFormatter());
	}


	@Test
	void testGetSetLineNumbersEnabled() {

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

		gutter.setFoldIndicatorEnabled(false);
		Assertions.assertFalse(gutter.isFoldIndicatorEnabled());

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
	void testIsSetIconRowHeaderEnabled() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertFalse(gutter.isIconRowHeaderEnabled());

		gutter.setIconRowHeaderEnabled(true);
		Assertions.assertTrue(gutter.isIconRowHeaderEnabled());

		gutter.setIconRowHeaderEnabled(false);
		Assertions.assertFalse(gutter.isIconRowHeaderEnabled());

	}


	@Test
	void testPaint() {
		RSyntaxTextArea textArea = new RSyntaxTextArea(PLAIN_TEXT);
		textArea.setCodeFoldingEnabled(true);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
		Gutter gutter = new Gutter(textArea);
		gutter.setBounds(0, 0, 80, 80);
		gutter.paint(createTestGraphics());
	}


	@Test
	void testPropertyChange_codeFoldingTogglingUpdatesGutter() {

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		textArea.setCodeFoldingEnabled(true);
		Gutter gutter = new Gutter(textArea);
		Assertions.assertTrue(gutter.isFoldIndicatorEnabled());

		textArea.setCodeFoldingEnabled(false);
		Assertions.assertFalse(gutter.isFoldIndicatorEnabled());
	}


	@Test
	void testPropertyChange_newDocumentUpdatesGutter() {

		RSyntaxTextArea textArea = new RSyntaxTextArea();
		//Gutter gutter = new Gutter(textArea);

		textArea.setDocument(new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_C));
		// TODO: How to verify?
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
	void testSetComponentOrientation() {

		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		gutter.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		Assertions.assertEquals(ComponentOrientation.RIGHT_TO_LEFT, gutter.getComponentOrientation());

		gutter.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		Assertions.assertEquals(ComponentOrientation.LEFT_TO_RIGHT, gutter.getComponentOrientation());
	}


	@Test
	void testSetFoldIcons() {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		EmptyTestFoldIndicatorIcon icon = new EmptyTestFoldIndicatorIcon(true);
		gutter.setFoldIcons(icon, icon);
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
		EmptyTestFoldIndicatorIcon collapsedIcon = new EmptyTestFoldIndicatorIcon(true);
		EmptyTestFoldIndicatorIcon expandedIcon = new EmptyTestFoldIndicatorIcon(false);
		gutter.setFoldIcons(collapsedIcon, expandedIcon);
		// Not much we can verify here
	}


	@Test
	void testSetTextArea_newTextArea() {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);
		gutter.setTextArea(new RSyntaxTextArea(PLAIN_TEXT));
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


	@Test
	void testGutterBorder_getSetColor() {
		Gutter.GutterBorder border = new Gutter.GutterBorder(1, 1, 1, 1);
		border.setColor(Color.PINK);
		Assertions.assertEquals(Color.PINK, border.getColor());
	}


	@Test
	void testGutterBorder_setEdges() {
		Gutter.GutterBorder border = new Gutter.GutterBorder(1, 1, 1, 1);
		border.setEdges(2, 2, 2, 2);
		Assertions.assertEquals(new Insets(2, 2, 2, 2), border.getBorderInsets());
	}

	@Test
	void testIconListener_addAndRemove() {
		RTextArea textArea = new RTextArea(PLAIN_TEXT);
		Gutter gutter = new Gutter(textArea);

		IconRowListener iconRowListener = new IconRowListener() {
			@Override
			public void bookmarkAdded(IconRowEvent e) {
			}
			@Override
			public void bookmarkRemoved(IconRowEvent e) {
			}
		};
		gutter.addIconRowListener(iconRowListener);
		gutter.removeIconRowListener(iconRowListener);

		// not much to do here as it just passes the call on to IconRowHeader - real tests
		// will be in there
	}
}
