package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.entities.directives.ElifDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.SymbolToken;
import cz.mg.tokenizer.entities.tokens.WordToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Service class ElifDirectiveParser implements DirectiveParser {
    private static volatile @Service ElifDirectiveParser instance;

    public static ElifDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new ElifDirectiveParser();
                    instance.expressionParser = ExpressionParser.getInstance();
                }
            }
        }
        return instance;
    }

    private ExpressionParser expressionParser;

    private ElifDirectiveParser() {
    }

    @Override
    public @Mandatory String getName() {
        return ElifDirective.KEYWORD;
    }

    @Override
    public @Mandatory ElifDirective parse(@Mandatory List<Token> line) {
        ElifDirective directive = new ElifDirective();
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.skip(WhitespaceToken.class);
        reader.read("#", SymbolToken.class);
        reader.skip(WhitespaceToken.class);
        directive.setKeyword(reader.read(ElifDirective.KEYWORD, WordToken.class));
        directive.setExpression(expressionParser.parse(line));
        return directive;
    }
}
