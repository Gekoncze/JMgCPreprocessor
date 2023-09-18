package cz.mg.c.preprocessor.processors.macro.entities;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Part;
import cz.mg.collections.list.List;

public @Entity class Macros {
    private List<Macro> definitions = new List<>();
    private List<MacroCall> calls = new List<>();

    public Macros() {
    }

    @Required @Part
    public List<Macro> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Macro> definitions) {
        this.definitions = definitions;
    }

    @Required @Part
    public List<MacroCall> getCalls() {
        return calls;
    }

    public void setCalls(List<MacroCall> calls) {
        this.calls = calls;
    }
}
