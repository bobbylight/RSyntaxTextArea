/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.demo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
//import javax.swing.text.StyleConstants;

import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.FoldIndicatorStyle;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;


/**
 * The root pane used by the demos.  This allows both the applet and the
 * stand-alone application to share the same UI.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class DemoRootPane extends JRootPane implements HyperlinkListener,
											SyntaxConstants {

	private RTextScrollPane scrollPane;
	private RSyntaxTextArea textArea;


	DemoRootPane() {
		textArea = createTextArea();
		setText("JavaExample.txt");
		textArea.setSyntaxEditingStyle(SYNTAX_STYLE_JAVA);
		scrollPane = new RTextScrollPane(textArea, true);
		Gutter gutter = scrollPane.getGutter();
		gutter.setBookmarkingEnabled(true);
		URL url = getClass().getResource("bookmark.png");
		gutter.setBookmarkIcon(new ImageIcon(url));
		getContentPane().add(scrollPane);
		ErrorStrip errorStrip = new ErrorStrip(textArea);
		//errorStrip.setBackground(java.awt.Color.blue);
		getContentPane().add(errorStrip, BorderLayout.LINE_END);
		setJMenuBar(createMenuBar());
	}


	private void addLookAndFeelItem(UIManager.LookAndFeelInfo info, ButtonGroup bg,
									JMenu menu) {
		LookAndFeelAction a = new LookAndFeelAction(info);
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(a);
		bg.add(item);
		menu.add(item);
	}


	private void addSyntaxItem(String name, String res, String style,
			ButtonGroup bg, JMenu menu) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(
				new ChangeSyntaxStyleAction(name, res, style));
		bg.add(item);
		menu.add(item);
	}


	private void addThemeItem(String name, String themeXml, ButtonGroup bg,
			JMenu menu) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(
				new ThemeAction(name, themeXml));
		bg.add(item);
		menu.add(item);
	}


	private static Action createCopyAsStyledTextAction(String themeName) throws IOException {
		String resource = "/org/fife/ui/rsyntaxtextarea/themes/" + themeName + ".xml";
		Theme theme = Theme.load(DemoRootPane.class.getResourceAsStream(resource));
		return new RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction(themeName, theme, false);
	}


	private JMenuBar createMenuBar() {

		JMenuBar mb = new JMenuBar();

		JMenu menu = new JMenu("Language");
		ButtonGroup bg = new ButtonGroup();
		addSyntaxItem("6502 Assembler", "Assembler6502.txt", SYNTAX_STYLE_ASSEMBLER_6502, bg, menu);
		addSyntaxItem("ActionScript", "ActionScriptExample.txt", SYNTAX_STYLE_ACTIONSCRIPT, bg, menu);
		addSyntaxItem("C",    "CExample.txt", SYNTAX_STYLE_C, bg, menu);
		addSyntaxItem("CSS",  "CssExample.txt", SYNTAX_STYLE_CSS, bg, menu);
		addSyntaxItem("Dockerfile", "DockerfileExample.txt", SYNTAX_STYLE_DOCKERFILE, bg, menu);
		addSyntaxItem("Go", "GoExample.txt", SYNTAX_STYLE_GO, bg, menu);
		addSyntaxItem("Handlebars", "HandlebarsExample.txt", SYNTAX_STYLE_HANDLEBARS, bg, menu);
		addSyntaxItem("Hosts", "HostsExample.txt", SYNTAX_STYLE_HOSTS, bg, menu);
		addSyntaxItem("HTML", "HtmlExample.txt", SYNTAX_STYLE_HTML, bg, menu);
		addSyntaxItem("INI", "IniExample.txt", SYNTAX_STYLE_INI, bg, menu);
		addSyntaxItem("Java", "JavaExample.txt", SYNTAX_STYLE_JAVA, bg, menu);
		addSyntaxItem("JavaScript", "JavaScriptExample.txt", SYNTAX_STYLE_JAVASCRIPT, bg, menu);
		addSyntaxItem("JSP", "JspExample.txt", SYNTAX_STYLE_JSP, bg, menu);
		addSyntaxItem("JSON", "JsonExample.txt", SYNTAX_STYLE_JSON_WITH_COMMENTS, bg, menu);
		addSyntaxItem("Kotlin", "KotlinExample.txt", SYNTAX_STYLE_KOTLIN, bg, menu);
		addSyntaxItem("LaTeX", "LatexExample.txt", SYNTAX_STYLE_LATEX, bg, menu);
		addSyntaxItem("Less", "LessExample.txt", SYNTAX_STYLE_LESS, bg, menu);
		addSyntaxItem("Markdown", "MarkdownExample.txt", SYNTAX_STYLE_MARKDOWN, bg, menu);
		addSyntaxItem("Perl", "PerlExample.txt", SYNTAX_STYLE_PERL, bg, menu);
		addSyntaxItem("PHP",  "PhpExample.txt", SYNTAX_STYLE_PHP, bg, menu);
		addSyntaxItem("Proto", "ProtoExample.txt", SYNTAX_STYLE_PROTO, bg, menu);
		addSyntaxItem("Python",  "PythonExample.txt", SYNTAX_STYLE_PYTHON, bg, menu);
		addSyntaxItem("Ruby", "RubyExample.txt", SYNTAX_STYLE_RUBY, bg, menu);
		addSyntaxItem("SQL",  "SQLExample.txt", SYNTAX_STYLE_SQL, bg, menu);
		addSyntaxItem("TypeScript", "TypeScriptExample.txt", SYNTAX_STYLE_TYPESCRIPT, bg, menu);
		addSyntaxItem("XML",  "XMLExample.txt", SYNTAX_STYLE_XML, bg, menu);
		addSyntaxItem("YAML", "YamlExample.txt", SYNTAX_STYLE_YAML, bg, menu);
		menu.getItem(2).setSelected(true);
		mb.add(menu);

		menu = new JMenu("View");
		JMenu foldStyleSubMenu = new JMenu("Fold Region Style");
		JRadioButtonMenuItem classicStyleItem = new JRadioButtonMenuItem(
			new FoldStyleAction(FoldIndicatorStyle.CLASSIC));
		JRadioButtonMenuItem modernStyleItem = new JRadioButtonMenuItem(new FoldStyleAction(FoldIndicatorStyle.MODERN));
		modernStyleItem.setSelected(true);
		bg = new ButtonGroup();
		bg.add(classicStyleItem);
		bg.add(modernStyleItem);
		foldStyleSubMenu.add(classicStyleItem);
		foldStyleSubMenu.add(modernStyleItem);
		menu.add(foldStyleSubMenu);
		JCheckBoxMenuItem cbItem = new JCheckBoxMenuItem(new CodeFoldingAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ViewLineHighlightAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ViewLineNumbersAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new AnimateBracketMatchingAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new BookmarksAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new WordWrapAction());
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ToggleAntiAliasingAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new MarkOccurrencesAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new TabLinesAction());
		menu.add(cbItem);
		mb.add(menu);

		menu = new JMenu("LookAndFeel");
		bg = new ButtonGroup();
		UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
		for (UIManager.LookAndFeelInfo info : infos) {
			addLookAndFeelItem(info, bg, menu);
		}
		mb.add(menu);

		bg = new ButtonGroup();
		menu = new JMenu("Themes");
		addThemeItem("Default", "default.xml", bg, menu);
		addThemeItem("Default (System Selection)", "default-alt.xml", bg, menu);
		addThemeItem("Dark", "dark.xml", bg, menu);
		addThemeItem("Druid", "druid.xml", bg, menu);
		addThemeItem("Monokai", "monokai.xml", bg, menu);
		addThemeItem("Eclipse", "eclipse.xml", bg, menu);
		addThemeItem("IDEA", "idea.xml", bg, menu);
		addThemeItem("Visual Studio", "vs.xml", bg, menu);
		mb.add(menu);

		menu = new JMenu("Help");
		JMenuItem item = new JMenuItem(new AboutAction());
		menu.add(item);
		mb.add(menu);

		return mb;

	}


	/**
	 * Creates the text area for this application.
	 *
	 * @return The text area.
	 */
	private RSyntaxTextArea createTextArea() {

		RSyntaxTextArea textArea = new RSyntaxTextArea(25, 70);
		textArea.setTabSize(3);
		textArea.setCaretPosition(0);
		textArea.addHyperlinkListener(this);
		textArea.requestFocusInWindow();
		textArea.setMarkOccurrences(true);
		textArea.setCodeFoldingEnabled(true);
		textArea.setClearWhitespaceLinesEnabled(false);

		InputMap im = textArea.getInputMap();
		ActionMap am = textArea.getActionMap();
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0), "decreaseFontSize");
		am.put("decreaseFontSize", new RSyntaxTextAreaEditorKit.DecreaseFontSizeAction());
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "increaseFontSize");
		am.put("increaseFontSize", new RSyntaxTextAreaEditorKit.IncreaseFontSizeAction());

		int ctrlShift = InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK;
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, ctrlShift), "copyAsStyledText");
		am.put("copyAsStyledText", new RSyntaxTextAreaEditorKit.CopyCutAsStyledTextAction(false));

		try {

			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, ctrlShift), "copyAsStyledTextMonokai");
			am.put("copyAsStyledTextMonokai", createCopyAsStyledTextAction("monokai"));

			im.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, ctrlShift), "copyAsStyledTextEclipse");
			am.put("copyAsStyledTextEclipse", createCopyAsStyledTextAction("eclipse"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// Since this demo allows the LookAndFeel and RSyntaxTextArea Theme to
		// be toggled independently of one another, we set this property to
		// true so matched bracket popups look good.  In an app where the
		// developer ensures the RSTA Theme always matches the LookAndFeel as
		// far as light/dark is concerned, this property can be omitted.
		System.setProperty(MatchedBracketPopup.PROPERTY_CONSIDER_TEXTAREA_BACKGROUND, "true");

		return textArea;
	}


	/**
	 * Focuses the text area.
	 */
	void focusTextArea() {
		textArea.requestFocusInWindow();
	}


	/**
	 * Called when a hyperlink is clicked in the text area.
	 *
	 * @param e The event.
	 */
	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType()==HyperlinkEvent.EventType.ACTIVATED) {
			URL url = e.getURL();
			if (url==null) {
				UIManager.getLookAndFeel().provideErrorFeedback(null);
			}
			else {
				JOptionPane.showMessageDialog(this,
									"URL clicked:\n" + url);
			}
		}
	}


	/**
	 * Sets the content in the text area to that in the specified resource.
	 *
	 * @param resource The resource to load.
	 */
	private void setText(String resource) {
		BufferedReader r;
		try {
			r = new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(resource), StandardCharsets.UTF_8));
			textArea.read(r, null);
			r.close();
			textArea.setCaretPosition(0);
			textArea.discardAllEdits();
		} catch (RuntimeException re) {
			throw re; // FindBugs
		} catch (Exception e) { // Never happens
			textArea.setText("Type here to see syntax highlighting");
		}
	}

	/**
	 * Shows the About dialog.
	 */
	private class AboutAction extends AbstractAction {

		AboutAction() {
			putValue(NAME, "About RSyntaxTextArea...");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(DemoRootPane.this,
					"<html><b>RSyntaxTextArea</b> - A Swing syntax highlighting text component" +
					"<br>Licensed under a modified BSD license",
					"About RSyntaxTextArea",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	/**
	 * Toggles whether matched brackets are animated.
	 */
	private class AnimateBracketMatchingAction extends AbstractAction {

		AnimateBracketMatchingAction() {
			putValue(NAME, "Animate Bracket Matching");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			textArea.setAnimateBracketMatching(
						!textArea.getAnimateBracketMatching());
		}

	}

	/**
	 * Toggles whether bookmarks are enabled.
	 */
	private class BookmarksAction extends AbstractAction {

		BookmarksAction() {
			putValue(NAME, "Bookmarks");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			scrollPane.setIconRowHeaderEnabled(
							!scrollPane.isIconRowHeaderEnabled());
		}

	}

	/**
	 * Changes the syntax style to a new value.
	 */
	private class ChangeSyntaxStyleAction extends AbstractAction {

		private String res;
		private String style;

		ChangeSyntaxStyleAction(String name, String res, String style) {
			putValue(NAME, name);
			this.res = res;
			this.style = style;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setText(res);
			textArea.setCaretPosition(0);
			textArea.setSyntaxEditingStyle(style);
		}

	}

	/**
	 * Toggles whether code folding is enabled.
	 */
	private class CodeFoldingAction extends AbstractAction {

		CodeFoldingAction() {
			putValue(NAME, "Code Folding");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			textArea.setCodeFoldingEnabled(!textArea.isCodeFoldingEnabled());
		}

	}

	/**
	 * Changes the appearance of the fold indicator region of the gutter.
	 */
	private class FoldStyleAction extends AbstractAction {

		private final FoldIndicatorStyle style;

		FoldStyleAction(FoldIndicatorStyle style) {
			this.style = style;
			String name = style.name().charAt(0) +
				style.name().substring(1).toLowerCase(Locale.getDefault());
			putValue(NAME, name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			scrollPane.getGutter().setFoldIndicatorStyle(style);
		}

	}

	/**
	 * Changes the look and feel of the demo application.
	 */
	private class LookAndFeelAction extends AbstractAction {

		private UIManager.LookAndFeelInfo info;

		LookAndFeelAction(UIManager.LookAndFeelInfo info) {
			putValue(NAME, info.getName());
			this.info = info;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				UIManager.setLookAndFeel(info.getClassName());
				SwingUtilities.updateComponentTreeUI(DemoRootPane.this);
			} catch (RuntimeException re) {
				throw re; // FindBugs
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Toggles whether "mark occurrences" is enabled.
	 */
	private class MarkOccurrencesAction extends AbstractAction {

		MarkOccurrencesAction() {
			putValue(NAME, "Mark Occurrences");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			textArea.setMarkOccurrences(!textArea.getMarkOccurrences());
		}

	}

	/**
	 * Toggles whether "tab lines" are enabled.
	 */
	private class TabLinesAction extends AbstractAction {

		private boolean selected;

		TabLinesAction() {
			putValue(NAME, "Tab Lines");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			selected = !selected;
			textArea.setPaintTabLines(selected);
		}

	}

	/**
	 * Changes the theme.
	 */
	private class ThemeAction extends AbstractAction {

		private String xml;

		ThemeAction(String name, String xml) {
			putValue(NAME, name);
			this.xml = xml;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			InputStream in = getClass().
				getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/" + xml);
			try {
				Theme theme = Theme.load(in);
				theme.apply(textArea);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}

	/**
	 * Toggles anti-aliasing.
	 */
	private class ToggleAntiAliasingAction extends AbstractAction {

		ToggleAntiAliasingAction() {
			putValue(NAME, "Anti-Aliasing");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			textArea.setAntiAliasingEnabled(!textArea.getAntiAliasingEnabled());
		}

	}

	/**
	 * Toggles whether the current line is highlighted.
	 */
	private class ViewLineHighlightAction extends AbstractAction {

		ViewLineHighlightAction() {
			putValue(NAME, "Current Line Highlight");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			textArea.setHighlightCurrentLine(
					!textArea.getHighlightCurrentLine());
		}

	}

	/**
	 * Toggles line number visibility.
	 */
	private class ViewLineNumbersAction extends AbstractAction {

		ViewLineNumbersAction() {
			putValue(NAME, "Line Numbers");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			scrollPane.setLineNumbersEnabled(
					!scrollPane.getLineNumbersEnabled());
		}

	}

	/**
	 * Toggles word wrap.
	 */
	private class WordWrapAction extends AbstractAction {

		WordWrapAction() {
			putValue(NAME, "Word Wrap");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			textArea.setLineWrap(!textArea.getLineWrap());
		}

	}


}
