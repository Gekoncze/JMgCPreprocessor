package cz.mg.c.preprocessor.processors.macro.services.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.directives.ErrorDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

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
        reader.read("#", SpecialToken.class);
        Token token = reader.read(ErrorDirective.KEYWORD, NameToken.class);
        directive.setToken(token);
        return directive;
    }
}
