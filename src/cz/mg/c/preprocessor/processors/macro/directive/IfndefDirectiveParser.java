package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.entities.directives.IfndefDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.WordToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Service class IfndefDirectiveParser implements DirectiveParser {
    private static volatile @Service IfndefDirectiveParser instance;

    public static IfndefDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new IfndefDirectiveParser();
                }
            }
        }
        return instance;
    }

    private IfndefDirectiveParser() {
    }

    @Override
    public @Mandatory String getName() {
        return IfndefDirective.KEYWORD;
    }

    @Override
    public @Mandatory IfndefDirective parse(@Mandatory List<Token> line) {
        IfndefDirective directive = new IfndefDirective();
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.skip(WhitespaceToken.class);
        reader.read("#", SpecialToken.class);
        reader.skip(WhitespaceToken.class);
        directive.setKeyword(reader.read(IfndefDirective.KEYWORD, WordToken.class));
        reader.skip(WhitespaceToken.class);
        directive.setName(reader.read(WordToken.class));
        reader.skip(WhitespaceToken.class);
        reader.readEnd();
        return directive;
    }
}
