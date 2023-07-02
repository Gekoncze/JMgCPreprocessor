package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.directives.ElifDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

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
        reader.read("#", SpecialToken.class);
        directive.setKeyword(reader.read(ElifDirective.KEYWORD, NameToken.class));
        directive.setExpression(expressionParser.parse(line));
        return directive;
    }
}
