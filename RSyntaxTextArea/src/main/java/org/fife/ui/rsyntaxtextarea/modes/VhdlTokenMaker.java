/* The following code was generated by JFlex 1.4.1 on 1/30/25, 6:32 PM */

/*
 * ORIGINAL VERSION SOURCE LINK
 * https://github.com/logisim-evolution/
 *
 *
 * THIS IS MY VERSION
 * File current version https://github.com/Var7600/VHDL_GENERATOR
 *
 *
 * VhdlTokenMaker.java - Scanner for the VHDL hardware description language.
 *
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea.modes;

import java.io.*;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractJFlexTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenImpl;



/**
 * A parser for the VHDL hardware description programming language.
 *
 * @author DOUDOU DIAWARA
 * @version 0.0
 *
 */

public class VhdlTokenMaker extends AbstractJFlexTokenMaker {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED =
    "\11\0\1\3\1\1\1\0\1\3\1\2\22\0\1\3\1\0\1\11"+
    "\3\0\1\52\1\43\1\45\1\45\1\53\1\0\1\45\1\22\2\0"+
    "\1\5\1\5\6\20\2\4\1\46\1\45\1\51\1\47\1\50\2\0"+
    "\1\23\1\16\1\24\1\12\1\25\1\6\1\14\1\31\1\13\1\41"+
    "\1\35\1\30\1\36\1\34\1\17\1\37\1\44\1\27\1\26\1\15"+
    "\1\32\1\42\1\40\1\21\1\33\1\7\1\45\1\0\1\45\1\0"+
    "\1\10\1\0\1\23\1\16\1\24\1\12\1\25\1\6\1\14\1\31"+
    "\1\13\1\41\1\35\1\30\1\36\1\34\1\17\1\37\1\44\1\27"+
    "\1\26\1\15\1\32\1\42\1\40\1\21\1\33\1\7\1\0\1\45"+
    "\uff83\0";

