package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenMutator;
import cz.mg.tokenizer.test.TokenValidator;

public @Test class ElifDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + ElifDirectiveParserTest.class.getSimpleName() + " ... ");

        ElifDirectiveParserTest test = new ElifDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final @Service ElifDirectiveParser parser = ElifDirectiveParser.getInstance();
    private final @Service DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenValidator tokenValidator = TokenValidator.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(ElifDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.special("#"), f.name("elif"), f.number("1"), f.operator("<"), f.number("2")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.name("elif"), directive.getKeyword());
                tokenValidator.assertEquals(
                    new List<>(f.number("1"), f.operator("<"), f.number("2")),
                    directive.getExpression()
                );
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.special("#"),
                f.whitespace(" "),
                f.name("elif"),
                f.whitespace(" "),
                f.number("1"),
                f.whitespace(" "),
                f.operator("<"),
                f.whitespace(" "),
                f.number("2"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.name("elif"), directive.getKeyword());
                tokenValidator.assertEquals(
                    new List<>(
                        f.whitespace(" "),
                        f.number("1"),
                        f.whitespace(" "),
                        f.operator("<"),
                        f.whitespace(" "),
                        f.number("2"),
                        f.whitespace(" ")
                    ),
                    directive.getExpression()
                );
            }
        );
    }
}
