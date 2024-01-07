package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.directives.DefineDirective;
import cz.mg.c.preprocessor.processors.macro.exceptions.MacroException;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.*;

public @Service class MacroParser {
    private static volatile @Service MacroParser instance;

    public static @Service MacroParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroParser();
                }
            }
        }
        return instance;
    }

    private MacroParser() {
    }

    public @Mandatory Macro parse(@Mandatory List<Token> line) {
        TokenReader reader = new TokenReader(line, MacroException::new);
        reader.skip(WhitespaceToken.class);
        reader.read("#", SpecialToken.class);
        reader.skip(WhitespaceToken.class);
        reader.read(DefineDirective.KEYWORD, WordToken.class);
        reader.skip(WhitespaceToken.class);
        return new Macro(
            readName(reader),
            readParameters(reader),
            readTokens(reader)
        );
    }

    private @Mandatory WordToken readName(@Mandatory TokenReader reader) {
        return reader.read(WordToken.class);
    }

    private @Optional List<Token> readParameters(@Mandatory TokenReader reader) {
        if (reader.has("(", BracketToken.class)) {
            List<Token> parameters = new List<>();
            int startPosition = reader.read().getPosition();
            boolean expectedName = true;
            while (true) {
                reader.skip(WhitespaceToken.class);
                if (reader.has(")", BracketToken.class)) {
                    int endPosition = reader.read().getPosition();
                    if (expectedName && !parameters.isEmpty()) {
                        throw new MacroException(
                            endPosition,
                            "Missing parameter for macro parameter list."
                        );
                    }
                    break;
                } else if (reader.has()) {
                    if (expectedName) {
                        if (reader.has("...", OperatorToken.class)) {
                            Token varargs = reader.read();
                            parameters.addLast(new WordToken("", varargs.getPosition()));
                            parameters.addLast(varargs);
                        } else {
                            parameters.addLast(reader.read(WordToken.class));
                            if (reader.has("...", OperatorToken.class)) {
                                parameters.addLast(reader.read());
                            }
                        }
                    } else {
                        reader.read(",", SeparatorToken.class);
                    }
                    expectedName = !expectedName;
                } else {
                    throw new MacroException(
                        startPosition,
                        "Missing right parenthesis for macro parameter list."
                    );
                }
            }
            return parameters;
        } else {
            return null;
        }
    }

    private @Mandatory List<Token> readTokens(@Mandatory TokenReader reader) {
        List<Token> tokens = new List<>();
        while (reader.has()) {
            tokens.addLast(reader.read());
        }
        return tokens;
    }
}
