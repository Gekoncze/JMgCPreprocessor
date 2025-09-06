package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assertions;
import cz.mg.token.test.TokenAssert;
import cz.mg.token.test.TokenFactory;
import cz.mg.token.test.TokenMutator;
import cz.mg.tokenizer.exceptions.TraceableException;

public @Test class IfdefDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + IfdefDirectiveParserTest.class.getSimpleName() + " ... ");

        IfdefDirectiveParserTest test = new IfdefDirectiveParserTest();
        test.testParse();
        test.testUnexpectedTrailingTokens();

        System.out.println("OK");
    }

    private final @Service IfdefDirectiveParser parser = IfdefDirectiveParser.getInstance();
    private final @Service DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(IfdefDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.symbol("#"), f.word("ifdef"), f.word("TEST")),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            directive -> {
                TokenAssert.assertEquals(f.word("ifdef"), directive.getKeyword());
                TokenAssert.assertEquals(f.word("TEST"), directive.getName());
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.symbol("#"),
                f.whitespace(" "),
                f.word("ifdef"),
                f.whitespace(" "),
                f.word("TEST"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3, 4, 5, 6),
            tokens -> parser.parse(tokens),
            directive -> {
                TokenAssert.assertEquals(f.word("ifdef"), directive.getKeyword());
                TokenAssert.assertEquals(f.word("TEST"), directive.getName());
            }
        );
    }

    private void testUnexpectedTrailingTokens() {
        Assertions
            .assertThatCode(() -> parser.parse(new List<>(
                f.symbol("#"),
                f.word("ifdef"),
                f.word("TEST"),
                f.whitespace(" ")
            )))
            .doesNotThrowAnyException();

        Assertions
            .assertThatCode(() -> parser.parse(new List<>(
                f.symbol("#"),
                f.word("ifdef"),
                f.word("TEST"),
                f.word("unexpected")
            )))
            .throwsException(TraceableException.class);

        Assertions
            .assertThatCode(() -> parser.parse(new List<>(
                f.symbol("#"),
                f.word("ifdef"),
                f.word("TEST"),
                f.whitespace(" "),
                f.word("unexpected")
            )))
            .throwsException(TraceableException.class);

        Assertions
            .assertThatCode(() -> parser.parse(new List<>(
                f.symbol("#"),
                f.word("ifdef"),
                f.word("TEST"),
                f.word("unexpected"),
                f.whitespace(" ")
            )))
            .throwsException(TraceableException.class);
    }
}
