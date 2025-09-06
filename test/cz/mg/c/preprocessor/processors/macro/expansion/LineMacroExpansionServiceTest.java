package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.entities.macro.MacroCall;
import cz.mg.c.entities.macro.Macros;
import cz.mg.c.entities.macro.system.FileMacro;
import cz.mg.c.entities.macro.system.LineMacro;
import cz.mg.c.preprocessor.processors.macro.components.MacroManager;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.token.Token;
import cz.mg.token.test.TokenAssert;
import cz.mg.token.tokens.NumberToken;
import cz.mg.token.tokens.WordToken;

import java.nio.file.Path;

public @Test class LineMacroExpansionServiceTest {
    public static void main(String[] args) {
        System.out.print("Running " + LineMacroExpansionServiceTest.class.getSimpleName() + " ... ");

        LineMacroExpansionServiceTest test = new LineMacroExpansionServiceTest();
        test.testExpand();

        System.out.println("OK");
    }

    private final @Service LineMacroExpansionService service = LineMacroExpansionService.getInstance();

    private void testExpand() {
        File file = new File(
            Path.of("test"),
            "int main(int argc, char** argv) {\n" +
                "\tprintf(\"%i\", __LINE__);" +
                "\treturn 0;\n" +
                "}"
        );
        MacroManager macros = new MacroManager(new Macros());
        LineMacro lineMacro = new LineMacro();
        FileMacro fileMacro = new FileMacro(file);
        macros.define(lineMacro);
        macros.define(fileMacro);
        MacroCall call = new MacroCall(lineMacro, new WordToken("__LINE__", 48), new List<>());
        List<Token> actualTokens = service.expand(macros, call);
        List<Token> expectedTokens = new List<>(new NumberToken("2", 48));
        TokenAssert.assertEquals(expectedTokens, actualTokens);
    }
}