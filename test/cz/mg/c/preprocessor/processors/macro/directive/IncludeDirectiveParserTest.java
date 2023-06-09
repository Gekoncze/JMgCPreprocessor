package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenMutator;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;

public @Test class IncludeDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + IncludeDirectiveParserTest.class.getSimpleName() + " ... ");

        IncludeDirectiveParserTest test = new IncludeDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final IncludeDirectiveParser parser = IncludeDirectiveParser.getInstance();
    private final DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final TokenMutator mutator = TokenMutator.getInstance();
    private final TokenValidator tokenValidator = TokenValidator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(IncludeDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.special("#"), f.name("include"), f.doubleQuote("stdio.h")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> tokenValidator.assertEquals(f.name("include"), directive.getKeyword())
        );
    }
}
