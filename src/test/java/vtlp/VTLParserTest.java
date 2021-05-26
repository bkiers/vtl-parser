package vtlp;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static vtlp.matcher.ContainsParserRule.containsParserRule;

public class VTLParserTest {

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
