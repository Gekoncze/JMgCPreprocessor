package cz.mg.c.preprocessor;

import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.test.MacroValidator;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.c.preprocessor.macro.entities.Macro;
import cz.mg.c.preprocessor.macro.entities.Macros;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.*;

import java.nio.file.Path;

public @Test class PreprocessorTest {
    public static void main(String[] args) {
        System.out.print("Running " + PreprocessorTest.class.getSimpleName() + " ... ");

        PreprocessorTest test = new PreprocessorTest();
        test.testProcessing();

        System.out.println("OK");
    }

    private final Preprocessor preprocessor = Preprocessor.getInstance();
    private final TokenValidator tokenValidator = TokenValidator.getInstance();
    private final MacroValidator macroValidator = MacroValidator.getInstance();

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

        Assert.assertEquals(true, macros.defined("PLUS"));
        macroValidator.assertEquals(
            new Macro(
                new NameToken("PLUS", 28),
                new List<>(new NameToken("x", 33), new NameToken("y", 36)),
                new List<>(new NameToken("x", 45 - 2), new OperatorToken("+", 47 - 2), new NameToken("y", 49 - 2))
            ),
            macros.getMap().get("PLUS")
        );

        tokenValidator.assertEquals(
            new List<>(
                new NameToken("int", 52 - 2),
                new NameToken("main", 56 - 2),
                new BracketToken("(", 60 - 2),
                new BracketToken(")", 61 - 2),
                new BracketToken("{", 63 - 2),
                new NameToken("printf", 69 - 2),
                new BracketToken("(", 75 - 2),
                new DoubleQuoteToken("%s at line %i: %i\\n", 85 - 2),
                new SeparatorToken(",", 106 - 2),
                new DoubleQuoteToken("/test/file/main.c", 116 - 2),
                new SeparatorToken(",", 124 - 2),
                new NumberToken("9", 126 - 2),
                new SeparatorToken(",", 134 - 2),
                new NumberToken("7", 141 - 2),
                new OperatorToken("+", 47 - 2),
                new NumberToken("3", 144 - 2),
                new BracketToken(")", 151 - 2),
                new SeparatorToken(";", 152 - 2),
                new BracketToken("}", 154 - 2)
            ),
            tokens
        );
    }
}
