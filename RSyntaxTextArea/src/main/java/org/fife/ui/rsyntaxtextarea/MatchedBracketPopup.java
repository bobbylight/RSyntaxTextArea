/*
 * 07/03/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.focusabletip.TipUtil;


/**
 * A tool tip-like popup that shows the line of code containing the bracket
 * matched to that at the caret position, if it is scrolled out of the user's
 * viewport.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class MatchedBracketPopup extends JWindow {

	private RSyntaxTextArea textArea;

	private transient Listener listener;

	private static final int LEFT_EMPTY_BORDER = 5;


	MatchedBracketPopup(Window parent, RSyntaxTextArea textArea, int
			offsToRender) {

		super(parent);
		this.textArea = textArea;
		JPanel cp = new JPanel(new BorderLayout());
		cp.setBorder(BorderFactory.createCompoundBorder(
				TipUtil.getToolTipBorder(),
				BorderFactory.createEmptyBorder(2, LEFT_EMPTY_BORDER, 5, 5)));
		cp.setBackground(TipUtil.getToolTipBackground());
		setContentPane(cp);

		cp.add(new JLabel(getText(offsToRender)));

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
			size.width = Math.min(size.width, 800);
		}
		return size;
	}


	private String getText(int offsToRender) {

		int line = 0;
		try {
			line = textArea.getLineOfOffset(offsToRender);
		} catch (BadLocationException ble) {
			ble.printStackTrace(); // Never happens
			return null;
		}

		int lastLine = line + 1;

		// Render prior line if the open brace line has no other text on it
		if (line > 0) {
			try {
				int startOffs = textArea.getLineStartOffset(line);
				int length = textArea.getLineEndOffset(line) - startOffs;
				String text = textArea.getText(startOffs, length);
				if (text.trim().length() == 1) {
					line--;
				}
			} catch (BadLocationException ble) {
				UIManager.getLookAndFeel().provideErrorFeedback(textArea);
				ble.printStackTrace();
			}
		}

		Font font = textArea.getFontForTokenType(TokenTypes.IDENTIFIER);
		StringBuilder sb = new StringBuilder("<html>");
		sb.append("<style>body { font-size:\"").append(font.getSize());
		sb.append("pt\" }</style><nobr>");
		while (line < lastLine) {
			Token t = textArea.getTokenListForLine(line);
			while (t!=null && t.isPaintable()) {
				t.appendHTMLRepresentation(sb, textArea, true, true);
				t = t.getNextToken();
			}
			sb.append("<br>");
			line++;
		}

		return sb.toString();

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
	}


	/**
	 * Positions this popup to be in the top right-hand corner of the parent
	 * editor.
	 */
	private void setLocation() {
		Point topLeft = textArea.getVisibleRect().getLocation();
		SwingUtilities.convertPointToScreen(topLeft, textArea);
		topLeft.y = Math.max(topLeft.y - 24, 0);
		setLocation(topLeft.x - LEFT_EMPTY_BORDER, topLeft.y);
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
			uninstallAndHide();
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
}