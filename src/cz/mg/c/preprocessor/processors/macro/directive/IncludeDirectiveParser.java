package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.directives.IncludeDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Service class IncludeDirectiveParser implements DirectiveParser {
    private static volatile @Service IncludeDirectiveParser instance;

    public static IncludeDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new IncludeDirectiveParser();
                }
            }
        }
        return instance;
    }

    private IncludeDirectiveParser() {
    }

    @Override
    public @Mandatory String getName() {
        return IncludeDirective.KEYWORD;
    }

    @Override
    public @Mandatory IncludeDirective parse(@Mandatory List<Token> line) {
        IncludeDirective directive = new IncludeDirective();
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.skip(WhitespaceToken.class);
        reader.read("#", SpecialToken.class);
        reader.skip(WhitespaceToken.class);
        directive.setKeyword(reader.read(IncludeDirective.KEYWORD, NameToken.class));
        return directive;
    }
}
