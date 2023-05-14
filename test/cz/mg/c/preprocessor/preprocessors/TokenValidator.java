package cz.mg.c.preprocessor.preprocessors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.annotations.requirement.Optional;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;

public @Service class TokenValidator {
    private static @Optional TokenValidator instance;

    public static @Mandatory TokenValidator getInstance() {
        if (instance == null) {
            instance = new TokenValidator();
        }
        return instance;
    }

    private TokenValidator() {
    }

    public void check(@Mandatory List<Token> actualTokens, String... expectation) {
        List<String> reality = new List<>();

        for (Token actualToken : actualTokens) {
            reality.addLast(actualToken.getText());
        }

        Assert
            .assertThatCollections(new List<>(expectation), reality)
            .withPrintFunction(s -> s.equals("\n") ? "\\n" : s)
            .verbose("[", ",", "]")
            .areEqual();
    }
}
