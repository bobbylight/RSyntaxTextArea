package org.fife.ui.rsyntaxtextarea.demo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

/**
 * A simple example showing how to do search and replace in a RSyntaxTextArea.
 * The toolbar isn't very user-friendly, but this is just to show you how to use
 * the API.
 */
public final class FindAndReplaceDemo extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private RSyntaxTextArea textArea;
	private JTextField searchField;
	private JCheckBox regexCB;
	private JCheckBox matchCaseCB;

	private FindAndReplaceDemo() {

		JPanel cp = new JPanel(new BorderLayout());

		textArea = new RSyntaxTextArea(20, 60);
		textArea.setText("one two three one\ntwo three one two\nthree one two three");
		textArea.setCaretPosition(0);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setCodeFoldingEnabled(true);
		RTextScrollPane sp = new RTextScrollPane(textArea);
		cp.add(sp);

		// Create a toolbar with searching options.
		JToolBar toolBar = new JToolBar();
		searchField = new JTextField(30);
		toolBar.add(searchField);
		final JButton nextButton = new JButton("Find Next");
		nextButton.setActionCommand("FindNext");
		nextButton.addActionListener(this);
		toolBar.add(nextButton);
		JButton prevButton = new JButton("Find Previous");
		prevButton.setActionCommand("FindPrev");
		prevButton.addActionListener(this);
		toolBar.add(prevButton);
		regexCB = new JCheckBox("Regex");
		toolBar.add(regexCB);
		matchCaseCB = new JCheckBox("Match Case");
		toolBar.add(matchCaseCB);
		cp.add(toolBar, BorderLayout.NORTH);

		// Make Enter and Shift + Enter search forward and backward,
		// respectively, when the search field is focused.
		InputMap im = searchField.getInputMap();
		ActionMap am = searchField.getActionMap();
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "searchForward");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_MASK), "searchBackward");
		am.put("searchForward", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextButton.doClick(0);
			}
		});
		am.put("searchBackward", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				prevButton.doClick(0);
			}
		});

		// Make Ctrl+F/Cmd+F focus the search field
		int defaultMod = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		im = textArea.getInputMap();
		am = textArea.getActionMap();
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, defaultMod), "doSearch");
		am.put("doSearch", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				searchField.requestFocusInWindow();
			}
		});

		setContentPane(cp);
		setTitle("Find and Replace Demo");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setLocationByPlatform(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// "FindNext" => search forward, "FindPrev" => search backward
		String command = e.getActionCommand();
		boolean forward = "FindNext".equals(command);

		// Create an object defining our search parameters.
		SearchContext context = new SearchContext();
		String text = searchField.getText();
		if (text.length() == 0) {
			return;
		}
		context.setSearchFor(text);
		context.setMatchCase(matchCaseCB.isSelected());
		context.setRegularExpression(regexCB.isSelected());
		context.setSearchForward(forward);
		context.setWholeWord(false);

		boolean found = SearchEngine.find(textArea, context).wasFound();
		if (!found) {
			JOptionPane.showMessageDialog(this, "Text not found");
		}

	}

	public static void main(String[] args) {
		// Start all Swing applications on the EDT.
		SwingUtilities.invokeLater(() -> {
			try {
				String laf = UIManager.getSystemLookAndFeelClassName();
				UIManager.setLookAndFeel(laf);
			} catch (Exception e) { /* never happens */ }
			FindAndReplaceDemo demo = new FindAndReplaceDemo();
			demo.setVisible(true);
			demo.textArea.requestFocusInWindow();
		});
	}

}
