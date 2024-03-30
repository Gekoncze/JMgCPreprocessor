package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.entities.directives.Directive;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.SymbolToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;
import cz.mg.tokenizer.entities.tokens.WordToken;

public @Service class DirectiveParsers {
    private static volatile @Service DirectiveParsers instance;

    public static @Service DirectiveParsers getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new DirectiveParsers();
                    instance.parsers = new List<>(
                        DefineDirectiveParser.getInstance(),
                        ElifDirectiveParser.getInstance(),
                        ElseDirectiveParser.getInstance(),
                        EndifDirectiveParser.getInstance(),
                        ErrorDirectiveParser.getInstance(),
                        IfdefDirectiveParser.getInstance(),
                        IfDirectiveParser.getInstance(),
                        IfndefDirectiveParser.getInstance(),
                        IncludeDirectiveParser.getInstance(),
                        UndefDirectiveParser.getInstance(),
                        WarningDirectiveParser.getInstance()
                    );
                }
            }
        }
        return instance;
    }

    private @Service List<DirectiveParser> parsers;

    private DirectiveParsers() {
    }

    public @Optional Directive parse(@Mandatory List<Token> line) {
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.skip(WhitespaceToken.class);
        if (reader.has("#", SymbolToken.class)) {
            reader.read();
            reader.skip(WhitespaceToken.class);
            Token name = reader.read(WordToken.class);
            return findParser(name).parse(line);
        } else {
            return null;
        }
    }

    private @Mandatory DirectiveParser findParser(@Mandatory Token name) {
        for (DirectiveParser parser : parsers) {
            if (parser.getName().equals(name.getText())) {
                return parser;
            }
        }

        throw new PreprocessorException(
            name.getPosition(),
            "Unsupported preprocessor directive '" + name.getText() + "'."
        );
    }
}
