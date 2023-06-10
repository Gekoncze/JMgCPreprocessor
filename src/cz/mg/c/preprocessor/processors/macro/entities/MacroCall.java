package cz.mg.c.preprocessor.processors.macro.entities;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Optional;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Link;
import cz.mg.annotations.storage.Part;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.Token;

public @Entity class MacroCall {
    private Macro macro;
    private Token token;
    private List<List<Token>> arguments;

    public MacroCall() {
    }

    public MacroCall(Macro macro, Token token, List<List<Token>> arguments) {
        this.macro = macro;
        this.token = token;
        this.arguments = arguments;
    }

    @Required @Link
    public Macro getMacro() {
        return macro;
    }

    public void setMacro(Macro macro) {
        this.macro = macro;
    }

    @Required @Part
    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Optional @Part
    public List<List<Token>> getArguments() {
        return arguments;
    }

    public void setArguments(List<List<Token>> arguments) {
        this.arguments = arguments;
    }
}
