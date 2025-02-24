/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


/**
 * Unit tests for the {@link PHPTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class PHPTokenMakerTest extends AbstractJFlexTokenMakerTest {

	/**
	 * The last token type on the previous line for this token maker to
	 * start parsing a new line as CSS.  This constant is only here so we can
	 * copy and paste tests from this class into others, such as HTML, PHP, and
	 * JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_CSS;

	/**
	 * The last token type on the previous line for this token maker to
	 * start parsing a new line as part of a CSS multi-line comment.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as JSP, PHP, and PHP token maker tests, with as little change as possible.
	 */
	private static final int CSS_MLC_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_CSS_MLC;

	/**
	 * The last token type on the previous line for this token maker to start
	 * parsing a new line as part of a CSS property key/value block.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_PROPERTY_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_CSS_PROPERTY;

	/**
	 * The last token type on the previous line for this token maker to start
	 * parsing a new line as part of a CSS property key/value block.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_VALUE_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_CSS_VALUE;

	/**
	 * The last token type on the previous line for this token maker to start
	 * parsing a new line as part of a string property value.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_STRING_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_CSS_STRING;

	/**
	 * The last token type on the previous line for this token maker to start
	 * parsing a new line as part of a char property value.  This constant
	 * is only here so we can copy and paste tests from this class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as possible.
	 */
	private static final int CSS_CHAR_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_CSS_CHAR;

	private static final int HTML_ATTR_DOUBLE_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_ATTR_DOUBLE;

	private static final int HTML_ATTR_SINGLE_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_ATTR_SINGLE;

	private static final int HTML_ATTR_SCRIPT_DOUBLE_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_ATTR_DOUBLE_QUOTE_SCRIPT;

	private static final int HTML_ATTR_SCRIPT_SINGLE_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_ATTR_SINGLE_QUOTE_SCRIPT;

	private static final int HTML_ATTR_STYLE_DOUBLE_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_ATTR_DOUBLE_QUOTE_STYLE;

	private static final int HTML_ATTR_STYLE_SINGLE_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_ATTR_SINGLE_QUOTE_STYLE;

	private static final int HTML_INTAG_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_INTAG;

	private static final int HTML_INTAG_SCRIPT_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_INTAG_SCRIPT;

	private static final int HTML_INTAG_STYLE_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_INTAG_STYLE;

	/**
	 * The last token type on the previous line for this token maker to
	 * start parsing a new line as JS.  This constant is only here so we can
	 * copy and paste tests from the JavaScriptTokenMakerTest class into others,
	 * such as HTML, PHP, and JSP token maker tests, with as little change as
	 * possible.
	 */
	private static final int JS_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_IN_JS;

	private static final int JS_DOC_COMMENT_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_IN_JS_COMMENT_DOCUMENTATION;

	private static final int JS_MLC_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_IN_JS_MLC;

	private static final int JS_INVALID_STRING_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_IN_JS_STRING_INVALID;

	private static final int JS_VALID_STRING_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_IN_JS_STRING_VALID;

	private static final int JS_INVALID_CHAR_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_IN_JS_CHAR_INVALID;

	private static final int JS_VALID_CHAR_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_IN_JS_CHAR_VALID;

	private static final int JS_INVALID_TEMPLATE_LITERAL_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_IN_JS_TEMPLATE_LITERAL_INVALID;

	private static final int JS_VALID_TEMPLATE_LITERAL_PREV_TOKEN_TYPE = PHPTokenMaker.INTERNAL_IN_JS_TEMPLATE_LITERAL_VALID;


	/**
	 * Creates an instance of the {@code TokenMaker} to test.
	 *
	 * @return The token maker to test.
	 */
	@Override
	protected TokenMaker createTokenMaker() {
		return new PHPTokenMaker();
	}


	@Test
	@Override
	protected void testCommon_getCurlyBracesDenoteCodeBlocks() {
		TokenMaker tm = createTokenMaker();
		Assertions.assertFalse(tm.getCurlyBracesDenoteCodeBlocks(
			PHPTokenMaker.LANG_INDEX_DEFAULT));
		Assertions.assertTrue(tm.getCurlyBracesDenoteCodeBlocks(
			PHPTokenMaker.LANG_INDEX_CSS));
		Assertions.assertTrue(tm.getCurlyBracesDenoteCodeBlocks(
			PHPTokenMaker.LANG_INDEX_JS));
		Assertions.assertTrue(tm.getCurlyBracesDenoteCodeBlocks(
			PHPTokenMaker.LANG_INDEX_PHP));
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {

		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(PHPTokenMaker.LANG_INDEX_DEFAULT);
		Assertions.assertEquals("<!--", startAndEnd[0]);
		Assertions.assertEquals("-->", startAndEnd[1]);

		startAndEnd = createTokenMaker().getLineCommentStartAndEnd(PHPTokenMaker.LANG_INDEX_JS);
		Assertions.assertEquals("//", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);

		startAndEnd = createTokenMaker().getLineCommentStartAndEnd(PHPTokenMaker.LANG_INDEX_CSS);
		Assertions.assertEquals("/*", startAndEnd[0]);
		Assertions.assertEquals("*/", startAndEnd[1]);

		startAndEnd = createTokenMaker().getLineCommentStartAndEnd(PHPTokenMaker.LANG_INDEX_PHP);
		Assertions.assertEquals("//", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i  == TokenTypes.FUNCTION || i == TokenTypes.VARIABLE ||
				i == TokenTypes.MARKUP_TAG_NAME;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}
	@Test
	void testCommon_getShouldIndentNextLineAfter_null() {
		Assertions.assertFalse(createTokenMaker().getShouldIndentNextLineAfter(null));
	}


	@Test
	void testCommon_getShouldIndentNextLineAfter_nullToken() {
		Assertions.assertFalse(createTokenMaker().getShouldIndentNextLineAfter(new TokenImpl()));
	}


	@Test
	void testCommon_getShouldIndentNextLineAfter_css_afterCurly() {
		Segment seg = createSegment("{");
		Token token = new TokenImpl(
			seg, 0, 0, 0, TokenTypes.SEPARATOR, PHPTokenMaker.LANG_INDEX_CSS);
		Assertions.assertTrue(createTokenMaker().getShouldIndentNextLineAfter(token));
	}


	@Test
	void testCommon_getShouldIndentNextLineAfter_css_afterParen() {
		Segment seg = createSegment("(");
		Token token = new TokenImpl(
			seg, 0, 0, 0, TokenTypes.SEPARATOR, PHPTokenMaker.LANG_INDEX_CSS);
		Assertions.assertTrue(createTokenMaker().getShouldIndentNextLineAfter(token));
	}


	@Test
	void testCommon_getShouldIndentNextLineAfter_css_afterRandomSingleCharToken() {
		Segment seg = createSegment("x");
		Token token = new TokenImpl(
			seg, 0, 0, 0, TokenTypes.IDENTIFIER, PHPTokenMaker.LANG_INDEX_CSS);
		Assertions.assertFalse(createTokenMaker().getShouldIndentNextLineAfter(token));
	}


	@Test
	void testCommon_getShouldIndentNextLineAfter_css_afterRandomMultiCharToken() {
		Segment seg = createSegment("xx");
		Token token = new TokenImpl(
			seg, 0, 1, 0, TokenTypes.IDENTIFIER, PHPTokenMaker.LANG_INDEX_CSS);
		Assertions.assertFalse(createTokenMaker().getShouldIndentNextLineAfter(token));
	}


	@Test
	void testCommon_getShouldIndentNextLineAfter_js_afterCurly() {
		Segment seg = createSegment("{");
		Token token = new TokenImpl(
			seg, 0, 0, 0, TokenTypes.SEPARATOR, PHPTokenMaker.LANG_INDEX_JS);
		Assertions.assertTrue(createTokenMaker().getShouldIndentNextLineAfter(token));
	}


	@Test
	void testCommon_getShouldIndentNextLineAfter_js_afterParen() {
		Segment seg = createSegment("(");
		Token token = new TokenImpl(
			seg, 0, 0, 0, TokenTypes.SEPARATOR, PHPTokenMaker.LANG_INDEX_JS);
		Assertions.assertTrue(createTokenMaker().getShouldIndentNextLineAfter(token));
	}


	@Test
	void testCommon_getShouldIndentNextLineAfter_js_afterRandomSingleCharToken() {
		Segment seg = createSegment("x");
		Token token = new TokenImpl(
			seg, 0, 0, 0, TokenTypes.IDENTIFIER, PHPTokenMaker.LANG_INDEX_JS);
		Assertions.assertFalse(createTokenMaker().getShouldIndentNextLineAfter(token));
	}


	@Test
	void testCommon_getShouldIndentNextLineAfter_js_afterRandomMultiCharToken() {
		Segment seg = createSegment("xx");
		Token token = new TokenImpl(
			seg, 0, 1, 0, TokenTypes.IDENTIFIER, PHPTokenMaker.LANG_INDEX_JS);
		Assertions.assertFalse(createTokenMaker().getShouldIndentNextLineAfter(token));
	}


	@Test
	void testPhp_BooleanLiterals() {

		String code = "<?php true false TRUE FALSE ?>";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");

		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "<?php"), "Unexpected token: " + token);
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();

		for (int i = 1; i < 5; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_BOOLEAN, token.getType());
			token = token.getNextToken();
			Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
			Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			token = token.getNextToken();
		}

		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "?>"));
		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testPhp_CharLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			PHPTokenMaker.INTERNAL_IN_PHP,
			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'", "'\\u00fe'",
			"'\\u00FE'", "'\\111'", "'\\222'", "'\\333'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'"
		);
	}


	@Test
	void testPhp_CharLiterals_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			PHPTokenMaker.INTERNAL_IN_PHP_CHAR,
			"rest of the string'");
	}


	@Test
	void testPhp_CreateOccurrenceMarker() {
		PHPTokenMaker tm = (PHPTokenMaker)createTokenMaker();
		Assertions.assertInstanceOf(HtmlOccurrenceMarker.class, tm.createOccurrenceMarker());
	}


	@Test
	void testPhp_EolComments() {

		String[] eolCommentLiterals = {
			"// Hello world",
			"# Hello world"
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}

	/* Not in PHPTokenMaker ???
	@Test
	public void testPhp_EolComments_URL() {

		String[] eolCommentLiterals = {
			"// Hello world https://www.sas.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assertions.assertEquals("https://www.sas.com", token.getLexeme());

		}

	}
	*/


	@Test
	@Disabled("Not sure why this fails")
	void testPhp_errorControlOperators() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			PHPTokenMaker.INTERNAL_IN_PHP,
			"@foo",
			"@bar"
		);
	}


	@Test
	void testPhp_errorNumericLiterals() {
		assertAllTokensOfType(TokenTypes.ERROR_NUMBER_FORMAT,
			PHPTokenMaker.INTERNAL_IN_PHP,
			"42foo",
			"1e17foo",
			"0x1ffoo"
		);
	}


	@Test
	void testPhp_FloatingPointLiterals() {

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
		Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);

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

		Assertions.assertEquals(PHPTokenMaker.INTERNAL_IN_PHP, token.getType());

	}


	@Test
	void testPhp_Functions() {

		assertAllTokensOfType(TokenTypes.FUNCTION, PHPTokenMaker.INTERNAL_IN_PHP,
			"__call",
			"__construct",
			"__getfunctions",
			"__getlastrequest",
			"__getlastresponse",
			"__gettypes",
			"__tostring",
			"abs",
			"acos",
			"acosh",
			"add",
			//("add"("_namespace"|"_root"|"action"|"color"|"cslashes"|"entry"|"fill"|"function"|"shape"|"slashes"|"string")),
			"aggregate",
			"aggregate_info",
			"aggregate_methods",
			"aggregate_methods_by_list",
			"aggregate_methods_by_regexp",
			"aggregate_properties",
			"aggregate_properties_by_list",
			"aggregate_properties_by_regexp",
			"aggregation_info",
			"align",
			//("apd_"("breakpoint"|"callstack"|"clunk"|"continue"|"croak"|"dump_function_table"|"dump_persistent_resources"|"dump_regular_resources"|"echo"|"get_active_symbols"|"set_pprof_trace"|"set_session"|"set_session_trace"|"set_socket_session_trace")),
			"append",
			"append_child",
			"append_sibling",
			"appendchild",
			"appenddata",
			//("array_"("change_key_case"|"chunk"|"combine"|"count_values"|"diff"|"diff_assoc"|"diff_key"|"diff_uassoc"|"diff_ukey"|"fill"|"filter"|"flip"|"intersect"|"intersect_assoc"|"intersect_key"|"intersect_uassoc"|"intersect_ukey"|"key_exists"|"keys"|"map"|"merge"|"merge_recursive"|"multisort"|"pad"|"pop"|"push"|"rand"|"reduce"|"reverse"|"search"|"shift"|"slice"|"splice"|"sum"|"udiff"|"udiff_assoc"|"udiff_uassoc"|"uintersect"|"uintersect_assoc"|"uintersect_uassoc"|"unique"|"unshift"|"values"|"walk"|"walk_recursive")),
			"arsort",
			"ascii2ebcdic",
			"asin",
			"asinh",
			"asort",
			"assert",
			"assert_options",
			"assign",
			"assignelem",
			"asxml",
			"atan",
			"atan2",
			"atanh",
			"attreditable",
			"attributes",
			"base64_decode",
			"base64_encode",
			"base_convert",
			"basename",
			"bcadd",
			"bccomp",
			"bcdiv",
			"bcmod",
			"bcmul",
			"bcpow",
			"bcpowmod",
			"bcscale",
			"bcsqrt",
			"bcsub",
			"begintransaction",
			"bin2hex",
			"bind_textdomain_codeset",
			"bindcolumn",
			"bindec",
			"bindparam",
			"bindtextdomain",
			"bzclose",
			"bzcompress",
			"bzdecompress",
			"bzerrno",
			"bzerror",
			"bzerrstr",
			"bzflush",
			"bzopen",
			"bzread",
			"bzwrite",
			"cal_days_in_month",
			"cal_from_jd",
			"cal_info",
			"cal_to_jd",
			"call_user_func",
			"call_user_func_array",
			"call_user_method",
			"call_user_method_array",
			//("ccvs_"("add"|"auth"|"command"|"count"|"delete"|"done"|"init"|"lookup"|"new"|"report"|"return"|"reverse"|"sale"|"status"|"textvalue"|"void")),
			"ceil",
			"chdir",
			"checkdate",
			"checkdnsrr",
			"checkin",
			"checkout",
			"chgrp",
			"child_nodes",
			"children",
			"chmod",
			"chop",
			"chown",
			"chr",
			"chroot",
			"chunk_split",
			"class_exists",
			"class_implements",
			"class_parents",
			"classkit_import",
			"classkit_method_add",
			"classkit_method_copy",
			"classkit_method_redefine",
			"classkit_method_remove",
			"classkit_method_rename",
			"clearstatcache",
			"clone_node",
			"clonenode",
			"close",
			"closedir",
			"closelog",
			"com",
			"commit",
			"compact",
			"connect",
			"connection_aborted",
			"connection_status",
			"connection_timeout",
			"constant",
			"content",
			"convert_cyr_string",
			"convert_uudecode",
			"convert_uuencode",
			"copy",
			"cos",
			"cosh",
			"count",
			"count_chars",
			"crack_check",
			"crack_closedict",
			"crack_getlastmessage",
			"crack_opendict",
			"crc32",
			//("create"("_attribute"|"_cdata_section"|"_comment"|"_element"|"_element_ns"|"_entity_reference"|"_function"|"_processing_instruction"|"_text_node"|"attribute"|"attributens"|"cdatasection"|"comment"|"document"|"documentfragment"|"documenttype"|"element"|"elementns"|"entityreference"|"processinginstruction"|"textnode")),
			"crypt",
			//("curl_"("close"|"copy_handle"|"errno"|"error"|"exec"|"getinfo"|"init"|"multi_add_handle"|"multi_close"|"multi_exec"|"multi_getcontent"|"multi_info_read"|"multi_init"|"multi_remove_handle"|"multi_select"|"setopt"|"version")),
			"current",
			"cybercash_base64_decode",
			"cybercash_base64_encode",
			"cybercash_decr",
			"cybercash_encr",
			"cyrus_authenticate",
			"cyrus_bind",
			"cyrus_close",
			"cyrus_connect",
			"cyrus_query",
			"cyrus_unbind",
			"data",
			"date",
			"date_sunrise",
			"date_sunset",
			"dblist",
			"dbmclose",
			"dbmdelete",
			"dbmexists",
			"dbmfetch",
			"dbmfirstkey",
			"dbminsert",
			"dbmnextkey",
			"dbmopen",
			"dbmreplace",
			"dbstat",
			"dcgettext",
			"dcngettext",
			"dcstat",
			"deaggregate",
			"debug_backtrace",
			"debug_print_backtrace",
			"debug_zval_dump",
			"debugger_off",
			"debugger_on",
			"decbin",
			"dechex",
			"decoct",
			"decrement",
			"define",
			"define_syslog_variables",
			"defined",
			"deg2rad",
			"delete",
			"deletedata",
			"description",
			"dgettext",
			//("dio_"("close"|"fcntl"|"open"|"read"|"seek"|"stat"|"tcsetattr"|"truncate"|"write")),
			"dir",
			"dirname",
			"disk_free_space",
			"disk_total_space",
			"diskfreespace",
			"dl",
			"dngettext",
			"dns_check_record",
			"dns_get_mx",
			"dns_get_record",
			"doctype",
			"document_element",
			"dom_import_simplexml",
			//("domxml_"("new_doc"|"open_file"|"open_mem"|"version"|"xmltree"|"xslt_stylesheet"|"xslt_stylesheet_doc"|"xslt_stylesheet_file")),
			"dotnet",
			"dotnet_load",
			"doubleval",
			"drawcurve",
			"drawcurveto",
			"drawline",
			"drawlineto",
			"dstanchors",
			"dstofsrcanchors",
			"dump_file",
			"dump_mem",
			"dump_node",
			"each",
			"easter_date",
			"easter_days",
			"ebcdic2ascii",
			"end",
			"entities",
			"eof",
			"erase",
			"ereg",
			"ereg_replace",
			"eregi",
			"eregi_replace",
			"error_log",
			"error_reporting",
			"errorcode",
			"errorinfo",
			"escapeshellarg",
			"escapeshellcmd",
			"exec",
			"execute",
			"exif_imagetype",
			"exif_read_data",
			"exif_tagname",
			"exif_thumbnail",
			"exp",
			"explode",
			"expm1",
			"export",
			"extension_loaded",
			"extract",
			"ezmlm_hash",
			"fclose",
			"feof",
			"fetch",
			"fetchall",
			"fetchsingle",
			"fflush",
			"fgetc",
			"fgetcsv",
			"fgets",
			"fgetss",
			"file",
			//("file"("_exists"|"_get_contents"|"_put_contents"|"atime"|"ctime"|"group"|"inode"|"mtime"|"owner"|"perms"|"pro"|"pro_fieldcount"|"pro_fieldname"|"pro_fieldtype"|"pro_fieldwidth"|"pro_retrieve"|"pro_rowcount"|"size"|"type")),
			"find",
			"first_child",
			"floatval",
			"flock",
			"floor",
			"flush",
			"fmod",
			"fnmatch",
			"fopen",
			"fpassthru",
			"fprintf",
			"fputcsv",
			"fputs",
			"fread",
			"free",
			"frenchtojd",
			"fribidi_log2vis",
			"fscanf",
			"fseek",
			"fsockopen",
			"fstat",
			"ftell",
			"ftok",
			//("ftp_"("alloc"|"cdup"|"chdir"|"chmod"|"close"|"connect"|"delete"|"exec"|"fget"|"fput"|"get"|"get_option"|"login"|"mdtm"|"mkdir"|"nb_continue"|"nb_fget"|"nb_fput"|"nb_get"|"nb_put"|"nlist"|"pasv"|"put"|"pwd"|"quit"|"raw"|"rawlist"|"rename"|"rmdir"|"set_option"|"site"|"size"|"ssl_connect"|"systype")),
			"ftruncate",
			"ftstat",
			"func_get_arg",
			"func_get_args",
			"func_num_args",
			"function_exists",
			"fwrite",
			"gd_info",
			"get",
			//("get"("_attr"|"_attribute"|"_attribute_node"|"_browser"|"_cfg_var"|"_class"|"_class_methods"|"_class_vars"|"_content"|"_current_user"|"_declared_classes"|"_declared_interfaces"|"_defined_constants"|"_defined_functions"|"_defined_vars"|"_element_by_id"|"_elements_by_tagname"|"_extension_funcs"|"_headers"|"_html_translation_table"|"_include_path"|"_included_files"|"_loaded_extensions"|"_magic_quotes_gpc"|"_magic_quotes_runtime"|"_meta_tags"|"_nodes"|"_object_vars"|"_parent_class"|"_required_files"|"_resource_type"|"allheaders"|"atime"|"attr"|"attribute"|"attributenode"|"attributenodens"|"attributens"|"buffering"|"children"|"crc"|"ctime"|"cwd"|"date"|"depth"|"elem"|"elementbyid"|"elementsbytagname"|"elementsbytagnamens"|"env"|"filename"|"filetime"|"functions"|"group"|"height"|"hostbyaddr"|"hostbyname"|"hostbynamel"|"hostos"|"imagesize"|"inneriterator"|"inode"|"iterator"|"lastmod"|"method"|"mtime"|"mxrr"|"mygid"|"myinode"|"mypid"|"myuid"|"name"|"nameditem"|"nameditemns"|"opt"|"owner"|"packedsize"|"path"|"pathname"|"perms"|"position"|"protobyname"|"protobynumber"|"randmax"|"rusage"|"servbyname"|"servbyport"|"shape1"|"shape2"|"size"|"stats"|"subiterator"|"text"|"timeofday"|"type"|"unpackedsize"|"version"|"width")),
			"glob",
			"gmdate",
			"gmmktime",
			//("gmp_"("abs"|"add"|"and"|"clrbit"|"cmp"|"com"|"div"|"div_q"|"div_qr"|"div_r"|"divexact"|"fact"|"gcd"|"gcdext"|"hamdist"|"init"|"intval"|"invert"|"jacobi"|"legendre"|"mod"|"mul"|"neg"|"or"|"perfect_square"|"popcount"|"pow"|"powm"|"prob_prime"|"random"|"scan0"|"scan1"|"setbit"|"sign"|"sqrt"|"sqrtrem"|"strval"|"sub"|"xor")),
			"gmstrftime",
			"gregoriantojd",
			//("gz"("close"|"compress"|"deflate"|"encode"|"eof"|"file"|"getc"|"gets"|"getss"|"inflate"|"open"|"passthru"|"puts"|"read"|"rewind"|"seek"|"tell"|"uncompress"|"write")),
			"handle",
			//("has"("_attribute"|"_attributes"|"_child_nodes"|"attribute"|"attributens"|"attributes"|"childnodes"|"children"|"feature"|"next"|"siblings")),
			"header",
			"headers_list",
			"headers_sent",
			"hebrev",
			"hebrevc",
			"hexdec",
			"highlight_file",
			"highlight_string",
			"html_dump_mem",
			"html_entity_decode",
			"htmlentities",
			"htmlspecialchars",
			//("http_"("build_query"|"response_code")),
			//("hw_"("array2objrec"|"changeobject"|"children"|"childrenobj"|"close"|"connect"|"connection_info"|"cp"|"deleteobject"|"docbyanchor"|"docbyanchorobj"|"document_attributes"|"document_bodytag"|"document_content"|"document_setcontent"|"document_size"|"dummy"|"edittext"|"error"|"errormsg"|"free_document"|"getanchors"|"getanchorsobj"|"getandlock"|"getchildcoll"|"getchildcollobj"|"getchilddoccoll"|"getchilddoccollobj"|"getobject"|"getobjectbyquery"|"getobjectbyquerycoll"|"getobjectbyquerycollobj"|"getobjectbyqueryobj"|"getparents"|"getparentsobj"|"getrellink"|"getremote"|"getremotechildren"|"getsrcbydestobj"|"gettext"|"getusername"|"identify"|"incollections"|"info"|"inscoll"|"insdoc"|"insertanchors"|"insertdocument"|"insertobject"|"mapid"|"modifyobject"|"mv"|"new_document"|"objrec2array"|"output_document"|"pconnect"|"pipedocument"|"root"|"setlinkroot"|"stat"|"unlock"|"who")),
			"hwapi_hgcsp",
			"hwstat",
			"hypot",
			//("ibase_"("add_user"|"affected_rows"|"backup"|"blob_add"|"blob_cancel"|"blob_close"|"blob_create"|"blob_echo"|"blob_get"|"blob_import"|"blob_info"|"blob_open"|"close"|"commit"|"commit_ret"|"connect"|"db_info"|"delete_user"|"drop_db"|"errcode"|"errmsg"|"execute"|"fetch_assoc"|"fetch_object"|"fetch_row"|"field_info"|"free_event_handler"|"free_query"|"free_result"|"gen_id"|"maintain_db"|"modify_user"|"name_result"|"num_fields"|"num_params"|"param_info"|"pconnect"|"prepare"|"query"|"restore"|"rollback"|"rollback_ret"|"server_info"|"service_attach"|"service_detach"|"set_event_handler"|"timefmt"|"trans"|"wait_event")),
			"iconv",
			//("iconv_"("get_encoding"|"mime_decode"|"mime_decode_headers"|"mime_encode"|"set_encoding"|"strlen"|"strpos"|"strrpos"|"substr")),
			"identify",
			"ignore_user_abort",
			//("image"("2wbmp"|"_type_to_extension"|"_type_to_mime_type"|"alphablending"|"antialias"|"arc"|"char"|"charup"|"colorallocate"|"colorallocatealpha"|"colorat"|"colorclosest"|"colorclosestalpha"|"colorclosesthwb"|"colordeallocate"|"colorexact"|"colorexactalpha"|"colormatch"|"colorresolve"|"colorresolvealpha"|"colorset"|"colorsforindex"|"colorstotal"|"colortransparent"|"copy"|"copymerge"|"copymergegray"|"copyresampled"|"copyresized"|"create"|"createfromgd"|"createfromgd2"|"createfromgd2part"|"createfromgif"|"createfromjpeg"|"createfrompng"|"createfromstring"|"createfromwbmp"|"createfromxbm"|"createfromxpm"|"createtruecolor"|"dashedline"|"destroy"|"ellipse"|"fill"|"filledarc"|"filledellipse"|"filledpolygon"|"filledrectangle"|"filltoborder"|"filter"|"fontheight"|"fontwidth"|"ftbbox"|"fttext"|"gammacorrect"|"gd"|"gd2"|"gif"|"interlace"|"istruecolor"|"jpeg"|"layereffect"|"line"|"loadfont"|"palettecopy"|"png"|"polygon"|"psbbox"|"pscopyfont"|"psencodefont"|"psextendfont"|"psfreefont"|"psloadfont"|"psslantfont"|"pstext"|"rectangle"|"rotate"|"savealpha"|"setbrush"|"setpixel"|"setstyle"|"setthickness"|"settile"|"string"|"stringup"|"sx"|"sy"|"truecolortopalette"|"ttfbbox"|"ttftext"|"types"|"wbmp"|"xbm")),
			//("imap_"("8bit"|"alerts"|"append"|"base64"|"binary"|"body"|"bodystruct"|"check"|"clearflag_full"|"close"|"createmailbox"|"delete"|"deletemailbox"|"errors"|"expunge"|"fetch_overview"|"fetchbody"|"fetchheader"|"fetchstructure"|"get_quota"|"get_quotaroot"|"getacl"|"getmailboxes"|"getsubscribed"|"header"|"headerinfo"|"headers"|"last_error"|"list"|"listmailbox"|"listscan"|"listsubscribed"|"lsub"|"mail"|"mail_compose"|"mail_copy"|"mail_move"|"mailboxmsginfo"|"mime_header_decode"|"msgno"|"num_msg"|"num_recent"|"open"|"ping"|"qprint"|"renamemailbox"|"reopen"|"rfc822_parse_adrlist"|"rfc822_parse_headers"|"rfc822_write_address"|"scanmailbox"|"search"|"set_quota"|"setacl"|"setflag_full"|"sort"|"status"|"subscribe"|"thread"|"timeout"|"uid"|"undelete"|"unsubscribe"|"utf7_decode"|"utf7_encode"|"utf8")),
			"implode",
			"import",
			"import_request_variables",
			"importnode",
			"in_array",
			"increment",
			"inet_ntop",
			"inet_pton",
			"info",
			//("ini_"("alter"|"get"|"get_all"|"restore"|"set")),
			"insert",
			"insert_before",
			"insertanchor",
			"insertbefore",
			"insertcollection",
			"insertdata",
			"insertdocument",
			"interface_exists",
			"internal_subset",
			"intval",
			"ip2long",
			"iptcembed",
			"iptcparse",
			//("is_"("a"|"array"|"blank_node"|"bool"|"callable"|"dir"|"double"|"executable"|"file"|"finite"|"float"|"infinite"|"int"|"integer"|"link"|"long"|"nan"|"null"|"numeric"|"object"|"readable"|"real"|"resource"|"scalar"|"soap_fault"|"string"|"subclass_of"|"uploaded_file"|"writable"|"writeable")),
			//("is"("asp"|"comment"|"dir"|"dot"|"executable"|"file"|"html"|"id"|"jste"|"link"|"php"|"readable"|"samenode"|"supported"|"text"|"whitespaceinelementcontent"|"writable"|"xhtml"|"xml")),
			"item",
			"iterator_count",
			"iterator_to_array",
			"java_last_exception_clear",
			"java_last_exception_get",
			"jddayofweek",
			"jdmonthname",
			"jdtofrench",
			"jdtogregorian",
			"jdtojewish",
			"jdtojulian",
			"jdtounix",
			"jewishtojd",
			"join",
			"jpeg2wbmp",
			//("json_"("decode"|"encode"|"last_error"("_msg")?)),
			"juliantojd",
			"key",
			"krsort",
			"ksort",
			"langdepvalue",
			"last_child",
			"lastinsertid",
			"lcg_value",
			//("ldap_"("8859_to_t61"|"add"|"bind"|"close"|"compare"|"connect"|"count_entries"|"delete"|"dn2ufn"|"err2str"|"errno"|"error"|"explode_dn"|"first_attribute"|"first_entry"|"first_reference"|"free_result"|"get_attributes"|"get_dn"|"get_entries"|"get_option"|"get_values"|"get_values_len"|"list"|"mod_add"|"mod_del"|"mod_replace"|"modify"|"next_attribute"|"next_entry"|"next_reference"|"parse_reference"|"parse_result"|"read"|"rename"|"sasl_bind"|"search"|"set_option"|"set_rebind_proc"|"sort"|"start_tls"|"t61_to_8859"|"unbind")),
			"levenshtein",
			"link",
			"linkinfo",
			"load",
			"loadhtml",
			"loadhtmlfile",
			"loadxml",
			"localeconv",
			"localtime",
			"lock",
			"log",
			"log10",
			"log1p",
			"long2ip",
			"lookupnamespaceuri",
			"lookupprefix",
			"lstat",
			"ltrim",
			"lzf_compress",
			"lzf_decompress",
			"lzf_optimized_for",
			"mail",
			"main",
			"max",
			//("mb_"("convert_case"|"convert_encoding"|"convert_kana"|"convert_variables"|"decode_mimeheader"|"decode_numericentity"|"detect_encoding"|"detect_order"|"encode_mimeheader"|"encode_numericentity"|"ereg"|"ereg_match"|"ereg_replace"|"ereg_search"|"ereg_search_getpos"|"ereg_search_getregs"|"ereg_search_init"|"ereg_search_pos"|"ereg_search_regs"|"ereg_search_setpos"|"eregi"|"eregi_replace"|"get_info"|"http_input"|"http_output"|"internal_encoding"|"language"|"list_encodings"|"output_handler"|"parse_str"|"preferred_mime_name"|"regex_encoding"|"regex_set_options"|"send_mail"|"split"|"strcut"|"strimwidth"|"strlen"|"strpos"|"strrpos"|"strtolower"|"strtoupper"|"strwidth"|"substitute_character"|"substr"|"substr_count")),
			//("mcal_"("append_event"|"close"|"create_calendar"|"date_compare"|"date_valid"|"day_of_week"|"day_of_year"|"days_in_month"|"delete_calendar"|"delete_event"|"event_add_attribute"|"event_init"|"event_set_alarm"|"event_set_category"|"event_set_class"|"event_set_description"|"event_set_end"|"event_set_recur_daily"|"event_set_recur_monthly_mday"|"event_set_recur_monthly_wday"|"event_set_recur_none"|"event_set_recur_weekly"|"event_set_recur_yearly"|"event_set_start"|"event_set_title"|"expunge"|"fetch_current_stream_event"|"fetch_event"|"is_leap_year"|"list_alarms"|"list_events"|"next_recurrence"|"open"|"popen"|"rename_calendar"|"reopen"|"snooze"|"store_event"|"time_valid"|"week_of_year")),
			//("mcrypt_"("cbc"|"cfb"|"create_iv"|"decrypt"|"ecb"|"enc_get_algorithms_name"|"enc_get_block_size"|"enc_get_iv_size"|"enc_get_key_size"|"enc_get_modes_name"|"enc_get_supported_key_sizes"|"enc_is_block_algorithm"|"enc_is_block_algorithm_mode"|"enc_is_block_mode"|"enc_self_test"|"encrypt"|"generic"|"generic_deinit"|"generic_end"|"generic_init"|"get_block_size"|"get_cipher_name"|"get_iv_size"|"get_key_size"|"list_algorithms"|"list_modes"|"module_close"|"module_get_algo_block_size"|"module_get_algo_key_size"|"module_get_supported_key_sizes"|"module_is_block_algorithm"|"module_is_block_algorithm_mode"|"module_is_block_mode"|"module_open"|"module_self_test"|"ofb")),
			"md5",
			"md5_file",
			"mdecrypt_generic",
			"memcache_debug",
			"memory_get_usage",
			"metaphone",
			"method_exists",
			"mhash",
			"mhash_count",
			"mhash_get_block_size",
			"mhash_get_hash_name",
			"mhash_keygen_s2k",
			"microtime",
			"mime_content_type",
			"mimetype",
			"min",
			"ming_setcubicthreshold",
			"ming_setscale",
			"ming_useswfversion",
			"mkdir",
			"mktime",
			"money_format",
			"move",
			"move_uploaded_file",
			"movepen",
			"movepento",
			"moveto",
			//("msession_"("connect"|"count"|"create"|"destroy"|"disconnect"|"find"|"get"|"get_array"|"get_data"|"inc"|"list"|"listvar"|"lock"|"plugin"|"randstr"|"set"|"set_array"|"set_data"|"timeout"|"uniq"|"unlock")),
			"msg_get_queue",
			"msg_receive",
			"msg_remove_queue",
			"msg_send",
			"msg_set_queue",
			"msg_stat_queue",
			"msql",
			"mt_getrandmax",
			"mt_rand",
			"mt_srand",
			"name",
			"natcasesort",
			"natsort",
			"next",
			"next_sibling",
			"nextframe",
			"ngettext",
			"nl2br",
			"nl_langinfo",
			//("node_"("name"|"type"|"value")),
			"normalize",
			"notations",
			//("notes_"("body"|"copy_db"|"create_db"|"create_note"|"drop_db"|"find_note"|"header_info"|"list_msgs"|"mark_read"|"mark_unread"|"nav_create"|"search"|"unread"|"version")),
			//("nsapi_"("request_headers"|"response_headers"|"virtual")),
			"number_format",
			//("ob_"("clean"|"end_clean"|"end_flush"|"flush"|"get_clean"|"get_contents"|"get_flush"|"get_length"|"get_level"|"get_status"|"gzhandler"|"iconv_handler"|"implicit_flush"|"list_handlers"|"start"|"tidyhandler")),
			"object",
			"objectbyanchor",
			//("oci"("_bind_by_name"|"_cancel"|"_close"|"_commit"|"_connect"|"_define_by_name"|"_error"|"_execute"|"_fetch"|"_fetch_all"|"_fetch_array"|"_fetch_assoc"|"_fetch_object"|"_fetch_row"|"_field_is_null"|"_field_name"|"_field_precision"|"_field_scale"|"_field_size"|"_field_type"|"_field_type_raw"|"_free_statement"|"_internal_debug"|"_lob_copy"|"_lob_is_equal"|"_new_collection"|"_new_connect"|"_new_cursor"|"_new_descriptor"|"_num_fields"|"_num_rows"|"_parse"|"_password_change"|"_pconnect"|"_result"|"_rollback"|"_server_version"|"_set_prefetch"|"_statement_type"|"bindbyname"|"cancel"|"closelob"|"collappend"|"collassign"|"collassignelem"|"collgetelem"|"collmax"|"collsize"|"colltrim"|"columnisnull"|"columnname"|"columnprecision"|"columnscale"|"columnsize"|"columntype"|"columntyperaw"|"commit"|"definebyname"|"error"|"execute"|"fetch"|"fetchinto"|"fetchstatement"|"freecollection"|"freecursor"|"freedesc"|"freestatement"|"internaldebug"|"loadlob"|"logoff"|"logon"|"newcollection"|"newcursor"|"newdescriptor"|"nlogon"|"numcols"|"parse"|"plogon"|"result"|"rollback"|"rowcount"|"savelob"|"savelobfile"|"serverversion"|"setprefetch"|"statementtype"|"writelobtofile"|"writetemporarylob")),
			"octdec",
			//("odbc_"("autocommit"|"binmode"|"close"|"close_all"|"columnprivileges"|"columns"|"commit"|"connect"|"cursor"|"data_source"|"do"|"error"|"errormsg"|"exec"|"execute"|"fetch_array"|"fetch_into"|"fetch_object"|"fetch_row"|"field_len"|"field_name"|"field_num"|"field_precision"|"field_scale"|"field_type"|"foreignkeys"|"free_result"|"gettypeinfo"|"longreadlen"|"next_result"|"num_fields"|"num_rows"|"pconnect"|"prepare"|"primarykeys"|"procedurecolumns"|"procedures"|"result"|"result_all"|"rollback"|"setoption"|"specialcolumns"|"statistics"|"tableprivileges"|"tables")),
			"offsetexists",
			"offsetget",
			"offsetset",
			"offsetunset",
			"opendir",
			"openlog",
			//("openssl_"("csr_export"|"csr_export_to_file"|"csr_new"|"csr_sign"|"error_string"|"free_key"|"get_privatekey"|"get_publickey"|"open"|"pkcs7_decrypt"|"pkcs7_encrypt"|"pkcs7_sign"|"pkcs7_verify"|"pkey_export"|"pkey_export_to_file"|"pkey_get_private"|"pkey_get_public"|"pkey_new"|"private_decrypt"|"private_encrypt"|"public_decrypt"|"public_encrypt"|"seal"|"sign"|"verify"|"x509_check_private_key"|"x509_checkpurpose"|"x509_export"|"x509_export_to_file"|"x509_free"|"x509_parse"|"x509_read")),
			//("ora_"("bind"|"close"|"columnname"|"columnsize"|"columntype"|"commit"|"commitoff"|"commiton"|"do"|"error"|"errorcode"|"exec"|"fetch"|"fetch_into"|"getcolumn"|"logoff"|"logon"|"numcols"|"numrows"|"open"|"parse"|"plogon"|"rollback")),
			"ord",
			"output",
			"output_add_rewrite_var",
			"output_reset_rewrite_vars",
			"overload",
			"override_function",
			"owner_document",
			"pack",
			"parent_node",
			"parents",
			"parse_ini_file",
			"parse_str",
			"parse_url",
			"parsekit_compile_file",
			"parsekit_compile_string",
			"parsekit_func_arginfo",
			"passthru",
			"pathinfo",
			"pclose",
			//("pcntl_"("alarm"|"exec"|"fork"|"getpriority"|"setpriority"|"signal"|"wait"|"waitpid"|"wexitstatus"|"wifexited"|"wifsignaled"|"wifstopped"|"wstopsig"|"wtermsig")),
			"pconnect",
			//("pdf_"("add_annotation"|"add_bookmark"|"add_launchlink"|"add_locallink"|"add_note"|"add_outline"|"add_pdflink"|"add_thumbnail"|"add_weblink"|"arc"|"arcn"|"attach_file"|"begin_page"|"begin_pattern"|"begin_template"|"circle"|"clip"|"close"|"close_image"|"close_pdi"|"close_pdi_page"|"closepath"|"closepath_fill_stroke"|"closepath_stroke"|"concat"|"continue_text"|"curveto"|"delete"|"end_page"|"end_pattern"|"end_template"|"endpath"|"fill"|"fill_stroke"|"findfont"|"fit_pdi_page"|"get_buffer"|"get_font"|"get_fontname"|"get_fontsize"|"get_image_height"|"get_image_width"|"get_majorversion"|"get_minorversion"|"get_parameter"|"get_pdi_parameter"|"get_pdi_value"|"get_value"|"initgraphics"|"lineto"|"load_font"|"makespotcolor"|"moveto"|"new"|"open"|"open_ccitt"|"open_file"|"open_gif"|"open_image"|"open_image_file"|"open_jpeg"|"open_memory_image"|"open_pdi"|"open_pdi_page"|"open_png"|"open_tiff"|"place_image"|"place_pdi_page"|"rect"|"restore"|"rotate"|"save"|"scale"|"set_border_color"|"set_border_dash"|"set_border_style"|"set_char_spacing"|"set_duration"|"set_font"|"set_horiz_scaling"|"set_info"|"set_info_author"|"set_info_creator"|"set_info_keywords"|"set_info_subject"|"set_info_title"|"set_leading"|"set_parameter"|"set_text_matrix"|"set_text_pos"|"set_text_rendering"|"set_text_rise"|"set_value"|"set_word_spacing"|"setcolor"|"setdash"|"setflat"|"setfont"|"setgray"|"setgray_fill"|"setgray_stroke"|"setlinecap"|"setlinejoin"|"setlinewidth"|"setmatrix"|"setmiterlimit"|"setpolydash"|"setrgbcolor"|"setrgbcolor_fill"|"setrgbcolor_stroke"|"show"|"show_boxed"|"show_xy"|"skew"|"stringwidth"|"stroke"|"translate")),
			"pfpro_cleanup",
			"pfpro_init",
			"pfpro_process",
			"pfpro_process_raw",
			"pfpro_version",
			"pfsockopen",
			//("pg_"("affected_rows"|"cancel_query"|"client_encoding"|"close"|"connect"|"connection_busy"|"connection_reset"|"connection_status"|"convert"|"copy_from"|"copy_to"|"dbname"|"delete"|"end_copy"|"escape_bytea"|"escape_string"|"fetch_all"|"fetch_array"|"fetch_assoc"|"fetch_object"|"fetch_result"|"fetch_row"|"field_is_null"|"field_name"|"field_num"|"field_prtlen"|"field_size"|"field_type"|"free_result"|"get_notify"|"get_pid"|"get_result"|"host"|"insert"|"last_error"|"last_notice"|"last_oid"|"lo_close"|"lo_create"|"lo_export"|"lo_import"|"lo_open"|"lo_read"|"lo_read_all"|"lo_seek"|"lo_tell"|"lo_unlink"|"lo_write"|"meta_data"|"num_fields"|"num_rows"|"options"|"parameter_status"|"pconnect"|"ping"|"port"|"put_line"|"query"|"result_error"|"result_seek"|"result_status"|"select"|"send_query"|"set_client_encoding"|"trace"|"tty"|"unescape_bytea"|"untrace"|"update"|"version")),
			"php_check_syntax",
			"php_ini_scanned_files",
			"php_logo_guid",
			"php_sapi_name",
			"php_strip_whitespace",
			"php_uname",
			"phpcredits",
			"phpinfo",
			"phpversion",
			"pi",
			"png2wbmp",
			"popen",
			"pos",
			//("posix_"("ctermid"|"get_last_error"|"getcwd"|"getegid"|"geteuid"|"getgid"|"getgrgid"|"getgrnam"|"getgroups"|"getlogin"|"getpgid"|"getpgrp"|"getpid"|"getppid"|"getpwnam"|"getpwuid"|"getrlimit"|"getsid"|"getuid"|"isatty"|"kill"|"mkfifo"|"setegid"|"seteuid"|"setgid"|"setpgid"|"setsid"|"setuid"|"strerror"|"times"|"ttyname"|"uname")),
			"pow",
			"prefix",
			"preg_grep",
			"preg_match",
			"preg_match_all",
			"preg_quote",
			"preg_replace",
			"preg_replace_callback",
			"preg_split",
			"prepare",
			"prev",
			"previous_sibling",
			"print_r",
			//("printer_"("abort"|"close"|"create_brush"|"create_dc"|"create_font"|"create_pen"|"delete_brush"|"delete_dc"|"delete_font"|"delete_pen"|"draw_bmp"|"draw_chord"|"draw_elipse"|"draw_line"|"draw_pie"|"draw_rectangle"|"draw_roundrect"|"draw_text"|"end_doc"|"end_page"|"get_option"|"list"|"logical_fontheight"|"open"|"select_brush"|"select_font"|"select_pen"|"set_option"|"start_doc"|"start_page"|"write")),
			"printf",
			"proc_close",
			"proc_get_status",
			"proc_nice",
			"proc_open",
			"proc_terminate",
			"process",
			"public_id",
			"putenv",
			"qdom_error",
			"qdom_tree",
			"query",
			"quoted_printable_decode",
			"quotemeta",
			"rad2deg",
			"rand",
			"range",
			"rar_close",
			"rar_entry_get",
			"rar_list",
			"rar_open",
			"rawurldecode",
			"rawurlencode",
			"read",
			"read_exif_data",
			"readdir",
			"readfile",
			"readgzfile",
			"readline",
			//("readline_"("add_history"|"callback_handler_install"|"callback_handler_remove"|"callback_read_char"|"clear_history"|"completion_function"|"info"|"list_history"|"on_new_line"|"read_history"|"redisplay"|"write_history")),
			"readlink",
			"realpath",
			"reason",
			"recode",
			"recode_file",
			"recode_string",
			"register_shutdown_function",
			"register_tick_function",
			"registernamespace",
			"relaxngvalidate",
			"relaxngvalidatesource",
			"remove",
			"remove_attribute",
			"remove_child",
			"removeattribute",
			"removeattributenode",
			"removeattributens",
			"removechild",
			"rename",
			"rename_function",
			"replace",
			"replace_child",
			"replace_node",
			"replacechild",
			"replacedata",
			"reset",
			"restore_error_handler",
			"restore_exception_handler",
			"restore_include_path",
			"result_dump_file",
			"result_dump_mem",
			"rewind",
			"rewinddir",
			"rmdir",
			"rollback",
			"rotate",
			"rotateto",
			"round",
			"rowcount",
			"rsort",
			"rtrim",
			"save",
			"savehtml",
			"savehtmlfile",
			"savexml",
			"scale",
			"scaleto",
			"scandir",
			"schemavalidate",
			"schemavalidatesource",
			"seek",
			"sem_acquire",
			"sem_get",
			"sem_release",
			"sem_remove",
			"serialize",
			//("sesam_"("affected_rows"|"commit"|"connect"|"diagnostic"|"disconnect"|"errormsg"|"execimm"|"fetch_array"|"fetch_result"|"fetch_row"|"field_array"|"field_name"|"free_result"|"num_fields"|"query"|"rollback"|"seek_row"|"sesam_settransaction")),
			//("session_"("cache_expire"|"cache_limiter"|"commit"|"decode"|"destroy"|"encode"|"get_cookie_params"|"id"|"is_registered"|"module_name"|"name"|"regenerate_id"|"register"|"save_path"|"set_cookie_params"|"set_save_handler"|"start"|"unregister"|"unset"|"write_close")),
			"set",
			//("set"("_attribute"|"_content"|"_error_handler"|"_exception_handler"|"_file_buffer"|"_include_path"|"_magic_quotes_runtime"|"_name"|"_namespace"|"_time_limit"|"action"|"attribute"|"attributenode"|"attributenodens"|"attributens"|"background"|"bounds"|"buffering"|"class"|"color"|"commitedversion"|"cookie"|"depth"|"dimension"|"down"|"font"|"frames"|"height"|"hit"|"indentation"|"leftfill"|"leftmargin"|"line"|"linespacing"|"locale"|"margins"|"name"|"over"|"persistence"|"rate"|"ratio"|"rawcookie"|"rightfill"|"rightmargin"|"spacing"|"type"|"up")),
			"sha1",
			"sha1_file",
			"shell_exec",
			"shm_attach",
			"shm_detach",
			"shm_get_var",
			"shm_put_var",
			"shm_remove",
			"shm_remove_var",
			"shmop_close",
			"shmop_delete",
			"shmop_open",
			"shmop_read",
			"shmop_size",
			"shmop_write",
			"show_source",
			"shuffle",
			"similar_text",
			"simplexml_import_dom",
			"simplexml_load_file",
			"simplexml_load_string",
			"sin",
			"sinh",
			"size",
			"sizeof",
			"skewx",
			"skewxto",
			"skewy",
			"skewyto",
			"sleep",
			//("socket_"("accept"|"bind"|"clear_error"|"close"|"connect"|"create"|"create_listen"|"create_pair"|"get_option"|"get_status"|"getpeername"|"getsockname"|"last_error"|"listen"|"read"|"recv"|"recvfrom"|"select"|"send"|"sendto"|"set_block"|"set_blocking"|"set_nonblock"|"set_option"|"set_timeout"|"shutdown"|"strerror"|"write")),
			"sort",
			"soundex",
			"specified",
			"spl_classes",
			"split",
			"spliti",
			"splittext",
			"sprintf",
			"sql_regcase",
			"sqrt",
			"srand",
			"srcanchors",
			"srcsofdst",
			"sscanf",
			"stat",
			//("str_"("ireplace"|"pad"|"repeat"|"replace"|"rot13"|"shuffle"|"split"|"word_count")),
			"strcasecmp",
			"strchr",
			"strcmp",
			"strcoll",
			"strcspn",
			//("stream_"("context_create"|"context_get_default"|"context_get_options"|"context_set_option"|"context_set_params"|"copy_to_stream"|"filter_append"|"filter_prepend"|"filter_register"|"filter_remove"|"get_contents"|"get_filters"|"get_line"|"get_meta_data"|"get_transports"|"get_wrappers"|"register_wrapper"|"select"|"set_blocking"|"set_timeout"|"set_write_buffer"|"socket_accept"|"socket_client"|"socket_enable_crypto"|"socket_get_name"|"socket_recvfrom"|"socket_sendto"|"socket_server"|"wrapper_register"|"wrapper_restore"|"wrapper_unregister")),
			//("str"("eammp3"|"ftime"|"ip_tags"|"ipcslashes"|"ipos"|"ipslashes"|"istr"|"len"|"natcasecmp"|"natcmp"|"ncasecmp"|"ncmp"|"pbrk"|"pos"|"ptime"|"rchr"|"rev"|"ripos"|"rpos"|"spn"|"str"|"tok"|"tolower"|"totime"|"toupper"|"tr"|"val")),
			"substr",
			"substr_compare",
			"substr_count",
			"substr_replace",
			"substringdata",
			//("swf_"("actiongeturl"|"actiongotoframe"|"actiongotolabel"|"actionnextframe"|"actionplay"|"actionprevframe"|"actionsettarget"|"actionstop"|"actiontogglequality"|"actionwaitforframe"|"addbuttonrecord"|"addcolor"|"closefile"|"definebitmap"|"definefont"|"defineline"|"definepoly"|"definerect"|"definetext"|"endbutton"|"enddoaction"|"endshape"|"endsymbol"|"fontsize"|"fontslant"|"fonttracking"|"getbitmapinfo"|"getfontinfo"|"getframe"|"labelframe"|"lookat"|"modifyobject"|"mulcolor"|"nextid"|"oncondition"|"openfile"|"ortho"|"ortho2"|"perspective"|"placeobject"|"polarview"|"popmatrix"|"posround"|"pushmatrix"|"removeobject"|"rotate"|"scale"|"setfont"|"setframe"|"shapearc"|"shapecurveto"|"shapecurveto3"|"shapefillbitmapclip"|"shapefillbitmaptile"|"shapefilloff"|"shapefillsolid"|"shapelinesolid"|"shapelineto"|"shapemoveto"|"showframe"|"startbutton"|"startdoaction"|"startshape"|"startsymbol"|"textwidth"|"translate"|"viewport")),
			"swfbutton_keypress",
			"symlink",
			"syslog",
			"system",
			"system_id",
			"tagname",
			"tan",
			"tanh",
			"target",
			"tcpwrap_check",
			"tell",
			"tempnam",
			"textdomain",
			//("tidy_"("access_count"|"clean_repair"|"config_count"|"diagnose"|"error_count"|"get_body"|"get_config"|"get_error_buffer"|"get_head"|"get_html"|"get_html_ver"|"get_output"|"get_release"|"get_root"|"get_status"|"getopt"|"is_xhtml"|"is_xml"|"load_config"|"parse_file"|"parse_string"|"repair_file"|"repair_string"|"reset_config"|"save_config"|"set_encoding"|"setopt"|"warning_count")),
			"time",
			"time_nanosleep",
			"title",
			"tmpfile",
			"token_get_all",
			"token_name",
			"touch",
			"trigger_error",
			"trim",
			"truncate",
			"type",
			"uasort",
			"ucfirst",
			"ucwords",
			"uksort",
			"umask",
			"uniqid",
			"unixtojd",
			"unlink",
			"unlink_node",
			"unlock",
			"unpack",
			"unregister_tick_function",
			"unserialize",
			"urldecode",
			"urlencode",
			"user",
			"user_error",
			"userlist",
			"usleep",
			"usort",
			"utf8_decode",
			"utf8_encode",
			"valid",
			"validate",
			"value",
			"values",
			//"var_"("dump"|"export"),
			//("variant_"("abs"|"add"|"and"|"cast"|"cat"|"cmp"|"date_from_timestamp"|"date_to_timestamp"|"div"|"eqv"|"fix"|"get_type"|"idiv"|"imp"|"int"|"mod"|"mul"|"neg"|"not"|"or"|"pow"|"round"|"set"|"set_type"|"sub"|"xor")),
			"version_compare",
			"vfprintf",
			"virtual",
			//("vpopmail_"("add_alias_domain"|"add_alias_domain_ex"|"add_domain"|"add_domain_ex"|"add_user"|"alias_add"|"alias_del"|"alias_del_domain"|"alias_get"|"alias_get_all"|"auth_user"|"del_domain"|"del_domain_ex"|"del_user"|"error"|"passwd"|"set_user_quota")),
			"vprintf",
			"vsprintf",
			//("w32api_"("deftype"|"init_dtype"|"invoke_function"|"register_function"|"set_call_method")),
			"wddx_add_vars",
			"wddx_deserialize",
			"wddx_packet_end",
			"wddx_packet_start",
			"wddx_serialize_value",
			"wddx_serialize_vars",
			"wordwrap",
			"write",
			"writetemporary",
			"xattr_get",
			"xattr_list",
			"xattr_remove",
			"xattr_set",
			"xattr_supported",
			"xinclude",
			//("xml"("_error_string"|"_get_current_byte_index"|"_get_current_column_number"|"_get_current_line_number"|"_get_error_code"|"_parse"|"_parse_into_struct"|"_parser_create"|"_parser_create_ns"|"_parser_free"|"_parser_get_option"|"_parser_set_option"|"_set_character_data_handler"|"_set_default_handler"|"_set_element_handler"|"_set_end_namespace_decl_handler"|"_set_external_entity_ref_handler"|"_set_notation_decl_handler"|"_set_object"|"_set_processing_instruction_handler"|"_set_start_namespace_decl_handler"|"_set_unparsed_entity_decl_handler"|"rpc_decode"|"rpc_decode_request"|"rpc_encode"|"rpc_encode_request"|"rpc_get_type"|"rpc_is_fault"|"rpc_parse_method_descriptions"|"rpc_server_add_introspection_data"|"rpc_server_call_method"|"rpc_server_create"|"rpc_server_destroy"|"rpc_server_register_introspection_callback"|"rpc_server_register_method"|"rpc_set_type")),
			"xpath",
			"xpath_eval",
			"xpath_eval_expression",
			"xpath_new_context",
			"xptr_eval",
			"xptr_new_context",
			//("xsl_xsltprocessor_"("get_parameter"|"has_exslt_support"|"import_stylesheet"|"register_php_functions"|"remove_parameter"|"set_parameter"|"transform_to_doc"|"transform_to_uri"|"transform_to_xml"|"xslt_backend_info")),
			//("xslt_"("backend_name"|"backend_version"|"create"|"errno"|"error"|"free"|"getopt"|"process"|"set_base"|"set_encoding"|"set_error_handler"|"set_log"|"set_object"|"set_sax_handler"|"set_sax_handlers"|"set_scheme_handler"|"set_scheme_handlers"|"setopt")),
			//("yp_"("all"|"cat"|"err_string"|"errno"|"first"|"get_default_domain"|"master"|"match"|"next"|"yp_order")),
			"zend_logo_guid",
			"zend_version",
			//("zip_"("close"|"entry_close"|"entry_compressedsize"|"entry_compressionmethod"|"entry_filesize"|"entry_name"|"entry_open"|"entry_read"|"open"|"read")),
			"zlib_get_coding_type",

			/* mysql functions */
			//("mysql_"("affected_rows"|"client_encoding"|"close"|"connect"|"create_db"|"data_seek"|"db_"("name"|"query")|"drop_db"|"errno"|"error"|"escape_string"|"fetch_"("array"|"assoc"|"field"|"lengths"|"object"|"row")|"field_"("flags"|"len"|"name"|"seek"|"table"|"type")|"free_result"|"get_"("client_info"|"host_info"|"proto_info"|"server_info")|"info"|"insert_id"|"list_"("dbs"|"fields"|"processes"|"tables")|"num_"("fields"|"rows")|"pconnect"|"ping"|"query"|"real_escape_string"|"result"|"select_db"|"set_charset"|"stat"|"tablename"|"thread_id"|"unbuffered_query")),

			/* Function aliases */
			"apache_request_headers",
			"apache_response_headers",
			"attr_get",
			"attr_set",
			"autocommit",
			"bind_param",
			"bind_result",
			"bzclose",
			"bzflush",
			"bzwrite",
			"change_user",
			"character_set_name",
			"checkdnsrr",
			"chop",
			"client_encoding",
			"close",
			"commit",
			"connect",
			"data_seek",
			"debug",
			"disable_reads_from_master",
			"disable_rpl_parse",
			"diskfreespace",
			"doubleval",
			"dump_debug_info",
			"enable_reads_from_master",
			"enable_rpl_parse",
			"escape_string",
			"execute",
			"fbsql",
			"fbsql_tablename",
			"fetch",
			"fetch_array",
			"fetch_assoc",
			"fetch_field",
			"fetch_field_direct",
			"fetch_fields",
			"fetch_object",
			"fetch_row",
			"field_count",
			"field_seek",
			"fputs",
			"free",
			"free_result",
			"ftp_quit",
			"get_client_info",
			"get_required_files",
			"get_server_info",
			"getallheaders",
			"getmxrr",
			"gmp_div",
			//("gz"("close"|"eof"|"getc"|"gets"|"getss"|"passthru"|"puts"|"read"|"rewind"|"seek"|"tell"|"gzwrite")),
			"imap_create",
			"imap_fetchtext",
			"imap_header",
			"imap_listmailbox",
			"imap_listsubscribed",
			"imap_rename",
			"ini_alter",
			"init",
			"is_double",
			"is_int",
			"is_integer",
			"is_real",
			"is_writeable",
			"join",
			"key_exists",
			"kill",
			"ldap_close",
			"ldap_modify",
			"magic_quotes_runtime",
			"master_query",
			"ming_keypress",
			"ming_setcubicthreshold",
			"ming_setscale",
			"ming_useconstants",
			"ming_useswfversion",
			"more_results",
			"next_result",
			"num_rows",
			//("oci"("_free_cursor"|"bindbyname"|"cancel"|"collappend"|"collassignelem"|"collgetelem"|"collmax"|"collsize"|"colltrim"|"columnisnull"|"columnname"|"columnprecision"|"columnscale"|"columnsize"|"columntype"|"columntyperaw"|"commit"|"definebyname"|"error"|"execute"|"fetch"|"fetchstatement"|"freecollection"|"freecursor"|"freedesc"|"freestatement"|"internaldebug"|"loadlob"|"logoff"|"logon"|"newcollection"|"newcursor"|"newdescriptor"|"nlogon"|"numcols"|"parse"|"passwordchange"|"plogon"|"result"|"rollback"|"rowcount"|"savelob"|"savelobfile"|"serverversion"|"setprefetch"|"statementtype"|"writelobtofile")),
			"odbc_do",
			"odbc_field_precision",
			"openssl_free_key",
			"openssl_get_privatekey",
			"openssl_get_publickey",
			"options",
			//("pg_"("clientencoding"|"cmdtuples"|"errormessage"|"exec"|"fieldisnull"|"fieldname"|"fieldnum"|"fieldprtlen"|"fieldsize"|"fieldtype"|"freeresult"|"getlastoid"|"loclose"|"locreate"|"loexport"|"loimport"|"loopen"|"loread"|"loreadall"|"lounlink"|"lowrite"|"numfields"|"numrows"|"result"|"setclientencoding")),
			"ping",
			"pos",
			"posix_errno",
			"prepare",
			"query",
			"read_exif_data",
			"real_connect",
			"real_escape_string",
			"real_query",
			"recode",
			"reset",
			"result_metadata",
			"rollback",
			"rpl_parse_enabled",
			"rpl_probe",
			"rpl_query_type",
			"select_db",
			"send_long_data",
			"session_commit",
			"set_file_buffer",
			"set_local_infile_default",
			"set_local_infile_handler",
			"set_opt",
			"show_source",
			"sizeof",
			"slave_query",
			"snmpwalkoid",
			"socket_get_status",
			"socket_getopt",
			"socket_set_blocking",
			"socket_set_timeout",
			"socket_setopt",
			"sqlite_fetch_string",
			"sqlite_has_more",
			"ssl_set",
			"stat",
			"stmt",
			"stmt_init",
			"store_result",
			"strchr",
			"stream_register_wrapper",
			"thread_safe",
			"use_result",
			"user_error",
			//("velocis_"("autocommit"|"close"|"commit"|"connect"|"exec"|"fetch"|"fieldname"|"fieldnum"|"freeresult"|"off_autocommit"|"result"|"rollback")),
			"virtual"
		);
	}


	@Test
	void testPhp_GetSetCompleteClosingTags() {
		try {
			PHPTokenMaker tm = (PHPTokenMaker)createTokenMaker();
			Assertions.assertFalse(tm.getCompleteCloseTags());
			PHPTokenMaker.setCompleteCloseTags(true);
			Assertions.assertTrue(tm.getCompleteCloseTags());
		} finally {
			PHPTokenMaker.setCompleteCloseTags(false);
		}
	}


	@Test
	void testPhp_HexLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_HEXADECIMAL,
			PHPTokenMaker.INTERNAL_IN_PHP,
			"0x1f", "0x1fl", "0x1fL",
			"0X1f", "0X1fl", "0X1fL",
			"0x1F", "0x1Fl", "0x1FL",
			"0X1F", "0X1Fl", "0X1FL",
			"071" // Octal numbers are rendered with hex styling
		);
	}


	@Test
	void testPhp_IntegerLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			PHPTokenMaker.INTERNAL_IN_PHP,
			"0", "42", /*"-7",*/
			"0l", "42l",
			"0L", "42L"
		);
	}


	@Test
	void testPhp_Keywords() {

		assertAllTokensOfType(TokenTypes.RESERVED_WORD, PHPTokenMaker.INTERNAL_IN_PHP,
			"__CLASS__",
			"__DIR__",
			"__FILE__",
			"__FUNCTION__",
			"__METHOD__",
			"__NAMESPACE__",
			"abstract",
			"and",
			"array",
			"as",
			"break",
			"case",
			"catch",
			"cfunction",
			"class",
			"clone",
			"const",
			"continue",
			"declare",
			"default",
			"die",
			"do",
			"echo",
			"else",
			"elseif",
			"empty",
			"enddeclare",
			"endfor",
			"endforeach",
			"endif",
			"endswitch",
			"endwhile",
			"eval",
			"extends",
			"final",
			"for",
			"foreach",
			"function",
			"global",
			"goto",
			"if",
			"implements",
			"include",
			"include_once",
			"interface",
			"instanceof",
			"isset",
			"list",
			"namespace",
			"new",
			"old_function",
			"or",
			"print",
			"private",
			"protected",
			"public",
			"require",
			"require_once",
			"static",
			"switch",
			"throw",
			"try",
			"unset",
			"use",
			"var",
			"while",
			"xor",

			"parent",
			"self",
			"stdClass")
		;

	}


	@Test
	void testPhp_Keywords2() {

		String code = "exit return";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.RESERVED_WORD_2, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(PHPTokenMaker.INTERNAL_IN_PHP, token.getType());

	}


	@Test
	void testPhp_MultiLineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			PHPTokenMaker.INTERNAL_IN_PHP,
			"/* Hello world */",
			"/* Hello world unterminated",
			"/**/"
		);
	}


	@Test
	void testPhp_MultiLineComments_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			PHPTokenMaker.INTERNAL_IN_PHP_MLC,
			"continued from another line",
			"continued from another line and closed */"
		);
	}


	@Test
	void testPhp_MultiLineComments_URL() {

		String[] mlcLiterals = {
			"/* Hello world https://www.sas.com */",
		};

		for (String code : mlcLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assertions.assertEquals("https://www.sas.com", token.getLexeme());

			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assertions.assertEquals(" */", token.getLexeme());

		}

	}


	@Test
	void testPhp_nullLiterals() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			PHPTokenMaker.INTERNAL_IN_PHP,
			"null",
			"NULL"
		);
	}


	@Test
	void testPhp_Operators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ && >>>";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.OPERATOR, token.getType(), "Not an operator: " + token);
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "), "Not a single space: " + token);
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(PHPTokenMaker.INTERNAL_IN_PHP, token.getType());

	}


	@Test
	void testPhp_Separators() {

		String code = "( ) [ ] { }";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_IN_PHP, 0);

		String[] separators = code.split(" +");
		for (int i = 0; i < separators.length; i++) {
			Assertions.assertEquals(separators[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.SEPARATOR, token.getType());
			// Just one extra test here
			Assertions.assertTrue(token.isSingleChar(TokenTypes.SEPARATOR, separators[i].charAt(0)));
			if (i < separators.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "), "Not a single space: " + token);
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(PHPTokenMaker.INTERNAL_IN_PHP, token.getType());

	}


	@Test
	void testPhp_StringLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			PHPTokenMaker.INTERNAL_IN_PHP,
			"\"\"", "\"hi\"", "\"\\u00fe\"", "\"\\\"\"");

	}


	@Test
	void testPhp_StringLiteral_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			PHPTokenMaker.INTERNAL_IN_PHP_STRING,
			"rest of the string unterminated",
			"rest of the string\""
		);
	}


	@Test
	void testPhp_StringLiteral_variable() {
		assertAllTokensOfType(TokenTypes.VARIABLE,
			PHPTokenMaker.INTERNAL_IN_PHP_STRING,
			"$foo",
			"$foo9",
			"$foo_bar"
		);
	}


	@Test
	void testPhp_variables() {
		assertAllTokensOfType(TokenTypes.VARIABLE,
			PHPTokenMaker.INTERNAL_IN_PHP,
			"$foo",
			"$foo9",
			"$foo_bar"
		);
	}


	@Test
	void testPhp_Whitespace() {
		assertAllTokensOfType(TokenTypes.WHITESPACE,
			PHPTokenMaker.INTERNAL_IN_PHP,
			" ",
			"\t",
			"\f",
			"   \t   "
		);
	}


	@Test
	void testCodeBlockInMiddleOfJavaScriptString() {

		Segment code = createSegment("'This is <?php echo \"bonkers\" ?>'");
		Token t = createTokenMaker().getTokenList(code, JS_PREV_TOKEN_TYPE, 0);

		Assertions.assertTrue(t.is(TokenTypes.LITERAL_CHAR, "'This is "));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.SEPARATOR, "<?php"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.WHITESPACE, " "));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.RESERVED_WORD, "echo"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.WHITESPACE, " "));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"bonkers\""));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.WHITESPACE, " "));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.MARKUP_TAG_DELIMITER, "?>"));
		t = t.getNextToken();
		Assertions.assertTrue(t.is(TokenTypes.LITERAL_CHAR, "'"));
	}


	@Test
	void testCss_atRule() {
		assertAllTokensOfType(TokenTypes.REGEX,
			CSS_PREV_TOKEN_TYPE,
			"@charset",
			"@import",
			"@namespace",
			"@media",
			"@document",
			"@page",
			"@font-face",
			"@keyframes",
			"@viewport"
		);
	}


	@Test
	void testCss_chars() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			CSS_PREV_TOKEN_TYPE,
			"'Hello world'",
			"'Hello \\'world\\''"
		);
	}


	@Test
	void testCss_char_continuedFromPreviousLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			CSS_CHAR_PREV_TOKEN_TYPE,
			"world'",
			"and \\'he\\' said so'",
			"continuation from a prior line"
		);
	}


	@Test
	void testCss_getLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(
			PHPTokenMaker.LANG_INDEX_CSS);
		Assertions.assertEquals("/*", startAndEnd[0]);
		Assertions.assertEquals("*/", startAndEnd[1]);
	}


	@Test
	void testCss_happyPath_stateContinuesToNextLine_mainState() {
		TokenMaker tm = createTokenMaker();
		Segment segment = createSegment("");
		Token token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);
		Assertions.assertEquals(CSS_PREV_TOKEN_TYPE, token.getType());
	}


	@Test
	void testCss_happyPath_stateContinuesToNextLine_valueState() {
		TokenMaker tm = createTokenMaker();
		Segment segment = createSegment("");
		Token token = tm.getTokenList(segment, CSS_VALUE_PREV_TOKEN_TYPE, 0);
		Assertions.assertEquals(CSS_VALUE_PREV_TOKEN_TYPE, token.getType());
	}


	@Test
	void testCss_happyPath_styleEndTagSwitchesState() {
		Segment segment = createSegment("</style>");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);

		Assertions.assertEquals(TokenTypes.MARKUP_TAG_DELIMITER, token.getType());
		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.MARKUP_TAG_NAME, token.getType());
		token = token.getNextToken();
		Assertions.assertEquals(TokenTypes.MARKUP_TAG_DELIMITER, token.getType());
	}


	@Test
	void testCss_happyPath_simpleSelector() {

		String code = "body { padding: 0; }";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);

		Assertions.assertTrue(token.is(TokenTypes.DATA_TYPE, "body"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.SEPARATOR, "{"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "padding"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, ":"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, "0"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, ";"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.SEPARATOR, "}"));

	}


	@Test
	void testCss_id() {

		String code = "#mainContent";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);

		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "#mainContent"));

	}


	@Test
	void testCss_lessLineComment_noLess() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			CSS_PREV_TOKEN_TYPE,
			false,
			"//"
		);
	}


	@Test
	void testCss_lessVar_noLess() {
		assertAllTokensOfType(TokenTypes.REGEX,
			CSS_PREV_TOKEN_TYPE,
			"@something",
			"@something-else"
		);
	}


	@Test
	void testCss_multiLineComment() {

		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE, CSS_PREV_TOKEN_TYPE,
			"/* Hello world */",
			"/* unterminated",
			"/**/"
		);
	}


	@Test
	void testCss_multiLineComment_continuedFromPreviousLine() {

		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE, CSS_MLC_PREV_TOKEN_TYPE,
			" world */",
			"still unterminated"
		);
	}


	@Test
	void testCss_multiLineComment_URL() {

		String[] comments = {
			"/* Hello world file://test.txt */",
			"/* Hello world ftp://ftp.google.com */",
			"/* Hello world https://www.google.com */",
			"/* Hello world https://www.google.com */",
			"/* Hello world www.google.com */"
		};

		for (String comment : comments) {

			Segment segment = createSegment(comment);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, CSS_PREV_TOKEN_TYPE, 0);

			Assertions.assertFalse(token.isHyperlink());
			Assertions.assertTrue(token.is(TokenTypes.COMMENT_MULTILINE, "/* Hello world "));

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());

			token = token.getNextToken();
			Assertions.assertFalse(token.isHyperlink());
			Assertions.assertTrue(token.is(TokenTypes.COMMENT_MULTILINE, " */"));
		}
	}


	@Test
	void testCss_operators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			CSS_PREV_TOKEN_TYPE,
			"+",
			">",
			"~",
			"^",
			"$",
			"|",
			"="
		);
	}


	@Test
	void testCss_propertyBlock_property_multiLineComment() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			CSS_PROPERTY_PREV_TOKEN_TYPE,
			"/* Hello world */",
			"/* unterminated",
			"/**/"
		);
	}


	@Test
	void testCss_propertyBlock_property_properties() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			CSS_PROPERTY_PREV_TOKEN_TYPE,
			"*foo",
			"foo",
			"_",
			"*_",
			"foo9"
		);
	}


	@Test
	void testCss_propertyBlock_property_operators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			CSS_PROPERTY_PREV_TOKEN_TYPE,
			":"
		);
	}


	@Test
	void testCss_propertyBlock_property_separators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			CSS_PROPERTY_PREV_TOKEN_TYPE,
			//"{",
			"}"
		);
	}


	@Test
	void testCss_propertyBlock_value_charLiteral() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"'foobar'"
		);
	}


	@Test
	void testCss_propertyBlock_value_function() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			CSS_VALUE_PREV_TOKEN_TYPE,
			false,
			"func("
		);
	}


	@Test
	void testCss_propertyBlock_value_identifier() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"foobar",
			",",
			"."
		);
	}


	@Test
	void testCss_propertyBlock_value_important() {
		assertAllTokensOfType(TokenTypes.ANNOTATION,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"!important"
		);
	}


	@Test
	void testCss_propertyBlock_value_multiLineComment() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"/* Hello world */",
			"/* unterminated",
			"/**/"
		);
	}


	@Test
	void testCss_propertyBlock_value_number() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"42",
			"42.",
			"42.3",
			"-42",
			"-42.",
			"-42.3",
			"4pt",
			"4pc",
			"4in",
			"4mm",
			"4cm",
			"4em",
			"4ex",
			"4px",
			"4ms",
			"4s",
			"4%",
			"#0",
			"#0A",
			"#0a",
			"#ff00ff"
		);
	}


	@Test
	void testCss_propertyBlock_value_operators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			CSS_VALUE_PREV_TOKEN_TYPE,
			";"
		);
	}


	@Test
	void testCss_propertyBlock_value_separators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			CSS_VALUE_PREV_TOKEN_TYPE,
			")",
			"}"
		);
	}


	@Test
	void testCss_propertyBlock_value_string() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			CSS_VALUE_PREV_TOKEN_TYPE,
			"\"foobar\""
		);
	}


	@Test
	void testCss_propertyValue_function() {

		String code = "background-image: url(\"test.png\");";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_CSS_PROPERTY, 0);

		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "background-image"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, ":"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.FUNCTION, "url"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.SEPARATOR, "("));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, "\"test.png\""));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.SEPARATOR, ")"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, ";"));

		code = "background-image: url('test.png');";
		segment = createSegment(code);
		tm = createTokenMaker();
		token = tm.getTokenList(segment, PHPTokenMaker.INTERNAL_CSS_PROPERTY, 0);

		Assertions.assertTrue(token.is(TokenTypes.RESERVED_WORD, "background-image"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, ":"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.FUNCTION, "url"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.SEPARATOR, "("));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_CHAR, "'test.png'"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.SEPARATOR, ")"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.OPERATOR, ";"));

	}


	@Test
	void testCss_pseudoClass() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			CSS_PREV_TOKEN_TYPE,
			":root",
			":nth-child",
			":nth-last-child",
			":nth-of-type",
			":nth-last-of-type",
			":first-child",
			":last-child",
			":first-of-type",
			":last-of-type",
			":only-child",
			":only-of-type",
			":empty",
			":link",
			":visited",
			":active",
			":hover",
			":focus",
			":target",
			":lang",
			":enabled",
			":disabled",
			":checked",
			"::first-line",
			"::first-letter",
			"::before",
			"::after",
			":not"
		);
	}


	@Test
	void testCss_pseudoClass_unknown() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			CSS_PREV_TOKEN_TYPE,
			":"
		);
	}


	@Test
	void testCss_selectors() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			CSS_PREV_TOKEN_TYPE,
			"*",
			".",
			".foo",
			".foo-bar",
			"foo",
			"-foo-bar",
			"foo-bar");
	}


	@Test
	void testCss_separators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			CSS_PREV_TOKEN_TYPE,
			";",
			"(",
			")",
			"[",
			"]"
		);
	}


	@Test
	void testCss_strings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			CSS_PREV_TOKEN_TYPE,
			"\"Hello world\"",
			"\"Hello \\\"world\\\"",
			"\"Unterminated string"
		);
	}


	@Test
	void testCss_string_continuedFromPreviousLine() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			CSS_STRING_PREV_TOKEN_TYPE,
			"world\"",
			"and \\\"he\\\" said so\""
		);
	}


	@Test
	void testHtml_attribute_doubleQuote() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_INTAG_PREV_TOKEN_TYPE,
			"\"attribute value\"",
			"\"unclosed attribute value"
		);
	}


	@Test
	void testHtml_attribute_doubleQuote_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_ATTR_DOUBLE_PREV_TOKEN_TYPE,
			"continued from prior line\"",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_attribute_singleQuote() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTMLTokenMaker.INTERNAL_INTAG,
			"'attribute value'",
			"'unclosed attribute value"
		);
	}


	@Test
	void testHtml_attribute_singleQuote_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_ATTR_SINGLE_PREV_TOKEN_TYPE,
			"continued from prior line'",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_attributeScriptTag_doubleQuote() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_INTAG_SCRIPT_PREV_TOKEN_TYPE,
			"\"attribute value\"",
			"\"unclosed attribute value"
		);
	}


	@Test
	void testHtml_attributeScriptTag_doubleQuote_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_ATTR_SCRIPT_DOUBLE_PREV_TOKEN_TYPE,
			"continued from prior line\"",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_attributeScriptTag_singleQuote() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTMLTokenMaker.INTERNAL_INTAG_SCRIPT,
			"'attribute value'",
			"'unclosed attribute value"
		);
	}


	@Test
	void testHtml_attributeScriptTag_singleQuote_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_ATTR_SCRIPT_SINGLE_PREV_TOKEN_TYPE,
			"continued from prior line'",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_attributeStyleTag_doubleQuote() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_INTAG_STYLE_PREV_TOKEN_TYPE,
			"\"attribute value\"",
			"\"unclosed attribute value"
		);
	}


	@Test
	void testHtml_attributeStyleTag_doubleQuote_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_ATTR_STYLE_DOUBLE_PREV_TOKEN_TYPE,
			"continued from prior line\"",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_attributeStyleTag_singleQuote() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_INTAG_STYLE_PREV_TOKEN_TYPE,
			"'attribute value'",
			"'unclosed attribute value"
		);
	}


	@Test
	void testHtml_attributeStyleTag_singleQuote_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
			HTML_ATTR_STYLE_SINGLE_PREV_TOKEN_TYPE,
			"continued from prior line'",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_comment() {
		assertAllTokensOfType(TokenTypes.MARKUP_COMMENT,
			TokenTypes.NULL,
			"<!-- Hello world -->",
			"<!-- unterminated"
		);
	}


	@Test
	void testHtml_comment_continuedFromPreviousLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_COMMENT,
			TokenTypes.MARKUP_COMMENT,
			"continued but unterminated",
			"continued -->"
		);
	}


	@Test
	void testHtml_comment_URL() {

		String[] urls = {
			"file://test.txt",
			"ftp://ftp.google.com",
			"https://www.google.com",
			"https://www.google.com",
			"www.google.com"
		};

		for (String literal : urls) {

			Segment segment = createSegment(literal);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.MARKUP_COMMENT, 0);
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.MARKUP_COMMENT, token.getType());
			Assertions.assertEquals(literal, token.getLexeme());
		}
	}


	@Test
	void testHtml_doctype() {
		assertAllTokensOfType(TokenTypes.MARKUP_DTD,
			"<!doctype html>",
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">",
			"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">",
			"<!doctype unclosed"
		);
	}


	@Test
	@Disabled("Multiline DTDs are not supported")
	void testHtml_doctype_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.MARKUP_DTD,
			TokenTypes.VARIABLE,
			"continued from prior line>",
			"continued and still unterminated"
		);
	}


	@Test
	void testHtml_entityReferences() {

		String[] entityReferences = {
			"&nbsp;", "&lt;", "&gt;", "&#4012",
		};

		for (String code : entityReferences) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.MARKUP_ENTITY_REFERENCE, token.getType());
		}

	}


	@Test
	void testHtml_happyPath_tagWithAttributes() {

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
	void testHtml_happyPath_closedTag() {

		String code = "<img src='foo.png'/>";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<'));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "img"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE, "src"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.OPERATOR, '='));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "'foo.png'"), "Unexpected token: " + token);
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "/>"));

	}


	@Test
	void testHtml_happyPath_closingTag() {

		String code = "</body>";
		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_DELIMITER, "</"));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.MARKUP_TAG_NAME, "body"));
		token = token.getNextToken();
		Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '>'));

	}


	@Test
	void testHtml_inTag_unterminatedOnThisLine() {
		Segment segment = createSegment("");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, HTML_INTAG_PREV_TOKEN_TYPE, 0);
		Assertions.assertEquals(PHPTokenMaker.INTERNAL_INTAG, token.getType());
	}


	@Test
	void testHtml_inTagScript_unterminatedOnThisLine() {
		Segment segment = createSegment("");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, HTML_INTAG_SCRIPT_PREV_TOKEN_TYPE, 0);
		Assertions.assertEquals(PHPTokenMaker.INTERNAL_INTAG_SCRIPT, token.getType());
	}


	@Test
	void testHtml_inTagStyle_unterminatedOnThisLine() {
		Segment segment = createSegment("");
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, HTML_INTAG_STYLE_PREV_TOKEN_TYPE, 0);
		Assertions.assertEquals(PHPTokenMaker.INTERNAL_INTAG_STYLE, token.getType());
	}


	@Test
	void testHtml_validHtml5TagNames() {

		String[] tagNames = {
			"a", "abbr", "acronym", "address", "applet", "area", "article",
			"aside", "audio", "b", "base", "basefont", "bdo", "bgsound", "big",
			"blink", "blockquote", "body", "br", "button", "canvas", "caption",
			"center", "cite", "code", "col", "colgroup", "command", "comment",
			"dd", "datagrid", "datalist", "datatemplate", "del", "details",
			"dfn", "dialog", "dir", "div", "dl", "dt", "em", "embed",
			"eventsource", "fieldset", "figure", "font", "footer", "form",
			"frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6",
			"head", "header", "hr", "html", "i", "iframe", "ilayer", "img",
			"input", "ins", "isindex", "kbd", "keygen", "label", "layer",
			"legend", "li", "link", "map", "mark", "marquee", "menu", "meta",
			"meter", "multicol", "nav", "nest", "nobr", "noembed", "noframes",
			"nolayer", "noscript", "object", "ol", "optgroup", "option",
			"output", "p", "param", "plaintext", "pre", "progress", "q", "rule",
			"s", "samp", "script", "section", "select", "server", "small",
			"source", "spacer", "span", "strike", "strong", "style",
			"sub", "sup", "table", "tbody", "td", "textarea", "tfoot", "th",
			"thead", "time", "title", "tr", "tt", "u", "ul", "var", "video"
		};

		TokenMaker tm = createTokenMaker();
		for (String tagName : tagNames) {

			for (int i = 0; i < tagName.length(); i++) {

				if (i > 0) {
					tagName = tagName.substring(0, i).toUpperCase() +
						tagName.substring(i);
				}

				String code = "<" + tagName;
				Segment segment = createSegment(code);
				Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
				Assertions.assertTrue(token.isSingleChar(TokenTypes.MARKUP_TAG_DELIMITER, '<'));
				token = token.getNextToken();
				Assertions.assertEquals(token.getType(), TokenTypes.MARKUP_TAG_NAME);

			}

		}

	}


	@Test
	void testHtml_loneIdentifier() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"foo",
			"123"
		);
	}


	@Test
	void testJS_BooleanLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_BOOLEAN,
			JS_PREV_TOKEN_TYPE,
			"true",
			"false"
		);
	}


	@Test
	void testJS_CharLiterals_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			JS_PREV_TOKEN_TYPE,
			"'\\xG7'", // Invalid hex/octal escape
			"'foo\\ubar'", "'\\u00fg'", // Invalid Unicode escape
			"'My name is \\ubar and I \\", // Continued onto another line
			"'This is unterminated and " // Unterminated string
		);
	}


	@Test
	void testJS_CharLiterals_valid() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			JS_PREV_TOKEN_TYPE,			"'a'", "'\\b'", "'\\t'", "'\\r'", "'\\f'", "'\\n'", "'\\u00fe'",
			"'\\u00FE'", "'\\111'", "'\\222'", "'\\333'",
			"'\\x77'",
			"'\\11'", "'\\22'", "'\\33'",
			"'\\1'",
			"'My name is Robert and I \\" // Continued onto another line
		);
	}


	@Test
	void testJS_CharLiterals_fromPriorLine_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			JS_INVALID_CHAR_PREV_TOKEN_TYPE,
			"still an invalid char literal",
			"still an invalid char literal even though terminated'"
		);
	}


	@Test
	void testJS_CharLiterals_fromPriorLine_valid() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			JS_VALID_CHAR_PREV_TOKEN_TYPE,
			"still a valid char literal'"
		);
	}


	@Test
	void testJS_DataTypes() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			JS_PREV_TOKEN_TYPE,
			"boolean",
			"byte",
			"char",
			"double",
			"float",
			"int",
			"long",
			"short"
		);
	}


	@Test
	void testJS_DocComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			JS_PREV_TOKEN_TYPE,
			"/** Hello world */"
		);
	}


	@Test
	void testJS_DocComments_BlockTags() {

		String[] blockTags = {
			"abstract", "access", "alias", "augments", "author", "borrows",
			"callback", "classdesc", "constant", "constructor", "constructs",
			"copyright", "default", "deprecated", "desc", "enum", "event",
			"example", "exports", "external", "file", "fires", "global",
			"ignore", "inner", "instance", "kind", "lends", "license",
			"link", "member", "memberof", "method", "mixes", "mixin", "module",
			"name", "namespace", "param", "private", "property", "protected",
			"public", "readonly", "requires", "return", "returns", "see", "since",
			"static", "summary", "this", "throws", "todo",
			"type", "typedef", "variation", "version"
		};

		for (String blockTag : blockTags) {
			blockTag = "@" + blockTag;
			Segment segment = createSegment(blockTag);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_DOC_COMMENT_PREV_TOKEN_TYPE, 0);
			// Can sometimes produce empty tokens, if e.g. @foo is first token
			// on a line. We could technically make that better, but it is not
			// the common case
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.COMMENT_KEYWORD, token.getType(), "Invalid block tag: " + blockTag);
		}

	}


	@Test
	@Disabled("Fails because we create a (possibly) 0-length token before this - yuck!")
	void testJS_DocComments_InlineTags() {
		assertAllTokensOfType(TokenTypes.COMMENT_KEYWORD,
			JS_DOC_COMMENT_PREV_TOKEN_TYPE,
			"@link",
			"@linkplain",
			"@linkcode",
			"@tutorial"
		);
	}


	@Test
	void testJS_DocComments_Markup() {
		String text = "<code>";
		Segment segment = createSegment(text);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, JS_DOC_COMMENT_PREV_TOKEN_TYPE, 0);
		// Can sometimes produce empty tokens, if e.g. @foo is first token
		// on a line. We could technically make that better, but it is not
		// the common case
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.COMMENT_MARKUP, "<code>"));
	}


	@Test
	void testJS_DocComments_URL() {

		String[] docCommentLiterals = {
			"/** Hello world https://www.sas.com */",
		};

		for (String code : docCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
			Assertions.assertEquals("https://www.sas.com", token.getLexeme());

			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
			Assertions.assertEquals(" */", token.getLexeme());

		}

	}


	@Test
	void testJS_EolComments() {

		String[] eolCommentLiterals = {
			"// Hello world",
		};

		for (String code : eolCommentLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
		}

	}


	@Test
	void testJS_EolComments_URL() {

		String[] eolCommentLiterals = {
			// Note: The 0-length token at the end of the first example is a
			// minor bug/performance thing
			"// Hello world https://www.sas.com",
			"// Hello world https://www.sas.com extra",
			"// Hello world https://www.sas.com",
			"// Hello world ftp://sas.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assertions.assertTrue(token.getLexeme().contains("sas.com"));

			token = token.getNextToken();
			// Note: The 0-length token at the end of the first example is a
			// minor bug/performance thing
			if (token != null && token.isPaintable() && token.length() > 0) {
				Assertions.assertFalse(token.isHyperlink());
				Assertions.assertTrue(token.is(TokenTypes.COMMENT_EOL, " extra"));
			}

		}

	}


	@Test
	void testJS_FloatingPointLiterals() {

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
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

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

		Assertions.assertEquals(JS_PREV_TOKEN_TYPE, token.getType());

	}


	@Test
	void testJS_Functions() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			JS_PREV_TOKEN_TYPE,
			"eval",
			"parseInt",
			"parseFloat",
			"escape",
			"unescape",
			"isNaN",
			"isFinite"
		);
	}


	@Test
	void testJS_HexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
			"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
			"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL ";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

		String[] literals = code.split(" +");
		for (int i = 0; i < literals.length; i++) {
			Assertions.assertEquals(literals[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType(), "Not a hex number: " + token);
			if (i < literals.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

	}


	@Test
	void testJS_Identifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			JS_PREV_TOKEN_TYPE,
			"foo",
			"$bar",
			"var1"
		);
	}


	@Test
	void testJS_Identifiers_errors() {
		assertAllTokensOfType(TokenTypes.ERROR_IDENTIFIER,
			JS_PREV_TOKEN_TYPE,
			"\\"
		);
	}


	@Test
	void testJS_Keywords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			JS_PREV_TOKEN_TYPE,
			"break", "case", "catch", "class", "const", "continue",
			"debugger", "default", "delete", "do", "else", "export", "extends", "finally", "for", "function", "if",
			"import", "in", "instanceof", "let", "new", "super", "switch",
			"this", "throw", "try", "typeof", "void", "while", "with",
			"NaN", "Infinity",
			"let" // As of 1.7, which is our default version
		);

		assertAllTokensOfType(TokenTypes.RESERVED_WORD_2,
			JS_PREV_TOKEN_TYPE,
			"return"
		);
	}


	@Test
	void testJS_MultiLineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			JS_PREV_TOKEN_TYPE,
			"/* Hello world */",
			"/* Hello world unterminated",
			"/**/"
		);
	}


	@Test
	void testJS_MultiLineComments_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			JS_MLC_PREV_TOKEN_TYPE,
			" this is continued from a prior line */",
			" this is also continued, but not terminated"
		);
	}


	@Test
	void testJS_MultiLineComments_URL() {
		String[] mlcLiterals = {
			"/* Hello world file://test.txt */",
			"/* Hello world ftp://ftp.google.com */",
			"/* Hello world https://www.google.com */",
			"/* Hello world https://www.google.com */",
			"/* Hello world www.google.com */"
		};

		for (String code : mlcLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());

			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assertions.assertEquals(" */", token.getLexeme());

		}

	}


	@Test
	void testJS_Numbers() {

		String[] ints = {
			"0", "42", /*"-7",*/
			"0l", "42l",
			"0L", "42L",
		};

		for (String code : ints) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, token.getType());
		}

		String[] floats = {
			"1e17", "3.14159", "5.7e-8", "2f", "2d",
		};

		for (String code : floats) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
		}

		String[] hex = {
			"0x1f", "0X1f", "0x1F", "0X1F",
		};

		for (String code : hex) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
		}

		String[] errors = {
			"42foo", "1e17foo", "0x1ffoo",
		};

		for (String code : errors) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.ERROR_NUMBER_FORMAT, token.getType());
		}

	}


	@Test
	void testJS_Operators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ && >>>";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.OPERATOR, token.getType(), "Not an operator: " + token);
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "), "Not a single space: " + token);
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(JS_PREV_TOKEN_TYPE, token.getType());

	}


	@Test
	void testJS_regex_startOfLine() {
		assertAllTokensOfType(TokenTypes.REGEX,
			JS_PREV_TOKEN_TYPE,
			"/foobar/",
			"/foobar/gim",
			"/foo\\/bar\\/bas/g"
		);
	}


	@Test
	void testTS_regex_followingCertainOperators() {
		assertAllSecondTokensAreRegexes(
			JS_PREV_TOKEN_TYPE,
			"=/foo/",
			"(/foo/",
			",/foo/",
			"?/foo/",
			":/foo/",
			"[/foo/",
			"!/foo/",
			"&/foo/",
			"=/foo/",
			"==/foo/",
			"!=/foo/",
			"<<=/foo/",
			">>=/foo/"
		);
	}


	@Test
	void testJS_regex_notWhenFollowingCertainTokens() {
		assertAllSecondTokensAreNotRegexes(
			JS_PREV_TOKEN_TYPE,
			"^/foo/",
			">>/foo/",
			"<</foo/",
			"--/foo/",
			"4/foo/"
		);
	}


	@Test
	void testJS_Separators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			JS_PREV_TOKEN_TYPE,
			"(",
			")",
			"[",
			"]",
			"{",
			"}"
		);
	}


	@Test
	void testJS_Separators_renderedAsIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			JS_PREV_TOKEN_TYPE,
			";",
			",",
			"."
		);
	}


	@Test
	void testJS_StringLiterals_invalid() {

		String[] stringLiterals = {
			"\"\\xG7\"", // Invalid hex/octal escape
			"\"foo\\ubar\"", "\"\\u00fg\"", // Invalid Unicode escape
			"\"My name is \\ubar and I \\", // Continued onto another line
			"\"This is unterminated and ", // Unterminated string
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.ERROR_STRING_DOUBLE, token.getType(), "Not an ERROR_STRING_DOUBLE: " + token);
		}

	}


	@Test
	void testJS_StringLiterals_valid() {

		String[] stringLiterals = {
			"\"\"", "\"hi\"", "\"\\x77\"", "\"\\u00fe\"", "\"\\\"\"",
			"\"My name is Robert and I \\", // String continued on another line
		};

		for (String code : stringLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE, token.getType());
		}

	}


	@Test
	void testJS_StringLiteralsFromPreviousLine_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			JS_INVALID_STRING_PREV_TOKEN_TYPE,
			"this is the rest of the string\"",
			"the rest of the string but still unterminated"
		);
	}


	@Test
	void testJS_StringLiteralsFromPreviousLine_valid() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			JS_VALID_STRING_PREV_TOKEN_TYPE,
			"this is the rest of the string\""
		);
	}


	@Test
	void testJS_TemplateLiterals_invalid() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			JS_PREV_TOKEN_TYPE,
			"`\\xG7`", // Invalid hex/octal escape
			"`foo\\ubar`", "`\\u00fg`", // Invalid Unicode escape
			"`My name is \\ubar and I " // Continued onto another line
		);
	}


	@Test
	void testJS_TemplateLiterals_invalid_unclosedExpression() {

		String code = "`Hello ${unclosedName";
		Segment seg = createSegment(code);
		TokenMaker tm = createTokenMaker();

		Token token = tm.getTokenList(seg, JS_PREV_TOKEN_TYPE, 0);
		Assertions.assertTrue(token.is(TokenTypes.LITERAL_BACKQUOTE, "`Hello "));
		token = token.getNextToken();
		Assertions.assertTrue(token.is(TokenTypes.VARIABLE, "${unclosedName"));
	}


	@Test
	void testJS_TemplateLiterals_valid_noInterpolatedExpression() {
		assertAllTokensOfType(TokenTypes.LITERAL_BACKQUOTE,
			JS_PREV_TOKEN_TYPE,
			"``", "`hi`", "`\\x77`", "`\\u00fe`", "`\\\"`",
			"`My name is Robert and I", // String continued on another line
			"`My name is Robert and I \\" // String continued on another line
		);
	}


	@Test
	void testJS_TemplateLiterals_valid_withInterpolatedExpression() {

		// Strings with tokens:  template, interpolated expression, template
		String[] templateLiterals = {
			"`My name is ${name}`",
			"`My name is ${'\"' + name + '\"'}`",
			"`Embedded example: ${2 + ${!!func()}}, wow",
		};

		for (String code : templateLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.LITERAL_BACKQUOTE, token.getType());
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.VARIABLE, token.getType());
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.LITERAL_BACKQUOTE, token.getType());
		}

	}


	@Test
	void testJS_TemplateLiterals_valid_continuedFromPriorLine() {

		String[] templateLiterals = {
			"and my name is ${name}`"
		};

		for (String code : templateLiterals) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_VALID_TEMPLATE_LITERAL_PREV_TOKEN_TYPE,
				0);
			Assertions.assertEquals(TokenTypes.LITERAL_BACKQUOTE, token.getType());
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.VARIABLE, token.getType());
			token = token.getNextToken();
			Assertions.assertEquals(TokenTypes.LITERAL_BACKQUOTE, token.getType());
		}

	}


	@Test
	void testJS_TemplateLiterals_invalid_continuedFromPriorLine() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			JS_INVALID_TEMPLATE_LITERAL_PREV_TOKEN_TYPE,
			"this is still an invalid template literal`");
	}


	@Test
	void testJS_Whitespace() {

		String[] whitespace = {
			" ", "\t", "\f", "   \t   ",
		};

		for (String code : whitespace) {
			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();
			Token token = tm.getTokenList(segment, JS_PREV_TOKEN_TYPE, 0);
			Assertions.assertEquals(TokenTypes.WHITESPACE, token.getType());
		}

	}
}
