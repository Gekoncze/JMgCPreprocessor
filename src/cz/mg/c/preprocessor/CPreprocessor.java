package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.macro.Macros;
import cz.mg.c.entities.macro.system.FileMacro;
import cz.mg.c.entities.macro.system.LineMacro;
import cz.mg.c.preprocessor.processors.MainPreprocessor;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.tokenizer.CTokenizer;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.tokenizer.Tokenizer;
import cz.mg.token.Token;

public @Component class CPreprocessor {
    private final @Mandatory MainPreprocessor mainPreprocessor = MainPreprocessor.getInstance();
    private final @Mandatory Tokenizer tokenizer;
    private final @Mandatory Macros macros;

    public CPreprocessor(@Mandatory Macros macros) {
        this(new CTokenizer(), macros);
    }

    public CPreprocessor(@Mandatory Tokenizer tokenizer, @Mandatory Macros macros) {
        this.tokenizer = tokenizer;
        this.macros = macros;
    }

    public @Mandatory List<Token> preprocess(@Mandatory File file) {
        MacroManager macroManager = new MacroManager(macros);
        return macroManager.temporary(() -> {
            return mainPreprocessor.process(file, macros, tokenizer);
        }, new FileMacro(file), new LineMacro());
    }
}