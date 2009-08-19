/*
 * 07/31/2009
 *
 * ExtendedHyeprlinkListener.java - A hyperlink event from a FocusableTip.
 * Copyright (C) 2009 Robert Futrell
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
package org.fife.ui.rsyntaxtextarea.parser;

import java.util.EventListener;
import javax.swing.event.HyperlinkEvent;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.focusabletip.FocusableTip;


/**
 * Listens for hyperlink events from {@link FocusableTip}s.  In addition to
 * the link event, the text area that the tip is for is also received, which
 * allows the listener to modify the displayed content, if desired.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface ExtendedHyperlinkListener extends EventListener {


	/**
	 * Called when a link in a {@link FocusableTip} is clicked.
	 *
	 * @param textArea The text area displaying the tip.
	 * @param e The event.
	 */
	public void linkClicked(RSyntaxTextArea textArea, HyperlinkEvent e);


}