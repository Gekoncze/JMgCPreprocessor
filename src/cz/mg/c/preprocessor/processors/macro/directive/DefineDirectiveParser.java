package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.directive.special.SpecialMacroParser;
import cz.mg.c.entities.directives.DefineDirective;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.components.TokenReader;
import cz.mg.token.Token;
import cz.mg.token.tokens.SymbolToken;
import cz.mg.token.tokens.WordToken;
import cz.mg.token.tokens.WhitespaceToken;

public @Service class DefineDirectiveParser implements DirectiveParser {
    private static volatile @Service DefineDirectiveParser instance;

    public static @Service DefineDirectiveParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new DefineDirectiveParser();
                    instance.macroParser = SpecialMacroParser.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service SpecialMacroParser macroParser;

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
        reader.skip(WhitespaceToken.class);
        reader.read("#", SymbolToken.class);
        reader.skip(WhitespaceToken.class);
        directive.setKeyword(reader.read(DefineDirective.KEYWORD, WordToken.class));
        reader.skip(WhitespaceToken.class);
        directive.setMacro(macroParser.parse(line));
        return directive;
    }
}