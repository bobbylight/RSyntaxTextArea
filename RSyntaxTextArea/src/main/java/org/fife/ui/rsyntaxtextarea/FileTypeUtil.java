/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rsyntaxtextarea;

import javax.swing.text.BadLocationException;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * Utility methods that help you determine what type of code is in a
 * file, to determine how to syntax highlight it.  Methods are provided
 * to both analyze the file name and the actual file content.<p>
 *
 * Typically, you'll want to inspect the file name before loading the
 * file into an {@code RSyntaxTextArea} or {@code TextEditorPane}
 * instance for best performance.  Here's an example of how to do so:
 *
 * <pre>
 * File file = getFileToOpen();
 * // Open the file for editing
 * TextEditorPane textArea = new TextEditorPane();
 * textArea.load(FileLocation.create(file));
 * // Guess the type of code to use for syntax highlighting
 * String style = FileTypeUtil.get().guessContentType(file);
 * textArea.setSyntaxEditingStyle(style);
 * </pre>
 *
 * Sometimes you won't be able to identify the type of code in a file
 * or stream; for example, if there is no extension on a shell script,
 * or if you're displaying output read from a stream (say HTML or XML)
 * instead of a flat file.  In such a case, you can try to guess the
 * content's file type as follows:
 *
 * <pre>
 * File file = getFileToOpen();
 * // Open the file for editing
 * TextEditorPane textArea = new TextEditorPane();
 * textArea.load(FileLocation.create(file));
 * // Guess the type of code to use for syntax highlighting
 * String style = FileTypeUtil.get().guessContentType(file);
 * if (style == null) {
 *     style = FileTypeUtil.get().guessContentType(textArea);
 * }
 * textArea.setSyntaxEditingStyle(style);
 * </pre>
 *
 * This logic primarily looks at the first line of the content and
 * attempts to identify the following:
 *
 * <ul>
 *   <li>A shebang, and if so, what file type is being interpreted</li>
 *   <li>Whether there's an XML processing instruction</li>
 *   <li>Whether there's an HTML doctype tag</li>
 * </ul>
 *
 * This logic is in a separate method from that which checks the file name
 * primarily for performance.  Rather than open the file twice (once to
 * determine the content type, and again to read into the text area), it's
 * better to simply read the content into the text area as
 * {@code SyntaxConstants#SYNTAX_STYLE_NONE}, then guess the content
 * type as shown above.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public final class FileTypeUtil implements SyntaxConstants {

	private Map<String, List<String>> map;

	private static final boolean DEFAULT_IGNORE_BACKUP_EXTENSIONS = true;

	private static final FileTypeUtil INSTANCE = new FileTypeUtil();


	private FileTypeUtil() {
		initializeFilters();
	}


	/**
	 * Returns the singleton instance of this class.
	 *
	 * @return The singleton instance of this class.
	 */
	public static FileTypeUtil get() {
		return INSTANCE;
	}


	/**
	 * Converts a {@code String} representing a wildcard file filter into
	 * a <code>Pattern</code> containing a regular expression good for
	 * finding files that match the wildcard expression.<p>
	 * <p>
	 * Example: For<p>
	 * <code>String regEx = FileTypeUtil.get().fileFilterToPattern("*.c");
	 * </code><p>
	 * the returned pattern will match <code>^.*\.c$</code>.<p>
	 * <p>
	 * Case-sensitivity is taken into account appropriately.
	 *
	 * @param fileFilter The file filter for which to create equivalent regular
	 *        expressions.  This filter can currently only contain the
	 *        wildcards {@code '*'} and {@code '?'}.
	 * @return A <code>Pattern</code> representing an equivalent regular
	 *         expression for the string passed in.
	 * @throws PatternSyntaxException If the file filter could not be parsed.
	 */
	public static Pattern fileFilterToPattern(String fileFilter) {
		String pattern = fileFilterToPatternImpl(fileFilter);
		int flags = RSyntaxUtilities.isOsCaseSensitive() ? 0 : (Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		return Pattern.compile(pattern, flags);
	}


	private static String fileFilterToPatternImpl(String filter) {

		StringBuilder sb = new StringBuilder("^");

		for (int i = 0; i < filter.length(); i++) {
			char ch = filter.charAt(i);
			switch (ch) {
				case '.':
					sb.append("\\.");
					break;
				case '*':
					sb.append(".*");
					break;
				case '?':
					sb.append('.');
					break;
				case '$':
					sb.append("\\$");
					break;
				default:
					sb.append(ch);
					break;
			}
		}

		return sb.append('$').toString();
	}


	/**
	 * Returns the mapping of content types to lists of extensions used
	 * by this class by default.
	 *
	 * @return The mapping.
	 */
	public Map<String, List<String>> getDefaultContentTypeToFilterMap() {

		// Deep copy
		Map<String, List<String>> result = new HashMap<>();

		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			result.put(entry.getKey(), new ArrayList<>(entry.getValue()));
		}
		return result;
	}


	/**
	 * Sets the text area's highlighting style based on its content (e.g.
	 * whether it contains "<code>#!</code>" at the top).
	 *
	 * @param textArea The text area to examine.
	 * @return The guessed content type.  This may be
	 *         {@code SyntaxConstants.SYNTAX_STYLE_NONE} if nothing can be
	 *         determined, but will never be {@code null}.
	 * @see SyntaxConstants
	 * @see #guessContentType(File)
	 * @see #guessContentType(File, boolean)
	 */
	public String guessContentType(RSyntaxTextArea textArea) {

		String style = SyntaxConstants.SYNTAX_STYLE_NONE;

		String firstLine;
		try {
			int endOffs = textArea.getLineEndOffset(0);
			if (textArea.getLineCount() > 1) {
				endOffs--;
			}
			firstLine = textArea.getText(0, endOffs);
		} catch (BadLocationException ble) { // Never happens
			ble.printStackTrace();
			return SyntaxConstants.SYNTAX_STYLE_NONE;
		}

		if (firstLine.startsWith("#!")) {
			style = guessContentTypeFromShebang(firstLine);
		}

		else if (firstLine.startsWith("<?xml") && firstLine.endsWith("?>")) {
			style = SyntaxConstants.SYNTAX_STYLE_XML;
		}

		else if (firstLine.startsWith("<!doctype html")) {
			style = SyntaxConstants.SYNTAX_STYLE_HTML;
		}

		return style;
	}


	/**
	 * Guesses the type of content in a file, based on its name.
	 * Backup extensions will be ignored.
	 *
	 * @param file The file, which may be {@code null}.
	 * @return The guessed file type. This may be
	 *         {@code SyntaxConstants.SYNTAX_STYLE_NONE} if nothing can be
	 *         determined, but will never be {@code null}.
	 * @see SyntaxConstants
	 * @see #guessContentType(File, boolean)
	 * @see #guessContentType(RSyntaxTextArea)
	 */
	public String guessContentType(File file) {
		return guessContentType(file, null);
	}


	/**
	 * Guesses the type of content in a file, based on its name.
	 * Backup extensions will be ignored.<p>
	 * <p>
	 * Note you'll typically only need to call this overload if your application
	 * implements syntax highlighting for additional/custom languages, or
	 * supports syntax highlighting files with an extension the default
	 * implementation doesn't know about.
	 *
	 * @param file The file, which may be {@code null}.
	 * @param filters The map of {@code SyntaxConstants} values to lists of
	 *        wildcard filters.  If this is {@code null}, a default set of
	 *        filters is used.
	 * @return The guessed file type. This may be
	 *         {@code SyntaxConstants.SYNTAX_STYLE_NONE} if nothing can be
	 *         determined, but will never be {@code null}.
	 * @see SyntaxConstants
	 * @see #guessContentType(File, boolean)
	 * @see #guessContentType(RSyntaxTextArea)
	 */
	public String guessContentType(File file, Map<String, List<String>> filters) {
		return guessContentType(file, filters, DEFAULT_IGNORE_BACKUP_EXTENSIONS);
	}

	/**
	 * Guesses the type of content in a file, based on its name.
	 *
	 * @param file The file, which may be {@code null}.
	 * @param ignoreBackupExtensions Whether to ignore backup extensions.
	 * @return The guessed file type. This may be
	 *         {@code SyntaxConstants.SYNTAX_STYLE_NONE} if nothing can be
	 *         determined, but will never be {@code null}.
	 * @see SyntaxConstants
	 * @see #guessContentType(File)
	 * @see #guessContentType(RSyntaxTextArea)
	 */
	public String guessContentType(File file, boolean ignoreBackupExtensions) {
		return guessContentType(file, null, ignoreBackupExtensions);
	}


	/**
	 * Guesses the type of content in a file, based on its name.<p>
	 * <p>
	 * Note you'll typically only need to call this overload if your application
	 * implements syntax highlighting for additional/custom languages, or
	 * supports syntax highlighting files with an extension the default
	 * implementation doesn't know about.
	 *
	 * @param file The file, which may be {@code null}.
	 * @param filters The map of {@code SyntaxConstants} values to lists of
	 *        wildcard filters.  If this is {@code null}, a default set of
	 *        filters is used.
	 * @param ignoreBackupExtensions Whether to ignore backup extensions.
	 * @return The guessed file type. This may be
	 *         {@code SyntaxConstants.SYNTAX_STYLE_NONE} if nothing can be
	 *         determined, but will never be {@code null}.
	 * @see SyntaxConstants
	 * @see #guessContentType(File)
	 * @see #guessContentType(RSyntaxTextArea)
	 */
	public String guessContentType(File file, Map<String, List<String>> filters, boolean ignoreBackupExtensions) {

		if (file == null) {
			return SyntaxConstants.SYNTAX_STYLE_NONE;
		}
		String fileName = file.getName();

		if (filters == null) {
			filters = map;
		}

		fileName = fileName.toLowerCase(); // Ignore casing of extensions

		// Ignore extensions that mean "this is a backup", but don't
		// denote the actual file type.
		if (ignoreBackupExtensions) {
			fileName = stripBackupExtensions(fileName);
		}

		String style = guessContentTypeFromFileName(fileName, filters);

		return style != null ? style : SyntaxConstants.SYNTAX_STYLE_NONE;
	}


	/**
	 * Looks for a syntax style for a file name in a given map.
	 *
	 * @param fileName The file name, possibly with a backup extension removed.
	 * @param filters  The map of {@code SyntaxConstants} values to lists of
	 *                 wildcard filters.
	 * @return The syntax style for the file, or {@code null} if nothing could
	 *         be determined.
	 */
	private static String guessContentTypeFromFileName(String fileName, Map<String, List<String>> filters) {

		String syntaxStyle = null;

		// Go by pattern matching (mostly by extension)
		for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
			for (String filter : entry.getValue()) {
				Pattern p = fileFilterToPattern(filter);
				if (p.matcher(fileName).matches()) {
					syntaxStyle = entry.getKey();
					// Stop immediately if we find a non-wildcard match,
					// as it'll be exact, e.g. "makefile"
					if (!filter.contains("*") && !filter.contains("?")) {
						break;
					}
				}
			}
		}

		return syntaxStyle;
	}


	/**
	 * Looks at a shebang line to try to divine the syntax style.
	 *
	 * @param firstLine The shebang line, e.g. {@code #!/bin/sh}.
	 * @return The syntax style to use.
	 */
	private static String guessContentTypeFromShebang(String firstLine) {

		String style = SyntaxConstants.SYNTAX_STYLE_NONE;

		// Take special care for the case of "#!/usr/bin/env programName"
		int space = firstLine.indexOf(' ', 2); // Skip the #!
		if (space > -1) {
			if (firstLine.startsWith("#!/usr/bin/env")) {
				int space2 = firstLine.indexOf(' ', space + 1);
				if (space2 == -1) { // No args, just program name
					space2 = firstLine.length();
				}
				firstLine = firstLine.substring(space + 1, space2);
			}
			else {
				firstLine = firstLine.substring(2, space);
			}
		}

		if (firstLine.endsWith("sh")) { // ksh, bash, sh, ...
			style = SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL;
		}
		else if (firstLine.endsWith("perl")) {
			style = SyntaxConstants.SYNTAX_STYLE_PERL;
		}
		else if (firstLine.endsWith("php")) {
			style = SyntaxConstants.SYNTAX_STYLE_PHP;
		}
		else if (firstLine.endsWith("python")) {
			style = SyntaxConstants.SYNTAX_STYLE_PYTHON;
		}
		else if (firstLine.endsWith("lua")) {
			style = SyntaxConstants.SYNTAX_STYLE_LUA;
		}
		else if (firstLine.endsWith("ruby")) {
			style = SyntaxConstants.SYNTAX_STYLE_RUBY;
		}

		return style;
	}


	private static void initFiltersImpl(Map<String, List<String>> map, String syntax, String... filters) {
		map.put(syntax, Arrays.asList(filters));
	}


	private void initializeFilters() {

		map = new HashMap<>();

		initFiltersImpl(map, SYNTAX_STYLE_ACTIONSCRIPT, "*.as", "*.asc");
		initFiltersImpl(map, SYNTAX_STYLE_ASSEMBLER_6502, "*.s");
		initFiltersImpl(map, SYNTAX_STYLE_ASSEMBLER_X86, "*.asm");
		initFiltersImpl(map, SYNTAX_STYLE_BBCODE, "*.bbc");
		initFiltersImpl(map, SYNTAX_STYLE_C, "*.c");
		initFiltersImpl(map, SYNTAX_STYLE_CLOJURE, "*.clj");
		initFiltersImpl(map, SYNTAX_STYLE_CPLUSPLUS, "*.cpp", "*.cxx", "*.h", "*.hpp");
		initFiltersImpl(map, SYNTAX_STYLE_CSHARP, "*.cs");
		initFiltersImpl(map, SYNTAX_STYLE_CSS, "*.css");
		initFiltersImpl(map, SYNTAX_STYLE_CSV, "*.csv");
		initFiltersImpl(map, SYNTAX_STYLE_D, "*.d");
		initFiltersImpl(map, SYNTAX_STYLE_DART, "*.dart");
		initFiltersImpl(map, SYNTAX_STYLE_DELPHI, "*.pas");
		initFiltersImpl(map, SYNTAX_STYLE_DOCKERFILE, "*.dockerfile", "Dockerfile");
		initFiltersImpl(map, SYNTAX_STYLE_DTD, "*.dtd");
		initFiltersImpl(map, SYNTAX_STYLE_FORTRAN, "*.f", "*.for", "*.fort", "*.f77", "*.f90");
		initFiltersImpl(map, SYNTAX_STYLE_GO, "*.go");
		initFiltersImpl(map, SYNTAX_STYLE_GROOVY, "*.groovy", "*.gradle", "*.grv", "*.gy", "*.gvy", "*.gsh");
		initFiltersImpl(map, SYNTAX_STYLE_HANDLEBARS, "*.hbs", "*.handlebars");
		initFiltersImpl(map, SYNTAX_STYLE_HOSTS, "hosts");
		initFiltersImpl(map, SYNTAX_STYLE_HTACCESS, ".htaccess");
		initFiltersImpl(map, SYNTAX_STYLE_HTML, "*.htm", "*.html");
		initFiltersImpl(map, SYNTAX_STYLE_INI, "*.ini", ".editorconfig");
		initFiltersImpl(map, SYNTAX_STYLE_JAVA, "*.java");
		initFiltersImpl(map, SYNTAX_STYLE_JAVASCRIPT, "*.js", "*.jsx");
		initFiltersImpl(map, SYNTAX_STYLE_JSON_WITH_COMMENTS, ".jshintrc", "tslint.json");
		initFiltersImpl(map, SYNTAX_STYLE_JSON, "*.json");
		initFiltersImpl(map, SYNTAX_STYLE_JSP, "*.jsp");
		initFiltersImpl(map, SYNTAX_STYLE_KOTLIN, "*.kt", "*.kts");
		initFiltersImpl(map, SYNTAX_STYLE_LATEX, "*.tex", "*.ltx", "*.latex");
		initFiltersImpl(map, SYNTAX_STYLE_LESS, "*.less");
		initFiltersImpl(map, SYNTAX_STYLE_LISP, "*.cl", "*.clisp", "*.el", "*.l", "*.lisp", "*.lsp", "*.ml");
		initFiltersImpl(map, SYNTAX_STYLE_LUA, "*.lua");
		initFiltersImpl(map, SYNTAX_STYLE_MAKEFILE, "Makefile", "makefile");
		initFiltersImpl(map, SYNTAX_STYLE_MARKDOWN, "*.md");
		initFiltersImpl(map, SYNTAX_STYLE_MXML, "*.mxml");
		initFiltersImpl(map, SYNTAX_STYLE_NSIS, "*.nsi");
		initFiltersImpl(map, SYNTAX_STYLE_PERL, "*.perl", "*.pl", "*.pm");
		initFiltersImpl(map, SYNTAX_STYLE_PHP, "*.php");
		initFiltersImpl(map, SYNTAX_STYLE_PROPERTIES_FILE, "*.properties");
		initFiltersImpl(map, SYNTAX_STYLE_PROTO, "*.proto");
		initFiltersImpl(map, SYNTAX_STYLE_PYTHON, "*.py");
		initFiltersImpl(map, SYNTAX_STYLE_RUBY, "*.rb", "Vagrantfile");
		initFiltersImpl(map, SYNTAX_STYLE_RUST, "*.rs");
		initFiltersImpl(map, SYNTAX_STYLE_SAS, "*.sas");
		initFiltersImpl(map, SYNTAX_STYLE_SCALA, "*.scala");
		initFiltersImpl(map, SYNTAX_STYLE_SQL, "*.sql");
		initFiltersImpl(map, SYNTAX_STYLE_TCL, "*.tcl", "*.tk");
		initFiltersImpl(map, SYNTAX_STYLE_TYPESCRIPT, "*.ts", "*.tsx");
		initFiltersImpl(map, SYNTAX_STYLE_UNIX_SHELL, "*.sh", "*.?sh");
		initFiltersImpl(map, SYNTAX_STYLE_VISUAL_BASIC, "*.vb");
		initFiltersImpl(map, SYNTAX_STYLE_WINDOWS_BATCH, "*.bat", "*.cmd");
		initFiltersImpl(map, SYNTAX_STYLE_XML, "*.xml", "*.xsl", "*.xsd", "*.xslt", "*.wsdl", "*.svg",
			"*.tmx", "*.pom", "*.manifest");
		initFiltersImpl(map, SYNTAX_STYLE_YAML, "*.yml", "*.yaml");
	}


	/**
	 * Strips the following extensions from the end of a file name, if they are
	 * there:
	 * <ul>
	 *     <li>{@code .orig}</li>
	 *     <li>{@code .bak}</li>
	 *     <li>{@code .old}</li>
	 * </ul>
	 * The idea is that these are typically backup files, and when the
	 * extension can be used to deduce a file's type/content, that
	 * extension should be ignored.
	 *
	 * @param fileName The file name.  This may be {@code null}.
	 * @return The same file name, with any of the above extensions removed.
	 */
	public static String stripBackupExtensions(String fileName) {
		if (fileName != null) {
			if (fileName.endsWith(".bak") || fileName.endsWith(".old")) {
				fileName = fileName.substring(0, fileName.length() - 4);
			}
			else if (fileName.endsWith(".orig")) {
				fileName = fileName.substring(0, fileName.length() - 5);
			}
		}
		return fileName;
	}
}
