package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.entities.directives.ErrorDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.token.Token;
import cz.mg.token.tokens.SymbolToken;
import cz.mg.token.tokens.WordToken;
import cz.mg.token.tokens.WhitespaceToken;

public @Service class ErrorDirectiveParser implements DirectiveParser {
    private static volatile @Service ErrorDirectiveParser instance;

    public static ErrorDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new ErrorDirectiveParser();
                }
            }
        }
        return instance;
    }

    private ErrorDirectiveParser() {
    }

    @Override
    public @Mandatory String getName() {
        return ErrorDirective.KEYWORD;
    }

    @Override
    public @Mandatory ErrorDirective parse(@Mandatory List<Token> line) {
        ErrorDirective directive = new ErrorDirective();
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.skip(WhitespaceToken.class);
        reader.read("#", SymbolToken.class);
        reader.skip(WhitespaceToken.class);
        directive.setKeyword(reader.read(ErrorDirective.KEYWORD, WordToken.class));
        directive.setMessage(readMessage(reader));
        return directive;
    }

    private @Optional String readMessage(@Mandatory TokenReader reader) {
        reader.skip(WhitespaceToken.class);
        StringBuilder builder = new StringBuilder();
        while (reader.has()) {
            builder.append(reader.read().getText());
        }
        String message = builder.toString().trim();
        return !message.isEmpty() ? message : null;
    }
}