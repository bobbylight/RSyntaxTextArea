package org.fife.ui.rsyntaxtextarea.folding;

import java.util.HashMap;
import java.util.Map;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;


public class FoldParserManager implements SyntaxConstants {

	/**
	 * Map from syntax styles to fold parsers.
	 */
	private Map foldParserMap;

	private static final FoldParserManager INSTANCE = new FoldParserManager();


	/**
	 * Private constructor to prevent instantiation.
	 */
	private FoldParserManager() {
		foldParserMap = createFoldParserMap();
	}


	private Map createFoldParserMap() {

		Map map = new HashMap();
		CFoldParserSupplier cfps = new CFoldParserSupplier();

		map.put(SYNTAX_STYLE_C,					cfps);
		map.put(SYNTAX_STYLE_CPLUSPLUS,			cfps);
		map.put(SYNTAX_STYLE_CSHARP,			cfps);
		map.put(SYNTAX_STYLE_JAVA,				cfps);
		map.put(SYNTAX_STYLE_PERL,				cfps);

		return map;

	}


	/**
	 * Returns the singleton instance of this class.
	 *
	 * @return The singleton instance.
	 */
	public static FoldParserManager get() {
		return INSTANCE;
	}


	/**
	 * Returns a fold parser to use for an editor highlighting code of a
	 * specific language.
	 *
	 * @param syntaxStyle A value from {@link SyntaxConstants}, such as
	 *        <code>SYNTAX_STYLE_JAVA</code>.
	 * @return A fold parser to use, or <code>null</code> if none is registered
	 *         for the language.
	 */
	public FoldParser getFoldParser(String syntaxStyle) {
		FoldParserSupplier supplier = (FoldParserSupplier)foldParserMap.
														get(syntaxStyle);
		if (supplier!=null) {
			return supplier.getFoldParser();
		}
		return null;
	}


	public interface FoldParserSupplier {

		public FoldParser getFoldParser();

	}


	/**
	 * Supplies a shared instance of {@link CurlyFoldParser}, lazily created.
	 */
	public static class CFoldParserSupplier implements FoldParserSupplier {

		private CurlyFoldParser parser;

		public FoldParser getFoldParser() {
			if (parser==null) {
				parser = new CurlyFoldParser(true);
			}
			return parser;
		}

	}


}