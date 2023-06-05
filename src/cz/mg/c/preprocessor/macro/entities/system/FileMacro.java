package cz.mg.c.preprocessor.macro.entities.system;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Link;
import cz.mg.c.preprocessor.macro.entities.Macro;
import cz.mg.file.File;

public @Entity class FileMacro extends Macro implements SystemMacro {
    private File file;

    public FileMacro() {
    }

    @Required @Link
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
