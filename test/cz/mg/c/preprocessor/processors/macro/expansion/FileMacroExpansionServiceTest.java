package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.c.entities.macro.MacroCall;
import cz.mg.c.entities.macro.Macros;
import cz.mg.c.entities.macro.system.FileMacro;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.entities.tokens.DoubleQuoteToken;
import cz.mg.tokenizer.entities.tokens.WordToken;
import cz.mg.tokenizer.test.TokenValidator;

import java.nio.file.Path;

public @Test class FileMacroExpansionServiceTest {
    public static void main(String[] args) {
        System.out.print("Running " + FileMacroExpansionServiceTest.class.getSimpleName() + " ... ");

        FileMacroExpansionServiceTest test = new FileMacroExpansionServiceTest();
        test.testExpand();

        System.out.println("OK");
    }

    private final @Service FileMacroExpansionService service = FileMacroExpansionService.getInstance();
    private final @Service TokenValidator validator = TokenValidator.getInstance();

    private void testExpand() {
        File file = new File(
            Path.of("test"),
            "int main(int argc, char** argv) {\n" +
                "\tprintf(\"%s\", __FILE__);" +
                "\treturn 0;\n" +
                "}"
        );
        MacroManager macros = new MacroManager(new Macros());
        FileMacro fileMacro = new FileMacro(file);
        macros.define(fileMacro);
        MacroCall call = new MacroCall(fileMacro, new WordToken("__FILE__", 48), new List<>());
        List<Token> actualTokens = service.expand(macros, call);
        List<Token> expectedTokens = new List<>(new DoubleQuoteToken(file.getPath().toAbsolutePath().toString(), 48));
        validator.assertEquals(expectedTokens, actualTokens);
    }
}
