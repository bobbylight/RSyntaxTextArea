/*
 * 01/12/2017
 *
 * CTokenRegistration.java - Registration class for C scanner.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMakerRegistration;
import org.fife.ui.rsyntaxtextarea.folding.CurlyFoldParser;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;

/**
 *
 * @author matta
 */
public class CTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE = "text/c";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return CTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return new CurlyFoldParser();
    }

}
