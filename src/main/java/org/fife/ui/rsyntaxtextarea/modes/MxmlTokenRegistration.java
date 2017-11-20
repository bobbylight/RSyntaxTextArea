/*
 * 01/12/2017
 *
 * MxmlTokenRegistration.java - Registration class for mxml scanner.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMakerRegistration;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;
import org.fife.ui.rsyntaxtextarea.folding.XmlFoldParser;

/**
 *
 * @author matta
 */
public class MxmlTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/mxml";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return MxmlTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return new XmlFoldParser();
    }

}
