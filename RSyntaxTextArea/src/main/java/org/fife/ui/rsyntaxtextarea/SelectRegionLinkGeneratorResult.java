/*
 * 02/16/2012
 *
 * Copyright (C) 2013 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import javax.swing.event.HyperlinkEvent;


/**
 * A link generator result that selects a region of text in the text area.
 * This will typically be used by IDE-style applications, to provide support
 * for "linking" the use of a variable in a document to its declaration.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see LinkGenerator
 */
public class SelectRegionLinkGeneratorResult implements LinkGeneratorResult {

	private RSyntaxTextArea textArea;
	private int sourceOffset;
	private int selStart;
	private int selEnd;


	public SelectRegionLinkGeneratorResult(RSyntaxTextArea textArea,
			int sourceOffset, int selStart, int selEnd) {
		this.textArea = textArea;
		this.sourceOffset = sourceOffset;
		this.selStart = selStart;
		this.selEnd = selEnd;
	}


	/**
	 * Selects the text in the text area.
	 */
	@Override
	public HyperlinkEvent execute() {
		textArea.select(selStart, selEnd);
		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSourceOffset() {
		return sourceOffset;
	}


}
