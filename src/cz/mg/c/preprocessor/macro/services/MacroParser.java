package cz.mg.c.preprocessor.macro.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.macro.entities.Macro;
import cz.mg.c.preprocessor.macro.utilities.MacroException;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.utilities.TokenReader;

public @Service class MacroParser {
    private static @Optional MacroParser instance;

    public static @Mandatory MacroParser getInstance() {
        if (instance == null) {
            instance = new MacroParser();
        }
        return instance;
    }

    private MacroParser() {
    }

    public @Mandatory Macro parse(@Mandatory List<Token> line) {
        TokenReader reader = new TokenReader(line);
        reader.read("#");
        reader.read("define");
        return new Macro(
            readName(reader),
            readParameters(reader),
            readTokens(reader)
        );
    }

    private @Mandatory Token readName(@Mandatory TokenReader reader) {
        return reader.read(NameToken.class);
    }

    private @Optional List<Token> readParameters(@Mandatory TokenReader reader) {
        if (reader.hasNext("(")) {
            List<Token> parameters = new List<>();
            int startPosition = reader.read().getPosition();
            boolean expectedName = true;
            while (true) {
                if (reader.hasNext(")")) {
                    int endPosition = reader.read().getPosition();
                    if (expectedName && !parameters.isEmpty()) {
                        throw new MacroException(
                            endPosition,
                            "Missing parameter for macro parameter list."
                        );
                    }
                    break;
                } else if (!reader.hasNext()) {
                    throw new MacroException(
                        startPosition,
                        "Missing right parenthesis for macro parameter list."
                    );
                } else {
                    if (expectedName) {
                        parameters.addLast(reader.read(NameToken.class));
                    } else {
                        reader.read(",");
                    }
                    expectedName = !expectedName;
                }
            }
            return parameters;
        } else {
            return null;
        }
    }

    private @Mandatory List<Token> readTokens(@Mandatory TokenReader reader) {
        List<Token> tokens = new List<>();
        while (reader.hasNext()) {
            tokens.addLast(reader.read());
        }
        return tokens;
    }
}