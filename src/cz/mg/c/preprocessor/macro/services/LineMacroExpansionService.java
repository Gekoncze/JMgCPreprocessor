package cz.mg.c.preprocessor.macro.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.macro.components.MacroExpansion;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NumberToken;
import cz.mg.tokenizer.services.PositionService;

public @Service class LineMacroExpansionService {
    private static volatile @Service LineMacroExpansionService instance;

    public static @Service LineMacroExpansionService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new LineMacroExpansionService();
                    instance.positionService = PositionService.getInstance();
                }
            }
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
