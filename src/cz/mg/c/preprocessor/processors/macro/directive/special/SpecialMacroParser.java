package cz.mg.c.preprocessor.processors.macro.directive.special;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.directive.MacroParser;
import cz.mg.c.entities.macro.Macro;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service class SpecialMacroParser {
    private static volatile @Service SpecialMacroParser instance;

    public static @Service SpecialMacroParser getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new SpecialMacroParser();
                    instance.macroParser = MacroParser.getInstance();
                    instance.operatorConcatenationService = OperatorConcatenationService.getInstance();
                    instance.tokenConcatenationService = TokenConcatenationService.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service MacroParser macroParser;
    private @Service OperatorConcatenationService operatorConcatenationService;
    private @Service TokenConcatenationService tokenConcatenationService;

    private SpecialMacroParser() {
    }

    public @Mandatory Macro parse(@Mandatory List<Token> line) {
        Macro macro = macroParser.parse(line);
        operatorConcatenationService.concatenate(macro.getTokens());
        tokenConcatenationService.concatenate(macro.getTokens());
        return macro;
    }
}
