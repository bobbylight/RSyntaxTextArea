/*
 * 03/08/2004
 *
 * SyntaxConstants.java - Constants used by RSyntaxTextArea and friends.
 * Copyright (C) 2004 Robert Futrell
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


/**
 * Constants used by <code>RSyntaxTextArea</code> and friends.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface SyntaxConstants {

	/**
	 * Style meaning don't syntax highlight anything.
	 */
	public static final int NO_SYNTAX_STYLE				= 0;


	/**
	 * Style for highlighting x86 assembler.
	 */
	public static final int ASSEMBLER_X86_SYNTAX_STYLE	= 1;


	/**
	 * Style for highlighting C.
	 */
	public static final int C_SYNTAX_STYLE				= 2;


	/**
	 * Style for highlighting C++.
	 */
	public static final int CPLUSPLUS_SYNTAX_STYLE		= 3;


	/**
	 * Style for highlighting C#.
	 */
	public static final int CSHARP_SYNTAX_STYLE			= 4;


	/**
	 * Style for highlighting CSS.
	 */
	public static final int CSS_SYNTAX_STYLE			= 5;


	/**
	 * Style for highlighting Fortran.
	 */
	public static final int FORTRAN_SYNTAX_STYLE			= 6;


	/**
	 * Style for highlighting Groovy.
	 */
	public static final int GROOVY_SYNTAX_STYLE			= 7;


	/**
	 * Style for highlighting HTML.
	 */
	public static final int HTML_SYNTAX_STYLE			= 8;


	/**
	 * Style for highlighting Java.
	 */
	public static final int JAVA_SYNTAX_STYLE			= 9;


	/**
	 * Style for highlighting JavaScript.
	 */
	public static final int JAVASCRIPT_SYNTAX_STYLE		= 10;


	/**
	 * Style for highlighting JSP.
	 */
	public static final int JSP_SYNTAX_STYLE			= 11;


	/**
	 * Style for highlighting Lua.
	 */
	public static final int LUA_SYNTAX_STYLE			= 12;


	/**
	 * Style for highlighting Perl.
	 */
	public static final int PERL_SYNTAX_STYLE			= 13;


	/**
	 * Style for highlighting properties files.
	 */
	public static final int PROPERTIES_FILE_SYNTAX_STYLE	= 14;


	/**
	 * Style for highlighting Python.
	 */
	public static final int PYTHON_SYNTAX_STYLE			= 15;


	/**
	 * Style for highlighting SAS keywords.
	 */
	public static final int SAS_SYNTAX_STYLE			= 16;


	/**
	 * Style for highlighting SQL.
	 */
	public static final int SQL_SYNTAX_STYLE			= 17;


	/**
	 * Style for highlighting Tcl.
	 */
	public static final int TCL_SYNTAX_STYLE			= 18;


	/**
	 * Style for highlighting UNIX shell keywords.
	 */
	public static final int UNIX_SHELL_SYNTAX_STYLE		= 19;


	/**
	 * Style for highlighting Windows batch files.
	 */
	public static final int WINDOWS_BATCH_SYNTAX_STYLE	= 20;


	/**
	 * Style for highlighting XML.
	 */
	public static final int XML_SYNTAX_STYLE			= 21;


	/**
	 * Style numbers higher than this are invalid.
	 */
	public static final int MAX_SYNTAX_STYLE_NUMBER		= 21;


}