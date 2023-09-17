package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.directives.IfdefDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Service class IfdefDirectiveParser implements DirectiveParser {
    private static volatile @Service IfdefDirectiveParser instance;

    public static IfdefDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new IfdefDirectiveParser();
                }
            }
        }
        return instance;
    }

    private IfdefDirectiveParser() {
    }

    @Override
    public @Mandatory String getName() {
        return IfdefDirective.KEYWORD;
    }

    @Override
    public @Mandatory IfdefDirective parse(@Mandatory List<Token> line) {
        IfdefDirective directive = new IfdefDirective();
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.skip(WhitespaceToken.class);
        reader.read("#", SpecialToken.class);
        reader.skip(WhitespaceToken.class);
        directive.setKeyword(reader.read(IfdefDirective.KEYWORD, NameToken.class));
        reader.skip(WhitespaceToken.class);
        directive.setName(reader.read(NameToken.class));
        reader.skip(WhitespaceToken.class);
        reader.readEnd();
        return directive;
    }
}
