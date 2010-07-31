/*
 * 07/28/2008
 *
 * RtfTransferable.java - Used during drag-and-drop to represent RTF text.
 * Copyright (C) 2008 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.datatransfer.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;


/**
 * Object used during copy/paste and DnD operations to represent RTF text.
 * It can return the text being moved as either RTF or plain text.  This
 * class is basically the same as
 * <code>java.awt.datatransfer.StringSelection</code>, except that it can also
 * return the text as RTF.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class RtfTransferable implements Transferable {

	/**
	 * The RTF data, in bytes (the RTF is 7-bit ascii).
	 */
	private byte[] data;


	/**
	 * The "flavors" the text can be returned as.
	 */
	private final DataFlavor[] FLAVORS = {
		new DataFlavor("text/rtf", "RTF"),
		DataFlavor.stringFlavor,
		DataFlavor.plainTextFlavor // deprecated
	};


	/**
	 * Constructor.
	 *
	 * @param data The RTF data.
	 */
	public RtfTransferable(byte[] data) {
		this.data = data;
	}


	public Object getTransferData(DataFlavor flavor)
					throws UnsupportedFlavorException, IOException {
		if (flavor.equals(FLAVORS[0])) { // RTF
			return new ByteArrayInputStream(data==null ? new byte[0] : data);
		}
		else if (flavor.equals(FLAVORS[1])) { // stringFlavor
			return data==null ? "" : RtfToText.getPlainText(data);
		}
		else if (flavor.equals(FLAVORS[2])) { // plainTextFlavor (deprecated)
			String text = ""; // Valid if data==null
			if (data!=null) {
				text = RtfToText.getPlainText(data);
			}
			return new StringReader(text);
		}
		else {
			throw new UnsupportedFlavorException(flavor);
		}
	}


	public DataFlavor[] getTransferDataFlavors() {
		return (DataFlavor[])FLAVORS.clone();
	}


	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (int i=0; i<FLAVORS.length; i++) {
			if (flavor.equals(FLAVORS[i])) {
				return true;
			}
		}
		return false;
	}


}