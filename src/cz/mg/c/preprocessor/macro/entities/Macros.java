package cz.mg.c.preprocessor.macro.entities;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Shared;
import cz.mg.collections.map.Map;

public @Entity class Macros {
    private Map<String, Macro> map = new Map<>(100);

    public Macros() {
    }

    @Required @Shared
    public Map<String, Macro> getMap() {
        return map;
    }

    public void setMap(Map<String, Macro> map) {
        this.map = map;
    }

    public void define(@Mandatory Macro definition) {
        map.set(definition.getName().getText(), definition);
    }

    public @Optional Macro get(@Mandatory String name) {
        return map.getOptional(name);
    }

    public boolean defined(@Mandatory String name) {
        return map.getOptional(name) != null;
    }

    public void undefine(@Mandatory String name) {
        map.remove(name);
    }
}
