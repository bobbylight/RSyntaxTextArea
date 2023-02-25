/*
 * 08/19/2004
 *
 * RTADefaultInputMap.java - The default input map for RTextAreas.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;


/**
 * The default input map for an <code>RTextArea</code>.  For the most part it is
 * exactly that the one for a <code>JTextArea</code>, but it adds a few things.
 * Currently, the new key bindings include:
 * <ul>
 *   <li>HOME key toggles between first character on line and first non-
 *       whitespace character on line.
 *   <li>INSERT key toggles between insert and overwrite modes.
 *   <li>Ctrl+DELETE key deletes all text between the caret and the end of the
 *       current line.
 *   <li>Ctrl+Shift+Up and Ctrl+Shift+Down move the current line up and
 *       down, respectively.
 *   <li>Ctrl+J joins lines.
 *   <li>Ctrl+Z is undo and Ctrl+Y/Ctrl+Shift+Z is redo
 *   <li>Ctrl+Up and Ctrl+Down shift the visible area of the text area up and
 *       down one line, respectively.
 *   <li>F2 and Shift+F2 moves to the next and previous bookmarks,
 *       respectively.
 *   <li>Ctrl+F2 toggles whether a bookmark is on the current line.
 *   <li>etc.
 * </ul>
 *
 * Note on macOS, {@code Cmd} is usually the modifier key instead of {@code Ctrl}
 * as it is on Windows and Linux.
 */
@SuppressWarnings("checkstyle:linelength")
public class RTADefaultInputMap extends InputMap {


