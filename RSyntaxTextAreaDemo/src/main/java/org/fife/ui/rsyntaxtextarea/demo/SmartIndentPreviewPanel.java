/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.demo;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.fife.ui.rsyntaxtextarea.demo.IndentAnalyzer.IndentResult;


/**
 * A collapsible panel that displays smart indent preview information.
 *
 * @author RSyntaxTextArea Demo
 * @version 1.0
 */
public class SmartIndentPreviewPanel extends JPanel {

	private final IndentAnalyzer indentAnalyzer;
	private final IndentFormatter indentFormatter;

	private JTextArea currentLineArea;
	private JLabel indentLevelLabel;
	private JLabel prefixLabel;
	private JPanel contentPanel;
	private JButton collapseButton;
	private JButton refreshButton;

	private boolean collapsed = false;


	public SmartIndentPreviewPanel() {
		this(new IndentAnalyzer(), new IndentFormatter());
	}


	public SmartIndentPreviewPanel(IndentAnalyzer indentAnalyzer, IndentFormatter indentFormatter) {
		this.indentAnalyzer = indentAnalyzer;
		this.indentFormatter = indentFormatter;
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		createUI();
	}


	private void createUI() {
		JPanel headerPanel = createHeaderPanel();
		add(headerPanel, BorderLayout.NORTH);

		contentPanel = createContentPanel();
		add(contentPanel, BorderLayout.CENTER);

		setMinimumSize(new Dimension(200, 50));
		setPreferredSize(new Dimension(280, 200));
	}


	private JPanel createHeaderPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

		JLabel titleLabel = new JLabel("Smart Indent Preview");
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		panel.add(titleLabel, BorderLayout.WEST);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));

		refreshButton = new JButton("Refresh");
		refreshButton.setToolTipText("Refresh indent analysis");
		refreshButton.setMargin(new Insets(2, 6, 2, 6));
		buttonPanel.add(refreshButton);

		collapseButton = new JButton("[-]");
		collapseButton.setToolTipText("Collapse/Expand panel");
		collapseButton.setMargin(new Insets(2, 6, 2, 6));
		collapseButton.addActionListener(e -> toggleCollapse());
		buttonPanel.add(collapseButton);

		panel.add(buttonPanel, BorderLayout.EAST);

		return panel;
	}


	private JPanel createContentPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createEtchedBorder(),
			"Indent Information",
			TitledBorder.LEFT,
			TitledBorder.TOP
		));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		JLabel currentLineLabel = new JLabel("Current Line:");
		currentLineLabel.setFont(currentLineLabel.getFont().deriveFont(Font.BOLD));
		panel.add(currentLineLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		currentLineArea = new JTextArea(3, 20);
		currentLineArea.setEditable(false);
		currentLineArea.setLineWrap(true);
		currentLineArea.setWrapStyleWord(true);
		currentLineArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		currentLineArea.setBorder(BorderFactory.createLoweredBevelBorder());
		JScrollPane scrollPane = new JScrollPane(currentLineArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollPane, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		JLabel indentLevelTitle = new JLabel("Suggested Indent Level:");
		indentLevelTitle.setFont(indentLevelTitle.getFont().deriveFont(Font.BOLD));
		panel.add(indentLevelTitle, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		indentLevelLabel = new JLabel("-");
		indentLevelLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
		indentLevelLabel.setForeground(new Color(0, 100, 0));
		panel.add(indentLevelLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		JLabel prefixTitle = new JLabel("Suggested Prefix:");
		prefixTitle.setFont(prefixTitle.getFont().deriveFont(Font.BOLD));
		panel.add(prefixTitle, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 1.0;
		prefixLabel = new JLabel("-");
		prefixLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		prefixLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		prefixLabel.setOpaque(true);
		prefixLabel.setBackground(Color.WHITE);
		panel.add(prefixLabel, gbc);

		return panel;
	}


	public void updatePreview(String text, int caretLine, String syntaxStyle) {
		try {
			IndentResult result = indentAnalyzer.analyze(text, caretLine, syntaxStyle);
			updateDisplay(result);
		} catch (Exception e) {
			updateDisplay(IndentResult.empty());
		}
	}


	private void updateDisplay(IndentResult result) {
		if (result.isEmpty()) {
			currentLineArea.setText("(Empty document)");
			indentLevelLabel.setText("-");
			indentLevelLabel.setForeground(Color.GRAY);
			prefixLabel.setText("-");
		} else if (!result.isSupported()) {
			currentLineArea.setText(result.getCurrentLine());
			indentLevelLabel.setText("(Language not supported)");
			indentLevelLabel.setForeground(Color.GRAY);
			prefixLabel.setText("-");
		} else {
			currentLineArea.setText(result.getCurrentLine());
			currentLineArea.setCaretPosition(0);

			int level = result.getIndentLevel();
			indentLevelLabel.setText(String.valueOf(level));
			indentLevelLabel.setForeground(new Color(0, 100, 0));

			String prefix = indentFormatter.format(level);
			if (prefix.isEmpty()) {
				prefixLabel.setText("(no indent)");
			} else {
				String displayPrefix = prefix.replace("\t", "→").replace(" ", "·");
				prefixLabel.setText("\"" + displayPrefix + "\"");
			}
		}
	}


	private void toggleCollapse() {
		collapsed = !collapsed;
		contentPanel.setVisible(!collapsed);
		collapseButton.setText(collapsed ? "[+]" : "[-]");
		revalidate();
	}


	public void setCollapsed(boolean collapsed) {
		if (this.collapsed != collapsed) {
			toggleCollapse();
		}
	}


	public boolean isCollapsed() {
		return collapsed;
	}


	public void addRefreshActionListener(ActionListener listener) {
		refreshButton.addActionListener(listener);
	}


	public IndentAnalyzer getIndentAnalyzer() {
		return indentAnalyzer;
	}


	public IndentFormatter getIndentFormatter() {
		return indentFormatter;
	}


}
