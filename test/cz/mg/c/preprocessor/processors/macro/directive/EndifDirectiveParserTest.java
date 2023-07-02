package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenMutator;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.exceptions.CodeException;

public @Test class EndifDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + EndifDirectiveParserTest.class.getSimpleName() + " ... ");

        EndifDirectiveParserTest test = new EndifDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final EndifDirectiveParser parser = EndifDirectiveParser.getInstance();
    private final DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final TokenMutator mutator = TokenMutator.getInstance();
    private final TokenValidator tokenValidator = TokenValidator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(EndifDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.special("#"), f.name("endif")),
            new List<>(0, 1),
            tokens -> parser.parse(tokens),
            directive -> tokenValidator.assertEquals(f.name("endif"), directive.getKeyword())
        );

        Assert
            .assertThatCode(() -> parser.parse(new List<>(f.special("#"), f.name("endif"), f.name("unexpected"))))
            .throwsException(CodeException.class);
    }
}
