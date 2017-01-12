/*
 * 01/12/2017
 *
 * AssemplerX86TokenRegistration.java - Registration class for AssemplerX86
 * scanner.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMakerRegistration;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;

/**
 *
 * @author matta
 */
public class AssemplerX86TokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE = "text/asm";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return AssemblerX86TokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return null;
    }

}
