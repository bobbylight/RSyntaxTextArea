/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */

package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link PowerShellTokenMaker} class.
 *
 * @author DOUDOU DIAWARA
 * @version 0.0
 */
public class PowerShellTokenMakerTest  extends  AbstractJFlexTokenMakerTest  {

	@Override
	protected TokenMaker createTokenMaker() {
		return new PowerShellTokenMaker() ;
	}

	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("#", startAndEnd[0]);
		Assertions.assertNull(startAndEnd[1]);
	}

	@Test
	void testComments(){
		assertAllTokensOfType(TokenTypes.COMMENT_EOL, "# This is a comment in Powershell!");
	}

	@Test
	void testReserveWords() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			"begin",
			"break",
			"catch",
			"class",
			"continue",
			"data",
			"define",
			"do",
			"dynamicparam",
			"else",
			"elseif",
			"end",
			"enum",
			"exit",
			"filter",
			"finally",
			"for",
			"foreach",
			"from",
			"function",
			"hidden",
			"if",
			"in",
			"param",
			"process",
			"pause",
			"return",
			"static",
			"switch",
			"throw",
			"trap",
			"try",
			"until",
			"using",
			"var",
			"while",
			"$true",
			"$false",
			"$null",
			"$_",
			"$PSItem",
			"$args",
			"$input",
			"$this",
			"$host"

		);
	}

	@Test
	void testReserveWordsTwo() {
		assertAllTokensOfType(TokenTypes.RESERVED_WORD_2,
			"$env:PATH",
			"$global:Apikey",
			"$script:Foo" ,
			"$local:Var",
			"$private:Var",
			"$PSVersionTable",
			"$PSVersionTable.PSVersion");
	}

	@Test
	void testIntegers() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"1",
					"25",
					"0");
	}

	@Test
	void testFloats() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_FLOAT,
			"3.14",
					"2.71",
					"1.444");
	}

	@Test
	void testHexNumber() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_HEXADECIMAL,
					"0x1A",
							"0xFF",
							"0xFFFFFFFF");
	}

	@Test
	void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"foo",
			"myVariable",
			"helloWorld",
			"address1",
			"address123",
			"long_variable_name"
		);
	}

	@Test
	/* test PowerShell cmdlets "Verb-Noun" */
	void testCmdlets() {
		assertAllTokensOfType(TokenTypes.FUNCTION,
			"Add-Type",
					"Add-WindowsDriver",
					// B
					"Backup-GPO",
					"Backup-SqlDatabase",
					// C
					"Clear-EventLog",
					"Clear-Content",
					// D
					"Disable-PSRemoting",
					"Disable-Service",
					// E
					"Enable-PSRemoting",
					"Enable-Service",
					// F
					"Format-Table",
					"Format-List",
					// G
					"Get-Command",
					"Get-Help",
					// H
					"Hide-VirtualDisk",
					// I
					"Import-Csv",
					"Import-Module",
					// J
					"Join-Path",
					"Join-Expression",
					// L
					"Lock-BitLocker",
					"Limit-EventLog",
					// M
					"Measure-Command",
					"Measure-Object",
					// N
					"New-Item",
					"New-Alias",
					// O
					"Out-File",
					"Out-Host",
					// P
					"Pop-Location",
					"Push-Location",
					// R
					"Remove-Item",
					"Remove-Module",
					// S
					"Start-Process",
					"Stop-Service",
					// T
					"Test-Connection",
					"Test-Path",
					// U
					"Undo-History",
					"Update-Help",
					// W
					"Write-Host",
					"Write-Output"
			);
	}

	@Test
	void testOperators() {
		assertAllTokensOfType(TokenTypes.OPERATOR,
			"=" ,
				"+" ,
				"-" ,
				"*" ,
				"/" ,
				"%" ,
				"+=" ,
				"-=" ,
				"*=" ,
				"/=" ,
				"%=" ,
				"!" ,
				"-not" ,
				"-and" ,
				"-or" ,
				"-xor" ,
				"-eq" ,
				"-ne" ,
				"-gt" ,
				"-ge" ,
				"-lt" ,
				"-le" ,
				"-like" ,
				"-notlike" ,
				"-match" ,
				"-notmatch" ,
				"-contains" ,
				"-notcontains" ,
				"-replace" ,
				"-split" ,
				"-join" ,
				/*others any arguments*/
				"-Name",
				"-InputObject",
				"::" ,
				"|"
		);
	}

	@Test
	void testSeparators() {
		assertAllTokensOfType(TokenTypes.SEPARATOR,
			"(" ,
			")" ,
			"[" ,
			"]" ,
			"{" ,
			"}" ,
			"," ,
			";" ,
			":" ,
			"@" ,
			"@(" ,
			"@{" ,
			"&"
		);
	}

	@Test
	void testStrings() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"00\"",
			"\"test started\"",
			"\"test finished\"",
			"\"1111\"",
			"\"Value\"",
			"\"foobar\"",
			"\"foobar\""
		);
	}

	@Test
	void testWhitespace() {
		assertAllTokensOfType(TokenTypes.WHITESPACE,
			" ",
			"\t",
			"\f",
			"     ",
			"  \t  ",
			"\t\t");
	}

}
