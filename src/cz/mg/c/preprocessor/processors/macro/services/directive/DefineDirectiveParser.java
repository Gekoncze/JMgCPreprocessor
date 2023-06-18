package cz.mg.c.preprocessor.processors.macro.services.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.entities.directives.DefineDirective;
import cz.mg.c.preprocessor.processors.macro.services.MacroParser;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

public @Service class DefineDirectiveParser implements DirectiveParser {
    private static volatile @Service DefineDirectiveParser instance;

    public static DefineDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new DefineDirectiveParser();
                    instance.macroParser = MacroParser.getInstance();
                }
            }
        }
        return instance;
    }

    private MacroParser macroParser;

    private DefineDirectiveParser() {
    }

    @Override
    public @Mandatory String getName() {
        return DefineDirective.KEYWORD;
    }

    @Override
    public @Mandatory DefineDirective parse(@Mandatory List<Token> line) {
        DefineDirective directive = new DefineDirective();
        TokenReader reader = new TokenReader(line, PreprocessorException::new);
        reader.read("#", SpecialToken.class);
        directive.setToken(reader.read(DefineDirective.KEYWORD, NameToken.class));
        directive.setMacro(macroParser.parse(line));
        return directive;
    }
}
