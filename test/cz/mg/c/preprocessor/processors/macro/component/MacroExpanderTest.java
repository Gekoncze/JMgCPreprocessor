package cz.mg.c.preprocessor.processors.macro.component;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.entities.macro.Macros;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpander;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.collections.list.List;
import cz.mg.test.Assertions;
import cz.mg.token.Token;
import cz.mg.token.test.TokenAssert;
import cz.mg.token.test.TokenFactory;
import cz.mg.tokenizer.exceptions.TraceableException;

public @Test class MacroExpanderTest {
    public static void main(String[] args) {
        System.out.print("Running " + MacroExpanderTest.class.getSimpleName() + " ... ");

        MacroExpanderTest test = new MacroExpanderTest();
        test.testNoMacroNoExpansionNoTokens();
        test.testNoMacroNoExpansion();
        test.testNoExpansion();
        test.testNoExpansionBrackets();
        test.testSimpleExpansion();
        test.testSimpleExpansionWithMoreTokens();
        test.testNoParametersAndNoImplementation();
        test.testParametersAndNoImplementation();
        test.testParametersAndNoImplementationWithSpaces();
        test.testNoParametersAndImplementation();
        test.testParametersAndImplementation();
        test.testBracketNesting();
        test.testRecursiveExpansion();
        test.testMissingRightParenthesis();

        System.out.println("OK");
    }

    private final @Service TokenFactory f = TokenFactory.getInstance();

    private void testNoMacroNoExpansionNoTokens() {
        List<Token> actualTokens = MacroExpander.expand(new List<>(), new MacroManager(new Macros()));
        List<Token> expectedTokens = new List<>();
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testNoMacroNoExpansion() {
        List<Token> line = new List<>(f.word("bar"));
        List<Token> actualTokens = MacroExpander.expand(line, new MacroManager(new Macros()));
        List<Token> expectedTokens = new List<>(f.word("bar"));
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testNoExpansion() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), new List<>(), new List<>(f.word("foo"))));
        List<Token> line = new List<>(f.word("bar"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(f.word("bar"));
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testNoExpansionBrackets() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), new List<>(), new List<>(f.word("foobar"))));
        List<Token> line = new List<>(f.word("FOO"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(f.word("FOO"));
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testSimpleExpansion() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), new List<>(), new List<>(f.word("foo"))));
        List<Token> line = new List<>(f.word("FOO"), f.symbol("("), f.symbol(")"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(f.word("foo"));
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testSimpleExpansionWithMoreTokens() {
        Macro foobar = new Macro(
            f.word("FOOBAR"),
            new List<>(),
            new List<>(f.word("foo"), f.symbol("+"), f.word("bar"))
        );
        MacroManager macros = new MacroManager(new Macros());
        macros.define(foobar);
        List<Token> line = new List<>(
            f.whitespace("\t"), f.word("FOOBAR"), f.symbol("("), f.symbol(")"), f.symbol(";")
        );
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(
            f.whitespace("\t"), f.word("foo"), f.symbol("+"), f.word("bar"), f.symbol(";")
        );
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testNoParametersAndNoImplementation() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), null, new List<>()));
        List<Token> line = new List<>(f.word("FOO"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>();
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testParametersAndNoImplementation() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), new List<>(), new List<>()));
        List<Token> line = new List<>(f.word("FOO"), f.symbol("("), f.symbol(")"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>();
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testParametersAndNoImplementationWithSpaces() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), new List<>(), new List<>()));
        List<Token> line = new List<>(f.word("FOO"), f.whitespace(" "), f.symbol("("), f.symbol(")"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>();
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testNoParametersAndImplementation() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), null, new List<>(f.word("_foo_"))));
        List<Token> line = new List<>(f.word("FOO"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(f.word("_foo_"));
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testParametersAndImplementation() {
        Macro foobar = new Macro(
            f.word("PLUS"),
            new List<>(f.word("x"), f.word("y")),
            new List<>(f.word("x"), f.symbol("+"), f.word("y"))
        );
        MacroManager macros = new MacroManager(new Macros());
        macros.define(foobar);
        List<Token> line = new List<>(
            f.whitespace("\t"),
            f.word("PLUS"),
            f.symbol("("),
            f.word("foo"),
            f.symbol(","),
            f.word("bar"),
            f.symbol(")"),
            f.symbol(";")
        );
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(
            f.whitespace("\t"), f.word("foo"), f.symbol("+"), f.word("bar"), f.symbol(";")
        );
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testBracketNesting() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), new List<>(f.word("x")), new List<>(f.word("x"))));
        List<Token> line = new List<>(
            f.word("FOO"),
            f.symbol("("),
            f.word("print"),
            f.symbol("("),
            f.word("factorial"),
            f.symbol("("),
            f.number("5"),
            f.symbol(")"),
            f.symbol(")"),
            f.symbol(")")
        );
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(
            f.word("print"),
            f.symbol("("),
            f.word("factorial"),
            f.symbol("("),
            f.number("5"),
            f.symbol(")"),
            f.symbol(")")
        );
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testRecursiveExpansion() {
        Macro foo = new Macro(
            f.word("FOO"),
            new List<>(),
            new List<>(f.symbol("*"), f.word("BAR"), f.symbol("("), f.symbol(")"))
        );
        Macro bar = new Macro(
            f.word("BAR"),
            new List<>(),
            new List<>(f.symbol("*"), f.word("FOO"), f.symbol("("), f.symbol(")"))
        );
        MacroManager macros = new MacroManager(new Macros());
        macros.define(foo);
        macros.define(bar);
        List<Token> line = new List<>(f.word("FOO"), f.symbol("("), f.symbol(")"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(
            f.symbol("*"), f.symbol("*"), f.word("FOO"), f.symbol("("), f.symbol(")")
        );
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }

    private void testMissingRightParenthesis() {
        Assertions.assertThatCode(() -> {
            MacroManager macros = new MacroManager(new Macros());
            macros.define(new Macro(f.word("FOO"), new List<>(), new List<>()));
            List<Token> line = new List<>(f.word("FOO"), f.symbol("("));
            MacroExpander.expand(line, macros);
        }).throwsException(TraceableException.class);
    }
}