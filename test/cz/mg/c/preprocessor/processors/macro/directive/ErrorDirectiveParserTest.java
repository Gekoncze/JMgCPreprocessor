package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenMutator;
import cz.mg.tokenizer.test.TokenValidator;

public @Test class ErrorDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + ErrorDirectiveParserTest.class.getSimpleName() + " ... ");

        ErrorDirectiveParserTest test = new ErrorDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final @Service ErrorDirectiveParser parser = ErrorDirectiveParser.getInstance();
    private final @Service DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenValidator tokenValidator = TokenValidator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(ErrorDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.special("#"), f.name("error"), f.name("oi"), f.name("oi"), f.name("oi")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> tokenValidator.assertEquals(f.name("error"), directive.getKeyword())
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.special("#"),
                f.whitespace(" "),
                f.name("error"),
                f.whitespace(" "),
                f.name("oi"),
                f.whitespace(" "),
                f.name("oi"),
                f.whitespace(" "),
                f.name("oi"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3),
            tokens -> parser.parse(tokens),
            directive -> tokenValidator.assertEquals(f.name("error"), directive.getKeyword())
        );
    }
}
