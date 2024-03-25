package cz.mg.c.preprocessor.test;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.entities.macro.Macro;
import cz.mg.c.entities.macro.Macros;
import cz.mg.collections.array.Array;
import cz.mg.tokenizer.test.TokenFactory;

public @Service class MacroFactory {
    private static volatile @Service MacroFactory instance;

    public static @Service MacroFactory getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new MacroFactory();
                    instance.tokenFactory = TokenFactory.getInstance();
                }
            }
        }
        return instance;
    }

    private @Service TokenFactory tokenFactory;

    public @Mandatory Macros create(@Mandatory Macro... macros) {
        Macros macrosInstance = new Macros();
        macrosInstance.getDefinitions().addCollectionLast(new Array<>(macros));
        return macrosInstance;
    }

    public @Mandatory Macro create(@Mandatory String name) {
        Macro macro = new Macro();
        macro.setName(tokenFactory.word(name));
        return macro;
    }

    public @Mandatory Macro createConstant(@Mandatory String name, int value) {
        Macro macro = new Macro();
        macro.setName(tokenFactory.word(name));
        macro.getTokens().addLast(tokenFactory.number(String.valueOf(value)));
        return macro;
    }
}
