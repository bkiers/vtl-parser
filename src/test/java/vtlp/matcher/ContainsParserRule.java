package vtlp.matcher;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import vtlp.VTLLexer;
import vtlp.VTLParser;

public class ContainsParserRule extends TypeSafeMatcher<String> {

  private final String parserRule;
  private String source = null;
  private String errorMessage = null;
  private String parseTree = null;

  public ContainsParserRule(String parserRule) {
    this.parserRule = parserRule;
  }

  @Override
  protected boolean matchesSafely(String source) {
    this.source = source;

    try {
      VTLLexer lexer = new VTLLexer(CharStreams.fromString(source));
      VTLParser parser = new VTLParser(new CommonTokenStream(lexer));

      parser.removeErrorListeners();
      parser.setErrorHandler(new BailErrorStrategy());

      ParseTree root = parser.parse();

      this.parseTree = root.toStringTree(parser);

      return this.parseTree.matches("^(?s).*?\\(" + this.parserRule + " .*$");
    }
    catch (Exception e) {
      Throwable rootCause = e;

      while (true) {
        Throwable temp = rootCause.getCause();
        if (temp == null) {
          break;
        }
        rootCause = temp;
      }

      this.errorMessage = rootCause.getMessage();
    }

    return false;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("the parse tree to contain `")
        .appendText(this.parserRule)
        .appendText("` for source: ")
        .appendText(this.source);
  }

  @Override
  protected void describeMismatchSafely(String item, Description description) {
    description.appendText("`")
        .appendText(this.parserRule)
        .appendText("` was not found: ")
        .appendText(this.errorMessage == null ? this.parseTree : this.errorMessage);
  }

  public static ContainsParserRule containsParserRule(String parserRule) {
    return new ContainsParserRule(parserRule);
  }
}
