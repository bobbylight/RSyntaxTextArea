/* The following code was generated by JFlex 1.4.1 on 7/9/16 6:48 PM */

/*
 * 04/12/2012
 *
 * DtdTokenMaker.java - Generates tokens for DTD syntax highlighting.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import java.io.IOException;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractJFlexTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenImpl;


/**
 * Scanner for DTD files.
 *
 * This implementation was created using
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.1; however, the generated file
 * was modified for performance.  Memory allocation needs to be almost
 * completely removed to be competitive with the handwritten lexers (subclasses
 * of <code>AbstractTokenMaker</code>, so this class has been modified so that
 * Strings are never allocated (via yytext()), and the scanner never has to
 * worry about refilling its buffer (needlessly copying chars around).
 * We can achieve this because RText always scans exactly 1 line of tokens at a
 * time, and hands the scanner this line as an array of characters (a Segment
 * really).  Since tokens contain pointers to char arrays instead of Strings
 * holding their contents, there is no need for allocating new memory for
 * Strings.<p>
 *
 * The actual algorithm generated for scanning has, of course, not been
 * modified.<p>
 *
 * If you wish to regenerate this file yourself, keep in mind the following:
 * <ul>
 *   <li>The generated <code>XMLTokenMaker.java</code> file will contain two
 *       definitions of both <code>zzRefill</code> and <code>yyreset</code>.
 *       You should hand-delete the second of each definition (the ones
 *       generated by the lexer), as these generated methods modify the input
 *       buffer, which we'll never have to do.</li>
 *   <li>You should also change the declaration/definition of zzBuffer to NOT
 *       be initialized.  This is a needless memory allocation for us since we
 *       will be pointing the array somewhere else anyway.</li>
 *   <li>You should NOT call <code>yylex()</code> on the generated scanner
 *       directly; rather, you should use <code>getTokenList</code> as you would
 *       with any other <code>TokenMaker</code> instance.</li>
 * </ul>
 *
 * @author Robert Futrell
 * @version 1.0
 */

public class DtdTokenMaker extends AbstractJFlexTokenMaker {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** lexical states */
  public static final int INTAG_START = 2;
  public static final int INTAG_ELEMENT = 3;
  public static final int YYINITIAL = 0;
  public static final int INTAG_ATTLIST = 4;
  public static final int COMMENT = 1;

