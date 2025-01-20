/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;


/**
 * Unit tests for {@code RTextAreaUI}.
 */
@ExtendWith(SwingRunnerExtension.class)
class RTextAreaUITest extends AbstractRTextAreaTest {

	@Test
	void testCreateUI_error_notRTextArea() {
		Assertions.assertThrows(IllegalArgumentException.class,
			() -> RTextAreaUI.createUI(new JTextArea()));
	}

	@Test
	void testCreateUI_happyPath() {
		Assertions.assertDoesNotThrow(() -> RTextAreaUI.createUI(new RTextArea()));
	}

	@Test
	void testInstallDefaults_doesNotOverrideRegularFont() {

		RTextArea textArea = new RTextArea();
		Font oldFont = new Font(FontUIResource.DIALOG, FontUIResource.ITALIC, 23);
		textArea.setFont(oldFont);

		RTextAreaUI ui = (RTextAreaUI)textArea.getUI();
		ui.installDefaults();
		Assertions.assertEquals(oldFont, textArea.getFont());
	}

	@Test
	void testInstallDefaults_overridesFontUIResource() {

		RTextArea textArea = new RTextArea();
		Font oldFont = new FontUIResource(FontUIResource.DIALOG, FontUIResource.ITALIC, 23);
		textArea.setFont(oldFont);

		RTextAreaUI ui = (RTextAreaUI)textArea.getUI();
		ui.installDefaults();
		Assertions.assertEquals(RTextArea.getDefaultFont(), textArea.getFont());
	}

	@Test
	void testInstallDefaults_overridesNullFont() {

		RTextArea textArea = new RTextArea();
		textArea.setFont(null);

		RTextAreaUI ui = (RTextAreaUI)textArea.getUI();
		ui.installDefaults();
		Assertions.assertEquals(RTextArea.getDefaultFont(), textArea.getFont());
	}

	@Test
	void testInstallUI_error_notRTextArea() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			RTextAreaUI ui = new RTextAreaUI(new RTextArea());
			ui.installUI(new JTextArea());
		}, "RTextAreaUI needs an instance of RTextArea!");
	}

	@Test
	void testInstallUI_happyPath() {
		Assertions.assertDoesNotThrow(() -> {
			RTextArea textArea = new RTextArea();
			RTextAreaUI ui = new RTextAreaUI(textArea);
			ui.installUI(textArea);
		});
	}

	@Test
	void testPaintBackground_nullBackground_noError() {
		Assertions.assertDoesNotThrow(() -> {
			RTextArea textArea = new RTextArea();
			RTextAreaUI ui = new RTextAreaUI(textArea);
			ui.installUI(textArea);
			textArea.setBackground(null);
			ui.paintBackground(createTestGraphics());
		});
	}

	@Test
	void testPaintCurrentLineHighlight_false() {
		Assertions.assertDoesNotThrow(() -> {
			RTextArea textArea = new RTextArea();
			RTextAreaUI ui = new RTextAreaUI(textArea);
			ui.installUI(textArea);
			textArea.setHighlightCurrentLine(false);
			ui.paintCurrentLineHighlight(createTestGraphics(), textArea.getVisibleRect());
		});
	}

	@Test
	void testPaintCurrentLineHighlight_true() {
		Assertions.assertDoesNotThrow(() -> {
			RTextArea textArea = new RTextArea();
			RTextAreaUI ui = new RTextAreaUI(textArea);
			ui.installUI(textArea);
			textArea.setHighlightCurrentLine(true);
			ui.paintCurrentLineHighlight(createTestGraphics(), textArea.getVisibleRect());
		});
	}

	@Test
	void testPaintCurrentLineHighlight_true_andFadeCurrentLineHighlight() {
		Assertions.assertDoesNotThrow(() -> {
			RTextArea textArea = new RTextArea();
			RTextAreaUI ui = new RTextAreaUI(textArea);
			ui.installUI(textArea);
			textArea.setHighlightCurrentLine(true);
			textArea.setFadeCurrentLineHighlight(true);
			ui.paintCurrentLineHighlight(createTestGraphics(), textArea.getVisibleRect());
		});
	}

	@Test
	void testPaintMarginLine_false() {
		Assertions.assertDoesNotThrow(() -> {
			RTextArea textArea = new RTextArea();
			RTextAreaUI ui = new RTextAreaUI(textArea);
			ui.installUI(textArea);
			textArea.setMarginLineEnabled(false);
			ui.paintMarginLine(createTestGraphics(), textArea.getVisibleRect());
		});
	}

	@Test
	void testPaintMarginLine_true() {
		Assertions.assertDoesNotThrow(() -> {
			RTextArea textArea = new RTextArea();
			RTextAreaUI ui = new RTextAreaUI(textArea);
			ui.installUI(textArea);
			textArea.setMarginLineEnabled(true);
			ui.paintMarginLine(createTestGraphics(), textArea.getVisibleRect());
		});
	}
}
