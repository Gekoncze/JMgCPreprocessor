package cz.mg.c.preprocessor.processors.macro.entities;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Optional;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Part;
import cz.mg.tokenizer.entities.Token;
import cz.mg.collections.list.List;

public @Entity class Macro {
    private Token name;
    private List<Token> parameters;
    private List<Token> tokens = new List<>();

    public Macro() {
    }

    public Macro(Token name, List<Token> parameters, List<Token> tokens) {
        this.name = name;
        this.parameters = parameters;
        this.tokens = tokens;
    }

    @Required @Part
    public Token getName() {
        return name;
    }

    public void setName(Token name) {
        this.name = name;
    }

    @Optional @Part
    public List<Token> getParameters() {
        return parameters;
    }

    public void setParameters(List<Token> parameters) {
        this.parameters = parameters;
    }

    @Required @Part
    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}
