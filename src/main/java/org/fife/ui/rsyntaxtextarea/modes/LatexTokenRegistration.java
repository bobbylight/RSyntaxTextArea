/*
 * 01/12/2017
 *
 * LatexTokenRegistration.java - Registration class for Latex
 * scanner.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMakerRegistration;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;
import org.fife.ui.rsyntaxtextarea.folding.LatexFoldParser;

/**
 *
 * @author matta
 */
public class LatexTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/latex";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return LatexTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return new LatexFoldParser();
    }

}
