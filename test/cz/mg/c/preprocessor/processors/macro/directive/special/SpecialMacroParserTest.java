package cz.mg.c.preprocessor.processors.macro.directive.special;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.test.MacroValidator;
import cz.mg.collections.list.List;
import cz.mg.tokenizer.entities.tokens.NameToken;
import cz.mg.tokenizer.entities.tokens.SpecialToken;
import cz.mg.tokenizer.test.TokenMutator;

public @Test class SpecialMacroParserTest {
    public static void main(String[] args) {
        System.out.print("Running " + SpecialMacroParserTest.class.getSimpleName() + " ... ");

        SpecialMacroParserTest test = new SpecialMacroParserTest();
        test.testConcatenation();

        System.out.println("OK");
    }

    private final @Service SpecialMacroParser parser = SpecialMacroParser.getInstance();
    private final @Service MacroValidator validator = MacroValidator.getInstance();
    private final @Service TokenMutator mutator = TokenMutator.getInstance();

    private void testConcatenation() {
        mutator.mutate(
            new List<>(
                new SpecialToken("#", 0),
                new NameToken("define", 1),
                new NameToken("TEST", 10),
                new NameToken("foo", 15),
                new SpecialToken("#", 19),
                new SpecialToken("#", 20),
                new NameToken("bar", 25)
            ),
            new List<>(0, 1, 2),
            tokens -> parser.parse(tokens),
            macro -> validator.assertEquals(
                new Macro(new NameToken("TEST", 10), null, new List<>(new NameToken("foobar", 15))),
                macro
            )
        );
    }
}