  /**
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /**
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\2\1\1\3\2\4\1\1\7\4"+
    "\1\1\14\4\1\1\2\5\2\1\1\6\1\1\1\7"+
    "\2\0\4\4\1\0\1\10\2\4\2\11\7\4\1\0"+
    "\5\4\1\0\1\4\1\6\1\4\1\0\2\4\1\12"+
    "\50\4\17\0\16\4\1\0\1\13\5\4\1\0\1\4"+
    "\1\0\44\4\1\14\31\0\10\4\1\14\1\15\41\4"+
    "\16\0\1\11\5\0\1\11\2\0\30\4\14\0\1\11"+
    "\1\0\22\4\12\0\1\13\7\4\10\0\5\4\1\11"+
    "\5\0\1\13\1\4\5\0\2\4\2\0\2\4\1\0"+
    "\4\4";

  private static int [] zzUnpackAction() {
    int [] result = new int[388];
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
    "\0\0\0\54\0\54\0\130\0\204\0\260\0\334\0\u0108"+
    "\0\u0134\0\u0160\0\u018c\0\u01b8\0\u01e4\0\u0210\0\u023c\0\u0268"+
    "\0\u0294\0\u02c0\0\u02ec\0\u0318\0\u0344\0\u0370\0\u039c\0\u03c8"+
    "\0\u03f4\0\u0420\0\u044c\0\u0478\0\u04a4\0\u04d0\0\54\0\u04fc"+
    "\0\u0528\0\u04fc\0\54\0\u0554\0\204\0\u0580\0\u05ac\0\u05d8"+
    "\0\u0604\0\u0630\0\u065c\0\u0134\0\54\0\u0688\0\u06b4\0\u0108"+
    "\0\u06e0\0\u070c\0\u0738\0\u0764\0\u0790\0\u07bc\0\u07e8\0\u0814"+
    "\0\u0840\0\u086c\0\u0898\0\u08c4\0\u08f0\0\u091c\0\u0948\0\u0974"+
    "\0\u0108\0\u09a0\0\u09cc\0\u09f8\0\u0a24\0\u0a50\0\u0a7c\0\u0aa8"+
    "\0\u0ad4\0\u0b00\0\u0b2c\0\u0b58\0\u0b84\0\u0bb0\0\u0bdc\0\u0c08"+
    "\0\u0c34\0\u0c60\0\u0c8c\0\u0cb8\0\u0ce4\0\u0d10\0\u0d3c\0\u0d68"+
    "\0\u0d94\0\u0dc0\0\u0dec\0\u0e18\0\u0e44\0\u0e70\0\u0e9c\0\u0ec8"+
    "\0\u0ef4\0\u0f20\0\u0f4c\0\u0f78\0\u0fa4\0\u0fd0\0\u0ffc\0\u1028"+
    "\0\u1054\0\u1080\0\u10ac\0\u10d8\0\u1104\0\u1130\0\u115c\0\u1188"+
    "\0\u11b4\0\u11e0\0\u120c\0\u1238\0\u1264\0\u1290\0\u12bc\0\u12e8"+
    "\0\u1314\0\u1340\0\u136c\0\u1398\0\u13c4\0\u13f0\0\u141c\0\u1448"+
    "\0\u1474\0\u14a0\0\u14cc\0\u14f8\0\u1524\0\u1550\0\u157c\0\u15a8"+
    "\0\u15d4\0\u1600\0\u162c\0\u1658\0\u1684\0\u16b0\0\u16dc\0\u1708"+
    "\0\u1734\0\u1760\0\u178c\0\u17b8\0\u17e4\0\u1810\0\u183c\0\u1868"+
    "\0\u1894\0\u18c0\0\u18ec\0\u1918\0\u1944\0\u1970\0\u199c\0\u19c8"+
    "\0\u19f4\0\u1a20\0\u1a4c\0\u1a78\0\u1aa4\0\u1ad0\0\u1afc\0\u1b28"+
    "\0\u1b54\0\u1b80\0\u1bac\0\u1bd8\0\u1c04\0\u1c30\0\u1c5c\0\u1c88"+
    "\0\u1cb4\0\u1ce0\0\u1d0c\0\u1d38\0\u1d64\0\u1d90\0\u1dbc\0\u1de8"+
    "\0\u1e14\0\54\0\u1e40\0\u1e6c\0\u1e98\0\u1ec4\0\u1ef0\0\u1f1c"+
    "\0\u1f48\0\u1f74\0\u1fa0\0\u1fcc\0\u1ff8\0\u2024\0\u2050\0\u207c"+
    "\0\u20a8\0\u20d4\0\u2100\0\u212c\0\u2158\0\u2184\0\u21b0\0\u21dc"+
    "\0\u2208\0\u2234\0\u2260\0\u228c\0\u22b8\0\u22e4\0\u2310\0\u233c"+
    "\0\u2368\0\u2394\0\u23c0\0\u0108\0\54\0\u23ec\0\u2418\0\u2444"+
    "\0\u2470\0\u249c\0\u24c8\0\u24f4\0\u2520\0\u254c\0\u2578\0\u25a4"+
    "\0\u25d0\0\u25fc\0\u2628\0\u2654\0\u2680\0\u26ac\0\u26d8\0\u2704"+
    "\0\u2730\0\u275c\0\u2788\0\u27b4\0\u27e0\0\u280c\0\u2838\0\u2864"+
    "\0\u2890\0\u28bc\0\u28e8\0\u2914\0\u2940\0\u296c\0\u2998\0\u29c4"+
    "\0\u29f0\0\u2a1c\0\u2a48\0\u2a74\0\u2aa0\0\u2acc\0\u2af8\0\u2b24"+
    "\0\u2b50\0\u2b7c\0\u2ba8\0\u2bd4\0\54\0\u2c00\0\u2c2c\0\u2c58"+
    "\0\u2c84\0\u2cb0\0\u2cdc\0\u2d08\0\u2d34\0\u2d60\0\u2d8c\0\u2db8"+
    "\0\u2de4\0\u2e10\0\u2e3c\0\u2e68\0\u2e94\0\u2ec0\0\u2eec\0\u2f18"+
    "\0\u2f44\0\u2f70\0\u2f9c\0\u2fc8\0\u2ff4\0\u3020\0\u304c\0\u3078"+
    "\0\u30a4\0\u30d0\0\u30fc\0\u3128\0\u3154\0\u3180\0\u31ac\0\u31d8"+
    "\0\u3204\0\u3230\0\u325c\0\u3288\0\u32b4\0\u32e0\0\u330c\0\u3338"+
    "\0\u3364\0\u3390\0\u33bc\0\u33e8\0\u3414\0\u3440\0\u346c\0\u3498"+
    "\0\u34c4\0\u34f0\0\u351c\0\u3548\0\u3574\0\u35a0\0\u35cc\0\u35f8"+
    "\0\u3624\0\u3650\0\u367c\0\u36a8\0\u36d4\0\u3700\0\u372c\0\u3758"+
    "\0\u3784\0\u37b0\0\u37dc\0\u3808\0\u3834\0\u3860\0\u388c\0\u0108"+
    "\0\u38b8\0\u38e4\0\u3910\0\u393c\0\u3968\0\u3994\0\u39c0\0\u39ec"+
    "\0\u3a18\0\u3a44\0\u3a70\0\u3a9c\0\u3ac8\0\u3af4\0\u3b20\0\u3b4c"+
    "\0\u3b78\0\u3ba4\0\u3bd0\0\u3bfc\0\u3c28\0\u3c54\0\u3c80\0\u3cac"+
    "\0\u3cd8\0\u3d04\0\u3d30\0\u3d5c\0\u3d88\0\u3db4\0\u3de0\0\u3e0c"+
    "\0\u2cdc\0\u3e38\0\u3e64\0\u3e90\0\u3ebc\0\u3ee8\0\u3f14\0\u3f40"+
    "\0\u3f6c\0\u3f98\0\u3fc4\0\u3ff0";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[388];
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
    "\1\2\1\3\1\4\1\5\2\6\1\7\2\10\1\11"+
    "\1\12\1\13\1\14\1\15\1\16\1\17\1\6\1\20"+
    "\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\10"+
    "\1\30\1\10\1\31\1\10\1\32\1\33\1\34\1\10"+
    "\1\35\1\36\1\10\1\37\1\40\1\41\1\2\1\42"+
    "\1\43\1\44\55\0\1\3\55\0\1\45\60\0\1\46"+
    "\1\0\1\47\45\0\5\10\1\0\1\10\1\50\3\10"+
    "\1\51\2\10\1\0\1\52\6\10\1\53\10\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\20\10\1\0"+
    "\1\10\7\0\1\54\2\0\6\54\1\55\42\54\4\0"+
    "\5\10\1\0\1\10\1\56\3\10\1\57\2\10\1\0"+
    "\20\10\1\0\1\10\13\0\2\10\1\60\2\10\1\0"+
    "\10\10\1\0\3\10\1\60\5\10\1\61\1\10\1\62"+
    "\4\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\2\10\1\63\1\10\1\64\2\10\1\65\10\10\1\0"+
    "\1\10\13\0\5\10\1\0\5\10\1\60\2\10\1\0"+
    "\4\10\1\66\1\10\1\67\1\10\1\70\7\10\1\0"+
    "\1\10\13\0\5\10\1\71\1\10\1\72\3\10\1\73"+
    "\2\10\1\0\2\10\1\74\2\10\1\75\1\10\1\76"+
    "\10\10\1\0\1\10\13\0\2\10\1\60\2\10\1\77"+
    "\3\10\1\100\4\10\1\0\4\10\1\101\2\10\1\102"+
    "\1\10\1\60\2\10\1\67\3\10\1\0\1\10\13\0"+
    "\5\10\1\103\5\10\1\104\2\10\1\0\11\10\1\105"+
    "\6\10\1\0\1\10\31\0\1\106\35\0\2\10\1\107"+
    "\2\10\1\0\3\10\1\110\1\111\3\10\1\0\1\10"+
    "\1\112\1\10\1\113\1\114\1\115\3\10\1\116\6\10"+
    "\1\0\1\10\13\0\5\10\1\0\5\10\1\117\2\10"+
    "\1\0\1\120\17\10\1\0\1\10\13\0\5\10\1\0"+
    "\7\10\1\121\1\0\5\10\1\122\3\10\1\123\6\10"+
    "\1\0\1\10\13\0\5\10\1\0\1\10\1\124\1\10"+
    "\1\125\4\10\1\0\2\10\1\126\1\10\2\127\1\130"+
    "\1\131\10\10\1\0\1\10\13\0\5\10\1\0\1\10"+
    "\1\132\3\10\1\133\2\10\1\0\1\134\1\10\1\135"+
    "\15\10\1\0\1\10\13\0\5\10\1\0\1\10\1\136"+
    "\3\10\1\137\2\10\1\0\1\140\17\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\3\10\1\141\5\10"+
    "\1\142\6\10\1\0\1\10\13\0\5\10\1\0\5\10"+
    "\1\143\2\10\1\0\1\144\1\10\1\145\4\10\1\146"+
    "\10\10\1\0\1\10\13\0\5\10\1\0\5\10\1\116"+
    "\2\10\1\0\1\147\17\10\1\0\1\10\13\0\5\10"+
    "\1\0\5\10\1\150\2\10\1\0\1\151\3\10\1\152"+
    "\2\10\1\153\10\10\1\0\1\10\13\0\5\10\1\0"+
    "\1\10\1\154\6\10\1\0\1\121\5\10\1\155\11\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\1\156"+
    "\17\10\1\0\1\10\14\0\1\157\1\0\1\157\2\0"+
    "\1\160\1\161\1\0\1\162\1\163\2\0\2\157\1\164"+
    "\1\0\1\165\1\166\1\167\1\170\1\171\1\157\4\0"+
    "\1\172\1\157\1\0\1\173\1\0\1\174\56\0\1\37"+
    "\54\0\1\37\56\0\1\43\12\0\1\47\54\0\1\175"+
    "\44\0\5\10\1\0\10\10\1\0\5\10\1\141\12\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\4\10"+
    "\1\60\13\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\5\10\1\176\12\10\1\0\1\10\13\0\5\10"+
    "\1\0\10\10\1\0\11\10\1\177\6\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\3\10\1\200\14\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\15\10"+
    "\1\201\2\10\1\0\1\10\13\0\5\10\1\0\3\10"+
    "\1\202\1\10\1\203\2\10\1\0\2\10\1\204\15\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\14\10"+
    "\1\205\3\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\11\10\1\206\6\10\1\0\1\10\13\0\5\10"+
    "\1\0\5\10\1\207\2\10\1\0\20\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\1\210\17\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\1\211\6\10"+
    "\1\212\10\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\2\10\1\213\15\10\1\0\1\10\13\0\5\10"+
    "\1\0\10\10\1\0\14\10\1\141\3\10\1\0\1\10"+
    "\14\0\1\214\52\0\5\10\1\0\3\10\1\215\4\10"+
    "\1\0\20\10\1\0\1\10\13\0\5\10\1\0\1\216"+
    "\4\10\1\217\2\10\1\0\20\10\1\0\1\10\13\0"+
    "\5\10\1\0\2\10\1\220\5\10\1\0\20\10\1\0"+
    "\1\10\13\0\5\10\1\0\5\10\1\221\2\10\1\0"+
    "\20\10\1\0\1\10\13\0\2\10\1\222\2\10\1\0"+
    "\10\10\1\0\3\10\1\60\14\10\1\0\1\10\14\0"+
    "\1\223\12\0\1\223\37\0\5\10\1\0\10\10\1\0"+
    "\6\10\1\224\11\10\1\0\1\10\13\0\5\10\1\0"+
    "\3\10\1\60\4\10\1\0\20\10\1\0\1\10\13\0"+
    "\3\225\3\0\1\225\3\0\1\225\1\0\1\225\2\0"+
    "\3\225\32\0\5\10\1\0\10\10\1\0\4\10\1\101"+
    "\13\10\1\0\1\10\13\0\5\10\1\0\5\10\1\104"+
    "\2\10\1\0\20\10\1\0\1\10\7\0\1\106\1\0"+
    "\52\106\4\0\5\10\1\0\3\10\1\226\4\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\3\10\1\227"+
    "\4\10\1\0\20\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\3\10\1\101\14\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\1\10\1\230\16\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\3\10\1\231"+
    "\14\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\1\10\1\232\2\10\1\233\13\10\1\0\1\10\13\0"+
    "\5\10\1\0\1\10\1\234\6\10\1\0\5\10\1\60"+
    "\12\10\1\0\1\10\13\0\5\10\1\0\1\101\7\10"+
    "\1\0\20\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\11\10\1\235\1\10\1\236\4\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\3\10\1\141\14\10"+
    "\1\0\1\10\13\0\5\10\1\0\1\10\1\102\6\10"+
    "\1\0\20\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\3\10\1\237\14\10\1\0\1\10\13\0\5\10"+
    "\1\0\1\60\2\10\1\240\4\10\1\0\20\10\1\0"+
    "\1\10\13\0\5\10\1\0\2\10\1\241\5\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\1\242\7\10"+
    "\1\0\20\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\5\10\1\243\11\10\1\244\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\1\101\4\10\1\101\12\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\1\245"+
    "\17\10\1\0\1\10\13\0\5\10\1\0\4\10\1\246"+
    "\3\10\1\0\20\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\3\10\1\247\14\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\4\10\2\101\12\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\11\10\1\250"+
    "\6\10\1\0\1\10\13\0\5\10\1\0\2\10\1\251"+
    "\1\252\4\10\1\0\1\10\1\253\11\10\1\101\1\254"+
    "\1\10\1\243\1\10\1\0\1\10\13\0\5\10\1\0"+
    "\3\10\1\255\1\256\3\10\1\0\11\10\1\257\6\10"+
    "\1\0\1\10\13\0\5\10\1\0\5\10\1\147\2\10"+
    "\1\0\20\10\1\0\1\10\13\0\5\10\1\0\4\10"+
    "\1\260\3\10\1\0\20\10\1\0\1\10\13\0\5\10"+
    "\1\0\10\10\1\0\2\10\1\60\15\10\1\0\1\10"+
    "\13\0\5\10\1\0\1\10\1\261\1\10\1\262\4\10"+
    "\1\0\1\263\17\10\1\0\1\10\13\0\5\10\1\0"+
    "\3\10\1\101\4\10\1\0\4\10\1\101\13\10\1\0"+
    "\1\10\13\0\5\10\1\0\3\10\1\264\4\10\1\0"+
    "\11\10\1\116\6\10\1\0\1\10\13\0\5\10\1\0"+
    "\7\10\1\102\1\0\15\10\1\60\2\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\5\10\1\265\12\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\14\10"+
    "\1\60\3\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\3\10\1\266\1\102\13\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\1\10\1\257\16\10\1\0"+
    "\1\10\13\0\5\10\1\0\5\10\1\267\2\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\4\10\1\141\13\10\1\0\1\10\13\0\5\10\1\0"+
    "\3\10\1\270\4\10\1\0\20\10\1\0\1\10\13\0"+
    "\5\10\1\0\1\10\1\50\6\10\1\0\2\10\1\213"+
    "\15\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\4\10\1\271\13\10\1\0\1\10\52\0\1\272\35\0"+
    "\1\273\1\0\1\274\60\0\1\275\1\0\1\276\44\0"+
    "\1\277\47\0\1\300\54\0\1\301\1\0\1\302\67\0"+
    "\1\303\24\0\1\304\1\0\1\305\14\0\1\306\34\0"+
    "\1\307\7\0\1\310\1\0\1\311\45\0\1\312\3\0"+
    "\1\313\1\0\1\314\15\0\1\272\23\0\1\315\27\0"+
    "\1\272\27\0\1\316\3\0\1\317\3\0\1\320\47\0"+
    "\1\321\62\0\1\322\35\0\1\323\43\0\5\10\1\0"+
    "\10\10\1\0\3\10\1\212\14\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\1\10\1\324\16\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\1\10\1\325"+
    "\16\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\11\10\1\326\6\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\2\10\1\327\15\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\7\10\1\102\10\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\4\10\1\330"+
    "\13\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\7\10\1\153\10\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\2\10\1\331\15\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\7\10\1\147\10\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\4\10\1\332"+
    "\13\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\11\10\1\333\6\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\2\10\1\334\15\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\11\10\1\60\6\10\1\0"+
    "\1\10\14\0\1\214\2\0\1\71\1\335\46\0\4\10"+
    "\1\336\1\0\10\10\1\0\20\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\10\10\1\60\7\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\5\10\1\337"+
    "\12\10\1\0\1\10\13\0\5\10\1\0\1\10\1\213"+
    "\6\10\1\0\20\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\1\10\1\340\16\10\1\0\1\10\13\0"+
    "\2\10\1\226\2\10\1\0\10\10\1\0\20\10\1\0"+
    "\1\10\14\0\1\223\2\0\1\77\1\335\6\0\1\223"+
    "\37\0\5\10\1\0\10\10\1\0\2\10\1\341\15\10"+
    "\1\0\1\10\13\0\3\225\1\0\1\103\1\335\1\225"+
    "\3\0\1\225\1\0\1\225\2\0\3\225\32\0\5\10"+
    "\1\0\10\10\1\0\2\10\1\51\15\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\4\10\1\342\13\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\2\10"+
    "\1\343\15\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\2\10\1\344\15\10\1\0\1\10\13\0\5\10"+
    "\1\0\10\10\1\0\6\10\1\345\11\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\1\216\17\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\1\346\17\10"+
    "\1\0\1\10\13\0\2\10\1\347\2\10\1\0\10\10"+
    "\1\0\3\10\1\350\14\10\1\0\1\10\13\0\5\10"+
    "\1\0\10\10\1\0\14\10\1\351\3\10\1\0\1\10"+
    "\13\0\5\10\1\0\1\10\1\352\6\10\1\0\2\10"+
    "\1\60\15\10\1\0\1\10\13\0\5\10\1\0\1\10"+
    "\1\353\6\10\1\0\20\10\1\0\1\10\13\0\5\10"+
    "\1\0\10\10\1\0\11\10\1\354\6\10\1\0\1\10"+
    "\13\0\4\10\1\355\1\0\10\10\1\0\20\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\2\10\1\356"+
    "\15\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\2\10\1\357\15\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\4\10\1\360\13\10\1\0\1\10\13\0"+
    "\5\10\1\0\3\10\1\361\4\10\1\0\20\10\1\0"+
    "\1\10\13\0\5\10\1\0\1\10\1\362\6\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\2\10\1\141"+
    "\5\10\1\0\20\10\1\0\1\10\13\0\5\10\1\0"+
    "\1\10\1\363\6\10\1\0\20\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\7\10\1\364\10\10\1\0"+
    "\1\10\13\0\5\10\1\0\5\10\1\365\2\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\5\10\1\344"+
    "\2\10\1\0\20\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\2\10\1\366\15\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\4\10\1\367\13\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\12\10\1\370"+
    "\5\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\2\10\1\265\15\10\1\0\1\10\13\0\5\10\1\0"+
    "\3\10\1\346\4\10\1\0\20\10\1\0\1\10\13\0"+
    "\5\10\1\0\1\10\1\265\6\10\1\0\20\10\1\0"+
    "\1\10\13\0\2\10\1\371\2\10\1\0\10\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\7\10\1\372\10\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\5\10\1\60\12\10\1\0\1\10\13\0"+
    "\5\10\1\0\1\10\1\373\1\10\1\374\4\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\1\10\1\375\16\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\6\10\1\60\11\10\1\0\1\10\13\0"+
    "\5\10\1\0\1\10\1\376\6\10\1\0\20\10\1\0"+
    "\1\10\37\0\1\377\36\0\1\u0100\66\0\1\u0101\50\0"+
    "\1\u0102\53\0\1\u0103\56\0\1\u0104\42\0\1\u0105\62\0"+
    "\1\u0106\54\0\1\u0107\64\0\1\u0108\40\0\1\u0109\54\0"+
    "\1\u010a\43\0\1\u010b\73\0\1\u0102\61\0\1\u010c\51\0"+
    "\1\u010d\41\0\1\u010e\33\0\1\u010f\61\0\1\u0110\65\0"+
    "\1\u010d\42\0\1\u0111\63\0\1\u0112\56\0\1\u0113\36\0"+
    "\1\u0114\53\0\1\u0115\44\0\5\10\1\0\3\10\1\u0116"+
    "\4\10\1\0\20\10\1\0\1\10\13\0\5\10\1\0"+
    "\5\10\1\u0117\2\10\1\0\20\10\1\0\1\10\13\0"+
    "\5\10\1\0\3\10\1\u0118\4\10\1\0\20\10\1\0"+
    "\1\10\13\0\5\10\1\0\2\10\1\u0119\5\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\3\10\1\u011a"+
    "\4\10\1\0\20\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\4\10\1\u011b\13\10\1\0\1\10\13\0"+
    "\5\10\1\0\1\360\7\10\1\0\20\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\3\10\1\u011c\14\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\17\10"+
    "\1\u011d\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\2\10\1\u011e\15\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\12\10\1\60\5\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\4\10\1\346\13\10\1\0"+
    "\1\10\13\0\5\10\1\0\1\10\1\u011f\6\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\3\10\1\346\14\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\4\10\1\102\13\10\1\0\1\10\13\0"+
    "\5\10\1\0\1\10\1\u0120\6\10\1\0\20\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\3\10\1\60"+
    "\14\10\1\0\1\10\13\0\5\10\1\0\1\10\1\u0121"+
    "\6\10\1\0\20\10\1\0\1\10\13\0\5\10\1\0"+
    "\3\10\1\u0122\4\10\1\0\20\10\1\0\1\10\13\0"+
    "\5\10\1\0\5\10\1\u0123\2\10\1\0\20\10\1\0"+
    "\1\10\13\0\2\10\1\60\2\10\1\0\10\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\3\10\1\216"+
    "\4\10\1\0\20\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\1\265\17\10\1\0\1\10\13\0\5\10"+
    "\1\0\10\10\1\0\5\10\1\u0124\12\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\1\10\1\102\16\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\4\10"+
    "\1\240\13\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\2\10\1\u0125\15\10\1\0\1\10\13\0\5\10"+
    "\1\0\10\10\1\0\10\10\1\70\7\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\11\10\1\u0126\6\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\3\10"+
    "\1\107\14\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\4\10\1\213\13\10\1\0\1\10\13\0\5\10"+
    "\1\0\10\10\1\0\4\10\1\u0125\13\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\4\10\1\354\13\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\1\u0127"+
    "\17\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\1\250\17\10\1\0\1\10\13\0\2\10\1\u0128\2\10"+
    "\1\0\10\10\1\0\20\10\1\0\1\10\13\0\5\10"+
    "\1\0\10\10\1\0\4\10\1\u0129\13\10\1\0\1\10"+
    "\13\0\5\10\1\0\3\10\1\u012a\4\10\1\0\20\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\14\10"+
    "\1\u012b\3\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\2\10\1\u012c\15\10\1\0\1\10\13\0\5\10"+
    "\1\0\10\10\1\0\1\u012d\17\10\1\0\1\10\32\0"+
    "\1\u012e\72\0\1\u012f\26\0\1\u0130\52\0\1\u0104\73\0"+
    "\1\u0131\44\0\1\u010d\41\0\1\u0132\65\0\1\u0133\62\0"+
    "\1\u0134\56\0\1\u0135\32\0\1\u0136\61\0\1\u0137\60\0"+
    "\1\u010f\47\0\1\u0138\43\0\1\u0139\53\0\1\u013a\67\0"+
    "\1\u010d\53\0\1\u013b\34\0\1\u010d\73\0\1\u0104\46\0"+
    "\1\u0134\43\0\1\6\42\0\5\10\1\0\1\10\1\u013c"+
    "\6\10\1\0\20\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\11\10\1\u013d\6\10\1\0\1\10\13\0"+
    "\5\10\1\0\5\10\1\60\2\10\1\0\20\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\2\10\1\u013e"+
    "\15\10\1\0\1\10\13\0\5\10\1\0\1\10\1\354"+
    "\6\10\1\0\20\10\1\0\1\10\13\0\5\10\1\0"+
    "\1\10\1\u013f\6\10\1\0\1\u0140\17\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\14\10\1\254\3\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\2\10"+
    "\1\u0141\15\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\1\u0142\17\10\1\0\1\10\13\0\5\10\1\0"+
    "\4\10\1\u0143\3\10\1\0\20\10\1\0\1\10\13\0"+
    "\5\10\1\0\3\10\1\u0144\4\10\1\0\20\10\1\0"+
    "\1\10\13\0\5\10\1\0\2\10\1\u0145\5\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\1\u0146\17\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\11\10\1\u0147\6\10\1\0\1\10\13\0\5\10"+
    "\1\0\5\10\1\u0148\2\10\1\0\20\10\1\0\1\10"+
    "\13\0\5\10\1\0\1\60\7\10\1\0\20\10\1\0"+
    "\1\10\13\0\5\10\1\0\2\10\1\u0149\5\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\4\10\1\216\13\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\2\10\1\u014a\15\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\1\u014b\17\10\1\0\1\10"+
    "\13\0\5\10\1\0\1\10\1\u014c\6\10\1\0\20\10"+
    "\1\0\1\10\13\0\5\10\1\0\5\10\1\u014d\2\10"+
    "\1\0\20\10\1\0\1\10\13\0\5\10\1\0\1\205"+
    "\7\10\1\0\3\10\1\346\14\10\1\0\1\10\13\0"+
    "\5\10\1\0\4\10\1\50\3\10\1\0\20\10\1\0"+
    "\1\10\42\0\1\320\33\0\1\u014e\63\0\1\u014f\56\0"+
    "\1\u0150\67\0\1\u0104\45\0\1\u0151\34\0\1\u010d\66\0"+
    "\1\u0152\53\0\1\u0104\50\0\1\u0153\55\0\1\u0154\34\0"+
    "\1\u0155\62\0\1\u0156\44\0\1\u0157\47\0\5\10\1\0"+
    "\5\10\1\213\2\10\1\0\20\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\11\10\1\243\6\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\4\10\1\u0158"+
    "\13\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\1\10\1\60\16\10\1\0\1\10\13\0\5\10\1\0"+
    "\3\10\1\141\4\10\1\0\20\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\1\10\1\u0159\16\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\11\10\1\u0158"+
    "\6\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\7\10\1\u0140\10\10\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\2\10\1\u015a\15\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\7\10\1\u015b\10\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\11\10\1\102"+
    "\6\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\2\10\1\u0146\15\10\1\0\1\10\13\0\5\10\1\0"+
    "\2\10\1\u015c\5\10\1\0\20\10\1\0\1\10\13\0"+
    "\4\10\1\u015d\1\0\10\10\1\0\20\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\1\10\1\u015e\16\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\5\10"+
    "\1\u0158\12\10\1\0\1\10\13\0\5\10\1\0\10\10"+
    "\1\0\17\10\1\u015f\1\0\1\10\13\0\5\10\1\0"+
    "\10\10\1\0\11\10\1\360\6\10\1\0\1\10\43\0"+
    "\1\u0160\53\0\1\u0161\42\0\1\u0162\42\0\1\u0163\66\0"+
    "\1\u013b\54\0\1\316\53\0\1\u0164\50\0\1\u0165\1\0"+
    "\1\165\14\0\1\u0166\17\0\1\u010d\101\0\1\u0167\23\0"+
    "\5\10\1\0\3\10\1\u0168\4\10\1\0\20\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\1\10\1\u0169"+
    "\16\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\4\10\1\u016a\13\10\1\0\1\10\13\0\5\10\1\0"+
    "\1\10\1\u016b\6\10\1\0\20\10\1\0\1\10\13\0"+
    "\5\10\1\0\10\10\1\0\2\10\1\u016c\15\10\1\0"+
    "\1\10\13\0\5\10\1\0\3\10\1\360\4\10\1\0"+
    "\20\10\1\0\1\10\13\0\5\10\1\0\10\10\1\0"+
    "\2\10\1\u0158\15\10\1\0\1\10\23\0\1\u016d\63\0"+
    "\1\u0152\53\0\1\u016e\42\0\1\u016f\65\0\1\u0170\52\0"+
    "\1\301\52\0\1\u0171\53\0\1\u0172\34\0\5\10\1\0"+
    "\5\10\1\u013e\2\10\1\0\20\10\1\0\1\10\13\0"+
    "\5\10\1\0\3\10\1\205\4\10\1\0\20\10\1\0"+
    "\1\10\13\0\5\10\1\0\10\10\1\0\1\324\17\10"+
    "\1\0\1\10\13\0\5\10\1\0\10\10\1\0\1\10"+
    "\1\u0173\1\10\1\u0174\14\10\1\0\1\10\13\0\5\10"+
    "\1\0\1\250\7\10\1\0\20\10\1\0\1\10\17\0"+
    "\1\u0175\60\0\1\u0176\72\0\1\u0177\27\0\1\u0178\73\0"+
    "\1\u0179\61\0\1\u0104\21\0\4\10\1\u017a\1\0\10\10"+
    "\1\0\20\10\1\0\1\10\13\0\4\10\1\u017b\1\0"+
    "\10\10\1\0\20\10\1\0\1\10\51\0\1\u0166\24\0"+
    "\1\u017c\54\0\1\u010d\66\0\1\u017d\30\0\5\10\1\0"+
    "\10\10\1\0\7\10\1\u017e\7\10\1\u011d\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\3\10\1\u017f\14\10"+
    "\1\0\1\10\26\0\1\u0180\57\0\1\310\34\0\5\10"+
    "\1\0\10\10\1\0\11\10\1\u017b\6\10\1\0\1\10"+
    "\13\0\5\10\1\0\1\10\1\u0181\6\10\1\0\20\10"+
    "\1\0\1\10\43\0\1\u010d\23\0\5\10\1\0\2\10"+
    "\1\u0182\5\10\1\0\20\10\1\0\1\10\13\0\5\10"+
    "\1\0\10\10\1\0\11\10\1\u0183\6\10\1\0\1\10"+
    "\13\0\5\10\1\0\10\10\1\0\2\10\1\u0184\15\10"+
    "\1\0\1\10\13\0\5\10\1\0\1\u0158\7\10\1\0"+
    "\20\10\1\0\1\10\7\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[16412];
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
    "\1\0\2\11\33\1\1\11\3\1\1\11\2\1\2\0"+
    "\4\1\1\0\1\11\13\1\1\0\5\1\1\0\3\1"+
    "\1\0\53\1\17\0\16\1\1\0\6\1\1\0\1\1"+
    "\1\0\44\1\1\11\31\0\11\1\1\11\41\1\16\0"+
    "\1\11\5\0\1\1\2\0\30\1\14\0\1\1\1\0"+
    "\22\1\12\0\10\1\10\0\6\1\5\0\2\1\5\0"+
    "\2\1\2\0\2\1\1\0\4\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[388];
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
	 * Constructor.  This must be here because JFlex does not generate a
	 * no-parameter constructor.
	 */
	public VhdlTokenMaker() {
		super();
	}

