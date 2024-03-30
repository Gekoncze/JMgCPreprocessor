package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.entities.directives.WarningDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.token.Token;
import cz.mg.token.tokens.SymbolToken;
import cz.mg.token.tokens.WordToken;
import cz.mg.token.tokens.WhitespaceToken;

public @Service class WarningDirectiveParser implements DirectiveParser {
    private static volatile @Service WarningDirectiveParser instance;

    public static WarningDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new WarningDirectiveParser();
                }
            }
        }
        return instance;
    }

    private WarningDirectiveParser() {
    }

    @Override
    public @Mandatory String getName() {
        return WarningDirective.KEYWORD;
    }

    @Override
    public @Mandatory WarningDirective parse(@Mandatory List<Token> line) {
        WarningDirective directive = new WarningDirective();
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.skip(WhitespaceToken.class);
        reader.read("#", SymbolToken.class);
        reader.skip(WhitespaceToken.class);
        directive.setKeyword(reader.read(WarningDirective.KEYWORD, WordToken.class));
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