package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.directives.IfDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;
import cz.mg.tokenizer.entities.tokens.WhitespaceToken;

public @Service class IfDirectiveParser implements DirectiveParser {
    private static volatile @Service IfDirectiveParser instance;

    public static IfDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new IfDirectiveParser();
                    instance.expressionParser = ExpressionParser.getInstance();
                }
            }
        }
        return instance;
    }

    private ExpressionParser expressionParser;

    private IfDirectiveParser() {
    }

    @Override
    public @Mandatory String getName() {
        return IfDirective.KEYWORD;
    }

    @Override
    public @Mandatory IfDirective parse(@Mandatory List<Token> line) {
        IfDirective directive = new IfDirective();
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.skip(WhitespaceToken.class);
        reader.read("#", SpecialToken.class);
        reader.skip(WhitespaceToken.class);
        directive.setKeyword(reader.read(IfDirective.KEYWORD, NameToken.class));
        directive.setExpression(expressionParser.parse(line));
        return directive;
    }
}
