package cz.mg.c.preprocessor.macro.services.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.macro.components.MacroExpansion;
import cz.mg.c.preprocessor.macro.entities.Macros;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Service interface MacroExpansionService {
    @Mandatory List<Token> expand(@Mandatory Macros macros, @Mandatory MacroExpansion expansion);
}
