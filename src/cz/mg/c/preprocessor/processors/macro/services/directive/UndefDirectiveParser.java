package cz.mg.c.preprocessor.processors.macro.services.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.directives.UndefDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

public @Service class UndefDirectiveParser implements DirectiveParser {
    private static volatile @Service UndefDirectiveParser instance;

    public static UndefDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new UndefDirectiveParser();
                }
            }
        }
        return instance;
    }

    private UndefDirectiveParser() {
    }

    @Override
    public @Mandatory String getName() {
        return UndefDirective.KEYWORD;
    }

    @Override
    public @Mandatory UndefDirective parse(@Mandatory List<Token> line) {
        UndefDirective directive = new UndefDirective();
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.read("#", SpecialToken.class);
        directive.setToken(reader.read(UndefDirective.KEYWORD, NameToken.class));
        directive.setName(reader.read(NameToken.class));
        reader.readEnd();
        return directive;
    }
}
