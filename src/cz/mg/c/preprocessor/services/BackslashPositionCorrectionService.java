package cz.mg.c.preprocessor.services;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;

public @Service class BackslashPositionCorrectionService {
    private static volatile @Service BackslashPositionCorrectionService instance;

    public static @Service BackslashPositionCorrectionService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new BackslashPositionCorrectionService();
                }
            }
        }
        return instance;
    }

    private BackslashPositionCorrectionService() {
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
