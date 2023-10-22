package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.exceptions.CodeException;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenMutator;
import cz.mg.tokenizer.test.TokenValidator;

public @Test class ElseDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + ElseDirectiveParserTest.class.getSimpleName() + " ... ");

        ElseDirectiveParserTest test = new ElseDirectiveParserTest();
        test.testParse();
        test.testUnexpectedTrailingTokens();

        System.out.println("OK");
    }

    private final @Service ElseDirectiveParser parser = ElseDirectiveParser.getInstance();
    private final @Service DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenValidator tokenValidator = TokenValidator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(ElseDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.special("#"), f.name("else")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> tokenValidator.assertEquals(f.name("else"), directive.getKeyword())
        );

        mutator.mutate(
            new List<>(f.whitespace(" "), f.special("#"), f.whitespace(" "), f.name("else"), f.whitespace(" ")),
            new List<>(0, 1, 2, 3, 4),
            tokens -> parser.parse(tokens),
            directive -> tokenValidator.assertEquals(f.name("else"), directive.getKeyword())
        );
    }

    private void testUnexpectedTrailingTokens() {
        Assert
            .assertThatCode(() -> parser.parse(new List<>(f.special("#"), f.name("else"), f.whitespace(" "))))
            .doesNotThrowAnyException();

        Assert
            .assertThatCode(() -> parser.parse(new List<>(f.special("#"), f.name("else"), f.name("unexpected"))))
            .throwsException(CodeException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.special("#"),
                f.name("else"),
                f.whitespace(" "),
                f.name("unexpected")
            )))
            .throwsException(CodeException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.special("#"),
                f.name("else"),
                f.name("unexpected"),
                f.whitespace(" ")
            )))
            .throwsException(CodeException.class);
    }
}
