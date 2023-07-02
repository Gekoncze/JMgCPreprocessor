package cz.mg.c.preprocessor.processors.macro.branch;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.components.MacroBranches;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpander;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service interface MacroBranchProcessor {
    void process(
        @Mandatory List<Token> line,
        @Mandatory Macros macros,
        @Mandatory MacroBranches branches,
        @Mandatory MacroExpander expander
    );
}
