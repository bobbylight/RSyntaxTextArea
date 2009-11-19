/*
 * 10/27/2004
 *
 * RSyntaxTextAreaDefaultInputMap.java - The default input map for
 * RSyntaxTextAreas.
 * Copyright (C) 2004 Robert Futrell
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

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

import org.fife.ui.rtextarea.RTADefaultInputMap;


/**
 * The default input map for an <code>RSyntaxTextArea</code>.
 * Currently, the new key bindings include:
 * <ul>
 *   <li>Shift+Tab indents the current line or currently selected lines
 *       to the left.
 * </ul>
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RSyntaxTextAreaDefaultInputMap extends RTADefaultInputMap {

	/**
	 * Constructs the default input map for an <code>RSyntaxTextArea</code>.
	 */
	public RSyntaxTextAreaDefaultInputMap() {

		int defaultMod = getDefaultModifier();
		//int ctrl = InputEvent.CTRL_MASK;
		int shift = InputEvent.SHIFT_MASK;
		//int alt = InputEvent.ALT_MASK;

		put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,   shift),				RSyntaxTextAreaEditorKit.rstaDecreaseIndentAction);
		put(KeyStroke.getKeyStroke('}'),									RSyntaxTextAreaEditorKit.rstaCloseCurlyBraceAction);
		put(KeyStroke.getKeyStroke('/'), 									RSyntaxTextAreaEditorKit.rstaCloseMarkupTagAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, defaultMod),			RSyntaxTextAreaEditorKit.rstaToggleCommentAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, defaultMod),	RSyntaxTextAreaEditorKit.rstaGoToMatchingBracketAction);

		// FIXME:  The keystroke associated with this action should be dynamic and
		// configurable and synchronized with the "trigger" defined in RSyntaxTextArea's
		// CodeTemplateManager.
		// NOTE:  no modifiers => mapped to keyTyped.  If we had "0" as a second
		// second parameter, we'd get the template action (keyPressed) AND the
		// default space action (keyTyped).
		//put(KeyStroke.getKeyStroke(' '),			RSyntaxTextAreaEditorKit.rstaPossiblyInsertTemplateAction);
		put(CodeTemplateManager.TEMPLATE_KEYSTROKE,	RSyntaxTextAreaEditorKit.rstaPossiblyInsertTemplateAction);

	}


}