    /**
	* Adds the token specified to the current linked list of tokens.
	*
	*@param tokenType The token's type.
	*/
    private void addToken(int tokenType){
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
		super.addToken(array, start, end, tokenType, startOffset);
		zzStartRead = zzMarkedPos;
	}

		@Override
	public String[] getLineCommentStartAndEnd(int languageIndex) {
		return new String[] { "--", null };
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
        // VHDL only needs the initial state
		s = text;
		try {
			yyreset(zzReader);
			yybegin(YYINITIAL);
			return yylex();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new TokenImpl();
		}

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
	public final void yyreset(Reader reader) {
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
	 * Refills the input buffer.
	 *
	 * @return      <code>true</code> if EOF was reached, otherwise
	 *              <code>false</code>.
	 */
	private boolean zzRefill() {
		return zzCurrentPos>=s.offset+s.count;
	}



  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public VhdlTokenMaker(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public VhdlTokenMaker(java.io.InputStream in) {
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
    while (i < 182) {
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
        case 9:
          { addToken(Token.RESERVED_WORD);
          }
        case 14: break;
        case 4:
          { addToken(Token.IDENTIFIER);
          }
        case 15: break;
        case 12:
          { addToken(Token.FUNCTION);
          }
        case 16: break;
        case 8:
          { addToken(Token.LITERAL_STRING_DOUBLE_QUOTE);
          }
        case 17: break;
        case 7:
          { addToken(Token.WHITESPACE);
          }
        case 18: break;
        case 3:
          { addToken(Token.LITERAL_NUMBER_DECIMAL_INT) ;
          }
        case 19: break;
        case 11:
          { addToken(Token.DATA_TYPE);
          }
        case 20: break;
        case 10:
          { addToken(Token.COMMENT_EOL);
          }
        case 21: break;
        case 1:
          { addToken(Token.ERROR_IDENTIFIER);
          }
        case 22: break;
        case 6:
          { addToken(Token.OPERATOR);
          }
        case 23: break;
        case 13:
          { addToken(Token.LITERAL_NUMBER_DECIMAL_INT);
          }
        case 24: break;
        case 2:
          { addNullToken(); return firstToken;
          }
        case 25: break;
        case 5:
          { addToken(Token.SEPARATOR);
          }
        case 26: break;
        default:
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            switch (zzLexicalState) {
            case YYINITIAL: {
              addNullToken(); return firstToken;
            }
            case 389: break;
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
