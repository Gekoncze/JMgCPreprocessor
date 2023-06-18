package cz.mg.c.preprocessor.processors.macro.services.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.directives.WarningDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

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
        reader.read("#", SpecialToken.class);
        Token token = reader.read(WarningDirective.KEYWORD, NameToken.class);
        directive.setToken(token);
        return directive;
    }
}
