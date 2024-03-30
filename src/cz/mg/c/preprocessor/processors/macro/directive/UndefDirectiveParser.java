package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.entities.directives.UndefDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.token.Token;
import cz.mg.token.tokens.SymbolToken;
import cz.mg.token.tokens.WordToken;
import cz.mg.token.tokens.WhitespaceToken;

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
        reader.skip(WhitespaceToken.class);
        reader.read("#", SymbolToken.class);
        reader.skip(WhitespaceToken.class);
        directive.setKeyword(reader.read(UndefDirective.KEYWORD, WordToken.class));
        reader.skip(WhitespaceToken.class);
        directive.setName(reader.read(WordToken.class));
        reader.skip(WhitespaceToken.class);
        reader.readEnd();
        return directive;
    }
}