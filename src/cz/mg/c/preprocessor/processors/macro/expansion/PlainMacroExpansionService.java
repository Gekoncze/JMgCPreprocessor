package cz.mg.c.preprocessor.processors.macro.expansion;

import cz.mg.annotations.classes.Service;
import cz.mg.annotations.requirement.Mandatory;
import cz.mg.c.preprocessor.processors.macro.entities.MacroCall;
import cz.mg.c.preprocessor.processors.macro.entities.Macros;
import cz.mg.collections.components.Capacity;
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
    public @Mandatory List<Token> expand(@Mandatory Macros macros, @Mandatory MacroCall call) {
        Map<String, List<Token>> map = createMap(call);
        List<Token> expandedTokens = new List<>();
        for (Token macroToken : call.getMacro().getTokens()) {
            List<Token> replacement = map.getOptional(macroToken.getText());
            if (replacement != null) {
                expandedTokens.addCollectionLast(replacement);
            } else {
                expandedTokens.addLast(macroToken);
            }
        }
        return expandedTokens;
    }

    private @Mandatory Map<String, List<Token>> createMap(@Mandatory MacroCall call) {
        Map<String, List<Token>> map = new Map<>(new Capacity(100));
        Iterator<Token> parameterIterator = Objects.requireNonNull(call.getMacro().getParameters()).iterator();
        Iterator<List<Token>> argumentIterator = Objects.requireNonNull(call.getArguments()).iterator();
        while (parameterIterator.hasNext() && argumentIterator.hasNext()) {
            Token parameter = parameterIterator.next();
            List<Token> arguments = argumentIterator.next();
            map.set(parameter.getText(), arguments);
        }
        return map;
    }
}
