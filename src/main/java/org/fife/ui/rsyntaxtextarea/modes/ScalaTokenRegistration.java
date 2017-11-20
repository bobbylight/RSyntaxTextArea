/*
 * 01/12/2017
 *
 * ScalaTokenRegistration.java - Registration class for Scala scanner.
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
public class ScalaTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/scala";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return ScalaTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return new CurlyFoldParser();
    }

}
