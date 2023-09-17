package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.test.*;
import cz.mg.collections.list.List;

public @Test class DefineDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + DefineDirectiveParserTest.class.getSimpleName() + " ... ");

        DefineDirectiveParserTest test = new DefineDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final DefineDirectiveParser parser = DefineDirectiveParser.getInstance();
    private final DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final TokenMutator mutator = TokenMutator.getInstance();
    private final TokenValidator tokenValidator = TokenValidator.getInstance();
    private final TokenFactory f = TokenFactory.getInstance();
    private final MacroValidator macroValidator = MacroValidator.getInstance();

    private void testParse() {
        parserValidator.validate(DefineDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.special("#"), f.name("define"), f.name("TEST")),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.name("define"), directive.getKeyword());
                macroValidator.assertEquals(new Macro(f.name("TEST"), null, new List<>()), directive.getMacro());
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.special("#"),
                f.whitespace(" "),
                f.name("define"),
                f.whitespace(" "),
                f.name("TEST"),
                f.whitespace(" ")
            ),
            new List<>(1, 3, 5),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenValidator.assertEquals(f.name("define"), directive.getKeyword());
                macroValidator.assertEquals(
                    new Macro(f.name("TEST"), null, new List<>(f.whitespace(" "))), directive.getMacro()
                );
            }
        );
    }
}
