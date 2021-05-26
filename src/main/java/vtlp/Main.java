package vtlp;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {
  public static void main(String[] args) {
    VTLLexer lexer = new VTLLexer(CharStreams.fromString("abc"));
    VTLParser parser = new VTLParser(new CommonTokenStream(lexer));
    System.out.println(parser.parse().toStringTree(parser));
  }
}