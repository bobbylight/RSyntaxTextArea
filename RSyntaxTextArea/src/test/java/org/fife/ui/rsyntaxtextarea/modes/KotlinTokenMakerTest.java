/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.text.Segment;


/**
 * Unit tests for the {@link KotlinTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class KotlinTokenMakerTest extends AbstractCDerivedTokenMakerTest {


	@Override
	protected TokenMaker createTokenMaker() {
		return new KotlinTokenMaker();
	}


	@Test
	@Override
	public void testCommon_GetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assertions.assertEquals("//", startAndEnd[0]);
		Assertions.assertNull(null, startAndEnd[1]);
	}


	@Test
	@Override
	public void testCommon_getMarkOccurrencesOfTokenType() {
		TokenMaker tm = createTokenMaker();
		for (int i = 0; i < TokenTypes.DEFAULT_NUM_TOKEN_TYPES; i++) {
			boolean expected = i == TokenTypes.IDENTIFIER || i == TokenTypes.FUNCTION;
			Assertions.assertEquals(expected, tm.getMarkOccurrencesOfTokenType(i));
		}
	}


	@Test
	void testAnnotations() {

		String code = "@Test @Foo @Foo_Bar_Bas @Number7";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.ANNOTATION, token.getType(), "Unexpected token type for token: " + token);
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testBinaryLiterals() {

		String code =
			"0b0 0b1 0B0 0B1 0b010 0B010 0b0_10 0B0_10";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testBooleanLiterals() {

		String code = "true false";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_BOOLEAN, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testCharLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_CHAR,
			"'a'",
			"'\\b'",
			"'\\t'",
			"'\\r'",
			"'\\f'",
			"'\\n'",
			"'\\u00fe'",
			"'\\u00FE'",
			"'\\111'",
			"'\\222'",
			"'\\333'",
			"'\\11'",
			"'\\22'",
			"'\\33'",
			"'\\1'"
		);
	}


	@Test
	void testCharLiterals_error() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			"'\\x'"
		);
	}


	@Test
	void testClassNames_java_lang() {

		String[] classNames = {
			"Appendable",
			"AutoCloseable",
			"CharSequence",
			"Cloneable",
			"Comparable",
			"Iterable",
			"ProcessHandle",
			"ProcessHandle.Info",
			"Readable",
			"Runnable",
			"StackWalker.StackFrame",
			"System.Logger",
			"Thread.UncaughtExceptionHandler",

			"Character",
			"Character.Subset",
			"Character.UnicodeBlock",
			"Class",
			"ClassLoader",
			"ClassValue",
			"Compiler",
			"Enum",
			"Enum.EnumDesc",
			"InheritableThreadLocal",
			"Integer",
			"Math",
			"Module",
			"ModuleLayer",
			"ModuleLayer.Controller",
			"Number",
			"Object",
			"Package",
			"Process",
			"ProcessBuilder",
			"ProcessBuilder.Redirect",
			"Record",
			"Runtime",
			"RuntimePermission",
			"Runtime.Version",
			"SecurityManager",
			"StackTraceElement",
			"StackWalker",
			"StrictMath",
			"StringBuffer",
			"StringBuilder",
			"System",
			"Thread",
			"ThreadGroup",
			"ThreadLocal",
			"Throwable",
			"Void",
			"Character.UnicodeScript",
			"ProcessBuilder.Redirect.Type",
			"Thread.State",
			"ArithmeticException",
			"ArrayIndexOutOfBoundsException",
			"ArrayStoreException",
			"ClassCastException",
			"ClassNotFoundException",
			"CloneNotSupportedException",
			"EnumConstantNotPresentException",
			"Exception",
			"IllegalAccessException",
			"IllegalArgumentException",
			"IllegalMonitorStateException",
			"IllegalStateException",
			"IllegalThreadStateException",
			"IndexOutOfBoundsException",
			"InstantiationException",
			"InterruptedException",
			"NegativeArraySizeException",
			"NoSuchFieldException",
			"NoSuchMethodException",
			"NullPointerException",
			"NumberFormatException",
			"RuntimeException",
			"SecurityException",
			"StringIndexOutOfBoundsException",
			"TypeNotPresentException",
			"UnsupportedOperationException",
			"AbstractMethodError",
			"AssertionError",
			"BootstrapMethodError",
			"ClassCircularityError",
			"ClassFormatError",
			"Error",
			"ExceptionInInitializerError",
			"IllegalAccessError",
			"IncompatibleClassChangeError",
			"InstantiationError",
			"InternalError",
			"LinkageError",
			"NoClassDefFoundError",
			"NoSuchFieldError",
			"NoSuchMethodError",
			"OutOfMemoryError",
			"StackOverflowError",
			"ThreadDeath",
			"UnknownError",
			"UnsatisfiedLinkError",
			"UnsupportedClassVersionError",
			"VerifyError",
			"VirtualMachineError",

			"Deprecated",
			"FunctionalInterface",
			"Override",
			"SafeVarargs",
			"SuppressWarnings"
		};

		assertAllTokensOfType(TokenTypes.FUNCTION, classNames);
	}


	@Test
	void testClassNames_java_io() {

		String[] classNames = {
			"Closeable",
			"DataInput",
			"DataOutput",
			"Externalizable",
			"FileFilter",
			"FilenameFilter",
			"Flushable",
			"ObjectInput",
			"ObjectInputFilter",
			"ObjectInputFilter.FilterInfo",
			"ObjectInputValidation",
			"ObjectOutput",
			"ObjectStreamConstants",
			"Serializable",

			"BufferedInputStream",
			"BufferedOutputStream",
			"BufferedReader",
			"BufferedWriter",
			"ByteArrayInputStream",
			"ByteArrayOutputStream",
			"CharArrayReader",
			"CharArrayWriter",
			"Console",
			"DataInputStream",
			"DataOutputStream",
			"File",
			"FileDescriptor",
			"FileInputStream",
			"FileOutputStream",
			"FilePermission",
			"FileReader",
			"FileWriter",
			"FilterInputStream",
			"FilterOutputStream",
			"FilterReader",
			"FilterWriter",
			"InputStream",
			"InputStreamReader",
			"LineNumberInputStream",
			"LineNumberReader",
			"ObjectInputStream",
			"ObjectInputStream.GetField",
			"ObjectOutputStream",
			"ObjectOutputStream.PutField",
			"ObjectStreamClass",
			"ObjectStreamField",
			"OutputStream",
			"OutputStreamWriter",
			"PipedInputStream",
			"PipedOutputStream",
			"PipedReader",
			"PipedWriter",
			"PrintStream",
			"PrintWriter",
			"PushbackInputStream",
			"PushbackReader",
			"RandomAccessFile",
			"Reader",
			"SequenceInputStream",
			"SerializablePermission",
			"StreamTokenizer",
			"StringBufferInputStream",
			"StringReader",
			"StringWriter",
			"Writer",

			"CharConversionException",
			"EOFException",
			"FileNotFoundException",
			"InterruptedIOException",
			"InvalidClassException",
			"InvalidObjectException",
			"IOException",
			"NotActiveException",
			"NotSerializableException",
			"ObjectStreamException",
			"OptionalDataException",
			"StreamCorruptedException",
			"SyncFailedException",
			"UncheckedIOException",
			"UnsupportedEncodingException",
			"UTFDataFormatException",
			"WriteAbortedException",

			"IOError",

			"Serial"
		};

		assertAllTokensOfType(TokenTypes.FUNCTION, classNames);
	}


	@Test
	void testClassNames_java_util() {

		String[] classNames = {
			"Collection",
			"Comparator",
			"Deque",
			"Enumeration",
			"EventListener",
			"Formattable",
			"Iterator",
			"List",
			"ListIterator",
			"Map",
			"Map.Entry",
			"NavigableMap",
			"NavigableSet",
			"Observer",
			"PrimitiveIterator",
			"PrimitiveIterator.OfDouble",
			"PrimitiveIterator.OfInt",
			"PrimitiveIterator.OfLong",
			"Queue",
			"RandomAccess",
			"Set",
			"SortedMap",
			"SortedSet",
			"Spliterator",
			"Spliterator.OfDouble",
			"Spliterator.OfInt",
			"Spliterator.OfLong",
			"Spliterator.OfPrimitive",

			"AbstractCollection",
			"AbstractList",
			"AbstractMap",
			"AbstractMap.SimpleEntry",
			"AbstractMap.SimpleImmutableEntry",
			"AbstractQueue",
			"AbstractSequentialList",
			"AbstractSet",
			"ArrayDeque",
			"ArrayList",
			"Arrays",
			"Base64",
			"Base64.Decoder",
			"Base64.Encoder",
			"BitSet",
			"Calendar",
			"Calendar.Builder",
			"Collections",
			"Currency",
			"Date",
			"Dictionary",
			"DoubleSummaryStatistics",
			"EnumMap",
			"EnumSet",
			"EventListenerProxy",
			"EventObject",
			"FormattableFlags",
			"Formatter",
			"GregorianCalendar",
			"HashMap",
			"HashSet",
			"Hashtable",
			"IdentityHashMap",
			"IntSummaryStatistics",
			"LinkedHashMap",
			"LinkedHashSet",
			"LinkedList",
			"ListResourceBundle",
			"Locale",
			"Locale.Builder",
			"Locale.LanguageRange",
			"LongSummaryStatistics",
			"Objects",
			"Observable",
			"Optional",
			"OptionalDouble",
			"OptionalInt",
			"OptionalLong",
			"PriorityQueue",
			"Properties",
			"PropertyPermission",
			"PropertyResourceBundle",
			"Random",
			"ResourceBundle",
			"ResourceBundle.Control",
			"Scanner",
			"ServiceLoader",
			"SimpleTimeZone",
			"Spliterators",
			"Spliterators.AbstractDoubleSpliterator",
			"Spliterators.AbstractIntSpliterator",
			"Spliterators.AbstractLongSpliterator",
			"Spliterators.AbstractSpliterator",
			"SpliteratorRandom",
			"Stack",
			"StringJoiner",
			"StringTokenizer",
			"Timer",
			"TimerTask",
			"TimeZone",
			"TreeMap",
			"TreeSet",
			"UUID",
			"Vector",
			"WeakHashMap",

			"Formatter.BigDecimalLayoutForm",
			"Locale.Category",
			"Locale.FilteringMode",

			"ConcurrentModificationException",
			"DuplicateFormatFlagsException",
			"EmptyStackException",
			"FormatFlagsConversionMismatchException",
			"FormatterClosedException",
			"IllegalFormatCodePointException",
			"IllegalFormatConversionException",
			"IllegalFormatException",
			"IllegalFormatFlagsException",
			"IllegalFormatPrecisionException",
			"IllegalFormatWidthException",
			"IllformedLocaleException",
			"InputMismatchException",
			"InvalidPropertiesFormatException",
			"MissingFormatArgumentException",
			"MissingFormatWidthException",
			"MissingResourceException",
			"NoSuchElementException",
			"TooManyListenersException",
			"UnknownFormatConversionException",
			"UnknownFormatFlagsException",

			"ServiceConfigurationError"
		};

		assertAllTokensOfType(TokenTypes.FUNCTION, classNames);
	}


	@Test
	void testDataTypes() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
			"Any",
			"Boolean",
			"Byte",
			"Unit",
			"String",
			"Int",
			"Short",
			"Long",
			"Double",
			"Float",
			"Char",
			"Array"
		);
	}


	@Test
	void testDocComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			"/** Hello world */");
	}


	@Test
	void testDocComments_continuedFromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			TokenTypes.COMMENT_DOCUMENTATION,
			"continued from a previous line */"
		);
	}


	@Test
	void testDocComments_keywords() {
		assertAllTokensOfType(TokenTypes.COMMENT_KEYWORD,
			TokenTypes.COMMENT_DOCUMENTATION,

			// block tags
			"@author",
			"@constructor",
			"@exception",
			"@param",
			"@property",
			"@receiver",
			"@return",
			"@sample",
			"@see",
			"@serial",
			"@serialData",
			"@serialField",
			"@since",
			"@suppress",
			"@throws",
			"@version",

			// inline tags
			"{@code }",
			"{@docRoot }",
			"{@inheritDoc }",
			"{@link }",
			"{@linkplain }",
			"{@literal }",
			"{@value }"
		);
	}


	@Test
	void testDocComments_markup() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			TokenTypes.COMMENT_DOCUMENTATION,
			false,
			"<code>",
			"</code>");
	}


	@Test
	void testDocComments_URL() {

		String[] docCommentLiterals = {
			"file://test.txt",
			"ftp://ftp.google.com",
			"https://www.google.com",
			"https://www.google.com",
			"www.google.com"
		};

		for (String literal : docCommentLiterals) {

			Segment segment = createSegment(literal);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.COMMENT_DOCUMENTATION, 0);
			Assertions.assertTrue(token.isHyperlink());
			Assertions.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
			Assertions.assertEquals(literal, token.getLexeme());
		}

	}


	@Test
	void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"// Hello world");
	}


	@Test
	void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"// Hello world https://www.sas.com",
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
	void testFloatingPointLiterals() {

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
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

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

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testHexLiterals() {

		String code = "0x1 0xfe 0x333333333333 0X1 0Xfe 0X33333333333 0xFE 0XFE " +
				"0x1l 0xfel 0x333333333333l 0X1l 0Xfel 0X33333333333l 0xFEl 0XFEl " +
				"0x1L 0xfeL 0x333333333333L 0X1L 0XfeL 0X33333333333L 0xFEL 0XFEL " +
				"0x1_1 0xf_e 0x33_3333_333333 0X1_1 0Xf_e 0X333_333_33333 0xF_E 0XF_E " +
				"0x1_1l 0xf_el 0x333_33333_3333l 0X1_1l 0Xf_el 0X333_3333_3333l 0xF_El 0XF_El " +
				"0x1_1L 0xf_eL 0x333_33333_3333L 0X1_1L 0Xf_eL 0X333_3333_3333L 0xF_EL 0XF_EL";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"foo",
			// Cyrillic chars - most Unicode chars are valid identifier chars
			"\u0438\u0439"
		);
	}


	@Test
	void testIdentifiers_error() {
		assertAllTokensOfType(TokenTypes.ERROR_IDENTIFIER,
			"foo\\bar"
		);
	}


	@Test
	void testIntegerLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_NUMBER_DECIMAL_INT,
			"0",
			"0l",
			"0L",
			"42",
			"42l",
			"42L",
			"123_456",
			"123_456l",
			"123456L"
		);
	}

	@Test
	void testIntegerLiterals_error() {
		assertAllTokensOfType(TokenTypes.ERROR_NUMBER_FORMAT,
			"42rst"
		);
	}


	@Test
	void testKeywords() {

		assertAllTokensOfType(TokenTypes.RESERVED_WORD,
			/* Hard keywords (sans "return", "true" and "false") */
			"as",
			"as?",
			"break",
			"class",
			"continue",
			"do",
			"else",
			"for",
			"fun",
			"if",
			"in",
			"!in",
			"interface",
			"is",
			"!is",
			"null",
			"object",
			"super",
			"this",
			"throw",
			"try",
			"typealias",
			"typeof",
			"val",
			"var",
			"when",
			"while",

			/* Soft keywords */
			"by",
			"catch",
			"constructor",
			"delegate",
			"dynamic",
			"field",
			"file",
			"finally",
			"get",
			"import",
			"init",
			"param",
			"property",
			"receiver",
			"set",
			"setparam",
			"where",

			/* Modifier keywords */
			"actual",
			"abstract",
			"annotation",
			"companion",
			"const",
			"crossinline",
			"data",
			"enum",
			"expect",
			"external",
			"final",
			"infix",
			"inline",
			"inner",
			"internal",
			"lateinit",
			"noinline",
			"open",
			"operator",
			"out",
			"override",
			"private",
			"protected",
			"public",
			"reified",
			"sealed",
			"suspend",
			"tailrec",
			"vararg"
		);

		assertAllTokensOfType(TokenTypes.RESERVED_WORD_2, "return");
	}


	@Test
	void testMultiLineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			"/* Hello world */",
			"/* Hello world unterminated",
			"/**/"
		);
	}


	@Test
	void testMultiLineComments_fromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			TokenTypes.COMMENT_MULTILINE,
			"continued from a previous line */",
			"continued from a previous line unterminated"
		);
	}


	@Test
	void testMultiLineComments_URL() {

		String[] mlcLiterals = {
			"/* Hello world https://www.sas.com */",
		};

		for (String code : mlcLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
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
	void testOctalLiterals() {

		// Note that octal tokens use the token type for hex literals.

		String code = "01 073 0333333333333 01 073 033333333333 073 073 " +
				"01l 073l 0333333333333l 01l 073l 033333333333l 073l 073l " +
				"01L 073L 0333333333333L 01L 073L 033333333333L 073L 073L " +
				"01_1 07_3 033_3333_333333 01_1 07_3 0333_333_33333 07_3 07_3 " +
				"01_1l 07_3l 0333_33333_3333l 01_1l 07_3l 0333_3333_3333l 07_3l 07_3l " +
				"01_1L 07_3L 0333_33333_3333L 01_1L 07_3L 0333_3333_3333L 07_3L 07_3L";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assertions.assertEquals(keywords[i], token.getLexeme());
			Assertions.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assertions.assertTrue(token.isWhitespace(), "Not a whitespace token: " + token);
				Assertions.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testOperators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ | && >>>";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

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

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testSeparators() {

		String code = "( ) [ ] { }";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

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

		Assertions.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	void testStringLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"",
			"\"hi\"",
			"\"\\u00fe\"",
			"\"\\\"\""
		);
	}


	@Test
	void testStringLiteral_error() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"\"unterminated string",
			"\"string with an invalid \\x escape in it\""
		);
	}


	@Test
	void testWhiteSpace() {
		assertAllTokensOfType(TokenTypes.WHITESPACE,
			" ",
			"   ",
			"\t",
			"\t\t",
			"\t  \n",
			"\f"
		);
	}
}
