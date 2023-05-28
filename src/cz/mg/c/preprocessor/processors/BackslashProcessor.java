package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;

public @Service class BackslashProcessor {
    private static @Optional BackslashProcessor instance;

    public static @Mandatory BackslashProcessor getInstance() {
        if (instance == null) {
            instance = new BackslashProcessor();
        }
        return instance;
    }

    private BackslashProcessor() {
    }

    /**
     * Joins rows that end with a backslash.
     */
    public @Mandatory String process(@Mandatory String content) {
        return content.replace("\\\n", "");
    }
}
