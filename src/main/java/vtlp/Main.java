package vtlp;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;

public class Main {

  private static void render(String fileName) {
    VelocityEngine ve = new VelocityEngine();
    ve.init();

    Template t = ve.getTemplate("src/main/templates/" + fileName);

    VelocityContext context = new VelocityContext();
    context.put("name", "World");

    StringWriter writer = new StringWriter();
    t.merge(context, writer);

    System.out.println(writer);
  }

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

  public static void main(String[] args) {

//    render("test.vm");

    String source = "$!foo";
    dump(source);

//    VTLLexer lexer = new VTLLexer(CharStreams.fromString("abc"));
//    VTLParser parser = new VTLParser(new CommonTokenStream(lexer));
//    System.out.println(parser.parse().toStringTree(parser));
  }
}