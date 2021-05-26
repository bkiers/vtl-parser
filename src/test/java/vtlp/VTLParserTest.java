package vtlp;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static vtlp.matcher.ContainsParserRule.containsParserRule;

public class VTLParserTest {

  @Test
  public void testTemplates() throws IOException {
    File[] files = new File("./src/main/templates").listFiles();

    for (File file : files) {
      try {
        VTLLexer lexer = new VTLLexer(CharStreams.fromFileName(file.getAbsolutePath()));
        VTLParser parser = new VTLParser(new CommonTokenStream(lexer));

        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());

        parser.parse();
      }
      catch (Exception e) {
        fail("Could not parse " + file.getName() + ": " + e.getMessage());
      }
    }
  }

  @Test
  public void testVariable() {
    String[] tests = {
        "... ${a}$foo ...",
        "... ${a}$mudSlinger ...",
        "... ${a}$mud-slinger ...",
        "... ${a}$mud_slinger ...",
        "... ${a}$mudSlinger1 ...",
    };

    for (String test : tests) {
      assertThat(test, containsParserRule("variable"));
    }
  }

  @Test
  public void testFormal() {
    String[] tests = {
        "... $!{a} ...",
        "... $normal${a} ...",
        "... $normal${a.b} ...",
        "... $normal${a.b.cccc['mu']} ...",
        "... $normal${a.b.cccc['mu'].method(1, 2, 3)} ...",
    };

    for (String test : tests) {
      assertThat(test, containsParserRule("formal"));
    }
  }

  @Test
  public void testIfDirective() {
    String[] tests = {
        "... #if (1 == 2) text #end ...",
        "... $VAR #if (1 == 2) text #end ...",
        "... $VAR#if (1 == 2) text #end ...",
    };

    for (String test : tests) {
      assertThat(test, containsParserRule("if_directive"));
    }
  }
}
