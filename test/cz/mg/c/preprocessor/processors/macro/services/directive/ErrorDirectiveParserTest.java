package cz.mg.c.preprocessor.processors.macro.services.directive;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenMutator;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;

public @Test class ErrorDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + ErrorDirectiveParserTest.class.getSimpleName() + " ... ");

        ErrorDirectiveParserTest test = new ErrorDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final ErrorDirectiveParser parser = ErrorDirectiveParser.getInstance();
    private final DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final TokenMutator mutator = TokenMutator.getInstance();
    private final TokenValidator tokenValidator = TokenValidator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(ErrorDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.special("#"), f.name("error"), f.name("oi"), f.name("oi"), f.name("oi")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> tokenValidator.assertEquals(f.name("error"), directive.getToken())
        );
    }
}
