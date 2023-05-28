package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.macro.MacroProcessor;
import cz.mg.c.preprocessor.macro.entities.Macros;
import cz.mg.c.preprocessor.macro.components.SystemMacros;
import cz.mg.c.preprocessor.processors.*;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.tokenizer.Tokenizer;
import cz.mg.tokenizer.entities.Token;

public @Service class Preprocessor {
    private static volatile @Service Preprocessor instance;

    public static @Service Preprocessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new Preprocessor();
                    instance.backslashProcessor = BackslashProcessor.getInstance();
                    instance.commentProcessor = CommentProcessor.getInstance();
                    instance.macroProcessor = MacroProcessor.getInstance();
                    instance.newlineProcessor = NewlineProcessor.getInstance();
                    instance.operatorProcessor = OperatorProcessor.getInstance();
                    instance.whitespaceProcessor = WhitespaceProcessor.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service BackslashProcessor backslashProcessor;
    private @Service CommentProcessor commentProcessor;
    private @Service MacroProcessor macroProcessor;
    private @Service NewlineProcessor newlineProcessor;
    private @Service OperatorProcessor operatorProcessor;
    private @Service WhitespaceProcessor whitespaceProcessor;

    private Preprocessor() {
    }

    /**
     * Preprocessing of c source code before parsing.
     */
    public @Mandatory List<Token> preprocess(@Mandatory File file, @Mandatory Macros macros) {
        macros.define(SystemMacros.__FILE__);
        macros.define(SystemMacros.__LINE__);

        List<Token> tokens = process(file, macros);

        macros.undefine(SystemMacros.__LINE__.getName().getText());
        macros.undefine(SystemMacros.__FILE__.getName().getText());

        return tokens;
    }

    private @Mandatory List<Token> process(@Mandatory File file, @Mandatory Macros macros) {

        String content = backslashProcessor.process(file.getContent());
        List<Token> tokens = new Tokenizer().tokenize(content);
        operatorProcessor.process(tokens);
        commentProcessor.process(tokens);
        List<List<Token>> lines = newlineProcessor.process(tokens);
        whitespaceProcessor.process(lines);
        return macroProcessor.process(lines, macros, file);
    }
}
