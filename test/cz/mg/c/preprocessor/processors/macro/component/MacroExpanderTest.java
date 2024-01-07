package cz.mg.c.preprocessor.processors.macro.component;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.components.MacroExpander;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.entities.macro.Macros;
import cz.mg.collections.list.List;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.exceptions.CodeException;
import cz.mg.tokenizer.test.TokenFactory;
import cz.mg.tokenizer.test.TokenValidator;

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
    private final @Service TokenValidator validator = TokenValidator.getInstance();

    private void testNoMacroNoExpansionNoTokens() {
        List<Token> actualTokens = MacroExpander.expand(new List<>(), new MacroManager(new Macros()));
        List<Token> expectedTokens = new List<>();
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testNoMacroNoExpansion() {
        List<Token> line = new List<>(f.word("bar"));
        List<Token> actualTokens = MacroExpander.expand(line, new MacroManager(new Macros()));
        List<Token> expectedTokens = new List<>(f.word("bar"));
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testNoExpansion() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), new List<>(), new List<>(f.word("foo"))));
        List<Token> line = new List<>(f.word("bar"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(f.word("bar"));
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testNoExpansionBrackets() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), new List<>(), new List<>(f.word("foobar"))));
        List<Token> line = new List<>(f.word("FOO"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(f.word("FOO"));
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testSimpleExpansion() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), new List<>(), new List<>(f.word("foo"))));
        List<Token> line = new List<>(f.word("FOO"), f.bracket("("), f.bracket(")"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(f.word("foo"));
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testSimpleExpansionWithMoreTokens() {
        Macro foobar = new Macro(
            f.word("FOOBAR"),
            new List<>(),
            new List<>(f.word("foo"), f.operator("+"), f.word("bar"))
        );
        MacroManager macros = new MacroManager(new Macros());
        macros.define(foobar);
        List<Token> line = new List<>(
            f.whitespace("\t"), f.word("FOOBAR"), f.bracket("("), f.bracket(")"), f.separator(";")
        );
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(
            f.whitespace("\t"), f.word("foo"), f.operator("+"), f.word("bar"), f.separator(";")
        );
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testNoParametersAndNoImplementation() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), null, new List<>()));
        List<Token> line = new List<>(f.word("FOO"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>();
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testParametersAndNoImplementation() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), new List<>(), new List<>()));
        List<Token> line = new List<>(f.word("FOO"), f.bracket("("), f.bracket(")"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>();
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testParametersAndNoImplementationWithSpaces() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), new List<>(), new List<>()));
        List<Token> line = new List<>(f.word("FOO"), f.whitespace(" "), f.bracket("("), f.bracket(")"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>();
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testNoParametersAndImplementation() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), null, new List<>(f.word("_foo_"))));
        List<Token> line = new List<>(f.word("FOO"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(f.word("_foo_"));
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testParametersAndImplementation() {
        Macro foobar = new Macro(
            f.word("PLUS"),
            new List<>(f.word("x"), f.word("y")),
            new List<>(f.word("x"), f.operator("+"), f.word("y"))
        );
        MacroManager macros = new MacroManager(new Macros());
        macros.define(foobar);
        List<Token> line = new List<>(
            f.whitespace("\t"),
            f.word("PLUS"),
            f.bracket("("),
            f.word("foo"),
            f.separator(","),
            f.word("bar"),
            f.bracket(")"),
            f.separator(";")
        );
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(
            f.whitespace("\t"), f.word("foo"), f.operator("+"), f.word("bar"), f.separator(";")
        );
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testBracketNesting() {
        MacroManager macros = new MacroManager(new Macros());
        macros.define(new Macro(f.word("FOO"), new List<>(f.word("x")), new List<>(f.word("x"))));
        List<Token> line = new List<>(
            f.word("FOO"),
            f.bracket("("),
            f.word("print"),
            f.bracket("("),
            f.word("factorial"),
            f.bracket("("),
            f.number("5"),
            f.bracket(")"),
            f.bracket(")"),
            f.bracket(")")
        );
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(
            f.word("print"),
            f.bracket("("),
            f.word("factorial"),
            f.bracket("("),
            f.number("5"),
            f.bracket(")"),
            f.bracket(")")
        );
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testRecursiveExpansion() {
        Macro foo = new Macro(
            f.word("FOO"),
            new List<>(),
            new List<>(f.special("*"), f.word("BAR"), f.bracket("("), f.bracket(")"))
        );
        Macro bar = new Macro(
            f.word("BAR"),
            new List<>(),
            new List<>(f.special("*"), f.word("FOO"), f.bracket("("), f.bracket(")"))
        );
        MacroManager macros = new MacroManager(new Macros());
        macros.define(foo);
        macros.define(bar);
        List<Token> line = new List<>(f.word("FOO"), f.bracket("("), f.bracket(")"));
        List<Token> actualTokens = MacroExpander.expand(line, macros);
        List<Token> expectedTokens = new List<>(
            f.special("*"), f.special("*"), f.word("FOO"), f.bracket("("), f.bracket(")")
        );
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testMissingRightParenthesis() {
        Assert.assertThatCode(() -> {
            MacroManager macros = new MacroManager(new Macros());
            macros.define(new Macro(f.word("FOO"), new List<>(), new List<>()));
            List<Token> line = new List<>(f.word("FOO"), f.bracket("("));
            MacroExpander.expand(line, macros);
        }).throwsException(CodeException.class);
    }
}
