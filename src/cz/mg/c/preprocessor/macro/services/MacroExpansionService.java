package cz.mg.c.preprocessor.macro.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.macro.entities.Macros;
import cz.mg.c.preprocessor.macro.components.MacroExpander;
import cz.mg.c.preprocessor.macro.components.MacroExpansion;
import cz.mg.c.preprocessor.macro.components.SystemMacros;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.tokenizer.entities.Token;

public @Service class MacroExpansionService {
    private static volatile @Service MacroExpansionService instance;

    public static @Service MacroExpansionService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroExpansionService();
                    instance.macroExpansionValidator = MacroExpansionValidator.getInstance();
                    instance.definedMacroExpansionService = DefinedMacroExpansionService.getInstance();
                    instance.fileMacroExpansionService = FileMacroExpansionService.getInstance();
                    instance.lineMacroExpansionService = LineMacroExpansionService.getInstance();
                    instance.plainMacroExpansionService = PlainMacroExpansionService.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service MacroExpansionValidator macroExpansionValidator;
    private @Service DefinedMacroExpansionService definedMacroExpansionService;
    private @Service FileMacroExpansionService fileMacroExpansionService;
    private @Service LineMacroExpansionService lineMacroExpansionService;
    private @Service PlainMacroExpansionService plainMacroExpansionService;

    private MacroExpansionService() {
    }

    public @Mandatory List<Token> expandRecursively(
        @Mandatory MacroExpansion expansion,
        @Mandatory Macros macros,
        @Mandatory File file
    ) {
        List<Token> expandedTokens = expand(expansion, macros, file);
        MacroExpander expander = new MacroExpander(macros, file);
        for (Token token : expandedTokens) {
            expander.expand(token);
        }
        expander.validateNotExpanding();
        return expander.getTokens();
    }

    private @Mandatory List<Token> expand(
        @Mandatory MacroExpansion expansion,
        @Mandatory Macros macros,
        @Mandatory File file
    ) {
        // TODO - check if this can be refactored
        macroExpansionValidator.validate(expansion);
        if (expansion.getMacro().getName().equals(SystemMacros.DEFINED.getName())) {
            return definedMacroExpansionService.expand(expansion, macros);
        } else if (expansion.getMacro().getName().equals(SystemMacros.__FILE__.getName())) {
            return fileMacroExpansionService.expand(expansion, file);
        } else if (expansion.getMacro().getName().equals(SystemMacros.__LINE__.getName())) {
            return lineMacroExpansionService.expand(expansion, file);
        } else {
            return plainMacroExpansionService.expand(expansion);
        }
    }
}
