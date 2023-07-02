package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.DirectiveParserValidator;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenMutator;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.exceptions.CodeException;

public @Test class UndefDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + UndefDirectiveParserTest.class.getSimpleName() + " ... ");

        UndefDirectiveParserTest test = new UndefDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final UndefDirectiveParser parser = UndefDirectiveParser.getInstance();
    private final DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final TokenMutator mutator = TokenMutator.getInstance();
    private final TokenValidator tokenValidator = TokenValidator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();

    private void testParse() {
        parserValidator.validate(UndefDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.special("#"), f.name("undef"), f.name("TEST")),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.name("undef"), directive.getKeyword());
                tokenValidator.assertEquals(f.name("TEST"), directive.getName());
            }
        );

        Assert
            .assertThatCode(() -> parser.parse(new List<>(
                f.special("#"),
                f.name("undef"),
                f.name("TEST"),
                f.name("unexpected")
            )))
            .throwsException(CodeException.class);
    }
}
