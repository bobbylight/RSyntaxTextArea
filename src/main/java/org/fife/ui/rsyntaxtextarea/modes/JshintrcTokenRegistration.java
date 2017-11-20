/*
 * 01/12/2017
 *
 * JshintrcTokenRegistration.java - Registration class for Jshintrc
 * scanner.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMakerRegistration;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;
import org.fife.ui.rsyntaxtextarea.folding.JsonFoldParser;

/**
 *
 * @author matta
 */
public class JshintrcTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/jshintrc";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return JshintrcTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return new JsonFoldParser();
    }

}