  /**
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED =
    "\11\0\1\1\1\2\1\0\1\1\23\0\1\1\1\24\1\3\1\41"+
    "\1\7\1\5\1\5\1\4\5\5\1\25\1\22\1\6\12\7\1\20"+
    "\1\5\1\23\1\5\1\26\2\5\1\34\1\7\1\37\1\40\1\27"+
    "\3\7\1\35\2\7\1\30\1\31\1\32\1\7\1\42\1\44\1\43"+
    "\1\36\1\33\1\45\5\7\1\5\1\0\1\5\1\0\1\5\1\0"+
    "\4\7\1\17\1\14\1\7\1\10\1\15\2\7\1\16\3\7\1\12"+
    "\2\7\1\13\1\11\2\7\1\21\3\7\3\0\1\5\uff81\0";

  /**
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /**
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\5\0\1\1\1\2\1\3\1\4\1\5\4\4\1\3"+
    "\1\6\2\3\2\7\2\10\2\7\1\11\5\0\2\3"+
    "\1\10\3\7\5\0\1\12\2\3\3\7\1\13\2\0"+
    "\1\14\2\3\3\7\2\0\2\3\1\15\2\7\2\3"+
    "\2\7\1\16\1\17\1\7";

  private static int [] zzUnpackAction() {
    int [] result = new int[70];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /**
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\46\0\114\0\162\0\230\0\276\0\344\0\u010a"+
    "\0\u0130\0\u0156\0\u017c\0\u01a2\0\u01c8\0\u01ee\0\u0214\0\u0156"+
    "\0\u023a\0\u0260\0\u0286\0\u02ac\0\u02d2\0\u02f8\0\u031e\0\u0344"+
    "\0\u036a\0\u0390\0\u03b6\0\u03dc\0\u0402\0\u0428\0\u044e\0\u0474"+
    "\0\u0156\0\u049a\0\u04c0\0\u04e6\0\u050c\0\u0532\0\u0558\0\u057e"+
    "\0\u05a4\0\u0156\0\u05ca\0\u05f0\0\u0616\0\u063c\0\u0662\0\u0156"+
    "\0\u0688\0\u06ae\0\u06d4\0\u06fa\0\u0720\0\u0746\0\u076c\0\u0792"+
    "\0\u07b8\0\u06d4\0\u07de\0\u0804\0\u02ac\0\u082a\0\u0850\0\u0876"+
    "\0\u089c\0\u08c2\0\u08e8\0\u0214\0\u0214\0\u090e";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[70];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /**
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\6\1\7\21\6\1\10\22\6\2\11\1\12\5\11"+
    "\1\13\3\11\1\14\4\11\1\15\3\11\1\16\20\11"+
    "\1\17\1\7\24\17\1\20\1\21\4\17\1\22\11\17"+
    "\1\23\1\7\24\23\1\20\17\23\1\24\1\7\1\24"+
    "\1\25\1\26\21\24\1\20\10\24\1\27\1\24\1\30"+
    "\4\24\1\6\1\0\21\6\1\0\22\6\1\0\1\7"+
    "\70\0\1\31\21\0\2\11\1\0\5\11\1\0\3\11"+
    "\1\0\4\11\1\0\3\11\1\0\20\11\57\0\1\32"+
    "\45\0\1\33\3\0\1\34\51\0\1\35\51\0\1\36"+
    "\20\0\1\17\1\0\24\17\1\0\20\17\1\0\24\17"+
    "\1\0\1\17\1\37\16\17\1\0\24\17\1\0\4\17"+
    "\1\40\12\17\1\23\1\0\24\23\1\0\17\23\1\24"+
    "\1\0\1\24\2\0\21\24\1\0\17\24\3\25\1\41"+
    "\42\25\4\26\1\41\41\26\1\24\1\0\1\24\2\0"+
    "\21\24\1\0\11\24\1\42\6\24\1\0\1\24\2\0"+
    "\21\24\1\0\6\24\1\43\5\24\1\44\2\24\25\0"+
    "\1\45\31\0\1\46\46\0\1\47\51\0\1\50\50\0"+
    "\1\51\52\0\1\52\17\0\1\17\1\0\24\17\1\0"+
    "\1\53\17\17\1\0\24\17\1\0\4\17\1\54\12\17"+
    "\1\24\1\0\1\24\2\0\21\24\1\0\5\24\1\55"+
    "\12\24\1\0\1\24\2\0\21\24\1\0\2\24\1\56"+
    "\15\24\1\0\1\24\2\0\21\24\1\0\1\57\16\24"+
    "\25\0\1\60\32\0\1\61\53\0\1\62\44\0\1\47"+
    "\50\0\1\63\23\0\1\17\1\0\24\17\1\0\2\17"+
    "\1\64\15\17\1\0\24\17\1\0\1\17\1\65\15\17"+
    "\1\24\1\0\1\24\2\0\21\24\1\0\4\24\1\66"+
    "\13\24\1\0\1\24\2\0\21\24\1\0\13\24\1\67"+
    "\4\24\1\0\1\24\2\0\21\24\1\0\15\24\1\70"+
    "\1\24\13\0\1\47\4\0\1\62\33\0\1\71\43\0"+
    "\2\72\12\63\1\72\1\63\1\72\1\0\2\72\1\0"+
    "\12\63\1\72\4\63\1\17\1\0\24\17\1\0\1\73"+
    "\17\17\1\0\24\17\1\0\6\17\1\74\10\17\1\24"+
    "\1\0\1\24\2\0\21\24\1\0\5\24\1\75\12\24"+
    "\1\0\1\24\2\0\21\24\1\0\1\24\1\76\16\24"+
    "\1\0\1\24\2\0\21\24\1\0\16\24\1\77\6\0"+
    "\1\63\37\0\1\17\1\0\24\17\1\0\3\17\1\100"+
    "\14\17\1\0\24\17\1\0\7\17\1\101\7\17\1\24"+
    "\1\0\1\24\2\0\21\24\1\0\6\24\1\102\11\24"+
    "\1\0\1\24\2\0\21\24\1\0\6\24\1\103\10\24"+
    "\1\17\1\0\24\17\1\0\4\17\1\104\13\17\1\0"+
    "\24\17\1\0\4\17\1\105\12\17\1\24\1\0\1\24"+
    "\2\0\21\24\1\0\1\106\17\24\1\0\1\24\2\0"+
    "\21\24\1\0\14\24\1\102\3\24\1\0\1\24\2\0"+
    "\21\24\1\0\11\24\1\75\5\24";

  private static int [] zzUnpackTrans() {
    int [] result = new int[2356];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\5\0\4\1\1\11\5\1\1\11\11\1\5\0\2\1"+
    "\1\11\3\1\5\0\1\11\5\1\1\11\2\0\6\1"+
    "\2\0\14\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[70];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /* user code: */

