package cz.mg.c.preprocessor.processors.backslash;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;

public @Service class BackslashPositionService {
    private static volatile @Service BackslashPositionService instance;

    public static @Service BackslashPositionService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new BackslashPositionService();
                }
            }
        }
        return instance;
    }

    private BackslashPositionService() {
    }

    @SuppressWarnings("SpellCheckingInspection")
    public int correct(@Mandatory String content, int position) {
        validate(content, position);

        int originalPosition = 0;
        int backslashedPosition = 0;

        while (backslashedPosition < position) {
            char ch = get(content, originalPosition);
            char nch = get(content, originalPosition + 1);

            if (ch == '\\' && nch == '\n') {
                backslashedPosition -= 2;
            }

            originalPosition++;
            backslashedPosition++;
        }

        while (get(content, originalPosition) == '\\' && get(content, originalPosition + 1) == '\n') {
            originalPosition += 2;
        }

        return originalPosition;
    }

    private char get(@Mandatory String content, int i) {
        if (i < content.length()) {
            return content.charAt(i);
        } else {
            return '\0';
        }
    }

    private void validate(@Mandatory String content, int position) {
        if (position < 0 || position >= content.length()) {
            throw new ArrayIndexOutOfBoundsException(
                "Position " + position + " is out of bounds of string of length " + content.length() + "."
            );
        }
    }
}