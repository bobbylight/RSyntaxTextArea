/*
 * 01/12/2017
 *
 * TokenMakerRegistration.java - Interface for registering a scanner.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import org.fife.ui.rsyntaxtextarea.folding.FoldParser;

/**
 *
 * @author matta
 */
public interface TokenMakerRegistration {

    String getLanguage();

    String getTokenMaker();
    
    FoldParser getFoldParser();
}
