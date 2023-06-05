package cz.mg.c.preprocessor.processors.macro.components;

import cz.mg.annotations.classes.Component;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.tokenizer.entities.Token;
import cz.mg.collections.list.List;

public @Component class MacroExpansion {
    private final @Mandatory Token token;
    private final @Mandatory Macro macro;
    private final @Optional List<List<Token>> arguments;
    private int nesting;

    public MacroExpansion(@Mandatory Token token, @Mandatory Macro macro, @Optional List<List<Token>> arguments) {
        this.token = token;
        this.macro = macro;
        this.arguments = arguments;
    }

    public @Mandatory Token getToken() {
        return token;
    }

    public @Mandatory Macro getMacro() {
        return macro;
    }

    public @Optional List<List<Token>> getArguments() {
        return arguments;
    }

    public int getNesting() {
        return nesting;
    }

    public void setNesting(int nesting) {
        this.nesting = nesting;
    }
}
