package cz.mg.c.preprocessor.processors.macro.directive;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.preprocessor.test.*;
import cz.mg.collections.list.List;
import cz.mg.token.test.TokenFactory;
import cz.mg.token.test.TokenMutator;
import cz.mg.token.test.TokenAssertions;

public @Test class DefineDirectiveParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + DefineDirectiveParserTest.class.getSimpleName() + " ... ");

        DefineDirectiveParserTest test = new DefineDirectiveParserTest();
        test.testParse();

        System.out.println("OK");
    }

    private final @Service DefineDirectiveParser parser = DefineDirectiveParser.getInstance();
    private final @Service DirectiveParserValidator parserValidator = DirectiveParserValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();
    private final @Service TokenAssertions tokenAssertions = TokenAssertions.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();
    private final @Service MacroAssertions macroAssertions = MacroAssertions.getInstance();

    private void testParse() {
        parserValidator.validate(DefineDirectiveParser.getInstance());

        mutator.mutate(
            new List<>(f.symbol("#"), f.word("define"), f.word("TEST")),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("define"), directive.getKeyword());
                macroAssertions.assertEquals(new Macro(f.word("TEST"), null, new List<>()), directive.getMacro());
            }
        );

        mutator.mutate(
            new List<>(
                f.whitespace(" "),
                f.symbol("#"),
                f.whitespace(" "),
                f.word("define"),
                f.whitespace(" "),
                f.word("TEST"),
                f.whitespace(" ")
            ),
            new List<>(0, 1, 2, 3, 4, 5),
            tokens -> parser.parse(tokens),
            directive -> {
                tokenAssertions.assertEquals(f.word("define"), directive.getKeyword());
                macroAssertions.assertEquals(
                    new Macro(f.word("TEST"), null, new List<>(f.whitespace(" "))), directive.getMacro()
                );
            }
        );
    }
}
