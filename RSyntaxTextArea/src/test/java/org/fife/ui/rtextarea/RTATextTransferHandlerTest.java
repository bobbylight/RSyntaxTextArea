/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import org.fife.ui.SwingRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;


/**
 * Unit tests for the {@link RTATextTransferHandler} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
@RunWith(SwingRunner.class)
public class RTATextTransferHandlerTest {


	@Test
	public void testCreateTransferable_noSelection() {

		JTextArea textArea = new JTextArea("Hello world");
		Assert.assertNull(new RTATextTransferHandler().createTransferable(textArea));
	}


	@Test
	public void testCreateTransferable_selection() {

		JTextArea textArea = new JTextArea("Hello world\r\nLine 2");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(7);

		Transferable transferable = new RTATextTransferHandler().createTransferable(textArea);
		Assert.assertNotNull(transferable);
		RTATextTransferHandler.TextTransferable tt = (RTATextTransferHandler.TextTransferable)transferable;
		Assert.assertEquals("llo w", tt.getPlainData());
	}


	@Test
	public void testGetSourceActions_editableComponent() {
		JTextArea textArea = new JTextArea();
		int actual = new RTATextTransferHandler().getSourceActions(textArea);
		Assert.assertEquals(RTATextTransferHandler.COPY_OR_MOVE, actual);
	}


	@Test
	public void testGetSourceActions_nonEditableComponent() {
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		int actual = new RTATextTransferHandler().getSourceActions(textArea);
		Assert.assertEquals(RTATextTransferHandler.COPY, actual);
	}


	@Test
	public void testImportData_happyPath() {

		JTextArea sourceArea = new JTextArea("Hello world\r\nLine 2");
		sourceArea.setSelectionStart(2);
		sourceArea.setSelectionEnd(16);

		RTATextTransferHandler handler = new RTATextTransferHandler();
		Transferable transferable = handler.createTransferable(sourceArea);

		JTextArea destArea = new JTextArea();
		Assert.assertTrue(handler.importData(destArea, transferable));

		Assert.assertEquals("llo world\nLin", destArea.getText());
	}


	@Test
	public void testImportData_withinSameComponentAndSelectionRange() {

		JTextArea sourceArea = new JTextArea("Hello world");
		sourceArea.setSelectionStart(2);
		sourceArea.setSelectionEnd(7);

		RTATextTransferHandler handler = new RTATextTransferHandler();
		Transferable transferable = handler.createTransferable(sourceArea);
		sourceArea.setCaretPosition(4); // Within the prior selection

		Assert.assertTrue(handler.importData(sourceArea, transferable));
		Assert.assertEquals("Hello world", sourceArea.getText()); // unchanged
	}


	@Test
	public void testCanImport_falseSinceNotEditable() {

		Assert.assertFalse(canImportImpl(false, DataFlavor.stringFlavor));
		Assert.assertFalse(canImportImpl(false, DataFlavor.getTextPlainUnicodeFlavor()));
	}


	@Test
	public void testCanImport_happyPath() {

		Assert.assertTrue(canImportImpl(true, DataFlavor.stringFlavor));
		Assert.assertTrue(canImportImpl(true, DataFlavor.getTextPlainUnicodeFlavor()));
	}


	private boolean canImportImpl(boolean editable, DataFlavor flavor) {

		JTextArea textArea = new JTextArea("Hello world");
		textArea.setSelectionStart(2);
		textArea.setSelectionEnd(7);
		textArea.setEditable(editable);

		RTATextTransferHandler handler = new RTATextTransferHandler();
		Transferable transferable = handler.createTransferable(textArea);

		return handler.canImport(textArea, new DataFlavor[] {
			flavor
		});
	}
}
