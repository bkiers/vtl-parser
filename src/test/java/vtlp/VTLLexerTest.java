package vtlp;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static vtlp.matcher.ProducesTokens.producesTokens;
import static vtlp.VTLLexer.*;

// Test cases from: https://velocity.apache.org/engine/1.7/user-guide.html#variables
public class VTLLexerTest {

  @Test
  @SuppressWarnings("unchecked")
  public void testReferences() {
    Object[][] tests = {
        { "$foo", Arrays.asList(DOLLAR, ID) },
        { "$mudSlinger", Arrays.asList(DOLLAR, ID) },
        { "$mud-slinger", Arrays.asList(DOLLAR, ID) },
        { "$mud_slinger", Arrays.asList(DOLLAR, ID) },
        { "$mudSlinger1", Arrays.asList(DOLLAR, ID) }
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testProperties() {
    Object[][] tests = {
        { "$customer.Address", Arrays.asList(DOLLAR, ID, DOT, ID) },
        { "$purchase.Total", Arrays.asList(DOLLAR, ID, DOT, ID) }
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testMethods() {
    Object[][] tests = {
        { "$customer.getAddress()",
            Arrays.asList(DOLLAR, ID, DOT, ID, OPAR, CPAR) },
        { "$purchase.getTotal(  )",
            Arrays.asList(DOLLAR, ID, DOT, ID, OPAR, CPAR) },
        { "$page.setTitle( \"My Home Page\" )",
            Arrays.asList(DOLLAR, ID, DOT, ID, OPAR, STRING, CPAR) },
        { "$person.setAttributes( [\"Strange\", \"Weird\", \"Excited\"] )",
            Arrays.asList(DOLLAR, ID, DOT, ID, OPAR, OBRACK, STRING, COMMA, STRING, COMMA, STRING, CBRACK, CPAR) },
        { "$sun.getPlanets()",
            Arrays.asList(DOLLAR, ID, DOT, ID, OPAR, CPAR) },
        { "$annelid.getDirt()",
            Arrays.asList(DOLLAR, ID, DOT, ID, OPAR, CPAR) },
        { "$album.getPhoto()",
            Arrays.asList(DOLLAR, ID, DOT, ID, OPAR, CPAR) },
        { "$myarray.set(1, 'test')",
            Arrays.asList(DOLLAR, ID, DOT, ID, OPAR, INTEGER, COMMA, STRING, CPAR) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testIndexNotation() {
    Object[][] tests = {
        { "$foo[0]", Arrays.asList(DOLLAR, ID, OBRACK, INTEGER, CBRACK) },
        { "$foo[$i]", Arrays.asList(DOLLAR, ID, OBRACK, REFERENCE, CBRACK) },
        { "$foo[\"bar\"]", Arrays.asList(DOLLAR, ID, OBRACK, STRING, CBRACK) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testChained() {
    Object[][] tests = {
        { "$foo.bar[1].junk",
            Arrays.asList(DOLLAR, ID, DOT, ID, OBRACK, INTEGER, CBRACK, DOT, ID) },
        { "$foo.callMethod()[1]",
            Arrays.asList(DOLLAR, ID, DOT, ID, OPAR, CPAR, OBRACK, INTEGER, CBRACK) },
        { "$foo[\"apple\"][4]",
            Arrays.asList(DOLLAR, ID, OBRACK, STRING, CBRACK, OBRACK, INTEGER, CBRACK) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testAssignIndexNotation() {
    Object[][] tests = {
        { "#set($foo[0] = 1)",
            Arrays.asList(SET, REFERENCE, OBRACK, INTEGER, CBRACK, ASSIGN, INTEGER, CPAR) },
        { "#set($foo.bar[1] = 3)",
            Arrays.asList(SET, REFERENCE, DOT, ID, OBRACK, INTEGER, CBRACK, ASSIGN, INTEGER, CPAR) },
        { "#set($map[\"apple\"] = \"orange\")",
            Arrays.asList(SET, REFERENCE, OBRACK, STRING, CBRACK, ASSIGN, STRING, CPAR) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFormalNotation() {
    Object[][] tests = {
        { "${mudSlinger}", Arrays.asList(DOLLAR_OBRACE, ID, CBRACE) },
        { "${customer.Address}", Arrays.asList(DOLLAR_OBRACE, ID, DOT, ID, CBRACE) },
        { "${purchase.getTotal()}", Arrays.asList(DOLLAR_OBRACE, ID, DOT, ID, OPAR, CPAR, CBRACE) },
        { "a ${vice}man", Arrays.asList(TEXT, TEXT, DOLLAR_OBRACE, ID, CBRACE, TEXT, TEXT, TEXT) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testQuietNotation() {
    Object[][] tests = {
        { "$!email", Arrays.asList(DOLLAR_EXCL, ID) },
        { "$!{email}", Arrays.asList(DOLLAR_EXCL_OBRACE, ID, CBRACE) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testForeach() {
    Object[][] tests = {
        { "#foreach($item in $foo)#end",
            Arrays.asList(FOREACH, REFERENCE, K_IN, REFERENCE, CPAR, END) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testIf() {
    Object[][] tests = {
        { "#if ($foo)#end", Arrays.asList(IF, REFERENCE, CPAR, END) },
        { "#if ( ! $foo)#end", Arrays.asList(IF, EXCL, REFERENCE, CPAR, END) },
        { "#if ($foo && $foo.bar)#end", Arrays.asList(IF, REFERENCE, AND, REFERENCE, DOT, ID, CPAR, END) },
        { "#if ($foo && $foo == \"bar\")#end", Arrays.asList(IF, REFERENCE, AND, REFERENCE, EQ, STRING, CPAR, END) },
        { "#{if} ($foo1 || $foo2)#{end}", Arrays.asList(IF, REFERENCE, OR, REFERENCE, CPAR, END) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testCodeKeywords() {
    Object[][] tests = {
        { "#set( { 'k': { 'k2': 123 } } )", Arrays.asList(SET, OBRACE, STRING, COLON, OBRACE, STRING, COLON, INTEGER, CBRACE, CBRACE, CPAR) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testMap() {
    Object[][] tests = {
        { "#if(null in)#end", Arrays.asList(IF, K_NULL, K_IN, CPAR, END) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testNestedExpressions() {
    Object[][] tests = {
        { "#if(((1+2)/3)==1)#end", Arrays.asList(IF, OPAR, OPAR, INTEGER, ADD, INTEGER, CPAR, DIV, INTEGER, CPAR, EQ, INTEGER, CPAR, END) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testDots() {
    Object[][] tests = {
        { "#set(123 1.2 .3 4. .. 9..10)", Arrays.asList(SET, INTEGER, FLOAT, FLOAT, FLOAT, RANGE, INTEGER, RANGE, INTEGER, CPAR) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testImmediateIf() {
    Object[][] tests = {
        { "$a.b#if(true)#end", Arrays.asList(DOLLAR, ID, DOT, ID, IF, K_TRUE, CPAR, END) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testImmediateComment() {
    Object[][] tests = {
        { "$a#* should be a comment *#", Arrays.asList(DOLLAR, ID) },
        { "$a#** should be a comment *#", Arrays.asList(DOLLAR, ID, VTL_COMMENT_BLOCK) },
        { "$a## should be a comment", Arrays.asList(DOLLAR, ID) },
        { "$a#[[ should be a single token ]]#", Arrays.asList(DOLLAR, ID, ESCAPED_BLOCK) },
    };

    for (Object[] testCase : tests) {
      assertThat((String)testCase[0], producesTokens((List<Integer>)testCase[1]));
    }
  }
}
