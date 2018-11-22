/*
 * 06/05/2016
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link HtaccessTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class HtaccessTokenMakerTest extends AbstractTokenMakerTest2 {


	@Override
	protected TokenMaker createTokenMaker() {
		return new HtaccessTokenMaker();
	}


	@Test
	public void testEolComments() {
		String[] eolCommentLiterals = {
			"# Hello world",
		};
		assertAllTokensOfType(eolCommentLiterals, TokenTypes.COMMENT_EOL);
	}


	@Test
	public void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"# Hello world http://www.sas.com",
		};

		for (String code : eolCommentLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());

			token = token.getNextToken();
			Assert.assertTrue(token.isHyperlink());
			Assert.assertEquals(TokenTypes.COMMENT_EOL, token.getType());
			Assert.assertEquals("http://www.sas.com", token.getLexeme());

		}

	}


	@Test
	public void testGetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assert.assertEquals("#", startAndEnd[0]);
		Assert.assertNull(startAndEnd[1]);
	}


	@Test
	public void testFunctions() {

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
		
		assertAllTokensOfType(functions, TokenTypes.FUNCTION);
	}


}