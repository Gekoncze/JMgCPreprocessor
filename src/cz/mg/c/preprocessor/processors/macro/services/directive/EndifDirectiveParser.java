package cz.mg.c.preprocessor.processors.macro.services.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.directives.EndifDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

public @Service class EndifDirectiveParser implements DirectiveParser {
    private static volatile @Service EndifDirectiveParser instance;

    public static EndifDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new EndifDirectiveParser();
                }
            }
        }
        return instance;
    }

    private EndifDirectiveParser() {
    }

    @Override
    public @Mandatory String getName() {
        return EndifDirective.KEYWORD;
    }

    @Override
    public @Mandatory EndifDirective parse(@Mandatory List<Token> line) {
        EndifDirective directive = new EndifDirective();
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.read("#", SpecialToken.class);
        directive.setToken(reader.read(EndifDirective.KEYWORD, NameToken.class));
        reader.readEnd();
        return directive;
    }
}
