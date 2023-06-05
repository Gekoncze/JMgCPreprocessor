package cz.mg.c.preprocessor.macro.entities.system;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Value;
import cz.mg.c.preprocessor.macro.entities.Macro;

public @Entity class LineMacro extends Macro implements SystemMacro {
    private int line;

    public LineMacro() {
    }

    @Required @Value
    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
