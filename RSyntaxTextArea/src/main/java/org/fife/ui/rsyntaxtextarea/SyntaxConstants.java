/*
 * 03/08/2004
 *
 * SyntaxConstants.java - Constants used by RSyntaxTextArea and friends.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


/**
 * Constants that define the different programming languages understood by
 * <code>RSyntaxTextArea</code>.  These constants are the values you can pass
 * to {@link RSyntaxTextArea#setSyntaxEditingStyle(String)} to get syntax
 * highlighting.<p>
 *
 * By default, all <code>RSyntaxTextArea</code>s can render all of these
 * languages, but this can be changed (the list can be augmented or completely
 * overwritten) on a per-text area basis.  What languages can be rendered is
 * actually managed by the {@link TokenMakerFactory} installed on the text
 * area's {@link RSyntaxDocument}.  By default, all
 * <code>RSyntaxDocumenet</code>s have a factory installed capable of handling
 * all of these languages.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface SyntaxConstants {

	/**
	 * Style meaning don't syntax highlight anything.
	 */
	String SYNTAX_STYLE_NONE			= "text/plain";


	/**
	 * Style for highlighting ActionScript.
	 */
	String SYNTAX_STYLE_ACTIONSCRIPT	= "text/actionscript";


	/**
	 * Style for highlighting x86 assembler.
	 */
	String SYNTAX_STYLE_ASSEMBLER_X86	= "text/asm";


	/**
	 * Style for highlighting x86 assembler.
	 */
	String SYNTAX_STYLE_ASSEMBLER_6502	= "text/asm6502";


	/**
	 * Style for highlighting BBCode.
	 */
	String SYNTAX_STYLE_BBCODE			= "text/bbcode";


	/**
	 * Style for highlighting C.
	 */
	String SYNTAX_STYLE_C				= "text/c";


	/**
	 * Style for highlighting Clojure.
	 */
	String SYNTAX_STYLE_CLOJURE			= "text/clojure";


	/**
	 * Style for highlighting C++.
	 */
	String SYNTAX_STYLE_CPLUSPLUS		= "text/cpp";


	/**
	 * Style for highlighting C#.
	 */
	String SYNTAX_STYLE_CSHARP			= "text/cs";


	/**
	 * Style for highlighting CSS.
	 */
	String SYNTAX_STYLE_CSS			= "text/css";


	/**
	 * Style for highlighting CSV.
	 */
	String SYNTAX_STYLE_CSV			= "text/csv";


	/**
	 * Syntax style for highlighting D.
	 */
	String SYNTAX_STYLE_D			= "text/d";


	/**
	 * Syntax style for highlighting Dockerfiles.
	 */
	String SYNTAX_STYLE_DOCKERFILE		= "text/dockerfile";


	/**
	 * Style for highlighting Dart.
	 */
	String SYNTAX_STYLE_DART		= "text/dart";


	/**
	 * Style for highlighting Delphi/Pascal.
	 */
	String SYNTAX_STYLE_DELPHI			= "text/delphi";


	/**
	 * Style for highlighting DTD files.
	 */
	String SYNTAX_STYLE_DTD			= "text/dtd";


	/**
	 * Style for highlighting Fortran.
	 */
	String SYNTAX_STYLE_FORTRAN			= "text/fortran";


	/**
	 * Style for highlighting go.
	 */
	String SYNTAX_STYLE_GO				= "text/golang";


	/**
	 * Style for highlighting Groovy.
	 */
	String SYNTAX_STYLE_GROOVY			= "text/groovy";


	/**
	 * Style for highlighting hosts files.
	 */
	String SYNTAX_STYLE_HOSTS			= "text/hosts";


	/**
	 * Style for highlighting .htaccess files.
	 */
	String SYNTAX_STYLE_HTACCESS		= "text/htaccess";


	/**
	 * Style for highlighting HTML.
	 */
	String SYNTAX_STYLE_HTML			= "text/html";


	/**
	 * Style for highlighting INI files.
	 */
	String SYNTAX_STYLE_INI			= "text/ini";


	/**
	 * Style for highlighting Java.
	 */
	String SYNTAX_STYLE_JAVA			= "text/java";


	/**
	 * Style for highlighting JavaScript.
	 */
	String SYNTAX_STYLE_JAVASCRIPT		= "text/javascript";


	/**
	 * Style for highlighting JSON.
	 */
	String SYNTAX_STYLE_JSON		= "text/json";


	/**
	 * Style for highlighting .jshintrc files (JSON with comments, so can be
	 * used for other times when you want this behavior).
	 */
	String SYNTAX_STYLE_JSON_WITH_COMMENTS	= "text/jshintrc";


	/**
	 * Style for highlighting JSP.
	 */
	String SYNTAX_STYLE_JSP			= "text/jsp";


	/**
	 * Style for highlighting LaTeX.
	 */
	String SYNTAX_STYLE_LATEX		= "text/latex";


	/**
	 * Style for highlighting Less.
	 */
	String SYNTAX_STYLE_LESS		= "text/less";


	/**
	 * Style for highlighting Lisp.
	 */
	String SYNTAX_STYLE_LISP		= "text/lisp";


	/**
	 * Style for highlighting Lua.
	 */
	String SYNTAX_STYLE_LUA			= "text/lua";


	/**
	 * Style for highlighting makefiles.
	 */
	String SYNTAX_STYLE_MAKEFILE		= "text/makefile";


	/**
	 * Style for highlighting MXML.
	 */
	String SYNTAX_STYLE_MXML			= "text/mxml";


	/**
	 * Style for highlighting NSIS install scripts.
	 */
	String SYNTAX_STYLE_NSIS			= "text/nsis";


	/**
	 * Style for highlighting Perl.
	 */
	String SYNTAX_STYLE_PERL			= "text/perl";


	/**
	 * Style for highlighting PHP.
	 */
	String SYNTAX_STYLE_PHP				= "text/php";


	/**
	 * Style for highlighting properties files.
	 */
	String SYNTAX_STYLE_PROPERTIES_FILE	= "text/properties";


	/**
	 * Style for highlighting Python.
	 */
	String SYNTAX_STYLE_PYTHON			= "text/python";


	/**
	 * Style for highlighting Ruby.
	 */
	String SYNTAX_STYLE_RUBY			= "text/ruby";


	/**
	 * Style for highlighting SAS keywords.
	 */
	String SYNTAX_STYLE_SAS			= "text/sas";


	/**
	 * Style for highlighting Scala.
	 */
	String SYNTAX_STYLE_SCALA		= "text/scala";


	/**
	 * Style for highlighting SQL.
	 */
	String SYNTAX_STYLE_SQL			= "text/sql";


	/**
	 * Style for highlighting Tcl.
	 */
	String SYNTAX_STYLE_TCL			= "text/tcl";


	/**
	 * Style for highlighting TypeScript.
	 */
	String SYNTAX_STYLE_TYPESCRIPT	= "text/typescript";


	/**
	 * Style for highlighting UNIX shell keywords.
	 */
	String SYNTAX_STYLE_UNIX_SHELL		= "text/unix";


	/**
	 * Style for highlighting Visual Basic.
	 */
	String SYNTAX_STYLE_VISUAL_BASIC	= "text/vb";


	/**
	 * Style for highlighting Windows batch files.
	 */
	String SYNTAX_STYLE_WINDOWS_BATCH	= "text/bat";


	/**
	 * Style for highlighting XML.
	 */
	String SYNTAX_STYLE_XML			= "text/xml";


	/**
	 * Syntax style for highlighting YAML.
	 */
	String SYNTAX_STYLE_YAML		= "text/yaml";


}
