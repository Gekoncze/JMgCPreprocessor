package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.entities.macro.MacroCall;
import cz.mg.c.entities.macro.Macros;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.preprocessor.test.MacroFactory;
import cz.mg.c.preprocessor.test.MacroAssertions;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.test.Assert;
import cz.mg.token.Token;
import cz.mg.token.tokens.*;
import cz.mg.token.tokens.quote.DoubleQuoteToken;
import cz.mg.tokenizer.exceptions.TraceableException;
import cz.mg.tokenizer.services.UserExceptionFactory;
import cz.mg.token.test.TokenAssertions;

import java.nio.file.Path;

public @Test class CPreprocessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + CPreprocessorTest.class.getSimpleName() + " ... ");

        CPreprocessorTest test = new CPreprocessorTest();
        test.testProcessing();
        test.testNestedMacros();
        test.testNestedConditions();
        test.testWhitespaces();
        test.testExpressions();

        System.out.println("OK");
    }

    private final @Service TokenAssertions tokenAssertions = TokenAssertions.getInstance();
    private final @Service MacroAssertions macroAssertions = MacroAssertions.getInstance();
    private final @Service UserExceptionFactory userExceptionFactory = UserExceptionFactory.getInstance();
    private final @Service MacroFactory m = MacroFactory.getInstance();

    private void testProcessing() {
        File file = new File(
            Path.of("/test/file/main.c"),
            """
            #include <stdio.h>
            
            #define PLUS(x, y) \\
                x + y
            
            int main() {
                printf(
                    "%s at line %i: %i\\n",
                    __FILE__, __LINE__, PLUS(7, 3)
                );
            }"""
        );

        Macros macros = new Macros();
        CPreprocessor preprocessor = new CPreprocessor(macros);
        List<Token> tokens = preprocessor.preprocess(file);

        Assert.assertEquals(1, macros.getDefinitions().count());
        macroAssertions.assertEquals(
            new Macro(
                new WordToken("PLUS", 28),
                new List<>(new WordToken("x", 33), new WordToken("y", 36)),
                new List<>(new WordToken("x", 45), new SymbolToken("+", 47), new WordToken("y", 49))
            ),
            macros.getDefinitions().getFirst()
        );

        tokenAssertions.assertEquals(
            new List<>(
                new WordToken("int", 52),
                new WordToken("main", 56),
                new SymbolToken("(", 60),
                new SymbolToken(")", 61),
                new SymbolToken("{", 63),
                new WordToken("printf", 69),
                new SymbolToken("(", 75),
                new DoubleQuoteToken("%s at line %i: %i\n", 85),
                new SymbolToken(",", 106),
                new DoubleQuoteToken("/test/file/main.c", 116),
                new SymbolToken(",", 124),
                new NumberToken("9", 126),
                new SymbolToken(",", 134),
                new NumberToken("7", 141),
                new SymbolToken("+", 47),
                new NumberToken("3", 144),
                new SymbolToken(")", 151),
                new SymbolToken(";", 152),
                new SymbolToken("}", 154)
            ),
            tokens
        );

        Assert.assertEquals(3, macros.getCalls().count());
        MacroCall call = macros.getCalls().get(2);
        Assert.assertNotNull(call.getArguments());
        Assert.assertEquals(2, call.getArguments().count());
        Assert.assertEquals("PLUS", call.getToken().getText());
        Assert.assertEquals("7", call.getArguments().get(0).get(0).getText());
        Assert.assertEquals("3", call.getArguments().get(1).get(0).getText());
    }

    private void testNestedMacros() {
        File file = new File(
            Path.of("/test/file/main.c"),
            """
            #define OPERATION(x, o, y) x o y
            #define MINUS(x, y) OPERATION(x, -, y)
            #define OPERATION(x, o, y) y o x
            MINUS(2, 7)
            """
        );

        Macros macros = new Macros();
        CPreprocessor preprocessor = new CPreprocessor(macros);
        List<Token> tokens = preprocessor.preprocess(file);

        Assert.assertEquals(2, macros.getDefinitions().count());
        Assert.assertEquals("OPERATION", macros.getDefinitions().getFirst().getName().getText());
        Assert.assertEquals("MINUS", macros.getDefinitions().getLast().getName().getText());

        macroAssertions.assertEquals(
            new Macro(
                new WordToken("MINUS", 41),
                new List<>(new WordToken("x", 47), new WordToken("y", 50)),
                new List<>(
                    new WordToken("OPERATION", 53),
                    new SymbolToken("(", 62),
                    new WordToken("x", 63),
                    new SymbolToken(",", 64),
                    new SymbolToken("-", 66),
                    new SymbolToken(",", 67),
                    new WordToken("y", 69),
                    new SymbolToken(")", 70)
                )
            ),
            macros.getDefinitions().getLast()
        );

        tokenAssertions.assertEquals(
            new List<>(
                new NumberToken("7", 114),
                new SymbolToken("-", 66),
                new NumberToken("2", 111)
            ),
            tokens
        );
    }

    private void testNestedConditions() {
        File file = new File(
            Path.of("/test/file/main.c"),
            """
            0
            #ifdef straw
            1
            #if defined(apple)
            2
            #define STRAWAPPLE
            3
            #elif defined(berry)
            4
            #define STRAWBERRY
            5
            #else
            6
            #define STRAW
            7
            #endif
            8
            #else
            9
            #define NONE
            10
            #endif
            11
            """
        );

        Macros macros = new Macros();
        macros.getDefinitions().addLast(new Macro(new WordToken("straw", -1), null, new List<>()));
        macros.getDefinitions().addLast(new Macro(new WordToken("berry", -1), null, new List<>()));

        CPreprocessor preprocessor = new CPreprocessor(macros);
        List<Token> tokens = preprocessor.preprocess(file);

        MacroManager manager = new MacroManager(macros);
        Assert.assertEquals(true, manager.defined("straw"));
        Assert.assertEquals(true, manager.defined("berry"));
        Assert.assertEquals(true, manager.defined("STRAWBERRY"));
        Assert.assertEquals(false, manager.defined("apple"));
        Assert.assertEquals(false, manager.defined("STRAWAPPLE"));
        Assert.assertEquals(false, manager.defined("STRAW"));
        Assert.assertEquals(false, manager.defined("NONE"));

        tokenAssertions.assertEquals(
            new List<>(
                new NumberToken("0", 0),
                new NumberToken("1", 15),
                new NumberToken("4", 80),
                new NumberToken("5", 101),
                new NumberToken("8", 134),
                new NumberToken("11", 167)
            ),
            tokens
        );
    }

    private void testWhitespaces() {
        File file = new File(
            Path.of("/test/file/main.c"),
            " # define PLUS( a ) a + a \n" +
            " PLUS ( 7 ) "
        );

        Macros macros = new Macros();

        CPreprocessor preprocessor = new CPreprocessor(macros);
        List<Token> tokens =  preprocessor.preprocess(file);

        MacroManager manager = new MacroManager(macros);
        Assert.assertEquals(true, manager.defined("PLUS"));

        tokenAssertions.assertEquals(
            new List<>(
                new NumberToken("7", 35),
                new SymbolToken("+", 22),
                new NumberToken("7", 35)
            ),
            tokens
        );

        Assert.assertEquals(false, macros.getDefinitions().isEmpty());
        Macro macro = macros.getDefinitions().getFirst();
        Assert.assertEquals("PLUS", macro.getName().getText());
        Assert.assertNotNull(macro.getParameters());
        Assert.assertNotNull(macro.getTokens());
        Assert.assertEquals(1, macro.getParameters().count());
        Assert.assertEquals("a", macro.getParameters().getFirst().getText());

        Assert.assertEquals(false, macros.getCalls().isEmpty());
        MacroCall call = macros.getCalls().getFirst();
        Assert.assertSame(macro, call.getMacro());
        Assert.assertEquals("PLUS", call.getToken().getText());
        Assert.assertNotNull(call.getArguments());
        Assert.assertEquals(1, call.getArguments().count());
        Assert.assertEquals(1, call.getArguments().getFirst().count());
        Assert.assertEquals("7", call.getArguments().getFirst().getFirst().getText());
    }

    private void testExpressions() {
        File file = new File(
            Path.of("/test/file/main.c"),
            """
            #ifndef FIRST_PRECONDITION
                #if (SECOND_PRECONDITION==1)
                    #if (defined(__cplusplus) && (__cplusplus >= 201103L)) \\
                        || (defined(_MSVC_LANG) && (_MSVC_LANG >= 201103L))
                    #endif
                #endif
            #endif
            """
        );

        Assert.assertThatCode(() -> wrap(file, () -> {
            new CPreprocessor(m.create()).preprocess(file);
        })).doesNotThrowAnyException();

        Assert.assertThatCode(() -> wrap(file, () -> {
            new CPreprocessor(m.create(m.create("FIRST_PRECONDITION"))).preprocess(file);
        })).doesNotThrowAnyException();
    }

    private void wrap(@Mandatory File file, @Mandatory Runnable runnable) {
        try {
            runnable.run();
        } catch (TraceableException e) {
            throw userExceptionFactory.create(file.getPath(), file.getContent(), e);
        }
    }
}