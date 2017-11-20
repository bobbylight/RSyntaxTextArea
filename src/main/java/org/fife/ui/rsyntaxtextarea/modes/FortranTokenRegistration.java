/*
 * 01/12/2017
 *
 * FortranTokenRegistration.java - Registration class for Fortran
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
public class FortranTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/fortran";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return FortranTokenMaker.class.getName();
    }

    @Override
    public FoldParser getFoldParser() {
        return null;
    }
}