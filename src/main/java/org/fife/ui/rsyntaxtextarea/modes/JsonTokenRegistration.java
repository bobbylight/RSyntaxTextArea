/*
 * 01/12/2017
 *
 * JsonTokenRegistration.java - Registration class for json
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
public class JsonTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/json";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return JsonTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return new JsonFoldParser();
    }

}
