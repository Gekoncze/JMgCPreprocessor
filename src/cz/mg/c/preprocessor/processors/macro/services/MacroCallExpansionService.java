package cz.mg.c.preprocessor.processors.macro.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.entities.system.DefinedMacro;
import cz.mg.c.preprocessor.processors.macro.entities.system.FileMacro;
import cz.mg.c.preprocessor.processors.macro.entities.system.LineMacro;
import cz.mg.c.preprocessor.processors.macro.services.expansion.*;
import cz.mg.collections.list.List;
import cz.mg.collections.map.Map;
import cz.mg.tokenizer.entities.Token;

public @Service class MacroCallExpansionService {
    private static volatile @Service MacroCallExpansionService instance;

    public static @Service MacroCallExpansionService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroCallExpansionService();
                    instance.macroCallValidator = MacroCallValidator.getInstance();
                    instance.systemMacroNames = new Map<>(10);
                    instance.systemMacroNames.set(DefinedMacro.NAME, DefinedMacro.NAME);
                    instance.systemMacroNames.set(FileMacro.NAME, FileMacro.NAME);
                    instance.systemMacroNames.set(LineMacro.NAME, LineMacro.NAME);
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
    private @Service Map<String, String> systemMacroNames;
    private @Service Map<String, MacroExpansionService> macroExpansionServices;

    private MacroCallExpansionService() {
    }

    public @Mandatory List<Token> expand(@Mandatory MacroCall call, @Mandatory Macros macros) {
        macroCallValidator.validate(call);
        String name = call.getMacro().getName().getText();
        String systemMacroName = systemMacroNames.getOptional(name);
        MacroExpansionService macroExpansionService = macroExpansionServices.get(systemMacroName);
        return macroExpansionService.expand(macros, call);
    }
}
