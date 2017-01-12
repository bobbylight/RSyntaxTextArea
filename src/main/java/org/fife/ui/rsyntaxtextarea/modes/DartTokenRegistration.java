/*
 * 01/12/2017
 *
 * DartTokenRegistration.java - Registration class for Dart scanner.
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
public class DartTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/dart";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return DartTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return new CurlyFoldParser();
    }
}
