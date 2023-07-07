package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.CommentProcessor;
import cz.mg.c.preprocessor.processors.TokenProcessor;
import cz.mg.c.preprocessor.processors.WhitespaceProcessor;
import cz.mg.c.preprocessor.processors.macro.MacroProcessor;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.entities.system.FileMacro;
import cz.mg.c.preprocessor.processors.macro.entities.system.LineMacro;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.tokenizer.entities.Token;

public @Service class Preprocessor {
    private static volatile @Service Preprocessor instance;

    public static @Service Preprocessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new Preprocessor();
                    instance.commentProcessor = CommentProcessor.getInstance();
                    instance.macroProcessor = MacroProcessor.getInstance();
                    instance.tokenProcessor = TokenProcessor.getInstance();
                    instance.whitespaceProcessor = WhitespaceProcessor.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service CommentProcessor commentProcessor;
    private @Service MacroProcessor macroProcessor;
    private @Service TokenProcessor tokenProcessor;
    private @Service WhitespaceProcessor whitespaceProcessor;

    private Preprocessor() {
    }

    /**
     * Preprocessing of c source code before parsing.
     */
    public @Mandatory List<Token> preprocess(@Mandatory File file, @Mandatory Macros macros) {
        return Macros.temporary(macros, new FileMacro(file), () -> {
            return Macros.temporary(macros, new LineMacro(), () -> {
                List<Token> tokens = tokenProcessor.process(file.getContent());
                commentProcessor.process(tokens);
                List<List<Token>> lines = whitespaceProcessor.process(tokens);
                return macroProcessor.process(lines, macros);
            });
        });
    }
}
