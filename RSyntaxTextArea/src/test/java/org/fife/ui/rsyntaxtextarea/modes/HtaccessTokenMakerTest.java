/*
 * 06/05/2016
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
 * Unit tests for the {@link HtaccessTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class HtaccessTokenMakerTest extends AbstractTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new HtaccessTokenMaker();
	}


	@Test
	void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"# Hello world"
		);
	}


	@Test
	void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"# Hello world https://www.sas.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assertions.assertEquals("https://www.sas.com", token.getLexeme());

		}

	}


	@Test
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("#", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
	}


	@Test
	void testFunctions() {

		String[] functions = {
				"AcceptPathInfo",
				"Action",
				"AddAlt",
				"AddAltByEncoding",
				"AddAltByType",
				"AddCharset",
				"AddDefaultCharset",
				"AddDescription",
				"AddEncoding",
				"AddHandler",
				"AddIcon",
				"AddIconByEncoding",
				"AddIconByType",
				"AddInputFilter",
				"AddLanguage",
				"AddOutputFilter",
				"AddOutputFilterByType",
				"AddType",
				"Allow",
				"Anonymous",
				"Anonymous_Authoritative",
				"Anonymous_LogEmail",
				"Anonymous_MustGiveEmail",
				"Anonymous_NoUserID",
				"Anonymous_VerifyEmail",
				"AuthAuthoritative",
				"AuthBasicAuthoritative",
				"AuthBasicProvider",
				"AuthDBMAuthoritative",
				"AuthDBMGroupFile",
				"AuthDBMType",
				"AuthDBMUserFile",
				"AuthDigestAlgorithm",
				"AuthDigestDomain",
				"AuthDigestFile",
				"AuthDigestGroupFile",
				"AuthDigestNonceFormat",
				"AuthDigestNonceLifetime",
				"AuthDigestQop",
				"AuthGroupFile",
				"AuthLDAPAuthoritative",
				"AuthLDAPBindDN",
				"AuthLDAPBindPassword",
				"AuthLDAPCompareDNOnServer",
				"AuthLDAPDereferenceAliases",
				"AuthLDAPEnabled",
				"AuthLDAPFrontPageHack",
				"AuthLDAPGroupAttribute",
				"AuthLDAPGroupAttributeIsDN",
				"AuthLDAPRemoteUserIsDN",
				"AuthLDAPUrl",
				"AuthName",
				"AuthType",
				"AuthUserFile",
				"BrowserMatch",
				"BrowserMatchNoCase",
				"CGIMapExtension",
				"CharsetDefault",
				"CharsetOptions",
				"CharsetSourceEnc",
				"CheckSpelling",
				"ContentDigest",
				"CookieDomain",
				"CookieExpires",
				"CookieName",
				"CookieStyle",
				"CookieTracking",
				"DefaultIcon",
				"DefaultLanguage",
				"DefaultType",
				"Deny",
				"DirectoryIndex",
				"DirectorySlash",
				"EnableMMAP",
				"EnableSendfile",
				"ErrorDocument",
				"Example",
				"ExpiresActive",
				"ExpiresByType",
				"ExpiresDefault",
				"FileETag",
				"ForceLanguagePriority",
				"ForceType",
				"Header",
				"HeaderName",
				"ImapBase",
				"ImapDefault",
				"ImapMenu",
				"IndexIgnore",
				"IndexOptions",
				"IndexOrderDefault",
				"ISAPIAppendLogToErrors",
				"ISAPIAppendLogToQuery",
				"ISAPIFakeAsync",
				"ISAPILogNotSupported",
				"ISAPIReadAheadBuffer",
				"LanguagePriority",
				"LimitRequestBody",
				"LimitXMLRequestBody",
				"MetaDir",
				"MetaFiles",
				"MetaSuffix",
				"MultiviewsMatch",
				"Options",
				"Order",
				"PassEnv",
				"ReadmeName",
				"Redirect",
				"RedirectMatch",
				"RedirectPermanent",
				"RedirectTemp",
				"RemoveCharset",
				"RemoveEncoding",
				"RemoveHandler",
				"RemoveInputFilter",
				"RemoveLanguage",
				"RemoveOutputFilter",
				"RemoveType",
				"RequestHeader",
				"Require",
				"RewriteBase",
				"RewriteCond",
				"RewriteEngine",
				"RewriteOptions",
				"RewriteRule",
				"RLimitCPU",
				"RLimitMEM",
				"RLimitNPROC",
				"Satisfy",
				"ScriptInterpreterSource",
				"ServerSignature",
				"SetEnv",
				"SetEnvIf",
				"SetEnvIfNoCase",
				"SetHandler",
				"SetInputFilter",
				"SetOutputFilter",
				"SSIErrorMsg",
				"SSITimeFormat",
				"SSLCipherSuite",
				"SSLOptions",
				"SSLProxyCipherSuite",
				"SSLProxyVerify",
				"SSLProxyVerifyDepth",
				"SSLRequire",
				"SSLRequireSSL",
				"SSLUserName",
				"SSLVerifyClient",
				"SSLVerifyDepth",
				"UnsetEnv",
				"XBitHack",
		};

		assertAllTokensOfType(TokenTypes.FUNCTION, functions);
	}


}
