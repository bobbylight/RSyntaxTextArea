/*
 * 10/27/2004
 *
 * RSyntaxTextAreaDefaultInputMap.java - The default input map for
 * RSyntaxTextAreas.
 * 
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
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
		// *nix causes trouble with CloseMarkupTagAction and ToggleCommentAction.
		// It triggers both KEY_PRESSED ctrl+'/' and KEY_TYPED '/' events when the
		// user presses ctrl+'/', but Windows and OS X do not.  So to appease
		// *nix, we remove the KEY_TYPED action and act on the KEY_PRESSED action.
		// Note we cannot simply remove the key-typed action; we must map it
		// to nothing to stop default action.
		put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0),					RSyntaxTextAreaEditorKit.rstaCloseMarkupTagAction);
		put(KeyStroke.getKeyStroke('/'),									"DoNothing");
		put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, defaultMod),			RSyntaxTextAreaEditorKit.rstaToggleCommentAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, defaultMod),	RSyntaxTextAreaEditorKit.rstaGoToMatchingBracketAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, defaultMod),		RSyntaxTextAreaEditorKit.rstaCollapseFoldAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, defaultMod),			RSyntaxTextAreaEditorKit.rstaExpandFoldAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_DIVIDE, defaultMod),			RSyntaxTextAreaEditorKit.rstaCollapseAllFoldsAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_MULTIPLY, defaultMod),		RSyntaxTextAreaEditorKit.rstaExpandAllFoldsAction);

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