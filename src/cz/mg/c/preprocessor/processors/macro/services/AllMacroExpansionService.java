package cz.mg.c.preprocessor.processors.macro.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpander;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.entities.system.DefinedMacro;
import cz.mg.c.preprocessor.processors.macro.entities.system.FileMacro;
import cz.mg.c.preprocessor.processors.macro.entities.system.LineMacro;
import cz.mg.c.preprocessor.processors.macro.services.expansion.*;
import cz.mg.collections.list.List;
import cz.mg.collections.map.Map;
import cz.mg.tokenizer.entities.Token;

@Deprecated
public @Service class AllMacroExpansionService {
    private static volatile @Service AllMacroExpansionService instance;

    public static @Service AllMacroExpansionService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new AllMacroExpansionService();
                    instance.macroCallValidator = MacroCallValidator.getInstance();
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

    private @Service MacroCallValidator macroCallValidator;
    private @Service Map<String, String> macroExpansionNames;
    private @Service Map<String, MacroExpansionService> macroExpansionServices;

    private AllMacroExpansionService() {
    }

    public @Mandatory List<Token> expandRecursively(@Mandatory MacroCall call, @Mandatory Macros macros) {
        return MacroExpander.expand(expand(call, macros), macros);
    }

    private @Mandatory List<Token> expand(@Mandatory MacroCall call, @Mandatory Macros macros) {
        macroCallValidator.validate(call);
        String name = call.getMacro().getName().getText();
        String macroExpansionName = macroExpansionNames.getOptional(name);
        MacroExpansionService macroExpansionService = macroExpansionServices.get(macroExpansionName);
        return macroExpansionService.expand(macros, call);
    }
}
