package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenMutator;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;

public @Test class WarningDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + WarningDirectiveParserTest.class.getSimpleName() + " ... ");

        WarningDirectiveParserTest test = new WarningDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final WarningDirectiveParser parser = WarningDirectiveParser.getInstance();
    private final DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final TokenMutator mutator = TokenMutator.getInstance();
    private final TokenValidator tokenValidator = TokenValidator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(WarningDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.special("#"), f.name("warning"), f.name("doko"), f.name("doko"), f.name("doko")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> tokenValidator.assertEquals(f.name("warning"), directive.getKeyword())
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.special("#"),
                f.whitespace(" "),
                f.name("warning"),
                f.whitespace(" "),
                f.name("doko"),
                f.whitespace(" "),
                f.name("doko"),
                f.whitespace(" "),
                f.name("doko"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3),
            tokens -> parser.parse(tokens),
            directive -> tokenValidator.assertEquals(f.name("warning"), directive.getKeyword())
        );
    }
}
