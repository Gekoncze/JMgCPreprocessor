package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.macro.Macros;
import cz.mg.c.preprocessor.processors.macro.MacroProcessor;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.tokenizer.entities.Token;

public @Service class MainPreprocessor {
    private static volatile @Service MainPreprocessor instance;

    public static @Service MainPreprocessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MainPreprocessor();
                    instance.commentProcessor = CommentProcessor.getInstance();
                    instance.macroProcessor = MacroProcessor.getInstance();
                    instance.tokenProcessor = TokenProcessor.getInstance();
                    instance.newlineProcessor = NewlineProcessor.getInstance();
                    instance.whitespaceProcessor = WhitespaceProcessor.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service CommentProcessor commentProcessor;
    private @Service MacroProcessor macroProcessor;
    private @Service TokenProcessor tokenProcessor;
    private @Service NewlineProcessor newlineProcessor;
    private @Service WhitespaceProcessor whitespaceProcessor;

    private MainPreprocessor() {
    }

    public @Mandatory List<Token> process(@Mandatory File file, @Mandatory Macros macros) {
        List<Token> tokens = tokenProcessor.process(file.getContent());
        commentProcessor.process(tokens);
        List<List<Token>> lines = newlineProcessor.process(tokens);
        tokens = macroProcessor.process(lines, macros);
        whitespaceProcessor.process(tokens);
        whitespaceProcessor.process(macros);
        return tokens;
    }
}
