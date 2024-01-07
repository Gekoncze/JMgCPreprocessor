package cz.mg.c.entities.macro.system;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Link;
import cz.mg.c.entities.macro.Macro;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.tokenizer.entities.tokens.WordToken;

public @Entity class FileMacro extends Macro implements SystemMacro {
    public static final @Mandatory String NAME = "__FILE__";

    private File file;

    public FileMacro() {
        super(
            new WordToken(NAME, -1),
            null,
            new List<>()
        );
    }

    public FileMacro(File file) {
        this();
        this.file = file;
    }

    @Required @Link
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
