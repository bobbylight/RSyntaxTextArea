/*
 * 12/14/2008 
 *
 * DefaultTokenMakerFactory.java - The default TokenMaker factory.
 * Copyright (C) 2008 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
package org.fife.ui.rsyntaxtextarea;

import java.util.HashMap;
import java.util.Map;


/**
 * The default implementation of <code>TokenMakerFactory</code>.  This factory
 * can create {@link TokenMaker}s for all languages known to
 * {@link RSyntaxTextArea}.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class DefaultTokenMakerFactory extends AbstractTokenMakerFactory
								implements SyntaxConstants {


	/**
	 * Creates and returns a mapping from keys to the names of
	 * {@link TokenMaker} implementation classes.  When
	 * {@link #getTokenMaker(String)} is called with a key defined in this
	 * map, a <code>TokenMaker</code> of the corresponding type is returned.
	 *
	 * @return The map.
	 */
	protected Map createTokenMakerKeyToClassNameMap() {

		HashMap map = new HashMap();

		String pkg = "org.fife.ui.rsyntaxtextarea.modes.";

		map.put(SYNTAX_STYLE_NONE,				pkg + "PlainTextTokenMaker");
		map.put(SYNTAX_STYLE_ACTIONSCRIPT,		pkg + "ActionScriptTokenMaker");
		map.put(SYNTAX_STYLE_ASSEMBLER_X86,		pkg + "AssemblerX86TokenMaker");
		map.put(SYNTAX_STYLE_BBCODE,			pkg + "BBCodeTokenMaker");
		map.put(SYNTAX_STYLE_C,					pkg + "CTokenMaker");
		map.put(SYNTAX_STYLE_CLOJURE,			pkg + "ClojureTokenMaker");
		map.put(SYNTAX_STYLE_CPLUSPLUS,			pkg + "CPlusPlusTokenMaker");
		map.put(SYNTAX_STYLE_CSHARP,			pkg + "CSharpTokenMaker");
		map.put(SYNTAX_STYLE_CSS,				pkg + "CSSTokenMaker");
		map.put(SYNTAX_STYLE_DELPHI,			pkg + "DelphiTokenMaker");
		map.put(SYNTAX_STYLE_FORTRAN,			pkg + "FortranTokenMaker");
		map.put(SYNTAX_STYLE_GROOVY,			pkg + "GroovyTokenMaker");
		map.put(SYNTAX_STYLE_HTML,				pkg + "HTMLTokenMaker");
		map.put(SYNTAX_STYLE_JAVA,				pkg + "JavaTokenMaker");
		map.put(SYNTAX_STYLE_JAVASCRIPT,		pkg + "JavaScriptTokenMaker");
		map.put(SYNTAX_STYLE_JSP,				pkg + "JSPTokenMaker");
		map.put(SYNTAX_STYLE_LISP,				pkg + "LispTokenMaker");
		map.put(SYNTAX_STYLE_LUA,				pkg + "LuaTokenMaker");
		map.put(SYNTAX_STYLE_MAKEFILE,			pkg + "MakefileTokenMaker");
		map.put(SYNTAX_STYLE_MXML,				pkg + "MxmlTokenMaker");
		map.put(SYNTAX_STYLE_PERL,				pkg + "PerlTokenMaker");
		map.put(SYNTAX_STYLE_PHP,				pkg + "PHPTokenMaker");
		map.put(SYNTAX_STYLE_PROPERTIES_FILE,	pkg + "PropertiesFileTokenMaker");
		map.put(SYNTAX_STYLE_PYTHON,			pkg + "PythonTokenMaker");
		map.put(SYNTAX_STYLE_RUBY,				pkg + "RubyTokenMaker");
		map.put(SYNTAX_STYLE_SAS,				pkg + "SASTokenMaker");
		map.put(SYNTAX_STYLE_SCALA,				pkg + "ScalaTokenMaker");
		map.put(SYNTAX_STYLE_SQL,				pkg + "SQLTokenMaker");
		map.put(SYNTAX_STYLE_TCL,				pkg + "TclTokenMaker");
		map.put(SYNTAX_STYLE_UNIX_SHELL,		pkg + "UnixShellTokenMaker");
		map.put(SYNTAX_STYLE_WINDOWS_BATCH,		pkg + "WindowsBatchTokenMaker");
		map.put(SYNTAX_STYLE_XML,				pkg + "XMLTokenMaker");

		return map;

	}


}