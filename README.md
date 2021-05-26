# VTLParser

A Velocity Template Language 2.0 parser backed by an ANTLR4 grammar, although 
it should also be able to parse earlier versions of the Velocity Template 
Language.

Firs generate the lexer- and parser classes:

```bash
mvn clean build
```

Then use the parser:

```java
String fileName = "/path/to/test.vm";

VTLLexer lexer = new VTLLexer(CharStreams.fromFileName(fileName));
VTLParser parser = new VTLParser(new CommonTokenStream(lexer));

ParseTree root = parser.parse();

...
```

and/or look at the [unit tests](https://github.com/bkiers/vtl-parser/tree/master/src/test/java/vtlp) 
to see how it can be used.