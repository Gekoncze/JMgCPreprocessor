package cz.mg.c.preprocessor.processors.macro.entities.system;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Link;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.tokenizer.entities.tokens.NameToken;

public @Entity class FileMacro extends Macro implements SystemMacro {
    public static final @Mandatory String NAME = "__FILE__";

    private File file;

    public FileMacro() {
        super(
            new NameToken(NAME, -1),
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
