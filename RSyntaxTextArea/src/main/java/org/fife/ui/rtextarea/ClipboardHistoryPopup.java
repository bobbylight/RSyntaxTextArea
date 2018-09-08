/*
 * 08/16/2014
 *
 * ClipboardHistoryPopup.java - Shows clipboard history in a popup window.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.Caret;

import org.fife.ui.rsyntaxtextarea.focusabletip.TipUtil;


/**
 * A popup window that displays the most recent snippets added to the clipboard
 * of an <code>RSyntaxTextArea</code>.  Selecting one pastes that snippet.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class ClipboardHistoryPopup extends JWindow {

	private RTextArea textArea;

	private ChoiceList list;
	private transient Listener listener;
	private boolean prevCaretAlwaysVisible;

	/**
	 * The space between the caret and the completion popup.
	 */
	private static final int VERTICAL_SPACE			= 1;

	private static final String MSG	= "org.fife.ui.rtextarea.RTextArea";


	/**
	 * Constructor.
	 *
	 * @param parent The parent window containing <code>textArea</code>.
	 * @param textArea The text area to paste into.
	 */
	ClipboardHistoryPopup(Window parent, RTextArea textArea) {

		super(parent);
		this.textArea = textArea;

		JPanel cp = new JPanel(new BorderLayout());
		cp.setBorder(BorderFactory.createCompoundBorder(
				org.fife.ui.rsyntaxtextarea.focusabletip.TipUtil.getToolTipBorder(),
				BorderFactory.createEmptyBorder(2, 5, 5, 5)));
		cp.setBackground(org.fife.ui.rsyntaxtextarea.focusabletip.TipUtil.getToolTipBackground());
		setContentPane(cp);

		ResourceBundle msg = ResourceBundle.getBundle(MSG);
		JLabel title = new JLabel(
				msg.getString("Action.ClipboardHistory.Popup.Label"));
		cp.add(title, BorderLayout.NORTH);

		list = new ChoiceList();
		JScrollPane sp = new JScrollPane(list);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		cp.add(sp);

		installKeyBindings();
		listener = new Listener();
		setLocation();

	}


	/**
	 * Overridden to ensure this popup stays in a specific size range.
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension size = super.getPreferredSize();
		if (size!=null) {
			size.width = Math.min(size.width, 300);
			size.width = Math.max(size.width, 200);
		}
		return size;
	}


	/**
	 * Inserts the selected item into the editor and disposes of this popup.
	 */
	private void insertSelectedItem() {
		Object lvp = list.getSelectedValue();
		if (lvp!=null) {
			listener.uninstallAndHide();
			String text = ((LabelValuePair)lvp).value;
			textArea.replaceSelection(text);
			ClipboardHistory.get().add(text); // Move this item to the top
		}
	}


	/**
	 * Adds key bindings to this popup.
	 */
	private void installKeyBindings() {

		InputMap im = getRootPane().getInputMap(
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap am = getRootPane().getActionMap();

		KeyStroke escapeKS = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		im.put(escapeKS, "onEscape");
		am.put("onEscape", new EscapeAction());
		list.getInputMap().remove(escapeKS);
	}


	public void setContents(List<String> contents) {
		list.setContents(contents);
		// Must re-size since we now have data!
		pack();
	}


	/**
	 * Positions this popup to be in the top right-hand corner of the parent
	 * editor.
	 */
	private void setLocation() {

		Rectangle r = null;
		try {
			r = textArea.modelToView(textArea.getCaretPosition());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		Point p = r.getLocation();
		SwingUtilities.convertPointToScreen(p, textArea);
		r.x = p.x;
		r.y = p.y;

		Rectangle screenBounds = TipUtil.getScreenBoundsForPoint(r.x, r.y);
		//Dimension screenSize = getToolkit().getScreenSize();

		int totalH = getHeight();

		// Try putting our stuff "below" the caret first.  We assume that the
		// entire height of our stuff fits on the screen one way or the other.
		int y = r.y + r.height +  VERTICAL_SPACE;
		if (y+totalH>screenBounds.height) {
			y = r.y - VERTICAL_SPACE - getHeight();
		}

		// Get x-coordinate of completions.  Try to align left edge with the
		// caret first.
		int x = r.x;
		if (!textArea.getComponentOrientation().isLeftToRight()) {
			x -= getWidth(); // RTL => align right edge
		}
		if (x<screenBounds.x) {
			x = screenBounds.x;
		}
		else if (x+getWidth()>screenBounds.x+screenBounds.width) { // completions don't fit
			x = screenBounds.x + screenBounds.width - getWidth();
		}

		setLocation(x, y);

	}


	@Override
	public void setVisible(boolean visible) {
		if (list.getModel().getSize()==0) {
			UIManager.getLookAndFeel().provideErrorFeedback(textArea);
			return;
		}
		super.setVisible(visible);
		updateTextAreaCaret(visible);
		if (visible) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					requestFocus();
					if (list.getModel().getSize()>0) {
						list.setSelectedIndex(0);
					}
					list.requestFocusInWindow();
				}
			});
		}
	}


	/**
	 * (Possibly) toggles the "always visible" state of the text area's
	 * caret.
	 *
	 * @param visible Whether this popup window was just made visible (as
	 *        opposed to hidden).
	 */
	private void updateTextAreaCaret(boolean visible) {
		Caret caret = textArea.getCaret();
		if (caret instanceof ConfigurableCaret) { // Always true by default
			ConfigurableCaret cc = (ConfigurableCaret)caret;
			if (visible) {
				prevCaretAlwaysVisible = cc.isAlwaysVisible();
				cc.setAlwaysVisible(true);
			}
			else {
				cc.setAlwaysVisible(prevCaretAlwaysVisible);
			}
		}
	}


	/**
	 * Action performed when Escape is pressed in this popup.
	 */
	private class EscapeAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			listener.uninstallAndHide();
		}

	}


	/**
	 * Listens for events in this popup.
	 */
	private class Listener extends WindowAdapter implements ComponentListener {

		Listener() {

			addWindowFocusListener(this);
			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount()==2) {
						insertSelectedItem();
					}
				}
			});
			list.getInputMap().put(
					KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "onEnter");
			list.getActionMap().put("onEnter", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					insertSelectedItem();
				}
			});

			// If anything happens to the "parent" window, hide this popup
			Window parent = (Window)getParent();
			parent.addWindowFocusListener(this);
			parent.addWindowListener(this);
			parent.addComponentListener(this);

		}

		@Override
		public void componentResized(ComponentEvent e) {
			uninstallAndHide();
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			uninstallAndHide();
		}

		@Override
		public void componentShown(ComponentEvent e) {
			uninstallAndHide();
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			uninstallAndHide();
		}

		@Override
		public void windowActivated(WindowEvent e) {
			checkForParentWindowEvent(e);
		}

		@Override
		public void windowLostFocus(WindowEvent e) {
			if (e.getSource()==ClipboardHistoryPopup.this) {
				uninstallAndHide();
			}
		}

		@Override
		public void windowIconified(WindowEvent e) {
			checkForParentWindowEvent(e);
		}

		private boolean checkForParentWindowEvent(WindowEvent e) {
			if (e.getSource()==getParent()) {
				uninstallAndHide();
				return true;
			}
			return false;
		}

		private void uninstallAndHide() {
			Window parent = (Window)getParent();
			parent.removeWindowFocusListener(this);
			parent.removeWindowListener(this);
			parent.removeComponentListener(this);
			removeWindowFocusListener(this);
			setVisible(false);
			dispose();
		}

	}


	/**
	 * The list component used in this popup.
	 */
	private static final class ChoiceList extends JList {

		private ChoiceList() {
			super(new DefaultListModel());
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			installKeyboardActions();
		}

		private void installKeyboardActions() {

			InputMap im = getInputMap();
			ActionMap am = getActionMap();

			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "onDown");
			am.put("onDown", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = (getSelectedIndex()+1) % getModel().getSize();
					ensureIndexIsVisible(index);
					setSelectedIndex(index);
				}
			});

			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "onUp");
			am.put("onUp", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int index = getSelectedIndex() - 1;
					if (index < 0) {
						index += getModel().getSize();
					}
					ensureIndexIsVisible(index);
					setSelectedIndex(index);
				}
			});

		}

		private void setContents(List<String> contents) {
			DefaultListModel model = (DefaultListModel)getModel();
			model.clear();
			for (String str : contents) {
				model.addElement(new LabelValuePair(str));
			}
			setVisibleRowCount(Math.min(model.getSize(), 8));
		}

	}


	/**
	 * Entries in the choices list are of this type.  This truncates entries
	 * that are too long.  In the future it can provide more information
	 * (line count for multi-line pastes, etc.).
	 */
	private static class LabelValuePair {

		private String label;
		private String value;

		private static final int LABEL_MAX_LENGTH = 50;

		LabelValuePair(String value) {
			this.label = this.value = value;
			int newline = label.indexOf('\n');
			boolean multiLine = false;
			if (newline>-1) {
				label = label.substring(0, newline);
				multiLine = true;
			}
			if (label.length()>LABEL_MAX_LENGTH) {
				label = label.substring(0, LABEL_MAX_LENGTH) + "...";
			}
			else if (multiLine) {
				int toRemove = 3 - (LABEL_MAX_LENGTH - label.length());
				if (toRemove>0) {
					label = label.substring(0, label.length()-toRemove);
				}
				label += "...";
			}
		}

		@Override
		public String toString() {
			return label;
		}

	}


}
