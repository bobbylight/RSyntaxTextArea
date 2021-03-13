/*
 * 03/12/2015
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.junit.Assert;
import org.junit.Test;


/**
 * Unit tests for the {@link JavaTokenMaker} class.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class JavaTokenMakerTest extends AbstractTokenMakerTest2 {


	@Override
	protected TokenMaker createTokenMaker() {
		return new JavaTokenMaker();
	}


	@Test
	public void testAnnotations() {

		String code = "@Test @Foo @Foo_Bar_Bas @Number7";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals("Unexpected token type for token: " + token, TokenTypes.ANNOTATION, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	public void testBinaryLiterals() {

		String code =
			"0b0 0b1 0B0 0B1 0b010 0B010 0b0_10 0B0_10";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.LITERAL_NUMBER_DECIMAL_INT, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	public void testBooleanLiterals() {

		String code = "true false";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.LITERAL_BOOLEAN, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	public void testCharLiterals() {
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
	public void testCharLiterals_error() {
		assertAllTokensOfType(TokenTypes.ERROR_CHAR,
			"'\\x'"
		);
	}


	@Test
	public void testClassNames_java_lang() {

		String[] classNames = {
			"Appendable",
			"AutoCloseable",
			"CharSequence",
			"Cloneable",
			"Comparable",
			"Iterable",
			"Readable",
			"Runnable",
			"Thread.UncaughtExceptionHandler",
			"Boolean",
			"Byte",
			"Character",
			"Character.Subset",
			"Character.UnicodeBlock",
			"Class",
			"ClassLoader",
			"ClassValue",
			"Compiler",
			"Double",
			"Enum",
			"Float",
			"InheritableThreadLocal",
			"Integer",
			"Long",
			"Math",
			"Number",
			"Object",
			"Package",
			"Process",
			"ProcessBuilder",
			"ProcessBuilder.Redirect",
			"Runtime",
			"RuntimePermission",
			"SecurityManager",
			"Short",
			"StackTraceElement",
			"StrictMath",
			"String",
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
			"VirtualMachineError"
		};

		assertAllTokensOfType(classNames, TokenTypes.FUNCTION);
	}


	@Test
	public void testClassNames_java_io() {

		String[] classNames = {
			"Closeable",
			"DataInput",
			"DataOutput",
			"Externalizable",
			"FileFilter",
			"FilenameFilter",
			"Flushable",
			"ObjectInput",
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

			"IOError"
		};

		assertAllTokensOfType(classNames, TokenTypes.FUNCTION);
	}


	@Test
	public void testClassNames_java_util() {

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

		assertAllTokensOfType(classNames, TokenTypes.FUNCTION);
	}


	@Test
	public void testDataTypes() {
		assertAllTokensOfType(TokenTypes.DATA_TYPE,
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
	public void testDocComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			"/** Hello world */");
	}


	@Test
	public void testDocComments_continuedFromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			TokenTypes.COMMENT_DOCUMENTATION,
			"continued from a previous line */"
		);
	}


	@Test
	public void testDocComments_keywords() {
		assertAllTokensOfType(TokenTypes.COMMENT_KEYWORD,
			TokenTypes.COMMENT_DOCUMENTATION,

				// current block tags
				"@author",
				"@deprecated",
				"@exception",
				"@param",
				"@return",
				"@see",
				"@serial",
				"@serialData",
				"@serialField",
				"@since",
				"@throws",
				"@version",

				// proposed doc tags
				"@category",
				"@example",
				"@tutorial",
				"@index",
				"@exclude",
				"@todo",
				"@internal",
				"@obsolete",
				"@threadsafety",

				// inline tag
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
	public void testDocComments_markup() {
		assertAllTokensOfType(TokenTypes.COMMENT_DOCUMENTATION,
			TokenTypes.COMMENT_DOCUMENTATION,
			"<code>",
			"</code>");
	}


	@Test
	public void testDocComments_URL() {

		String[] docCommentLiterals = {
			"file://test.txt",
			"ftp://ftp.google.com",
			"http://www.google.com",
			"https://www.google.com",
			"www.google.com"
		};

		for (String literal : docCommentLiterals) {

			Segment segment = createSegment(literal);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.COMMENT_DOCUMENTATION, 0);
			Assert.assertTrue(token.isHyperlink());
			Assert.assertEquals(TokenTypes.COMMENT_DOCUMENTATION, token.getType());
			Assert.assertEquals(literal, token.getLexeme());
		}

	}


	@Test
	public void testEolComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_EOL,
			"// Hello world");
	}


	@Test
	public void testEolComments_URL() {

		String[] eolCommentLiterals = {
			"// Hello world http://www.sas.com",
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
	public void testFloatingPointLiterals() {

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
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.LITERAL_NUMBER_FLOAT, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	@Override
	public void testGetLineCommentStartAndEnd() {
		String[] startAndEnd = createTokenMaker().getLineCommentStartAndEnd(0);
		Assert.assertEquals("//", startAndEnd[0]);
		Assert.assertNull(null, startAndEnd[1]);
	}


	@Test
	public void testHexLiterals() {

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
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	public void testIdentifiers() {
		assertAllTokensOfType(TokenTypes.IDENTIFIER,
			"foo",
			// Cyrillic chars - most Unicode chars are valid identifier chars
			"\u0438\u0439"
		);
	}


	@Test
	public void testIdentifiers_error() {
		assertAllTokensOfType(TokenTypes.ERROR_IDENTIFIER,
			"foo\\bar"
		);
	}


	@Test
	public void testIntegerLiterals() {
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
	public void testIntegerLiterals_error() {
		assertAllTokensOfType(TokenTypes.ERROR_NUMBER_FORMAT,
			"42rst"
		);
	}


	@Test
	public void testKeywords() {

		String code = "abstract assert break case catch class const continue " +
				"default do else enum extends final finally for goto if " +
				"implements import instanceof interface native new null package " +
				"private protected public static strictfp super switch " +
				"synchronized this throw throws transient try void volatile while";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.RESERVED_WORD, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(TokenTypes.NULL, token.getType());

		segment = createSegment("return");
		token = tm.getTokenList(segment, TokenTypes.NULL, 0);
		Assert.assertEquals("return", token.getLexeme());
		Assert.assertEquals(TokenTypes.RESERVED_WORD_2, token.getType());
		token = token.getNextToken();
		Assert.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	public void testMultiLineComments() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			"/* Hello world */"
		);
	}


	@Test
	public void testMultiLineComments_continuedFromPreviousLine() {
		assertAllTokensOfType(TokenTypes.COMMENT_MULTILINE,
			TokenTypes.COMMENT_MULTILINE,
			"continued from a previous line */"
		);
	}


	@Test
	public void testMultiLineComments_URL() {

		String[] mlcLiterals = {
			"/* Hello world http://www.sas.com */",
		};

		for (String code : mlcLiterals) {

			Segment segment = createSegment(code);
			TokenMaker tm = createTokenMaker();

			Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);
			Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());

			token = token.getNextToken();
			Assert.assertTrue(token.isHyperlink());
			Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assert.assertEquals("http://www.sas.com", token.getLexeme());

			token = token.getNextToken();
			Assert.assertEquals(TokenTypes.COMMENT_MULTILINE, token.getType());
			Assert.assertEquals(" */", token.getLexeme());

		}

	}


	@Test
	public void testOctalLiterals() {

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
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.LITERAL_NUMBER_HEXADECIMAL, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue(token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	public void testOperators() {

		String assignmentOperators = "+ - <= ^ ++ < * >= % -- > / != ? >> ! & == : >> ~ | && >>>";
		String nonAssignmentOperators = "= -= *= /= |= &= ^= += %= <<= >>= >>>=";
		String code = assignmentOperators + " " + nonAssignmentOperators;

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] keywords = code.split(" +");
		for (int i = 0; i < keywords.length; i++) {
			Assert.assertEquals(keywords[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.OPERATOR, token.getType());
			if (i < keywords.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue("Not a single space: " + token, token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	public void testSeparators() {

		String code = "( ) [ ] { }";

		Segment segment = createSegment(code);
		TokenMaker tm = createTokenMaker();
		Token token = tm.getTokenList(segment, TokenTypes.NULL, 0);

		String[] separators = code.split(" +");
		for (int i = 0; i < separators.length; i++) {
			Assert.assertEquals(separators[i], token.getLexeme());
			Assert.assertEquals(TokenTypes.SEPARATOR, token.getType());
			// Just one extra test here
			Assert.assertTrue(token.isSingleChar(TokenTypes.SEPARATOR, separators[i].charAt(0)));
			if (i < separators.length - 1) {
				token = token.getNextToken();
				Assert.assertTrue("Not a whitespace token: " + token, token.isWhitespace());
				Assert.assertTrue("Not a single space: " + token, token.is(TokenTypes.WHITESPACE, " "));
			}
			token = token.getNextToken();
		}

		Assert.assertEquals(TokenTypes.NULL, token.getType());

	}


	@Test
	public void testStringLiterals() {
		assertAllTokensOfType(TokenTypes.LITERAL_STRING_DOUBLE_QUOTE,
			"\"\"",
			"\"hi\"",
			"\"\\u00fe\"",
			"\"\\\"\""
		);
	}


	@Test
	public void testStringLiteral_error() {
		assertAllTokensOfType(TokenTypes.ERROR_STRING_DOUBLE,
			"\"unterminated string",
			"\"string with an invalid \\x escape in it\""
		);
	}


	@Test
	public void testWhiteSpace() {
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
