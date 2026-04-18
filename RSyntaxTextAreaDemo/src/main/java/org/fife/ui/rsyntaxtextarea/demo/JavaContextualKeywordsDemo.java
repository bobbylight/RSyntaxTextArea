package org.fife.ui.rsyntaxtextarea.demo;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * A focused demo showing Java restricted keywords both in real language
 * constructs and as ordinary identifiers such as member names.
 */
public final class JavaContextualKeywordsDemo extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String TEXT =
		"module demo.sample {\n" +
		"    exports demo.api to demo.client;\n" +
		"    opens demo.internal;\n" +
		"    requires transitive demo.dependency;\n" +
		"    uses demo.spi.Service;\n" +
		"    provides demo.spi.Service with demo.impl.ServiceImpl;\n" +
		"}\n" +
		"\n" +
		"sealed interface Shape permits Circle, Square {\n" +
		"}\n" +
		"\n" +
		"non-sealed class Square implements Shape {\n" +
		"}\n" +
		"\n" +
		"record Circle(int radius) implements Shape {\n" +
		"\n" +
		"    private int open;\n" +
		"    private int yield;\n" +
		"    private int record;\n" +
		"\n" +
		"    int open() {\n" +
		"        var value = radius;\n" +
		"        return value;\n" +
		"    }\n" +
		"\n" +
		"    int record() {\n" +
		"        return this.record;\n" +
		"    }\n" +
		"\n" +
		"    int yield() {\n" +
		"        return this.yield;\n" +
		"    }\n" +
		"}\n" +
		"\n" +
		"class KeywordUsage {\n" +
		"\n" +
		"    void demo(int record, int open, int yield) {\n" +
		"        Circle circle = new Circle(4);\n" +
		"        int value = switch (record) {\n" +
		"            case 0 -> yield;\n" +
		"            default -> {\n" +
		"                yield circle.open() + circle.record() + circle.yield() + open;\n" +
		"            }\n" +
		"        };\n" +
		"        System.out.println(value);\n" +
		"    }\n" +
		"}\n";


	private JavaContextualKeywordsDemo() {

		super("Java Contextual Keywords Demo");

		RSyntaxTextArea textArea = new RSyntaxTextArea(30, 90);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setCodeFoldingEnabled(true);
		textArea.setAntiAliasingEnabled(true);
		textArea.setMarkOccurrences(true);
		textArea.setText(TEXT);
		int caret = TEXT.indexOf("    int open()");
		textArea.setCaretPosition(caret > -1 ? caret : 0);

		RTextScrollPane scrollPane = new RTextScrollPane(textArea);
		setLayout(new BorderLayout());
		add(scrollPane);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);

	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ignored) {
				// Ignore and fall back to the default look and feel.
			}
			new JavaContextualKeywordsDemo().setVisible(true);
		});
	}

}
