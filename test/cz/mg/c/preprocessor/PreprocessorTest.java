package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.entities.macro.MacroCall;
import cz.mg.c.entities.macro.Macros;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.preprocessor.test.MacroFactory;
import cz.mg.c.preprocessor.test.MacroValidator;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.*;
import cz.mg.tokenizer.exceptions.TraceableException;
import cz.mg.tokenizer.services.UserExceptionFactory;
import cz.mg.tokenizer.test.TokenValidator;

import java.nio.file.Path;

public @Test class PreprocessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + PreprocessorTest.class.getSimpleName() + " ... ");

        PreprocessorTest test = new PreprocessorTest();
        test.testProcessing();
        test.testNestedMacros();
        test.testNestedConditions();
        test.testWhitespaces();
        test.testExpressions();

        System.out.println("OK");
    }

    private final @Service Preprocessor preprocessor = Preprocessor.getInstance();
    private final @Service TokenValidator tokenValidator = TokenValidator.getInstance();
    private final @Service MacroValidator macroValidator = MacroValidator.getInstance();
    private final @Service UserExceptionFactory userExceptionFactory = UserExceptionFactory.getInstance();
    private final @Service MacroFactory m = MacroFactory.getInstance();

    private void testProcessing() {
        File file = new File(
            Path.of("/test/file/main.c"),
            "#include <stdio.h>\n" +
            "\n" +
            "#define PLUS(x, y) \\\n" +
            "    x + y\n" +
            "\n" +
            "int main() {\n" +
            "    printf(\n" +
            "        \"%s at line %i: %i\\n\",\n" +
            "        __FILE__, __LINE__, PLUS(7, 3)\n" +
            "    );\n" +
            "}"
        );

        Macros macros = new Macros();
        List<Token> tokens = preprocessor.preprocess(file, macros);

        Assert.assertEquals(1, macros.getDefinitions().count());
        macroValidator.assertEquals(
            new Macro(
                new WordToken("PLUS", 28),
                new List<>(new WordToken("x", 33), new WordToken("y", 36)),
                new List<>(new WordToken("x", 45), new OperatorToken("+", 47), new WordToken("y", 49))
            ),
            macros.getDefinitions().getFirst()
        );

        tokenValidator.assertEquals(
            new List<>(
                new WordToken("int", 52),
                new WordToken("main", 56),
                new BracketToken("(", 60),
                new BracketToken(")", 61),
                new BracketToken("{", 63),
                new WordToken("printf", 69),
                new BracketToken("(", 75),
                new DoubleQuoteToken("%s at line %i: %i\\n", 85),
                new SeparatorToken(",", 106),
                new DoubleQuoteToken("/test/file/main.c", 116),
                new SeparatorToken(",", 124),
                new NumberToken("9", 126),
                new SeparatorToken(",", 134),
                new NumberToken("7", 141),
                new OperatorToken("+", 47),
                new NumberToken("3", 144),
                new BracketToken(")", 151),
                new SeparatorToken(";", 152),
                new BracketToken("}", 154)
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
            "#define OPERATION(x, o, y) x o y\n" +
            "#define MINUS(x, y) OPERATION(x, -, y)\n" +
            "#define OPERATION(x, o, y) y o x\n" +
            "MINUS(2, 7)"
        );

        Macros macros = new Macros();
        List<Token> tokens = preprocessor.preprocess(file, macros);

        Assert.assertEquals(2, macros.getDefinitions().count());
        Assert.assertEquals("OPERATION", macros.getDefinitions().getFirst().getName().getText());
        Assert.assertEquals("MINUS", macros.getDefinitions().getLast().getName().getText());

        macroValidator.assertEquals(
            new Macro(
                new WordToken("MINUS", 41),
                new List<>(new WordToken("x", 47), new WordToken("y", 50)),
                new List<>(
                    new WordToken("OPERATION", 53),
                    new BracketToken("(", 62),
                    new WordToken("x", 63),
                    new SeparatorToken(",", 64),
                    new OperatorToken("-", 66),
                    new SeparatorToken(",", 67),
                    new WordToken("y", 69),
                    new BracketToken(")", 70)
                )
            ),
            macros.getDefinitions().getLast()
        );

        tokenValidator.assertEquals(
            new List<>(
                new NumberToken("7", 114),
                new OperatorToken("-", 66),
                new NumberToken("2", 111)
            ),
            tokens
        );
    }

    private void testNestedConditions() {
        File file = new File(
            Path.of("/test/file/main.c"),
            "0\n" +
            "#ifdef straw\n" +
            "1\n" +
            "#if defined(apple)\n" +
            "2\n" +
            "#define STRAWAPPLE\n" +
            "3\n" +
            "#elif defined(berry)\n" +
            "4\n" +
            "#define STRAWBERRY\n" +
            "5\n" +
            "#else\n" +
            "6\n" +
            "#define STRAW\n" +
            "7\n" +
            "#endif\n" +
            "8\n" +
            "#else\n" +
            "9\n" +
            "#define NONE\n" +
            "10\n" +
            "#endif\n" +
            "11\n"
        );

        Macros macros = new Macros();
        macros.getDefinitions().addLast(new Macro(new WordToken("straw", -1), null, new List<>()));
        macros.getDefinitions().addLast(new Macro(new WordToken("berry", -1), null, new List<>()));

        List<Token> tokens = preprocessor.preprocess(file, macros);

        MacroManager manager = new MacroManager(macros);
        Assert.assertEquals(true, manager.defined("straw"));
        Assert.assertEquals(true, manager.defined("berry"));
        Assert.assertEquals(true, manager.defined("STRAWBERRY"));
        Assert.assertEquals(false, manager.defined("apple"));
        Assert.assertEquals(false, manager.defined("STRAWAPPLE"));
        Assert.assertEquals(false, manager.defined("STRAW"));
        Assert.assertEquals(false, manager.defined("NONE"));

        tokenValidator.assertEquals(
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

        List<Token> tokens =  preprocessor.preprocess(file, macros);

        MacroManager manager = new MacroManager(macros);
        Assert.assertEquals(true, manager.defined("PLUS"));

        tokenValidator.assertEquals(
            new List<>(
                new NumberToken("7", 35),
                new OperatorToken("+", 22),
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
            "#ifndef FIRST_PRECONDITION\n" +
            "    #if (SECOND_PRECONDITION==1)\n" +
            "        #if (defined(__cplusplus) && (__cplusplus >= 201103L)) \\\n" +
            "            || (defined(_MSVC_LANG) && (_MSVC_LANG >= 201103L))\n" +
            "        #endif\n" +
            "    #endif\n" +
            "#endif"
        );

        Assert.assertThatCode(() -> wrap(file, () -> {
            preprocessor.preprocess(file, m.create());
        })).doesNotThrowAnyException();

        Assert.assertThatCode(() -> wrap(file, () -> {
            preprocessor.preprocess(file, m.create(m.create("FIRST_PRECONDITION")));
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
