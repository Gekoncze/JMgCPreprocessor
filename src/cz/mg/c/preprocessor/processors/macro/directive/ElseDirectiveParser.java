package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.directives.ElseDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.WordToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Service class ElseDirectiveParser implements DirectiveParser {
    private static volatile @Service ElseDirectiveParser instance;

    public static ElseDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new ElseDirectiveParser();
                }
            }
        }
        return instance;
    }

    private ElseDirectiveParser() {
    }

    @Override
    public @Mandatory String getName() {
        return ElseDirective.KEYWORD;
    }

    @Override
    public @Mandatory ElseDirective parse(@Mandatory List<Token> line) {
        ElseDirective directive = new ElseDirective();
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.skip(WhitespaceToken.class);
        reader.read("#", SpecialToken.class);
        reader.skip(WhitespaceToken.class);
        directive.setKeyword(reader.read(ElseDirective.KEYWORD, WordToken.class));
        reader.skip(WhitespaceToken.class);
        reader.readEnd();
        return directive;
    }
}
