/*
 * 08/29/2014
 *
 * ClipboardHistory.java - A history of text added to the clipboard.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Listens for cuts and copies from instances of {@link RTextArea}.  This is
 * used for the "clipboard history" shortcut (Ctrl+Shift+V by default).<p>
 *
 * Note that this class does not listen for all events on the system clipboard,
 * because that functionality is pretty fragile.  See
 * <a href="http://stackoverflow.com/questions/5484927/listen-to-clipboard-changes-check-ownership">
 * http://stackoverflow.com/questions/5484927/listen-to-clipboard-changes-check-ownership</a>
 * for more information.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class ClipboardHistory {

	private static ClipboardHistory INSTANCE;

	private List<String> history;
	private int maxSize;

	private static final int DEFAULT_MAX_SIZE = 12;


	private ClipboardHistory() {
		history = new ArrayList<String>();
		maxSize = DEFAULT_MAX_SIZE;
	}


	/**
	 * Adds an entry to the clipboard history.
	 *
	 * @param str The text to add.
	 * @see #getHistory()
	 */
	public void add(String str) {
		int size = history.size();
		if (size==0) {
			history.add(str);
		}
		else {
			int index = history.indexOf(str);
			if (index!=size-1) {
				if (index>-1) {
					history.remove(index);
				}
				history.add(str);
			}
			trim();
		}
	}


	/**
	 * Returns the singleton instance of this class, lazily creating it if
	 * necessary.<p>
	 *
	 * This method should only be called on the EDT.
	 *
	 * @return The singleton instance of this class.
	 */
	public static final ClipboardHistory get() {
		if (INSTANCE==null) {
			INSTANCE = new ClipboardHistory();
		}
		return INSTANCE;
	}


	/**
	 * Returns the clipboard history, in most-recently-used order.
	 *
	 * @return The clipboard history.
	 */
	public List<String> getHistory() {
		List<String> copy = new ArrayList<String>(this.history);
		Collections.reverse(copy);
		return copy;
	}


	/**
	 * Returns the maximum number of clipboard values remembered.
	 *
	 * @return The maximum number of clipboard values remembered.
	 * @see #setMaxSize(int)
	 */
	public int getMaxSize() {
		return maxSize;
	}


	/**
	 * Sets the maximum number of clipboard values remembered.
	 *
	 * @param maxSize The maximum number of clipboard values to remember.
	 * @throws IllegalArgumentException If <code>maxSize</code> is not greater
	 *         than zero.
	 * @see #getMaxSize()
	 */
	public void setMaxSize(int maxSize) {
		if (maxSize<=0) {
			throw new IllegalArgumentException("Maximum size must be >= 0");
		}
		this.maxSize = maxSize;
		trim();
	}


	/**
	 * Ensures the remembered set of strings is not larger than the maximum
	 * allowed size.
	 */
	private void trim() {
		while (history.size()>maxSize) {
			history.remove(0);
		}
	}


}