/*
 * 11/14/2003
 *
 * RTextArea.java - An extension of JTextArea that adds many features.
 * Copyright (C) 2003 Robert Futrell
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
package org.fife.ui.rtextarea;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoManager;

import org.fife.print.RPrintUtilities;
import org.fife.ui.rtextarea.Macro.MacroRecord;


/**
 * An extension of <code>JTextArea</code> that adds the following features:
 * <ul>
 *    <li>Insert/Overwrite modes (can be toggled via the Insert key)
 *    <li>A right-click popup menu with standard editing options
 *    <li>Macro support
 *    <li>"Mark all" functionality.
 *    <li>A way to change the background to an image (gif/jpg)
 *    <li>Highlight the current line (can be toggled)
 *    <li>An easy way to print its text (implements Printable)
 *    <li>Hard/soft (emulated with spaces) tabs
 *    <li>Fixes a bug with setTabSize
 *    <li>Other handy new methods
 * </ul>
 * NOTE:  If the background for an <code>RTextArea</code> is set to a color,
 * its opaque property is set to <code>true</code> for performance reasons.  If
 * the background is set to an image, then the opaque property is set to
 * <code>false</code>.  This slows things down a little, but if it didn't happen
 * then we would see garbage on-screen when the user scrolled through a document
 * using the arrow keys (not the page-up/down keys though).  You should never
 * have to set the opaque property yourself; it is always done for you.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RTextArea extends RTextAreaBase
								implements Printable, Serializable {

	/**
	 * Constant representing insert mode.
	 */
	public static final int INSERT_MODE				= 0;

	/**
	 * Constant representing overwrite mode.
	 */
	public static final int OVERWRITE_MODE				= 1;

	public static final String MARK_ALL_COLOR_PROPERTY	= "RTA.markAllColor";

	/**
	 * Constants for all actions.
	 */
	private static final int MIN_ACTION_CONSTANT			= 0;
	public static final int COPY_ACTION				= 0;
	public static final int CUT_ACTION					= 1;
	public static final int DELETE_ACTION				= 2;
	public static final int LINE_DOWN_ACTION			= 3;
	public static final int LINE_UP_ACTION				= 4;
	public static final int PASTE_ACTION				= 5;
	public static final int REDO_ACTION				= 6;
	public static final int SELECT_ALL_ACTION			= 7;
	public static final int UNDO_ACTION				= 8;
	private static final int MAX_ACTION_CONSTANT			= 8;

	private static final Color DEFAULT_MARK_ALL_COLOR		= Color.ORANGE;

	private int textMode;					// INSERT_MODE or OVERWRITE_MODE.

	// All macros are shared across all RTextAreas.
	private static boolean recordingMacro;		// Whether we're recording a macro.
	private static Macro currentMacro;

	private static JPopupMenu rightClickMenu;
	private static RecordableTextAction cutAction;
	private static RecordableTextAction copyAction;
	private static RecordableTextAction pasteAction;
	private static RecordableTextAction deleteAction;
	private static RecordableTextAction lineDownAction;
	private static RecordableTextAction lineUpAction;
	private static RecordableTextAction undoAction;
	private static RecordableTextAction redoAction;
	private static RecordableTextAction selectAllAction;

	private static IconGroup iconGroup;		// Info on icons for actions.

	private RUndoManager undoManager;

	private ArrayList markAllHighlights;		// Highlights from "mark all".
	private String markedWord;				// Expression marked in "mark all."
	private ChangableHighlightPainter markAllHighlightPainter;

	private boolean inUndoRedo;

	public static final int INSERT_CARET		= 0;
	public static final int OVERWRITE_CARET		= 1;

	private int[] carets;		// Index 0=>insert caret, 1=>overwrite.

	private static String cantUndoText;
	private static String cantRedoText;

	private static final String RESOURCE_BUNDLE	= "org.fife.ui.rtextarea.RTextArea";


	/**
	 * Creates a new <code>RTextArea</code> with the following properties:
	 * 10-point monospaced font, no word wrap, INSERT_MODE, Plain white
	 * background, tab size of 5 spaces, and font color of black.
	 */
	public RTextArea() {
		this(null, false, INSERT_MODE);
	}


	/**
	 * Creates a new <code>RTextArea</code>.
	 *
	 * @param font The font to use in this text area.
	 * @param wordWrapEnabled Whether or not to use word wrap.
	 * @param textMode Either <code>INSERT_MODE</code> or
	 *        <code>OVERWRITE_MODE</code>.
	 */
	public RTextArea(Font font, boolean wordWrapEnabled, int textMode) {

		super(font, wordWrapEnabled);

		// Create and initialize our actions.  This will only do so once
		// when the first RTextArea is created) as these actions are shared.
		initActions(this);

		// Set the line terminator property of the document to default to the
		// OS value.  We do this because by default, Documents do NOT have
		// this property set (it only gets set when the document is populated
		// via an EditorKit.read(doc, ...) method call).  This mess up RText's
		// "Document Properties" dialog, as it wants to display a line-terminator
		// value even if the document is a new (unsaved) one.  So, we'll just
		// give it a default here.
		getDocument().putProperty(
			RTextAreaEditorKit.EndOfLineStringProperty,
			System.getProperty("line.separator"));

		// Set the defaults for various stuff.
		Color markAllHighlightColor = getDefaultMarkAllHighlightColor();
		markAllHighlightPainter = new ChangableHighlightPainter(
										markAllHighlightColor);
		setMarkAllHighlightColor(markAllHighlightColor);
		carets = new int[2];
		carets[INSERT_CARET] = ConfigurableCaret.VERTICAL_LINE_STYLE;
		carets[OVERWRITE_CARET] = ConfigurableCaret.BLOCK_STYLE;
		setDragEnabled(true);			// Enable drag-and-drop.

		// Set values for stuff the user passed in.
		setTextMode(textMode); // carets array must be initialized first!

		// Install the undo manager.
		undoManager = new RUndoManager(this);
		getDocument().addUndoableEditListener(undoManager);

		// Fix the odd "Ctrl+H <=> Backspace" Java behavior.
		fixCtrlH();

	}


	/**
	 * Adds an action event to the current macro.  This shouldn't be called
	 * directly, as it is called by the actions themselves.
	 *
	 * @param id The ID of the recordable text action.
	 * @param actionCommand The "command" of the action event passed to it.
	 */
	static synchronized void addToCurrentMacro(String id,
											String actionCommand) {
		currentMacro.addMacroRecord(new Macro.MacroRecord(id, actionCommand));
	}


	/**
	 * Begins recording a macro.  After this method is called, all input/caret
	 * events, etc. are recorded until <code>endMacroRecording</code> is
	 * called.  If this method is called but the text component is already
	 * recording a macro, nothing happens (but the macro keeps recording).
	 *
	 * @see #isRecordingMacro
	 * @see #endRecordingMacro
	 */
	public static synchronized void beginRecordingMacro() {
		if (isRecordingMacro()) {
			//System.err.println("Macro already being recorded!");
			return;
		}
		//JOptionPane.showMessageDialog(this, "Now recording a macro");
		if (currentMacro!=null)
			currentMacro = null; // May help gc?
		currentMacro = new Macro();
		recordingMacro = true;
	}


	/**
	 * Clears any "mark all" highlights, if any.
	 *
	 * @see #markAll
	 * @see #getMarkAllHighlightColor
	 * @see #setMarkAllHighlightColor
	 */
	public void clearMarkAllHighlights() {
		Highlighter h = getHighlighter();
		if (h!=null && markAllHighlights!=null) {
			int count = markAllHighlights.size();
			for (int i=0; i<count; i++)
				h.removeHighlight(markAllHighlights.get(i));
			markAllHighlights.clear();
		}
		markedWord = null;
		repaint();
	}


	/**
	 * Returns the document to use for an <code>RTextArea</code>.
	 *
	 * @return The document.
	 */
	protected Document createDefaultModel() {
		return new RTextAreaDocument();
	}


	/**
	 * Returns the caret event/mouse listener for <code>RTextArea</code>s.
	 *
	 * @return The caret event/mouse listener.
	 */
	protected RTAMouseListener createMouseListener() {
		return new RTextAreaMutableCaretEvent(this);
	}


	/**
	 * Initializes the right-click popup menu with appropriate actions.
	 */
	private static void createRightClickMenu() {

		rightClickMenu = new JPopupMenu();
		JMenuItem menuItem;

		menuItem = new JMenuItem(undoAction);
		menuItem.setAccelerator(null);
		menuItem.setToolTipText(null);
		rightClickMenu.add(menuItem);

		menuItem = new JMenuItem(redoAction);
		menuItem.setAccelerator(null);
		menuItem.setToolTipText(null);
		rightClickMenu.add(menuItem);

		rightClickMenu.addSeparator();

		menuItem = new JMenuItem(cutAction);
		menuItem.setAccelerator(null);
		menuItem.setToolTipText(null);
		rightClickMenu.add(menuItem);

		menuItem = new JMenuItem(copyAction);
		menuItem.setAccelerator(null);
		menuItem.setToolTipText(null);
		rightClickMenu.add(menuItem);

		menuItem = new JMenuItem(pasteAction);
		menuItem.setAccelerator(null);
		menuItem.setToolTipText(null);
		rightClickMenu.add(menuItem);

		menuItem = new JMenuItem(deleteAction);
		menuItem.setAccelerator(null);
		menuItem.setToolTipText(null);
		rightClickMenu.add(menuItem);

		rightClickMenu.addSeparator();

		menuItem = new JMenuItem(selectAllAction);
		menuItem.setAccelerator(null);
		menuItem.setToolTipText(null);
		rightClickMenu.add(menuItem);

		ComponentOrientation orientation = ComponentOrientation.
								getOrientation(Locale.getDefault());
		rightClickMenu.applyComponentOrientation(orientation);

	}


	/**
	 * Returns the a real UI to install on this text area.
	 *
	 * @return The UI.
	 */
	protected RTextAreaUI createRTextAreaUI() {
		return new RTextAreaUI(this);
	}


	/**
	 * Removes all undoable edits from this document's undo manager.  This
	 * method also makes the undo/redo actions disabled.<br><br>
	 *
	 * NOTE:  For some reason, it appears I have to create an entirely new
	 *        <code>undoManager</code> for undo/redo to continue functioning
	 *        properly; if I don't, it only ever lets you do one undo.  Not
	 *        too sure why this is...
	 */
	public void discardAllEdits() {
		undoManager.discardAllEdits();
		getDocument().removeUndoableEditListener(undoManager);
		undoManager = new RUndoManager(this);
		getDocument().addUndoableEditListener(undoManager);
		undoManager.updateActions();
	}


	/**
	 * Ends recording a macro.  If this method is called but the text component
	 * is not recording a macro, nothing happens.
	 *
	 * @see #isRecordingMacro
	 * @see #beginRecordingMacro
	 */
	/*
	 * FIXME:  This should throw an exception if we're not recording a macro.
	 */
	public static synchronized void endRecordingMacro() {
		if (!isRecordingMacro()) {
			//System.err.println("Not recording a macro!");
			return;
		}
		recordingMacro = false;
	}


	/**
	 * Notifies all listeners that a caret change has occured.
	 *
	 * @param e The caret event.
	 */
	protected void fireCaretUpdate(CaretEvent e) {

		// Decide whether we need to repaint the current line background.
		possiblyUpdateCurrentLineHighlightLocation();

		// Now, if there is a highlighted region of text, allow them to cut
		// and copy.
		if (e!=null && e.getDot()!=e.getMark()) {// && !cutAction.isEnabled()) {
			cutAction.setEnabled(true);
			copyAction.setEnabled(true);
		}

		// Otherwise, if there is no highlighted region, don't let them cut
		// or copy.  The condition here should speed things up, because this
		// way, we will only enable the actions the first time the selection
		// becomes nothing.
		else if (cutAction.isEnabled()==true) {
			cutAction.setEnabled(false);
			copyAction.setEnabled(false);
		}

		super.fireCaretUpdate(e);

	}


	/**
	 * Removes the "Ctrl+H <=> Backspace" behavior that Java shows, for some
	 * odd reason...
	 */
	private void fixCtrlH() {
		InputMap inputMap = getInputMap();
		KeyStroke char010 = KeyStroke.getKeyStroke("typed \010");
		InputMap parent = inputMap;
		while (parent != null) {
			parent.remove(char010);
			parent = parent.getParent();
		}
		KeyStroke backspace = KeyStroke.getKeyStroke("BACK_SPACE");
		inputMap.put(backspace, DefaultEditorKit.deletePrevCharAction);
	}


	/**
	 * Provides a way to gain access to the editor actions on the right-click
	 * popup menu.  This way you can make toolbar/menu bar items use the actual
	 * actions used by all <code>RTextArea</code>s, so that icons stay
	 * synchronized and you don't have to worry about enabling/disabling them
	 * yourself.<p>
	 * Keep in mind that these actions are shared across all instances of
	 * <code>RTextArea</code>, so a change to any action returned by this
	 * method is global across all <code>RTextArea</code> editors in your
	 * application.
	 *
	 * @param action The action to retrieve, such as <code>CUT_ACTION</code>.
	 *        If the action name is invalid, <code>null</code> is returned.
	 * @return The action, or <code>null</code> if an invalid action is
	 *         requested.
	 */
	public static RecordableTextAction getAction(int action) {
		if (action<MIN_ACTION_CONSTANT || action>MAX_ACTION_CONSTANT)
			return null;
		switch (action) {
			case COPY_ACTION:
				return copyAction;
			case CUT_ACTION:
				return cutAction;
			case DELETE_ACTION:
				return deleteAction;
			case LINE_DOWN_ACTION:
				return lineDownAction;
			case LINE_UP_ACTION:
				return lineUpAction;
			case PASTE_ACTION:
				return pasteAction;
			case REDO_ACTION:
				return redoAction;
			case SELECT_ALL_ACTION:
				return selectAllAction;
			case UNDO_ACTION:
				return undoAction;
		}
		return null;
	}


	/**
	 * Returns the macro currently stored in this <code>RTextArea</code>.
	 * Since macros are shared, all <code>RTextArea</code>s in the currently-
	 * running application are using this macro.
	 *
	 * @return The current macro, or <code>null</code> if no macro has been
	 *         recorded/loaded.
	 * @see #loadMacro
	 */
	public static synchronized Macro getCurrentMacro() {
		return currentMacro;
	}


	/**
	 * Returns the default color used for "mark all."
	 *
	 * @return The color.
	 * @see #getMarkAllHighlightColor
	 * @see #setMarkAllHighlightColor
	 */
	public static final Color getDefaultMarkAllHighlightColor() {
		return DEFAULT_MARK_ALL_COLOR;
	}


	/**
	 * Returns the icon group being used for the actions of this text area.
	 *
	 * @return The icon group.
	 * @see #setIconGroup
	 */
	public static IconGroup getIconGroup() {
		return iconGroup;
	}


	/**
	 * Returns the color used in "mark all."
	 *
	 * @return The color.
	 * @see #setMarkAllHighlightColor
	 */
	public Color getMarkAllHighlightColor() {
		return (Color)markAllHighlightPainter.getPaint();
	}


	/**
	 * Returns the maximum ascent of all fonts used in this text area.  In
	 * the case of a standard <code>RTextArea</code>, this is simply the
	 * ascent of the current font.<p>
	 *
	 * This value could be useful, for example, to implement a line-numbering
	 * scheme.
	 *
	 * @return The ascent of the current font.
	 */
	public int getMaxAscent() {
		return getFontMetrics(getFont()).getAscent();
	}


	/**
	 * Returns the text mode this editor pane is currently in.
	 *
	 * @return Either <code>RTextArea.INSERT_MODE</code> or
	 *         <code>RTextArea.OVERWRITE_MODE</code>.
	 */
	public final int getTextMode() {
		return textMode;
	}


	/**
	 * Does the actual dirtywork of replacing the selected text in this
	 * text area (i.e., in its document).  This method provides a hook for
	 * subclasses to handle this in a different way.
	 *
	 * @param content The content to add.
	 */
	protected void handleReplaceSelection(String content) {
		Document doc = getDocument();
		if (doc != null) {
			try {
				Caret c = getCaret();
				int dot = c.getDot();
				int mark = c.getMark();
				int p0 = Math.min(dot, mark);
				int p1 = Math.max(dot, mark);
				((RTextAreaDocument)doc).replace(p0, p1 - p0,
										content, null);
			} catch (BadLocationException e) {
				UIManager.getLookAndFeel().
						provideErrorFeedback(RTextArea.this);
			}
		}
	}


	private static boolean first = false;
	/**
	 * Creates and localizes our shared actions.
	 *
	 * @param ta The text area being created.
	 */
	private static final synchronized void initActions(RTextArea ta) {

		// We only need to initialize the actions for the first created
		// text area, since they're shared among all text areas.
		if (!first) {

			first = true;

			ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);
			cantUndoText = bundle.getString("CantUndo");
			cantRedoText = bundle.getString("CantRedo");

			// Create actions for right-click popup menu.
			// 1.5.2004/pwy: Replaced the CTRL_MASK with the cross-platform version...
			int defaultModifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

			String name = bundle.getString("CutActionName");
			char mnemonic = bundle.getString("CutActionMnemonic").charAt(0);
			cutAction = new RTextAreaEditorKit.CutAction(name, null, name,
				new Integer(mnemonic), KeyStroke.getKeyStroke(KeyEvent.VK_X, defaultModifier));

			name = bundle.getString("CopyActionName");
			mnemonic = bundle.getString("CopyActionMnemonic").charAt(0);
			copyAction = new RTextAreaEditorKit.CopyAction(name, null, name,
				new Integer(mnemonic), KeyStroke.getKeyStroke(KeyEvent.VK_C, defaultModifier));

			name = bundle.getString("PasteActionName");
			mnemonic = bundle.getString("PasteActionMnemonic").charAt(0);
			pasteAction = new RTextAreaEditorKit.PasteAction(name, null, name,
				new Integer(mnemonic), KeyStroke.getKeyStroke(KeyEvent.VK_V, defaultModifier));

			name = bundle.getString("DeleteActionName");
			mnemonic = bundle.getString("DeleteActionMnemonic").charAt(0);
			deleteAction = new RTextAreaEditorKit.DeleteNextCharAction(name, null, name,
				new Integer(mnemonic), KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

			undoAction = new RTextAreaEditorKit.UndoAction(cantUndoText, null, "Undo",
				new Integer(KeyEvent.VK_Z), KeyStroke.getKeyStroke(KeyEvent.VK_Z, defaultModifier));

			redoAction = new RTextAreaEditorKit.RedoAction(cantRedoText, null, "Redo",
				new Integer(KeyEvent.VK_Y), KeyStroke.getKeyStroke(KeyEvent.VK_Y, defaultModifier));

			name = bundle.getString("SAActionName");
			mnemonic = bundle.getString("SAActionMnemonic").charAt(0);
			selectAllAction = new RTextAreaEditorKit.SelectAllAction(name, null, name,
				new Integer(mnemonic), KeyStroke.getKeyStroke(KeyEvent.VK_A, defaultModifier));

			bundle = null;

		} // End of if (!first).

	}


	/**
	 * Returns whether or not RText is in the middle of an undo or redo
	 * operation.  This method is package-private so that
	 * <code>ConfigurableCaret</code> can know whether an insert or
	 * removal is part of an undo or redo.  This method is also a giant HACK
	 * to get around the fact that there is no way for a Caret to know that
	 * an event is coming from an undo/redo unless the DocumentEvent passed
	 * to it says so.  In J2SE5.0, DefaultCaret knows because the event is of
	 * type UndoRedoDocumentEvent, but that class is (of course) package-
	 * private, so we can't get to it.  So we work around it by having the
	 * caret poll the text area directly.
	 *
	 *@return Whether the text area is currently doing an undo or redo.
	 */
	boolean inUndoRedo() {
		return inUndoRedo;
	}


	/**
	 * Returns whether or not a macro is being recorded.
	 *
	 * @return Whether or not a macro is being recorded.
	 * @see #beginRecordingMacro
	 * @see #endRecordingMacro
	 */
	public static synchronized boolean isRecordingMacro() {
		return recordingMacro;
	}


	/**
	 * Loads a macro to be used by all <code>RTextArea</code>s in the current
	 * application.
	 *
	 * @param macro The macro to load.
	 * @see #getCurrentMacro
	 */
	public static synchronized void loadMacro(Macro macro) {
		currentMacro = macro;
	}


	/**
	 * Marks all instances of the specified text in this text area.
	 *
	 * @param toMark The text to mark.
	 * @param matchCase Whether the match should be case-sensitive.
	 * @param wholeWord Whether the matches should be surrounded by spaces
	 *        or tabs.
	 * @param regex Whether <code>toMark</code> is a Java regular expression.
	 * @return The number of matches marked.
	 * @see #clearMarkAllHighlights
	 * @see #getMarkAllHighlightColor
	 * @see #setMarkAllHighlightColor
	 */
	public int markAll(String toMark, boolean matchCase, boolean wholeWord,
					boolean regex) {
		Highlighter h = getHighlighter();
		int numMarked = 0;
		if (toMark!=null && !toMark.equals(markedWord) && h!=null) {
			if (markAllHighlights!=null)
				clearMarkAllHighlights();
			else
				markAllHighlights = new ArrayList(10);
			int caretPos = getCaretPosition();
			markedWord = toMark;
			setCaretPosition(0);
			boolean found = SearchEngine.find(this, toMark, true, matchCase,
										wholeWord, regex);
			while (found) {
				int start = getSelectionStart();
				int end = getSelectionEnd();
				try {
					markAllHighlights.add(h.addHighlight(start, end,
										markAllHighlightPainter));
				} catch (BadLocationException ble) {
					ble.printStackTrace();
				}
				numMarked++;
				found = SearchEngine.find(this, toMark, true,
									matchCase, wholeWord, regex);
			}
			setCaretPosition(caretPos);
			repaint();
		}
		return numMarked;
	}


	/**
	 * "Plays back" the last recorded macro in this text area.
	 */
	public synchronized void playbackLastMacro() {
		if (currentMacro!=null) {
			Action[] actions = getActions();
			int numActions = actions.length;
			List macroRecords = currentMacro.getMacroRecords();
			int num = macroRecords.size();
			if (num>0) {
				undoManager.beginInternalAtomicEdit();
				try {
					for (int i=0; i<num; i++) {
						MacroRecord record = (MacroRecord)macroRecords.get(i);
						for (int j=0; j<numActions; j++) {
							if ((actions[j] instanceof RecordableTextAction) &&
								record.id.equals(
								((RecordableTextAction)actions[j]).getMacroID())) {
								actions[j].actionPerformed(
									new ActionEvent(this,
												ActionEvent.ACTION_PERFORMED,
												record.actionCommand));
								break;
							}
						}
					}
				} finally {
					undoManager.endInternalAtomicEdit();
				}
			}
		}
	}


	/**
	 * Method called when it's time to print this badboy (the oldschool,
	 * AWT way).
	 *
	 * @param g The context into which the page is drawn.
	 * @param pageFormat The size and orientation of the page being drawn.
	 * @param pageIndex The zero based index of the page to be drawn.
	 */
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		return RPrintUtilities.printDocumentWordWrap(g, this, getFont(), pageIndex, pageFormat, getTabSize());
	}


	/**
	 * We override this method because the super version gives us an entirely
	 * new <code>Document</code>, thus requiring us to re-attach our Undo
	 * manager.  With this version we just replace the text.
	 */
	public void read(Reader in, Object desc) throws IOException {

		RTextAreaEditorKit kit = (RTextAreaEditorKit)getUI().
											getEditorKit(this);
		setText(null);
		Document doc = getDocument();
		if (desc != null)
			doc.putProperty(Document.StreamDescriptionProperty, desc);
		try {
			// NOTE:  Resets the "line terminator" property of the document.
			kit.read(in, doc, 0);
		} catch (BadLocationException e) {
			throw new IOException(e.getMessage());
		}

	}


	// Attempt to redo an "action" from the RTextEditorPane.
	public void redoLastAction() {

		// NOTE:  The try/catch block shouldn't be necessary...
		try {
			if (undoManager.canRedo())
				undoManager.redo();
		} catch (CannotRedoException f) {
			f.printStackTrace();
		}

	}


	/**
	 * Replaces text from the indicated start to end position with the
	 * new text specified.  Does nothing if the model is null.  Simply
	 * does a delete if the new string is null or empty.
	 * <p>
	 * This method is thread safe, although most Swing methods
	 * are not.<p>
	 * This method is overridden so that our Undo manager remembers it as a
	 * single operation (it has trouble with this, especially for
	 * <code>RSyntaxTextArea</code> and the "auto-indent" feature).
	 *
	 * @param str the text to use as the replacement
	 * @param start the start position >= 0
	 * @param end the end position >= start
	 * @exception IllegalArgumentException  if part of the range is an
	 *  invalid position in the model
	 * @see #insert
	 * @see #replaceRange
	 */
	public void replaceRange(String str, int start, int end) {
		if (end < start)
			throw new IllegalArgumentException("end before start");
		Document doc = getDocument();
		if (doc != null) {
			try {
				// Without this, in some cases we'll have to do two undos
				// for one logical operation (for example, try editing a
				// Java source file in an RSyntaxTextArea, and moving a line
				// with text already on it down via Enter.  Without this
				// line, doing a single "undo" moves all later text up,
				// but the first line moved down isn't there!  Doing a
				// second undo puts it back.
				undoManager.beginInternalAtomicEdit();
				((RTextAreaDocument)doc).replace(start, end - start,
                               		                     str, null);
			} catch (BadLocationException e) {
				throw new IllegalArgumentException(e.getMessage());
			} finally {
				undoManager.endInternalAtomicEdit();
			}
		}
    }


	/**
	 * This method overrides <code>JTextComponent</code>'s
	 * <code>replaceSelection</code>, so that if <code>textMode</code> is
	 * <code>OVERWRITE_MODE</code>, it actually overwrites.
	 *
	 * @param text The content to replace the selection with.
	 */
	public void replaceSelection(String text) {

		// It's legal for null to be used here...
		if (text==null) {
			handleReplaceSelection(text);
			return;
		}

		if (getTabsEmulated() && text.indexOf('\t')>-1) {
			text = replaceTabsWithSpaces(text);
		}

		// If the user wants to overwrite text...
		if (textMode==OVERWRITE_MODE) {

			Caret caret = getCaret();
			int caretPos = caret.getDot();
			Document doc = getDocument();
			Element map = doc.getDefaultRootElement();
			int curLine = map.getElementIndex(caretPos);
			int lastLine = map.getElementCount() - 1;

			try {

				// If the user hit Enter, just go to the next line.
				if (text.equals("\n")) {

					if (curLine==lastLine) {
						setCaretPosition(getLineEndOffset(curLine));
						// Okay because undoManager will still work, as
						// it's definitely not a replace.
						handleReplaceSelection(text);
					}
					else {
						setCaretPosition(getLineStartOffset(curLine+1));
					}

					return;

				} // End of if (text.equals("\n")).

				// If we're not at the end of a line, select the characters
				// that will be overwritten (otherwise JTextArea will simply
				// insert in front of them).
				int curLineEnd = getLineEndOffset(curLine);
				if (caretPos==caret.getMark() && caretPos!=curLineEnd) {//!getText(caretPos,1).equals("\n")) {
					if (curLine==lastLine)
						caretPos = Math.min(caretPos+text.length(), curLineEnd);
					else
						caretPos = Math.min(caretPos+text.length(), curLineEnd-1);
					caret.moveDot(caretPos);//moveCaretPosition(caretPos);
				}

			} catch (BadLocationException ble) {
				/* This should never happen. */
				UIManager.getLookAndFeel().provideErrorFeedback(RTextArea.this);
				ble.printStackTrace();
			}

		} // End of if (textMode==OVERWRITE_MODE).

		// Now, actually do the inserting/replacing.  Our undoManager will
		// take care of remembering the remove/insert as atomic if we are in
		// overwrite mode.
		handleReplaceSelection(text);

	}


	private StringBuffer repTabsSB;
	/**
	 * Replaces all instances of the tab character in <code>text</code> with
	 * the number of spaces equivalent to a tab in this text area.<p>
	 *
	 * This method should only be called from threadsafe methods, such as
	 * {@link replaceSelection(String)}.
	 *
	 * @param text The <code>java.lang.String</code> in which to replace tabs
	 *        with spaces.  This has already been verified to have at least
	 *        one tab character in it.
	 * @return A <code>java.lang.String</code> just like <code>text</code>,
	 *         but with spaces instead of tabs.
	 */
	private final String replaceTabsWithSpaces(final String text) {

		String tabText = "";
		int temp = getTabSize();
		for (int i=0; i<temp; i++) {
			tabText += ' ';
		}

		// Common case: User's entering a single tab (pressed the tab key).
		if (text.length()==1) {
			return tabText;
		}

		// Otherwise, there may be more than one tab.  Manually search for
		// tabs for performance, as opposed to using String#replaceAll().
		// This method is called for each character inserted when "replace
		// tabs with spaces" is enabled, so we need to be quick.

		//return text.replaceAll("\t", tabText);
		if (repTabsSB==null) {
			repTabsSB = new StringBuffer();
		}
		repTabsSB.setLength(0);
		char[] array = text.toCharArray(); // Wouldn't be needed in 1.5!
		int oldPos = 0;
		int pos = 0;
		while ((pos=text.indexOf('\t', oldPos))>-1) {
			//repTabsSB.append(text, oldPos, pos); // Added in Java 1.5
			if (pos>oldPos) {
				repTabsSB.append(array, oldPos, pos-oldPos);
			}
			repTabsSB.append(tabText);
			oldPos = pos + 1;
		}
		if (oldPos<array.length) {
			repTabsSB.append(array, oldPos, array.length-oldPos);
		}

		return repTabsSB.toString();

	}



	/**
	 * Sets the properties of one of the actions this text area owns.
	 *
	 * @param action The action to modify; for example,
	 *        <code>CUT_ACTION</code>.
	 * @param name The new name for the action.
	 * @param mnemonic The new mnemonic for the action.
	 * @param accelerator The new accelerator key for the action.
	 */
	public static void setActionProperties(int action, String name,
							char mnemonic, KeyStroke accelerator) {
		setActionProperties(action, name, new Integer(mnemonic), accelerator);
	}


	/**
	 * Sets the properties of one of the actions this text area owns.
	 *
	 * @param action The action to modify; for example,
	 *        <code>CUT_ACTION</code>.
	 * @param name The new name for the action.
	 * @param mnemonic The new mnemonic for the action.
	 * @param accelerator The new accelerator key for the action.
	 */
	public static void setActionProperties(int action, String name,
							Integer mnemonic, KeyStroke accelerator) {

		Action tempAction = null;

		switch (action) {
			case CUT_ACTION:
				tempAction = cutAction;
				break;
			case COPY_ACTION:
				tempAction = copyAction;
				break;
			case PASTE_ACTION:
				tempAction = pasteAction;
				break;
			case DELETE_ACTION:
				tempAction = deleteAction;
				break;
			case SELECT_ALL_ACTION:
				tempAction = selectAllAction;
				break;
			case UNDO_ACTION:
			case REDO_ACTION:
			default:
				return;
		}

		tempAction.putValue(Action.NAME, name);
		tempAction.putValue(Action.SHORT_DESCRIPTION, name);
		tempAction.putValue(Action.ACCELERATOR_KEY, accelerator);
		tempAction.putValue(Action.MNEMONIC_KEY, mnemonic);

	}


	/**
	 * This method is overridden to make sure that instances of
	 * <code>RTextArea</code> only use <code>ConfigurableCaret</code>s.
	 * To set the style of caret (vertical line, block, etc.) used for
	 * insert or overwrite mode, use <code>setCaretStyle</code>.
	 *
	 * @param caret The caret to use.  If this is not an instance of
	 *        <code>ConfigurableCaret</code>, an exception is thrown.
	 * @throws IllegalArgumentException If the specified caret is not an
	 *         <code>ConfigurableCaret</code>.
	 * @see #setCaretStyle
	 */
	public void setCaret(Caret caret) {
		if (!(caret instanceof ConfigurableCaret))
			throw new IllegalArgumentException(
						"RTextArea needs ConfigurableCaret");
		super.setCaret(caret);
	}


	/**
	 * Sets the style of caret used when in insert or overwrite mode.
	 *
	 * @param mode Either <code>INSERT_MODE</code> or
	 *        <code>OVERWRITE_MODE</code>.
	 * @param style The style for the caret (such as
	 *        <code>ConfigurableCaret.VERTICAL_LINE_STYLE</code>).
	 * @see org.fife.ui.rtextarea.ConfigurableCaret
	 */
	public void setCaretStyle(int mode, int style) {
		style = (style>=ConfigurableCaret.MIN_STYLE &&
					style<=ConfigurableCaret.MAX_STYLE ?
						style :
						ConfigurableCaret.VERTICAL_LINE_STYLE);
		carets[mode] = style;
		if (mode==getTextMode())
			// Will repaint the caret if necessary.
			((ConfigurableCaret)getCaret()).setStyle(style);
	}


	/**
	 * Sets the document used by this text area.
	 *
	 * @param document The new document to use.
	 * @throws IllegalArgumentException If the document is not an instance of
	 *         <code>RTextAreaDocument</code>.
	 */
	public void setDocument(Document document) {
		if (!(document instanceof RTextAreaDocument))
			throw new IllegalArgumentException("RTextArea requires " +
				"instances of RTextAreaDocument for its document!");
		super.setDocument(document);
	}


	/**
	 * Sets the path in which to find images to associate with the editor's
	 * actions.  The path MUST contain the following images (with the
	 * appropriate extension as defined by the icon group):<br>
	 * <ul>
	 *   <li>cut</li>
	 *   <li>copy</li>
	 *   <li>paste</li>
	 *   <li>delete</li>
	 *   <li>undo</li>
	 *   <li>redo</li>
	 *   <li>selectall</li>
	 * </ul>
	 * If any of the above images don't exist, the corresponding action will
	 * not have an icon.
	 *
	 * @param group The icon group to load.
	 * @see #getIconGroup
	 */
	public static synchronized void setIconGroup(IconGroup group) {
		Icon icon = group.getIcon("cut");
		cutAction.putValue(Action.SMALL_ICON, icon);
		icon = group.getIcon("copy");
		copyAction.putValue(Action.SMALL_ICON, icon);
		icon = group.getIcon("paste");
		pasteAction.putValue(Action.SMALL_ICON, icon);
		icon = group.getIcon("delete");
		deleteAction.putValue(Action.SMALL_ICON, icon);
		icon = group.getIcon("undo");
		undoAction.putValue(Action.SMALL_ICON, icon);
		icon = group.getIcon("redo");
		redoAction.putValue(Action.SMALL_ICON, icon);
		icon = group.getIcon("selectall");
		selectAllAction.putValue(Action.SMALL_ICON, icon);
		iconGroup = group;
	}


	/**
	 * Sets the color used for "mark all."  This fires a property change of
	 * type <code>MARK_ALL_COLOR_PROPERTY</code>.
	 *
	 * @param color The color to use for "mark all."
	 * @see #getMarkAllHighlightColor
	 */
	public void setMarkAllHighlightColor(Color color) {
		Color old = (Color)markAllHighlightPainter.getPaint();
		if (old!=null && !old.equals(color)) {
			markAllHighlightPainter.setPaint(color);
			if (markedWord!=null)
				repaint();	// Repaint if words are highlighted.
			firePropertyChange(MARK_ALL_COLOR_PROPERTY, old, color);
		}
	}


	/**
	 * Sets whether the edges of selections are rounded in this text area.
	 * This method fires a property change of type
	 * <code>ROUNDED_SELECTION_PROPERTY</code>.
	 *
	 * @param rounded Whether selection edges should be rounded.
	 * @see #getRoundedSelectionEdges
	 */
	public void setRoundedSelectionEdges(boolean rounded) {
		if (getRoundedSelectionEdges()!=rounded) {
			markAllHighlightPainter.setRoundedEdges(rounded);
			super.setRoundedSelectionEdges(rounded); // Fires event.
		}
	}


	/**
	 * Sets the text mode for this editor pane.
	 *
	 * @param mode Either <code>RTextArea.INSERT_MODE</code> or
	 *        <code>RTextArea.OVERWRITE_MODE</code>.
	 */
	public void setTextMode(int mode) {

		if (mode!=INSERT_MODE && mode!=OVERWRITE_MODE)
			mode = INSERT_MODE;

		if (textMode != mode) {
			ConfigurableCaret cc = (ConfigurableCaret)getCaret();
			cc.setStyle(carets[mode]);
			textMode = mode;
		}

	}


	/**
	 * Sets the UI used by this text area.  This is overridden so only the
	 * right-click popup menu's UI is updated.  The look and feel of an
	 * <code>RTextArea</code> is independent of the Java Look and Feel, and so
	 * this method does not change the text area itself.  Subclasses (such as
	 * <code>RSyntaxTextArea</code> can call <code>setRTextAreaUI</code> if
	 * they wish to install a new UI.
	 *
	 * FIXME:  This method will be called once for each open
	 * <code>RTextArea</code>, when it only needs to be called once!
	 *
	 * @param ui This parameter is ignored.
	 */
	public final void setUI(TextUI ui) {
		// Update the popup menu's ui.
		if (RTextArea.rightClickMenu!=null)
			SwingUtilities.updateComponentTreeUI(RTextArea.rightClickMenu);
	}


	/**
	 * Attempt to undo an "action" done in this text area.
	 */
	public void undoLastAction() {

		// NOTE: that the try/catch block shouldn't be necessary...

		try {
			if (undoManager.canUndo())
				undoManager.undo();
		}
		catch (CannotUndoException f) {
			JOptionPane.showMessageDialog(this, "Error doing Undo: " + f +
					"\nPlease report this at " +
					"http://sourceforge.net/projects/rtext",
					"rtext - Error", JOptionPane.ERROR_MESSAGE);
		}

	}


	/**
	 * Modified from <code>MutableCaretEvent</code> in
	 * <code>JTextComponent</code> so that mouse events get fired when the user
	 * is selecting text with the mouse as well.  This class also displays the
	 * popup menu when the user right-clicks in the text area.
	 */
	protected class RTextAreaMutableCaretEvent extends RTAMouseListener {

		protected RTextAreaMutableCaretEvent(RTextArea textArea) {
			super(textArea);
		}

		public void focusGained(FocusEvent e) {
			Caret c = getCaret();
			boolean enabled = c.getDot()!=c.getMark();
			cutAction.setEnabled(enabled);
			copyAction.setEnabled(enabled);
			undoManager.updateActions(); // To reflect this text area.
		}

		public void focusLost(FocusEvent e) {
		}

		public void mouseDragged(MouseEvent e) {
			if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
				Caret caret = getCaret();
				dot = caret.getDot();
				mark = caret.getMark();
				fireCaretUpdate(this);
			}
		}

		public void mousePressed(MouseEvent e) {
			// WORKAROUND:  Since JTextComponent only updates the caret
			// location on mouse clicked and released, we'll do it on dragged
			// events when the left mouse button is clicked.
			if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
				Caret caret = getCaret();
				dot = caret.getDot();
				mark = caret.getMark();
				fireCaretUpdate(this);
			}
		}

		public void mouseReleased(MouseEvent e) {
			if ((e.getModifiers()&MouseEvent.BUTTON3_MASK)!=0)
				showPopup(e);
		}

		/**
		 * Shows a popup menu with cut, copy, paste, etc. options if the
		 * user clicked the right button.
		 *
		 * @param e The mouse event that caused this method to be called.
		 */
		private void showPopup(MouseEvent e) {
			if (RTextArea.rightClickMenu==null)
				createRightClickMenu();
			RTextArea.rightClickMenu.show(e.getComponent(),
										e.getX(), e.getY());
		}

	}


	/**
	 * This class manages undos/redos for a particular editor pane.  It groups
	 * all undos that occur one character position apart together, to avoid
	 * Java's horrible "one character at a time" undo behavior.  It also
	 * recognizes "replace" actions (i.e., text is selected, then the user
	 * types), and treats it as a single action, instead of a remove/insert
	 * action pair.
	 */
	class RUndoManager extends UndoManager {

		public RCompoundEdit compoundEdit;
		private RTextArea textArea;
		private int lastOffset;
		private boolean internalAtomicEdit;

		public RUndoManager(RTextArea textArea) {
			this.textArea = textArea;
		}

		/**
		 * Begins an "atomic" edit.  This method is called when RTextArea
		 * KNOWS that some edits should be compound automatically, such as
		 * when the user is typing in overwrite mode (the deletion of the
		 * current char + insertion of the new one) or the playing back of a
		 * macro.
		 */
		public void beginInternalAtomicEdit() {
			if (compoundEdit!=null)
				compoundEdit.end();
			compoundEdit = new RCompoundEdit();
			internalAtomicEdit = true;
		}

		/**
		 * Ends an "atomic" edit.
		 */
		public void endInternalAtomicEdit() {
			addEdit(compoundEdit);
			compoundEdit.end();
			compoundEdit = null;
			internalAtomicEdit = false;
			updateActions();	// Needed to show the new display name.
		}

		public void undoableEditHappened(UndoableEditEvent e) {

			// This happens when the first undoable edit occurs, and
			// just after an undo.  So, we need to update our actions.
			if (compoundEdit==null) {
				compoundEdit = startCompoundEdit(e.getEdit());
				updateActions();
				return;
			}

			else if (internalAtomicEdit==true) {
				compoundEdit.addEdit(e.getEdit());
				return;
			}

			// This happens when there's already an undo that has occured.
			// Test to see if these undos are on back-to-back characters,
			// and if they are, group them as a single edit.  Since an
			// undo has already occured, there is no need to update our
			// actions here.
			int diff = textArea.getCaretPosition() - lastOffset;
			// "<=1" allows contiguous "overwrite mode" keypresses to be
			// grouped together.
			if (Math.abs(diff)<=1) {//==1) {
				compoundEdit.addEdit(e.getEdit());
				lastOffset += diff;
				//updateActions();
				return;
			}

			// This happens when this undoableedit didn't occur at the
			// character just after the presious undlabeledit.  Since an
			// undo has already occured, there is no need to update our
			// actions here either.
			compoundEdit.end();
			compoundEdit = startCompoundEdit(e.getEdit());
			//updateActions();

		}

		private RCompoundEdit startCompoundEdit(UndoableEdit edit) {
			lastOffset = textArea.getCaretPosition();
			compoundEdit = new RCompoundEdit();
			compoundEdit.addEdit(edit);
			addEdit(compoundEdit);
			return compoundEdit;
		}

		public void undo() throws CannotUndoException {
			inUndoRedo = true;
			super.undo();
			updateActions();
			inUndoRedo = false;
		}

		public void redo() throws CannotRedoException {
			inUndoRedo = true;
			super.redo();
			updateActions();
			inUndoRedo = false;
		}

		/**
		 * Ensures that undo/redo actions are enabled appropriately and have
		 * descriptive text at all times.
		 */
		public void updateActions() {

			String text;

			if (canUndo()==true) {
				undoAction.setEnabled(true);
				text = getUndoPresentationName();
undoAction.putValue(Action.NAME, text);
undoAction.putValue(Action.SHORT_DESCRIPTION, text);
			}
			else {
if (undoAction.isEnabled()) {
				undoAction.setEnabled(false);
				text = cantUndoText;
undoAction.putValue(Action.NAME, text);
undoAction.putValue(Action.SHORT_DESCRIPTION, text);
}
			}
//			undoAction.putValue(Action.NAME, text);
//			undoAction.putValue(Action.SHORT_DESCRIPTION, text);

			if (canRedo()==true) {
				redoAction.setEnabled(true);
				text = getRedoPresentationName();
redoAction.putValue(Action.NAME, text);
redoAction.putValue(Action.SHORT_DESCRIPTION, text);
			}
			else {
if (redoAction.isEnabled()) {
				redoAction.setEnabled(false);
				text = cantRedoText;
redoAction.putValue(Action.NAME, text);
redoAction.putValue(Action.SHORT_DESCRIPTION, text);
}
			}
//			redoAction.putValue(Action.NAME, text);
//			redoAction.putValue(Action.SHORT_DESCRIPTION, text);

		}

		// Action used by RUndoManager.  
		class RCompoundEdit extends CompoundEdit {

			public String getUndoPresentationName() {
				return UIManager.getString("AbstractUndoableEdit.undoText");
			}

			public String getRedoPresentationName() {
				return UIManager.getString("AbstractUndoableEdit.redoText");
			}

			public boolean isInProgress() {
				return false;
			}

			public void undo() throws CannotUndoException {
				if (compoundEdit!=null)
					compoundEdit.end();
				super.undo();
				compoundEdit = null;
			}

		}

	}


}