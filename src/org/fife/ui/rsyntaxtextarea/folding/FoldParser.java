/*
 * 10/08/2011
 *
 * FoldParser.java - Locates folds in an RSyntaxTextArea instance.
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
package org.fife.ui.rsyntaxtextarea.folding;

import java.util.List;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;


/**
 * Locates folds in a document.  If you are implementing a language that has
 * sections of source code that can be logically "folded," you can create an
 * instance of this interface that locates those regions and represents them
 * as {@link Fold}s.  <code>RSyntaxTextArea</code> knows how to take it from
 * there and implement code folding in the editor.
 *
 * @author Robert Futrell
 * @version 1.0
 * @see CurlyFoldParser
 */
public interface FoldParser {


	/**
	 * Returns a list of all folds in the text area.
	 *
	 * @param textArea The text area whose contents should be analyzed.
	 * @return The list of folds.  If this method returns <code>null</code>,
	 *         it is treated as if no folds were found.
	 */
	public List getFolds(RSyntaxTextArea textArea);


}