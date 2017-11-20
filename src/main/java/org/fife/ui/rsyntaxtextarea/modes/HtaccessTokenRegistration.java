/*
 * 01/12/2017
 *
 * HtaccessTokenRegistration.java - Registration class for htaccess
 * scanner.
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
public class HtaccessTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/htaccess";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return HtaccessTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return new XmlFoldParser();
    }

}
