package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.exceptions.TraceableException;
import cz.mg.token.test.TokenFactory;
import cz.mg.token.test.TokenMutator;
import cz.mg.token.test.TokenAssertions;

public @Test class IfndefDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + IfndefDirectiveParserTest.class.getSimpleName() + " ... ");

        IfndefDirectiveParserTest test = new IfndefDirectiveParserTest();
        test.testParse();
        test.testUnexpectedTrailingTokens();

        System.out.println("OK");
    }

    private final @Service IfndefDirectiveParser parser = IfndefDirectiveParser.getInstance();
    private final @Service DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenAssertions tokenAssertions = TokenAssertions.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(IfndefDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.symbol("#"), f.word("ifndef"), f.word("TEST")),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("ifndef"), directive.getKeyword());
                tokenAssertions.assertEquals(f.word("TEST"), directive.getName());
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.symbol("#"),
                f.whitespace(" "),
                f.word("ifndef"),
                f.whitespace(" "),
                f.word("TEST"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("ifndef"), directive.getKeyword());
                tokenAssertions.assertEquals(f.word("TEST"), directive.getName());
            }
        );
    }

    private void testUnexpectedTrailingTokens() {
        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.symbol("#"),
                f.word("ifndef"),
                f.word("TEST"),
                f.whitespace(" ")
            )))
            .doesNotThrowAnyException();

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.symbol("#"),
                f.word("ifndef"),
                f.word("TEST"),
                f.word("unexpected")
            )))
            .throwsException(TraceableException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.symbol("#"),
                f.word("ifndef"),
                f.word("TEST"),
                f.whitespace(""),
                f.word("unexpected")
            )))
            .throwsException(TraceableException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.symbol("#"),
                f.word("ifndef"),
                f.word("TEST"),
                f.word("unexpected"),
                f.whitespace(" ")
            )))
            .throwsException(TraceableException.class);
    }
}
