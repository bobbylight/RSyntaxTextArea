/*
 * 03/23/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link MxmlTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class MxmlTokenMakerTest extends AbstractJFlexTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new MxmlTokenMaker();
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			Assertions.assertFalse(tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	void testMxml_actionScript_booleans() {
		assertAllTokensOfType(TokenTypes.LITERAL_BOOLEAN,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"true",
			"false"
		);
	}


	@Test
	void testMxml_actionScript_chars() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"'c'",
			"'\\n'",
			"'\\f'",
			"'\\r'",
			"'\\n'",
			"'\\b'",
			"'\\t'",
			"'\\uff07'",
			"'more than one character in char'"
		);
	}


	@Test
	void testMxml_actionScript_chars_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"'c",
			"'\\uff07",
			"'\\ufffx'"
		);
	}


	@Test
	void testMxml_actionScript_dataTypes() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"Array",
			"Boolean",
			"Color",
			"Date",
			"Function",
			"int",
			"Key",
			"MovieClip",
			"Math",
			"Mouse",
			"Null",
			"Number",
			"Object",
			"Selection",
			"Sound",
			"String",
			"uint",
			"Vector",
			"XML",
			"XMLNode",
			"XMLSocket"
		);
	}


	@Test
	void testMxml_actionScript_endScriptTag() {

		String code = "</Script>";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, MxmlTokenMaker.INTERNAL_IN_AS, 0);
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "</"));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "Script"));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, ">"));
	}


	@Test
	void testMxml_actionScript_endTag() {

		String code = "</body>";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(segment, 0, 0);
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "</"));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "body"));

		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, ">"));
	}


	@Test
	void testMxml_actionScript_eolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"// Hello world");
	}


	@Test
	void testMxml_actionScript_eolComments_url() {

		String[] eolCommentLiterals = {
			"// Hello world https://www.google.com trailing text",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, MxmlTokenMaker.INTERNAL_IN_AS, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assertions.assertEquals("https://www.google.com", token.getLexeme());

			token = token.getNextToken();
			Assertions.assertTrue(token.is(TokenTypes.COMMENT_EOL, " trailing text"));
		}

	}


	@Test
	void testMxml_actionScript_floatingPointLiterals() {

		String code =
			// Basic doubles
			"3.0 4.2 3.0 4.2 .111 " +
				// Basic floats ending in f, F, d, or D
				"3f 3F 3d 3D 3.f 3.F 3.d 3.D 3.0f 3.0F 3.0d 3.0D .111f .111F .111d .111D " +
				// lower-case exponent, no sign
				"3e7f 3e7F 3e7d 3e7D 3.e7f 3.e7F 3.e7d 3.e7D 3.0e7f 3.0e7F 3.0e7d 3.0e7D .111e7f .111e7F .111e7d .111e7D " +
				// Upper-case exponent, no sign
				"3E7f 3E7F 3E7d 3E7D 3.E7f 3.E7F 3.E7d 3.E7D 3.0E7f 3.0E7F 3.0E7d 3.0E7D .111E7f .111E7F .111E7d .111E7D " +
				// Lower-case exponent, positive
				"3e+7f 3e+7F 3e+7d 3e+7D 3.e+7f 3.e+7F 3.e+7d 3.e+7D 3.0e+7f 3.0e+7F 3.0e+7d 3.0e+7D .111e+7f .111e+7F .111e+7d .111e+7D " +
				// Upper-case exponent, positive
				"3E+7f 3E+7F 3E+7d 3E+7D 3.E+7f 3.E+7F 3.E+7d 3.E+7D 3.0E+7f 3.0E+7F 3.0E+7d 3.0E+7D .111E+7f .111E+7F .111E+7d .111E+7D " +
				// Lower-case exponent, negative
				"3e-7f 3e-7F 3e-7d 3e-7D 3.e-7f 3.e-7F 3.e-7d 3.e-7D 3.0e-7f 3.0e-7F 3.0e-7d 3.0e-7D .111e-7f .111e-7F .111e-7d .111e-7D " +
				// Upper-case exponent, negative
				"3E-7f 3E-7F 3E-7d 3E-7D 3.E-7f 3.E-7F 3.E-7d 3.E-7D 3.0E-7f 3.0E-7F 3.0E-7d 3.0E-7D .111E-7f .111E-7F .111E-7d .111E-7D";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, MxmlTokenMaker.INTERNAL_IN_AS, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(MxmlTokenMaker.INTERNAL_IN_AS, token.getType(), "Expected null, found: " + token);

	}


	@Test
	void testMxml_actionScript_functions() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"call",
			"escape",
			"eval",
			"fscommand",
			"getProperty",
			"getTimer",
			"getURL",
			"getVersion",
			"gotoAndPlay",
			"gotoAndStop",
			"#include",
			"isFinite",
			"isNaN",
			"loadMovie",
			"loadMovieNum",
			"loadVariables",
			"loadVariablesNum",
			"maxscroll",
			"newline",
			"nextFrame",
			"nextScene",
			"parseFloat",
			"parseInt",
			"play",
			"prevFrame",
			"prevScene",
			"print",
			"printAsBitmap",
			"printAsBitmapNum",
			"printNum",
			"random",
			"removeMovieClip",
			"scroll",
			"setProperty",
			"startDrag",
			"stop",
			"stopAllSounds",
			"stopDrag",
			"targetPath",
			"toggleHighQuality",
			"trace",
			"unescape",
			"unloadMovie",
			"unloadMovieNum",
			"updateAfterEvent"
		);
	}


	@Test
	void testMxml_actionScript_hexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
			"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
			"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, MxmlTokenMaker.INTERNAL_IN_AS, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType(), "Invalid hex literal: " + token);
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(MxmlTokenMaker.INTERNAL_IN_AS, token.getType());

	}


	@Test
	void testMxml_actionScript_identifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"foo",
			"bar",
			";",
			",",
			"."
		);
	}


	@Test
	void testMxml_actionScript_integerLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"0",
			"0l",
			"0L",
			"42",
			"42l",
			"42L",
			"123456L"
		);
	}

	@Test
	void testMxml_actionScript_integerLiterals_error() {
		assertAllTokensOfType(TokenTypes.ERROR_NUMBER_FORMAT,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"42rst"
		);
	}


	@Test
	void testMxml_actionScript_multiLineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"/* Hello world unterminated",
			"/* Hello world */",
			"/**/"
		);
	}


	@Test
	void testMxml_actionScript_multiLineComments_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			MxmlTokenMaker.INTERNAL_IN_AS_MLC,
			"continued from a previous ine and unterminated",
			"continued from a previous line */"
		);
	}


	@Test
	void testMxml_actionScript_multiLineComments_URL() {

		String[] mlcLiterals = {
			"/* Hello world https://www.google.com */",
		};

		for (String code : mlcLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, MxmlTokenMaker.INTERNAL_IN_AS, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assertions.assertEquals("https://www.google.com", token.getLexeme());

			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assertions.assertEquals(" */", token.getLexeme());

		}

	}


	@Test
	void testMxml_actionScript_operators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ | && >>>";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, MxmlTokenMaker.INTERNAL_IN_AS, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.OPERATOR, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "), "Not a single space: " + token);
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(MxmlTokenMaker.INTERNAL_IN_AS, token.getType());

	}


	@Test
	void testMxml_actionScript_reservedWords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"add",
			"and",
			"break",
			"case",
			"catch",
			"class",
			"const",
			"continue",
			"default",
			"delete",
			"do",
			"dynamic",
			"else",
			"eq",
			"extends",
			"final",
			"finally",
			"for",
			"for each",
			"function",
			"ge",
			"get",
			"gt",
			"if",
			"ifFrameLoaded",
			"implements",
			"import",
			"in",
			"include",
			"interface",
			"internal",
			"label",
			"le",
			"lt",
			"namespace",
			"native",
			"ne",
			"new",
			"not",
			"on",
			"onClipEvent",
			"or",
			"override",
			"package",
			"private",
			"protected",
			"public",
			"return",
			"set",
			"static",
			"super",
			"switch",
			"tellTarget",
			"this",
			"throw",
			"try",
			"typeof",
			"use",
			"var",
			"void",
			"while",
			"with",
			"null",
			"undefined"
			);
	}


	@Test
	void testMxml_actionScript_separators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"(",
			")",
			"{",
			"}",
			"[",
			"]"
		);
	}


	@Test
	void testMxml_actionScript_strings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"\"closed string\"",
			"\"string with \\n\\f\\t\\r\\b\\\" escapes\""
		);
	}


	@Test
	void testMxml_actionScript_strings_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			MxmlTokenMaker.INTERNAL_IN_AS,
			"\"unclosed string"
		);
	}


	@Test
	void testMxml_actionScript_whitespace() {
		assertAllTokensOfType(TokenTypes.WHITESPACE,
			MxmlTokenMaker.INTERNAL_IN_AS,
			" ",
			"   ",
			"\t",
			"  \t  "
		);
	}


	@Test
	void testMxml_cdata_happyPath() {

		String code = "<![CDATA[foo]]>;";
		Segment seg = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(seg, TokenTypes.NULL, 0);
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_CDATA_DELIMITER, "<![CDATA["));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_CDATA, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_CDATA_DELIMITER, "]]>"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));
	}

	@Test
	void testMxml_cdata_continuedFromPriorLine() {

		String code = "foo]]>;";
		Segment seg = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(seg, TokenTypes.MARKUP_CDATA, 0);
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_CDATA, "foo"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_CDATA_DELIMITER, "]]>"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.IDENTIFIER, ";"));
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("<!--", startAndEnd[0]);
		Assertions.assertEquals("-->", startAndEnd[1]);
	}


	@Test
	void testMxml_comment() {
		assertAllTokensOfType(TokenTypes.MARKUP_COMMENT,
			"<!-- Hello world -->",
			"<!-- unclosed"
		);
	}


	@Test
	void testMxml_comment_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_COMMENT,
			TokenTypes.MARKUP_COMMENT,
			"continued from prior line and closed -->",
			"continued from prior line and unclosed"
		);
	}


	@Test
	void testMxml_comment_URL() {

		String code = "<!-- Hello world https://www.google.com -->";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "<!-- Hello world "), "Bad MARKUP_COMMENT: " + token);
		token = token.getNextToken();
		Assertions.assertTrue(token.isHyperlink());
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, "https://www.google.com"));
		token = token.getNextToken();
		Assertions.assertFalse(token.isHyperlink());
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_COMMENT, " -->"));

	}


	@Test
	void testMxml_doctype() {

		String[] doctypes = {
			"<!doctype html>",
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">",
			"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">",
			"<!DOCTYPE note [ xxx ]>",
		};

		for (String code : doctypes) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.MARKUP_DTD, token.getType());
		}

	}


	@Test
	void testMXML_doctype_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_DTD,
			TokenTypes.MARKUP_DTD,
			"continued from prior line unterminated",
			"continued from prior line>"
		);
	}


	@Test
	void testMxml_entityReferences() {
		assertAllTokensOfType(TokenTypes.MARKUP_ENTITY_REFERENCE,
			"&nbsp;", "&lt;", "&gt;", "&#4012"
			);
	}


	@Test
	void testXML_getSetCompleteCloseTags() {
		try {
			Assertions.assertTrue(MxmlTokenMaker.getCompleteCloseMarkupTags());
			MxmlTokenMaker.setCompleteCloseTags(false);
			Assertions.assertFalse(MxmlTokenMaker.getCompleteCloseMarkupTags());
		} finally {
			MxmlTokenMaker.setCompleteCloseTags(true);
		}
	}


	@Test
	void testMxml_happyPath_tagWithAttributes() {

		String code = "<body onload=\"doSomething()\" data-extra='true'>";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<'));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "body"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "onload"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "\"doSomething()\""), "Unexpected token: " + token);
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "data-extra"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "'true'"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '>'));

	}


	@Test
	void testMXML_inAttrDouble() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			MxmlTokenMaker.INTERNAL_ATTR_DOUBLE,
			"continued to next line",
			"ends on this line\""
		);
	}


	@Test
	void testMXML_inAttrDouble_scriptTag() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			MxmlTokenMaker.INTERNAL_ATTR_DOUBLE_QUOTE_SCRIPT,
			"continued to next line",
			"ends on this line\""
		);
	}


	@Test
	void testMXML_inAttrSingle() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			MxmlTokenMaker.INTERNAL_ATTR_SINGLE,
			"continued to next line",
			"ends on this line'"
		);
	}


	@Test
	void testMXML_inAttrSingle_scriptTag() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			MxmlTokenMaker.INTERNAL_ATTR_SINGLE_QUOTE_SCRIPT,
			"continued to next line",
			"ends on this line'"
		);
	}


	@Test
	void testMXML_inProcessingInstruction() {
		assertAllTokensOfType(TokenTypes.MARKUP_PROCESSING_INSTRUCTION,
			TokenTypes.MARKUP_PROCESSING_INSTRUCTION,
			"continued to next line",
			"ends on this line ?>"
		);
	}


	@Test
	void testMXML_inTag_attributeNames() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE,
			MxmlTokenMaker.INTERNAL_INTAG,
			"foo",
			"value123",
			"cszčšž",
			"xmlns:cszčšž"
		);
	}


	@Test
	void testMXML_inTagScript_attributeNames() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE,
			MxmlTokenMaker.INTERNAL_INTAG_SCRIPT,
			"foo",
			"value123",
			"cszčšž",
			"xmlns:cszčšž"
		);
	}


	@Test
	void testMxml_processingInstructions() {

		String[] doctypes = {
			"<?xml version=\"1.0\" encoding=\"UTF-8\" ?>",
			"<?xml version='1.0' encoding='UTF-8' ?>",
			"<?xml-stylesheet type=\"text/css\" href=\"style.css\"?>",
		};

		for (String code : doctypes) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.MARKUP_PROCESSING_INSTRUCTION, token.getType());
		}

	}


	@Test
	void testMxml_nonScriptTags() {
		String[] startTags = {
			"<foo attr=\"attr\"",
			"<bar other-attr=\"other-attr\""
		};

		for (String code : startTags) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "<"));

			token = token.getNextToken();
			String tagName = code.substring(1, code.indexOf(' '));
			Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, tagName));

			token = token.getNextToken();
			Assertions.assertTrue(token.isSingleChar(TokenTypes.WHITESPACE, ' '));

			token = token.getNextToken();
			String attr = code.substring(code.indexOf(' ') + 1, code.indexOf('='));
			Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, attr));

			token = token.getNextToken();
			Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));

			token = token.getNextToken();
			Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, '"' + attr + '"'));
		}
	}
}
