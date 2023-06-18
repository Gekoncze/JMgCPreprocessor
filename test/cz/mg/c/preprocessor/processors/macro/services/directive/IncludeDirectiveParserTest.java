package cz.mg.c.preprocessor.processors.macro.services.directive;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.entities.directives.IncludeDirective;
import cz.mg.c.preprocessor.test.*;
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
            tokens -> {
                IncludeDirective directive = parser.parse(tokens);
                tokenValidator.assertEquals(f.name("include"), directive.getToken());
            }
        );
    }
}