	/**
	 * Token type specific to XMLTokenMaker denoting a line ending with an
	 * unclosed XML tag; thus a new line is beginning still inside of the tag.
	 */
	public static final int INTERNAL_INTAG_START					= -1;

	/**
	 * Token type specific to XMLTokenMaker denoting a line ending with an
	 * unclosed DOCTYPE element.
	 */
	public static final int INTERNAL_INTAG_ELEMENT					= -2;

	/**
	 * Token type specific to XMLTokenMaker denoting a line ending with an
	 * unclosed, locally-defined DTD in a DOCTYPE element.
	 */
	public static final int INTERNAL_INTAG_ATTLIST			= -3;

	/**
	 * Token type specific to XMLTokenMaker denoting a line ending with an
	 * unclosed comment.  The state to return to when this comment ends is
	 * embedded in the token type as well.
	 */
	public static final int INTERNAL_IN_COMMENT			= -(1<<11);

	/**
	 * The state we were in prior to the current one.  This is used to know
	 * what state to resume after an MLC ends.
	 */
	private int prevState;


	/**
	 * Constructor.  This must be here because JFlex does not generate a
	 * no-parameter constructor.
	 */
	public DtdTokenMaker() {
	}


	/**
	 * Adds the token specified to the current linked list of tokens as an
	 * "end token;" that is, at <code>zzMarkedPos</code>.
	 *
	 * @param tokenType The token's type.
	 */
	private void addEndToken(int tokenType) {
		addToken(zzMarkedPos,zzMarkedPos, tokenType);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 * @see #addToken(int, int, int)
	 */
	private void addHyperlinkToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so, true);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int tokenType) {
		addToken(zzStartRead, zzMarkedPos-1, tokenType);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param tokenType The token's type.
	 */
	private void addToken(int start, int end, int tokenType) {
		int so = start + offsetShift;
		addToken(zzBuffer, start,end, tokenType, so);
	}


	/**
	 * Adds the token specified to the current linked list of tokens.
	 *
	 * @param array The character array.
	 * @param start The starting offset in the array.
	 * @param end The ending offset in the array.
	 * @param tokenType The token's type.
	 * @param startOffset The offset in the document at which this token
	 *                    occurs.
	 */
	@Override
	public void addToken(char[] array, int start, int end, int tokenType, int startOffset) {
		super.addToken(array, start,end, tokenType, startOffset);
		zzStartRead = zzMarkedPos;
	}


	/**
	 * Always returns <tt>false</tt>, as you never want "mark occurrences"
	 * working in XML files.
	 *
	 * @param type The token type.
	 * @return Whether tokens of this type should have "mark occurrences"
	 *         enabled.
	 */
	@Override
	public boolean getMarkOccurrencesOfTokenType(int type) {
		return false;
	}


	/**
	 * Returns the first token in the linked list of tokens generated
	 * from <code>text</code>.  This method must be implemented by
	 * subclasses so they can correctly implement syntax highlighting.
	 *
	 * @param text The text from which to get tokens.
	 * @param initialTokenType The token type we should start with.
	 * @param startOffset The offset into the document at which
	 *        <code>text</code> starts.
	 * @return The first <code>Token</code> in a linked list representing
	 *         the syntax highlighted text.
	 */
	@Override
	public Token getTokenList(Segment text, int initialTokenType, int startOffset) {

		resetTokenList();
		this.offsetShift = -text.offset + startOffset;
		prevState = YYINITIAL;

		// Start off in the proper state.
		int state = YYINITIAL;
		switch (initialTokenType) {
			case INTERNAL_INTAG_START:
				state = INTAG_START;
				break;
			case INTERNAL_INTAG_ELEMENT:
				state = INTAG_ELEMENT;
				break;
			case INTERNAL_INTAG_ATTLIST:
				state = INTAG_ATTLIST;
				break;
			default:
				if (initialTokenType<-1024) { // INTERNAL_IN_COMMENT - prevState
					int main = -(-initialTokenType & 0xffffff00);
					switch (main) {
						default: // Should never happen
						case INTERNAL_IN_COMMENT:
							state = COMMENT;
							break;
					}
					prevState = -initialTokenType&0xff;
				}
				else { // Shouldn't happen
					state = YYINITIAL;
				}
		}

		start = text.offset;
		s = text;
		try {
			yyreset(zzReader);
			yybegin(state);
			return yylex();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new TokenImpl();
		}

	}


	/**
	 * Refills the input buffer.
	 *
	 * @return      <code>true</code> if EOF was reached, otherwise
	 *              <code>false</code>.
	 */
	private boolean zzRefill() {
		return zzCurrentPos>=s.offset+s.count;
	}


	/**
	 * Resets the scanner to read from a new input stream.
	 * Does not close the old reader.
	 *
	 * All internal variables are reset, the old input stream
	 * <b>cannot</b> be reused (internal buffer is discarded and lost).
	 * Lexical state is set to <tt>YY_INITIAL</tt>.
	 *
	 * @param reader   the new input stream
	 */
	public final void yyreset(java.io.Reader reader) {
		// 's' has been updated.
		zzBuffer = s.array;
		/*
		 * We replaced the line below with the two below it because zzRefill
		 * no longer "refills" the buffer (since the way we do it, it's always
		 * "full" the first time through, since it points to the segment's
		 * array).  So, we assign zzEndRead here.
		 */
		//zzStartRead = zzEndRead = s.offset;
		zzStartRead = s.offset;
		zzEndRead = zzStartRead + s.count - 1;
		zzCurrentPos = zzMarkedPos = s.offset;
		zzLexicalState = YYINITIAL;
		zzReader = reader;
		zzAtEOF  = false;
	}




  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public DtdTokenMaker(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public DtdTokenMaker(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /**
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 138) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  @Override
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public org.fife.ui.rsyntaxtextarea.Token yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = zzLexicalState;


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 3:
          { addToken(Token.IDENTIFIER);
          }
        case 16: break;
        case 2:
          { addToken(Token.WHITESPACE);
          }
        case 17: break;
        case 1:
          { /* Not really valid */ addToken(Token.IDENTIFIER);
          }
        case 18: break;
        case 12:
          { int temp=zzStartRead; addToken(start,zzStartRead-1, Token.MARKUP_COMMENT); addHyperlinkToken(temp,zzMarkedPos-1, Token.MARKUP_COMMENT); start = zzMarkedPos;
          }
        case 19: break;
        case 9:
          { addToken(Token.MARKUP_TAG_DELIMITER); yybegin(INTAG_START);
          }
        case 20: break;
        case 6:
          { addToken(Token.MARKUP_TAG_DELIMITER); yybegin(YYINITIAL);
          }
        case 21: break;
        case 10:
          { int temp = zzMarkedPos; addToken(start,zzStartRead+2, Token.MARKUP_COMMENT); start = temp; yybegin(prevState);
          }
        case 22: break;
        case 11:
          { start = zzStartRead; prevState = zzLexicalState; yybegin(COMMENT);
          }
        case 23: break;
        case 7:
          { addToken(Token.MARKUP_TAG_ATTRIBUTE);
          }
        case 24: break;
        case 15:
          { addToken(Token.MARKUP_TAG_NAME); yybegin(INTAG_ATTLIST);
          }
        case 25: break;
        case 14:
          { addToken(Token.MARKUP_TAG_NAME); yybegin(INTAG_ELEMENT);
          }
        case 26: break;
        case 13:
          { addToken(Token.MARKUP_PROCESSING_INSTRUCTION);
          }
        case 27: break;
        case 4:
          {
          }
        case 28: break;
        case 5:
          { addToken(start,zzStartRead-1, Token.MARKUP_COMMENT); addEndToken(INTERNAL_IN_COMMENT - prevState); return firstToken;
          }
        case 29: break;
        case 8:
          { addToken(Token.MARKUP_TAG_ATTRIBUTE_VALUE);
          }
        case 30: break;
        default:
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            switch (zzLexicalState) {
            case INTAG_START: {
              addEndToken(INTERNAL_INTAG_START); return firstToken;
            }
            case 71: break;
            case INTAG_ELEMENT: {
              addEndToken(INTERNAL_INTAG_ELEMENT); return firstToken;
            }
            case 72: break;
            case YYINITIAL: {
              addNullToken(); return firstToken;
            }
            case 73: break;
            case INTAG_ATTLIST: {
              addEndToken(INTERNAL_INTAG_ATTLIST); return firstToken;
            }
            case 74: break;
            case COMMENT: {
              addToken(start,zzStartRead-1, Token.MARKUP_COMMENT); addEndToken(INTERNAL_IN_COMMENT - prevState); return firstToken;
            }
            case 75: break;
            default:
            return null;
            }
          }
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
