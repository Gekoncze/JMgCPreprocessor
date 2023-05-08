package cz.mg.c.preprocessor.macro.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.macro.utilities.MacroExpansion;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NumberToken;
import cz.mg.tokenizer.services.PositionService;
import cz.mg.collections.list.List;
import cz.mg.file.File;

public @Service class LineMacroExpansionService {
    private static @Optional LineMacroExpansionService instance;

    public static @Mandatory LineMacroExpansionService getInstance() {
        if (instance == null) {
            instance = new LineMacroExpansionService();
            instance.positionService = PositionService.getInstance();
        }
        return instance;
    }

    private PositionService positionService;

    private LineMacroExpansionService() {
    }

    public @Mandatory List<Token> expand(@Mandatory MacroExpansion expansion, @Mandatory File file) {
        return new List<>(
            new NumberToken(
                "" + positionService.find(file.getContent(), expansion.getToken().getPosition()).getRow(),
                expansion.getToken().getPosition()
            )
        );
    }
}
