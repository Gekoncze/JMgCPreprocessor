package cz.mg.c.preprocessor.processors.backslash;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;

/**
 * Class that joins rows that end with a backslash.
 * This is an unfortunate hacky feature of C programming language, which requires a hacky solution.
 * After tokens are parsed from content with joined rows, the token positions need to be fixed
 * using backslash position processor.
 */
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

    public @Mandatory String process(@Mandatory String content) {
        return content.replace("\\\n", "");
    }
}
