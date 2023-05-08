package cz.mg.c.preprocessor.macro.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.macro.utilities.MacroExpansion;
import cz.mg.tokenizer.entities.Token;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.tokenizer.entities.tokens.DoubleQuoteToken;

public @Service class FileMacroExpansionService {
    private static @Optional FileMacroExpansionService instance;

    public static @Mandatory FileMacroExpansionService getInstance() {
        if (instance == null) {
            instance = new FileMacroExpansionService();
        }
        return instance;
    }

    private FileMacroExpansionService() {
    }

    public @Mandatory List<Token> expand(@Mandatory MacroExpansion expansion, @Mandatory File file) {
        return new List<>(
            new DoubleQuoteToken(
                file.getPath().toAbsolutePath().toString(),
                expansion.getToken().getPosition()
            )
        );
    }
}
