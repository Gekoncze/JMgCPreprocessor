package cz.mg.c.preprocessor.processors.macro;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.components.MacroBranches;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpander;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service class MacroProcessor {
    private static volatile @Service MacroProcessor instance;

    public static @Service MacroProcessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroProcessor();
                }
            }
        }
        return instance;
    }

    private MacroProcessor() {
    }

    /**
     * Removes macro definitions from lines and returns list of remaining tokens.
     * Macro definitions are stored into macros parameter.
     * Macro calls are evaluated during the processing.
     */
    public @Mandatory List<Token> process(@Mandatory List<List<Token>> lines, @Mandatory Macros macros) {
        MacroBranches branches = new MacroBranches();
        MacroManager manager = new MacroManager(macros);
        MacroExpander expander = new MacroExpander(manager);

        for (List<Token> line : lines) {
            branches.getBranch().process(line, manager, branches, expander);
        }

        branches.validateIsRoot();
        expander.endExpanding();

        return expander.getTokens();
    }
}
