package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.*;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.exceptions.CodeException;

public @Test class IfdefDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + IfdefDirectiveParserTest.class.getSimpleName() + " ... ");

        IfdefDirectiveParserTest test = new IfdefDirectiveParserTest();
        test.testParse();
        test.testUnexpectedTrailingTokens();

        System.out.println("OK");
    }

    private final IfdefDirectiveParser parser = IfdefDirectiveParser.getInstance();
    private final DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final TokenMutator mutator = TokenMutator.getInstance();
    private final TokenValidator tokenValidator = TokenValidator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(IfdefDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.special("#"), f.name("ifdef"), f.name("TEST")),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.name("ifdef"), directive.getKeyword());
                tokenValidator.assertEquals(f.name("TEST"), directive.getName());
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.special("#"),
                f.whitespace(" "),
                f.name("ifdef"),
                f.whitespace(" "),
                f.name("TEST"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3, 4, 5, 6),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.name("ifdef"), directive.getKeyword());
                tokenValidator.assertEquals(f.name("TEST"), directive.getName());
            }
        );
    }

    private void testUnexpectedTrailingTokens() {
        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.special("#"),
                f.name("ifdef"),
                f.name("TEST"),
                f.whitespace(" ")
            )))
            .doesNotThrowAnyException();

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.special("#"),
                f.name("ifdef"),
                f.name("TEST"),
                f.name("unexpected")
            )))
            .throwsException(CodeException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.special("#"),
                f.name("ifdef"),
                f.name("TEST"),
                f.whitespace(" "),
                f.name("unexpected")
            )))
            .throwsException(CodeException.class);

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.special("#"),
                f.name("ifdef"),
                f.name("TEST"),
                f.name("unexpected"),
                f.whitespace(" ")
            )))
            .throwsException(CodeException.class);
    }
}
