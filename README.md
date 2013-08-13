RSyntaxTextArea is a customizable, syntax highlighting text component for Java Swing applications.  Out of the box, it supports syntax highlighting for over 30 programming languages, code folding, search and replace, and has add-on libraries for code completion and spell checking.  Syntax highlighting for additional languages [can be added](http://fifesoft.com/rsyntaxtextarea/doc/) via tools such as [JFlex](http://jflex.de).

RSyntaxTextArea is available under a [modified BSD license](https://github.com/bobbylight/RSyntaxTextArea/blob/master/distfiles/RSyntaxTextArea.License.txt).  For more information, visit [http://fifesoft.com/rsyntaxtextarea](http://fifesoft.com/rsyntaxtextarea).

# Example Usage

RSyntaxTextArea is simply a subclass of JTextComponent, so it can be dropped into any Swing application with ease.

```java
import java.awt.*;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

public class TextEditorDemo extends JFrame {

   public TextEditorDemo() {

      JPanel cp = new JPanel(new BorderLayout());

      RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
      textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
      textArea.setCodeFoldingEnabled(true);
      textArea.setAntiAliasingEnabled(true);
      RTextScrollPane sp = new RTextScrollPane(textArea);
      sp.setFoldIndicatorEnabled(true);
      cp.add(sp);

      setContentPane(cp);
      setTitle("Text Editor Demo");
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      pack();
      setLocationRelativeTo(null);

   }

   public static void main(String[] args) {
      // Start all Swing applications on the EDT.
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            new TextEditorDemo().setVisible(true);
         }
      });
   }

}
```
# Sister Projects

RSyntaxTextArea provides syntax highlighting, code folding, and many other features out-of-the-box, but when building a code editor you often want to go further.  Below is a list of small add-on libraries that add more complex functionality:

* [AutoComplete](https://github.com/bobbylight/AutoComplete) - Adds code completion to RSyntaxTextArea (or any other JTextComponent).
* [RSTALanguageSupport](https://github.com/bobbylight/RSTALanguageSupport) - Code completion for RSTA for the following languages: Java, JavaScript, HTML, PHP, JSP, Perl, C, Unix Shell.  Built on both RSTA and AutoComplete.
* [SpellChecker](https://github.com/bobbylight/SpellChecker) - Adds squiggle-underline spell checking to RSyntaxTextArea.
* [RSTAUI](https://github.com/bobbylight/RSTAUI) - Common dialogs needed by text editing applications: Find, Replace, Go to Line, File Properties.

# Getting Help

* Add an issue on GitHub
* Ask in the [project forum](http://fifesoft.com/forum/)
* Check the project's [home page](http://fifesoft.com/rsyntaxtextarea)

