/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMakerRegistration;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;

/**
 *
 * @author matta
 */
public class PlainTextTokenRegistration implements TokenMakerRegistration {

    public static final String SYNTAX_STYLE	= "text/none";

    @Override
    public String getLanguage() {
        return SYNTAX_STYLE;
    }

    @Override
    public String getTokenMaker() {
        return PlainTextTokenMaker.class.getName();
    }
    
    @Override
    public FoldParser getFoldParser() {
        return null;
    }
}