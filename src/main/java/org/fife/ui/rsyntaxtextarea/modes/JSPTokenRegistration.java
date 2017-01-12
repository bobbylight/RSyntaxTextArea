/*
 * 01/12/2017
 *
 * JSPTokenRegistration.java - Registration class for Jsp scanner.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMakerRegistration;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;
import org.fife.ui.rsyntaxtextarea.folding.HtmlFoldParser;

/**
 *
 * @author matta
 */
public class JSPTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/jsp";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return JSPTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return new HtmlFoldParser(HtmlFoldParser.LANGUAGE_JSP);
    }

}
