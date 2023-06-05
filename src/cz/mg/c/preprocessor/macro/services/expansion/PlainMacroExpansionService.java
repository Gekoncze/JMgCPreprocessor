package cz.mg.c.preprocessor.macro.services.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.macro.components.MacroExpansion;
import cz.mg.c.preprocessor.macro.entities.Macros;
import cz.mg.tokenizer.entities.Token;
import cz.mg.collections.list.List;
import cz.mg.collections.map.Map;

import java.util.Iterator;
import java.util.Objects;

public @Service class PlainMacroExpansionService implements MacroExpansionService {
    private static volatile @Service PlainMacroExpansionService instance;

    public static @Service PlainMacroExpansionService getInstance() {
        if (instance == null) {
            synchronized (Service.class) {
                if (instance == null) {
                    instance = new PlainMacroExpansionService();
                }
            }
        }
        return instance;
    }

    private PlainMacroExpansionService() {
    }

    @Override
    public @Mandatory List<Token> expand(@Mandatory Macros macros, @Mandatory MacroExpansion expansion) {
        Map<String, List<Token>> map = createMap(expansion);
        List<Token> expandedTokens = new List<>();
        for (Token macroToken : expansion.getMacro().getTokens()) {
            List<Token> replacement = map.getOptional(macroToken.getText());
            if (replacement != null) {
                expandedTokens.addCollectionLast(replacement);
            } else {
                expandedTokens.addLast(macroToken);
            }
        }
        return expandedTokens;
    }

    private @Mandatory Map<String, List<Token>> createMap(@Mandatory MacroExpansion expansion) {
        Map<String, List<Token>> map = new Map<>(100);
        Iterator<Token> parameterIterator = Objects.requireNonNull(expansion.getMacro().getParameters()).iterator();
        Iterator<List<Token>> argumentIterator = Objects.requireNonNull(expansion.getArguments()).iterator();
        while (parameterIterator.hasNext() && argumentIterator.hasNext()) {
            Token parameter = parameterIterator.next();
            List<Token> arguments = argumentIterator.next();
            map.set(parameter.getText(), arguments);
        }
        return map;
    }
}
