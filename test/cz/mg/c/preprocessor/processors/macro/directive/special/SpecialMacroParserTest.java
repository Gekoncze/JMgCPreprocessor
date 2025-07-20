package cz.mg.c.preprocessor.processors.macro.directive.special;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.preprocessor.test.MacroAssertions;
import cz.mg.collections.list.List;
import cz.mg.token.tokens.SymbolToken;
import cz.mg.token.tokens.WordToken;
import cz.mg.token.test.TokenMutator;

public @Test class SpecialMacroParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + SpecialMacroParserTest.class.getSimpleName() + " ... ");

        SpecialMacroParserTest test = new SpecialMacroParserTest();
        test.testConcatenation();

        System.out.println("OK");
    }

    private final @Service SpecialMacroParser parser = SpecialMacroParser.getInstance();
    private final @Service MacroAssertions assertions = MacroAssertions.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();

    private void testConcatenation() {
        mutator.mutate(
            new List<>(
                new SymbolToken("#", 0),
                new WordToken("define", 1),
                new WordToken("TEST", 10),
                new WordToken("foo", 15),
                new SymbolToken("##", 19),
                new WordToken("bar", 25)
            ),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            macro -> assertions.assertEquals(
                new Macro(new WordToken("TEST", 10), null, new List<>(new WordToken("foobar", 15))),
                macro
            )
        );
    }
}