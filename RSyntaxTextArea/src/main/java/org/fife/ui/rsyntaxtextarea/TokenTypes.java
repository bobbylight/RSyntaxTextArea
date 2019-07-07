/*
 * 12/04/2011
 *
 * TokenTypes.java - All token types supported by RSyntaxTextArea.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;


/**
 * All token types supported by RSyntaxTextArea.<p>
 *
 * Note that all valid token types are &gt;= 0, so extensions of the TokenMaker
 * class are free to internally use all ints &lt; 0 ONLY for "end-of-line"
 * style markers; they are ignored by painting implementations.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface TokenTypes {

	/**
	 * Tokens of type <code>NULL</code> mark the end of lines with no
	 * multi-line token at the end (such as a block comment in C++).
	 */
	int NULL							= 0;

	int COMMENT_EOL						= 1;
	int COMMENT_MULTILINE				= 2;
	int COMMENT_DOCUMENTATION			= 3;
	int COMMENT_KEYWORD					= 4;
	int COMMENT_MARKUP					= 5;

	int RESERVED_WORD					= 6;
	int RESERVED_WORD_2					= 7;

	int FUNCTION						= 8;

	int LITERAL_BOOLEAN					= 9;
	int LITERAL_NUMBER_DECIMAL_INT		= 10;
	int LITERAL_NUMBER_FLOAT			= 11;
	int LITERAL_NUMBER_HEXADECIMAL		= 12;
	int LITERAL_STRING_DOUBLE_QUOTE		= 13;
	int LITERAL_CHAR					= 14;
	int LITERAL_BACKQUOTE				= 15;

	int DATA_TYPE						= 16;

	int VARIABLE						= 17;

	int REGEX							= 18;

	int ANNOTATION						= 19;

	int IDENTIFIER						= 20;

	int WHITESPACE						= 21;

	int SEPARATOR						= 22;

	int OPERATOR						= 23;

	int PREPROCESSOR					= 24;

	int MARKUP_TAG_DELIMITER			= 25;
	int MARKUP_TAG_NAME					= 26;
	int MARKUP_TAG_ATTRIBUTE			= 27;
	int MARKUP_TAG_ATTRIBUTE_VALUE		= 28;
	int MARKUP_COMMENT					= 29;
	int MARKUP_DTD						= 30;
	int MARKUP_PROCESSING_INSTRUCTION	= 31;
	int MARKUP_CDATA_DELIMITER			= 32;
	int MARKUP_CDATA					= 33;
	int MARKUP_ENTITY_REFERENCE			= 34;

	int ERROR_IDENTIFIER				= 35;
	int ERROR_NUMBER_FORMAT				= 36;
	int ERROR_STRING_DOUBLE 			= 37;
	int ERROR_CHAR						= 38;

	int DEFAULT_NUM_TOKEN_TYPES = 39;

}
