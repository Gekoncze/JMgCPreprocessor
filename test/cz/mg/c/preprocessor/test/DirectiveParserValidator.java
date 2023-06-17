package cz.mg.c.preprocessor.test;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.exceptions.PreprocessorException;
import cz.mg.c.preprocessor.processors.macro.services.directive.DirectiveParser;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;

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
            .assertThatCode(() -> parser.parse(new List<>(new NameToken("x", 0))))
            .throwsException(PreprocessorException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(new SpecialToken("#", 0))))
            .throwsException(PreprocessorException.class);

        Assert.assertEquals(false, parser.getName().isEmpty());
        Assert.assertEquals(false, parser.getName().contains(" "));
        Assert.assertEquals(parser.getName(), parser.getName().toLowerCase());
    }
}
