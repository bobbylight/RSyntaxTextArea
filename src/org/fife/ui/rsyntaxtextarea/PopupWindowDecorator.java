/*
 * 01/11/2011
 *
 * PopupWindowDecorator.java - Hook allowing hosting applications to decorate
 * JWindows created by the AutoComplete library.
 * Copyright (C) 2011 Robert Futrell
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

import javax.swing.JWindow;


/**
 * A hook allowing hosting applications to decorate JWindows created by the
 * AutoComplete library.  For example, you could use the
 * <a href="http://jgoodies.com/">JGoodies</a> library to add drop shadows
 * to the windows. 
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class PopupWindowDecorator {

	/**
	 * The singleton instance of this class.
	 */
	private static PopupWindowDecorator decorator;


	/**
	 * Callback called whenever an appropriate JWindow is created by the
	 * AutoComplete library.  Implementations can decorate the window however
	 * they see fit.
	 *
	 * @param window The newly-created window.
	 */
	public abstract void decorate(JWindow window);


	/**
	 * Returns the singleton instance of this class.  This should only be
	 * called on the EDT.
	 *
	 * @return The singleton instance of this class, or <code>null</code>
	 *         for none.
	 * @see #set(PopupWindowDecorator)
	 */
	public static PopupWindowDecorator get() {
		return decorator;
	}


	/**
	 * Sets the singleton instance of this class.  This should only be called
	 * on the EDT.
	 *
	 * @param decorator The new instance of this class.  This may be
	 *        <code>null</code>.
	 * @see #get()
	 */
	public static void set(PopupWindowDecorator decorator) {
		PopupWindowDecorator.decorator = decorator;
	}


}