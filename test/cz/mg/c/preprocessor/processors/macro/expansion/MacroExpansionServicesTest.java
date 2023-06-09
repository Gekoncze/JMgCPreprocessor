package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.classes.Test;
import cz.mg.c.preprocessor.processors.macro.entities.Macro;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.c.preprocessor.processors.macro.entities.system.FileMacro;
import cz.mg.c.preprocessor.test.TokenFactory;
import cz.mg.c.preprocessor.test.TokenValidator;
import cz.mg.collections.list.List;
import cz.mg.file.File;
import cz.mg.test.Assert;
import cz.mg.tokenizer.entities.Token;
import cz.mg.tokenizer.exceptions.CodeException;

import java.nio.file.Path;

public @Test class MacroExpansionServicesTest {
    public static void main(String[] args) {
        System.out.print("Running " + MacroExpansionServicesTest.class.getSimpleName() + " ... ");

        MacroExpansionServicesTest test = new MacroExpansionServicesTest();
        test.testExpandPlainMacro();
        test.testExpandSystemMacro();
        test.testValidation();

        System.out.println("OK");
    }

    private final @Service MacroExpansionServices services = MacroExpansionServices.getInstance();
    private final @Service TokenFactory f = TokenFactory.getInstance();
    private final @Service TokenValidator validator = TokenValidator.getInstance();

    private void testExpandPlainMacro() {
        Macros macros = new Macros();
        Macro macro = new Macro(f.name("MACRO"), new List<>(), new List<>(f.name("Aki")));
        macros.define(macro);
        MacroCall call = new MacroCall(macro, f.name("MACRO"), new List<>());
        List<Token> actualTokens = services.expand(call, macros);
        List<Token> expectedTokens = new List<>(f.name("Aki"));
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testExpandSystemMacro() {
        Macros macros = new Macros();
        File file = new File(Path.of("chan"), "");
        FileMacro fileMacro = new FileMacro(file);
        macros.define(fileMacro);
        MacroCall call = new MacroCall(fileMacro, f.name("__FILE__"), null);
        List<Token> actualTokens = services.expand(call, macros);
        List<Token> expectedTokens = new List<>(f.doubleQuote(file.getPath().toAbsolutePath().toString()));
        validator.assertEquals(expectedTokens, actualTokens);
    }

    private void testValidation() {
        Assert.assertThatCode(() -> {
            Macros macros = new Macros();
            Macro macro = new Macro(f.name("FAIL"), null, null);
            macros.define(macro);
            MacroCall call = new MacroCall(macro, f.name("FAIL"), new List<>());
            services.expand(call, macros);
        }).throwsException(CodeException.class);
    }
}
