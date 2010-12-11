/*
 * 08/11/2009
 *
 * TaskTagParser.java - Parser that scans code comments for task tags.
 * Copyright (C) 2009 Robert Futrell
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
package org.fife.ui.rsyntaxtextarea.parser;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.text.Element;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Token;


/**
 * Parser that identifies "task tags," such as "<code>TODO</code>",
 * "<code>FIXME</code>", etc. in source code comments.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class TaskTagParser extends AbstractParser {

	private DefaultParseResult result;
	private String DEFAULT_TASK_PATTERN	= "TODO|FIXME|HACK";
	private Pattern taskPattern;

	private static final Color COLOR = new Color(48, 150, 252);


	/**
	 * Creates a new task parser.  The default parser treats the following
	 * identifiers in comments as task definitions:  "<code>TODO</code>",
	 * "<code>FIXME</code>", and "<code>HACK</code>".
	 */
	public TaskTagParser() {
		result = new DefaultParseResult(this);
		setTaskPattern(DEFAULT_TASK_PATTERN);
	}


	/**
	 * Returns the regular expression used to search for tasks.
	 *
	 * @return The regular expression.  This may be <code>null</code> if no
	 *         regular expression was specified (or an empty string was
	 *         specified).
	 * @see #setTaskPattern(String)
	 */
	public String getTaskPattern() {
		return taskPattern==null ? null : taskPattern.pattern();
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
				if (t.isComment()) {

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
				TaskNotice pn = new TaskNotice(this, text, line, offs, len);
				pn.setLevel(ParserNotice.INFO);
				pn.setShowInEditor(false);
				pn.setColor(COLOR);
				result.addNotice(pn);
			}

		}

		return result;

	}


	/**
	 * Sets the pattern of task identifiers.  You will usually want this to be
	 * a list of words "or'ed" together, such as
	 * "<code>TODO|FIXME|HACK|REMIND</code>".
	 *
	 * @param pattern The pattern.  A value of <code>null</code> or an
	 *        empty string effectively disables task parsing.
	 * @throws PatternSyntaxException If <code>pattern</code> is an invalid
	 *         regular expression.
	 * @see #getTaskPattern()
	 */
	public void setTaskPattern(String pattern) throws PatternSyntaxException {
		if (pattern==null || pattern.length()==0) {
			taskPattern = null;
		}
		else {
			taskPattern = Pattern.compile(pattern);
		}
	}


	/**
	 * A parser notice that signifies a task.
	 */
	public static class TaskNotice extends DefaultParserNotice {

		public TaskNotice(Parser parser, String message, int line, int offs,
							int length) {
			super(parser, message, line, offs, length);
		}

	}


}