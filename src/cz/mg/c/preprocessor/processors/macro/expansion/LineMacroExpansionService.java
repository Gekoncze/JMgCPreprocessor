package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.entities.system.FileMacro;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.NumberToken;
import cz.mg.tokenizer.services.PositionService;

public @Service class LineMacroExpansionService implements MacroExpansionService {
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

    private @Service PositionService positionService;

    private LineMacroExpansionService() {
    }

    @Override
    public @Mandatory List<Token> expand(@Mandatory Macros macros, @Mandatory MacroCall call) {
        FileMacro fileMacro = (FileMacro) macros.getMap().get(FileMacro.NAME);
        String content = fileMacro.getFile().getContent();
        int position = call.getToken().getPosition();
        int row = positionService.find(content, position).getRow();
        return new List<>(new NumberToken("" + row, position));
    }
}
