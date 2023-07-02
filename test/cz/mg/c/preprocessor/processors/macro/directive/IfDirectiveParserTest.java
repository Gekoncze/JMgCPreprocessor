package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenMutator;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;

public @Test class IfDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + IfDirectiveParserTest.class.getSimpleName() + " ... ");

        IfDirectiveParserTest test = new IfDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final IfDirectiveParser parser = IfDirectiveParser.getInstance();
    private final DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final TokenMutator mutator = TokenMutator.getInstance();
    private final TokenValidator tokenValidator = TokenValidator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(IfDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.special("#"), f.name("if"), f.number("1"), f.operator(">"), f.number("2")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.name("if"), directive.getKeyword());
                tokenValidator.assertEquals(
                    new List<>(f.number("1"), f.operator(">"), f.number("2")),
                    directive.getExpression()
                );
            }
        );
    }
}
