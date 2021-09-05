/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rtextarea.AbstractRTextAreaTest;
import org.fife.ui.rtextarea.RTextArea;

import java.awt.*;
import java.awt.event.ActionEvent;


/**
 * A base class for unit tests that need to create {@code RSyntaxTextArea}s.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public abstract class AbstractRSyntaxTextAreaTest extends AbstractRTextAreaTest {

	private static final String DEFAULT_SYNTAX_STYLE = SyntaxConstants.SYNTAX_STYLE_C;

	protected static final String DEFAULT_CODE = "{\n" +
		"  {\n" +
		"    println(\"Wow\");\n" +
		"  }\n" +
		"}";


	/**
	 * Returns a fake action event for test purposes.
	 *
	 * @param textArea The source text area.
	 * @param command The event's command.
	 * @return The fake action event.
	 */
	static ActionEvent createActionEvent(RTextArea textArea, String command) {
		return new ActionEvent(textArea, 0, command);
	}


	/**
	 * Returns a new text area editing C code, with code folding enabled.
	 *
	 * @return The text area.
	 */
	protected static RSyntaxTextArea createTextArea() {
		return createTextArea(null);
	}


	/**
	 * Returns a new text area editing C code, with code folding enabled.
	 *
	 * @param code A snippet of C code to edit.
	 * @return The text area.
	 */
	protected static RSyntaxTextArea createTextArea(String code) {
		return createTextArea(null, code);
	}


	/**
	 * Returns a new text area editing C code, with code folding enabled.
	 *
	 * @param syntaxStyle The syntax style to be editing.
	 * @param code The initial code snippet to be editing.
	 * @return The text area.
	 */
	protected static RSyntaxTextArea createTextArea(String syntaxStyle, String code) {

		if (syntaxStyle == null) {
			syntaxStyle = DEFAULT_SYNTAX_STYLE;
		}

		if (code == null) {
			code = DEFAULT_CODE;
		}

		RSyntaxTextArea textArea = new RSyntaxTextArea(code) {
			@Override
			public Graphics getGraphics() {
				return createTestGraphics();
			}
		};

		textArea.setSyntaxEditingStyle(syntaxStyle);
		textArea.setCodeFoldingEnabled(true);
		textArea.getFoldManager().reparse();
		textArea.setMarkOccurrences(true);
		textArea.setPaintTabLines(true);
		textArea.setBounds(0, 0, 80, 80);
		return textArea;
	}
}
