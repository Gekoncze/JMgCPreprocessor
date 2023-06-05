package cz.mg.c.preprocessor.processors.macro.services.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpansion;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.entities.system.FileMacro;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.DoubleQuoteToken;

public @Service class FileMacroExpansionService implements MacroExpansionService {
    private static volatile @Service FileMacroExpansionService instance;

    public static @Service FileMacroExpansionService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new FileMacroExpansionService();
                }
            }
        }
        return instance;
    }

    private FileMacroExpansionService() {
    }

    @Override
    public @Mandatory List<Token> expand(@Mandatory Macros macros, @Mandatory MacroExpansion expansion) {
        FileMacro macro = (FileMacro) expansion.getMacro();
        String path = macro.getFile().getPath().toAbsolutePath().toString();
        int position = expansion.getToken().getPosition();
        return new List<>(new DoubleQuoteToken(path, position));
    }
}
