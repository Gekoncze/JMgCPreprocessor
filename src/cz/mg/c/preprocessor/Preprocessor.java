package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.macro.MacroProcessor;
import cz.mg.c.preprocessor.macro.entities.Macros;
import cz.mg.c.preprocessor.macro.utilities.SystemMacros;
import cz.mg.c.preprocessor.processors.*;
import cz.mg.tokenizer.Tokenizer;
import cz.mg.tokenizer.entities.Token;
import cz.mg.collections.list.List;
import cz.mg.file.File;

public @Service class Preprocessor {
    private static @Optional Preprocessor instance;

    public static @Mandatory Preprocessor getInstance() {
        if (instance == null) {
            instance = new Preprocessor();
            instance.backslashProcessor = BackslashProcessor.getInstance();
            instance.bracketProcessor = BracketProcessor.getInstance();
            instance.commentProcessor = CommentProcessor.getInstance();
            instance.macroProcessor = MacroProcessor.getInstance();
            instance.newlineProcessor = NewlineProcessor.getInstance();
            instance.operatorProcessor = OperatorProcessor.getInstance();
            instance.whitespaceProcessor = WhitespaceProcessor.getInstance();
        }
        return instance;
    }

    private BackslashProcessor backslashProcessor;
    private BracketProcessor bracketProcessor;
    private CommentProcessor commentProcessor;
    private MacroProcessor macroProcessor;
    private NewlineProcessor newlineProcessor;
    private OperatorProcessor operatorProcessor;
    private WhitespaceProcessor whitespaceProcessor;

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
        List<Token> tokens = new Tokenizer().tokenize(file.getContent());
        backslashProcessor.process(tokens);
        operatorProcessor.process(tokens);
        commentProcessor.process(tokens);
        List<List<Token>> lines = newlineProcessor.process(tokens);
        whitespaceProcessor.process(lines);
        return macroProcessor.process(lines, macros, file);
    }
}
