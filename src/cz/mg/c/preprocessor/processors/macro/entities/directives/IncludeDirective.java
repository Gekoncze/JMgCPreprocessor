package cz.mg.c.preprocessor.processors.macro.entities.directives;

import cz.mg.annotations.classes.Entity;
import cz.mg.annotations.requirement.Required;
import cz.mg.annotations.storage.Value;
import cz.mg.tokenizer.entities.tokens.WordToken;

import java.nio.file.Path;

public @Entity class IncludeDirective extends Directive {
    public static final String KEYWORD = "include";

    private boolean library;
    private Path path;

    public IncludeDirective() {
    }

    public IncludeDirective(WordToken keyword) {
        super(keyword);
    }

    @Required @Value
    public boolean isLibrary() {
        return library;
    }

    public void setLibrary(boolean library) {
        this.library = library;
    }

    @Required @Value
    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
