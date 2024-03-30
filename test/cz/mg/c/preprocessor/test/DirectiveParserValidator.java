package cz.mg.c.preprocessor.test;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.directive.DirectiveParser;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.tokens.SymbolToken;
import cz.mg.tokenizer.entities.tokens.WordToken;

public @Service class DirectiveParserValidator {
    private static volatile @Service DirectiveParserValidator instance;

    public static @Service DirectiveParserValidator getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new DirectiveParserValidator();
                }
            }
        }
        return instance;
    }

    private DirectiveParserValidator() {
    }

    public void validate(@Mandatory DirectiveParser parser) {
        Assert
            .assertThatCode(() -> parser.parse(new List<>()))
            .throwsException(PreprocessorException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(new WordToken("x", 0))))
            .throwsException(PreprocessorException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(new SymbolToken("#", 0))))
            .throwsException(PreprocessorException.class);

        Assert.assertEquals(false, parser.getName().isEmpty());
        Assert.assertEquals(false, parser.getName().contains(" "));
        Assert.assertEquals(parser.getName(), parser.getName().toLowerCase());
    }
}
