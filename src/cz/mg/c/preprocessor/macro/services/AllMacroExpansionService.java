package cz.mg.c.preprocessor.macro.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.macro.components.MacroExpander;
import cz.mg.c.preprocessor.macro.components.MacroExpansion;
import cz.mg.c.preprocessor.macro.entities.Macros;
import cz.mg.c.preprocessor.macro.entities.system.DefinedMacro;
import cz.mg.c.preprocessor.macro.entities.system.FileMacro;
import cz.mg.c.preprocessor.macro.entities.system.LineMacro;
import cz.mg.c.preprocessor.macro.services.expansion.*;
import cz.mg.collections.list.List;
import cz.mg.collections.map.Map;
import cz.mg.tokenizer.entities.Token;

public @Service class AllMacroExpansionService {
    private static volatile @Service AllMacroExpansionService instance;

    public static @Service AllMacroExpansionService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new AllMacroExpansionService();
                    instance.macroExpansionValidator = MacroExpansionValidator.getInstance();
                    instance.macroExpansionNames = new Map<>(10);
                    instance.macroExpansionNames.set(DefinedMacro.NAME, DefinedMacro.NAME);
                    instance.macroExpansionNames.set(FileMacro.NAME, FileMacro.NAME);
                    instance.macroExpansionNames.set(LineMacro.NAME, LineMacro.NAME);
                    instance.macroExpansionServices = new Map<>(10);
                    instance.macroExpansionServices.set(DefinedMacro.NAME, DefinedMacroExpansionService.getInstance());
                    instance.macroExpansionServices.set(FileMacro.NAME, FileMacroExpansionService.getInstance());
                    instance.macroExpansionServices.set(LineMacro.NAME, LineMacroExpansionService.getInstance());
                    instance.macroExpansionServices.set(null, PlainMacroExpansionService.getInstance());
                }
            }
        }
        return instance;
    }

    private @Service MacroExpansionValidator macroExpansionValidator;
    private @Service Map<String, String> macroExpansionNames;
    private @Service Map<String, MacroExpansionService> macroExpansionServices;

    private AllMacroExpansionService() {
    }

    public @Mandatory List<Token> expandRecursively(@Mandatory MacroExpansion expansion, @Mandatory Macros macros) {
        List<Token> expandedTokens = expand(expansion, macros);
        MacroExpander expander = new MacroExpander(macros);
        for (Token token : expandedTokens) {
            expander.expand(token);
        }
        expander.validateNotExpanding();
        return expander.getTokens();
    }

    private @Mandatory List<Token> expand(@Mandatory MacroExpansion expansion, @Mandatory Macros macros) {
        macroExpansionValidator.validate(expansion);
        String name = expansion.getMacro().getName().getText();
        String macroExpansionName = macroExpansionNames.getOptional(name);
        MacroExpansionService macroExpansionService = macroExpansionServices.get(macroExpansionName);
        return macroExpansionService.expand(macros, expansion);
    }
}
