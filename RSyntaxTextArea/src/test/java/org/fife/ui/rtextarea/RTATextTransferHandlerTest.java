/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;


/**
 * Unit tests for the {@link RTATextTransferHandler} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@ExtendWith(SwingRunnerExtension.class)
class RTATextTransferHandlerTest {


	@Test
	void testCreateTransferable_noSelection() {

		JTextArea textArea = new JTextArea("Hello world");
		Assertions.assertNull(new RTATextTransferHandler().createTransferable(textArea));
	}


	@Test
	void testCreateTransferable_selection() {

		JTextArea textArea = new JTextArea("Hello world\r\nLine 2");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(7);

		Transferable transferable = new RTATextTransferHandler().createTransferable(textArea);
		Assertions.assertNotNull(transferable);
		RTATextTransferHandler.TextTransferable tt = (RTATextTransferHandler.TextTransferable)transferable;
		Assertions.assertEquals("llo w", tt.getPlainData());
	}


	@Test
	void testGetSourceActions_editableComponent() {
		JTextArea textArea = new JTextArea();
		int actual = new RTATextTransferHandler().getSourceActions(textArea);
		Assertions.assertEquals(RTATextTransferHandler.COPY_OR_MOVE, actual);
	}


	@Test
	void testGetSourceActions_nonEditableComponent() {
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		int actual = new RTATextTransferHandler().getSourceActions(textArea);
		Assertions.assertEquals(RTATextTransferHandler.COPY, actual);
	}


	@Test
	void testImportData_happyPath() {

		JTextArea sourceArea = new JTextArea("Hello world\r\nLine 2");
		sourceArea.setSelectionStart(2);
		sourceArea.setSelectionEnd(16);

		RTATextTransferHandler handler = new RTATextTransferHandler();
		Transferable transferable = handler.createTransferable(sourceArea);

		JTextArea destArea = new JTextArea();
		Assertions.assertTrue(handler.importData(destArea, transferable));

		Assertions.assertEquals("llo world\nLin", destArea.getText());
	}


	@Test
	void testImportData_withinSameComponentAndSelectionRange() {

		JTextArea sourceArea = new JTextArea("Hello world");
		sourceArea.setSelectionStart(2);
		sourceArea.setSelectionEnd(7);

		RTATextTransferHandler handler = new RTATextTransferHandler();
		Transferable transferable = handler.createTransferable(sourceArea);
		sourceArea.setCaretPosition(4); // Within the prior selection

		Assertions.assertTrue(handler.importData(sourceArea, transferable));
		Assertions.assertEquals("Hello world", sourceArea.getText()); // unchanged
	}


	@Test
	void testCanImport_falseSinceNotEditable() {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		Assertions.assertFalse(canImportImpl(false, DataFlavor.stringFlavor));
		Assertions.assertFalse(canImportImpl(false, DataFlavor.getTextPlainUnicodeFlavor()));
	}


	@Test
	void testCanImport_happyPath() {

		Assumptions.assumeFalse(GraphicsEnvironment.isHeadless());

		Assertions.assertTrue(canImportImpl(true, DataFlavor.stringFlavor));
		Assertions.assertTrue(canImportImpl(true, DataFlavor.getTextPlainUnicodeFlavor()));
	}


	private boolean canImportImpl(boolean editable, DataFlavor flavor) {

		JTextArea textArea = new JTextArea("Hello world");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(7);
		textArea.setEditable(editable);

		RTATextTransferHandler handler = new RTATextTransferHandler();

		return handler.canImport(textArea, new DataFlavor[] {
			flavor
		});
	}
}
