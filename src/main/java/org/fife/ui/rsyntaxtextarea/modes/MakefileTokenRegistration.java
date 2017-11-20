/*
 * 01/12/2017
 *
 * MakefileTokenRegistration.java - Registration class for Makefile
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
public class MakefileTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/makefile";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return MakefileTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return null;
    }

}
