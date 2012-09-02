RSyntaxTextArea Readme
----------------------
Please contact me if you are using RSyntaxTextArea in your project!  I like
to know when people are finding it useful.  Please send mail to:
robert -at- fifesoft dot com.


* About RSyntaxTextArea

  RSyntaxTextArea is a syntax highlighting text component, written in Swing.
  It allows applications to easily embed syntax highlighting for most common
  programming languages.  Most other standard programmer's editor features are
  built-in: auto-indent, bracket matching, undo/redo, etc.  Auto-completion
  is available as an add-on library (see http://fifesoft.com/autocomplete).

* License

  RSyntaxTextArea is licensed under a modified BSD license.  Please see the
  included RSyntaxTextArea.License.txt file.

* Compiling

  If you wish to compile RSyntaxTextArea from source, the easiest way to do so
  is via the included Ant build script.  The default target builds the jar.
  
    cd RSyntaxTextArea
    ant
  
  Note that RSyntaxTextArea should be built with a 1.4 JDK, to ensure strict
  compatibility when running in a 1.4 JVM.  If you choose to compile with
  Java 5 or later, you will run into the following compiler error:
  
      [javac] ...\XmlParser.java:216: unreported exception java.io.IOException;
              must be caught or declared to be thrown
      [javac] 			return super.resolveEntity(publicId, systemId);
  
  This occurs because Java 5 changed the signature of the resolveEntity()
  method to also throw IOExceptions.  Simply adding IOException to the throws
  clause of the method in XmlParser.java will clear up the problem.
  
  In a future release, RSyntaxTextArea may drop support for Java 1.4, at which
  time the XmlParser class will of course be modified to compile cleanly with
  Java 5 compilers.
  
* Feedback

  I hope you find RSyntaxTextArea useful.  Bug reports, feature requests, and
  just general questions are always welcome.  Ways you can submit feedback:
  
    * http://forum.fifesoft.com (preferred)
         Has a forum for RSyntaxTextArea and related projects, where you can
         ask questions and get feedback quickly.

    * http://sourceforge.net/projects/rsyntaxtextarea
         Has a tracker for bug reports, feature requests, etc.

    * http://fifesoft.com/rsyntaxtextarea
         Project home page, which contains general information and example
         source code.

* Thanks

  Translations:
     
     * Arabic:                 Linostar
     * Chinese:                Terrance, peter_barnes, Sunquan, sonyichi, zvest
     * Chinese (Traditional):  kin Por Fok, liou xiao
     * Dutch:                  Roel, Sebastiaan, lovepuppy
     * French:                 PivWan
     * German:                 bikerpete
     * Hungarian:              flatron
     * Indonesian:             azis, Sonny
     * Italian:                Luca, stepagweb
     * Japanese:               izumi, tomoM
     * Korean:                 Changkyoon sbrownii
     * Polish:                 Chris, Maciej Mrug
     * Portuguese (Brazil):    Pat, Marcos Parmeggiani, Leandro
     * Russian:                Nadiya, Vladimir
     * Spanish:                Leonardo, phrodo, daloporhecho
     * Turkish:                Burak