	/**
	 * Constructs the default input map for an <code>RTextArea</code>.
	 */
	public RTADefaultInputMap() {

		super();

		int defaultMod = RTextArea.getDefaultModifier();
		int ctrl = InputEvent.CTRL_DOWN_MASK;
		int alt = InputEvent.ALT_DOWN_MASK;
		int shift = InputEvent.SHIFT_DOWN_MASK;
		boolean isOSX = RTextArea.isOSX();
		int moveByWordMod = isOSX ? alt : defaultMod;

		put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME,   0),					isOSX ? DefaultEditorKit.beginAction : DefaultEditorKit.beginLineAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME,   shift),					isOSX ? DefaultEditorKit.selectionBeginAction : DefaultEditorKit.selectionBeginLineAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME,   defaultMod),			DefaultEditorKit.beginAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME,   defaultMod|shift),	DefaultEditorKit.selectionBeginAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_END,    0),					isOSX ? DefaultEditorKit.endAction : DefaultEditorKit.endLineAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_END,    shift),					isOSX ? DefaultEditorKit.selectionEndAction : DefaultEditorKit.selectionEndLineAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_END,    defaultMod),			DefaultEditorKit.endAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_END,    defaultMod|shift),	DefaultEditorKit.selectionEndAction);

		put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,   0),					DefaultEditorKit.backwardAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,   shift),					DefaultEditorKit.selectionBackwardAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,   moveByWordMod),			DefaultEditorKit.previousWordAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,   moveByWordMod|shift),	DefaultEditorKit.selectionPreviousWordAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,   0),					DefaultEditorKit.downAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,   shift),					DefaultEditorKit.selectionDownAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,   defaultMod),			RTextAreaEditorKit.rtaScrollDownAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,   alt),	RTextAreaEditorKit.rtaLineDownAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,  0),					DefaultEditorKit.forwardAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,  shift),					DefaultEditorKit.selectionForwardAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,  moveByWordMod),			DefaultEditorKit.nextWordAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,  moveByWordMod|shift),	DefaultEditorKit.selectionNextWordAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,     0),					DefaultEditorKit.upAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,     shift),					DefaultEditorKit.selectionUpAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,     defaultMod),			RTextAreaEditorKit.rtaScrollUpAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,     alt),	RTextAreaEditorKit.rtaLineUpAction);

		put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,   0),					DefaultEditorKit.pageUpAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,   shift),				RTextAreaEditorKit.rtaSelectionPageUpAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,   defaultMod|shift), 	RTextAreaEditorKit.rtaSelectionPageLeftAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0),					DefaultEditorKit.pageDownAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, shift),				RTextAreaEditorKit.rtaSelectionPageDownAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, defaultMod|shift),	RTextAreaEditorKit.rtaSelectionPageRightAction);

		put(KeyStroke.getKeyStroke(KeyEvent.VK_CUT,    0),				getCutAction());
		put(KeyStroke.getKeyStroke(KeyEvent.VK_COPY,   0),				getCopyAction());
		put(KeyStroke.getKeyStroke(KeyEvent.VK_PASTE,  0),				DefaultEditorKit.pasteAction);

		put(KeyStroke.getKeyStroke(KeyEvent.VK_X, defaultMod),			getCutAction());
		put(KeyStroke.getKeyStroke(KeyEvent.VK_C, defaultMod),			getCopyAction());
		put(KeyStroke.getKeyStroke(KeyEvent.VK_V, defaultMod),			DefaultEditorKit.pasteAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_V, defaultMod|shift),	RTextAreaEditorKit.clipboardHistoryAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),				DefaultEditorKit.deleteNextCharAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, shift),			getCutAction());
		put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, defaultMod),		RTextAreaEditorKit.rtaDeleteRestOfLineAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0),				RTextAreaEditorKit.rtaToggleTextModeAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, shift),			DefaultEditorKit.pasteAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, defaultMod),		getCopyAction());
		put(KeyStroke.getKeyStroke(KeyEvent.VK_A,      defaultMod),		DefaultEditorKit.selectAllAction);

		put(KeyStroke.getKeyStroke(KeyEvent.VK_D, defaultMod),			RTextAreaEditorKit.rtaDeleteLineAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_J, defaultMod),			RTextAreaEditorKit.rtaJoinLinesAction);

		put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, shift),		DefaultEditorKit.deletePrevCharAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, moveByWordMod),	RTextAreaEditorKit.rtaDeletePrevWordAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,   0),				DefaultEditorKit.insertTabAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),				DefaultEditorKit.insertBreakAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, shift),			DefaultEditorKit.insertBreakAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, defaultMod),		RTextAreaEditorKit.rtaDumbCompleteWordAction);

		put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, defaultMod),			RTextAreaEditorKit.rtaUndoAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, defaultMod),			RTextAreaEditorKit.rtaRedoAction);

		put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0),					RTextAreaEditorKit.rtaNextBookmarkAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, shift),				RTextAreaEditorKit.rtaPrevBookmarkAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, defaultMod),			RTextAreaEditorKit.rtaToggleBookmarkAction);

		put(KeyStroke.getKeyStroke(KeyEvent.VK_K, defaultMod|shift),	RTextAreaEditorKit.rtaPrevOccurrenceAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_K, defaultMod),			RTextAreaEditorKit.rtaNextOccurrenceAction);

		// Shortcuts specific to macOS.
		// macOS keyboard shortcuts documentation: https://support.apple.com/en-us/HT201236
		if (isOSX) {

			// These shortcuts overlap different shortcuts on Windows (see above for actions defined conditionally
			// via isOSX)
			put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, defaultMod),		DefaultEditorKit.beginLineAction);
			put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, defaultMod),		DefaultEditorKit.endLineAction);
			put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, defaultMod|shift),	DefaultEditorKit.selectionBeginLineAction);
			put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, defaultMod|shift),	DefaultEditorKit.selectionEndLineAction);

			// The actions below have additional shortcuts on macOS

			// Move/select to top or bottom of document
			put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, defaultMod),		DefaultEditorKit.beginAction);
			put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,     defaultMod|shift),	DefaultEditorKit.selectionBeginAction);
			put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, defaultMod),		DefaultEditorKit.endAction);
			put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,    defaultMod|shift),	DefaultEditorKit.selectionEndAction);

			// Basic keyboard navigation (yes, via Ctrl)
			put(KeyStroke.getKeyStroke(KeyEvent.VK_F, ctrl), DefaultEditorKit.forwardAction);
			put(KeyStroke.getKeyStroke(KeyEvent.VK_B, ctrl), DefaultEditorKit.backwardAction);
			put(KeyStroke.getKeyStroke(KeyEvent.VK_A, ctrl), DefaultEditorKit.beginLineAction);
			put(KeyStroke.getKeyStroke(KeyEvent.VK_E, ctrl), DefaultEditorKit.endLineAction);
			put(KeyStroke.getKeyStroke(KeyEvent.VK_P, ctrl), DefaultEditorKit.upAction);
			put(KeyStroke.getKeyStroke(KeyEvent.VK_N, ctrl), DefaultEditorKit.downAction);
			put(KeyStroke.getKeyStroke(KeyEvent.VK_O, ctrl), DefaultEditorKit.insertBreakAction);

			// Extra redo shortcut
			put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, defaultMod|shift), RTextAreaEditorKit.rtaRedoAction);

		}

		/* NOTE:  Currently, macros aren't part of the default input map for */
		/* RTextArea, as they display their own popup windows, etc. which    */
		/* may or may not clash with the application in which the RTextArea  */
		/* resides.  You can add the macro actions yourself into an          */
		/* application if you want.  They may become standard in the future  */
		/* if I can find a way to implement them that I like.                */
		/*
		put(KeyStroke.getKeyStroke(KeyEvent.VK_R,      defaultModifier|shift),	RTextAreaEditorKit.rtaBeginRecordingMacroAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_S,      defaultModifier|shift),	RTextAreaEditorKit.rtaEndRecordingMacroAction);
		put(KeyStroke.getKeyStroke(KeyEvent.VK_M,      defaultModifier|shift),	RTextAreaEditorKit.rtaPlaybackLastMacroAction);
		*/

	}

	protected String getCopyAction() {
		return DefaultEditorKit.copyAction;
	}

	protected String getCutAction() {
		return DefaultEditorKit.cutAction;
	}

}

