/*
 * 01/12/2017
 *
 * UnixShellTokenRegistration.java - Registration class for Unix Shell
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
public class UnixShellTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/unix";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return UnixShellTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return null;
    }

}
