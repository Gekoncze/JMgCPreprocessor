package cz.mg.c.preprocessor.processors;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.entities.macro.Macros;
import cz.mg.collections.list.List;
import cz.mg.token.Token;
import cz.mg.token.test.TokenAssert;
import cz.mg.token.test.TokenFactory;

public @Test class WhitespaceProcessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + WhitespaceProcessorTest.class.getSimpleName() + " ... ");

        WhitespaceProcessorTest test = new WhitespaceProcessorTest();
        test.testProcessing();
        test.testMacroProcessing();

        System.out.println("OK");
    }

    private final @Service WhitespaceProcessor processor = WhitespaceProcessor.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testProcessing() {
        testProcessing(
            new List<>(),
            new List<>()
        );

        testProcessing(
            new List<>(f.whitespace(" ")),
            new List<>()
        );

        testProcessing(
            new List<>(f.word("a")),
            new List<>(f.word("a"))
        );

        testProcessing(
            new List<>(
                f.word("a"),
                f.whitespace(" "),
                f.number("11"),
                f.whitespace("\t"),
                f.symbol(";")
            ),
            new List<>(
                f.word("a"),
                f.number("11"),
                f.symbol(";")
            )
        );

        testProcessing(
            new List<>(
                f.whitespace("\t"),
                f.word("a"),
                f.whitespace(" ")
            ),
            new List<>(
                f.word("a")
            )
        );
    }

    private void testProcessing(@Mandatory List<Token> tokens, @Mandatory List<Token> result) {
        processor.process(tokens);
        TokenAssert.assertEquals(result, tokens);
    }

    private void testMacroProcessing() {
        testMacroProcessing(
            new Macro(f.word("FOO"), null, new List<>()),
            new List<>()
        );

        testMacroProcessing(
            new Macro(f.word("FOO"), null, new List<>(f.whitespace(" "))),
            new List<>()
        );

        testMacroProcessing(
            new Macro(
                f.word("FOO"),
                null,
                new List<>(f.whitespace(" "), f.word("x"), f.number("7"))
            ),
            new List<>(f.word("x"), f.number("7"))
        );

        testMacroProcessing(
            new Macro(
                f.word("FOO"),
                null,
                new List<>(f.word("x"), f.whitespace(" "), f.number("7"))
            ),
            new List<>(f.word("x"), f.number("7"))
        );

        testMacroProcessing(
            new Macro(
                f.word("FOO"),
                null,
                new List<>(f.word("x"), f.number("7"), f.whitespace(" "))
            ),
            new List<>(f.word("x"), f.number("7"))
        );
    }

    private void testMacroProcessing(@Mandatory Macro macro, @Mandatory List<Token> result) {
        Macros macros = new Macros();
        macros.getDefinitions().addLast(macro);
        processor.process(macros);
        TokenAssert.assertEquals(result, macro.getTokens());
    }
}