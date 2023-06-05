package cz.mg.c.preprocessor.processors.backslash;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;

public @Service class BackslashProcessor {
    private static volatile @Service BackslashProcessor instance;

    public static @Service BackslashProcessor getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new BackslashProcessor();
                }
            }
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
