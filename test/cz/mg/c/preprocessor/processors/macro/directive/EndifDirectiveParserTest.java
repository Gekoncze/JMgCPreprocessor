package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.exceptions.TraceableException;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenMutator;
import cz.mg.tokenizer.test.TokenValidator;

public @Test class EndifDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + EndifDirectiveParserTest.class.getSimpleName() + " ... ");

        EndifDirectiveParserTest test = new EndifDirectiveParserTest();
        test.testParse();
        test.testUnexpectedTrailingTokens();

        System.out.println("OK");
    }

    private final @Service EndifDirectiveParser parser = EndifDirectiveParser.getInstance();
    private final @Service DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenValidator tokenValidator = TokenValidator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(EndifDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.symbol("#"), f.word("endif")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> tokenValidator.assertEquals(f.word("endif"), directive.getKeyword())
        );

        mutator.mutate(
            new List<>(f.whitespace(" "), f.symbol("#"), f.whitespace(" "), f.word("endif"), f.whitespace(" ")),
            new List<>(0, 1, 2, 3),
            tokens -> parser.parse(tokens),
            directive -> tokenValidator.assertEquals(f.word("endif"), directive.getKeyword())
        );
    }

    private void testUnexpectedTrailingTokens() {
        Assert
            .assertThatCode(() -> parser.parse(new List<>(f.symbol("#"), f.word("endif"), f.whitespace(" "))))
            .doesNotThrowAnyException();

        Assert
            .assertThatCode(() -> parser.parse(new List<>(f.symbol("#"), f.word("endif"), f.word("unexpected"))))
            .throwsException(TraceableException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.symbol("#"),
                f.word("endif"),
                f.whitespace(" "),
                f.word("unexpected")
            )))
            .throwsException(TraceableException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.symbol("#"),
                f.word("endif"),
                f.word("unexpected"),
                f.whitespace(" ")
            )))
            .throwsException(TraceableException.class);
    }
}
