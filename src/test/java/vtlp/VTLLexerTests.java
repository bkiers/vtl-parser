package vtlp;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static vtlp.VTLLexer.*;

public class VTLLexerTests {

  private static void dump(String source) {
    VTLLexer lexer = new VTLLexer(CharStreams.fromString(source));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    tokens.fill();

    for (Token t : tokens.getTokens()) {
      System.out.printf("%-20s '%s'\n",
          VTLLexer.VOCABULARY.getSymbolicName(t.getType()),
          t.getText());
    }
  }

  private static List<Token> tokens(String source) {
    VTLLexer lexer = new VTLLexer(CharStreams.fromString(source));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    tokens.fill();

    // Discard EOF
    return tokens.getTokens().subList(0, tokens.size() - 1);
  }

  private static List<Integer> tokenTypes(String source) {
    VTLLexer lexer = new VTLLexer(CharStreams.fromString(source));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    tokens.fill();

    return tokens.getTokens().stream().map(Token::getType).collect(Collectors.toList());
  }

//  @Test
//  public void dump() {
//    String source = "#set($foo.bar[1] = 3)";
//    dump(source);
//  }

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
      String source = (String)testCase[0];
      List<Token> tokens = tokens(source);
      List<Integer> types = tokens.stream().map(Token::getType).collect(Collectors.toList());
      List<Integer> expectedTypes = (List<Integer>)testCase[1];

      String message = String.format("Failed: %s\nExpected : %s\nGot      : %s",
          source,
          expectedTypes.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()),
          types.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()));

      assertThat(message, types, equalTo(expectedTypes));
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
      String source = (String)testCase[0];
      List<Token> tokens = tokens(source);
      List<Integer> types = tokens.stream().map(Token::getType).collect(Collectors.toList());
      List<Integer> expectedTypes = (List<Integer>)testCase[1];

      String message = String.format("Failed: %s\nExpected : %s\nGot      : %s",
          source,
          expectedTypes.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()),
          types.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()));

      assertThat(message, types, equalTo(expectedTypes));
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
      String source = (String)testCase[0];
      List<Token> tokens = tokens(source);
      List<Integer> types = tokens.stream().map(Token::getType).collect(Collectors.toList());
      List<Integer> expectedTypes = (List<Integer>)testCase[1];

      String message = String.format("Failed: %s\nExpected : %s\nGot      : %s",
          source,
          expectedTypes.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()),
          types.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()));

      assertThat(message, types, equalTo(expectedTypes));
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
      String source = (String)testCase[0];
      List<Token> tokens = tokens(source);
      List<Integer> types = tokens.stream().map(Token::getType).collect(Collectors.toList());
      List<Integer> expectedTypes = (List<Integer>)testCase[1];

      String message = String.format("Failed: %s\nExpected : %s\nGot      : %s",
          source,
          expectedTypes.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()),
          types.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()));

      assertThat(message, types, equalTo(expectedTypes));
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
      String source = (String)testCase[0];
      List<Token> tokens = tokens(source);
      List<Integer> types = tokens.stream().map(Token::getType).collect(Collectors.toList());
      List<Integer> expectedTypes = (List<Integer>)testCase[1];

      String message = String.format("Failed: %s\nExpected : %s\nGot      : %s",
          source,
          expectedTypes.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()),
          types.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()));

      assertThat(message, types, equalTo(expectedTypes));
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
      String source = (String)testCase[0];
      List<Token> tokens = tokens(source);
      List<Integer> types = tokens.stream().map(Token::getType).collect(Collectors.toList());
      List<Integer> expectedTypes = (List<Integer>)testCase[1];

      String message = String.format("Failed: %s\nExpected : %s\nGot      : %s",
          source,
          expectedTypes.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()),
          types.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()));

      assertThat(message, types, equalTo(expectedTypes));
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
      String source = (String)testCase[0];
      List<Token> tokens = tokens(source);
      List<Integer> types = tokens.stream().map(Token::getType).collect(Collectors.toList());
      List<Integer> expectedTypes = (List<Integer>)testCase[1];

      String message = String.format("Failed: %s\nExpected : %s\nGot      : %s",
          source,
          expectedTypes.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()),
          types.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()));

      assertThat(message, types, equalTo(expectedTypes));
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
      String source = (String)testCase[0];
      List<Token> tokens = tokens(source);
      List<Integer> types = tokens.stream().map(Token::getType).collect(Collectors.toList());
      List<Integer> expectedTypes = (List<Integer>)testCase[1];

      String message = String.format("Failed: %s\nExpected : %s\nGot      : %s",
          source,
          expectedTypes.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()),
          types.stream().map(VOCABULARY::getSymbolicName).collect(Collectors.toList()));

      assertThat(message, types, equalTo(expectedTypes));
    }
  }

  // TODO: https://velocity.apache.org/engine/1.7/user-guide.html#variables
}
