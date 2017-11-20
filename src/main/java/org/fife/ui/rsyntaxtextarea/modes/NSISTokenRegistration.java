/*
 * 01/12/2017
 *
 * NSISTokenRegistration.java - Registration class for nsis scanner.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMakerRegistration;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;
import org.fife.ui.rsyntaxtextarea.folding.NsisFoldParser;

/**
 *
 * @author matta
 */
public class NSISTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/nsis";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return NSISTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return new NsisFoldParser();
    }

}
