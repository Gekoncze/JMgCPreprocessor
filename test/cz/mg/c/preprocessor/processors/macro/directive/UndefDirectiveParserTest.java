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

public @Test class UndefDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + UndefDirectiveParserTest.class.getSimpleName() + " ... ");

        UndefDirectiveParserTest test = new UndefDirectiveParserTest();
        test.testParse();
        test.testUnexpectedTrailingTokens();

        System.out.println("OK");
    }

    private final @Service UndefDirectiveParser parser = UndefDirectiveParser.getInstance();
    private final @Service DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenValidator tokenValidator = TokenValidator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(UndefDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.special("#"), f.name("undef"), f.name("TEST")),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.name("undef"), directive.getKeyword());
                tokenValidator.assertEquals(f.name("TEST"), directive.getName());
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.special("#"),
                f.whitespace(" "),
                f.name("undef"),
                f.whitespace(" "),
                f.name("TEST"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3, 4, 5),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.name("undef"), directive.getKeyword());
                tokenValidator.assertEquals(f.name("TEST"), directive.getName());
            }
        );
    }

    private void testUnexpectedTrailingTokens() {
        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.special("#"),
                f.name("undef"),
                f.name("TEST"),
                f.whitespace(" ")
            )))
            .doesNotThrowAnyException();

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.special("#"),
                f.name("undef"),
                f.name("TEST"),
                f.name("unexpected")
            )))
            .throwsException(CodeException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.special("#"),
                f.name("undef"),
                f.name("TEST"),
                f.whitespace(" "),
                f.name("unexpected")
            )))
            .throwsException(CodeException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.special("#"),
                f.name("undef"),
                f.name("TEST"),
                f.name("unexpected"),
                f.whitespace(" ")
            )))
            .throwsException(CodeException.class);
    }
}
