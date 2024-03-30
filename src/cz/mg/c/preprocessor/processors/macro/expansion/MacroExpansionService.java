package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.entities.macro.MacroCall;
import cz.mg.collections.list.List;
import cz.mg.token.Token;

public @Service interface MacroExpansionService {
    @Mandatory List<Token> expand(@Mandatory MacroManager macros, @Mandatory MacroCall call);
}