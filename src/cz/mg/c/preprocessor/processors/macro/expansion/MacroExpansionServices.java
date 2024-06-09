package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.macro.MacroCall;
import cz.mg.c.entities.macro.system.DefinedMacro;
import cz.mg.c.entities.macro.system.FileMacro;
import cz.mg.c.entities.macro.system.LineMacro;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.collections.list.List;
import cz.mg.collections.map.Map;
import cz.mg.token.Token;

public @Service class MacroExpansionServices {
    private static volatile @Service MacroExpansionServices instance;

    public static @Service MacroExpansionServices getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroExpansionServices();
                    instance.macroCallValidator = MacroCallValidator.getInstance();
                    instance.systemMacroNames = new Map<>();
                    instance.systemMacroNames.set(DefinedMacro.NAME, DefinedMacro.NAME);
                    instance.systemMacroNames.set(FileMacro.NAME, FileMacro.NAME);
                    instance.systemMacroNames.set(LineMacro.NAME, LineMacro.NAME);
                    instance.macroExpansionServices = new Map<>();
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

    private MacroExpansionServices() {
    }

    public @Mandatory List<Token> expand(@Mandatory MacroCall call, @Mandatory MacroManager macros) {
        macroCallValidator.validate(call);
        String macroName = call.getMacro().getName().getText();
        String systemMacroName = systemMacroNames.getOptional(macroName);
        MacroExpansionService macroExpansionService = macroExpansionServices.get(systemMacroName);
        macros.called(call);
        return macroExpansionService.expand(macros, call);
    }
}