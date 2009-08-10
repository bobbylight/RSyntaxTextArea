package org.fife.ui.rsyntaxtextarea;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.text.Element;

import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;


/**
 * Parser that identifies "task tags," such as "<code>TODO</code>",
 * "<code>FIXME</code>", etc. in comments.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class TaskTagParser extends AbstractParser {

	private DefaultParseResult result;
	private String DEFAULT_TASK_PATTERN	= "TODO|FIXME";
	private Pattern taskPattern;


	public TaskTagParser() {
		result = new DefaultParseResult(this);
		setTaskPattern(DEFAULT_TASK_PATTERN);
	}


	public ParseResult parse(RSyntaxDocument doc, String style) {

		Element root = doc.getDefaultRootElement();
		int lineCount = root.getElementCount();

		if (taskPattern==null ||
				style==null || SyntaxConstants.SYNTAX_STYLE_NONE.equals(style)){
			result.clearNotices();
			result.setParsedLines(0, lineCount-1);
			return result;
		}

		// TODO: Pass in parsed line range and just do that
		result.clearNotices();
		result.setParsedLines(0, lineCount-1);

		for (int line=0; line<lineCount; line++) {

			Token t = doc.getTokenListForLine(line);
			int offs = -1;
			int start = -1;
			String text = null;

			while (t!=null && t.isPaintable()) {
				if (t.type==Token.COMMENT_EOL ||
						t.type==Token.COMMENT_MULTILINE ||
						t.type==Token.COMMENT_DOCUMENTATION) {

					offs = t.offset;
					text = t.getLexeme();

					Matcher m = taskPattern.matcher(text);
					if (m.find()) {
						start = m.start();
						offs += start;
						break;
					}

				}
				t = t.getNextToken();
			}

			if (start>-1) {
				text = text.substring(start);
				// TODO: Strip off end of MLC's if they're there.
				int len = text.length();
				ParserNotice pn = new ParserNotice(this, text, line, offs, len);
				result.addNotice(pn);
			}

		}

		return result;

	}


	/**
	 * Sets the pattern of task identifiers, for example,
	 * "<code>TODO|FIXME</code>".
	 *
	 * @param pattern The pattern.  A value of <code>null</code> effectively
	 *        disables task parsing.
	 * @throws PatternSyntaxException If <code>pattern</code> is an invalid
	 *         regular expression.
	 */
	public void setTaskPattern(String pattern) throws PatternSyntaxException {
		if (pattern==null) {
			taskPattern = null;
		}
		else {
			taskPattern = Pattern.compile(pattern);
		}
	}


}