/*
 * 07/28/2008
 *
 * StyledTextTransferable.java - Used during drag-and-drop to represent RTF text.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;


/**
 * Object used during copy/paste and DnD operations to represent styled text.
 * It can return the text being moved as HTML, RTF or plain text.  This
 * class is basically the same as
 * <code>java.awt.datatransfer.StringSelection</code>, except that it can also
 * return the text in a couple of styled text formats.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class StyledTextTransferable implements Transferable {

	/**
	 * The transferred text, as HTML.
	 */
	private String html;

	/**
	 * The RTF data, in bytes (the RTF is 7-bit ascii).
	 */
	private byte[] rtfBytes;


	/**
	 * The "flavors" the text can be returned as.
	 */
	private static final DataFlavor[] FLAVORS = {
		DataFlavor.fragmentHtmlFlavor,
		new DataFlavor("text/rtf", "RTF"),
		DataFlavor.stringFlavor,
		DataFlavor.plainTextFlavor // deprecated
	};


	/**
	 * Constructor.
	 *
	 * @param html The transferred text, as HTML.
	 * @param rtfBytes The transferred text, as RTF bytes.
	 */
	StyledTextTransferable(String html, byte[] rtfBytes) {
		this.html = html;
		this.rtfBytes = rtfBytes;
	}


	@Override
	public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException, IOException {

		if (flavor.equals(FLAVORS[0])) { // HTML
			return html;
		}

		else if (flavor.equals(FLAVORS[1])) { // RTF
			return new ByteArrayInputStream(rtfBytes ==null ? new byte[0] : rtfBytes);
		}

		else if (flavor.equals(FLAVORS[2])) { // stringFlavor
			return rtfBytes ==null ? "" : RtfToText.getPlainText(rtfBytes);
		}

		else if (flavor.equals(FLAVORS[3])) { // plainTextFlavor (deprecated)
			String text = ""; // Valid if data==null
			if (rtfBytes !=null) {
				text = RtfToText.getPlainText(rtfBytes);
			}
			return new StringReader(text);
		}

		throw new UnsupportedFlavorException(flavor);
	}


	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return FLAVORS.clone();
	}


	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (DataFlavor flavor1 : FLAVORS) {
			if (flavor.equals(flavor1)) {
				return true;
			}
		}
		return false;
	}


}
